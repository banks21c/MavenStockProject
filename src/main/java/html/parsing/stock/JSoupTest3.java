package html.parsing.stock;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupTest3 {

	
	private static Logger logger = LoggerFactory.getLogger(JSoupTest3.class);

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
		new JSoupTest3();
	}

	JSoupTest3() {

		readNews();
	}

	public void readNews() {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			// 종합정보
			String url = "https://static.nid.naver.com/getLoginStatus.nhn?callback=showGNB&charset=utf-8&svc=admin.blog&template=gnb_utf8&one_naver=0";
			System.out.println(url);

//			Connection.Response res = Jsoup.connect(url).timeout(10 * 1000).execute();
			Connection.Response res = Jsoup.connect(url).ignoreContentType(true)
				.timeout(3000)
				.header("Origin", "https://m.stock.naver.com/")
				.header("Referer", "https://m.stock.naver.com/")
				.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//				.header("Content-Type", "application/x-www-form-urlencoded")
//				.header("Content-Type", "text/*")
//				.header("Content-Type", "application/xml")
//				.header("Content-Type", "text/*, application/xml")
//				.header("Content-Type", "application/xhtml+xml")
//				.header("Content-Type", "text/*, application/xml")
//				.header("Content-Type", "text/*, application/xhtml+xml")
//				.header("Mime-Type", "application/json;charset=UTF-8")
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
