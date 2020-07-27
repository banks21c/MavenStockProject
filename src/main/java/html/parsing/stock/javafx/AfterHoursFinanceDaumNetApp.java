package html.parsing.stock.javafx;

import java.io.IOException;
import java.net.MalformedURLException;
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
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AfterHoursFinanceDaumNetApp extends Application {

	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AfterHoursFinanceDaumNetApp.class);

	final static String homeUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String daumKospiAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String daumKosdaqAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSDAQ";

	final String FONT_FAMILY = "Arial"; // define font family you need
	final String FX_FONT_STYLE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;";
	final double MAX_FONT_SIZE = 15.0; // define max font size you need
	final String FX_FONT_STYLE_DEFAULT = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: black ;";
	final String FX_FONT_STYLE_RED = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: red ;";
	final String FX_FONT_STYLE_LARGE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;-fx-fill: black ;";

	URL url;
	String strProtocol;
	String strHost;
	String strProtocolHost = null;
	String strPath = null;
	String strQuery = null;
	String strRef = null;
	int iPort;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("시간외단일가");
		try {
			url = new URL(daumKospiAfterHoursUrl);
			strProtocol = url.getProtocol();
			strHost = url.getHost();
			strProtocolHost = strProtocol + "://" + strHost;
			strPath = url.getPath();
			strQuery = url.getQuery();
			strRef = url.getRef();
			iPort = url.getPort();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);
		urlTf.setText(daumKospiAfterHoursUrl);

		WebView webView = new WebView();
		webView.setPrefHeight(900);
		WebEngine webengine = webView.getEngine();

		webengine.load(daumKospiAfterHoursUrl);

		webengine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable1 :" + observable);
			logger.debug(" oldState1 :" + oldState + " newState1:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf.setText(webengine.getLocation());

				String strContent = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				strContent = strContent.replace("\"//", "\"" + strProtocol + "://");
				strContent = strContent.replace("\"/", "\"" + strProtocolHost + "/");
				strContent = strContent.replace("\"app", "\"" + strProtocolHost + "/dist/daum/app");

//				System.out.println("strContent1:" + strContent);

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

		HBox urlHBox = new HBox(10);
		HBox naviTxtHBox = getNavigateText(webView);
		urlHBox.getChildren().addAll(naviTxtHBox);
		urlHBox.getChildren().addAll(hSeparator1);
		urlHBox.getChildren().addAll(urlTf);
		urlHBox.getChildren().addAll(hSeparator2);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(saveBtn);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(urlHBox);
		vBox.getChildren().addAll(webView);

		Scene scene = new Scene(vBox, 960, 600);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public HBox getNavigateText(WebView webView) {
		// Top
		Text homeTxt = new Text("🏠");
		homeTxt.setStyle(FX_FONT_STYLE_LARGE);
//		Text backTxt = new Text("←");🏠
		Text backTxt = new Text("⇦");
		backTxt.setStyle(FX_FONT_STYLE_LARGE);
//		Text forwardTxt = new Text("→");
		Text forwardTxt = new Text("⇨");
//		Text forwardTxt = new Text("➲");
		forwardTxt.setStyle(FX_FONT_STYLE_LARGE);
		Text reloadTxt = new Text("⟳");
		reloadTxt.setStyle(FX_FONT_STYLE_LARGE);

		Separator hSeparator1 = new Separator();
		hSeparator1.setOrientation(Orientation.HORIZONTAL);
		hSeparator1.setPrefWidth(10);

		Separator hSeparator2 = new Separator();
		hSeparator2.setOrientation(Orientation.HORIZONTAL);
		hSeparator2.setPrefWidth(10);

		Separator hSeparator3 = new Separator();
		hSeparator3.setOrientation(Orientation.HORIZONTAL);
		hSeparator3.setPrefWidth(10);

		homeTxt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						webView.getEngine().load(homeUrl);
					}
				});
		backTxt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						webView.getEngine().getHistory().go(-1);
					}
				});
		forwardTxt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						webView.getEngine().getHistory().go(1);
					}
				});
		reloadTxt.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						webView.getEngine().reload();
					}
				});
		return new HBox(homeTxt, hSeparator1, backTxt, hSeparator2, forwardTxt, hSeparator3, reloadTxt);
	}

	public void saveHtml(String contentHtml, String title) {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
		String strYmd = sdf.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("[yyyy-MM-dd_hhmmss]", Locale.KOREAN);
		String strYmdhms = sdf2.format(new Date());

		sb.append("<h1>").append(strYmd).append("_").append(title).append("</h1>");
		try {
			URL url = new URL(daumKospiAfterHoursUrl);
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
