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
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AfterHoursKosdaqFinanceDaumNetApp extends Application {

	
	private static Logger logger = LoggerFactory.getLogger(AfterHoursKosdaqFinanceDaumNetApp.class);
	public final static String USER_HOME = System.getProperty("user.home");

	final static String daumKospiAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String daumKosdaqAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSDAQ";

	final String FONT_FAMILY = "Arial"; // define font family you need
	final double MAX_FONT_SIZE = 20.0; // define max font size you need
	final String FX_FONT_STYLE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;";

	WebEngine webengine = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("코스닥 시간외단일가");

		Label urlLbl = new Label("URL");
		urlLbl.setPrefWidth(50);
		urlLbl.setPrefHeight(25);
		urlLbl.setAlignment(Pos.TOP_LEFT);
		urlLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label

		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);
		urlTf.setText(daumKosdaqAfterHoursUrl);

		WebView webView = new WebView();
		webView.setPrefHeight(900);
		webengine = webView.getEngine();

		webengine.load(daumKosdaqAfterHoursUrl);
		
		webengine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable1 :" + observable);
			logger.debug(" oldState1 :" + oldState + " newState1:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf.setText(webengine.getLocation());
			}
		});

//		webengine.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
//			@Override
//			public void changed(ObservableValue observable, State oldState, State newState) {
//				logger.debug("observable2 :" + observable);
//				logger.debug(" oldState2 :" + oldState + " newState2:" + newState);
//				if (newState == State.SUCCEEDED) {
//					urlTf.setText(webengine.getLocation());
//				}
//			}
//		});
		
		urlTf.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				System.out.println("code:" + event.getCode());
				System.out.println("code:" + event.getText());
				System.out.println("code:" + event.getCharacter());
				System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
				System.out.println("event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
				System.out.println("urlTf.getText():" + urlTf.getText());
				if (event.getCode().equals(KeyCode.ENTER)) {
					String strUrl = urlTf.getText();
					System.out.println("url1:" + strUrl);
					if (!strUrl.toLowerCase().startsWith("http") && !strUrl.toLowerCase().startsWith("https")) {
						if (!strUrl.contains(".") || strUrl.contains(" ")) {
							strUrl = "https://www.google.com/search?q=" + strUrl + "&oq=" + strUrl;
						} else {
							strUrl = "http://" + strUrl;
						}
					}
					System.out.println("url2:" + strUrl);
					urlTf.setText(strUrl);
					webView.getEngine().load(strUrl);

					String strContent = (String) webView.getEngine()
							.executeScript("document.documentElement.outerHTML");
					System.out.println("strContent:" + strContent);

					webView.getEngine().loadContent(strContent);
				}
			};
		});

		Button goBtn = new Button("Go");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String strUrl = urlTf.getText();
						webView.getEngine().load(strUrl);
					};

				});

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveHtml(html, "코스닥 시간외 단일가");
					};

				});

//		VBox vBox = new VBox(urlTf, webView, button1);
		Separator hSeparator1 = new Separator();
		hSeparator1.setOrientation(Orientation.HORIZONTAL);
		hSeparator1.setPrefWidth(10);

		Separator hSeparator2 = new Separator();
		hSeparator2.setOrientation(Orientation.HORIZONTAL);
		hSeparator2.setPrefWidth(10);

		HBox hBox = new HBox();
		hBox.getChildren().addAll(urlLbl);
		hBox.getChildren().addAll(urlTf);
		hBox.getChildren().addAll(hSeparator1);
		hBox.getChildren().addAll(goBtn);
		hBox.getChildren().addAll(hSeparator2);
		hBox.getChildren().addAll(saveBtn);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(hBox);
		vBox.getChildren().addAll(webView);

		Scene scene = new Scene(vBox, 960, 600);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void saveHtml(String contentHtml, String title) {
		String strUrl = "https://finance.daum.net/domestic/after_hours?market=KOSDAQ";
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

			String fileName = USER_HOME + "\\documents\\" + strYmdhms + "_" + title + ".html";
			String tableHtml = Jsoup.parse(table.outerHtml()).html();
			FileUtil.fileWrite(fileName, tableHtml);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
