package revoluttransfer.models

data class ResultData<T>(
        val isSuccess: Boolean,
        val data: T? = null,
        val reason: String? = null
)