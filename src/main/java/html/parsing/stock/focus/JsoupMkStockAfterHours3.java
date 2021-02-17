package html.parsing.stock.focus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;

/**
 * 유튜브에서 네이버로 공유할때
 */
public class JsoupMkStockAfterHours3 {

	private static final Logger logger = LoggerFactory.getLogger(JsoupMkStockAfterHours3.class);

	static String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	public final static String USER_HOME = System.getProperty("user.home");

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new JsoupMkStockAfterHours3();
	}

	JsoupMkStockAfterHours3() throws IOException, Exception {
		StockVO svo = new StockVO();
		svo.setStockCode("337930");
		svo = getAfterHoursStockTradeInfoVO(svo);
	}

	public static List<StockVO> getAfterHoursStockTradeInfoList(List<StockVO> uniqueStockList) {
		List<StockVO> svoList = new ArrayList<StockVO>();
		for (StockVO svo : uniqueStockList) {
			StockVO svo2 = getAfterHoursStockTradeInfoVO(svo);
			svoList.add(svo2);
		}
		return svoList;
	}

	public static LinkedHashMap<String, List<StockVO>> getAfterHoursStockTradeInfoList(
			LinkedHashMap<String, List<StockVO>> newLowHighPriceMap) {

		LinkedHashMap<String, List<StockVO>> newLowHighPriceMap2 = new LinkedHashMap<String, List<StockVO>>();

		Set keySet = newLowHighPriceMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);
			List<StockVO> svoList = new ArrayList<StockVO>();
			for (StockVO svo : stockList) {
				StockVO svo2 = getAfterHoursStockTradeInfoVO(svo);
				svoList.add(svo2);
			}
			newLowHighPriceMap2.put(key, svoList);
		}
		return newLowHighPriceMap2;
	}

	public static StockVO getAfterHoursStockTradeInfoVO(StockVO svo) {
		String stockCode = svo.getStockCode();
		try {
			Document doc = Jsoup.connect("https://vip.mk.co.kr/newSt/price/current9.php?stCode=" + stockCode).get();
			Elements table1Els = doc.select(".table_1");
			Element table1El = null;
			if (table1Els.size() > 1) {
				table1El = doc.select(".table_1").get(1);
				Element trEl = table1El.selectFirst("tr");
				Element tdEl = trEl.select("td").get(1);
				String afterHoursTxt = tdEl.text();
				logger.debug("afterHoursTxt:" + afterHoursTxt);
				String strAfterHoursVaryPrice = tdEl.select("span").text().trim();
				logger.debug("strAfterHoursVaryPrice:" + strAfterHoursVaryPrice);
				int iAfterHoursVaryPrice = 0;
				iAfterHoursVaryPrice = Integer.parseInt(strAfterHoursVaryPrice.replace(",", "").replace("▲", "")
						.replace("▼", "").replace("↑", "").replace("↓", "").trim());
				logger.debug("iAfterHoursVaryPrice:" + iAfterHoursVaryPrice);
				tdEl.select("span").remove();
				String strAfterHoursEndPrice = tdEl.text().replace("/", "").trim();
				logger.debug("strAfterHoursEndPrice:" + strAfterHoursEndPrice);
				int iAfterHoursEndPrice = 0;
				iAfterHoursEndPrice = Integer.parseInt(strAfterHoursEndPrice.replace(",", ""));
				logger.debug("iAfterHoursEndPrice:" + iAfterHoursEndPrice);
				String plusMinus = "";
				float fVaryRate = 0f;
				int iCurPrice = 0;
				String strAfterHoursVaryRate = "0";
				DecimalFormat df = new DecimalFormat("#,##0.##%");
				if (strAfterHoursVaryPrice.startsWith("▲")) {
					plusMinus = "+";
					iCurPrice = iAfterHoursEndPrice - iAfterHoursVaryPrice;
					logger.debug("iCurPrice:" + iCurPrice);
					fVaryRate = (float) iAfterHoursVaryPrice / iCurPrice * 100;
					logger.debug("fVaryRate:" + fVaryRate);
					strAfterHoursVaryRate = df.format(fVaryRate);
				} else if (strAfterHoursVaryPrice.startsWith("▼")) {
					plusMinus = "-";
					iCurPrice = iAfterHoursEndPrice + iAfterHoursVaryPrice;
					logger.debug("iCurPrice:" + iCurPrice);
					fVaryRate = (float) iAfterHoursVaryPrice / iCurPrice * 100;
					logger.debug("fVaryRate:" + fVaryRate);
					strAfterHoursVaryRate = df.format(fVaryRate);
				} else {
					strAfterHoursVaryRate = "0%";
				}
				logger.debug("strAfterHoursVaryRate:" + strAfterHoursVaryRate);
				String strAfterHoursPrice = afterHoursTxt + " / " + strAfterHoursVaryRate;
				logger.debug("strAfterHoursPrice:" + strAfterHoursPrice);
				svo.setStrAfterHoursPrice(strAfterHoursPrice);
				svo.setStrAfterHoursEndPrice(strAfterHoursEndPrice);
				svo.setStrAfterHoursVaryPrice(strAfterHoursVaryPrice);
				svo.setStrAfterHoursVaryRate(strAfterHoursVaryRate);
			}
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return svo;
	}

}
