package html.parsing.stock.stockholders;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
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

import html.parsing.stock.util.DataSort.RetainAmountDescCompare;
import html.parsing.stock.model.MajorStockHolderVO;
import html.parsing.stock.util.FileUtil;

public class MajorStockHoldersInputDayPriceVsCurPrice {

	private static final Logger logger = LoggerFactory.getLogger(MajorStockHoldersInputDayPriceVsCurPrice.class);

	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	static String baseDay = "";
	static String chosenDay = "";
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

		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
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

		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
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
	public void execute() {
		long start = System.currentTimeMillis();
		try {
			readAndWriteMajorStockHolders();
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

	public void readAndWriteMajorStockHolders() throws Exception {
		inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;

		kospiStockList = StockUtil.readStockCodeNameList("kospi");
		kosdaqStockList = StockUtil.readStockCodeNameList("kosdaq");
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
		pageNo = StockUtil.getChosenDayPageNo("005930", "삼성전자", baseDay);
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

//	@Test
	public void getStockHomepage() {
		getStockHompage(1, "105560", "KB금융");
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

		// 기준일가 또는 올해 상장했을 경우 상장일가 구하기
		if (baseDay.equals("")) {
			baseDay = thisYearFirstTradeDay;
		}
		chosenDay = StockUtil.getChosenDay(baseDay, listedDay);
		logger.debug("chosenDay :" + chosenDay);
		svo.setChosenDay(chosenDay);

//		String chosenDayEndPrice = StockUtil.getChosenDayEndPrice(strStockCode, strStockName, chosenDay);
		String chosenDayEndPrice = "0";
		if (baseDay.equals(chosenDay)) {
			chosenDayEndPrice = StockUtil.findChosenDayEndPrice(strStockCode, strStockName, chosenDay, pageNo);
		} else {
			chosenDayEndPrice = StockUtil.getChosenDayEndPrice(strStockCode, strStockName, chosenDay);
		}
		logger.debug("chosenDayEndPrice :" + chosenDayEndPrice);
		svo.setChosenDayEndPrice(chosenDayEndPrice);

		chosenDayEndPrice = chosenDayEndPrice.replaceAll(",", "");
		logger.debug("chosenDayEndPrice :" + chosenDayEndPrice);
		if (chosenDayEndPrice.equals(""))
			chosenDayEndPrice = "0";
		int iChosenDayEndPrice = Integer.parseInt(chosenDayEndPrice);
		svo.setiChosenDayEndPrice(iChosenDayEndPrice);
		logger.debug("iChosenDayEndPrice :" + iChosenDayEndPrice);
		// ===========================================================================

		Document doc;
		try {
			// 종합정보
			// https://finance.naver.com/item/main.nhn?code=065450
			String itemMainUrl = "http://finance.naver.com/item/main.nhn?code=" + strStockCode;
			logger.debug("itemMainUrl:" + itemMainUrl);
			doc = Jsoup.connect(itemMainUrl).get();
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

			svo = StockUtil.getTodayMarkertPrice(doc, svo, strStockCode);
			int iCurPrice = svo.getiCurPrice();
			logger.debug("iCurPrice:" + iCurPrice);

			// ===========================================================================
			double upDownRatio = 0d;
			if (iChosenDayEndPrice != 0) {
				if (iChosenDayEndPrice < iCurPrice) {
					double d1 = iCurPrice - iChosenDayEndPrice;
					double d2 = d1 / iChosenDayEndPrice * 100;
					upDownRatio = Math.round(d2 * 100) / 100.0;
				} else if (iChosenDayEndPrice > iCurPrice) {
					double d1 = iChosenDayEndPrice - iCurPrice;
					double d2 = d1 / iChosenDayEndPrice * 100;
					upDownRatio = -(Math.round(d2 * 100) / 100.0);
				}
			}
			logger.debug("특정일 대비 up,down 비율:" + upDownRatio + "%");
			svo.setChosenDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
			// ===========================================================================

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

							StockVO tempSvo = getStock(iCurPrice, iChosenDayEndPrice, td);

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
						StockVO tempSvo = getStock(iCurPrice, iChosenDayEndPrice, td);
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

	public static StockVO getStock(int iCurPrice, int iChosenDayEndPrice, Elements td) {
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
		long lChosenDayRetainAmount = lRetainVolume * iChosenDayEndPrice;
		String chosenDayRetainAmount = df.format(lChosenDayRetainAmount);

		long lChosenDayRetainAmountByMillion = lChosenDayRetainAmount / 1000000;
		String chosenDayRetainAmountByMillion = df.format(lChosenDayRetainAmountByMillion);

		majorStockHolderVO.setChosenDayRetainAmount(chosenDayRetainAmount);
		majorStockHolderVO.setlChosenDayRetainAmount(lChosenDayRetainAmount);

		majorStockHolderVO.setChosenDayRetainAmountByMillion(chosenDayRetainAmountByMillion);
		majorStockHolderVO.setlChosenDayRetainAmountByMillion(lChosenDayRetainAmountByMillion);

		// 차이금액=특정시점 총액-현재 총액
		long lChosenDayVsCurDayGapAmount = lRetainAmount - lChosenDayRetainAmount;
		String chosenDayVsCurDayGapAmount = df.format(lChosenDayVsCurDayGapAmount);
		majorStockHolderVO.setlChosenDayVsCurDayGapAmount(lChosenDayVsCurDayGapAmount);
		majorStockHolderVO.setChosenDayVsCurDayGapAmount(chosenDayVsCurDayGapAmount);

		long lChosenDayVsCurDayGapAmountByMillion = lChosenDayVsCurDayGapAmount / 1000000;
		String chosenDayVsCurDayGapAmountByMillion = df.format(lChosenDayVsCurDayGapAmountByMillion);
		majorStockHolderVO.setlChosenDayVsCurDayGapAmountByMillion(lChosenDayVsCurDayGapAmountByMillion);
		majorStockHolderVO.setChosenDayVsCurDayGapAmountByMillion(chosenDayVsCurDayGapAmountByMillion);
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
		sb1.append("\t<h3>비교 대상 기준일 :" + chosenDay + "</h3>");

		long lTotalChosenDayVsCurDayGapAmount = 0;
		long lTotalChosenDayRetainAmount = 0;
		long lRetainAmount = 0;

		for (StockVO svo : list) {
			Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
			for (int i = 0; i < vt.size(); i++) {
				MajorStockHolderVO holderVO = vt.get(i);
				// 현재 보유총액
				lRetainAmount += holderVO.getlRetainAmount();
				// 특정일총액
				lTotalChosenDayRetainAmount += holderVO.getlChosenDayRetainAmount();
				// 특정일VS현재 차이총액
				long lChosenDayVsCurDayGapAmount = holderVO.getlChosenDayVsCurDayGapAmount();
				lTotalChosenDayVsCurDayGapAmount += lChosenDayVsCurDayGapAmount;
			}
		}

		String totalChosenDayRetainAmount = df.format(lTotalChosenDayRetainAmount);
		sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalChosenDayRetainAmount) + "<br/>\r\n");

		String retainAmount = df.format(lRetainAmount);
		sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

		double dTotalChosenDayRetainAmount = Double.parseDouble(lTotalChosenDayRetainAmount + "");
		double dRetainAmount = Double.parseDouble(lRetainAmount + "");
		double gapRatio = Math
				.round((dTotalChosenDayRetainAmount - dRetainAmount) / dTotalChosenDayRetainAmount * 100 * 100) / 100.0;
		gapRatio = -gapRatio;

		String totalChosenDayVsCurDayGapAmount = df.format(lTotalChosenDayVsCurDayGapAmount);
		sb1.append("기준일대비 현재 보유총액 차이(원) = ");
		sb1.append(StockUtil.moneyUnitSplit("원", lTotalChosenDayVsCurDayGapAmount));

		sb1.append("<br/>\r\n");
		sb1.append("기준일대비 등락율 = ");
		sb1.append(gapRatio + "%<br/>\r\n");

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가 대비 등락율</td>\r\n");
		if (!inputWordIsSameAsMajorStockHolders) {
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재 보유총액(억)</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 보유총액(억)</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 대비 총액차(억)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;

		for (StockVO svo : list) {
			String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
			Vector vt = svo.getMajorStockHolderList();
			int listSize = vt.size();
			if (svo != null) {
				String chosenDayEndPriceVsCurPriceUpDownRatioStyle = "";
				if (svo.getChosenDayEndPriceVsCurPriceUpDownRatio() < 0) {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "blue";
				} else if (svo.getChosenDayEndPriceVsCurPriceUpDownRatio() == 0) {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "gray";
				} else {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "red";
				}
				for (int i = 0; i < listSize; i++) {
					sb1.append("<tr>\r\n");
					if (i == 0) {
						sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + "><a href='" + url + "' target=_new>"
								+ svo.getStockName() + "</a></td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>" + svo.getCurPrice()
								+ "</td>\r\n");

						String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
						String varyPrice = svo.getVaryPrice();
						if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
								|| specialLetter.startsWith("+")) {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'><font color='red'>"
									+ specialLetter + " " + varyPrice + "</font></td>\r\n");
						} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
								|| specialLetter.startsWith("-")) {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'><font color='blue'>"
									+ specialLetter + " " + varyPrice + "</font></td>\r\n");
						} else {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'>0</td>\r\n");
						}

						String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
						if (varyRatio.startsWith("+")) {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'><font color='red'>"
									+ varyRatio + "</font></td>\r\n");
						} else if (varyRatio.startsWith("-")) {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'><font color='blue'>"
									+ varyRatio + "</font></td>\r\n");
						} else {
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'><font color='black'>"
									+ varyRatio + "</font></td>\r\n");
						}

						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getChosenDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right' class='"
								+ chosenDayEndPriceVsCurPriceUpDownRatioStyle + "'>")
								.append(svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");
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
							+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right' class='" + chosenDayEndPriceVsCurPriceUpDownRatioStyle
							+ "'>" + StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayVsCurDayGapAmount())
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
		sb1.append("\t<h3>비교 대상 기준일 :" + chosenDay + "</h3>");

		long lTotalChosenDayVsCurDayGapAmount = 0;
		long lTotalChosenDayRetainAmount = 0;
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
					lTotalChosenDayRetainAmount += holderVO.getlChosenDayRetainAmount();
					// 특정일VS현재 차이총액
					long lChosenDayVsCurDayGapAmount = holderVO.getlChosenDayVsCurDayGapAmount();
					lTotalChosenDayVsCurDayGapAmount += lChosenDayVsCurDayGapAmount;
				}
			}
		}

		String totalChosenDayRetainAmount = df.format(lTotalChosenDayRetainAmount);
		sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalChosenDayRetainAmount) + "<br/>\r\n");

		String retainAmount = df.format(lRetainAmount);
		sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

		double dTotalChosenDayRetainAmount = Double.parseDouble(lTotalChosenDayRetainAmount + "");
		double dRetainAmount = Double.parseDouble(lRetainAmount + "");
		double gapRatio = Math
				.round((dTotalChosenDayRetainAmount - dRetainAmount) / dTotalChosenDayRetainAmount * 100 * 100) / 100.0;
		if (dTotalChosenDayRetainAmount < dRetainAmount) {
			gapRatio = -gapRatio;
		}

		String totalChosenDayVsCurDayGapAmount = df.format(lTotalChosenDayVsCurDayGapAmount);
		sb1.append("기준일대비 현재 보유총액 차이(원) = ");
		sb1.append(StockUtil.moneyUnitSplit("원", lTotalChosenDayVsCurDayGapAmount));

		sb1.append("<br/>\r\n");
		sb1.append("기준일대비 등락율 = ");
		sb1.append(gapRatio + "%");

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가 대비 등락율</td>\r\n");
		if (!inputWordIsSameAsMajorStockHolders) {
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재 보유총액(억)</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 보유총액(억)</td>\r\n");
		sb1.append(
				"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 대비 총액차(억)</td>\r\n");
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
								.append(svo.getChosenDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");
					}

					if (!inputWordIsSameAsMajorStockHolders) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");

					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayRetainAmount()) + "</td>\r\n");
					sb1.append("<td style='text-align:right'>"
							+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayVsCurDayGapAmount()) + "</td>\r\n");

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
		sb1.append("\t<h2>비교 대상 기준일 :" + chosenDay + "</h2>");

//		keySet = kospiKosdaqStockMap.keySet();
		kospiKosdaqIt = keySet.iterator();
		while (kospiKosdaqIt.hasNext()) {
			String strKospiKosdaqKey = (String) kospiKosdaqIt.next();
			logger.debug("strKospiKosdaqKey2 :" + strKospiKosdaqKey);
			List<StockVO> list = kospiKosdaqStockMap.get(strKospiKosdaqKey);

			long lTotalChosenDayVsCurDayGapAmount = 0;
			long lTotalChosenDayRetainAmount = 0;
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
						lTotalChosenDayRetainAmount += holderVO.getlChosenDayRetainAmount();
						// 특정일VS현재 차이총액
						long lChosenDayVsCurDayGapAmount = holderVO.getlChosenDayVsCurDayGapAmount();
						lTotalChosenDayVsCurDayGapAmount += lChosenDayVsCurDayGapAmount;
					}
				}
			}

			if (lRetainAmount <= 0) {
				continue;
			}

			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>");

			String totalChosenDayRetainAmount = df.format(lTotalChosenDayRetainAmount);
			sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalChosenDayRetainAmount) + "<br/>\r\n");

			String retainAmount = df.format(lRetainAmount);
			sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

			double dTotalChosenDayRetainAmount = Double.parseDouble(lTotalChosenDayRetainAmount + "");
			double dRetainAmount = Double.parseDouble(lRetainAmount + "");
			double gapRatio = Math.round(
					(dTotalChosenDayRetainAmount - dRetainAmount) / dTotalChosenDayRetainAmount * 100 * 100) / 100.0;
			gapRatio = -gapRatio;

			String totalChosenDayVsCurDayGapAmount = df.format(lTotalChosenDayVsCurDayGapAmount);
			sb1.append("기준일대비 현재 보유총액 차이(원) = ");
			sb1.append(StockUtil.moneyUnitSplit("원", lTotalChosenDayVsCurDayGapAmount));

			sb1.append("<br/>\r\n");
			sb1.append("기준일대비 등락율 = ");
			sb1.append(gapRatio + "%");

			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가 대비 등락율</td>\r\n");
			if (!inputWordIsSameAsMajorStockHolders) {
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
			}
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재 보유총액(억)</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 보유총액(억)</td>\r\n");
			sb1.append(
					"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 대비 총액차(억)</td>\r\n");
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
						sb1.append("<td style='text-align:right'>").append(svo.getChosenDayEndPrice())
								.append("</td>\r\n");
						sb1.append("<td style='text-align:right'>")
								.append(svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");

						if (!inputWordIsSameAsMajorStockHolders) {
							sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
						}
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayRetainAmount()) + "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayVsCurDayGapAmount())
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
			sb1.append("\t<h2>비교 대상 기준일 :" + chosenDay + "</h2>");
			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>\r\n");

			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {

				String assetMgmtCo = it.next();

				long lTotalChosenDayVsCurDayGapAmount = 0;
				long lTotalChosenDayRetainAmount = 0;
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
							lTotalChosenDayRetainAmount += holderVO.getlChosenDayRetainAmount();
							// 특정일VS현재 차이총액
							long lChosenDayVsCurDayGapAmount = holderVO.getlChosenDayVsCurDayGapAmount();
							lTotalChosenDayVsCurDayGapAmount += lChosenDayVsCurDayGapAmount;
						}
					}
				}

				if (lRetainAmount <= 0) {
					continue;
				}

				String title = strYMD + " " + assetMgmtCo + " 보유종목(보유금액순)";
				sb1.append("\t<h3>" + title + "</h3>");

				String totalChosenDayRetainAmount = df.format(lTotalChosenDayRetainAmount);
				sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalChosenDayRetainAmount) + "<br/>\r\n");

				String retainAmount = df.format(lRetainAmount);
				sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

				double dTotalChosenDayRetainAmount = Double.parseDouble(lTotalChosenDayRetainAmount + "");
				double dRetainAmount = Double.parseDouble(lRetainAmount + "");
				double gapRatio = Math
						.round((dTotalChosenDayRetainAmount - dRetainAmount) / dTotalChosenDayRetainAmount * 100 * 100)
						/ 100.0;
				gapRatio = -gapRatio;

				String totalChosenDayVsCurDayGapAmount = df.format(lTotalChosenDayVsCurDayGapAmount);
				sb1.append("기준일대비 현재 보유총액 차이(원) = ");
				sb1.append(StockUtil.moneyUnitSplit("원", lTotalChosenDayVsCurDayGapAmount));

				sb1.append("<br/>\r\n");
				sb1.append("기준일대비 등락율 = ");
				sb1.append(gapRatio + "%");

				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가 대비 등락율</td>\r\n");
				if (!inputWordIsSameAsMajorStockHolders) {
					sb1.append(
							"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
				}
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 대비 총액차(억)</td>\r\n");
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
							sb1.append("<td style='text-align:right'>").append(svo.getChosenDayEndPrice())
									.append("</td>\r\n");
							sb1.append("<td style='text-align:right'>")
									.append(svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");

							if (!inputWordIsSameAsMajorStockHolders) {
								sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
							}
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayRetainAmount())
									+ "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayVsCurDayGapAmount())
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

			sb1.append("\t<h2>비교 대상 기준일 :" + chosenDay + "</h2>");
			sb1.append("\t<h2>" + strKospiKosdaqKey + "</h2>\r\n");

			Set<String> keys = investCompanyMap.keySet();
			Iterator<String> it = keys.iterator();
			int mapCnt = 1;
			while (it.hasNext()) {

				String investCompany = it.next();
				logger.debug("investCompany :" + investCompany);

				long lTotalChosenDayVsCurDayGapAmount = 0;
				long lTotalChosenDayRetainAmount = 0;
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
							lTotalChosenDayRetainAmount += holderVO.getlChosenDayRetainAmount();
							// 특정일VS현재 차이총액
							long lChosenDayVsCurDayGapAmount = holderVO.getlChosenDayVsCurDayGapAmount();
							lTotalChosenDayVsCurDayGapAmount += lChosenDayVsCurDayGapAmount;
						}
					}
				}

				if (lRetainAmount <= 0) {
					continue;
				}

				sb1.append("\t<h3>" + mapCnt++ + "." + investCompany + "</h3>");

				String totalChosenDayRetainAmount = df.format(lTotalChosenDayRetainAmount);
				sb1.append("기준일 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lTotalChosenDayRetainAmount) + "<br/>\r\n");

				String retainAmount = df.format(lRetainAmount);
				sb1.append("현재 보유총액(원) = " + StockUtil.moneyUnitSplit("원", lRetainAmount) + "<br/>\r\n");

				double dTotalChosenDayRetainAmount = Double.parseDouble(lTotalChosenDayRetainAmount + "");
				double dRetainAmount = Double.parseDouble(lRetainAmount + "");
				double gapRatio = Math
						.round((dTotalChosenDayRetainAmount - dRetainAmount) / dTotalChosenDayRetainAmount * 100 * 100)
						/ 100.0;
				gapRatio = -gapRatio;

				String totalChosenDayVsCurDayGapAmount = df.format(lTotalChosenDayVsCurDayGapAmount);
				sb1.append("기준일대비 현재 보유총액 차이(원) = ");
				sb1.append(StockUtil.moneyUnitSplit("원", lTotalChosenDayVsCurDayGapAmount));

				sb1.append("<br/>\r\n");
				sb1.append("기준일대비 등락율 = ");
				sb1.append(gapRatio + "%");

				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가 대비 등락율</td>\r\n");
				if (!inputWordIsSameAsMajorStockHolders) {
					sb1.append(
							"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
				}
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 보유총액(억)</td>\r\n");
				sb1.append(
						"	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 대비 총액차(억)</td>\r\n");
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

							String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
							String varyPrice = svo.getVaryPrice();
							if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
									|| specialLetter.startsWith("+")) {
								sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " "
										+ varyPrice + "</font></td>\r\n");
							} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
									|| specialLetter.startsWith("-")) {
								sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " "
										+ varyPrice + "</font></td>\r\n");
							} else {
								sb1.append("<td style='text-align:right'>0</td>\r\n");
							}

							String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
							if (varyRatio.startsWith("+")) {
								sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio
										+ "</font></td>\r\n");
							} else if (varyRatio.startsWith("-")) {
								sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio
										+ "</font></td>\r\n");
							} else {
								sb1.append("<td style='text-align:right'><font color='black'>" + varyRatio
										+ "</font></td>\r\n");
							}

							sb1.append("<td style='text-align:right'>").append(svo.getChosenDayEndPrice())
									.append("</td>\r\n");
							sb1.append("<td style='text-align:right'>")
									.append(svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");

							if (!inputWordIsSameAsMajorStockHolders) {
								sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
							}
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
							sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlRetainAmount()) + "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayRetainAmount())
									+ "</td>\r\n");
							sb1.append("<td style='text-align:right'>"
									+ StockUtil.moneyUnitSplit("억", holderVO.getlChosenDayVsCurDayGapAmount())
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
