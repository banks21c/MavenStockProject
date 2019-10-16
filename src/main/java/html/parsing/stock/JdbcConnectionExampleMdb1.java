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

public class JdbcConnectionExampleMdb1 {

    Connection con;
    Statement st;
    ResultSet rs;
    String db;

    public JdbcConnectionExampleMdb1() throws InstantiationException, IllegalAccessException {
        try {
//            String path = new java.io.File("C:\\eclipse-jee-oxygen-workspace\\StockProject\\src\\html\\parsing\\stock\\stock.accdb").getAbsolutePath();
            String path = new java.io.File("C:\\eclipse-jee-oxygen-workspace\\StockProject\\src\\html\\parsing\\stock\\stock.mdb").getAbsolutePath();
            System.out.println("path:" + path);
//            db = "JDBC:ODBC:Driver=Microsoft Access Driver (*.mdb, *.accdb); DBQ=" + path;
//            db = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + path;
            db = "jdbc:ucanaccess://" + path;
            doConnection();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    public void doConnection() throws InstantiationException, IllegalAccessException {
        try {
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
//            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            //con = DriverManager.getConnection(db, "", "");
            con = DriverManager.getConnection(db);
            st = con.createStatement();
            rs = st.executeQuery("select * from KOSPI");
            while (rs.next()) {
                //System.out.println(rs.getObject(1));
                System.out.println(rs.getObject("회사명") + " " + rs.getObject("종목코드"));
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("드라이버가 classpath 잡히지 않음");
        } catch (SQLException se) {
            System.out.println("JAVA와 oracle 연결 실패 "
                    + "or  stmt객체 생성실패 or stmt객체 실행 실패");
            se.printStackTrace();
        } finally {
            //6. 연결 객체 닫기
            try {
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
            System.out.println("연결 객체 닫기 완료");
        }

    }

    public static void main(String... argS) throws InstantiationException, IllegalAccessException {
        new JdbcConnectionExampleMdb1();
    }
}
