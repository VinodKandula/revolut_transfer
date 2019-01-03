package revoluttransfer

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.OngoingStubbing
import revoluttransfer.models.db.Account
import revoluttransfer.models.db.minus
import revoluttransfer.models.db.plus
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.account.TransactionCodeResult
import revoluttransfer.services.transfer.TransferServiceImpl
import java.math.BigDecimal

fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!

@RunWith(MockitoJUnitRunner::class)
class TransferServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    private lateinit var interactor: TransferServiceImpl

    @Before
    fun setUp() {
        interactor = TransferServiceImpl(accountRepository)
    }

    @Test
    fun `failed if amount of money is bigger than current balance`() {
        val testTransferDto = TransferDto(
                moneyAmount = "1000",
                debitAccountNumber = 1,
                creditAccountNumber = 2
        )
        whenever(accountRepository.findByNumber(1)).thenReturn(Account("20".toBigDecimal(), true, 1))
        whenever(accountRepository.findByNumber(2)).thenReturn(Account("20".toBigDecimal(), true, 2))
        val result = interactor.commitTransfer(testTransferDto)
        assert(!result.isSuccess)
    }

    @Test
    fun `success if transfer with proper balance`() {
        val rawMoneyToTransfer = "20"
        val transferMoney = BigDecimal(rawMoneyToTransfer)
        val testTransferDto = TransferDto(
                moneyAmount = rawMoneyToTransfer,
                debitAccountNumber = 1,
                creditAccountNumber = 2
        )
        val account1 = Account("2000".toBigDecimal(), true, 1)
        val account2 = Account("20".toBigDecimal(), true, 2)
        whenever(accountRepository.findByNumber(1)).thenReturn(account1)
        whenever(accountRepository.findByNumber(2)).thenReturn(account2)
        whenever(accountRepository.saveAccountChanges(account1.minus(transferMoney), account2.plus(transferMoney))).thenReturn(TransactionCodeResult.SUCCESS)
        val result = interactor.commitTransfer(testTransferDto)
        assert(result.isSuccess)
    }

}