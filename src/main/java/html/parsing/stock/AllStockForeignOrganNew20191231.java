package html.parsing.stock;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.ForeignTradingVolumeAscCompare;
import html.parsing.stock.DataSort.ForeignTradingVolumeDescCompare;
import html.parsing.stock.DataSort.OrganTradingVolumeAscCompare;
import html.parsing.stock.DataSort.OrganTradingVolumeDescCompare;
import html.parsing.stock.model.ResultVO;
import html.parsing.stock.util.FileUtil;

public class AllStockForeignOrganNew20191231 {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganNew20191231.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYmd4Compare = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN).format(new Date());

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	private static final int EXTRACT_COUNT = 20;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganNew20191231(1);
	}

	AllStockForeignOrganNew20191231() {
		strYmd4Compare = JOptionPane.showInputDialog("추출일자를 입력해 주세요(yyyy-mm-dd)", strYmd4Compare);

		// 에이치엘비
		ResultVO result = readOne("028300");
		if (result.getResultMessage().equals("SUCCESS")) {
			writeFile(result.getStockList(), "코스피", true, "거래량");
		}
	}

	AllStockForeignOrganNew20191231(int i) {
		strYmd4Compare = JOptionPane.showInputDialog("추출일자를 입력해 주세요(yyyy-mm-dd)", strYmd4Compare);
//		MakeKospiKosdaqList.makeKospiKosdaqList();

		// 모든 주식 정보를 조회한다.
		// StockUtil.readStockCodeNameListFromKrx(stockMkt or kosdaqMkt)
		List<StockVO> kospiAllStockList;
		List<StockVO> kospiAllStockForeignBuyList = new ArrayList<StockVO>();
		List<StockVO> kospiAllStockForeignSellList = new ArrayList<StockVO>();
		List<StockVO> kospiAllStockOrganBuyList = new ArrayList<StockVO>();
		List<StockVO> kospiAllStockOrganSellList = new ArrayList<StockVO>();
		List<StockVO> kospiForeignTopSomeStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kospiForeignTopSomeStockSellList = new ArrayList<StockVO>();
		List<StockVO> kospiOrganTopSomeStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kospiOrganTopSomeStockSellList = new ArrayList<StockVO>();

		List<StockVO> kosdaqAllStockList;
		List<StockVO> kosdaqOrganAllStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kosdaqOrganAllStockSellList = new ArrayList<StockVO>();
		List<StockVO> kosdaqForeignAllStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kosdaqForeignAllStockSellList = new ArrayList<StockVO>();
		List<StockVO> kosdaqOrganTopSomeStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kosdaqOrganTopSomeStockSellList = new ArrayList<StockVO>();
		List<StockVO> kosdaqForeignTopSomeStockBuyList = new ArrayList<StockVO>();
		List<StockVO> kosdaqForeignTopSomeStockSellList = new ArrayList<StockVO>();

		// 기관 코스피 순매수, 순매도
		Map<String, List<StockVO>> kospiForeignTopSomeStockMap = new HashMap<String, List<StockVO>>();
		// 기관 코스닥 순매수, 순매도
		Map<String, List<StockVO>> kosdaqForeignTopSomeStockMap = new HashMap<String, List<StockVO>>();
		// 외국인 코스피 순매수, 순매도
		Map<String, List<StockVO>> kospiOrganTopSomeStockMap = new HashMap<String, List<StockVO>>();
		// 외국인 코스닥 순매수, 순매도
		Map<String, List<StockVO>> kosdaqOrganTopSomeStockMap = new HashMap<String, List<StockVO>>();

		// 기관 코스피,코스닥 순매수, 순매도
		Map<String, List<StockVO>> organTopSomeStockMap = new HashMap<String, List<StockVO>>();
		// 외국인 코스피,코스닥 순매수, 순매도
		Map<String, List<StockVO>> foreignTopSomeStockMap = new HashMap<String, List<StockVO>>();

		// 기관,외국인 코스피,코스닥 순매수,순매도
		Map<String, List<StockVO>> topSomeStockMap = new HashMap<String, List<StockVO>>();

		try {
			// 코스피
			kospiAllStockList = StockUtil.readStockCodeNameListFromKrx("stockMkt");
			logger.debug("kospiAllStockList.size :" + kospiAllStockList.size());
			kospiAllStockList = getAllStockInfo(kospiAllStockList).getStockList();
			if (kospiAllStockList.size() > 0) {
				kospiAllStockForeignBuyList.addAll(kospiAllStockList);
				kospiAllStockForeignSellList.addAll(kospiAllStockList);
				kospiAllStockOrganBuyList.addAll(kospiAllStockList);
				kospiAllStockOrganSellList.addAll(kospiAllStockList);
				logger.debug("kospiAllStockForeignBuyList.size :" + kospiAllStockForeignBuyList.size());
				logger.debug("kospiAllStockForeignSellList.size :" + kospiAllStockForeignSellList.size());
				logger.debug("kospiAllStockOrganBuyList.size :" + kospiAllStockOrganBuyList.size());
				logger.debug("kospiAllStockOrganSellList.size :" + kospiAllStockOrganSellList.size());
			} else {
				return;
			}

			// 코스닥
			kosdaqAllStockList = StockUtil.readStockCodeNameListFromKrx("kosdaqMkt");
			if (kosdaqAllStockList.size() > 0) {

				logger.debug("kosdaqAllStockList.size :" + kosdaqAllStockList.size());
				kosdaqAllStockList = getAllStockInfo(kosdaqAllStockList).getStockList();
				kosdaqForeignAllStockBuyList.addAll(kosdaqAllStockList);
				kosdaqForeignAllStockSellList.addAll(kosdaqAllStockList);
				kosdaqOrganAllStockBuyList.addAll(kosdaqAllStockList);
				kosdaqOrganAllStockSellList.addAll(kosdaqAllStockList);
				logger.debug("kosdaqAllStockForeignBuyList.size :" + kosdaqForeignAllStockBuyList.size());
				logger.debug("kosdaqAllStockForeignSellList.size :" + kosdaqForeignAllStockSellList.size());
				logger.debug("kosdaqAllStockOrganBuyList.size :" + kosdaqOrganAllStockBuyList.size());
				logger.debug("kosdaqAllStockOrganSellList.size :" + kosdaqOrganAllStockSellList.size());
			} else {
				return;
			}
			// 1.외국인 코스피 순매수 순위
			Collections.sort(kospiAllStockForeignBuyList, new ForeignTradingVolumeDescCompare());
			// 2.외국인 코스피 순매도 순위
			Collections.sort(kospiAllStockForeignSellList, new ForeignTradingVolumeAscCompare());
			// 3.기관 코스피 순매수 순위
			Collections.sort(kospiAllStockOrganBuyList, new OrganTradingVolumeDescCompare());
			// 4.기관 코스피 순매도 순위
			Collections.sort(kospiAllStockOrganSellList, new OrganTradingVolumeAscCompare());

			// 5.외국인 코스닥 순매수 순위
			Collections.sort(kosdaqForeignAllStockBuyList, new ForeignTradingVolumeDescCompare());
			// 6.외국인 코스닥 순매도 순위
			Collections.sort(kosdaqForeignAllStockSellList, new ForeignTradingVolumeAscCompare());
			// 7.기관 코스닥 순매수 순위
			Collections.sort(kosdaqOrganAllStockBuyList, new OrganTradingVolumeDescCompare());
			// 8.기관 코스닥 순매도 순위
			Collections.sort(kosdaqOrganAllStockSellList, new OrganTradingVolumeAscCompare());

			// 1.외국인 코스피 순매수 TopSome
			kospiForeignTopSomeStockBuyList = kospiAllStockForeignBuyList.subList(0, EXTRACT_COUNT);
			// 2.외국인 코스피 순매도 TopSome
			kospiForeignTopSomeStockSellList = kospiAllStockForeignSellList.subList(0, EXTRACT_COUNT);
			// 3.기관 코스피 순매수 TopSome
			kospiOrganTopSomeStockBuyList = kospiAllStockOrganBuyList.subList(0, EXTRACT_COUNT);
			// 4.기관 코스피 순매도 TopSome
			kospiOrganTopSomeStockSellList = kospiAllStockOrganSellList.subList(0, EXTRACT_COUNT);

			// 5.외국인 코스닥 순매수 TopSome
			kosdaqForeignTopSomeStockBuyList = kosdaqForeignAllStockBuyList.subList(0, EXTRACT_COUNT);
			// 6.외국인 코스닥 순매도 TopSome
			kosdaqForeignTopSomeStockSellList = kosdaqForeignAllStockSellList.subList(0, EXTRACT_COUNT);
			// 7.기관 코스닥 순매수 TopSome
			kosdaqOrganTopSomeStockBuyList = kosdaqOrganAllStockBuyList.subList(0, EXTRACT_COUNT);
			// 8.기관 코스닥 순매도 TopSome
			kosdaqOrganTopSomeStockSellList = kosdaqOrganAllStockSellList.subList(0, EXTRACT_COUNT);

			logger.debug("2.kospiForeignTopSomeStockBuyList.size :" + kospiForeignTopSomeStockBuyList.size());
			logger.debug("2.kospiForeignTopSomeStockSellList.size :" + kospiForeignTopSomeStockSellList.size());
			logger.debug("2.kospiOrganTopSomeStockBuyList.size :" + kospiOrganTopSomeStockBuyList.size());
			logger.debug("2.kospiOrganTopSomeStockSellList.size :" + kospiOrganTopSomeStockSellList.size());

			logger.debug("2.kosdaqForeignTopSomeStockBuyList.size :" + kosdaqForeignTopSomeStockBuyList.size());
			logger.debug("2.kosdaqForeignTopSomeStockSellList.size :" + kosdaqForeignTopSomeStockSellList.size());
			logger.debug("2.kosdaqOrganTopSomeStockBuyList.size :" + kosdaqOrganTopSomeStockBuyList.size());
			logger.debug("2.kosdaqOrganTopSomeStockSellList.size :" + kosdaqOrganTopSomeStockSellList.size());

			// 1.외국인 코스피 순매수,순매도 TopSome
			kospiForeignTopSomeStockMap.put("코스피 외국인 순매수 종목", kospiForeignTopSomeStockBuyList);
			kospiForeignTopSomeStockMap.put("코스피 외국인 순매도 종목", kospiForeignTopSomeStockSellList);
			writeFile(kospiForeignTopSomeStockMap, "코스피 외국인 매매 종목", 1);

			// 2.외국인 코스닥 순매수,순매도 TopSome
			kosdaqForeignTopSomeStockMap.put("코스닥 외국인 순매수 종목", kosdaqForeignTopSomeStockBuyList);
			kosdaqForeignTopSomeStockMap.put("코스닥 외국인 순매도 종목", kosdaqForeignTopSomeStockSellList);
			writeFile(kosdaqForeignTopSomeStockMap, "코스닥 외국인 매매 종목", 1);

			foreignTopSomeStockMap.put("코스피 외국인 순매수 종목", kospiForeignTopSomeStockBuyList);
			foreignTopSomeStockMap.put("코스피 외국인 순매도 종목", kospiForeignTopSomeStockSellList);
			foreignTopSomeStockMap.put("코스닥 외국인 순매수 종목", kosdaqForeignTopSomeStockBuyList);
			foreignTopSomeStockMap.put("코스닥 외국인 순매도 종목", kosdaqForeignTopSomeStockSellList);
			writeFile(foreignTopSomeStockMap, "코스피, 코스닥 외국인 매매 종목", 2);

			// 3.기관 코스피 순매수,순매도 TopSome
			kospiOrganTopSomeStockMap.put("코스피 기관 순매수 종목", kospiOrganTopSomeStockBuyList);
			kospiOrganTopSomeStockMap.put("코스피 기관 순매도 종목", kospiOrganTopSomeStockSellList);
			writeFile(kospiOrganTopSomeStockMap, "코스피 기관 매매 종목", 1);

			// 4.기관 코스닥 순매수,순매도 TopSome
			kosdaqOrganTopSomeStockMap.put("코스닥 기관 순매수 종목", kosdaqOrganTopSomeStockBuyList);
			kosdaqOrganTopSomeStockMap.put("코스닥 기관 순매도 종목", kosdaqOrganTopSomeStockSellList);
			writeFile(kosdaqOrganTopSomeStockMap, "코스닥 기관 매매 종목", 1);

			organTopSomeStockMap.put("코스피 기관 순매수 종목", kospiOrganTopSomeStockBuyList);
			organTopSomeStockMap.put("코스피 기관 순매도 종목", kospiOrganTopSomeStockSellList);
			organTopSomeStockMap.put("코스닥 기관 순매수 종목", kosdaqOrganTopSomeStockBuyList);
			organTopSomeStockMap.put("코스닥 기관 순매도 종목", kosdaqOrganTopSomeStockSellList);
			writeFile(organTopSomeStockMap, "코스피, 코스닥 기관 매매 종목", 2);

			topSomeStockMap.put("코스피 외국인 순매수 종목", kospiForeignTopSomeStockBuyList);
			topSomeStockMap.put("코스피 외국인 순매도 종목", kospiForeignTopSomeStockSellList);
			topSomeStockMap.put("코스닥 외국인 순매수 종목", kosdaqForeignTopSomeStockBuyList);
			topSomeStockMap.put("코스닥 외국인 순매도 종목", kosdaqForeignTopSomeStockSellList);
			topSomeStockMap.put("코스피 기관 순매수 종목", kospiOrganTopSomeStockBuyList);
			topSomeStockMap.put("코스피 기관 순매도 종목", kospiOrganTopSomeStockSellList);
			topSomeStockMap.put("코스닥 기관 순매수 종목", kosdaqOrganTopSomeStockBuyList);
			topSomeStockMap.put("코스닥 기관 순매도 종목", kosdaqOrganTopSomeStockSellList);
			writeFile(topSomeStockMap, "코스피,코스닥 기관,외국인 매매 종목", 3);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ResultVO readOne(String stockCode) {
		ResultVO result = new ResultVO();
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockInfo(cnt, stockCode, "");
		stocks.add(stock);
		String resultCode = stock.getResultCode();
		String resultMessage = stock.getResultMessage();
		String resultDetailMessage = stock.getResultDetailMessage();
		logger.debug("resultCode :" + resultCode);
		logger.debug("resultMessage :" + resultMessage);
		logger.debug("resultDetailMessage :" + resultDetailMessage);
		result.setResultCode(resultCode);
		result.setResultMessage(resultMessage);
		result.setResultDetailMessage(resultDetailMessage);
		result.setStockList(stocks);
		return result;
	}

	public ResultVO getAllStockInfo(List<StockVO> stockList) {
		ResultVO result = new ResultVO();
		List<StockVO> stocks = new ArrayList<StockVO>();

		StockVO stock = null;
		String stockCode = null;
		String stockName = null;
		int cnt = 1;
		boolean isClosed = true;
		if (stockList != null && stockList.size() > 0) {
			StockVO svo = stockList.get(0);
			stockCode = svo.getStockCode();
			stockName = svo.getStockName();
			logger.debug(stockCode + "\t" + stockName);

			stock = getStockInfo(cnt, stockCode, stockName);
			if (stock.getResultMessage().equals("FAIL")) {
				isClosed = false;
			}
		}

		if (isClosed) {
			String resultCode = stock.getResultCode();
			String resultMessage = stock.getResultMessage();
			String resultDetailMessage = stock.getResultDetailMessage();
			logger.debug("resultCode :" + resultCode);
			logger.debug("resultMessage :" + resultMessage);
			logger.debug("resultDetailMessage :" + resultDetailMessage);
			result.setResultCode(resultCode);
			result.setResultMessage(resultMessage);
			result.setResultDetailMessage(resultDetailMessage);
			result.setStockList(stocks);

			for (StockVO svo : stockList) {
				stockCode = svo.getStockCode();
				stockName = svo.getStockName();
				logger.debug(stockCode + "\t" + stockName);

				stock = getStockInfo(cnt, stockCode, stockName);
				if (stock != null) {
					stock.setStockName(stockName);
					stocks.add(stock);
				}
				cnt++;
			}
		}
		result.setStockList(stocks);
		return result;
	}

	public static List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		File f = new File(userHome + "\\Downloads\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				logger.debug(cnt + "." + read);
				stockCode = read.split("\t")[0];
				stockName = read.split("\t")[1];
				logger.debug(stockCode + "\t" + stockName);

				if (stockCode.length() != 6) {
					continue;
				}

				StockVO stock = getStockInfo(cnt, stockCode, stockName);
				if (stock != null) {
					stock.setStockName(stockName);
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

	public static StockVO getStockInfo(int cnt, String code, String name) {
		Document doc;
		StockVO stock = new StockVO();
		try {
			// 종합정보
			String url = "http://finance.naver.com/item/main.nhn?code=" + code;
			doc = Jsoup.connect(url).get();
			if (cnt == 1) {
				// logger.debug(doc.title());
				// logger.debug(doc.html());
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

			String specialLetter = "";
			String sign = "";
			String curPrice = "";
			String varyRatio = "";

			int iCurPrice = 0;
			int iVaryPrice = 0;

			for (int i = 0; i < edds.size(); i++) {
				Element dd = edds.get(i);
				String text = dd.text();
				logger.debug("data:" + text);

				if (text.startsWith("현재가")) {
					logger.debug("data1:" + dd.text());
					text = text.replaceAll("플러스", "+");
					text = text.replaceAll("마이너스", "-");
					text = text.replaceAll("상승", "▲");
					text = text.replaceAll("하락", "▼");
					text = text.replaceAll("퍼센트", "%");

					String txts[] = text.split(" ");
					curPrice = txts[1];
					stock.setCurPrice(txts[1]);
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
					logger.debug("txts.length:" + txts.length);
					if (txts.length == 7) {
						stock.setVaryRatio(txts[5] + txts[6]);
					} else if (txts.length == 8) {
						stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
					}
					varyRatio = stock.getVaryRatio();
					stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
					logger.debug("상승률:" + stock.getVaryRatio());
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

			url = "http://finance.naver.com/item/frgn.nhn?code=" + code;
			logger.debug("url :"+url);
			doc = Jsoup.connect(url).get();

			String tradingDate = "";
			String foreignTradingVolume = "";
			String organTradingVolume = "";

			Elements type2Elements = doc.select(".type2");
			Element element2 = type2Elements.get(1);
			// 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
			// tr
			Elements trElements = element2.select("tr");
			boolean find = false;
			for (Element trElement : trElements) {
				Elements tdElements = trElement.select("td");
				if (tdElements.size() == 9) {
					// 날짜
					tradingDate = tdElements.get(0).text();
					logger.debug("strYmd4Compare :"+strYmd4Compare);
					logger.debug("tradingDate :"+tradingDate);
					if (strYmd4Compare.equals(tradingDate)) {
						// 기관순매매량
						organTradingVolume = tdElements.get(5).text();
						// 외인순매매량
						foreignTradingVolume = tdElements.get(6).text();
						find = true;
						break;
					}
				}
			}
			if (!find) {
				logger.debug("당일 집계가 되지 않았거나 입력하신 일자 데이터가 존재하지 않습니다.");
				stock.setResultCode("F00001");
				stock.setResultMessage("FAIL");
//				JOptionPane.showMessageDialog(null, "당일 집계가 되지 않았거나 입력하신 일자 데이터가 존재하지 않습니다.");
				return stock;
			}else {
				stock.setResultCode("S00001");
				stock.setResultMessage("SUCCESS");

			}

			foreignTradingVolume = StringUtils.defaultIfEmpty(foreignTradingVolume, "0");
			organTradingVolume = StringUtils.defaultIfEmpty(organTradingVolume, "0");
			// foreignTradingVolume = StringUtils.defaultIfEmpty(foreignTradingVolume,
			// "0").replaceAll("\\+", "");
			// organTradingVolume = StringUtils.defaultIfEmpty(organTradingVolume,
			// "0").replaceAll("\\+", "");

			logger.debug("foreignTradingVolume:" + foreignTradingVolume);
			logger.debug("organTradingVolume:" + organTradingVolume);

			int iForeignTradingVolume = Integer.parseInt(foreignTradingVolume.replaceAll(",", ""));
			int iOrganTradingVolume = Integer.parseInt(organTradingVolume.replaceAll(",", ""));
			int iForeignOrganTradingVolume = iForeignTradingVolume + iOrganTradingVolume;

			logger.debug("iForeignTradingVolume:" + iForeignTradingVolume);
			logger.debug("iOrganTradingVolume:" + iOrganTradingVolume);

			long standardPrice = 0;
			if (sign.equals("+")) {
				standardPrice = iCurPrice - iVaryPrice / 2;
			}
			if (sign.equals("-")) {
				standardPrice = iCurPrice + iVaryPrice / 2;
			}
			if (sign.equals("")) {
				standardPrice = iCurPrice;
			}

			logger.debug("standardPrice :" + standardPrice);

			long iForeignTradeAmount = iForeignTradingVolume * standardPrice;
			long iOrganTradeAmount = iOrganTradingVolume * standardPrice;
			// 금액은 만 단위
			iForeignTradeAmount = iForeignTradeAmount / 10000;
			iOrganTradeAmount = iOrganTradeAmount / 10000;

			logger.debug("외인거래금액 :" + iForeignTradeAmount);
			logger.debug("기관거래금액 :" + iOrganTradeAmount);

			stock.setlForeignTradingAmount(iForeignTradeAmount);
			stock.setlOrganTradingAmount(iOrganTradeAmount);
			stock.setlForeignOrganTradingAmount((iForeignTradeAmount + iOrganTradeAmount));

			DecimalFormat df = new DecimalFormat("#,##0");
			String foreignTradeAmount = df.format(iForeignTradeAmount);
			String organTradeAmount = df.format(iOrganTradeAmount);
			String foreignOrganTradingVolume = df.format(iForeignOrganTradingVolume);
			String foreignOrganTradingAmount = df.format((iForeignTradeAmount + iOrganTradeAmount));

			stock.setForeignTradingAmount(foreignTradeAmount);
			stock.setOrganTradingAmount(organTradeAmount);
			stock.setForeignOrganTradingAmount(foreignOrganTradingAmount);

			stock.setForeignTradingVolume(foreignTradingVolume);
			stock.setOrganTradingVolume(organTradingVolume);
			stock.setForeignOrganTradingVolume(foreignOrganTradingVolume);

			stock.setiForeignTradingVolume(iForeignTradingVolume);
			stock.setiOrganTradingVolume(iOrganTradingVolume);
			stock.setiForeignOrganTradingVolume(iForeignOrganTradingVolume);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stock;
	}

	Map<String, List<StockVO>> TopSomeKospiStockMapByAmount = new HashMap<String, List<StockVO>>();

	public void writeFile(Map<String, List<StockVO>> topSomeStockMapByAmount, String title, int type) {

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

		String ymd = new SimpleDateFormat("yyyy년 MM월 dd일").format(new Date());
		sb1.append("<h2>" + strYMD + title + "(단위:만원,주)</h2>");

		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd] HHmmss", Locale.KOREAN);
		String strDate = sdf.format(new Date());

		Set<String> keys = topSomeStockMapByAmount.keySet();
		int rowCnt = 1;
		for (String key : keys) {
			logger.debug("key :" + key);
			List<StockVO> list = topSomeStockMapByAmount.get(key);

			sb1.append("<div style='display:inline-block'>\r\n");
			sb1.append("<h3>" + key + " 종목 TOP " + EXTRACT_COUNT + "</h3>\r\n");
			sb1.append("<table>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락률</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(만원)</td>\r\n");
			sb1.append("</tr>\r\n");

			int cnt = 1;
			for (StockVO s : list) {
				// 거래량(거래금액)이 없으면 건너뛴다.
				if (key.contains("외국인")) {
					if (s.getiForeignTradingVolume() == 0) {
						continue;
					}
				} else {
					if (s.getiOrganTradingVolume() == 0) {
						continue;
					}
				}

				if (s != null) {
					sb1.append("<tr>\r\n");
					String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
					sb1.append("<td>" + cnt++ + "</td>\r\n");
					sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					String varyPrice = s.getVaryPrice();
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
					if (key.contains("외국인")) {
						sb1.append("<td style='text-align:right'>" + s.getForeignTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + s.getForeignTradingAmount() + "</td>\r\n");
					} else {
						sb1.append("<td style='text-align:right'>" + s.getOrganTradingVolume() + "</td>\r\n");
						sb1.append("<td style='text-align:right'>" + s.getOrganTradingAmount() + "</td>\r\n");
					}
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("</div>\r\n");
			if (rowCnt % 2 == 0) {
				sb1.append("<br/>\r\n");
			}
			rowCnt++;
		}
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\Downloads\\" + strDate + "_" + title + ".html";
		logger.debug("fileName :" + fileName);
		FileUtil.fileWrite(fileName, sb1.toString());
	}

	public void writeFile(List<StockVO> list, String title, boolean isForeign, String gubun) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd] HHmmss", Locale.KOREAN);
		String strDate = sdf.format(new Date());

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
		sb1.append("\t<font size=5>" + strYMD + title + "</font>");
		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락률</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(만원)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;
		for (StockVO s : list) {
			// 거래량(거래금액)이 없으면 건너뛴다.
			if (isForeign) {
				if (s.getiForeignTradingVolume() == 0) {
					continue;
				}
			} else {
				if (s.getiOrganTradingVolume() == 0) {
					continue;
				}
			}

			if (s != null) {
				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
				sb1.append("<td>" + cnt++ + "</td>\r\n");
				sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
				sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

				String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
				String varyPrice = s.getVaryPrice();
				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
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
					sb1.append("<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
				}
				if (isForeign) {
					sb1.append("<td style='text-align:right'>" + s.getForeignTradingVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getForeignTradingAmount() + "</td>\r\n");
				} else {
					sb1.append("<td style='text-align:right'>" + s.getOrganTradingVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + s.getOrganTradingAmount() + "</td>\r\n");
				}
				sb1.append("</tr>\r\n");
			}
		}
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\Downloads\\" + strDate + "_" + title + ".html";
		logger.debug("fileName :" + fileName);
		FileUtil.fileWrite(fileName, sb1.toString());
	}

}
