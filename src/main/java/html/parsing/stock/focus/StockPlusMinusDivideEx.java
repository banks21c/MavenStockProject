package html.parsing.stock.focus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
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

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.ChosenDayEndPriceVsCurPriceUpDownRatioRatioAscCompare;
import html.parsing.stock.util.DataSort.ChosenDayEndPriceVsCurPriceUpDownRatioRatioDescCompare;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;

public class StockPlusMinusDivideEx extends Thread {

	
	public final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockPlusMinusDivideEx.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.SSS", Locale.KOREAN).format(new Date());

	DecimalFormat df = new DecimalFormat("###.##");

	String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
	int iHms = Integer.parseInt(strHms);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
	String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;
	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	int topCount = 0;
	int upCount = 0;
	int bottomCount = 0;
	int downCount = 0;
	int steadyCount = 0;

	StringBuilder sBuilder;
	int iExtractCount = -1;

	static String baseDay = "";
	static String chosenDay = "";
	static String thisYearLowestTradeDay = "2020.03.19";
	static String thisYearFirstTradeDay = "2020.01.02";
	static String thisYearPeakTradeDay = "2020.01.20";// 2020년 최고지수 2277.23
	static String lastYearFirstTradeDay = "2019.01.02";// 2019년 첫 거래일
	static String lastYearPeakTradeDay = "2019.04.15";// 2019년 최고지수 2252.05
	static String twoYearAgoFirstTradeDay = "2018.01.02";// 2018년 첫 거래일
	static String twoYearAgoPeakTradeDay = "2018.01.29";// 2018년 최고지수 2607.20
	
	// 특정 날짜가 몇 페이지에 있는가?
	static int pageNo = 0;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		baseDay = StringUtils.defaultString(JOptionPane.showInputDialog("비교 기준일을 입력해주세요.")).trim();
		if (baseDay.equals(""))
			baseDay = thisYearFirstTradeDay;
		
		// 대표주식(삼성전자) 기준일이 몇 페이지에 있는지 페이지 번호 구하기
		pageNo = StockUtil.getChosenDayPageNo("005930", "삼성전자", baseDay);
		logger.debug("pageNo :" + pageNo);
		
		long startTime = System.currentTimeMillis();

//		AllStockPlusMinusDivide list1 = new AllStockPlusMinusDivide(1);
		StockPlusMinusDivideEx list1 = new StockPlusMinusDivideEx();
		list1.start();

		long endTime = System.currentTimeMillis();
		String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
		System.out.println("call time :" + elapsedTimeSecond);
		System.out.println("main method call finished.");
	}

	StockPlusMinusDivideEx() {
		
		logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void run() {
		execute();
	}

	StockPlusMinusDivideEx(int i) {
		List<StockVO> kospiStockList = readOne("123890", "한국자산신탁");
//      List<StockVO> kospiStockList = readOne("032980");
		StringBuilder info1 = getStockInformation(kospiStockList, "코스피", "상승율");
		writeFile(info1, "코스피 상승율");
//
//      List<StockVO> kospiStockList1 = readOne("123890");
//      writeFile(kospiStockList1, kospiFileName, "코스피", "상승율");
		 List<StockVO> kosdaqStockList = readOne("204990");
			StringBuilder info2 = getStockInformation(kosdaqStockList, "코스닥", "상승율");
			writeFile(info2, "코스닥 상승율");
		// writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
	}

	public void execute() {
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		System.out.println(this.getClass().getSimpleName() + " .execute started");

//		String strExtractCount = JOptionPane.showInputDialog("몇 건씩 추출할까요?", "50");
//		if (strExtractCount != null && !strExtractCount.equals("")) {
//			iExtractCount = Integer.parseInt(strExtractCount);
//		} else {
//			iExtractCount = -1;
//		}
		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = new ArrayList<StockVO>();
		try {
			kospiAllStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			logger.debug("kospiAllStockList.size1 :" + kospiAllStockList.size());
		}catch(Exception e) {
			kospiAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
			logger.debug("kospiAllStockList.size2 :" + kospiAllStockList.size());
		}
		StockVO svo4Date = kospiAllStockList.get(0);
		strYmdDashBracket = StockUtil.getDateInfo(svo4Date.getStockCode());

		StockUtil sUtil = new StockUtil();
		kospiAllStockList = sUtil.getAllStockInfoAddBaseDayPrice(kospiAllStockList, baseDay, thisYearFirstTradeDay, pageNo);
		iExtractCount = kospiAllStockList.size();
		logger.debug("kospiAllStockList.size :" + kospiAllStockList.size());

		sBuilder = new StringBuilder();
		// 1.기준일 대비 상승율순 정렬
		Collections.sort(kospiAllStockList, new ChosenDayEndPriceVsCurPriceUpDownRatioRatioDescCompare());
		StringBuilder info1 = getStockInformation(kospiAllStockList, "코스피", "기준일 대비 상승율");
		writeFile(info1, "코스피 기준일 대비 등락율(상승율순)");

		// 2.기준일 대비 하락율순 정렬
		Collections.sort(kospiAllStockList, new ChosenDayEndPriceVsCurPriceUpDownRatioRatioAscCompare());
		StringBuilder info2 = getStockInformation(kospiAllStockList, "코스피", "기준일 대비 하락율");
		writeFile(info2, "코스피 기준일 대비 등락율(하락율순)");

		// 코스닥
		List<StockVO> kosdaqAllStockList = new ArrayList<StockVO>();
		try {
			kosdaqAllStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kosdaqAllStockList.size1 :" + kosdaqAllStockList.size());
		}catch(Exception e) {
			kosdaqAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
			logger.debug("kosdaqAllStockList.size2 :" + kosdaqAllStockList.size());
		}
		kosdaqAllStockList = sUtil.getAllStockInfoAddBaseDayPrice(kosdaqAllStockList, baseDay, thisYearFirstTradeDay, pageNo);

		iExtractCount = kosdaqAllStockList.size();
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		// 1.기준일 대비 상승율순 정렬
		Collections.sort(kosdaqAllStockList, new ChosenDayEndPriceVsCurPriceUpDownRatioRatioDescCompare());
		StringBuilder info5 = getStockInformation(kosdaqAllStockList, "코스닥", "기준일 대비 상승율");
		writeFile(info5, "코스닥 기준일 대비 등락율(상승율순)");

		// 2.기준일 대비 하락율순 정렬
		Collections.sort(kosdaqAllStockList, new ChosenDayEndPriceVsCurPriceUpDownRatioRatioAscCompare());
		StringBuilder info6 = getStockInformation(kosdaqAllStockList, "코스닥", "기준일 대비 하락율");
		writeFile(info6, "코스닥 기준일 대비 등락율(하락율순)");

	}

	public List<StockVO> readOne(String stockCode) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode);
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	public List<StockVO> readOne(String stockCode, String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode);
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	public List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

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

				StockVO stock = getStockInfo(cnt, strStockCode);
				if (stock != null) {
					stock.setStockName(strStockName);
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

	public StockVO getStockInfo(int cnt, String code) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(code);
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();

			Elements new_totalinfos = doc.select(".new_totalinfo");

			if (new_totalinfos == null || new_totalinfos.isEmpty()) {
				return stock;
			}

			Element new_totalinfo = new_totalinfos.get(0);
			Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
			Element blind = new_totalinfo_doc.select(".blind").get(0);

			if (blind == null) {
				return stock;
			}

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
					String strStockName = text.substring(4);
					// System.out.println("strStockName:" + strStockName);
					stock.setStockName(strStockName);
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
//            System.out.println("date:" + date);
			System.out.println("specialLetter:" + specialLetter);
			System.out.println("sign:" + sign);
			System.out.println("curPrice:" + curPrice);
			System.out.println("varyPrice:" + varyPrice);
//            System.out.println("beforePrice:" + beforePrice);
			System.out.println("varyRatio:" + varyRatio);
//            System.out.println("startPrice:" + startPrice);
//            System.out.println("highPrice:" + highPrice);
//            System.out.println("lowPrice:" + lowPrice);
//            System.out.println("TradingVolume:" + tradingVolume);
//            System.out.println("tradingAmount:" + tradingAmount);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public void writeFile(StringBuilder sb, String title) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<!doctype html>\r\n");
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;font-size:12px;}\r\n");
		sb1.append("    .red {color:red}\r\n");
		sb1.append("    .blue {color:blue}\r\n");
		sb1.append("    .gray {color:gray}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2>" + strYmdDashBracket + " " + title + "</h2>");
		sb1.append("\t<h2>기준일 :" + baseDay +"</h2>");
//		sb1.append("<h4><font color='red'>상한가:" + topCount + "</font><font color='red'> 상승:" + upCount
//			+ "</font><font color='blue'> 하한가:" + bottomCount + "</font><font color='blue'> 하락:" + downCount
//			+ "</font><font color='gray'> 보합:" + steadyCount + "</font></h4>");
		sb1.append(sb.toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = USER_HOME + "\\documents\\" + strYmdDashBracket + " " + strHms + "_" + title;
		if (iExtractCount != -1) {
			fileName += iExtractCount;
		}
		fileName += ".html";

		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public StringBuilder getStockInformation(List<StockVO> list, String title, String gubun) {
		StringBuilder sb1 = new StringBuilder();
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
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가대비등락율</td>\r\n");
		sb1.append("</tr>\r\n");
		int cnt = 1;
		for (StockVO svo : list) {
			if (svo != null) {
				String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
				System.out.println("specialLetter+++>" + specialLetter);
				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
				sb1.append("<td>" + cnt + "</td>\r\n");
				sb1.append("<td><a href='" + url + "' target='_sub'>" + svo.getStockName() + "</a></td>\r\n");

				String varyPrice = svo.getVaryPrice();

				System.out.println("varyPrice+++>" + varyPrice);

				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
					|| specialLetter.startsWith("+")) {
					sb1.append("<td style='text-align:right;color:red'>"
						+ StringUtils.defaultIfEmpty(svo.getCurPrice(), "") + "</td>\r\n");
					sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
						+ "</font></td>\r\n");
				} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
					|| specialLetter.startsWith("-")) {
					sb1.append("<td style='text-align:right;color:blue'>"
						+ StringUtils.defaultIfEmpty(svo.getCurPrice(), "") + "</td>\r\n");
					sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
						+ "</font></td>\r\n");
				} else {
					sb1.append("<td style='text-align:right;color:metal'>"
						+ StringUtils.defaultIfEmpty(svo.getCurPrice(), "") + "</td>\r\n");
					sb1.append("<td style='text-align:right'>0</td>\r\n");
				}

				// if(gubun.equals("ALL")){
				// String varyRatio =
				// StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
				// if (varyRatio.startsWith("+")) {
				// sb1.append("<td style='text-align:right'><font
				// color='red'>" + varyRatio + "</font></td>\r\n");
				// } else if (varyRatio.startsWith("-")) {
				// sb1.append("<td style='text-align:right'><font
				// color='blue'>" + varyRatio + "</font></td>\r\n");
				// } else {
				// sb1.append(
				// "<td style='text-align:right'><font color='black'>" +
				// varyRatio + "</font></td>\r\n");
				// }
				// sb1.append("<td style='text-align:right'>" +
				// StringUtils.defaultIfEmpty(s.getTradingVolume(),"") +
				// "</td>\r\n");
				// sb1.append("<td style='text-align:right'>" +
				// StringUtils.defaultIfEmpty(s.getTradingAmount(),"") +
				// "</td>\r\n");
				// }else if(gubun.equals("상승율")||gubun.equals("하락율")){
				// String varyRatio =
				// StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
				// if (varyRatio.startsWith("+")) {
				// sb1.append("<td style='text-align:right'><font
				// color='red'>" + varyRatio + "</font></td>\r\n");
				// } else if (varyRatio.startsWith("-")) {
				// sb1.append("<td style='text-align:right'><font
				// color='blue'>" + varyRatio + "</font></td>\r\n");
				// } else {
				// sb1.append(
				// "<td style='text-align:right'><font color='black'>" +
				// varyRatio + "</font></td>\r\n");
				// }
				// }else if(gubun.equals("거래량")){
				// sb1.append("<td style='text-align:right'>" +
				// StringUtils.defaultIfEmpty(s.getTradingVolume(),"") +
				// "</td>\r\n");
				// }else if(gubun.equals("거래대금")){
				// sb1.append("<td style='text-align:right'>" +
				// StringUtils.defaultIfEmpty(s.getTradingAmount(),"") +
				// "</td>\r\n");
				// }
				String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
				if (varyRatio.startsWith("+")) {
					sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
				} else if (varyRatio.startsWith("-")) {
					sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
				} else {
					sb1.append(
						"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
				}
				sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(svo.getTradingVolume(), "")
					+ "</td>\r\n");
				sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(svo.getTradingAmount(), "") + "</td>\r\n");
				sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(svo.getChosenDayEndPrice(), "") + "</td>\r\n");
				
				String chosenDayEndPriceVsCurPriceUpDownRatioStyle = "";
				if (svo.getChosenDayEndPriceVsCurPriceUpDownRatio() < 0) {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "blue";
				} else if (svo.getChosenDayEndPriceVsCurPriceUpDownRatio() == 0) {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "gray";
				} else {
					chosenDayEndPriceVsCurPriceUpDownRatioStyle = "red";
				}
				
				sb1.append("<td style='text-align:right' class='" + chosenDayEndPriceVsCurPriceUpDownRatioStyle + "'>" + svo.getChosenDayEndPriceVsCurPriceUpDownRatio() + "%</td>\r\n");

				sb1.append("</tr>\r\n");

			}
			cnt++;
		}
		return sb1;
	}


	public void readNews(List<StockVO> allStockList) {

		int cnt = 1;

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			for (StockVO vo : allStockList) {
				strStockCode = vo.getStockCode();
				strStockName = vo.getStockName();

				System.out.println(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

				Document doc = Jsoup
					.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
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

					System.out.println("title:" + a1.html());
					System.out.println("href:" + a1.attr("href"));
					System.out.println("source:" + source.html());
					System.out.println("dayTime:" + dayTime.html());

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

			String fileName = USER_HOME + "\\documents\\NewsTest." + strDate + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
