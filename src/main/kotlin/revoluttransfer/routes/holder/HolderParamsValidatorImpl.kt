package revoluttransfer.routes.holder

import revoluttransfer.EMAIL_REGEX
import revoluttransfer.models.ResultData
import java.util.regex.Pattern

class HolderParamsValidatorImpl : HolderParamsValidator {

    private val pattern by lazy { Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE) }

    override fun validateAndGet(parameters: String): ResultData<String> {
        return if (pattern.matcher(parameters).matches()) {
            ResultData(true, parameters)
        } else {
            ResultData(isSuccess = false, reason = "wrong email")
        }
    }
}