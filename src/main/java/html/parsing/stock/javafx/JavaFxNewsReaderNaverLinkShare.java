package html.parsing.stock.javafx;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.webkit.network.CookieManager;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.NewsPublisher;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class JavaFxNewsReaderNaverLinkShare extends Application {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(JavaFxNewsReaderNaverLinkShare.class);

	List<StockVO> kospiUniqueStockList = new ArrayList<>();
	List<StockVO> kosdaqUniqueStockList = new ArrayList<>();

	WebEngine webengine = null;

	String strNidAut = "";
	String strNidSes = "";

	TextField nidAutTf = new TextField();
	TextArea nidSesTa = new TextArea();
	Text shareResultTxt = new Text();
	ComboBox<String> categoryListComboBox = new ComboBox<String>();

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("엔젤반 브라우저");
		final String FONT_FAMILY = "Arial"; // define font family you need
		final double MAX_FONT_SIZE = 20.0; // define max font size you need
		final String FX_FONT_STYLE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;";

		Separator vSeparator = new Separator();
		vSeparator.setOrientation(Orientation.VERTICAL);
		vSeparator.setPrefHeight(10);

		Separator hSeparator = new Separator();
		hSeparator.setOrientation(Orientation.HORIZONTAL);
		hSeparator.setPrefWidth(10);

		// Top
//		Text backTxt = new Text("←");
		Text backTxt = new Text("⇦");
		backTxt.setStyle(FX_FONT_STYLE);
//		Text forwardTxt = new Text("→");
		Text forwardTxt = new Text("⇨");
//		Text forwardTxt = new Text("➲");
		forwardTxt.setStyle(FX_FONT_STYLE);
		Text reloadTxt = new Text("⟳");
		reloadTxt.setStyle(FX_FONT_STYLE);

		Label backLbl = new Label("←");
		backLbl.setPrefWidth(50);
		backLbl.setPrefHeight(25);
		backLbl.setAlignment(Pos.TOP_LEFT);
		backLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label		

		Label forwardLbl = new Label("→");
		forwardLbl.setPrefWidth(50);
		forwardLbl.setPrefHeight(25);
		forwardLbl.setAlignment(Pos.TOP_LEFT);
		forwardLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label		

		Label reloadLbl = new Label("⟳");
		reloadLbl.setPrefWidth(50);
		reloadLbl.setPrefHeight(25);
		reloadLbl.setAlignment(Pos.TOP_LEFT);
		reloadLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label		

		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);

		urlTf.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
			new EventHandler<javafx.scene.input.KeyEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				System.out.println("code:" + event.getCode());
				System.out.println("code:" + event.getText());
				System.out.println("code:" + event.getCharacter());
				System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
				System.out.println("event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
				if (event.getCode().equals(KeyCode.ENTER)) {
					webengine.load(urlTf.getText());
				}
			}
		});

		urlTf.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			getNaverCookies();
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				webengine.load(urlTf.getText());
			}
		});

		Button shareBtn = new Button("네이버 블로그 글쓰기");
		shareBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				//네이버 블로그 공유
				System.out.println("네이버 블로그 글쓰기");
				shareResultTxt.setText("");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

					String url = urlTf.getText();
					logger.debug("url :" + url);
					if (org.apache.commons.lang3.StringUtils.defaultString(url).equals("")) {
						return;
					}
					createHTMLFile(url);
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}
		});

//		HBox urlHBox = new HBox(backLbl, forwardLbl, reloadLbl, urlTf, goBtn);
		HBox urlHBox = new HBox(10);
		urlHBox.getChildren().addAll(hSeparator);
		urlHBox.getChildren().addAll(backTxt);
		urlHBox.getChildren().addAll(forwardTxt);
		urlHBox.getChildren().addAll(reloadTxt);
		urlHBox.getChildren().addAll(urlTf);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(shareBtn);
		urlHBox.getChildren().addAll(shareResultTxt);

		WebView webView = new WebView();
		webengine = webView.getEngine();

//		webengine.load("https://finance.daum.net/domestic/all_quotes");
		webengine.load("https://www.naver.com");
		webengine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			if (Worker.State.SUCCEEDED.equals(newValue)) {
				urlTf.setText(webengine.getLocation());
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

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				System.out.println("html:" + html);
				//saveHtml(html, "전광판");
				try {
					saveCookies();
				} catch (NoSuchMethodException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		;
		});
		
		Text nidAutTxt = new Text("NID_AUT");
		nidAutTxt.setStyle(FX_FONT_STYLE);

		nidAutTf = new TextField();
		nidAutTf.setPrefWidth(800);
		nidAutTf.setPrefHeight(25);
		nidAutTf.setAlignment(Pos.TOP_LEFT);
		HBox nidAutBox = new HBox(nidAutTxt, nidAutTf);

		Text nidSesTxt = new Text("NID_SES");
		nidSesTxt.setStyle(FX_FONT_STYLE);

		nidSesTa = new TextArea();
		nidSesTa.setPrefWidth(800);
		nidSesTa.setPrefHeight(125);
		nidSesTa.setWrapText(true);

		categoryListComboBox.getItems().addAll(
			"266:쿠팡 상품 추천", "267:로켓배송", "268:로켓프레시", "269:로켓직구", "270:정기배송", "271:골드박스", "272:기획전", "274:카테고리별 베스트 상품", "275:PL 상품", "276:PL 브랜드별 상품", "277:추천 상품", "33:소개, 알림, 공지", "173:유행, 트렌드, 동향", "255:역사", "88:사회, 문화", "198:국정교과서", "216:혼이비정상", "31:정치, 정부, 정책", "180:선거", "7:국외, 해외, 국제, 세계", "249:북한", "236:미국", "228:중국", "237:일본", "2:경제, 산업", "256:삼성", "260:현대", "141:부동산", "238:가상(암호)화폐", "250:투자썰전", "47:IT(Info Tech)", "258:BT(Bio Tech)", "259:NT(Nano Tech)", "199:카페베네", "131:증권", "265:미국", "146:증권↑↓↗↘", "153:특징주", "164:신고, 신저가", "235:시간외단일가", "278:증권뉴스", "176:제약,약품, 바이오", "264:IT(Info Tech)", "273:조선", "190:삼성주", "171:국민연금", "261:ETN, ETF", "188:핸디소프트", "253:Entertainment", "166:외국인 보유", "170:PER", "172:상하한일수", "148:데이타", "155:Top 100", "159:기외 연속매수", "160:기외 연속매도", "156:기외 거래량", "161:기외 거래대금", "157:기외 양매수금", "162:기외 양매수량", "158:기외 양매도금", "163:기외 양매도량", "152:기획기사", "209:방송, 언론", "210:JTBC", "201:뉴스공장", "202:파파이스", "206:스포트라이트", "150:건강", "207:치매", "29:비타민", "140:운동", "151:식당", "208:마약", "263:질병", "132:Manuka Honey", "9:음식, 식료품", "262:환경", "142:사건, 사고", "182:세월호", "234:4대강", "204:5촌살인사건", "241:MeToo", "243:갑질", "244:댓글사건", "121:오늘의 잠언", "177:오늘의 계시", "128:오늘의 성경", "120:오늘의 말씀", "149:오늘의 사진", "123:오늘의 영어", "178:주일, 수요말씀", "245:인물", "197:문재인대통령", "189:노무현대통령", "225:인물1", "179:이승만", "183:박정희", "240:이명박", "185:박근혜", "193:이재명", "191:김기춘", "186:최태민", "200:김재규", "184:최순실", "229:장준하", "192:역사", "147:브렉시트", "145:자동차", "174:여행", "관광", "196:레져", "144:신앙", "181:종교", "230:과학", "111:LearningJava, 4Th", "94:자바 IO, NIO NetPrg", "50:Node.js 프로그래밍", "70:막힘없이배우는Java프로그래밍", "89:HTML5를 활용한 모바일웹앱", "90:1부.HTML5주요기능", "91:2부. jQueryMobile", "92:3부.Sencha Touch", "5:웹 프로그래밍", "127:모바일 프로그래밍", "130:모던웹을위한HTML5프로그래밍", "35:연예, 엔터, 재미", "129:해외직구", "32:쇼핑", "135:문화, 예술", "3:음악", "139:미술", "49:영화", "6:연예", "8:책", "211:교양", "212:다큐", "213:교육", "46:보안", "24:패션", "37:뷰티", "19:디자인", "114:메르스", "25:생활", "10:스포츠", "30:동영상", "69:월남전", "43:영감의 시", "126:천국과지옥", "125:정명석선생님", "137:프로그램", "45:CSS", "87:Eclipse", "247:easyui", "93:Google", "44:HTML", "27:JavaScript", "26:Java", "42:jQuery", "248:NetBeans", "112:Node.js", "86:Spring", "246:Mybatis", "115:Swing", "39:Thymeleaf", "254:tomcat", "113:Software", "36:드라이버", "257:Freemarker", "133:데이터베이스", "41:Oracle", "48:MSSQL", "40:MySQL", "134:운영체제", "22:Windows", "21:Unix, Linux", "175:레오사진", "233:광고"
		);
		categoryListComboBox.setPromptText("Please select one");

		HBox nidSesBox = new HBox(nidSesTxt, nidSesTa, categoryListComboBox);

		VBox nidBox = new VBox(nidAutBox, nidSesBox, saveBtn);

		VBox saveBtnBox = new VBox(saveBtn);
		saveBtnBox.setAlignment(Pos.CENTER);

		VBox vBox = new VBox(urlHBox, webView, vSeparator, nidBox, saveBtnBox);
//		VBox vBox = new VBox(urlTf, webView, separator, saveBtn);
//		vBox.autosize();
		vBox.setAlignment(Pos.TOP_LEFT);
		Scene scene = new Scene(vBox, 1300, 800);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void saveHtml(String contentHtml, String title) {
		String strUrl = "https://finance.daum.net/domestic/all_quotes";
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
		String strYmd = sdf.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("[yyyy-MM-dd_hhmmss]", Locale.KOREAN);
		String strYmdhms = sdf2.format(new Date());

		Document doc = Jsoup.parse(contentHtml);
		Elements marketEls = doc.select(".marketPrice .tabW ul li a");
		String market_ko = "";
		String market_en = "";
		for (Element marketEl : marketEls) {
			String marketClass = marketEl.attr("class");
			if (marketClass.equals("on")) {
				market_ko = marketEl.attr("title");
			}
		}
		System.out.println("market" + ":" + market_ko);
		if (market_ko.equals("코스피")) {
			market_en = "kospi";
		} else {
			market_en = "kosdaq";
		}
		Elements tableEls = doc.select("table");
		System.out.println("tableEls.size:" + tableEls.size());
		int stockCount = 0;
		int fundCount = 0;
		int preferredStockCount = 0;
		Map stockMap = new HashMap<>();
		for (Element tableEl : tableEls) {
			Elements trEls = tableEl.select("tr");
			for (Element trEl : trEls) {

				Elements tdEls = trEl.select("td");
				int tdCnt = 0;
				for (Element tdEl : tdEls) {
					logger.debug("tdEl:" + tdEl);
					if (tdCnt == 0) {
						Elements aEls = tdEl.select("a");
						if (aEls.size() >= 0) {
							Element firstAEl = aEls.first();
							System.out.println("firstAEl:" + firstAEl);
							if (firstAEl != null) {
								String strStockName = firstAEl.text();
								String onclick = firstAEl.attr("onclick");
								String strStockCode = onclick.substring(onclick.lastIndexOf("/") + 1);
								strStockCode = strStockCode.replace("'", "");
								if (strStockCode.startsWith("A")) {
									if (!strStockName.startsWith("ARIRANG")
										&& !strStockName.startsWith("KINDEX")
										&& !strStockName.startsWith("TIGER")
										&& !strStockName.startsWith("KBSTAR")
										&& !strStockName.startsWith("SMART")
										&& !strStockName.startsWith("KODEX")
										&& !strStockName.startsWith("TREX")
										&& !strStockName.startsWith("HANARO")
										&& !strStockName.startsWith("KOSEF")
										&& !strStockName.contains("코스피")
										&& !strStockName.contains("레버리지")
										&& !strStockName.contains("S&P")
										&& !strStockName.contains("마이다스")
										&& !strStockName.contains("고배당")
										&& !strStockName.contains("FOCUS")) {

										stockCount++;
										strStockCode = strStockCode.replace("A", "");
										System.out.println(strStockCode + "\t" + strStockName);
										stockMap.put(strStockCode, strStockName);
									}
								} else {
									fundCount++;
								}
								if (strStockCode.toLowerCase().contains("k")) {
//								System.out.println("우선주 "+strStockCode+"(" + strStockName+")");
									System.out.println("우선주 " + strStockName + "(" + strStockCode + ")");
									preferredStockCount++;
								}
							}

						}
					}

					if (tdCnt == 1) {
						String curPrice = tdEl.text();
						curPrice = curPrice.replace(",", "");
						int iCurPrice = Integer.parseInt(curPrice);
					}
					//specialLetter ↑↓ ▲ ▼ -
					if (tdCnt == 2) {
						String specialLetter = tdEl.select("i").text();
						tdEl.select("i").remove();
						String varyPrice = StringUtils.defaultIfEmpty(tdEl.text(), "0");
						logger.debug("varyPrice1:" + varyPrice);
						varyPrice = varyPrice.replace(",", "");
						logger.debug("varyPrice2:" + varyPrice);
						int iVaryPrice = Integer.parseInt(varyPrice);
					}
					if (tdCnt == 3) {
						String varyRatio = tdEl.text();
						logger.debug("varyRatio:" + varyRatio);
						varyRatio = varyRatio.replace("%", "");
						float fVaryRatio = Float.parseFloat(varyRatio);
					}
					tdCnt++;
				}
			}

		}
		System.out.println("stockCount:" + stockCount);
		System.out.println("fundCount:" + fundCount);
		System.out.println("preferredStockCount:" + preferredStockCount);
		System.out.println("stockCount- preferredStockCount:" + (stockCount - preferredStockCount));
		System.out.println("stockMap.size:" + stockMap.size());
		StringBuilder stockCodeNameSb = new StringBuilder();
		Set keySet = stockMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String strStockCode = (String) it.next();
			String strStockName = (String) stockMap.get(strStockCode);
			stockCodeNameSb.append(strStockCode + "\t" + strStockName + "\r\n");
		}
		//String jsonArray = JSONArray.toJSONString(stockVoList);
		//jsonArray=list
		List<Map> stockMapList = new ArrayList<>();
		stockMapList.add(stockMap);
//		String jsonArray = JSONArray.toJSONString(stockMapList);
		String jsonArray = stockMapList.toArray().toString();
		//jsonObject=map
//		String jsonObject = JSONObject.toJSONString(stockMap);
		String jsonObject = stockMap.toString();
		String fileName = "";
//		fileName = userHome + "\\documents\\" + strYmdhms + "_" + market_en + "_list.json";
		fileName = market_en + "_list.json";
		FileUtil.fileWrite(fileName, jsonObject);
//		fileName = userHome + "\\documents\\" + strYmdhms + "_" + market_en + "_list.txt";
		fileName = market_en + "_list.txt";
		FileUtil.fileWrite(fileName, stockCodeNameSb.toString());
		JOptionPane.showMessageDialog(null, "주식 목록을 추출하였습니다.");
	}

	private void saveCookies() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, IOException {
		CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
		Field f = cookieManager.getClass().getDeclaredField("store");
		f.setAccessible(true);
		Object cookieStore = f.get(cookieManager);

		Class cookieClass = Class.forName("com.sun.webkit.network.Cookie");
		System.out.println("cookieClass:" + cookieClass);

		Field bucketsField = Class.forName("com.sun.webkit.network.CookieStore").getDeclaredField("buckets");
		bucketsField.setAccessible(true);
		Map buckets = (Map) bucketsField.get(cookieStore);
		System.out.println("buckets:" + buckets);
		f.setAccessible(true);
		Map<String, Collection> cookiesToSave = new HashMap<>();
		for (Object o : buckets.entrySet()) {
			Map.Entry pair = (Map.Entry) o;
			String key = (String) pair.getKey();
			Map cookies = (Map) pair.getValue();
			cookiesToSave.put(key, cookies.values());
		}

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(cookiesToSave);
		System.out.println("json:" + json);
		JSONObject jo = new JSONObject(json);
		callJsonObjectLoop(jo);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		nidAutTf.setText(strNidAut);
		nidSesTa.setText(strNidSes);
		Files.write(Paths.get("cookies.json"), json.getBytes());
	}

	private void getNaverCookies() {
		try {
			CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
			System.out.println("cookieManager:" + cookieManager);
			System.out.println("cookieManager.getClass():" + cookieManager.getClass());
			Field f = cookieManager.getClass().getDeclaredField("store");
			f.setAccessible(true);
			Object cookieStore = f.get(cookieManager);

			Class cookieClass = Class.forName("com.sun.webkit.network.Cookie");
			System.out.println("cookieClass:" + cookieClass);

			Field bucketsField = Class.forName("com.sun.webkit.network.CookieStore").getDeclaredField("buckets");
			bucketsField.setAccessible(true);
			Map buckets = (Map) bucketsField.get(cookieStore);
			System.out.println("buckets:" + buckets);
			f.setAccessible(true);
			Map<String, Collection> cookiesToSave = new HashMap<>();
			for (Object o : buckets.entrySet()) {
				Map.Entry pair = (Map.Entry) o;
				String key = (String) pair.getKey();
				Map cookies = (Map) pair.getValue();
				cookiesToSave.put(key, cookies.values());
			}

			Gson gson = new GsonBuilder().create();
			String json = gson.toJson(cookiesToSave);
			System.out.println("json:" + json);
			JSONObject jo = new JSONObject(json);
			callJsonObjectLoop(jo);
			System.out.println("strNidAut:" + strNidAut);
			System.out.println("strNidSes:" + strNidSes);
			nidAutTf.setText(strNidAut);
			nidSesTa.setText(strNidSes);
		} catch (NoSuchFieldException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void callJsonObjectLoop(Object obj) {
		if (obj instanceof JSONObject) {
			JSONObject jObj = (JSONObject) obj;
			Iterator keysIt = jObj.keys();
			while (keysIt.hasNext()) {
				String key = (String) keysIt.next();
				Object value = jObj.get(key);
				System.out.println(key + ":" + value.toString());
				if (value.toString().equals("NID_SES")) {
					strNidSes = (String) jObj.get("value");
					System.out.println("strNidSes:" + strNidSes);
				} else if (value.toString().equals("NID_AUT")) {
					strNidAut = (String) jObj.get("value");
					System.out.println("strNidAut:" + strNidAut);
				}
				if (value instanceof JSONObject) {
					callJsonObjectLoop((JSONObject) value);
				} else if (value instanceof JSONArray) {
					callJsonObjectLoop((JSONArray) value);
				}
			}
		} else if (obj instanceof JSONArray) {
			JSONArray jAry = (JSONArray) obj;
			for (int i = 0; i < jAry.length(); i++) {
				JSONObject jObj = (JSONObject) jAry.get(i);
				System.out.println("jObj:" + jObj.toString());
				callJsonObjectLoop(jObj);
			}
		}
	}

	private void createHTMLFile(String url) {
		if (url.equals("")) {
			return;
		}
		System.out.println("url:" + url);
		// tab2에서 페이지 이동
		int idx = 0;
		String newsCompany = "";
		for (NewsPublisher np : NewsPublisher.values()) {
			String newsPublisherDomain = np.getName();
			idx = np.ordinal();
			if (url.contains(newsPublisherDomain)) {
				System.out.println("idx:" + idx + " newsPublisherDomain:" + newsPublisherDomain);
				System.out.println("주소가 일치합니다. idx:" + idx);
				newsCompany = np.toString();
				System.out.println("newsCompany:" + newsCompany);
				break;
			}
		}
		StringBuilder sb = new StringBuilder();

		if (newsCompany.equals("")) {
			shareResultTxt.setText("뉴스 클래스 부재");
			return;
		}

		Class<?> c;
		try {
			c = Class.forName("html.parsing.stock.news." + newsCompany);
			System.out.println("Class Name:" + c.getName());
			System.out.println("url:" + url);
			// c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs );
			Method method = c.getDeclaredMethod("createHTMLFile", String.class);
			sb = (StringBuilder) method.invoke(String.class, new Object[]{url});
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.INFO, sb.toString());
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
			| NoSuchMethodException | SecurityException ex) {
			java.util.logging.Logger.getLogger(JavaFxNewsReaderNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}

		Document htmlDoc = Jsoup.parse(sb.toString());
		logger.debug("htmlDoc:" + htmlDoc.html());

		htmlDoc.select("meta").remove();
		sb.delete(0, sb.length());
		sb.setLength(0);

		String strCategoryNo = "33";
		String strCategoryName = "증권";
		String strSelectedCategory = String.valueOf(categoryListComboBox.getSelectionModel().getSelectedItem());
		System.out.println("strSelectedCategory :" + strSelectedCategory);
		String strSelectedCategoryArray[] = strSelectedCategory.split(":");
		System.out.println("strSelectedCategoryArray.length :" + strSelectedCategoryArray.length);
		if (strSelectedCategoryArray.length > 1) {
			strCategoryNo = strSelectedCategoryArray[0];
			strCategoryName = strSelectedCategoryArray[1];
			System.out.println("strSelectedCategoryArray[0]-------------->" + strSelectedCategoryArray[0]);
			System.out.println("strSelectedCategoryArray[1]-------------->" + strSelectedCategoryArray[1]);
		}

		String strShareTitle = htmlDoc.select("h2#title").text();
		String strShareUrl = htmlDoc.select("a").first().attr("href");
		StringBuilder contentSb = new StringBuilder();
		contentSb.append(htmlDoc.html());
		contentSb.toString();

		logger.debug("strShareTitle:" + strShareTitle);
		logger.debug("strShareUrl:" + strShareUrl);

		if (naverBlogLinkShare(contentSb, strCategoryName, strShareTitle, strShareUrl)) {
			shareResultTxt.setText("블로그 글쓰기 성공");
		} else {
			shareResultTxt.setText("블로그 글쓰기 실패");
		}
	}

	public boolean naverBlogLinkShare(StringBuilder contentSb, String strCategoryName, String strShareTitle, String strShareUrl) {
		strNidAut = nidAutTf.getText();
		strNidSes = nidSesTa.getText();
		return NaverUtil.naverBlogLinkShare(strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName, contentSb, null);
	}
}
