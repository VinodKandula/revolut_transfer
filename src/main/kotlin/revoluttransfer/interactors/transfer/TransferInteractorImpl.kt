package revoluttransfer.interactors.transfer

import com.google.inject.Inject
import revoluttransfer.TRANSACTIONS_RETRY
import revoluttransfer.models.ResultData
import revoluttransfer.models.db.Account
import revoluttransfer.models.db.minus
import revoluttransfer.models.db.plus
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.TransactionCodeResult
import revoluttransfer.repositories.holder.HolderRepository
import java.math.BigDecimal

class TransferInteractorImpl @Inject constructor(
        private val accountRepository: AccountRepository,
        private val holderRepository: HolderRepository
) : TransferInteractor {

    override fun commitTransfer(transferDto: TransferDto): ResultData<Unit> {
        val moneyToTransfer = BigDecimal(transferDto.moneyAmount)
        return when {
            transferDto.creditAccountNumber == null && transferDto.creditHolderEmail != null ->
                transferByEmail(transferDto.debitAccountNumber, transferDto.creditHolderEmail, moneyToTransfer)

            transferDto.creditAccountNumber != null && transferDto.creditHolderEmail == null ->
                transferByAccountNumber(transferDto.debitAccountNumber, transferDto.creditAccountNumber.toLong(), moneyToTransfer)
            else -> ResultData(
                    isSuccess = false,
                    reason = "something wrong with input params"
            )
        }
    }

    private fun transferByAccountNumber(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): ResultData<Unit> =
            processTransaction(debitAccountNumber, creditAccountNumber, moneyToTransfer)

    private fun transferByEmail(debitAccountNumber: Long, creditHolderEmail: String, moneyToTransfer: BigDecimal): ResultData<Unit> {
        val creditedHolder = holderRepository.getHolderByEmail(creditHolderEmail)
        return creditedHolder
                ?.let {
                    if (creditedHolder.accounts.none { it.number == debitAccountNumber }) {
                        val creditAccount = creditedHolder.accounts.first { it.isDefault }
                        processTransaction(debitAccountNumber, creditAccount.number, moneyToTransfer)
                    } else {
                        ResultData(isSuccess = false, reason = "credited email is the same as debited number holder")
                    }
                }
                ?: ResultData(isSuccess = false, reason = "no holder was found for given email")
    }


    private fun processTransaction(debitAccountNumber: Long, creditAccountNumber: Long, moneyToTransfer: BigDecimal): ResultData<Unit> {
        for (i in 1..TRANSACTIONS_RETRY) {
            val debitAccount = accountRepository.findByNumber(debitAccountNumber)
            val creditAccount = accountRepository.findByNumber(creditAccountNumber)
            if (creditAccount != null && debitAccount != null) {
                val result = applyMoneyTransaction(debitAccount, creditAccount, moneyToTransfer)
                when (result) {
                    TransactionCodeResult.SUCCESS -> return ResultData(isSuccess = true, reason = "transfer was committed successfully current balance: ${debitAccount.balance}")
                    TransactionCodeResult.NOT_ENOUGH_MONEY -> return ResultData(
                            isSuccess = false,
                            reason = "Not enough money"
                    )
                    else -> {
                    }
                }
            }
        }
        return ResultData(isSuccess = false, reason = "transfer was not committed")
    }

    private fun applyMoneyTransaction(debitAccount: Account, creditAccount: Account, moneyToTransfer: BigDecimal): TransactionCodeResult {
        return if (debitAccount.balance > moneyToTransfer) {
            accountRepository.saveAccountChanges(debitAccount.minus(moneyToTransfer), creditAccount.plus(moneyToTransfer))
        } else {
            TransactionCodeResult.NOT_ENOUGH_MONEY
        }
    }
}

