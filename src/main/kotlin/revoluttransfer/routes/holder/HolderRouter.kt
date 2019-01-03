package revoluttransfer.routes.holder

import revoluttransfer.interactors.holder.HolderService
import revoluttransfer.routes.HOLDER_PATH
import revoluttransfer.routes.Router
import revoluttransfer.utils.ResponseConstructor
import spark.Spark.*
import javax.inject.Inject

class HolderRouter @Inject constructor(
        private val responseConstructor: ResponseConstructor,
        private val holderParamsValidator: HolderParamsValidator,
        private val holderService: HolderService
) : Router {

    override fun register() {

        path(HOLDER_PATH) {
            get("/") { _, response ->
                responseConstructor.inflateResponseWithResult(response, holderService.getAllHolders())
            }
            get("/:email") { request, response ->
                val paramsValidationResult = holderParamsValidator.validateAndGet(request.params("email"))
                if (paramsValidationResult.isSuccess && paramsValidationResult.data != null) {
                    val transferResult = holderService.getHolderByEmail(paramsValidationResult.data)
                    if (transferResult.isSuccess) {

                        return@get responseConstructor.inflateResponseWithResult(response, transferResult)
                    } else {
                        return@get responseConstructor.inflateErrorResponseWithResult(response, 400, transferResult)
                    }
                }
            }
            after("/*") { _, response -> response.header("Content-Type", "application/json") }
        }

    }
}