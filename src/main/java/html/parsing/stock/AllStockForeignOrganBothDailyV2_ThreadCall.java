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
import html.parsing.stock.util.DataSort.ForeignOrganTradingAmountAscCompare;
import html.parsing.stock.util.DataSort.ForeignOrganTradingAmountDescCompare;
import html.parsing.stock.util.StockUtil;

public class AllStockForeignOrganBothDailyV2_ThreadCall {

	AllStockForeignOrganBothDailyV2_Thread thread1 = null;
	AllStockForeignOrganBothDailyV2_Thread thread2 = null;

	int iFirstDayOfWeek;
	int iLastDayOfWeek;

	List<StockVO> kospiForeignDescList = new ArrayList<StockVO>();
	List<StockVO> kospiForeignAscList = new ArrayList<StockVO>();
	List<StockVO> kospiOrganDescList = new ArrayList<StockVO>();
	List<StockVO> kospiOrganAscList = new ArrayList<StockVO>();

	List<StockVO> kosdaqForeignDescList = new ArrayList<StockVO>();
	List<StockVO> kosdaqForeignAscList = new ArrayList<StockVO>();
	List<StockVO> kosdaqOrganDescList = new ArrayList<StockVO>();
	List<StockVO> kosdaqOrganAscList = new ArrayList<StockVO>();

	// 코스피 일간(Daily) 외인,기관 양매수 거래대금
	List<StockVO> kospiAllStockBuyList = new ArrayList<StockVO>();
	// 코스피 일간(Daily) 외인,기관 양매도 거래대금
	List<StockVO> kospiAllStockSellList = new ArrayList<StockVO>();
	// 코스닥 일간(Daily) 외인,기관 양매수 거래대금
	List<StockVO> kosdaqAllStockBuyList = new ArrayList<StockVO>();
	// 코스닥 일간(Daily) 외인,기관 양매도 거래대금
	List<StockVO> kosdaqAllStockSellList = new ArrayList<StockVO>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganBothDailyV2_ThreadCall();
	}

	AllStockForeignOrganBothDailyV2_ThreadCall() {
		// 주간 거래일을 알아낸다.
//		getFirstLastDayOfWeek();

		thread1 = new AllStockForeignOrganBothDailyV2_Thread("코스피", iFirstDayOfWeek, iLastDayOfWeek);
		thread1.start();

		thread2 = new AllStockForeignOrganBothDailyV2_Thread("코스닥", iFirstDayOfWeek, iLastDayOfWeek);
		thread2.start();

		System.out.println("getThread1State :" + getThread1State());
		System.out.println("getThread2State :" + getThread2State());
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
		cal.setTime(date);
		cal.add(Calendar.DATE, 1 - cal.get(Calendar.DAY_OF_WEEK));
		String strFirstDayOfWeek = simpleDateFormat.format(cal.getTime());
		iFirstDayOfWeek = Integer.parseInt(strFirstDayOfWeek);
		System.out.println("첫번째 요일(일요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));
		cal.setTime(date);
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
		String strLastDayOfWeek = simpleDateFormat.format(cal.getTime());
		iLastDayOfWeek = Integer.parseInt(strLastDayOfWeek);
		System.out.println("마지막 요일(토요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));
	}

	public class AllStockForeignOrganBothDailyV2_Thread extends Thread {

		final String USER_HOME = System.getProperty("user.home");
		private Logger logger = LoggerFactory.getLogger(AllStockForeignOrganBothDailyV2_Thread.class);

		String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
		int iYear = Integer.parseInt(strYear);

		// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
		// Locale.KOREAN).format(new Date());
		String strYMD = "";
		DecimalFormat df = new DecimalFormat("#,##0");

		List<StockVO> foreignDescList = new ArrayList<StockVO>();
		List<StockVO> foreignAscList = new ArrayList<StockVO>();
		List<StockVO> organDescList = new ArrayList<StockVO>();
		List<StockVO> organAscList = new ArrayList<StockVO>();

		int iFirstDayOfWeek;
		int iLastDayOfWeek;
		String strDailyOrWeekly = "일간(Daily) ";
		int extractDayCount = 1;
		String marketType = "";
		long start = 0;
		long end = 0;

		AllStockForeignOrganBothDailyV2_Thread() {
			// test();
			real();
		}

		AllStockForeignOrganBothDailyV2_Thread(String marketType, int iFirstDayOfWeek, int iLastDayOfWeek) {
			this.marketType = marketType;
			this.iFirstDayOfWeek = iFirstDayOfWeek;
			this.iLastDayOfWeek = iLastDayOfWeek;
		}

		void test() {
			// List<StockVO> kospiStockList = readOne("005930", "삼성전자");
			List<StockVO> kospiStockList = readOne("034120", "SBS");
			kospiStockList = getAllStockInfo(kospiStockList);

			kospiStockList = getAllStockTrade(kospiStockList);
			// 코스피 일간(Daily) 외인,기관 양매수 거래대금
			kospiAllStockBuyList = getAllStockList(kospiStockList, "buy");
			// 코스피 일간(Daily) 외인,기관 양매도 거래대금
			kospiAllStockSellList = getAllStockList(kospiStockList, "sell");
			if (kospiAllStockBuyList.size() > 0) {
				writeFile();
			}
		}

		void real() {
			start();
		}

		public void run() {
			start = System.currentTimeMillis();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date startDate = new Date(start);
			logger.debug("start time:" + sdf.format(startDate));

			logger.debug("실행시간 : " + (start) / 1000 + "초");

			// 모든 주식 정보를 조회한다.
			List<StockVO> stockList = StockUtil.readStockCodeNameList(marketType);
			stockList = getAllStockInfo(stockList);
			System.out.println("stockList.size :" + stockList.size());

			stockList = getAllStockTrade(stockList);
			if (marketType.equals("코스피")) {
				// 코스피 일간(Daily) 외인,기관 양매수 거래대금
				kospiAllStockBuyList = getAllStockList(stockList, "buy");
				// 코스피 일간(Daily) 외인,기관 양매도 거래대금
				kospiAllStockSellList = getAllStockList(stockList, "sell");
				System.out.println("1.kospiAllStockBuyList.size :" + kospiAllStockBuyList.size());
				System.out.println("1.kospiAllStockSellList.size :" + kospiAllStockSellList.size());
				System.out.println("1.kosdaqAllStockBuyList.size :" + kosdaqAllStockBuyList.size());
				System.out.println("1.kosdaqAllStockSellList.size :" + kosdaqAllStockSellList.size());
				// 코스피 외인,기관 양매수,양매도 거래대금순 정렬
				Collections.sort(kospiAllStockBuyList, new ForeignOrganTradingAmountDescCompare());
				Collections.sort(kospiAllStockSellList, new ForeignOrganTradingAmountAscCompare());
			} else {
				// 코스닥 일간(Daily) 외인,기관 양매수 거래대금
				kosdaqAllStockBuyList = getAllStockList(stockList, "buy");
				// 코스닥 일간(Daily) 외인,기관 양매도 거래대금
				kosdaqAllStockSellList = getAllStockList(stockList, "sell");
				System.out.println("2.kospiAllStockBuyList.size :" + kospiAllStockBuyList.size());
				System.out.println("2.kospiAllStockSellList.size :" + kospiAllStockSellList.size());
				System.out.println("2.kosdaqAllStockBuyList.size :" + kosdaqAllStockBuyList.size());
				System.out.println("2.kosdaqAllStockSellList.size :" + kosdaqAllStockSellList.size());
				// 코스닥 외인,기관 양매수,양매도 거래대금순 정렬
				Collections.sort(kosdaqAllStockBuyList, new ForeignOrganTradingAmountDescCompare());
				Collections.sort(kosdaqAllStockSellList, new ForeignOrganTradingAmountAscCompare());
			}

			System.out.println("getThread1State :" + getThread1State());
			System.out.println("getThread2State :" + getThread2State());
			if (getThread1State() == Thread.State.TERMINATED && getThread2State() == Thread.State.RUNNABLE) {
				writeFile();

				end = System.currentTimeMillis();
				Date endDate = new Date(end);
				logger.debug("end time:" + sdf.format(endDate));

				long timeElapsed = end - start;
				logger.debug("실행시간 : " + (end - start) / 1000 + "초");

				int second = (int) timeElapsed / 1000 % 60;
				int minute = (int) timeElapsed / (1000 * 60) % 60;
				int hour = (int) timeElapsed / (1000 * 60 * 60);

				logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
			}
		}

		public List<StockVO> readOne(String stockCode, String stockName) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			StockVO svo = new StockVO();
			svo.setStockCode(stockCode);
			svo.setStockName(stockName);
			stocks.add(svo);
			return stocks;
		}

		public List<StockVO> getAllStockInfo(List<StockVO> stockList) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			for (StockVO svo : stockList) {
				stockCode = svo.getStockCode();
				stockName = svo.getStockName();
				System.out.println("_______________________________________");
				System.out.println(cnt + "." + stockCode + "\t" + stockName);
				System.out.println("_______________________________________");

				svo = getStockInfo(stockCode, stockName);
				// svo = getStockTrade(svo);

				if (svo != null) {
					stocks.add(svo);
				}
				cnt++;
			}
			return stocks;
		}

		public StockVO getStockInfo(String strStockCode, String strStockName) {
			Document doc;
			StockVO svo = new StockVO();
			svo.setStockCode(strStockCode);
			svo.setStockName(strStockName);
			try {
				// 종합정보
				doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();

				Elements dates = doc.select(".date");
				if (dates != null) {
					if (dates.size() > 0) {
						Element date = dates.get(0);
						strYMD = date.ownText();
						strYMD = date.childNode(0).toString().trim();
						strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
					}
				}
				Elements new_totalinfos = doc.select(".new_totalinfo");

				if (new_totalinfos == null || new_totalinfos.isEmpty()) {
					return svo;
				}

				Element new_totalinfo = new_totalinfos.get(0);
				Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
				Element blind = new_totalinfo_doc.select(".blind").get(0);

				if (blind == null) {
					return svo;
				}

				Elements edds = blind.select("dd");

				String strSpecialLetter = "";
				String strSign = "";
				String strCurPrice = "";
				String strVaryPrice = "";
				String strVaryRatio = "";

				int iCurPrice = 0;
				int iVaryPrice = 0;

				for (int i = 0; i < edds.size(); i++) {
					Element dd = edds.get(i);
					String text = dd.text();
					System.out.println("data:" + text);

					if (text.startsWith("현재가")) {
						System.out.println("data1:" + dd.text());
						text = text.replaceAll("플러스", "+");
						text = text.replaceAll("마이너스", "-");
						text = text.replaceAll("상승", "▲");
						text = text.replaceAll("하락", "▼");
						text = text.replaceAll("퍼센트", "%");

						String txts[] = text.split(" ");
						strCurPrice = txts[1];
						svo.setCurPrice(strCurPrice);
						svo.setiCurPrice(Integer
								.parseInt(StringUtils.defaultIfEmpty(svo.getCurPrice(), "0").replaceAll(",", "")));
						iCurPrice = svo.getiCurPrice();

						// 특수문자
						strSpecialLetter = txts[3].replaceAll("보합", "");
						svo.setSpecialLetter(strSpecialLetter);

						strVaryPrice = txts[4];
						svo.setVaryPrice(strVaryPrice);
						svo.setiVaryPrice(Integer
								.parseInt(StringUtils.defaultIfEmpty(svo.getVaryPrice(), "0").replaceAll(",", "")));
						iVaryPrice = svo.getiVaryPrice();

						// +- 부호
						strSign = txts[5];
						svo.setSign(strSign);
						System.out.println("txts.length:" + txts.length);
						if (txts.length == 7) {
							svo.setVaryRatio(txts[5] + txts[6]);
						} else if (txts.length == 8) {
							svo.setVaryRatio(txts[5] + txts[6] + txts[7]);
						}
						strVaryRatio = svo.getVaryRatio();
						svo.setfVaryRatio(Float.parseFloat(strVaryRatio.replaceAll("%", "")));
						System.out.println("상승률:" + svo.getVaryRatio());
					}

					if (text.startsWith("전일가")) {
						svo.setBeforePrice(text.split(" ")[1]);
						svo.setiBeforePrice(Integer.parseInt(svo.getBeforePrice().replaceAll(",", "")));
					}
					if (text.startsWith("시가")) {
						svo.setStartPrice(text.split(" ")[1]);
						svo.setiStartPrice(Integer.parseInt(svo.getStartPrice().replaceAll(",", "")));
					}
					if (text.startsWith("고가")) {
						svo.setHighPrice(text.split(" ")[1]);
						svo.setiHighPrice(Integer.parseInt(svo.getHighPrice().replaceAll(",", "")));
					}
					if (text.startsWith("상한가")) {
						svo.setMaxPrice(text.split(" ")[1]);
						svo.setiMaxPrice(Integer.parseInt(svo.getMaxPrice().replaceAll(",", "")));
					}
					if (text.startsWith("저가")) {
						svo.setLowPrice(text.split(" ")[1]);
						svo.setiLowPrice(Integer.parseInt(svo.getLowPrice().replaceAll(",", "")));
					}
					if (text.startsWith("하한가")) {
						svo.setMinPrice(text.split(" ")[1]);
						svo.setiMinPrice(Integer.parseInt(svo.getMinPrice().replaceAll(",", "")));
					}
					if (text.startsWith("거래량")) {
						svo.setTradingVolume(text.split(" ")[1]);
						svo.setlTradingVolume(Long.parseLong(svo.getTradingVolume().replaceAll(",", "")));
					}
					if (text.startsWith("거래대금") || text.startsWith("거래금액")) {
						svo.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
						svo.setlTradingAmount(Integer
								.parseInt(StringUtils.defaultIfEmpty(svo.getTradingAmount().replaceAll(",", ""), "0")));
					}
				}

				String upDown = doc.select(".no_exday").get(0).select("em span").get(0).text();
				if (upDown.equals("상한가")) {
					strSpecialLetter = "↑";
				} else if (upDown.equals("하한가")) {
					strSpecialLetter = "↓";
				}
				svo.setSpecialLetter(strSpecialLetter);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return svo;
		}

		public List<StockVO> getAllStockTrade(List<StockVO> stockList) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			for (StockVO svo : stockList) {
				stockCode = svo.getStockCode();
				stockName = svo.getStockName();
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
				// =========================================================
				// 투자자별 매매동향 - 외인 보유주수, 보유율
				// https://finance.naver.com/item/frgn.nhn?code=102460&page=1
				// https://finance.naver.com/item/frgn.nhn?code=102460&page=2
				System.out.println("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode);
				Document doc = Jsoup.connect("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode).get();

				String foreignTradingVolume = "";
				String organTradingVolume = "";
				long lForeignTradingVolume = 0;
				long lOrganTradingVolume = 0;
				long lForeignOrganTradingVolume = 0L;

				long lForeignTradeAmount = 0;
				long lOrganTradeAmount = 0;
				long lForeignOrganTradingAmount = 0L;

				Elements type2Elements = doc.select(".type2");
				Element type2Element = type2Elements.get(1);
				System.out.println("type2:" + type2Element);
				// 외인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
				// tr
				Elements trElements = type2Element.select("tr");
				int dayCnt = 0;

				// 기준가,표준가 산정
				long standardPrice = 0;
				String strSign = "";
				String strEndPrice = "";
				String strVaryPrice = "";
				int iEndPrice = 0;
				int iVaryPrice = 0;

				for (Element trElement : trElements) {
					// td
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 9) {
						if (dayCnt == extractDayCount) {
							break;
						}
						// 기관순매매량
						organTradingVolume = tdElements.get(5).text();
						// 외인순매매량
						foreignTradingVolume = tdElements.get(6).text();

						System.out.println("organTradingVolume :" + organTradingVolume);
						System.out.println("foreignTradingVolume :" + foreignTradingVolume);

						if (!((organTradingVolume.startsWith("+") && foreignTradingVolume.startsWith("+"))
								|| ((organTradingVolume.startsWith("-") && foreignTradingVolume.startsWith("-"))))) {
							return null;
						}

						strEndPrice = StringUtils.defaultIfEmpty(tdElements.get(1).text(), "0");
						strVaryPrice = StringUtils.defaultIfEmpty(tdElements.get(2).text(), "0");

						iEndPrice = Integer.parseInt(strEndPrice.replace(",", ""));
						iVaryPrice = Integer.parseInt(strVaryPrice.replace(",", ""));

						String strVaryRate = tdElements.get(3).text();
						if (strVaryRate.startsWith("+")) {
							strSign = "+";
						} else if (strVaryRate.startsWith("-")) {
							strSign = "-";
						}
						// 기준가,표준가 산정
						if (strSign.equals("+")) {
							standardPrice = iEndPrice - iVaryPrice / 2;
						}
						if (strSign.equals("-")) {
							standardPrice = iEndPrice + iVaryPrice / 2;
						}
						if (strSign.equals("")) {
							standardPrice = iEndPrice;
						}

						// 기관순매매량
						organTradingVolume = tdElements.get(5).text();
						organTradingVolume = organTradingVolume.replace(",", "");
						if (organTradingVolume.matches(".*[0-9]+.*")) {
							long tempLOrganTradingVolume = Long.parseLong(organTradingVolume);
							System.out.println("기관:" + standardPrice + " X " + organTradingVolume + "="
									+ (standardPrice * tempLOrganTradingVolume));
							lOrganTradingVolume += tempLOrganTradingVolume;
							lOrganTradeAmount += tempLOrganTradingVolume * standardPrice;
						}
						// 외인순매매량
						foreignTradingVolume = tdElements.get(6).text();
						foreignTradingVolume = foreignTradingVolume.replace(",", "");
						if (foreignTradingVolume.matches(".*[0-9]+.*")) {
							long tempLForeignTradingVolume = Long.parseLong(foreignTradingVolume);
							System.out.println("\t\t외인:" + standardPrice + " X " + foreignTradingVolume + "="
									+ (standardPrice * tempLForeignTradingVolume));
							lForeignTradingVolume += tempLForeignTradingVolume;
							lForeignTradeAmount += tempLForeignTradingVolume * standardPrice;
						}

						dayCnt++;
					}
				}

				foreignTradingVolume = df.format(lForeignTradingVolume);
				organTradingVolume = df.format(lOrganTradingVolume);

				foreignTradingVolume = StringUtils.defaultIfEmpty(foreignTradingVolume, "0");
				organTradingVolume = StringUtils.defaultIfEmpty(organTradingVolume, "0");

				System.out.println("foreignTradingVolume:" + foreignTradingVolume);
				System.out.println("organTradingVolume:" + organTradingVolume);

				lForeignOrganTradingVolume = lForeignTradingVolume + lOrganTradingVolume;

				System.out.println("iForeignTradingVolume:" + lForeignTradingVolume);
				System.out.println("iOrganTradingVolume:" + lOrganTradingVolume);
				System.out.println("iForeignOrganTradingVolume:" + lForeignOrganTradingVolume);

				// 금액은 만 단위
				lForeignTradeAmount = lForeignTradeAmount / 10000;
				lOrganTradeAmount = lOrganTradeAmount / 10000;
				lForeignOrganTradingAmount = lForeignTradeAmount + lOrganTradeAmount;

				System.out.println("외인거래금액 :" + lForeignTradeAmount);
				System.out.println("기관거래금액 :" + lOrganTradeAmount);

				svo.setlForeignTradingAmount(lForeignTradeAmount);
				svo.setlOrganTradingAmount(lOrganTradeAmount);
				svo.setlForeignOrganTradingAmount(lForeignOrganTradingAmount);

				String foreignTradeAmount = df.format(lForeignTradeAmount);
				String organTradeAmount = df.format(lOrganTradeAmount);
				String foreignOrganTradingVolume = df.format(lForeignOrganTradingVolume);
				String foreignOrganTradingAmount = df.format(lForeignOrganTradingAmount);

				svo.setForeignTradingAmount(foreignTradeAmount);
				svo.setOrganTradingAmount(organTradeAmount);
				svo.setForeignOrganTradingAmount(foreignOrganTradingAmount);

				svo.setForeignTradingVolume(foreignTradingVolume);
				svo.setOrganTradingVolume(organTradingVolume);
				svo.setForeignOrganTradingVolume(foreignOrganTradingVolume);

				svo.setlForeignTradingVolume(lForeignTradingVolume);
				svo.setlOrganTradingVolume(lOrganTradingVolume);
				svo.setlForeignOrganTradingVolume(lForeignOrganTradingVolume);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return svo;
		}

		public List<StockVO> getAllStockList(List<StockVO> stockList, String buyOrSell) {
			List<StockVO> stocks = new ArrayList<StockVO>();

			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			for (StockVO svo : stockList) {
				stockCode = svo.getStockCode();
				stockName = svo.getStockName();
				System.out.println("_______________________________________");
				System.out.println(cnt + "." + stockCode + "\t" + stockName);
				System.out.println("_______________________________________");

				long lForeignTradingAmount = svo.getlForeignTradingAmount();
				long lOrganTradingAmount = svo.getlOrganTradingAmount();
				System.out.println("lForeignTradingAmount :" + lForeignTradingAmount);
				System.out.println("lOrganTradingAmount :" + lOrganTradingAmount);

				if (buyOrSell.equals("buy")) {
					if (lForeignTradingAmount > 0 && lOrganTradingAmount > 0) {
						stocks.add(svo);
					}
				} else {
					if (lForeignTradingAmount < 0 && lOrganTradingAmount < 0) {
						stocks.add(svo);
					}
				}
				cnt++;
			}
			return stocks;
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
						.append(strDailyOrWeekly + "코스피,코스닥 외인,기관 양매수,양매도 거래대금").append("</font></div>\r\n");

				sb1.append("\t<div><font size=3>").append(strDailyOrWeekly + "코스피 외인,기관 양매수,양매도 거래대금")
						.append("</font><div>\r\n");
				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
				sb1.append(
						"<th colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(만원)</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</th>\r\n");

				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
				sb1.append(
						"<th colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(만원)</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</th>\r\n");
				sb1.append("</tr>\r\n");

				sb1.append("<tr>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</th>\r\n");
				sb1.append("</tr>\r\n");

				int cnt = 1;

				int kospiAllStockBuyListSize = kospiAllStockBuyList.size();
				int kospiAllStockSellListSize = kospiAllStockSellList.size();
				int listGap1 = Math.abs(kospiAllStockBuyListSize - kospiAllStockSellListSize);

				if (kospiAllStockBuyListSize > kospiAllStockSellListSize) {
					for (int i = 0; i < listGap1; i++) {
						kospiAllStockBuyList.add(new StockVO());
					}
				} else {
					for (int i = 0; i < listGap1; i++) {
						kospiAllStockBuyList.add(new StockVO());
					}
				}

				System.out.println("kospiAllStockBuyListSize1 :" + kospiAllStockBuyListSize);
				System.out.println("kospiAllStockSellListSize1 :" + kospiAllStockSellListSize);

				for (int i = 0; i < kospiAllStockBuyList.size(); i++) {
					// 코스피 일간(Daily) 외인,기관 양매수 거래대금
					StockVO svo1 = kospiAllStockBuyList.get(i);
					// 코스피 일간(Daily) 외인,기관 양매도 거래대금
					StockVO svo2 = kospiAllStockSellList.get(i);

					sb1.append("<tr>\r\n");
					// 순매수
					String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url1 + "' target='_new'>" + svo1.getStockName() + "</a></td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(svo1.getSpecialLetter(), "");
					String strCurPrice = svo1.getCurPrice();
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
							+ strCurPrice + "</td>\r\n");

					long lForeignTradingAmount1 = svo1.getlForeignTradingAmount();
					String amount1FontColor = "color:metal";
					if (lForeignTradingAmount1 < 0) {
						amount1FontColor = "color:blue";
					} else if (lForeignTradingAmount1 > 0) {
						amount1FontColor = "color:red";
					}
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>"
							+ svo1.getForeignTradingAmount() + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>" + svo1.getOrganTradingAmount()
							+ "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>"
							+ svo1.getForeignOrganTradingAmount() + "</td>\r\n");

					// 순매도
					String url2 = "http://finance.naver.com/item/main.nhn?code=" + svo2.getStockCode();
					sb1.append("<td><a href='" + url2 + "' target='_new'>" + svo2.getStockName() + "</a></td>\r\n");

					String specialLetter2 = StringUtils.defaultIfEmpty(svo2.getSpecialLetter(), "");
					String strCurPrice2 = svo2.getCurPrice();
					String curPrice2FontColor = "";
					if (specialLetter2.startsWith("↑") || specialLetter2.startsWith("▲")
							|| specialLetter2.startsWith("+")) {
						curPrice2FontColor = "color:red";
					} else if (specialLetter2.startsWith("↓") || specialLetter2.startsWith("▼")
							|| specialLetter2.startsWith("-")) {
						curPrice2FontColor = "color:blue";
					} else {
						curPrice2FontColor = "color:black";
					}
					sb1.append("<td style='text-align:right;" + curPrice2FontColor + "'>" + strCurPrice2 + "</td>\r\n");

					long lForeignTradingAmount2 = svo2.getlForeignTradingAmount();
					String amount2FontColor = "color:metal";
					if (lForeignTradingAmount2 < 0) {
						amount2FontColor = "color:blue";
					} else if (lForeignTradingAmount2 > 0) {
						amount2FontColor = "color:red";
					}
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>"
							+ svo2.getForeignTradingAmount() + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>" + svo2.getOrganTradingAmount()
							+ "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>"
							+ svo2.getForeignOrganTradingAmount() + "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
				sb1.append("</table>\r\n");

				sb1.append("<br><br>\r\n");

				sb1.append("\t<div><font size=3>").append(strDailyOrWeekly + "코스닥 외인,기관 양매수,양매도 거래대금")
						.append("</font></div>");
				sb1.append("<table>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
				sb1.append(
						"<th colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(만원)</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</th>\r\n");

				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
				sb1.append(
						"<th colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(만원)</th>\r\n");
				sb1.append(
						"<th rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</th>\r\n");
				sb1.append("</tr>\r\n");

				sb1.append("<tr>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</th>\r\n");
				sb1.append(
						"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</th>\r\n");
				sb1.append("</tr>\r\n");

				cnt = 1;

				int kosdaqAllStockBuyListSize = kosdaqAllStockBuyList.size();
				int kosdaqAllStockSellListSize = kosdaqAllStockSellList.size();
				int listGap2 = Math.abs(kosdaqAllStockBuyListSize - kosdaqAllStockSellListSize);
				if (kosdaqAllStockBuyListSize > kosdaqAllStockSellListSize) {
					for (int i = 0; i < listGap2; i++) {
						kosdaqAllStockSellList.add(new StockVO());
					}
				} else {
					for (int i = 0; i < listGap2; i++) {
						kosdaqAllStockBuyList.add(new StockVO());
					}
				}
				System.out.println("kosdaqAllStockBuyListSize1 :" + kosdaqAllStockBuyListSize);
				System.out.println("kosdaqAllStockSellListSize1 :" + kosdaqAllStockSellListSize);

				for (int i = 0; i < kosdaqAllStockBuyList.size(); i++) {
					// 코스닥 일간(Daily) 외인,기관 양매수 거래대금
					StockVO svo1 = kosdaqAllStockBuyList.get(i);
					// 코스닥 일간(Daily) 외인,기관 양매도 거래대금
					StockVO svo2 = kosdaqAllStockSellList.get(i);

					sb1.append("<tr>\r\n");
					// 순매수
					String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url1 + "' target='_new'>" + svo1.getStockName() + "</a></td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(svo1.getSpecialLetter(), "");
					String strCurPrice = svo1.getCurPrice();
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
							+ strCurPrice + "</td>\r\n");

					long lForeignTradingAmount1 = svo1.getlForeignTradingAmount();
					String amount1FontColor = "color:metal";
					if (lForeignTradingAmount1 < 0) {
						amount1FontColor = "color:blue";
					} else if (lForeignTradingAmount1 > 0) {
						amount1FontColor = "color:red";
					}
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>"
							+ svo1.getForeignTradingAmount() + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>" + svo1.getOrganTradingAmount()
							+ "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount1FontColor + ";'>"
							+ svo1.getForeignOrganTradingAmount() + "</td>\r\n");

					// 순매도
					String url2 = "http://finance.naver.com/item/main.nhn?code=" + svo2.getStockCode();
					sb1.append("<td><a href='" + url2 + "' target='_new'>" + svo2.getStockName() + "</a></td>\r\n");

					String specialLetter2 = StringUtils.defaultIfEmpty(svo2.getSpecialLetter(), "");
					String strCurPrice2 = svo2.getCurPrice();
					String curPrice2FontColor = "";
					if (specialLetter2.startsWith("↑") || specialLetter2.startsWith("▲")
							|| specialLetter2.startsWith("+")) {
						curPrice2FontColor = "color:red";
					} else if (specialLetter2.startsWith("↓") || specialLetter2.startsWith("▼")
							|| specialLetter2.startsWith("-")) {
						curPrice2FontColor = "color:blue";
					} else {
						curPrice2FontColor = "color:black";
					}
					sb1.append("<td style='text-align:right;" + curPrice2FontColor + "'>" + strCurPrice2 + "</td>\r\n");

					long lForeignTradingAmount2 = svo2.getlForeignTradingAmount();
					String amount2FontColor = "color:metal";
					if (lForeignTradingAmount2 < 0) {
						amount2FontColor = "color:blue";
					} else if (lForeignTradingAmount2 > 0) {
						amount2FontColor = "color:red";
					}
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>"
							+ svo2.getForeignTradingAmount() + "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>" + svo2.getOrganTradingAmount()
							+ "</td>\r\n");
					sb1.append("<td style='text-align:right;" + amount2FontColor + ";'>"
							+ svo2.getForeignOrganTradingAmount() + "</td>\r\n");

					sb1.append("</tr>\r\n");
				}
				sb1.append("</table>\r\n");

				sb1.append("</body>\r\n");
				sb1.append("</html>\r\n");
				FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strDate + "_" + strDailyOrWeekly
						+ "코스피,코스닥 외인,기관 양매수,양매도 거래대금" + ".html");
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
