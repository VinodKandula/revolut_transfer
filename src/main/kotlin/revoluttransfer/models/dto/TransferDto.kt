package revoluttransfer.models.dto

data class TransferDto(
        val moneyAmount: String,
        val debitAccountNumber: Long,
        val creditAccountNumber: Long
)