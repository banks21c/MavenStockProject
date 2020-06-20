package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FindStock1 {

    final static String userHome = System.getProperty("user.home");
    private static final Logger logger = LoggerFactory.getLogger(FindStock1.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;

    List<StockVO> kospiStockList = new ArrayList<>();
    List<StockVO> kosdaqStockList = new ArrayList<>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        FindStock1 findStock = new FindStock1();
    }

    FindStock1() {

        // 모든 주식 정보를 조회한다.
        kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
        kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
        logger.debug("kospiStockList.size :" + kospiStockList.size());
        logger.debug("kosdaqStockList.size :" + kosdaqStockList.size());
        logger.debug("____________________________________________");

        // 코스피
        for (int j = 0; j < kospiStockList.size(); j++) {
            StockVO svo = kospiStockList.get(j);
            String stockCode = svo.getStockCode();
            String stockName = svo.getStockName();
            if (stockCode.endsWith("40") && stockName.contains("에")) {
                logger.debug("kospi========>"+stockName+"("+stockCode+")");
            }
        }
        // 코스닥
        for (int j = 0; j < kosdaqStockList.size(); j++) {
            StockVO svo = kosdaqStockList.get(j);
            String stockCode = svo.getStockCode();
            String stockName = svo.getStockName();
            if (stockCode.endsWith("40") && stockName.contains("에")) {
                logger.debug("kosdaq========>"+stockName+"("+stockCode+")");
            }
        }
        logger.debug("____________________________________________");
    }
}
