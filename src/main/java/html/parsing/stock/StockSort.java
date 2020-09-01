package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

import html.parsing.stock.util.DataSort.LineUpAscCompare;

public class StockSort {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockSort.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StockSort(1);
	}

	StockSort() {

		// readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd","KOSPI");
		// readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ");
		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		List<StockVO> kosdaqStockList = readOne("214420", "토니모리");
		writeFile(kosdaqStockList, kosdaqFileName, "코스닥");
	}

	StockSort(int i) {

		

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		// 코스피
		List<StockVO> kospiStockList = readFile("코스피", kospiFileName);
		logger.debug("kospiStockList.size :" + kospiStockList.size());

		Collections.sort(kospiStockList, new LineUpAscCompare());

		// 코스닥
		List<StockVO> kosdaqStockList = readFile("코스닥", kosdaqFileName);
		logger.debug("kosdaqStockList.size :" + kosdaqStockList.size());

		Collections.sort(kosdaqStockList, new LineUpAscCompare());

		writeFile(kospiStockList, kospiFileName, "코스피");
		writeFile(kosdaqStockList, kosdaqFileName, "코스닥");
	}

	public List<StockVO> readOne(String stockCode, String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode, stockName);
		if (stock != null) {
			stock.setStockName(stockName);
			stocks.add(stock);
		}
		return stocks;
	}

	public List<StockVO> readFile(String kospidaq, String fileName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				logger.debug(cnt + "." + read);
				stockCode = read.split("\t")[0];
				stockName = read.split("\t")[1];

				if (stockCode.length() != 6) {
					continue;
				}
				StockVO stock = getStockInfo(cnt, stockCode, stockName);
				if (stock != null) {
					stock.setStockName(stockName);
					stocks.add(stock);
				}
				cnt++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {

		}
		return stocks;
	}

	public StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);

		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			// logger.debug("doc:"+doc);

			Elements dates = doc.select(".date");
			if (dates != null) {
				if (dates.size() > 0) {
					Element date = dates.get(0);
					strYMD = date.ownText();
					strYMD = date.childNode(0).toString().trim();
					strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
				}
			}
			String market = doc.select(".wrap_company .description img").get(0).attr("class");
			stock.setMarket(market);
			logger.debug("market" + market);

			Element new_totalinfo = doc.select(".new_totalinfo").get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);
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
				logger.debug("text:" + text);

				if (text.startsWith("현재가")) {
					// logger.debug("data1:" + dd.text());
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
					// logger.debug("txts.length:" + txts.length);
					if (txts.length == 7) {
						stock.setVaryRatio(txts[5] + txts[6]);
					} else if (txts.length == 8) {
						stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
					}
					varyRatio = stock.getVaryRatio();
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
					// logger.debug("상승률:" + stock.getVaryRatio());
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

			if (specialLetter.equals("↑")) {
				stock.setStockGubun("상한가↑");
				stock.setLineUp(11);
				return stock;
			}
			if (specialLetter.equals("↓")) {
				stock.setStockGubun("하한가↓");
				stock.setLineUp(21);
				return stock;
			}

			String highPrice = stock.getHighPrice();
			String lowPrice = stock.getLowPrice();
			String maxPrice = stock.getMaxPrice();
			logger.debug("highPrice:" + highPrice);
			logger.debug("lowPrice:" + lowPrice);
			logger.debug("maxPrice:" + maxPrice);
			// 고가가 0이 아니고 고가가 상한가인가?
			logger.debug("고가가 0이 아니고 고가가 상한가인가?"
					+ String.valueOf(!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())));
			// 현재가가 상한가가 아닌가?
			logger.debug("현재가가 상한가가 아닌가?" + String.valueOf(!curPrice.equals(stock.getMaxPrice())));
			// 고가가 상한가인가?
			logger.debug("고가가 상한가인가?" + String.valueOf(highPrice.equals(stock.getMaxPrice())));
			// 고가가 0이 아니고 고가가 상한가인가?
			if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
					&& !curPrice.equals(stock.getMaxPrice())) {
				stock.setStockGubun("상터치↑↘");
				stock.setLineUp(12);
				return stock;
			}
			if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
					&& !curPrice.equals(stock.getMinPrice())) {
				stock.setStockGubun("하터치↓↗");
				stock.setLineUp(22);
				return stock;
			}

			float fRatio = 0f;
			if (varyRatio.indexOf("%") != -1) {
				fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
				if (fRatio >= 15) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun("+15%이상↗");
						stock.setLineUp(13);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun("-15%이상↘");
						stock.setLineUp(23);
					}
					return stock;
				}
				if (fRatio >= 10 && fRatio < 15) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun("+10%이상↗");
						stock.setLineUp(14);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun("-10%이상↘");
						stock.setLineUp(24);
					}
					return stock;
				}
				if (fRatio >= 5 && fRatio < 10) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun("+5%이상↗");
						stock.setLineUp(15);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun("-5%이상↘");
						stock.setLineUp(25);
					}
					return stock;
				}
			}

			// 현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
			float higher = 0;
			String flag = "";
			int icur = stock.getiCurPrice();
			int ihigh = stock.getiHighPrice();
			int ilow = stock.getiLowPrice();

			int iTradingVolume = stock.getiTradingVolume();
			if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
				higher = Math.abs(icur - ihigh);
				flag = "↗↘";
			} else {
				higher = Math.abs(icur - ilow);
				flag = "↘↗";
			}
			logger.debug("higher:" + higher + "\t" + (higher / icur * 100));
			float upDownRatio = higher / icur * 100;
			String strUpDownRatio = df.format(upDownRatio);
			if (upDownRatio > 10 && iTradingVolume > 0) {
				stock.setStockGubun(strUpDownRatio + "%" + flag);
				stock.setLineUp(16);
				return stock;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String fileName, String title) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
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
			sb1.append("\t<font size=5>" + strYMD + title + "</font>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			sb1.append(
					"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
			sb1.append("</tr>\r\n");

			for (StockVO s : list) {
				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>" + s.getStockGubun() + "</td>\r\n");
					sb1.append("<td><a href='" + url + "' target='_new'>" + s.getStockName() + "</a></td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();
					logger.debug("specialLetter+++>" + specialLetter);
					logger.debug("varyPrice+++>" + varyPrice);
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

					String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
					} else {
						sb1.append(
								"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + s.getTradingVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getTradingAmount() + "</td>\r\n");
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			logger.debug(sb1.toString());
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
