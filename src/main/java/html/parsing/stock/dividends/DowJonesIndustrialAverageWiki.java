package html.parsing.stock.dividends;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;
import html.parsing.stock.util.DataSort.DividendRateDescCompare;
import html.parsing.stock.util.FileUtil;

public class DowJonesIndustrialAverageWiki extends News implements NewsInterface {

//public static final String SERVER_URI = "https://www.nyse.com/listings_directory/stock";
	public static final String SERVER_URI = "https://www.nyse.com/api/quotes/filter";
	// 다우존스
	String dowJones = "https://ko.wikipedia.org/wiki/다우_존스_산업평균지수";
	String itoozaStockUrlPrefix = "http://us.itooza.com/stocks/summary/";

	private static final Logger logger = LoggerFactory.getLogger(DowJonesIndustrialAverageWiki.class);
	
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {

		new DowJonesIndustrialAverageWiki();
	}

	DowJonesIndustrialAverageWiki() {
		List<StockVO> dowJonesList = getUsStockList(dowJones, "dowJones");
		logger.debug("dowJonesList.size:" + dowJonesList.size());
		Collections.sort(dowJonesList, new DividendRateDescCompare());
		writeUsStockList(dowJonesList, "dowJones");
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

			Element tableEl = doc.select(".wikitable").get(0);
			Elements trEls = tableEl.select("tbody tr");
			// th line은 삭제
			trEls.remove(0);
			int i = 0;
			for (Element trEl : trEls) {
				StockVO svo = new StockVO();
				String companyNameEn = "";
				String companyNameHan = "";
				String wikiCompanyUrl = "";
				String stockExchange = "";
				String abbreviation = "";
				String industry = "";
				String eps = "";
				String dividends = "";
				String dividendRate = "";
				String curPrice = "";
				String varyRatio = "";
				String varyPrice = "";
				String stockTotalAmount = "";
				String totalNumberOfStock = "";
				String pbr = "";
				String ev = "";
				String per = "";
				String roe = "";
				String bps = "";

				int j = 0;
				Elements tdEls = trEl.select("td");
				for (Element tdEl : tdEls) {
					logger.debug("tdEl:" + tdEl);
					Elements as = tdEl.select("a");
					Element a;
					if (as.size() > 0) {
						a = tdEl.select("a").get(0);
						if (j == 0) {
							companyNameHan = a.attr("title");
							logger.debug((i + 1) + ".companyNameHan:" + companyNameHan);
							companyNameEn = a.text();
							logger.debug((i + 1) + ".companyNameEn:" + companyNameEn);
							wikiCompanyUrl = a.attr("href");
							logger.debug((i + 1) + ".wikiCompanyUrl1:" + wikiCompanyUrl);
							wikiCompanyUrl = URLDecoder.decode(wikiCompanyUrl, "UTF8");
							logger.debug((i + 1) + ".wikiCompanyUrl2:" + wikiCompanyUrl);

							svo.setCompanyNameHan(companyNameHan);
							svo.setCompanyNameEn(companyNameEn);
							svo.setWikiCompanyUrl(wikiCompanyUrl);
						}
						if (j == 1) {
							stockExchange = a.text();
							svo.setStockExchange(stockExchange);
						}
						if (j == 2) {
							abbreviation = a.text();
							svo.setStockName(abbreviation);
							svo.setStockCode(abbreviation);
						}
						if (j == 3) {
							industry = a.text();
							svo.setIndustry(industry);
						}
					} else {
						if (j == 0) {
							companyNameEn = tdEl.text();
							svo.setCompanyNameEn(companyNameEn);
						}
						if (j == 1) {
							stockExchange = tdEl.text();
							svo.setStockExchange(stockExchange);
						}
						if (j == 2) {
							abbreviation = tdEl.text();
							svo.setStockName(abbreviation);
							svo.setStockCode(abbreviation);
						}
						if (j == 3) {
							industry = tdEl.text();
							svo.setIndustry(industry);
						}
					}

					String itoozaStockUrl = itoozaStockUrlPrefix + abbreviation;
					svo.setUrl(itoozaStockUrl);

					doc = Jsoup.connect(itoozaStockUrl).get();

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
								dividends = dividends.replace("달러", "");
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
								eps = eps.replace("달러", "");
								svo.setEps(eps);
								logger.debug("EPS:" + eps);
							} else if (title.equals("주당순자산 BPS")) {
								bps = th.parent().child(1).text();
								bps = bps.replace("달러", "");
								svo.setBps(bps);
								logger.debug("BPS:" + bps);
							}
						}
					}
					j++;
				}
				stockList.add(svo);
				i++;
			}
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(ComListedOnTheUS_KrInvestingCom_V1.class.getName()).log(Level.SEVERE,
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
		sb.append("		<th>회사명</th>");
		sb.append("		<th>거래소</th>");
		sb.append("		<th>약어</th>");
		sb.append("		<th>산업</th>");
		sb.append("		<th>현재가($)</th>");
		sb.append("		<th>주당순이익($)</th>");
		sb.append("		<th>배당금($)</th>");
		sb.append("		<th>배당률(%)</th>");
		sb.append("	</tr>");
		for (StockVO svo : stockList) {
			sb.append("	<tr>");
			String companyNameEn = svo.getCompanyNameEn();
			String stockExchange = svo.getStockExchange();
			String stockName = svo.getStockName();
			String url = svo.getUrl();
			String industry = svo.getIndustry();
			String curPrice = svo.getCurPrice();
			String eps = svo.getEps();
			String dividends = svo.getDividends();
			String dividendRate = svo.getDividendRate();
			sb.append("		<td><a href='" + url + "' target='_new'>" + companyNameEn + "</a></td>");
			sb.append("		<td>" + stockExchange + "</td>");
			sb.append("		<td><a href='" + url + "' target='_new'>" + stockName + "</a></td>");
			sb.append("		<td>" + industry + "</td>");
			sb.append("		<td style='text-align:right'>" + curPrice + "</td>");
			sb.append("		<td style='text-align:right'>" + eps + "</td>");
			sb.append("		<td style='text-align:right'>" + dividends + "</td>");
			sb.append("		<td style='text-align:right'>" + dividendRate + "</td>");
			sb.append("	</tr>");
		}
		sb.append("</table>");

//			logger.debug(sb.toString());
		doc = Jsoup.parse(sb.toString());
		fileName = USER_HOME + File.separator + "documents" + File.separator + strCurrentDate + "_" + gubun
				+ "_List_.html";
		logger.debug("fileName :" + fileName);
		FileUtil.fileWrite(fileName, doc.html());
		logger.debug("file write finished");
	}

}
