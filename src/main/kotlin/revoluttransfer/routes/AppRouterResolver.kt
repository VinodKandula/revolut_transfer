package revoluttransfer.routes

import com.google.inject.Guice
import revoluttransfer.createTestEntities
import revoluttransfer.di.ApplicationModule
import revoluttransfer.routes.transfer.TransferRouter
import javax.persistence.EntityManager

class AppRouterResolver {

    fun registerAppRoutes() {
        val injector = Guice.createInjector(ApplicationModule())
        createTestEntities(injector.getInstance(EntityManager::class.java))
        injector.getInstance(TransferRouter::class.java).register()
    }
}