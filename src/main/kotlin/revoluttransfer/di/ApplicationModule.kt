package revoluttransfer.di

import com.google.inject.AbstractModule
import revoluttransfer.createConcurrentMapAccounts
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.routes.transfer.TransferParamsValidator
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl
import revoluttransfer.services.transfer.TransferService
import revoluttransfer.services.transfer.TransferServiceImpl

/*
    Because I have no specific scopes in test task and for simplicity I provide all dependencies in one module
*/
class ApplicationModule : AbstractModule() {

    override fun configure() {

        bind(AccountRepository::class.java).toInstance(AccountRepositoryImpl(createConcurrentMapAccounts()))
        bind(TransferParamsValidator::class.java).to(TransferParamsValidatorImpl::class.java).asEagerSingleton()
        bind(TransferService::class.java).to(TransferServiceImpl::class.java).asEagerSingleton()
    }
}