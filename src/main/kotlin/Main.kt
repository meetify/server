import server.Server

/**
 * Created by kr3v on 30.08.2016.
 * Main file, used to launch server, etc
 */

fun main(args: Array<String>) {
    Thread(Server(8080)).start()
//    DBMain.connect()
//    println(DBUsers.getSalt("user"))
}