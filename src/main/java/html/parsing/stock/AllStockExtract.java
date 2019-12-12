package html.parsing.stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AllStockExtract {

	final static String userHome = System.getProperty("user.home");
	java.util.logging.Logger logger = null;

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
		List<StockVO> kospiAllStockList;
		List<StockVO> kosdaqAllStockList;
		// 코스피
		kospiAllStockList = StockUtil.readStockCodeNameListFromKrx("stockMkt");
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());
		System.out.println("kospiAllStockList :" + kospiAllStockList);

		// 코스닥
		kosdaqAllStockList = StockUtil.readStockCodeNameListFromKrx("kosdaqMkt");
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());
		System.out.println("kosdaqAllStockList :" + kosdaqAllStockList);
	}

}
