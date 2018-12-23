package revoluttransfer.routes

import com.google.gson.Gson
import revoluttransfer.interactors.TransferInteractorImpl
import revoluttransfer.manager
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepositoryImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferRouter

class AppRouterResolver {

    fun registerAppRoutes() {
        TransferRouter(
                transferParamsValidator = TransferParamsValidator(Gson()),
                transferInteractor = TransferInteractorImpl(
                        accountRepository = AccountRepositoryImpl(manager),
                        holderRepository = HolderRepositoryImpl(manager)
                )
        ).register()
    }

}