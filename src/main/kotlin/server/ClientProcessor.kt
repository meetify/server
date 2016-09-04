package server

import com.lambdaworks.crypto.SCryptUtil
import server.database.DBUsers
import serverModule.Response
import java.io.DataInputStream
import java.io.DataOutputStream
import java.math.BigInteger
import java.net.Socket
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

/**
 * Created by kr3v on 31.08.2016.
 * Client processor is class which used to response on client's requests.
 * TODO: implement login with AES (or similar) encryption
 */
class ClientProcessor(var socket: Socket,
                      var input: DataInputStream = DataInputStream(socket.inputStream),
                      var output: DataOutputStream = DataOutputStream(socket.outputStream)) : Runnable {

    private fun bin2hex(data: ByteArray) = String.format("%0" + data.size * 2 + "X", BigInteger(1, data))

    private fun hash(string: String): String {
        val sha512 = MessageDigest.getInstance("SHA-512")
        sha512.reset()
        return bin2hex(sha512.digest(string.toByteArray(Charset.forName("US-ASCII"))))
    }

    override fun run() {
        while (!socket.isClosed) {
            val input = input.readUTF()
            when (input) {
                "login" -> Server.logger.info(login().toString())
                "register" -> Server.logger.info(register().toString())
                "vklogin" -> Server.logger.info("")
            }
        }
    }

    fun login(): Response {
        val username = input.readUTF()
        val salt = DBUsers.getSalt(username)
        if (salt == "")
            return Response.ERROR
        output.writeUTF(salt)
        val response = DBUsers.login(username, input.readUTF())
        output.writeUTF(response.toString())
        return response
    }

    fun register(): Response {
        val salt = hash(Random().nextLong().toString())
        output.writeUTF(salt)
        val input = input.readUTF().split("#")
        val response = DBUsers.register(input[0], SCryptUtil.scrypt(input[1], 16384, 8, 1), salt)
        output.writeUTF(response.toString())
        return response
    }

    fun VKLogin(): Response {
        val userVKID = input.readInt()
        val userVKFriendList = input.readUTF().parseList()
        return Response.ERROR
    }

    fun String.parseList(): List<Int> {
        val list = ArrayList<Int>()
        return list
    }

}
