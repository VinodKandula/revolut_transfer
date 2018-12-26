package revoluttransfer.interactors.holder

import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.HolderDto

interface HolderInteractor {

    fun getAllHolders(): ResultData<List<HolderDto>>

    fun getHolderByEmail(email: String): ResultData<HolderDto>

}
