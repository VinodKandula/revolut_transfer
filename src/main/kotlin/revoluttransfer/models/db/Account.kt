package revoluttransfer.models.db

import revoluttransfer.models.dto.AccountDto
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(
        name = "Accounts",
        indexes = [Index(name = "number", columnList = "number", unique = true)]
)
data class Account(
        @GeneratedValue(strategy = GenerationType.AUTO) @Id val id: UUID? = null,
        var balance: BigDecimal,
        val isDefault: Boolean,
        val number: Long,
        @Version var version: Int? = null
)

fun Account.toDto() = AccountDto(balance, isDefault, number)