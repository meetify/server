package server.database

import com.lambdaworks.crypto.SCryptUtil
import server.Response
import server.Server
import server.database.data.User
import java.sql.ResultSet
import java.util.*

/**
 * Created by kr3v on 03.09.2016.
 * Working with users table in database, login + register works
 */

object DBUsers {
    fun register(username: String, passwordHash: String, salt: String): Response {
        try {
            if (DBMain.connection != null) {
                val statementCheck = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE userMail LIKE ?")
                statementCheck.setString(1, username)

                val result = statementCheck.executeQuery()
                if (result.next())
                    return Response.ERROR

                val statementUpdate = DBMain.connection!!.prepareStatement(
                        "INSERT INTO users(userMail, userHash, userSalt) VALUES(?,?,?)")
                statementUpdate.setString(1, username)
                statementUpdate.setString(2, passwordHash)
                statementUpdate.setString(3, salt)
                statementUpdate.executeUpdate()
                return Response.OK
            }
        } catch (e: Exception) {
        }
        return Response.ERROR
    }

    fun login(username: String, passwordHash: String): Response {
        try {
            if (DBMain.connection != null) {
                val statement = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE userMail LIKE ?")
                statement.setString(1, username)
                val response = statement.executeQuery()
                response.next()
                if (SCryptUtil.check(passwordHash, response.getString("user_hash"))) {
                    Server.logger.warning("")
                    return Response.OK
                }
            }
        } catch (e: Exception) {
        }
        return Response.ERROR
    }

    fun getSalt(username: String): String {
        try {
            if (DBMain.connection != null) {
                val statement = DBMain.connection!!.prepareStatement(
                        "SELECT * FROM users WHERE userMail LIKE ?")
                statement.setString(1, username)
                val response = statement.executeQuery()
                response.next()
                return response.getString("user_salt")
            }
        } catch (e: Exception) {
        }
        return ""
    }

    fun createUser(resultSet: ResultSet) = User(
            resultSet.getInt(1),
            resultSet.getInt(5),
            null
    )

    fun createUsers(resultSet: ResultSet = DBMain.connection!!.prepareStatement(
            "SELECT * FROM  users"
    ).executeQuery()): List<User> {
        val listUser = ArrayList<User>()
        while (resultSet.next()) {
            listUser.add(createUser(resultSet))
        }
        return listUser
    }

    fun updateDB(user: User) {
    }

    fun updateDB(users: List<User>) {
    }

}