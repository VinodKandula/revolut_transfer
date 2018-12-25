package revoluttransfer.routes

import revoluttransfer.utils.ResponseInflator
import spark.Spark
import spark.Spark.exception
import javax.inject.Inject

class CommonExceptionRouter @Inject constructor(private val responseInflator: ResponseInflator) : Router {
    override fun register() {
        exception(Exception::class.java) { exception, _, response ->
            responseInflator.inflateErrorResponseWithException(response, exception)
        }
    }
}