package html.parsing.stock.dividends;

import java.io.IOException;
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

import html.parsing.stock.util.DataSort.DividendRateDescCompare;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.FileUtil;

public class KoreaStockDividends extends Thread {

	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(KoreaStockDividends.class);

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
	// main
	private final String totalInfoUrl = "http://finance.naver.com/item/main.nhn?code=";
	// 종목분석
	private final String naverItemAnalysisUrl = "https://finance.naver.com/item/coinfo.nhn?code=";
	// 기업현황
	private final String naverEntStatUrl = "https://navercomp.wisereport.co.kr/v2/company/c1010001.aspx?cmp_cd=";

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

//		KoreaStockDividends list1 = new KoreaStockDividends(-1);
		KoreaStockDividends list1 = new KoreaStockDividends();
		list1.start();

		long endTime = System.currentTimeMillis();
		String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
		System.out.println("call time :" + elapsedTimeSecond);
		System.out.println("main method call finished.");
	}

	KoreaStockDividends() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public void run() {
		execute();
	}

	KoreaStockDividends(int i) {
		//효성화학
//		List<StockVO> kospiAllStockDividendsInfoList = readOne("298000","효성화학");
		
		List<StockVO> kospiAllStockDividendsInfoList = readOne("204210","모두투어리츠");

		// 코스피 배당수익률순 정렬
		Collections.sort(kospiAllStockDividendsInfoList, new DividendRateDescCompare());
		StringBuilder info1 = getStockInformation(kospiAllStockDividendsInfoList, "코스피", "배당수익률");
		writeFile(info1, "코스피 배당수익률");		
	}

	public void execute() {
		logger.debug(this.getClass().getSimpleName() + " .execute started");
		System.out.println(this.getClass().getSimpleName() + " .execute started");

		// 모든 주식 정보를 조회한다.
		// 코스피
		List<StockVO> kospiAllStockList = new ArrayList<StockVO>();
		List<StockVO> kospiAllStockDividendsInfoList = new ArrayList<StockVO>();
		List<StockVO> kosdaqAllStockDividendsInfoList = new ArrayList<StockVO>();
		try {
			kospiAllStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			logger.debug("kospiAllStockList.size1 :" + kospiAllStockList.size());
		} catch (Exception e) {
			kospiAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
			logger.debug("kospiAllStockList.size2 :" + kospiAllStockList.size());
		}

		kospiAllStockDividendsInfoList = getStockPerInfoList(kospiAllStockList);

		// 코스피 배당수익률순 정렬
		Collections.sort(kospiAllStockDividendsInfoList, new DividendRateDescCompare());
		StringBuilder info1 = getStockInformation(kospiAllStockDividendsInfoList, "코스피", "배당수익률");
		writeFile(info1, "코스피 배당수익률");

		// 코스닥
		List<StockVO> kosdaqAllStockList = new ArrayList<StockVO>();
		try {
			kosdaqAllStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kosdaqAllStockList.size1 :" + kosdaqAllStockList.size());
		} catch (Exception e) {
			kosdaqAllStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
			logger.debug("kosdaqAllStockList.size2 :" + kosdaqAllStockList.size());
		}

		kosdaqAllStockDividendsInfoList = getStockPerInfoList(kosdaqAllStockList);

		// 코스닥 배당수익률순 정렬
		Collections.sort(kosdaqAllStockDividendsInfoList, new DividendRateDescCompare());
		StringBuilder info6 = getStockInformation(kosdaqAllStockDividendsInfoList, "코스닥", "배당수익률");
		writeFile(info6, "코스닥 배당수익률");

	}

	public List<StockVO> getStockPerInfoList(List<StockVO> allStockList) {
		List<StockVO> list = new ArrayList<StockVO>();
		for (StockVO svo : allStockList) {
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			logger.debug("stockCode1:" + stockCode);
			logger.debug("stockName1:" + stockName);
			svo = getStockInfo(stockCode,stockName);
			logger.debug("stockCode2:" + svo.getStockCode());
			logger.debug("stockName2:" + svo.getStockName());
			list.add(svo);
		}
		return list;
	}

	public List<StockVO> readOne(String stockCode,String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		StockVO stock = getStockInfo(stockCode,stockName);
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	public StockVO getStockInfo(String stockCode,String stockName) {
		String curPrice = "";
		StockVO svo = new StockVO();
		svo.setStockCode(stockCode);
		svo.setStockName(stockName);
		try {
			Document doc = Jsoup.connect(naverItemAnalysisUrl + stockCode).get();
//			stockName = doc.select(".h_company h2 a").text();
//			svo.setStockName(stockName);
			curPrice = doc.select(".spot .rate_info .today .no_today").text();
			logger.debug("curPrice:" + curPrice);
			if (curPrice.contains(" ")) {
				curPrice = curPrice.split(" ")[0];
			}
			logger.debug("curPrice:" + curPrice);
			svo.setCurPrice(curPrice);

			doc = Jsoup.connect(naverEntStatUrl + stockCode).get();

			Elements dts = doc.select(".cmp-table-row .cmp-table-cell.td0301 dl dt");
			for (Element dt : dts) {
				Element newDt = dt.clone();
				dt.select("b").remove();
				String indexKey = dt.text();
				String indexValue = newDt.select("b").text();
				logger.debug("indexKey:"+indexKey);
				logger.debug("indexValue:"+indexValue);
				if (indexKey.equals("EPS")) {
					svo.setEps(indexValue);
				} else if (indexKey.equals("BPS")) {
					svo.setBps(indexValue);
				} else if (indexKey.equals("PER")) {
					svo.setPer(indexValue);
				} else if (indexKey.equals("업종PER")) {
					svo.setBizTypePer(indexValue);
				} else if (indexKey.equals("PBR")) {
					svo.setPbr(indexValue);
				} else if (indexKey.equals("현금배당수익률")) {
					indexValue = indexValue.replace("%", "");
					float fDividendRate = 0;
					if(!indexValue.equals("")) {
						fDividendRate = Float.parseFloat(indexValue);
						svo.setfDividendRate(fDividendRate);
					}
					svo.setDividends(indexValue);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return svo;
	}

	public void writeFile(StringBuilder sb, String title) {
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
		if (iExtractCount > 0) {
			sb1.append("\t<h2>").append(title).append("TOP ").append(iExtractCount).append("</h2>");
		} else {
			sb1.append("\t<h2>").append(title).append("</h2>");
		}		
		sb1.append("\t<h5>작성일 :" + strYmdDash + "</h5>");
		sb1.append(sb.toString());
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		title = title.replace(" ", "_");
		String fileName = USER_HOME + "\\documents\\" + strYmdDashBracket + "_" + strHms + "_" + title;
		if (iExtractCount != -1) {
			fileName += iExtractCount;
		}
		fileName += ".html";

		FileUtil.fileWrite(fileName, sb1.toString());
		logger.debug("file write finished");
	}

	public StringBuilder getStockInformation(List<StockVO> list, String title, String gubun) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<table>\r\n");
		sb1.append("<tr>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가(원)</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>EPS</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>BPS</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PER</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>업종PER</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PBR</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현금배당수익률(%)</td>\r\n");
		sb1.append("</tr>\r\n");

		int cnt = 1;
		for (StockVO s : list) {
			String stockCode = s.getStockCode();
			String stockName = s.getStockName();
			if(stockCode.equals("")||stockName.equals("")) {
				logger.debug("stockCode:"+stockCode);
				logger.debug("stockName:"+stockName);
				continue;
			}
			sb1.append("<tr>\r\n");
			String url = totalInfoUrl + s.getStockCode();
			sb1.append("<td>" + cnt + "</td>\r\n");
			sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");
			sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getEps()) + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getBps()) + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getPer()) + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getBizTypePer()) + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getPbr()) + "</td>\r\n");
			sb1.append("<td style='text-align:right'>" + StringUtils.defaultString(s.getDividends()) + "</td>\r\n");
			sb1.append("</tr>\r\n");
			if (cnt == iExtractCount) {
				break;
			}
			cnt++;
		}
		sb1.append("</table>\r\n");
		return sb1;
	}

}
