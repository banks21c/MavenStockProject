/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import html.parsing.stock.model.StockNewsVO;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.DataSort.StockNameLengthDescCompare;

/**
 *
 * @author parsing-25
 */
public class StockUtil {

	private final static String TOTAL_INFO_URL = "http://finance.naver.com/item/main.nhn?code=";
	final static String userHome = System.getProperty("user.home");
	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E hh.mm.SSS", Locale.KOREAN).format(new Date());
	private static final Logger logger = LoggerFactory.getLogger(StockUtil.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYyyyMmDd = new SimpleDateFormat("yyyy년 M월 d일
	// E",Locale.KOREAN).format(new Date());
	int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
	String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70";

	// STOCK_LIST_TYPE
	// 1 : txt
	// 2 : excel
	// 3 : web
	private static final int STOCK_LIST_TYPE = 1;

	static DecimalFormat df = new DecimalFormat("###.##");

	public static String protocol;
	public static String host;
	public static String path;
	public static String filePath;
	public static String protocolHost;
	public static String file;

	public static String getDateForFileName(String strDate) {
		if (strDate == null) {
			return strDate;
		}
		strDate = strDate.replaceAll(" ", "_");
		strDate = strDate.replaceAll("/", ".");
		strDate = strDate.replaceAll(":", ".");
		strDate = strDate.replaceAll("T", "_");
		strDate = strDate.replaceAll("\\+", "_");
		strDate = "[" + strDate + "]";

		return strDate;
	}

	public static String getTitleForFileName(String strTitleForFileName) {
		if (strTitleForFileName == null) {
			return strTitleForFileName;
		}
		strTitleForFileName = strTitleForFileName.replaceAll("\\|", "_");
//		strTitleForFileName = strTitleForFileName.replaceAll("|", "_");
		strTitleForFileName = strTitleForFileName.replaceAll(" ", "_");
		strTitleForFileName = strTitleForFileName.replaceAll("/", ".");
		strTitleForFileName = strTitleForFileName.replaceAll(":", ".");
		strTitleForFileName = strTitleForFileName.replaceAll("\"", "'");
		strTitleForFileName = strTitleForFileName.replaceAll("\\?", "§");
		strTitleForFileName = strTitleForFileName.replaceAll("<", "[");
		strTitleForFileName = strTitleForFileName.replaceAll(">", "]");
		strTitleForFileName = strTitleForFileName.replaceAll("‘", "'");
		strTitleForFileName = strTitleForFileName.replaceAll("’", "'");
		return strTitleForFileName;
	}

	public static String nbspString(String s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			sb.append(s.charAt(i));
			sb.append("&nbsp;");
		}
		return sb.toString();
	}

	public static List<StockVO> readKospiStockCodeNameList() {
		String kospiFileName = GlobalVariables.kospiFileName;
		List<StockVO> kospiStockList = new ArrayList<>();
		try {
			kospiStockList = readStockCodeNameListFromExcel(kospiFileName);
		} catch (Exception ex) {
			logger.debug("1.ex.getMessage :" + ex.getMessage());
			kospiStockList = getStockCodeNameListFromKindKrxCoKr("stockMkt");
		}
		return kospiStockList;
	}

	public static List<StockVO> readKosdaqStockCodeNameList() {
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		List<StockVO> kosdaqStockList = new ArrayList<>();
		try {
			kosdaqStockList = readStockCodeNameListFromExcel(kosdaqFileName);
		} catch (Exception ex) {
			logger.debug("2.ex.getMessage :" + ex.getMessage());
			kosdaqStockList = getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
		}
		return kosdaqStockList;
	}

	public static List<StockVO> readKospiStockCodeNameListFromTxtFile() {
		String kospiFileName = GlobalVariables.KOSPI_LIST_TXT;
		List<StockVO> kospiStockList = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(kospiFileName));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = "";
			StockVO svo;
			String strStockCode;
			String strStockName;
			while ((line = br.readLine()) != null) {
				logger.debug("line:" + line);
				if (line.contains(" ")) {
					String separator = "[ \t]*";
					String lineArray[] = line.split(Pattern.quote(separator));
//					String lineArray[] = line.split("[ \t]*");
					if (lineArray.length >= 2) {
						svo = new StockVO();
						strStockCode = lineArray[0];
						strStockName = lineArray[1];
						svo.setStockCode(strStockCode);
						svo.setStockName(strStockName);
						kospiStockList.add(svo);
					}
				}
			}
		} catch (Exception ex) {
			logger.debug("1.ex.getMessage :" + ex.getMessage());
		}
		return kospiStockList;
	}

	public static List<StockVO> readKosdaqStockCodeNameListFromTxtFile() {
		String kosdaqFileName = GlobalVariables.KOSDAQ_LIST_TXT;
		List<StockVO> kosdaqStockList = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(kosdaqFileName));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = "";
			StockVO svo;
			String strStockCode;
			String strStockName;
			while ((line = br.readLine()) != null) {
				logger.debug("line:" + line);
				if (line.contains(" ")) {
					String separator = "[ \t]*";
					String lineArray[] = line.split(Pattern.quote(separator));
//					String lineArray[] = line.split("[ \t]*");
					if (lineArray.length >= 2) {
						svo = new StockVO();
						strStockCode = lineArray[0];
						strStockName = lineArray[1];
						svo.setStockCode(strStockCode);
						svo.setStockName(strStockName);
						kosdaqStockList.add(svo);
					}
				}
			}
		} catch (Exception ex) {
			logger.debug("1.ex.getMessage :" + ex.getMessage());
		}
		return kosdaqStockList;
	}

	public static List<StockVO> readStockCodeNameList(String marketType) {
		String strFileName = "";
		if (marketType.equals("kospi") || marketType.equals("코스피")) {
			if (STOCK_LIST_TYPE == 1) {
				strFileName = GlobalVariables.KOSPI_LIST_TXT;
				return readStockCodeNameListFromTxtFile(strFileName);
			} else if (STOCK_LIST_TYPE == 2) {
				strFileName = GlobalVariables.kospiFileName;
				return readStockCodeNameListFromExcel(strFileName);
			} else if (STOCK_LIST_TYPE == 3) {
				String krxMarketType = "stockMkt";
				return readStockCodeNameListFromKrx(krxMarketType);
			} else {
				strFileName = GlobalVariables.KOSPI_LIST_TXT;
				return readStockCodeNameListFromTxtFile(strFileName);
			}
		} else {
			if (STOCK_LIST_TYPE == 1) {
				strFileName = GlobalVariables.KOSDAQ_LIST_TXT;
				return readStockCodeNameListFromTxtFile(strFileName);
			} else if (STOCK_LIST_TYPE == 2) {
				strFileName = GlobalVariables.kosdaqFileName;
				return readStockCodeNameListFromExcel(strFileName);
			} else if (STOCK_LIST_TYPE == 3) {
				String krxMarketType = "kosdaqMkt";
				return readStockCodeNameListFromKrx(krxMarketType);
			} else {
				strFileName = GlobalVariables.KOSDAQ_LIST_TXT;
				return readStockCodeNameListFromTxtFile(strFileName);
			}
		}
	}

	public static List<StockVO> readStockCodeNameListFromTxtFile(String stockListTxtFileName) {
		List<StockVO> stockList = new ArrayList<>();
		try {
			FileInputStream fileInputStream = new FileInputStream(new File(stockListTxtFileName));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
			BufferedReader br = new BufferedReader(inputStreamReader);
			String line = "";
			StockVO svo;
			String strStockCode;
			String strStockName;
			while ((line = br.readLine()) != null) {
				logger.debug("line:" + line);
				if (line.matches("[0-9]*[ \t].*")) {
					String separator = "\t";
					String lineArray[] = line.split(Pattern.quote(separator));
//					String lineArray[] = line.split("[ \t]*");
					if (lineArray.length >= 2) {
						svo = new StockVO();
						strStockCode = lineArray[0];
						strStockName = lineArray[1];
						svo.setStockCode(strStockCode);
						svo.setStockName(strStockName);
						svo.setStockNameLength(strStockName.length());
						stockList.add(svo);
					}
				}
			}
		} catch (Exception ex) {
			logger.debug("1.ex.getMessage :" + ex.getMessage());
		}
		return stockList;
	}

	public static void readStockCodeNameListFromTxtFile(List<StockVO> stockList, String stockListTxtFileName) {
		try {

			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			logger.debug(". AbsolutePath:" + new File(".").getAbsolutePath());
			File f = new File("./" + stockListTxtFileName);
			logger.debug("f.exists():" + f.exists());
			InputStream inputStream = null;
			if (f.exists()) {
				inputStream = new FileInputStream(f);
				logger.debug("inputStream1: " + inputStream);
			} else {
				// classes root 경로
				inputStream = StockUtil.class.getResourceAsStream("/" + stockListTxtFileName);
				logger.debug("inputStream2: " + inputStream);
				logger.debug("class 경로 read " + stockListTxtFileName + " Resource");
			}

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
				BufferedReader br = new BufferedReader(inputStreamReader);
				String line = "";
				StockVO svo;
				String strStockCode;
				String strStockName;
				while ((line = br.readLine()) != null) {
					logger.debug("line:" + line);
					if (line.matches("[0-9]*[ \t].*")) {
						String separator = "\t";
						String lineArray[] = line.split(Pattern.quote(separator));
//					String lineArray[] = line.split("[ \t]*");
						if (lineArray.length >= 2) {
							svo = new StockVO();
							strStockCode = lineArray[0];
							strStockName = lineArray[1];
							svo.setStockCode(strStockCode);
							svo.setStockName(strStockName);
							svo.setStockNameLength(strStockName.length());
							stockList.add(svo);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.debug("1.ex.getMessage :" + ex.getMessage());
		}
	}

	public static List<StockVO> readKospiStockCodeNameListFromExcel() throws Exception {
		String kospiFileName = GlobalVariables.kospiFileName;
		List<StockVO> kospiStockList = new ArrayList<>();

		readStockCodeNameListFromExcel(kospiStockList, kospiFileName);
		Collections.sort(kospiStockList, new StockNameLengthDescCompare());
		return kospiStockList;
	}

	public static List<StockVO> readKosdaqStockCodeNameListFromExcel() throws Exception {
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		List<StockVO> kosdaqStockList = new ArrayList<>();

		readStockCodeNameListFromExcel(kosdaqStockList, kosdaqFileName);
		Collections.sort(kosdaqStockList, new StockNameLengthDescCompare());
		return kosdaqStockList;
	}

	/**
	 * marketType = stockMkt, kosdaqMkt
	 *
	 * @param marketType
	 * @return
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromKrx(String marketType) {
		List<StockVO> stockList = getStockCodeNameListFromKindKrxCoKr(marketType);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	/**
	 *
	 * @param marketType
	 * @param searchType 13:상장법인 01:관리종목 05:불성실공시법인 07:자산2조법인 99:외국법인 181:(코스닥)
	 *                   우량기업부 182:(코스닥) 벤처기업부 183:(코스닥) 중견기업부 184:(코스닥) 기술성장기업부
	 *                   11:KRX100 06:KOSPI200 09:STAR30 10:PREMIER
	 * @return
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromKrx(String marketType, String searchType) throws Exception {
		List<StockVO> stockList = getStockCodeNameListFromKindKrxCoKr(marketType, searchType);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readKospiStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = getStockCodeNameListFromKindKrxCoKr("stockMkt");
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readKosdaqStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readAllStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = new ArrayList<>();
		List<StockVO> stockList1 = getStockCodeNameListFromKindKrxCoKr("stockMkt");
		List<StockVO> stockList2 = getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
		stockList.addAll(stockList1);
		stockList.addAll(stockList2);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readAllStockCodeNameListFromExcel() throws Exception {
		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			stockList.addAll(readStockCodeNameListFromExcel(stockList, kospiFileName));
			stockList.addAll(readStockCodeNameListFromExcel(stockList, kosdaqFileName));
		} catch (Exception e) {
			e.printStackTrace();
			stockList.addAll(getStockCodeNameListFromKindKrxCoKr("stockMkt"));
			stockList.addAll(getStockCodeNameListFromKindKrxCoKr("kosdaqMkt"));
		}
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static StringBuilder makeStockLinkString(StringBuilder sb1) throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			stockList = readAllStockCodeNameListFromExcel();
		} catch (Exception e) {
			e.printStackTrace();
			return sb1;
		}
		for (int i = 0; i < stockList.size(); i++) {
			StockVO vo = stockList.get(i);
//			logger.debug("증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
		}
		if (stockList.size() <= 0) {
			logger.debug("추출한 종목이 없습니다.");
			return sb1;
		}
		return StockUtil.stockLinkString(sb1, stockList);
	}

	public static String makeStockLinkString(String textBodyHtml) throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			stockList = readAllStockCodeNameListFromExcel();
		} catch (Exception e) {
			e.printStackTrace();
			return textBodyHtml;
		}
		logger.debug("stockList.size:" + stockList.size());
		for (int i = 0; i < stockList.size(); i++) {
			StockVO vo = stockList.get(i);
//			logger.debug("증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
		}
		if (stockList.size() <= 0) {
			logger.debug("추출한 종목이 없습니다.");
			return textBodyHtml;
		}
		return stockLinkString(textBodyHtml, stockList);
	}

	public static String makeStockLinkStringByKrx(String textBodyHtml) {

		List<StockVO> stockList = new ArrayList<>();

		stockList.addAll(getStockCodeNameListFromKindKrxCoKr("stockMkt"));
		stockList.addAll(getStockCodeNameListFromKindKrxCoKr("kosdaqMkt"));
		Collections.sort(stockList, new StockNameLengthDescCompare());
		for (int i = 0; i < stockList.size(); i++) {
			StockVO vo = stockList.get(i);
//		logger.debug("증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
		}
		if (stockList.size() <= 0) {
			logger.debug("추출한 종목이 없습니다.");
			return textBodyHtml;
		}
		return StockUtil.stockLinkString(textBodyHtml, stockList);
	}

	public static String makeStockLinkStringByTxtFile(String textBodyHtml) {

		String kospiFileName = GlobalVariables.KOSPI_LIST_TXT;
		String kosdaqFileName = GlobalVariables.KOSDAQ_LIST_TXT;
		List<StockVO> stockList = new ArrayList<>();

		List<StockVO> kospiStockList = readStockCodeNameListFromTxtFile(kospiFileName);
		List<StockVO> kosdaqStockList = readStockCodeNameListFromTxtFile(kosdaqFileName);
		stockList.addAll(kospiStockList);
		stockList.addAll(kosdaqStockList);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		if (stockList.size() <= 0) {
			logger.debug("추출한 종목이 없습니다.");
			return textBodyHtml;
		}
		return StockUtil.stockLinkString(textBodyHtml, stockList);
	}

	public static String makeStockLinkStringByExcel(String textBodyHtml) {

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		List<StockVO> stockList = new ArrayList<>();

		try {
			stockList.addAll(readStockCodeNameListFromExcel(kospiFileName));
			stockList.addAll(readStockCodeNameListFromExcel(kosdaqFileName));
		} catch (Exception e) {
			logger.debug("Exception e:" + e.getMessage());
			e.printStackTrace();
			stockList.addAll(getStockCodeNameListFromKindKrxCoKr("stockMkt"));
			stockList.addAll(getStockCodeNameListFromKindKrxCoKr("kosdaqMkt"));
		}
		Collections.sort(stockList, new StockNameLengthDescCompare());
		for (int i = 0; i < stockList.size(); i++) {
			StockVO vo = stockList.get(i);
//		logger.debug("증권코드:" + vo.getStockCode() + " 증권명:" + vo.getStockName());
		}
		if (stockList.size() <= 0) {
			logger.debug("추출한 종목이 없습니다.");
			return textBodyHtml;
		}
		return StockUtil.stockLinkString(textBodyHtml, stockList);
	}

	public static StringBuilder makeStockLinkString(StringBuilder sb1, List<StockVO> stockList) {
		return stockLinkString(sb1, stockList);
	}

	public static StringBuilder stockLinkString(StringBuilder sb, List<StockVO> stockList) {
		String strStockLinkString = stockLinkString(sb.toString(), stockList);
		return new StringBuilder(strStockLinkString);
	}

	public static List<String> exceptWordList() {
		List<String> exceptWordList = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			File f = new File("./StockExtractExceptWord.txt");
//			System.out.println("f.exists1:"+f.exists());
			if (f.exists()) {
				fr = new FileReader(f);
				br = new BufferedReader(fr);
			} else {
				// classes root 경로
				f = new File("/StockExtractExceptWord.txt");
//				System.out.println("f.exists2:"+f.exists());
				fr = new FileReader(f);
				br = new BufferedReader(fr);
			}

			String strLine = "";
			while ((strLine = br.readLine()) != null) {
				exceptWordList.add(strLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return exceptWordList;
	}

	public static String stockTitleLinkString(String strNews, List<StockVO> stockList) {
		logger.debug("stockLinkString.....................");
		strNews = strNews.replaceAll("&amp;", "&");
		// logger.debug("strNews:[" + strNews + "]");
		// logger.debug("stockList.size():[" + stockList.size() + "]");
		List<String> exceptWordList = exceptWordList();

		Document doc = null;
		List<StockVO> newsStockList = new ArrayList<StockVO>();
		for (int i = 0; i < stockList.size(); i++) {
			StockVO stock = stockList.get(i);
			String stockCode = stock.getStockCode();
			String stockName = stock.getStockName();

			doc = Jsoup.parse(strNews);
			if (strNews.contains("현대차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			} else if (strNews.contains("현대자동차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대자동차", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("현대자동차") + "</a></strong>");
			} else if (strNews.contains("현대자동차") && stockName.equals("현대차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대자동차", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("현대자동차") + "</a></strong>");
			} else if (strNews.contains("현대차") && stockName.equals("현대차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			} else if (strNews.contains("POSCO") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("POSCO") + "</a></strong>");
			} else if (strNews.contains("POSCO") && stockName.equals("POSCO")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("POSCO") + "</a></strong>");
			} else if (strNews.contains("포스코") && stockName.equals("POSCO")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("포스코",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("포스코") + "</a></strong>");
			} else if (strNews.contains("포스코") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("포스코",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("포스코") + "</a></strong>");
			} else if (strNews.contains(stockName)) {
				int count = StringUtils.countMatches(strNews, stockName);
				logger.debug(stockName + " 갯수:" + count);
				Elements imgs = doc.select("img");
				if (imgs.size() > 0) {
					String src = imgs.attr("src");
					if (src.contains(stockName)) {
						continue;
					}
				}
				if (StockExtractExceptWord.dupCheck(exceptWordList, stockName, strNews)) {
					logger.debug("예외어 발견.....");
					break;
				}
				newsStockList.add(stock);
				// logger.debug("stock link : " + stockCode + ":" + stockName);
				strNews = strNews.replaceAll(stockName, "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString(stockName) + "</a></strong>");

			}

		}
		strNews = strNews.replaceAll("&nbsp;", "");
		logger.debug("stockLinkString end.....................");
		return strNews;
	}

	public static String stockLinkString(String strNews, List<StockVO> stockList) {
		logger.debug("stockLinkString.....................");
		strNews = strNews.replaceAll("&amp;", "&");
		// logger.debug("strNews:[" + strNews + "]");
		// logger.debug("stockList.size():[" + stockList.size() + "]");
		List<String> exceptWordList = exceptWordList();

		Document doc = null;
		List<StockVO> newsStockList = new ArrayList<StockVO>();
		for (int i = 0; i < stockList.size(); i++) {
			StockVO stock = stockList.get(i);
			String stockCode = stock.getStockCode();
			String stockName = stock.getStockName();
			if (stockName.equals("씨젠")) {
				logger.debug("뉴스가 씨젠을 포함하고 있는가?" + strNews.contains("씨젠") + "");
			}

			doc = Jsoup.parse(strNews);

			if (strNews.contains("현대차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			} else if (strNews.contains("현대자동차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대자동차", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("현대자동차") + "</a></strong>");
			} else if (strNews.contains("현대자동차") && stockName.equals("현대차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대자동차", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("현대자동차") + "</a></strong>");
			} else if (strNews.contains("현대차") && stockName.equals("현대차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			} else if (strNews.contains("POSCO") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("POSCO") + "</a></strong>");
			} else if (strNews.contains("POSCO") && stockName.equals("POSCO")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString("POSCO") + "</a></strong>");
			} else if (strNews.contains("포스코") && stockName.equals("POSCO")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("포스코",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("포스코") + "</a></strong>");
			} else if (strNews.contains("포스코") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("포스코",
						"<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>" + nbspString("포스코") + "</a></strong>");
			} else if (strNews.contains(stockName)) {
				int count = StringUtils.countMatches(strNews, stockName);
				logger.debug(stockName + " 갯수:" + count);
				Elements imgs = doc.select("img");
				if (imgs.size() > 0) {
					String src = imgs.attr("src");
					if (src.contains(stockName)) {
						continue;
					}
				}
				if (StockExtractExceptWord.dupCheck(exceptWordList, stockName, strNews)) {
					logger.debug("예외어 발생.....");
					continue;
				}
				newsStockList.add(stock);
				// logger.debug("stock link : " + stockCode + ":" + stockName);
				strNews = strNews.replaceAll(stockName, "<strong><a href='" + TOTAL_INFO_URL + stockCode + "'>"
						+ nbspString(stockName) + "</a></strong>");

			}
		}

		logger.debug("newsStockList:" + newsStockList);
		logger.debug("newsStockList.size:" + newsStockList.size());
		List<StockVO> newsStockList2 = getNewsStockInfo(newsStockList);
		logger.debug("newsStockList2.size:" + newsStockList2.size());
		StringBuilder newsStockTable = createNewsStockTable(newsStockList2);
//		StringBuilder newsStockChart = drawNewsStockChart(newsStockList2);
//		newsStockTable.append(newsStockChart);

		strNews = strNews.replaceAll("&nbsp;", "");
		logger.debug("stockLinkString end.....................");
		return strNews + "<br>" + newsStockTable.toString();
	}

	/**
	 * 엑셀 파일 읽어서 코스피,코스닥 종목코드,종목명 목록을 추출한다.
	 *
	 * @param stockList
	 * @param fileName
	 * @return List<StockVO>
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromExcel(List<StockVO> stockList, String fileName)
			throws Exception {
		List<StockVO> svoList = new ArrayList<>();
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		logger.debug("fileName:" + fileName);
		File file = new File(fileName);
		Workbook workbook = WorkbookFactory.create(file);
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		Iterator<Row> rowIterator = sheet.rowIterator();
		int cnt = 0;
		while (rowIterator.hasNext()) {
			StockVO svo = new StockVO();
			Row row = rowIterator.next();

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();
			String strStockName = null;
			String strStockCode = null;
			int stockNameLength = 0;
			if (row.getLastCellNum() > 1) {
				int i = 0;
				while (cellIterator.hasNext()) {
					if (i == 2) {
						break;
					}
					Cell cell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);
					if (i == 0) {
						strStockName = cellValue;
						svo.setStockName(strStockName);
						stockNameLength = strStockName.length();
						svo.setStockNameLength(stockNameLength);
					}
					if (i == 1) {
						strStockCode = cellValue;
						svo.setStockCode(strStockCode);
					}
					i++;
				}
				if (strStockCode.length() != 6) {
					continue;
				}
//					logger.debug(strStockCode + "\t" + strStockName);
			}
			svoList.add(svo);
			cnt++;
		}
		// Closing the workbook
		workbook.close();
		stockList.addAll(svoList);
		return stockList;
	}

	/**
	 * 엑셀 파일 읽어서 코스피,코스닥 종목코드,종목명 목록을 추출한다.
	 *
	 * @param stockList
	 * @param fileName
	 * @return List<StockVO>
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromExcel(String fileName) {
		List<StockVO> svoList = new ArrayList<>();
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		logger.debug("fileName:" + fileName);
		File file = new File(fileName);
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(file);

			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();

			// 1. You can obtain a rowIterator and columnIterator and iterate over them
			Iterator<Row> rowIterator = sheet.rowIterator();
			int cnt = 0;
			while (rowIterator.hasNext()) {
				StockVO svo = new StockVO();
				Row row = rowIterator.next();

				// Now let's iterate over the columns of the current row
				Iterator<Cell> cellIterator = row.cellIterator();
				String strStockName = null;
				String strStockCode = null;
				int stockNameLength = 0;
				if (row.getLastCellNum() > 1) {
					int i = 0;
					while (cellIterator.hasNext()) {
						if (i == 2) {
							break;
						}
						Cell cell = cellIterator.next();
						String cellValue = dataFormatter.formatCellValue(cell);
						if (i == 0) {
							strStockName = cellValue;
							svo.setStockName(strStockName);
							stockNameLength = strStockName.length();
							svo.setStockNameLength(stockNameLength);
						}
						if (i == 1) {
							strStockCode = cellValue;
							svo.setStockCode(strStockCode);
						}
						i++;
					}
					if (strStockCode.length() != 6) {
						continue;
					}
//					logger.debug(strStockCode + "\t" + strStockName);
				}
				svoList.add(svo);
				cnt++;
			}
			// Closing the workbook
			workbook.close();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return svoList;
	}

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> svoList = new ArrayList<>();
		int cnt = 0;
		for (StockVO svo : stockList) {
			cnt++;
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();
			StockVO vo = getStockInfo(cnt, stockCode, stockName);
			if (vo != null) {
				svoList.add(vo);
			} else {
				logger.debug("vo##########:" + vo);
				logger.debug(stockName + "(" + stockCode + ") is null");
//				stockList.remove(svo);
			}
		}
		return svoList;
	}

	public static List<StockVO> getAllStockInfoAddBaseDayPrice(List<StockVO> stockList, String baseDay,
			String thisYearFirstTradeDay, int pageNo) {
		List<StockVO> svoList = new ArrayList<>();
		int cnt = 0;
		for (StockVO svo : stockList) {
			cnt++;
			String strStockCode = svo.getStockCode();
			String strStockName = svo.getStockName();
			StockVO vo = getStockInfo(cnt, strStockCode, strStockName);
			logger.debug("getCurPrice :" + vo.getCurPrice());
			logger.debug("getiCurPrice :" + vo.getiCurPrice());

			if (vo != null) {
				// ===========================================================================
				// 상장일 구하기
				String listedDay = StringUtils.defaultString(StockUtil.getStockListedDay(strStockCode));
				logger.debug("listedDay :" + listedDay);
				if (listedDay.equals("")) {
					logger.debug(
							strStockName + "(" + strStockCode + ")" + " 상장일 정보가 없습니다. 존재하지 않는 주식입니다.(상장폐지 여부 확인 필요)");
					continue;
				}
				vo.setListedDay(listedDay);

				// 기준일가 또는 올해 상장했을 경우 상장일가 구하기
				if (baseDay.equals("")) {
					baseDay = thisYearFirstTradeDay;
				}
				String chosenDay = StockUtil.getChosenDay(baseDay, listedDay);
				logger.debug("chosenDay :" + chosenDay);
				vo.setChosenDay(chosenDay);

//				String chosenDayEndPrice = StockUtil.getChosenDayEndPrice(strStockCode, strStockName, chosenDay);
				String chosenDayEndPrice = "0";
				if (baseDay.equals(chosenDay)) {
					chosenDayEndPrice = StockUtil.findChosenDayEndPrice(strStockCode, strStockName, chosenDay, pageNo);
				} else {
					chosenDayEndPrice = StockUtil.getChosenDayEndPrice(strStockCode, strStockName, chosenDay);
				}
				logger.debug("chosenDayEndPrice :" + chosenDayEndPrice);
				vo.setChosenDayEndPrice(chosenDayEndPrice);

				chosenDayEndPrice = chosenDayEndPrice.replaceAll(",", "");
				logger.debug("chosenDayEndPrice :" + chosenDayEndPrice);
				if (chosenDayEndPrice.equals("")) {
					chosenDayEndPrice = "0";
				}
				int iChosenDayEndPrice = Integer.parseInt(chosenDayEndPrice);
				vo.setiChosenDayEndPrice(iChosenDayEndPrice);
				logger.debug("iChosenDayEndPrice :" + iChosenDayEndPrice);

				int iCurPrice = vo.getiCurPrice();
				// ===========================================================================
				double upDownRatio = 0d;
				if (iChosenDayEndPrice != 0) {
					if (iChosenDayEndPrice < iCurPrice) {
						double d1 = iCurPrice - iChosenDayEndPrice;
						double d2 = d1 / iChosenDayEndPrice * 100;
						upDownRatio = Math.round(d2 * 100) / 100.0;
					} else if (iChosenDayEndPrice > iCurPrice) {
						double d1 = iChosenDayEndPrice - iCurPrice;
						double d2 = d1 / iChosenDayEndPrice * 100;
						upDownRatio = -(Math.round(d2 * 100) / 100.0);
					}
				}
				logger.debug("iCurPrice:" + iCurPrice + " iChosenDayEndPrice :" + iChosenDayEndPrice);
				logger.debug("특정일 대비 up,down 비율:" + upDownRatio + "%");
				vo.setChosenDayEndPriceVsCurPriceUpDownRatio(upDownRatio);
				// ===========================================================================

				svoList.add(vo);
			} else {
				logger.debug("vo##########:" + vo);
				logger.debug(strStockName + "(" + strStockCode + ") is null");
//				stockList.remove(svo);
			}
		}
		return svoList;
	}

	/**
	 * 엑셀 파일 읽어서 코스피,코스닥 종목코드,종목명 목록을 추출한다.
	 *
	 * @param fileName
	 * @return List<StockVO>
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	public static List<StockVO> getAllStockListFromExcel(String fileName)
			throws IOException, EncryptedDocumentException, InvalidFormatException {
		List<StockVO> svoList = new ArrayList<>();
		// Creating a Workbook from an Excel file (.xls or .xlsx)
		logger.debug("fileName:" + fileName);
		File file = new File(fileName);
		Workbook workbook = WorkbookFactory.create(file);
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		logger.debug("\n\n getAllStockListFromExcel \n");
		logger.debug("\n\nIterating over Rows and Columns using Iterator3\n");
		Iterator<Row> rowIterator = sheet.rowIterator();
		int cnt = 0;
		while (rowIterator.hasNext()) {
			StockVO svo = new StockVO();
			Row row = rowIterator.next();

			// Now let's iterate over the columns of the current row
			Iterator<Cell> cellIterator = row.cellIterator();
			String strStockName = null;
			String strStockCode = null;
			if (row.getLastCellNum() > 1) {
				int i = 0;
				while (cellIterator.hasNext()) {
					if (i == 2) {
						break;
					}
					Cell cell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);
					if (i == 0) {
						strStockName = cellValue;
						svo.setStockName(strStockName);
					}
					if (i == 1) {
						strStockCode = cellValue;
						svo.setStockCode(strStockCode);
					}
					i++;
				}
				if (strStockCode.length() != 6) {
					continue;
				}
				svoList.add(svo);
			}
			cnt++;
		}
		// Closing the workbook
		workbook.close();
		return svoList;
	}

	public List<StockVO> getAllStockInfo(String fileName) {
		List<StockVO> svoList = new ArrayList<>();
		try {
			// Creating a Workbook from an Excel file (.xls or .xlsx)
			logger.debug("fileName:" + fileName);
			File file = new File(fileName);
			Workbook workbook = WorkbookFactory.create(file);
			// Getting the Sheet at index zero
			Sheet sheet = workbook.getSheetAt(0);

			// Create a DataFormatter to format and get each cell's value as String
			DataFormatter dataFormatter = new DataFormatter();

			// 1. You can obtain a rowIterator and columnIterator and iterate over them
			logger.debug("\n\n getAllStockListFromExcel \n");
			logger.debug("\n\nIterating over Rows and Columns using Iterator3\n");
			Iterator<Row> rowIterator = sheet.rowIterator();
			int cnt = 0;
			while (rowIterator.hasNext()) {
				StockVO svo = new StockVO();
				Row row = rowIterator.next();

				// Now let's iterate over the columns of the current row
				Iterator<Cell> cellIterator = row.cellIterator();
				String strStockName = null;
				String strStockCode = null;
				if (row.getLastCellNum() > 1) {
					int i = 0;
					while (cellIterator.hasNext()) {
						if (i == 2) {
							break;
						}
						Cell cell = cellIterator.next();
						String cellValue = dataFormatter.formatCellValue(cell);
						if (i == 0) {
							strStockName = cellValue;
							svo.setStockName(strStockName);
						}
						if (i == 1) {
							strStockCode = cellValue;
							svo.setStockCode(strStockCode);
						}
						i++;
					}
					if (strStockCode.length() != 6) {
						continue;
					}
					svo = getStockInfo(cnt, strStockCode, strStockName);
					if (svo != null) {
						svoList.add(svo);
					}
				}
				cnt++;
			}
			// Closing the workbook
			workbook.close();
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvalidFormatException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (EncryptedDocumentException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return svoList;
	}

	/**
	 * extract stockcode, stockname from kind.krx.co.kr gubun = stockMkt, kosdaqMkt
	 *
	 * @param stockList
	 * @param fileName
	 * @return
	 */
	public static final String SERVER_URI = "http://kind.krx.co.kr/corpgeneral/corpList.do";

	public static List<StockVO> getStockCodeNameListFromKindKrxCoKr(String marketType, String searchType) {
		List<StockVO> svoList = new ArrayList<>();
		try {
			String param = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3"
					+ "&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=" + "&marketType=" + marketType + "&searchType="
					+ searchType + "&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

			String strUri = SERVER_URI + "?" + param;
			logger.debug("strUri:" + strUri);
//			Document doc = Jsoup.parse(new URL(strUri).openStream(), "EUC-KR", strUri);

//			Connection conn = Jsoup.connect(strUri).cookie("cookiereference", "cookievalue").method(Method.POST);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", MediaType.APPLICATION_JSON
					+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			headers.put("Accept-Encoding", "gzip, deflate");
//			headers.put("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.put("Accept-Language", "ko");
			headers.put("Cache-Control", "max-age=0");
			headers.put("Connection", "keep-alive");
			headers.put("Content-Length", "215");
//			headers.put("Content-Type", "application/x-www-form-urlencoded");
//			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
//			headers.put("Content-Type", "text/*; charset=EUC-KR");
//			headers.put("Content-Type", "application/xml; charset=EUC-KR");
//			headers.put("Content-Type", "application/xhtml+xml; charset=EUC-KR");
			headers.put("Content-Type", "application/vnd.ms-excel; charset=EUC-KR");
			headers.put("Cookie",
					"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
			// headers.put("Host", "kind.krx.co.kr");
			// headers.put("Origin", "http://kind.krx.co.kr");
			// headers.put("Referer",
			// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
			headers.put("Host", "203.235.1.50");
			headers.put("Origin", "http://203.235.1.50");
			headers.put("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
			headers.put("Upgrade-Insecure-Requests", "1");
			headers.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.put("User-Agent", USER_AGENT);

			headers.put("X-Requested-With", "XMLHttpRequest");

//			Connection conn = Jsoup.connect(strUri).headers(headers).cookie("cookiereference", "cookievalue").method(Method.POST);
//			Document doc = Jsoup.parse(new String(conn.execute().bodyAsBytes(), "EUC-KR"));
			// Initialize UnSupportedMimeTypeExeception class
//			UnsupportedMimeTypeException mimeType = new UnsupportedMimeTypeException("Hey this is Mime", "application/vnd.ms-excel", strUri);
//			String mime = mimeType.getMimeType();
//			logger.debug("mime :" + mime);
//			Jsoup.connect(url).requestBody(json).header("Content-Type", "application/json").post();
			Document doc = Jsoup.connect(strUri).requestBody("JSON").headers(headers)
					// .cookies(response.cookies())
					.ignoreContentType(true).post();

			/* 총 라인수는 종목수 + 1, 첫째 줄은 header */
			Elements trElements = doc.select("tr");
			for (int i = 0; i < trElements.size(); i++) {
				Elements tdElements = trElements.get(i).select("td");
				if (tdElements.size() > 0) {
					StockVO svo = new StockVO();

					if (tdElements.size() > 1) {
						String strStockName = tdElements.get(0).text();
						String strStockCode = tdElements.get(1).text();
						logger.debug((i) + "." + strStockName + "(" + strStockCode + ")");
						svo.setStockName(strStockName);
						svo.setStockCode(strStockCode);
						svo.setStockNameLength(strStockName.length());
						if (strStockCode.length() != 6) {
							logger.debug(strStockCode + "\t" + strStockName + " 종목은 체크바랍니다.");
						}
						svoList.add(svo);
					}
				}
			}

		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (EncryptedDocumentException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		logger.debug("svoList.size :" + svoList.size());
		return svoList;
	}

	public static List<StockVO> getStockCodeNameListFromKindKrxCoKr(String marketType) {
		// searchType 13 = 상장법인
		String searchType = "13";
		List<StockVO> svoList = new ArrayList<>();
		try {
			String param = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3"
					+ "&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=" + "&marketType=" + marketType + "&searchType="
					+ searchType + "&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

			String strUri = SERVER_URI + "?" + param;
//			Document doc = Jsoup.parse(new URL(strUri).openStream(), "EUC-KR", strUri);

//			Connection conn = Jsoup.connect(strUri).cookie("cookiereference", "cookievalue").method(Method.POST);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", MediaType.APPLICATION_JSON
					+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			headers.put("Accept-Encoding", "gzip, deflate");
//			headers.put("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.put("Accept-Language", "ko");
			headers.put("Cache-Control", "max-age=0");
			headers.put("Connection", "keep-alive");
			headers.put("Content-Length", "215");
//			headers.put("Content-Type", "application/x-www-form-urlencoded");
//			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
//			headers.put("Content-Type", "text/*; charset=EUC-KR");
//			headers.put("Content-Type", "application/xml; charset=EUC-KR");
//			headers.put("Content-Type", "application/xhtml+xml; charset=EUC-KR");
			headers.put("Content-Type", "application/vnd.ms-excel; charset=EUC-KR");
			headers.put("Cookie",
					"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
			// headers.put("Host", "kind.krx.co.kr");
			// headers.put("Origin", "http://kind.krx.co.kr");
			// headers.put("Referer",
			// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
			headers.put("Host", "203.235.1.50");
			headers.put("Origin", "http://203.235.1.50");
			headers.put("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
			headers.put("Upgrade-Insecure-Requests", "1");
//			headers.put("User-Agent", USER_AGENT);
			headers.put("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.put("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

			headers.put("X-Requested-With", "XMLHttpRequest");

//			Connection conn = Jsoup.connect(strUri).headers(headers).cookie("cookiereference", "cookievalue").method(Method.POST);
//			Document doc = Jsoup.parse(new String(conn.execute().bodyAsBytes(), "EUC-KR"));
			// Initialize UnSupportedMimeTypeExeception class
			UnsupportedMimeTypeException mimeType = new UnsupportedMimeTypeException("Hey this is Mime",
					"application/vnd.ms-excel", strUri);
			String mime = mimeType.getMimeType();
			logger.debug("mime :" + mime);
//			Jsoup.connect(url).requestBody(json).header("Content-Type", "application/json").post();
			Document doc = Jsoup.connect(strUri).requestBody("JSON").headers(headers)
					// .cookies(response.cookies())
					.ignoreContentType(true).post();
			/* 총 라인수는 종목수 + 1, 첫째 줄은 header */
			Elements trElements = doc.select("tr");
			for (int i = 0; i < trElements.size(); i++) {
				Elements tdElements = trElements.get(i).select("td");
				if (tdElements.size() > 0) {
					StockVO svo = new StockVO();

					if (tdElements.size() > 1) {
						String strStockName = tdElements.get(0).text();
						String strStockCode = tdElements.get(1).text();
						System.out.print((i) + "." + strStockName + "(");
						logger.debug(strStockCode + ")");
						svo.setStockName(strStockName);
						svo.setStockCode(strStockCode);
						svo.setStockNameLength(strStockName.length());
						if (strStockCode.length() != 6) {
							logger.debug(strStockCode + "\t" + strStockName + " 종목은 체크바랍니다.");
						}
						svoList.add(svo);
					}
				}
			}

		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (EncryptedDocumentException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		logger.debug("svoList.size :" + svoList.size());
		return svoList;
	}

	// @Test
	public void test() {
		getStockInfo(1, "159650", "하이골드8호");
	}

	public static StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		logger.debug("stockName:" + strStockName + "(" + strStockCode + ")");
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
		try {
			// 종합정보
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();
			// logger.debug("doc:"+doc);

			Elements new_totalinfos = doc.select(".new_totalinfo");
			if (new_totalinfos.size() <= 0) {
				return stock;
			}
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
					logger.debug("varyRatio :" + varyRatio);
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public static void readFile(List<StockVO> stockList, String fileName) {
//        //파일 입력
//        FileInputStream fileInputStream;
//        try {
//            fileInputStream = new FileInputStream(fileName);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            //파일 출력
//            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//            OutputStreamWriter OutputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF8");
//            BufferedWriter bufferedWriter = new BufferedWriter(OutputStreamWriter);
//        } catch (FileNotFoundException ex) {
//            java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedEncodingException ex) {
//            java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }

		logger.debug("stockList start.....................");
		File f = new File(userHome + "\\documents\\" + fileName);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String read = null;
			String stockCode = null;
			String stockName = null;
			int stockNameLength = 0;
			int cnt = 1;
			while ((read = reader.readLine()) != null) {
				logger.debug(cnt + "." + read);
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
				cnt++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e1) {
		} finally {
			logger.debug("stockList end.....................");
		}
	}

	public static List<StockVO> getNewsStockInfo(List<StockVO> stockList) {

		for (StockVO stock : stockList) {
			getNewsStockInfo(stock);
		}
		return stockList;
	}

	public static StockVO getNewsStockInfo(StockVO stock) {
		Document doc;

		String code = stock.getStockCode();

		try {
			// 종합정보
			doc = Jsoup.connect(TOTAL_INFO_URL + code).get();

			Elements dates = doc.select(".date");
			if (dates != null) {
				if (dates.size() > 0) {
					Element date = dates.get(0);
					strYMD = date.ownText();
					strYMD = date.childNode(0).toString().trim();
					strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
				}
			}
			logger.debug("strYMD2:[" + strYMD + "]");

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
				logger.debug("text:" + text);
				if (text.startsWith("종목명")) {
					String stockName = text.substring(4);
					logger.debug("stockName:" + stockName);
					stock.setStockName(stockName);
				}

				if (text.startsWith("현재가")) {
					logger.debug("data1:" + dd.text());
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public StockVO getStockInfo(StockVO stock) {
		Document doc;

		String strStockCode = stock.getStockCode();

		try {
			// 종합정보
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();

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
				logger.debug("text:" + text);
				if (text.startsWith("종목명")) {
					String stockName = text.substring(4);
					logger.debug("stockName:" + stockName);
					stock.setStockName(stockName);
				}

				if (text.startsWith("현재가")) {
					logger.debug("data1:" + dd.text());
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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public static StringBuilder createNewsStockTable(List<StockVO> stockList) {
		StringBuilder sb1 = new StringBuilder();
		if (stockList != null && stockList.size() > 0) {
			sb1.append("<table width='548'>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			sb1.append(
					"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>일차트</td>\r\n");
			sb1.append("</tr>\r\n");
			int cnt = 1;
			for (StockVO s : stockList) {
				if (s != null) {
					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					logger.debug("specialLetter+++>" + specialLetter);
					sb1.append("<tr>\r\n");
					String url = TOTAL_INFO_URL + s.getStockCode();
					sb1.append("<td>").append(cnt++).append("</td>\r\n");
					sb1.append("<td><a href='").append(url).append("' target='_sub'>").append(s.getStockName())
							.append("</a></td>\r\n");

					String varyPrice = s.getVaryPrice();

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
					sb1.append("<td style='text-align:right'>")
							.append(StringUtils.defaultIfEmpty(s.getTradingVolume(), "")).append("</td>\r\n");
					sb1.append("<td style='text-align:right'>")
							.append(StringUtils.defaultIfEmpty(s.getTradingAmount(), "")).append("</td>\r\n");
					sb1.append("<td style='border-bottom:1px solid gray;background-color:white;'>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/area/day/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/area/day/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/day/"
							+ s.getStockCode()
							+ ".png' target='_new'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/day/"
							+ s.getStockCode() + ".png' width='100px'></a><br/>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/week/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/week/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/month/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/month/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
					sb1.append("</td>\r\n");
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
		}
		logger.debug("createNewsStockTable:[" + sb1.toString() + "]");
		return sb1;
	}

	public static StringBuilder drawNewsStockChart(List<StockVO> stockList) {
		StringBuilder sb1 = new StringBuilder();
		if (stockList != null && stockList.size() > 0) {
			sb1.append("<table width='548' style='border:1px solid gray;background-color:gray;'>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>차트</td>\r\n");
			sb1.append("</tr>\r\n");
			int cnt = 1;
			for (StockVO s : stockList) {
				if (s != null) {
					String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
					logger.debug("specialLetter+++>" + specialLetter);
					sb1.append("<tr>\r\n");
					String url = TOTAL_INFO_URL + s.getStockCode();
					sb1.append(
							"<td style='border-bottom:1px solid gray;background-color:white;'>" + cnt++ + "</td>\r\n");
					sb1.append("<td style='border-bottom:1px solid gray;background-color:white;'><a href='" + url
							+ "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");
					sb1.append("<td style='border-bottom:1px solid gray;background-color:white;'>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/area/day/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/area/day/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
					sb1.append(
							"	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/day/" + s.getStockCode()
									+ ".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/day/"
									+ s.getStockCode() + ".png' width='350px'></a><br/>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/week/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/week/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
//					sb1.append("	<a href='https://ssl.pstatic.net/imgfinance/chart/item/candle/month/"+s.getStockCode()+".png'><img src='https://ssl.pstatic.net/imgfinance/chart/item/candle/month/"+s.getStockCode()+".png' width='350px'></a><br/>\r\n");
					sb1.append("</td>\r\n");

					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
		}
		logger.debug("createNewsStockTable:[" + sb1.toString() + "]");
		return sb1;
	}

	public static Document getUrlDocument(String url) {
		Document doc = null;
		try {
			// This will get you the response.
			Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false)
					.userAgent(USER_AGENT).execute();
			// This will get you cookies
			Map<String, String> loginCookies = res.cookies();
			// And this is the easiest way I've found to remain in session
			doc = Jsoup.connect(url).cookies(loginCookies).userAgent(USER_AGENT).get();
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return doc;
	}

	public static String getMyCommentBox() {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<div style='border:1px solid #afaefe;width:548px;'>\r\n");
		sb1.append("<span style='font:12px bold;border:1px solid #afaefe'>My Comment</span>\r\n");
		sb1.append("<h3>\r\n");
		sb1.append("<span style='background-color: rgb(51, 51, 51); color: rgb(255, 255, 0);'>~~~</span>\r\n");
		sb1.append("</h3>\r\n");
		sb1.append("</div>\r\n");
		return sb1.toString();
	}

	public static String getMyCommentBox(String strComment) {
		StringBuilder sb1 = new StringBuilder();
		sb1.append("<div style='border:1px solid #afaefe;width:548px;'>\r\n");
		sb1.append("<span style='font:12px bold;border:1px solid #afaefe'>My Comment</span>\r\n");
		sb1.append("<h3>\r\n");
		sb1.append("<span style='background-color: rgb(51, 51, 51); color: rgb(255, 255, 0);'>").append(strComment)
				.append("</span>\r\n");
		sb1.append("</h3>\r\n");
		sb1.append("</div>\r\n");
		return sb1.toString();
	}

	public void getStockIsAlive() {
//		String url1 = "https://finance.naver.com/item/main.nhn?code=005930";
		// 두산건설
//		String url = "https://finance.naver.com/search/search.nhn?query=011160";
		// 삼성전자
		String url = "https://finance.naver.com/search/search.nhn?query=005930";
		try {
			Document doc = Jsoup.connect(url).get();
			String strCount = doc.select(".result_area p em").text();
			logger.debug("strCount:" + strCount);
			if (strCount.equals("0")) {
				logger.debug("dead");
			} else {
				logger.debug("alive");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getStockListedDay(String stockCode) {
		String listedDay = "";
		Document doc;
		try {
			// 종합분석-기업개요
			String companyInfoUrl = "http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=" + stockCode;
			logger.debug("companyInfoUrl:" + companyInfoUrl);

			doc = Jsoup.connect(companyInfoUrl).get();
//			StringBuffer sb = sendPost(companyInfoUrl, "UTF-8");
//			doc = Jsoup.parse(sb.toString());
			String strDoc = doc.html();
			if (strDoc.equals("")) {
				return "";
			}
			strDoc = strDoc.replace("&nbsp;", " ");

			doc = Jsoup.parse(strDoc);

			Element cTB201 = doc.getElementById("cTB201");
			if (cTB201 == null) {
				return "";
			}

			Elements trEls = cTB201.select("tbody tr");
			for (Element tr : trEls) {
				Elements thEls = tr.select("th");
				Elements tdEls = tr.select("td");
				int thCnt = 0;
				for (Element th : thEls) {

					String key = th.text();
					String value = tdEls.get(thCnt).text();

					logger.debug("key:" + key + " value:" + value);
					if (key.equals("설립일")) {
						// 설립일
						String foundDay = value.substring(0, value.indexOf(" "));
						// 상장일
						listedDay = value.substring(value.indexOf(" ")).trim().replaceAll("\\(", "")
								.replaceAll("\\)", "").split(" ")[1];
						logger.debug(foundDay + "===" + listedDay);
					}
					thCnt++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listedDay.replaceAll("/", ".");
	}

	private static boolean findDateYn = false;

	public static String getChosenDay(String chosenDay, String listedDay) {
		// 특정일 정보가 없으면???
		if (chosenDay.equals("")) {
			return "";
		}

		// 상장일 정보가 없으면???
		if (listedDay.equals("")) {
			return chosenDay;
		}

		int iChosenDay = Integer.parseInt(chosenDay.replaceAll("\\.", "0"));
		int iListedDay = Integer.parseInt(listedDay.replaceAll("\\.", "0"));
		if (iListedDay < iChosenDay) {
			// 상장일이 찾으려는 날짜보다 과거이면...
			// 찾으려는 날짜가 상장일 이후이면...
			return chosenDay;
		} else {
			return listedDay;
		}
	}

	// @Test
	public void getChosenDayEndPriceTest() throws Exception {
		String chosenDayEndPrice = getChosenDayEndPrice("204210", "모두투어리츠", "2020.01.21");
	}

	public static String getChosenDayEndPrice(String stockCode, String stockName, String chosenDay) {
		logger.debug("chosenDay:" + chosenDay);
		logger.debug("findDateYn:" + findDateYn);

		String chosenDayEndPrice = "";
		// 상장일이 찾으려는 날짜보다 과거이면...찾으려는 날짜
		// 상장일이 찾으려는 날짜보다 이후이면...상장일
		// 찾으려는 날짜가 상장일 이후이면...찾으려는 날짜
		// 찾으려는 날짜가 상장일 이전이면...상장일
		int pageNo = 1;
		findDateYn = false;
		while (!findDateYn) {
			if (pageNo > 100) {
				break;
			}
			chosenDayEndPrice = findChosenDayEndPrice(stockCode, stockName, chosenDay, pageNo);
			pageNo++;
		}
		logger.debug(stockCode + " " + stockName + " " + chosenDay + " 종가 :" + chosenDayEndPrice);
		return chosenDayEndPrice;
	}

//	@Test
	public void getChosenDayPageNoTest() {
		int pageNo = getChosenDayPageNo("005930", "삼성전자", "2020.01.02");
		logger.debug("pageNo:" + pageNo);
		pageNo = getChosenDayPageNo("005930", "삼성전자", "2019.01.02");
		logger.debug("pageNo:" + pageNo);
		pageNo = getChosenDayPageNo("005930", "삼성전자", "2018.01.02");
		logger.debug("pageNo:" + pageNo);
	}

	public static int getChosenDayPageNo(String stockCode, String stockName, String chosenDay) {
		logger.debug("chosenDay:" + chosenDay);

		// 상장일이 찾으려는 날짜보다 과거이면...찾으려는 날짜
		// 상장일이 찾으려는 날짜보다 이후이면...상장일
		// 찾으려는 날짜가 상장일 이후이면...찾으려는 날짜
		// 찾으려는 날짜가 상장일 이전이면...상장일
		int pageNo = 1;
		findDateYn = false;
		while (!findDateYn) {
			if (pageNo > 100) {
				break;
			}
			try {
				pageNo = findChosenDayPageNo(stockCode, stockName, chosenDay, pageNo);
				if (!findDateYn) {
					pageNo++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.debug(stockCode + " " + stockName + " " + chosenDay + " pageNo :" + pageNo);
		return pageNo;
	}

	// @Test
	public void findChosenDayEndPriceTest() throws Exception {
		String chosenDayEndPrice = StockUtil.findChosenDayEndPrice("338100", "NH프라임리츠", "NH프라임리츠", 9);
		logger.debug("chosenDayEndPrice:" + chosenDayEndPrice);
	}

	@Test
	public void a() throws Exception {
		String chosenDayEndPrice = findChosenDayEndPrice("353200", "대덕전자", "2020.05.21", 1);
		logger.debug("chosenDayEndPrice :" + chosenDayEndPrice);

	}

	/**
	 * 특정일 종가 구하기
	 *
	 * @param stockCode
	 * @param stockName
	 * @param chosenDay
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	public static String findChosenDayEndPrice(String stockCode, String stockName, String chosenDay, int pageNo) {
		String chosenDayEndPrice = "0";
		Document doc;
		// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
		String nhnSiseDayUrl = "https://finance.naver.com/item/sise_day.nhn?code=" + stockCode + "&page=" + pageNo;
		logger.debug("nhnSiseDayUrl : " + nhnSiseDayUrl);

//		// This will get you the response.
//		Connection.Response res = Jsoup.connect(nhnSiseDayUrl).method(Connection.Method.POST).followRedirects(false)
//				.userAgent(USER_AGENT).execute();
//		// This will get you cookies
//		Map<String, String> loginCookies = res.cookies();
//		// And this is the easiest way I've found to remain in session
//		doc = Jsoup.connect(nhnSiseDayUrl).cookies(loginCookies).userAgent(userAgent).get();
		StringBuffer sb;
		try {
			doc = Jsoup.connect(nhnSiseDayUrl).get();
//			sb = sendPost(nhnSiseDayUrl, "EUC-KR");
//			logger.debug("sb:" + sb);
//			doc = Jsoup.parse(sb.toString());

			doc.select("link").remove();
			doc.select("javascript").remove();

			String strDoc = doc.html();
			strDoc = strDoc.replace("&nbsp;", " ");

			doc = Jsoup.parse(strDoc);

			Elements type2Es = doc.select(".type2");

			if (type2Es.size() > 0) {
				Element type2 = doc.select(".type2").get(0);
				Elements thEls = type2.select("tbody tr th");

				int dayIndex = 0;
				int endPriceIndex = 0;
				for (int i = 0; i < thEls.size(); i++) {
					Element thEl = thEls.get(i);
					String key = thEl.text();
					if (key.equals("날짜")) {
						dayIndex = i;
					} else if (key.equals("종가")) {
						endPriceIndex = i;
					}
				}
				logger.debug("endPriceIndex:" + endPriceIndex);

				Elements trEls = type2.select("tbody tr");
				String temp_tradeDay = "";
				String temp_chosenDayEndPrice = "";
				for (Element tr : trEls) {
					Elements tdEls = tr.select("td");
					if (tdEls.size() > 1) {
						Element dayEl = tdEls.get(dayIndex);
						temp_tradeDay = dayEl.text();
						logger.debug("temp_tradeDay:" + temp_tradeDay);

						Element chosenDayEndPriceEl = tdEls.get(endPriceIndex);
						temp_chosenDayEndPrice = chosenDayEndPriceEl.text();
						logger.debug("temp_chosenDayEndPrice:" + temp_chosenDayEndPrice);
						if (chosenDay.equals(temp_tradeDay)) {
							logger.debug(temp_tradeDay + "\t" + temp_chosenDayEndPrice);
							chosenDayEndPrice = temp_chosenDayEndPrice;
							findDateYn = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("chosenDayEndPrice:" + chosenDayEndPrice);
		return chosenDayEndPrice;
	}

	public static int findChosenDayPageNo(String stockCode, String stockName, String chosenDay, int pageNo)
			throws IOException {
		Document doc;
		// 종합분석-기업개요
//			doc = Jsoup.connect("https://finance.naver.com/item/sise_day.nhn?code=" + stockCode).get();
//			String url = "https://finance.naver.com/item/frgn.nhn?code=" + stockCode;
		String nhnSiseDayUrl = "https://finance.naver.com/item/sise_day.nhn?code=" + stockCode + "&page=" + pageNo;
		logger.debug("nhnSiseDayUrl : " + nhnSiseDayUrl);
		// This will get you the response.
		Connection.Response res = Jsoup.connect(nhnSiseDayUrl).method(Connection.Method.POST).followRedirects(false)
				.userAgent(USER_AGENT).execute();
		// This will get you cookies
		Map<String, String> loginCookies = res.cookies();
		// And this is the easiest way I've found to remain in session
		doc = Jsoup.connect(nhnSiseDayUrl).cookies(loginCookies).userAgent(USER_AGENT).get();

		doc.select("link").remove();
		doc.select("javascript").remove();

		String strDoc = doc.html();
		strDoc = strDoc.replace("&nbsp;", " ");

		doc = Jsoup.parse(strDoc);

		Element type2 = doc.select(".type2").get(0);

		Elements thEls = type2.select("tbody tr th");
		int dayIndex = 0;
		for (int i = 0; i < thEls.size(); i++) {
			Element thEl = thEls.get(i);
			String key = thEl.text();
			if (key.equals("날짜")) {
				dayIndex = i;
			}
		}
		Elements trEls = type2.select("tbody tr");
		String temp_tradeDay = "";
		for (Element tr : trEls) {
			Elements tdEls = tr.select("td");
			if (tdEls.size() > 1) {
				Element dayEl = tdEls.get(dayIndex);
				temp_tradeDay = dayEl.text();
				if (chosenDay.equals(temp_tradeDay)) {
					findDateYn = true;
					break;
				}
			}
		}
		logger.debug("pageNo:" + pageNo);
		return pageNo;
	}

	// @Test
	public void moneyUnitSplitTest() {
		String amount = "94,395,847,741,020".replaceAll(",", "");
		long lAmount = Long.parseLong(amount);
		logger.debug("moneyUnitSplit :" + moneyUnitSplit("원", lAmount));
	}

	public static String moneyUnitSplit(String unit, long lAmount) {
		String strPlusMinus = "";
		if (lAmount < 0) {
			strPlusMinus = "-";
		}
		StringBuffer sb = new StringBuffer();
		String strAmount = String.valueOf(Math.abs(lAmount));
		// 조
		/*
		 * int jo = (int) (Long.parseLong(strAmount) / 1000000000000L); String strJo =
		 * String.valueOf(jo); int iJo = Integer.parseInt(strJo);
		 */

		BigInteger[] jo1 = new BigInteger(strAmount).divideAndRemainder(new BigInteger("1000000000000"));
		String jo2 = jo1[0].toString();
		BigInteger[] jo3 = new BigInteger(jo2).divideAndRemainder(new BigInteger("10000"));

		String strJo = "";
		if (jo3.length > 1) {
			strJo = jo3[1].toString();
		} else {
			strJo = jo3[0].toString();
		}
		int iJo = Integer.parseInt(strJo);

		// 억
		/*
		 * int uk = (int) (Long.parseLong(strAmount) / 100000000); double uk2 = uk /
		 * 10000d; String uk3 = String.valueOf(uk2); String strUk =
		 * uk3.substring(uk3.indexOf(".") + 1); int iUk = Integer.parseInt(strUk);
		 */
		BigInteger[] uk1 = new BigInteger(strAmount).divideAndRemainder(new BigInteger("100000000"));
		String uk2 = uk1[0].toString();
		BigInteger[] uk3 = new BigInteger(uk2).divideAndRemainder(new BigInteger("10000"));

		String strUk = "";
		if (uk3.length > 1) {
			strUk = uk3[1].toString();
		} else {
			strUk = uk3[0].toString();
		}
		int iUk = Integer.parseInt(strUk);
		// 만
		/*
		 * long man = Long.parseLong(strAmount) / 10000; double man2 = man / 10000d;
		 * String man3 = String.valueOf(man2); String strMan =
		 * man3.substring(man3.indexOf(".") + 1); int iMan = Integer.parseInt(strMan);
		 */

		BigInteger[] man1 = new BigInteger(strAmount).divideAndRemainder(new BigInteger("10000"));
		String man2 = man1[0].toString();
		BigInteger[] man3 = new BigInteger(man2).divideAndRemainder(new BigInteger("10000"));

		String strMan = "";
		if (man3.length > 1) {
			strMan = man3[1].toString();
		} else {
			strMan = man3[0].toString();
		}
		int iMan = Integer.parseInt(strMan);

		BigInteger[] won = new BigInteger(strAmount).divideAndRemainder(new BigInteger("10000"));
		String strWon = "";
		if (won.length > 1) {
			strWon = won[1].toString();
		} else {
			strWon = won[0].toString();
		}
		int iWon = Integer.parseInt(strWon);

		sb.append(strPlusMinus);
		if (!strJo.equals("0")) {
			sb.append(iJo + "조 ");
		}
		if (unit.equals("억") || unit.equals("만") || unit.equals("원")) {
			if (!strUk.equals("0")) {
				sb.append(iUk + "억 ");
			}
		}
		if (unit.equals("만") || unit.equals("원")) {
			if (!strMan.equals("0")) {
				sb.append(iMan + "만 ");
			}
		}
		if (unit.equals("원")) {
			if (!strWon.equals("0")) {
				sb.append(iWon + "원");
			}
		}

		return sb.toString();
	}

	public static String getStockNameFromCode(String stockCode) {
		String stockName = "";
		Document doc;
		try {
			// 종합분석-기업개요
			doc = Jsoup.connect("https://finance.naver.com/item/main.nhn?code=" + stockCode).get();
			logger.debug("title:" + doc.title());

			Element e = doc.select(".h_company h2 a, .h_company h2 a").first();
			if (e != null) {
				stockName = e.text();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stockName;
	}

	public static String getStockCodeFromName(String strStockName) {
		String enc = "EUC-KR";
		// https://finance.naver.com/search/searchList.nhn?query=%BB%EF%BC%BA%C0%FC%C0%DA
		String encodedStockName = null;
		String strStockCode = "";
		Document doc;
		try {
			encodedStockName = URLEncoder.encode(strStockName, enc);
			logger.debug(strStockName + " url encode by" + enc + " : " + encodedStockName);
			// 종합분석-기업개요
//			doc = Jsoup.connect("https://search.naver.com/search.naver?sm=sta_hty.finance&where=nexearch&ie=UTF8&query=" + stockName).get();
//			doc = Jsoup.connect("https://finance.naver.com/search/search.nhn?query=" + stockName).get();
			doc = Jsoup.connect("https://finance.naver.com/search/searchList.nhn?query=" + encodedStockName).get();
			logger.debug("doc:" + doc);

			Elements as = doc.select(".tbl_search .tit a");
			if (as.size() > 0) {
				logger.debug("as:\n" + as);
				String href = as.get(0).attr("href");
				logger.debug("href:" + href);
				strStockCode = href.substring(href.indexOf("=") + 1);
				logger.debug("strStockCode:" + strStockCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strStockCode;
	}

//	public static void main(String args[]) throws Exception {
//		String kospiFileName = GlobalVariables.kospiFileName;
//		String kosdaqFileName = GlobalVariables.kosdaqFileName;
//		List<StockVO> stockList = new ArrayList<>();
//		readStockCodeNameListFromExcel(stockList, kospiFileName);
//	}
//	@Test
	public void getTodayMarkertPriceTest() {
		StockVO stock = new StockVO();
		getTodayMarkertPrice(stock, "105560");
	}

	public static StockVO getTodayMarkertPrice(StockVO stock, String code) {
		Document doc;
		stock.setStockCode(code);

		try {
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
			// <dl class="blind">
			// <dt>종목 시세 정보</dt>
			// <dd>2020년 04월 27일 16시 10분 기준 장마감</dd>
			// <dd>종목명 KB금융</dd>
			// <dd>종목코드 105560 코스피</dd>
			// <dd>현재가 34,200 전일대비 상승 3,100 플러스 9.97 퍼센트</dd>
			// <dd>전일가 31,100</dd>
			// <dd>시가 31,500</dd>
			// <dd>고가 34,200</dd>
			// <dd>상한가 40,400</dd>
			// <dd>저가 31,350</dd>
			// <dd>하한가 21,800</dd>
			// <dd>거래량 6,240,181</dd>
			// <dd>거래대금 207,022백만</dd>
			// </dl>
			String specialLetter = "";
			String sign = "";
			String curPrice = "";
			String varyPrice = "";
			String varyRatio = "";

			for (int i = 0; i < edds.size(); i++) {
				Element dd = edds.get(i);
				String text = dd.text();
				logger.debug("text:" + text);
				if (text.startsWith("종목명")) {
					String stockName = text.substring(4);
					logger.debug("stockName:" + stockName);
					stock.setStockName(stockName);
				}

//				if (text.startsWith("종목코드")) {
//					String stockCode = text.substring(5);
//					logger.debug("stockCode:" + stockCode);
//					stock.setStockCode(stockCode);
//				}
				if (text.startsWith("현재가")) {
					logger.debug("data1:" + dd.text());
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

					// 특수문자
					specialLetter = txts[3].replaceAll("보합", "");
					stock.setSpecialLetter(specialLetter);

					varyPrice = txts[4];
					stock.setVaryPrice(varyPrice);
					stock.setiVaryPrice(Integer
							.parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));

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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stock;

	}

	public static StockVO getTodayMarkertPrice(Document doc, StockVO stock, String code) {
		stock.setStockCode(code);

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
		// <dl class="blind">
		// <dt>종목 시세 정보</dt>
		// <dd>2020년 04월 27일 16시 10분 기준 장마감</dd>
		// <dd>종목명 KB금융</dd>
		// <dd>종목코드 105560 코스피</dd>
		// <dd>현재가 34,200 전일대비 상승 3,100 플러스 9.97 퍼센트</dd>
		// <dd>전일가 31,100</dd>
		// <dd>시가 31,500</dd>
		// <dd>고가 34,200</dd>
		// <dd>상한가 40,400</dd>
		// <dd>저가 31,350</dd>
		// <dd>하한가 21,800</dd>
		// <dd>거래량 6,240,181</dd>
		// <dd>거래대금 207,022백만</dd>
		// </dl>
		String specialLetter = "";
		String sign = "";
		String curPrice = "";
		String varyPrice = "";
		String varyRatio = "";

		for (int i = 0; i < edds.size(); i++) {
			Element dd = edds.get(i);
			String text = dd.text();
			logger.debug("text:" + text);
			if (text.startsWith("종목명")) {
				String stockName = text.substring(4);
				logger.debug("stockName:" + stockName);
				stock.setStockName(stockName);
			}

//				if (text.startsWith("종목코드")) {
//					String stockCode = text.substring(5);
//					logger.debug("stockCode:" + stockCode);
//					stock.setStockCode(stockCode);
//				}
			if (text.startsWith("현재가")) {
				logger.debug("data1:" + dd.text());
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

				// 특수문자
				specialLetter = txts[3].replaceAll("보합", "");
				stock.setSpecialLetter(specialLetter);

				varyPrice = txts[4];
				stock.setVaryPrice(varyPrice);
				stock.setiVaryPrice(
						Integer.parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));

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
		return stock;

	}

//	@Test
	public void TOTAL_INFO_URLCheck() {
		String strStockCode = "159650";
		String strStockName = "하이골드8호";
		TOTAL_INFO_URLCheck(strStockCode, strStockName);
	}

	public static Document TOTAL_INFO_URLCheck(String strStockCode, String strStockName) {
		logger.debug("stockName:" + strStockName + "(" + strStockCode + ")");
		Document doc = null;
		try {
			// 종합정보
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();
			logger.debug("doc:" + doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	/**
	 * Get 방식
	 *
	 * @throws Exception
	 */
	private void sendGet() throws Exception {
		String url = "http://bobr2.tistory.com/";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// 전송방식
		con.setRequestMethod("GET");
		// Request Header 정의
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setConnectTimeout(10000); // 컨텍션타임아웃 10초
		con.setReadTimeout(5000); // 컨텐츠조회 타임아웃 5총

		int responseCode = con.getResponseCode();
		logger.debug("\nSending 'GET' request to URL : " + url);
		logger.debug("Response Code : " + responseCode);

		Charset charset = Charset.forName("UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		logger.debug("조회결과 : " + response.toString());
	}

	/**
	 * Post 방식 Https
	 *
	 * @throws Exception
	 */
	private void sendPostHttps() throws Exception {

		String url = "https://selfsolve.apple.com/wcResults.do";
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setConnectTimeout(10000); // 컨텍션타임아웃 10초
		con.setReadTimeout(5000); // 컨텐츠조회 타임아웃 5총
		// Send post request
		con.setDoOutput(true); // 항상 갱신된내용을 가져옴.
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		logger.debug("\nSending 'POST' request to URL : " + url);
		logger.debug("Post parameters : " + urlParameters);
		logger.debug("Response Code : " + responseCode);

		Charset charset = Charset.forName("UTF-8");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		logger.debug(response.toString());
	}

	/**
	 * Post 방식 Https
	 *
	 * @throws Exception
	 */
	public static StringBuffer sendPost(String url, String charsetName) throws Exception {
		logger.debug("sendPost url : " + url);
		logger.debug("sendPost charsetName : " + charsetName);

//		String url = "http://bobr2.tistory.com/";
//		String urlParameters = "?Param1=aaaa" + "&Param2=bbbb";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setConnectTimeout(5000); // 커넥션타임아웃 10초
		con.setReadTimeout(5000); // 컨텐츠조회 타임아웃 5초
		// Send post request
		con.setDoOutput(true); // 항상 갱신된내용을 가져옴.
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		logger.debug("sendPost wr1 : " + wr);
//		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
		logger.debug("sendPost wr2 : " + wr);

		int responseCode = con.getResponseCode();
		logger.debug("\nSending 'POST' request to URL : " + url);
//		logger.debug("Post parameters : " + urlParameters);
		logger.debug("Response Code : " + responseCode);

		// Naver : <meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
		Charset charset = Charset.forName(charsetName);
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));
		String inputLine;
		StringBuffer sb = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			logger.debug("inputLine:" + inputLine);
			sb.append(inputLine + "\r\n");
		}
		in.close();
		// logger.debug(sb.toString());
		return sb;
	}

//	@Test
//	public void getDateInfoTest() {
//		getDateInfo("005930");
//	}

	public static String getDateInfo(String strStockCode) {
		// 종합정보
		// http://finance.naver.com/item/main.nhn?code=005930
		Document doc;
		String strYmdDash;
		int iYmd;
		String strYmdDashBracket = null;
		try {
			// 종합정보
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();
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

					logger.debug("iYmd:[" + iYmd + "]");
					logger.debug("strYmdDash:[" + strYmdDash + "]");
				}
			}
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return strYmdDashBracket;
	}

	public static StringBuilder getNews(List<StockVO> allStockList) {

		StringBuilder sb1 = new StringBuilder();

		String strStockCode;
		String strStockName;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
		String strDefaultDate = sdf.format(new Date());
		int iYmd = Integer.parseInt(strDefaultDate);

		for (StockVO vo : allStockList) {
			strStockCode = vo.getStockCode();
			strStockName = vo.getStockName();
			logger.debug(strStockName + ":" + strStockCode);

			// 종합정보
			Document doc;
			try {
				// 5페이지 정도 돌면서 오늘 기사를 가져온다.
				for (int page = 1; page <= 5; page++) {
					String strUrl = "http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page="
							+ page;
					doc = Jsoup.connect(strUrl).get();
					Elements types = doc.select(".type5");
					if (types.size() <= 0) {
						return sb1;
					}
					Element type = types.get(0);

					Elements trs = type.getElementsByTag("tr");
					if (trs != null) {
						logger.debug("trs.size:" + trs.size());
					}

					boolean isToday = true;
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

						String strTitle = a1.html();
						String strHref = a1.attr("href");
						String strSource = source.html();
						String strDayTime = dayTime.html();
						String strYmd2 = strDayTime.substring(0, 10);
						int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

						logger.debug("a:" + a1);
						logger.debug("source:" + source);
						logger.debug("dayTime:" + dayTime);
						logger.debug("title:" + strTitle);
						logger.debug("href:" + strHref);
						logger.debug("source:" + strSource);
						logger.debug("dayTime:" + strDayTime);
						logger.debug("dayTime:" + dayTime.text());
						logger.debug("strYmd2:" + strYmd2);
						logger.debug("iYmd:" + iYmd);
						logger.debug("iYmd2:" + iYmd2);
						logger.debug("iYmd == iYmd2 :" + (iYmd == iYmd2));

						// 기사를 쓴 날이 오늘이면 가져온다.
						if (iYmd == iYmd2) {
							String newsReadUrl = "http://finance.naver.com" + strHref;
							logger.debug("newsReadUrl:" + newsReadUrl);

							sb1.append("<br><br>\r\n");
							sb1.append("기사주소:[<a href='" + newsReadUrl + "'>" + newsReadUrl + "</a>]\r\n");
							sb1.append("<br><br>\r\n");
							sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
									+ strStockName + "(" + strStockCode + ")</a></h3>");

							doc = Jsoup.connect(newsReadUrl).get();
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
							doc.select("script").remove();

							Element view = doc.select(".view").get(0);

							String strView = view.toString();
							strView = strView.replaceAll(strStockName,
									"<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
											+ strStockName + "</a>");

							sb1.append(strView);
							sb1.append("\n");

							logger.debug("view:" + view);
						} else {
							isToday = false;
						}
					}
					if (!isToday) {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1;
	}

	public static StringBuilder getNews(StockVO stock) {

		StringBuilder sb1 = new StringBuilder();

		String strStockCode = stock.getStockCode();
		String strStockName = stock.getStockName();
		logger.debug("strStockCode:" + strStockCode + " strStockName:" + strStockName);

		// 종합정보
		Document doc;
		List<StockNewsVO> stockNewsList1 = new ArrayList<StockNewsVO>();
		try {
			// 028260 삼성물산
			// http://finance.naver.com/item/news.nhn?code=091580
			// http://finance.naver.com/item/news_news.nhn?code=028260&page=1
			doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=1").get();
			// http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
			// logger.debug(doc.html());
			Element type5 = doc.select(".type5").get(0);
			logger.debug("type5:" + type5);

			Elements trs = type5.getElementsByTag("tr");

			for (int i = 0; i < trs.size(); i++) {
				Element tr = trs.get(i);
				logger.debug("tr:[" + tr + "]");

				Elements tds = tr.getElementsByTag("td");
				logger.debug("tds.size:[" + tds.size() + "]");
				if (tds.size() < 3) {
					continue;
				}

				Element dayTime = tr.select(".date").first();
				Element a1 = tr.getElementsByTag("a").first();
				Element source = tr.select(".info").first();

				if (a1 == null) {
					continue;
				}

				logger.debug("dayTime:" + dayTime);
				logger.debug("a1:" + a1);
				logger.debug("source" + source);
				// logger.debug("a:" + a1);
				// logger.debug("source:" + source);
				// logger.debug("title:" + a1.html());
				// logger.debug("href:" + a1.attr("href"));
				// logger.debug("source:" + source.html());

				String strTitle = a1.html();
				String strLink = a1.attr("href");
				String strSource = source.html();
				String strDayTime = dayTime.html();
				String yyyyMMdd = strDayTime.substring(0, 10);
				logger.debug("yyyyMMdd:" + yyyyMMdd);
				String strDayTimeText = dayTime.text();

				String strYmdTemp = yyyyMMdd.replaceAll("\\.", "");
				logger.debug("strYmdTemp:" + strYmdTemp);
				int iYmdTemp = Integer.parseInt(strYmdTemp);
				logger.debug("yyyyMMdd:" + yyyyMMdd);
				logger.debug("iYmdTemp:" + iYmdTemp);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.KOREAN);
				String strDefaultDate = sdf.format(new Date());
				int iYmd = Integer.parseInt(strDefaultDate);

				if (iYmdTemp < iYmd) {
					continue;
				}

				String year = yyyyMMdd.substring(0, 4);
				String month = yyyyMMdd.substring(5, 7);
				String day = yyyyMMdd.substring(8, 10);
				int iYear = Integer.parseInt(year);
				int iMonth = Integer.parseInt(month) - 1;
				int iDay = Integer.parseInt(day);
				// logger.debug(year + month + day);

				Calendar cal1 = Calendar.getInstance();// 오늘 ymd
				Calendar cal2 = Calendar.getInstance();// 기사입력일 ymd
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

					strView = strView.replaceAll("<script type=\"text/javascript\" src=\"/js/news_read.js\"></script>",
							"");
					strView = strView.replaceAll("NEWS\" data-cid=\"ne_[0-9]*_[0-9]*\" style=\"visibility: hidden;\">",
							"");

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
					// 소스 수정필요
					StockNewsVO newsVO = new StockNewsVO();
					newsVO.setTitle(title);
					newsVO.setCompany(company);
					newsVO.setDate(date);

					// 뉴스 중복체크
					boolean isSameNews = false;
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
							isSameNews = true;
						}
					}
					logger.debug("newsVO:" + newsVO);
					if (!isSameNews) {
						logger.debug("strStockCode:" + strStockCode);
						logger.debug("strStockName:" + strStockName);
						stockNewsList1.add(newsVO);
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
								+ strStockName + "(" + strStockCode + ")</a></h3>");
						sb1.append(strView);
						sb1.append("<br><br>\n");
					}
				} else {
					break;
				}
			}
			logger.debug("=========================================");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb1;
	}

}
