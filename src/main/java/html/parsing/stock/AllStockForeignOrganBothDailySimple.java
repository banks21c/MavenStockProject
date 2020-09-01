package html.parsing.stock;

import html.parsing.stock.model.StockVO;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.ForeignOrganTradingAmountAscCompare;
import html.parsing.stock.util.DataSort.ForeignOrganTradingAmountDescCompare;
import html.parsing.stock.util.StockUtil;
import java.util.logging.Level;

public class AllStockForeignOrganBothDailySimple {

	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganBothDailySimple.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static DecimalFormat df = new DecimalFormat("#,##0");
	String strDailyOrWeekly = "일간(Daily) ";
	static int extractDayCount = 1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganBothDailySimple();
	}

	AllStockForeignOrganBothDailySimple() {
//		test();
		real();
	}

	void test() {
		List<StockVO> kospiStockList = readOne("005930", "삼성전자");
		kospiStockList = getAllStockInfo(kospiStockList);

		// 코스피 일간(Daily) 외인,기관 양매수 거래대금
		List<StockVO> kospiAllStockBuyList = getAllStockTrade(kospiStockList, "buy");
		// 코스피 일간(Daily) 외인,기관 양매도 거래대금
		List<StockVO> kospiAllStockSellList = getAllStockTrade(kospiStockList, "sell");

		writeFile(kospiAllStockBuyList, kospiAllStockSellList, strDailyOrWeekly + "코스피 외인,기관 양매수,양매도 거래량");
	}

	void real() {
		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		List<StockVO> kospiAllStockListCopy = new ArrayList();
		kospiAllStockListCopy.addAll(kospiAllStockList);

		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		List<StockVO> kosdaqAllStockListCopy = new ArrayList();
		kosdaqAllStockListCopy.addAll(kosdaqAllStockList);

		// 코스피 외인,기관 양매매 거래대금순 정렬
		Collections.sort(kospiAllStockList, new ForeignOrganTradingAmountDescCompare());
		Collections.sort(kospiAllStockListCopy, new ForeignOrganTradingAmountAscCompare());
		writeFile(kospiAllStockList, kospiAllStockListCopy, strDailyOrWeekly + "코스피 외인,기관 양매매 거래대금");

		// 코스닥 외인,기관 양매매 거래대금순 정렬
		Collections.sort(kosdaqAllStockList, new ForeignOrganTradingAmountDescCompare());
		Collections.sort(kosdaqAllStockListCopy, new ForeignOrganTradingAmountAscCompare());
		writeFile(kosdaqAllStockList, kosdaqAllStockListCopy, strDailyOrWeekly + "코스닥 외인,기관 양매수 거래대금");

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

			StockVO stock = getStockInfo(stockCode, stockName);
			if (stock != null) {
				stock.setStockName(stockName);
				stocks.add(stock);
			}
			cnt++;
		}
		return stocks;
	}

	public static List<StockVO> getAllStockTrade(List<StockVO> stockList, String buyOrSell) {
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

			svo = getStockTrade(svo, buyOrSell);
//			svo = getStockTrade(svo);

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

	public static StockVO getStockTrade(StockVO svo, String buyOrSell) {
		try {
			String strStockCode = svo.getStockCode();
			// =========================================================
			// 투자자별 매매동향 - 외국인 보유주수, 보유율
			// http://finance.naver.com/item/frgn.nhn?code=102460&page=1
			// http://finance.naver.com/item/frgn.nhn?code=102460&page=2
			System.out.println("http://finance.naver.com/item/frgn.nhn?code=" + strStockCode);
			Document doc = Jsoup.connect("http://finance.naver.com/item/frgn.nhn?code=" + strStockCode).get();

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
					if (buyOrSell.equals("buy")) {
						if (!organTradingVolume.startsWith("+") || !foreignTradingVolume.startsWith("+")) {
							return null;
						}
					} else {
						if (!organTradingVolume.startsWith("-") || !foreignTradingVolume.startsWith("-")) {
							return null;
						}
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
			java.util.logging.Logger.getLogger(AllStockForeignOrganBothWeeklyV1.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return svo;
	}

	public void writeFile(List<StockVO> descList, List<StockVO> ascList, String title) {
		System.out.println("list.size:" + descList.size());
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
			sb1.append("\t<font size=5>").append(strYMD).append(" ").append(title).append("</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append(
					"<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>매수대금(만원)</td>\r\n");
			sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</td>\r\n");
			sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append(
					"<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>매도대금(만원)</td>\r\n");
			sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</td>\r\n");
			sb1.append("</tr>\r\n");

			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (int i = 0; i < descList.size(); i++) {
				StockVO svo1 = descList.get(i);

				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + svo1.getStockCode();
				sb1.append("<td>" + cnt++ + "</td>\r\n");
				sb1.append("<td><a href='" + url + "' target='_new'>" + svo1.getStockName() + "</a></td>\r\n");
				long lForeignTradingAmount1 = svo1.getlForeignTradingAmount();
				String fontColor = "metal";
				if (lForeignTradingAmount1 < 0) {
					fontColor = "blue";
				} else if (lForeignTradingAmount1 > 0) {
					fontColor = "red";
				}
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>" + svo1.getForeignTradingAmount()
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>" + svo1.getOrganTradingAmount()
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>"
						+ svo1.getForeignOrganTradingAmount() + "</td>\r\n");

				StockVO svo2 = ascList.get(i);
				sb1.append("<td><a href='" + url + "' target='_new'>" + svo2.getStockName() + "</a></td>\r\n");
				long lForeignTradingAmount2 = svo2.getlForeignTradingAmount();
				fontColor = "metal";
				if (lForeignTradingAmount2 < 0) {
					fontColor = "blue";
				} else if (lForeignTradingAmount2 > 0) {
					fontColor = "red";
				}
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>" + svo2.getForeignTradingAmount()
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>" + svo2.getOrganTradingAmount()
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right;color:" + fontColor + ";'>"
						+ svo2.getForeignOrganTradingAmount() + "</td>\r\n");

				sb1.append("</tr>\r\n");
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strDate + "_" + title + ".html");
			fw.write(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
		}
	}

}
