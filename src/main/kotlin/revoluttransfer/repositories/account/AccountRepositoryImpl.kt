package revoluttransfer.repositories.account

import com.google.inject.Inject
import revoluttransfer.models.db.Account
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.ReentrantLock
import javax.persistence.EntityManager
import javax.persistence.OptimisticLockException
import kotlin.concurrent.withLock

class AccountRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : AccountRepository {

    private var lock = ReentrantLock()

    override fun findByNumber(number: Long): Account? {
        println("Thread name ${Thread.currentThread().id}")
        lock.withLock {
            val result = entityManager
                    .createQuery("select a from Account a where a.number = :number", Account::class.java)
                    .setParameter("number", number)
                    .resultStream
                    .findFirst()
                    .map { it.copy() }
                    .orElse(null)
            return result
        }
    }

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account): TransactionCodeResult {
        lock.lock()
        try {
            entityManager.transaction.begin()
            entityManager.merge(debitAccount)
            entityManager.merge(creditAccount)
            entityManager.transaction.commit()
        } catch (ex: OptimisticLockException) {
            System.out.println("Catch optimistic lock")
            entityManager.transaction.rollback()
            return TransactionCodeResult.UPDATE_CONFLICT
        } catch (ex: Exception) {
            ex.printStackTrace()
            entityManager.transaction.rollback()
            return TransactionCodeResult.UNKNOWN
        } finally {
            System.out.println("release")
            lock.unlock()
        }
        return TransactionCodeResult.SUCCESS
    }
}