package server

import java.net.BindException
import java.net.ServerSocket
import java.util.logging.Logger

/**
 * Created by Dima on 31.08.2016.
 * Server main class. If
 */
class Server private constructor(
        var port: Int,
        var socket: ServerSocket,
        var logger: Logger,
        var running: Boolean) : Runnable {

    @Throws(BindException::class)
    constructor(port: Int) : this(
            { ->
                if (port == 0) {
                    ServerSocket(0).localPort
                } else port
            }(),
            { ->
                try {
                    ServerSocket(port)
                } catch(e: BindException) {
                    Logger.getAnonymousLogger().warning("port $port is already used")
                    throw e
                }
            }(),
            Logger.getAnonymousLogger(),
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

        if (socket.isClosed) {
            logger.warning("socket is closed, trying to open again on $port, ${toString()}")
            try {
                socket = ServerSocket(port)
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
            val socketAccept = socket.accept()
            if (socketAccept != null) {
                Thread(ClientProcessor(socketAccept)).start()
            }

        } while (socketAccept != null)

        if (!socket.isClosed) {
            socket.close()
        }
        running = false
    }

}