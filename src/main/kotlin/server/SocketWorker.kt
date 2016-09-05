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
        while (!Server.socketServer!!.isClosed) {
//            logger.warning("again")
            socketList.forEach(fun(socket: SimpleSocket) {
                if (!socket.used && socket.isClosed()) {
                    logger.warning("remove")
                    socketList.remove(socket)
                } else if (socket.available()) {
                    synchronized(socket.used) {
                        logger.warning("available")
                        socket.used = true
                        executorService.submit {
                            ClientProcessor(socket).run()
                            socket.used = false
                        }
                    }
                }
            })
        }
    }

}