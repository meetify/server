package server.database

import com.lambdaworks.crypto.SCryptUtil
import server.Response
import server.Server

/**
 * Created by kr3v on 03.09.2016.
 * Working with users table in database, login + register works
 */

object DBUsers {

    fun register(username: String, passwordHash: String, salt: String): Response {

        try {
            if (DBMain.connection != null) {
                val statementCheck = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE user_name LIKE ?")
                statementCheck.setString(1, username)

                val result = statementCheck.executeQuery()
                if (result.next())
                    return Response.ERROR

                val statementUpdate = DBMain.connection!!.prepareStatement(
                        "INSERT INTO users(user_name, user_hash, user_salt) VALUES(?,?,?)")
                statementUpdate.setString(1, username)
                statementUpdate.setString(2, passwordHash)
                statementUpdate.setString(3, salt)
                statementUpdate.executeUpdate()
                return Response.OK
            }
        } catch (e: Exception) {
            return Response.ERROR
        }
        return Response.ERROR
    }

    fun login(username: String, passwordHash: String): Response {
        try {
            if (DBMain.connection != null) {
                val statement = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE user_name LIKE ?")
                statement.setString(1, username)
                val response = statement.executeQuery()
                response.next()
                if (SCryptUtil.check(passwordHash, response.getString("user_hash"))) {
                    Server.logger.warning("")
                    return Response.OK
                }
                return Response.ERROR
            }
        } catch (e: Exception) {
            return Response.ERROR
        }
        return Response.ERROR
    }

    fun getSalt(username: String): String {
        try {
            if (DBMain.connection != null) {
                val statement = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE user_name LIKE ?")
                statement.setString(1, username)
                val response = statement.executeQuery()
                response.next()
                return response.getString("user_salt")
//                return ans
            }
        } catch (e: Exception) {
            return ""
        }
        return ""
    }
}