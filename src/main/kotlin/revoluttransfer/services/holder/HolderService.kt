package revoluttransfer.services.holder

import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.HolderDto

interface HolderService {

    fun getAllHolders(): ResultData<List<HolderDto>>

    fun getHolderByEmail(email: String): ResultData<HolderDto>

}

