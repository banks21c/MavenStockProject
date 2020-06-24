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
import java.util.List;
import java.util.Locale;
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

import html.parsing.stock.util.DataSort.SpecificDayVsCurPriceUpDownRatioDescCompare;
import html.parsing.stock.model.MajorStockHolderVO;
import html.parsing.stock.util.FileUtil;

public class NationalPensionThreadRun {

	private static final Logger logger = LoggerFactory.getLogger(NationalPensionThreadRun.class);

	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	static String specificDay = "";
	static String thisYearFirstTradeDay = "2020.01.02";
	static String thisYearPeakTradeDay = "2020.01.20";// 2020년 최고지수 2277.23
	static String lastYearFirstTradeDay = "2019.01.02";// 2019년 첫 거래일
	static String lastYearPeakTradeDay = "2019.04.15";// 2019년 최고지수 2252.05
	static String twoYearAgoFirstTradeDay = "2018.01.02";// 2018년 첫 거래일
	static String twoYearAgoPeakTradeDay = "2018.01.29";// 2018년 최고지수 2607.20

	static String majorStockHolders = "";

	static DecimalFormat df = new DecimalFormat("#,##0");
	String moneyUnit = "만";

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		if (majorStockHolders.equals(""))
			majorStockHolders = "국민연금공단";
		specificDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (specificDay.equals(""))
			specificDay = thisYearFirstTradeDay;

		NationalPensionThread thread1 = new NationalPensionThread("kospi");
		thread1.start();
		Thread.sleep(5000);
//		NationalPensionThread thread2 = new NationalPensionThread("kosdaq");
//		thread2.start();

	}

	class NationalPensionThread extends Thread {

		String strYMD = "";
		boolean inputWordIsSameAsMajorStockHolders = false;

		String marketGubun;
		List<StockVO> stockList = new ArrayList<StockVO>();

		NationalPensionThread(String marketGubun) {
			this.marketGubun = marketGubun;
			logger.debug("marketGubun:" + this.marketGubun);
		}

		public void run() {
			// 프로그램 실행 시작 시간
			long start = System.currentTimeMillis();

			try {
				readAndWriteMajorStockHolders();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 프로그램 실행 종료 시간
			long end = System.currentTimeMillis();
			long timeElapsed = end - start;
			logger.debug("실행시간 : " + (end - start) / 1000 + "초");

			int second = (int) timeElapsed / 1000 % 60;
			int minute = (int) timeElapsed / (1000 * 60) % 60;
			int hour = (int) timeElapsed / (1000 * 60 * 60);

			logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
		}

		public void readAndWriteMajorStockHolders() throws Exception {
			String strMarketGubun = "";
			String krxMarketGubun = "";
			if (marketGubun.equals("kospi")) {
				strMarketGubun = "코스피";
				krxMarketGubun = "stockMkt";
			} else if (marketGubun.equals("kosdaq")) {
				strMarketGubun = "코스닥";
				krxMarketGubun = "kosdaqMkt";
			}
			logger.debug("strMarketGubun :" + strMarketGubun);
			logger.debug("krxMarketGubun :" + krxMarketGubun);

			stockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(krxMarketGubun);
			logger.debug("stockList.size2 :" + stockList.size());

			stockList = getAllStockInfo(stockList);

			Collections.sort(stockList, new SpecificDayVsCurPriceUpDownRatioDescCompare());

			writeFile(stockList, strMarketGubun + majorStockHolders + " 투자현황");
		}

		public List<StockVO> readOne(String stockCode, String stockName) throws Exception {
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

		long totalAmount = 0;

		public List<StockVO> getAllStockInfo(List<StockVO> stockList) throws Exception {
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
		public StockVO getStockHompage(int cnt, String strStockCode, String strStockName) throws Exception {
			logger.debug(cnt + ". strStockCode :" + strStockCode + " strStockName :" + strStockName);

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

			// 기준일가 또는 올해 상장했을 경우 상장일가 구하기
			specificDay = StockUtil.getSpecificDay(specificDay, listedDay);
			logger.debug("specificDay :" + specificDay);
			stock.setSpecificDay(specificDay);
			String specificDayEndPrice = StockUtil.getChosenDayEndPrice(strStockCode, strStockName, specificDay);
			logger.debug("specificDayEndPrice1 :" + specificDayEndPrice);
			stock.setSpecificDayEndPrice(specificDayEndPrice);

			specificDayEndPrice = specificDayEndPrice.replaceAll(",", "");
			logger.debug("specificDayEndPrice2 :" + specificDayEndPrice);
			if (specificDayEndPrice.equals(""))
				specificDayEndPrice = "0";
			int iSpecificDayEndPrice = Integer.parseInt(specificDayEndPrice);
			stock.setiSpecificDayEndPrice(iSpecificDayEndPrice);
			logger.debug("iSpecificDayEndPrice :" + iSpecificDayEndPrice);
			// ===========================================================================

			try {
				// 종합정보
				String itemMainUrl = "http://finance.naver.com/item/main.nhn?code=" + strStockCode;
				logger.debug("itemMainUrl1:" + itemMainUrl);

//				Document doc = Jsoup.connect(itemMainUrl).get();

				StringBuffer sb;
				Document doc = null;
				sb = StockUtil.sendPost(itemMainUrl, "UTF-8");
				doc = Jsoup.parse(sb.toString());

				logger.debug("doc.title:" + doc.title());
				stock.setStockCode(strStockCode);
				stock.setStockName(strStockName);
				logger.debug("strStockCode:" + strStockCode);
				logger.debug("strStockName:" + strStockName);

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
				stock.setChosenDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
				// ===========================================================================

				Elements no_exday = doc.select(".no_exday");
				Element new_totalinfo = null;
				if (no_exday.size() > 0) {
					new_totalinfo = no_exday.get(0);
//					logger.debug("new_totalinfo:" + new_totalinfo);
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
						logger.debug("text:" + text);

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
				doc = Jsoup
						.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + strStockCode)
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
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
			sb1.append("기준일 총금액(원) = " + totalSpecificDayRetainAmount + "<br/>\r\n");

			String totalSpecificDayVsCurDayGapAmount = df.format(lTotalSpecificDayVsCurDayGapAmount);
			sb1.append("기준일대비 현재총금액 차이(" + moneyUnit + ") = ");
			sb1.append(StockUtil.moneyUnitSplit(moneyUnit, lTotalSpecificDayVsCurDayGapAmount));

			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가(원)</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가(원)</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 比<br/>등락율</td>\r\n");
			if (!inputWordIsSameAsMajorStockHolders) {
				sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주요주주</td>\r\n");
			}
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주식수</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재총금액(" + moneyUnit
					+ ")</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 <br/>총금액(" + moneyUnit
					+ ")</td>\r\n");
			sb1.append("	<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일 比 <br/>총액차(" + moneyUnit
					+ ")</td>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;

			for (StockVO svo : list) {
				String itemMainUrl = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
				logger.debug("itemMainUrl2:" + itemMainUrl);
				Vector vt = svo.getMajorStockHolderList();
				int listSize = vt.size();
				if (svo != null) {
					for (int i = 0; i < listSize; i++) {
						sb1.append("<tr>\r\n");
						if (i == 0) {
							sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
							sb1.append("<td rowspan=" + listSize + "><a href='" + itemMainUrl + "'>"
									+ svo.getStockName() + "</a></td>\r\n");
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'>" + svo.getCurPrice()
									+ "</td>\r\n");
							sb1.append("<td rowspan=" + listSize + " style='text-align:right'>")
									.append(svo.getChosenDayEndPrice()).append("</td>\r\n");

							String strColor = "color:black";
							double rate = svo.getSpecificDayEndPriceVsCurPriceUpDownRatio();
							if (rate < 0) {
								strColor = "color:blue;";
							} else if (rate > 0) {
								strColor = "color:red;";
							}

							sb1.append("<td rowspan=" + listSize + " style='text-align:right;" + strColor + "'>")
									.append(svo.getSpecificDayEndPriceVsCurPriceUpDownRatio() + "%")
									.append("</td>\r\n");
						}

						MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
						if (!inputWordIsSameAsMajorStockHolders) {
							sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
						}
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
						sb1.append("<td style='text-align:right'>" + holderVO.getRetainAmount() + "</td>\r\n");
						sb1.append(
								"<td style='text-align:right'>" + holderVO.getSpecificDayRetainAmount() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + holderVO.getSpecificDayVsCurDayGapAmount()
								+ "</td>\r\n");

//					sb1.append("<td style='text-align:right'>" + holderVO.getRetainAmount() + "</td>\r\n");
//					sb1.append("<td style='text-align:right'>" + holderVO.getSpecificDayRetainAmount() + "</td>\r\n");
//					sb1.append("<td style='text-align:right'>" + holderVO.getSpecificDayVsCurDayGapAmount() + "</td>\r\n");

						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit(moneyUnit, holderVO.getlRetainAmount()) + "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit(moneyUnit, holderVO.getlSpecificDayRetainAmount())
								+ "</td>\r\n");
						sb1.append("<td style='text-align:right'>"
								+ StockUtil.moneyUnitSplit(moneyUnit, holderVO.getlSpecificDayVsCurDayGapAmount())
								+ "</td>\r\n");

						sb1.append("</tr>\r\n");
					}
				}
			}

			sb1.append("</table>\r\n");

			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			String fileName = userHome + "\\documents\\" + strDate + "_" + title + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());
		}
	}

}