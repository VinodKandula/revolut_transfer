package revoluttransfer.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import revoluttransfer.PERSISTNECE_NAME
import revoluttransfer.interactors.TransferInteractor
import revoluttransfer.interactors.TransferInteractorImpl
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepository
import revoluttransfer.repositories.holder.HolderRepositoryImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

class ApplicationModule : AbstractModule() {

    override fun configure() {
        bind(TransferParamsValidator::class.java).to(TransferParamsValidatorImpl::class.java).asEagerSingleton()
        bind(HolderRepository::class.java).to(HolderRepositoryImpl::class.java).asEagerSingleton()
        bind(AccountRepository::class.java).to(AccountRepositoryImpl::class.java).asEagerSingleton()
        bind(TransferInteractor::class.java).to(TransferInteractorImpl::class.java).asEagerSingleton()
    }

    @Singleton
    @Provides
    fun provideEntityManager(factory: EntityManagerFactory): EntityManager = factory.createEntityManager()

    @Singleton
    @Provides
    fun provideEntityManagerFactory() = Persistence.createEntityManagerFactory(PERSISTNECE_NAME)
}