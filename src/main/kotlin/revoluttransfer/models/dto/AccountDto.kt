package revoluttransfer.models.dto

import revoluttransfer.models.db.Account
import java.math.BigDecimal

data class AccountDto(val balance: BigDecimal)

fun Account.mapTo() = AccountDto(BigDecimal(10000))