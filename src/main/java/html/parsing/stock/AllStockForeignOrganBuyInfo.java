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

import html.parsing.stock.util.DataSort.ForeignStraitBuyCountDescCompare;
import html.parsing.stock.util.DataSort.OrganStraitBuyCountDescCompare;
import html.parsing.stock.util.StockUtil;

public class AllStockForeignOrganBuyInfo {

	final static String USER_HOME = System.getProperty("user.home");
	private static final Logger logger = LoggerFactory.getLogger(AllStockForeignOrganBuyInfo.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static DecimalFormat df = new DecimalFormat("#,##0");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganBuyInfo(1);
	}

	AllStockForeignOrganBuyInfo() {
		List<StockVO> kospiStockList = readOne("005930","삼성전자");
		kospiStockList = getAllStockInfo(kospiStockList);
		writeFile(kospiStockList, "코스피", true);
	}

	AllStockForeignOrganBuyInfo(int i) {
		// 모든 주식 정보를 조회한다.
		// 코스피
//        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		// 코스닥
//        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 1.외국인 연속매수순 정렬
		Collections.sort(kospiAllStockList, new ForeignStraitBuyCountDescCompare());
		Collections.sort(kosdaqAllStockList, new ForeignStraitBuyCountDescCompare());

		writeFile(kospiAllStockList, "코스피 외국인 연속매수일수 ", true);
		writeFile(kosdaqAllStockList, "코스닥 외국인 연속매수일수 ", true);

		// 2.기관 연속매수순 정렬
		Collections.sort(kospiAllStockList, new OrganStraitBuyCountDescCompare());
		Collections.sort(kosdaqAllStockList, new OrganStraitBuyCountDescCompare());

		writeFile(kospiAllStockList, "코스피 기관 연속매수일수 ", false);
		writeFile(kosdaqAllStockList, "코스닥 기관 연속매수일수 ", false);
	}

	public static List<StockVO> readOne(String stockCode,String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode, stockName);
		if (stock != null) {
			stocks.add(stock);
		}
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

			StockVO stock = getStockInfo(cnt, stockCode, stockName);
			if (stock != null) {
				stock.setStockName(stockName);
				stocks.add(stock);
			}
			cnt++;
		}
		return stocks;
	}

	public static StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
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
				return stock;
			}

			Element new_totalinfo = new_totalinfos.get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);

			if (blind == null) {
				return stock;
			}

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
				System.out.println("data:" + text);

				if (text.startsWith("현재가")) {
					System.out.println("data1:" + dd.text());
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
					System.out.println("txts.length:" + txts.length);
					if (txts.length == 7) {
						stock.setVaryRatio(txts[5] + txts[6]);
					} else if (txts.length == 8) {
						stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
					}
					varyRatio = stock.getVaryRatio();
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
					System.out.println("상승률:" + stock.getVaryRatio());
				}

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
					stock.setlTradingVolume(Long.parseLong(stock.getTradingVolume().replaceAll(",", "")));
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

			// =========================================================
			// 투자자별 매매동향 - 외국인 보유주수, 보유율
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=1
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=2
			long lOrganCount = 0;
			long lForeignCount = 0;

			long lOrganVolumeSum = 0;
			long lOrganAmountSum = 0;

			long lForeignVolumeSum = 0;
			long lForeignAmountSum = 0;
			for (int i = 1; i <= 3; i++) {
				doc = Jsoup.connect("https://finance.naver.com/item/frgn.nhn?code=" + strStockCode + "&page=" + i).get();
				// System.out.println(doc.html());
				Elements type2Elements = doc.select(".type2");
				Element type2Element = type2Elements.get(1);
				System.out.println("type2:" + type2Element);
				// 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
				// tr
				Elements trElements = type2Element.select("tr");
				for (Element trElement : trElements) {
					// td
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 9) {
						// 종가
						String endPrice = tdElements.get(1).text();
						// 전일대비
						varyPrice = tdElements.get(2).text();
						try {
							endPrice = StringUtils.replace(endPrice, ",", "");
							varyPrice = StringUtils.replace(varyPrice, ",", "");
						} catch (Exception e) {
							e.printStackTrace();
						}

						char plusMinus = ' ';
						String ratio = tdElements.get(3).text();
						if (ratio.startsWith("+")) {
							plusMinus = '+';
						} else if (ratio.startsWith("-")) {
							plusMinus = '-';
						}
						if (!endPrice.matches("[0-9]*")) {
							System.out.println("endPrice is not matches number~~~");
							continue;
						}
						System.out.println("endPrice:" + endPrice);
						System.out.println("varyPrice:" + varyPrice);
						double iEndPrice = Integer.parseInt(endPrice);
						double iHalfVaryPrice = Integer.parseInt(varyPrice) / 2;
						double dAveragePrice = 0;
						if (plusMinus == '+') {
							dAveragePrice = iEndPrice - iHalfVaryPrice;
						} else if (plusMinus == '-') {
							dAveragePrice = iEndPrice + iHalfVaryPrice;
						} else {
							dAveragePrice = iEndPrice;
						}
						System.out.println("dAveragePrice:" + dAveragePrice);
						// 기관순매매량
						String organVolume = tdElements.get(5).text();
						long lOrganVolume = 0;
						try {
							organVolume = StringUtils.replace(organVolume, ",", "");
							lOrganVolume = Long.parseLong(organVolume);
							System.out.println("organVolume:" + organVolume);
							System.out.println("dOrganVolume:" + lOrganVolume);
						} catch (Exception e) {
							e.printStackTrace();
						}

						char organSign = ' ';
						if (organVolume.startsWith("+") || organVolume.startsWith("-")) {
							organSign = organVolume.charAt(0);
						}
						if (organSign == '+') {
							lOrganAmountSum += dAveragePrice * lOrganVolume;
							lOrganVolumeSum += lOrganVolume;
							lOrganCount++;
						} else {
							break;
						}
					}
				}
				for (Element trElement : trElements) {
					// td
					Elements tdElements = trElement.select("td");
					if (tdElements.size() == 9) {
						// 종가
						String endPrice = tdElements.get(1).text();
						// 전일대비
						varyPrice = tdElements.get(2).text();
						try {
							endPrice = StringUtils.replace(endPrice, ",", "");
							varyPrice = StringUtils.replace(varyPrice, ",", "");
						} catch (Exception e) {
							e.printStackTrace();
						}

						char plusMinus = ' ';
						String ratio = tdElements.get(3).text();
						if (ratio.startsWith("+")) {
							plusMinus = '+';
						} else if (ratio.startsWith("-")) {
							plusMinus = '-';
						}

						if (!endPrice.matches("[0-9]*")) {
							System.out.println("endPrice is not matches number~~~");
							continue;
						}

						double iEndPrice = Integer.parseInt(endPrice);
						double iHalfVaryPrice = Integer.parseInt(varyPrice) / 2;
						double dAveragePrice = 0;
						if (plusMinus == '+') {
							dAveragePrice = iEndPrice - iHalfVaryPrice;
						} else if (plusMinus == '-') {
							dAveragePrice = iEndPrice + iHalfVaryPrice;
						} else {
							dAveragePrice = iEndPrice;
						}
						System.out.println("dAveragePrice:" + dAveragePrice);

						// 외국인순매매량
						String foreignVolume = tdElements.get(6).text();
						long lForeignVolume = 0;
						try {
							foreignVolume = StringUtils.replace(foreignVolume, ",", "");
							lForeignVolume = Long.parseLong(foreignVolume);
							System.out.println("foreignVolume:" + foreignVolume);
						} catch (Exception e) {
							e.printStackTrace();
						}

						char foreignSign = ' ';
						if (foreignVolume.startsWith("+") || foreignVolume.startsWith("-")) {
							foreignSign = foreignVolume.charAt(0);
						}
						if (foreignSign == '+') {
							lForeignAmountSum += dAveragePrice * lForeignVolume;
							lForeignVolumeSum += lForeignVolume;
							lForeignCount++;
						} else {
							break;
						}
					}
				}

				if (lOrganCount == i * 20 || lForeignCount == i * 20) {
					continue;
				} else {
					break;
				}
			}

			String organTradingVolume = df.format(lOrganVolumeSum);
			String organTradingAmount = df.format((lOrganAmountSum / 10000));
			String foreignTradingVolume = df.format(lForeignVolumeSum);
			String foreignTradingAmount = df.format((lForeignAmountSum / 10000));

			stock.setOrganStraitBuyCount(lOrganCount);
			stock.setOrganTradingVolume(organTradingVolume);
			stock.setOrganTradingAmount(organTradingAmount);

			stock.setForeignStraitBuyCount(lForeignCount);
			stock.setForeignTradingVolume(foreignTradingVolume);
			stock.setForeignTradingAmount(foreignTradingAmount);

			// System.out.println(stock.getOrganTradingVolume());
			// System.out.println(stock.getOrganTradingAmount());
			// System.out.println(stock.getForeignTradingVolume());
			// System.out.println(stock.getForeignTradingAmount());
			if (lOrganCount == 0 && lForeignCount == 0) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String title, boolean isForeign) {
		System.out.println("list.size:" + list.size());
		try {
			StringBuilder sb1 = new StringBuilder();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");
			sb1.append("\t<font size=5>" + strYMD + " " + title + "</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			if (isForeign) {
				sb1.append("<td colspan='3' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
			} else {
				sb1.append("<td colspan='3' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
			}
			sb1.append("</tr>");
			sb1.append("<tr>");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>연속매수일수</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인거래금액<br>(만원)</td>\r\n");
			sb1.append("</tr>");

			int cnt = 1;
			for (StockVO svo : list) {
				if (svo != null) {
					long sellCount = 0;

					if (isForeign) {
						sellCount = svo.getForeignStraitBuyCount();
					} else {
						sellCount = svo.getOrganStraitBuyCount();
					}
					if (sellCount == 0) {
						continue;
					}

					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url + "' target='_new'>" + svo.getStockName() + "</a></td>\r\n");
					sb1.append("<td style='text-align:right'>" + svo.getCurPrice() + "</td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
					String varyPrice = svo.getVaryPrice();
					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
						|| specialLetter.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
							+ "</font></td>\r\n");
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
						|| specialLetter.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
							+ "</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}
					if (isForeign) {
						sb1.append("<td style='text-align:right'>" + svo.getForeignStraitBuyCount() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getForeignTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getForeignTradingAmount() + "</td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'>" + svo.getOrganStraitBuyCount() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getOrganTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getOrganTradingAmount() + "</td>\r\n");
					}
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println(sb1.toString());
			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strYMD + "_" + title + ".html");
			fw.write(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
		}
	}

}
