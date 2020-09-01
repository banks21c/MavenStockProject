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

public class JSoupTest1 {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(JSoupTest1.class);

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
		new JSoupTest1();
	}

	JSoupTest1() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			// 종합정보
			String url = "https://m.stock.naver.com/api/item/getTrendList.nhn?code=005930&size=10&bizdate=20200805";
			System.out.println(url);

//			Connection.Response res = Jsoup.connect(url).timeout(10 * 1000).execute();
			Connection.Response res = Jsoup.connect(url)
				.timeout(3000)
				.header("Origin", "https://m.stock.naver.com/")
				.header("Referer", "https://m.stock.naver.com/")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
				.header("Content-Type", "application/x-www-form-urlencoded")
//				.header("Content-Type", "text/*")
//				.header("Content-Type", "application/xml")
//				.header("Content-Type", "text/*, application/xml")
//				.header("Content-Type", "application/xhtml+xml")
//				.header("Content-Type", "text/*, application/xhtml+xml")
				.header("Mime-Type", "application/json;charset=UTF-8")
				.header("Accept-Encoding", "gzip, deflate, br")
				.header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
				.method(Connection.Method.GET)
				.execute();
			String contentType = res.contentType();
			System.out.println(contentType);

			String body = res.body();
			System.out.println(body);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
