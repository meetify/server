import server.Server
import server.database.DBMain
import server.database.DBUsers

/**
 * Created by kr3v on 30.08.2016.
 * Main file, used to launch server, etc
 */

fun main(args: Array<String>) {
    DBMain.connect()
    DBUsers.register("user", "pass", "salt")
    DBUsers.register("user1", "pass1", "salt1")
    DBUsers.register("user2", "pass2", "salt2")
    println(DBUsers.createUsers())
}