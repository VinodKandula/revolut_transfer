package revoluttransfer

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import revoluttransfer.models.db.Account
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.services.transfer.TransferServiceImpl
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger


@RunWith(MockitoJUnitRunner::class)
class MultipleThreadTestTransferServiceTest {

    val accountRepository = AccountRepositoryImpl(createConcurrentMapAccounts().apply {
        put(1, Account(
                balance = BigDecimal(2000),
                isDefault = true,
                number = 1,
                version = 0
        ))
        put(2, Account(
                balance = BigDecimal(2000),
                isDefault = true,
                number = 2,
                version = 0
        ))
        put(3, Account(
                balance = BigDecimal(2000),
                isDefault = true,
                number = 3,
                version = 0
        ))
    })

    private lateinit var interactor: TransferServiceImpl

    @Before
    fun setUp() {
        interactor = TransferServiceImpl(accountRepository)
    }

    @Test
    fun `transfer from 1 account to 2`() {
        val lostTransactionCount = AtomicInteger(0)
        val latch = CountDownLatch(200)
        val executor1 = Executors.newFixedThreadPool(20)
        for (i in 0 until 200) {
            executor1.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 1,
                        creditAccountNumber = 2

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
        }
        latch.await()
        val ac1 = accountRepository.findByNumber(1)!!
        val ac2 = accountRepository.findByNumber(2)!!
        val balance1 = ac1.balance
        val balance2 = ac2.balance
        println("balance1 $balance1")
        println("balance2 $balance2")
        assert(balance1.plus(balance2) == BigDecimal(4000))
        assert(ac1.version == ac2.version)
    }

    @Test
    fun `transfer from 1 account to 2 account and from 2 to 3 account`() {
        val lostTransactionCount = AtomicInteger(0)
        val latch = CountDownLatch(400)
        val executor1 = Executors.newFixedThreadPool(20)
        val executor2 = Executors.newFixedThreadPool(20)
        for (i in 0 until 200) {
            executor1.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 1,
                        creditAccountNumber = 2

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
            executor2.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 2,
                        creditAccountNumber = 3

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
        }
        latch.await()
        val ac1 = accountRepository.findByNumber(1)!!
        val ac2 = accountRepository.findByNumber(2)!!
        val ac3 = accountRepository.findByNumber(3)!!
        val balance1 = ac1.balance
        val balance2 = ac2.balance
        val balance3 = ac3.balance
        println("balance1 $balance1")
        println("balance2 $balance2")
        println("balance3 $balance3")
        println("lost attemps ${lostTransactionCount.get()}")
        if (lostTransactionCount.get() == 0) {
            assert(ac1.balance == 0.toBigDecimal())
            assert(ac2.balance == 2000.toBigDecimal())
            assert(ac3.balance == 4000.toBigDecimal())
        }
        assert(balance1.plus(balance2).plus(balance3) == BigDecimal(6000))
    }

    @Test
    fun `transfer from 1 account to 2 account and from 2 to 3 and from 3 to 1`() {
        val lostTransactionCount = AtomicInteger(0)
        val latch = CountDownLatch(600)
        val executor1 = Executors.newFixedThreadPool(20)
        val executor2 = Executors.newFixedThreadPool(20)
        val executor3 = Executors.newFixedThreadPool(20)
        for (i in 0 until 200) {
            executor1.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 1,
                        creditAccountNumber = 2

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
            executor2.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 2,
                        creditAccountNumber = 3

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
            executor3.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 3,
                        creditAccountNumber = 1

                ))
                if (!result.isSuccess) {
                    lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
        }
        latch.await()
        val ac1 = accountRepository.findByNumber(1)!!
        val ac2 = accountRepository.findByNumber(2)!!
        val ac3 = accountRepository.findByNumber(3)!!
        val balance1 = ac1.balance
        val balance2 = ac2.balance
        val balance3 = ac3.balance
        println("balance1 $balance1")
        println("balance2 $balance2")
        println("balance3 $balance3")
        println("lost attemps ${lostTransactionCount.get()}")
        if (lostTransactionCount.get() == 0) {
            assert(ac1.balance == 2000.toBigDecimal())
            assert(ac2.balance == 2000.toBigDecimal())
            assert(ac3.balance == 2000.toBigDecimal())
        }
        assert(balance1.plus(balance2).plus(balance3) == BigDecimal(6000))
    }

    @Test
    fun `transfer from 1 account to 2 account amount of money bigger than 1 account have`() {
        val lostTransactionCount = AtomicInteger(0)
        val notEnoughMoneyAttempts = AtomicInteger(0)
        val latch = CountDownLatch(400)
        val executor1 = Executors.newFixedThreadPool(20)
        for (i in 0 until 400) {
            executor1.execute {
                val result = interactor.commitTransfer(TransferDto(
                        moneyAmount = "10",
                        debitAccountNumber = 1,
                        creditAccountNumber = 2

                ))
                when {
                    !result.isSuccess && result.reason == "Not enough money" -> notEnoughMoneyAttempts.incrementAndGet()
                    !result.isSuccess -> lostTransactionCount.incrementAndGet()
                }
                latch.countDown()
            }
        }
        latch.await()
        val ac1 = accountRepository.findByNumber(1)!!
        val ac2 = accountRepository.findByNumber(2)!!
        val balance1 = ac1.balance
        val balance2 = ac2.balance
        println("balance1 $balance1")
        println("balance2 $balance2")
        println("lost attemps ${lostTransactionCount.get()}")
        println("not enough money attemps ${notEnoughMoneyAttempts.get()}")
        if (lostTransactionCount.get() == 0) {
            assert(notEnoughMoneyAttempts.get() == 200)
        }
        assert(balance1.plus(balance2) == BigDecimal(4000))
    }
}
