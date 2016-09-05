package server

import server.database.DBMain
import java.awt.List
import java.net.BindException
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.logging.Logger

/**
 * Created by kr3v on 31.08.2016.
 * Server main class.
 * TODO: make SSL implementation of ServerSocket
 */
object Server : Runnable {

    val socketList = Collections.newSetFromMap(ConcurrentHashMap<SimpleSocket, Boolean>())
    val logger: Logger = Logger.getAnonymousLogger()
    var port: Int = 8080
    var socketServer: ServerSocket = ServerSocket(8080)

    private fun  MutableSet<SimpleSocket>.add(element: Socket?) {
        add(SimpleSocket(element!!))
    }

    override fun run() {
        if (!DBMain.isConnected) {
            DBMain.connect()
        }

        socketServer.use {
            do {
                val socket = socketServer.accept()
                if (socket != null) {
                    logger.info("accepted $socket")
                    socketList.add(socket)
                }

            } while (true)
        }
        DBMain.disconnect()

    }

}
