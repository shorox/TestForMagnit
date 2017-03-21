package ru.test.magnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao {

    private Connection connection;
    private Statement statement;

    public Dao(String url, String username, String password){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }catch (ClassNotFoundException | SQLException e){
        }
    }

    public Test save(Test test) {
        try {
            statement.executeUpdate("INSERT INTO test(field) values('" + test.getField() + "')");
            return test;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Test> getAll() {
        List<Test> resultList = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT * FROM test");
            while (result.next()) {
                Test test = new Test();
                test.setField(result.getInt("field"));
                resultList.add(test);
            }
        }catch (SQLException e){
        }
        return resultList;
    }

    public void close(){
        try {
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e){
        }
    }
}
