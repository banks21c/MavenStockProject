package html.parsing.stock;

import html.parsing.stock.util.StockUtil;
import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockEndPrice4Day {

	private static Logger logger = LoggerFactory.getLogger(StockEndPrice4Day.class);
	private static boolean findDate = false;

	@Test
	public static String getEndPriceTest1() {
		String targetDay = "2020.01.02";

		// String stockCode1 = "204270";
		// String stockName1 = "제이앤티씨";

		String stockCode = "052260";
		String stockName = "SK바이오랜드";
		String listedDay = StockUtil.getStockListedDay(stockCode);

		return getSpecificDayPrice(targetDay, listedDay, stockCode, stockName);
	}
	
	public static String getSpecificDayPrice(String targetDay, String listedDay, String stockCode, String stockName) {
		String findDay = "";
		int iTargetDay = Integer.parseInt(targetDay.replaceAll("\\.", ""));
		int iListedDay = Integer.parseInt(listedDay.replaceAll("\\.", ""));
		if (iListedDay < iTargetDay) {
			// 상장일이 찾으려는 날짜보다 과거이면...
			// 찾으려는 날짜가 상장일 이후이면...
			findDay = targetDay;
		} else {
			findDay = listedDay;
		}
		return findSpecificDayPrice(stockCode, stockName, findDay);
	}

	public static String findSpecificDayPrice(String stockCode, String stockName, String findDay) {
		System.out.println("findDay:" + findDay);
		System.out.println("findDate:" + findDate);

		String specificDayPrice = "";
		// 상장일이 찾으려는 날짜보다 과거이면...
		// 찾으려는 날짜가 상장일 이후이면...
		int pageNo = 1;
		findDate = false;
		while (!findDate) {
			specificDayPrice = findSpecificDayPrice(stockCode, stockName, findDay, pageNo++);
		}
		logger.debug(stockCode + " " + stockName + " " + findDay + " 종가 :" + specificDayPrice);
		return specificDayPrice;
	}

	public static String findSpecificDayPrice(String stockCode, String stockName, String findDay, int pageNo) {
		String specificDayPrice = "0";
		Document doc;
		try {
			// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
			String url = "https://finance.naver.com/item/sise_day.nhn?code=" + stockCode + "&page=" + pageNo;
			logger.debug("url : " + url);
			String userAgent = "Mozilla";
			// This will get you the response.
			Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false)
					.userAgent(userAgent).execute();
			// This will get you cookies
			Map<String, String> loginCookies = res.cookies();
			// And this is the easiest way I've found to remain in session
			doc = Jsoup.connect(url).cookies(loginCookies).userAgent(userAgent).get();
			doc.select("link").remove();
			doc.select("javascript").remove();

			String strDoc = doc.html();
			strDoc = strDoc.replace("&nbsp;", " ");

			doc = Jsoup.parse(strDoc);

			Elements type2s = doc.select(".type2");
			Element type2 = doc.select(".type2").get(0);

			Elements thEls = type2.select("tbody tr th");
			int dayIndex = 0;
			int specificDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("날짜")) {
					dayIndex = i;
				} else if (key.equals("종가")) {
					specificDayIndex = i;
				}
			}
			Elements trEls = type2.select("tbody tr");
			String temp_specificDay = "";
			String temp_specificDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				if (tdEls.size() > 1) {
					Element dayEl = tdEls.get(dayIndex);
					Element specificDayPriceEl = tdEls.get(specificDayIndex);
					temp_specificDay = dayEl.text();
					temp_specificDayPrice = specificDayPriceEl.text();
					if (findDay.equals(temp_specificDay)) {
						logger.debug(temp_specificDay + "\t" + temp_specificDayPrice);
						specificDayPrice = temp_specificDayPrice;
						findDate = true;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (specificDayPrice.equals(""))
			specificDayPrice = "0";
		return specificDayPrice;
	}

	public static String getEndPrice2(String stockCode, String stockName, String tradeDay, int pageNo) {
		System.out.println("pageNo:" + pageNo);
		String specificDayPrice = "";
		Document doc;
		try {
			// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
			String url = "https://finance.naver.com/item/sise_day.nhn?code=" + stockCode + "&page=1";
			logger.debug("url : " + url);
			String userAgent = "Mozilla";
			// This will get you the response.
			Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false)
					.userAgent(userAgent).execute();
			// This will get you cookies
			Map<String, String> loginCookies = res.cookies();
			// And this is the easiest way I've found to remain in session
			doc = Jsoup.connect(url).cookies(loginCookies).userAgent(userAgent).get();
			doc.select("link").remove();
			doc.select("javascript").remove();

			logger.debug("doc:" + doc);
			String strDoc = doc.html();
			strDoc = strDoc.replace("&nbsp;", " ");

			doc = Jsoup.parse(strDoc);

			Elements type2s = doc.select(".type2");
			logger.debug("type2.size:" + type2s.size());
			Element type2 = doc.select(".type2").get(0);

			Elements thEls = type2.select("tbody tr th");
			int dayIndex = 0;
			int specificDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("날짜")) {
					dayIndex = i;
				} else if (key.equals("종가")) {
					specificDayIndex = i;
				}
			}
			logger.debug("dayIndex:" + dayIndex);
			logger.debug("specificDayIndex:" + specificDayIndex);
			Elements trEls = type2.select("tbody tr");
			String temp_specificDay = "";
			String temp_specificDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				if (tdEls.size() > 1) {
					Element dayEl = tdEls.get(dayIndex);
					Element specificDayPriceEl = tdEls.get(specificDayIndex);
					temp_specificDay = dayEl.text();
					temp_specificDayPrice = specificDayPriceEl.text();
					logger.debug(temp_specificDay + "\t" + temp_specificDayPrice);
					if (tradeDay.equals(temp_specificDay)) {
						specificDayPrice = temp_specificDayPrice;
						findDate = true;
					}
				}
			}
			if (!findDate) {
				specificDayPrice = getEndPrice2(stockCode, stockName, tradeDay, ++pageNo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return specificDayPrice;
	}

}
