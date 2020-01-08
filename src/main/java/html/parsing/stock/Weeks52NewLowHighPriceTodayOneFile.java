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
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.NameAscCompare;
import html.parsing.stock.util.FileUtil;

public class Weeks52NewLowHighPriceTodayOneFile extends Thread {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = null;

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
	int iHms = Integer.parseInt(strHms);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYyyyMmDd = new SimpleDateFormat("yyyy년 M월 d일
	// E",Locale.KOREAN).format(new Date());
	int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
	String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	List<StockVO> kospiStockList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

	LinkedHashMap<String, List<StockVO>> newLowHighPriceMap = new LinkedHashMap<String, List<StockVO>>();
	List<StockVO> newLowPriceList = new ArrayList<StockVO>();
	List<StockVO> kospiNewLowPriceList = new ArrayList<StockVO>();
	List<StockVO> kospiNewHighPriceList = new ArrayList<StockVO>();
	List<StockVO> kosdaqNewLowPriceList = new ArrayList<StockVO>();
	List<StockVO> kosdaqNewHighPriceList = new ArrayList<StockVO>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Weeks52NewLowHighPriceTodayOneFile().start();
//		new Weeks52NewLowHighPriceToday(1);
	}

	Weeks52NewLowHighPriceTodayOneFile() {
	}

	@Override
	public void run() {
		execute();
	}

	Weeks52NewLowHighPriceTodayOneFile(int i) {
		Class thisClass = this.getClass();
		logger = LoggerFactory.getLogger(thisClass);
		readOne("053610", "프로텍");

		Collections.sort(kospiNewLowPriceList, new NameAscCompare());
		Collections.sort(kospiNewHighPriceList, new NameAscCompare());

		newLowPriceList.addAll(kospiNewLowPriceList);
		newLowPriceList.addAll(kospiNewHighPriceList);

		newLowHighPriceMap.put("코스피 신저가", kospiNewLowPriceList);
		newLowHighPriceMap.put("코스피 신고가", kospiNewHighPriceList);

		writeFile(newLowHighPriceMap);
	}

	public void execute() {
		Class thisClass = this.getClass();
		logger = LoggerFactory.getLogger(thisClass);
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		// MakeKospiKosdaqList.makeKospiKosdaqList();

		Properties props = new Properties();
		try {
			// InputStream is = new FileInputStream("log4j.properties");
			// props.load(new FileInputStream("log4j.properties"));
			// InputStream is =
			// getClass().getResourceAsStream("/log4j.properties");
			logger.debug(getClass().getResource(".").getPath());
			logger.debug(getClass().getResource("/").getPath());
			props.load(getClass().getResourceAsStream("/log4j.properties"));
			logger.debug("props:" + props);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyConfigurator.configure(props);

		// ClassLoader loader = Thread.currentThread().getContextClassLoader();
		// URL url = loader.getResource("log4j.properties");
		// PropertyConfigurator.configure(url);
		File log4jfile = new File("log4j.properties");
		String absolutePath = log4jfile.getAbsolutePath();
		logger.debug("absolutePath :" + absolutePath);
		PropertyConfigurator.configure(absolutePath);

		// 모든 주식 정보를 조회한다.
		// 코스피
//        readFile("코스피", kospiFileName);
		// 코스피 신저가
//		newLowPriceList = StockUtil.getAllStockInfo(kospiFileName);
		// 코스닥
//      readFile("코스닥", kosdaqFileName);
		try {
			kospiStockList = StockUtil.getAllStockList(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockList(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
			logger.debug("kosdaqStockList1 :" + kosdaqStockList);
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(Weeks52NewLowHighPriceTodayOneFile.class.getName()).log(Level.SEVERE,
					null, ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
			logger.debug("kosdaqStockList2 :" + kosdaqStockList);
		}

		for (int i = 0; i < kospiStockList.size(); i++) {
			StockVO svo = kospiStockList.get(i);
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			logger.debug("stockCode :" + stockCode + " stockName :" + stockName);
			svo = getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "P");
		}
		logger.debug("kosdaqStockList.size :" + kosdaqStockList.size());
		for (int i = 0; i < kosdaqStockList.size(); i++) {
			StockVO svo = kosdaqStockList.get(i);
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			logger.debug("stockCode :" + stockCode + " stockName :" + stockName);
			svo = getStockInfo((i + 1), svo.getStockCode(), svo.getStockName(), "D");
		}

		Collections.sort(kospiNewHighPriceList, new NameAscCompare());
		Collections.sort(kosdaqNewHighPriceList, new NameAscCompare());
		Collections.sort(kospiNewLowPriceList, new NameAscCompare());
		Collections.sort(kosdaqNewLowPriceList, new NameAscCompare());

		newLowPriceList.addAll(kospiNewHighPriceList);
		newLowPriceList.addAll(kosdaqNewHighPriceList);
		newLowPriceList.addAll(kospiNewLowPriceList);
		newLowPriceList.addAll(kosdaqNewLowPriceList);

		newLowHighPriceMap.put("코스피 신고가", kospiNewHighPriceList);
		newLowHighPriceMap.put("코스닥 신고가", kosdaqNewHighPriceList);
		newLowHighPriceMap.put("코스피 신저가", kospiNewLowPriceList);
		newLowHighPriceMap.put("코스닥 신저가", kosdaqNewLowPriceList);

		writeFile(newLowHighPriceMap);

	}

	public void readOne(String stockCode, String stockName) {
		int cnt = 1;
		strStockCode = stockCode;
		strStockName = stockName;
		getStockInfo(cnt, strStockCode, strStockName, "P");
	}

	public void readFile(String marketGubun, String fileName) {

		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				logger.debug(cnt + "." + read);
				strStockCode = read.split("\t")[0];
				strStockName = read.split("\t")[1];
				logger.debug(strStockCode + "\t" + strStockName);

				if (strStockCode.length() != 6) {
					continue;
				}
				getStockInfo(cnt, strStockCode, strStockName, marketGubun);
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

	public StockVO getStockInfo(int cnt, String strStockCode, String strStockName, String marketGubun) {
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
					strYmdDash = date.ownText();
					strYmdDash = date.childNode(0).toString().trim();

					String strYmd4Int = strYmdDash.replaceAll("\\.", "");
					if (strYmd4Int.length() > 8) {
						strYmd4Int = strYmd4Int.substring(0, 8);
					}
					iYmd = Integer.parseInt(strYmd4Int);

					strYmdDash = strYmdDash.replaceAll("\\.", "-");
					strYmdDash = strYmdDash.replaceAll(":", "-");
					strYmdDashBracket = "[" + strYmdDash + "]";
				}
			}
			logger.debug("iYmd:[" + iYmd + "]");
			logger.debug("strYmdDash:[" + strYmdDash + "]");

			// Element tradeVolumeText =
			// doc.select(".sp_txt9").get(0);
			String tradeVolumeText = doc.select(".sp_txt9").get(0).parent().child(1).child(0).text();
			if (tradeVolumeText.equals("0")) {
				return null;
			}
			logger.debug("tradeVolumeText:" + tradeVolumeText);

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
				logger.debug("text:" + text + " / " + text.split(" ")[0] + " " + text.split(" ")[1]);
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
			logger.debug("weeks52MaxPrice :" + stock.getWeeks52MaxPrice());
			logger.debug("weeks52MinPrice :" + stock.getWeeks52MinPrice());

			// curPrice = curPrice.replaceAll(",", "").trim();
			weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
			weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();

			logger.debug("curPrice:" + curPrice);

			int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
			int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);

			logger.debug("stock.getiHighPrice():" + stock.getiHighPrice() + " iWeeks52MaxPrice:" + iWeeks52MaxPrice
					+ "================:" + (stock.getiHighPrice() >= iWeeks52MaxPrice));
			logger.debug("stock.getiLowPrice():" + stock.getiLowPrice() + " iWeeks52MaxPrice:" + iWeeks52MinPrice
					+ "================:" + (stock.getiLowPrice() >= iWeeks52MinPrice));

			if (stock.getiHighPrice() >= iWeeks52MaxPrice) {
				if (marketGubun.equals("P")) {
					kospiNewHighPriceList.add(stock);
					logger.debug("kospiNewHighPriceList :" + kospiNewHighPriceList + " kospiNewHighPriceList.size :"
							+ kospiNewHighPriceList.size());
				} else {
					kosdaqNewHighPriceList.add(stock);
					logger.debug("kosdaqNewHighPriceList :" + kosdaqNewHighPriceList + " kosdaqNewHighPriceList.size :"
							+ kosdaqNewHighPriceList.size());
				}
			} else if (stock.getiLowPrice() <= iWeeks52MinPrice) {
				if (marketGubun.equals("P")) {
					kospiNewLowPriceList.add(stock);
					logger.debug("kospiNewLowPriceList :" + kospiNewLowPriceList + " kospiNewLowPriceList.size :"
							+ kospiNewLowPriceList.size());
				} else {
					kosdaqNewLowPriceList.add(stock);
					logger.debug("kosdaqNewLowPriceList :" + kosdaqNewLowPriceList + " kosdaqNewLowPriceList.size :"
							+ kosdaqNewLowPriceList.size());
				}
			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
		return stock;
	}

	public void writeFile(Map<String, List<StockVO>> newLowHighPriceMap) {
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
		sb1.append("\t<h1>").append(strYmdDashBracket).append(" 코스피,코스닥 신고,신저가").append("</h1>");

		Set keySet = newLowHighPriceMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);

			sb1.append("\t<h2>").append(key).append("</h2>");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
			if (key.contains("신저가")) {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>저가</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최저가</td>\r\n");
			} else if (key.contains("신고가")) {
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>고가</td>\r\n");
				sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최고가</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : stockList) {
				logger.debug("s :" + s);
				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>").append(cnt++).append("</td>\r\n");
					sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(s.getStockName())
							.append("</a></td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();

					logger.debug("specialLetter+++>" + specialLetter);
					logger.debug("varyPrice+++>" + varyPrice);

					if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
							|| specialLetter.startsWith("+")) {
						sb1.append("<td style='text-align:right;color:red'>")
								.append(StringUtils.defaultIfEmpty(s.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='red'>").append(specialLetter).append(" ")
								.append(varyPrice).append("</font></td>\r\n");
					} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
							|| specialLetter.startsWith("-")) {
						sb1.append("<td style='text-align:right;color:blue'>")
								.append(StringUtils.defaultIfEmpty(s.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'><font color='blue'>").append(specialLetter).append(" ")
								.append(varyPrice).append("</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right;color:metal'>")
								.append(StringUtils.defaultIfEmpty(s.getCurPrice(), "")).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>0</td>\r\n");
					}

					String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>").append(varyRatio)
								.append("</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>").append(varyRatio)
								.append("</font></td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'><font color='black'>").append(varyRatio)
								.append("</font></td>\r\n");
					}

					if (key.contains("신저가")) {
						sb1.append("<td style='text-align:right'>").append(s.getLowPrice()).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>").append(s.getWeeks52MinPrice()).append("</td>\r\n");
					} else if (key.contains("신고가")) {
						sb1.append("<td style='text-align:right'>").append(s.getHighPrice()).append("</td>\r\n");
						sb1.append("<td style='text-align:right'>").append(s.getWeeks52MaxPrice()).append("</td>\r\n");
					}

					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br><br>\r\n");
		}

		// 뉴스 첨부
		sb1.append(getNews(newLowPriceList).toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug(sb1.toString());
		String fileName = userHome + "\\documents\\" + strYmdDashBracket + " " + strHms + "_코스피,코스닥 신고,신저가.html";
		logger.debug("fileName==>" + fileName);
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public StringBuilder getNews(List<StockVO> allStockList) {

		StringBuilder sb1 = new StringBuilder();

		for (StockVO vo : allStockList) {
			strStockCode = vo.getStockCode();
			strStockName = vo.getStockName();

			// 종합정보
			logger.debug("strStockCode:" + strStockCode + " strStockName:" + strStockName);
			logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=");

			Document doc;
			try {
				// http://finance.naver.com/item/news_news.nhn?code=110570
				doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=")
						.get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
				// logger.debug(doc.html());
				Elements types = doc.select(".type5");
				Element type = null;
				if (types.size() <= 0) {
					return sb1;
				}
				type = doc.select(".type5").get(0);

				Elements trs = type.getElementsByTag("tr");
				if (trs != null) {
					logger.debug("trs.size:" + trs.size());
				}

				for (int i = 0; i < trs.size(); i++) {
					Element tr = trs.get(i);

					Elements tds = tr.getElementsByTag("td");
					if (tds.size() < 3) {
						continue;
					}

					Element a1 = tr.getElementsByTag("a").first();
					Element source = tr.getElementsByClass("info").first();
					Element dayTime = tr.getElementsByClass("date").first();

					if (a1 == null) {
						continue;
					}
					logger.debug("a:" + a1);
					logger.debug("source:" + source);
					logger.debug("dayTime:" + dayTime);
					logger.debug("title:" + a1.html());
					logger.debug("href:" + a1.attr("href"));
					logger.debug("source:" + source.html());
					logger.debug("dayTime:" + dayTime.html());

					String strTitle = a1.html();
					String strLink = a1.attr("href");
					String strSource = source.html();
					String strDayTime = dayTime.html();
					String strYmd2 = strDayTime.substring(0, 10);
					int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

					// sb1.append("<h3>"+ strTitle +"</h3>\n");
					// sb1.append("<div>"+ strSource+" | "+ strDayTime
					// +"</div>\n");
					if (iYmd <= iYmd2) {
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=").append(strStockCode)
								.append("'>").append(strStockName).append("(").append(strStockCode)
								.append(")</a></h3>");
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");

						doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
						Elements link_news_elements = doc.select(".link_news");
						if (link_news_elements != null) {
							link_news_elements.remove();
						}
						Elements naver_splugin = doc.select(".naver-splugin");
						logger.debug("naver_splugin:" + naver_splugin);
						if (naver_splugin != null) {
							naver_splugin.remove();
						}
						doc.select("a").remove();
						doc.select("li").remove();

						Element view = doc.select(".view").get(0);

						String strView = view.toString();
						strView = strView.replaceAll(strStockName,
								"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
										+ strStockName + "</a>");

						sb1.append(strView);
						sb1.append("\n");

						logger.debug("view:" + view);
					}
				}
			} catch (IOException e) {
			}
		}
		return sb1;
	}

	public void readNews(List<StockVO> allStockList) {

		int cnt = 1;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(userHome + "\\documents\\NewsTest." + strDate + ".html");
			StringBuilder sb1 = new StringBuilder();

			for (StockVO vo : allStockList) {
				strStockCode = vo.getStockCode();
				strStockName = vo.getStockName();

				logger.debug(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=");

				Document doc = Jsoup
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=")
						.get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
				doc.select("script").remove();
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

					logger.debug("title:" + a1.html());
					logger.debug("href:" + a1.attr("href"));
					logger.debug("source:" + source.html());
					logger.debug("dayTime:" + dayTime.html());

					String strTitle = a1.html();
					String strLink = a1.attr("href");
					String strSource = source.html();
					String strDayTime = dayTime.html();
					String strYmd2 = strDayTime.substring(0, 10);
					int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

					// sb1.append("<h3>"+ strTitle +"</h3>\n");
					// sb1.append("<div>"+ strSource+" | "+ strDayTime
					// +"</div>\n");
					if (iYmd <= iYmd2) {
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=").append(strStockCode)
								.append("'>").append(strStockName).append("(").append(strStockCode)
								.append(")</a></h3>\n");

						doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
						Elements link_news_elements = doc.select(".link_news");
						if (link_news_elements != null) {
							link_news_elements.remove();
						}
						Elements naver_splugin = doc.select(".naver-splugin");
						logger.debug("naver_splugin:" + naver_splugin);
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

						logger.debug("view:" + view);
					}
				}
			}

			logger.debug(sb1.toString());

			fw.write(sb1.toString());
			fw.close();

		} catch (IOException | NumberFormatException e) {
		}
	}

}
