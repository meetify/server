import server.Server

/**
 * Created by Dima on 30.08.2016.
 * Main file, used to launch server, etc
 */

fun main(args: Array<String>) {
    val server = Server(8080)
    server.socket.close()
    Thread(server).start()
    Thread(server).start() //will not launch second Thread due to using the same instance of Server
}