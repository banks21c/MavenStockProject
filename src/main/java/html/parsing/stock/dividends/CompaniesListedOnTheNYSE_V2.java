package html.parsing.stock.dividends;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.FileUtil;
import io.restassured.response.Response;

public class CompaniesListedOnTheNYSE_V2 {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";

	private static final Logger logger = LoggerFactory.getLogger(CompaniesListedOnTheNYSE_V2.class);
	public final static String USER_HOME = System.getProperty("user.home");
	
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new CompaniesListedOnTheNYSE_V2();
	}

	CompaniesListedOnTheNYSE_V2() throws IOException, Exception {
		downloadTest1("nyse_download1.html");
	}

	private static void downloadTest1(String fileName) {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
//			int maxResultsPerPage = 100;
			int maxResultsPerPage = 1000;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage, "");
//			for (int pageNo = 1; pageNo <= 64; pageNo++) {
			for (int pageNo = 1; pageNo <= 7; pageNo++) {
				authPayload.setPageNumber(pageNo);
				Response response1 = given()
					.body(authPayload)
					.contentType("application/json")
					.post(SERVER_URI);
				String body = response1.getBody().print();
//				logger.debug("body1:" + body);

				JSONArray json = new JSONArray(body);
				for (int i = 0; i < json.length(); i++) {
					JSONObject obj = (JSONObject) json.get(i);
//					logger.debug("obj:" + obj.toString());
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
//				logger.debug("json1:" + json.toString());
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
				if (instrumentName.contains("INC")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("INC") + "INC".length());
				} else if (instrumentName.contains("CORP")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("CORP") + "CORP".length());
				} else if (instrumentName.contains("GROUP")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("GROUP") + "GROUP".length());
				} else if (instrumentName.contains("HLDG CO")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("HLDG CO") + "HLDG CO".length());
				} else if (instrumentName.contains("HLDG IN")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("HLDG IN") + "HLDG IN".length());
				} else if (instrumentName.contains("HLDGS IN")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("HLDGS IN") + "HLDGS IN".length());
				} else if (instrumentName.contains("HLDGS")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("HLDGS") + "HLDGS".length());
				} else if (instrumentName.contains("INFRASTRUCTURE LP")) {
					instrumentName = instrumentName.substring(0, instrumentName.indexOf("INFRASTRUCTURE LP") + "INFRASTRUCTURE LP".length());
				}
				String googleStockUrl = "https://www.google.com/search?q=" + symbolExchangeTicker + " " + instrumentName + " 주가&oq=" + symbolExchangeTicker + " " + instrumentName + " 주가";
				logger.debug("symbolExchangeTicker :" + symbolExchangeTicker);
				logger.debug("instrumentName :" + instrumentName);
				logger.debug("googleStockUrl :" + googleStockUrl);
				Document doc;
				try {
					doc = Jsoup.connect(googleStockUrl).get();
					fileName = USER_HOME + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE" + "_" + symbolExchangeTicker + "_" + instrumentName + ".html";
					FileUtil.fileWrite(fileName, doc.html());
				} catch (Exception e) {
					e.printStackTrace();
				}

				String url = (String) m.get("url");
//				logger.debug((i + 1) + "." + symbolExchangeTicker + "\t" + instrumentName + "\t" + url);
				sb.append("<tr>\r\n");
				sb.append("	<td>" + (i + 1) + "</td>\r\n");
				sb.append("	<td><a href='" + url + "' target='_new'>" + symbolExchangeTicker + "</a></td>\r\n");
				sb.append("	<td>" + instrumentName + "</td>\r\n");
				sb.append("</tr>\r\n");
			}
			sb.append("</table>\r\n");
			fileName = USER_HOME + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE" + "_" + "List" + ".html";
			FileUtil.fileWrite(fileName, sb.toString());
			logger.debug("downloadTest1 finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");

	}
}
