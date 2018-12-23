package revoluttransfer.routes.transfer

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.inject.Inject
import revoluttransfer.EMAIL_REGEX
import revoluttransfer.models.OperationResult
import revoluttransfer.models.dto.TransferDto
import java.math.BigDecimal
import java.util.regex.Pattern

class TransferParamsValidatorImpl @Inject constructor(private val gson: Gson) : TransferParamsValidator {

    private val pattern by lazy { Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE) }

    override fun validateAndGet(body: String): OperationResult<TransferDto> {
        val transferDto = try {
            gson.fromJson(body, TransferDto::class.java)
        } catch (ex: JsonSyntaxException) {
            return OperationResult(
                    isSuccess = false,
                    failReason = ex.localizedMessage
            )
        }
        val moneyToTransfer = try {
            transferDto.moneyAmount.toBigDecimal()
        } catch (ex: NumberFormatException) {
            return OperationResult(
                    isSuccess = false,
                    failReason = ex.localizedMessage
            )
        }
        return when {
            transferDto.creditHolderEmail == null && transferDto.creditAccountNumber == null -> OperationResult(
                    isSuccess = false,
                    failReason = "data for credited client wasn\'t provided"
            )
            transferDto.creditHolderEmail != null && !pattern.matcher(transferDto.creditHolderEmail).matches() -> OperationResult(
                    isSuccess = false,
                    failReason = "email is wrong"
            )
            transferDto.debitAccountNumber == transferDto.creditAccountNumber -> OperationResult(
                    isSuccess = false,
                    failReason = "debitAccountNumber is equal to creditAccountNumber"
            )
            moneyToTransfer < BigDecimal.ZERO -> {
                OperationResult(
                        isSuccess = false,
                        failReason = "money amount can\'t be less than zero "
                )
            }
            else -> OperationResult(
                    isSuccess = true,
                    data = transferDto
            )
        }
    }
}