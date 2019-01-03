package revoluttransfer

import revoluttransfer.models.db.Account
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap


fun createConcurrentMapAccounts() = ConcurrentHashMap<Long, Account>().apply {
    createTestAccountList().forEach {
        put(it.number, it)
    }
}

fun createTestAccountList(): List<Account> {
    return listOf(
            Account(balance = BigDecimal(1000000), isDefault = true, number = 10101010),
            Account(balance = BigDecimal(1000000), isDefault = false, number = 10101020),
            Account(balance = BigDecimal(1000000), isDefault = false, number = 10101030),
            Account(balance = BigDecimal(1000000), isDefault = true, number = 10101040),
            Account(balance = BigDecimal(1000000), isDefault = false, number = 10101050),
            Account(balance = BigDecimal(1000000), isDefault = true, number = 10101060),
            Account(balance = BigDecimal(1000000), isDefault = true, number = 10101070),
            Account(balance = BigDecimal(1000000), isDefault = false, number = 10101080)
    )

}