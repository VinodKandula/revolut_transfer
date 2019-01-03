package revoluttransfer.services.transfer

import com.google.inject.Inject
import revoluttransfer.TRANSACTIONS_RETRY
import revoluttransfer.models.ResultData
import revoluttransfer.models.db.minus
import revoluttransfer.models.db.plus
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.TransactionCodeResult
import java.math.BigDecimal

class TransferServiceImpl @Inject constructor(private val accountRepository: AccountRepository) : TransferService {

    override fun commitTransfer(transferDto: TransferDto): ResultData<Unit> {
        return processTransactionData(transferDto.debitAccountNumber, transferDto.creditAccountNumber, transferDto.moneyAmount.toBigDecimal())
    }

    private fun processTransactionData(debitedAccountNumber: Long, creditedAccountNumber: Long, moneyToTransfer: BigDecimal): ResultData<Unit> {
        for (i in 1..TRANSACTIONS_RETRY) {
            val debitAccount = accountRepository.findByNumber(debitedAccountNumber)
            val creditAccount = accountRepository.findByNumber(creditedAccountNumber)
            if (creditAccount != null && debitAccount != null) {
                if (debitAccount.balance >= moneyToTransfer) {
                    val transactionResult = accountRepository.saveAccountChanges(
                            debitAccount = debitAccount.minus(moneyToTransfer),
                            creditAccount = creditAccount.plus(moneyToTransfer)
                    )
                    if (transactionResult == TransactionCodeResult.SUCCESS) {
                        return ResultData(
                                isSuccess = true,
                                reason = "transfer was committed successfully current balance: ${debitAccount.balance}"
                        )
                    }
                } else {
                    return ResultData(
                            isSuccess = false,
                            reason = "Not enough money"
                    )
                }
            }
        }
        println("transfer was not committed, try again later")
        return ResultData(isSuccess = false, reason = "transfer was not committed, try again later")
    }

}

