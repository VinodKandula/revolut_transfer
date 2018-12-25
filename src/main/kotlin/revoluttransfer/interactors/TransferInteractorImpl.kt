package revoluttransfer.interactors

import com.google.inject.Inject
import revoluttransfer.TRANSACTIONS_RETRY
import revoluttransfer.models.OperationResult
import revoluttransfer.models.db.Account
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.holder.HolderRepository
import java.math.BigDecimal
import javax.persistence.OptimisticLockException
import javax.persistence.RollbackException

class TransferInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val holderRepository: HolderRepository
) : TransferInteractor {

    override fun commitTransfer(transferDto: TransferDto): OperationResult<Unit> {
        val moneyToTransfer = BigDecimal(transferDto.moneyAmount)
        return when {
            transferDto.creditAccountNumber == null && transferDto.creditHolderEmail != null ->
                transferByEmail(transferDto.debitAccountNumber, transferDto.creditHolderEmail, moneyToTransfer)

            transferDto.creditAccountNumber != null && transferDto.creditHolderEmail == null ->
                transferByAccountNumber(transferDto.debitAccountNumber, transferDto.creditAccountNumber.toLong(), moneyToTransfer)
            else -> OperationResult(
                    isSuccess = false,
                    reason = "something wrong with input params"
            )
        }
    }

    private fun transferByAccountNumber(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): OperationResult<Unit> =
            processTransaction(debitAccountNumber, creditAccountNumber, moneyToTransfer)

    private fun transferByEmail(debitAccountNumber: Long, creditHolderEmail: String, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        val creditedHolder = holderRepository.getHolderByEmail(creditHolderEmail).copy()
        return if (creditedHolder.accounts.none { it.number == debitAccountNumber }) {
            val creditAccount = creditedHolder.accounts.first { it.isDefault }
            processTransaction(debitAccountNumber, creditAccount.number, moneyToTransfer)
        } else {
            OperationResult(isSuccess = false, reason = "credited email is the same as debited number holder")
        }
    }

    private fun processTransaction(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        for (i in 1..TRANSACTIONS_RETRY) {
            val debitAccount = accountRepository.findByNumber(debitAccountNumber)
            val creditAccount = accountRepository.findByNumber(creditAccountNumber)
            if (creditAccount != null && debitAccount != null) {
                val result = applyMoneyTransaction(debitAccount, creditAccount, moneyToTransfer)
                when {
                    result.isSuccess -> return OperationResult(isSuccess = true, reason = "transfer was committed successfully current balace: ${debitAccount.balance}")
                    !result.isSuccess && result.data == TransactionCodeResult.NOT_ENOUGH_MONEY -> return OperationResult(
                            isSuccess = false,
                            reason = result.reason
                    )
                }
            }
        }
        return OperationResult(isSuccess = false, reason = "transfer wasn't committed")
    }

    private fun applyMoneyTransaction(debitAccount: Account, creditAccount: Account, moneyToTransfer: BigDecimal): OperationResult<TransactionCodeResult> {
        return if (debitAccount.balance > moneyToTransfer) {
            try {
                debitAccount.balance = debitAccount.balance.minus(moneyToTransfer)
                creditAccount.balance = creditAccount.balance.plus(moneyToTransfer)
                accountRepository.saveAccountChanges(debitAccount, creditAccount)
                OperationResult(true, TransactionCodeResult.SUCCESS)
            } catch (ex: RollbackException) {
                System.out.println("Rollback")
                OperationResult(false, data = TransactionCodeResult.ROLLBACK_CONFLICT, reason = "transaction update conflict")
            } catch (ex: OptimisticLockException) {
                System.out.println("OptimisticLockException")
                OperationResult(false, data = TransactionCodeResult.UPDATE_CONFLICT, reason = "transaction update conflict")
            }
        } else {
            OperationResult(isSuccess = false, data = TransactionCodeResult.NOT_ENOUGH_MONEY, reason = "debited account have not enough money")
        }
    }
}

