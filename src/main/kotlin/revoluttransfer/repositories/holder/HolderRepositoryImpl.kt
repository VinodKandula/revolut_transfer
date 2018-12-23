package revoluttransfer.repositories.holder

import revoluttransfer.models.db.Holder
import javax.persistence.EntityManager

class HolderRepositoryImpl(private val entityManager: EntityManager) : HolderRepository {

    override fun getHolderByEmail(email: String): Holder {
        return entityManager
                .createQuery("select h from Holder h where h.email = :email", Holder::class.java)
                .setParameter("email", email)
                .singleResult
    }
}