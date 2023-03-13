import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")
    val url = "jdbc:postgresql://127.0.1:5432/postgres"
    val user = "test"
    val password = "test"

    return DriverManager.getConnection(url, user, password)
}
