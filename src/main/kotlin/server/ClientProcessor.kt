package server

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

/**
 * Created by Dima on 31.08.2016.
 * Client processor is class which used to response on client's requests.
 * TODO: implement basic parts of this class
 */
class ClientProcessor(var socket: Socket,
                      var input: DataInputStream = DataInputStream(socket.inputStream),
                      var output: DataOutputStream = DataOutputStream(socket.outputStream)) : Runnable {

    override fun run() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}