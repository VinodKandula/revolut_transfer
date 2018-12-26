package revoluttransfer.models.db

import revoluttransfer.models.dto.HolderDto
import javax.persistence.*

@Entity
@Table(
        name = "Holders",
        indexes = [Index(name = "email", columnList = "email", unique = true)]
)
data class Holder(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
        val email: String,
        val name: String,
        val lastName: String,
        @OneToMany val accounts: List<Account> = emptyList(),
        @Version val version: Int = 0
)

fun Holder.toDto() = HolderDto(email, name, lastName, accounts.map { it.toDto() })