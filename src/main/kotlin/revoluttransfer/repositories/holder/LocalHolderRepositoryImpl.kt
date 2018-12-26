package revoluttransfer.repositories.holder

import revoluttransfer.models.db.Holder
import java.util.concurrent.ConcurrentHashMap

class LocalHolderRepositoryImpl(private val dataSet: ConcurrentHashMap.KeySetView<Holder, Boolean>) : HolderRepository {

    override fun getHolderByEmail(email: String): Holder? = dataSet.first { it.email == email }

    override fun getAll() = dataSet.toList()

}