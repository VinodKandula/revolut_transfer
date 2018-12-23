package revoluttransfer

import com.google.gson.Gson
import org.junit.Test
import revoluttransfer.routes.transfer.TransferParamsValidatorImpl

class TransferParamsValidatorImplTest {

    private val validator = TransferParamsValidatorImpl(Gson())

    @Test
    fun `unsuccess result if money ammount is less than 0`() {
        val body = "{ moneyAmount: '-100', debitAccountNumber: '10101010', creditAccountNumber: '10101040' }"
        assert(!validator.validateAndGet(body).isSuccess)
    }

    @Test
    fun `unsuccess result if email is null and credit account is null`() {
        val body = "{ moneyAmount: '-100', debitAccountNumber: '10101010' }"
        assert(!validator.validateAndGet(body).isSuccess)
    }

    @Test
    fun `unsuccess result if debit account is the same as credit account `() {
        val body = "{ moneyAmount: '100', debitAccountNumber: '10101010', creditAccountNumber: '10101010' }"
        assert(!validator.validateAndGet(body).isSuccess)
    }

    @Test
    fun `unsuccess result if email is wrong formatted`() {
        val body = "{ moneyAmount: '100', debitAccountNumber: '10101010', creditHolderEmail: 'h2@gmail....com' }"
        assert(!validator.validateAndGet(body).isSuccess)
    }

}