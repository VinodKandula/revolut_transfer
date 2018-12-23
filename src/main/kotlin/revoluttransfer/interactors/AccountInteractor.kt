package revoluttransfer.interactors

import revoluttransfer.repositories.account.AccountRepository

interface AccountInteractor

class AccountInteractorImpl(repository: AccountRepository) : AccountInteractor