package revoluttransfer.models

data class ResponseDto(
        val code: Int,
        val data: String?,
        val reason: String?
)