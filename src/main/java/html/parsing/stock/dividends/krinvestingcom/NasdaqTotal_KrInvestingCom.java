package html.parsing.stock.dividends.krinvestingcom;

import html.parsing.stock.util.DataSort.DividendRateDescCompare;
import html.parsing.stock.dividends.NewAuthPayload;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.News;
import html.parsing.stock.util.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class NasdaqTotal_KrInvestingCom extends News {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";
	// 나스닥 종합지수
	String nasdaqTotal = "https://kr.investing.com/equities/StocksFilter?noconstruct=1&smlID=595&sid=&tabletype=price&index_id=14958";

	private static final Logger logger = LoggerFactory.getLogger(NasdaqTotal_KrInvestingCom.class);
	final static String userHome = System.getProperty("user.home");
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {
		new NasdaqTotal_KrInvestingCom();
	}

	NasdaqTotal_KrInvestingCom() {
		// 나스닥 종합지수
		List<StockVO> nasdaqTotalList = getUsStockList(nasdaqTotal, "nasdaqTotal");
		Collections.sort(nasdaqTotalList, new DividendRateDescCompare());
		writeUsStockList(nasdaqTotalList, "nasdaqTotal");
		logger.debug("nasdaqTotalList.size:" + nasdaqTotalList.size());
	}

	public String getTitle(String gubun) {
		String title = "";
		if (gubun.equals("sAndP500")) {
			title = "S&P 500";
		} else if (gubun.equals("nasdaq100")) {
			title = "나스닥 100";

		} else if (gubun.equals("nasdaqTotal")) {
			title = "나스닥 종합지수";

		} else if (gubun.equals("dowJones")) {
			title = "다우존스";
		}
		return title;
	}

	public StringBuilder getUsStockListString(String url, String gubun) {
		StringBuilder sb = new StringBuilder();
		try {
			Document doc;
			doc = Jsoup.connect(url).get();
			doc.select("script").remove();
			getURL(url);
			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			sb.append("<h1>").append(getTitle(gubun)).append("</h1>\r\n");
			Element tableEl = doc.select("#cross_rate_markets_stocks_1").get(0);
			Elements trEls = tableEl.select("tbody tr");
			logger.debug("trEls.size:" + trEls.size());
			logger.debug("trEls:" + trEls);

			sb.append("<table>");
			sb.append("<tr>");
			sb.append("<th>종목</th>");
			sb.append("<th>현재가</th>");
			sb.append("<th>주당순이익</th>");
			sb.append("<th>배당금</th>");
			sb.append("<th>배당률(%)</th>");
			sb.append("</tr>");
			for (Element trEl : trEls) {
				logger.debug("trEl:" + trEl);
//				sb.append(trEl);
				sb.append("<tr>");
				Elements tdEls = trEl.select("td");
				int j = 0;
				String curPrice = "";
				String stockName = "";
				String krInvUrl = "";
				String eps = "";
				String dividends = "";
				String dividendRate = "";
				for (Element tdEl : tdEls) {
					logger.debug("tdEl:" + tdEl);
					if (j == 1) {
						Element a = tdEl.select("a").get(0);
						stockName = a.attr("title");
						stockName = a.text();
						krInvUrl = a.attr("href");

						// 로봇으로 인식되는 것을 방지하기 위해 sleep을 준다.
						try {
							logger.debug("잠을 잡니다.");
							Thread.sleep(1000);
							logger.debug("잠을 깹니다.");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Document krInvUrlDoc = Jsoup.connect(krInvUrl).get();
						logger.debug("-----------------------");
//						logger.debug(krInvUrlDoc.select(".overviewDataTable").html());
						Elements divEls = krInvUrlDoc.select(".overviewDataTable div");
						logger.debug("5:" + divEls.get(5).html());
						logger.debug("8:" + divEls.get(8).html());
						eps = divEls.get(5).select(".float_lang_base_2").text();
						logger.debug("eps:" + eps);
						String tempDividends = divEls.get(8).select(".float_lang_base_2").text();
						String tempDividendsArray[] = tempDividends.split(" ");
						if (tempDividendsArray.length == 2) {
							dividends = tempDividendsArray[0];
							dividendRate = tempDividendsArray[1];
							dividendRate = dividendRate.replace("(", "");
							dividendRate = dividendRate.replace(")", "");
							dividendRate = dividendRate.replace("%", "");
							logger.debug("dividends:" + dividends);
							logger.debug("dividendRate:" + dividendRate);
						}
						logger.debug("-----------------------");

					}
					if (j == 2) {
						curPrice = tdEl.text();
						logger.debug("curPrice:" + curPrice);
					}
					j++;
				}
				sb.append("</tr>");
			}
			sb.append("</table>");

//			String tableHtml = tableElmt.outerHtml();
//			logger.debug(tableHtml);
			doc = Jsoup.parse(sb.toString());
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_" + gubun
					+ "_List_.html";
			logger.debug("fileName :" + fileName);
			FileUtil.fileWrite(fileName, doc.html());
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(NasdaqTotal_KrInvestingCom.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return sb;
	}

	public List<StockVO> getUsStockList(String url, String gubun) {
		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			Document doc;
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
//					logger.debug((j + 1) + ".tdEl:" + tdEl);
					if (j == 1) {
						Element a = tdEl.select("a").get(0);
						stockName = a.attr("title");
						stockName = a.text();
						logger.debug((i + 1) + ".stockName:" + stockName);
						svo.setStockName(stockName);
						krInvUrl = a.attr("href");
						logger.debug("krInvUrl:" + krInvUrl);
						svo.setUrl(krInvUrl);

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
			java.util.logging.Logger.getLogger(NasdaqTotal_KrInvestingCom.class.getName()).log(Level.SEVERE,
					null, ex);
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
		sb.append("		<th>현재가($)</th>");
		sb.append("		<th>주당순이익($)</th>");
		sb.append("		<th>배당금($)</th>");
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

	@Test
	private void downloadTest0() {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
			int maxResultsPerPage = 10;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage,
					"");
			for (int pageNo = 1; pageNo <= 1; pageNo++) {
				authPayload.setPageNumber(pageNo);
				Response response1 = given().body(authPayload).contentType("application/json").post(SERVER_URI);
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
			int thousandCount = 0;
			List<StockVO> stockList = new ArrayList<StockVO>();
			for (int i = 0; i < nyseList.size(); i++) {
				StockVO svo = new StockVO();
				Map m = nyseList.get(i);
				String symbolExchangeTicker = (String) m.get("symbolExchangeTicker");
				svo.setSymbolExchangeTicker(symbolExchangeTicker);

				String itoozaStockUrl = "http://us.itooza.com/stocks/summary/" + symbolExchangeTicker;
				logger.debug((i + 1) + ".symbolExchangeTicker :" + symbolExchangeTicker);
				logger.debug((i + 1) + ".itoozaStockUrl :" + itoozaStockUrl);
				Document doc;
				String curPrice = "";
				String varyPrice = "";
				String varyRatio = "";
				String stockTotalAmount = "";
				String totalNumberOfStock = "";
				String ev = "";
				String dividends = "";
				String dividendRate = "";
				String per = "";
				String pbr = "";
				String roe = "";
				String eps = "";
				String bps = "";

				try {
					doc = Jsoup.connect(itoozaStockUrl).get();

					Elements detail = doc.select(".globalStock #container .schChartTitle .detail");
					curPrice = doc.select(".globalStock #container .schChartTitle .detail li.num").text();
					svo.setCurPrice(curPrice);
					logger.debug("curPrice :" + curPrice);
					Elements spanEls = doc.select(".globalStock #container .schChartTitle .detail li.per span");
					Element spanEl;
					if (spanEls.size() > 0) {
						spanEl = doc.select(".globalStock #container .schChartTitle .detail li.per span").get(0);
						String incOrDec = spanEl.attr("class");
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span")
								.get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc
								.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec)
								.get(0);
						varys.select("span").remove();
						varyPrice = StringUtils.defaultString(varys.text());
						svo.setVaryPrice(varyPrice);
						logger.debug("incOrDec :" + incOrDec);
						logger.debug("varyPrice :" + varyPrice);
						logger.debug("ratio :" + varyRatio);
					}
					Elements ths = doc.select(".globalStock #container .schChartArea .chartDate .tableRowtype th");
					if (ths.size() > 0) {
						for (Element th : ths) {
							String title = th.text();
							if (title.equals("시가총액")) {
								stockTotalAmount = th.parent().child(1).text();
								svo.setStockTotalAmount(stockTotalAmount);
								logger.debug("stockTotalAmount:" + stockTotalAmount);
							} else if (title.equals("기업가치 EV")) {
								ev = th.parent().child(1).text();
								svo.setEv(ev);
								logger.debug("value:" + ev);
							} else if (title.equals("주식수")) {
								totalNumberOfStock = th.parent().child(1).text();
								svo.setTotalNumberOfStock(totalNumberOfStock);
								logger.debug("volume:" + totalNumberOfStock);
							} else if (title.equals("주당배당금")) {
								dividends = th.parent().child(1).text();
								svo.setDividends(StringUtils.defaultString(dividends));
								logger.debug("dividends:" + dividends);
							} else if (title.equals("배당수익률")) {
								dividendRate = th.parent().child(1).text();
								dividendRate = StringUtils.defaultIfEmpty(dividendRate.replace("%", ""), "0");
								svo.setDividendRate(dividendRate);
								svo.setfDividendRate(Float.parseFloat(dividendRate));
								logger.debug("dividendRate:" + dividendRate);
							} else if (title.equals("주가수익배수 PER")) {
								per = th.parent().child(1).text();
								svo.setPer(per);
								logger.debug("PER:" + per);
							} else if (title.equals("주가순자산배수 PBR")) {
								pbr = th.parent().child(1).text();
								svo.setPbr(pbr);
								logger.debug("PBR:" + pbr);
							} else if (title.equals("자기자본이익률 ROE")) {
								roe = th.parent().child(1).text();
								svo.setRoe(roe);
								logger.debug("ROE:" + roe);
							} else if (title.equals("주당순이익 EPS")) {
								eps = th.parent().child(1).text();
								svo.setEps(eps);
								logger.debug("EPS:" + eps);
							} else if (title.equals("주당순자산 BPS")) {
								bps = th.parent().child(1).text();
								svo.setBps(bps);
								logger.debug("BPS:" + bps);
							}
						}
					}
					String homePage = doc.select(".globalStock #container .summary .etc li a").text();
					String instrumentName = (String) m.get("instrumentName");
					logger.debug((i + 1) + ".instrumentName :" + instrumentName);
					instrumentName = instrumentName.replace("%", "PERCENT");
					instrumentName = instrumentName.replace("/", "DIVIDE");
					svo.setInstrumentName(instrumentName);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String url = (String) m.get("url");
				svo.setUrl(url);
//				logger.debug((i + 1) + "." + symbolExchangeTicker + "\t" + instrumentName + "\t" + url);
				stockList.add(svo);
			}

			Collections.sort(stockList, new DividendRateDescCompare());

			StringBuffer sb = getNewStringBufferWithHeader();
			int remainCount = 0;
			for (int i = 0; i < stockList.size(); i++) {
				StockVO svo = stockList.get(i);
				sb.append("<tr>\r\n");
				sb.append("	<td>" + (i + 1) + "</td>\r\n");
				sb.append("	<td><a href='" + svo.getUrl() + "' target='_new'>" + svo.getSymbolExchangeTicker()
						+ "</a></td>\r\n");
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName()
						+ "</a></td>\r\n");
				sb.append("	<td>" + svo.getCurPrice() + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryPrice()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryRatio()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividends()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividendRate(), "0") + "%</td>\r\n");
				sb.append("</tr>\r\n");
				// 1개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_"
							+ (thousandCount * maxResultsPerPage) + ".html";
					logger.debug("fileName :" + fileName);

					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
			}
			// 1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_"
					+ remainCount + ".html";
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

	@Test
	private void downloadTest1() {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
//			int maxResultsPerPage = 100;
			int maxResultsPerPage = 1000;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage,
					"");
//			for (int pageNo = 1; pageNo <= 64; pageNo++) {
			for (int pageNo = 1; pageNo <= 7; pageNo++) {
				authPayload.setPageNumber(pageNo);
				Response response1 = given().body(authPayload).contentType("application/json").post(SERVER_URI);
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
			StringBuffer sb = getNewStringBufferWithHeader();
			int thousandCount = 0;
			List<StockVO> stockList = new ArrayList<StockVO>();
			for (int i = 0; i < nyseList.size(); i++) {
				StockVO svo = new StockVO();
				Map m = nyseList.get(i);
				String symbolExchangeTicker = (String) m.get("symbolExchangeTicker");
				String instrumentName = (String) m.get("instrumentName");
				String itoozaStockUrl = "http://us.itooza.com/stocks/summary/" + symbolExchangeTicker;
				logger.debug((i + 1) + ".symbolExchangeTicker :" + symbolExchangeTicker);
				logger.debug((i + 1) + ".instrumentName :" + instrumentName);
				logger.debug((i + 1) + ".itoozaStockUrl :" + itoozaStockUrl);
				Document doc;
				String curPrice = "";
				String varyPrice = "";
				String varyRatio = "";
				String stockTotalAmount = "";
				String totalNumberOfStock = "";
				String ev = "";
				String dividends = "";
				String dividendRate = "";
				String per = "";
				String pbr = "";
				String roe = "";
				String eps = "";
				String bps = "";
				int remainCount = 0;
				try {
					doc = Jsoup.connect(itoozaStockUrl).get();

					Elements detail = doc.select(".globalStock #container .schChartTitle .detail");
					curPrice = doc.select(".globalStock #container .schChartTitle .detail li.num").text();
					svo.setCurPrice(curPrice);
					Elements spanEls = doc.select(".globalStock #container .schChartTitle .detail li.per span");
					Element spanEl;
					if (spanEls.size() > 0) {
						spanEl = doc.select(".globalStock #container .schChartTitle .detail li.per span").get(0);
						String incOrDec = spanEl.attr("class");
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span")
								.get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc
								.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec)
								.get(0);
						varys.select("span").remove();
						varyPrice = StringUtils.defaultString(varys.text());
						svo.setVaryPrice(varyPrice);
						logger.debug("incOrDec :" + incOrDec);
						logger.debug("varyPrice :" + varyPrice);
						logger.debug("ratio :" + varyRatio);
					}
					Elements ths = doc.select(".globalStock #container .schChartArea .chartDate .tableRowtype th");
					if (ths.size() > 0) {
						for (Element th : ths) {
							String title = th.text();
							if (title.equals("시가총액")) {
								stockTotalAmount = th.parent().child(1).text();
								svo.setStockTotalAmount(stockTotalAmount);
								logger.debug("stockTotalAmount:" + stockTotalAmount);
							} else if (title.equals("기업가치 EV")) {
								ev = th.parent().child(1).text();
								svo.setEv(ev);
								logger.debug("value:" + ev);
							} else if (title.equals("주식수")) {
								totalNumberOfStock = th.parent().child(1).text();
								svo.setTotalNumberOfStock(totalNumberOfStock);
								logger.debug("volume:" + totalNumberOfStock);
							} else if (title.equals("주당배당금")) {
								dividends = th.parent().child(1).text();
								dividends = dividends.replace("달러", "$");
								svo.setDividends(StringUtils.defaultString(dividends));
								logger.debug("dividends:" + dividends);
							} else if (title.equals("배당수익률")) {
								dividendRate = th.parent().child(1).text();
								dividendRate = StringUtils.defaultIfEmpty(dividendRate.replace("%", ""), "0");
								svo.setDividendRate(dividendRate);
								svo.setfDividendRate(Float.parseFloat(dividendRate));
								logger.debug("dividendRate:" + dividendRate);
							} else if (title.equals("주가수익배수 PER")) {
								per = th.parent().child(1).text();
								svo.setPer(per);
								logger.debug("PER:" + per);
							} else if (title.equals("주가순자산배수 PBR")) {
								pbr = th.parent().child(1).text();
								svo.setPbr(pbr);
								logger.debug("PBR:" + pbr);
							} else if (title.equals("자기자본이익률 ROE")) {
								roe = th.parent().child(1).text();
								svo.setRoe(roe);
								logger.debug("ROE:" + roe);
							} else if (title.equals("주당순이익 EPS")) {
								eps = th.parent().child(1).text();
								eps = eps.replace("달러", "$");
								svo.setEps(eps);
								logger.debug("EPS:" + eps);
							} else if (title.equals("주당순자산 BPS")) {
								bps = th.parent().child(1).text();
								bps = bps.replace("달러", "$");
								svo.setBps(bps);
								logger.debug("BPS:" + bps);
							}
						}
					}
					String homePage = doc.select(".globalStock #container .summary .etc li a").text();
					instrumentName = instrumentName.replace("%", "PERCENT");
					instrumentName = instrumentName.replace("/", "DIVIDE");
				} catch (Exception e) {
					e.printStackTrace();
				}

				String url = (String) m.get("url");
//				logger.debug((i + 1) + "." + symbolExchangeTicker + "\t" + instrumentName + "\t" + url);
				sb.append("<tr>\r\n");
				sb.append("	<td>" + (i + 1) + "</td>\r\n");
				sb.append("	<td><a href='" + url + "' target='_new'>" + symbolExchangeTicker + "</a></td>\r\n");
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName()
						+ "</a></td>\r\n");
				sb.append("	<td>" + curPrice + "</td>\r\n");
				sb.append("	<td>" + varyPrice + "</td>\r\n");
				sb.append("	<td>" + varyRatio + "</td>\r\n");
				sb.append("	<td>" + dividends + "</td>\r\n");
				sb.append("	<td>" + dividendRate + "</td>\r\n");
				sb.append("</tr>\r\n");
				// 1000개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_"
							+ (thousandCount * 1000) + ".html";
					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
				stockList.add(svo);
			}
			// 1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE" + "_"
					+ "List" + ".html";
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

	@Test
	private void downloadTest2() {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
//			int maxResultsPerPage = 100;
			int maxResultsPerPage = 1000;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage,
					"");
//			for (int pageNo = 1; pageNo <= 64; pageNo++) {
			for (int pageNo = 1; pageNo <= 7; pageNo++) {
				authPayload.setPageNumber(pageNo);
				Response response1 = given().body(authPayload).contentType("application/json").post(SERVER_URI);
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
			int thousandCount = 0;
			List<StockVO> stockList = new ArrayList<StockVO>();
			for (int i = 0; i < nyseList.size(); i++) {
				StockVO svo = new StockVO();
				Map m = nyseList.get(i);
				String symbolExchangeTicker = (String) m.get("symbolExchangeTicker");
				svo.setSymbolExchangeTicker(symbolExchangeTicker);

				String itoozaStockUrl = "http://us.itooza.com/stocks/summary/" + symbolExchangeTicker;
				logger.debug((i + 1) + ".symbolExchangeTicker :" + symbolExchangeTicker);
				logger.debug((i + 1) + ".itoozaStockUrl :" + itoozaStockUrl);
				Document doc;
				String curPrice = "";
				String varyPrice = "";
				String varyRatio = "";
				String stockTotalAmount = "";
				String totalNumberOfStock = "";
				String ev = "";
				String dividends = "";
				String dividendRate = "";
				String per = "";
				String pbr = "";
				String roe = "";
				String eps = "";
				String bps = "";

				try {
					doc = Jsoup.connect(itoozaStockUrl).get();

					Elements category = doc.select(".globalStock #container .schChartTitle .info li.category");
					String market = category.text();
					svo.setMarket(market);
					Elements detail = doc.select(".globalStock #container .schChartTitle .detail");
					curPrice = doc.select(".globalStock #container .schChartTitle .detail li.num").text();
					svo.setCurPrice(curPrice);
					Elements spanEls = doc.select(".globalStock #container .schChartTitle .detail li.per span");
					Element spanEl;
					if (spanEls.size() > 0) {
						spanEl = doc.select(".globalStock #container .schChartTitle .detail li.per span").get(0);
						String incOrDec = spanEl.attr("class");
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span")
								.get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc
								.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec)
								.get(0);
						varys.select("span").remove();
						varyPrice = StringUtils.defaultString(varys.text());
						svo.setVaryPrice(varyPrice);
						logger.debug("incOrDec :" + incOrDec);
						logger.debug("varyPrice :" + varyPrice);
						logger.debug("ratio :" + varyRatio);
					}
					Elements ths = doc.select(".globalStock #container .schChartArea .chartDate .tableRowtype th");
					if (ths.size() > 0) {
						for (Element th : ths) {
							String title = th.text();
							if (title.equals("시가총액")) {
								stockTotalAmount = th.parent().child(1).text();
								svo.setStockTotalAmount(stockTotalAmount);
								logger.debug("stockTotalAmount:" + stockTotalAmount);
							} else if (title.equals("기업가치 EV")) {
								ev = th.parent().child(1).text();
								svo.setEv(ev);
								logger.debug("value:" + ev);
							} else if (title.equals("주식수")) {
								totalNumberOfStock = th.parent().child(1).text();
								svo.setTotalNumberOfStock(totalNumberOfStock);
								logger.debug("volume:" + totalNumberOfStock);
							} else if (title.equals("주당배당금")) {
								dividends = th.parent().child(1).text();
								svo.setDividends(StringUtils.defaultString(dividends));
								logger.debug("dividends:" + dividends);
							} else if (title.equals("배당수익률")) {
								dividendRate = th.parent().child(1).text();
								dividendRate = StringUtils.defaultIfEmpty(dividendRate.replace("%", ""), "0");
								svo.setDividendRate(dividendRate);
								svo.setfDividendRate(Float.parseFloat(dividendRate));
								logger.debug("dividendRate:" + dividendRate);
							} else if (title.equals("주가수익배수 PER")) {
								per = th.parent().child(1).text();
								svo.setPer(per);
								logger.debug("PER:" + per);
							} else if (title.equals("주가순자산배수 PBR")) {
								pbr = th.parent().child(1).text();
								svo.setPbr(pbr);
								logger.debug("PBR:" + pbr);
							} else if (title.equals("자기자본이익률 ROE")) {
								roe = th.parent().child(1).text();
								svo.setRoe(roe);
								logger.debug("ROE:" + roe);
							} else if (title.equals("주당순이익 EPS")) {
								eps = th.parent().child(1).text();
								svo.setEps(eps);
								logger.debug("EPS:" + eps);
							} else if (title.equals("주당순자산 BPS")) {
								bps = th.parent().child(1).text();
								svo.setBps(bps);
								logger.debug("BPS:" + bps);
							}
						}
					}
					String homePage = doc.select(".globalStock #container .summary .etc li a").text();
					String instrumentName = (String) m.get("instrumentName");
					logger.debug((i + 1) + ".instrumentName :" + instrumentName);
					instrumentName = instrumentName.replace("%", "PERCENT");
					instrumentName = instrumentName.replace("/", "DIVIDE");
					svo.setInstrumentName(instrumentName);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String url = (String) m.get("url");
				svo.setUrl(url);
//				logger.debug((i + 1) + "." + symbolExchangeTicker + "\t" + instrumentName + "\t" + url);

				stockList.add(svo);
			}

			Collections.sort(stockList, new DividendRateDescCompare());

			StringBuffer sb = getNewStringBufferWithHeader();
			int remainCount = 0;
			for (int i = 0; i < stockList.size(); i++) {
				StockVO svo = stockList.get(i);
				sb.append("<tr>\r\n");
				sb.append("	<td>" + (i + 1) + "</td>\r\n");
				sb.append("	<td><a href='" + svo.getUrl() + "' target='_new'>" + svo.getSymbolExchangeTicker()
						+ "</a></td>\r\n");
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " "
						+ svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName()
						+ "</a></td>\r\n");
				sb.append("	<td>" + svo.getCurPrice() + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryPrice()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryRatio()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividends()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividendRate(), "0") + "%</td>\r\n");
				sb.append("</tr>\r\n");
				// 1000개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_"
							+ (thousandCount * maxResultsPerPage) + ".html";
					logger.debug("fileName :" + fileName);

					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
			}
			// 1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_"
					+ remainCount + ".html";
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

	public StringBuffer getNewStringBufferWithHeader() {
		StringBuffer sb = new StringBuffer();
		sb.append("<table>\r\n");
		sb.append("<tr>\r\n");
		sb.append("	<th>No.</th>\r\n");
		sb.append("	<th>Symbol</th>\r\n");
		sb.append("	<th>Name</th>\r\n");
		sb.append("	<th>현재가</th>\r\n");
		sb.append("	<th>전일대비</th>\r\n");
		sb.append("	<th>등락률(%)</th>\r\n");
		sb.append("	<th>주당배당금</th>\r\n");
		sb.append("	<th>배당수익률</th>\r\n");
		sb.append("</tr>\r\n");
		return sb;
	}
}
