package revoluttransfer.interactors

import com.google.inject.Inject
import revoluttransfer.models.OperationResult
import revoluttransfer.models.db.Account
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.holder.HolderRepository
import java.math.BigDecimal

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
                    failReason = "something wrong with input params"
            )
        }
    }

    private fun transferByAccountNumber(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        val creditAccount = accountRepository.findByNumber(creditAccountNumber)
        val debitAccount = accountRepository.findByNumber(debitAccountNumber)
        val result = subtractMoney(debitAccount, creditAccount, moneyToTransfer)
        return result
    }

    private fun transferByEmail(debitAccountNumber: Long, creditHolderEmail: String, moneyToTransfer: BigDecimal): OperationResult<Unit> {
        val creditedHolder = holderRepository.getHolderByEmail(creditHolderEmail)
        return if (creditedHolder.accounts.none { it.number == debitAccountNumber }) {
            val creditAccount = creditedHolder.accounts.first { it.isDefault }
            val debitAccount = accountRepository.findByNumber(debitAccountNumber)
            subtractMoney(debitAccount, creditAccount, moneyToTransfer)
        } else {
            OperationResult(isSuccess = false, failReason = "credited email is the same as debited number holder")
        }
    }

    private fun subtractMoney(debitAccount: Account, creditAccount: Account, moneyToTransfer: BigDecimal): OperationResult<Unit> =
            if (debitAccount.balance > moneyToTransfer) {
                debitAccount.balance = debitAccount.balance.minus(moneyToTransfer)
                creditAccount.balance = creditAccount.balance.plus(moneyToTransfer)
                accountRepository.saveAccountChanges(debitAccount, creditAccount)
                OperationResult(true)
            } else {
                OperationResult(isSuccess = false, failReason = "debited account have not enough money")
            }
}