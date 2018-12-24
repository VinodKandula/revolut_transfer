package revoluttransfer.models.db

import javax.persistence.*

@Entity
@Table(name = "Holders")
data class Holder(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
        val email: String,
        val name: String,
        val lastName: String,
        @OneToMany val accounts: List<Account> = emptyList(),
        @Version var version: Int? = null
)
