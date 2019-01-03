package revoluttransfer.repositories.account

import revoluttransfer.models.db.Account
import revoluttransfer.models.db.incrementVersion
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

class AccountRepositoryImpl(private val dataSet: ConcurrentHashMap<Long, Account>) : AccountRepository {

    private val isTransactionInProgress = AtomicBoolean(false)

    override fun findByNumber(number: Long): Account? = dataSet[number]

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account): TransactionCodeResult {
        return if (isTransactionInProgress.compareAndSet(false, true)) {
            val cacheDebitAccount = dataSet[debitAccount.number]
            val cacheCreditAccount = dataSet[creditAccount.number]
            return if (cacheDebitAccount?.version == debitAccount.version && cacheCreditAccount?.version == creditAccount.version) {
                dataSet[debitAccount.number] = debitAccount.incrementVersion()
                dataSet[creditAccount.number] = creditAccount.incrementVersion()
                dropProgressOrThrow()
                println("success transaction ${TransactionCodeResult.SUCCESS}")
                TransactionCodeResult.SUCCESS
            } else {
                dropProgressOrThrow()
                println("failed transaction ${TransactionCodeResult.UPDATE_CONFLICT}")
                TransactionCodeResult.UPDATE_CONFLICT
            }
        } else {
            println("failed transaction ${TransactionCodeResult.ONGOING_TRANSACTION_IN_PROCESS}")
            TransactionCodeResult.ONGOING_TRANSACTION_IN_PROCESS
        }
    }

    private fun dropProgressOrThrow() {
        if (!isTransactionInProgress.compareAndSet(true, false)) {
            throw IllegalStateException("Storage is not consistent")
        }

    }
}