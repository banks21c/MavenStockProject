package html.parsing.stock;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.StockUtil;

public class AllStockWeeklyVaryPriceRate_ThreadCall {

	AllStockWeeklyVaryPriceRate_Thread thread1 = null;
	AllStockWeeklyVaryPriceRate_Thread thread2 = null;

	int iToday;
	int iLastWeekLastTradeDay;
	int iThisWeekLastTradeDay;

	long start = 0;
	long end = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockWeeklyVaryPriceRate_ThreadCall();
	}

	AllStockWeeklyVaryPriceRate_ThreadCall() {
		start = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = new Date(start);
		System.out.println("start time:" + sdf.format(startDate));

		System.out.println("실행시간 : " + (start) / 1000 + "초");

		// 주간 거래일을 알아낸다.
		getFirstLastDayOfWeek();

		thread1 = new AllStockWeeklyVaryPriceRate_Thread("코스피", iLastWeekLastTradeDay, iThisWeekLastTradeDay);
		thread1.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		thread2 = new AllStockWeeklyVaryPriceRate_Thread("코스닥", iLastWeekLastTradeDay, iThisWeekLastTradeDay);
		thread2.start();

		try {
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		end = System.currentTimeMillis();
		Date endDate = new Date(end);
		System.out.println("end time:" + sdf.format(endDate));

		long timeElapsed = end - start;
		System.out.println("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		System.out.println("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
	}

	public Thread.State getThread1State() {
		if (thread1 != null) {
			return thread1.getState();
		} else {
			return null;
		}
	}

	public Thread.State getThread2State() {
		if (thread2 != null) {
			return thread2.getState();
		} else {
			return null;
		}
	}

	public void getFirstLastDayOfWeek() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

		// 오늘 날짜
		iToday = Integer.parseInt(simpleDateFormat.format(new Date()));

		Date date = null;
		StringBuilder dialogMsg = new StringBuilder();
		dialogMsg.append("날짜를 년월일로 8자리 입력하여 주세요. 양식)YYYYMMDD ex)20200815");
		dialogMsg.append("\n입력하지 않을 경우 오늘 날짜 기준으로 데이터를 추출합니다.");
		dialogMsg.append("\n주의 시작 요일은 일요일입니다.일요일~토요일");
		String ymd = JOptionPane.showInputDialog(dialogMsg.toString());
		System.out.println("ymd:" + ymd);
		if (!ymd.equals("")) {
			try {
				date = simpleDateFormat.parse(ymd);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			date = new Date();
		}
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		System.out.println("Calendar.DAY_OF_MONTH :" + Calendar.DAY_OF_MONTH);
		System.out.println("Calendar.DAY_OF_YEAR :" + Calendar.DAY_OF_YEAR);
		System.out.println("Calendar.DAY_OF_WEEK :" + Calendar.DAY_OF_WEEK);
		System.out.println("Calendar.DAY_OF_WEEK_IN_MONTH :" + Calendar.DAY_OF_WEEK_IN_MONTH);
		System.out.println("cal.get(Calendar.DAY_OF_WEEK) :" + cal.get(Calendar.DAY_OF_WEEK));
		System.out.println("1 - cal.get(Calendar.DAY_OF_WEEK) :" + (1 - cal.get(Calendar.DAY_OF_WEEK)));
		cal.setTime(date);
		cal.add(Calendar.DATE, 1 - cal.get(Calendar.DAY_OF_WEEK) - 2);
		String strFirstDayOfWeek = simpleDateFormat.format(cal.getTime());
		iLastWeekLastTradeDay = Integer.parseInt(strFirstDayOfWeek);
		System.out.println("iLastWeekLastTradeDay :" + iLastWeekLastTradeDay);
		System.out.println("지난주 마지막 주식 거래일(금요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));
		cal.setTime(date);
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) - 1);
		String strLastDayOfWeek = simpleDateFormat.format(cal.getTime());
		iThisWeekLastTradeDay = Integer.parseInt(strLastDayOfWeek);
		if (iThisWeekLastTradeDay > iToday) {
			iThisWeekLastTradeDay = iToday;
		}
		System.out.println("iThisWeekLastTradeDay :" + iThisWeekLastTradeDay);
		System.out.println("이번주 마지막 주식 거래일(금요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));
	}

	public class AllStockWeeklyVaryPriceRate_Thread extends Thread {

		final String USER_HOME = System.getProperty("user.home");
		private Logger logger = LoggerFactory.getLogger(AllStockWeeklyVaryPriceRate_Thread.class);

		String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
		int iYear = Integer.parseInt(strYear);

		// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
		// Locale.KOREAN).format(new Date());
		String strYMD = "";
		DecimalFormat df = new DecimalFormat("#,##0");
		DecimalFormat ratioDf = new DecimalFormat("#,##0.##");

		List<StockVO> stockList = new ArrayList<StockVO>();

		int iLastWeekLastTradeDay;
		int iThisWeekLastTradeDay;
		String strDailyOrWeekly = "주간(Weekly) ";
		String marketType = "";

		AllStockWeeklyVaryPriceRate_Thread() {
			// test();
			real();
		}

		AllStockWeeklyVaryPriceRate_Thread(String marketType, int iLastWeekLastTradeDay, int iThisWeekLastTradeDay) {
			this.marketType = marketType;
			this.iLastWeekLastTradeDay = iLastWeekLastTradeDay;
			this.iThisWeekLastTradeDay = iThisWeekLastTradeDay;
		}

		void test() {
			stockList = readOne("034120", "SBS");

			stockList = getAllStockTrade(stockList);
			Collections.sort(stockList, new VaryRatioDescCompare());
			writeFile();
		}

		void real() {
			start();
		}

		public void run() {
			// 모든 주식 정보를 조회한다.
			System.out.println("marketType :" + marketType);
			stockList = StockUtil.readStockCodeNameList(marketType);
			System.out.println("stockList.size :" + stockList.size());

			stockList = getAllStockTrade(stockList);
			Collections.sort(stockList, new VaryRatioDescCompare());

			writeFile();
		}

		public List<StockVO> readOne(String stockCode, String stockName) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			StockVO svo = new StockVO();
			svo.setStockCode(stockCode);
			svo.setStockName(stockName);
			stocks.add(svo);
			return stocks;
		}

		public List<StockVO> getAllStockTrade(List<StockVO> stockList) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			for (StockVO svo : stockList) {
				stockCode = svo.getStockCode();
				stockName = svo.getStockName();
				
				svo.setiLastWeekLastTradeDay(iLastWeekLastTradeDay);
				svo.setiThisWeekLastTradeDay(iThisWeekLastTradeDay);
				
				System.out.println("_______________________________________");
				System.out.println(cnt + "." + stockCode + "\t" + stockName);
				System.out.println("_______________________________________");

				svo = getStockTrade(svo);

				if (svo != null) {
					stocks.add(svo);
				}
				cnt++;
			}
			return stocks;
		}

		public StockVO getStockTrade(StockVO svo) {
			try {
				String strStockCode = svo.getStockCode();
				String strStockName = svo.getStockName();
				logger.debug(strStockCode + " " + strStockName);
				// =========================================================
				// 투자자별 매매동향 - 외인 보유주수, 보유율
				// https://finance.naver.com/item/frgn.nhn?code=102460&page=1
				// https://finance.naver.com/item/frgn.nhn?code=102460&page=2
				System.out.println("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode);
				Document doc = Jsoup.connect("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode).get();

				Elements type2Elements = doc.select(".type2");
				Element type2Element = type2Elements.get(1);
				// 외인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
				// tr
				Elements trElements = type2Element.select("tr");

				String strSign = "";
				String strTradeDate = "";
				int iTradeDate = 0;
				String strEndPrice = "";
				String strVaryPrice = "";
				int iEndPrice = 0;

				int iThisWeekLastTradeDayEndPrice = 0;
				int iLastWeekLastTradeDayEndPrice = 0;

				// 상장일 구하기
				logger.debug("strStockCode :" + strStockCode);
				String listedDay = StringUtils.defaultString(StockUtil.getStockListedDay(strStockCode));
				if(listedDay.equals("")) {
					listedDay = StringUtils.defaultString(StockUtil.getStockListedDay2(strStockCode));
				}
				listedDay = listedDay.replace(".", "");
				int iListedDay = 0;
				if(!listedDay.equals("")) {
					iListedDay = Integer.parseInt(listedDay.replace(".", ""));
				}else {
					logger.debug(strStockCode+":"+strStockName+"는 상장일이 없습니다. 점검 요망");
				}
				logger.debug("listedDay :" + listedDay);
				logger.debug("iListedDay :" + iListedDay);
				
				logger.debug("iThisWeekLastTradeDay :" + iThisWeekLastTradeDay);
				logger.debug("iLastWeekLastTradeDay :" + iLastWeekLastTradeDay);
				
				if(strStockCode.equals("268600") || strStockCode.equals("225220")) {
					logger.debug("listedDay :" + listedDay);
					logger.debug("iLastWeekLastTradeDay :" + iLastWeekLastTradeDay);					
				}
				
				int iLastWeekLastTradeDay1 = svo.getiLastWeekLastTradeDay();
				
				if(iLastWeekLastTradeDay1 < iListedDay) {
					logger.debug("상장일이 최근 1주일 이내입니다.");
					iLastWeekLastTradeDay1 = iListedDay;
					svo.setiLastWeekLastTradeDay(iListedDay);
				}
				
				for (Element trElement : trElements) {
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 9) {
						strTradeDate = StringUtils.defaultIfEmpty(tdElements.get(0).text(), "0");
						strEndPrice = StringUtils.defaultIfEmpty(tdElements.get(1).text(), "0");
						iEndPrice = Integer.parseInt(strEndPrice.replace(",", ""));

						iTradeDate = Integer.parseInt(strTradeDate.replace(".", ""));
						logger.debug("iTradeDate :" + iTradeDate);
						if (iTradeDate == iThisWeekLastTradeDay) {
							iThisWeekLastTradeDayEndPrice = iEndPrice;
							logger.debug("iThisWeekLastTradeDayEndPrice :" + iThisWeekLastTradeDayEndPrice);
						}

						if (iTradeDate == iLastWeekLastTradeDay1) {
							iLastWeekLastTradeDayEndPrice = iEndPrice;
							logger.debug("iLastWeekLastTradeDayEndPrice :" + iLastWeekLastTradeDayEndPrice);
							break;
						}
					}
				}

				int iVaryPrice = 0;
				iVaryPrice = iThisWeekLastTradeDayEndPrice - iLastWeekLastTradeDayEndPrice;
				logger.debug("iVaryPrice :" + iVaryPrice);
				strVaryPrice = df.format(iVaryPrice);

				String specialLetter = "";
				if (iVaryPrice < 0) {
					strSign = "-";
					specialLetter = "▼";
				} else if (iVaryPrice > 0) {
					strSign = "+";
					specialLetter = "▲";
				} else {
					strSign = "";
				}
				svo.setSign(strSign);
				svo.setSpecialLetter(specialLetter);

				float fVaryRatio = 0f;
				fVaryRatio = (float) iVaryPrice / (float) iLastWeekLastTradeDayEndPrice * 100;

				String thisWeekLastTradeDayEndPrice;
				String lastWeekLastTradeDayEndPrice;
				thisWeekLastTradeDayEndPrice = df.format(iThisWeekLastTradeDayEndPrice);
				lastWeekLastTradeDayEndPrice = df.format(iLastWeekLastTradeDayEndPrice);
				String strVaryRatio = ratioDf.format(fVaryRatio);

				svo.setLastWeekLastTradeDayEndPrice(lastWeekLastTradeDayEndPrice);
				svo.setThisWeekLastTradeDayEndPrice(thisWeekLastTradeDayEndPrice);
				svo.setiLastWeekLastTradeDayEndPrice(iLastWeekLastTradeDayEndPrice);
				svo.setiThisWeekLastTradeDayEndPrice(iThisWeekLastTradeDayEndPrice);
				svo.setVaryPrice(strVaryPrice);
				svo.setiVaryPrice(iVaryPrice);
				svo.setfVaryRatio(fVaryRatio);
				svo.setVaryRatio(strVaryRatio);

				logger.debug("thisWeekLastTradeDayEndPrice :" + thisWeekLastTradeDayEndPrice);
				logger.debug("lastWeekLastTradeDayEndPrice :" + lastWeekLastTradeDayEndPrice);
				logger.debug("strVaryPrice :" + strVaryPrice);
				logger.debug("fVaryRaio :" + fVaryRatio);
				logger.debug("strVaryRatio :" + strVaryRatio);
				logger.debug("strVaryRatio :" + strVaryRatio);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return svo;
		}

		public void writeFile() {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
				String strDate = sdf.format(new Date());

				StringBuilder sb1 = new StringBuilder();
				sb1.append("<html lang='ko'>\r\n");
				sb1.append("<head>\r\n");
				sb1.append("<style>\r\n");
				sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
				sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
				sb1.append("</style>\r\n");
				sb1.append("</head>\r\n");
				sb1.append("<body>\r\n");
				sb1.append("\t<div><font size=5>").append(strYMD).append(" ")
						.append(strDailyOrWeekly + " " + marketType + " 한주간 주가 등락율").append("</font></div>\r\n");

				sb1.append("\t<div><font size=3>").append(strDailyOrWeekly + " 한주간 주가 등락율").append("</font><div>\r\n");
				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>일주전가<br>"+iLastWeekLastTradeDay+"</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가<br>"+iThisWeekLastTradeDay+"</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락가</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</th>\r\n");

				sb1.append("</tr>\r\n");

				int cnt = 1;


				for (int i = 0; i < stockList.size(); i++) {
					// 코스피 주간(Weekly) 외인,기관 양매수 거래대금
					StockVO svo1 = stockList.get(i);

					sb1.append("<tr>\r\n");
					// 순매수
					String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url1 + "' target='_new'>" + svo1.getStockName() + "</a></td>\r\n");

					String lastWeekLastTradeDayEndPrice = svo1.getLastWeekLastTradeDayEndPrice();
					String thisWeekLastTradeDayEndPrice = svo1.getThisWeekLastTradeDayEndPrice();
					
					String sign = StringUtils.defaultIfEmpty(svo1.getSign(), "");
					String specialLetter = StringUtils.defaultIfEmpty(svo1.getSpecialLetter(), "");
					String curPrice1FontColor = "";
					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
							|| specialLetter.startsWith("+")) {
						curPrice1FontColor = "color:red";
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
							|| specialLetter.startsWith("-")) {
						curPrice1FontColor = "color:blue";
					} else {
						curPrice1FontColor = "color:black";
					}
					sb1.append("<td style='text-align:right;" + curPrice1FontColor + "'>"
							+ lastWeekLastTradeDayEndPrice + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + curPrice1FontColor + "'>"
							+ thisWeekLastTradeDayEndPrice + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + curPrice1FontColor + "'>"
							+ svo1.getVaryPrice() + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + curPrice1FontColor + "'>"
							+ svo1.getVaryRatio() + "%</td>\r\n");

					sb1.append("</tr>\r\n");
				}
				sb1.append("</table>\r\n");

				sb1.append("</body>\r\n");
				sb1.append("</html>\r\n");
				FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strDate + "_" + strDailyOrWeekly
						+ marketType + "_한주간_주가_등락율" + ".html");
				fw.write(sb1.toString());
				fw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
			}
		}
	}
}
