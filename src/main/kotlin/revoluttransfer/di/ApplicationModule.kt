package revoluttransfer.di

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import revoluttransfer.interactors.TransferInteractor
import revoluttransfer.interactors.TransferInteractorImpl
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepository
import revoluttransfer.repositories.holder.HolderRepositoryImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl
import javax.persistence.EntityManager
import javax.persistence.Persistence

class ApplicationModule : AbstractModule() {

    override fun configure() {
//        bind(EntityManager::class.java).toInstance(provideEntityManager())
        bind(TransferParamsValidator::class.java).to(TransferParamsValidatorImpl::class.java)
        bind(HolderRepository::class.java).to(HolderRepositoryImpl::class.java)
        bind(AccountRepository::class.java).to(AccountRepositoryImpl::class.java)
        bind(TransferInteractor::class.java).to(TransferInteractorImpl::class.java)
    }

    @Singleton
    @Provides
    fun provideEntityManager(): EntityManager = Persistence.createEntityManagerFactory("transfers").createEntityManager()
}