package revoluttransfer.repositories.holder

import com.google.inject.Inject
import revoluttransfer.models.db.Holder
import javax.persistence.EntityManager

class HolderRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : HolderRepository {

    override fun getHolderByEmail(email: String): Holder? {
        return entityManager
                .createQuery("select h from Holder h where h.email = :email", Holder::class.java)
                .setParameter("email", email)
                .resultStream
                .map { it.copy() }
                .findFirst()
                .orElse(null)

    }

    override fun getAll(): List<Holder>? {
        return entityManager.createQuery("from Holder", Holder::class.java).resultList
    }
}