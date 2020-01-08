package html.parsing.stock;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.StockNameLengthDescCompare;

public class StockThemeAll {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockThemeAll.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
	String strYmd = sdf0.format(new Date());
	int iYmd = Integer.parseInt(strYmd);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E
	// ",Locale.KOREAN).format(new Date());
	String strYMD = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";
	String strDate = "";
	// String strThemeName = "2차전지 관련주";
	// String strThemeCode = "";
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	List<StockVO> allStockList = new ArrayList<StockVO>();
	List<StockVO> searchList = new ArrayList<StockVO>();

	Calendar cal1 = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();

	List<StockNewsVO> stockNewsList1 = new ArrayList<StockNewsVO>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StockThemeAll stockThemeAll = new StockThemeAll(1);
	}

	StockThemeAll() {



		// 코스피
		readFile("코스피", kospiFileName);
		// 코스닥
		readFile("코스닥", kosdaqFileName);


		Collections.sort(allStockList, new StockNameLengthDescCompare());

		writeThemeMarketPrice();
	}

	StockThemeAll(int i) {

		// MakeKospiKosdaqList.makeKospiKosdaqList();
		strDate = strDefaultDate;
		String year = strDate.substring(0, 4);
		String month = strDate.substring(5, 7);
		String day = strDate.substring(8, 10);
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month) - 1;
		int iDay = Integer.parseInt(day);
		// System.out.println(year + month + day);

		cal1.set(iYear, iMonth, iDay);

		// 코스피
		readFile("코스피", kospiFileName);
		// 코스닥
		readFile("코스닥", kosdaqFileName);


		Collections.sort(allStockList, new StockNameLengthDescCompare());

		writeThemeMarketPrice();
	}

	public void readOne(String stockCode) {
		int cnt = 1;
		StockVO stock = new StockVO();
		stock.setStockCode(stockCode);
	}

	private void readFile(String kospidaq, String fileName) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int stockNameLength = 0;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				// System.out.println(cnt + "." + read);
				stockCode = read.split("\t")[0];
				stockName = read.split("\t")[1];
				stockNameLength = stockName.length();

				StockVO stock1 = new StockVO();
				stock1.setStockCode(stockCode);
				stock1.setStockName(stockName);
				stock1.setStockNameLength(stockNameLength);

				if (stockCode.length() != 6) {
					continue;
				}
				allStockList.add(stock1);
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

	private void writeThemeMarketPrice() {
		Document doc;

		try {
			// 테마목록
			doc = Jsoup.connect("http://finance.naver.com/sise/theme.nhn").get();
			String linkHref = doc.select("link").get(0).attr("href");
			String linkHrefDate = linkHref.substring(linkHref.indexOf("?") + 1);
			System.out.println("linkHrefDate:" + linkHrefDate);
			// System.out.println("doc:"+doc);

			Elements themeTable = doc.select("table.type_1.theme");
			// System.out.println("themeTable:"+themeTable);
			Elements themeTr = themeTable.select("tr");
			System.out.println("themeTr.size:" + themeTr.size());
			StringBuffer themeMarketPrice = new StringBuffer("<h1>테마주 시세 보기</h1>");
			for (int i = 3; i < themeTr.size(); i++) {
				Element tr = themeTr.get(i);
				// System.out.println("tr:" + tr);
				Elements as = tr.select("a");
				// System.out.println("as:" + as+" as.size:"+as.size());
				if (as != null && as.size() > 0) {
					String strThemeName = as.first().text();
					System.out.println("테마명:" + strThemeName);
					String themeLink = tr.select("a").first().attr("href");
					if (themeLink != null && themeLink.indexOf("&no=") != -1) {
						String strThemeCode = themeLink.substring(themeLink.indexOf("&no=") + 4);
						System.out.println("strThemeCode1:" + strThemeCode);
						themeMarketPrice.append("\t<h2>" + strThemeName + "</h2>");
						themeMarketPrice.append(getThemeMarketPrice2(strThemeCode));
					}
				}
			}
			writeFile(themeMarketPrice.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getThemeMarketPrice2(String strThemeCode) {
		Document doc = null;
		String themeMarketPrice = "";
		if (strThemeCode != null) {
			try {
				doc = Jsoup.connect("http://finance.naver.com/sise/sise_group_detail.nhn?type=theme&no=" + strThemeCode)
					.get();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Elements themeStockTables = doc.select(".type_5");
			Element themeStockTable = themeStockTables.get(0);
			themeStockTable.attr("style", "margin:0 0 10px 0; width:548px;");
			// System.out.println("themeStockTable:"+themeStockTable);
			Element themeStockTableColgroup = themeStockTable.select("colgroup").get(0);
			themeStockTableColgroup.remove();
			// Elements themeStockTableCol = themeStockTableColgroup.select("col");
			// for(int i=0;i<themeStockTableCol.size();i++){
			// Element col = themeStockTableCol.get(i);
			// if (i == 4 || i == 5 || i == 8) {
			// col.remove();
			// }
			// }
			searchList.clear();
			themeStockTable.select("caption").remove();
			Elements themeStockTableTr = themeStockTable.select("tr");
			themeStockTableTr.removeAttr("onmouseover");
			themeStockTableTr.removeAttr("onmouseout");
			for (int i = 0; i < themeStockTableTr.size(); i++) {
				Element tr = themeStockTableTr.get(i);
				Elements ths = tr.select("th");
				for (int j = 0; j < ths.size(); j++) {
					Element th = ths.get(j);
					th.attr("style", "white-space:nowrap");

					if (j == 4 || j == 5 || j == 8 || j == 9) {
						th.remove();
					}
				}

				Elements tds = tr.select("td");
				String color = "";
				if (!tds.isEmpty() && tds.size() > 2) {
					System.out.println("tds.size:" + tds.size());
					Elements spanElements = tds.get(2).select("span");
					if (!spanElements.isEmpty()) {
						Element spanElement = spanElements.get(0);
						if (spanElement != null && !spanElement.text().equals("")) {
							String spanClass = spanElement.attr("class");
							System.out.println("spanClass:" + spanClass);
							if (spanClass.contains("nv01")) {
								color = "blue";
								spanElement.attr("style", "color:blue");
							} else if (spanClass.contains("red01") || spanClass.contains("red02")) {
								color = "red";
								spanElement.attr("style", "color:red");
							}
							System.out.println("color :" + color);
						}
					}
				}

				String upDown = tds.select("img").attr("alt");
				for (int j = 0; j < tds.size(); j++) {
					Element td = tds.get(j);

					if (j == 4 || j == 5 || j == 8 || j == 9) {
						td.remove();
					}
					td.removeAttr("style");
					if (j == 0) {
						td.attr("style", "text-align:left;padding:0 5px;");
					} else {
						if (upDown.equals("상승")) {
							td.attr("style", "text-align:right;padding:0 5px;color:red;");
						} else if (upDown.equals("하락")) {
							td.attr("style", "text-align:right;padding:0 5px;color:blue");
						} else {
							td.attr("style", "text-align:right;padding:0 5px;");
						}
					}
					td.removeClass("number");
					td.removeAttr("class");
					System.out.println("span:" + td.select("span"));
				}
				if (tds.size() == 10) {
					Element a = tds.select("a").get(0);
					String href = a.attr("href");
					String stockCode = "";
					if (href != null && href.indexOf("code=") != -1) {
						stockCode = href.substring(href.indexOf("code=") + 5);
					}
					String stockName = a.text();
					System.out.println(stockCode + ":" + stockName);

					StockVO stock = new StockVO();
					stock.setStockCode(stockCode);
					stock.setStockName(stockName);

					searchList.add(stock);
				}
			}
			themeMarketPrice = themeStockTable.outerHtml();
			themeMarketPrice = themeMarketPrice.replaceAll("<a href=\"/", "<a href=\"http://finance.naver.com/");
		}
		return themeMarketPrice;
	}

	public void writeFile(String themeMarketPrice) {
		try {
			FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_테마주_시세_보기.html");
			StringBuilder sb1 = new StringBuilder();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    th {border:1px solid #aaaaaa;background:#00c73c;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td.tar {text-align:right}\r\n");
			sb1.append("    td.red {color:red;}\r\n");
			sb1.append("    td.blue {color:blue;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(themeMarketPrice + "\r\n");

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
			System.out.println("file write finished...");
		}
	}

}
