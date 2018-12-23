package revoluttransfer


import revoluttransfer.routes.AppRouterResolver
import spark.Spark.port


fun main(args: Array<String>) {
    println("Start project")
    port(8888)
    AppRouterResolver().registerAppRoutes()
}

