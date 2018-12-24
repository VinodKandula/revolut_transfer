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

    private fun transferByAccountNumber(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        val creditAccount = accountRepository.findByNumber(creditAccountNumber)
        val debitAccount = accountRepository.findByNumber(debitAccountNumber)
        return if (creditAccount == null || debitAccount == null) {
            OperationResult(isSuccess = false, reason = "one of accounts wasn't found")
        } else {
            processTransaction(debitAccount, creditAccount, moneyToTransfer)
        }
    }

    private fun transferByEmail(debitAccountNumber: Long, creditHolderEmail: String, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        val creditedHolder = holderRepository.getHolderByEmail(creditHolderEmail)
        return if (creditedHolder.accounts.none { it.number == debitAccountNumber }) {
            val creditAccount = creditedHolder.accounts.first { it.isDefault }
            val debitAccount = accountRepository.findByNumber(debitAccountNumber)
            if (debitAccount != null) {
                processTransaction(debitAccount, creditAccount, moneyToTransfer)
            } else {
                OperationResult(isSuccess = false, reason = "debitAccount is null")
            }
        } else {
            OperationResult(isSuccess = false, reason = "credited email is the same as debited number holder")
        }
    }

    private fun processTransaction(debitAccount: Account, creditAccount: Account, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        for (i in 1..TRANSACTIONS_RETRY) {
            val updatedDebitAccount = accountRepository.findByNumber(debitAccount.number)
            val updatedCreditAccount = accountRepository.findByNumber(creditAccount.number)
            if (updatedCreditAccount != null && updatedDebitAccount != null) {
                val result = applyMoneyTransaction(updatedDebitAccount, updatedCreditAccount, moneyToTransfer)
                when {
                    result.isSuccess -> return OperationResult(isSuccess = true, reason = "transfer was committed successfully current balace: ${updatedDebitAccount.balance}")
                    !result.isSuccess && result.data == TransactionCodeResult.NOT_ENOUGH_MONEY -> return OperationResult(
                            isSuccess = false,
                            reason = result.reason
                    )
                }
            }
        }
        return OperationResult(isSuccess = false, reason = "transfer wasn't commited")
    }

    private fun applyMoneyTransaction(debitAccount: Account, creditAccount: Account, moneyToTransfer: BigDecimal): OperationResult<TransactionCodeResult> {
        return if (debitAccount.balance > moneyToTransfer) {
            try {
                debitAccount.balance = debitAccount.balance.minus(moneyToTransfer)
                creditAccount.balance = creditAccount.balance.plus(moneyToTransfer)
                accountRepository.saveAccountChanges(debitAccount, creditAccount)
                OperationResult(true, TransactionCodeResult.SUCCESS)
            } catch (ex: RollbackException) {
                OperationResult(false, data = TransactionCodeResult.ROLLBACK_CONFLICT, reason = "transaction update conflict")
            } catch (ex: OptimisticLockException) {
                OperationResult(false, data = TransactionCodeResult.UPDATE_CONFLICT, reason = "transaction update conflict")
            }
        } else {
            OperationResult(isSuccess = false, data = TransactionCodeResult.NOT_ENOUGH_MONEY, reason = "debited account have not enough money")
        }
    }
}

