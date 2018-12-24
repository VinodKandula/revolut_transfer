package revoluttransfer.routes.transfer

import com.google.inject.Inject
import revoluttransfer.TRANSFER_PATH
import revoluttransfer.interactors.TransferInteractor
import revoluttransfer.utils.ResponseInflator
import spark.Spark.*

class TransferRouter @Inject constructor(
        private val responseInflator: ResponseInflator,
        private val transferParamsValidator: TransferParamsValidator,
        private val transferInteractor: TransferInteractor
) {

    fun register() {

        post(TRANSFER_PATH) { request, response ->
            val paramsValidationResult = transferParamsValidator.validateAndGet(request.body())
            if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                val transferResult = transferInteractor.commitTransfer(paramsValidationResult.data)
                if (transferResult.isSuccess) {
                    return@post responseInflator.inflateResponseWithResult(response, 200, transferResult)
                } else {
                    return@post responseInflator.inflateResponseWithResult(response, 400, transferResult)

                }
            } else {
                return@post responseInflator.inflateResponseWithResult(response, 400, paramsValidationResult)
            }
        }

        after(TRANSFER_PATH) { _, response -> response.header("Content-Type", "application/json") }

        exception(Exception::class.java) { exception, _, response ->
            responseInflator.inflateResponseWithException(response, exception)
        }
    }

}

