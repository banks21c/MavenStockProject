package html.parsing.stock;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.TradingAmountDescCompare;
import html.parsing.stock.util.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.StockUtil;

public class StockSpac {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockSpac.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StockSpac(1);
	}

	StockSpac() {

		List<StockVO> kospiStockList = readOne("071970");
		writeFile(kospiStockList, "코스닥");
	}

	StockSpac(int i) {

		// 모든 주식 정보를 조회한다.
		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("코스닥");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 1.등락율순 정렬
		Collections.sort(kosdaqAllStockList, new VaryRatioDescCompare());

		writeFile(kosdaqAllStockList, "스팩 등락율 ");

		// 2.거래량 정렬
		Collections.sort(kosdaqAllStockList, new TradingVolumeDescCompare());

		writeFile(kosdaqAllStockList, "스팩 거래량");

		// 3.거래대금순 정렬
		Collections.sort(kosdaqAllStockList, new TradingAmountDescCompare());

		writeFile(kosdaqAllStockList, "스팩 거래대금");

	}

	public static List<StockVO> readOne(String stockCode) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode, "");
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();
		int cnt = 0;
		for (StockVO svo : stockList) {
			cnt++;
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			if (stockName.contains("스팩")) {
				svo = getStockInfo(cnt, stockCode, stockName);
				if (svo != null) {
					stocks.add(svo);
				}
			}
		}
		return stocks;
	}

	public static StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		try {
			// 종합정보
			logger.debug("code:" + strStockCode + " name:" + strStockName);
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			stock.setStockCode(strStockCode);
			stock.setStockName(strStockName);

			String varyRatio = "";
			String curPrice = "";
			String varyPrice = "";
			String sign = "";
			String specialLetter = "";

			int iCurPrice = 0;
			int iVaryPrice = 0;

			Elements curPriceEls = doc.select(".spot .rate_info .today .no_today em").get(0).select("span");
			StringBuilder curPriceSb = new StringBuilder();
			if (curPriceEls.size() > 0) {
				for (Element curPriceEl : curPriceEls) {
					String curPriceElClass = curPriceEl.attr("class");
					if (curPriceElClass.startsWith("no") || curPriceElClass.startsWith("shim")) {
						curPriceSb.append(curPriceEl.text());
					}
				}
				System.out.println(curPriceSb.toString());
			}
			curPrice = curPriceSb.toString();

			Elements varyPriceEls = doc.select(".spot .rate_info .today .no_exday em").get(0).select("span");
			StringBuilder varyPriceSb = new StringBuilder();
			if (varyPriceEls.size() > 0) {
				for (Element varyPriceEl : varyPriceEls) {
					String varyPriceElClass = varyPriceEl.attr("class");
					if (varyPriceElClass.startsWith("no") || varyPriceElClass.startsWith("shim")) {
						varyPriceSb.append(varyPriceEl.text());
					}
				}
				System.out.println(varyPriceSb.toString());
			}
			varyPrice = varyPriceSb.toString();

			Elements varyRatioEls = doc.select(".spot .rate_info .today .no_exday em").get(1).select("span");
			StringBuilder varyRatioSb = new StringBuilder();
			if (varyRatioEls.size() > 0) {
				for (Element varyRatioEl : varyRatioEls) {
					String varyRatioElClass = varyRatioEl.attr("class");
					if (varyRatioElClass.startsWith("no") || varyRatioElClass.startsWith("jum")) {
						varyRatioSb.append(varyRatioEl.text());
					}
					if (varyRatioElClass.startsWith("ico")) {
						sign = varyRatioEl.text();
					}
				}
				System.out.println(varyRatioSb.toString());
			}
			varyRatio = varyRatioSb.toString();

			System.out.println("sign:" + sign);
			if (sign.equals("-")) {
				specialLetter = "▼";
				varyPrice = sign + varyPrice;
				varyRatio = sign + varyRatio;
			} else if (sign.equals("+")) {
				specialLetter = "▲";
			} else if (sign.equals("상한가")) {
				specialLetter = "↑";
			} else if (sign.equals("하한가")) {
				specialLetter = "↓";
			}

			System.out.println("curPrice:" + curPrice);
			System.out.println("varyPrice:" + varyPrice);
			System.out.println("varyRatio:" + varyRatio);
			System.out.println("specialLetter :" + specialLetter);

			stock.setSpecialLetter(specialLetter);
			stock.setSign(sign);
			stock.setCurPrice(curPrice);
			stock.setiCurPrice(Integer.parseInt(curPrice.replaceAll(",", "")));
			stock.setVaryPrice(varyPrice);
			stock.setiVaryPrice(Integer.parseInt(varyPrice.replaceAll(",", "")));
			stock.setVaryRatio(varyRatio);

			iCurPrice = Integer.parseInt(StringUtils.defaultIfEmpty(curPrice.replaceAll(",", ""), "0"));
			iVaryPrice = Integer.parseInt(StringUtils.defaultIfEmpty(varyPrice.replaceAll(",", ""), "0"));

			stock.setiCurPrice(iCurPrice);
			stock.setiVaryPrice(iVaryPrice);
			stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));

			Elements edds = doc.getElementsByTag("dd");
			System.out.println("dds.size:" + edds.size());
			for (int i = 0; i < edds.size(); i++) {
				Element dd = edds.get(i);
				System.out.println("data:" + dd.text());
				String text = dd.text();
				if (text.startsWith("거래량")) {
					System.out.println("거래량 :" + text.split(" ")[1]);
					stock.setTradingVolume(text.split(" ")[1]);
					stock.setlTradingVolume(Long.parseLong(stock.getTradingVolume().replaceAll(",", "")));
				}
				if (text.startsWith("거래대금")) {
					stock.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
					stock.setlTradingAmount(Integer
							.parseInt(StringUtils.defaultIfEmpty(stock.getTradingAmount().replaceAll(",", ""), "0")));
				}
				System.out.println("data:" + dd.text());

				if (text.startsWith("고가")) {
					stock.setHighPrice(text.split(" ")[1]);
				}
				if (text.startsWith("저가")) {
					stock.setLowPrice(text.split(" ")[1]);
				}
			}

			Elements dds = doc.select("dd");
			for (Element dd : dds) {
				String priceTxt = dd.text();
				System.out.println("priceTxt:" + priceTxt);
				if ((priceTxt.indexOf("가") != -1 || priceTxt.indexOf("량") != -1)
						&& priceTxt.matches("(.*)[0-9]+(.*)")) {
					// String priceSplit[] = priceTxt.replaceAll(",", "").split(" ");
					String priceSplit[] = priceTxt.split(" ");
					System.out.println(priceSplit[0] + "-------------->" + priceSplit[1]);
					// ht.put(priceSplit[0], priceSplit[1]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String title) {
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
			sb1.append("\t<font size=5>" + title + "</font>");
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

			int cnt = 1;
			for (StockVO s : list) {
				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url + "' target='_new'>" + s.getStockName() + "</a></td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();
					System.out.println("specialLetter+++>" + specialLetter);
					System.out.println("varyPrice+++>" + varyPrice);
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
			System.out.println(sb1.toString());
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
