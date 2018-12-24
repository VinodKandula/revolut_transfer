package revoluttransfer

const val V1 = "/v1/"

const val ACCOUNTS_PATH = V1 + "accounts"
const val TRANSFER_PATH = V1 + "transfer"

const val EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$"
const val TRANSACTIONS_RETRY = 5