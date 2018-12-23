package revoluttransfer.interactors

import revoluttransfer.models.db.Holder
import revoluttransfer.repositories.holder.HolderRepository

interface HolderInteractor {

    fun createHolder(nam: String, lastName: String, email: String)

}

class HolderInteractorImpl(private val holderRepository: HolderRepository) : HolderInteractor {

    override fun createHolder(name: String, lastName: String, email: String) {
//        holderRepository.create(Holder(email = email, lastName = lastName, name = name))
    }


}