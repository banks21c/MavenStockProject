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

import html.parsing.stock.util.DataSort.ForeignTradingAmountDescCompare;
import html.parsing.stock.util.DataSort.ForeignTradingVolumeDescCompare;
import html.parsing.stock.util.DataSort.OrganTradingAmountDescCompare;
import html.parsing.stock.util.DataSort.OrganTradingVolumeDescCompare;
import html.parsing.stock.util.StockUtil;
import java.util.logging.Level;

public class AllStockForeignOrganDaily {

	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganDaily.class);

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
		new AllStockForeignOrganDaily(1);
	}

	AllStockForeignOrganDaily() {
		List<StockVO> kospiStockList = readOne("005930", "삼성전자");
		kospiStockList = getAllStockInfo(kospiStockList);
		writeFile(kospiStockList, "코스피", "외국인", "거래량");
	}

	AllStockForeignOrganDaily(int i) {
		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);

		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);

		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 1.외국인 거래량순 정렬
		Collections.sort(kospiAllStockList, new ForeignTradingVolumeDescCompare());
		Collections.sort(kosdaqAllStockList, new ForeignTradingVolumeDescCompare());

		writeFile(kospiAllStockList, "일간(Daily) 코스피 외국인 순매매량", "외국인", "순매매량");
		writeFile(kosdaqAllStockList, "일간(Daily) 코스닥 외국인 순매매량", "외국인", "순매매량");

		// 2.외국인 거래대금순 정렬
		Collections.sort(kospiAllStockList, new ForeignTradingAmountDescCompare());
		Collections.sort(kosdaqAllStockList, new ForeignTradingAmountDescCompare());

		writeFile(kospiAllStockList, "일간(Daily) 코스피 외국인 순매매(거래대금순)", "외국인", "거래대금");
		writeFile(kosdaqAllStockList, "일간(Daily) 코스닥 외국인 순매매(거래대금순)", "외국인", "거래대금");

		// 3.기관 거래량순 정렬
		Collections.sort(kospiAllStockList, new OrganTradingVolumeDescCompare());
		Collections.sort(kosdaqAllStockList, new OrganTradingVolumeDescCompare());

		writeFile(kospiAllStockList, "일간(Daily) 코스피 기관 순매매량", "기관", "순매매량");
		writeFile(kosdaqAllStockList, "일간(Daily) 코스닥 기관 순매매량", "기관", "순매매량");

		// 4.기관 거래대금순 정렬
		Collections.sort(kospiAllStockList, new OrganTradingAmountDescCompare());
		Collections.sort(kosdaqAllStockList, new OrganTradingAmountDescCompare());

		writeFile(kospiAllStockList, "일간(Daily) 코스피 기관 순매매(거래대금순)", "기관", "거래대금");
		writeFile(kosdaqAllStockList, "일간(Daily) 코스닥 기관 순매매(거래대금순)", "기관", "거래대금");

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
			svo = getStockTrade(svo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return svo;
	}

	public static StockVO getStockTrade(StockVO svo) {
		try {
			String strStockCode = svo.getStockCode();
			// =========================================================
			// 투자자별 매매동향 - 외국인 보유주수, 보유율
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=1
			// https://finance.naver.com/item/frgn.nhn?code=102460&page=2
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
			// 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
			// tr
			Elements trElements = type2Element.select("tr");
			int dayCnt = 0;

			//기준가,표준가 산정
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
					if (dayCnt == 1) {
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
					//기준가,표준가 산정
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
						System.out.println("기관:" + standardPrice + " X " + organTradingVolume + "=" + (standardPrice * tempLOrganTradingVolume));
						lOrganTradingVolume += tempLOrganTradingVolume;
						lOrganTradeAmount += tempLOrganTradingVolume * standardPrice;
					}
					// 외인순매매량
					foreignTradingVolume = tdElements.get(6).text();
					foreignTradingVolume = foreignTradingVolume.replace(",", "");
					if (foreignTradingVolume.matches(".*[0-9]+.*")) {
						long tempLForeignTradingVolume = Long.parseLong(foreignTradingVolume);
						System.out.println("\t\t외인:" + standardPrice + " X " + foreignTradingVolume + "=" + (standardPrice * tempLForeignTradingVolume));
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
			java.util.logging.Logger.getLogger(AllStockForeignOrganDaily.class.getName()).log(Level.SEVERE, null, ex);
		}
		return svo;
	}

	public void writeFile(List<StockVO> list, String title, String trader, String gubun1) {
		System.out.println("list.size:" + list.size());
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

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
			sb1.append("\t<font size=5>").append(strYMD).append(" ").append(title).append("</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매매량</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>순매매대금(만원)</td>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO svo : list) {
				// 순매매량(순매매대금)이 없으면 건너뛴다.
				if (trader.equals("외국인")) {
					if (svo.getlForeignTradingVolume() == 0) {
						continue;
					}
				} else {
					if (svo.getlOrganTradingVolume() == 0) {
						continue;
					}
				}

				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
				sb1.append("<td>").append(cnt++).append("</td>\r\n");
				sb1.append("<td><a href='").append(url).append("' target='_new'>").append(svo.getStockName()).append("</a></td>\r\n");
				sb1.append("<td style='text-align:right'>").append(svo.getCurPrice()).append("</td>\r\n");

				String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
				String varyPrice = svo.getVaryPrice();
				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
					|| specialLetter.startsWith("+")) {
					sb1.append("<td style='text-align:right'><font color='red'>").append(specialLetter).append(" ").append(varyPrice).append("</font></td>\r\n");
				} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
					|| specialLetter.startsWith("-")) {
					sb1.append("<td style='text-align:right'><font color='blue'>").append(specialLetter).append(" ").append(varyPrice).append("</font></td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>0</td>\r\n");
				}

				String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
				if (varyRatio.startsWith("+")) {
					sb1.append("<td style='text-align:right'><font color='red'>").append(varyRatio).append("</font></td>\r\n");
				} else if (varyRatio.startsWith("-")) {
					sb1.append("<td style='text-align:right'><font color='blue'>").append(varyRatio).append("</font></td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'><font color='black'>").append(varyRatio).append("</font></td>\r\n");
				}
				if (trader.equals("외국인")) {
					sb1.append("<td style='text-align:right'>").append(svo.getForeignTradingVolume()).append("</td>\r\n");
					sb1.append("<td style='text-align:right'>").append(svo.getForeignTradingAmount()).append("</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>").append(svo.getOrganTradingVolume()).append("</td>\r\n");
					sb1.append("<td style='text-align:right'>").append(svo.getOrganTradingAmount()).append("</td>\r\n");
				}
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
