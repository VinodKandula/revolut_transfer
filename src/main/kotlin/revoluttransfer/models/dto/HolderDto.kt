package revoluttransfer.models.dto

data class HolderDto(
        val email: String,
        val name: String,
        val lastName: String,
        val accounts: List<AccountDto> = emptyList()
)