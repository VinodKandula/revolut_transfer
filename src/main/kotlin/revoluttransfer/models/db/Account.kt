package revoluttransfer.models.db

import revoluttransfer.models.dto.AccountDto
import java.math.BigDecimal
import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Version

data class Account(
        @GeneratedValue(strategy = GenerationType.AUTO) @Id val id: UUID? = null,
        val balance: BigDecimal,
        val isDefault: Boolean,
        val number: Long,
        @Version val version: Int = 0
)

fun Account.minus(money: BigDecimal) = this.copy(
        id = this.id,
        isDefault = this.isDefault,
        number = this.number,
        version = this.version,
        balance = this.balance.minus(money)
)

fun Account.plus(money: BigDecimal) = this.copy(
        id = this.id,
        isDefault = this.isDefault,
        number = this.number,
        version = this.version,
        balance = this.balance.plus(money)
)

fun Account.incrementVersion() = this.copy(
        id = this.id,
        isDefault = this.isDefault,
        number = this.number,
        version = this.version + 1,
        balance = this.balance
)

fun Account.toDto() = AccountDto(balance, isDefault, number)