package html.parsing.stock.javafx;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.FileUtil;

public class AfterHoursKosdaqFinanceDaumNetJsoupTest {

	
	private static Logger logger = LoggerFactory.getLogger(AfterHoursKosdaqFinanceDaumNetJsoupTest.class);
	public final static String USER_HOME = System.getProperty("user.home");

	final static String daumKospiAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String daumKosdaqAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSDAQ";
	final static String daumKospiAfterHoursUrlApi = "https://finance.daum.net/api/trend/after_hours_spac?page=1&perPage=30&fieldName=changeRate&order=asc&market=KOSPI&type=CHANGE_FALL&pagination=true";
	final static String daumKosdaqAfterHoursUrlApi = "https://finance.daum.net/api/trend/after_hours_spac?page=1&perPage=30&fieldName=changeRate&order=asc&market=KOSDAQ&type=CHANGE_FALL&pagination=true";

	static SimpleDateFormat sdf2 = new SimpleDateFormat("[yyyy-MM-dd_hhmmss]", Locale.KOREAN);
	static String strYmdhms = sdf2.format(new Date());

	public static void main(String[] args) throws IOException {
		String title = "kospi";
		Document doc = Jsoup.connect(daumKospiAfterHoursUrlApi).userAgent(
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36")
				.get();

		String fileName = USER_HOME + "\\documents\\" + strYmdhms + "_" + title + ".html";
		FileUtil.fileWrite(fileName, doc.html());
	}

}
