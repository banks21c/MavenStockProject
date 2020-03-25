package html.parsing.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.NameAscCompare;
import html.parsing.stock.DataSort.Weeks52NewHighPriceVsCurPriceDownRatioAscCompare;
import html.parsing.stock.DataSort.Weeks52NewLowPriceVsCurPriceUpRatioDescCompare;
import html.parsing.stock.util.FileUtil;

public class Weeks52NewLowHighPriceVsCurPrice extends Thread {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = null;

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
	int iHms = Integer.parseInt(strHms);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYyyyMmDd = new SimpleDateFormat("yyyy년 M월 d일
	// E",Locale.KOREAN).format(new Date());
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Weeks52NewLowHighPriceVsCurPrice().start();
//		new Weeks52NewLowHighPriceVsCurPrice(1);
	}

	Weeks52NewLowHighPriceVsCurPrice() {
	}

	@Override
	public void run() {
		execute();
	}

	Weeks52NewLowHighPriceVsCurPrice(int i) {
		Class thisClass = this.getClass();
		logger = LoggerFactory.getLogger(thisClass);

//		readOne("005930", "삼성전자", "P");
//		Collections.sort(kospiStockDataList, new NameAscCompare());
//		writeFile(kospiStockDataList, "코스피", "이름순");
//
//		readOne("134780", "화진", "D");
//		Collections.sort(kosdaqStockDataList, new NameAscCompare());
//		writeFile(kosdaqStockDataList, "코스닥", "이름순");

		readOne("145210", "세화아이엠씨", "D");
		Collections.sort(kosdaqStockDataList, new NameAscCompare());
		writeFile(kosdaqStockDataList, "코스닥", "이름순");

	}

	public void execute() {
		Class thisClass = this.getClass();
		logger = LoggerFactory.getLogger(thisClass);
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		// MakeKospiKosdaqList.makeKospiKosdaqList();

		Properties props1 = new Properties();
		Properties props2 = new Properties();
		try {
			InputStream is = getClass().getResourceAsStream("log4j.properties");
			props1.load(is);
			PropertyConfigurator.configure(props1);
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

		// 모든 주식 정보를 조회한다.
//        readFile("코스피", kospiFileName);
		// 코스피 신저가
//		newLowPriceList = StockUtil.getAllStockInfo(kospiFileName);
//      readFile("코스닥", kosdaqFileName);
		try {
			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
			logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());
		}

		/** 날짜 정보 가져오기 */
		StockVO svo4Date = kospiStockList.get(0);
		getDateInfo(svo4Date.getStockCode());

		for (int i = 0; i < kospiStockList.size(); i++) {
			StockVO svo = kospiStockList.get(i);
			logger.debug("======================================================================");
			logger.debug("코스피." + (i + 1) + "." + svo.getStockCode() + "." + svo.getStockName());
			logger.debug("======================================================================");
			getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "P");
		}
		Collections.sort(kospiStockDataList, new NameAscCompare());
		writeFile(kospiStockDataList, "코스피", "이름순");

		Collections.sort(kospiStockDataList, new Weeks52NewHighPriceVsCurPriceDownRatioAscCompare());
		writeFile(kospiStockDataList, "코스피", "하락율순");

		Collections.sort(kospiStockDataList, new Weeks52NewLowPriceVsCurPriceUpRatioDescCompare());
		writeFile(kospiStockDataList, "코스피", "상승율순");

		logger.debug("kosdaqStockList.size :" + kosdaqStockList.size());
		for (int i = 0; i < kosdaqStockList.size(); i++) {
			StockVO svo = kosdaqStockList.get(i);
			logger.debug("======================================================================");
			logger.debug("코스닥." + (i + 1) + "." + svo.getStockCode() + "." + svo.getStockName());
			logger.debug("======================================================================");
			getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "D");
		}
		Collections.sort(kosdaqStockDataList, new NameAscCompare());
		writeFile(kosdaqStockDataList, "코스닥", "이름순");

		Collections.sort(kosdaqStockDataList, new Weeks52NewHighPriceVsCurPriceDownRatioAscCompare());
		writeFile(kosdaqStockDataList, "코스닥", "하락율순");

		Collections.sort(kosdaqStockDataList, new Weeks52NewLowPriceVsCurPriceUpRatioDescCompare());
		writeFile(kosdaqStockDataList, "코스닥", "상승율순");
		/*
		 * Collections.sort(kospiNewHighPriceList, new NameAscCompare());
		 * Collections.sort(kosdaqNewHighPriceList, new NameAscCompare());
		 * Collections.sort(kospiNewLowPriceList, new NameAscCompare());
		 * Collections.sort(kosdaqNewLowPriceList, new NameAscCompare());
		 *
		 * newHighPriceList.addAll(kospiNewHighPriceList);
		 * newHighPriceList.addAll(kosdaqNewHighPriceList);
		 * newLowPriceList.addAll(kospiNewLowPriceList);
		 * newLowPriceList.addAll(kosdaqNewLowPriceList);
		 *
		 * newLowHighPriceMap.put("코스피 신고가", kospiNewHighPriceList);
		 * newLowHighPriceMap.put("코스닥 신고가", kosdaqNewHighPriceList);
		 * newLowHighPriceMap.put("코스피 신저가", kospiNewLowPriceList);
		 * newLowHighPriceMap.put("코스닥 신저가", kosdaqNewLowPriceList);
		 *
		 * writeFile(newLowHighPriceMap);
		 */
	}

	public void readOne(String stockCode, String stockName, String marketGubun) {
		int cnt = 1;
		strStockCode = stockCode;
		strStockName = stockName;

		getStockInfo(cnt, strStockCode, strStockName, marketGubun);
	}

	public void readFile(String marketGubun, String fileName) {

		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				logger.debug(cnt + "." + read);
				strStockCode = read.split("\t")[0];
				strStockName = read.split("\t")[1];
				logger.debug(strStockCode + "\t" + strStockName);

				if (strStockCode.length() != 6) {
					continue;
				}
				getStockInfo(cnt, strStockCode, strStockName, marketGubun);
				cnt++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	public void getDateInfo(String strStockCode) {
		Document doc;
		try {
			// 종합정보
			logger.debug("종합정보 http://finance.naver.com/item/main.nhn?code=" + strStockCode);
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			// logger.debug("doc:"+doc);

			Elements dates = doc.select(".date");
			if (dates != null) {
				if (dates.size() > 0) {
					Element date = dates.get(0);
					strYmdDash = date.ownText();
					strYmdDash = date.childNode(0).toString().trim();

					String strYmd4Int = strYmdDash.replaceAll("\\.", "");
					if (strYmd4Int.length() > 8) {
						strYmd4Int = strYmd4Int.substring(0, 8);
					}
					iYmd = Integer.parseInt(strYmd4Int);

					strYmdDash = strYmdDash.replaceAll("\\.", "-");
					strYmdDash = strYmdDash.replaceAll(":", "-");
					strYmdDashBracket = "[" + strYmdDash + "]";
				}
			}
			logger.debug("iYmd:[" + iYmd + "]");
			logger.debug("strYmdDash:[" + strYmdDash + "]");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			logger.debug("종합정보 http://finance.naver.com/item/main.nhn?code=" + strStockCode);
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			// logger.debug("doc:"+doc);

			// 거래량 체크, 거래량이 0이면 빠져나간다.
			// Element tradeVolumeText = doc.select(".sp_txt9").get(0);
			String tradeVolumeText = doc.select(".sp_txt9").get(0).parent().child(1).child(0).text();
			logger.debug("tradeVolumeText:" + tradeVolumeText);

			Element new_totalinfo = doc.select(".new_totalinfo").get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);
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
				logger.debug(text + " / " + text.split(" ")[0] + " " + text.split(" ")[1]);
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

			// 종목분석-기업현황
			logger.debug("종목분석-기업현황 http://companyinfo.stock.naver.com/company/c1010001.aspx?cmp_cd=" + strStockCode);
			doc = Jsoup.connect("http://companyinfo.stock.naver.com/company/c1010001.aspx?cmp_cd=" + strStockCode)
					.get();
			// 시세 및 주주현황
			Element edd = doc.getElementById("cTB11");
			Element td0 = edd.select("td").first();
			Element td1 = edd.select("td").get(1);

			String strTd0[] = td0.text().split("/");
			String strTd1[] = td1.text().split("/");

			// String curPrice = strTd0[0].substring(0, strTd0[0].indexOf("원"));
			String weeks52MaxPrice = strTd1[0].substring(0, strTd1[0].indexOf("원"));
			String weeks52MinPrice = strTd1[1].substring(0, strTd1[1].indexOf("원"));

			// stock.setCurPrice(curPrice);
			stock.setWeeks52MaxPrice(weeks52MaxPrice);
			stock.setWeeks52MinPrice(weeks52MinPrice);
			logger.debug("weeks52MaxPrice :" + stock.getWeeks52MaxPrice());
			logger.debug("weeks52MinPrice :" + stock.getWeeks52MinPrice());

			// curPrice = curPrice.replaceAll(",", "").trim();
			weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
			weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();

			stock.setiWeeks52MaxPrice(Integer.parseInt(weeks52MaxPrice));
			stock.setiWeeks52MinPrice(Integer.parseInt(weeks52MinPrice));
			logger.debug("iWeeks52MaxPrice :" + stock.getiWeeks52MaxPrice());
			logger.debug("iWeeks52MinPrice :" + stock.getiWeeks52MinPrice());

			logger.debug("curPrice:" + curPrice);

			int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
			int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);

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

			logger.debug("stock.getiHighPrice():" + stock.getiHighPrice() + " iWeeks52MaxPrice:" + iWeeks52MaxPrice
					+ ".고가 > 52주 최고가?" + (stock.getiHighPrice() > iWeeks52MaxPrice));
			logger.debug("stock.getiLowPrice():" + stock.getiLowPrice() + " iWeeks52MaxPrice:" + iWeeks52MinPrice
					+ ".저가 < 52주 최저가?" + (stock.getiLowPrice() < iWeeks52MinPrice));

			if (stock.getiHighPrice() > iWeeks52MaxPrice) {
				stock.setWeeks52NewHighPrice(true);
				if (marketGubun.equals("P")) {
					kospiNewHighPriceList.add(stock);
					logger.debug(" kospiNewHighPriceList.size :" + kospiNewHighPriceList.size());
				} else {
					kosdaqNewHighPriceList.add(stock);
					logger.debug(" kosdaqNewHighPriceList.size :" + kosdaqNewHighPriceList.size());
				}
			} else if (stock.getiLowPrice() < iWeeks52MinPrice) {
				stock.setWeeks52NewLowPrice(true);
				if (marketGubun.equals("P")) {
					kospiNewLowPriceList.add(stock);
					logger.debug(" kospiNewLowPriceList.size :" + kospiNewLowPriceList.size());
				} else {
					kosdaqNewLowPriceList.add(stock);
					logger.debug(" kosdaqNewLowPriceList.size :" + kosdaqNewLowPriceList.size());
				}
			}

//			// 상장일 구하기
//			String listedDay = StockUtil.getStockListedDay(strStockCode);
//			logger.debug("listedDay :" + listedDay);
//			stock.setListedDay(listedDay);
//
//			// 연초가 또는 올해 상장했을 경우 상장일가 구하기
//			String yearFirstTradeDay = "2020.01.02";
//			yearFirstTradeDay= StockUtil.getYearFirstTradeDay(yearFirstTradeDay, listedDay);
//			stock.setYearFirstTradeDay(yearFirstTradeDay);
//			String yearFirstTradeDayEndPrice = StockUtil.getYearFirstTradeDayEndPrice(strStockCode, strStockName, yearFirstTradeDay);
//			stock.setYearFirstTradeDayEndPrice(yearFirstTradeDayEndPrice);
//			
//			yearFirstTradeDayEndPrice = yearFirstTradeDayEndPrice.replaceAll(",", "");
//			logger.debug("yearFirstTradeDayEndPrice :" + yearFirstTradeDayEndPrice);
//			if(yearFirstTradeDayEndPrice.equals("")) yearFirstTradeDayEndPrice = "0";
//			int iYearFirstTradeDayEndPrice = Integer.parseInt(yearFirstTradeDayEndPrice);
//			logger.debug("iYearFirstTradeDayEndPrice :" + iYearFirstTradeDayEndPrice);
//
//			double upDownRatio = 0d;
//			if (iYearFirstTradeDayEndPrice != 0) {
//				if (iYearFirstTradeDayEndPrice < iCurPrice) {
//					double d1 = iCurPrice - iYearFirstTradeDayEndPrice;
//					double d2 = d1 / iYearFirstTradeDayEndPrice * 100;
//					upDownRatio = Math.round(d2 * 100) / 100.0;
//				} else if (iYearFirstTradeDayEndPrice > iCurPrice) {
//					double d1 = iYearFirstTradeDayEndPrice - iCurPrice;
//					double d2 = d1 / iYearFirstTradeDayEndPrice * 100;
//					upDownRatio = -(Math.round(d2 * 100) / 100.0);
//				}
//			}
//			logger.debug("특정일 대비 up,down 비율:" + upDownRatio + "%");
//			stock.setYearFirstTradeDayEndPriceVsCurPriceUpDownRatio(upDownRatio);

			if (marketGubun.equals("P")) {
				kospiStockDataList.add(stock);
			} else {
				kosdaqStockDataList.add(stock);
			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}

	public void writeFile(List<StockVO> kospiKosdaqStockList, String stockGubun, String orderBy) {
		String fileNameSuffix = "";
		String lowHighPriceTxt = "";
		if (orderBy.equals("상승율순")) {
			lowHighPriceTxt = "신저가";
		} else {
			lowHighPriceTxt = "신고가";
		}
		fileNameSuffix = " 52주 "+lowHighPriceTxt+" 대비 등락율(" + orderBy + ")";

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
		sb1.append("\t<h1>").append(strYmdDashBracket).append(stockGubun + fileNameSuffix).append("</h1>");

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최저가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최고가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>최저가 대비 상승율</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>최고가 대비 하락율</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;
		for (StockVO s : kospiKosdaqStockList) {
			logger.debug("s :" + s);
			if (s != null) {
				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
				sb1.append("<td>").append(cnt++).append("</td>\r\n");
				sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(s.getStockName())
						.append("</a></td>\r\n");

				String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
				String varyPrice = s.getVaryPrice();

				logger.debug("specialLetter+++>" + specialLetter);
				logger.debug("varyPrice+++>" + varyPrice);

				String fontColor = "metal";
				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
					fontColor = "red";
				} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
						|| specialLetter.startsWith("-")) {
					fontColor = "blue";
				}

				sb1.append("<td style='text-align:right;color:" + fontColor + "'>")
						.append(StringUtils.defaultIfEmpty(s.getCurPrice(), "")).append("</td>\r\n");
				sb1.append("<td style='text-align:right;color:" + fontColor + "'>").append(specialLetter).append(" ")
						.append(varyPrice).append("</td>\r\n");

				String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
				fontColor = "black";
				if (varyRatio.startsWith("+")) {
					fontColor = "red";
				} else if (varyRatio.startsWith("-")) {
					fontColor = "blue";
				}
				sb1.append("<td class='ratio' style='text-align:right;color:" + fontColor + "'>").append(varyRatio)
						.append("</td>\r\n");

				sb1.append("<td style='text-align:right'>").append(StringUtils.defaultString(s.getWeeks52MinPrice()))
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right'>").append(StringUtils.defaultString(s.getWeeks52MaxPrice()))
						.append("</td>\r\n");

				sb1.append("<td style='text-align:right'>").append(s.getWeeks52NewLowPriceVsCurPriceUpRatio() + "%")
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right'>").append(s.getWeeks52NewHighPriceVsCurPriceDownRatio() + "%")
						.append("</td>\r\n");

				sb1.append("</tr>\r\n");
			}
		}
		sb1.append("</table>\r\n");
		sb1.append("<br><br>\r\n");

		// 뉴스 첨부
		sb1.append(getNews(newLowPriceList).toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug(sb1.toString());

		// millisecond
		String SSS = new SimpleDateFormat("SSS").format(new Date());

		String fileName = userHome + "\\documents\\" + strYmdDashBracket + "_" + strHms + "." + SSS + "_" + stockGubun
				+ fileNameSuffix + ".html";
		logger.debug("fileName==>" + fileName);
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public void writeFile(Map<String, List<StockVO>> newLowHighPriceMap) {
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
		sb1.append("\t<h1>").append(strYmdDashBracket).append(" 코스피,코스닥 신고,신저가").append("</h1>");

		Set keySet = newLowHighPriceMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);

			sb1.append("\t<h2>").append(key).append("</h2>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
			if (key.contains("신저가")) {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>저가</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최저가</td>\r\n");
			} else if (key.contains("신고가")) {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>고가</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최고가</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : stockList) {
				logger.debug("s :" + s);
				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>").append(cnt++).append("</td>\r\n");
					sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(s.getStockName())
							.append("</a></td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();

					logger.debug("specialLetter+++>" + specialLetter);
					logger.debug("varyPrice+++>" + varyPrice);

					String fontColor = "metal";
					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
							|| specialLetter.startsWith("+")) {
						fontColor = "red";
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
							|| specialLetter.startsWith("-")) {
						fontColor = "blue";
					}

					sb1.append("<td style='text-align:right;color:" + fontColor + "'>")
							.append(StringUtils.defaultIfEmpty(s.getCurPrice(), "")).append("</td>\r\n");
					sb1.append("<td style='text-align:right;color:" + fontColor + "'>").append(specialLetter)
							.append(" ").append(varyPrice).append("</td>\r\n");

					String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					fontColor = "black";
					if (varyRatio.startsWith("+")) {
						fontColor = "red";
					} else if (varyRatio.startsWith("-")) {
						fontColor = "blue";
					}
					sb1.append("<td class='ratio' style='text-align:right;color:" + fontColor + "'>").append(varyRatio)
							.append("</td>\r\n");

					if (key.contains("신저가")) {
						sb1.append("<td style='text-align:right'>").append(s.getLowPrice()).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>").append(s.getWeeks52MinPrice()).append("</td>\r\n");
					} else if (key.contains("신고가")) {
						sb1.append("<td style='text-align:right'>").append(s.getHighPrice()).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>").append(s.getWeeks52MaxPrice()).append("</td>\r\n");
					}

					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br><br>\r\n");
		}

		// 뉴스 첨부
		sb1.append(getNews(newLowPriceList).toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug(sb1.toString());

		// millisecond
		String SSS = new SimpleDateFormat("SSS").format(new Date());

		String fileName = userHome + "\\documents\\" + strYmdDashBracket + "_" + strHms + "." + SSS
				+ "_코스피,코스닥 신고,신저가.html";
		logger.debug("fileName==>" + fileName);
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public StringBuilder getNews(List<StockVO> allStockList) {

		StringBuilder sb1 = new StringBuilder();

		for (StockVO vo : allStockList) {
			strStockCode = vo.getStockCode();
			strStockName = vo.getStockName();

			// 종합정보
			logger.debug("strStockCode:" + strStockCode + " strStockName:" + strStockName);
			logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

			Document doc;
			try {
				// http://finance.naver.com/item/news_news.nhn?code=110570
				doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
						.get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
				// logger.debug(doc.html());
				Elements types = doc.select(".type5");
				Element type = null;
				if (types.size() <= 0) {
					return sb1;
				}
				type = doc.select(".type5").get(0);

				Elements trs = type.getElementsByTag("tr");
				if (trs != null) {
					logger.debug("trs.size:" + trs.size());
				}

				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);

					Elements tds = tr.getElementsByTag("td");
					if (tds.size() < 3) {
						continue;
					}

					Element a1 = tr.getElementsByTag("a").first();
					Element source = tr.getElementsByClass("info").first();
					Element dayTime = tr.getElementsByClass("date").first();

					if (a1 == null) {
						continue;
					}
					logger.debug("a:" + a1);
					logger.debug("source:" + source);
					logger.debug("dayTime:" + dayTime);
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
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=").append(strStockCode)
								.append("'>").append(strStockName).append("(").append(strStockCode)
								.append(")</a></h3>");
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");

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
						doc.select("a").remove();
						doc.select("li").remove();

						Element view = doc.select(".view").get(0);

						String strView = view.toString();
						strView = strView.replaceAll(strStockName,
								"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
										+ strStockName + "</a>");

						sb1.append(strView);
						sb1.append("\n");

						logger.debug("view:" + view);
					}
				}
			} catch (IOException e) {
			}
		}
		return sb1;
	}

	public void readNews(List<StockVO> allStockList) {

		int cnt = 1;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(userHome + "\\documents\\NewsTest." + strDate + ".html");
			StringBuilder sb1 = new StringBuilder();

			for (StockVO vo : allStockList) {
				strStockCode = vo.getStockCode();
				strStockName = vo.getStockName();

				logger.debug(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

				Document doc = Jsoup
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
						.get();
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

			fw.write(sb1.toString());
			fw.close();

		} catch (IOException | NumberFormatException e) {
		}
	}

}
