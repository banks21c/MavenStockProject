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
import html.parsing.stock.util.DataSort.ForeignOrganTradingVolumeAscCompare;
import html.parsing.stock.util.DataSort.ForeignOrganTradingVolumeDescCompare;
import html.parsing.stock.util.StockUtil;

public class AllStockForeignOrganBothStraight {

	final static String USER_HOME = System.getProperty("user.home");
	private static final Logger logger = LoggerFactory.getLogger(AllStockForeignOrganBothStraight.class);

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
		new AllStockForeignOrganBothStraight(1);
	}

	AllStockForeignOrganBothStraight() {
		List<StockVO> kospiStockList = readOne("005930","삼성전자");
		kospiStockList = getAllStockInfo(kospiStockList);
		writeFile(kospiStockList, "코스피", false, false);
	}

	AllStockForeignOrganBothStraight(int i) {
		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 외인,기관 양매수 거래량순 정렬
		Collections.sort(kospiAllStockList, new ForeignOrganTradingVolumeDescCompare());
		Collections.sort(kosdaqAllStockList, new ForeignOrganTradingVolumeDescCompare());
		writeFile(kospiAllStockList, "코스피 외인,기관 양매수 거래량", true, true);
		writeFile(kosdaqAllStockList, "코스닥 외인,기관 양매수 거래량", true, true);
		// 외인,기관 양매수 거래대금순 정렬
		Collections.sort(kospiAllStockList, new ForeignOrganTradingAmountDescCompare());
		Collections.sort(kosdaqAllStockList, new ForeignOrganTradingAmountDescCompare());
		writeFile(kospiAllStockList, "코스피 외인,기관 양매수 거래대금", false, true);
		writeFile(kosdaqAllStockList, "코스닥 외인,기관 양매수 거래대금", false, true);

		// 외인,기관 양매도 거래량순 정렬
		Collections.sort(kospiAllStockList, new ForeignOrganTradingVolumeAscCompare());
		Collections.sort(kosdaqAllStockList, new ForeignOrganTradingVolumeAscCompare());
		writeFile(kospiAllStockList, "코스피 외인,기관 양매도 거래량", true, false);
		writeFile(kosdaqAllStockList, "코스닥 외인,기관 양매도 거래량", true, false);
		// 외인,기관 양매도 거래대금순 정렬
		Collections.sort(kospiAllStockList, new ForeignOrganTradingAmountAscCompare());
		Collections.sort(kosdaqAllStockList, new ForeignOrganTradingAmountAscCompare());
		writeFile(kospiAllStockList, "코스피 외인,기관 양매도 거래대금", false, false);
		writeFile(kosdaqAllStockList, "코스닥 외인,기관 양매도 거래대금", false, false);

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
			// http://finance.naver.com/item/frgn.nhn?code=102460&page=1
			// http://finance.naver.com/item/frgn.nhn?code=102460&page=2
			doc = Jsoup.connect("http://finance.naver.com/item/frgn.nhn?code=" + strStockCode).get();

			String foreignTradingVolume = "";
			String organTradingVolume = "";
			long lForeignTradingVolume = 0;
			long lOrganTradingVolume = 0;

			Elements type2Elements = doc.select(".type2");
			Element type2Element = type2Elements.get(1);
			System.out.println("type2:" + type2Element);
			// 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
			// tr
			Elements trElements = type2Element.select("tr");
			int dayCnt = 0;
			for (Element trElement : trElements) {
				// td
				Elements tdElements = trElement.select("td");
				if (tdElements.size() == 9) {
					if (dayCnt == 1) {
						break;
					}
					// 종가(현재가)
					curPrice = tdElements.get(1).text();
					// 전일비(변동가)
					varyPrice = tdElements.get(2).text();
					// 등락율
					varyRatio = tdElements.get(3).text();

					String imgSrc = tdElements.get(2).select("img").attr("src");

					if (imgSrc.indexOf("ico_up.gif") != -1) {
						specialLetter = "▲";
					} else if (imgSrc.indexOf("ico_up02.gif") != -1) {
						specialLetter = "↑";
					} else if (imgSrc.indexOf("ico_down.gif") != -1) {
						specialLetter = "▼";
					} else if (imgSrc.indexOf("ico_down02.gif") != -1) {
						specialLetter = "↓";
					} else {
						specialLetter = "";
					}

					System.out.println("strYMD:" + strYMD);
					System.out.println("curPrice:" + curPrice);
					System.out.println("varyPrice:" + varyPrice);
					System.out.println("varyRatio:" + varyRatio);
					System.out.println("specialLetter:" + specialLetter);

					if (curPrice.matches(".*[0-9]+.*")) {
						stock.setCurPrice(curPrice);
						iCurPrice = Integer.parseInt(curPrice.replaceAll(",", ""));
						stock.setiCurPrice(iCurPrice);
					}
					if (varyPrice.matches(".*[0-9]+.*")) {
						stock.setVaryPrice(varyPrice);
						iVaryPrice = Integer.parseInt(varyPrice.replaceAll(",", ""));
						stock.setiVaryPrice(iVaryPrice);
					}
					if (varyRatio.matches(".*[0-9]+.*")) {
						stock.setVaryRatio(varyRatio);
					}

					stock.setSpecialLetter(specialLetter);

					// 기관순매매량
					organTradingVolume = tdElements.get(5).text();
					organTradingVolume = organTradingVolume.replace(",", "");
					if (organTradingVolume.matches(".*[0-9]+.*")) {
						lForeignTradingVolume += Long.parseLong(organTradingVolume);
					}
					// 외인순매매량
					foreignTradingVolume = tdElements.get(6).text();
					foreignTradingVolume = foreignTradingVolume.replace(",", "");
					if (organTradingVolume.matches(".*[0-9]+.*")) {
						lOrganTradingVolume += Long.parseLong(foreignTradingVolume);
					}
					dayCnt++;
				}
			}

			foreignTradingVolume = df.format(lForeignTradingVolume);
			organTradingVolume = df.format(lOrganTradingVolume);

			foreignTradingVolume = StringUtils.defaultIfEmpty(foreignTradingVolume, "0");
			organTradingVolume = StringUtils.defaultIfEmpty(organTradingVolume, "0");
			// foreignTradingVolume = StringUtils.defaultIfEmpty(foreignTradingVolume,
			// "0").replaceAll("\\+", "");
			// organTradingVolume = StringUtils.defaultIfEmpty(organTradingVolume,
			// "0").replaceAll("\\+", "");

			System.out.println("foreignTradingVolume:" + foreignTradingVolume);
			System.out.println("organTradingVolume:" + organTradingVolume);

			long lForeignOrganTradingVolume = 0;

			if (foreignTradingVolume.matches(".*[0-9]+.*")) {
				lForeignTradingVolume = Long.parseLong(foreignTradingVolume.replaceAll(",", ""));
			}
			if (organTradingVolume.matches(".*[0-9]+.*")) {
				lOrganTradingVolume = Long.parseLong(organTradingVolume.replaceAll(",", ""));
			}
			lForeignOrganTradingVolume = lForeignTradingVolume + lOrganTradingVolume;

			System.out.println("iForeignTradingVolume:" + lForeignTradingVolume);
			System.out.println("iOrganTradingVolume:" + lOrganTradingVolume);
			System.out.println("iForeignOrganTradingVolume:" + lForeignOrganTradingVolume);

			long standardPrice = 0;
			if (sign.equals("+")) {
				standardPrice = iCurPrice - iVaryPrice / 2;
			}
			if (sign.equals("-")) {
				standardPrice = iCurPrice + iVaryPrice / 2;
			}
			if (sign.equals("")) {
				standardPrice = iCurPrice;
			}

			System.out.println("standardPrice :" + standardPrice);

			long lForeignTradeAmount = lForeignTradingVolume * standardPrice;
			long lOrganTradeAmount = lOrganTradingVolume * standardPrice;
			// 금액은 만 단위
			lForeignTradeAmount = lForeignTradeAmount / 10000;
			lOrganTradeAmount = lOrganTradeAmount / 10000;

			System.out.println("외인거래금액 :" + lForeignTradeAmount);
			System.out.println("기관거래금액 :" + lOrganTradeAmount);

			stock.setlForeignTradingAmount(lForeignTradeAmount);
			stock.setlOrganTradingAmount(lOrganTradeAmount);
			stock.setlForeignOrganTradingAmount((lForeignTradeAmount + lOrganTradeAmount));

			String foreignTradeAmount = df.format(lForeignTradeAmount);
			String organTradeAmount = df.format(lOrganTradeAmount);
			String foreignOrganTradingVolume = df.format(lForeignOrganTradingVolume);
			if (lForeignOrganTradingVolume > 0) {
				foreignOrganTradingVolume = "+" + foreignOrganTradingVolume;
			}
			String foreignOrganTradingAmount = df.format((lForeignTradeAmount + lOrganTradeAmount));

			stock.setForeignTradingAmount(foreignTradeAmount);
			stock.setOrganTradingAmount(organTradeAmount);
			stock.setForeignOrganTradingAmount(foreignOrganTradingAmount);

			stock.setForeignTradingVolume(foreignTradingVolume);
			stock.setOrganTradingVolume(organTradingVolume);
			stock.setForeignOrganTradingVolume(foreignOrganTradingVolume);

			stock.setlForeignTradingVolume(lForeignTradingVolume);
			stock.setlOrganTradingVolume(lOrganTradingVolume);
			stock.setlForeignOrganTradingVolume(lForeignOrganTradingVolume);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String title, boolean isVolume, boolean isUp) {
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
			sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
			if (isVolume) {
				sb1.append("<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
				sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계</td>\r\n");
			} else {
				sb1.append(
					"<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(만원)</td>\r\n");
				sb1.append(
					"<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>합계(만원)</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			sb1.append("<tr>\r\n");
			if (isVolume) {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
			} else {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO svo : list) {
				// 외인,기관 거래량이 하나라도 없을 경우는 건너뛴다.
				if (isUp) {
					if (svo.getlForeignTradingVolume() <= 0 || svo.getlOrganTradingVolume() <= 0) {
						continue;
					}
				} else {
					if (svo.getlForeignTradingVolume() >= 0 || svo.getlOrganTradingVolume() >= 0) {
						continue;
					}
				}

				if (svo != null) {
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

					String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
					} else {
						sb1.append(
							"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
					}
					if (isVolume) {
						sb1.append("<td style='text-align:right'>" + svo.getForeignTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getOrganTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getForeignOrganTradingVolume() + "</td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'>" + svo.getForeignTradingAmount() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getOrganTradingAmount() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + svo.getForeignOrganTradingAmount() + "</td>\r\n");
					}
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\" + strYMD + "_" + title + ".html");
			fw.write(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
		}
	}

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> svoList = new ArrayList<>();
		int cnt = 0;
		for (StockVO svo : stockList) {
			cnt++;
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			System.out.println("_______________________________________");
			System.out.println(cnt + "." + stockCode + "\t" + stockName);
			System.out.println("_______________________________________");
			StockVO vo = getStockInfo(cnt, stockCode, stockName);
			if (vo != null) {
				svoList.add(vo);
			} else {
				logger.debug("vo##########:" + vo);
				logger.debug(stockName + "(" + stockCode + ") is null");
//				stockList.remove(svo);
			}
		}
		return svoList;
	}
}
