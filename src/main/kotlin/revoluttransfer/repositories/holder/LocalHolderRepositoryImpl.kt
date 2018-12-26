package revoluttransfer.repositories.holder

import revoluttransfer.createTestAccountList
import revoluttransfer.createTestHolders
import revoluttransfer.models.db.Holder
import java.util.concurrent.ConcurrentHashMap

class LocalHolderRepositoryImpl : HolderRepository {

    private val map = ConcurrentHashMap.newKeySet<Holder>().apply {
        createTestHolders(createTestAccountList()).forEach {
            add(it)
        }
    }

    override fun getHolderByEmail(email: String): Holder? = map.first { it.email == email }

    override fun getAll() = map.toList()

}