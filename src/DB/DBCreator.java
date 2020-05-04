package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCreator {

    public static void createTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS money_flow( uuid UUID PRIMARY KEY, amount double precision, description varchar(64), date DATE, type varchar(64));";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dropTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE IF EXISTS money_flow;";
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
