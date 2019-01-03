package revoluttransfer.services.transfer

import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.TransferDto

interface TransferService {
    fun commitTransfer(transferDto: TransferDto): ResultData<Unit>

}