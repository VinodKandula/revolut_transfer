package revoluttransfer.routes.transfer

import com.google.inject.Inject
import revoluttransfer.services.transfer.TransferService
import revoluttransfer.routes.Router
import revoluttransfer.routes.TRANSFER_PATH
import revoluttransfer.utils.ResponseConstructor
import spark.Spark.after
import spark.Spark.post

class TransferRouter @Inject constructor(
        private val responseConstructor: ResponseConstructor,
        private val transferParamsValidator: TransferParamsValidator,
        private val transferService: TransferService
) : Router {

    override fun register() {

        post(TRANSFER_PATH) { request, response ->
            val paramsValidationResult = transferParamsValidator.validateAndGet(request.body())
            if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                val transferResult = transferService.commitTransfer(paramsValidationResult.data)
                if (transferResult.isSuccess) {
                    return@post responseConstructor.constructResponseWithResult(
                            response = response,
                            resultData = transferResult
                    )
                } else {
                    return@post responseConstructor.constructErrorResponseWithResult(response, 400, transferResult)
                }
            } else {
                return@post responseConstructor.constructErrorResponseWithResult(response, 400, paramsValidationResult)
            }
        }

        after(TRANSFER_PATH) { _, response -> response.header("Content-Type", "application/json") }
    }

}

