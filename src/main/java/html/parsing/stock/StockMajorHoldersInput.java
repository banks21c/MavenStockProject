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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.RetainAmountDescCompare;
import html.parsing.stock.util.DataSort.RetainRatioDescCompare;
import html.parsing.stock.model.MajorStockHolderVO;

public class StockMajorHoldersInput {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockMajorHoldersInput.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDate = sdf.format(new Date());

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String majorStockHolders;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		majorStockHolders = JOptionPane.showInputDialog("대주주명을 입력해주세요.");
		new StockMajorHoldersInput(1);
	}

	StockMajorHoldersInput() {

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		// 대웅제약 069620
		List<StockVO> kospiStockList = readOne("069620");
		writeFile(kospiStockList, kospiFileName, "코스피");
	}

	StockMajorHoldersInput(int i) {

		

		String kospiFileName = "new_kospi_우선주제외.html";
		String kosdaqFileName = "new_kosdaq_우선주제외.html";

		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
		System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());
		kospiAllStockList = getStockMarketPrice(kospiAllStockList);

		// 코스닥
		List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());
		kosdaqAllStockList = getStockMarketPrice(kosdaqAllStockList);

		Collections.sort(kospiAllStockList, new RetainRatioDescCompare());
		Collections.sort(kosdaqAllStockList, new RetainRatioDescCompare());

		writeFile(kospiAllStockList, kospiFileName, "코스피 " + majorStockHolders + " 보유종목 보유율순");
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 " + majorStockHolders + " 보유종목 보유율순");

		Collections.sort(kospiAllStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqAllStockList, new RetainAmountDescCompare());

		writeFile(kospiAllStockList, kospiFileName, "코스피 " + majorStockHolders + " 보유종목 금액순");
		writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 " + majorStockHolders + " 보유종목 금액순");

	}

	public static List<StockVO> readOne(String stockCode) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockHompage(cnt, stockCode, "");
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	static long totalAmount = 0;

	public static List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				System.out.println(cnt + "." + read);
				stockCode = read.split("\t")[0];
				stockName = read.split("\t")[1];

				if (stockCode.length() != 6) {
					continue;
				}
				StockVO stock = getStockHompage(cnt, stockCode, stockName);
				if (stock != null) {
					stocks.add(stock);
					totalAmount += stock.getlRetainAmount();
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

	public List<StockVO> getStockMarketPrice(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		for (StockVO vo : stockList) {
			StockVO stock = getStockInfo(vo);
			stocks.add(stock);
		}
		return stocks;
	}

	// 일별시세
	// table class="type2"
	public StockVO getStockInfo(StockVO stock) {
		Document doc;
		String strStockCode = stock.getStockCode();
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/sise_day.nhn?code=" + strStockCode + "&page=1").get();
			// System.out.println("doc:"+doc);

			Elements type2s = doc.select(".type2");
			System.out.println("type2s.size:" + type2s.size());
			if (type2s.size() <= 0) {
				return null;
			}
			Element type2 = doc.select(".type2").get(0);
			Elements trElements = type2.select("tr");
			String date = "";
			String curPrice = "0";
			String varyPrice = "0";
			String beforePrice = "0";
			String startPrice = "0";
			String highPrice = "0";
			String lowPrice = "0";
			String tradingVolume = "0";
			String tradingAmount = "0";
			String maxPrice = "0";
			String minPrice = "0";

			int iCurPrice = 0;
			int iVaryPrice = 0;
			int iBeforePrice = 0;
			int iHighPrice = 0;
			int iLowPrice = 0;
			int iGapPrice = 0;
			int iMidPrice = 0;
			long lTradingVolume = 0;
			long lTradingAmount = 0;

			String specialLetter = "";
			String sign = "";
			String varyRatio = "";

			// Element trthElement = trElements.get(0);
			// Elements thElements = trthElement.select("th");
			// for (Element thElement : thElements) {
			// String th = thElement.text();
			// System.out.println("th:" + th);
			// }
			for (Element trElement : trElements) {
				// td
				Elements tdElements = trElement.select("td");
				System.out.println("tdElements.size:" + tdElements.size());
				if (tdElements.size() == 0) {
					continue;
				}
				if (tdElements.get(0).text().equals(strDate)) {
					date = tdElements.get(0).text();
					curPrice = tdElements.get(1).text();
					varyPrice = tdElements.get(2).text();
					System.out.println("varyPrice:" + varyPrice);
					startPrice = tdElements.get(3).text();
					highPrice = tdElements.get(4).text();
					lowPrice = tdElements.get(5).text();
					tradingVolume = tdElements.get(6).text();

					iCurPrice = Integer.parseInt(curPrice.replaceAll(",", ""));
					iVaryPrice = Integer.parseInt(varyPrice.replaceAll(",", ""));
					iHighPrice = Integer.parseInt(highPrice.replaceAll(",", ""));
					iLowPrice = Integer.parseInt(lowPrice.replaceAll(",", ""));
					iGapPrice = iHighPrice - iLowPrice;
					iMidPrice = iLowPrice + iGapPrice / 2;

					lTradingVolume = Long.parseLong(tradingVolume.replaceAll(",", ""));
					lTradingAmount = iMidPrice * lTradingVolume / 1000000;
					tradingAmount = NumberFormat.getNumberInstance(Locale.US).format(lTradingAmount);

					stock.setCurPrice(curPrice);
					stock.setiCurPrice(Integer.parseInt(StringUtils.defaultIfEmpty(curPrice, "0").replaceAll(",", "")));

					stock.setVaryPrice(varyPrice);
					stock.setiVaryPrice(
							Integer.parseInt(StringUtils.defaultIfEmpty(varyPrice, "0").replaceAll(",", "")));

					Elements imgElements = tdElements.get(2).select("img");
					String imgName = "";
					if (imgElements != null && imgElements.size() == 1) {
						Element imgElement = imgElements.get(0);
						if (imgElement != null) {
							imgName = imgElement.attr("src");
							imgName = imgName.substring(imgName.lastIndexOf("/") + 1);
							System.out.println("imgName:" + imgName);
							if (imgName.equals("ico_up.gif")) {
								specialLetter = "▲";
								sign = "+";
							} else if (imgName.equals("ico_down.gif")) {
								specialLetter = "▼";
								sign = "-";
							} else if (imgName.equals("ico_up02.gif")) {
								specialLetter = "↑";
								sign = "+";
							} else if (imgName.equals("ico_down02.gif")) {
								specialLetter = "↓";
								sign = "-";
							} else if (imgName.equals("ico_up.gif")) {
								specialLetter = "";
							}
						}
					}
					stock.setSpecialLetter(specialLetter);
					stock.setSign(sign);

					if (sign.equals("+")) {
						iBeforePrice = iCurPrice - iVaryPrice;
						double dVaryRatio = +((double) iVaryPrice / iBeforePrice) * 100;
						String tempVaryRatio = String.format("%,.2f", dVaryRatio);
						varyRatio = tempVaryRatio + " %";
					} else {
						iBeforePrice = iCurPrice + iVaryPrice;
						double dVaryRatio = -((double) iVaryPrice / iBeforePrice) * 100;
						String tempVaryRatio = String.format("%,.2f", dVaryRatio);
						varyRatio = tempVaryRatio + " %";
					}
					System.out.println("varyRatio:" + varyRatio);
					stock.setVaryRatio(varyRatio);
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));

					beforePrice = NumberFormat.getNumberInstance(Locale.US).format(iBeforePrice);

					stock.setBeforePrice(beforePrice);
					stock.setiBeforePrice(iBeforePrice);

					stock.setStartPrice(startPrice);
					stock.setiStartPrice(Integer.parseInt(startPrice.replaceAll(",", "")));

					stock.setHighPrice(highPrice);
					stock.setiHighPrice(Integer.parseInt(highPrice.replaceAll(",", "")));

					stock.setLowPrice(lowPrice);
					stock.setiLowPrice(Integer.parseInt(lowPrice.replaceAll(",", "")));

					stock.setTradingVolume(tradingVolume);
					stock.setiTradingVolume(Integer.parseInt(tradingVolume.replaceAll(",", "")));

					stock.setTradingAmount(tradingAmount);
					stock.setlTradingAmount(
							Long.parseLong(StringUtils.defaultIfEmpty(tradingAmount.replaceAll(",", ""), "0")));

					stock.setMaxPrice(maxPrice);
					stock.setiMaxPrice(Integer.parseInt(stock.getMaxPrice().replaceAll(",", "")));

					stock.setMinPrice(minPrice);
					stock.setiMinPrice(Integer.parseInt(stock.getMinPrice().replaceAll(",", "")));

				}
			}

			System.out.println("date:" + date);
			System.out.println("specialLetter:" + specialLetter);
			System.out.println("sign:" + sign);
			System.out.println("curPrice:" + curPrice);
			System.out.println("varyPrice:" + varyPrice);
			System.out.println("beforePrice:" + beforePrice);
			System.out.println("varyRatio:" + varyRatio);
			System.out.println("startPrice:" + startPrice);
			System.out.println("highPrice:" + highPrice);
			System.out.println("lowPrice:" + lowPrice);
			System.out.println("TradingVolume:" + tradingVolume);
			System.out.println("tradingAmount:" + tradingAmount);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	// 종목분석-기업현황
	// http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
	// 종목분석-기업개요
	// http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
	public static StockVO getStockHompage(int cnt, String code, String name) {
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
			stock.setStockName(name);

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

			String curPrice = "";
			int iCurPrice = 0;

			for (int i = 0; i < edds.size(); i++) {
				Element dd = edds.get(i);
				String text = dd.text();

				if (text.startsWith("현재가")) {
					// System.out.println("data:" + text);
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
				}
			}

			// 종목분석-기업현황
			doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();
			if (cnt == 1) {
				// System.out.println("title:" + doc.title());
				// System.out.println(doc.html());
			}

			Elements aClass = doc.select(".gHead01");
			Element aElem = null;
			if (aClass.size() <= 0) {
				return null;
			}
			if (aClass.size() > 1) {
				aElem = aClass.get(1);
			}
			System.out.println("aElem:" + aElem);
			if (aElem != null) {
				Elements tdElem = aElem.select("td");
				Element trElem = null;

				String retainVolume = "";
				long lRetainVolume = 0;

				String retainAmount = "";
				long lRetainAmount = 0;

				String retainRatio = "";
				float fRetainRatio = 0;

				long lRetainAmountTotal = 0;
				long lRetainVolumeTotal = 0;
				float fRetainRatioTotal = 0;

				stock.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

				for (Element td : tdElem) {
					String majorStockHolderName = td.attr("title");
					// if (title.equals(majorStockHolders)) {
					if (majorStockHolderName.indexOf(majorStockHolders) != -1) {
						System.out.println("title:" + majorStockHolderName);
						stock.setMajorStockHolders(majorStockHolderName);
						trElem = td.parent();
						System.out.println("trElem:" + trElem);

						Elements trTd = trElem.select("td");
						MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();
						for (int i = 0; i < trTd.size(); i++) {
							if (i == 1) {
								retainVolume = trTd.get(1).text();
								retainVolume = retainVolume.substring(0, retainVolume.length() - 1);
								lRetainVolume = Long.parseLong(retainVolume.replaceAll(",", ""));
								lRetainAmount = lRetainVolume * iCurPrice / 1000000;

								lRetainAmountTotal += lRetainAmount;
								lRetainVolumeTotal += lRetainVolume;

								DecimalFormat df = new DecimalFormat("#,##0");
								retainAmount = df.format(lRetainAmount);
							}
							if (i == 2) {
								retainRatio = trTd.get(2).text();
								retainRatio = retainRatio.substring(0, retainRatio.length() - 1);
								fRetainRatio = Float.parseFloat(retainRatio);

								fRetainRatioTotal += fRetainRatio;
							}
							majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
							majorStockHolderVO.setRetainVolume(retainVolume);
							majorStockHolderVO.setRetainAmount(retainAmount);
							majorStockHolderVO.setRetainRatio(retainRatio);
						}
						stock.getMajorStockHolderList().add(majorStockHolderVO);
					}
				}
				System.out.println(stock.getMajorStockHolderList().toString());

				stock.setlRetainVolume(lRetainVolumeTotal);
				stock.setlRetainAmount(lRetainAmountTotal);
				stock.setfRetainRatio(fRetainRatioTotal);

				if (stock.getMajorStockHolderList().size() > 0) {
					return stock;
				} else {
					return null;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

			sb1.append("<p><span style=\"font-size: 14pt;\"><b>").append(strYMD).append(title)
					.append("</b></span></p>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>전일비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>등락율</td>\r\n");

//            if (majorStockHolders.indexOf("국민연금") == -1) {
//                sb1.append(
//                        "<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
//            }
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유율</td>\r\n");
			sb1.append(
					"<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");

			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : list) {
				Vector lst = s.getMajorStockHolderList();
				int listSize = lst.size();
				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td rowspan=").append(listSize).append(">").append(cnt++).append("</td>\r\n");
					sb1.append("<td rowspan=").append(listSize).append("><a href='").append(url).append("'>")
							.append(s.getStockName()).append("</a></td>\r\n");

					for (int i = 0; i < listSize; i++) {
						if (i > 0) {
							sb1.append("<tr>\r\n");
						}

						MajorStockHolderVO vo = (MajorStockHolderVO) lst.get(i);
//                        if (majorStockHolders.indexOf("국민연금") == -1) {
//                            sb1.append("			<td>" + vo.getMajorStockHolderName() + "</td>\r\n");
//                        }
						String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
						String curPrice = s.getCurPrice();
						String varyPrice = s.getVaryPrice();
						String varyRatio = s.getVaryRatio();

						System.out.println("specialLetter+++>" + specialLetter);
						System.out.println("varyPrice+++>" + varyPrice);

						if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
								|| specialLetter.startsWith("+")) {
							sb1.append("<td style='text-align:right;color:red'>")
									.append(StringUtils.defaultIfEmpty(curPrice, "")).append("</td>\r\n");
							sb1.append("<td style='text-align:right;color:red'>")
									.append(StringUtils.defaultIfEmpty(varyPrice, "")).append("</td>\r\n");
							sb1.append("<td style='text-align:right;color:red'>").append(specialLetter).append(" ")
									.append(varyRatio).append("</td>\r\n");
						} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
								|| specialLetter.startsWith("-")) {
							sb1.append("<td style='text-align:right;color:blue'>")
									.append(StringUtils.defaultIfEmpty(curPrice, "")).append("</td>\r\n");
							sb1.append("<td style='text-align:right;color:blue'>")
									.append(StringUtils.defaultIfEmpty(varyPrice, "")).append("</td>\r\n");
							sb1.append("<td style='text-align:right;color:blue'>").append(specialLetter).append(" ")
									.append(varyRatio).append("</td>\r\n");
						} else {
							sb1.append("<td style='text-align:right;color:metal'>")
									.append(StringUtils.defaultIfEmpty(curPrice, "")).append("</td>\r\n");
							sb1.append("<td style='text-align:right'>0</td>\r\n");
							sb1.append("<td style='text-align:right'>0%</td>\r\n");
						}
						sb1.append("	<td style='text-align:right'>").append(vo.getRetainVolume())
								.append("</td>\r\n");
						sb1.append("	<td style='text-align:right'>").append(vo.getRetainRatio())
								.append("%</td>\r\n");
						sb1.append("	<td style='text-align:right'>").append(vo.getRetainAmount())
								.append("</td>\r\n");
						sb1.append("</tr>\r\n");
					}
				}
			}
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			fw.write(sb1.toString());
			System.out.println(sb1.toString());
			fw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {

		}
	}

}
