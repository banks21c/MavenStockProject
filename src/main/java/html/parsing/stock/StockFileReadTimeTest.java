package html.parsing.stock;

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
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockFileReadTimeTest {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger1 = java.util.logging.Logger.getLogger("StockSort");
    private static final Logger logger2 = LoggerFactory.getLogger(StockFileReadTimeTest.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    private String kospiFileName = "new_kospi_우선주제외.html";
    private String kosdaqFileName = "new_kosdaq_우선주제외.html";

    List<StockVO> allStockList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockFileReadTimeTest(1);
    }

    StockFileReadTimeTest(int i) {
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        Properties props = new Properties();
        try {
            // InputStream is = new FileInputStream("log4j.properties");
            // props.load(new FileInputStream("log4j.properties"));
            // InputStream is = getClass().getResourceAsStream("/log4j.properties");
            props.load(getClass().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);

        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("log4j.properties");
        // PropertyConfigurator.configure(url);
        File log4jfile = new File("log4j.properties");
        String absolutePath = log4jfile.getAbsolutePath();
        PropertyConfigurator.configure(absolutePath);

        long startTime = System.currentTimeMillis();
        // 모든 주식 정보를 조회한다.
        // 코스피
        getAllStockList("코스피", kospiFileName);
        // 코스닥
        getAllStockList("코스닥", kosdaqFileName);
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;
        System.out.println("총 소요시간 :" + (timeElapsed) + "밀리초");

    }

    public void getAllStockList(String kospidaq, String fileName) {
        long startTime = System.currentTimeMillis();
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                StockVO stock = new StockVO();
                cnt++;
                System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];
                stock.setStockCode(stockCode);
                stock.setStockName(stockName);
                stock.setStockNameLength(stockName.length());

                if (stockCode.length() != 6) {
                    continue;
                }
                allStockList.add(stock);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            System.out.println("소요시간 :" + (timeElapsed) + "밀리초");
        }
    }
}
