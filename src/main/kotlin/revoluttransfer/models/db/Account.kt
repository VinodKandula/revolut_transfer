package revoluttransfer.models.db

import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Accounts")
data class Account(
        @GeneratedValue(strategy = GenerationType.AUTO) @Id val id: UUID? = null,
        var balance: BigDecimal,
        val isDefault: Boolean,
        val number: Long
)