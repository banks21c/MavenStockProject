package html.parsing.stock;

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
	private static int pageNo = 1;

	@Test
	public void getEndPrice() {
		//상장일이 찾으려는 날짜보다 과거이면...
		//찾으려는 날짜가 상장일 이후이면...
		while(!findDate) {
			String endPrice = getEndPrice("052260", "SK바이오랜드", "2020.01.02", pageNo++);
			logger.debug("052260" + " SK바이오랜드" + " 2020.01.02" + " 종가 :" + endPrice);
		}
	}

	public static String getEndPrice(String stockCode, String stockName, String tradeDay, int pageNo) {
		System.out.println("pageNo:"+pageNo);
		String endPrice = "";
		Document doc;
		try {
			// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
			String url = "https://finance.naver.com/item/sise_day.nhn?code=" + stockCode + "&page="+pageNo;
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
			int endDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("날짜")) {
					dayIndex = i;
				} else if (key.equals("종가")) {
					endDayIndex = i;
				}
			}
			logger.debug("dayIndex:" + dayIndex);
			logger.debug("endDayIndex:" + endDayIndex);
			Elements trEls = type2.select("tbody tr");
			String day = "";
			String endDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				if (tdEls.size() > 1) {
					Element dayEl = tdEls.get(dayIndex);
					Element endDayPriceEl = tdEls.get(endDayIndex);
					logger.debug(dayEl.text()+"\t"+ endDayPriceEl.text());
					if(tradeDay.equals(dayEl.text())) {
						findDate = true;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return endPrice.replaceAll("/", ".");
	}

	public static String getEndPrice2(String stockCode, String stockName, String tradeDay, int pageNo) {
		System.out.println("pageNo:"+pageNo);
		String endDayPrice = "";
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
			int endDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("날짜")) {
					dayIndex = i;
				} else if (key.equals("종가")) {
					endDayIndex = i;
				}
			}
			logger.debug("dayIndex:" + dayIndex);
			logger.debug("endDayIndex:" + endDayIndex);
			Elements trEls = type2.select("tbody tr");
			String temp_endDay = "";
			String temp_endDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				if (tdEls.size() > 1) {
					Element dayEl = tdEls.get(dayIndex);
					Element endDayPriceEl = tdEls.get(endDayIndex);
					temp_endDay = dayEl.text();
					temp_endDayPrice = endDayPriceEl.text();
					logger.debug(temp_endDay+"\t"+ temp_endDayPrice);
					if(tradeDay.equals(temp_endDay)) {
						endDayPrice = temp_endDayPrice;
						findDate = true;
					}
				}
			}
			if(!findDate) {
				endDayPrice = getEndPrice2(stockCode, stockName, tradeDay, ++pageNo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return endDayPrice.replaceAll("/", ".");
	}

}
