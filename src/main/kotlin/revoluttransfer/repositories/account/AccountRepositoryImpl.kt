package revoluttransfer.repositories.account

import revoluttransfer.models.db.Account
import javax.persistence.EntityManager

class AccountRepositoryImpl(private val entityManager: EntityManager) : AccountRepository {

    override fun findByNumber(number: Long): Account {
        return entityManager
                .createQuery("select a from Account a where a.number = :number", Account::class.java)
                .setParameter("number", number)
                .singleResult
    }
}