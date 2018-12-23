package revoluttransfer.models.dto

import revoluttransfer.models.db.Holder


data class HolderDto(
        val email: String,
        val name: String,
        val lastName: String
)

fun Holder.mapToDto() = HolderDto(
        email = this.email,
        name = this.name,
        lastName = this.lastName
)