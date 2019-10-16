package html.parsing.stock;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author parsing-25
 */
public class JdbcConnectionExample1 {

    JdbcConnectionExample1() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //1. 드라이버 로딩
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("1. 드라이버 로딩 성공");

            //2. DB연결
            String url = "jdbc:oracle:thin:@52.79.164.101:1521:spop";
            con = DriverManager.getConnection(url, "spop", "madeopen");
            System.out.println("2. DB와 연결 성공 con : " + con);
            //3. Statement 객체 생성
            stmt = con.createStatement();
            System.out.println("3. stmt객체 생성 성공 stmt : " + stmt);
            //4. Statement 객체 실행
            String sql = "WITH SQLS AS (SELECT DBID FROM SCHEDULER_EXCEPTION WHERE ( ( ( SYSDATE BETWEEN EXCEPT_START_DT AND EXCEPT_END_DT ) AND ( EXCEPT_RPT_FREQ_DIV_CD NOT IN ( '1', '2', '3' ) ) ) OR ( EXCEPT_RPT_YN = 'Y' AND ( TO_CHAR(SYSDATE, 'yyyymmdd') BETWEEN EXCEPT_RPT_TERM_START_DAY AND EXCEPT_RPT_TERM_END_DAY ) AND ( ( EXCEPT_RPT_CYCLE IN (SELECT TO_CHAR(SYSDATE, 'dy') AS DY FROM DUAL) AND EXCEPT_RPT_FREQ_DIV_CD = '2' ) OR ( EXCEPT_RPT_FREQ_DIV_CD = '3' AND EXCEPT_RPT_STD_DIV_CD = '1' AND EXCEPT_RPT_CYCLE = TO_CHAR(SYSDATE, 'dd' ) ) OR ( ( SYSDATE BETWEEN EXCEPT_START_DT AND EXCEPT_END_DT ) AND EXCEPT_RPT_FREQ_DIV_CD = '3' AND EXCEPT_RPT_YN = 'Y' ) OR ( TO_NUMBER(TO_CHAR(SYSDATE, 'HH')) BETWEEN EXCEPT_RPT_START_TIME AND EXCEPT_RPT_END_TIME AND EXCEPT_RPT_FREQ_DIV_CD = '1' ) ) ) )) SELECT C.DB_NAME SID, A.DB_USER_ID USERID, A.DB_USER_PASSWORD PASSWD, A.DB_CONNECT_IP HOST, A.DB_CONNECT_PORT PORT, A.WRKJOB_CD FROM APM_CONNECTION A, WRKJOB_CD B, DATABASE C WHERE A.APM_OPERATE_TYPE_CD = 1 AND A.WRKJOB_CD = B.WRKJOB_CD AND B.DBID = C.DBID AND C.USE_YN = 'Y' AND C.DBID NOT IN (SELECT DBID FROM SQLS) ";
            rs = stmt.executeQuery(sql);

            System.out.println("SID \t USERID \t PASSWD \t HOST \t PORT \t WRKJOB_CD");

            System.out.println("--------------------------");
            int count = 0;
            //5. 결과값(집합)에서 Data추출
            while (rs.next()) {
                String SID = rs.getString(1);
                String USERID = rs.getString(2);
                String PASSWD = rs.getString(3);
                String HOST = rs.getString(4);
                String PORT = rs.getString(5);
                String WRKJOB_CD = rs.getString(6);
                System.out.println(SID + "\t" + USERID + "\t" + PASSWD + "\t" + HOST + "\t" + PORT + "\t" + WRKJOB_CD);
                count++;
            }
            System.out.println("총 " + count + "개 데이터 검색!!");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("드라이버가 classpath잡히지 않음");
            cnfe.printStackTrace();

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
        new JdbcConnectionExample1();
    }
}
