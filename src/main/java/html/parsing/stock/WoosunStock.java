package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WoosunStock {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(WoosunStock.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new WoosunStock(1);
    }

    WoosunStock() {

    }

    WoosunStock(int i) {

        

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);

    }

    public List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                cnt++;
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];
                if (stockName.indexOf("우") != -1) {
                    System.out.print("이름에 우짜:" + stockName);
                    if (stockName.length() - 1 == stockName.lastIndexOf("우")) {
                        System.out.print("마지막 글짜 우:" + stockName);
                        if (!stockName.equals("미래에셋대우") && !stockName.equals("포스코대우") && !stockName.equals("동우")
                                && !stockName.equals("연우")) {
                            System.out.println("\t우선주:" + stockName);
                        }
                    }
                    if (stockName.length() - 1 == stockName.lastIndexOf("B")) {
                        System.out.println("\t우선주:" + stockName);
                    }
                    System.out.println("");
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
        return stocks;
    }

}
