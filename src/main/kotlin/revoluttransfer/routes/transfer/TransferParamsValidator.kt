package revoluttransfer.routes.transfer

import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.TransferDto

interface TransferParamsValidator {
    fun validateAndGet(body: String): ResultData<TransferDto>
}