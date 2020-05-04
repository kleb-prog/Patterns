package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:h2:~/test;AUTO_SERVER=TRUE";
    private static final String USER = "sa";
    private static final String PASS = "";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            registerDriver();
            try {
                connection = DriverManager.getConnection(URL, USER, PASS);
            } catch (SQLException e) {
                System.err.println("Ошибка установления соединения: " + e.getMessage());
                System.err.println("Код ошибки: " + e.getErrorCode());
            }
        }
        return connection;
    }

    private static void registerDriver() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
        } catch (SQLException e) {
            System.err.println("Ошибка регистрации драйвера: " + e.getMessage());
            System.err.println("Код ошибки: " + e.getErrorCode());
        }
    }

    public static void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.err.println("Код ошибки: " + e.getErrorCode());
        }
    }
}
