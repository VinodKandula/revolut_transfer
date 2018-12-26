package revoluttransfer.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import revoluttransfer.PERSISTNECE_NAME
import revoluttransfer.interactors.holder.HolderInteractor
import revoluttransfer.interactors.holder.HolderInteractorImpl
import revoluttransfer.interactors.transfer.TransferInteractor
import revoluttransfer.interactors.transfer.TransferInteractorImpl
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.account.LocalMapAccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepository
import revoluttransfer.repositories.holder.HolderRepositoryImpl
import revoluttransfer.repositories.holder.LocalHolderRepositoryImpl
import revoluttransfer.routes.holder.HolderParamsValidator
import revoluttransfer.routes.holder.HolderParamsValidatorImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

/*
    Because I have no specific scopes in test task and for simplicity I provide all dependencies in one module
*/
class ApplicationModule(private val isDbMode: Boolean = false) : AbstractModule() {

    override fun configure() {
        if (isDbMode) {
            bind(HolderRepository::class.java).to(HolderRepositoryImpl::class.java).asEagerSingleton()
            bind(AccountRepository::class.java).to(AccountRepositoryImpl::class.java).asEagerSingleton()
        } else {
            bind(HolderRepository::class.java).to(LocalHolderRepositoryImpl::class.java).asEagerSingleton()
            bind(AccountRepository::class.java).to(LocalMapAccountRepositoryImpl::class.java).asEagerSingleton()
        }
        bind(HolderParamsValidator::class.java).to(HolderParamsValidatorImpl::class.java).asEagerSingleton()
        bind(TransferParamsValidator::class.java).to(TransferParamsValidatorImpl::class.java).asEagerSingleton()
        bind(HolderInteractor::class.java).to(HolderInteractorImpl::class.java).asEagerSingleton()
        bind(TransferInteractor::class.java).to(TransferInteractorImpl::class.java).asEagerSingleton()
    }

    @Singleton
    @Provides
    fun provideEntityManager(factory: EntityManagerFactory): EntityManager = factory.createEntityManager()

    @Singleton
    @Provides
    fun provideEntityManagerFactory() = Persistence.createEntityManagerFactory(PERSISTNECE_NAME)
}