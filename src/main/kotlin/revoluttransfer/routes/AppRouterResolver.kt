package revoluttransfer.routes

import com.google.inject.Guice
import revoluttransfer.createTestDbEntities
import revoluttransfer.di.ApplicationModule
import revoluttransfer.routes.holder.HolderRouter
import revoluttransfer.routes.transfer.TransferRouter
import javax.persistence.EntityManager

class AppRouterResolver {

    fun registerAppRoutes(isDbMode: Boolean = true) {
        val injector = Guice.createInjector(ApplicationModule(isDbMode))
        if (isDbMode) {
            createTestDbEntities(injector.getInstance(EntityManager::class.java))
        }
        injector.getInstance(TransferRouter::class.java).register()
        injector.getInstance(HolderRouter::class.java).register()
        injector.getInstance(CommonExceptionRouter::class.java).register()
    }
}