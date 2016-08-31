import org.junit.Assert
import org.junit.Test
import server.Server
import java.net.BindException

/**
 * Created by Dima on 31.08.2016.
 * Tests for kotlin/server.Server
 */
@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "UNUSED_VALUE")
class ServerTest : Assert() {

    @Test(expected = BindException::class)
    fun serverConstructorTest() {
        val server = Server(0)
        Server(server.port)
    }

    @Test
    fun serverRunningTest() {
        val serverA = Server(0)
        serverA.socket.close()
        Server(serverA.socket.localPort)
        serverA.run()
        assertEquals(false, serverA.running)
    }
}