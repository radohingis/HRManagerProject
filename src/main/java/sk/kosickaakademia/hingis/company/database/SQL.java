package sk.kosickaakademia.hingis.company.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQL {

    public Connection connect() {
        try {
            Properties props = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.properties");
            props.load(inputStream);
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            Connection connection = DriverManager.getConnection(url, username, password);

            if(connection != null) {
                System.out.println("Connected to company");
                return connection;
            } else {
                System.out.println("Connection failed");
                return null;
            }

        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Connection disconnect(Connection connection) {
        try {
            if (connection != null) {
                System.out.println("Connection closed");
                connection.close();
            }
        } catch (SQLException ex) {
            System.out.println("No database for closing connection / not connected before");
            ex.printStackTrace();
        }
        return null;
    }
}
