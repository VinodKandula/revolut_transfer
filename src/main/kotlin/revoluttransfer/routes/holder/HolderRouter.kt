package revoluttransfer.routes.holder

import revoluttransfer.interactors.holder.HolderInteractor
import revoluttransfer.routes.HOLDER_PATH
import revoluttransfer.routes.Router
import revoluttransfer.utils.ResponseInflator
import spark.Spark.*
import javax.inject.Inject

class HolderRouter @Inject constructor(
        private val responseInflator: ResponseInflator,
        private val holderParamsValidator: HolderParamsValidator,
        private val holderInteractor: HolderInteractor
) : Router {

    override fun register() {

        path(HOLDER_PATH) {
            get("/") { request, response ->
                responseInflator.inflateResponseWithResult(response, holderInteractor.getAllHolders())
            }
            get("/:email") { request, response ->
                val paramsValidationResult = holderParamsValidator.validateAndGet(request.params("email"))
                if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                    val transferResult = holderInteractor.getHolderByEmail(paramsValidationResult.data)
                    if (transferResult.isSuccess) {

                        return@get responseInflator.inflateResponseWithResult(response, transferResult)
                    } else {
                        return@get responseInflator.inflateErrorResponseWithResult(response, 400, transferResult)
                    }
                }
            }
            after("/*") { _, response -> response.header("Content-Type", "application/json") }
        }

    }
}