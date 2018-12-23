package revoluttransfer.models

data class OperationResult<T>(
        val isSuccess: Boolean,
        val data: T? = null,
        val failReason: String? = null
)