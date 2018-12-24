package revoluttransfer.models

data class OperationResult<T>(
        val isSuccess: Boolean,
        val data: T? = null,
        val reason: String? = null
)