/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestJDBC {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/benchresources";

    //  mysql database credentials
    static final String USER_NAME = "root";
    static final String PASSWORD = "";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        System.out.println("JDBC Data Interaction without Spring\n");

        // variables
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = null;
        sql = "SELECT player_id, name, age, matches FROM Player";

        try {
            // load driver and get connection
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

            // create SQL statement and fire query
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            System.out.println("ID\tName\t\t\tAge\tMatches");
            System.out.println("==\t================\t===\t=======");

            // extracts resultset and get each value
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + "\t" + resultSet.getString(2) + "\t" + resultSet.getInt(3) + "\t" + resultSet.getInt(4));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }

    }
}
