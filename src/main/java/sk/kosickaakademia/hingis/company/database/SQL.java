package sk.kosickaakademia.hingis.company.database;

import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.enumerator.Gender;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class SQL {

    private final String INSERTQUERY = "insert into user (fname, lname, age, gender) values (?,?,?,?)";

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

    public boolean insertNewUser(User user){
        try(Connection connection = connect()) {
            if(connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERTQUERY);
                preparedStatement.setString(1, user.getFname());
                preparedStatement.setString(2, user.getLname());
                preparedStatement.setInt(3, user.getAge());
                preparedStatement.setInt(4, user.getGender().getValue());
                int queryAffected = preparedStatement.executeUpdate();
                return queryAffected == 1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
