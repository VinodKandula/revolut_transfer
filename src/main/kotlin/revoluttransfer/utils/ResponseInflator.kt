package revoluttransfer.utils

import com.google.gson.Gson
import com.google.inject.Inject
import revoluttransfer.models.OperationResult
import revoluttransfer.models.ResponseDto
import spark.Response

class ResponseInflator @Inject constructor(private val gson: Gson) {

    fun <T> inflateResponseWithResult(response: Response, code: Int, result: OperationResult<T>) {
        response.status(code)
        response.body(gson.toJson(
                ResponseDto(
                        code = code,
                        resason = result.reason ?: ""
                ),
                ResponseDto::class.java
        ))
    }

    fun inflateResponseWithException(response: Response, exception: Exception) {
        exception.printStackTrace()
        response.status(400)
        response.body(gson.toJson(
                ResponseDto(
                        code = 400,
                        resason = exception.localizedMessage
                ),
                ResponseDto::class.java
        ))
    }
}