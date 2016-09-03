package server.database

import server.Server
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by kr3v on 03.09.2016.
 * Connect/disconnect to database
 */
object DBMain {
    private val name = "kr3v"
    private val password = "wrPq93D#"

    var ip = "127.0.0.1"
    var port = "5432"
    var dbname = "meetify"
    val url = "jdbc:postgresql://$ip:$port/$dbname"

    var connection: Connection? = null
    var isConnected = false

    fun connect() {
        Server.logger.info("connecting to db $url")
        if (isConnected) {
            Server.logger.warning("already connected to $url")
            return
        }
        Class.forName("org.postgresql.Driver")
        try {
            connection = DriverManager.getConnection(url, name, password)
            Server.logger.info("connected")
        } catch (ex: Exception) {
            Logger.getLogger(javaClass.name).log(Level.SEVERE, null, ex)
        }
    }

    fun disconnect() {
        if (connection != null && !connection!!.isClosed) {
            try {
                connection!!.close()
            } catch (ex: SQLException) {
                Logger.getLogger(javaClass.name).log(Level.SEVERE, null, ex)
            }
        }
    }
}