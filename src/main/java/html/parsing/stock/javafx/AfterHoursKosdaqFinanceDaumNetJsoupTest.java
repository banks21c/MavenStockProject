package html.parsing.stock.javafx;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.FileUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AfterHoursKosdaqFinanceDaumNetJsoupTest {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AfterHoursKosdaqFinanceDaumNetJsoupTest.class);

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

		String fileName = userHome + "\\documents\\" + strYmdhms + "_" + title + ".html";
		FileUtil.fileWrite(fileName, doc.html());
	}

}
