package sk.kosickaakademia.hingis.company.database;

import sk.kosickaakademia.hingis.company.entity.User;
import sk.kosickaakademia.hingis.company.enumerator.Gender;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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

    public boolean insertNewUser(User user){
        try(Connection connection = connect()) {
            if(connection != null) {
                String INSERTQUERY = "insert into user (fname, lname, age, gender) values (?,?,?,?)";
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

    public PreparedStatement selectByGender(int gender) {
        if(gender >= 0){
            try(Connection connection = connect()) {
                String SELECTBYGENDERQUERY = "select * from user where gender = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SELECTBYGENDERQUERY);
                preparedStatement.setInt(1, gender);
                execute(preparedStatement);
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    private List<User> executeSelect(PreparedStatement preparedStatement) {
        List<User> userList = new ArrayList<>();
        int results = 0;
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet != null) {
                while(resultSet.next()) {
                    results++;
                    int id = resultSet.getInt("id");
                    String fname = resultSet.getString("fname");
                    String lname = resultSet.getString("lname");
                    int age = resultSet.getInt("age");
                    int gender = resultSet.getInt("gender");
                    userList.add(new User(id, fname, lname, age, gender));
                    System.out.println(id + " " + fname + " " + lname + " " + age + " " + gender);
                }
            } else {
                System.out.println("No users found");
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if(userList.size() != 0) {
            return userList;
        } else {
            System.out.println("No users found");
            return null;
        }
    }
}
