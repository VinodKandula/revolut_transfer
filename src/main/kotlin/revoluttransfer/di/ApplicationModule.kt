package revoluttransfer.di

import com.google.inject.AbstractModule
import revoluttransfer.createConcurrentKeySet
import revoluttransfer.createConcurrentMapAccounts
import revoluttransfer.services.holder.HolderService
import revoluttransfer.services.holder.HolderServiceImpl
import revoluttransfer.services.transfer.TransferService
import revoluttransfer.services.transfer.TransferServiceImpl
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepository
import revoluttransfer.repositories.holder.HolderRepositoryImpl
import revoluttransfer.routes.holder.HolderParamsValidator
import revoluttransfer.routes.holder.HolderParamsValidatorImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl

/*
    Because I have no specific scopes in test task and for simplicity I provide all dependencies in one module
*/
class ApplicationModule : AbstractModule() {

    override fun configure() {
        bind(HolderRepository::class.java).toInstance(HolderRepositoryImpl(createConcurrentKeySet()))
        bind(AccountRepository::class.java).toInstance(AccountRepositoryImpl(createConcurrentMapAccounts()))
        bind(HolderParamsValidator::class.java).to(HolderParamsValidatorImpl::class.java).asEagerSingleton()
        bind(TransferParamsValidator::class.java).to(TransferParamsValidatorImpl::class.java).asEagerSingleton()
        bind(HolderService::class.java).to(HolderServiceImpl::class.java).asEagerSingleton()
        bind(TransferService::class.java).to(TransferServiceImpl::class.java).asEagerSingleton()
    }
}