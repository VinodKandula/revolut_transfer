package revoluttransfer.routes

import revoluttransfer.utils.ResponseConstructor
import spark.Spark.exception
import javax.inject.Inject

class CommonExceptionRouter @Inject constructor(private val responseConstructor: ResponseConstructor) : Router {
    override fun register() {
        exception(Exception::class.java) { exception, _, response ->
            responseConstructor.inflateErrorResponseWithException(response, exception)
        }
    }
}