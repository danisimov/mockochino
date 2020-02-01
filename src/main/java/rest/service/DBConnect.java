package rest.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Created by danisimov on 1/10/20
 */
class DBConnect {

    private static Connection connection;

    private DBConnect() {
    }

    static Statement getStatement() {
        if (connection == null) {
            setConnection();
        }
        return createStatement();
    }

    private static void setConnection() {
        try {
            // create a connection to the database
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/rest/db/mockochino.db");
            Logger.getLogger(DBConnect.class.getName()).info("Connection to SQLite has been established.");
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).warning(e.getMessage());
        }
    }

    private static Statement createStatement() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (SQLException e) {
            Logger.getLogger(DBConnect.class.getName()).warning(e.getMessage());
        }
        return statement;
    }
}
