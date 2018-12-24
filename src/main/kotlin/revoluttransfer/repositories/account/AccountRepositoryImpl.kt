package revoluttransfer.repositories.account

import com.google.inject.Inject
import revoluttransfer.models.db.Account
import java.util.concurrent.locks.ReentrantLock
import javax.persistence.EntityManager
import kotlin.concurrent.withLock

class AccountRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : AccountRepository {

    private var lock = ReentrantLock()

    override fun findByNumber(number: Long): Account? {
        lock.withLock {
            return entityManager
                    .createQuery("select a from Account a where a.number = :number", Account::class.java)
                    .setParameter("number", number)
                    .resultStream
                    .findFirst()
                    .orElse(null)
        }
    }

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account) {
        try {
            lock.lock()
            entityManager.transaction.begin()
            entityManager.merge(debitAccount)
            entityManager.merge(creditAccount)
            entityManager.transaction.commit()
        } catch (ex: Exception) {
            entityManager.transaction.rollback()
            lock.unlock()
            throw ex
        } finally {
            if (lock.isLocked) {
                lock.unlock()
            }
        }
    }
}