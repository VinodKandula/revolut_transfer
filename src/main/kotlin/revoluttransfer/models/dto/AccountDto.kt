package revoluttransfer.models.dto

import java.math.BigDecimal

data class AccountDto(
        var balance: BigDecimal,
        val isDefault: Boolean,
        val number: Long
)