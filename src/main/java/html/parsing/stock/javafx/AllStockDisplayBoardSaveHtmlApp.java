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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AllStockDisplayBoardSaveHtmlApp extends Application {

	
	private static Logger logger = LoggerFactory.getLogger(AllStockDisplayBoardSaveHtmlApp.class);
	public final static String USER_HOME = System.getProperty("user.home");

	WebEngine webengine = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("코스피 전광판");

		WebView webView = new WebView();
		webengine = webView.getEngine();

		webengine.load("https://finance.daum.net/domestic/all_quotes");

		Button button1 = new Button("Save");
		button1.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveHtml(html, "전광판");
					};

				});

		VBox vBox = new VBox(webView, button1);
		Scene scene = new Scene(vBox, 960, 600);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void saveHtml(String contentHtml, String title) {
		String strUrl = "https://finance.daum.net/domestic/all_quotes";
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
			Elements spanEls = doc.select("span");
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

			String fileName = USER_HOME + "\\documents\\" + strYmdhms + "_" + title + ".html";

			FileUtil.fileWrite(fileName, doc.html());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
