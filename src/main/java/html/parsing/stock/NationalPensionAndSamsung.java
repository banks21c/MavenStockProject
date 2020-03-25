package html.parsing.stock;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;

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

public class NationalPensionAndSamsung {

	private static String SAMSUNG_CORP_NM[] = {"멀티캠퍼스","삼성카드", "삼성바이오로직스","삼성전자", "삼성SDI", "삼성물산", "삼성전기", "삼성에스디에스", "삼성화재", "삼성생명", "호텔신라",
			"삼성증권", "삼성중공업", "에스원", "삼성엔지니어링", "제일기획", "신세계", "신세계인터내셔날", "신세계 I&C", "신세계푸드" };
	private static String SAMSUNG_CORP_CD[] = {"067280","029780", "207940","005930", "006400", "028260", "009150", "018260", "000810", "032830",
			"008770", "016360", "010140", "012750", "028050", "030000", "004170", "031430", "035510", "031440" };

	private static final Logger logger = LoggerFactory.getLogger(NationalPensionAndSamsung.class);

	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String majorStockHolders = "";
	static boolean inputWordIsSameAsMajorStockHolders = false;
	static List<StockVO> kospiStockList = new ArrayList<StockVO>();
	static List<StockVO> kosdaqStockList = new ArrayList<StockVO>();
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	static DecimalFormat df = new DecimalFormat("#,##0");

	public void readAndWriteMajorStockHoldersTest() {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();

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

		// 두산건설 011160
		kospiStockList = readOne("011160", "두산건설");
		logger.debug("kospiStockList:" + kospiStockList);
		writeFile(kospiStockList, "코스피");

	}

	public void readAndWriteMajorStockHolders_bak() throws Exception {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		try {
//			kospiStockList = StockUtil.readKospiStockCodeNameListFromExcel();
//			kosdaqStockList = StockUtil.readKosdaqStockCodeNameListFromExcel();
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

		kospiStockList = getAllStockInfo(kospiStockList);
		kosdaqStockList = getAllStockInfo(kosdaqStockList);

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + " 보유금액순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + " 보유금액순");

	}

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
		kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
		logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());

		kospiStockList = getSamsungStock(kospiStockList);
		kosdaqStockList = getSamsungStock(kosdaqStockList);
		logger.debug("samsung kospiStockList.size():"+kospiStockList.size());
		logger.debug("samsung kosdaqStockList.size():"+kosdaqStockList.size());

		kospiStockList = getAllStockInfo(kospiStockList);
		kosdaqStockList = getAllStockInfo(kosdaqStockList);

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + "(삼성) 보유금액순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + "(삼성) 보유금액순");

	}

	public static List<StockVO> readOne(String stockCode, String stockName) {
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

	public static List<StockVO> getSamsungStock(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		for (StockVO svo : stockList) {
			String stockCode = svo.getStockCode();
			for(String scc:SAMSUNG_CORP_CD) {
				if(stockCode.equals(scc)) {
					logger.debug("["+stockCode+"]\t["+scc+"]");
					stocks.add(svo);
				}
			}
		}

		return stocks;
	}

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();

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

		StockVO stock = new StockVO();
		stock.setListedDay(listedDay);

		// 연초가 또는 올해 상장했을 경우 상장일가 구하기
		String specificDay = "2020.01.02";
		specificDay = StockUtil.getSpecificDay(specificDay, listedDay);
		stock.setSpecificDay(specificDay);
		String specificDayEndPrice = StockUtil.getSpecificDayEndPrice(strStockCode, strStockName, specificDay);
		stock.setSpecificDayEndPrice(specificDayEndPrice);

		specificDayEndPrice = specificDayEndPrice.replaceAll(",", "");
		logger.debug("specificDayEndPrice :" + specificDayEndPrice);
		if (specificDayEndPrice.equals(""))
			specificDayEndPrice = "0";
		int iSpecificDayEndPrice = Integer.parseInt(specificDayEndPrice);
		stock.setiSpecificDayEndPrice(iSpecificDayEndPrice);
		logger.debug("iSpecificDayEndPrice :" + iSpecificDayEndPrice);
		// ===========================================================================

		Document doc;
		try {
			// 종합정보
			String itemMainUrl = "http://finance.naver.com/item/main.nhn?code=" + strStockCode;
			logger.debug("itemMainUrl:" + itemMainUrl);
			doc = Jsoup.connect(itemMainUrl).get();
			if (cnt == 1) {
				// logger.debug(doc.title());
				// logger.debug(doc.html());
			}
			stock.setStockCode(strStockCode);
			stock.setStockName(strStockName);

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
			stock.setCurPrice(curPriceWithComma);
			stock.setiCurPrice(iCurPrice);

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
			stock.setSpecificDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
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
			stock.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

			long lRetainAmountTotal = 0;
			long lRetainVolumeTotal = 0;
			float fRetainRatioTotal = 0;

			for (Element tr : trElem) {
				Elements td = tr.select("td");

				String retainVolumeWithComma = "";
				String retainVolumeWithoutComma = "";
				long lRetainVolume = 0;

				String retainAmount = "";
				String retainAmountByMillion = "";
				long lRetainAmount = 0;
				long lRetainAmountByMillion = 0;

				String specificDayRetainAmount = "";
				long lSpecificDayRetainAmount = 0;

				String specificDayRetainAmountByMillion = "";
				long lSpecificDayRetainAmountByMillion = 0;

				String retainRatio = "";
				float fRetainRatio = 0;

				if (td.size() > 0) {
					// 주요주주
					String majorStockHolderName = td.get(0).attr("title");
					logger.debug("majorStockHolderName:" + majorStockHolderName);
					if (majorStockHolderName.equals(""))
						break;
					if (majorStockHolderName.equals(majorStockHolders)) {
						inputWordIsSameAsMajorStockHolders = true;
						logger.debug("inputWordIsSameAsMajorStockHolders:" + inputWordIsSameAsMajorStockHolders);
					}

					if (majorStockHolders.equals("") || majorStockHolderName.indexOf(majorStockHolders) != -1) {
						// 보유주식수
						retainVolumeWithComma = StringUtils.defaultIfEmpty(td.get(1).text(), "0");
						retainVolumeWithoutComma = retainVolumeWithComma.replaceAll(",", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&nbsp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&bsp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&sp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&p;", "");
						lRetainVolume = Long.parseLong(retainVolumeWithoutComma);

						// 단위: 백만원
						lRetainAmount = lRetainVolume * iCurPrice;
						lRetainAmountByMillion = lRetainAmount / 1000000;

						retainAmount = df.format(lRetainAmount);
						retainAmountByMillion = df.format(lRetainAmountByMillion);

						retainRatio = StringUtils.defaultIfEmpty(td.get(2).text(), "0");
						logger.debug("retainRatio1 :[" + retainRatio + "]");
						retainRatio = retainRatio.replaceAll("&nbsp;", "");
						retainRatio = retainRatio.replaceAll("&bsp;", "");
						retainRatio = retainRatio.replaceAll("&sp;", "");
						retainRatio = retainRatio.replaceAll("&p;", "");
						fRetainRatio = Float.parseFloat(retainRatio);

						lRetainAmountTotal += lRetainAmount;
						lRetainVolumeTotal += lRetainVolume;
						fRetainRatioTotal += fRetainRatio;
						logger.debug("retainVolumeWithComma :" + retainVolumeWithComma);
						logger.debug("retainAmount :" + retainAmount);
						logger.debug("retainRatio :" + retainRatio);

						MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();

						majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
						majorStockHolderVO.setRetainVolume(retainVolumeWithComma);
						majorStockHolderVO.setRetainAmount(retainAmount);
						majorStockHolderVO.setRetainAmountByMillion(retainAmountByMillion);
						majorStockHolderVO.setRetainRatio(retainRatio);

						majorStockHolderVO.setlRetainVolume(lRetainVolume);
						majorStockHolderVO.setlRetainAmount(lRetainAmount);
						majorStockHolderVO.setlRetainAmountByMillion(lRetainAmountByMillion);
						majorStockHolderVO.setfRetainRatio(fRetainRatio);

						// 특정일 보유가격=보유주식수*특정일종가
						lSpecificDayRetainAmount = lRetainVolume * iSpecificDayEndPrice;
						specificDayRetainAmount = df.format(lSpecificDayRetainAmount);

						lSpecificDayRetainAmountByMillion = lSpecificDayRetainAmount / 1000000;
						specificDayRetainAmountByMillion = df.format(lSpecificDayRetainAmountByMillion);

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
						String specificDayVsCurDayGapAmountByMillion = df
								.format(lSpecificDayVsCurDayGapAmountByMillion);
						majorStockHolderVO
								.setlSpecificDayVsCurDayGapAmountByMillion(lSpecificDayVsCurDayGapAmountByMillion);
						majorStockHolderVO
								.setSpecificDayVsCurDayGapAmountByMillion(specificDayVsCurDayGapAmountByMillion);
						logger.debug("majorStockHolderVO :" + majorStockHolderVO);

						stock.getMajorStockHolderList().add(majorStockHolderVO);
						logger.debug(stock.getMajorStockHolderList().toString());
					}
				}
			}

			stock.setlRetainVolume(lRetainVolumeTotal);
			stock.setlRetainAmount(lRetainAmountTotal);
			stock.setfRetainRatio(fRetainRatioTotal);

			logger.debug("stock.getMajorStockHolderList().size() :" + stock.getMajorStockHolderList().size());

			if (stock.getMajorStockHolderList().size() > 0) {
				return stock;
			} else {
				logger.debug("stock.getMajorStockHolderList().size() is less than 0");
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeFile(List<StockVO> list, String title) {
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd] HH.mm.ss.SSS", Locale.KOREAN);

		String strDate = sdf.format(new Date());

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
		sb1.append("\t<h3>" + strYMD + title + "</h3>");

		long lTotalSpecificDayVsCurDayGapAmount = 0;
		long lTotalSpecificDayRetainAmount = 0;
		long lRetainAmount = 0;

		for (StockVO svo : list) {
			Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
			for (int i = 0; i < vt.size(); i++) {
				MajorStockHolderVO holderVO = vt.get(i);
				// 현재보유총액
				lRetainAmount += holderVO.getlRetainAmount();
				// 특정일총액
				lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();
				// 특정일VS현재 차이총액
				long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
				lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;
			}
		}

		String retainAmount = df.format(lRetainAmount);
		sb1.append("현재 총금액(원) = " + retainAmount + "<br/>\r\n");

		String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
		sb1.append("연초 총금액(원) = " + totalSpecificDayRetainAmount + "<br/>\r\n");

		String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
		sb1.append("연초대비 현재총금액 차이(원) = ");
		sb1.append(StockUtil.moneyUnitSplit(lTotalSpecificDayVsCurDayGapAmount));

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가 대비 등락율</td>\r\n");
		if (!inputWordIsSameAsMajorStockHolders) {
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재총금액</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초총금액</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가 대비 총액차(원)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;

		for (StockVO svo : list) {
			String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
			Vector vt = svo.getMajorStockHolderList();
			int listSize = vt.size();
			if (svo != null) {
				for (int i = 0; i < listSize; i++) {
					sb1.append("<tr>\r\n");
					if (i == 0) {
						sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + "><a href='" + url + "'>" + svo.getStockName()
								+ "</a></td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>" + svo.getCurPrice()
								+ "</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getSpecificDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%").append("</td>\r\n");
					}

					MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
					if (!inputWordIsSameAsMajorStockHolders) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainAmount() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getSpecificDayRetainAmount() + "</td>\r\n");
					sb1.append(
							"<td style='text-align:right'>" + holderVO.getSpecificDayVsCurDayGapAmount() + "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
			}
		}

		sb1.append("</table>\r\n");

		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\documents\\[" + strDate + "]_" + title + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());
	}

}
