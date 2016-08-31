package server

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

/**
 * Created by kr3v on 31.08.2016.
 * Client processor is class which used to response on client's requests.
 * TODO: implement basic parts of this class
 */
class ClientProcessor(var socket: Socket,
                      var input: DataInputStream = DataInputStream(socket.inputStream),
                      var output: DataOutputStream = DataOutputStream(socket.outputStream)) : Runnable {

    /**
     *
     */
    override fun run() {
        var string: String?
        do {
            string = input.readUTF()

        } while (string != null)
    }

}