package server

import server.Server.logger
import server.Server.socketList
import java.util.concurrent.Executors

/**
 * Created by kr3v on 05.09.2016.
 */
object SocketWorker : Runnable {

    val executorService = Executors.newCachedThreadPool()!!

    override fun run() {
        while (!Server.socketServer.isClosed) {
            socketList.forEach(fun(socket: SimpleSocket) {
                if (!socket.used && socket.isClosed()) {
                    logger.warning("remove")
                    socketList.remove(socket)
                    return
                }
                synchronized(socket.used) {
                    if (socket.used) {
                        return
                    }
                    socket.used = true
                }

                if (socket.available() && socket.used) {
                    executorService.submit {
                        ClientProcessor(socket).run()
                        socket.used = false
                    }
                } else {
                    socket.used = false
                }
            })
        }
    }
}