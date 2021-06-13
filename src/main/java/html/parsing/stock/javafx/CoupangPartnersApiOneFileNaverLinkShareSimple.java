/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.javafx;

import java.awt.Desktop;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
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

import com.coupang.partners.HmacGenerator;

import html.parsing.stock.util.NaverUtil;

/**
 *
 * @author banks
 */
public class CoupangPartnersApiOneFileNaverLinkShareSimple extends javax.swing.JFrame implements WindowListener,
	WindowFocusListener,
	WindowStateListener {

	private final static String COUPANG_PARTNERS_NOTICE = "<div>※ 쿠팡 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있습니다.</div>";
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

	private String strBlogId;
	private String strNidAut;
	private String strNidSes;

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

	/**
	 * Creates new form NewJFrame1
	 */
	public CoupangPartnersApiOneFileNaverLinkShareSimple() {
		initComponents();
		initList();
		initKeys();
		setDefaultDate();
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
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
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

	public void setDefaultDate() {
	}

	public void test() {
//		getShortenedUrl("https://www.coupang.com/np/coupangglobal");
		getReportsClicks("20200501", "20200630");
		getReportsOrders("20200501", "20200630");
		getReportsCancels("20200501", "20200630");

		// 카테고리 별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
		getBestcategoryProducts();
		getGoldboxProducts();
		// 쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
		// COUPANG_PL_URL = API_PATH + "​​/products​/coupangPL";
		getCoupangPLProducts();
		// 쿠팡 PL 브랜드별 상품 상세 정보를 생성합니다.
		getCoupangPLBrandProducts();
		// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
		getSearchProducts("egg+slicer");
	}

	private static void open(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
			} catch (IOException e) {
				/* TODO: error handling */ }
		} else {
			/* TODO: error handling */ }
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                buttonGroup1 = new javax.swing.ButtonGroup();
                buttonGroup2 = new javax.swing.ButtonGroup();
                buttonGroup3 = new javax.swing.ButtonGroup();
                buttonGroup4 = new javax.swing.ButtonGroup();
                jPanel16 = new javax.swing.JPanel();
                jPanel14 = new javax.swing.JPanel();
                jPanel19 = new javax.swing.JPanel();
                jLabel6 = new javax.swing.JLabel();
                accessKeyTf = new javax.swing.JTextField();
                accessKeyTfDelBtn = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                secretKeyTf = new javax.swing.JTextField();
                secretKeyTfDelBtn = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();
                jPanel5 = new javax.swing.JPanel();
                jPanel8 = new javax.swing.JPanel();
                jPanel10 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jPanel13 = new javax.swing.JPanel();
                jCheckBox1 = new javax.swing.JCheckBox();
                jLabel3 = new javax.swing.JLabel();
                jLabel14 = new javax.swing.JLabel();
                categoryIdJComboBox = new javax.swing.JComboBox<>();
                jPanel15 = new javax.swing.JPanel();
                goldboxJCheckBox = new javax.swing.JCheckBox();
                jLabel4 = new javax.swing.JLabel();
                jPanel17 = new javax.swing.JPanel();
                coupangPLJCheckBox = new javax.swing.JCheckBox();
                jLabel5 = new javax.swing.JLabel();
                jPanel25 = new javax.swing.JPanel();
                coupangPLBrandJCheckBox = new javax.swing.JCheckBox();
                jLabel11 = new javax.swing.JLabel();
                jLabel13 = new javax.swing.JLabel();
                brandIdJComboBox = new javax.swing.JComboBox<>();
                jPanel26 = new javax.swing.JPanel();
                searchJCheckBox = new javax.swing.JCheckBox();
                jLabel12 = new javax.swing.JLabel();
                jLabel20 = new javax.swing.JLabel();
                keywordTf = new javax.swing.JTextField();
                keywordDeleteJButton = new javax.swing.JButton();
                jPanel7 = new javax.swing.JPanel();
                jPanel28 = new javax.swing.JPanel();
                jPanel32 = new javax.swing.JPanel();
                bestcategoriesResultLbl = new javax.swing.JLabel();
                jPanel33 = new javax.swing.JPanel();
                goldboxResultLbl = new javax.swing.JLabel();
                jPanel34 = new javax.swing.JPanel();
                coupangPLResultLbl = new javax.swing.JLabel();
                jPanel35 = new javax.swing.JPanel();
                coupangPLBrandResultLbl = new javax.swing.JLabel();
                jPanel36 = new javax.swing.JPanel();
                searchResultLbl = new javax.swing.JLabel();
                jPanel37 = new javax.swing.JPanel();
                jPanel2 = new javax.swing.JPanel();
                jLabel15 = new javax.swing.JLabel();
                jScrollPane1 = new javax.swing.JScrollPane();
                nidAutTa = new javax.swing.JTextArea();
                jPanel38 = new javax.swing.JPanel();
                jPanel39 = new javax.swing.JPanel();
                jLabel21 = new javax.swing.JLabel();
                jScrollPane2 = new javax.swing.JScrollPane();
                nidSesTa = new javax.swing.JTextArea();
                jPanel40 = new javax.swing.JPanel();
                jPanel1 = new javax.swing.JPanel();
                jButton1 = new javax.swing.JButton();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setTitle("웹페이지 추출");
                setMinimumSize(new java.awt.Dimension(1100, 800));
                setSize(new java.awt.Dimension(1100, 1000));
                getContentPane().add(jPanel16, java.awt.BorderLayout.WEST);

                jPanel14.setMinimumSize(new java.awt.Dimension(1000, 1000));
                jPanel14.setPreferredSize(new java.awt.Dimension(1000, 1000));
                jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                jPanel19.setPreferredSize(new java.awt.Dimension(1000, 33));
                jPanel19.setRequestFocusEnabled(false);
                jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                jLabel6.setText("ACCESS KEY");
                jPanel19.add(jLabel6);

                accessKeyTf.setMinimumSize(new java.awt.Dimension(250, 25));
                accessKeyTf.setPreferredSize(new java.awt.Dimension(250, 25));
                accessKeyTf.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                accessKeyTfActionPerformed(evt);
                        }
                });
                jPanel19.add(accessKeyTf);

                accessKeyTfDelBtn.setText("지우기");
                accessKeyTfDelBtn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                accessKeyTfDelBtnActionPerformed(evt);
                        }
                });
                jPanel19.add(accessKeyTfDelBtn);

                jLabel1.setText("SECRET KEY");
                jPanel19.add(jLabel1);

                secretKeyTf.setMinimumSize(new java.awt.Dimension(300, 25));
                secretKeyTf.setName(""); // NOI18N
                secretKeyTf.setPreferredSize(new java.awt.Dimension(300, 25));
                secretKeyTf.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                secretKeyTfActionPerformed(evt);
                        }
                });
                jPanel19.add(secretKeyTf);

                secretKeyTfDelBtn.setText("지우기");
                secretKeyTfDelBtn.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                secretKeyTfDelBtnActionPerformed(evt);
                        }
                });
                jPanel19.add(secretKeyTfDelBtn);

                jButton3.setText("저장");
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });
                jPanel19.add(jButton3);

                jPanel14.add(jPanel19);

                jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel5.setMinimumSize(new java.awt.Dimension(990, 180));
                jPanel5.setPreferredSize(new java.awt.Dimension(1000, 180));
                jPanel5.setLayout(new java.awt.BorderLayout());

                jPanel8.setMaximumSize(new java.awt.Dimension(900, 1000));
                jPanel8.setMinimumSize(new java.awt.Dimension(900, 150));
                jPanel8.setPreferredSize(new java.awt.Dimension(900, 150));
                jPanel8.setRequestFocusEnabled(false);
                jPanel8.setLayout(new java.awt.GridLayout(6, 1));

                jPanel10.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel10.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                jLabel2.setText("products 쿠팡 상품의 상세 정보를 생성함");
                jPanel10.add(jLabel2);

                jPanel8.add(jPanel10);

                jPanel13.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel13.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                jCheckBox1.setName(""); // NOI18N
                jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jCheckBox1ActionPerformed(evt);
                        }
                });
                jPanel13.add(jCheckBox1);

                jLabel3.setText("카테고리 별 베스트 상품");
                jPanel13.add(jLabel3);

                jLabel14.setForeground(new java.awt.Color(255, 0, 0));
                jLabel14.setText("카테고리ID");
                jPanel13.add(jLabel14);

                categoryIdJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "전체", "1001: 여성패션", "", "1002: 남성패션", "", "1003: 베이비패션 (0~3세)", "", "1004: 여아패션 (3세 이상)", "", "1005: 남아패션 (3세 이상)", "", "1006: 스포츠패션", "", "1007: 신발", "", "1008: 가방/잡화", "", "1010: 뷰티", "", "1011: 출산/유아동", "", "1012: 식품", "", "1013: 주방용품", "", "1014: 생활용품", "", "1015: 홈인테리어", "", "1016: 가전디지털", "", "1017: 스포츠/레저", "", "1018: 자동차용품", "", "1019: 도서/음반/DVD", "", "1020: 완구/취미", "", "1021: 문구/오피스", "", "1024: 헬스/건강식품", "", "1025: 국내여행", "", "1026: 해외여행", "", "1029: 반려동물용품" }));
                categoryIdJComboBox.setMinimumSize(new java.awt.Dimension(200, 21));
                categoryIdJComboBox.setPreferredSize(new java.awt.Dimension(200, 21));
                jPanel13.add(categoryIdJComboBox);

                jPanel8.add(jPanel13);

                jPanel15.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel15.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                goldboxJCheckBox.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                goldboxJCheckBoxActionPerformed(evt);
                        }
                });
                jPanel15.add(goldboxJCheckBox);

                jLabel4.setText("골드박스 상품");
                jPanel15.add(jLabel4);

                jPanel8.add(jPanel15);

                jPanel17.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel17.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                coupangPLJCheckBox.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                coupangPLJCheckBoxActionPerformed(evt);
                        }
                });
                jPanel17.add(coupangPLJCheckBox);

                jLabel5.setText("쿠팡 PL 상품");
                jPanel17.add(jLabel5);

                jPanel8.add(jPanel17);

                jPanel25.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel25.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                coupangPLBrandJCheckBox.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                coupangPLBrandJCheckBoxActionPerformed(evt);
                        }
                });
                jPanel25.add(coupangPLBrandJCheckBox);

                jLabel11.setText("쿠팡 PL 브랜드 별 상품");
                jPanel25.add(jLabel11);

                jLabel13.setForeground(new java.awt.Color(255, 0, 0));
                jLabel13.setText("브랜드ID");
                jPanel25.add(jLabel13);

                brandIdJComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "전체", "1001: 탐사", "1002: 코멧", "1003: Gomgom", "1004: 줌", "1005: 마케마케", "1006: 곰곰", "1007: 꼬리별", "1008: 베이스알파에센셜", "1009: 요놈", "1010: 비타할로", "1011: 비지엔젤", "1012: 타이니스타" }));
                jPanel25.add(brandIdJComboBox);

                jPanel8.add(jPanel25);

                jPanel26.setMinimumSize(new java.awt.Dimension(800, 20));
                jPanel26.setPreferredSize(new java.awt.Dimension(800, 25));
                jPanel26.setRequestFocusEnabled(false);
                jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                searchJCheckBox.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                searchJCheckBoxActionPerformed(evt);
                        }
                });
                jPanel26.add(searchJCheckBox);

                jLabel12.setText("쿠팡 검색 상품");
                jPanel26.add(jLabel12);

                jLabel20.setForeground(new java.awt.Color(255, 0, 0));
                jLabel20.setText("키워드");
                jPanel26.add(jLabel20);

                keywordTf.setMinimumSize(new java.awt.Dimension(100, 21));
                keywordTf.setPreferredSize(new java.awt.Dimension(100, 21));
                jPanel26.add(keywordTf);

                keywordDeleteJButton.setText("지우기");
                keywordDeleteJButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                keywordDeleteJButtonActionPerformed(evt);
                        }
                });
                jPanel26.add(keywordDeleteJButton);

                jPanel8.add(jPanel26);

                jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

                jPanel7.setMinimumSize(new java.awt.Dimension(50, 0));
                jPanel7.setPreferredSize(new java.awt.Dimension(80, 0));
                jPanel7.setLayout(new java.awt.GridLayout(6, 0));
                jPanel7.add(jPanel28);

                jPanel32.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel32.add(bestcategoriesResultLbl);

                jPanel7.add(jPanel32);

                jPanel33.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel33.add(goldboxResultLbl);

                jPanel7.add(jPanel33);

                jPanel34.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel34.add(coupangPLResultLbl);

                jPanel7.add(jPanel34);

                jPanel35.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel35.add(coupangPLBrandResultLbl);

                jPanel7.add(jPanel35);

                jPanel36.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel36.add(searchResultLbl);

                jPanel7.add(jPanel36);

                jPanel5.add(jPanel7, java.awt.BorderLayout.EAST);

                jPanel14.add(jPanel5);

                jPanel37.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                jPanel37.setMinimumSize(new java.awt.Dimension(1000, 400));
                jPanel37.setPreferredSize(new java.awt.Dimension(1000, 400));
                jPanel37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

                jPanel2.setPreferredSize(new java.awt.Dimension(990, 24));
                jPanel2.setLayout(new java.awt.BorderLayout());

                jLabel15.setText("NID_AUT");
                jLabel15.setPreferredSize(new java.awt.Dimension(100, 15));
                jPanel2.add(jLabel15, java.awt.BorderLayout.WEST);

                jScrollPane1.setPreferredSize(new java.awt.Dimension(500, 24));

                nidAutTa.setColumns(20);
                nidAutTa.setLineWrap(true);
                nidAutTa.setRows(1);
                nidAutTa.setPreferredSize(new java.awt.Dimension(500, 24));
                jScrollPane1.setViewportView(nidAutTa);

                jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
                jPanel2.add(jPanel38, java.awt.BorderLayout.EAST);

                jPanel37.add(jPanel2);

                jPanel39.setPreferredSize(new java.awt.Dimension(990, 150));
                jPanel39.setLayout(new java.awt.BorderLayout());

                jLabel21.setText("NID_SES");
                jLabel21.setPreferredSize(new java.awt.Dimension(100, 15));
                jPanel39.add(jLabel21, java.awt.BorderLayout.WEST);

                jScrollPane2.setPreferredSize(new java.awt.Dimension(500, 0));

                nidSesTa.setColumns(20);
                nidSesTa.setLineWrap(true);
                nidSesTa.setRows(7);
                jScrollPane2.setViewportView(nidSesTa);

                jPanel39.add(jScrollPane2, java.awt.BorderLayout.CENTER);
                jPanel39.add(jPanel40, java.awt.BorderLayout.EAST);

                jPanel37.add(jPanel39);

                jPanel14.add(jPanel37);

                getContentPane().add(jPanel14, java.awt.BorderLayout.CENTER);

                jPanel1.setMinimumSize(new java.awt.Dimension(1000, 50));
                jPanel1.setPreferredSize(new java.awt.Dimension(1000, 50));
                jPanel1.setRequestFocusEnabled(false);

                jButton1.setText("저장");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });
                jPanel1.add(jButton1);

                getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

                pack();
        }// </editor-fold>//GEN-END:initComponents

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		bestcategoriesResultLbl.setText("");
		goldboxResultLbl.setText("");
		coupangPLResultLbl.setText("");
		coupangPLBrandResultLbl.setText("");
		searchResultLbl.setText("");

		save();
        }//GEN-LAST:event_jButton1ActionPerformed

	private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jCheckBox1ActionPerformed
		// TODO add your handling code here:
		JCheckBox jCheckBox = (JCheckBox) evt.getSource();
		isJCheckBox1Selected = jCheckBox.isSelected();
		System.out.println(isJCheckBox1Selected);
	}// GEN-LAST:event_jCheckBox1ActionPerformed

	private void goldboxJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_goldboxJCheckBoxActionPerformed
		// TODO add your handling code here:
		bestcategoriesResultLbl.setText("");
		goldboxResultLbl.setText("");
		coupangPLResultLbl.setText("");
		coupangPLBrandResultLbl.setText("");
		searchResultLbl.setText("");

		JCheckBox jCheckBox = (JCheckBox) evt.getSource();
		isGoldboxJCheckBoxSelected = jCheckBox.isSelected();
		System.out.println(isGoldboxJCheckBoxSelected);
	}// GEN-LAST:event_goldboxJCheckBoxActionPerformed

	private void coupangPLJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_coupangPLJCheckBoxActionPerformed
		// TODO add your handling code here:
		JCheckBox jCheckBox = (JCheckBox) evt.getSource();
		isCoupangPLJCheckBoxSelected = jCheckBox.isSelected();
		System.out.println(isCoupangPLJCheckBoxSelected);
	}// GEN-LAST:event_coupangPLJCheckBoxActionPerformed

	private void coupangPLBrandJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_coupangPLBrandJCheckBoxActionPerformed
		// TODO add your handling code here:
		JCheckBox jCheckBox = (JCheckBox) evt.getSource();
		isCoupangPLBrandJCheckBoxSelected = jCheckBox.isSelected();
		System.out.println(isCoupangPLBrandJCheckBoxSelected);
	}// GEN-LAST:event_coupangPLBrandJCheckBoxActionPerformed

	private void searchJCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_searchJCheckBoxActionPerformed
		// TODO add your handling code here:
		JCheckBox jCheckBox = (JCheckBox) evt.getSource();
		isSearchJCheckBoxSelected = jCheckBox.isSelected();
		System.out.println(isSearchJCheckBoxSelected);
	}// GEN-LAST:event_searchJCheckBoxActionPerformed

	private void keywordDeleteJButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_keywordDeleteJButtonActionPerformed
		// TODO add your handling code here:
		keywordTf.setText("");
	}// GEN-LAST:event_keywordDeleteJButtonActionPerformed

	int countCheckedCheckBoxes() {

		int checkNums = 0;
		if (jCheckBox1.isSelected()) {
			checkNums++;
		}
		if (goldboxJCheckBox.isSelected()) {
			checkNums++;
		}
		if (coupangPLJCheckBox.isSelected()) {
			checkNums++;
		}
		if (coupangPLBrandJCheckBox.isSelected()) {
			checkNums++;
		}
		if (searchJCheckBox.isSelected()) {
			checkNums++;
		}
		return checkNums;
	}

	public void save() {
		ACCESS_KEY = accessKeyTf.getText();
		SECRET_KEY = secretKeyTf.getText();
		boolean bResult = false;
		System.out.println("isJCheckBox1Selected:" + isJCheckBox1Selected);
		System.out.println("isGoldboxJCheckBoxSelected:" + isGoldboxJCheckBoxSelected);
		System.out.println("isCoupangPLJCheckBoxSelected:" + isCoupangPLJCheckBoxSelected);
		System.out.println("isCoupangPLBrandJCheckBoxSelected:" + isCoupangPLBrandJCheckBoxSelected);
		System.out.println("isSearchJCheckBoxSelected:" + isSearchJCheckBoxSelected);
		System.out.println("isClicksJCheckBoxSelected:" + isClicksJCheckBoxSelected);
		System.out.println("isCancelsJCheckBoxSelected:" + isCancelsJCheckBoxSelected);
		System.out.println("isCancelsJCheckBoxSelected:" + isCancelsJCheckBoxSelected);
		System.out.println("isLinkJCheckBoxSelected:" + isLinkJCheckBoxSelected);

		int selectedCheckboxCount = countCheckedCheckBoxes();
		if (selectedCheckboxCount == 0) {
			JOptionPane.showMessageDialog(rootPane, "선택한 항목이 없습니다.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String strNidAut = nidAutTa.getText();
		String strNidSes = nidSesTa.getText();

		if (strNidAut.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_AUT를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		} else if (strNidSes.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_SES를 입력하여 주세요.", "주의", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (isJCheckBox1Selected) {
			String item = categoryIdJComboBox.getItemAt(categoryIdJComboBox.getSelectedIndex());
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

		if (isGoldboxJCheckBoxSelected) {
			bResult = getGoldboxProducts();
			if (bResult) {
				goldboxResultLbl.setText("처리 완료");
			}
		}

		if (isCoupangPLJCheckBoxSelected) {
			bResult = getCoupangPLProducts();
			if (bResult) {
				coupangPLResultLbl.setText("처리 완료");
			}
		}

		if (isCoupangPLBrandJCheckBoxSelected) {
			// getCoupangPLBrandProducts();
			String item = brandIdJComboBox.getItemAt(brandIdJComboBox.getSelectedIndex());
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

		if (isSearchJCheckBoxSelected) {
			String keyword = keywordTf.getText();
			bResult = getSearchProducts(keyword);
			if (bResult) {
				searchResultLbl.setText("처리 완료");
			}
		}

	}// GEN-LAST:event_jButton1ActionPerformed

	private void secretKeyTfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_secretKeyTfActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_secretKeyTfActionPerformed

	private void secretKeyTfDelBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_secretKeyTfDelBtnActionPerformed
		// TODO add your handling code here:
		secretKeyTf.setText("");
	}// GEN-LAST:event_secretKeyTfDelBtnActionPerformed

	private void accessKeyTfActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_accessKeyTfActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_accessKeyTfActionPerformed

	private void accessKeyTfDelBtnActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_accessKeyTfDelBtnActionPerformed
		// TODO add your handling code here:
		accessKeyTf.setText("");
	}// GEN-LAST:event_accessKeyTfDelBtnActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton3ActionPerformed
		// TODO add your handling code here:
		try {
//			File f = new File("coupangPartners.properties");
			File f = new File("./coupangPartners.properties");
			FileWriter fw;
			fw = new FileWriter(f);
			fw.write("access_key=" + accessKeyTf.getText() + "\r\n");
			fw.write("secret_key=" + secretKeyTf.getText() + "\r\n");
			fw.flush();
			fw.close();
		} catch (IOException ex) {
			Logger.getLogger(CoupangPartnersApiOneFileNaverLinkShareSimple.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println("파일 저장 완료");
	}// GEN-LAST:event_jButton3ActionPerformed

	public String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}
		return null;
	}

	public String getShortenedUrl(String normalUrl) {
		System.out.println("normalUrl :" + normalUrl);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		strDate = sdf.format(new Date());
		String shortenUrl = "";
		String originalUrl = "";
		String landingUrl = "";
		// Generate HMAC string
		String authorization = generate(REQUEST_METHOD_POST, DEEPLINK_URL, ACCESS_KEY, SECRET_KEY);

		// Send request
		String strNormalUrlJson = "{\"coupangUrls\": [\"" + normalUrl + "\"]}";
		StringEntity entity = new StringEntity(strNormalUrlJson, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		HttpHost host = org.apache.http.HttpHost.create(DOMAIN);
		HttpRequest request = org.apache.http.client.methods.RequestBuilder.post(DEEPLINK_URL).setEntity(entity)
			.addHeader("Authorization", authorization).build();

		org.apache.http.HttpResponse httpResponse;
		try {
			httpResponse = org.apache.http.impl.client.HttpClientBuilder.create().build().execute(host, request);
			// verify
			String returnJson = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("returnJson :" + returnJson);
			JSONObject jsonObject = new JSONObject(returnJson);
			System.out.println("jsonObject:" + jsonObject.toString());
			Set keySet = jsonObject.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				System.out.println("key:" + key);
				Object obj = jsonObject.get(key);
				System.out.println("value:" + obj.toString());
				if (key.equals("data")) {
					JSONArray datas = (JSONArray) jsonObject.get("data");
					if (datas.length() > 0) {
						JSONObject data = (JSONObject) datas.get(0);
						shortenUrl = data.getString("shortenUrl");
						originalUrl = data.getString("originalUrl");
						landingUrl = data.getString("landingUrl");
					}
					System.out.println("datas:" + datas.toString());
					System.out.println("shortenUrl:" + shortenUrl);
					System.out.println("originalUrl:" + originalUrl);
					System.out.println("landingUrl:" + landingUrl);
				}
			}

		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		return shortenUrl;
	}

	public String getReportsClicks(String startDate, String endDate) {
		String strParamJson = "";
		String reports_url = REPORTS_CLICKS_URL + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("클릭상품", reports_url, "", strParamJson);
	}

	public String getReportsOrders(String startDate, String endDate) {
		String strParamJson = "";
		String reports_url = REPORTS_ORDERS_URL + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("주문상품", reports_url, "", strParamJson);
	}

	public String getReportsCancels(String startDate, String endDate) {
		String strParamJson = "";
		String reports_url = REPORTS_CANCELS_URL + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("취소상품", reports_url, "", strParamJson);
	}

	public void naverBlogLinkShare(StringBuilder contentSb, String strCategoryName, String strShareTitle) {
		strNidAut = nidAutTa.getText();
		strNidSes = nidSesTa.getText();
		String strShareUrl = "";
		NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryName, contentSb, rootPane);
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
		sb.append(COUPANG_PARTNERS_NOTICE);

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
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append(COUPANG_PARTNERS_NOTICE);

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
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "WOW 와우회원 전용 매일 오전 7시 골드박스 1일특가";
		String strBlogCategoryName = "골드박스 상품";
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
		sb.append(COUPANG_PARTNERS_NOTICE);

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
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append(COUPANG_PARTNERS_NOTICE);

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
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		SEARCH_URL = SEARCH_URL + "?keyword=" + encodedKeyword + "&limit=" + limit;
//		server_url = server_url + "?keyword=food&limit=" + limit;
		System.out.println("server_url:" + SEARCH_URL);
		String data = getData("상품검색", SEARCH_URL, "", strParamJson);
		sb.append(data);
		sb.append(COUPANG_PARTNERS_NOTICE);

		String shareTitle = strYmdBlacket + " " + "상품검색";
		String strBlogCategoryName = "추천 상품";
		naverBlogLinkShare(sb, strBlogCategoryName, shareTitle);
		return true;
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
							Logger.getLogger(CoupangPartnersApiOneFileNaverLinkShareSimple.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					sb.append("</ul>");
					sb.append("</div>");
				}

			}

		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
		return sb.toString();
	}

	/**
	 * Generate HMAC signature
	 *
	 * @param method
	 * @param uri http request uri
	 * @param accessKey access key that Coupang partner granted for calling
	 * open api
	 * @param secretKey secret key that Coupang partner granted for calling
	 * open api
	 * @return HMAC signature
	 */
	public static String generate(String method, String uri, String accessKey, String secretKey) {
		String[] parts = uri.split("\\?");
		if (parts.length > 2) {
			throw new RuntimeException("incorrect uri format");
		} else {
			String path = parts[0];
			String query = "";
			if (parts.length == 2) {
				query = parts[1];
			}

			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyMMdd'T'HHmmss'Z'");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			String datetime = dateFormatGmt.format(new Date());
			String message = datetime + method + path + query;

			String signature;
			try {
				SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(STANDARD_CHARSET), ALGORITHM);
				Mac mac = Mac.getInstance(ALGORITHM);
				mac.init(signingKey);
				byte[] rawHmac = mac.doFinal(message.getBytes(STANDARD_CHARSET));
				signature = Hex.encodeHexString(rawHmac);
			} catch (GeneralSecurityException e) {
				throw new IllegalArgumentException("Unexpected error while creating hash: " + e.getMessage(), e);
			}

			return String.format("CEA algorithm=%s, access-key=%s, signed-date=%s, signature=%s", "HmacSHA256",
				accessKey, datetime, signature);
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(CoupangPartnersApiOneFileNaverLinkShareSimple.class.getName())
				.log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CoupangPartnersApiOneFileNaverLinkShareSimple().setVisible(true);
			}
		});
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTextField accessKeyTf;
        private javax.swing.JButton accessKeyTfDelBtn;
        private javax.swing.JLabel bestcategoriesResultLbl;
        private javax.swing.JComboBox<String> brandIdJComboBox;
        private javax.swing.ButtonGroup buttonGroup1;
        private javax.swing.ButtonGroup buttonGroup2;
        private javax.swing.ButtonGroup buttonGroup3;
        private javax.swing.ButtonGroup buttonGroup4;
        private javax.swing.JComboBox<String> categoryIdJComboBox;
        private javax.swing.JCheckBox coupangPLBrandJCheckBox;
        private javax.swing.JLabel coupangPLBrandResultLbl;
        private javax.swing.JCheckBox coupangPLJCheckBox;
        private javax.swing.JLabel coupangPLResultLbl;
        private javax.swing.JCheckBox goldboxJCheckBox;
        private javax.swing.JLabel goldboxResultLbl;
        private javax.swing.JButton jButton1;
        private javax.swing.JButton jButton3;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel12;
        private javax.swing.JLabel jLabel13;
        private javax.swing.JLabel jLabel14;
        private javax.swing.JLabel jLabel15;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel20;
        private javax.swing.JLabel jLabel21;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel10;
        private javax.swing.JPanel jPanel13;
        private javax.swing.JPanel jPanel14;
        private javax.swing.JPanel jPanel15;
        private javax.swing.JPanel jPanel16;
        private javax.swing.JPanel jPanel17;
        private javax.swing.JPanel jPanel19;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel25;
        private javax.swing.JPanel jPanel26;
        private javax.swing.JPanel jPanel28;
        private javax.swing.JPanel jPanel32;
        private javax.swing.JPanel jPanel33;
        private javax.swing.JPanel jPanel34;
        private javax.swing.JPanel jPanel35;
        private javax.swing.JPanel jPanel36;
        private javax.swing.JPanel jPanel37;
        private javax.swing.JPanel jPanel38;
        private javax.swing.JPanel jPanel39;
        private javax.swing.JPanel jPanel40;
        private javax.swing.JPanel jPanel5;
        private javax.swing.JPanel jPanel7;
        private javax.swing.JPanel jPanel8;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JButton keywordDeleteJButton;
        private javax.swing.JTextField keywordTf;
        private javax.swing.JTextArea nidAutTa;
        private javax.swing.JTextArea nidSesTa;
        private javax.swing.JCheckBox searchJCheckBox;
        private javax.swing.JLabel searchResultLbl;
        private javax.swing.JTextField secretKeyTf;
        private javax.swing.JButton secretKeyTfDelBtn;
        // End of variables declaration//GEN-END:variables

	@Override
	public void windowOpened(WindowEvent e) {
		System.out.println("WindowListener method called: windowOpened.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("WindowListener method called: windowClosing.");
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.out.println("WindowListener method called: windowClosed.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowIconified(WindowEvent e) {
		System.out.println("WindowListener method called: windowIconified.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		System.out.println("WindowListener method called: windowDeiconified.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowActivated(WindowEvent e) {
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		System.out.println("WindowListener method called: windowActivated.");
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		System.out.println("WindowListener method called: windowDeactivated.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		System.out.println("WindowListener method called: windowGainedFocus.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowLostFocus(WindowEvent e) {
		System.out.println("WindowListener method called: windowLostFocus.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		System.out.println("WindowListener method called: windowStateChanged.");
//		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
