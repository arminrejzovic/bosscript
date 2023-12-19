package interpreter.packages.sql

import interpreter.Environment
import interpreter.values.NativeFunction
import interpreter.values.Null
import java.sql.DriverManager


val sql = Environment(variables = hashMapOf(
    "objekatIzJSON" to NativeFunction(""){ args ->
        Null()
    },
    "JSONTekst" to NativeFunction(""){ args ->
        Null()
    }
))

fun main() {
    val connection = DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/bosscript?schema=boss",
        "postgres",
        "dinder"
    )

    val statement = connection.createStatement()
    statement.executeUpdate("INSERT INTO boss.t_users (first_name, last_name, username) values ('Dinder', 'Debeli', 'dinder')")
    val results = statement.executeQuery("SELECT * FROM boss.t_users")

    val obj = arrayListOf<HashMap<String, Any>>()
    var objArrayIndex = 0

    while (results.next()) {
        val map = HashMap<String, Any>()
        for (i in 1..results.metaData.columnCount){
            val columnName = results.metaData.getColumnName(i)
            val data = results.getObject(i)
            map[columnName] = data
        }
        obj.add(map)
        objArrayIndex++
    }
    println(obj)

    // print all columns
}