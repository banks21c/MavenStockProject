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

/**
 *
 * @author parsing-25
 */
public class JdbcConnectionExample {

    JdbcConnectionExample() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //1. 드라이버 로딩
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("1. 드라이버 로딩 성공");

            //2. DB연결
            String url = "jdbc:oracle:thin:@localhost:1521:JAVA";
            con = DriverManager.getConnection(url, "scott", "tiger");
            System.out.println("2. DB와 연결 성공 con : " + con);
            //3. Statement 객체 생성
            stmt = con.createStatement();
            System.out.println("3. stmt객체 생성 성공 stmt : " + stmt);
            //4. Statement 객체 실행
            String sql = "select * from DEPT";
            rs = stmt.executeQuery(sql);
            System.out.println("DEPTNO \tDNAME \t LOC");
            System.out.println("--------------------------");
            int count = 0;
            //5. 결과값(집합)에서 Data추출
            while (rs.next()) {
                int deptno = rs.getInt(1); //int deptno = rs.getInt("DEPTNO");
                String dname = rs.getString(2);
                String loc = rs.getString(3);
                System.out.println(deptno + "\t" + dname + "\t" + loc);
                count++;
            }
            System.out.println("총 " + count + "개 데이터 검색!!");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("드라이버가 classpath잡히지 않음");
        } catch (SQLException se) {
            System.out.println("JAVA와 oracle 연결 실패 "
                    + "or  stmt객체 생성실패 or stmt객체 실행 실패");
        } finally {
            //6. 연결 객체 닫기
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
            }
            System.out.println("연결 객체 닫기 완료");
        }
    }

    public static void main(String args[]) {
        new JdbcConnectionExample();
    }
}
