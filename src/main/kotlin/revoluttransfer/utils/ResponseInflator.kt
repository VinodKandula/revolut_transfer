package revoluttransfer.utils

import com.google.gson.Gson
import com.google.inject.Inject
import revoluttransfer.models.ErrorResponseDto
import revoluttransfer.models.ResponseDto
import revoluttransfer.models.ResultData
import spark.Response

class ResponseInflator @Inject constructor(private val gson: Gson) {

    fun <T> inflateResponseWithResult(response: Response, resultData: ResultData<T>) {
        response.status(200)
        response.body(gson.toJson(
                ResponseDto(
                        code = 200,
                        data = resultData.data?.let { gson.toJson(it) },
                        reason = resultData.reason
                )
        ))
    }



    fun <T> inflateErrorResponseWithResult(response: Response, code: Int, resultData: ResultData<T>) {
        response.status(code)
        response.body(gson.toJson(
                ErrorResponseDto(
                        code = code,
                        reason = resultData.reason ?: ""
                ),
                ErrorResponseDto::class.java
        ))
    }

    fun inflateErrorResponseWithException(response: Response, exception: Exception) {
        exception.printStackTrace()
        response.status(400)
        response.body(gson.toJson(
                ErrorResponseDto(
                        code = 400,
                        reason = exception.localizedMessage
                ),
                ErrorResponseDto::class.java
        ))
    }
}