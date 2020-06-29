package html.parsing.stock.focus;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
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
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.StockNameAscCompare;
import html.parsing.stock.util.DataSort.TradingAmountDescCompare;
import html.parsing.stock.util.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.util.DataSort.VaryRatioAscCompare;
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;

public class StockPlusMinusDivide extends Thread {

	private final static String TOTAL_INFO_URL = "http://finance.naver.com/item/main.nhn?code=";
	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(StockPlusMinusDivide.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E hh.mm.SSS", Locale.KOREAN).format(new Date());

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

	static int iExtractCount = -1;

	private String strNidAut;
	private String strNidSes;

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();

		String strExtractCount = JOptionPane.showInputDialog("몇 건씩 추출할까요?", "-1");
		if (strExtractCount != null && !strExtractCount.equals("")) {
			iExtractCount = Integer.parseInt(strExtractCount);
		} else {
			iExtractCount = -1;
		}

		StockPlusMinusDivide list1 = new StockPlusMinusDivide();
		list1.start();

		long endTime = System.currentTimeMillis();
		String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
		System.out.println("call time :" + elapsedTimeSecond);
		System.out.println("main method call finished.");
	}

	StockPlusMinusDivide() {
		logger = LoggerFactory.getLogger(this.getClass());
		iExtractCount = -1;
	}

	StockPlusMinusDivide(String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	@Override
	public void run() {
		execute();
	}

	StockPlusMinusDivide(int i) {
		List<StockVO> kospiStockList = readOne("123890", "한국자산신탁");
//      List<StockVO> kospiStockList = readOne("032980");
		StringBuilder info1 = getStockInfoHtml(kospiStockList, "코스피", "상승율");
		// writeFile(info1, "코스피", "상승율");
		StringBuilder html = createHtmlString(info1, "코스피", "상승율");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

//      List<StockVO> kospiStockList1 = readOne("123890");
//      writeFile(kospiStockList1, kospiFileName, "코스피", "상승율");
		List<StockVO> kosdaqStockList = readOne("204990");
		StringBuilder info2 = getStockInfoHtml(kosdaqStockList, "코스닥", "상승율");
		writeFile(info2, "코스닥", " 상승율");
		// writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
		html = createHtmlString(info1, "코스닥", "상승율");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);
	}

	public void getDateInfo(String stockCode) {
		try {
			// 종합정보
			Document doc = Jsoup.connect(TOTAL_INFO_URL + stockCode).get();
			// logger.debug("doc:"+doc);

			Elements dates = doc.select(".date");
			logger.debug("dates:" + dates);
			if (dates != null) {
				logger.debug("dates.size:" + dates.size());
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
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void execute() {
		long start = System.currentTimeMillis();

		execute("코스피");
		execute("코스닥");

		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");
	}

	public void execute_bak() {
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		System.out.println(this.getClass().getSimpleName() + " .execute started");

		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = new ArrayList<StockVO>();
		try {
			kospiAllStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			logger.debug("kospiAllStockList.size1 :" + kospiAllStockList.size());
		} catch (Exception e) {
			kospiAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
			logger.debug("kospiAllStockList.size2 :" + kospiAllStockList.size());
		}
		StockVO svo4Date = kospiAllStockList.get(0);
		getDateInfo(svo4Date.getStockCode());

		StockUtil sUtil = new StockUtil();
		kospiAllStockList = sUtil.getAllStockInfo(kospiAllStockList);
		logger.debug("kospiAllStockList.size :" + kospiAllStockList.size());

		topCount = sUtil.getTopCount();
		upCount = sUtil.getUpCount();
		bottomCount = sUtil.getBottomCount();
		downCount = sUtil.getDownCount();
		steadyCount = sUtil.getSteadyCount();

		// 1.상승율순 정렬
		Collections.sort(kospiAllStockList, new VaryRatioDescCompare());
		StringBuilder info1 = getStockInfoHtml(kospiAllStockList, "코스피", "상승율");

		// 2.하락율순 정렬
		Collections.sort(kospiAllStockList, new VaryRatioAscCompare());
		StringBuilder info2 = getStockInfoHtml(kospiAllStockList, "코스피", "하락율");

		// 3.보합
		Collections.sort(kospiAllStockList, new StockNameAscCompare());
		StringBuilder info3 = getStockInfoHtml(kospiAllStockList, "코스피", "보합");

		// 4.거래량 정렬
		Collections.sort(kospiAllStockList, new TradingVolumeDescCompare());
		StringBuilder info4 = getStockInfoHtml(kospiAllStockList, "코스피", "거래량");

		// 5.거래대금순 정렬
		Collections.sort(kospiAllStockList, new TradingAmountDescCompare());
		StringBuilder info5 = getStockInfoHtml(kospiAllStockList, "코스피", "거래대금");

		writeFile(info1, "코스피", " 상승율");
		writeFile(info2, "코스피", " 하락율");
		writeFile(info3, "코스피", " 보합");
		writeFile(info4, "코스피", " 거래량");
		writeFile(info5, "코스피", " 거래대금");

		// 초기화
		sUtil.setTopCount(0);
		sUtil.setUpCount(0);
		sUtil.setBottomCount(0);
		sUtil.setDownCount(0);
		sUtil.setSteadyCount(0);

		// 코스닥
		List<StockVO> kosdaqAllStockList = new ArrayList<StockVO>();
		try {
			kosdaqAllStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kosdaqAllStockList.size1 :" + kosdaqAllStockList.size());
		} catch (Exception e) {
			kosdaqAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
			logger.debug("kosdaqAllStockList.size2 :" + kosdaqAllStockList.size());
		}
		kosdaqAllStockList = sUtil.getAllStockInfo(kosdaqAllStockList);
		System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		topCount = sUtil.getTopCount();
		upCount = sUtil.getUpCount();
		bottomCount = sUtil.getBottomCount();
		downCount = sUtil.getDownCount();
		steadyCount = sUtil.getSteadyCount();

		// 6.상승율순 정렬
		Collections.sort(kosdaqAllStockList, new VaryRatioDescCompare());
		StringBuilder info6 = getStockInfoHtml(kosdaqAllStockList, "코스닥", "상승율");

		// 7.하락율순 정렬
		Collections.sort(kosdaqAllStockList, new VaryRatioAscCompare());
		StringBuilder info7 = getStockInfoHtml(kosdaqAllStockList, "코스닥", "하락율");

		// 8.보합
		Collections.sort(kospiAllStockList, new StockNameAscCompare());
		StringBuilder info8 = getStockInfoHtml(kosdaqAllStockList, "코스닥", "보합");

		// 9.거래량 정렬
		Collections.sort(kosdaqAllStockList, new TradingVolumeDescCompare());
		StringBuilder info9 = getStockInfoHtml(kosdaqAllStockList, "코스닥", "거래량");

		// 10.거래대금순 정렬
		Collections.sort(kosdaqAllStockList, new TradingAmountDescCompare());
		StringBuilder info10 = getStockInfoHtml(kosdaqAllStockList, "코스닥", "거래대금");

		writeFile(info6, "코스닥", " 상승율");
		writeFile(info7, "코스닥", " 하락율");
		writeFile(info8, "코스닥", " 보합");
		writeFile(info9, "코스닥", " 거래량");
		writeFile(info10, "코스닥", " 거래대금");

	}

	public void execute(String marketType) {
		logger.debug(this.getClass().getSimpleName() + " .execute(" + marketType + ")started");

		String marketFileName = "";
		String krxMarketGubun = "";
		if (marketType.equals("코스피")) {
			marketFileName = kospiFileName;
			krxMarketGubun = "stockMkt";
		} else {
			marketFileName = kosdaqFileName;
			krxMarketGubun = "kosdaqMkt";
		}
		// 모든 주식 정보를 조회한다.
		List<StockVO> stockList = new ArrayList<StockVO>();
		stockList = StockUtil.readStockCodeNameList(marketType);
		logger.debug("stockList.size1 :" + stockList.size());
		StockVO svo4Date = stockList.get(0);
		getDateInfo(svo4Date.getStockCode());

		StockUtil sUtil = new StockUtil();
		stockList = sUtil.getAllStockInfo(stockList);
		logger.debug("stockList.size :" + stockList.size());

		topCount = sUtil.getTopCount();
		upCount = sUtil.getUpCount();
		bottomCount = sUtil.getBottomCount();
		downCount = sUtil.getDownCount();
		steadyCount = sUtil.getSteadyCount();

		// 1.상승율순 정렬
		Collections.sort(stockList, new VaryRatioDescCompare());
		StringBuilder info1 = getStockInfoHtml(stockList, marketType, "상승율");

		// 2.하락율순 정렬
		Collections.sort(stockList, new VaryRatioAscCompare());
		StringBuilder info2 = getStockInfoHtml(stockList, marketType, "하락율");

		// 3.보합
		StringBuilder info3 = new StringBuilder();
		if (iExtractCount <= 0) {
			Collections.sort(stockList, new StockNameAscCompare());
			info3 = getStockInfoHtml(stockList, marketType, "보합");
		}

		// 4.거래량 정렬
		Collections.sort(stockList, new TradingVolumeDescCompare());
		StringBuilder info4 = getStockInfoHtml(stockList, marketType, "거래량");

		// 5.거래대금순 정렬
		Collections.sort(stockList, new TradingAmountDescCompare());
		StringBuilder info5 = getStockInfoHtml(stockList, marketType, "거래대금");

//		writeFile(info1, marketType , " 상승율");
//		writeFile(info2, marketType , " 하락율");
//		writeFile(info3, marketType , " 보합");
//		writeFile(info4, marketType , " 거래량");
//		writeFile(info5, marketType , " 거래대금");

		StringBuilder html;

		html = createHtmlString(info1, marketType, "상승율");
		writeFile(html, marketType, "상승율");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

		html = createHtmlString(info2, marketType, "하락율");
		writeFile(html, marketType, "하락율");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

		html = createHtmlString(info3, marketType, "보합");
		writeFile(html, marketType, "보합");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

		html = createHtmlString(info4, marketType, "거래량");
		writeFile(html, marketType, "거래량");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

		html = createHtmlString(info5, marketType, "거래대금");
		writeFile(html, marketType, "거래대금");
		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

		logger.debug("file write finished");
	}

	// 네이버 블로그에 공유
	public void naverBlogLinkShare(StringBuilder html) {
		String strUrl = "";
		String strTitle = Jsoup.parse(html.toString()).select("h2#title").text();
		String categoryName = "증권↑↓↗↘";
		StringBuilder contentSb = html;
		logger.debug("strNidAut:" + strNidAut);
		logger.debug("strNidSes:" + strNidSes);
		if (strNidAut != null && strNidSes != null) {
			NaverUtil.naverBlogLinkShare(strNidAut, strNidSes, strUrl, strTitle, categoryName, contentSb, null);
		}
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
			doc = Jsoup.connect(TOTAL_INFO_URL + code).get();

			Elements new_totalinfos = doc.select(".new_totalinfo");

			if (new_totalinfos == null || new_totalinfos.size() == 0) {
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
				topCount++;
			} else if (specialLetter.equals("▲")) {
				upCount++;
			} else if (specialLetter.equals("↓")) {
				bottomCount++;
			} else if (specialLetter.equals("▼")) {
				downCount++;
			} else {
				if (stock.getiTradingVolume() > 0) {
					steadyCount++;
				}
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

	public StringBuilder createHtmlString(StringBuilder sb, String marketType, String title) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<!doctype html>\r\n");
		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("<style>\r\n");
		sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
		sb1.append("    td {border:1px solid #aaaaaa;font-size:12px;}\r\n");
		sb1.append("</style>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");
		sb1.append("\t<h2>" + strYmdDashBracket + " " + title + "</h2>");
		sb1.append("<h4><font color='red'>상한가:" + topCount + "</font><font color='red'> 상승:" + upCount
				+ "</font><font color='gray'> 보합:" + steadyCount + "</font><font color='blue'> 하락:" + downCount
				+ "</font><font color='blue'> 하한가:" + bottomCount + "</font></h4>");
		sb1.append(sb.toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		return sb1;
	}

	public void writeFile(StringBuilder sb, String marketType, String title) {
		title = title.replace(" ", "_");
		String fileName = USER_HOME + "\\documents\\" + strYmdDashBracket + "_" + strHms + "_" + title;
		if (iExtractCount != -1) {
			fileName += iExtractCount;
		}
		fileName += ".html";

		FileUtil.fileWrite(fileName, sb.toString());
		logger.debug(fileName + " write finished");
	}

	public StringBuilder getStockInfoHtml(List<StockVO> list, String marketType, String descGubun) {
		StringBuilder sb1 = new StringBuilder();
		if (iExtractCount > 0) {
			sb1.append("\t<h2 id='title'>").append(marketType).append(" ").append(descGubun).append("TOP ")
					.append(iExtractCount).append("</h2>");
		} else {
			sb1.append("\t<h2 id='title'>").append(marketType).append(" ").append(descGubun).append("</h2>");
		}

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
			String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
			logger.debug(descGubun + " specialLetter+++>" + specialLetter);
			if (descGubun.equals("상승율")) {
				// 1.상승율
				if (specialLetter.equals("") || specialLetter.equals("▼") || specialLetter.equals("↓")) {
					continue;
				}
			} else if (descGubun.equals("하락율")) {
				// 2.하락율
				if (specialLetter.equals("") || specialLetter.equals("▲") || specialLetter.equals("↑")) {
					continue;
				}
			} else if (descGubun.equals("보합")) {
				// 3.보합
				if (specialLetter.equals("▼") || specialLetter.equals("↓") || specialLetter.equals("▲")
						|| specialLetter.equals("↑")) {
					continue;
				}
			}
			sb1.append("<tr>\r\n");
			String url = TOTAL_INFO_URL + s.getStockCode();
			sb1.append("<td>" + cnt + "</td>\r\n");
			sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");

			String varyPrice = s.getVaryPrice();

			logger.debug(descGubun + " varyPrice+++>" + varyPrice);
			logger.debug(descGubun + " 변동가격:" + specialLetter + varyPrice);

			if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
				sb1.append("<td style='text-align:right;color:red'>" + StringUtils.defaultIfEmpty(s.getCurPrice(), "")
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
						+ "</font></td>\r\n");
			} else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
					|| specialLetter.startsWith("-")) {
				sb1.append("<td style='text-align:right;color:blue'>" + StringUtils.defaultIfEmpty(s.getCurPrice(), "")
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
						+ "</font></td>\r\n");
			} else {
				sb1.append("<td style='text-align:right;color:metal'>" + StringUtils.defaultIfEmpty(s.getCurPrice(), "")
						+ "</td>\r\n");
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
			String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
			if (varyRatio.startsWith("+")) {
				sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
			} else if (varyRatio.startsWith("-")) {
				sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
			} else {
				sb1.append("<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
			}
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingVolume(), "")
					+ "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingAmount(), "")
					+ "</td>\r\n");

			sb1.append("</tr>\r\n");
			if (cnt == iExtractCount) {
				break;
			}
			cnt++;
		}
		sb1.append("</table>\r\n");
		// 뉴스 첨부
		// sb1.append(getNews(list).toString());
		return sb1;
	}

	public StringBuilder getNews(List<StockVO> allStockList) {

		StringBuilder sb1 = new StringBuilder();

		for (StockVO vo : allStockList) {
			strStockCode = vo.getStockCode();
			strStockName = vo.getStockName();

			// 종합정보
			System.out.println("strStockCode:" + strStockCode + " strStockName:" + strStockName);
			System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

			Document doc;
			try {
				// http://finance.naver.com/item/news_news.nhn?code=110570
				doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
						.get();
				// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
				// System.out.println(doc.html());
				Elements types = doc.select(".type5");
				Element type = null;
				if (types.size() <= 0) {
					return sb1;
				}
				type = doc.select(".type5").get(0);

				Elements trs = type.getElementsByTag("tr");
				if (trs != null) {
					System.out.println("trs.size:" + trs.size());
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
					System.out.println("a:" + a1);
					System.out.println("source:" + source);
					System.out.println("dayTime:" + dayTime);
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
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
								+ strStockName + "(" + strStockCode + ")</a></h3>");
						// sb1.append("<h3>"+ strTitle +"</h3>\n");
						// sb1.append("<div>"+ strSource+" | "+ strDayTime
						// +"</div>\n");

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
						doc.select("a").remove();
						doc.select("li").remove();

						Element view = doc.select(".view").get(0);

						String strView = view.toString();
						strView = strView.replaceAll(strStockName,
								"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
										+ strStockName + "</a>");

						sb1.append(strView);
						sb1.append("\n");

						System.out.println("view:" + view);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
						.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=").get();
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
