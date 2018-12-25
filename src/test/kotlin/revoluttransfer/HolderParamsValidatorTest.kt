package revoluttransfer

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import revoluttransfer.routes.holder.HolderParamsValidatorImpl


@RunWith(MockitoJUnitRunner::class)
class HolderParamsValidatorTest {

    private val validator = HolderParamsValidatorImpl()

    @Test
    fun `unsuccess result if email is wrong formatted`() {
        val body = "h2@gmail....com"
        assert(!validator.validateAndGet(body).isSuccess)
    }

    @Test
    fun `success result if email is wrong formatted`() {
        val body = "h2@gmail.com"
        val result = validator.validateAndGet(body)
        assert(result.isSuccess)
        assert(result.data == body)
    }

}