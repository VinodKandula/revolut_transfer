package revoluttransfer

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revoluttransfer.interactors.transfer.TransferServiceImpl
import revoluttransfer.models.db.Account
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepositoryImpl
import revoluttransfer.repositories.holder.HolderRepository
import java.math.BigDecimal


@RunWith(MockitoJUnitRunner::class)
class MultipleThreadTestTransferServiceTest {
    
    val accountRepository = AccountRepositoryImpl(createConcurrentMapAccounts().apply {
        put(1, Account(
                balance = BigDecimal(200),
                isDefault = true,
                number = 1,
                version = 0
        ))
        put(2, Account(
                balance = BigDecimal(200),
                isDefault = true,
                number = 2,
                version = 0
        ))

    })

    @Mock
    private lateinit var holderRepository: HolderRepository

    private lateinit var interactor: TransferServiceImpl

    @Before
    fun setUp() {
        interactor = TransferServiceImpl(accountRepository, holderRepository)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `data consistency in multiple threads`() {
        runBlocking {
            repeat(10) {
                launch(newSingleThreadContext("concurrency")) {

                    interactor.commitTransfer(TransferDto(
                            moneyAmount = "10",
                            debitAccountNumber = 1,
                            creditAccountNumber = 2

                    ))
                }
            }
        }
        val ac1 = accountRepository.findByNumber(1)!!
        val ac2 = accountRepository.findByNumber(2)!!
        val balance1 = ac1.balance
        val balance2 = ac2.balance
        println("balance1 $balance1")
        println("balance2 $balance2")
        assert(balance1.plus(balance2) == BigDecimal(400))
        assert(ac1.version == ac2.version)
    }


}