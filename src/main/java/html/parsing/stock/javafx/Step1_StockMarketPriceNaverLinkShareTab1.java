package html.parsing.stock.javafx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.coupang.partners.HmacGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.webkit.network.CookieManager;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.NewsPublisher;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;
import html.parsing.stock.util.StockUtil;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Step1_StockMarketPriceNaverLinkShareTab1 extends Application {

	private final static String COUPANG_PARTNERS_NOTICE = "<div>※ 쿠팡 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있습니다.</div>";
	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(Step1_StockMarketPriceNaverLinkShareTab1.class);

	static String homeUrl = "";
	final static String displayBoardUrl = "https://finance.daum.net/domestic/all_quotes";
	final static String afterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String naverLoginUrl1 = "https://nid.naver.com/nidlogin.login?mode=form&svctype=40960&url=https%3A%2F%2Fwww.naver.com";
	final static String naverLoginUrl2 = "https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com";
	final static String naverUrl = "https://www.naver.com/";
	final static String coupangUrl = "https://www.coupang.com";

	final static String daumKospiAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSPI";
	final static String daumKosdaqAfterHoursUrl = "https://finance.daum.net/domestic/after_hours?market=KOSDAQ";

	final String FONT_FAMILY = "Arial"; // define font family you need
	final String FX_FONT_STYLE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;";
	final double MAX_FONT_SIZE = 15.0; // define max font size you need
	final String FX_FONT_STYLE_DEFAULT = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: black ;";
	final String FX_FONT_STYLE_RED = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: red ;";
	final String FX_FONT_STYLE_LARGE = "-fx-font-family: 'Arial';-fx-font-size: 20px;-fx-font-weight: bold;-fx-fill: black ;";
	final String FX_FONT_STYLE_NAVER_LOG_ON = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: green ;";
	final String FX_FONT_STYLE_NAVER_LOG_OFF = "-fx-font-family: 'Arial';-fx-font-size: 15px;-fx-font-weight: bold;-fx-fill: red ;";

	private String strBlogId;
	private String isLogin;
	String strNidAut = "";
	String strNidSes = "";

	TextArea myCommentTa1;

	TextArea myCommentTa2;

	TextField accessKeyTf;
	TextField secretKeyTf;
	Text shareResultTxt1;

	javafx.scene.control.CheckBox cb1;
	javafx.scene.control.CheckBox cb2;
	javafx.scene.control.CheckBox cb3;
	javafx.scene.control.CheckBox cb4;
	javafx.scene.control.CheckBox cb5;

	ComboBox<String> naverBlogCategoryListComboBox;
	ComboBox<String> coupangCategoryListComboBox;
	ComboBox<String> coupangBrandListComboBox;

	Label bestcategoriesResultLbl;
	Label goldboxResultLbl;
	Label coupangPLResultLbl;
	Label coupangPLBrandResultLbl;
	Label searchResultLbl;

	TextField keywordTf;

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

	Calendar cal = Calendar.getInstance();
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

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

	String strNaverBlogCategoryNo = "";
	String strNaverBlogCategoryName = "";

	String strFileName;
	URI uri = null;
	URL url = null;
	String strProtocol = null;
	String strHost = null;
	String strProtocolHost = null;
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

	// Replace with your own ACCESS_KEY and SECRET_KEY
	private String ACCESS_KEY = "";
	private String SECRET_KEY = "";

	private final static String REQUEST_METHOD_GET = "GET";
	private final static String DOMAIN = "https://api-gateway.coupang.com";
	private final static String API_PATH = "/v2/providers/affiliate_open_api/apis/openapi/v1";

	private static DecimalFormat df = new DecimalFormat("#,##0");

	// GET
	// 카테고리 별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
//	private final static String BESTCATEGORIES_URL = API_PATH + "​/products​/bestcategories​/{categoryId}";
	private final static String BESTCATEGORIES_URL = API_PATH + "/products/bestcategories/";
	private final static String[][] bestCategoriesArray = { { "1001", "여성패션" }, { "1002", "남성패션" },
			{ "1003", "베이비패션 (0~3세)" }, { "1004", "여아패션 (3세 이상)" }, { "1005", "남아패션 (3세 이상)" }, { "1006", "스포츠패션" },
			{ "1007", "신발" }, { "1008", "가방/잡화" }, { "1010", "뷰티" }, { "1011", "출산/유아동" }, { "1012", "식품" },
			{ "1013", "주방용품" }, { "1014", "생활용품" }, { "1015", "홈인테리어" }, { "1016", "가전디지털" }, { "1017", "스포츠/레저" },
			{ "1018", "자동차용품" }, { "1019", "도서/음반/DVD" }, { "1020", "완구/취미" }, { "1021", "문구/오피스" },
			{ "1024", "헬스/건강식품" }, { "1025", "국내여행" }, { "1026", "해외여행" }, { "1029", "반려동물용품" } };
	// 골드박스 상품에 대한 상세 상품 정보를 생성합니다. (골드박스 상품은 매일 오전 7:30에 업데이트 됩니다)
	private final static String GOLDBOX_URL = API_PATH + "/products/goldbox";
	// 쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
	private final static String COUPANG_PL_URL = API_PATH + "/products/coupangPL";
	// 쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
//	private final static String COUPANG_PL_BRAND_URL = API_PATH +"/products/coupangPL/{brandId}";
	private final static String COUPANG_PL_BRAND_URL = API_PATH + "/products/coupangPL/";
	private final static String coupangPlBrandArray[][] = { { "1001", "탐사" }, { "1002", "코멧" }, { "1003", "Gomgom" },
			{ "1004", "줌" }, { "1005", "마케마케" }, { "1006", "곰곰" }, { "1007", "꼬리별" }, { "1008", "베이스알파에센셜" },
			{ "1009", "요놈" }, { "1010", "비타할로" }, { "1011", "비지엔젤" }, { "1012", "타이니스타" } };
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
	TabPane tabPane = null;
	// This is our ObservableList that will hold our ComboBox items
	private ObservableList<String> items = FXCollections.observableArrayList();

	TextField urlTf1 = new TextField();
	TextField urlTf2 = new TextField();
	TextField urlTf3 = new TextField();
	TextField urlTf4 = new TextField();
	TextField urlTf5 = new TextField();

	WebView webView1 = null;
	WebView webView2 = null;
	WebView webView3 = null;
	WebView webView4 = null;
	WebView webView5 = null;

	WebEngine webEngine1 = null;
	WebEngine webEngine2 = null;
	WebEngine webEngine3 = null;
	WebEngine webEngine4 = null;
	WebEngine webEngine5 = null;

	HBox onAndOffHBox = new HBox();
	HBox mainTopHBox = new HBox();
	Text onAndOffTxt = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("엔젤 브라우저");

		tabPane = new TabPane();

		Tab loginTab = new Tab("네이버 로그인", getNaverLoginTab());
		Tab tab1 = new Tab("Daum 전종목 시세", getStockBoardTab());
		Tab afterHourTab = new Tab("Daum 시간외단일가", getAfterHourTab());
		Tab tab2 = new Tab("Naver Home", getNaverTab());
		Tab tab3 = new Tab("Coupang Partner", getCoupangTab());

		tabPane.getTabs().add(loginTab);
		tabPane.getTabs().add(tab1);
		tabPane.getTabs().add(afterHourTab);
		tabPane.getTabs().add(tab2);
		tabPane.getTabs().add(tab3);

		int numTabs = tabPane.getTabs().size();
		System.out.println("numTabs:" + numTabs);

		addMainTopBox();

		VBox mainVBox = new VBox();
		mainVBox.getChildren().addAll(mainTopHBox);
		mainVBox.getChildren().addAll(tabPane);

		Scene scene = new Scene(mainVBox, 1300, 1000);

		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void addMainTopBox() {
		Button naverBlogShareBtn = new Button("네이버 블로그 공유");

		naverBlogShareBtn.setOnMouseReleased(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt1.setText("...");
			}

		});

		naverBlogShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {

				TextField urlTf = null;
				int selectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();
				switch (selectedTabIndex) {
				case 0:
					urlTf = urlTf1;
					urlTf.setText(webEngine1.getLocation());
					break;
				case 1:
					urlTf = urlTf2;
					urlTf.setText(webEngine2.getLocation());
					break;
				case 2:
					urlTf = urlTf3;
					urlTf.setText(webEngine3.getLocation());
					break;
				case 3:
					urlTf = urlTf4;
					urlTf.setText(webEngine4.getLocation());
					break;
				case 4:
					urlTf = urlTf5;
					urlTf.setText(webEngine5.getLocation());
					break;
				default:
					break;
				}

				mainTopHBox.getChildren().remove(shareResultTxt1);
				shareResultTxt1 = new Text();
				shareResultTxt1.setText("...");
				mainTopHBox.getChildren().addAll(shareResultTxt1);

				// 네이버 블로그 공유
				System.out.println("네이버 블로그 글쓰기");
				getNaverCookies();
				logger.debug("strNidAut1 :" + strNidAut);
				logger.debug("strNidSes1 :" + strNidSes);
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

					switch (selectedTabIndex) {
					case 0:
						urlTf1.setText(strUrl);
						break;
					case 1:
						urlTf2.setText(strUrl);
						break;
					case 2:
						urlTf3.setText(strUrl);
						break;
					case 3:
						urlTf4.setText(strUrl);
						break;
					case 4:
						urlTf5.setText(strUrl);
						break;
					default:
						break;
					}

					createHTMLFile(strUrl, myCommentTa1.getText());
				} else {
//					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해 주세요.");
					shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
					return;
				}

			}

		});

		// 네이버 블로그 카테고리 목록 콤보박스
		naverBlogCategoryListComboBox = new ComboBox<String>();
		// Let's "permanently" set our ComboBox items to the "items" ObservableList.
		// This causes the
		// ComboBox to "observe" the list for changes
		naverBlogCategoryListComboBox.setItems(items);
		naverBlogCategoryListComboBox.setMinWidth(247);
		items.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends String> arg0) {
				System.out.println("1,arg0:" + arg0);
				System.out.println("1.Changed!!");
			}
		});

		items.addListener((Change<? extends String> c) -> {
			while (c.next()) {
				if (c.wasUpdated()) {
					int start = c.getFrom();
					int end = c.getTo();
					for (int i = start; i < end; i++) {
						System.out.println("Element at position " + i + " was updated to: " + c);
					}
				}
			}
		});

		naverBlogCategoryListComboBox.setPromptText("Please select one");
		naverBlogCategoryListComboBox.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {

					public void changed(ObservableValue<? extends String> observable, String oldValue,
							String newValue) {
						if (newValue != null) {
							String[] strSelectedCategoryArray = newValue.split(":");
							strNaverBlogCategoryNo = strSelectedCategoryArray[0];
							strNaverBlogCategoryName = strSelectedCategoryArray[1];
							System.out.println(
									"strSelectedCategoryArray[0]-------------->" + strSelectedCategoryArray[0]);
							System.out.println(
									"strSelectedCategoryArray[1]-------------->" + strSelectedCategoryArray[1]);
						}

					}
				});

		naverBlogCategoryListComboBox.getItems().addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(ListChangeListener.Change<? extends String> arg0) {
				System.out.println("2.arg0:" + arg0);
				System.out.println("2.Changed!!");
			}
		});

		Button stockPriceShareBtn = new Button("주식 시세 공유");
		stockPriceShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt1.setText("...");
				// 주식 시세 공유
				System.out.println("주식 시세 공유");
				getNaverCookies();
				logger.debug("strBlogId :" + strBlogId);
				logger.debug("strNidAut1 :" + strNidAut);
				logger.debug("strNidSes1 :" + strNidSes);
				if (!strBlogId.equals("") && !strNidAut.equals("") && !strNidSes.equals("")) {

					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strBlogId, strNidAut,
							strNidSes);
					step2.schedulerStart();
				} else {
//					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
					return;
				}

			}

		});

		Button instantShareBtn = new Button("주식 시세 즉시 공유");
		instantShareBtn.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				shareResultTxt1.setText("...");
				// 즉시 공유
				System.out.println("즉시 공유");
				getNaverCookies();
				logger.debug("strBlogId :" + strBlogId);
				logger.debug("strNidAut1 :" + strNidAut);
				logger.debug("strNidSes1 :" + strNidSes);
				if (!strBlogId.equals("") && !strNidAut.equals("") && !strNidSes.equals("")) {

					Step2_StockMarketPriceScheduler step2 = new Step2_StockMarketPriceScheduler(strBlogId, strNidAut,
							strNidSes, true);
					step2.schedulerStart();
				} else {
//					JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
					shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
					return;
				}

			}

		});

		Button shareCoupangPrdtBtn = new Button("쿠팡 상품 자동 공유 시작");
		shareCoupangPrdtBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						getNaverCookies();
						logger.debug("strBlogId :" + strBlogId);
						logger.debug("strNidAut1 :" + strNidAut);
						logger.debug("strNidSes1 :" + strNidSes);
						if (!strBlogId.equals("") && !strNidAut.equals("") && !strNidSes.equals("")) {

//							Step2_ShareCoupangPrdtScheduler step2 = new Step2_ShareCoupangPrdtScheduler(strNidAut, strNidSes,true);
							Step2_ShareCoupangPrdtScheduler step2 = new Step2_ShareCoupangPrdtScheduler(strBlogId,
									strNidAut, strNidSes);
							step2.schedulerStart();
						} else {
//							JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
							shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
							return;
						}
					};
				});

		Button directPostCoupangPrdtBtn = new Button("쿠팡 상품 즉시 포스팅 시작");
		directPostCoupangPrdtBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						getNaverCookies();
						logger.debug("strBlogId :" + strBlogId);
						logger.debug("strNidAut1 :" + strNidAut);
						logger.debug("strNidSes1 :" + strNidSes);
						if (!strBlogId.equals("") && !strNidAut.equals("") && !strNidSes.equals("")) {

//							Step2_ShareCoupangPrdtScheduler step2 = new Step2_ShareCoupangPrdtScheduler(strNidAut, strNidSes,true);
							Step2_ShareCoupangPrdtScheduler step2 = new Step2_ShareCoupangPrdtScheduler(strBlogId,
									strNidAut, strNidSes, true);
							step2.schedulerStart();
						} else {
//							JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해주세요.");
							shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
							return;
						}
					};
				});

		shareResultTxt1 = new Text();
		onAndOffTxt = new Text(" OFF");
		onAndOffTxt.setStyle(FX_FONT_STYLE_NAVER_LOG_OFF);
		onAndOffHBox.getChildren().addAll(onAndOffTxt);

		mainTopHBox.getChildren().addAll(naverBlogCategoryListComboBox);
		mainTopHBox.getChildren().addAll(naverBlogShareBtn);
		mainTopHBox.getChildren().addAll(stockPriceShareBtn);
		mainTopHBox.getChildren().addAll(instantShareBtn);
		mainTopHBox.getChildren().addAll(shareCoupangPrdtBtn);
		mainTopHBox.getChildren().addAll(directPostCoupangPrdtBtn);
		mainTopHBox.getChildren().addAll(getCiNaverImageView("/images/ci/ci_naver.png"));
		mainTopHBox.getChildren().addAll(onAndOffHBox);
		mainTopHBox.getChildren().addAll(shareResultTxt1);
	}

	public VBox getNaverLoginTab() {
		webView1 = new WebView();
		webView1.setPrefHeight(1000);
		webEngine1 = webView1.getEngine();
		webEngine1.load(naverLoginUrl1);

		urlTf1 = new TextField();
		urlTf1.setPrefWidth(800);
		urlTf1.setPrefHeight(25);
		urlTf1.setAlignment(Pos.TOP_LEFT);

		urlTf1.setText(naverLoginUrl1);

		urlTf1.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
				new EventHandler<javafx.scene.input.KeyEvent>() { // Was
					// missing
					// the
					// <MouseEvent>
					@Override
					public void handle(javafx.scene.input.KeyEvent event) {
						System.out.println("code:" + event.getCode());
						System.out.println("code:" + event.getText());
						System.out.println("code:" + event.getCharacter());
						System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
						System.out.println(
								"event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
						System.out.println("urlTf.getText():" + urlTf1.getText());
						if (event.getCode().equals(KeyCode.ENTER)) {
							String url = urlTf1.getText();
							System.out.println("url1:" + url);
							if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
								if (!url.contains(".") || url.contains(" ")) {
									url = "https://www.google.com/search?q=" + url + "&oq=" + url;
								} else {
									url = "http://" + url;
								}
							}
							System.out.println("url2:" + url);
							urlTf1.setText(url);
							webEngine1.load(url);
							getNaverCookies();
							// 네이버 카테고리를 가져온다.
							getNaverBlogCategory();
						}
					};
				});

		urlTf1.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			System.out.println("oldValue:" + oldValue);
			System.out.println("newValue:" + newValue);
			// https://nid.naver.com/nidlogin.login?mode=form&url=https%3A%2F%2Fwww.naver.com
			shareResultTxt1.setText("...");
			if (oldValue
					.equals("https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=40960")
					&& newValue.equals(naverUrl)) {
				System.out.println("blogblogblogblogblogblog");
				// 네이버에 로그인하여 주소창에 주소가 변경되면 네이버 쿠키를 가져온다.
				getNaverCookies();
				// 네이버 카테고리를 가져온다.
				getNaverBlogCategory();
			}
		});

		webEngine1.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
			logger.debug("oldValue :" + oldValue + " newValue:" + newValue);
			if (javafx.concurrent.Worker.State.SUCCEEDED.equals(newValue)) {
				logger.debug("webengine.getLocation:" + webEngine1.getLocation());
				urlTf1.setText(webEngine1.getLocation());
			}
		});
		webEngine1.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable :" + observable + " oldState :" + oldState + " newState:" + newState);
			if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
				urlTf1.setText(webEngine1.getLocation());
			}
		});

		webEngine1.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					urlTf1.setText(webEngine1.getLocation());
				}
			}
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String url = urlTf1.getText();
						System.out.println("url1:" + url);
						if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
							if (!url.contains(".") || url.contains(" ")) {
								url = "https://www.google.com/search?q=" + url + "&oq=" + url;
							} else {
								url = "http://" + url;
							}
						}
						System.out.println("url2:" + url);
						urlTf1.setText(url);
						webEngine1.load(url);
					}
				});

		Separator vSeparator1 = new Separator();
		vSeparator1.setOrientation(Orientation.VERTICAL);
		vSeparator1.setPrefHeight(10);

		HBox urlHBox = new HBox(10);
		HBox naviTxtHBox = getNavigateText(webView1);
		urlHBox.getChildren().addAll(naviTxtHBox);
		urlHBox.getChildren().addAll(urlTf1);
		urlHBox.getChildren().addAll(goBtn);

		VBox vBox = new VBox(urlHBox, webView1);
		vBox.autosize();
		vBox.setAlignment(Pos.TOP_CENTER);
//		Scene scene = new Scene(vBox, 1300, 800);
		return vBox;
	}

	public VBox getStockBoardTab() {
		webView2 = new WebView();
		webView2.setPrefHeight(1000);

		webEngine2 = webView2.getEngine();
		webEngine2.load(displayBoardUrl);

		urlTf2 = new TextField();
		urlTf2.setPrefWidth(800);
		urlTf2.setPrefHeight(25);
		urlTf2.setAlignment(Pos.TOP_LEFT);

		urlTf2.setText(displayBoardUrl);

		urlTf2.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
				new EventHandler<javafx.scene.input.KeyEvent>() {
					@Override
					public void handle(javafx.scene.input.KeyEvent event) {
						System.out.println("code:" + event.getCode());
						System.out.println("code:" + event.getText());
						System.out.println("code:" + event.getCharacter());
						System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
						System.out.println(
								"event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
						System.out.println("urlTf.getText():" + urlTf2.getText());
						if (event.getCode().equals(KeyCode.ENTER)) {
							String url = urlTf2.getText();
							System.out.println("url1:" + url);
							if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
								if (!url.contains(".") || url.contains(" ")) {
									url = "https://www.google.com/search?q=" + url + "&oq=" + url;
								} else {
									url = "http://" + url;
								}
							}
							System.out.println("url2:" + url);
							urlTf2.setText(url);
							webView2.getEngine().load(url);
						}
					};
				});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String url = urlTf2.getText();
						System.out.println("url1:" + url);
						if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
							if (!url.contains(".") || url.contains(" ")) {
								url = "https://www.google.com/search?q=" + url + "&oq=" + url;
							} else {
								url = "http://" + url;
							}
						}
						System.out.println("url2:" + url);
						urlTf2.setText(url);
						webView2.getEngine().load(url);
					}
				});

		Button saveStockListBtn = new Button("Save");
		saveStockListBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView2.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveStockList(html, "전광판");
					};
				});

		Separator vSeparator1 = new Separator();
		vSeparator1.setOrientation(Orientation.VERTICAL);
		vSeparator1.setPrefHeight(10);

		HBox urlHBox = new HBox(10);
		HBox naviTxtHBox = getNavigateText(webView2);
		urlHBox.getChildren().addAll(naviTxtHBox);
		urlHBox.getChildren().addAll(urlTf2);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(saveStockListBtn);

		VBox vBox = new VBox(urlHBox, webView2);
		vBox.autosize();
		vBox.setAlignment(Pos.TOP_CENTER);
//		Scene scene = new Scene(vBox, 1300, 800);
		return vBox;
	}

	public VBox getAfterHourTab() {
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

		urlTf3 = new TextField();
		urlTf3.setPrefWidth(800);
		urlTf3.setPrefHeight(25);
		urlTf3.setAlignment(Pos.TOP_LEFT);
		urlTf3.setText(daumKospiAfterHoursUrl);

		webView3 = new WebView();
		webView3.setPrefHeight(900);

		webEngine3 = webView3.getEngine();

		webEngine3.load(daumKospiAfterHoursUrl);

		webEngine3.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable1 :" + observable);
			logger.debug(" oldState1 :" + oldState + " newState1:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf3.setText(webEngine3.getLocation());
				shareResultTxt1.setText("......");

				String strContent = (String) webView3.getEngine().executeScript("document.documentElement.outerHTML");

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
		urlTf3.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
				new EventHandler<javafx.scene.input.KeyEvent>() {
					@Override
					public void handle(javafx.scene.input.KeyEvent event) {
						System.out.println("code:" + event.getCode());
						System.out.println("code:" + event.getText());
						System.out.println("code:" + event.getCharacter());
						System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
						System.out.println(
								"event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
						System.out.println("urlTf.getText():" + urlTf3.getText());
						if (event.getCode().equals(KeyCode.ENTER)) {
							String strUrl = urlTf3.getText();
							System.out.println("url1:" + strUrl);
							if (!strUrl.toLowerCase().startsWith("http") && !strUrl.toLowerCase().startsWith("https")) {
								if (!strUrl.contains(".") || strUrl.contains(" ")) {
									strUrl = "https://www.google.com/search?q=" + strUrl + "&oq=" + strUrl;
								} else {
									strUrl = "http://" + strUrl;
								}
							}
							System.out.println("url2:" + strUrl);
							urlTf3.setText(strUrl);
							webView3.getEngine().load(strUrl);

						}
					};
				});

		Button goBtn = new Button("Go");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String strUrl = urlTf3.getText();
						webView3.getEngine().load(strUrl);
					};

				});

		Button saveAfterHoursBtn = new Button("Save");
		saveAfterHoursBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView3.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveAfterHoursHtml(html, "시간외단일가");
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
		HBox naviTxtHBox = getNavigateText(webView3);
		urlHBox.getChildren().addAll(naviTxtHBox);
		urlHBox.getChildren().addAll(hSeparator1);
		urlHBox.getChildren().addAll(urlTf3);
		urlHBox.getChildren().addAll(hSeparator2);
		urlHBox.getChildren().addAll(goBtn);
		urlHBox.getChildren().addAll(saveAfterHoursBtn);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(urlHBox);
		vBox.getChildren().addAll(webView3);

		return vBox;
	}

	public VBox getNaverTab() {
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

		urlTf4 = new TextField();
		urlTf4.setPrefWidth(800);
		urlTf4.setPrefHeight(25);
		urlTf4.setAlignment(Pos.TOP_LEFT);
		urlTf4.setText(naverUrl);

		webView4 = new WebView();
//		webView.setPrefHeight(800);
		webView4.setMinHeight(800);
		webView4.setPrefHeight(800);
		webView4.setMaxHeight(1600);

//		webView.autosize();
		webEngine4 = webView4.getEngine();

		webEngine4.load(naverLoginUrl2);

//		webengine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			logger.debug("oldValue :" + oldValue + " newValue:" + newValue);
//			if (Worker.State.SUCCEEDED.equals(newValue)) {
//				logger.debug("webengine.getLocation:" + webengine.getLocation());
//				urlTf.setText(webengine.getLocation());
//			}
//		});
		webEngine4.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable :" + observable + " oldState :" + oldState + " newState:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf4.setText(webEngine4.getLocation());
			}
		});

		webEngine4.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					urlTf4.setText(webEngine4.getLocation());
				}
			}
		});

		Button saveBtn = new Button("Save Cookies");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView4.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveCookies();
					};
				});

		urlTf4.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
				new EventHandler<javafx.scene.input.KeyEvent>() {
					@Override
					public void handle(javafx.scene.input.KeyEvent event) {
						System.out.println("code:" + event.getCode());
						System.out.println("code:" + event.getText());
						System.out.println("code:" + event.getCharacter());
						System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
						System.out.println(
								"event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
						if (event.getCode().equals(KeyCode.ENTER)) {
							String url = urlTf4.getText();
							System.out.println("url1:" + url);
							if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
								if (!url.contains(".") || url.contains(" ")) {
									url = "https://www.google.com/search?q=" + url + "&oq=" + url;
								} else {
									url = "http://" + url;
								}
							}
							System.out.println("url2:" + url);
							urlTf4.setText(url);
							webEngine4.load(url);
							getNaverCookies();
							// 네이버 카테고리를 가져온다.
							getNaverBlogCategory();
						}
					}
				});

		urlTf4.textProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			System.out.println("oldValue1:" + oldValue);
			System.out.println("newValue1:" + newValue);
			System.out.println("naverUrl:" + naverUrl);
			// oldValue :https://nid.naver.com/nidlogin.login
			// old,newValue :
			// https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1
			// newValue : https://www.naver.com/
			shareResultTxt1.setText("...");
			if (oldValue.equals("https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1")
					&& newValue.equals(naverUrl)) {
				System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				// 네이버에 로그인하여 주소창에 주소가 변경되면 네이버 쿠키를 가져온다.
				getNaverCookies();

				// 네이버 카테고리를 가져온다.
				getNaverBlogCategory();

				getNaverId(webView4);

			}
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String url = urlTf4.getText();
						System.out.println("url1:" + url);
						if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
							if (!url.contains(".") || url.contains(" ")) {
								url = "https://www.google.com/search?q=" + url + "&oq=" + url;
							} else {
								url = "http://" + url;
							}
						}
						System.out.println("url2:" + url);
						urlTf4.setText(url);
						webEngine4.load(url);
					}
				});

//		HBox urlHBox = new HBox(backLbl, forwardLbl, reloadLbl, urlTf, goBtn);
		HBox urlHBox = new HBox(10);

		HBox naviTxtHBox = getNavigateText(webView4);
		urlHBox.getChildren().addAll(naviTxtHBox);

		urlHBox.getChildren().addAll(urlTf4);
		urlHBox.getChildren().addAll(goBtn);

		Text myCommentTxt = new Text("My Comment");
		myCommentTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		myCommentTa1 = new TextArea();
		myCommentTa1.setPrefWidth(800);
		myCommentTa1.setPrefHeight(50);
		myCommentTa1.setWrapText(true);
		HBox myCommentBox = new HBox(myCommentTxt, myCommentTa1);

		VBox nidBox = new VBox(myCommentBox, saveBtn);

		VBox saveBtnBox = new VBox(saveBtn);
		saveBtnBox.setAlignment(Pos.CENTER);

		Separator vSeparator = new Separator();
		vSeparator.setOrientation(Orientation.VERTICAL);
		vSeparator.setPrefWidth(10);

		VBox vBox = new VBox(urlHBox, webView4, vSeparator, nidBox, saveBtnBox);
//		VBox vBox = new VBox(urlTf, webView, separator, saveBtn);
//		vBox.autosize();
		vBox.setAlignment(Pos.TOP_LEFT);
		return vBox;
	}

	public VBox getCoupangTab() {
		accessKeyTf = new TextField();
		accessKeyTf.setPrefWidth(400);
		accessKeyTf.setPrefHeight(25);
		accessKeyTf.setAlignment(Pos.TOP_LEFT);

		secretKeyTf = new TextField();
		secretKeyTf.setPrefWidth(400);
		secretKeyTf.setPrefHeight(25);
		secretKeyTf.setAlignment(Pos.TOP_LEFT);

		getUrlAttr();
		initKeys();

		urlTf5 = new TextField();
		urlTf5.setPrefWidth(800);
		urlTf5.setPrefHeight(25);
		urlTf5.setAlignment(Pos.TOP_LEFT);
		urlTf5.setText(naverUrl);

		webView5 = new WebView();
//		webView.setPrefHeight(800);
		webView5.setMinHeight(600);
		webView5.setPrefHeight(500);
		webView5.setMaxHeight(800);

//		webView.autosize();
		webEngine5 = webView5.getEngine();
		webEngine5.load(coupangUrl);

//		webengine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//			logger.debug("oldValue :" + oldValue + " newValue:" + newValue);
//			if (Worker.State.SUCCEEDED.equals(newValue)) {
//				logger.debug("webengine.getLocation:" + webengine.getLocation());
//				urlTf.setText(webengine.getLocation());
//			}
//		});
		webEngine5.getLoadWorker().stateProperty().addListener((observable, oldState, newState) -> {
			logger.debug("observable :" + observable + " oldState :" + oldState + " newState:" + newState);
			if (newState == State.SUCCEEDED) {
				urlTf5.setText(webEngine5.getLocation());
			}
		});

		webEngine5.getLoadWorker().stateProperty().addListener(new javafx.beans.value.ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					urlTf5.setText(webEngine5.getLocation());
				}
			}
		});

		Button saveBtn = new Button("Save");
		saveBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String html = (String) webView5.getEngine().executeScript("document.documentElement.outerHTML");

						System.out.println("html:" + html);
						saveCookies();
					};
				});

		urlTf5.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED,
				new EventHandler<javafx.scene.input.KeyEvent>() {
					@Override
					public void handle(javafx.scene.input.KeyEvent event) {
						System.out.println("code:" + event.getCode());
						System.out.println("code:" + event.getText());
						System.out.println("code:" + event.getCharacter());
						System.out.println("KeyCode.ENTER:" + KeyCode.ENTER);
						System.out.println(
								"event.getCode().equals(KeyCode.ENTER):" + event.getCode().equals(KeyCode.ENTER));
						if (event.getCode().equals(KeyCode.ENTER)) {
							String url = urlTf5.getText();
							System.out.println("url1:" + url);
							if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
								if (!url.contains(".") || url.contains(" ")) {
									url = "https://www.google.com/search?q=" + url + "&oq=" + url;
								} else {
									url = "http://" + url;
								}
							}
							System.out.println("url2:" + url);
							urlTf5.setText(url);
							webEngine5.load(url);
							getNaverCookies();

							// 네이버 카테고리를 가져온다.
							getNaverBlogCategory();
						}
					}
				});

		urlTf5.textProperty().addListener((observable, oldValue, newValue) -> {
//			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			System.out.println("textfield changed from " + oldValue + " to " + newValue);
			System.out.println("oldValue1:" + oldValue);
			System.out.println("newValue1:" + newValue);
			System.out.println("naverUrl:" + naverUrl);
			// oldValue :https://nid.naver.com/nidlogin.login
			// old,newValue :
			// https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1
			// newValue : https://www.naver.com/
			shareResultTxt1.setText("...");
			if (oldValue.equals("https://nid.naver.com/signin/v3/finalize?url=https%3A%2F%2Fwww.naver.com&svctype=1")
					&& newValue.equals(naverUrl)) {
				System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				// 네이버에 로그인하여 주소창에 주소가 변경되면 네이버 쿠키를 가져온다.
				getNaverCookies();

				// 네이버 카테고리를 가져온다.
				getNaverBlogCategory();
			}
		});

		Button goBtn = new Button("GO");
		goBtn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
				new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
					@Override
					public void handle(javafx.scene.input.MouseEvent event) {
						String url = urlTf5.getText();
						System.out.println("url1:" + url);
						if (!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")) {
							if (!url.contains(".") || url.contains(" ")) {
								url = "https://www.google.com/search?q=" + url + "&oq=" + url;
							} else {
								url = "http://" + url;
							}
						}
						System.out.println("url2:" + url);
						urlTf5.setText(url);
						webEngine5.load(url);
					}
				});

		HBox urlHBox = new HBox(10);

		HBox naviTxtHBox = getNavigateText(webView5);
		urlHBox.getChildren().addAll(naviTxtHBox);

		urlHBox.getChildren().addAll(urlTf5);
		urlHBox.getChildren().addAll(goBtn);

		Text myCommentTxt = new Text("My Comment");
		myCommentTxt.setStyle(FX_FONT_STYLE_DEFAULT);

		myCommentTa2 = new TextArea();
		myCommentTa2.setPrefWidth(800);
		myCommentTa2.setPrefHeight(50);
		myCommentTa2.setWrapText(true);

		HBox myCommentBox = new HBox(myCommentTxt, myCommentTa2);

		VBox nidBox = new VBox(myCommentBox, saveBtn);

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
							ex.getMessage();
						}
					};
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

		coupangCategoryListComboBox = new ComboBox<String>();
		coupangCategoryListComboBox.getItems().addAll("1001: 여성패션", "", "1002: 남성패션", "", "1003: 베이비패션 (0~3세)", "",
				"1004: 여아패션 (3세 이상)", "", "1005: 남아패션 (3세 이상)", "", "1006: 스포츠패션", "", "1007: 신발", "", "1008: 가방/잡화",
				"", "1010: 뷰티", "", "1011: 출산/유아동", "", "1012: 식품", "", "1013: 주방용품", "", "1014: 생활용품", "",
				"1015: 홈인테리어", "", "1016: 가전디지털", "", "1017: 스포츠/레저", "", "1018: 자동차용품", "", "1019: 도서/음반/DVD", "",
				"1020: 완구/취미", "", "1021: 문구/오피스", "", "1024: 헬스/건강식품", "", "1025: 국내여행", "", "1026: 해외여행", "",
				"1029: 반려동물용품");
		coupangCategoryListComboBox.setMinHeight(21);
		coupangCategoryListComboBox.setMinWidth(200);
		coupangCategoryListComboBox.setPromptText("전체");

		coupangBrandListComboBox = new ComboBox<String>();
		coupangBrandListComboBox.getItems().addAll("1001: 탐사", "1002: 코멧", "1003: Gomgom", "1004: 줌", "1005: 마케마케",
				"1006: 곰곰", "1007: 꼬리별", "1008: 베이스알파에센셜", "1009: 요놈", "1010: 비타할로", "1011: 비지엔젤", "1012: 타이니스타");
		coupangBrandListComboBox.setMinHeight(21);
		coupangBrandListComboBox.setMinWidth(200);
		coupangBrandListComboBox.setPromptText("전체");

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
					};
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
		prdtHbox1.getChildren().addAll(coupangCategoryListComboBox);
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
		prdtHbox4.getChildren().addAll(coupangBrandListComboBox);
		prdtHbox4.getChildren().addAll(coupangPLBrandResultLbl);

		prdtHbox5.getChildren().addAll(cb5);
		prdtHbox5.getChildren().addAll(coupangPrdtTxt7);
		prdtHbox5.getChildren().addAll(coupangPrdtTxt8);
		prdtHbox5.getChildren().addAll(keywordTf);
		prdtHbox5.getChildren().addAll(keywordDelBtn);
		prdtHbox5.getChildren().addAll(searchResultLbl);

		prdtHbox6.getChildren().addAll(shareCoupangBtn);

		VBox vBox = new VBox(urlHBox, webView5, hSeparator3, nidBox, saveBtnBox, coupangKeyHbox, prdtVbox);
//		VBox vBox = new VBox(urlTf, webView, separator, saveBtn);
//		vBox.autosize();
		vBox.setAlignment(Pos.TOP_LEFT);
		return vBox;
	}

	public void saveStockList(String contentHtml, String title) {
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
//		fileName = USER_HOME + "\\documents\\" + strYmdhms + "_" + market_en + "_list.txt";
		fileName = market_en + "_list.txt";
		FileUtil.fileWrite(fileName, stockCodeNameSb.toString());
//		JOptionPane.showMessageDialog(null, market_ko+"주식 목록을 추출하였습니다.");
		shareResultTxt1.setText(market_ko + "주식 목록을 추출하였습니다.");
	}

	private void saveCookies() {
		CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
		Field f;
		try {
			f = cookieManager.getClass().getDeclaredField("store");
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
			Files.write(Paths.get("cookies.json"), json.getBytes());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getNaverCookies() {
		try {
			CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
			System.out.println("cookieManager:" + cookieManager);
			System.out.println("cookieManager.getClass():" + cookieManager.getClass());
			System.out.println("cookieManager.getClass().getName():" + cookieManager.getClass().getName());
			System.out.println("cookieManager.getClass().getSimpleName():" + cookieManager.getClass().getSimpleName());
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
			
			if (!strNidAut.equals("") && !strNidSes.equals("")) {
				onAndOffTxt = new Text(" ON");
				onAndOffTxt.setStyle(FX_FONT_STYLE_NAVER_LOG_ON);
				onAndOffHBox.getChildren().clear();
				onAndOffHBox.getChildren().addAll(onAndOffTxt);
			} else {
				onAndOffTxt = new Text(" OFF");
				onAndOffTxt.setStyle(FX_FONT_STYLE_NAVER_LOG_OFF);
				onAndOffHBox.getChildren().clear();
				onAndOffHBox.getChildren().addAll(onAndOffTxt);
			}
			
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

	private void getNaverId(WebView webView) {
		String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");
		System.out.println("html:" + html);

		Document naverMainDoc = Jsoup.parse(html);
		Elements naverMainDocScripts = naverMainDoc.select("script");
		for (Element naverMainDocScript : naverMainDocScripts) {
			String scriptData = naverMainDocScript.data();
			if (!scriptData.contains("window.nmain.gv")) {
				continue;
			}
			scriptData = scriptData.replace("window.nmain.gv = ", "");

			JSONObject jObj = new JSONObject(scriptData);
			Iterator scriptKeysIt = jObj.keys();
			while (scriptKeysIt.hasNext()) {
				String key = (String) scriptKeysIt.next();
				if (key.equals("isLogin")) {
					isLogin = jObj.getString(key);
				} else if (key.equals("userId")) {
					strBlogId = jObj.getString(key);
				}
			}
		}
		System.out.println("isLogin:" + isLogin);
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("-------------");
	}

	public ImageView getCiNaverImageView() {
		final ImageView imgView = new ImageView();
		Image image1 = new Image(this.getClass().getResourceAsStream("/images/ci/ci_naver.png"));
		imgView.setImage(image1);
		return imgView;
	}

	public BufferedImage loadImage(String fileName) {

		BufferedImage buff = null;
		try {
			buff = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return buff;

	}

	public ImageView getCiNaverImageView(String imagePath) {
		System.out.println("imagePath :" + imagePath);
		final ImageView imgView = new ImageView();
		InputStream is = null;
		is = this.getClass().getResourceAsStream(imagePath);
		if (is == null) {
			is = this.getClass().getResourceAsStream("/resources" + imagePath);
		}

		Image image1 = new Image(is);
		imgView.setImage(image1);
		imgView.setFitHeight(20);
		imgView.setFitWidth(image1.getWidth());
		return imgView;
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
		createHTMLFile(strUrl, myCommentTa1.getText());
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
			shareResultTxt1.setText("뉴스 클래스 부재");
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
			sb = (StringBuilder) method.invoke(String.class, new Object[] { strUrl, strMyComment });
			if (sb.toString().equals("")) {
				method = c.getDeclaredMethod("parseHTMLFile", String.class, String.class, String.class);
				String strContent = (String) webView4.getEngine().executeScript("document.documentElement.outerHTML");
				logger.debug("strContent:" + strContent);
				sb = (StringBuilder) method.invoke(String.class, new Object[] { strUrl, strContent, strMyComment });
			}
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.INFO, sb.toString());
		} catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			return;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if (sb.toString().equals("")) {
			logger.debug("기사를 읽어오지 못했습니다.");
//			JOptionPane.showMessageDialog(null, "기사를 읽어오지 못했습니다.");
			shareResultTxt1.setText("기사를 읽어오지 못했습니다.");
			return;
		}

		Document htmlDoc = Jsoup.parse(sb.toString());
		logger.debug("htmlDoc:" + htmlDoc.html());

		htmlDoc.select("meta").remove();
		sb.delete(0, sb.length());
		sb.setLength(0);

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
		strNaverBlogCategoryName = "증권";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		
		if (naverBlogLinkShare(contentSb, strNaverBlogCategoryNo, strShareTitle, strShareUrl)) {
			shareResultTxt1.setText("블로그 글쓰기 성공");
		} else {
			shareResultTxt1.setText("블로그 글쓰기 실패");
		}
		myCommentTa1.setText("...");

	}

	public boolean naverBlogLinkShare(StringBuilder contentSb, String strNaverBlogCategoryNo, String strShareTitle,
			String strShareUrl) {
		logger.debug("strBlogId:" + strBlogId);
		logger.debug("strNidAut:" + strNidAut);
		logger.debug("strNidSes:" + strNidSes);

		if (!strNidAut.equals("") && !strNidSes.equals("") && !strBlogId.equals("")) {
			return NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strShareUrl, strShareTitle,
					strNaverBlogCategoryNo, contentSb, null);
		} else {
			System.out.println("로그인하지 않았습니다.");
			return false;
		}
	}

	private void getUrlAttr() {
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
			System.out.println(
					"getClass().getProtectionDomain().getClassLoader().getResource(\"coupangPartners.properties\"):"
							+ getClass().getProtectionDomain().getClassLoader()
									.getResource("coupangPartners.properties"));

			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			System.out.println(". AbsolutePath:" + new File(".").getAbsolutePath());
			File f = new File("./coupangPartners.properties");
			System.out.println("f.exists():" + f.exists());
			if (f.exists()) {
				System.out.println("./coupangPartners.properties File exists");
				is = new FileInputStream(f);
				props.load(is);
				System.out.println("props :" + props);
				accessKey = (String) props.get("access_key");
				secretKey = (String) props.get("secret_key");
				System.out.println("accessKey :" + accessKey);
				System.out.println("secretKey :" + secretKey);
				if (accessKey.equals("") || secretKey.equals("")) {
					// classes root 경로
					is = getClass().getResourceAsStream("/coupangPartners.properties");
					System.out.println("class 경로 read /coupangPartners.properties Resource");
				}
			} else {
				// classes root 경로
				is = getClass().getResourceAsStream("/coupangPartners.properties");
//				is = getClass().getClassLoader().getResourceAsStream("/coupangPartners.properties");
				System.out.println("class 경로 read /coupangPartners.properties Resource");

				String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
				System.out.println("rootPath :" + rootPath);

				String defaultConfigPath = rootPath + "coupangPartners.properties";
				Properties defaultProps = new Properties();
				defaultProps.load(new FileInputStream(defaultConfigPath));
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

		if (strNidAut.equals("")) {
//			JOptionPane.showMessageDialog(null, "NID_AUT를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(null, "NID_SES를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (cb1.isSelected()) {
			bestcategoriesResultLbl.setText("");
			String item = String.valueOf(coupangCategoryListComboBox.getSelectionModel().getSelectedItem());
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
			cb1.setSelected(false);
		}

		if (cb2.isSelected()) {
			goldboxResultLbl.setText("");
			bResult = getGoldboxProducts();
			if (bResult) {
				goldboxResultLbl.setText("처리 완료");
			}
			cb2.setSelected(false);
		}

		if (cb3.isSelected()) {
			coupangPLResultLbl.setText("");
			bResult = getCoupangPLProducts();
			if (bResult) {
				coupangPLResultLbl.setText("처리 완료");
			}
			cb3.setSelected(false);
		}

		if (cb4.isSelected()) {
			coupangPLBrandResultLbl.setText("");
			// getCoupangPLBrandProducts();
			String item = String.valueOf(coupangBrandListComboBox.getSelectionModel().getSelectedItem());
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
			cb4.setSelected(false);
		}

		if (cb5.isSelected()) {
			searchResultLbl.setText("");
			String keyword = keywordTf.getText();
			bResult = getSearchProducts(keyword);
			if (bResult) {
				searchResultLbl.setText("처리 완료");
			}
			cb5.setSelected(false);
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
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		String shareTitle = strYmdBlacket + " " + "카테고리별 베스트상품(" + categoryNm + ") TOP" + limit;
		sb.append("<div style='width:100%;'><h1>").append(shareTitle).append("</h1></div>");
		System.out.println(categoryId + ":" + categoryNm);
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		String bestcategoriesUrl = BESTCATEGORIES_URL + categoryId + "?limit=" + limit;
		String data = getData("카테고리별 베스트상품", bestcategoriesUrl, categoryNm, strParamJson);
		sb.append(data);
		sb.append(COUPANG_PARTNERS_NOTICE);

		strNaverBlogCategoryName = "카테고리별 베스트 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
	}

	public StringBuilder getBestcategoryProducts(int idx, String categoryId, String categoryNm, int limit) {
		StringBuilder sb = new StringBuilder();
		System.out.println((idx + 1) + "." + categoryId + ":" + categoryNm);
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		String bestcategoriesUrl = BESTCATEGORIES_URL + categoryId + "?limit=" + limit;
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
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "카테고리별 베스트상품";
		strNaverBlogCategoryName = "카테고리별 베스트 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
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
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "WOW 와우회원 전용 매일 오전 7시 골드박스 1일특가";
		strNaverBlogCategoryName = "골드박스 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
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
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "쿠팡 PL 상품 TOP" + limit;
		strNaverBlogCategoryName = "PL 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
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
		strNaverBlogCategoryName = "PL 브랜드별 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
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
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "쿠팡 PL 브랜드별 상품 TOP" + limit;
		strNaverBlogCategoryName = "PL 브랜드별 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
	}

	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다. 6분에 1번 호출 가능)
	// SEARCH_URL = API_PATH + "​/products​/search";
	public boolean getSearchProducts(String keyword) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmdBlacket).append(" ").append("쿠팡 제품 추천 합니다!쿠팡! |")
				.append(keyword).append("</h1></div>");
		String strParamJson = "";
		int limit = 20;
		String encodedKeyword = "";
		try {
			encodedKeyword = URLEncoder.encode(keyword, "UTF8");
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		String strSearchUrl = SEARCH_URL + "?keyword=" + encodedKeyword + "&limit=" + limit;
//		server_url = server_url + "?keyword=food&limit=" + limit;
		System.out.println("server_url:" + strSearchUrl);
		String data = getData("상품검색", strSearchUrl, "", strParamJson);
		sb.append(data);
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + keyword;
		strNaverBlogCategoryName = "추천 상품";
		strNaverBlogCategoryNo = getCategoryNo(strNaverBlogCategoryName);
		return naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "");
	}

	public String getData(String apiGubun, String server_url, String categoryNm, String strParamJson) {
		System.out.println("server_url :" + server_url);
		StringBuilder sb = new StringBuilder();
		// Generate HMAC string
		String authorization = HmacGenerator.generate(REQUEST_METHOD_GET, server_url, SECRET_KEY, ACCESS_KEY);
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
							java.util.logging.Logger
									.getLogger(CoupangPartnersApiOneFileNaverLinkShareSimple.class.getName())
									.log(Level.SEVERE, null, ex);
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
						int selectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();
						switch (selectedTabIndex) {
						case 0:
							homeUrl = naverLoginUrl1;
							break;
						case 1:
							homeUrl = displayBoardUrl;
							break;
						case 2:
							homeUrl = afterHoursUrl;
							break;
						case 3:
							homeUrl = naverUrl;
							break;
						case 4:
							homeUrl = coupangUrl;
							break;
						default:
							break;
						}
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

	/**
	 * Daum 시간외단일가 save 버튼 클릭시 html을 저장한다.
	 */
	public void saveAfterHoursHtml(String contentHtml, String title) {
		StringBuilder sb = new StringBuilder();

		SimpleDateFormat hourSdf = new SimpleDateFormat("HH", Locale.KOREAN);
		String strHour = hourSdf.format(new Date());
		int iHour = Integer.parseInt(strHour);

		System.out.println("dayOfWeek:" + dayOfWeek);
		System.out.println("strHour:" + strHour);
		Calendar c = Calendar.getInstance();
		if (iHour >= 0 && iHour <= 8) {
			if (dayOfWeek >= 3 && dayOfWeek <= 7) {
				c.add(Calendar.DATE, -1);
				strYmdBlacket = sdf0.format(c.getTime());
			} else if (dayOfWeek == 1) {// 일요일일 경우
				c.add(Calendar.DATE, -2);
				strYmdBlacket = sdf0.format(c.getTime());
			}
		} else {
			if (dayOfWeek == 7 || dayOfWeek == 1) {
				c.add(Calendar.DATE, -1);
				strYmdBlacket = sdf0.format(c.getTime());
			}
		}

		try {
			URL url = new URL(daumKospiAfterHoursUrl);
			String strProtocol = url.getProtocol();
			String strHost = url.getHost();

			contentHtml = contentHtml.replace("\"//", "\"" + strProtocol + "://");
			contentHtml = contentHtml.replace("\"/", "\"" + strProtocol + "://" + strHost + "/");

			contentHtml = contentHtml.replace("\'//", "\'" + strProtocol + "://");
			contentHtml = contentHtml.replace("\'/", "\'" + strProtocol + "://" + strHost + "/");
			sb.append(contentHtml);

			List<StockVO> stockList = new ArrayList<StockVO>();
			Document doc = Jsoup.parse(sb.toString());
			doc.select("i").tagName("b");
			String mktType = doc.select(".tab .on a").text();
			logger.debug("mktType:" + mktType);
			String upDownType = doc.select("ul.box_tabs .on").get(0).text();
			logger.debug("upDownType:" + upDownType);

			Element table = null;
			if (upDownType.equals("상승")) {
				table = doc.select(".box_contents table").get(0);
			} else if (upDownType.equals("하락")) {
				table = doc.select(".box_contents table").get(1);
			} else {
				table = doc.select(".box_contents table").get(2);
			}

			Elements aEls = table.select("a");
			for (Element aEl : aEls) {
				String strStockName = aEl.text();
				String strHref = aEl.attr("href");
				String strStockCode = strHref.substring(strHref.lastIndexOf("/") + 1);
				logger.debug(strStockCode + ":" + strStockName);
				if (strStockCode.contains("javascript")) {
					continue;
				}
				if (strStockCode.startsWith("A")) {
					strStockCode = strStockCode.substring(1);
					StockVO svo = new StockVO();
					svo.setStockCode(strStockCode);
					svo.setStockName(strStockName);
					stockList.add(svo);
				}
			}
			logger.debug("list.size:" + stockList.size());

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

//			String fileName = USER_HOME + "\\documents\\" + strYmdhms + "_" + title + ".html";
//			String tableHtml = Jsoup.parse(table.outerHtml()).html();
//			FileUtil.fileWrite(fileName, tableHtml);
			sb = new StringBuilder();
			sb.append("<h1>").append(strYmdBlacket).append(" ").append(mktType).append(" ").append(title).append("(")
					.append(upDownType).append(")").append("</h1>\r\n");
//			sb.append(table.outerHtml());
			StringBuilder tableOuterHtml = new StringBuilder(table.outerHtml());
			// 다음 스톡 링크 제거하고 네이버 스톡 링크 걸기
			StringBuilder naverLinkHtml = StockUtil.stockLinkString(tableOuterHtml, stockList);
			sb.append(naverLinkHtml);

			// 뉴스 첨부
			StringBuilder newsAddedStockList = StockUtil.getNews(stockList);
			System.out.println("newsAddedStockList:" + newsAddedStockList);
			// 증권명에 증권링크 생성
			StringBuilder stockTableAdded = StockUtil.stockLinkString(newsAddedStockList, stockList);
			System.out.println("stockTableAdded:" + stockTableAdded);
			sb.append(stockTableAdded.toString());

			strNaverBlogCategoryNo = "235";// "시간외단일가";
			String shareTitle = strYmdBlacket + " " + mktType + " 시간외단일가(" + upDownType + ")";

			if (!strNidAut.equals("") && !strNidSes.equals("")) {
				if (naverBlogLinkShare(sb, strNaverBlogCategoryNo, shareTitle, "")) {
//					JOptionPane.showMessageDialog(null, shareTitle + " 데이터를 공유하였습니다.");
					shareResultTxt1.setText(shareTitle + " 데이터를 공유하였습니다.");
				}
			} else {
//				JOptionPane.showMessageDialog(null, "먼저 네이버에 로그인해 주세요.");
				shareResultTxt1.setText("먼저 네이버에 로그인해 주세요.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 쿠키에 있는 NID_AUT,NID_SES 정보를 이용하여 네이버 카테고리 정보를 가져온다.
	 */
	public void getNaverBlogCategory() {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut).append(";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes).append(";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			System.out.println("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
//			builder = builder.path("/LinkShare.nhn");
			builder = builder.path("/openapi/share");
			// 죽을 뻔 한 아기 수달을 살려줬더니 생긴 일 ㅣ What Happened After Rescuing A Nearly Dying Baby
			// Otter Is..
			String strUrl = "https://www.youtube.com/watch?v=J6zD3h_I3Lc";
			strUrl = URLEncoder.encode(strUrl, "UTF-8");
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			System.out.println("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			// [B@2460600f
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			System.out.println("unzipString:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
				if (!unzipString.equals("")) {
					// 카테고리 초기화
					items.clear();

					naverBlogCategoryListComboBox.setItems(items);

					List<String> categories = new ArrayList<>();
					Document doc = Jsoup.parse(unzipString);
					Elements categoryEls = doc.select("#_categoryList option");
					for (Element categoryEl : categoryEls) {
						String categoryNo = categoryEl.attr("value");
						String categoryName = categoryEl.text();
						String categoryNoAndName = categoryNo + ":" + categoryName;
						categories.add(categoryNoAndName);
					}
					items.setAll(categories);
				}
			}
			;
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCategoryNo(String categoryName) {
		String categoryNo = "";
//		ObservableList<String> items = FXCollections.observableArrayList();
		ObservableList<String> items = naverBlogCategoryListComboBox.getItems();

		for (int i = 0; i < items.size(); i++) {
			String item = items.get(i);
			if (item.contains(":")) {
				String strCategoryArray[] = item.split(":");
				System.out.println("strCategoryArray.length :" + strCategoryArray.length);
				if (strCategoryArray.length > 1) {
					strNaverBlogCategoryNo = strCategoryArray[0];
					strNaverBlogCategoryName = strCategoryArray[1];
					System.out.println("strNaverBlogCategoryNo-------------->" + strNaverBlogCategoryNo);
					System.out.println("strNaverBlogCategoryName-------------->" + strNaverBlogCategoryName);
				}
				if (categoryName.equals(strNaverBlogCategoryName)) {
					categoryNo = strNaverBlogCategoryNo;
					break;
				}
			}
		}
		return categoryNo;
	}
}
