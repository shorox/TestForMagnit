package ru.test.magnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Класс <code>Dao</code> для связи с базой данных и получения запросов
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

    public Dao(Properties property){
        try {
            Class.forName(property.getProperty("db.driver"));
            connection = DriverManager.getConnection(
                    property.getProperty("db.url"),
                    property.getProperty("db.user"),
                    property.getProperty("db.password"));

            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM test");
            connection.setAutoCommit(false);
            System.out.println("Success connected to DB.");
        }catch (ClassNotFoundException | SQLException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод <code>commit()</code> коммитит в базу данных
     */
    public void commit(){
        try {
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Метод <code>save()</code> сохраняет объект в таблице Test базы данных
     * @param test сохраняемый объект
     */
    public Test save(Test test) {
        try {
            statement.addBatch("INSERT INTO test(field) values('" + test.getField() + "')");
            return test;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                System.err.println(e1.getMessage());
            }
            return null;
        }
    }

    /**
     * Метод <code>getAll()</code> возвращает список объектов, содержащихся в таблице Test базы данных
     */
    public List<Test> getAll() {
        List<Test> resultList = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT field FROM test ORDER BY field");
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
     * Метод <code>close()</code> закрывает текущий коннект
     */
    public void close(){
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Connect closed to DB.");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
