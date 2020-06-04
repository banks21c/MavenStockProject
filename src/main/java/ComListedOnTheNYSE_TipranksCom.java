
import html.parsing.stock.DataSort.DividendRateDescCompare;
import html.parsing.stock.StockVO;
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
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ComListedOnTheNYSE_TipranksCom {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";

	private static final Logger logger = LoggerFactory.getLogger(ComListedOnTheNYSE_TipranksCom.class);
	final static String userHome = System.getProperty("user.home");
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new ComListedOnTheNYSE_TipranksCom();
	}

	ComListedOnTheNYSE_TipranksCom() throws IOException, Exception {
		downloadTest2();
	}

	@Test
	private void downloadTest0() {
		long start = System.currentTimeMillis();
		try {
			List<Map> nyseList = new ArrayList<Map>();
			int maxResultsPerPage = 10;
			NewAuthPayload authPayload = new NewAuthPayload("EQUITY", 1, "NORMALIZED_TICKER", "ASC", maxResultsPerPage, "");
			for (int pageNo = 1; pageNo <= 1; pageNo++) {
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
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span").get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec).get(0);
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
				sb.append("	<td><a href='" + svo.getUrl() + "' target='_new'>" + svo.getSymbolExchangeTicker() + "</a></td>\r\n");
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName() + "</a></td>\r\n");
				sb.append("	<td>" + svo.getCurPrice() + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryPrice()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryRatio()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividends()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividendRate(), "0") + "%</td>\r\n");
				sb.append("</tr>\r\n");
				//1개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_" + (thousandCount * maxResultsPerPage) + ".html";
					logger.debug("fileName :" + fileName);

					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
			}
			//1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_" + remainCount + ".html";
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
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span").get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec).get(0);
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
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName() + "</a></td>\r\n");
				sb.append("	<td>" + curPrice + "</td>\r\n");
				sb.append("	<td>" + varyPrice + "</td>\r\n");
				sb.append("	<td>" + varyRatio + "</td>\r\n");
				sb.append("	<td>" + dividends + "</td>\r\n");
				sb.append("	<td>" + dividendRate + "</td>\r\n");
				sb.append("</tr>\r\n");
				//1000개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_" + (thousandCount * 1000) + ".html";
					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
				stockList.add(svo);
			}
			//1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE" + "_" + "List" + ".html";
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
			int thousandCount = 0;
			List<StockVO> stockList = new ArrayList<StockVO>();
			for (int i = 0; i < nyseList.size(); i++) {
				StockVO svo = new StockVO();
				Map m = nyseList.get(i);
				String symbolExchangeTicker = (String) m.get("symbolExchangeTicker");
				svo.setSymbolExchangeTicker(symbolExchangeTicker);

				String tipranksStockUrl = "https://www.tipranks.com/stocks/"+symbolExchangeTicker.toLowerCase()+"/stock-analysis";
				logger.debug((i + 1) + ".symbolExchangeTicker :" + symbolExchangeTicker);
				logger.debug((i + 1) + ".itoozaStockUrl :" + tipranksStockUrl);
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
					doc = Jsoup.connect(tipranksStockUrl).get();

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
						varyRatio = doc.select(".globalStock #container .schChartTitle .detail li.per span > span").get(0).text();
						varyRatio = varyRatio.replace("(", "");
						varyRatio = varyRatio.replace(")", "");
						varyRatio = StringUtils.defaultString(varyRatio);
						svo.setVaryRatio(varyRatio);
						Element varys = doc.select(".globalStock #container .schChartTitle .detail li.per span." + incOrDec).get(0);
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
				sb.append("	<td><a href='" + svo.getUrl() + "' target='_new'>" + svo.getSymbolExchangeTicker() + "</a></td>\r\n");
				sb.append("	<td><a href='https://www.google.com/search?q=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가&oq=" + svo.getSymbolExchangeTicker() + " " + svo.getInstrumentName() + " 주가" + "' target='_new'>" + svo.getInstrumentName() + "</a></td>\r\n");
				sb.append("	<td>" + svo.getCurPrice() + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryPrice()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getVaryRatio()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividends()) + "</td>\r\n");
				sb.append("	<td>" + StringUtils.defaultString(svo.getDividendRate(), "0") + "%</td>\r\n");
				sb.append("</tr>\r\n");
				//1000개마다 파일로 저장한다.
				remainCount = (i + 1) % maxResultsPerPage;
				if (remainCount == 0) {
					thousandCount++;
					sb.append("</table>\r\n");
					fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_" + (thousandCount * maxResultsPerPage) + ".html";
					logger.debug("fileName :" + fileName);

					FileUtil.fileWrite(fileName, sb.toString());
					sb = getNewStringBufferWithHeader();
				}
			}
			//1000개로 몇개 쓰고 남은 것은 여기에서 파일로 저장한다.
			sb.append("</table>\r\n");
			fileName = userHome + File.separator + "documents" + File.separator + strCurrentDate + "_NYSE_List_" + remainCount + ".html";
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
