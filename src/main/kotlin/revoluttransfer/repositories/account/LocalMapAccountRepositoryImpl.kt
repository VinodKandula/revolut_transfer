package revoluttransfer.repositories.account

import revoluttransfer.models.db.Account
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class LocalMapAccountRepositoryImpl : AccountRepository {

    private val lock = ReentrantLock()

    private val map = ConcurrentHashMap<Long, Account>().apply {
        val account1 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101010)
        val account2 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101020)
        val account3 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101030)
        val account4 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101040)
        val account5 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101050)
        val account6 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101060)
        val account7 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101070)
        val account8 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101080)

        put(account1.number, account1)
        put(account2.number, account2)
        put(account3.number, account3)
        put(account4.number, account4)
        put(account5.number, account5)
        put(account6.number, account6)
        put(account7.number, account7)
        put(account8.number, account8)
    }

    override fun findByNumber(number: Long): Account? = map[number]

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account): TransactionCodeResult {
        lock.withLock {
            val cacheDebitAccount = map[debitAccount.number]
            val cacheCreditAccount = map[creditAccount.number]
            return if (cacheDebitAccount?.version == debitAccount.version && cacheCreditAccount?.version == creditAccount.version) {
                map[debitAccount.number] = debitAccount.copy(version = debitAccount.version + 1)
                map[creditAccount.number] = creditAccount.copy(version = creditAccount.version + 1)
                println("Success transaction")
                TransactionCodeResult.SUCCESS
            } else {
                println("Catch optimistic lock")
                TransactionCodeResult.UPDATE_CONFLICT
            }
        }
    }
}