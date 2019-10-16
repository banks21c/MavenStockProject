/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author parsing-25
 */
public class JdbcConnection2Openpop {

    JdbcConnection2Openpop() {
        Connection con = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 1. 드라이버 로딩
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("1. 드라이버 로딩 성공");

            // 2. DB연결
            // String url = "jdbc:oracle:thin:@106.10.45.204:1521:OPENPOP";
            String url = "jdbc:oracle:thin:@115.68.102.154:1521:OPENPOP";
            con = DriverManager.getConnection(url, "openpop", "!madeopen6165");
            System.out.println("2. DB와 연결 성공 con : " + con);
            // 3. Statement 객체 생성
            stmt = con.createStatement();
            System.out.println("3. stmt객체 생성 성공 stmt : " + stmt);
            // 4. Statement 객체 실행
            String sql = "SELECT * FROM DBIO_LOAD_SQL WHERE SQL_TEXT LIKE '%'||'한글'||'%'";
//            String sql = "SELECT * FROM DBIO_LOAD_SQL WHERE INSTR(SQL_TEXT,'한글') > 0";
//            String sql = "SELECT * FROM DBIO_LOAD_SQL";
            rs = stmt.executeQuery(sql);
            System.out.println("FILE_NO \tQUERY_SEQ \tSQL_TEXT \tREG_DT");
            System.out.println("--------------------------");
            int count = 0;
            // 5. 결과값(집합)에서 Data추출
            String file_no = null;
            String sql_text = null;
            while (rs.next()) {
                file_no = rs.getString(1);
                String query_seq = rs.getString(2);
                sql_text = rs.getString(3);
                String reg_dt = rs.getString(4);
                System.out.println(file_no + "\t" + query_seq + "\t" + sql_text + "\t" + reg_dt);
                count++;
            }

            String insertSql = "insert into DBIO_LOAD_SQL(FILE_NO,QUERY_SEQ,SQL_TEXT,REG_DT) values(?,(select max(query_seq)+1 from dbio_load_sql where file_no=?),?,sysdate)";
            pstmt = con.prepareStatement(insertSql);

            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                pstmt.setString(1, file_no);
                pstmt.setString(2, file_no);
                pstmt.setString(3, sql_text);
                pstmt.executeUpdate();
                String currentTime = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new java.util.Date());
                System.out.println("i:" + i + " 현재시각:" + currentTime);
                if (i % 10 == 0) {
                    con.commit();
                }
            }
            con.commit();
            long finishedTime = System.currentTimeMillis();
            System.out.println("실행 시간 : " + (finishedTime - startTime) / 1000.0 / 60 / 60 + "시간"
                    + (finishedTime - startTime) / 1000.0 / 60 + "분" + (finishedTime - startTime) / 1000.0 + "초");
            //System.out.println("총 " + count + "개 데이터 검색!!");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("드라이버가 classpath잡히지 않음");
        } catch (SQLException se) {
            System.out.println("JAVA와 oracle 연결 실패 " + "or  stmt객체 생성실패 or stmt객체 실행 실패");
            se.printStackTrace();
        } finally {
            // 6. 연결 객체 닫기
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (pstmt != null) {
                    pstmt.close();
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
        new JdbcConnection2Openpop();
    }
}
