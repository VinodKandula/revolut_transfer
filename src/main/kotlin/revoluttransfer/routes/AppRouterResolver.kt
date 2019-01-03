package revoluttransfer.routes

import com.google.inject.Guice
import revoluttransfer.di.ApplicationModule
import revoluttransfer.routes.holder.HolderRouter
import revoluttransfer.routes.transfer.TransferRouter

class AppRouterResolver {

    fun registerHttpRoutes() {
        val injector = Guice.createInjector(ApplicationModule())
        injector.getInstance(TransferRouter::class.java).register()
        injector.getInstance(HolderRouter::class.java).register()
        injector.getInstance(CommonExceptionRouter::class.java).register()
    }
}