package revoluttransfer.routes.account

import revoluttransfer.ACCOUNTS_PATH
import revoluttransfer.interactors.AccountInteractor
import spark.Spark.get
import spark.Spark.path

class AccountRouter(interactor: AccountInteractor) {

    fun register() {

        path(ACCOUNTS_PATH) {
            get("/") { request, response -> }
            get("/:accountId") { request, response -> }
        }
    }
}