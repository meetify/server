import server.Server
import server.SocketWorker
import serverModule.ServerConnect
import serverModule.listeners.ConnectListener

/**
 * Created by kr3v on 30.08.2016.
 * Main file, used to launch server, etc
 */

fun main(args: Array<String>) {
    Thread(Server).start()
    Thread(SocketWorker).start()
    val a = ServerConnect()
    a.connectListener = object : ConnectListener {
        override fun onDone() {
            println("done")
        }

        override fun onError() {
            println("error")
        }
    }
    a.register("user", "pass")
}