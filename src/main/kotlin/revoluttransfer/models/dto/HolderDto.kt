package revoluttransfer.models.dto

import revoluttransfer.models.db.Account

data class HolderDto(
        val email: String,
        val name: String,
        val lastName: String,
        val accounts: List<AccountDto> = emptyList()
)