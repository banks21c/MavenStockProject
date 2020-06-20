/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coupang.partners.urlgenerate;

import html.parsing.stock.util.FileUtil;
import java.io.File;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

public final class OpenApiTestApplication11 {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(OpenApiTestApplication11.class);
	private final static String REQUEST_METHOD_POST = "POST";
	private final static String REQUEST_METHOD_GET = "GET";
	private final static String DOMAIN = "https://api-gateway.coupang.com";
	private final static String API_PATH = "/v2/providers/affiliate_open_api/apis/openapi/v1";
	private final static String DEEPLINK_URL = API_PATH + "/deeplink";
	private static String strFileName = "";
	final static String USER_HOME = System.getProperty("user.home");
	private static SimpleDateFormat sdf_ymd = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
	private static String strYmd = sdf_ymd.format(new Date());

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
	private static String strDate = sdf.format(new Date());

	DecimalFormat df = new DecimalFormat("#,##0");
	//채널ID
	private final static String subId = "";

	//GET
	//카테고리 별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
//	private final static String BESTCATEGORIES_URL = API_PATH + "​/products​/bestcategories​/{categoryId}";
	private final static String BESTCATEGORIES_URL = API_PATH + "/products/bestcategories/";
	String[][] bestCategoriesArray = {
		{"1001", "여성패션"},
		{"1002", "남성패션"},
		{"1003", "베이비패션 (0~3세)"},
		{"1004", "여아패션 (3세 이상)"},
		{"1005", "남아패션 (3세 이상)"},
		{"1006", "스포츠패션"},
		{"1007", "신발"},
		{"1008", "가방/잡화"},
		{"1010", "뷰티"},
		{"1011", "출산/유아동"},
		{"1012", "식품"},
		{"1013", "주방용품"},
		{"1014", "생활용품"},
		{"1015", "홈인테리어"},
		{"1016", "가전디지털"},
		{"1017", "스포츠/레저"},
		{"1018", "자동차용품"},
		{"1019", "도서/음반/DVD"},
		{"1020", "완구/취미"},
		{"1021", "문구/오피스"},
		{"1024", "헬스/건강식품"},
		{"1025", "국내여행"},
		{"1026", "해외여행"},
		{"1029", "반려동물용품"}
	};
	//골드박스 상품에 대한 상세 상품 정보를 생성합니다. (골드박스 상품은 매일 오전 7:30에 업데이트 됩니다)
	private final static String GOLDBOX_URL = API_PATH + "/products/goldbox";
	//쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
	private final static String COUPANG_PL_URL = API_PATH + "/products/coupangPL";
	//쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
//	private final static String COUPANG_PL_BRAND_URL = API_PATH +"/products/coupangPL/{brandId}";
	private final static String COUPANG_PL_BRAND_URL = API_PATH + "/products/coupangPL/";
	private final static String coupangPlBrandArray[][] = {
		{"1001", "탐사"},
		{"1002", "코멧"},
		{"1003", "Gomgom"},
		{"1004", "줌"},
		{"1005", "마케마케"},
		{"1006", "곰곰"},
		{"1007", "꼬리별"},
		{"1008", "베이스알파에센셜"},
		{"1009", "요놈"},
		{"1010", "비타할로"},
		{"1011", "비지엔젤"},
		{"1012", "타이니스타"}
	};
	//검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
	private final static String SEARCH_URL = API_PATH + "/products/search";

//	private final static String REPORTS_CLICKS_URL = API_PATH+"/v1/reports/clicks";
//	private final static String REPORTS_ORDERS_URL = API_PATH+"/v1/reports/orders";
//	private final static String REPORTS_CANCELS_URL = API_PATH+"/v1/reports/cancels";
	private final static String REPORTS_CLICKS_URL = API_PATH + "/reports/clicks";
	private final static String REPORTS_ORDERS_URL = API_PATH + "/reports/orders";
	private final static String REPORTS_CANCELS_URL = API_PATH + "/reports/cancels";
	// Replace with your own ACCESS_KEY and SECRET_KEY
	private final static String ACCESS_KEY = "1895fbee-cac6-456a-9d9e-7b198e8735b8";
	private final static String SECRET_KEY = "a59ac9889dbeb7b32cd7304bf361e13c05e0387a";
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/search?component=&q=good&channel=user\",\"https://www.coupang.com/np/coupangglobal\"]}";
	//실패
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://pages.coupang.com/f/s299\"]}";
	//성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/\"]}";
	//성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/goldbox\"]}";
	//성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://loyalty.coupang.com/loyalty/sign-up/home\"]}";
	//성공
//    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/campaigns/82\"]}";
	//성공
	private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/coupangglobal\"]}";

	OpenApiTestApplication11() {
//		getShortenedUrl("https://www.coupang.com/np/coupangglobal");
//		getReportsClicks(REPORTS_CLICKS_URL,"20200501", "20200630");
//		getReportsOrders(REPORTS_ORDERS_URL,"20200501", "20200630");
//		getReportsCancels(REPORTS_CANCELS_URL, "20200501", "20200630");

		//카테고리 별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
//		getBestcategoryProducts(BESTCATEGORIES_URL);
//		getGoldboxProducts(GOLDBOX_URL);
		//쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
		//COUPANG_PL_URL = API_PATH + "​​/products​/coupangPL";
//		getCoupangPLProducts(COUPANG_PL_URL);
		//쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
//		getCoupangPLBrandProducts(COUPANG_PL_BRAND_URL);
		//검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
		getSearchProducts(SEARCH_URL, "새싹보리");
	}

	public static String getShortenedUrl(String normalUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmd).append(" ").append("쿠팡 단축 URL").append("</h1></div>");

		String shortenUrl = "";
		String originalUrl = "";
		String landingUrl = "";
		// Generate HMAC string
		String authorization = HmacGenerator.generate(REQUEST_METHOD_POST, DEEPLINK_URL, ACCESS_KEY, SECRET_KEY);

		// Send request
		String strNormalUrlJson = "{\"coupangUrls\": [\"" + normalUrl + "\"]}";
		StringEntity entity = new StringEntity(strNormalUrlJson, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		org.apache.http.HttpHost host = org.apache.http.HttpHost.create(DOMAIN);
		org.apache.http.HttpRequest request = org.apache.http.client.methods.RequestBuilder
			.post(DEEPLINK_URL).setEntity(entity)
			.addHeader("Authorization", authorization)
			.build();

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
			Logger.getLogger(OpenApiTestApplication11.class.getName()).log(Level.SEVERE, null, ex);
		}
		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_카테고리별 베스트 상품.html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
		return shortenUrl;
	}

	public String getReportsClicks(String reports_url, String startDate, String endDate) {
		String strParamJson = "";
		reports_url = reports_url + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("클릭상품", reports_url, "", strParamJson);
	}

	public String getReportsOrders(String reports_url, String startDate, String endDate) {
		String strParamJson = "";
		reports_url = reports_url + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("주문상품", reports_url, "", strParamJson);
	}

	public String getReportsCancels(String reports_url, String startDate, String endDate) {
		String strParamJson = "";
		reports_url = reports_url + "?startDate=" + startDate + "&endDate=" + endDate;
		return getData("취소상품", reports_url, "", strParamJson);
	}

	//카테고리별 베스트 상품에 대한 상세 상품 정보를 생성합니다.
	//BESTCATEGORIES_URL = API_PATH + "​/products​/bestcategories​/{categoryId}";
	public void getBestcategoryProducts(String bestcategoriesUrl) {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmd).append(" ").append("카테고리별 베스트 상품").append("</h1></div>");
		for (int i = 0; i < bestCategoriesArray.length; i++) {
			String codeValue[] = bestCategoriesArray[i];
			String categoryId = "";
			String categoryNm = "";
			if (codeValue.length == 2) {
				categoryId = codeValue[0];
				categoryNm = codeValue[1];
				System.out.println((i + 1) + "." + categoryId + ":" + categoryNm);
				String strParamJson = "";
				System.out.println("strParamJson:" + strParamJson);
				bestcategoriesUrl = bestcategoriesUrl + categoryId + "?limit=" + limit;
				String data = getData("카테고리별 베스트상품", bestcategoriesUrl, categoryNm, strParamJson);
				sb.append(data);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					Logger.getLogger(OpenApiTestApplication11.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_카테고리별 베스트 상품.html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
	}

	//골드박스 상품에 대한 상세 상품 정보를 생성합니다. (골드박스 상품은 매일 오전 7:30에 업데이트 됩니다)
	//GOLDBOX_URL = API_PATH + "​/products​/goldbox";
	public void getGoldboxProducts(String server_url) {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;padding-top:20px;float:left;'><h1>").append(strYmd).append(" ").append("골드박스 상품").append("</h1></div>");
		String strParamJson = "";
		System.out.println("strParamJson:" + strParamJson);
		String data = getData("골드박스 상품", server_url, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_골드박스 상품.html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
	}

	//쿠팡 PL 상품에 대한 상세 정보를 생성합니다.
	//COUPANG_PL_URL = API_PATH + "​​/products​/coupangPL";
	public void getCoupangPLProducts(String server_url) {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;padding-top:20px;float:left;'><h1>").append(strYmd).append(" ").append("쿠팡 PL 상품").append("</h1></div>");
		String strParamJson = "{\"limit\": \"" + limit + "\"}";
		System.out.println("strParamJson:" + strParamJson);
		String data = getData("쿠팡PL상품", server_url, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_쿠팡 PL 브랜드 별 상품.html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
	}

	//쿠팡 PL 브랜드 별 상품 상세 정보를 생성합니다.
	//COUPANG_PL_BRAND_URL = API_PATH + "​​/products​/coupangPL​/{brandId}";
	public void getCoupangPLBrandProducts(String server_url) {
		int limit = 20;
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmd).append(" ").append("쿠팡 PL 브랜드 별 상품").append("</h1></div>");
		for (int i = 0; i < coupangPlBrandArray.length; i++) {
			String codeValue[] = coupangPlBrandArray[i];
			String brandId = "";
			String brandNm = "";
			if (codeValue.length == 2) {
				brandId = codeValue[0];
				brandNm = codeValue[1];
				System.out.println((i + 1) + "." + brandId + ":" + brandNm);
				server_url = server_url + brandId + "?limit=" + limit;
				String strParamJson = "";
				String data = getData("쿠팡PL브랜드상품", server_url, brandNm, strParamJson);
				sb.append(data);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException ex) {
					Logger.getLogger(OpenApiTestApplication11.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_쿠팡 PL 브랜드 별 상품.html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
	}

	//검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다.)
	//SEARCH_URL = API_PATH + "​/products​/search";
	public String getSearchProducts(String server_url, String keyword) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100%;'><h1>").append(strYmd).append(" ").append("상품검색:").append(keyword).append("</h1></div>");
		String strParamJson = "";
		int limit = 20;
		String encodedKeyword = "";
		try {
			encodedKeyword = URLEncoder.encode(keyword, "UTF8");
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(OpenApiTestApplication1.class.getName()).log(Level.SEVERE, null, ex);
		}
		server_url = server_url + "?keyword=" + encodedKeyword + "&limit=" + limit;
//		server_url = server_url + "?keyword=food&limit=" + limit;
		logger.debug("server_url:" + server_url);
		String data = getData("상품검색", server_url, "", strParamJson);
		sb.append(data);
		sb.append("<div>※ 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있음</div>");

		strFileName = USER_HOME + File.separator + "documents" + File.separator + strDate + "_상품검색("+keyword+").html";
		FileUtil.fileWrite(strFileName, Jsoup.parse(sb.toString()).html());
//		FileUtil.fileWrite(strFileName, sb.toString());		
		return sb.toString();
	}

	public String getData(String apiGubun, String server_url, String categoryNm, String strParamJson) {
		System.out.println("server_url :" + server_url);
		StringBuilder sb = new StringBuilder();
		// Generate HMAC string
		String authorization = HmacGenerator.generate(REQUEST_METHOD_GET, server_url, ACCESS_KEY, SECRET_KEY);
		System.out.println("authorization:" + authorization);
		// Send request
		StringEntity entity = new StringEntity(strParamJson, "UTF-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");

		org.apache.http.HttpHost host = org.apache.http.HttpHost.create(DOMAIN);
		org.apache.http.HttpRequest request = org.apache.http.client.methods.RequestBuilder
			.get(server_url).setEntity(entity)
			.addHeader("Authorization", authorization)
			.build();

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
					logger.debug("data.length:" + data.length());
					if (!categoryNm.equals("")) {
						sb.append("<div style='width:100%;padding-top:20px;float:left;'><h3>").append(" ").append(categoryNm).append("</h3></div>");
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

						sb.append("<li style='float:left;width:250px;height:390px;background-color: #fff; box-shadow: none; border: 1px solid #dfe1e5; border-radius: 8px; overflow: hidden; margin: 0 0 6px 0;margin-right:8px;margin-top:1px;padding:5px 10px;'>");
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
								logger.debug("discountRate :" + discountRate);
								logger.debug("originalPrice :" + originalPrice);
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
							sb.append("<span style='overflow: hidden;display: block; left: 6px;top: 5px;width: 30px;height: 30px;text-indent: 0.5em; color:#fff;background-color:#f00;'>").append(rank).append("</span>");
						}
						sb.append("<a href='").append(productUrl).append("' target='new' style='text-decoration:none;'>");
						sb.append("<div>");
						sb.append("<img src='").append(productImage).append("' style='width:230px;height:230px;'>");
						sb.append("</div>");

						if (apiGubun.equals("골드박스 상품")) {
							sb.append("<div>");
							sb.append("<img src='http://image8.coupangcdn.com/image/badges/falcon/v1/web/rocketwow-bi-16@2x.png' alt='로켓와우' style='width:79px;height:20px;'>");
							sb.append("</div>");
						}

						sb.append("<div>");
						sb.append(productName);
						sb.append("</div>");
						sb.append("<div style='font-size:15px;color:red;background-color:yellow;'>");
						sb.append(discountRate);
						sb.append("</div>");
						sb.append("<div style='color:#888;text-decoration:line-through;'>");
						sb.append(originalPrice);
						sb.append("</div>");
						sb.append("<div style='font-size:16px;font-family:Tahoma;color: #ae0000;'>");
						sb.append(strProductPriceWithComma).append("원");
						if (isRocket) {
							sb.append("<span class='badge rocket'>");
							sb.append("<img src='http://image10.coupangcdn.com/image/badges/rocket/rocket_logo.png' height='16' alt='로켓배송'>");
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
						logger.debug("____________________________________");

						try {
							Thread.sleep(1000);
						} catch (InterruptedException ex) {
							Logger.getLogger(OpenApiTestApplication11.class.getName()).log(Level.SEVERE, null, ex);
						}
					}
					sb.append("</ul>");
					sb.append("</div>");
				}

			}

		} catch (IOException ex) {
			Logger.getLogger(OpenApiTestApplication11.class.getName()).log(Level.SEVERE, null, ex);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		new OpenApiTestApplication11();

	}
}
