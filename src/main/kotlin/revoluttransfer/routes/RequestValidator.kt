package revoluttransfer.routes

import revoluttransfer.models.ResultData

interface RequestValidator<T> {

    fun validateAndGet(parameters: String): ResultData<T>
}