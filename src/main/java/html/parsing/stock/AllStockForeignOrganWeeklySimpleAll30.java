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
import html.parsing.stock.util.DataSort.ForeignTradingAmountAscCompare;
import html.parsing.stock.util.DataSort.ForeignTradingAmountDescCompare;
import html.parsing.stock.util.DataSort.OrganTradingAmountAscCompare;
import html.parsing.stock.util.DataSort.OrganTradingAmountDescCompare;
import html.parsing.stock.util.StockUtil;

public class AllStockForeignOrganWeeklySimpleAll30 {

	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganWeeklySimpleAll30.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static DecimalFormat df = new DecimalFormat("#,##0.####");

	List<StockVO> kospiForeignDescList = new ArrayList();
	List<StockVO> kospiForeignAscList = new ArrayList();
	List<StockVO> kospiOrganDescList = new ArrayList();
	List<StockVO> kospiOrganAscList = new ArrayList();

	List<StockVO> kosdaqForeignDescList = new ArrayList();
	List<StockVO> kosdaqForeignAscList = new ArrayList();
	List<StockVO> kosdaqOrganDescList = new ArrayList();
	List<StockVO> kosdaqOrganAscList = new ArrayList();

	static int iFirstDayOfWeek;
	static int iLastDayOfWeek;
	String strDailyOrWeekly = "주간(Weekly) ";
	static int extractDayCount = 5;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganWeeklySimpleAll30();
	}

	AllStockForeignOrganWeeklySimpleAll30() {
		// 주간 거래일을 알아낸다.
		getFirstLastDayOfWeek();

		// test();
		long start = System.currentTimeMillis();

		real();

		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
	}

	void test() {
		List<StockVO> kospiStockList = readOne("005930", "삼성전자");
		kospiStockList = getAllStockInfo(kospiStockList);
		Collections.sort(kospiStockList, new ForeignTradingAmountAscCompare());
		writeFile(kospiStockList, kospiStockList, "코스피", "외국인", "거래금액");
	}

	void real() {
		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		kospiForeignDescList.addAll(kospiAllStockList);
		kospiForeignAscList.addAll(kospiAllStockList);
		kospiOrganDescList.addAll(kospiAllStockList);
		kospiOrganAscList.addAll(kospiAllStockList);

		// 코스피 외국인 거래대금순 정렬
		Collections.sort(kospiForeignDescList, new ForeignTradingAmountDescCompare());
		Collections.sort(kospiForeignAscList, new ForeignTradingAmountAscCompare());
		// 코스피 기관 거래대금순 정렬
		Collections.sort(kospiOrganDescList, new OrganTradingAmountDescCompare());
		Collections.sort(kospiOrganAscList, new OrganTradingAmountAscCompare());
		writeFile("코스피");

		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		kosdaqForeignDescList.addAll(kosdaqAllStockList);
		kosdaqForeignAscList.addAll(kosdaqAllStockList);
		kosdaqOrganDescList.addAll(kosdaqAllStockList);
		kosdaqOrganAscList.addAll(kosdaqAllStockList);

		// 코스닥 기관 거래대금순 정렬
		Collections.sort(kosdaqForeignDescList, new ForeignTradingAmountDescCompare());
		Collections.sort(kosdaqForeignAscList, new ForeignTradingAmountAscCompare());
		// 코스닥 기관 거래대금순 정렬
		Collections.sort(kosdaqOrganDescList, new OrganTradingAmountDescCompare());
		Collections.sort(kosdaqOrganAscList, new OrganTradingAmountAscCompare());
		writeFile("코스닥");

	}

	public static List<StockVO> readOne(String stockCode, String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO svo = new StockVO();
		svo.setStockCode(stockCode);
		svo.setStockName(stockName);
		stocks.add(svo);
		return stocks;
	}

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
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
			svo = getStockTrade(svo);

			if (svo != null) {
				stocks.add(svo);
			}
			cnt++;
		}
		return stocks;
	}

	public static StockVO getStockInfo(String strStockCode, String strStockName) {
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
					svo.setiCurPrice(
							Integer.parseInt(StringUtils.defaultIfEmpty(svo.getCurPrice(), "0").replaceAll(",", "")));
					iCurPrice = svo.getiCurPrice();

					// 특수문자
					strSpecialLetter = txts[3].replaceAll("보합", "");
					svo.setSpecialLetter(strSpecialLetter);

					strVaryPrice = txts[4];
					svo.setVaryPrice(strVaryPrice);
					svo.setiVaryPrice(
							Integer.parseInt(StringUtils.defaultIfEmpty(svo.getVaryPrice(), "0").replaceAll(",", "")));
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

	public static List<StockVO> getAllStockTrade(List<StockVO> stockList) {
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

	public static StockVO getStockTrade(StockVO svo) {
		try {
			String strStockCode = svo.getStockCode();
			// =========================================================
			// 투자자별 매매동향 - 외국인 보유주수, 보유율
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=1
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=2
			System.out.println("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode);
			Document doc = Jsoup.connect("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode).get();

			String foreignTradingVolume = "";
			String organTradingVolume = "";
			long lForeignTradingVolume = 0;
			long lOrganTradingVolume = 0;
			long lForeignOrganTradingVolume = 0L;

			double dForeignTradeAmount = 0;
			double dOrganTradeAmount = 0;
			double dForeignOrganTradingAmount = 0d;

			Elements type2Elements = doc.select(".type2");
			Element type2Element = type2Elements.get(1);
			System.out.println("type2:" + type2Element);
			// 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
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

//					if (!((organTradingVolume.startsWith("+") && foreignTradingVolume.startsWith("+"))
//							|| (organTradingVolume.startsWith("-") && foreignTradingVolume.startsWith("-")))) {
//						return null;
//					}
					String strTradeDay = StringUtils.defaultIfEmpty(tdElements.get(0).text(), "");
					strTradeDay = strTradeDay.replace(".", "");
					int iTradeDay = Integer.parseInt(strTradeDay);
					System.out.println("iTradeDay:" + iTradeDay);
					System.out.println("iFirstDayOfWeek:" + iFirstDayOfWeek);
					System.out.println("iLastDayOfWeek:" + iLastDayOfWeek);
					if (iTradeDay < iFirstDayOfWeek || iTradeDay > iLastDayOfWeek) {
						break;
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
						dOrganTradeAmount += tempLOrganTradingVolume * standardPrice;
					}
					// 외인순매매량
					foreignTradingVolume = tdElements.get(6).text();
					foreignTradingVolume = foreignTradingVolume.replace(",", "");
					if (foreignTradingVolume.matches(".*[0-9]+.*")) {
						long tempLForeignTradingVolume = Long.parseLong(foreignTradingVolume);
						System.out.println("\t\t외인:" + standardPrice + " X " + foreignTradingVolume + "="
								+ (standardPrice * tempLForeignTradingVolume));
						lForeignTradingVolume += tempLForeignTradingVolume;
						dForeignTradeAmount += tempLForeignTradingVolume * standardPrice;
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
			dForeignTradeAmount = dForeignTradeAmount / 100000000;
			dOrganTradeAmount = dOrganTradeAmount / 100000000;
			dForeignOrganTradingAmount = dForeignTradeAmount + dOrganTradeAmount;

			System.out.println("외인거래금액 :" + dForeignTradeAmount);
			System.out.println("기관거래금액 :" + dOrganTradeAmount);

			svo.setdForeignTradingAmount(dForeignTradeAmount);
			svo.setdOrganTradingAmount(dOrganTradeAmount);
			svo.setdForeignOrganTradingAmount(dForeignOrganTradingAmount);

			String foreignTradeAmount = df.format(dForeignTradeAmount);
			String organTradeAmount = df.format(dOrganTradeAmount);
			String foreignOrganTradingVolume = df.format(lForeignOrganTradingVolume);
			String foreignOrganTradingAmount = df.format(dForeignOrganTradingAmount);

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

	public void writeFile(List<StockVO> descList, List<StockVO> ascList, String title, String trader, String gubun1) {
		System.out.println("descList.size:" + descList.size());
		System.out.println("ascList.size:" + ascList.size());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH.mm.ss.SSS]", Locale.KOREAN);
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
			sb1.append("\t<font size=5>").append(strYMD).append(" ").append(title).append("</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' rowspan='2'>No.</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append(trader).append("순매수</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append(trader).append("순매도</th>\r\n");
			sb1.append("</tr>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매수량</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매수대금(억원)</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매도량</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매도대금(억원)</th>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (int i = 0; i < descList.size(); i++) {
				StockVO svo1 = descList.get(i);
				StockVO svo2 = ascList.get(i);
				sb1.append("<tr>\r\n");
				String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
				String url2 = "http://finance.naver.com/item/main.nhn?code=" + svo2.getStockCode();
				sb1.append("<td>").append(cnt++).append("</td>\r\n");
				sb1.append("<td><a href='").append(url1).append("' target='_new'>").append(svo1.getStockName())
						.append("</a></td>\r\n");

				if (trader.equals("외국인")) {
					sb1.append("<td style='text-align:right;'>").append(svo1.getForeignTradingVolume())
							.append("</td>\r\n");
					sb1.append("<td style='text-align:right;'>").append(svo1.getForeignTradingAmount())
							.append("</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right;'>").append(svo1.getOrganTradingVolume())
							.append("</td>\r\n");
					sb1.append("<td style='text-align:right;'>").append(svo1.getOrganTradingAmount())
							.append("</td>\r\n");
				}

				sb1.append("<td><a href='").append(url2).append("' target='_new'>").append(svo2.getStockName())
						.append("</a></td>\r\n");

				if (trader.equals("외국인")) {
					sb1.append("<td style='text-align:right;'>").append(svo2.getForeignTradingVolume())
							.append("</td>\r\n");
					sb1.append("<td style='text-align:right;'>").append(svo2.getForeignTradingAmount())
							.append("</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right;'>").append(svo2.getOrganTradingVolume())
							.append("</td>\r\n");
					sb1.append("<td style='text-align:right;'>").append(svo2.getOrganTradingAmount())
							.append("</td>\r\n");
				}
				sb1.append("</tr>\r\n");
				if (i == 29) {
					break;
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strDate + "_" + title + ".html");
			fw.write(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	public void writeFile(String marketType) {
		List<StockVO> foreignDescList = null;
		List<StockVO> foreignAscList = null;
		List<StockVO> organDescList = null;
		List<StockVO> organAscList = null;

		if (marketType.equals("kospi") || marketType.equals("코스피")) {
			foreignDescList = kospiForeignDescList;
			foreignAscList = kospiForeignAscList;
			organDescList = kospiOrganDescList;
			organAscList = kospiOrganAscList;
		} else {
			foreignDescList = kosdaqForeignDescList;
			foreignAscList = kosdaqForeignAscList;
			organDescList = kosdaqOrganDescList;
			organAscList = kosdaqOrganAscList;
		}
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
			sb1.append("\t<font size=4>").append(strYMD).append(strDailyOrWeekly + marketType + " 외국인,기관 순매매(거래대금순)")
					.append("</font><br/>");
			sb1.append("\t<font size=3>").append(" ").append(strDailyOrWeekly + marketType + " 외국인 순매매(거래대금순)")
					.append("</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' rowspan='2'>No.</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append("외국인").append("순매수</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append("외국인").append("순매도</th>\r\n");
			sb1.append("</tr>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매수대금(억원)</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매도대금(억원)</th>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (int i = 0; i < foreignDescList.size(); i++) {
				StockVO svo1 = foreignDescList.get(i);
				StockVO svo2 = foreignAscList.get(i);
				sb1.append("<tr>\r\n");
				String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
				String url2 = "http://finance.naver.com/item/main.nhn?code=" + svo2.getStockCode();
				sb1.append("<td>").append(cnt++).append("</td>\r\n");
				sb1.append("<td><a href='").append(url1).append("' target='_new'>").append(svo1.getStockName())
						.append("</a></td>\r\n");
				String sign1 = svo1.getSign();
				String fontColor1 = "";
				if (sign1.equals("+")) {
					fontColor1 = "color:red;";
				} else if (sign1.equals("-")) {
					fontColor1 = "color:blue;";
				} else {
					fontColor1 = "color:black;";
				}

				String sign2 = svo2.getSign();
				String fontColor2 = "";
				if (sign2.equals("+")) {
					fontColor2 = "color:red;";
				} else if (sign2.equals("-")) {
					fontColor2 = "color:blue;";
				} else {
					fontColor2 = "color:black;";
				}
				sb1.append("<td style='text-align:right;" + fontColor1 + "'>").append(svo1.getCurPrice())
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right;'>").append(svo1.getForeignTradingAmount()).append("</td>\r\n");

				sb1.append("<td><a href='").append(url2).append("' target='_new'>").append(svo2.getStockName())
						.append("</a></td>\r\n");

				sb1.append("<td style='text-align:right;" + fontColor2 + "'>").append(svo2.getCurPrice())
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right;'>").append(svo2.getForeignTradingAmount()).append("</td>\r\n");
				sb1.append("</tr>\r\n");
				if (i == 29) {
					break;
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br>\r\n");
			sb1.append("\t<font size=3>").append(" ").append(strDailyOrWeekly + marketType + " 기관 순매매(거래대금순)")
					.append("</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' rowspan='2'>No.</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append("기관").append("순매수</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;' colspan='3'>")
					.append("기관").append("순매도</th>\r\n");
			sb1.append("</tr>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매수대금(억원)</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</th>\r\n");
			sb1.append("<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</th>\r\n");
			sb1.append(
					"<th style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매도대금(억원)</th>\r\n");
			sb1.append("</tr>\r\n");

			int cnt2 = 1;
			for (int i = 0; i < organDescList.size(); i++) {
				StockVO svo1 = organDescList.get(i);
				StockVO svo2 = organAscList.get(i);
				sb1.append("<tr>\r\n");
				String url1 = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
				String url2 = "http://finance.naver.com/item/main.nhn?code=" + svo2.getStockCode();
				sb1.append("<td>").append(cnt2++).append("</td>\r\n");
				sb1.append("<td><a href='").append(url1).append("' target='_new'>").append(svo1.getStockName())
						.append("</a></td>\r\n");
				String sign1 = svo1.getSign();
				String fontColor1 = "";
				if (sign1.equals("+")) {
					fontColor1 = "color:red;";
				} else if (sign1.equals("-")) {
					fontColor1 = "color:blue;";
				} else {
					fontColor1 = "color:black;";
				}

				String sign2 = svo2.getSign();
				String fontColor2 = "";
				if (sign2.equals("+")) {
					fontColor2 = "color:red;";
				} else if (sign2.equals("-")) {
					fontColor2 = "color:blue;";
				} else {
					fontColor2 = "color:black;";
				}

				sb1.append("<td style='text-align:right;" + fontColor1 + "'>").append(svo1.getCurPrice())
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right;'>").append(svo1.getOrganTradingAmount()).append("</td>\r\n");

				sb1.append("<td><a href='").append(url2).append("' target='_new'>").append(svo2.getStockName())
						.append("</a></td>\r\n");

				sb1.append("<td style='text-align:right;" + fontColor2 + "'>").append(svo2.getCurPrice())
						.append("</td>\r\n");
				sb1.append("<td style='text-align:right;'>").append(svo2.getOrganTradingAmount()).append("</td>\r\n");
				sb1.append("</tr>\r\n");
				if (i == 29) {
					break;
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strDate + "_" + strDailyOrWeekly + marketType
					+ " 외국인,기관 순매매(거래대금순)" + ".html");
			fw.write(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
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
}
