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
import static html.parsing.stock.javafx.CoupangPartnersApiNaverLinkShare.generate;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.NewsPublisher;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Properties;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;

public class Step1_StockMarketPriceNaverLinkShareTab1 extends Application {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(Step1_StockMarketPriceNaverLinkShareTab1.class);

	List<StockVO> kospiUniqueStockList = new ArrayList<>();
	List<StockVO> kosdaqUniqueStockList = new ArrayList<>();

	String strNidAut = "";
	String strNidSes = "";

	TextField nidAutTf = new TextField();
	TextArea nidSesTa = new TextArea();
	TextArea myCommentTa = new TextArea();

	Text shareResultTxt = new Text();

	final String FONT_FAMILY = "Arial"; // define font family you need
	final double MAX_FONT_SIZE = 15.0; // define max font size you need
	final String FX_FONT_STYLE_DEFAULT = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: black ;";
	final String FX_FONT_STYLE_RED = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: red ;";
	final String FX_FONT_STYLE_LARGE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;-fx-fill: black ;";

	TextField accessKeyTf = new TextField();
	TextField secretKeyTf = new TextField();

	javafx.scene.control.CheckBox cb1;
	javafx.scene.control.CheckBox cb2;
	javafx.scene.control.CheckBox cb3;
	javafx.scene.control.CheckBox cb4;
	javafx.scene.control.CheckBox cb5;

	ComboBox<String> nBlogCategoryListComboBox1;
	ComboBox<String> nBlogCategoryListComboBox2;
	ComboBox<String> cCategoryListComboBox;
	ComboBox<String> cBrandListComboBox;

	Label bestcategoriesResultLbl;
	Label goldboxResultLbl;
	Label coupangPLResultLbl;
	Label coupangPLBrandResultLbl;
	Label searchResultLbl;

	TextField keywordTf;

	private static final long serialVersionUID = 1341726937516862047L;
	final static String USER_HOME = System.getProperty("user.home");
	private static final String ALGORITHM = "HmacSHA256";
	private static final Charset STANDARD_CHARSET = Charset.forName("UTF-8");

	private String strCoupangHomeUrl = "https://www.coupang.com/";

	String strTitle = "로켓배송";
	String productDivIdOrClassName = "div.newcx_list";
	String productListIdOrClassName = "ul.promotion_list";

	String boxWidthStyle = "width:214px;";
	String boxHeightStyle = "height:450px;";
	String imgWidthStyle = "width:212px;";
	String imgHeightStyle = "height:212px;";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
	String strDate = sdf.format(new Date());
	SimpleDateFormat sdf0 = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
	String strYmdBlacket = sdf0.format(new Date());

	SimpleDateFormat sdf_ymd = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
//	String strYmd = sdf_ymd.format(new Date());

//	String strDefaultStartDate = sdf_ymd.format( LocalDateTime.from(new Date().toInstant()).minusMonths(3));
	String strDefaultStartDate = sdf_ymd.format(new DateTime().minusMonths(3).toDate());
//	String strDefaultStartDate = sdf_ymd.format( new DateTime(new Date()).minusMonths(3).toDate());

	String strDefaultEndDate = sdf_ymd.format(new Date());

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
	String strYmdhms = sdf1.format(new Date());

	String strCategoryName = "";

	String strFileName;
	URI uri = null;
	URL url = null;
	String strProtocol = null;
	String strHost = null;
	String strPath = null;
	String strQuery = null;
	String strRef = null;
	int iPort;

	// 쿠팡배너
	String coupangBannerHtml = "<a href='${strCoupangHomeUrl}' target='_blank'><img src='https://ads-partners.coupang.com/banners/248366?subId=&traceId=V0-301-879dd1202e5c73b2-I248366&w=728&h=90' alt=''></a>";
	// 골그박스
	String goldboxBannerHtml = "<a href='${strGoldboxUrl}' target='_blank'><img src='https://ads-partners.coupang.com/banners/248368?subId=&traceId=V0-301-969b06e95b87326d-I248368&w=728&h=90' alt=''></a>";
	// 로켓와우
	String rocketWowBannerHtml = "<a href='${strRocketWowUrl}' target='_blank'><img src='https://ads-partners.coupang.com/banners/248630?subId=&traceId=V0-301-bae0f72e5e59e45f-I248630&w=728&h=90' alt=''></a>";
	// 로켓프레시
	String rocketFreshBannerHtml = "<a href='${strRocketFreshUrl}' target='_blank'><img src='https://ads-partners.coupang.com/banners/248367?subId=&traceId=V0-301-371ae01f4226dec2-I248367&w=728&h=90' alt=''></a>";
	// 로켓직구
	String rocketJikguBannerHtml = "<a href='${strRocketJikguUrl}' target='_blank'><img src='https://ads-partners.coupang.com/banners/248642?subId=&traceId=V0-301-50c6c2b97fba9aee-I248642&w=728&h=90' alt=''></a>";
	// 정기배송
//	String fixedDeliveryBannerHtml = "<a href='${strFixedDeliveryUrl}' target='_blank'><img src='https://image7.coupangcdn.com/image/displayitem/displayitem_d9cff975-232b-415d-950c-edc800a1e93d.png' alt='기본배너'></a>";
	String fixedDeliveryBannerHtml = "<a href='${strFixedDeliveryUrl}' target='_blank'><img src='https://image7.coupangcdn.com/image/displayitem/displayitem_d9cff975-232b-415d-950c-edc800a1e93d.png' alt='기본배너'></a>";

	// 로켓배송
	String rocketDeliveryBannerHtml = "<a href='${strRocketDeliveryUrl}'> <img src='https://static.coupangcdn.com/ca/cmg_paperboy/image/1565948764070/0819%28%EC%9B%94%29-C0-Left.jpg' alt=''> </a>";
	// 기획전
	String exhibitionBannerHtml = "<a href='#'> <img src='http://img1a.coupangcdn.com/image/promotion/promotion_title.png' alt=''> </a>";

	String topBanner = rocketDeliveryBannerHtml;

	boolean isJCheckBox1Selected = false;
	boolean isGoldboxJCheckBoxSelected = false;
	boolean isCoupangPLJCheckBoxSelected = false;
	boolean isCoupangPLBrandJCheckBoxSelected = false;
	boolean isSearchJCheckBoxSelected = false;
	boolean isClicksJCheckBoxSelected = false;
	boolean isOrdersJCheckBoxSelected = false;
	boolean isCancelsJCheckBoxSelected = false;
	boolean isLinkJCheckBoxSelected = false;

	// Replace with your own ACCESS_KEY and SECRET_KEY
	private String ACCESS_KEY = "";
	private String SECRET_KEY = "";

	private final static String REQUEST_METHOD_POST = "POST";
	private final static String REQUEST_METHOD_GET = "GET";
	private final static String DOMAIN = "https://api-gateway.coupang.com";
	private final static String API_PATH = "/v2/providers/affiliate_open_api/apis/openapi/v1";
	private final static String DEEPLINK_URL = API_PATH + "/deeplink";

	private static DecimalFormat df = new DecimalFormat("#,##0");
	// 채널ID
	private final static String subId = "";

	// GET
	// 카테고리 별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
//	private final static String BESTCATEGORIES_URL = API_PATH + "​/products​/bestcategories​/{categoryId}";
	private final static String BESTCATEGORIES_URL = API_PATH + "/products/bestcategories/";
	private final static String[][] bestCategoriesArray = {{"1001", "여성패션"}, {"1002", "남성패션"},
	{"1003", "베이비패션 (0~3세)"}, {"1004", "여아패션 (3세 이상)"}, {"1005", "남아패션 (3세 이상)"}, {"1006", "스포츠패션"},
	{"1007", "신발"}, {"1008", "가방/잡화"}, {"1010", "뷰티"}, {"1011", "출산/유아동"}, {"1012", "식품"},
	{"1013", "주방용품"}, {"1014", "생활용품"}, {"1015", "홈인테리어"}, {"1016", "가전디지털"}, {"1017", "스포츠/레저"},
	{"1018", "자동차용품"}, {"1019", "도서/음반/DVD"}, {"1020", "완구/취미"}, {"1021", "문구/오피스"},
	{"1024", "헬스/건강식품"}, {"1025", "국내여행"}, {"1026", "해외여행"}, {"1029", "반려동물용품"}};
	// 골드박스 상품에 대한 상세 상품 정보를 생성합니다. (골드박스 상품은 매일 오전 7:30에 업데이트 됩니다)
	private final static String GOLDBOX_URL = API_PATH + "/products/goldbox";
	// 쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
	private final static String COUPANG_PL_URL = API_PATH + "/products/coupangPL";
	// 쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
//	private final static String COUPANG_PL_BRAND_URL = API_PATH +"/products/coupangPL/{brandId}";
	private final static String COUPANG_PL_BRAND_URL = API_PATH + "/products/coupangPL/";
	private final static String coupangPlBrandArray[][] = {{"1001", "탐사"}, {"1002", "코멧"}, {"1003", "Gomgom"},
	{"1004", "줌"}, {"1005", "마케마케"}, {"1006", "곰곰"}, {"1007", "꼬리별"}, {"1008", "베이스알파에센셜"},
	{"1009", "요놈"}, {"1010", "비타할로"}, {"1011", "비지엔젤"}, {"1012", "타이니스타"}};
	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
	private static String SEARCH_URL = API_PATH + "/products/search";

//	private final static String REPORTS_CLICKS_URL = API_PATH+"/v1/reports/clicks";
//	private final static String REPORTS_ORDERS_URL = API_PATH+"/v1/reports/orders";
//	private final static String REPORTS_CANCELS_URL = API_PATH+"/v1/reports/cancels";
	private final static String REPORTS_CLICKS_URL = API_PATH + "/reports/clicks";
	private final static String REPORTS_ORDERS_URL = API_PATH + "/reports/orders";
	private final static String REPORTS_CANCELS_URL = API_PATH + "/reports/cancels";

//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/search?component=&q=good&channel=user\",\"https://www.coupang.com/np/coupangglobal\"]}";
	// 실패
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://pages.coupang.com/f/s299\"]}";
	// 성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/\"]}";
	// 성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/goldbox\"]}";
	// 성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://loyalty.coupang.com/loyalty/sign-up/home\"]}";
	// 성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/campaigns/82\"]}";
	// 성공
	private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/coupangglobal\"]}";

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("엔젤 브라우저");

		TabPane tabPane = new TabPane();

		Tab tab1 = new Tab("Daum 전종목 시세", getTab1());
		Tab tab2 = new Tab("Naver Home", getTab2());
		Tab tab3 = new Tab("Coupang Partner", getTab3());

		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);

		int numTabs = tabPane.getTabs().size();
		System.out.println("numTabs:" + numTabs);
		VBox tabPaneVBox = new VBox(tabPane);
//		tab1.setContent(getTab1());
//		tab2.setContent(getTab2());
//		tab3.setContent(getTab3());

		Scene scene = new Scene(tabPaneVBox, 1300, 1000);

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
				webView.getEngine().load("https://www.naver.com");
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

	public VBox getTab1() {
		WebView webView = new WebView();
		webView.setPrefHeight(1000);
		WebEngine webengine = webView.getEngine();
		String daumUrl = "https://finance.daum.net/domestic/all_quotes";
		webengine.load(daumUrl);

		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);

		urlTf.setText(daumUrl);

		urlTf.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
			new EventHandler<javafx.scene.input.KeyEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				System.out.println("code:" + event.getCode());
				System.out.println("code:" + event.getText());
				System.out.println("code:" + event.getCharacter());
				System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
				System.out.println("event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
				System.out.println("urlTf.getText():" + urlTf.getText());
				if (event.getCode().equals(KeyCode.ENTER)) {
					String url = urlTf.getText();
					System.out.println("url1:" + url);
					if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
						if (!url.contains(".") || url.contains(" ")) {
							url = "https://www.google.com/search?q=" + url + "&oq=" + url;
						} else {
							url = "http://" + url;
						}
					}
					System.out.println("url2:" + url);
					urlTf.setText(url);
					webView.getEngine().load(url);
				}
			}
		;
		});
		
		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String url = urlTf.getText();
				System.out.println("url1:" + url);
				if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
					if (!url.contains(".") || url.contains(" ")) {
						url = "https://www.google.com/search?q=" + url + "&oq=" + url;
					} else {
						url = "http://" + url;
					}
				}
				System.out.println("url2:" + url);
				urlTf.setText(url);
				webView.getEngine().load(url);
			}
		});

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				System.out.println("html:" + html);
				saveHtml(html, "전광판");
			}
		;
		});
		
		Separator vSeparator1 = new Separator();
		vSeparator1.setOrientation(Orientation.VERTICAL);
		vSeparator1.setPrefHeight(10);

		HBox urlHBox = new HBox(10);
		HBox naviTxtHBox = getNavigateText(webView);
		urlHBox.getChildren().addAll(naviTxtHBox);
		urlHBox.getChildren().addAll(urlTf);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(saveBtn);

		VBox vBox = new VBox(urlHBox, webView);
		vBox.autosize();
		vBox.setAlignment(Pos.TOP_CENTER);
//		Scene scene = new Scene(vBox, 1300, 800);
		return vBox;
	}

	public VBox getTab2() {
//		Label backLbl = new Label("←");
//		backLbl.setPrefWidth(50);
//		backLbl.setPrefHeight(25);
//		backLbl.setAlignment(Pos.TOP_LEFT);
//		backLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label
//
//		Label forwardLbl = new Label("→");
//		forwardLbl.setPrefWidth(50);
//		forwardLbl.setPrefHeight(25);
//		forwardLbl.setAlignment(Pos.TOP_LEFT);
//		forwardLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label
//
//		Label reloadLbl = new Label("⟳");
//		reloadLbl.setPrefWidth(50);
//		reloadLbl.setPrefHeight(25);
//		reloadLbl.setAlignment(Pos.TOP_LEFT);
//		reloadLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label

		String naverUrl = "https://www.naver.com";
		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);
		urlTf.setText(naverUrl);

		WebView webView = new WebView();
//		webView.setPrefHeight(800);
		webView.setMinHeight(800);
		webView.setPrefHeight(800);
		webView.setMaxHeight(1600);

//		webView.autosize();
		final WebEngine webengine;
		webengine = webView.getEngine();

		webengine.load(naverUrl);

//		webengine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			logger.debug("oldValue :" + oldValue + " newValue:" + newValue);
//			if (Worker.State.SUCCEEDED.equals(newValue)) {
//				logger.debug("webengine.getLocation:" + webengine.getLocation());
//				urlTf.setText(webengine.getLocation());
//			}
//		});
		webengine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable :" + observable + " oldState :" + oldState + " newState:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf.setText(webengine.getLocation());
			}
		});

		webengine.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					urlTf.setText(webengine.getLocation());
				}
			}
		});

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				System.out.println("html:" + html);
				// saveHtml(html, "전광판");
				try {
					saveCookies();
				} catch (NoSuchMethodException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		;
		});
		
		urlTf.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				System.out.println("code:" + event.getCode());
				System.out.println("code:" + event.getText());
				System.out.println("code:" + event.getCharacter());
				System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
				System.out.println("event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
				if (event.getCode().equals(KeyCode.ENTER)) {
					String url = urlTf.getText();
					System.out.println("url1:" + url);
					if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
						if (!url.contains(".") || url.contains(" ")) {
							url = "https://www.google.com/search?q=" + url + "&oq=" + url;
						} else {
							url = "http://" + url;
						}
					}
					System.out.println("url2:" + url);
					urlTf.setText(url);
					webengine.load(url);
					getNaverCookies();
				}
			}
		});

		urlTf.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			shareResultTxt.setText("...");
			if (oldValue.equals("https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1")
				&& newValue.equals("https://www.naver.com/")) {
				System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				// 네이버에 로그인하여 주소창에 주소가 변경되면 네이버 쿠키를 가져온다.
				getNaverCookies();
			}
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String url = urlTf.getText();
				System.out.println("url1:" + url);
				if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
					if (!url.contains(".") || url.contains(" ")) {
						url = "https://www.google.com/search?q=" + url + "&oq=" + url;
					} else {
						url = "http://" + url;
					}
				}
				System.out.println("url2:" + url);
				urlTf.setText(url);
				webengine.load(url);
			}
		});

		Button shareBtn = new Button("네이버 블로그 글쓰기");

		shareBtn.setOnMouseReleased(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
			}

		});

		shareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 글쓰기");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

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
					createHTMLFile(strUrl, myCommentTa.getText());
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

		Button stockPriceShareBtn = new Button("주식 시세 공유");
		stockPriceShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 공유");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

					String url = urlTf.getText();
					System.out.println("url1:" + url);
					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strNidAut, strNidSes);
					step2.schedulerStart();
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

		Button instantShareBtn = new Button("즉시 공유");
		instantShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 공유");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

					String url = urlTf.getText();
					System.out.println("url1:" + url);
					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strNidAut, strNidSes,
						true);
					step2.schedulerStart();
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

//		shareBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
//			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
//			@Override
//			public void handle(javafx.scene.input.MouseEvent event) {
//				//네이버 블로그 공유
//				System.out.println("네이버 블로그 글쓰기");
//				getNaverCookies();
//				logger.debug("strNidAut :" + strNidAut);
//				logger.debug("strNidSes :" + strNidSes);
//				if (!strNidAut.equals("") && !strNidSes.equals("")) {
//
//					String url = urlTf.getText();
//					System.out.println("url1:" + url);
//					if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
//						if (!url.contains(".") || url.contains(" ")) {
//							url = "https://www.google.com/search?q=" + url + "&oq=" + url;
//						} else {
//							url = "http://" + url;
//						}
//					}
//					System.out.println("url2:" + url);
//					createHTMLFile(url);
//				} else {
//					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
//					return;
//				}
//
//			}
//		});
//		HBox urlHBox = new HBox(backLbl, forwardLbl, reloadLbl, urlTf, goBtn);
		HBox urlHBox = new HBox(10);

		HBox naviTxtHBox = getNavigateText(webView);
		urlHBox.getChildren().addAll(naviTxtHBox);

		urlHBox.getChildren().addAll(urlTf);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(shareBtn);
		urlHBox.getChildren().addAll(stockPriceShareBtn);
		urlHBox.getChildren().addAll(instantShareBtn);
		urlHBox.getChildren().addAll(shareResultTxt);

		Text nidAutTxt = new Text("NID_AUT");
		nidAutTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		nidAutTf = new TextField();
		nidAutTf.setPrefWidth(800);
		nidAutTf.setPrefHeight(25);
		nidAutTf.setAlignment(Pos.TOP_LEFT);

		nBlogCategoryListComboBox1 = new ComboBox<String>();
		nBlogCategoryListComboBox1.getItems().addAll("266:쿠팡 상품 추천", "267:로켓배송", "268:로켓프레시", "269:로켓직구", "270:정기배송",
			"271:골드박스", "272:기획전", "274:카테고리별 베스트 상품", "275:PL 상품", "276:PL 브랜드별 상품", "277:추천 상품", "33:소개,알림,공지",
			"173:유행,트렌드,동향", "255:역사", "88:사회,문화", "198:국정교과서", "216:혼이비정상", "31:정치,정부,정책", "180:선거",
			"7:국외,해외,국제,세계", "249:북한", "236:미국", "228:중국", "237:일본", "2:경제,산업", "256:삼성", "260:현대", "141:부동산",
			"238:가상(암호)화폐", "250:투자썰전", "47:IT(Info Tech)", "258:BT(Bio Tech)", "259:NT(Nano Tech)", "199:카페베네",
			"131:증권", "265:미국", "146:증권↑↓↗↘", "153:특징주", "164:신고,신저가", "235:시간외단일가", "278:증권뉴스", "176:제약,약품,바이오",
			"264:IT(Info Tech)", "273:조선", "190:삼성주", "171:국민연금", "261:ETN,ETF", "188:핸디소프트", "253:Entertainment",
			"166:외국인 보유", "170:리포트,리서치", "172:상하한일수", "148:데이타", "155:Top 100", "159:기외 연속매수", "160:기외 연속매도",
			"156:기외 거래량", "161:기외 거래대금", "157:기외 양매수금", "162:기외 양매수량", "158:기외 양매도금", "163:기외 양매도량", "152:기획기사",
			"209:방송,언론", "210:JTBC", "201:뉴스공장", "202:파파이스", "206:스포트라이트", "150:건강", "207:치매", "29:비타민", "140:운동",
			"151:식당", "208:마약", "263:질병", "132:Manuka Honey", "9:음식,식료품", "262:환경", "142:사건,사고", "182:세월호",
			"234:4대강", "204:5촌살인사건", "241:MeToo", "243:갑질", "244:댓글사건", "121:오늘의 잠언", "177:오늘의 계시", "128:오늘의 성경",
			"120:오늘의 말씀", "149:오늘의 사진", "123:오늘의 영어", "178:주일,수요말씀", "245:인물", "197:문재인대통령", "189:노무현대통령",
			"225:인물1", "179:이승만", "183:박정희", "240:이명박", "185:박근혜", "193:이재명", "191:김기춘", "186:최태민", "200:김재규",
			"184:최순실", "229:장준하", "192:역사", "147:브렉시트", "145:자동차", "174:여행", "관광", "196:레져", "144:신앙", "181:종교",
			"230:과학", "111:LearningJava, 4Th", "94:자바 IO, NIO NetPrg", "50:Node.js 프로그래밍", "70:막힘없이배우는Java프로그래밍",
			"89:HTML5를 활용한 모바일웹앱", "90:1부.HTML5주요기능", "91:2부. jQueryMobile", "92:3부.Sencha Touch", "5:웹 프로그래밍",
			"127:모바일 프로그래밍", "130:모던웹을위한HTML5프로그래밍", "35:연예,엔터,재미", "129:해외직구", "32:쇼핑", "135:문화,예술", "3:음악",
			"139:미술", "49:영화", "6:연예", "8:책", "211:교양", "212:다큐", "213:교육", "46:보안", "24:패션", "37:뷰티", "19:디자인",
			"114:메르스", "25:생활", "10:스포츠", "30:동영상", "69:월남전", "43:영감의 시", "126:천국과지옥", "125:정명석선생님", "137:프로그램",
			"45:CSS", "87:Eclipse", "247:easyui", "93:Google", "44:HTML", "27:JavaScript", "26:Java", "42:jQuery",
			"248:NetBeans", "112:Node.js", "86:Spring", "246:Mybatis", "115:Swing", "39:Thymeleaf", "254:tomcat",
			"113:Software", "36:드라이버", "257:Freemarker", "133:데이터베이스", "41:Oracle", "48:MSSQL", "40:MySQL",
			"134:운영체제", "22:Windows", "21:Unix, Linux", "175:레오사진", "233:광고");
		nBlogCategoryListComboBox1.setPromptText("Please select one");

		HBox nidAutBox = new HBox(nidAutTxt, nidAutTf, nBlogCategoryListComboBox1);

		Text nidSesTxt = new Text("NID_SES");
		nidSesTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		nidSesTa = new TextArea();
		nidSesTa.setPrefWidth(800);
		nidSesTa.setPrefHeight(50);
		nidSesTa.setWrapText(true);
		HBox nidSesBox = new HBox(nidSesTxt, nidSesTa);

		Text myCommentTxt = new Text("My Comment");
		myCommentTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		myCommentTa = new TextArea();
		myCommentTa.setPrefWidth(800);
		myCommentTa.setPrefHeight(50);
		myCommentTa.setWrapText(true);
		HBox myCommentBox = new HBox(myCommentTxt, myCommentTa);

		VBox nidBox = new VBox(nidAutBox, nidSesBox, myCommentBox, saveBtn);

		VBox saveBtnBox = new VBox(saveBtn);
		saveBtnBox.setAlignment(Pos.CENTER);

		Separator vSeparator = new Separator();
		vSeparator.setOrientation(Orientation.VERTICAL);
		vSeparator.setPrefWidth(10);

		VBox vBox = new VBox(urlHBox, webView, vSeparator, nidBox, saveBtnBox);
//		VBox vBox = new VBox(urlTf, webView, separator, saveBtn);
//		vBox.autosize();
		vBox.setAlignment(Pos.TOP_LEFT);
		return vBox;
	}

	public VBox getTab3() {

		initList();
		initKeys();

		String naverUrl = "https://www.naver.com";
		TextField urlTf = new TextField();
		urlTf.setPrefWidth(800);
		urlTf.setPrefHeight(25);
		urlTf.setAlignment(Pos.TOP_LEFT);
		urlTf.setText(naverUrl);

		WebView webView = new WebView();
//		webView.setPrefHeight(800);
		webView.setMinHeight(400);
		webView.setPrefHeight(500);
		webView.setMaxHeight(600);

//		webView.autosize();
		final WebEngine webengine;
		webengine = webView.getEngine();

		webengine.load(naverUrl);

//		webengine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			logger.debug("oldValue :" + oldValue + " newValue:" + newValue);
//			if (Worker.State.SUCCEEDED.equals(newValue)) {
//				logger.debug("webengine.getLocation:" + webengine.getLocation());
//				urlTf.setText(webengine.getLocation());
//			}
//		});
		webengine.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable :" + observable + " oldState :" + oldState + " newState:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf.setText(webengine.getLocation());
			}
		});

		webengine.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					urlTf.setText(webengine.getLocation());
				}
			}
		});

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				System.out.println("html:" + html);
				// saveHtml(html, "전광판");
				try {
					saveCookies();
				} catch (NoSuchMethodException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (InvocationTargetException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (IllegalAccessException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (NoSuchFieldException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (ClassNotFoundException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		;
		});
		
		urlTf.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			@Override
			public void handle(javafx.scene.input.KeyEvent event) {
				System.out.println("code:" + event.getCode());
				System.out.println("code:" + event.getText());
				System.out.println("code:" + event.getCharacter());
				System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
				System.out.println("event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
				if (event.getCode().equals(KeyCode.ENTER)) {
					String url = urlTf.getText();
					System.out.println("url1:" + url);
					if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
						if (!url.contains(".") || url.contains(" ")) {
							url = "https://www.google.com/search?q=" + url + "&oq=" + url;
						} else {
							url = "http://" + url;
						}
					}
					System.out.println("url2:" + url);
					urlTf.setText(url);
					webengine.load(url);
					getNaverCookies();
				}
			}
		});

		urlTf.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			shareResultTxt.setText("...");
			if (oldValue.equals("https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1")
				&& newValue.equals("https://www.naver.com/")) {
				System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				// 네이버에 로그인하여 주소창에 주소가 변경되면 네이버 쿠키를 가져온다.
				getNaverCookies();
			}
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String url = urlTf.getText();
				System.out.println("url1:" + url);
				if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
					if (!url.contains(".") || url.contains(" ")) {
						url = "https://www.google.com/search?q=" + url + "&oq=" + url;
					} else {
						url = "http://" + url;
					}
				}
				System.out.println("url2:" + url);
				urlTf.setText(url);
				webengine.load(url);
			}
		});

		Button shareBtn = new Button("네이버 블로그 글쓰기");

		shareBtn.setOnMouseReleased(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
			}

		});

		shareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 글쓰기");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

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
					createHTMLFile(strUrl, myCommentTa.getText());
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

		Button stockPriceShareBtn = new Button("주식 시세 공유");
		stockPriceShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 공유");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

					String url = urlTf.getText();
					System.out.println("url1:" + url);
					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strNidAut, strNidSes);
					step2.schedulerStart();
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

		Button instantShareBtn = new Button("즉시 공유");
		instantShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt.setText("...");
				// 네이버 블로그 공유
				System.out.println("네이버 블로그 공유");
				getNaverCookies();
				logger.debug("strNidAut :" + strNidAut);
				logger.debug("strNidSes :" + strNidSes);
				if (!strNidAut.equals("") && !strNidSes.equals("")) {

					String url = urlTf.getText();
					System.out.println("url1:" + url);
					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strNidAut, strNidSes,
						true);
					step2.schedulerStart();
				} else {
					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					return;
				}

			}

		});

		HBox urlHBox = new HBox(10);

		HBox naviTxtHBox = getNavigateText(webView);
		urlHBox.getChildren().addAll(naviTxtHBox);

		urlHBox.getChildren().addAll(urlTf);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(shareBtn);
		urlHBox.getChildren().addAll(stockPriceShareBtn);
		urlHBox.getChildren().addAll(instantShareBtn);
		urlHBox.getChildren().addAll(shareResultTxt);

		Text nidAutTxt = new Text("NID_AUT");
		nidAutTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		nidAutTf = new TextField();
		nidAutTf.setPrefWidth(800);
		nidAutTf.setPrefHeight(25);
		nidAutTf.setAlignment(Pos.TOP_LEFT);

		nBlogCategoryListComboBox2 = new ComboBox<String>();
		nBlogCategoryListComboBox2.getItems().addAll("266:쿠팡 상품 추천", "267:로켓배송", "268:로켓프레시", "269:로켓직구", "270:정기배송",
			"271:골드박스", "272:기획전", "274:카테고리별 베스트 상품", "275:PL 상품", "276:PL 브랜드별 상품", "277:추천 상품", "33:소개,알림,공지",
			"173:유행,트렌드,동향", "255:역사", "88:사회,문화", "198:국정교과서", "216:혼이비정상", "31:정치,정부,정책", "180:선거",
			"7:국외,해외,국제,세계", "249:북한", "236:미국", "228:중국", "237:일본", "2:경제,산업", "256:삼성", "260:현대", "141:부동산",
			"238:가상(암호)화폐", "250:투자썰전", "47:IT(Info Tech)", "258:BT(Bio Tech)", "259:NT(Nano Tech)", "199:카페베네",
			"131:증권", "265:미국", "146:증권↑↓↗↘", "153:특징주", "164:신고,신저가", "235:시간외단일가", "278:증권뉴스", "176:제약,약품,바이오",
			"264:IT(Info Tech)", "273:조선", "190:삼성주", "171:국민연금", "261:ETN,ETF", "188:핸디소프트", "253:Entertainment",
			"166:외국인 보유", "170:리포트,리서치", "172:상하한일수", "148:데이타", "155:Top 100", "159:기외 연속매수", "160:기외 연속매도",
			"156:기외 거래량", "161:기외 거래대금", "157:기외 양매수금", "162:기외 양매수량", "158:기외 양매도금", "163:기외 양매도량", "152:기획기사",
			"209:방송,언론", "210:JTBC", "201:뉴스공장", "202:파파이스", "206:스포트라이트", "150:건강", "207:치매", "29:비타민", "140:운동",
			"151:식당", "208:마약", "263:질병", "132:Manuka Honey", "9:음식,식료품", "262:환경", "142:사건,사고", "182:세월호",
			"234:4대강", "204:5촌살인사건", "241:MeToo", "243:갑질", "244:댓글사건", "121:오늘의 잠언", "177:오늘의 계시", "128:오늘의 성경",
			"120:오늘의 말씀", "149:오늘의 사진", "123:오늘의 영어", "178:주일,수요말씀", "245:인물", "197:문재인대통령", "189:노무현대통령",
			"225:인물1", "179:이승만", "183:박정희", "240:이명박", "185:박근혜", "193:이재명", "191:김기춘", "186:최태민", "200:김재규",
			"184:최순실", "229:장준하", "192:역사", "147:브렉시트", "145:자동차", "174:여행", "관광", "196:레져", "144:신앙", "181:종교",
			"230:과학", "111:LearningJava, 4Th", "94:자바 IO, NIO NetPrg", "50:Node.js 프로그래밍", "70:막힘없이배우는Java프로그래밍",
			"89:HTML5를 활용한 모바일웹앱", "90:1부.HTML5주요기능", "91:2부. jQueryMobile", "92:3부.Sencha Touch", "5:웹 프로그래밍",
			"127:모바일 프로그래밍", "130:모던웹을위한HTML5프로그래밍", "35:연예,엔터,재미", "129:해외직구", "32:쇼핑", "135:문화,예술", "3:음악",
			"139:미술", "49:영화", "6:연예", "8:책", "211:교양", "212:다큐", "213:교육", "46:보안", "24:패션", "37:뷰티", "19:디자인",
			"114:메르스", "25:생활", "10:스포츠", "30:동영상", "69:월남전", "43:영감의 시", "126:천국과지옥", "125:정명석선생님", "137:프로그램",
			"45:CSS", "87:Eclipse", "247:easyui", "93:Google", "44:HTML", "27:JavaScript", "26:Java", "42:jQuery",
			"248:NetBeans", "112:Node.js", "86:Spring", "246:Mybatis", "115:Swing", "39:Thymeleaf", "254:tomcat",
			"113:Software", "36:드라이버", "257:Freemarker", "133:데이터베이스", "41:Oracle", "48:MSSQL", "40:MySQL",
			"134:운영체제", "22:Windows", "21:Unix, Linux", "175:레오사진", "233:광고");
		nBlogCategoryListComboBox2.setPromptText("Please select one");

		HBox nidAutBox = new HBox(nidAutTxt, nidAutTf, nBlogCategoryListComboBox2);

		Text nidSesTxt = new Text("NID_SES");
		nidSesTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		nidSesTa = new TextArea();
		nidSesTa.setPrefWidth(800);
		nidSesTa.setPrefHeight(50);
		nidSesTa.setWrapText(true);
		HBox nidSesBox = new HBox(nidSesTxt, nidSesTa);

		Text myCommentTxt = new Text("My Comment");
		myCommentTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		myCommentTa = new TextArea();
		myCommentTa.setPrefWidth(800);
		myCommentTa.setPrefHeight(50);
		myCommentTa.setWrapText(true);
		HBox myCommentBox = new HBox(myCommentTxt, myCommentTa);

		VBox nidBox = new VBox(nidAutBox, nidSesBox, myCommentBox, saveBtn);

		VBox saveBtnBox = new VBox(saveBtn);
		saveBtnBox.setAlignment(Pos.CENTER);

		Separator vSeparator = new Separator();
		vSeparator.setOrientation(Orientation.VERTICAL);
		vSeparator.setPrefWidth(10);

		HBox coupangKeyHbox = new HBox();

		Label accessKeyLbl = new Label("Access Key ");
		accessKeyLbl.setPrefHeight(25);
		accessKeyLbl.setAlignment(Pos.TOP_LEFT);
		accessKeyLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label

		accessKeyTf.setPrefWidth(400);
		accessKeyTf.setPrefHeight(25);
		accessKeyTf.setAlignment(Pos.TOP_LEFT);

		Separator hSeparator2 = new Separator();
		hSeparator2.setOrientation(Orientation.HORIZONTAL);
		hSeparator2.setPrefWidth(10);

		Label secretKeyLbl = new Label("Secret Key ");
		secretKeyLbl.setPrefHeight(25);
		secretKeyLbl.setAlignment(Pos.TOP_LEFT);
		secretKeyLbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label

		secretKeyTf.setPrefWidth(400);
		secretKeyTf.setPrefHeight(25);
		secretKeyTf.setAlignment(Pos.TOP_LEFT);

		Separator hSeparator3 = new Separator();
		hSeparator3.setOrientation(Orientation.HORIZONTAL);
		hSeparator3.setPrefWidth(10);

		Button keySaveBtn = new Button("Save");
		keySaveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				try {
					File f = new File("./coupangPartners.properties");
					FileWriter fw;
					fw = new FileWriter(f);
					fw.write("access_key=" + accessKeyTf.getText() + "\r\n");
					fw.write("secret_key=" + secretKeyTf.getText() + "\r\n");
					fw.flush();
					fw.close();
				} catch (IOException ex) {
					java.util.logging.Logger.getLogger(CoupangPartnersApiNaverLinkShare.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		;
		});
		
		coupangKeyHbox.getChildren().addAll(accessKeyLbl);
		coupangKeyHbox.getChildren().addAll(accessKeyTf);
		coupangKeyHbox.getChildren().addAll(hSeparator2);
		coupangKeyHbox.getChildren().addAll(secretKeyLbl);
		coupangKeyHbox.getChildren().addAll(secretKeyTf);
		coupangKeyHbox.getChildren().addAll(hSeparator3);
		coupangKeyHbox.getChildren().addAll(keySaveBtn);

		cb1 = new javafx.scene.control.CheckBox();
		cb2 = new javafx.scene.control.CheckBox();
		cb3 = new javafx.scene.control.CheckBox();
		cb4 = new javafx.scene.control.CheckBox();
		cb5 = new javafx.scene.control.CheckBox();

		Text coupangPrdtTxt1 = createText("카테고리 별 베스트 상품");
		Text coupangPrdtTxt2 = createText("카테고리ID");
		Text coupangPrdtTxt3 = createText("골드박스 상품");
		Text coupangPrdtTxt4 = createText("쿠팡 PL 상품");
		Text coupangPrdtTxt5 = createText("쿠팡 PL 브랜드 별 상품");
		Text coupangPrdtTxt6 = createText("브랜드ID");
		Text coupangPrdtTxt7 = createText("쿠팡 검색 상품");
		Text coupangPrdtTxt8 = createText("키워드");

		coupangPrdtTxt2.setStyle(FX_FONT_STYLE_RED);
		coupangPrdtTxt6.setStyle(FX_FONT_STYLE_RED);

		cCategoryListComboBox = new ComboBox<String>();
		cCategoryListComboBox.getItems().addAll("1001: 여성패션", "", "1002: 남성패션", "", "1003: 베이비패션 (0~3세)", "", "1004: 여아패션 (3세 이상)", "", "1005: 남아패션 (3세 이상)", "", "1006: 스포츠패션", "", "1007: 신발", "", "1008: 가방/잡화", "", "1010: 뷰티", "", "1011: 출산/유아동", "", "1012: 식품", "", "1013: 주방용품", "", "1014: 생활용품", "", "1015: 홈인테리어", "", "1016: 가전디지털", "", "1017: 스포츠/레저", "", "1018: 자동차용품", "", "1019: 도서/음반/DVD", "", "1020: 완구/취미", "", "1021: 문구/오피스", "", "1024: 헬스/건강식품", "", "1025: 국내여행", "", "1026: 해외여행", "", "1029: 반려동물용품");
		cCategoryListComboBox.setMinHeight(21);
		cCategoryListComboBox.setMinWidth(200);
		cCategoryListComboBox.setPromptText("전체");

		cBrandListComboBox = new ComboBox<String>();
		cBrandListComboBox.getItems().addAll("1001: 탐사", "1002: 코멧", "1003: Gomgom", "1004: 줌", "1005: 마케마케", "1006: 곰곰", "1007: 꼬리별", "1008: 베이스알파에센셜", "1009: 요놈", "1010: 비타할로", "1011: 비지엔젤", "1012: 타이니스타");
		cBrandListComboBox.setMinHeight(21);
		cBrandListComboBox.setMinWidth(200);
		cBrandListComboBox.setPromptText("전체");

		keywordTf = new TextField();
		Button keywordDelBtn = new Button("지우기");

		Button shareCoupangBtn = new Button("쿠팡 상품 공유");
		shareCoupangBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				bestcategoriesResultLbl.setText("");
				goldboxResultLbl.setText("");
				coupangPLResultLbl.setText("");
				coupangPLBrandResultLbl.setText("");
				searchResultLbl.setText("");

				save();
			}
		;
		});		

		bestcategoriesResultLbl = new Label("");
		goldboxResultLbl = new Label("");
		coupangPLResultLbl = new Label("");
		coupangPLBrandResultLbl = new Label("");
		searchResultLbl = new Label("");

		VBox prdtVbox = new VBox();
		HBox prdtHbox1 = new HBox();
		HBox prdtHbox2 = new HBox();
		HBox prdtHbox3 = new HBox();
		HBox prdtHbox4 = new HBox();
		HBox prdtHbox5 = new HBox();
		HBox prdtHbox6 = new HBox();

		prdtVbox.getChildren().addAll(prdtHbox1);
		prdtVbox.getChildren().addAll(prdtHbox2);
		prdtVbox.getChildren().addAll(prdtHbox3);
		prdtVbox.getChildren().addAll(prdtHbox4);
		prdtVbox.getChildren().addAll(prdtHbox5);
		prdtVbox.getChildren().addAll(prdtHbox6);

		prdtHbox1.getChildren().addAll(cb1);
		prdtHbox1.getChildren().addAll(coupangPrdtTxt1);
		prdtHbox1.getChildren().addAll(coupangPrdtTxt2);
		prdtHbox1.getChildren().addAll(cCategoryListComboBox);
		prdtHbox1.getChildren().addAll(bestcategoriesResultLbl);

		prdtHbox2.getChildren().addAll(cb2);
		prdtHbox2.getChildren().addAll(coupangPrdtTxt3);
		prdtHbox2.getChildren().addAll(goldboxResultLbl);

		prdtHbox3.getChildren().addAll(cb3);
		prdtHbox3.getChildren().addAll(coupangPrdtTxt4);
		prdtHbox3.getChildren().addAll(coupangPLResultLbl);

		prdtHbox4.getChildren().addAll(cb4);
		prdtHbox4.getChildren().addAll(coupangPrdtTxt5);
		prdtHbox4.getChildren().addAll(coupangPrdtTxt6);
		prdtHbox4.getChildren().addAll(cBrandListComboBox);
		prdtHbox4.getChildren().addAll(coupangPLBrandResultLbl);

		prdtHbox5.getChildren().addAll(cb5);
		prdtHbox5.getChildren().addAll(coupangPrdtTxt7);
		prdtHbox5.getChildren().addAll(coupangPrdtTxt8);
		prdtHbox5.getChildren().addAll(keywordTf);
		prdtHbox5.getChildren().addAll(keywordDelBtn);
		prdtHbox5.getChildren().addAll(searchResultLbl);

		prdtHbox6.getChildren().addAll(shareCoupangBtn);

		VBox vBox = new VBox(urlHBox, webView, hSeparator3, nidBox, saveBtnBox, coupangKeyHbox, prdtVbox);
//		VBox vBox = new VBox(urlTf, webView, separator, saveBtn);
//		vBox.autosize();
		vBox.setAlignment(Pos.TOP_LEFT);
		return vBox;
	}

	public void saveHtml(String contentHtml, String title) {
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
									if (!strStockName.startsWith("ARIRANG") && !strStockName.startsWith("KINDEX")
										&& !strStockName.startsWith("TIGER") && !strStockName.startsWith("KBSTAR")
										&& !strStockName.startsWith("SMART") && !strStockName.startsWith("KODEX")
										&& !strStockName.startsWith("TREX") && !strStockName.startsWith("HANARO")
										&& !strStockName.startsWith("KOSEF") && !strStockName.contains("코스피")
										&& !strStockName.contains("레버리지") && !strStockName.contains("S&P")
										&& !strStockName.contains("마이다스") && !strStockName.contains("고배당")
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
					// specialLetter ↑↓ ▲ ▼ -
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
		// String jsonArray = JSONArray.toJSONString(stockVoList);
		// jsonArray=list
		List<Map> stockMapList = new ArrayList<>();
		stockMapList.add(stockMap);
//		String jsonArray = JSONArray.toJSONString(stockMapList);
		String jsonArray = stockMapList.toArray().toString();
		// jsonObject=map
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

	private void saveCookies() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
		NoSuchFieldException, ClassNotFoundException, IOException {
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
			System.out.println("key:" + key);
			Map cookies = (Map) pair.getValue();
			System.out.println("cookies.values():" + cookies.values());
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
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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

	private void createHTMLFile(String strUrl) {
		createHTMLFile(strUrl, myCommentTa.getText());
	}

	private void createHTMLFile(String strUrl, String strMyComment) {
		if (strUrl.equals("")) {
			return;
		}
		System.out.println("createHTMLFile strUrl:" + strUrl);
		// tab2에서 페이지 이동
		int idx = 0;
		String newsCompany = "";
		for (NewsPublisher np : NewsPublisher.values()) {
			String newsPublisherDomain = np.getName();
			idx = np.ordinal();
			if (strUrl.contains(newsPublisherDomain)) {
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
			// c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs );
//			Method method = c.getDeclaredMethod("createHTMLFile", String.class);
//			sb = (StringBuilder) method.invoke(String.class, new Object[]{url});
			Method method = c.getDeclaredMethod("createHTMLFile", String.class, String.class);
			sb = (StringBuilder) method.invoke(String.class, new Object[]{strUrl, strMyComment});
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.INFO, sb.toString());
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
			| NoSuchMethodException | SecurityException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			return;
		}

		Document htmlDoc = Jsoup.parse(sb.toString());
		logger.debug("htmlDoc:" + htmlDoc.html());

		htmlDoc.select("meta").remove();
		sb.delete(0, sb.length());
		sb.setLength(0);

		String strCategoryNo = "33";
		String strCategoryName = "증권";
		String strSelectedCategory = String.valueOf(nBlogCategoryListComboBox1.getSelectionModel().getSelectedItem());
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
		logger.debug("strShareTitle:" + strShareTitle);
		if (strShareTitle.equals("")) {
			return;
		}
		Elements shareUrlEls = htmlDoc.select("a");
		String strShareUrl = "";
		if (shareUrlEls.size() > 0) {
			strShareUrl = htmlDoc.select("a").first().attr("href");
		}
		if (strShareUrl.equals("")) {
			return;
		}
		StringBuilder contentSb = new StringBuilder();
		contentSb.append(htmlDoc.html());
		contentSb.toString();

		logger.debug("strShareUrl:" + strShareUrl);

		if (naverBlogLinkShare(contentSb, strCategoryName, strShareTitle, strShareUrl)) {
			shareResultTxt.setText("블로그 글쓰기 성공");
		} else {
			shareResultTxt.setText("블로그 글쓰기 실패");
		}
		myCommentTa.setText("...");

	}
	
	public void naverBlogLinkShare(StringBuilder contentSb, String strCategoryName, String strShareTitle) {
		strNidAut = nidAutTf.getText();
		strNidSes = nidSesTa.getText();
		String strShareUrl = "";
		NaverUtil.naverBlogLinkShare(strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName, contentSb, null);
	}
	
	public boolean naverBlogLinkShare(StringBuilder contentSb, String strCategoryName, String strShareTitle, String strShareUrl) {
		strNidAut = nidAutTf.getText();
		strNidSes = nidSesTa.getText();
		return NaverUtil.naverBlogLinkShare(strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName, contentSb, null);
	}

	private void initList() {
		try {
			url = new URL(strCoupangHomeUrl);
			strProtocol = url.getProtocol();
			strHost = url.getHost();
			iPort = url.getPort();
			strPath = url.getPath();
			System.out.println("strPath:" + strPath);
		} catch (MalformedURLException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void initKeys() {
		Properties props = new Properties();
		String accessKey = "";
		String secretKey = "";
		InputStream is = null;
		try {
			System.out.println("getClass().getProtectionDomain().getCodeSource().getLocation().getPath():"
				+ getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			System.out.println("getClass().getProtectionDomain().getClassLoader().getResource(\"coupangPartners.properties\"):"
				+ getClass().getProtectionDomain().getClassLoader().getResource("coupangPartners.properties"));

			//jar를 실행하였을 경우는 jar와 동일 경로
			//ide에서 실행하였을 경우에는 프로젝트 경로
			//프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			System.out.println(". AbsolutePath:" + new File(".").getAbsolutePath());
			File f = new File("./coupangPartners.properties");
			System.out.println("f.exists():" + f.exists());
			if (f.exists()) {
				is = new FileInputStream(f);
				props.load(is);
				System.out.println("props :" + props);
				accessKey = (String) props.get("access_key");
				secretKey = (String) props.get("secret_key");
				System.out.println("accessKey :" + accessKey);
				System.out.println("secretKey :" + secretKey);
				if (accessKey.equals("") || secretKey.equals("")) {
					//classes root 경로
					is = getClass().getResourceAsStream("/coupangPartners.properties");
					System.out.println("class 경로 read /coupangPartners.properties Resource");
				}
			} else {
				//classes root 경로
				is = getClass().getResourceAsStream("/coupangPartners.properties");
				System.out.println("class 경로 read /coupangPartners.properties Resource");
			}
			System.out.println("is :" + is);
			if (is != null) {
				props.load(is);
				System.out.println("props :" + props);
				accessKey = (String) props.get("access_key");
				secretKey = (String) props.get("secret_key");

				System.out.println("access key2 :" + accessKey);
				System.out.println("secret key2 :" + secretKey);
				accessKeyTf.setText(accessKey);
				secretKeyTf.setText(secretKey);
				PropertyConfigurator.configure(props);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setLabelAtt(Label lbl) {
//			lbl.setText("");
		lbl.setPrefWidth(150);
		lbl.setPrefHeight(25);
		lbl.setAlignment(Pos.TOP_LEFT);
		lbl.setFont(new Font(FONT_FAMILY, MAX_FONT_SIZE)); // set to Label
	}

	public Text createText(String strTxt) {
		Text txt = new Text(strTxt);
		txt.setStyle(FX_FONT_STYLE_DEFAULT);
		return txt;
	}

	public void save() {
		ACCESS_KEY = accessKeyTf.getText();
		SECRET_KEY = secretKeyTf.getText();
		boolean bResult = false;

		int selectedCheckboxCount = countCheckedCheckBoxes();
		if (selectedCheckboxCount == 0) {
			JOptionPane.showMessageDialog(null, "선택한 항목이 없습니다.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		}

		strNidAut = nidAutTf.getText();
		strNidSes = nidSesTa.getText();

		if (strNidAut.equals("")) {
			JOptionPane.showMessageDialog(null, "NID_AUT를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (strNidSes.equals("")) {
			JOptionPane.showMessageDialog(null, "NID_SES를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cb1.isSelected()) {
			String item = String.valueOf(cCategoryListComboBox.getSelectionModel().getSelectedItem());
			System.out.println("item:" + item);
			if (item.contains(":")) {
				String categoryId = item.substring(0, item.indexOf(":"));
				String categoryNm = item.substring(item.indexOf(":") + 1);
				System.out.println("categoryId:[" + categoryId + "]");
				System.out.println("categoryNm:[" + categoryNm + "]");
				bResult = getBestcategoryProducts(categoryId, categoryNm);
				if (bResult) {
					bestcategoriesResultLbl.setText("처리 완료");
				}
			} else {
				bResult = getBestcategoryProducts();
				if (bResult) {
					bestcategoriesResultLbl.setText("처리 완료");
				}
			}
		}

		if (cb2.isSelected()) {
			bResult = getGoldboxProducts();
			if (bResult) {
				goldboxResultLbl.setText("처리 완료");
			}
		}

		if (cb3.isSelected()) {
			bResult = getCoupangPLProducts();
			if (bResult) {
				coupangPLResultLbl.setText("처리 완료");
			}
		}

		if (cb4.isSelected()) {
			// getCoupangPLBrandProducts();
			String item = String.valueOf(cBrandListComboBox.getSelectionModel().getSelectedItem());
			System.out.println("item:" + item);
			if (item.contains(":")) {
				String brandId = item.substring(0, item.indexOf(":"));
				String brandNm = item.substring(item.indexOf(":") + 1);
				System.out.println("brandId:" + brandId);
				System.out.println("brandNm:" + brandNm);
				bResult = getCoupangPLBrandProducts(brandId, brandNm);
				if (bResult) {
					coupangPLBrandResultLbl.setText("처리 완료");
				}
			} else {
				bResult = getCoupangPLBrandProducts();
				if (bResult) {
					coupangPLBrandResultLbl.setText("처리 완료");
				}
			}
		}

		if (cb5.isSelected()) {
			String keyword = keywordTf.getText();
			bResult = getSearchProducts(keyword);
			if (bResult) {
				searchResultLbl.setText("처리 완료");
			}
		}
	}

		int countCheckedCheckBoxes() {

		int checkNums = 0;
		if (cb1.isSelected()) {
			checkNums++;
		}
		if (cb2.isSelected()) {
			checkNums++;
		}
		if (cb3.isSelected()) {
			checkNums++;
		}
		if (cb4.isSelected()) {
			checkNums++;
		}
		if (cb5.isSelected()) {
			checkNums++;
		}
		return checkNums;
	}

	// 카테고리별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
	// BESTCATEGORIES_URL = API_PATH + "​/products​/bestcategories​/{categoryId}";
	public boolean getBestcategoryProducts(String categoryId, String categoryNm) {
		String bestcategoriesUrl = "";
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		String shareTitle = strYmdBlacket + " " + "카테고리별 베스트상품(" + categoryNm + ") TOP" + limit;
		sb.append("<div style='width:100%;'><h1>").append(shareTitle).append("</h1></div>");
		System.out.println(categoryId + ":" + categoryNm);
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		bestcategoriesUrl = BESTCATEGORIES_URL + categoryId + "?limit=" + limit;
		String data = getData("카테고리별 베스트상품", bestcategoriesUrl, categoryNm, strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String strBlogCategoryName = "카테고리별 베스트 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);

		return true;
	}

	public StringBuilder getBestcategoryProducts(int idx, String categoryId, String categoryNm, int limit) {
		String bestcategoriesUrl = "";
		StringBuilder sb = new StringBuilder();
		System.out.println((idx + 1) + "." + categoryId + ":" + categoryNm);
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		bestcategoriesUrl = BESTCATEGORIES_URL + categoryId + "?limit=" + limit;
		String data = getData("카테고리별 베스트상품", bestcategoriesUrl, categoryNm, strParamJson);
		sb.append(data);
		return sb;
	}

	public boolean getBestcategoryProducts() {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmdBlacket).append(" ").append("카테고리별 베스트 상품")
			.append(limit).append("</h1></div>");
		for (int i = 0; i < bestCategoriesArray.length; i++) {
			String codeValue[] = bestCategoriesArray[i];
			String categoryId = "";
			String categoryNm = "";
			if (codeValue.length == 2) {
				categoryId = codeValue[0].trim();
				categoryNm = codeValue[1].trim();
				StringBuilder sb2 = getBestcategoryProducts(i, categoryId, categoryNm, limit);
				sb.append(sb2);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String shareTitle = strYmdBlacket + " " + "카테고리별 베스트상품";
		String strBlogCategoryName = "카테고리별 베스트 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);

		return true;
	}

	// 골드박스 상품에 대한 상세 상품 정보를 생성합니다. (골드박스 상품은 매일 오전 7:30에 업데이트 됩니다)
	// GOLDBOX_URL = API_PATH + "​/products​/goldbox";
	public boolean getGoldboxProducts() {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;float:left;'><h1>").append(strYmdBlacket).append(" ")
			.append("WOW 와우회원 전용 매일 오전 7시 골드박스 1일특가").append("</h1></div>");
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		String data = getData("골드박스 상품", GOLDBOX_URL, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String shareTitle = strYmdBlacket + " " + "WOW 와우회원 전용 매일 오전 7시 골드박스 1일특가";
		String strBlogCategoryName = "골드박스";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);

		return true;
	}

	// 쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
	// COUPANG_PL_URL = API_PATH + "​​/products​/coupangPL";
	public boolean getCoupangPLProducts() {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;float:left;'><h1>").append(strYmdBlacket).append(" ").append("쿠팡 PL 상품 TOP")
			.append(limit).append("</h1></div>");
		String strParamJson = "{\"limit\": \"" + limit + "\"}";
		System.out.println("strParamJson:" + strParamJson);
		String data = getData("쿠팡PL상품", COUPANG_PL_URL, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String shareTitle = strYmdBlacket + " " + "쿠팡 PL 상품 TOP" + limit;
		String strBlogCategoryName = "PL 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);

		return true;
	}

	// 쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
	// COUPANG_PL_BRAND_URL = API_PATH + "​​/products​/coupangPL​/{brandId}";
	public boolean getCoupangPLBrandProducts(String brandId, String brandNm) {
		String server_url = "";
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		System.out.println(brandId + ":" + brandNm);
		server_url = COUPANG_PL_BRAND_URL + brandId + "?limit=" + limit;
		String strParamJson = "";
		String data = getData("쿠팡PL브랜드상품", server_url, brandNm, strParamJson);
		sb.append(data);

		String shareTitle = strYmdBlacket + " " + "쿠팡 PL 브랜드별(" + brandNm + ") 상품 TOP" + limit;
		String strBlogCategoryName = "PL 브랜드별 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);
		return true;
	}

	public StringBuilder getCoupangPLBrandProducts(int idx, String brandId, String brandNm, int limit) {
		String server_url;
		StringBuilder sb = new StringBuilder();
		System.out.println((idx + 1) + "." + brandId + ":" + brandNm);
		server_url = COUPANG_PL_BRAND_URL + brandId + "?limit=" + limit;
		String strParamJson = "";
		String data = getData("쿠팡PL브랜드상품", server_url, brandNm, strParamJson);
		sb.append(data);
		return sb;
	}

	public boolean getCoupangPLBrandProducts() {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmdBlacket).append(" ").append("쿠팡 PL 브랜드별 상품 TOP")
			.append(limit).append("</h1></div>");
		for (int i = 0; i < coupangPlBrandArray.length; i++) {
			String codeValue[] = coupangPlBrandArray[i];
			String brandId;
			String brandNm;
			if (codeValue.length == 2) {
				brandId = codeValue[0].trim();
				brandNm = codeValue[1].trim();
				StringBuilder sb2 = getCoupangPLBrandProducts(i, brandId, brandNm, limit);
				sb.append(sb2);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String shareTitle = strYmdBlacket + " " + "쿠팡 PL 브랜드별 상품 TOP" + limit;
		String strBlogCategoryName = "PL 브랜드별 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);

		return true;
	}

	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
	// SEARCH_URL = API_PATH + "​/products​/search";
	public boolean getSearchProducts(String keyword) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmdBlacket).append(" ").append("상품검색:").append(keyword)
			.append("</h1></div>");
		String strParamJson = "";
		int limit = 20;
		String encodedKeyword = "";
		try {
			encodedKeyword = URLEncoder.encode(keyword, "UTF8");
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		SEARCH_URL = SEARCH_URL + "?keyword=" + encodedKeyword + "&limit=" + limit;
//		server_url = server_url + "?keyword=food&limit=" + limit;
		System.out.println("server_url:" + SEARCH_URL);
		String data = getData("상품검색", SEARCH_URL, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		String shareTitle = strYmdBlacket + " " + "상품검색";
		String strBlogCategoryName = "추천 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);
		return true;
	}

	public String getData(String apiGubun, String server_url, String categoryNm, String strParamJson) {
		System.out.println("server_url :" + server_url);
		StringBuilder sb = new StringBuilder();
		// Generate HMAC string
		String authorization = generate(REQUEST_METHOD_GET, server_url, ACCESS_KEY, SECRET_KEY);
		System.out.println("authorization:" + authorization);
		// Send request
		StringEntity entity = new StringEntity(strParamJson, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		org.apache.http.HttpHost host = org.apache.http.HttpHost.create(DOMAIN);
		org.apache.http.HttpRequest request = RequestBuilder.get(server_url).setEntity(entity)
			.addHeader("Authorization", authorization).build();

		org.apache.http.HttpResponse httpResponse;
		try {
			httpResponse = HttpClientBuilder.create().build().execute(host, request);
			// verify
			String returnJson = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("returnJson :" + returnJson);
			JSONObject jsonObject = new JSONObject(returnJson);
			System.out.println("jsonObject:" + jsonObject.toString());
			Set keySet = jsonObject.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
//				System.out.println("key:" + key);
				Object obj = jsonObject.get(key);
//				System.out.println("value:" + obj.toString());
				System.out.println(key + ":" + obj);
				if (key.equals("data")) {
					JSONArray data = null;

					if (apiGubun.equals("상품검색")) {
						JSONObject jObject = (JSONObject) jsonObject.get("data");
						data = (JSONArray) jObject.get("productData");
					} else {
						data = (JSONArray) jsonObject.get("data");

					}
					System.out.println("data.length:" + data.length());
					if (!categoryNm.equals("")) {
						sb.append("<div style='width:100%;padding-top:20px;float:left;'><h3>").append(" ")
							.append(categoryNm).append("</h3></div>");
					}
					sb.append("<div style='width:100%;float:left;'>");
					sb.append("<ul style='list-style:none;padding-left:0'>");
					for (int i = 0; i < data.length(); i++) {
						JSONObject dataObj2 = (JSONObject) data.get(i);
						Set keySet2 = dataObj2.keySet();
						Iterator it2 = keySet2.iterator();

						String productImage = "";
						String productId = "";
						String productUrl = "";
						String categoryName = "";
						String productName = "";
						String productPrice = "";
						String strProductPriceWithComma = "";
						boolean isRocket = false;
						String discountRate = "";
						String originalPrice = "";
						String rank = "";

						sb.append(
							"<li style='float:left;width:250px;height:430px;background-color: #fff; box-shadow: none; border: 1px solid #dfe1e5; border-radius: 8px; overflow: hidden; margin: 0 0 6px 0;margin-right:8px;margin-top:1px;padding:5px 10px;'>");
						while (it2.hasNext()) {
							String key2 = (String) it2.next();
//							System.out.println("key2:" + key2);
							Object obj2 = dataObj2.get(key2);
//							System.out.println("obj2:" + obj2.toString());
							System.out.println(key2 + ":" + obj2);
							if (key2.equals("productImage")) {
								productImage = obj2.toString();
							}
							if (key2.equals("productId")) {
								productId = obj2.toString();
							}
							if (key2.equals("productUrl")) {
								productUrl = obj2.toString();
								Document doc = Jsoup.connect(productUrl).timeout(0).userAgent("Opera").get();
								discountRate = doc.select(".prod-price .prod-origin-price .discount-rate").text();
								if (discountRate.equals("%")) {
									discountRate = "";
								}
								if (discountRate.contains(" ")) {
									discountRate = discountRate.substring(0, discountRate.indexOf(" "));
								}
								originalPrice = doc.select(".prod-price .prod-origin-price .origin-price").text();
								if (originalPrice.equals("원")) {
									originalPrice = "";
								}
								if (originalPrice.contains(" ")) {
									originalPrice = originalPrice.substring(0, originalPrice.indexOf(" "));
								}
								System.out.println("discountRate :" + discountRate);
								System.out.println("originalPrice :" + originalPrice);
							}
							if (key2.equals("categoryName")) {
								categoryName = obj2.toString();
							}
							if (key2.equals("productName")) {
								productName = obj2.toString();
							}
							if (key2.equals("productPrice")) {
								productPrice = obj2.toString();
								strProductPriceWithComma = df.format(Integer.parseInt(productPrice));
							}
							if (key2.equals("isRocket")) {
								isRocket = (boolean) obj2;
							}
							if (key2.equals("rank")) {
								rank = obj2.toString();
							}
						}
						if (!rank.equals("")) {
							sb.append(
								"<span style='overflow: hidden;display: block; left: 6px;top: 5px;width: 30px;height: 30px;text-indent: 0.5em; color:#fff;background-color:#f00;'>")
								.append(rank).append("</span>");
						}
						sb.append("<a href='").append(productUrl)
							.append("' target='new' style='text-decoration:none;'>");
						sb.append("<div>");
						sb.append("<img src='").append(productImage).append("' style='width:230px;height:230px;'>");
						sb.append("</div>");

						if (apiGubun.equals("골드박스 상품")) {
							sb.append("<div>");
							sb.append(
								"<img src='http://image8.coupangcdn.com/image/badges/falcon/v1/web/rocketwow-bi-16@2x.png' alt='로켓와우' style='width:79px;height:20px;'>");
							sb.append("</div>");
						}

						sb.append("<div>");
						sb.append(productName);
						sb.append("</div>");
						if (!discountRate.equals("")) {
							sb.append(
								"<div style='font-size:20px;color:red;background-color:yellow;text-align:center;font-weight:bold;'>");
							sb.append(discountRate + "↓");
							sb.append("</div>");
						}
						sb.append("<div style='color:#888;text-decoration:line-through;'>");
						sb.append(originalPrice);
						sb.append("</div>");
						sb.append("<div style='font-size:16px;font-family:Tahoma;color: #ae0000;'>");
						sb.append(strProductPriceWithComma).append("원");
						if (isRocket) {
							sb.append("<span class='badge rocket'>");
							sb.append(
								"<img src='http://image10.coupangcdn.com/image/badges/rocket/rocket_logo.png' height='16' alt='로켓배송'>");
							sb.append("</span>");
						}
						sb.append("</div>");
						if (apiGubun.equals("골드박스 상품")) {
							sb.append("<div style='color:#ae0000;font-size:14px;'>");
							sb.append("로켓와우회원가");
							sb.append("</div>");
						}
						sb.append("</a>");
						sb.append("</li>");
						System.out.println("____________________________________");

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ex) {
							java.util.logging.Logger.getLogger(CoupangPartnersApiOneFileNaverLinkShareSimple.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					sb.append("</ul>");
					sb.append("</div>");
				}

			}

		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		return sb.toString();
	}

}
