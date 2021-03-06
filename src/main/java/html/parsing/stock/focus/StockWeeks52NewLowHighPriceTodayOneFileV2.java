package html.parsing.stock.focus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.NameAscCompare;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.NaverUtil;
import html.parsing.stock.util.StockUtil;

public class StockWeeks52NewLowHighPriceTodayOneFileV2 extends Thread {

	private final static String TOTAL_INFO_URL = "http://finance.naver.com/item/main.nhn?code=";
	final static String USER_HOME = System.getProperty("user.home");
	private Logger logger = LoggerFactory.getLogger(StockWeeks52NewLowHighPriceTodayOneFileV2.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
	int iHms = Integer.parseInt(strHms);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
	String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	List<StockVO> kospiStockList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

	List<StockVO> kospiStockDataList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockDataList = new ArrayList<StockVO>();

	LinkedHashMap<String, List<StockVO>> newLowHighPriceMap = new LinkedHashMap<String, List<StockVO>>();
	List<StockVO> newLowPriceList = new ArrayList<StockVO>();
	List<StockVO> newHighPriceList = new ArrayList<StockVO>();
	List<StockVO> kospiNewLowPriceList = new ArrayList<StockVO>();
	List<StockVO> kospiNewHighPriceList = new ArrayList<StockVO>();
	List<StockVO> kosdaqNewLowPriceList = new ArrayList<StockVO>();
	List<StockVO> kosdaqNewHighPriceList = new ArrayList<StockVO>();
	String strFileName;

	String strBlogId = "";
	String strNidAut = "";
	String strNidSes = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		new MStockWeeks52NewLowHighPriceOneFile().start();
//		new StockWeeks52NewLowHighPriceTodayOneFile(1);
		new StockWeeks52NewLowHighPriceTodayOneFileV2().test();
	}

	StockWeeks52NewLowHighPriceTodayOneFileV2() {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		Properties props1 = new Properties();
		Properties props2 = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("log4j.properties");
			if (is != null) {
				props1.load(is);
				PropertyConfigurator.configure(props1);
			}
			logger.debug("props1:" + props1);
			InputStream is2 = new FileInputStream("log4j.properties");
			props2.load(is2);
			logger.debug("props2:" + props2);
			PropertyConfigurator.configure(props2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// URL url = loader.getResource("log4j.properties");
		// PropertyConfigurator.configure(url);
		File log4jfile = new File("log4j.properties");
		String absolutePath = log4jfile.getAbsolutePath();
		logger.debug("absolutePath :" + absolutePath);
		PropertyConfigurator.configure(absolutePath);
	}

	public StockWeeks52NewLowHighPriceTodayOneFileV2(String strBlogId, String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	@Override
	public void run() {
		execute();
	}

	StockWeeks52NewLowHighPriceTodayOneFileV2(int i) {

		logger = LoggerFactory.getLogger(this.getClass());

//		readOne("001060", "JW중외제약", "코스피");
		readOne("279410", "한화에이스스팩4호", "코스닥");
		logger.debug("kospiStockDataList.size :" + kospiStockDataList.size());
		logger.debug("kospiNewHighPriceList.size :" + kospiNewHighPriceList.size());
		logger.debug("kospiNewLowPriceList.size :" + kospiNewLowPriceList.size());
		Collections.sort(kospiStockDataList, new NameAscCompare());
		Collections.sort(kospiNewHighPriceList, new NameAscCompare());
		Collections.sort(kospiNewLowPriceList, new NameAscCompare());

//		writeFile(kospiStockDataList, "코스피","신고가,신저가", "이름순");
//		if (kospiNewHighPriceList.size() > 0) {
//			writeFile(kospiNewHighPriceList, "코스피", "신고가", "이름순");
//		}
//		if (kospiNewLowPriceList.size() > 0) {
//			writeFile(kospiNewLowPriceList, "코스피", "신저가", "이름순");
//		}
//		if (kospiNewLowPriceList.size() > 0) {
//			Collections.sort(kospiNewLowPriceList, new NameAscCompare());
//			newLowPriceList.addAll(kospiNewLowPriceList);
//			writeFile(newLowPriceList, "코스닥,코스피", "신저가", "이름순");
//		}
//		
//		if (kospiNewHighPriceList.size() > 0) {
//			Collections.sort(kospiNewHighPriceList, new NameAscCompare());
//			newHighPriceList.addAll(kospiNewHighPriceList);
//			writeFile(newHighPriceList, "코스닥,코스피", "신고가", "이름순");
//		}
		newLowHighPriceMap.put("코스피 신저가", kospiNewLowPriceList);
		newLowHighPriceMap.put("코스피 신고가", kospiNewHighPriceList);

		StringBuilder html = createHtmlString(newLowHighPriceMap);
		writeFile(html);
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);
	}

	public void execute() {

		kospiStockList = StockUtil.readStockCodeNameList("코스피");
		if (kospiStockList != null) {
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} else {
			logger.debug("kospiStockList is null");
		}
		kosdaqStockList = StockUtil.readStockCodeNameList("코스닥");
		if (kosdaqStockList != null) {
			logger.debug("kosdaqStockList.size1 :" + kosdaqStockList.size());
		} else {
			logger.debug("kosdaqStockList is null");
		}

		/**
		 * 날짜 정보 가져오기
		 */
		StockVO svo4Date = kospiStockList.get(0);
		strYmdDashBracket = StockUtil.getDateInfo(svo4Date.getStockCode());

		for (int i = 0; i < kospiStockList.size(); i++) {
			StockVO svo = kospiStockList.get(i);
			logger.debug("======================================================================");
			logger.debug("코스피." + (i + 1) + "." + svo.getStockCode() + "." + svo.getStockName());
			logger.debug("======================================================================");
			getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "코스피");
		}
		/*
		 * Collections.sort(kospiStockDataList, new NameAscCompare());
		 * writeFile(kospiStockDataList, "코스피", "이름순");
		 * 
		 * Collections.sort(kospiStockDataList, new
		 * Weeks52NewHighPriceVsCurPriceDownRatioAscCompare());
		 * writeFile(kospiStockDataList, "코스피", "하락율순");
		 * 
		 * Collections.sort(kospiStockDataList, new
		 * Weeks52NewLowPriceVsCurPriceUpRatioDescCompare());
		 * writeFile(kospiStockDataList, "코스피", "상승율순");
		 */
		logger.debug("kosdaqStockList.size :" + kosdaqStockList.size());
		for (int i = 0; i < kosdaqStockList.size(); i++) {
			StockVO svo = kosdaqStockList.get(i);
			logger.debug("======================================================================");
			logger.debug("코스닥." + (i + 1) + "." + svo.getStockCode() + "." + svo.getStockName());
			logger.debug("======================================================================");
			getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "코스닥");
		}
		/*
		 * Collections.sort(kosdaqStockDataList, new NameAscCompare());
		 * writeFile(kosdaqStockDataList, "코스닥", "이름순");
		 * 
		 * Collections.sort(kosdaqStockDataList, new
		 * Weeks52NewHighPriceVsCurPriceDownRatioDescCompare());
		 * writeFile(kosdaqStockDataList, "코스닥", "하락율순");
		 * 
		 * Collections.sort(kosdaqStockDataList, new
		 * Weeks52NewLowPriceVsCurPriceUpRatioDescCompare());
		 * writeFile(kosdaqStockDataList, "코스닥", "상승율순");
		 */
		Collections.sort(kospiNewHighPriceList, new NameAscCompare());
		Collections.sort(kosdaqNewHighPriceList, new NameAscCompare());
		Collections.sort(kospiNewLowPriceList, new NameAscCompare());
		Collections.sort(kosdaqNewLowPriceList, new NameAscCompare());

		logger.debug("kospiNewHighPriceList.size:" + kospiNewHighPriceList.size());
		logger.debug("kosdaqNewHighPriceList.size:" + kosdaqNewHighPriceList.size());
		logger.debug("kospiNewLowPriceList.size:" + kospiNewLowPriceList.size());

		logger.debug("kosdaqNewLowPriceList.size:" + kosdaqNewLowPriceList.size());

		newHighPriceList.addAll(kospiNewHighPriceList);
		newHighPriceList.addAll(kosdaqNewHighPriceList);
		newLowPriceList.addAll(kospiNewLowPriceList);
		newLowPriceList.addAll(kosdaqNewLowPriceList);

		logger.debug("newHighPriceList.size:" + newHighPriceList.size());
		logger.debug("newLowPriceList.size:" + newLowPriceList.size());

		StringBuilder html;
		int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		if (newHighPriceList.size() > 0) {
			Collections.sort(newHighPriceList, new NameAscCompare());

			newHighPriceList = NaverStockTradingVolume.getStockTradingVolumeList(newHighPriceList);
			newHighPriceList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(newHighPriceList);
			if (iHourMinute >= 1830 || iHourMinute < 800) {
				// 기관,외인,개인매매
				newHighPriceList = NaverStockTradingVolume.getStockTradingVolumeList(newHighPriceList);
				// 시간외단일가
				newHighPriceList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(newHighPriceList);
			}
			html = createHtmlString(newHighPriceList, "코스닥,코스피", "신고가", "이름순");
			writeFile(html, "코스닥,코스피", "신고가", "이름순");
			// 네이버 블로그에 공유
			naverBlogLinkShare(html);
		}
		if (newLowPriceList.size() > 0) {
			Collections.sort(newLowPriceList, new NameAscCompare());
			newLowPriceList = NaverStockTradingVolume.getStockTradingVolumeList(newLowPriceList);
			newHighPriceList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(newLowPriceList);
			if (iHourMinute >= 1830 || iHourMinute < 800) {
				// 기관,외인,개인매매
				newLowPriceList = NaverStockTradingVolume.getStockTradingVolumeList(newLowPriceList);
				// 시간외단일가
				newLowPriceList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(newLowPriceList);
			}
			html = createHtmlString(newLowPriceList, "코스닥,코스피", "신저가", "이름순");
			writeFile(html, "코스닥,코스피", "신저가", "이름순");
			// 네이버 블로그에 공유
			naverBlogLinkShare(html);
		}

		if (kospiNewHighPriceList.size() > 0) {
			newLowHighPriceMap.put("코스피 신고가", kospiNewHighPriceList);
		}
		if (kosdaqNewHighPriceList.size() > 0) {
			newLowHighPriceMap.put("코스닥 신고가", kosdaqNewHighPriceList);
		}
		if (kospiNewLowPriceList.size() > 0) {
			newLowHighPriceMap.put("코스피 신저가", kospiNewLowPriceList);
		}
		if (kosdaqNewLowPriceList.size() > 0) {
			newLowHighPriceMap.put("코스닥 신저가", kosdaqNewLowPriceList);
		}
		if (newLowHighPriceMap.size() > 0) {
			if (iHourMinute >= 1830 || iHourMinute < 800) {
				// 기관,외인,개인매매
				newLowHighPriceMap = NaverStockTradingVolume.getStockTradingVolumeList(newLowHighPriceMap);
				// 시간외단일가
				newLowHighPriceMap = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(newLowHighPriceMap);
			}
			html = createHtmlString(newLowHighPriceMap);
			writeFile(html);
			// 네이버 블로그에 공유
			naverBlogLinkShare(html);
		}

	}

	public void readOne(String stockCode, String stockName, String marketGubun) {
		int cnt = 1;
		strStockCode = stockCode;
		strStockName = stockName;

		getStockInfo(cnt, strStockCode, strStockName, marketGubun);
	}

	public void getStockInfo(int cnt, String strStockCode, String strStockName, String marketGubun) {
		logger.debug("======================================================================");
		logger.debug(marketGubun + "." + cnt + "." + strStockCode + "." + strStockName);
		logger.debug("======================================================================");
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
		try {
			// 종합정보
			logger.debug("종합정보 TOTAL_INFO_URL" + strStockCode);
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();
			// logger.debug("doc:"+doc);

			// 거래량 체크, 거래량이 0이면 빠져나간다.
			// Element tradeVolumeText = doc.select(".sp_txt9").get(0);
			Elements tradeVolumeTextEls = doc.select(".sp_txt9");
			if (tradeVolumeTextEls.size() > 0) {
				logger.debug("tradeVolumeTextEls.size:" + tradeVolumeTextEls.size());
				Element tradeVolumeTextEl = tradeVolumeTextEls.get(0);
				Element parent = tradeVolumeTextEl.parent();
				Element child1 = parent.child(1);
				Element child0 = child1.child(0);
				String tradeVolumeText = child0.text();
//			String tradeVolumeText = doc.select(".sp_txt9").get(0).parent().child(1).child(0).text();
				logger.debug("tradeVolumeText:" + tradeVolumeText);
			}
			Elements new_totalinfoEls = doc.select(".new_totalinfo");
			if (tradeVolumeTextEls.size() <= 0) {
				logger.debug("new_totalinfo.size is 0(zero)");
				return;
			}
			Element new_totalinfo = new_totalinfoEls.get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);

			if (blind == null) {
				return;
			}

			Elements edds = blind.select("dd");

			String specialLetter = "";
			String sign = "";
			String curPrice = "";
			String varyPrice = "";
			String varyRatio = "";

			int iCurPrice = 0;
			int iVaryPrice = 0;

			for (int i = 0; i < edds.size(); i++) {
				Element dd = edds.get(i);
				String text = dd.text();

				if (text.startsWith("현재가")) {
					// logger.debug("data1:" + dd.text());
					text = text.replaceAll("플러스", "+");
					text = text.replaceAll("마이너스", "-");
					text = text.replaceAll("상승", "▲");
					text = text.replaceAll("하락", "▼");
					text = text.replaceAll("퍼센트", "%");

					String txts[] = text.split(" ");
					curPrice = txts[1];
					stock.setCurPrice(curPrice);
					stock.setiCurPrice(
							Integer.parseInt(StringUtils.defaultIfEmpty(stock.getCurPrice(), "0").replaceAll(",", "")));
					iCurPrice = stock.getiCurPrice();

					// 특수문자
					specialLetter = txts[3].replaceAll("보합", "");
					stock.setSpecialLetter(specialLetter);

					varyPrice = txts[4];
					stock.setVaryPrice(varyPrice);
					stock.setiVaryPrice(Integer
							.parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));
					iVaryPrice = stock.getiVaryPrice();

					// +- 부호
					sign = txts[5];
					stock.setSign(sign);
					// logger.debug("txts.length:" + txts.length);
					if (txts.length == 7) {
						stock.setVaryRatio(txts[5] + txts[6]);
					} else if (txts.length == 8) {
						stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
					}
					varyRatio = stock.getVaryRatio();
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
					// logger.debug("상승률:" + stock.getVaryRatio());
				}

				if (text.startsWith("전일가")) {
					stock.setBeforePrice(text.split(" ")[1]);
					stock.setiBeforePrice(Integer.parseInt(stock.getBeforePrice().replaceAll(",", "")));
				}
				if (text.startsWith("시가")) {
					stock.setStartPrice(text.split(" ")[1]);
					stock.setiStartPrice(Integer.parseInt(stock.getStartPrice().replaceAll(",", "")));
				}
				if (text.startsWith("고가")) {
					stock.setHighPrice(text.split(" ")[1]);
					stock.setiHighPrice(Integer.parseInt(stock.getHighPrice().replaceAll(",", "")));
				}
				if (text.startsWith("상한가")) {
					stock.setMaxPrice(text.split(" ")[1]);
					stock.setiMaxPrice(Integer.parseInt(stock.getMaxPrice().replaceAll(",", "")));
				}
				if (text.startsWith("저가")) {
					stock.setLowPrice(text.split(" ")[1]);
					stock.setiLowPrice(Integer.parseInt(stock.getLowPrice().replaceAll(",", "")));
				}
				if (text.startsWith("하한가")) {
					stock.setMinPrice(text.split(" ")[1]);
					stock.setiMinPrice(Integer.parseInt(stock.getMinPrice().replaceAll(",", "")));
				}
				if (text.startsWith("거래량")) {
					stock.setTradingVolume(text.split(" ")[1]);
					stock.setlTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
				}
				if (text.startsWith("거래대금") || text.startsWith("거래금액")) {
					stock.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
					stock.setlTradingAmount(Integer
							.parseInt(StringUtils.defaultIfEmpty(stock.getTradingAmount().replaceAll(",", ""), "0")));
				}
			}

			String upDown = doc.select(".no_exday").get(0).select("em span").get(0).text();
			if (upDown.equals("상한가")) {
				specialLetter = "↑";
			} else if (upDown.equals("하한가")) {
				specialLetter = "↓";
			}
			stock.setSpecialLetter(specialLetter);

			String strStockAllInfoPrefix = "https://m.stock.naver.com/api/html/item/getOverallInfo.nhn?code=";
			logger.debug("종합정보:" + strStockAllInfoPrefix + strStockCode);
			doc = Jsoup.connect(strStockAllInfoPrefix + strStockCode).get();
			Elements totalLstLiEls = doc.select(".total_list li");
			String weeks52MaxPrice = "";
			String weeks52MinPrice = "";
			for (Element totalLstLiEl : totalLstLiEls) {
				logger.debug("header:" + totalLstLiEl.select("div"));
				if (totalLstLiEl.select("div").text().equals("52주 최고")) {
					weeks52MaxPrice = totalLstLiEl.select("span").text();
				}
				if (totalLstLiEl.select("div").text().equals("52주 최저")) {
					weeks52MinPrice = totalLstLiEl.select("span").text();
				}
			}

			stock.setWeeks52MaxPrice(weeks52MaxPrice);
			stock.setWeeks52MinPrice(weeks52MinPrice);
			logger.debug("weeks52MaxPrice :" + stock.getWeeks52MaxPrice());
			logger.debug("weeks52MinPrice :" + stock.getWeeks52MinPrice());

			// curPrice = curPrice.replaceAll(",", "").trim();
			weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
			weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();

			int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
			int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);

			stock.setiWeeks52MaxPrice(iWeeks52MaxPrice);
			stock.setiWeeks52MinPrice(iWeeks52MinPrice);
			logger.debug("iWeeks52MaxPrice :" + stock.getiWeeks52MaxPrice());
			logger.debug("iWeeks52MinPrice :" + stock.getiWeeks52MinPrice());
			logger.debug("curPrice:" + curPrice);

			if (iWeeks52MaxPrice != 0 && iWeeks52MinPrice != 0) {
				double upRatio = 0d;
				double downRatio = 0d;
				if (iWeeks52MinPrice < iCurPrice) {
					double d1 = iCurPrice - iWeeks52MinPrice;
					double d2 = d1 / iWeeks52MinPrice * 100;
					upRatio = Math.round(d2 * 100) / 100.0;
				} else if (iWeeks52MinPrice > iCurPrice) {
					logger.debug("신저가라네...");
					double d1 = iWeeks52MinPrice - iCurPrice;
					double d2 = d1 / iWeeks52MinPrice * 100;
					upRatio = -(Math.round(d2 * 100) / 100.0);
				}
				if (iWeeks52MaxPrice > iCurPrice) {
					double d1 = iWeeks52MaxPrice - iCurPrice;
					double d2 = d1 / iWeeks52MaxPrice * 100;
					downRatio = -(Math.round(d2 * 100) / 100.0);
				} else if (iWeeks52MaxPrice < iCurPrice) {
					logger.debug("신고가라네...");
					double d1 = iCurPrice - iWeeks52MaxPrice;
					double d2 = d1 / iWeeks52MaxPrice * 100;
					downRatio = Math.round(d2 * 100) / 100.0;
				}
				logger.debug("52주 신저가 대비 upRatio:" + upRatio + "% 상승");
				logger.debug("52주 신고가 대비 downRatio:" + downRatio + "% 하락");

				stock.setWeeks52NewHighPriceVsCurPriceDownRatio(downRatio);
				stock.setWeeks52NewLowPriceVsCurPriceUpRatio(upRatio);
			}

			logger.debug("stock.getiHighPrice():" + stock.getiHighPrice() + " iWeeks52MaxPrice:" + iWeeks52MaxPrice
					+ ".고가 > 52주 최고가?" + (stock.getiHighPrice() > iWeeks52MaxPrice));
			logger.debug("stock.getiLowPrice():" + stock.getiLowPrice() + " iWeeks52MaxPrice:" + iWeeks52MinPrice
					+ ".저가 < 52주 최저가?" + (stock.getiLowPrice() < iWeeks52MinPrice));

			// 신고가
			if (stock.getiHighPrice() > iWeeks52MaxPrice) {
				stock.setWeeks52NewHighPrice(true);
				if (marketGubun.equals("코스피")) {
					kospiNewHighPriceList.add(stock);
					logger.debug(" kospiNewHighPriceList.size :" + kospiNewHighPriceList.size());
				} else {
					kosdaqNewHighPriceList.add(stock);
					logger.debug(" kosdaqNewHighPriceList.size :" + kosdaqNewHighPriceList.size());
				}
			}
			// 신저가
			// 최저가=0 제외
			if ((stock.getiLowPrice() != 0) && (stock.getiLowPrice() < iWeeks52MinPrice)) {
				stock.setWeeks52NewLowPrice(true);
				if (marketGubun.equals("코스피")) {
					kospiNewLowPriceList.add(stock);
					logger.debug(" kospiNewLowPriceList.size :" + kospiNewLowPriceList.size());
				} else {
					kosdaqNewLowPriceList.add(stock);
					logger.debug(" kosdaqNewLowPriceList.size :" + kosdaqNewLowPriceList.size());
				}
			}
			if (marketGubun.equals("코스피")) {
				kospiStockDataList.add(stock);
			} else {
				kosdaqStockDataList.add(stock);
			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (NumberFormatException e) {
			logger.debug(e.getMessage());
		}
	}

	public StringBuilder createHtmlString(List<StockVO> stockList, String stockMarketGubun, String lowHighGubun,
			String orderBy) {
		logger.debug("kospiKosdaqStockList.size :" + stockList.size());
		String strFileNameSuffix = "";
		if (lowHighGubun.equals("신고가")) {// 신고가
//			strFileNameSuffix = "52주 신저가 대비 상승율(" + orderBy + ")";
			strFileNameSuffix = "52주 " + lowHighGubun + "(" + orderBy + ")";
		} else if (lowHighGubun.equals("신저가")) {// 신저가
//			strFileNameSuffix = "52주 신고가 대비 하락율(" + orderBy + ")";
			strFileNameSuffix = "52주 " + lowHighGubun + "(" + orderBy + ")";
		}
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<!doctype html>\r\n");
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;font-size:12px;}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2 id='title'>").append(strYmdDashBracket).append(" ").append(stockMarketGubun).append(" ")
				.append(strFileNameSuffix).append("</h2>");

		sb1.append("<table style='border-collapse:collapse'>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최저가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최고가</td>\r\n");
		sb1.append(
				"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>최저가 대비 상승율</td>\r\n");
		sb1.append(
				"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>최고가 대비 하락율</td>\r\n");
		int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		if (iHourMinute >= 1830 || iHourMinute < 800) {
			// 2020.12.13 추가
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외국인매매</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관매매</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>개인매매</td>\r\n");
			// 2020.12.17 추가
			sb1.append(
					"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>시간외단일가</td>\r\n");
		}
		sb1.append("</tr>\r\n");

		int cnt = 1;
		for (StockVO svo : stockList) {
			// 2020.12.13 추가
			DecimalFormat formatter = new DecimalFormat("#,##0");
			int iForeignTradingVolume = svo.getiForeignTradingVolume();
			int iOrganTradingVolume = svo.getiOrganTradingVolume();
			int iIndividualTradingVolume = svo.getiIndividualTradingVolume();

			String strForeignTradingVolume = formatter.format(iForeignTradingVolume);
			String strOrganTradingVolume = formatter.format(iOrganTradingVolume);
			String strIndividualTradingVolume = formatter.format(iIndividualTradingVolume);

			if (iForeignTradingVolume > 0 && iOrganTradingVolume > 0) {
				sb1.append("<tr style='border:1px solid red;'>\r\n");
			} else {
				sb1.append("<tr>\r\n");
			}
			String url = TOTAL_INFO_URL + svo.getStockCode();
			sb1.append("<td>").append(cnt++).append("</td>\r\n");
			sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(svo.getStockName())
					.append("</a></td>\r\n");

			String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
			String varyPrice = svo.getVaryPrice();

			logger.debug("specialLetter+++>" + specialLetter);
			logger.debug("varyPrice+++>" + varyPrice);

			if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
				sb1.append("<td style='text-align:right;color:red'>")
						.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
				sb1.append("<td style='text-align:right'><font color='red'>").append(specialLetter).append(" ")
						.append(varyPrice).append("</font></td>\r\n");
			} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
					|| specialLetter.startsWith("-")) {
				sb1.append("<td style='text-align:right;color:blue'>")
						.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
				sb1.append("<td style='text-align:right'><font color='blue'>").append(specialLetter).append(" ")
						.append(varyPrice).append("</font></td>\r\n");
			} else {
				sb1.append("<td style='text-align:right;color:metal'>")
						.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
				sb1.append("<td style='text-align:right'>0</td>\r\n");
			}

			String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
			if (varyRatio.startsWith("+")) {
				sb1.append("<td style='text-align:right'><font color='red'>").append(varyRatio)
						.append("</font></td>\r\n");
			} else if (varyRatio.startsWith("-")) {
				sb1.append("<td style='text-align:right'><font color='blue'>").append(varyRatio)
						.append("</font></td>\r\n");
			} else {
				sb1.append("<td style='text-align:right'><font color='black'>").append(varyRatio)
						.append("</font></td>\r\n");
			}

			sb1.append("<td style='text-align:right'>").append(StringUtils.defaultString(svo.getWeeks52MinPrice()))
					.append("</td>\r\n");
			sb1.append("<td style='text-align:right'>").append(StringUtils.defaultString(svo.getWeeks52MaxPrice()))
					.append("</td>\r\n");

			sb1.append("<td style='text-align:right'>").append(svo.getWeeks52NewLowPriceVsCurPriceUpRatio()).append("%")
					.append("</td>\r\n");
			sb1.append("<td style='text-align:right'>").append(svo.getWeeks52NewHighPriceVsCurPriceDownRatio())
					.append("%").append("</td>\r\n");

			if (iHourMinute >= 1830 || iHourMinute < 800) {
				// 외국인 매매
				if (iForeignTradingVolume < 0) {
					sb1.append("<td style='text-align:right;color:blue'>" + strForeignTradingVolume + "</td>\r\n");
				} else if (iForeignTradingVolume > 0) {
					sb1.append("<td style='text-align:right;color:red'>" + strForeignTradingVolume + "</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>0</td>\r\n");
				}
				// 기관 매매
				if (iOrganTradingVolume < 0) {
					sb1.append("<td style='text-align:right;color:blue'>" + strOrganTradingVolume + "</td>\r\n");
				} else if (iOrganTradingVolume > 0) {
					sb1.append("<td style='text-align:right;color:red'>" + strOrganTradingVolume + "</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>0</td>\r\n");
				}
				// 개인 매매
				if (iIndividualTradingVolume < 0) {
					sb1.append("<td style='text-align:right;color:blue'>" + strIndividualTradingVolume + "</td>\r\n");
				} else if (iIndividualTradingVolume > 0) {
					sb1.append("<td style='text-align:right;color:red'>" + strIndividualTradingVolume + "</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>0</td>\r\n");
				}
				// 2020.12.17 추가
				String strAfterHoursPrice = svo.getStrAfterHoursPrice();
				String strAfterHoursSign = StringUtils.defaultString(svo.getStrAfterHoursSign());
				String strAfterHoursPriceColor = "";
				String strAfterHoursPriceBgColor = "";
				if (strAfterHoursSign.equals("+")) {
					strAfterHoursPriceColor = "red";
					strAfterHoursPriceBgColor = "yellow";
				} else if (strAfterHoursSign.equals("-")) {
					strAfterHoursPriceColor = "blue";
				}
				sb1.append("<td style='text-align:right;color:" + strAfterHoursPriceColor + ";background-color:"
						+ strAfterHoursPriceBgColor + "'>" + strAfterHoursPrice + "</td>\r\n");
			}
			sb1.append("</tr>\r\n");
		}
		sb1.append("</table>\r\n");
		sb1.append("<br><br>\r\n");

		// 뉴스 첨부
		StringBuilder newsAddedStockList = StockUtil.getNews(stockList);
		// 증권명에 증권링크 생성
		StringBuilder stockTableAdded = StockUtil.stockLinkString(newsAddedStockList, stockList);
		sb1.append(stockTableAdded.toString());

		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug(sb1.toString());
		return sb1;
	}

	public void writeFile(StringBuilder sb, String stockMarketGubun, String lowHighGubun, String orderBy) {
		String strFileNameSuffix = "";
		if (lowHighGubun.equals("신고가")) {// 신고가
//			strFileNameSuffix = "52주 신저가 대비 상승율(" + orderBy + ")";
			strFileNameSuffix = "52주 " + lowHighGubun + "(" + orderBy + ")";
		} else if (lowHighGubun.equals("신저가")) {// 신저가
//			strFileNameSuffix = "52주 신고가 대비 하락율(" + orderBy + ")";
			strFileNameSuffix = "52주 " + lowHighGubun + " (" + orderBy + ")";
		}
		strFileName = USER_HOME + "\\documents\\" + strYmdDashBracket + "_" + strHms + "_" + stockMarketGubun + "_"
				+ strFileNameSuffix + ".html";
		logger.debug("strFileName==>" + strFileName);
		FileUtil.fileWrite(strFileName, sb.toString());
	}

	public void writeFile(StringBuilder sb) {
		strFileName = USER_HOME + "\\documents\\" + strYmdDashBracket + "_" + strHms + "_코스피,코스닥_52주_신고,신저가.html";
		logger.debug("strFileName==>" + strFileName);
		FileUtil.fileWrite(strFileName, sb.toString());
	}

	public StringBuilder createHtmlString(Map<String, List<StockVO>> newLowHighPriceMap) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2 id='title'>").append(strYmdDashBracket).append(" " + "코스피,코스닥 신고,신저가").append("</h2>");

		Set keySet = newLowHighPriceMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);

			sb1.append("\t<h2>").append(key).append("</h2>");
			sb1.append("<table style='border-collapse:collapse'>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			if (key.contains("신저가")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최저가</td>\r\n");
			} else if (key.contains("신고가")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최고가</td>\r\n");
			}
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			if (key.contains("신저가")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>신저가</td>\r\n");
			} else if (key.contains("신고가")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>신고가</td>\r\n");
			}

			// 2020.12.13 추가
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외국인매매</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관매매</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>개인매매</td>\r\n");
			// 2020.12.17 추가
			sb1.append(
					"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>시간외단일가</td>\r\n");

			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO svo : stockList) {
				logger.debug("s :" + svo);
				if (svo != null) {

					// 2020.12.13 추가
					DecimalFormat formatter = new DecimalFormat("#,##0");
					int iForeignTradingVolume = svo.getiForeignTradingVolume();
					int iOrganTradingVolume = svo.getiOrganTradingVolume();
					int iIndividualTradingVolume = svo.getiIndividualTradingVolume();

					String strForeignTradingVolume = formatter.format(iForeignTradingVolume);
					String strOrganTradingVolume = formatter.format(iOrganTradingVolume);
					String strIndividualTradingVolume = formatter.format(iIndividualTradingVolume);

					if (iForeignTradingVolume > 0 && iOrganTradingVolume > 0) {
						sb1.append("<tr style='border:1px solid red;'>\r\n");
					} else {
						sb1.append("<tr>\r\n");
					}

					String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
					sb1.append("<td>").append(cnt++).append("</td>\r\n");
					sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(svo.getStockName())
							.append("</a></td>\r\n");

					if (key.contains("신저가")) {
						sb1.append("<td style='text-align:right'>").append(svo.getWeeks52MinPrice())
								.append("</td>\r\n");
					} else if (key.contains("신고가")) {
						sb1.append("<td style='text-align:right'>").append(svo.getWeeks52MaxPrice())
								.append("</td>\r\n");
					}

					String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
					String varyPrice = svo.getVaryPrice();

					logger.debug("specialLetter+++>" + specialLetter);
					logger.debug("varyPrice+++>" + varyPrice);

					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
							|| specialLetter.startsWith("+")) {
						sb1.append("<td style='text-align:right;color:red'>")
								.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='red'>").append(specialLetter).append(" ")
								.append(varyPrice).append("</font></td>\r\n");
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
							|| specialLetter.startsWith("-")) {
						sb1.append("<td style='text-align:right;color:blue'>")
								.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='blue'>").append(specialLetter).append(" ")
								.append(varyPrice).append("</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right;color:metal'>")
								.append(StringUtils.defaultIfEmpty(svo.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}

					String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>").append(varyRatio)
								.append("</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>").append(varyRatio)
								.append("</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'><font color='black'>").append(varyRatio)
								.append("</font></td>\r\n");
					}

					if (key.contains("신저가")) {
						sb1.append("<td style='text-align:right'>").append(svo.getLowPrice()).append("</td>\r\n");
					} else if (key.contains("신고가")) {
						sb1.append("<td style='text-align:right'>").append(svo.getHighPrice()).append("</td>\r\n");
					}

					// 외국인 매매
					if (iForeignTradingVolume < 0) {
						sb1.append("<td style='text-align:right;color:blue'>" + strForeignTradingVolume + "</td>\r\n");
					} else if (iForeignTradingVolume > 0) {

						if (iOrganTradingVolume > 0) {
							sb1.append("<td style='text-align:right;color:red;background-color:yellow;'>"
									+ strForeignTradingVolume + "</td>\r\n");
						} else {
							sb1.append(
									"<td style='text-align:right;color:red'>" + strForeignTradingVolume + "</td>\r\n");
						}
					} else {
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}

					// 기관 매매
					if (iOrganTradingVolume < 0) {
						sb1.append("<td style='text-align:right;color:blue'>" + strOrganTradingVolume + "</td>\r\n");
					} else if (iOrganTradingVolume > 0) {

						if (iForeignTradingVolume > 0) {
							sb1.append("<td style='text-align:right;color:red;background-color:yellow;'>"
									+ strOrganTradingVolume + "</td>\r\n");
						} else {
							sb1.append("<td style='text-align:right;color:red'>" + strOrganTradingVolume + "</td>\r\n");
						}

					} else {
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}

					// 개인매매
					if (iIndividualTradingVolume < 0) {
						sb1.append(
								"<td style='text-align:right;color:blue'>" + strIndividualTradingVolume + "</td>\r\n");
					} else if (iIndividualTradingVolume > 0) {
						sb1.append(
								"<td style='text-align:right;color:red'>" + strIndividualTradingVolume + "</td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}
					// 2020.12.17 추가
					String strAfterHoursPrice = svo.getStrAfterHoursPrice();
					String strAfterHoursSign = StringUtils.defaultString(svo.getStrAfterHoursSign());
					String strAfterHoursPriceColor = "";
					String strAfterHoursPriceBgColor = "";
					if (strAfterHoursSign.equals("+")) {
						strAfterHoursPriceColor = "red";
						strAfterHoursPriceBgColor = "yellow";
					} else if (strAfterHoursSign.equals("-")) {
						strAfterHoursPriceColor = "blue";
					}
					sb1.append("<td style='text-align:right;color:" + strAfterHoursPriceColor + ";background-color:"
							+ strAfterHoursPriceBgColor + "'>" + strAfterHoursPrice + "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br><br>\r\n");
		}

		logger.debug("keySet:" + keySet.toString());
		keySet = newLowHighPriceMap.keySet();
		logger.debug("keySet:" + keySet.toString());
		logger.debug("it:" + it.toString());
		it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);
			// 뉴스 첨부
			StringBuilder newsAddedStockList = StockUtil.getNews(stockList);
			// 증권명에 증권링크 생성
			StringBuilder stockTableAdded = StockUtil.stockLinkString(newsAddedStockList, stockList);
			sb1.append(stockTableAdded.toString());
		}

		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		return sb1;
	}

	public void readNews(List<StockVO> allStockList) {

		int cnt = 1;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			for (StockVO vo : allStockList) {
				strStockCode = vo.getStockCode();
				strStockName = vo.getStockName();

				logger.debug(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

				Document doc = Jsoup
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=").get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
				doc.select("script").remove();
				Element e1 = doc.select(".type5").get(0);

				Elements trs = e1.getElementsByTag("tr");

				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);

					Elements tds = tr.getElementsByTag("td");
					if (tds.size() < 3) {
						continue;
					}

					Element a1 = tr.getElementsByTag("a").first();
					Element source = tr.getElementsByTag("td").get(2);
					Element dayTime = tr.getElementsByTag("span").first();

					logger.debug("title:" + a1.html());
					logger.debug("href:" + a1.attr("href"));
					logger.debug("source:" + source.html());
					logger.debug("dayTime:" + dayTime.html());

					String strTitle = a1.html();
					String strLink = a1.attr("href");
					String strSource = source.html();
					String strDayTime = dayTime.html();
					String strYmd2 = strDayTime.substring(0, 10);
					int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

					// sb1.append("<h3>"+ strTitle +"</h3>\n");
					// sb1.append("<div>"+ strSource+" | "+ strDayTime
					// +"</div>\n");
					if (iYmd <= iYmd2) {
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=").append(strStockCode)
								.append("'>").append(strStockName).append("(").append(strStockCode)
								.append(")</a></h3>\n");

						doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
						Elements link_news_elements = doc.select(".link_news");
						if (link_news_elements != null) {
							link_news_elements.remove();
						}
						Elements naver_splugin = doc.select(".naver-splugin");
						logger.debug("naver_splugin:" + naver_splugin);
						if (naver_splugin != null) {
							naver_splugin.remove();
						}
						Element view = doc.select(".view").get(0);

						String strView = view.toString();
						strView = strView.replaceAll(strStockName,
								"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
										+ strStockName + "</a>");

						sb1.append(strView);
						sb1.append("<br><br>\n");

						logger.debug("view:" + view);
					}
				}
			}

			logger.debug(sb1.toString());

			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\NewsTest." + strDate + ".html");
			fw.write(sb1.toString());
			fw.close();

		} catch (IOException | NumberFormatException e) {
		}
	}

	// 네이버 블로그에 공유
	public void naverBlogLinkShare(StringBuilder html) {
		String strUrl = "";
		String strTitle = Jsoup.parse(html.toString()).select("h2#title").text();
		String strBlogCategoryNo = "164";// 신고,신저가
		StringBuilder contentSb = html;
		logger.debug("strNidAut:" + strNidAut);
		logger.debug("strNidSes:" + strNidSes);
		if (!StringUtils.defaultIfEmpty(strNidAut, "").equals("")
				&& !StringUtils.defaultIfEmpty(strNidSes, "").equals("")) {
			NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strBlogCategoryNo,
					contentSb, null);
		}
	}

	public void test() {
		String strStockAllInfoPrefix = "https://m.stock.naver.com/api/html/item/getOverallInfo.nhn?code=";
		logger.debug("종합정보:" + strStockAllInfoPrefix + strStockCode);
		Document doc;
		try {
			doc = Jsoup.connect(strStockAllInfoPrefix + strStockCode).get();
			System.out.println("doc :" + doc);
			Elements totalLstLiEls = doc.select("ul.total_list li");
			System.out.println("totalLstLiEls :" + totalLstLiEls);
			String weeks52MaxPrice = "";
			String weeks52MinPrice = "";
			for (Element totalLstLiEl : totalLstLiEls) {
				System.out.println("header:" + totalLstLiEl.select("div"));
				if (totalLstLiEl.select("div").text().equals("52주 최고")) {
					weeks52MaxPrice = totalLstLiEl.select("span").text();
				}
				if (totalLstLiEl.select("div").text().equals("52주 최저")) {
					weeks52MinPrice = totalLstLiEl.select("span").text();
				}
			}
			logger.debug("weeks52MaxPrice :" + weeks52MaxPrice);
			logger.debug("weeks52MinPrice :" + weeks52MinPrice);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
