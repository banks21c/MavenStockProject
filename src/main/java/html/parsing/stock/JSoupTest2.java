package html.parsing.stock;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupTest2 {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(JSoupTest2.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JSoupTest2();
	}

	JSoupTest2() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			// 종합정보
			String url = "https://finance.daum.net/api/quotes/sectors?market=KOSPI";

			System.out.println(url);

			Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
			.get();

			System.out.println(doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
