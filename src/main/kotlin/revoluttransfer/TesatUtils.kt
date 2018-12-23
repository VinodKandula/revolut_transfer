package revoluttransfer

import revoluttransfer.models.db.Account
import revoluttransfer.models.db.Holder
import java.math.BigDecimal
import javax.persistence.EntityManager


fun createTestEntities(manager: EntityManager) {

    manager.transaction.begin()
    val account1 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101010)
    val account2 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101020)
    val account3 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101030)

    val account4 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101040)
    val account5 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101050)

    val account6 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101060)

    val account7 = Account(balance = BigDecimal(1000000), isDefault = true, number = 10101070)
    val account8 = Account(balance = BigDecimal(1000000), isDefault = false, number = 10101080)

    val holder1 = Holder(name = "holder1", lastName = "H1", email = "h1@gmail.com", accounts = listOf(account1, account2, account3))
    val holder2 = Holder(name = "holder2", lastName = "H2", email = "h2@gmail.com", accounts = listOf(account4, account5))
    val holder3 = Holder(name = "holder3", lastName = "H3", email = "h3@gmail.com", accounts = listOf(account6))
    val holder4 = Holder(name = "holder4", lastName = "H4", email = "h4@gmail.com", accounts = listOf(account7, account8))

    manager.persist(account1)
    manager.persist(account2)
    manager.persist(account3)
    manager.persist(account4)
    manager.persist(account5)
    manager.persist(account6)
    manager.persist(account7)
    manager.persist(account8)
    manager.persist(holder1)
    manager.persist(holder2)
    manager.persist(holder3)
    manager.persist(holder4)
    manager.transaction.commit()
}