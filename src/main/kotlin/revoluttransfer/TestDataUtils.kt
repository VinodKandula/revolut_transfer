package revoluttransfer

import revoluttransfer.models.db.Account
import revoluttransfer.models.db.Holder
import java.math.BigDecimal
import java.util.concurrent.ConcurrentHashMap
import javax.persistence.EntityManager


fun createConcurrentKeySet() = ConcurrentHashMap.newKeySet<Holder>().apply {
    createTestHolders(createTestAccountList()).forEach {
        add(it)
    }
}

fun createConcurrentMapAccounts() = ConcurrentHashMap<Long, Account>().apply {
    createTestAccountList().forEach {
        put(it.number, it)
    }
}

fun createTestHolders(accounts: List<Account>): List<Holder> {
    return listOf(
            Holder(name = "holder1", lastName = "H1", email = "h1@gmail.com", accounts = listOf(accounts[0], accounts[1], accounts[2])),
            Holder(name = "holder2", lastName = "H2", email = "h2@gmail.com", accounts = listOf(accounts[3], accounts[4])),
            Holder(name = "holder3", lastName = "H3", email = "h3@gmail.com", accounts = listOf(accounts[5])),
            Holder(name = "holder4", lastName = "H4", email = "h4@gmail.com", accounts = listOf(accounts[6], accounts[7]))
    )

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