package html.parsing.stock.dividends.krinvestingcom;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.DividendRateDescCompare;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.News;
import html.parsing.stock.util.FileUtil;

public class Dowjones_KrInvestingCom extends News {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
//	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";
	// 다우존스
	String dowJones = "https://kr.investing.com/equities/StocksFilter?noconstruct=1&smlID=595&sid=&tabletype=price&index_id=169";

	private static final Logger logger = LoggerFactory.getLogger(Dowjones_KrInvestingCom.class);
	final static String userHome = System.getProperty("user.home");
	static String strCurrentDate = new SimpleDateFormat("yyyy.MM.dd_E_HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {
		new Dowjones_KrInvestingCom();
	}

	Dowjones_KrInvestingCom() {
		// 다우존스
		List<StockVO> dowJonesList = getUsStockList(dowJones, "dowJones");
		logger.debug("dowJonesList.size:" + dowJonesList.size());
		Collections.sort(dowJonesList, new DividendRateDescCompare());
		writeUsStockList(dowJonesList, "dowJones");
	}

	public String getTitle(String gubun) {
		String title = "";
		if (gubun.equals("sAndP500")) {
			title = "S&P 500 배당률";
		} else if (gubun.equals("nasdaq100")) {
			title = "나스닥 100 배당률";

		} else if (gubun.equals("nasdaqTotal")) {
			title = "나스닥 종합지수 배당률";

		} else if (gubun.equals("dowJones")) {
			title = "다우존스 배당률";
		}
		return title;
	}

	public List<StockVO> getUsStockList(String url, String gubun) {
		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			Document doc;
			// url =
			// "https://kr.investing.com/equities/StocksFilter?noconstruct=1&smlID=595&sid=&tabletype=price&index_id=169";
			doc = Jsoup.connect(url).get();
			doc.select("script").remove();
			getURL(url);
			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Element tableEl = doc.select("#cross_rate_markets_stocks_1").get(0);
			Elements trEls = tableEl.select("tbody tr");

			int i = 0;
			for (Element trEl : trEls) {
				logger.debug((i + 1) + ".trEl:" + trEl);
				StockVO svo = new StockVO();
				String curPrice = "";
				String stockName = "";
				String krInvUrl = "";
				String eps = "";
				String dividends = "";
				String dividendRate = "";

				int j = 0;
				Elements tdEls = trEl.select("td");
				for (Element tdEl : tdEls) {
					logger.debug((j + 1) + ".tdEl:" + tdEl);
					if (j == 1) {
						Element a = tdEl.select("a").get(0);
						stockName = a.attr("title");
						stockName = a.text();
						logger.debug((i + 1) + ".stockName:" + stockName);
						svo.setStockName(stockName);
						krInvUrl = a.attr("href");
						svo.setUrl(krInvUrl);
						logger.debug("krInvUrl:" + krInvUrl);

						try {
							int relaxTime = (int) (Math.random() * 100) % 10 * 1000;
							logger.debug((relaxTime / 1000) + "초간 쉽니다.");
							Thread.sleep(relaxTime);
							logger.debug((relaxTime / 1000) + "초뒤 재가동합니다.");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Document krInvUrlDoc = Jsoup.connect(krInvUrl).get();
						Elements divEls = krInvUrlDoc.select(".overviewDataTable div");
						eps = divEls.get(5).select(".float_lang_base_2").text();
						logger.debug("eps:" + eps);
						svo.setEps(eps);
						String tempDividends = divEls.get(8).select(".float_lang_base_2").text();
						logger.debug("tempDividends:" + tempDividends);
						String tempDividendsArray[] = tempDividends.split(" ");
						if (tempDividendsArray.length == 2) {
							dividends = tempDividendsArray[0];
							logger.debug("dividends:" + dividends);

							svo.setDividends(dividends);

							dividendRate = StringUtils.defaultIfEmpty(tempDividendsArray[1], "0");
							dividendRate = dividendRate.replace("(", "");
							dividendRate = dividendRate.replace(")", "");
							dividendRate = dividendRate.replace("%", "");
							svo.setDividendRate(dividendRate);
							if (!dividendRate.equals("N/A")) {
								float fDividendRate = Float.parseFloat(dividendRate);
								logger.debug("fDividendRate:" + fDividendRate);
								svo.setfDividendRate(fDividendRate);
							}
						}

					}
					if (j == 2) {
						curPrice = tdEl.text();
						logger.debug("curPrice:" + curPrice);
						svo.setCurPrice(curPrice);
					}
					j++;
				}
				stockList.add(svo);
				i++;
			}
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
		return stockList;
	}

	public void writeUsStockList(List<StockVO> stockList, String gubun) {
		StringBuilder sb = new StringBuilder();
		Document doc;

		sb.append("<h1>").append(getTitle(gubun)).append("</h1>\r\n");
		sb.append("<table>");
		sb.append("	<tr>");
		sb.append("		<th>종목</th>");
		sb.append("		<th>현재가</th>");
		sb.append("		<th>주당순이익</th>");
		sb.append("		<th>배당금</th>");
		sb.append("		<th>배당률(%)</th>");
		sb.append("	</tr>");
		for (StockVO svo : stockList) {
			sb.append("	<tr>");
			String stockName = svo.getStockName();
			String url = svo.getUrl();
			String curPrice = svo.getCurPrice();
			String eps = svo.getEps();
			String dividends = svo.getDividends();
			String dividendRate = svo.getDividendRate();
			sb.append("		<td><a href='" + url + "' target='_new'>" + stockName + "</a></td>");
			sb.append("		<td>" + curPrice + "</td>");
			sb.append("		<td>" + eps + "</td>");
			sb.append("		<td>" + dividends + "</td>");
			sb.append("		<td>" + dividendRate + "</td>");
			sb.append("	</tr>");
		}
		sb.append("</table>");

//			String tableHtml = tableElmt.outerHtml();
//			logger.debug(tableHtml);
		doc = Jsoup.parse(sb.toString());
		fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_" + gubun
				+ "_List_.html";
		logger.debug("fileName :" + fileName);
		FileUtil.fileWrite(fileName, doc.html());
		logger.debug("file write finished");
	}
}
