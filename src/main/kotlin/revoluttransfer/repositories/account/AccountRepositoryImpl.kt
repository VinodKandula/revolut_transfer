package revoluttransfer.repositories.account

import com.google.inject.Inject
import revoluttransfer.models.db.Account
import java.util.concurrent.locks.ReentrantReadWriteLock
import javax.persistence.EntityManager
import kotlin.concurrent.write

class AccountRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : AccountRepository {

    private var lock = ReentrantReadWriteLock()

    override fun findByNumber(number: Long): Account? {
        println("Thread name ${Thread.currentThread().id}")
        lock.write {
            return entityManager
                    .createQuery("select a from Account a where a.number = :number", Account::class.java)
                    .setParameter("number", number)
                    .resultStream
                    .findFirst()
                    .map { it.copy() }
                    .orElse(null)
        }
    }

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account) {
        try {
            lock.writeLock().lock()
            entityManager.transaction.begin()
            entityManager.merge(debitAccount)
            entityManager.merge(creditAccount)
            entityManager.transaction.commit()
        } finally {
            lock.writeLock().unlock()
        }
    }
}