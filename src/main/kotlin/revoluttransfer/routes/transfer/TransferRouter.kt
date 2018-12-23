package revoluttransfer.routes.transfer

import com.google.gson.Gson
import revoluttransfer.interactors.TransferInteractor
import revoluttransfer.models.ResponseDto
import revoluttransfer.TRANSFER_PATH
import spark.Spark.*

class TransferRouter(
        private val transferParamsValidator: TransferParamsValidator,
        private val transferInteractor: TransferInteractor
) {

    fun register() {

        post(TRANSFER_PATH) { request, response ->
            System.out.println("TransferRouter with body" + request.body())
            val paramsValidationResult = transferParamsValidator.validateAndGet(request.body())
            System.out.println("TransferRouter validagted")
            if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                System.out.println("TransferRouter start interactor")
                val transferResult = transferInteractor.commitTransfer(paramsValidationResult.data)
                if (transferResult.isSuccess) {
                    response.status(200)
                    return@post Gson().toJson(
                            ResponseDto(
                                    code = 200,
                                    resason = "transfer was committed successfully"
                            ),
                            ResponseDto::class.java
                    )
                } else {
                    response.status(400)
                    return@post Gson().toJson(
                            ResponseDto(
                                    code = 400,
                                    resason = transferResult.failReason ?: "transfer wasn\'t committed successfully"
                            ),
                            ResponseDto::class.java
                    )
                }

            } else {
                response.status(400)
                return@post Gson().toJson(
                        ResponseDto(
                                code = 418,
                                resason = paramsValidationResult.failReason ?: "something when wrong"
                        ),
                        ResponseDto::class.java
                )
            }
        }
        after(TRANSFER_PATH) { request, response -> response.header("Content-Type", "application/json") }
        exception(Exception::class.java) { exception, _, response ->
            exception.printStackTrace()
            response.body(Gson().toJson(
                    ResponseDto(
                            code = 400,
                            resason = exception.localizedMessage
                    ),
                    ResponseDto::class.java
            ))
        }
    }

}

