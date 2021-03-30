package sk.kosickaakademia.hingis.company.database;

import sk.kosickaakademia.hingis.company.entity.User;

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
            System.out.println(username);
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
                String INSERTQUERY = "insert into user (fname, lname, age, gender) values (?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(INSERTQUERY);
                preparedStatement.setString(1, user.getFname());
                preparedStatement.setString(2, user.getLname());
                preparedStatement.setInt(3, user.getAge());
                preparedStatement.setInt(4, user.getGender().getValue());
                int queryAffected = preparedStatement.executeUpdate();
                return queryAffected == 1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    
    public boolean updateUser(int id, String column, String value) {
            String UPDATEUSERQUERY;

            if(!(column.equalsIgnoreCase("fname")
            || column.equalsIgnoreCase("lname")
            || column.equalsIgnoreCase("age")
            || column.equalsIgnoreCase("gender"))) {

                System.out.println("column doesn't exist");
                return false;
            }

            if(     column.equalsIgnoreCase("age")
                    || column.equalsIgnoreCase("gender")) {

                    int numberValue = Integer.parseInt(value);
                    UPDATEUSERQUERY = "update user set " + column + "=" + numberValue + " where id = ?";

            } else {

                    UPDATEUSERQUERY = "update user set " + column + "=\'" + value.toLowerCase() + "\' where id = ?";

            }
            if(id < 1 || column.isEmpty() || value.isEmpty()) return false;

            if(column.equalsIgnoreCase("age")
                    && (Integer.parseInt(value) < 1
                    || Integer.parseInt(value) > 99)) {

                System.out.println("out of boundaries");
                return false;
            }

            if(column.equalsIgnoreCase("gender")
                    && (Integer.parseInt(value) < 0
                    || Integer.parseInt(value) > 2)) {

                System.out.println("out of boundaries");
                return false;
            }

            else {
                try(Connection connection = connect()) {
                    PreparedStatement preparedStatement
                            = connection
                            .prepareStatement(UPDATEUSERQUERY);

                    preparedStatement.setInt(1, id);
                    System.out.println(preparedStatement);
                    preparedStatement.executeUpdate();
                    return true;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        return false;
    }

    public boolean deleteUser(int id) {
        try(Connection connection = connect()) {
            String DELETEUSERQUERY = "delete from user where id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(DELETEUSERQUERY);

            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();

            return result == 1 ? true : false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public List<User> selectByGender(int gender) {
        if(gender >= 0){
            try(Connection connection = connect()) {
                String SELECTBYGENDERQUERY = "select * from user where gender = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(SELECTBYGENDERQUERY);
                preparedStatement.setInt(1, gender);
                return executeSelect(preparedStatement);
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public List<User> selectRangeBasedOnUserAge(int from, int to) {
        if(from > to) {
            System.out.println("Input should be ascending");
            return null;
        } else {
            try(Connection connection = connect()) {
                String AGERANGESELECTIONQUERY = "select * from user where age between ? and ?";
                PreparedStatement preparedStatement = connection.prepareStatement(AGERANGESELECTIONQUERY);
                preparedStatement.setInt(1, from);
                preparedStatement.setInt(2, to);
                return executeSelect(preparedStatement);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public User getUserById(int id) {
        String GETUSERBYIDQUERY = "select * from user where id = ?";
        try(Connection connection = connect()){
                PreparedStatement preparedStatement = connection.prepareStatement(GETUSERBYIDQUERY);
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    String fname = resultSet.getString("fname");
                    String lname = resultSet.getString("lname");
                    int age = resultSet.getInt("age");
                    int gender = resultSet.getInt("gender");
                    User user = new User(id, fname, lname, age, gender);
                    user.stringify();
                    return user;
                } else {
                    System.out.println("User not found");
                    return null;
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        String GETALLSUERSQUERY = "select * from user";
        try(Connection connection = connect()) {
                PreparedStatement preparedStatement = connection.prepareStatement(GETALLSUERSQUERY);
                return executeSelect(preparedStatement);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<User> getUsersByPattern(String pattern) {
        String GETUSERBYPATTERNQUERY = "select * from user where fname like ? or lname like ?";
        if(pattern.equals("")) {
            System.out.println("Pattern required");
            return null;
        } else {
            try(Connection connection = connect()) {
                PreparedStatement preparedStatement = connection.prepareStatement(GETUSERBYPATTERNQUERY);
                preparedStatement.setString(1, "%" + pattern + "%");
                preparedStatement.setString(2, "%" + pattern + "%");
                return executeSelect(preparedStatement);
            } catch (SQLException ex){
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
                    User user = new User(id, fname, lname, age, gender);
                    userList.add(user);
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
