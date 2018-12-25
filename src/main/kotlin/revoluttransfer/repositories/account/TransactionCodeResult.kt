package revoluttransfer.repositories.account

enum class TransactionCodeResult {
    NOT_ENOUGH_MONEY,
    UPDATE_CONFLICT,
    ROLLBACK_CONFLICT,
    UNKNOWN,
    SUCCESS
}