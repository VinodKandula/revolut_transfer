package revoluttransfer.repositories.holder

import com.google.inject.Inject
import revoluttransfer.models.db.Holder
import javax.persistence.EntityManager

class HolderRepositoryImpl @Inject constructor(private val entityManager: EntityManager) : HolderRepository {

    override fun getHolderByEmail(email: String): Holder {
        return entityManager
                .createQuery("select h from Holder h where h.email = :email", Holder::class.java)
                .setParameter("email", email)
                .singleResult
                .apply {
                    entityManager.detach(this)
                }
    }
}