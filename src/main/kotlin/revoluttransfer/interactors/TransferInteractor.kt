package revoluttransfer.interactors

import revoluttransfer.models.OperationResult
import revoluttransfer.models.dto.TransferDto

interface TransferInteractor {
    fun commitTransfer(transferDto: TransferDto): OperationResult<Unit>

}