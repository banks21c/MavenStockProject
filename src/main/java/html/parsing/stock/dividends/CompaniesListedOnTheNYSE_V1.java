package html.parsing.stock.dividends;

import com.fasterxml.jackson.databind.SerializationFeature;

import html.parsing.stock.util.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class CompaniesListedOnTheNYSE_V1 {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";

	private static final Logger logger = LoggerFactory.getLogger(CompaniesListedOnTheNYSE_V1.class);
	final static String userHome = System.getProperty("user.home");
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new CompaniesListedOnTheNYSE_V1();
	}

	CompaniesListedOnTheNYSE_V1() throws IOException, Exception {
		printHttpMessageConverter();
		downloadTest1("nyse_download1.html");
		downloadTest2("nyse_download2.html");
		downloadTest3("nyse_download3.html");
		downloadTest4("nyse_download4.html");
	}

	/**
	 * HttpMessageConverter list
	 * org.springframework.http.converter.ByteArrayHttpMessageConverter
	 * org.springframework.http.converter.StringHttpMessageConverter
	 * org.springframework.http.converter.ResourceHttpMessageConverter
	 * org.springframework.http.converter.xml.SourceHttpMessageConverter
	 * org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter
	 * org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter
	 * org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
	 */
	private void printHttpMessageConverter() {
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		for (HttpMessageConverter httpMessageConverter : messageConverters) {
			System.out.println(httpMessageConverter);
		}
	}

	private static void downloadTest1(String fileName) {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
			int maxResultsPerPage = 100;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage, "");
			for (int pageNo = 1; pageNo <= 64; pageNo++) {
				authPayload.setPageNumber(pageNo);
				Response response1 = given()
					.body(authPayload)
					.contentType("application/json")
					.post(SERVER_URI);
				String body = response1.getBody().print();
				logger.debug("body1:" + body);

				JSONArray json = new JSONArray(body);
				for (int i = 0; i < json.length(); i++) {
					JSONObject obj = (JSONObject) json.get(i);
					logger.debug("obj:" + obj.toString());
					String symbolExchangeTicker = obj.getString("symbolExchangeTicker");
					String instrumentName = "";
					Object instrumentNameObj = obj.get("instrumentName");
					if (!JSONObject.NULL.equals(instrumentNameObj)) {
						instrumentName = (String) instrumentNameObj;
					}

					String url = obj.getString("url");
					Map nyseMap = new HashMap();
					nyseMap.put("symbolExchangeTicker", symbolExchangeTicker);
					nyseMap.put("instrumentName", instrumentName);
					nyseMap.put("url", url);
					nyseList.add(nyseMap);
				}
				logger.debug("json1:" + json.toString());
			}
			logger.debug("nyseList.size:" + nyseList.size());
			StringBuffer sb = new StringBuffer();
			sb.append("<table>\r\n");
			sb.append("<tr>\r\n");
			sb.append("	<td>No.</td>\r\n");
			sb.append("	<td>Symbol</td>\r\n");
			sb.append("	<td>Name</td>\r\n");
			sb.append("</tr>\r\n");
			for (int i = 0; i < nyseList.size(); i++) {
				Map m = nyseList.get(i);
				String symbolExchangeTicker = (String) m.get("symbolExchangeTicker");
				String instrumentName = (String) m.get("instrumentName");
				String url = (String) m.get("url");
				System.out.println((i + 1) + "." + symbolExchangeTicker + "\t" + instrumentName + "\t" + url);
				sb.append("<tr>\r\n");
				sb.append("	<td>" + (i+1) + "</td>\r\n");
				sb.append("	<td><a href='" + url + "' target='_new'>" + symbolExchangeTicker + "</a></td>\r\n");
				sb.append("	<td>" + instrumentName + "</td>\r\n");
				sb.append("</tr>\r\n");
			}
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE" + "_" + "List" + ".html";
			FileUtil.fileWrite(fileName, sb.toString());
			logger.debug("downloadTest1 finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		System.out.println("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		System.out.println("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");

	}

	private static void downloadTest2(String fileName) {
		try {

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "*/*");
//			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");

			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "135");
			headers.set("Content-Type", "application/json");

			headers.set("Cookie", "__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
			headers.set("Cookie", "__cfduid=da0701204d87a74fce513f61892b87af81589614227; _ga=GA1.2.722532489.1589669909; _gid=GA1.2.2065720609.1589669909; cookieDisclaimer=true; _gat_UA-97108014-2=1; ICE_notification=479528458.56425.0000; JSESSIONID=987427A936C111E702FB551D1554AD1D; ICE=!dzXtaxsJYSRnM/+QmW/NR4Un8KL87E1GytnwU22CjBxvVKbxvH+kH29OmVnlQRqyLfbx/IwoEvgs9g==; TS01ebd031=0100e6d49568bd6ce042623ecb73c7a7efad742aab47019a4db0951f75eaaef4e29f745683639efd8fc5c73421267546c089090cf2542bfbc8bf27fed0e56ca4bc7f37941a7f93debaa86af83b44962f93c8c0a80d");
			headers.set("Host", "www.nyse.com");
			headers.set("Origin", "https://www.nyse.com");
			headers.set("Referer", "https://www.nyse.com/listings_directory/stock");

			headers.set("Sec-Fetch-Site", "same-origin");
			headers.set("Sec-Fetch-Mode", "cors");
			headers.set("Sec-Fetch-Dest", "empty");
			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
			headers.setContentType(MediaType.APPLICATION_JSON);

			JSONObject request = new JSONObject();
			request.put("instrumentType", "EQUITY");
			request.put("pageNumber", "1");
			request.put("sortColumn", "NORMALIZED_TICKER");
			request.put("sortOrder", "ASC");
			request.put("maxResultsPerPage", "10");
			request.put("filterToken", "");

			HttpEntity<String> entity0 = new HttpEntity<String>(request.toString(), headers);

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new StringHttpMessageConverter());
//			messageConverters.add(new MappingJackson2HttpMessageConverter());
			RestTemplate restTemplate = new RestTemplate(messageConverters);

			ResponseEntity<String> loginResponse = restTemplate
				.exchange(SERVER_URI, HttpMethod.POST, entity0, String.class);
			if (loginResponse.getStatusCode() == HttpStatus.OK) {
				String body = loginResponse.getBody();
				logger.debug("body2:" + body);
				JSONArray json = new JSONArray(body);

				logger.debug("json2:" + json.toString());
			} else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				// nono... bad credentials
			}
			logger.debug("downloadTest2 finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void downloadTest3(String fileName) {
		try {
			JSONObject request = new JSONObject();
			request.put("instrumentType", "EQUITY");
			request.put("pageNumber", "1");
			request.put("sortColumn", "NORMALIZED_TICKER");
			request.put("sortOrder", "ASC");
			request.put("maxResultsPerPage", "10");
			request.put("filterToken", "");

			//List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			//messageConverters.add(new StringHttpMessageConverter());
//			messageConverters.add(new MappingJackson2HttpMessageConverter());
//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
			MappingJackson2HttpMessageConverter messageConverters = new MappingJackson2HttpMessageConverter();
			messageConverters.getObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(messageConverters);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

			ResponseEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				String body = response.getBody();
				logger.debug("body3:" + body);
				JSONArray json = new JSONArray(body);

				logger.debug("json3:" + json.toString());
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				// nono... bad credentials
			}
			logger.debug("downloadTest3 finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void downloadTest4(String fileName) {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("instrumentType", "EQUITY");
			param.put("pageNumber", "1");
			param.put("sortColumn", "NORMALIZED_TICKER");
			param.put("sortOrder", "ASC");
			param.put("maxResultsPerPage", "10");
			param.put("filterToken", "");
			JSONObject request = new JSONObject(param);

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new StringHttpMessageConverter());
//			messageConverters.add(new MappingJackson2HttpMessageConverter());
			RestTemplate restTemplate = new RestTemplate(messageConverters);
//			RestTemplate restTemplate = new RestTemplate();
//			restTemplate.getMessageConverters().add(messageConverters);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);

			ResponseEntity<String> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				//Files.write(Paths.get(fileName), response.getBody());
				String body = response.getBody();
				logger.debug("body4:" + body);
				JSONArray json = new JSONArray(body);

				logger.debug("json4:" + json.toString());
			} else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				// nono... bad credentials
			}
			logger.debug("downloadTest4 finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
