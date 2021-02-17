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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import html.parsing.stock.util.DataSort.TradingAmountDescCompare;
import html.parsing.stock.util.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.util.DataSort.VaryRatioAscCompare;
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.StockUtil;

public class StockTopRankInput {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockTopRankInput.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static int numberOfStock;

	static int topCount = 0;
	static int upCount = 0;
	static int bottomCount = 0;
	static int downCount = 0;
	static int steadyCount = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String num = JOptionPane.showInputDialog("상위 몇개를 추출할까요?");
		int iNum = Integer.parseInt(num);
		numberOfStock = iNum;
		new StockTopRankInput(1);
	}

	StockTopRankInput() {

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		List<StockVO> kospiStockList = readOne("012450", "한화테크윈");
		writeFile(kospiStockList, kospiFileName, "코스피", "ALL");

		List<StockVO> kospiStockList1 = readOne("123890", "");
		writeFile(kospiStockList1, kospiFileName, "코스피", "상승율");
		// List<Stock> kosdaqStockList = readOne("003520");
		// writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
	}

	StockTopRankInput(int i) {

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = StockUtil.readStockCodeNameList("kospi");
		kospiAllStockList = getAllStockInfo(kospiAllStockList);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

		// 코스닥
		List<StockVO> kosdaqAllStockList = StockUtil.readStockCodeNameList("kosdaq");
		kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 1.상승율순 정렬
		Collections.sort(kospiAllStockList, new VaryRatioDescCompare());
		writeFile(kospiAllStockList, kospiFileName, "코스피 상승율 Top " + numberOfStock, "상승율");

		// 2.하락율순 정렬
		Collections.sort(kospiAllStockList, new VaryRatioAscCompare());
		writeFile(kospiAllStockList, kospiFileName, "코스피 하락율 Top " + numberOfStock, "하락율");

		// 3.거래량 정렬
		Collections.sort(kospiAllStockList, new TradingVolumeDescCompare());
		writeFile(kospiAllStockList, kospiFileName, "코스피 거래량 Top " + numberOfStock, "거래량");

		// 4.거래대금순 정렬
		Collections.sort(kospiAllStockList, new TradingAmountDescCompare());
		writeFile(kospiAllStockList, kospiFileName, "코스피 거래대금 Top " + numberOfStock, "거래대금");

		topCount = 0;
		upCount = 0;
		bottomCount = 0;
		downCount = 0;
		steadyCount = 0;

		// 1.상승율순 정렬
		Collections.sort(kosdaqAllStockList, new VaryRatioDescCompare());
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 상승율 Top " + numberOfStock, "상승율");

		// 2.하락율순 정렬
		Collections.sort(kosdaqAllStockList, new VaryRatioAscCompare());
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 하락율 Top " + numberOfStock, "하락율");

		// 3.거래량 정렬
		Collections.sort(kosdaqAllStockList, new TradingVolumeDescCompare());
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 거래량 Top " + numberOfStock, "거래량");

		// 4.거래대금순 정렬
		Collections.sort(kosdaqAllStockList, new TradingAmountDescCompare());
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 거래대금 Top " + numberOfStock, "거래대금");

	}

	public static List<StockVO> readOne(String stockCode, String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode);
		if (stock != null) {
			stock.setStockName(stockName);
			stocks.add(stock);
		}
		return stocks;
	}

	public static StockVO getStockInfo(int cnt, String code) {
		Document doc;
		StockVO stock = new StockVO();
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
			if (cnt == 1) {
				// System.out.println(doc.title());
				// System.out.println(doc.html());
			}
			stock.setStockCode(code);

			Elements dates = doc.select(".date");
			if (dates != null) {
				if (dates.size() > 0) {
					Element date = dates.get(0);
					strYMD = date.ownText();
					strYMD = date.childNode(0).toString().trim();
					strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
				}
			}
			Element new_totalinfo = doc.select(".new_totalinfo").get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);
			Elements edds = blind.select("dd");

			String specialLetter = "";
			String sign = "";
			String curPrice = "";
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

					String varyPrice = txts[4];
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

			if (specialLetter.equals("↑")) {
				topCount++;
			} else if (specialLetter.equals("▲")) {
				upCount++;
			} else if (specialLetter.equals("↓")) {
				bottomCount++;
			} else if (specialLetter.equals("▼")) {
				downCount++;
			} else {
					steadyCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
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
			sb1.append("\t<h2>" + strYMD + title + "</h2><br>");
			sb1.append("<h4><font color='red'>상한가:" + topCount + "</font><font color='red'> 상승:" + upCount
				+ "</font><font color='blue'> 하한가:" + bottomCount + "</font><font color='blue'> 하락:" + downCount
				+ "</font><font color='gray'> 보합:" + steadyCount + "</font></h4>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			// if(gubun.equals("상승율")){
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>상승율</td>\r\n");
			// }else if(gubun.equals("하락율")){
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>하락율</td>\r\n");
			// }else if(gubun.equals("거래량")){
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			// }else if(gubun.equals("거래대금")){
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			// sb1.append("<td
			// style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
			// }

			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");

			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : list) {
				if (s != null) {
					if (cnt == numberOfStock + 1) {
						break;
					}
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url + "' target='_new'>" + s.getStockName() + "</a></td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();

					System.out.println("specialLetter+++>" + specialLetter);
					System.out.println("varyPrice+++>" + varyPrice);

					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
						|| specialLetter.startsWith("+")) {
						sb1.append("<td style='text-align:right;color:red'>"
							+ StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
							+ "</font></td>\r\n");
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
						|| specialLetter.startsWith("-")) {
						sb1.append("<td style='text-align:right;color:blue'>"
							+ StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
							+ "</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right;color:metal'>"
							+ StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}

					// if(gubun.equals("ALL")){
					// String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					// if (varyRatio.startsWith("+")) {
					// sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio +
					// "</font></td>\r\n");
					// } else if (varyRatio.startsWith("-")) {
					// sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio +
					// "</font></td>\r\n");
					// } else {
					// sb1.append(
					// "<td style='text-align:right'><font color='black'>" + varyRatio +
					// "</font></td>\r\n");
					// }
					// sb1.append("<td style='text-align:right'>" +
					// StringUtils.defaultIfEmpty(s.getTradingVolume(),"") + "</td>\r\n");
					// sb1.append("<td style='text-align:right'>" +
					// StringUtils.defaultIfEmpty(s.getTradingAmount(),"") + "</td>\r\n");
					// }else if(gubun.equals("상승율")||gubun.equals("하락율")){
					// String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					// if (varyRatio.startsWith("+")) {
					// sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio +
					// "</font></td>\r\n");
					// } else if (varyRatio.startsWith("-")) {
					// sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio +
					// "</font></td>\r\n");
					// } else {
					// sb1.append(
					// "<td style='text-align:right'><font color='black'>" + varyRatio +
					// "</font></td>\r\n");
					// }
					// }else if(gubun.equals("거래량")){
					// sb1.append("<td style='text-align:right'>" +
					// StringUtils.defaultIfEmpty(s.getTradingVolume(),"") + "</td>\r\n");
					// }else if(gubun.equals("거래대금")){
					// sb1.append("<td style='text-align:right'>" +
					// StringUtils.defaultIfEmpty(s.getTradingAmount(),"") + "</td>\r\n");
					// }
					String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
					} else {
						sb1.append(
							"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingVolume(), "")
						+ "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingAmount(), "")
						+ "</td>\r\n");

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
			StockVO vo = StockUtil.getStockInfo(cnt, stockCode, stockName);
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
