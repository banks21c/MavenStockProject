/**
 * 
 */
package html.parsing.stock.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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

import html.parsing.stock.javafx.CoupangPartnersApiOneFileNaverLinkShareSimple;

/**
 * @author banksfamily
 *
 */
public class CoupangUtil {

	private static Logger logger = LoggerFactory.getLogger(CoupangUtil.class);

	private final static String COUPANG_PARTNERS_NOTICE = "<div>※ 쿠팡 파트너스 활동을 통해 일정액의 수수료를 제공받을 수 있습니다.</div>";
	private final static String REQUEST_METHOD_GET = "GET";
	private final static String DOMAIN = "https://api-gateway.coupang.com";
	private final static String API_PATH = "/v2/providers/affiliate_open_api/apis/openapi/v1";
	private static String SEARCH_URL = API_PATH + "/products/search";
	private static DecimalFormat df = new DecimalFormat("#,##0");

	/**
	 * 
	 */
	public CoupangUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// 검색 키워드에 대한 쿠팡 검색 결과와 상세 상품 정보를 생성합니다 (1 시간당 최대 10번 호출 가능합니다. 6분에 1번 호출 가능)
	// SEARCH_URL = API_PATH + "​/products​/search";
	public static boolean getSearchProducts(String keyword, String strBlogId, String strNidAut, String strNidSes,
			String strAccessKey, String strSecretKey) {
		StringBuilder contentSb = new StringBuilder();
		contentSb.append("<div style='width:100%;'><h1>").append(" ").append("쿠팡 제품 추천 합니다!쿠팡! |").append(keyword)
				.append("</h1></div>");
		String strParamJson = "";
		int limit = 20;
		String encodedKeyword = "";
		try {
			encodedKeyword = URLEncoder.encode(keyword, "UTF8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		String strSearchUrl = SEARCH_URL + "?keyword=" + encodedKeyword + "&limit=" + limit;
//		server_url = server_url + "?keyword=food&limit=" + limit;
		logger.debug("server_url:" + strSearchUrl);
		String data = getData("상품검색", strSearchUrl, "", strParamJson, strAccessKey, strSecretKey);
		contentSb.append(data);
		contentSb.append(COUPANG_PARTNERS_NOTICE);

		String strShareTitle = "[쿠팡 제품 추천] " + keyword;
		String strNaverBlogCategoryName = "추천 상품";
		String strNaverBlogCategoryNo = getNaverBlogCategoryNo(strNidAut, strNidSes, strNaverBlogCategoryName);
		return NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, "", strShareTitle, strNaverBlogCategoryNo,
				contentSb, null);
	}

	public static String getData(String apiGubun, String server_url, String categoryNm, String strParamJson,
			String strAccessKey, String strSecretKey) {
		logger.debug("server_url :" + server_url);
		StringBuilder sb = new StringBuilder();
		// Generate HMAC string
		String authorization = HmacGenerator.generate(REQUEST_METHOD_GET, server_url, strSecretKey, strAccessKey);
		logger.debug("authorization:" + authorization);
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
			logger.debug("returnJson :" + returnJson);
			JSONObject jsonObject = new JSONObject(returnJson);
			logger.debug("jsonObject:" + jsonObject.toString());
			Set keySet = jsonObject.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
//				logger.debug("key:" + key);
				Object obj = jsonObject.get(key);
//				logger.debug("value:" + obj.toString());
				logger.debug(key + ":" + obj);
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
//							logger.debug("key2:" + key2);
							Object obj2 = dataObj2.get(key2);
//							logger.debug("obj2:" + obj2.toString());
							logger.debug(key2 + ":" + obj2);
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
						logger.debug("____________________________________");

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
			ex.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 쿠키에 있는 NID_AUT,NID_SES 정보를 이용하여 네이버 카테고리 정보를 가져온다.
	 */
	public static String getNaverBlogCategoryNo(String strNidAut, String strNidSes, String strCategoryName) {
		String strCategoryNo = "";
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
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

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
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			// [B@2460600f
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			logger.debug("unzipString:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
				if (!unzipString.equals("")) {
					Document doc = Jsoup.parse(unzipString);
					Elements categoryEls = doc.select("#_categoryList option");
					for (Element categoryEl : categoryEls) {
						String categoryNo = categoryEl.attr("value");
						String categoryName = categoryEl.text();
						if (strCategoryName.equals(categoryName)) {
							strCategoryNo = categoryNo;
						}
					}
				}
			}
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return strCategoryNo;
	}

}
