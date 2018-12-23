package revoluttransfer

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.OngoingStubbing
import revoluttransfer.interactors.TransferInteractorImpl
import revoluttransfer.models.db.Account
import revoluttransfer.models.db.Holder
import revoluttransfer.models.dto.TransferDto
import revoluttransfer.repositories.account.AccountRepository
import revoluttransfer.repositories.holder.HolderRepository

fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)!!

@RunWith(MockitoJUnitRunner::class)
class TransferInteractorTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var holderRepository: HolderRepository

    private lateinit var interactor: TransferInteractorImpl

    @Before
    fun setUp() {
        interactor = TransferInteractorImpl(accountRepository, holderRepository)
    }

    @Test
    fun `failed if amount of money is bigger than current balance`() {
        val testTransferDto = TransferDto(
                moneyAmount = "1000",
                debitAccountNumber = 1,
                creditAccountNumber = 2
        )
        whenever(accountRepository.findByNumber(1)).thenReturn(Account(null, "20".toBigDecimal(), true, 1))
        whenever(accountRepository.findByNumber(2)).thenReturn(Account(null, "20".toBigDecimal(), true, 2))
        val result = interactor.commitTransfer(testTransferDto)
        assert(!result.isSuccess)
    }

    @Test
    fun `failed if email address belongs to the debit account number holder`() {
        val email = "test@email.com"
        val testTransferDto = TransferDto(
                moneyAmount = "1000",
                debitAccountNumber = 1,
                creditHolderEmail = email
        )
        val testAccount = Account(null, "20".toBigDecimal(), true, 1)
        whenever(holderRepository.getHolderByEmail(email)).thenReturn(Holder(email = email, name = "", lastName = "", accounts = listOf(testAccount)))
        val result = interactor.commitTransfer(testTransferDto)
        assert(!result.isSuccess)
    }

    @Test
    fun `success if transfer with proper balance`() {
        val testTransferDto = TransferDto(
                moneyAmount = "1000",
                debitAccountNumber = 1,
                creditAccountNumber = 2
        )
        whenever(accountRepository.findByNumber(1)).thenReturn(Account(null, "2000".toBigDecimal(), true, 1))
        whenever(accountRepository.findByNumber(2)).thenReturn(Account(null, "20".toBigDecimal(), true, 2))
        val result = interactor.commitTransfer(testTransferDto)
        assert(result.isSuccess)
    }

    @Test
    fun `success if transfer to right email`() {
        val email = "email@e.com"
        val debitAccount = 10L
        val creditAccount = 20L
        val testTransferDto = TransferDto(
                moneyAmount = "1000",
                debitAccountNumber = debitAccount,
                creditHolderEmail = email
        )
        val testAccount = Account(null, "2000".toBigDecimal(), true, 10)
        whenever(accountRepository.findByNumber(10L)).thenReturn(testAccount)
        whenever(holderRepository.getHolderByEmail(email)).thenReturn(Holder(email = email, name = "", lastName = "", accounts = listOf(Account(null, "20".toBigDecimal(), true, creditAccount))))
        val result = interactor.commitTransfer(testTransferDto)
        assert(result.isSuccess)
    }
}