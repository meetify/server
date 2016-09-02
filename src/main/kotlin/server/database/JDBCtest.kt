package server.database

import java.sql.*
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by kr3v on 31.08.2016.
 */
class JDBCtest {

    fun main(args: Array<String>) {
        var connection: Connection? = null
        //URL к базе состоит из протокола:подпротокола://[хоста]:[порта_СУБД]/[БД] и других_сведений
        val url = "jdbc:postgresql://127.0.0.1:5432/meetify"
        //Имя пользователя БД
        val name = "kr3v"
        //Пароль
        val password = "2w3e4r5t!Q"
        try {
            //Загружаем драйвер
            Class.forName("org.postgresql.Driver")
            println("Драйвер подключен")
            //Создаём соединение
            connection = DriverManager.getConnection(url, name, password)
            println("Соединение установлено")
            //Для использования SQL запросов существуют 3 типа объектов:
            //1.Statement: используется для простых случаев без параметров
            var statement: Statement? = null

            statement = connection!!.createStatement()
            //Выполним запрос
            val result1 = statement!!.executeQuery(
                    "SELECT * FROM users where id >2 and id <10")
            //result это указатель на первую строку с выборки
            //чтобы вывести данные мы будем использовать
            //метод next() , с помощью которого переходим к следующему элементу
            println("Выводим statement")
            while (result1.next()) {
                println("Номер в выборке #" + result1.row
                        + "\t Номер в базе #" + result1.getInt("id")
                        + "\t" + result1.getString("username"))
            }
            // Вставить запись
            statement.executeUpdate(
                    "INSERT INTO users(username) values('name')")
            //Обновить запись
            statement.executeUpdate(
                    "UPDATE users SET username = 'admin' where id = 1")


            //2.PreparedStatement: предварительно компилирует запросы,
            //которые могут содержать входные параметры
            var preparedStatement: PreparedStatement? = null
            // ? - место вставки нашего значеня
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users where id > ? and id < ?")
            //Устанавливаем в нужную позицию значения определённого типа
            preparedStatement!!.setInt(1, 2)
            preparedStatement.setInt(2, 10)
            //выполняем запрос
            val result2 = preparedStatement.executeQuery()

            println("Выводим PreparedStatement")
            while (result2.next()) {
                println("Номер в выборке #" + result2.row
                        + "\t Номер в базе #" + result2.getInt("id")
                        + "\t" + result2.getString("username"))
            }

            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users(username) values(?)")
            preparedStatement!!.setString(1, "user_name")
            //метод принимает значение без параметров
            //темже способом можно сделать и UPDATE
            preparedStatement.executeUpdate()


            //3.CallableStatement: используется для вызова хранимых функций,
            // которые могут содержать входные и выходные параметры
            var callableStatement: CallableStatement? = null
            //Вызываем функцию myFunc (хранится в БД)
            callableStatement = connection.prepareCall(
                    " { call myfunc(?,?) } ")
            //Задаём входные параметры
            callableStatement!!.setString(1, "Dima")
            callableStatement.setString(2, "Alex")
            //Выполняем запрос
            val result3 = callableStatement.executeQuery()
            //Если CallableStatement возвращает несколько объектов ResultSet,
            //то нужно выводить данные в цикле с помощью метода next
            //у меня функция возвращает один объект
            result3.next()
            println(result3.getString("MESSAGE"))
            //если функция вставляет или обновляет, то используется метод executeUpdate()

        } catch (ex: Exception) {
            //выводим наиболее значимые сообщения
            Logger.getLogger(JDBCtest::class.java.name).log(Level.SEVERE, null, ex)
        } finally {
            if (connection != null) {
                try {
                    connection.close()
                } catch (ex: SQLException) {
                    Logger.getLogger(JDBCtest::class.java.name).log(Level.SEVERE, null, ex)
                }

            }
        }

    }
}