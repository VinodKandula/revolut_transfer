package revoluttransfer.interactors.transfer

import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.TransferDto

interface TransferInteractor {
    fun commitTransfer(transferDto: TransferDto): ResultData<Unit>

}