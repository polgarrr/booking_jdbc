import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JdbcTest {
        @Test
        public void shouldGetJdbcConnection() throws SQLException {
            try(Connection connection = getNewConnection()) {
                assertTrue(connection.isValid(1));
                assertFalse(connection.isClosed());
                UUID uuid = UUID.randomUUID();
                String usersInsertQuery = "insert into booking.users (id, phone, email, first_name, last_name, middle_name) values ('" + uuid + "', '79999999999', 'test@mail.ru', 'Ivan', 'Ivanov', 'Ivanovich')";
                Statement createUser = connection.createStatement();
                createUser.executeUpdate(usersInsertQuery);
            }
        }

        private Connection getNewConnection() throws SQLException {
            String url = "jdbc:postgresql://localhost:5432/macbook";
            String user = "macbook";
            String passwd = "colidh10b";
            return DriverManager.getConnection(url, user, passwd);
        }
}
