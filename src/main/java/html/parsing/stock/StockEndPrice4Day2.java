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

public class StockEndPrice4Day2 {

	private static Logger logger = LoggerFactory.getLogger(StockEndPrice4Day2.class);
	private static boolean findDate = false;

	@Test
	public static String getChosenDayPrice(String targetDay, String listedDay, String stockCode, String stockName) {
		String chosenDay = "";
		int iTargetDay = Integer.parseInt(targetDay.replaceAll("\\.", ""));
		int iListedDay = Integer.parseInt(listedDay.replaceAll("\\.", ""));
		if (iListedDay < iTargetDay) {
			// 상장일이 찾으려는 날짜보다 과거이면...
			// 찾으려는 날짜가 상장일 이후이면...
			chosenDay = targetDay;
		} else {
			chosenDay = listedDay;
		}
		return findChosenDayPrice(stockCode, stockName, chosenDay);
	}

	public static String findChosenDayPrice(String stockCode, String stockName, String chosenDay) {
		System.out.println("chosenDay:" + chosenDay);
		System.out.println("findDate:" + findDate);

		String chosenDayPrice = "";
		// 상장일이 찾으려는 날짜보다 과거이면...
		// 찾으려는 날짜가 상장일 이후이면...
		int pageNo = 1;
		findDate = false;
		while (!findDate) {
			chosenDayPrice = findChosenDayPrice(stockCode, stockName, chosenDay, pageNo++);
		}
		logger.debug(stockCode + " " + stockName + " " + chosenDay + " 종가 :" + chosenDayPrice);
		return chosenDayPrice;
	}

	public static String findChosenDayPrice(String stockCode, String stockName, String chosenDay, int pageNo) {
		String chosenDayPrice = "0";
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
			int chosenDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("날짜")) {
					dayIndex = i;
				} else if (key.equals("종가")) {
					chosenDayIndex = i;
				}
			}
			Elements trEls = type2.select("tbody tr");
			String temp_chosenDay = "";
			String temp_chosenDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				if (tdEls.size() > 1) {
					Element dayEl = tdEls.get(dayIndex);
					Element chosenDayPriceEl = tdEls.get(chosenDayIndex);
					temp_chosenDay = dayEl.text();
					temp_chosenDayPrice = chosenDayPriceEl.text();
					if (chosenDay.equals(temp_chosenDay)) {
						logger.debug(temp_chosenDay + "\t" + temp_chosenDayPrice);
						chosenDayPrice = temp_chosenDayPrice;
						findDate = true;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (chosenDayPrice.equals(""))
			chosenDayPrice = "0";
		return chosenDayPrice;
	}

}
