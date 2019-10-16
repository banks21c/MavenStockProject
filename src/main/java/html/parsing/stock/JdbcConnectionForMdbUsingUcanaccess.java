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

public class JdbcConnectionForMdbUsingUcanaccess {

    Connection con;
    Statement st;
    ResultSet rs;
    String db = "jdbc:ucanaccess://C:/eclipse-jee-oxygen-workspace/StockProject/src/html/parsing/stock/stock.accdb;memory=false";

    public JdbcConnectionForMdbUsingUcanaccess() throws InstantiationException, IllegalAccessException {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            //con = DriverManager.getConnection("jdbc:ucanaccess://C:/eclipse-jee-oxygen-workspace/StockProject/src/html/parsing/stock/stock.mdb", "", "");
            //con = DriverManager.getConnection("jdbc:ucanaccess://C:/eclipse-jee-oxygen-workspace/StockProject/src/html/parsing/stock/stock.accdb;memory=false");
            //con = DriverManager.getConnection("jdbc:ucanaccess://C:/eclipse-jee-oxygen-workspace/StockProject/src/html/parsing/stock/stock.mdb");
            //con = DriverManager.getConnection(db, "", "");
            con = DriverManager.getConnection(db);
            System.out.println("con:" + con);
            st = con.createStatement();
            System.out.println("st:" + st);
            rs = st.executeQuery("select company,stock_code from KOSPI");
            System.out.println("rs:" + rs);
            while (rs.next()) {
                System.out.println(rs.getObject("company") + " " + rs.getObject("stock_code"));
//                System.out.println(rs.getObject("회사명")+" "+rs.getObject("종목코드"));
            }
        } catch (ClassNotFoundException cnfe) {
            System.out.println("드라이버가 classpath 잡히지 않음");
        } catch (SQLException se) {
            System.out.println("JAVA와 oracle 연결 실패 "
                    + "or  stmt객체 생성실패 or stmt객체 실행 실패");
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
        new JdbcConnectionForMdbUsingUcanaccess();
    }
}
