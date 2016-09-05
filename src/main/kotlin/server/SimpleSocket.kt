package server

import serverModule.CustomSocket
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.Socket
import javax.xml.crypto.Data

/**
 * Created by kr3v on 05.09.2016.
 */

class SimpleSocket(socket: Socket) : CustomSocket(socket){

    var used = false

    override fun run() = Unit

    override fun read(fn: () -> Any)= fn()

    override fun write(fn: (Any) -> Unit, any: Any) = fn(any)

    override fun update() = Unit

    fun isClosed() = socket.isClosed

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as SimpleSocket

        if (socket != other.socket) return false

        return true
    }

    override fun hashCode(): Int {
        return socket.hashCode()
    }
}