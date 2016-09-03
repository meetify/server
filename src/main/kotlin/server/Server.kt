package server

import server.database.DBMain
import java.net.BindException
import java.net.ServerSocket
import java.util.logging.Logger

/**
 * Created by kr3v on 31.08.2016.
 * Server main class.
 * TODO: make SSL implementation of ServerSocket
 */
class Server private constructor(
        var port: Int,
        var socketServer: ServerSocket,
        var running: Boolean) : Runnable {

    companion object {
        val logger: Logger = Logger.getAnonymousLogger()
    }

    @Throws(BindException::class)
    constructor(port: Int) : this(
            { ->
                if (port == 0) {
                    val socketTemp = ServerSocket(0)
                    socketTemp.use {
                        socketTemp.localPort
                    }
                } else port
            }(),
            { ->
                try {
                    ServerSocket(port)
                } catch(e: BindException) {
                    logger.warning("port $port is already used")
                    throw e
                }
            }(),
            false)

    override fun run() {
        synchronized(running) {
            if (!running) {
                running = true
            } else {
                logger.warning("this server running not in this Server.run, finishing, ${toString()}")
                return
            }
        }

        if (!DBMain.isConnected) {
            DBMain.connect()
        }
        if (socketServer.isClosed) {
            logger.warning("socketServer is closed, trying to open again on $port, ${toString()}")
            try {
                socketServer = ServerSocket(port)
            } catch(e: BindException) {
                /**
                 * TODO: info about BindException in Server.run, if 'running' condition isn't enough
                 */
                logger.warning("port $port is already used, finishing server, ${toString()}")
                running = false
                return
            }
        }

        do {
            val socket = socketServer.accept()
            if (socket != null) {
                logger.info("accepted $socket")
                Thread(ClientProcessor(socket)).start()
            }

        } while (socket != null && !socketServer.isClosed)

        socketServer.use { }
        running = false
        DBMain.disconnect()
    }

}