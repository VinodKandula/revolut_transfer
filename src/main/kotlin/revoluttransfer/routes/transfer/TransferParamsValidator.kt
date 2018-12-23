package revoluttransfer.routes.transfer

import revoluttransfer.models.OperationResult
import revoluttransfer.models.dto.TransferDto

interface TransferParamsValidator {
    fun validateAndGet(body: String): OperationResult<TransferDto>
}