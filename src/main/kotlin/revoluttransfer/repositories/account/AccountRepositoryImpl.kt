package revoluttransfer.repositories.account

import com.google.inject.Inject
import revoluttransfer.models.db.Account
import javax.persistence.EntityManager

class AccountRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : AccountRepository {

    override fun findByNumber(number: Long): Account {
        return entityManager
                .createQuery("select a from Account a where a.number = :number", Account::class.java)
                .setParameter("number", number)
                .singleResult
    }

    override fun saveAccountChanges(debitAccount: Account, creditAccount: Account) {
        entityManager.transaction.begin()
        entityManager.persist(debitAccount)
        entityManager.persist(creditAccount)
        entityManager.transaction.commit()
    }
}