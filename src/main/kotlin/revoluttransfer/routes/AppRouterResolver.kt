package revoluttransfer.routes

import com.google.inject.Guice
import revoluttransfer.createTestEntities
import revoluttransfer.di.ApplicationModule
import revoluttransfer.routes.holder.HolderRouter
import revoluttransfer.routes.transfer.TransferRouter
import spark.Spark
import javax.persistence.EntityManager

class AppRouterResolver {

    fun registerAppRoutes() {
        val injector = Guice.createInjector(ApplicationModule())
        createTestEntities(injector.getInstance(EntityManager::class.java))
        injector.getInstance(TransferRouter::class.java).register()
        injector.getInstance(HolderRouter::class.java).register()
        injector.getInstance(CommonExceptionRouter::class.java).register()
    }
}