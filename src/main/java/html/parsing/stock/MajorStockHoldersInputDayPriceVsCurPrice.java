package html.parsing.stock;

import java.io.IOException;
import java.math.BigInteger;
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
import html.parsing.stock.DataSort.RetainRatioDescCompare;
import html.parsing.stock.util.FileUtil;

public class MajorStockHoldersInputDayPriceVsCurPrice {

	private static final Logger logger = LoggerFactory.getLogger(MajorStockHoldersInputDayPriceVsCurPrice.class);

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
		
		// 케이씨씨글라스 344820 
		kospiStockList = readOne("344820 ", "케이씨씨글라스");
		logger.debug("kospiStockList:" + kospiStockList);
		writeFile(kospiStockList, "코스피");
		
	}

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
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

		Collections.sort(kospiStockList, new RetainRatioDescCompare());
		Collections.sort(kosdaqStockList, new RetainRatioDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + " 보유율순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + " 보유율순");

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + " 보유금액순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + " 보유금액순");

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
		logger.debug(cnt+". code :" + strStockCode + " name :" + strStockName);
		Document doc;
		StockVO stock = new StockVO();

		// ===========================================================================
		// 상장일 구하기
		String listedDay = StockUtil.getStockListedDay(strStockCode);
		logger.debug("listedDay :" + listedDay);
		stock.setListedDay(listedDay);

		// 연초가 또는 올해 상장했을 경우 상장일가 구하기
		String yearFirstTradeDay = "2020.01.02";
		yearFirstTradeDay = StockUtil.getYearFirstTradeDay(yearFirstTradeDay, listedDay);
		stock.setYearFirstTradeDay(yearFirstTradeDay);
		String yearFirstTradeDayEndPrice = StockUtil.getYearFirstTradeDayEndPrice(strStockCode, strStockName,
				yearFirstTradeDay);
		stock.setYearFirstTradeDayEndPrice(yearFirstTradeDayEndPrice);

		yearFirstTradeDayEndPrice = yearFirstTradeDayEndPrice.replaceAll(",", "");
		logger.debug("yearFirstTradeDayEndPrice :" + yearFirstTradeDayEndPrice);
		if (yearFirstTradeDayEndPrice.equals(""))
			yearFirstTradeDayEndPrice = "0";
		int iYearFirstTradeDayEndPrice = Integer.parseInt(yearFirstTradeDayEndPrice);
		stock.setiYearFirstTradeDayEndPrice(iYearFirstTradeDayEndPrice);
		logger.debug("iYearFirstTradeDayEndPrice :" + iYearFirstTradeDayEndPrice);
		// ===========================================================================

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
			if (iYearFirstTradeDayEndPrice != 0) {
				if (iYearFirstTradeDayEndPrice < iCurPrice) {
					double d1 = iCurPrice - iYearFirstTradeDayEndPrice;
					double d2 = d1 / iYearFirstTradeDayEndPrice * 100;
					upDownRatio = Math.round(d2 * 100) / 100.0;
				} else if (iYearFirstTradeDayEndPrice > iCurPrice) {
					double d1 = iYearFirstTradeDayEndPrice - iCurPrice;
					double d2 = d1 / iYearFirstTradeDayEndPrice * 100;
					upDownRatio = -(Math.round(d2 * 100) / 100.0);
				}
			}
			logger.debug("특정일 대비 up,down 비율:" + upDownRatio + "%");
			stock.setYearFirstTradeDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
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
			for (Element tr : trElem) {
				Elements trTd = tr.select("td");

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

				long lRetainAmountTotal = 0;
				long lRetainVolumeTotal = 0;
				float fRetainRatioTotal = 0;

				//MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();
				if (trTd.size() > 0) {
					String majorStockHolderName = trTd.get(0).attr("title");
					logger.debug("majorStockHolderName:"+majorStockHolderName);
					if (majorStockHolderName.equals(""))
						break;
					if (majorStockHolderName.equals(majorStockHolders)) {
						inputWordIsSameAsMajorStockHolders = true;
						logger.debug("inputWordIsSameAsMajorStockHolders:"+inputWordIsSameAsMajorStockHolders);
					}

					if (majorStockHolders.equals("") || majorStockHolderName.indexOf(majorStockHolders) != -1) {
						retainVolumeWithComma = StringUtils.defaultIfEmpty(trTd.get(1).text(), "0");
						retainVolumeWithoutComma = retainVolumeWithComma.replaceAll(",", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&nbsp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&bsp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&sp;", "");
						retainVolumeWithoutComma = retainVolumeWithoutComma.replaceAll("&p;", "");
						lRetainVolume = Long.parseLong(retainVolumeWithoutComma);
						// 단위: 백만원
						lRetainAmount = lRetainVolume * iCurPrice;
						lRetainAmountByMillion = lRetainAmount / 1000000;
						
						lRetainAmountTotal += lRetainAmount;
						lRetainVolumeTotal += lRetainVolume;
						
						retainAmount = df.format(lRetainAmount);
						retainAmountByMillion = df.format(lRetainAmountByMillion);
						
						retainRatio = StringUtils.defaultIfEmpty(trTd.get(2).text(), "0");
						logger.debug("retainRatio1 :[" + retainRatio + "]");
						retainRatio = retainRatio.replaceAll("&nbsp;", "");
						retainRatio = retainRatio.replaceAll("&bsp;", "");
						retainRatio = retainRatio.replaceAll("&sp;", "");
						retainRatio = retainRatio.replaceAll("&p;", "");
						fRetainRatio = Float.parseFloat(retainRatio);
						
						fRetainRatioTotal += fRetainRatio;
						logger.debug("majorStockHolderName :" + majorStockHolderName);
						logger.debug("retainVolumeWithComma :" + retainVolumeWithComma);
						logger.debug("retainAmount :" + retainAmount);
						logger.debug("retainRatio :" + retainRatio);

						MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();
						majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
						majorStockHolderVO.setRetainVolume(retainVolumeWithComma);
						majorStockHolderVO.setRetainAmount(retainAmount);
						majorStockHolderVO.setRetainAmountByMillion(retainAmountByMillion);
						majorStockHolderVO.setRetainRatio(retainRatio);

						lSpecificDayRetainAmount = lRetainVolume * iYearFirstTradeDayEndPrice;
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

				stock.setlRetainVolumeTotal(lRetainVolumeTotal);
				stock.setlRetainAmountTotal(lRetainAmountTotal);
				stock.setfRetainRatioTotal(fRetainRatioTotal);
			}
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
		long lRetainAmountTotal = 0;

		for (StockVO svo : list) {
			lRetainAmountTotal += svo.getlRetainAmountTotal();
			Vector<MajorStockHolderVO> vt = svo.getMajorStockHolderList();
			for (int i = 0; i < vt.size(); i++) {
				MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
				long lSpecificDayVsCurDayGapAmount = holderVO.getlSpecificDayVsCurDayGapAmount();
				lTotalSpecificDayVsCurDayGapAmount += lSpecificDayVsCurDayGapAmount;

				lTotalSpecificDayRetainAmount += holderVO.getlSpecificDayRetainAmount();

			}
		}

		String retainAmountTotal = df.format(lRetainAmountTotal);
		sb1.append("현재 총금액(원) = " + retainAmountTotal + "<br/>\r\n");
		
		String totalSpecificDayRetainAmount = df.format(lTotalSpecificDayRetainAmount);
		sb1.append("연초 총금액(원) = " + totalSpecificDayRetainAmount + "<br/>\r\n");

		String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
		sb1.append("연초대비 현재총금액 차이(원) = " + totalSpecificDayVsCurDayGapAmount + "<br/>\r\n");

		String strPlusMinus = "";
		if (lTotalSpecificDayVsCurDayGapAmount < 0) {
			strPlusMinus = "-";
		} else {
			strPlusMinus = "+";
		}

		String strAmount = String.valueOf(Math.abs(lTotalSpecificDayVsCurDayGapAmount));

		int jo = (int) (Long.parseLong(strAmount) / 1000000000000L);
		String strJo = String.valueOf(jo);
		int iJo = Integer.parseInt(strJo);

		int uk = (int) (Long.parseLong(strAmount) / 100000000);
		double uk2 = uk / 10000d;
		String uk3 = String.valueOf(uk2);
		String strUk = uk3.substring(uk3.indexOf(".") + 1);
		int iUk = Integer.parseInt(strUk);

		long man = Long.parseLong(strAmount) / 10000;
		double man2 = man / 10000d;
		String man3 = String.valueOf(man2);
		String strMan = man3.substring(man3.indexOf(".") + 1);
		int iMan = Integer.parseInt(strMan);

		/*
		 * String strWon = ""; if(strAmount.length() >= 4) { strWon =
		 * strAmount.substring(strAmount.length()-4); }else { strWon = strAmount; } int
		 * iWon = Integer.parseInt(strWon);
		 */
		BigInteger[] won2 = new BigInteger(strAmount).divideAndRemainder(new BigInteger("10000"));
		String strWon = "";
		if (won2.length > 1) {
			strWon = won2[1].toString();
		} else {
			strWon = won2[0].toString();
		}
		int iWon = Integer.parseInt(strWon);

		sb1.append("연초대비 총금액차 = ");
		sb1.append(strPlusMinus);
		if (!strJo.equals("0")) {
			sb1.append(iJo + "조 ");
		}
		if (!strUk.equals("0")) {
			sb1.append(iUk + "억 ");
		}
		if (!strMan.equals("0")) {
			sb1.append(iMan + "만 ");
		}
		if (!strWon.equals("0")) {
			sb1.append(iWon + "원");
		}

		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가 대비 등락율</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>연초가 대비 총액차(원)</td>\r\n");
		if(!inputWordIsSameAsMajorStockHolders) {
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
		}
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>보유율</td>\r\n");
		sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");
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
								.append(svo.getYearFirstTradeDayEndPrice()).append("</td>\r\n");
						sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
								.append(svo.getYearFirstTradeDayEndPriceVsCurPriceUpDownRatio() + "%")
								.append("</td>\r\n");
					}

					MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
					sb1.append(
							"<td style='text-align:right'>" + holderVO.getSpecificDayVsCurDayGapAmount() + "</td>\r\n");
					if(!inputWordIsSameAsMajorStockHolders) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}					
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainAmountByMillion() + "</td>\r\n");

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
