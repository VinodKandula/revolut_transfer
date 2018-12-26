package revoluttransfer


import revoluttransfer.routes.AppRouterResolver
import spark.Spark.port


fun main(args: Array<String>) {
    println("Start project")
    port(8888)
    if (args.isNotEmpty()) {
        if (args[0] == DB_MODE) {
            println("DB mode is on")
            AppRouterResolver().registerAppRoutes(true)
        }
    } else {
        println("local cache is on")
        AppRouterResolver().registerAppRoutes(false)
    }
}

