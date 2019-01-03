package revoluttransfer.models.db

import java.math.BigDecimal

data class Account(
        val balance: BigDecimal,
        val isDefault: Boolean,
        val number: Long,
        val version: Int = 0
)

fun Account.minus(money: BigDecimal) = this.copy(
        isDefault = this.isDefault,
        number = this.number,
        version = this.version,
        balance = this.balance.minus(money)
)

fun Account.plus(money: BigDecimal) = this.copy(
        isDefault = this.isDefault,
        number = this.number,
        version = this.version,
        balance = this.balance.plus(money)
)

fun Account.incrementVersion() = this.copy(
        isDefault = this.isDefault,
        number = this.number,
        version = this.version + 1,
        balance = this.balance
)