package revoluttransfer

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import revoluttransfer.interactors.holder.HolderServiceImpl
import revoluttransfer.models.db.Holder
import revoluttransfer.models.dto.HolderDto
import revoluttransfer.repositories.holder.HolderRepository


@RunWith(MockitoJUnitRunner::class)
class HolderServiceTest {

    @Mock
    private lateinit var repository: HolderRepository

    private lateinit var holderInteractor: HolderServiceImpl

    @Before
    fun setUp() {
        holderInteractor = HolderServiceImpl(repository)
    }

    @Test
    fun `fails when no email was found`() {
        val testEmail ="email@q.com"
        whenever(repository.getHolderByEmail(testEmail)).thenReturn(null)
        val result = holderInteractor.getHolderByEmail(testEmail)
        assert(!result.isSuccess)
    }

    @Test
    fun `success when email was found`() {
        val testEmail ="email@q.com"
        val testHolder = Holder(email = testEmail, name = "test", lastName ="test")
        whenever(repository.getHolderByEmail(testEmail)).thenReturn(testHolder)
        val result = holderInteractor.getHolderByEmail(testEmail)
        assert(result.isSuccess)
        assert(result.data is HolderDto)
    }

    @Test
    fun `success when resultset is not empty`() {
        val testHolder1 = Holder(email = "email@q.com", name = "test", lastName ="test")
        val testHolder2 = Holder(email = "email@q.com", name = "test", lastName ="test")
        val testHolder3 = Holder(email = "email@q.com", name = "test", lastName ="test")
        val testHolder4 = Holder(email = "email@q.com", name = "test", lastName ="test")
        whenever(repository.getAll()).thenReturn(arrayListOf(testHolder1, testHolder2, testHolder3, testHolder4))
        val result = holderInteractor.getAllHolders()
        assert(result.isSuccess)
        assert(result.data is List<HolderDto>)
        assert(result.data?.size == 4)
    }

    @Test
    fun `success when there are no holders`() {
        whenever(repository.getAll()).thenReturn(null)
        val result = holderInteractor.getAllHolders()
        assert(result.isSuccess)
        assert(result.data?.isEmpty() == true)
    }


}