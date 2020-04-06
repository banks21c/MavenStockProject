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

import html.parsing.stock.news.News;

public class StockNewsToday extends News {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockNewsToday.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDate = sdf.format(new Date());
	String strDate2 = strDate.replaceAll("\\.", "-");

	String strStockCodeOrName = "코오롱생명과학";
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockName = "삼성물산";
	String strStockCode = "028260";
	boolean findYn = false;

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	StockVO stock = new StockVO();
	List<StockVO> allStockList = new ArrayList<>();
	List<StockVO> kospiStockList = new ArrayList<>();
	List<StockVO> kosdaqStockList = new ArrayList<>();
	List<StockVO> searchStockList = new ArrayList<StockVO>();

	DecimalFormat df = new DecimalFormat("###.##");
	Calendar cal1 = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();

	List<StockNewsVO> stockNewsList1 = new ArrayList<StockNewsVO>();
	List<StockNewsVO> stockNewsList2 = new ArrayList<StockNewsVO>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StockNewsToday(1);
	}

	StockNewsToday() {


		// List<Stock> kospiStockList = readOne("071970");
		// writeFile(kospiStockList,kospiFileName,"코스피");

		readOne(strStockCode, strStockName);
		System.out.println("stock:" + stock);
		writeFile(stock, kosdaqFileName, stock.getStockName());

	}

	StockNewsToday(int i) {

		// MakeKospiKosdaqList.makeKospiKosdaqList();
		strDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDate);
		strDate2 = strDate.replaceAll("\\.", "-");
		System.out.println("strDate:" + strDate);
		System.out.println("strDate2:" + strDate2);
		String year = strDate.substring(0, 4);
		String month = strDate.substring(5, 7);
		String day = strDate.substring(8, 10);
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month) - 1;
		int iDay = Integer.parseInt(day);
		// System.out.println(year + month + day);

		cal1.set(iYear, iMonth, iDay);
		// 코스피
		simpleReadFile(kospiStockList, "코스피", kospiFileName);
		writeFile(kospiStockList, kospiFileName, "오늘의 코스피 뉴스");

		// 코스닥
		simpleReadFile(kosdaqStockList, "코스닥", kosdaqFileName);
		writeFile(kosdaqStockList, kosdaqFileName, "오늘의 코스닥 뉴스");
	}

	public void readOne(String stockCode, String stockName) {
		int cnt = 1;
		getStockInfo(cnt, stockCode, stockName);
	}

	public void simpleReadFile(List<StockVO> stockList, String kospidaq, String fileName) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int stockNameLength = 0;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				System.out.println(cnt + "." + read);
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
				stockList.add(stock1);
				allStockList.add(stock1);
				cnt++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
		}
	}

	public void readFile(String kospidaq, String fileName) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				System.out.println(cnt + "." + read);
				String stockCode = read.split("\t")[0];
				String stockName = read.split("\t")[1];

				if (stockCode.length() != 6) {
					continue;
				}
				getStockInfo(cnt, stockCode, stockName);
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
		System.out.println("code:" + strStockCode);
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);

		try {
			stock.setStockCode(strStockCode);
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			// System.out.println("doc:"+doc);

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
			System.out.println("market" + market);

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
				// System.out.println("text:" + text);
				if (text.startsWith("종목명")) {
					String stockName = text.substring(4);
					// System.out.println("stockName:" + stockName);
					stock.setStockName(stockName);
				}

				if (text.startsWith("현재가")) {
					// System.out.println("data1:" + dd.text());
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
					// System.out.println("txts.length:" + txts.length);
					if (txts.length == 7) {
						stock.setVaryRatio(txts[5] + txts[6]);
					} else if (txts.length == 8) {
						stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
					}
					varyRatio = stock.getVaryRatio();
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
					// System.out.println("상승률:" + stock.getVaryRatio());
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
					stock.setiTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
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
			}
			if (specialLetter.equals("↓")) {
				stock.setStockGubun("하한가↓");
				stock.setLineUp(21);
			}

			String highPrice = stock.getHighPrice();
			String lowPrice = stock.getLowPrice();
			String maxPrice = stock.getMaxPrice();
			System.out.println("highPrice:" + highPrice);
			System.out.println("lowPrice:" + lowPrice);
			System.out.println("maxPrice:" + maxPrice);
			// 고가가 0이 아니고 고가가 상한가인가?
			System.out.println(!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice()));
			// 현재가가 상한가가 아닌가?
			System.out.println(!curPrice.equals(stock.getMaxPrice()));
			// 고가가 상한가인가?
			System.out.println(highPrice.equals(stock.getMaxPrice()));
			// 고가가 0이 아니고 고가가 상한가인가?
			if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
				&& !curPrice.equals(stock.getMaxPrice())) {
				stock.setStockGubun("상터치↑↘");
				stock.setLineUp(12);
			}
			if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
				&& !curPrice.equals(stock.getMinPrice())) {
				stock.setStockGubun("하터치↓↗");
				stock.setLineUp(22);
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
				System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
				float upDownRatio = higher / icur * 100;
				// upDownRatio = ((int)(upDownRatio * 100))/100f;
				String strUpDownRatio = df.format(upDownRatio);
				if (higher / icur * 100 > 10 && iTradingVolume > 0) {
					stock.setStockGubun(strUpDownRatio + "%" + flag);
					stock.setLineUp(16);
				}
			} else {
				higher = Math.abs(icur - ilow);
				flag = "↘↗";
				System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
				float upDownRatio = higher / icur * 100;
				// upDownRatio = ((int)(upDownRatio * 100))/100f;
				String strUpDownRatio = df.format(upDownRatio);
				if (upDownRatio > 10 && iTradingVolume > 0) {
					stock.setStockGubun(strUpDownRatio + "%" + flag);
					stock.setLineUp(16);
				}
			}

			float fRatio = 0f;
			if (varyRatio.indexOf("%") != -1) {
				fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
				if (fRatio >= 5) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun("+5%이상↗");
						stock.setLineUp(13);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun("-5%이상↘");
						stock.setLineUp(23);
					}
				}
			}
			allStockList.add(stock);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(List<StockVO> list, String fileName, String title) {
		// File f = new File(userHome + "\\documents\\" + fileName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
			if (strDate.equals("")) {
				strDate = sdf.format(new Date());
				strDate2 = sdf2.format(new Date());
			}

			FileWriter fw = new FileWriter(userHome + "\\documents\\[" + strDate2 + "]_" + title + ".html");
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
			sb1.append("\t<h2>" + "[" + strDate2 + "] " + title + "</h2>");

			// 뉴스 첨부
			sb1.append(getAllNews(list).toString());

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

	public void writeFile(StockVO stock, String fileName, String title) {
		// File f = new File(userHome + "\\documents\\" + fileName);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
			if (strDate.equals("")) {
				strDate = sdf.format(new Date());
				strDate2 = sdf2.format(new Date());
			}

			FileWriter fw = new FileWriter(userHome + "\\documents\\[" + strDate2 + "]_" + title + ".html");
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
			sb1.append("\t<h2>" + "[" + strDate2 + "] " + title + "</h2>");

			// 뉴스 첨부
			sb1.append(getNews(stock).toString());

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

	public StringBuilder getAllNews(List<StockVO> stockList) {
		System.out.println("stockList.size:" + stockList.size());

		StringBuilder sb = new StringBuilder();

		for (StockVO stock : stockList) {
			// 중복 뉴스 체크 로직 구현해야....
			StringBuilder sb1 = getNews(stock);
			sb.append(sb1);
		}
		return sb;
	}

	public StringBuilder getNews(StockVO stock) {

		StringBuilder sb1 = new StringBuilder();

		strStockCode = stock.getStockCode();
		strStockName = stock.getStockName();

		// 종합정보
		System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
		// String strDate = sdf.format(new Date());
		System.out.println("날짜:" + strDate);

		Document doc;
		try {
			String url = "http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=";

			News gurl = new News();
			gurl.getURL(url);
			String protocol = gurl.getProtocol();
			String host = gurl.getHost();
			String path = gurl.getPath();
			String protocolHost = gurl.getProtocolHost();

			doc = Jsoup.connect(url).get();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);

			Element e1 = doc.select(".type5").get(0);

			Elements trs = e1.getElementsByTag("tr");

			for (int i = 0; i < trs.size(); i++) {
				Element tr = trs.get(i);

				Elements tds = tr.getElementsByTag("td");
				if (tds.size() < 3) {
					continue;
				}

				Element a1 = tr.getElementsByTag("a").first();
				Element source = tr.select(".info").get(0);
				Element dayTime = tr.select(".date").get(0);

				if (a1 == null) {
					continue;
				}

				String strTitle = a1.html();
				String strLink = a1.attr("href");
				String strSource = source.html();
				String strDayTime = dayTime.html();
				String yyyyMMdd = strDayTime.substring(0, 10);

				// System.out.println("strTitle:" + strTitle);
				// System.out.println("strLink:" + strLink);
				// System.out.println("strSource:" + strSource);
				// System.out.println("strDayTime:" + strDayTime);
				// System.out.println("strDayTimeText:" + strDayTimeText);
				// System.out.println("yyyyMMdd:" + yyyyMMdd);
				// System.out.println("strDate:" + strDate);
				if (strDate.equals(yyyyMMdd)) {

					doc = Jsoup.connect(strLink).get();
					doc.select("meta").remove();
					doc.select("link").remove();
					doc.select("script").remove();
					Elements link_news_elements = doc.select(".link_news");
					if (link_news_elements != null) {
						link_news_elements.remove();
					}

					Elements naver_splugin = doc.select(".naver-splugin");
					System.out.println("naver_splugin:" + naver_splugin);
					if (naver_splugin != null) {
						naver_splugin.remove();
					}

					doc.select("a").remove();
					doc.select("li").remove();
					Element view = doc.select(".view").get(0);
					System.out.println("view.className:" + view.className());
					System.out.println("attr(class):" + view.attr("class"));
					view.attr("style", "width:548px;");
					view.select("td").attr("style", "border:0;");

					Elements tables = view.select("table");
					for (Element table : tables) {
						String width = table.attr("width");
						if (width != null && !width.equals("")) {
							int iWidth = Integer.parseInt(width);
							if (iWidth > 548) {
								table.attr("width", "548");
							}
						}
						String cellspacing = table.attr("cellspacing");
						if (cellspacing != null && !cellspacing.equals("")) {
							int iCellspacing = Integer.parseInt(cellspacing);
							if (iCellspacing > 0) {
								table.attr("cellspacing", "0");
							}
						}
					}
					Elements imgTables = view.select("table#ImgTable");
					System.out.println("imgTables.size():" + imgTables.size());
					System.out.println("imgTables1:" + imgTables);
					for (Element imgTable : imgTables) {
						System.out.println("imgTable:" + imgTable);
						Elements imgs = imgTable.select(".end_photo_org");
						System.out.println("imgs.size():" + imgs.size());
						System.out.println("imgs:" + imgs);
						Element img = null;
						if (imgs.size() > 0) {
							img = imgTable.select(".end_photo_org").get(0);
							imgTable.html("");
							System.out.println("img1:" + img);
							//imgTable.html(img.toString());
							imgTable.replaceWith(img);
							System.out.println("imgTable:" + imgTable);
						}
					}
					System.out.println("imgTables2:" + imgTables);

					System.out.println("attr(style):" + view.attr("style"));

					String strView = view.toString();
					//System.out.println("strView:" + strView);
					System.out.println("strStockName:" + strStockName);
					strView = strView.replaceAll(strStockName, "<a href='http://finance.naver.com/item/main.nhn?code="
						+ strStockCode + "'>" + strStockName + "</a>");
					strView = strView.replaceAll("\\[\\]", "");
					strView = strView.replaceAll("<ul></ul>", "");
					strView = strView.replaceAll("<br>\r\n<br>\r\n<br>", "<br><br>");
					strView = strView.replaceAll("&amp;", "&");

					strView = strView.replaceAll("<script type=\"text/javascript\" src=\"/js/news_read.js\"></script>",
						"");

					String title = view.select("tr th strong").get(0).text();
					Element dateElement = view.select("tr th span span").get(0);
					String date = dateElement.text();

					Element companyElement = view.select("tr th span").get(0);
					companyElement.select("img").remove();
					companyElement.select("span").remove();
					String company = companyElement.text();

					System.out.println("title:" + title);
					System.out.println("company:" + company);
					System.out.println("date:" + date);
					System.out.println("stockNewsList1:" + stockNewsList1);
					System.out.println("stockNewsList1.size:" + stockNewsList1.size());
					// 소스 수정필요
					StockNewsVO newsVO = new StockNewsVO();
					newsVO.setTitle(title);
					newsVO.setCompany(company);
					newsVO.setDate(date);

					if (stockNewsList1.size() == 0) {
						stockNewsList1.add(newsVO);
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
							+ strStockName + "(" + strStockCode + ")</a></h3>");
						sb1.append(strView);
						sb1.append("<br><br>\n");
					} else {
						for (StockNewsVO sNewsVO : stockNewsList1) {
							String tempTitle = sNewsVO.getTitle();
							String tempCompany = sNewsVO.getCompany();
							String tempDate = sNewsVO.getDate();

							System.out.println("tempTitle:" + tempTitle);
							System.out.println("tempCompany:" + tempCompany);
							System.out.println("tempDate:" + tempDate);
							System.out.println("title.equals(tempTitle):" + title.equals(tempTitle));
							System.out.println("company.equals(tempCompany):" + company.equals(tempCompany));
							System.out.println("date.equals(tempDate):" + date.equals(tempDate));

							if ((title.equals(tempTitle) && company.equals(tempCompany) && date.equals(tempDate))) {
								System.out.println("다 같으면...");
								newsVO = null;
							}
						}
						System.out.println("newsVO:" + newsVO);
						if (newsVO != null) {
							System.out.println("strStockCode:" + strStockCode);
							System.out.println("strStockName:" + strStockName);
							stockNewsList1.add(newsVO);
							sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
								+ strStockName + "(" + strStockCode + ")</a></h3>");
							sb1.append(strView);
							sb1.append("<br><br>\n");
						}
					}
				} else {
					break;
				}
			}
			System.out.println("=========================================");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 증권명에 증권링크 생성
		sb1 = StockUtil.stockLinkString(sb1, allStockList);
		return sb1;
	}

}
