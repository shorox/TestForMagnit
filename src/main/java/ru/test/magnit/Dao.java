package ru.test.magnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс данных для связи с базой данных и получения запросов
 * @author Sharov V.
 */
public class Dao {

    /**
     * Коннект к базе данных
     */
    private Connection connection;

    /**
     * Объект запросов к базе данных
     */
    private Statement statement;

    public Dao(String url, String username, String password){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        }catch (ClassNotFoundException | SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод save() сохраняет объект в таблице Test базы данных
     * @param test сохраняемый объект
     * @return результатом является возвращаемый объект
     * @exception SQLException если запись прошла безуспешно, то возвращается пустое значение
     */
    public Test save(Test test) {
        try {
            statement.executeUpdate("INSERT INTO test(field) values('" + test.getField() + "')");
            return test;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Метод getAll() возвращает список объектов, содержащихся в таблице Test базы данных
     * @return возвращает список объектов
     */
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
            System.err.println(e.getMessage());
        }
        return resultList;
    }

    /**
     * Метод close() закрывает текущий коннект
     */
    public void close(){
        try {
            if (connection != null) {
                connection.close();
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
