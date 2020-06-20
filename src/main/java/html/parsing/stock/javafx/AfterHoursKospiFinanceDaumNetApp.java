package html.parsing.stock.javafx;

import html.parsing.stock.util.FileUtil;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfterHoursKospiFinanceDaumNetApp extends Application {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AfterHoursKospiFinanceDaumNetApp.class);

	WebEngine webengine = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("코스피 시간외단일가");

		WebView webView = new WebView();
		webengine = webView.getEngine();

		webengine.load("https://finance.daum.net/domestic/after_hours?market=KOSPI");

		Button button1 = new Button("Save");
		button1.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveHtml(html, "코스피 시간외 단일가");
					};

				});

		VBox vBox = new VBox(webView, button1);
		Scene scene = new Scene(vBox, 960, 600);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void saveHtml(String contentHtml, String title) {
		String strUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
		String strYmd = sdf.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("[yyyy-MM-dd_hhmmss]", Locale.KOREAN);
		String strYmdhms = sdf2.format(new Date());

		sb.append("<h1>").append(strYmd).append("_").append(title).append("</h1>");
		try {
			URL url = new URL(strUrl);
			String strProtocol = url.getProtocol();
			String strHost = url.getHost();

			contentHtml = contentHtml.replace("\"//", "\"" + strProtocol + "://");
			contentHtml = contentHtml.replace("\"/", "\"" + strProtocol + "://" + strHost + "/");

			contentHtml = contentHtml.replace("\'//", "\'" + strProtocol + "://");
			contentHtml = contentHtml.replace("\'/", "\'" + strProtocol + "://" + strHost + "/");
			sb.append(contentHtml);

			Document doc = Jsoup.parse(sb.toString());
			Element table = doc.select(".box_contents table").first();
			Elements spanEls = table.select("span");
			for (Element spanEl : spanEls) {
				String spanElClass = spanEl.attr("class");
				if (spanElClass.contains("up")) {
					spanEl.attr("style", "color:red;");
					logger.debug("spanEl.parent:" + spanEl.parent());
				} else if (spanElClass.contains("down")) {
					spanEl.attr("style", "color:blue;");
					logger.debug("spanEl.parent:" + spanEl.parent());
				}
			}

			String fileName = userHome + "\\documents\\" + strYmdhms + "_" + title + ".html";
			String tableHtml = Jsoup.parse(table.outerHtml()).html();
			FileUtil.fileWrite(fileName, tableHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
