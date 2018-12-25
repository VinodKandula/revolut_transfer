package revoluttransfer.routes.transfer

import com.google.inject.Inject
import revoluttransfer.interactors.transfer.TransferInteractor
import revoluttransfer.routes.Router
import revoluttransfer.routes.TRANSFER_PATH
import revoluttransfer.utils.ResponseInflator
import spark.Spark.*

class TransferRouter @Inject constructor(
        private val responseInflator: ResponseInflator,
        private val transferParamsValidator: TransferParamsValidator,
        private val transferInteractor: TransferInteractor
) : Router {

    override fun register() {

        post(TRANSFER_PATH) { request, response ->
            val paramsValidationResult = transferParamsValidator.validateAndGet(request.body())
            if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                val transferResult = transferInteractor.commitTransfer(paramsValidationResult.data)
                if (transferResult.isSuccess) {
                    return@post responseInflator.inflateResponseWithResult(
                            response = response,
                            resultData = transferResult
                    )
                } else {
                    return@post responseInflator.inflateErrorResponseWithResult(response, 400, transferResult)
                }
            } else {
                return@post responseInflator.inflateErrorResponseWithResult(response, 400, paramsValidationResult)
            }
        }

        after(TRANSFER_PATH) { _, response -> response.header("Content-Type", "application/json") }
    }

}

