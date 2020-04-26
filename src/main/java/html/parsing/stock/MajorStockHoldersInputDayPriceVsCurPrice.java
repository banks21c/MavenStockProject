package html.parsing.stock;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.RetainAmountDescCompare;
import html.parsing.stock.util.FileUtil;

public class MajorStockHoldersInputDayPriceVsCurPrice {

	private static final Logger logger = LoggerFactory.getLogger(MajorStockHoldersInputDayPriceVsCurPrice.class);

	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	static String baseDay = "";
	static String specificDay = "";
	static String thisYearLowestTradeDay = "2020.03.19";
	static String thisYearFirstTradeDay = "2020.01.02";
	static String thisYearPeakTradeDay = "2020.01.20";// 2020년 최고지수 2277.23
	static String lastYearFirstTradeDay = "2019.01.02";// 2019년 첫 거래일
	static String lastYearPeakTradeDay = "2019.04.15";// 2019년 최고지수 2252.05
	static String twoYearAgoFirstTradeDay = "2018.01.02";// 2018년 첫 거래일
	static String twoYearAgoPeakTradeDay = "2018.01.29";// 2018년 최고지수 2607.20

	// 특정 날짜가 몇 페이지에 있는가?
	static int pageNo = 0;

	// 주요주주 목록
	static Map<String, String> majorStockHolderNameMap = new HashMap<String, String>();
	// 자산운용사 목록
	static Map<String, String> investCompanyMap = new HashMap<String, String>();
	// 증권사 목록
	static Map<String, String> stockCoMap = new HashMap<String, String>();
	// 은행 목록
	static Map<String, String> bankMap = new HashMap<String, String>();
	// 투자회사 목록
	static Map<String, String> investmentMap = new HashMap<String, String>();

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String inputMajorStockHolders = "국민연금공단";
	static boolean inputWordIsSameAsMajorStockHolders = false;
	static Map<String, List<StockVO>> kospiKosdaqStockMap = new HashMap<String, List<StockVO>>();
	static List<StockVO> kospiStockList = new ArrayList<StockVO>();
	static List<StockVO> kosdaqStockList = new ArrayList<StockVO>();
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd][HH.mm.ss.SSS]", Locale.KOREAN);

	static DecimalFormat df = new DecimalFormat("#,##0");

	public void readAndWriteMajorStockHoldersOne() {
		inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;

//		// 대웅제약 069620
//		kospiStockList = readOne("069620", "대웅제약");
//		logger.debug("kospiStockList:" + kospiStockList);
//		writeFile(kospiStockList, "코스피");
//		// 삼성전자 005930
//		kospiStockList = readOne("005930", "삼성전자");
//		logger.debug("kospiStockList:" + kospiStockList);
//		writeFile(kospiStockList, "코스피");
//
//		// 유니슨 018000
//		kosdaqStockList = readOne("018000", "유니슨");
//		logger.debug("kosdaqStockList:" + kosdaqStockList);
//		writeFile(kosdaqStockList, "코스닥");
//
//		// 유안타제6호스팩 340360
//		kosdaqStockList = readOne("340360", "유안타제6호스팩");
//		logger.debug("kosdaqStockList:" + kosdaqStockList);
//		writeFile(kosdaqStockList, "코스닥");

//		// 케이씨씨글라스 344820
//		kospiStockList = readOne("344820 ", "케이씨씨글라스");
//		logger.debug("kospiStockList:" + kospiStockList);
//		writeFile(kospiStockList, "코스피");

		// 유안타제5호스팩 유안타제5호스팩
		kosdaqStockList = readOne("336060 ", "유안타제5호스팩");
		logger.debug("kosdaqStockList:" + kosdaqStockList);
		writeFile(kosdaqStockList, "코스닥 " + inputMajorStockHolders + " 보유종목(보유금액순)");

		kospiKosdaqStockMap.put("코스닥", kosdaqStockList);

		logger.debug("kospiKosdaqStockMap:" + kospiKosdaqStockMap);
		logger.debug("kospiKosdaqStockMap.size:" + kospiKosdaqStockMap.size());

		// 두산건설 011160
//		kospiStockList = readOne("011160", "두산건설");
//		logger.debug("kospiStockList:" + kospiStockList);
//		writeFile(kospiStockList, "코스피");

		logger.debug("investCompanyMap.size=>" + investCompanyMap.size());
		if (inputMajorStockHolders.equals("")) {
			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			int count = 1;
			while (it.hasNext()) {
				String majorStockHolder = it.next();
				logger.debug("it.next():majorStockHolder =>" + majorStockHolder);
				writeFile(count, kospiKosdaqStockMap, majorStockHolder);
				count++;
			}
		}

	}

	public void readAndWriteMajorStockHoldersTwo() throws Exception {
		inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;

		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
		logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());

		// kospiStockList = readOne("344820 ", "케이씨씨글라스");
		kospiStockList = readOne("005930", "삼성전자");
		// kosdaqStockList = readOne("052260 ", "SK바이오랜드");
		kosdaqStockList = readOne("068270", "셀트리온");

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		kospiKosdaqStockMap.put("코스피", kospiStockList);
		kospiKosdaqStockMap.put("코스닥", kosdaqStockList);

		writeFile(kospiStockList, "코스피 " + inputMajorStockHolders + " 보유종목(보유금액순)");
		writeFile(kosdaqStockList, "코스닥 " + inputMajorStockHolders + " 보유종목(보유금액순)");

		writeFile(kospiKosdaqStockMap);

	}

	public void readAndWriteMajorStockHoldersThree() throws Exception {
		inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;

//		try {
//			//kospiStockList = StockUtil.readKospiStockCodeNameListFromExcel();
//			//kosdaqStockList = StockUtil.readKosdaqStockCodeNameListFromExcel();
//			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
//			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
//			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
//			logger.debug("kosdaqStockList.size1 :" + kosdaqStockList.size());
//		} catch (Exception ex) {
//			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
//		}

		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
		logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());

		kospiStockList = getAllStockInfo(kospiStockList);
		kosdaqStockList = getAllStockInfo(kosdaqStockList);

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		kospiKosdaqStockMap.put("코스피", kospiStockList);
		kospiKosdaqStockMap.put("코스닥", kosdaqStockList);

		writeFile(kospiStockList, "코스피 " + inputMajorStockHolders + " 보유종목(보유금액순)");
		writeFile(kosdaqStockList, "코스닥 " + inputMajorStockHolders + " 보유종목(보유금액순)");

		if (inputMajorStockHolders.equals("")) {
			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			int count = 1;
			while (it.hasNext()) {
				String majorStockHolder = it.next();
				writeFile(count, kospiKosdaqStockMap, majorStockHolder);
				count++;
			}
			logger.debug("file write finished");
		}

	}

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
		inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;

		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
		logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());

		kospiStockList = getAllStockInfo(kospiStockList);
		kosdaqStockList = getAllStockInfo(kosdaqStockList);

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		kospiKosdaqStockMap.put("코스피", kospiStockList);
		kospiKosdaqStockMap.put("코스닥", kosdaqStockList);

		writeFile(kospiStockList, "코스피 " + inputMajorStockHolders + " 보유종목(보유금액순)");
		writeFile(kosdaqStockList, "코스닥 " + inputMajorStockHolders + " 보유종목(보유금액순)");

		// 자산운용사
		writeFile(kospiKosdaqStockMap, investCompanyMap, "자산운용사");
		// 증권사
		writeFile(kospiKosdaqStockMap, stockCoMap, "증권사");
		// 은행
		writeFile(kospiKosdaqStockMap, bankMap, "은행");
		// 투자회사
		writeFile(kospiKosdaqStockMap, investmentMap, "투자회사");

	}

	public static List<StockVO> readOne(String stockCode, String stockName) {
		logger.debug(stockCode + "\t" + stockName);
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockHompage(cnt, stockCode, stockName);
		if (stock != null) {
			stocks.add(stock);
		} else {
			System.out.println("stock is null");
		}
		return stocks;
	}

	static long totalAmount = 0;

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		// 대표주식(삼성전자) 페이지 번호 구하기
		pageNo = StockUtil.getSpecificDayPageNo("005930", "삼성전자", baseDay);
		logger.debug("pageNo :" + pageNo);

		int cnt = 0;
		for (StockVO svo : stockList) {
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();

			StockVO stock = getStockHompage(cnt, stockCode, stockName);
			if (stock != null) {
				stocks.add(stock);
				totalAmount += stock.getlRetainAmount();
			}
			cnt++;
		}

		return stocks;
	}

	// 종목분석-기업현황
	// http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
	// 종목분석-기업개요
	// http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
	public static StockVO getStockHompage(int cnt, String strStockCode, String strStockName) {
		logger.debug(cnt + ". code :" + strStockCode + " name :" + strStockName);

		// ===========================================================================
		// 상장일 구하기
		String listedDay = StringUtils.defaultString(StockUtil.getStockListedDay(strStockCode));
		logger.debug("listedDay :" + listedDay);
		if (listedDay.equals("")) {
			logger.debug(strStockName + "(" + strStockCode + ")" + " 상장일 정보가 없습니다. 존재하지 않는 주식입니다.(상장폐지 여부 확인 필요)");
			return null;
		}

		StockVO svo = new StockVO();
		svo.setListedDay(listedDay);

		// 연초가 또는 올해 상장했을 경우 상장일가 구하기
		specificDay = StockUtil.getSpecificDay(baseDay, listedDay);
		logger.debug("specificDay :" + specificDay);
		svo.setSpecificDay(specificDay);

//		String specificDayEndPrice = StockUtil.getSpecificDayEndPrice(strStockCode, strStockName, specificDay);
		String specificDayEndPrice = "0";
		try {
			if (baseDay.equals(specificDay)) {
				specificDayEndPrice = StockUtil.findSpecificDayEndPrice(strStockCode, strStockName, specificDay,
						pageNo);
			} else {
				specificDayEndPrice = StockUtil.getSpecificDayEndPrice(strStockCode, strStockName, specificDay);
			}
			logger.debug("specificDayEndPrice :" + specificDayEndPrice);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		svo.setSpecificDayEndPrice(specificDayEndPrice);

		specificDayEndPrice = specificDayEndPrice.replaceAll(",", "");
		logger.debug("specificDayEndPrice :" + specificDayEndPrice);
		if (specificDayEndPrice.equals(""))
			specificDayEndPrice = "0";
		int iSpecificDayEndPrice = Integer.parseInt(specificDayEndPrice);
		svo.setiSpecificDayEndPrice(iSpecificDayEndPrice);
		logger.debug("iSpecificDayEndPrice :" + iSpecificDayEndPrice);
		// ===========================================================================

		Document doc;
		try {
			// 종합정보
			// https://finance.naver.com/item/main.nhn?code=065450
			String itemMainUrl = "http://finance.naver.com/item/main.nhn?code=" + strStockCode;
			logger.debug("itemMainUrl:" + itemMainUrl);
			doc = Jsoup.connect(itemMainUrl).get();
			if (cnt == 1) {
				// logger.debug(doc.title());
				// logger.debug(doc.html());
			}
			svo.setStockCode(strStockCode);
			svo.setStockName(strStockName);

			Elements dates = doc.select(".date");
			if (dates != null) {
				if (dates.size() > 0) {
					Element date = dates.get(0);
					strYMD = date.ownText();
					strYMD = date.childNode(0).toString().trim();
					strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
				}
			}

			int iCurPrice = 0;

//			Elements blindElements = doc.select(".no_today .blind");
			Elements blindElements = doc.select(".spot .rate_info .today .no_today");
			if (blindElements.size() <= 0) {
				logger.debug("blindElements.size is less than 0");
				return null;
			}
			String curPriceWithComma = blindElements.get(0).text();
			logger.debug("curPriceWithComma:" + curPriceWithComma);
			if (curPriceWithComma.contains(" "))
				curPriceWithComma = curPriceWithComma.split(" ")[0];
			String curPriceWithoutComma = curPriceWithComma.replace(",", "");
			iCurPrice = Integer.parseInt(curPriceWithoutComma);
			svo.setCurPrice(curPriceWithComma);
			svo.setiCurPrice(iCurPrice);

			// ===========================================================================
			double upDownRatio = 0d;
			if (iSpecificDayEndPrice != 0) {
				if (iSpecificDayEndPrice < iCurPrice) {
					double d1 = iCurPrice - iSpecificDayEndPrice;
					double d2 = d1 / iSpecificDayEndPrice * 100;
					upDownRatio = Math.round(d2 * 100) / 100.0;
				} else if (iSpecificDayEndPrice > iCurPrice) {
					double d1 = iSpecificDayEndPrice - iCurPrice;
					double d2 = d1 / iSpecificDayEndPrice * 100;
					upDownRatio = -(Math.round(d2 * 100) / 100.0);
				}
			}
			logger.debug("특정일 대비 up,down 비율:" + upDownRatio + "%");
			svo.setSpecificDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
			// ===========================================================================

			Elements no_exday = doc.select(".no_exday");
			Element new_totalinfo = null;
			if (no_exday.size() > 0) {
				new_totalinfo = no_exday.get(0);
				logger.debug("new_totalinfo:" + new_totalinfo);
				Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
				logger.debug("new_totalinfo_doc:" + new_totalinfo_doc);
				Elements no_up0 = new_totalinfo_doc.select(".no_up");
				logger.debug("no_up0:" + no_up0);
				if (no_up0.size() > 0) {
					Element no_up_idx0 = no_up0.get(0);
					logger.debug("no_up_idx0:" + no_up_idx0);
					Element span0 = no_up0.select("span").get(0);
					Element span1 = no_up0.select("span").get(1);
					logger.debug("span0 :" + span0);
					logger.debug("span1 :" + span1);

					Element no_up_idx1 = no_up0.get(1);
					logger.debug("no_up_idx1:" + no_up_idx1);

					String text = span0.text();

					// logger.debug("data:" + text);
					text = text.replaceAll("플러스", "+");
					text = text.replaceAll("마이너스", "-");
					text = text.replaceAll("상승", "▲");
					text = text.replaceAll("하락", "▼");
					text = text.replaceAll("퍼센트", "%");

//					stock.setVaryRatio(strYMD);
//					stock.setSpecialLetter(text);
				}

			}

			// 종목분석-기업현황
			doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + strStockCode)
					.get();
			if (cnt == 1) {
				// logger.debug("title:" + doc.title());
				// logger.debug(doc.html());
			}

			Elements aClass = doc.select("table.gHead01 tbody");
			// Elements aClass = doc.select("#cTB13");
			Element aElem = null;
			if (aClass.size() <= 0) {
				logger.debug("aClass.size is less than 0");
				return null;
			}
			if (aClass.size() > 1) {
				aElem = aClass.get(1);
			}
			logger.debug("aElem:" + aElem);

			Elements trElem = aElem.select("tr");
			svo.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

			long lRetainAmountTotal = 0;
			long lRetainVolumeTotal = 0;
			float fRetainRatioTotal = 0;

			String majorStockHolderName = "";

			for (Element tr : trElem) {
				Elements td = tr.select("td");

				if (td.size() > 0) {
					// 주요주주
					majorStockHolderName = td.get(0).attr("title");

					logger.debug("td title majorStockHolderName:" + majorStockHolderName);
					if (majorStockHolderName.equals(""))
						continue;

					if (!inputMajorStockHolders.equals("") && !majorStockHolderName.contains(inputMajorStockHolders)) {
						continue;
					}

					if (majorStockHolderName.equals(inputMajorStockHolders)) {
						inputWordIsSameAsMajorStockHolders = true;
						logger.debug("inputWordIsSameAsMajorStockHolders:" + inputWordIsSameAsMajorStockHolders);
					}

					if (!inputMajorStockHolders.equals("")) {
						// 입력한 주요주주가 있을 경우
						if (majorStockHolderName.contains(inputMajorStockHolders)) {
							if (majorStockHolderName.contains("자산운용")) {
								investCompanyMap.put(majorStockHolderName, majorStockHolderName);
							} else if (majorStockHolderName.contains("증권")) {
								stockCoMap.put(majorStockHolderName, majorStockHolderName);
							} else if (majorStockHolderName.contains("은행")) {
								bankMap.put(majorStockHolderName, majorStockHolderName);
							} else if (majorStockHolderName.toLowerCase().contains("investment")) {
								investmentMap.put(majorStockHolderName, majorStockHolderName);
							}

							StockVO tempSvo = getStock(iCurPrice, iSpecificDayEndPrice, td);

							lRetainAmountTotal += tempSvo.getlRetainAmount();
							lRetainVolumeTotal += tempSvo.getlRetainVolume();
							fRetainRatioTotal += tempSvo.getfRetainRatio();

							Vector<MajorStockHolderVO> majorStockHolderVO = tempSvo.getMajorStockHolderList();

							svo.getMajorStockHolderList().addAll(majorStockHolderVO);
							logger.debug(svo.getMajorStockHolderList().toString());
							break;
						}
					} else {
						// 입력한 주요주주가 없을 경우
						majorStockHolderNameMap.put(majorStockHolderName, majorStockHolderName);
						StockVO tempSvo = getStock(iCurPrice, iSpecificDayEndPrice, td);
						lRetainAmountTotal += tempSvo.getlRetainAmount();
						lRetainVolumeTotal += tempSvo.getlRetainVolume();
						fRetainRatioTotal += tempSvo.getfRetainRatio();

						Vector<MajorStockHolderVO> majorStockHolderVO = tempSvo.getMajorStockHolderList();

						svo.getMajorStockHolderList().addAll(majorStockHolderVO);
					}

				}
			}

			svo.setlRetainVolume(lRetainVolumeTotal);
			svo.setlRetainAmount(lRetainAmountTotal);
			svo.setfRetainRatio(fRetainRatioTotal);

			logger.debug("stock.getMajorStockHolderList().size() :" + svo.getMajorStockHolderList().size());

			if (svo.getMajorStockHolderList().size() > 0) {
				return svo;
			} else {
				logger.debug("stock.getMajorStockHolderList().size() is less than 0");
				return null;
			}

		} catch (

		IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringNumberWithoutComma(String strWithComma) {
		String strWithoutComma = strWithComma.replaceAll(",", "");
		strWithoutComma = strWithoutComma.replaceAll("&nbsp;", "");
		strWithoutComma = strWithoutComma.replaceAll("&bsp;", "");
		strWithoutComma = strWithoutComma.replaceAll("&sp;", "");
		strWithoutComma = strWithoutComma.replaceAll("&p;", "");
		return strWithoutComma;
	}

	public static StockVO getStock(int iCurPrice, int iSpecificDayEndPrice, Elements td) {
		StockVO tempSvo = new StockVO();
		tempSvo.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

		String majorStockHolderName = StringUtils.defaultIfEmpty(td.get(0).attr("title"), "");

		// 보유주식수
		String retainVolumeWithComma = StringUtils.defaultIfEmpty(td.get(1).text(), "0");
		String retainVolumeWithoutComma = getStringNumberWithoutComma(retainVolumeWithComma);
		long lRetainVolume = Long.parseLong(retainVolumeWithoutComma);

		// 단위: 백만원
		long lRetainAmount = lRetainVolume * iCurPrice;
		long lRetainAmountByMillion = lRetainAmount / 1000000;

		String retainAmount = df.format(lRetainAmount);
		String retainAmountByMillion = df.format(lRetainAmountByMillion);

		String retainRatio = StringUtils.defaultIfEmpty(td.get(2).text(), "0");
		retainRatio = getStringNumberWithoutComma(retainRatio);
		logger.debug("retainRatio1 :[" + retainRatio + "]");
		float fRetainRatio = Float.parseFloat(retainRatio);

		tempSvo.setlRetainAmount(lRetainAmount);
		tempSvo.setlRetainVolume(lRetainVolume);
		tempSvo.setfRetainRatio(fRetainRatio);

		logger.debug("retainVolumeWithComma :" + retainVolumeWithComma);
		logger.debug("retainAmount :" + retainAmount);
		logger.debug("retainRatio :" + retainRatio);

		MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();

		majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
		logger.debug("set and getMajorStockHolderName:" + majorStockHolderVO.getMajorStockHolderName());
		majorStockHolderVO.setRetainVolume(retainVolumeWithComma);
		majorStockHolderVO.setRetainAmount(retainAmount);
		majorStockHolderVO.setRetainAmountByMillion(retainAmountByMillion);
		majorStockHolderVO.setRetainRatio(retainRatio);

		majorStockHolderVO.setlRetainVolume(lRetainVolume);
		majorStockHolderVO.setlRetainAmount(lRetainAmount);
		majorStockHolderVO.setlRetainAmountByMillion(lRetainAmountByMillion);
		majorStockHolderVO.setfRetainRatio(fRetainRatio);

		// 특정일 보유가격=보유주식수*특정일종가
		long lSpecificDayRetainAmount = lRetainVolume * iSpecificDayEndPrice;
		String specificDayRetainAmount = df.format(lSpecificDayRetainAmount);

		long lSpecificDayRetainAmountByMillion = lSpecificDayRetainAmount / 1000000;
		String specificDayRetainAmountByMillion = df.format(lSpecificDayRetainAmountByMillion);

		majorStockHolderVO.setSpecificDayRetainAmount(specificDayRetainAmount);
		majorStockHolderVO.setlSpecificDayRetainAmount(lSpecificDayRetainAmount);

		majorStockHolderVO.setSpecificDayRetainAmountByMillion(specificDayRetainAmountByMillion);
		majorStockHolderVO.setlSpecificDayRetainAmountByMillion(lSpecificDayRetainAmountByMillion);

		// 차이금액=특정시점 총액-현재 총액
		long lSpecificDayVsCurDayGapAmount = lRetainAmount - lSpecificDayRetainAmount;
		String specificDayVsCurDayGapAmount = df.format(lSpecificDayVsCurDayGapAmount);
		majorStockHolderVO.setlSpecificDayVsCurDayGapAmount(lSpecificDayVsCurDayGapAmount);
		majorStockHolderVO.setSpecificDayVsCurDayGapAmount(specificDayVsCurDayGapAmount);

		long lSpecificDayVsCurDayGapAmountByMillion = lSpecificDayVsCurDayGapAmount / 1000000;
		String specificDayVsCurDayGapAmountByMillion = df.format(lSpecificDayVsCurDayGapAmountByMillion);
		majorStockHolderVO.setlSpecificDayVsCurDayGapAmountByMillion(lSpecificDayVsCurDayGapAmountByMillion);
		majorStockHolderVO.setSpecificDayVsCurDayGapAmountByMillion(specificDayVsCurDayGapAmountByMillion);
		logger.debug("majorStockHolderVO :" + majorStockHolderVO);

		tempSvo.getMajorStockHolderList().add(majorStockHolderVO);
		logger.debug(tempSvo.getMajorStockHolderList().toString());
		return tempSvo;
	}

	public void writeFile(List<StockVO> list, String title) {
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd][HH.mm.ss.SSS]", Locale.KOREAN);

		String strDate = sdf.format(new Date());

		StringBuilder sb1 = new StringBuilder();
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    .red {color:red}\r\n");
		sb1.append("    .blue {color:blue}\r\n");
		sb1.append("    .gray {color:gray}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2>" + strYMD + title + "</h2>");
		sb1.append("\t<h3>비교 대상 기준일 :" + specificDay + "</h3>");

		long lTotalSpecificDayVsCurDayGapAmount = 0;
		long lTotalSpecificDayRetainAmount = 0;
		long lRetainAmount = 0;

		for (StockVO svo : list) {
			Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
			for (int i = 0; i < vt.size(); i++) {
				MajorStockHolderVO holderVO = vt.get(i);
				// 현재 보유총액
				lRetainAmount += holderVO.getlRetainAmount();
				// 특정일총액
				lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
				// 특정일VS현재 차이총액
				long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
				lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
			}
		}

		String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
		sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalSpecificDayRetainAmount) + "<br/>\r\n");

		String retainAmount = df.format(lRetainAmount);
		sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

		double dTotalSpecificDayRetainAmount = Double.parseDouble(lTotalSpecificDayRetainAmount + "");
		double dRetainAmount = Double.parseDouble(lRetainAmount + "");
		double gapRatio = Math.round(
				(dTotalSpecificDayRetainAmount - dRetainAmount) / dTotalSpecificDayRetainAmount * 100 * 100) / 100.0;
		gapRatio = -gapRatio;

		String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
		sb1.append("기준일대비 현재 보유총액 차이(원) = ");
		sb1.append(StockUtil.moneyUnitSplit("원", lTotalSpecificDayVsCurDayGapAmount));

		sb1.append("<br/>\r\n");
		sb1.append("기준일대비 등락율 = ");
		sb1.append(gapRatio + "%<br/>\r\n");

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가 대비 등락율</td>\r\n");
		if (!inputWordIsSameAsMajorStockHolders) {
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재 보유총액(억)</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 보유총액(억)</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 대비 총액차(억)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;

		for (StockVO svo : list) {
			String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
			Vector vt = svo.getMajorStockHolderList();
			int listSize = vt.size();
			if (svo != null) {
				String specificDayEndPriceVsCurPriceUpDownRatioStyle = "";
				if (svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() < 0) {
					specificDayEndPriceVsCurPriceUpDownRatioStyle = "blue";
				} else if (svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() == 0) {
					specificDayEndPriceVsCurPriceUpDownRatioStyle = "gray";
				} else {
					specificDayEndPriceVsCurPriceUpDownRatioStyle = "red";
				}
				for (int i = 0; i < listSize; i++) {
					sb1.append("<tr>\r\n");
					if (i == 0) {
						sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + "><a href='" + url + "' target=_new>"
								+ svo.getStockName() + "</a></td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>" + svo.getCurPrice()
								+ "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getSpecificDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right' class='"
								+ specificDayEndPriceVsCurPriceUpDownRatioStyle + "'>")
								.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");
					}

					MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
					if (!inputWordIsSameAsMajorStockHolders) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");

					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right' class='" + specificDayEndPriceVsCurPriceUpDownRatioStyle
							+ "'>" + StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayVsCurDayGapAmount())
							+ "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
			}
		}

		sb1.append("</table>\r\n");
		if (investCompanyMap.size() > 0) {
			sb1.append("<h3>자산운용사 목록</h3>");
			logger.debug("assetMgmtCoMap.size:" + investCompanyMap.size());

			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			int count = 0;
			StringBuilder assetMgmtCoSb = new StringBuilder();
			while (it.hasNext()) {
				String key = it.next();
				if (count != 0) {
					assetMgmtCoSb.append(",");
				}
				assetMgmtCoSb.append("" + key + "");
				count++;
			}
			sb1.append(assetMgmtCoSb.toString());
		}
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\documents\\" + strDate + "_" + title + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public void writeFile(List<StockVO> list, String majorStockHolder, String title) {
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd][HH.mm.ss.SSS]", Locale.KOREAN);

		String strDate = sdf.format(new Date());

		StringBuilder sb1 = new StringBuilder();
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    .red {color:red}\r\n");
		sb1.append("    .blue {color:blue}\r\n");
		sb1.append("    .gray {color:gray}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2>" + strYMD + title + "</h2>");
		sb1.append("\t<h3>비교 대상 기준일 :" + specificDay + "</h3>");

		long lTotalSpecificDayVsCurDayGapAmount = 0;
		long lTotalSpecificDayRetainAmount = 0;
		long lRetainAmount = 0;

		for (StockVO svo : list) {
			Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
			for (int i = 0; i < vt.size(); i++) {
				MajorStockHolderVO holderVO = vt.get(i);
				String majorStockHolderName = holderVO.getMajorStockHolderName();
				if (majorStockHolderName.contains(majorStockHolder)) {
					// 현재 보유총액
					lRetainAmount += holderVO.getlRetainAmount();
					// 특정일총액
					lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
					// 특정일VS현재 차이총액
					long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
					lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
				}
			}
		}

		String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
		sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalSpecificDayRetainAmount) + "<br/>\r\n");

		String retainAmount = df.format(lRetainAmount);
		sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

		double dTotalSpecificDayRetainAmount = Double.parseDouble(lTotalSpecificDayRetainAmount + "");
		double dRetainAmount = Double.parseDouble(lRetainAmount + "");
		double gapRatio = Math.round(
				(dTotalSpecificDayRetainAmount - dRetainAmount) / dTotalSpecificDayRetainAmount * 100 * 100) / 100.0;
		if (dTotalSpecificDayRetainAmount < dRetainAmount) {
			gapRatio = -gapRatio;
		}

		String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
		sb1.append("기준일대비 현재 보유총액 차이(원) = ");
		sb1.append(StockUtil.moneyUnitSplit("원", lTotalSpecificDayVsCurDayGapAmount));

		sb1.append("<br/>\r\n");
		sb1.append("기준일대비 등락율 = ");
		sb1.append(gapRatio + "%");

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가 대비 등락율</td>\r\n");
		if (!inputWordIsSameAsMajorStockHolders) {
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재 보유총액(억)</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 보유총액(억)</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 대비 총액차(억)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;

		for (StockVO svo : list) {
			String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
			Vector vt = svo.getMajorStockHolderList();
			int listSize = vt.size();
			for (int i = 0; i < listSize; i++) {
				MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
				String majorStockHolderName = holderVO.getMajorStockHolderName();
				if (majorStockHolderName.contains(majorStockHolder)) {

					sb1.append("<tr>\r\n");
					if (i == 0) {
						sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + "><a href='" + url + "' target=_new>"
								+ svo.getStockName() + "</a></td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>" + svo.getCurPrice()
								+ "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getSpecificDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");
					}

					if (!inputWordIsSameAsMajorStockHolders) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");

					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayVsCurDayGapAmount()) + "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
			}
		}

		sb1.append("</table>\r\n");
		sb1.append("<h3>자산운용사 목록</h3>");

		Set<String> keys = investCompanyMap.keySet();
		Iterator<String> it = keys.iterator();
		int count = 0;
		StringBuilder assetMgmtCoSb = new StringBuilder();
		while (it.hasNext()) {
			String key = it.next();
			if (count != 0) {
				assetMgmtCoSb.append(",");
			}
			assetMgmtCoSb.append("" + key + "");
			count++;
		}
		sb1.append(assetMgmtCoSb.toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\documents\\" + strDate + "_" + title + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public void writeFile(int count, Map<String, List<StockVO>> kospiKosdaqStockMap, String majorStockHolder) {
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd][HH.mm.ss.SSS]", Locale.KOREAN);

		String strDate = sdf.format(new Date());

		StringBuilder sb1 = new StringBuilder();
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    .red {color:red}\r\n");
		sb1.append("    .blue {color:blue}\r\n");
		sb1.append("    .gray {color:gray}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		Set<String> keySet = kospiKosdaqStockMap.keySet();
		Iterator kospiKosdaqIt = keySet.iterator();
		int listSize = 0;
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey1 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);
			listSize += list.size();
		}
		logger.debug("listSize=>" + listSize);
		// 데이터가 없으면 빠져나간다.
		if (listSize == 0) {
			return;
		}

		String title = strYMD + " " + majorStockHolder + " 보유종목(보유금액순)";
		sb1.append("\t<h1>" + title + "</h1>");
		sb1.append("\t<h2>비교 대상 기준일 :" + specificDay + "</h2>");

//		keySet = kospiKosdaqStockMap.keySet();
		kospiKosdaqIt = keySet.iterator();
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey2 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);

			long lTotalSpecificDayVsCurDayGapAmount = 0;
			long lTotalSpecificDayRetainAmount = 0;
			long lRetainAmount = 0;

			for (StockVO svo : list) {
				Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
				for (int i = 0; i < vt.size(); i++) {
					MajorStockHolderVO holderVO = vt.get(i);
					String majorStockHolderName = holderVO.getMajorStockHolderName();
					if (majorStockHolderName.contains(majorStockHolder)) {
						// 현재 보유총액
						lRetainAmount += holderVO.getlRetainAmount();
						// 특정일총액
						lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
						// 특정일VS현재 차이총액
						long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
						lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
					}
				}
			}

			if (lRetainAmount <= 0) {
				continue;
			}

			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>");

			String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
			sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalSpecificDayRetainAmount) + "<br/>\r\n");

			String retainAmount = df.format(lRetainAmount);
			sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

			double dTotalSpecificDayRetainAmount = Double.parseDouble(lTotalSpecificDayRetainAmount + "");
			double dRetainAmount = Double.parseDouble(lRetainAmount + "");
			double gapRatio = Math
					.round((dTotalSpecificDayRetainAmount - dRetainAmount) / dTotalSpecificDayRetainAmount * 100 * 100)
					/ 100.0;
			gapRatio = -gapRatio;

			String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
			sb1.append("기준일대비 현재 보유총액 차이(원) = ");
			sb1.append(StockUtil.moneyUnitSplit("원", lTotalSpecificDayVsCurDayGapAmount));

			sb1.append("<br/>\r\n");
			sb1.append("기준일대비 등락율 = ");
			sb1.append(gapRatio + "%");

			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가 대비 등락율</td>\r\n");
			if (!inputWordIsSameAsMajorStockHolders) {
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
			}
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재 보유총액(억)</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 보유총액(억)</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 대비 총액차(억)</td>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;

			for (StockVO svo : list) {
				String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
				Vector vt = svo.getMajorStockHolderList();
				int majorStockHolderListSize = vt.size();
				logger.debug(svo.getStockName() + "\t majorStockHolderListSize:" + majorStockHolderListSize);
				for (int i = 0; i < majorStockHolderListSize; i++) {
					MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
					String majorStockHolderName = holderVO.getMajorStockHolderName();
					logger.debug("getMajorStockHolderName:" + majorStockHolderName);
					if (majorStockHolderName.contains(majorStockHolder)) {
						logger.debug("majorStockHolderName.contains(majorStockHolder):"
								+ (majorStockHolderName.contains(majorStockHolder)));
						logger.debug("majorStockHolder:" + majorStockHolder + "\t majorStockHolderName:"
								+ majorStockHolderName);

						sb1.append("<tr>\r\n");
						sb1.append("<td>" + cnt++ + "</td>\r\n");
						sb1.append("<td><a href='" + url + "' target=_new>" + svo.getStockName() + "</a></td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getCurPrice() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>").append(svo.getSpecificDayEndPrice())
								.append("</td>\r\n");
						sb1.append("<td style='text-align:right'>")
								.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");

						if (!inputWordIsSameAsMajorStockHolders) {
							sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
						}
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayRetainAmount()) + "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayVsCurDayGapAmount())
								+ "</td>\r\n");

						sb1.append("</tr>\r\n");
					}
				}
			}

			sb1.append("</table>\r\n");
		}
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");

		String fileName = userHome + "\\documents\\" + strDate + "_코스피,코스닥_"
				+ StringUtils.leftPad(String.valueOf(count), 2, "0") + "." + majorStockHolder + "_보유종목(보유금액순).html";
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public void writeFile(Map<String, List<StockVO>> kospiKosdaqStockMap) {

		Set<String> keySet = kospiKosdaqStockMap.keySet();
		Iterator kospiKosdaqIt = keySet.iterator();
		int listSize = 0;
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey1 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);
			listSize += list.size();
		}
		logger.debug("listSize=>" + listSize);
		// 데이터가 없으면 빠져나간다.
		if (listSize == 0) {
			return;
		}

		StringBuilder sb1;
		kospiKosdaqIt = keySet.iterator();
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey2 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);
			sb1 = new StringBuilder();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    .red {color:red}\r\n");
			sb1.append("    .blue {color:blue}\r\n");
			sb1.append("    .gray {color:gray}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");
			sb1.append("\t<h2>비교 대상 기준일 :" + specificDay + "</h2>");
			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>\r\n");

			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {

				String assetMgmtCo = it.next();

				long lTotalSpecificDayVsCurDayGapAmount = 0;
				long lTotalSpecificDayRetainAmount = 0;
				long lRetainAmount = 0;

				for (StockVO svo : list) {
					Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
					for (int i = 0; i < vt.size(); i++) {
						MajorStockHolderVO holderVO = vt.get(i);
						String majorStockHolderName = holderVO.getMajorStockHolderName();
						if (majorStockHolderName.contains(assetMgmtCo)) {
							// 현재 보유총액
							lRetainAmount += holderVO.getlRetainAmount();
							// 특정일총액
							lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
							// 특정일VS현재 차이총액
							long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
							lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
						}
					}
				}

				if (lRetainAmount <= 0) {
					continue;
				}

				String title = strYMD + " " + assetMgmtCo + " 보유종목(보유금액순)";
				sb1.append("\t<h3>" + title + "</h3>");

				String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
				sb1.append(
						"기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalSpecificDayRetainAmount) + "<br/>\r\n");

				String retainAmount = df.format(lRetainAmount);
				sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

				double dTotalSpecificDayRetainAmount = Double.parseDouble(lTotalSpecificDayRetainAmount + "");
				double dRetainAmount = Double.parseDouble(lRetainAmount + "");
				double gapRatio = Math.round(
						(dTotalSpecificDayRetainAmount - dRetainAmount) / dTotalSpecificDayRetainAmount * 100 * 100)
						/ 100.0;
				gapRatio = -gapRatio;

				String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
				sb1.append("기준일대비 현재 보유총액 차이(원) = ");
				sb1.append(StockUtil.moneyUnitSplit("원", lTotalSpecificDayVsCurDayGapAmount));

				sb1.append("<br/>\r\n");
				sb1.append("기준일대비 등락율 = ");
				sb1.append(gapRatio + "%");

				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가 대비 등락율</td>\r\n");
				if (!inputWordIsSameAsMajorStockHolders) {
					sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
				}
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재 보유총액(억)</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 대비 총액차(억)</td>\r\n");
				sb1.append("</tr>\r\n");

				int cnt = 1;

				for (StockVO svo : list) {
					String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
					Vector vt = svo.getMajorStockHolderList();
					int majorStockHolderListSize = vt.size();
					logger.debug(svo.getStockName() + "\t majorStockHolderListSize:" + majorStockHolderListSize);
					for (int i = 0; i < majorStockHolderListSize; i++) {
						MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
						String majorStockHolderName = holderVO.getMajorStockHolderName();
						logger.debug("getMajorStockHolderName:" + majorStockHolderName);
						if (majorStockHolderName.contains(assetMgmtCo)) {
							logger.debug("majorStockHolderName.contains(majorStockHolder):"
									+ (majorStockHolderName.contains(assetMgmtCo)));
							logger.debug("majorStockHolder:" + assetMgmtCo + "\t majorStockHolderName:"
									+ majorStockHolderName);

							sb1.append("<tr>\r\n");
							sb1.append("<td>" + cnt++ + "</td>\r\n");
							sb1.append("<td><a href='" + url + "' target=_new>" + svo.getStockName() + "</a></td>\r\n");
							sb1.append("<td style='text-align:right'>" + svo.getCurPrice() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>").append(svo.getSpecificDayEndPrice())
									.append("</td>\r\n");
							sb1.append("<td style='text-align:right'>")
									.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%")
									.append("</td>\r\n");

							if (!inputWordIsSameAsMajorStockHolders) {
								sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
							}
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayRetainAmount())
									+ "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayVsCurDayGapAmount())
									+ "</td>\r\n");

							sb1.append("</tr>\r\n");
						}
					}
				}

			}
			sb1.append("</table>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			String strDate = sdf.format(new Date());
			String fileName = userHome + "\\documents\\" + strDate + "_" + strKospiKosdaqKey + "_주요주주 보유종목(보유금액순).html";
			FileUtil.fileWrite(fileName, sb1.toString());
		}

	}

	public void writeFile(Map<String, List<StockVO>> kospiKosdaqStockMap, Map<String, String> investCompanyMap,
			String companyType) {
		logger.debug("companyType :" + companyType);
		logger.debug("investCompanyMap :" + investCompanyMap);
		logger.debug("kospiKosdaqStockMap.size :" + kospiKosdaqStockMap.size());
		Set<String> keySet = kospiKosdaqStockMap.keySet();
		Iterator kospiKosdaqIt = keySet.iterator();
		int listSize = 0;
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey1 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);
			listSize += list.size();
		}
		logger.debug("listSize=>" + listSize);
		// 데이터가 없으면 빠져나간다.
		if (listSize == 0) {
			return;
		}

		StringBuilder sb1;
		keySet = kospiKosdaqStockMap.keySet();
		kospiKosdaqIt = keySet.iterator();
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey2 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);
			sb1 = new StringBuilder();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    .red {color:red}\r\n");
			sb1.append("    .blue {color:blue}\r\n");
			sb1.append("    .gray {color:gray}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			String title = strYMD + " " + strKospiKosdaqKey + " " + companyType + " 주요주주 보유종목(보유금액순)";
			sb1.append("\t<h2>" + title + "</h2>");

			sb1.append("\t<h2>비교 대상 기준일 :" + specificDay + "</h2>");
			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>\r\n");

			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			int mapCnt = 1;
			while (it.hasNext()) {

				String investCompany = it.next();
				logger.debug("investCompany :" + investCompany);

				long lTotalSpecificDayVsCurDayGapAmount = 0;
				long lTotalSpecificDayRetainAmount = 0;
				long lRetainAmount = 0;

				for (StockVO svo : list) {
					Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
					for (int i = 0; i < vt.size(); i++) {
						MajorStockHolderVO holderVO = vt.get(i);
						String majorStockHolderName = holderVO.getMajorStockHolderName();
						if (majorStockHolderName.contains(investCompany)) {
							logger.debug(svo.getStockName() + "(" + svo.getStockCode() + ")");
							logger.debug(majorStockHolderName + ".contains(" + investCompany + ")");
							// 현재 보유총액
							lRetainAmount += holderVO.getlRetainAmount();
							// 특정일총액
							lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
							// 특정일VS현재 차이총액
							long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
							lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
						}
					}
				}

				if (lRetainAmount <= 0) {
					continue;
				}

				sb1.append("\t<h3>" + mapCnt++ + "." + investCompany + "</h3>");

				String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
				sb1.append(
						"기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalSpecificDayRetainAmount) + "<br/>\r\n");

				String retainAmount = df.format(lRetainAmount);
				sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

				double dTotalSpecificDayRetainAmount = Double.parseDouble(lTotalSpecificDayRetainAmount + "");
				double dRetainAmount = Double.parseDouble(lRetainAmount + "");
				double gapRatio = Math.round(
						(dTotalSpecificDayRetainAmount - dRetainAmount) / dTotalSpecificDayRetainAmount * 100 * 100)
						/ 100.0;
				gapRatio = -gapRatio;

				String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
				sb1.append("기준일대비 현재 보유총액 차이(원) = ");
				sb1.append(StockUtil.moneyUnitSplit("원", lTotalSpecificDayVsCurDayGapAmount));

				sb1.append("<br/>\r\n");
				sb1.append("기준일대비 등락율 = ");
				sb1.append(gapRatio + "%");

				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일가 대비 등락율</td>\r\n");
				if (!inputWordIsSameAsMajorStockHolders) {
					sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
				}
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재 보유총액(억)</td>\r\n");
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;'>기준일 대비 총액차(억)</td>\r\n");
				sb1.append("</tr>\r\n");

				int cnt = 1;

				for (StockVO svo : list) {
					Vector vt = svo.getMajorStockHolderList();
					int majorStockHolderListSize = vt.size();
					logger.debug(svo.getStockName() + "\t majorStockHolderListSize:" + majorStockHolderListSize);
					for (int i = 0; i < majorStockHolderListSize; i++) {
						MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
						String majorStockHolderName = holderVO.getMajorStockHolderName();
						logger.debug("getMajorStockHolderName:" + majorStockHolderName);
						if (majorStockHolderName.contains(investCompany)) {
							// logger.debug(majorStockHolderName+".contains("+investCompany+"):"+
							// (majorStockHolderName.contains(investCompany)));
							// logger.debug("majorStockHolder:" + investCompany + "\t
							// majorStockHolderName:"+ majorStockHolderName);

							sb1.append("<tr>\r\n");
							sb1.append("<td>" + cnt++ + "</td>\r\n");
							String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
							sb1.append("<td><a href='" + url + "' target=_new>" + svo.getStockName() + "</a></td>\r\n");
							sb1.append("<td style='text-align:right'>" + svo.getCurPrice() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>").append(svo.getSpecificDayEndPrice())
									.append("</td>\r\n");
							sb1.append("<td style='text-align:right'>")
									.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%")
									.append("</td>\r\n");

							if (!inputWordIsSameAsMajorStockHolders) {
								sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
							}
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayRetainAmount())
									+ "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlSpecificDayVsCurDayGapAmount())
									+ "</td>\r\n");

							sb1.append("</tr>\r\n");
						}
					}
				}
				sb1.append("</table>\r\n");
				sb1.append("<br/><br/>\r\n");
				sb1.append("<hr>\r\n");
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			String strDate = sdf.format(new Date());
			String fileName = userHome + "\\documents\\" + strDate + "_" + strKospiKosdaqKey + "_" + companyType
					+ "_보유종목(보유금액순).html";
			FileUtil.fileWrite(fileName, sb1.toString());
		}

	}

}
