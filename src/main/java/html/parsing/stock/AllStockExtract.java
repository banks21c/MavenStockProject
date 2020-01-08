package html.parsing.stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllStockExtract {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockExtract.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new AllStockExtract();
	}

	AllStockExtract() throws Exception {
		List<StockVO> allStockList;
		List<StockVO> kospiAllStockList;
		List<StockVO> kosdaqAllStockList;
		// 코스피,코스닥
		allStockList = StockUtil.readStockCodeNameListFromKrx("");
		System.out.println("allStockList.size :" + allStockList.size());

		// 코스피
		kospiAllStockList = StockUtil.readStockCodeNameListFromKrx("stockMkt");
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		// 코스닥
		kosdaqAllStockList = StockUtil.readStockCodeNameListFromKrx("kosdaqMkt");
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		/*
		13:상장법인
		01:관리종목
		05:불성실공시법인
		07:자산2조법인
		99:외국법인
		181:(코스닥) 우량기업부
		182:(코스닥) 벤처기업부
		183:(코스닥) 중견기업부
		184:(코스닥) 기술성장기업부
		11:KRX100
		06:KOSPI200
		09:STAR30
		10:PREMIER
		*/
		String searchTypes[] = {"13","01","05","07","99","181","182","183","184","11","06","09","10"};
//		String searchTypes[] = {"06"};
		for(String searchType : searchTypes) {
			allStockList = StockUtil.readStockCodeNameListFromKrx("",searchType);
			System.out.println(searchType+":allStockList.size :" + allStockList.size());
		}

	}

}
