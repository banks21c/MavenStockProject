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

	@Test
	public void getEndPrice() {
		String endPrice = getEndPrice("052260", "SK바이오랜드", "2020.01.02");
		logger.debug("052260" + " SK바이오랜드" + " 2020.01.02" + " 종가 :" + endPrice);
	}

	public static String getEndPrice(String stockCode, String stockName, String tradeDay) {
		String endPrice = "";
		Document doc;
		try {
			// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
			String url = "https://finance.naver.com/item/sise_day.nhn?code="+stockCode;
            String userAgent = "Mozilla";
            // This will get you the response.
            Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false).userAgent(userAgent).execute();
            // This will get you cookies
            Map<String, String> loginCookies = res.cookies();
            // And this is the easiest way I've found to remain in session
            doc = Jsoup.connect(url).cookies(loginCookies).userAgent(userAgent).get();

			logger.debug("doc:" + doc);
			String strDoc = doc.html();
			strDoc = strDoc.replace("&nbsp;", " ");

			doc = Jsoup.parse(strDoc);

			Elements type2s = doc.select(".type2");
			logger.debug("type2.size:" + type2s.size());
			Element type2 = doc.select(".type2").get(0);

			Elements thEls = type2.select("tbody tr th");
			int endDayIndex = 0;
			for (int i = 0; i < thEls.size(); i++) {
				Element thEl = thEls.get(i);
				String key = thEl.text();
				if (key.equals("종가")) {
					endDayIndex = i;
				}
			}
			logger.debug("endDayIndex:" + endDayIndex);
			Elements trEls = type2.select("tbody tr");
			String endDayPrice = "";
			for (Element tr : trEls) {
				Elements tdEls = tr.select("td");
				logger.debug("tdEls.size:" + tdEls.size());
				if (tdEls.size() > 1) {
					Element endDayPriceEl = tdEls.get(endDayIndex);
					//Element endDayPriceEl = tdEls.get(endDayIndex).text();
//					logger.debug("endDayPrice:" + endDayPrice);
					logger.debug("endDayPriceEl:" + endDayPriceEl);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return endPrice.replaceAll("/", ".");
	}

}
