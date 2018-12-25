package revoluttransfer.repositories.account

import revoluttransfer.models.db.Account

interface AccountRepository {
    fun findByNumber(number: Long): Account?
    fun saveAccountChanges(debitAccount: Account, creditAccount: Account): TransactionCodeResult
}