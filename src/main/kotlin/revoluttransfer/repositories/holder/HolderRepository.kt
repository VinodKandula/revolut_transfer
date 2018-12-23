package revoluttransfer.repositories.holder

import revoluttransfer.models.db.Holder


interface HolderRepository {

    fun getHolderByEmail(email: String): Holder
}