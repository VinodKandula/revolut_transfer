package revoluttransfer.repositories.account

import revoluttransfer.models.db.Account
import revoluttransfer.models.db.incrementVersion
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class LocalMapAccountRepositoryImpl(private val dataSet: ConcurrentHashMap<Long, Account>) : AccountRepository {

    private val lock = ReentrantLock()

    override fun findByNumber(number: Long): Account? = dataSet[number]

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account): TransactionCodeResult {
        lock.withLock {
            val cacheDebitAccount = dataSet[debitAccount.number]
            val cacheCreditAccount = dataSet[creditAccount.number]
            return if (cacheDebitAccount?.version == debitAccount.version && cacheCreditAccount?.version == creditAccount.version) {
                dataSet[debitAccount.number] = debitAccount.incrementVersion()
                dataSet[creditAccount.number] = creditAccount.incrementVersion()
                println("Success transaction")
                TransactionCodeResult.SUCCESS
            } else {
                println("Catch optimistic lock")
                TransactionCodeResult.UPDATE_CONFLICT
            }
        }
    }
}