import io.ktor.server.application.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.connectToPostgres(): Connection {
    Class.forName("org.postgresql.Driver")
    val url = "jdbc:postgresql://localhost/test"
    val user = "postgres"
    val password = "password"

    return DriverManager.getConnection(url, user, password)
}
