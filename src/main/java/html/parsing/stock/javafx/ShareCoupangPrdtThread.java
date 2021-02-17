package html.parsing.stock.javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coupang.partners.CoupangApiConstants;
import com.coupang.partners.HmacGenerator;

import html.parsing.stock.util.NaverUtil;

public class ShareCoupangPrdtThread extends Thread {

	private static Logger logger = LoggerFactory.getLogger(ShareCoupangPrdtThread.class);

	DecimalFormat df = new DecimalFormat("#,##0");

	SimpleDateFormat sdf0 = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
	String strYmdBlacket = sdf0.format(new Date());

	private final static String REQUEST_METHOD_GET = "GET";
	private final static String DOMAIN = "https://api-gateway.coupang.com";
	private final static String API_PATH = "/v2/providers/affiliate_open_api/apis/openapi/v1";
	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
	private static String SEARCH_URL = API_PATH + "/products/search";

	private String strUserId;
	private String strNidAut;
	private String strNidSes;
	private String strBlogCategoryNo;

	private String SECRET_KEY;
	private String ACCESS_KEY;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();

		ShareCoupangPrdtThread list1 = new ShareCoupangPrdtThread();
		list1.start();

		long endTime = System.currentTimeMillis();
		String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
		System.out.println("call time :" + elapsedTimeSecond);
		System.out.println("main method call finished.");
	}

	ShareCoupangPrdtThread() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public ShareCoupangPrdtThread(String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}
	
	public ShareCoupangPrdtThread(String strUserId, String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.strUserId = strUserId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}
	
	@Override
	public void run() {
		initKeys();
		execute();
	}

	ShareCoupangPrdtThread(int i) {
	}

	public void execute() {
		long start = System.currentTimeMillis();

		// 키워드를 검색
		String selectedKeyword = getFavoriteKeyword();
		logger.debug("selectedKeyword :" + selectedKeyword);
		boolean getResult = getSearchProducts(selectedKeyword);
		logger.debug("getResult :" + getResult);

		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
	}

	// 네이버 블로그에 공유
//	public void naverBlogLinkShare(StringBuilder html) {
//		String strUrl = "";
//		String strTitle = Jsoup.parse(html.toString()).select("h2#title").text();
//		strBlogCategoryNo = "146";// 증권↑↓↗↘
//		StringBuilder contentSb = html;
//		logger.debug("strNidAut:" + strNidAut);
//		logger.debug("strNidSes:" + strNidSes);
//		if (!StringUtils.defaultIfEmpty(strNidAut, "").equals("")
//				&& !StringUtils.defaultIfEmpty(strNidSes, "").equals("")
//				&& !StringUtils.defaultIfEmpty(strUserId, "").equals("")
//				) {
//			NaverUtil.naverBlogLinkShare(strUserId, strNidAut, strNidSes, strUrl, strTitle, strBlogCategoryNo, contentSb, null);
//		}
//	}

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
				this.ACCESS_KEY = accessKey;
				this.SECRET_KEY = secretKey;
				PropertyConfigurator.configure(props);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다. 6분마다 호출하면 됨)
	// SEARCH_URL = API_PATH + "​/products​/search";
	public boolean getSearchProducts(String keyword) {
		StringBuilder sb = new StringBuilder();
//		sb.append("<div style='width:100%;'><h1>").append(strYmdBlacket).append(" ").append("쿠팡을 추천 합니다!쿠팡! | ").append(keyword) .append("</h1></div>");
		sb.append("<div style='width:100%;'><h1>").append("쿠팡을 추천 합니다!쿠팡! | ").append(keyword) .append("</h1></div>");
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
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

//		String shareTitle = strYmdBlacket + " " + keyword;
		String shareTitle = "[쿠팡 추천 상품] " + keyword;
		
		strBlogCategoryNo = NaverUtil.getNaverBlogCategoryNo(strNidAut, strNidSes, "추천 상품");
		
		naverBlogLinkShare(sb, strBlogCategoryNo, shareTitle);
		return true;
	}

	public String getData(String apiGubun, String server_url, String categoryNm, String strParamJson) {
		System.out.println("server_url :" + server_url);
		StringBuilder sb = new StringBuilder();
		// Generate HMAC string
		String authorization = HmacGenerator.generate(REQUEST_METHOD_GET, server_url, SECRET_KEY, ACCESS_KEY);
		System.out.println("authorization :" + authorization);
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

						StringBuilder liStyleSb = new StringBuilder();
						liStyleSb.append("float:left;");
						liStyleSb.append("width:250px;");
						liStyleSb.append("height:430px;");
						liStyleSb.append("background-color: #fff;");
						liStyleSb.append("box-shadow: none;");
						liStyleSb.append("border: 1px solid #dfe1e5;");
						liStyleSb.append("border-radius: 8px;");
						liStyleSb.append("overflow: hidden;");
						liStyleSb.append("margin: 0 0 6px 0;");
						liStyleSb.append("margin-right:8px;");
						liStyleSb.append("margin-top:1px;");
						liStyleSb.append("padding:5px 10px;");

						sb.append("<li style='" + liStyleSb.toString() + "'>");
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

	public void naverBlogLinkShare(StringBuilder contentSb, String strCategoryNo, String strShareTitle) {
		String strShareUrl = "";
		logger.debug("strUserId :" + strUserId);
		logger.debug("strNidAut :" + strNidAut);
		logger.debug("strNidSes :" + strNidSes);
		if (!strNidAut.equals("") && !strNidSes.equals("") && !strUserId.equals("")
				) {
			NaverUtil.naverBlogLinkShare(strUserId, strNidAut, strNidSes, strShareUrl, strShareTitle, strCategoryNo, contentSb,
					null);
		}
	}

	public String getFavoriteKeyword() {
		String favoriteKeyword = "";
		List<String> favoriteKeywordsList = new ArrayList<String>();
		try {
			System.out.println("getClass().getProtectionDomain():" + getClass().getProtectionDomain());
			System.out.println("getClass().getProtectionDomain().getCodeSource():"
					+ getClass().getProtectionDomain().getCodeSource());
			System.out.println("getClass().getProtectionDomain().getCodeSource().getLocation():"
					+ getClass().getProtectionDomain().getCodeSource().getLocation());
			System.out.println("getClass().getProtectionDomain().getCodeSource().getLocation().getPath():"
					+ getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			/// D:/workspace-spring-tool-suite-4-4.3.1.RELEASE/MavenStockProject/target/classes/
			System.out.println("getClass().getProtectionDomain().getClassLoader():"
					+ getClass().getProtectionDomain().getClassLoader());
			System.out
					.println("getClass().getProtectionDomain().getClassLoader().getResource(\"favoriteKeywords.dat\"):"
							+ getClass().getProtectionDomain().getClassLoader().getResource("favoriteKeywords.dat"));

			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			System.out.println(". AbsolutePath:" + new File(".").getAbsolutePath());
			File f = new File("./favoriteKeywords.dat");
			System.out.println("f.exists():" + f.exists());
			if (f.exists()) {
				System.out.println("./favoriteKeywords.dat File exists");
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String keyword = "";
				while ((keyword = br.readLine()) != null) {
					favoriteKeywordsList.add(keyword);
				}
				br.close();
			} else {
				// classes root 경로
				InputStream is = null;
				is = getClass().getResourceAsStream("/favoriteKeywords.dat");
//				is = getClass().getClassLoader().getResourceAsStream("/favoriteKeywords.dat");
				System.out.println("class 경로 read /favoriteKeywords.dat Resource InputStream:" + is);

				String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
				System.out.println("rootPath :" + rootPath);

				String defaultConfigPath = rootPath + "favoriteKeywords.dat";
				f = new File(defaultConfigPath);
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String keyword = "";
				while ((keyword = br.readLine()) != null) {
					favoriteKeywordsList.add(keyword);
				}
				br.close();
				fr.close();
			}

			int randomNumber = (int) (Math.random() * favoriteKeywordsList.size());
			int index = randomNumber > 0 ? randomNumber - 1 : 0;
			favoriteKeyword = favoriteKeywordsList.get(index);

			System.out.println("randomNumber :" + randomNumber);
			System.out.println("index :" + index);
			System.out.println("favoriteKeyword :" + favoriteKeyword);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return favoriteKeyword;
	}

}
