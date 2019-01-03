package revoluttransfer.routes.transfer

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.inject.Inject
import revoluttransfer.models.ResultData
import revoluttransfer.models.dto.TransferDto
import java.math.BigDecimal

class TransferParamsValidatorImpl @Inject constructor(private val gson: Gson) : TransferParamsValidator {

    override fun validateAndGet(body: String): ResultData<TransferDto> {
        val transferDto = try {
            gson.fromJson(body, TransferDto::class.java)
        } catch (ex: JsonSyntaxException) {
            return ResultData(
                    isSuccess = false,
                    reason = ex.localizedMessage
            )
        }
        val moneyToTransfer = try {
            transferDto.moneyAmount.toBigDecimal()
        } catch (ex: NumberFormatException) {
            return ResultData(
                    isSuccess = false,
                    reason = ex.localizedMessage
            )
        }
        return when {
            transferDto.debitAccountNumber == transferDto.creditAccountNumber -> ResultData(
                    isSuccess = false,
                    reason = "debitAccountNumber is equal to creditAccountNumber"
            )
            moneyToTransfer < BigDecimal.ZERO -> {
                ResultData(
                        isSuccess = false,
                        reason = "money amount can\'t be less than zero "
                )
            }
            else -> ResultData(
                    isSuccess = true,
                    data = transferDto
            )
        }
    }
}