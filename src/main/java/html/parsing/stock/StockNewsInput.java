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

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockNewsVO;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.StockNameLengthDescCompare;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;

public class StockNewsInput {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockNewsInput.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E
	// ",Locale.KOREAN).format(new Date());
	String strYMD = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";
	String strDate = "";
	String strStockCodeOrName = "롯데케미칼";
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	StockVO stock = new StockVO();
	List<StockVO> allStockList = new ArrayList<StockVO>();
	List<StockVO> searchStockList = new ArrayList<StockVO>();

	Calendar cal1 = Calendar.getInstance();
	Calendar cal2 = Calendar.getInstance();

	List<StockNewsVO> stockNewsList1 = new ArrayList<StockNewsVO>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StockNewsInput(1);
	}

	StockNewsInput() {

		// List<Stock> kospiStockList = readOne("071970");
		// writeNewsFile(kospiStockList,kospiFileName,"코스피");

		// readOne("246690", "티에스인베스트먼트");
		writeNewsFile(searchStockList);

	}

	StockNewsInput(int i) {

		// MakeKospiKosdaqList.makeKospiKosdaqList();
		strDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
		if (strDate.equals("")) {
			strDate = strDefaultDate;
		}
		strYMD = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";

		String year = strDate.substring(0, 4);
		String month = strDate.substring(5, 7);
		String day = strDate.substring(8, 10);
		int iYear = Integer.parseInt(year);
		int iMonth = Integer.parseInt(month) - 1;
		int iDay = Integer.parseInt(day);
		// logger.debug(year + month + day);

		cal1.set(iYear, iMonth, iDay);

		strStockCodeOrName = JOptionPane.showInputDialog("종목명이나 종목코드를 입력해 주세요", strStockCodeOrName);
		// logger.debug("strStockCodeOrName:" + strStockCodeOrName);

		// 코스피
		readFile("코스피", kospiFileName);
		// 코스닥
		readFile("코스닥", kosdaqFileName);

		Collections.sort(searchStockList, new StockNameLengthDescCompare());
		Collections.sort(allStockList, new StockNameLengthDescCompare());

		writeNewsFile(searchStockList);
	}

	public void readOne(String stockCode) {
		int cnt = 1;
		StockVO stock = new StockVO();
		stock.setStockCode(stockCode);
		getStockInfo(cnt, stock);
	}

	public void readFile(String kospidaq, String fileName) {
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int stockNameLength = 0;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				// logger.debug(cnt + "." + read);
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

				if (strStockCodeOrName.equals(stockCode) || stockName.indexOf(strStockCodeOrName) != -1) {
					strStockCode = stockCode;
					strStockName = stockName;
					searchStockList.add(getStockInfo(cnt, stock1));
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

	public StockVO getStockInfo(int cnt, StockVO stock) {
		Document doc;
		String code = stock.getStockCode();
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
			// logger.debug("doc:"+doc);

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
				// logger.debug("text:" + text);
				if (text.startsWith("종목명")) {
					String stockName = text.substring(4);
					// logger.debug("stockName:" + stockName);
					stock.setStockName(stockName);
				}

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
					stock.setlTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
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

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeNewsFile(List<StockVO> searchList) {
		// File f = new File(userHome + "\\documents\\" + fileName);
		try {
			FileWriter fw = new FileWriter(
					userHome + "\\documents\\" + strDate + "_오늘의_" + strStockCodeOrName + "_관련_뉴스.html");
			StringBuilder sb1 = new StringBuilder();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
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
			sb1.append("\t<h2>").append(strYMD).append(" 오늘의 ").append(strStockCodeOrName).append(" 관련 뉴스</h2>");

			sb1.append("<table>\r\n");
			sb1.append("	<tr>\r\n");
			sb1.append("		<th>종목명</th>");
			sb1.append("		<th>현재가</th>");
			sb1.append("		<th>전일대비</th>");
			sb1.append("		<th>등락율</th>");
			sb1.append("		<th>거래량</th>");
			sb1.append("		<th>거래대금(백만)</th>");
			sb1.append("	</tr>\r\n");
			for (StockVO svo : searchList) {
				sb1.append("	<tr>\r\n");
				sb1.append("		<td><a href='http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode()
						+ "\'>" + svo.getStockName() + "</a></td>");
				sb1.append("		<td class='tar'>" + svo.getCurPrice() + "</td>");

				String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
				String varyPrice = svo.getVaryPrice();
				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
					sb1.append("		<td class='tar red'>" + specialLetter + " " + varyPrice + "</font></td>\r\n");
				} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
						|| specialLetter.startsWith("-")) {
					sb1.append("		<td class='tar blue'>" + specialLetter + " " + varyPrice + "</font></td>\r\n");
				} else {
					sb1.append("		<td class='tar'>0</td>\r\n");
				}

				String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
				if (varyRatio.startsWith("+")) {
					sb1.append("		<td class='tar red'>" + varyRatio + "</font></td>\r\n");
				} else if (varyRatio.startsWith("-")) {
					sb1.append("		<td class='tar blue'>" + varyRatio + "</font></td>\r\n");
				} else {
					sb1.append("		<td class='tar'>" + varyRatio + "</font></td>\r\n");
				}

				sb1.append("		<td class='tar'>" + svo.getTradingVolume() + "</td>");
				sb1.append("		<td class='tar'>" + svo.getTradingAmount() + "</td>");
				sb1.append("	</tr>\r\n");
			}
			sb1.append("</table>\r\n");

			StringBuilder commentSb = new StringBuilder();
			for (StockVO s : searchList) {
				if (s != null) {
					Document classAnalysisDoc = Jsoup.connect(
							"http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + s.getStockCode())
							.get();
					// logger.debug("classAnalysisDoc:"+classAnalysisDoc);
					Elements comment = classAnalysisDoc.select(".cmp_comment");
					String strCommentCheck = classAnalysisDoc.select(".cmp_comment .dot_cmp li").text();
					if (strCommentCheck.equals("해당 자료가 없습니다.")) {
						continue;
					}
					commentSb.append("<div>\n");
					commentSb.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" + s.getStockCode()
							+ "'>" + s.getStockName() + "(" + s.getStockCode() + ")" + "</a></h4>\n");
					commentSb.append("<p>\n");
					commentSb.append(comment + "\n");
					commentSb.append("</p>");
					commentSb.append("</div>\n");
				}
			}
			if (!commentSb.toString().equals("")) {
				sb1.append("<table style='width:548px'>\r\n");
				sb1.append("<tr>\r\n");
				sb1.append("<td>\r\n");
				sb1.append(commentSb.toString());
				sb1.append("</td>\r\n");
				sb1.append("</tr>\r\n");
				sb1.append("</table>\r\n");
			}
			// 뉴스 첨부
			sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
					+ strStockCodeOrName + "(" + strStockCode + ")</a></h3>");

			sb1.append(getAllNews(searchList).toString());

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
			logger.debug("file write finished...");
		}
	}

	public StringBuilder getAllNews(List<StockVO> stockList) {
		logger.debug("stockList.size:" + stockList.size());

		StringBuilder sb = new StringBuilder();

		for (StockVO stock : stockList) {
			// 중복 뉴스 체크 로직 구현해야....
			StringBuilder sb1 = StockUtil.getNews(stock);
			sb.append(sb1);
		}
		return sb;
	}

	public StringBuilder getNews(StockVO stock) {

		StringBuilder sb1 = new StringBuilder();

		strStockCode = stock.getStockCode();
		strStockName = stock.getStockName();

		// 종합정보
		Document doc;
		try {
			for (int page = 1; page <= 10; page++) {
				doc = Jsoup
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=" + page)
						.get();

				Element e1 = doc.select(".tb_cont table").get(0);
				logger.debug(e1.text());

				Elements trs = e1.getElementsByTag("tr");

				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);

					Elements tds = tr.getElementsByTag("td");
					if (tds.size() < 3) {
						continue;
					}

					Element a1 = tr.getElementsByTag("a").first();
					Element source = tr.getElementsByTag("td").get(2);

					if (a1 == null) {
						continue;
					}

					String strTitle = a1.html();
					String strLink = a1.attr("href");
					String strSource = source.html();

					Element dayTime = tr.select(".date").first();
					String strDayTime = dayTime.html();
					String yyyyMMdd = strDayTime.substring(0, 10);
					logger.debug("yyyyMMdd:" + yyyyMMdd);
					String strDayTimeText = dayTime.text();

					String year = yyyyMMdd.substring(0, 4);
					String month = yyyyMMdd.substring(5, 7);
					String day = yyyyMMdd.substring(8, 10);
					int iYear = Integer.parseInt(year);
					int iMonth = Integer.parseInt(month) - 1;
					int iDay = Integer.parseInt(day);
					// logger.debug(year + month + day);

					cal2.set(iYear, iMonth, iDay);

					long diffSec = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000; // 초
					long diffDay = diffSec / (60 * 60 * 24); // 날
					// logger.debug("두 날자의 일 차이수 = " + diffDay);

					if (diffDay >= 0) {

						doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
						doc.select("meta").remove();
						doc.select("link").remove();
						doc.select("script").remove();
						Elements link_news_elements = doc.select(".link_news");
						if (link_news_elements != null) {
							link_news_elements.remove();
						}

						Elements naver_splugin = doc.select(".naver-splugin");
						logger.debug("naver_splugin:" + naver_splugin);
						if (naver_splugin != null) {
							naver_splugin.remove();
						}

						// doc.select("a").remove();
						doc.select("li").remove();
						Elements aElements = doc.select("a");
						for (int ii = 0; ii < aElements.size(); ii++) {
							Element aElement = aElements.get(ii);
							String aElementText = aElement.text();

							Element p = doc.createElement("span");
							p.appendText(aElementText);

							aElement.replaceWith(p);
							// logger.debug(aElement);
						}
						Element view = doc.select(".view").get(0);
						view.select(".end_btn").remove();
						// logger.debug("view.className:" + view.className());
						// logger.debug("attr(class):" + view.attr("class"));
						view.attr("style", "width:548px");
						// logger.debug("attr(style):" + view.attr("style"));

						String strView = view.toString();
						// logger.debug("strStockName:" + strStockName);
						// strView = strView.replaceAll(strStockName, "<a
						// href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>" +
						// strStockName + "</a>");
						strView = strView.replaceAll("\\[\\]", "");
						strView = strView.replaceAll("<ul></ul>", "");
						strView = strView.replaceAll("<br>\r\n<br>\r\n<br>", "<br><br>");
						strView = strView.replaceAll("&amp;", "&");

						strView = strView
								.replaceAll("<script type=\"text/javascript\" src=\"/js/news_read.js\"></script>", "");

						String title = view.select("tr th strong").get(0).text();
						Element dateElement = view.select("tr th span span").get(0);
						String date = dateElement.text();

						Element companyElement = view.select("tr th span").get(0);
						companyElement.select("img").remove();
						companyElement.select("span").remove();
						String company = companyElement.text();

						logger.debug("title:" + title);
						logger.debug("company:" + company);
						logger.debug("date:" + date);
						logger.debug("stockNewsList1:" + stockNewsList1);
						logger.debug("stockNewsList1.size:" + stockNewsList1.size());
						// 소스 수정필요
						StockNewsVO newsVO = new StockNewsVO();
						newsVO.setTitle(title);
						newsVO.setCompany(company);
						newsVO.setDate(date);

						if (stockNewsList1.size() == 0) {
							stockNewsList1.add(newsVO);
							sb1.append(strView);
							sb1.append("<br><br>\n");
						} else {
							for (StockNewsVO sNewsVO : stockNewsList1) {
								String tempTitle = sNewsVO.getTitle();
								String tempCompany = sNewsVO.getCompany();
								String tempDate = sNewsVO.getDate();

								logger.debug("tempTitle:" + tempTitle);
								logger.debug("tempCompany:" + tempCompany);
								logger.debug("tempDate:" + tempDate);
								logger.debug("title.equals(tempTitle):" + title.equals(tempTitle));
								logger.debug("company.equals(tempCompany):" + company.equals(tempCompany));
								logger.debug("date.equals(tempDate):" + date.equals(tempDate));

								if ((title.equals(tempTitle) && company.equals(tempCompany) && date.equals(tempDate))) {
									logger.debug("다 같으면...");
									newsVO = null;
								}
							}
							logger.debug("newsVO:" + newsVO);
							if (newsVO != null) {
								logger.debug("strStockCode:" + strStockCode);
								logger.debug("strStockName:" + strStockName);
								stockNewsList1.add(newsVO);

								sb1.append(strView);
								sb1.append("<br><br>\n");
							}
						}
					} else {
						break;
					}
				}
				logger.debug("=========================================");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 증권명에 증권링크 생성
		sb1 = StockUtil.stockLinkString(sb1, allStockList);
		return sb1;
	}

}
