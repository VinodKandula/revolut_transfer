package revoluttransfer.interactors.holder

import revoluttransfer.models.ResultData
import revoluttransfer.models.db.toDto
import revoluttransfer.models.dto.HolderDto
import revoluttransfer.repositories.holder.HolderRepository
import javax.inject.Inject

class HolderInteractorImpl @Inject constructor(private val holderRepository: HolderRepository) : HolderInteractor {

    override fun getAllHolders(): ResultData<List<HolderDto>> =
            ResultData(
                    isSuccess = true,
                    data = holderRepository.getAll()?.map { it.toDto() } ?: emptyList()
            )


    override fun getHolderByEmail(email: String): ResultData<HolderDto> {
        val holder = holderRepository.getHolderByEmail(email)
        return holder
                ?.let { ResultData(isSuccess = true, data = it.toDto()) }
                ?: ResultData(isSuccess = false, reason = "no account for given email was founf")
    }
}