package html.parsing.stock.focus;

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
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.NameAscCompare;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;

public class Weeks52NewLowHighPriceInput {

	
	private static Logger logger1 = null;
	private static java.util.logging.Logger logger2 = null;
	public final static String USER_HOME = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYyyyMmDd = new SimpleDateFormat("yyyy년 M월 d일
	// E",Locale.KOREAN).format(new Date());
	int iYyyyMmDd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYyyyMmDd = strDefaultDate.replaceAll("\\.", "-");
	String strYyyyMmDdBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	List<StockVO> newLowPriceList = new ArrayList<StockVO>();
	List<StockVO> newHighPriceList = new ArrayList<StockVO>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Weeks52NewLowHighPriceInput(1);
	}

	Weeks52NewLowHighPriceInput() {

		logger1 = LoggerFactory.getLogger(this.getClass());

		readOne("053610", "프로텍");
		writeFile(newLowPriceList, kospiFileName, "코스피 신저가", "LOW");
		writeFile(newHighPriceList, kospiFileName, "코스피 신고가", "HIGH");

	}

	Weeks52NewLowHighPriceInput(int i) {

		logger1 = LoggerFactory.getLogger(this.getClass());
		
		String strInputDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
		System.out.println("strInputDate:" + strInputDate);
		if (strInputDate != null) {
			strDefaultDate = strInputDate;
			String year = strDefaultDate.substring(0, 4);
			String month = strDefaultDate.substring(5, 7);
			String day = strDefaultDate.substring(8, 10);
			int iYear = Integer.parseInt(year);
			int iMonth = Integer.parseInt(month) - 1;
			int iDay = Integer.parseInt(day);

			iYyyyMmDd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
			strYyyyMmDd = strDefaultDate.replaceAll("\\.", "-");
			strYyyyMmDdBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";
		}
		logger1.debug("strYMD2:[" + strYyyyMmDd + "]");
		logger2.log(java.util.logging.Level.INFO, "strYMD1:[" + strYyyyMmDd + "]");

		Properties props = new Properties();
		try {
			// InputStream is = new FileInputStream("log4j.properties");
			// props.load(new FileInputStream("log4j.properties"));
			// InputStream is =
			// getClass().getResourceAsStream("/log4j.properties");
			props.load(getClass().getResourceAsStream("log4j.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyConfigurator.configure(props);

		// ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// URL url = loader.getResource("log4j.properties");
		// PropertyConfigurator.configure(url);
		File log4jfile = new File("log4j.properties");
		String absolutePath = log4jfile.getAbsolutePath();
		PropertyConfigurator.configure(absolutePath);

		// 모든 주식 정보를 조회한다.
		// 코스피
		readFile("코스피", kospiFileName);
		// 코스피 신저가
		Collections.sort(newLowPriceList, new NameAscCompare());
		// 코스피 신고가
		Collections.sort(newHighPriceList, new NameAscCompare());

		writeFile(newLowPriceList, kospiFileName, "코스피 신저가", "LOW");
		writeFile(newHighPriceList, kosdaqFileName, "코스피 신고가", "HIGH");

		newLowPriceList.clear();
		newHighPriceList.clear();

		// 코스닥
		readFile("코스닥", kosdaqFileName);
		// 코스닥 신저가
		Collections.sort(newLowPriceList, new NameAscCompare());
		// 코스닥 신고가
		Collections.sort(newHighPriceList, new NameAscCompare());

		writeFile(newLowPriceList, kospiFileName, "코스닥 신저가", "LOW");
		writeFile(newHighPriceList, kosdaqFileName, "코스닥 신고가", "HIGH");

	}

	public void readOne(String stockCode, String stockName) {
		int cnt = 1;
		strStockCode = stockCode;
		strStockName = stockName;
		getStockInfo(cnt, strStockCode, strStockName);
	}

	public void readFile(String kospidaq, String fileName) {

		File f = new File(USER_HOME + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				System.out.println(cnt + "." + read);
				strStockCode = read.split("\t")[0];
				strStockName = read.split("\t")[1];
				System.out.println(strStockCode + "\t" + strStockName);

				if (strStockCode.length() != 6) {
					continue;
				}
				getStockInfo(cnt, strStockCode, strStockName);
				cnt++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
		}
	}

	public StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/sise_day.nhn?code=" + strStockCode + "&page=1").get();
			// System.out.println("doc:"+doc);

			Elements type5s = doc.select(".type5");
			System.out.println("type5s.size:" + type5s.size());
			Element type5 = doc.select(".type5").get(0);

			Elements trElements = type5.select("tr");
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
				if (tdElements.get(0).text().equals(strDefaultDate)) {
					date = tdElements.get(0).text();
					curPrice = tdElements.get(1).text();
					varyPrice = tdElements.get(2).text();
					startPrice = tdElements.get(3).text();
					highPrice = tdElements.get(4).text();
					lowPrice = tdElements.get(5).text();
					tradingVolume = tdElements.get(6).text();

					if (tradingVolume.equals("0")) {
						return null;
					}

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

					break;
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

			// 종목분석-기업현황
			doc = Jsoup.connect("http://companyinfo.stock.naver.com/company/c1010001.aspx?cmp_cd=" + strStockCode)
					.get();

			Element edd = doc.getElementById("cTB11");
			Element td0 = edd.select("td").first();
			Element td1 = edd.select("td").get(1);

			String strTd0[] = td0.text().split("/");
			String strTd1[] = td1.text().split("/");

			// String curPrice = strTd0[0].substring(0, strTd0[0].indexOf("원"));
			String weeks52MaxPrice = strTd1[0].substring(0, strTd1[0].indexOf("원"));
			String weeks52MinPrice = strTd1[1].substring(0, strTd1[1].indexOf("원"));

			// stock.setCurPrice(curPrice);
			stock.setWeeks52MaxPrice(weeks52MaxPrice);
			stock.setWeeks52MinPrice(weeks52MinPrice);

			// curPrice = curPrice.replaceAll(",", "").trim();
			weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
			weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();

			System.out.println("curPrice:" + curPrice);
			System.out.println("weeks52MaxPrice:" + weeks52MaxPrice);
			System.out.println("weeks52MinPrice:" + weeks52MinPrice);

			int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
			int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);

			if (stock.getiHighPrice() >= iWeeks52MaxPrice) {
				newHighPriceList.add(stock);
			} else if (stock.getiLowPrice() <= iWeeks52MinPrice) {
				newLowPriceList.add(stock);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return stock;
	}

	public void writeFile(List<StockVO> stockList, String fileName, String title, String gubun) {
		try {
			FileWriter fw = new FileWriter(
					USER_HOME + "\\documents\\" + strYyyyMmDdBracket + "_" + title.replaceAll(" ", "_") + ".html");
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
			sb1.append("\t<h2>" + strYyyyMmDdBracket + " " + title + "</h2>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			if (gubun.equals("LOW")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>저가</td>\r\n");
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최저가</td>\r\n");
			} else if (gubun.equals("HIGH")) {
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>고가</td>\r\n");
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 최고가</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : stockList) {
				if (s != null) {
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

					String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
					} else {
						sb1.append(
								"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
					}

					if (gubun.equals("LOW")) {
						sb1.append("<td style='text-align:right'>" + s.getLowPrice() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + s.getWeeks52MinPrice() + "</td>\r\n");
					} else if (gubun.equals("HIGH")) {
						sb1.append("<td style='text-align:right'>" + s.getHighPrice() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + s.getWeeks52MaxPrice() + "</td>\r\n");
					}
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br><br>\r\n");

			for (StockVO s : stockList) {
				if (s != null) {
					Document classAnalysisDoc = Jsoup.connect(
							"http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + s.getStockCode())
							.get();
					// System.out.println("classAnalysisDoc:"+classAnalysisDoc);
					Elements comment = classAnalysisDoc.select(".cmp_comment");
					sb1.append("<div>\n");
					sb1.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" + s.getStockCode() + "'>"
							+ s.getStockName() + "(" + s.getStockCode() + ")" + "</a></h4>\n");
					sb1.append("<p>\n");
					sb1.append(comment + "\n");
					sb1.append("</p>");
					sb1.append("</div>\n");
					sb1.append("<br>\n");
				}
			}

			// 뉴스 첨부
			StringBuilder newsAddedStockList = StockUtil.getNews(stockList);
			// 증권명에 증권링크 생성
			StringBuilder stockTableAdded = StockUtil.stockLinkString(newsAddedStockList, stockList);
			sb1.append(stockTableAdded.toString());

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

	public void readNews(List<StockVO> allStockList) {

		int cnt = 1;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(USER_HOME + "\\documents\\NewsTest." + strDate + ".html");
			StringBuilder sb1 = new StringBuilder();

			for (StockVO vo : allStockList) {
				strStockCode = vo.getStockCode();
				strStockName = vo.getStockName();

				System.out.println(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

				Document doc = Jsoup
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=").get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=

				Element e1 = doc.select(".type5").get(0);

				Elements trs = e1.getElementsByTag("tr");

				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);

					Elements tds = tr.getElementsByTag("td");
					if (tds.size() < 3) {
						continue;
					}

					Element a1 = tr.getElementsByTag("a").first();
					Element source = tr.getElementsByTag("td").get(2);
					Element dayTime = tr.getElementsByTag("span").first();

					System.out.println("title:" + a1.html());
					System.out.println("href:" + a1.attr("href"));
					System.out.println("source:" + source.html());
					System.out.println("dayTime:" + dayTime.html());

					String strTitle = a1.html();
					String strLink = a1.attr("href");
					String strSource = source.html();
					String strDayTime = dayTime.html();
					String strYyyyMmDd2 = strDayTime.substring(0, 10);
					int iYyyyMmDd2 = Integer.parseInt(strYyyyMmDd2.replaceAll("\\.", ""));

					// sb1.append("<h3>"+ strTitle +"</h3>\n");
					// sb1.append("<div>"+ strSource+" | "+ strDayTime
					// +"</div>\n");
					if (iYyyyMmDd <= iYyyyMmDd2) {
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
								+ strStockName + "(" + strStockCode + ")" + "</a></h3>\n");

						doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
						Elements link_news_elements = doc.select(".link_news");
						if (link_news_elements != null) {
							link_news_elements.remove();
						}
						Elements naver_splugin = doc.select(".naver-splugin");
						System.out.println("naver_splugin:" + naver_splugin);
						if (naver_splugin != null) {
							naver_splugin.remove();
						}
						Element view = doc.select(".view").get(0);

						String strView = view.toString();
						strView = strView.replaceAll(strStockName,
								"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
										+ strStockName + "</a>");

						sb1.append(strView);
						sb1.append("<br><br>\n");

						System.out.println("view:" + view);
					}
				}
			}

			System.out.println(sb1.toString());

			fw.write(sb1.toString());
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
