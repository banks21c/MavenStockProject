/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import html.parsing.stock.DataSort.StockNameLengthDescCompare;

/**
 *
 * @author parsing-25
 */
public class StockUtil {

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

	int topCount = 0;
	int upCount = 0;
	int bottomCount = 0;
	int downCount = 0;
	int steadyCount = 0;

	public int getTopCount() {
		return topCount;
	}

	public void setTopCount(int topCount) {
		this.topCount = topCount;
	}

	public int getUpCount() {
		return upCount;
	}

	public void setUpCount(int upCount) {
		this.upCount = upCount;
	}

	public int getBottomCount() {
		return bottomCount;
	}

	public void setBottomCount(int bottomCount) {
		this.bottomCount = bottomCount;
	}

	public int getDownCount() {
		return downCount;
	}

	public void setDownCount(int downCount) {
		this.downCount = downCount;
	}

	public int getSteadyCount() {
		return steadyCount;
	}

	public void setSteadyCount(int steadyCount) {
		this.steadyCount = steadyCount;
	}

	static List<StockVO> allStockList = new ArrayList<StockVO>();
	static List<StockVO> topStockList = new ArrayList<StockVO>();
	static List<StockVO> bottomStockList = new ArrayList<StockVO>();
	static List<StockVO> topTouchStockList = new ArrayList<StockVO>();
	static List<StockVO> bottomTouchStockList = new ArrayList<StockVO>();
	static List<StockVO> upDownStockList = new ArrayList<StockVO>();
	static List<StockVO> downUpStockList = new ArrayList<StockVO>();
	static List<StockVO> over5PerUpStockList = new ArrayList<StockVO>();
	static List<StockVO> over5PerDownStockList = new ArrayList<StockVO>();
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
	 * @param marketType
	 * @return
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromKrx(String marketType) throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		getStockCodeNameListFromKindKrxCoKr(stockList, marketType);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}
	/**
	 *
	 * @param marketType
	 * @param searchType
			13:상장법인
			01:관리종목
			05:불성실공시법인
			07:자산2조법인
			99:외국법인
			181:(코스닥) 우량기업부
			182:(코스닥) 벤처기업부
			183:(코스닥) 중견기업부
			184:(코스닥) 기술성장기업부
			11:KRX100
			06:KOSPI200
			09:STAR30
			10:PREMIER
	 * @return
	 * @throws Exception
	 */
	public static List<StockVO> readStockCodeNameListFromKrx(String marketType, String searchType) throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		getStockCodeNameListFromKindKrxCoKr(stockList, marketType, searchType);
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readKospiStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		getStockCodeNameListFromKindKrxCoKr(stockList, "stockMkt");
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readKosdaqStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		getStockCodeNameListFromKindKrxCoKr(stockList, "kosdaqMkt");
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readAllStockCodeNameListFromKrx() throws Exception {
		List<StockVO> stockList = new ArrayList<StockVO>();
		getStockCodeNameListFromKindKrxCoKr(stockList, "stockMkt");
		getStockCodeNameListFromKindKrxCoKr(stockList, "kosdaqMkt");
		Collections.sort(stockList, new StockNameLengthDescCompare());
		return stockList;
	}

	public static List<StockVO> readAllStockCodeNameListFromExcel() throws Exception {
		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;

		List<StockVO> stockList = new ArrayList<StockVO>();
		try {
			readStockCodeNameListFromExcel(stockList, kospiFileName);
			readStockCodeNameListFromExcel(stockList, kosdaqFileName);
		} catch (Exception e) {
			e.printStackTrace();
			getStockCodeNameListFromKindKrxCoKr(stockList, "stockMkt");
			getStockCodeNameListFromKindKrxCoKr(stockList, "kosdaqMkt");
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
		logger.debug("stockList.size:"+stockList.size());
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

		getStockCodeNameListFromKindKrxCoKr(stockList, "stockMkt");
		getStockCodeNameListFromKindKrxCoKr(stockList, "kosdaqMkt");
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

	public static String makeStockLinkStringByExcel(String textBodyHtml) {

		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		List<StockVO> stockList = new ArrayList<>();

		try {
			readStockCodeNameListFromExcel(stockList, kospiFileName);
			readStockCodeNameListFromExcel(stockList, kosdaqFileName);
		} catch (Exception e) {
			logger.debug("Exception e:" + e.getMessage());
			e.printStackTrace();
			getStockCodeNameListFromKindKrxCoKr(stockList, "stockMkt");
			getStockCodeNameListFromKindKrxCoKr(stockList, "kosdaqMkt");
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

	public static String stockTitleLinkString(String textBodyHtml, List<StockVO> stockList) {
		logger.debug("stockLinkString.....................");
		String strNews = textBodyHtml;
		strNews = strNews.replaceAll("&amp;", "&");
		// logger.debug("strNews:[" + strNews + "]");
		// logger.debug("stockList.size():[" + stockList.size() + "]");
		Document doc = null;
		List<StockVO> newsStockList = new ArrayList<StockVO>();
		for (int i = 0; i < stockList.size(); i++) {
			StockVO stock = stockList.get(i);
			String stockCode = stock.getStockCode();
			String stockName = stock.getStockName();

			doc = Jsoup.parse(strNews);
			if (strNews.contains("현대차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차", "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			}
			if (strNews.contains("POSCO") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString("POSCO") + "</a></strong>");
			}
			if (strNews.contains(stockName)) {
				int count = StringUtils.countMatches(strNews, stockName);
				logger.debug(stockName + " 갯수:" + count);
				Elements imgs = doc.select("img");
				if (imgs.size() > 0) {
					String src = imgs.attr("src");
					if (src.contains(stockName)) {
						continue;
					}
				}
				if (StockExtractExceptWord.dupCheck(stockName, strNews)) {
					break;
				}
				newsStockList.add(stock);
				// logger.debug("stock link : " + stockCode + ":" + stockName);
				strNews = strNews.replaceAll(stockName, "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString(stockName) + "</a></strong>");

			}

		}
		strNews = strNews.replaceAll("&nbsp;", "");
		logger.debug("stockLinkString end.....................");
		return strNews;
	}

	public static String stockLinkString(String textBodyHtml, List<StockVO> stockList) {
		logger.debug("stockLinkString.....................");
		String strNews = textBodyHtml;
		strNews = strNews.replaceAll("&amp;", "&");
		// logger.debug("strNews:[" + strNews + "]");
		// logger.debug("stockList.size():[" + stockList.size() + "]");
		Document doc = null;
		List<StockVO> newsStockList = new ArrayList<StockVO>();
		for (int i = 0; i < stockList.size(); i++) {
			StockVO stock = stockList.get(i);
			String stockCode = stock.getStockCode();
			String stockName = stock.getStockName();

			doc = Jsoup.parse(strNews);
			if (strNews.contains("현대차") && stockName.equals("현대자동차")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("현대차", "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString("현대차") + "</a></strong>");
			}
			if (strNews.contains("POSCO") && stockName.equals("포스코")) {
				newsStockList.add(stock);
				strNews = strNews.replaceAll("POSCO", "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString("POSCO") + "</a></strong>");
			}
			if (strNews.contains(stockName)) {
				int count = StringUtils.countMatches(strNews, stockName);
				logger.debug(stockName + " 갯수:" + count);
				Elements imgs = doc.select("img");
				if (imgs.size() > 0) {
					String src = imgs.attr("src");
					if (src.contains(stockName)) {
						continue;
					}
				}
				if (StockExtractExceptWord.dupCheck(stockName, strNews)) {
					break;
				}

				newsStockList.add(stock);
				// logger.debug("stock link : " + stockCode + ":" + stockName);
				strNews = strNews.replaceAll(stockName, "<strong><a href='http://finance.naver.com/item/main.nhn?code="
						+ stockCode + "'>" + nbspString(stockName) + "</a></strong>");

			}

		}

		logger.debug("newsStockList:" + newsStockList);
		List<StockVO> newsStockList2 = getNewsStockInfo(newsStockList);
		logger.debug("newsStockList2:" + newsStockList2);
		StringBuilder newsStockTable = createNewsStockTable(newsStockList2);

		strNews = strNews.replaceAll("&nbsp;", "");
		logger.debug("stockLinkString end.....................");
		return strNews + "<br>" + newsStockTable.toString();
	}

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
					}
					if (i == 1) {
						strStockCode = cellValue;
					}
					i++;
				}
				if (strStockCode.length() != 6) {
					continue;
				}
//					logger.debug(strStockCode + "\t" + strStockName);
			}
			svo.setStockCode(strStockCode);
			svo.setStockName(strStockName);
			svo.setStockNameLength(strStockName.length());
			svoList.add(svo);
			cnt++;
		}
		// Closing the workbook
		workbook.close();
		stockList.addAll(svoList);
		return stockList;
	}

	public List<StockVO> getAllStockInfo(List<StockVO> stockList) {
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

	public List<StockVO> getAllStockListInfo(String fileName) {
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
			logger.debug("\n\n getAllStockInfo \n");
			logger.debug("\n\nIterating over Rows and Columns using Iterator2\n");
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
						}
						if (i == 1) {
							strStockCode = cellValue;
						}
						i++;
					}
					if (strStockCode.length() != 6) {
						continue;
					}
					svo.setStockCode(strStockCode);
					svo.setStockName(strStockName);
//					svo = getStockInfo(cnt, strStockCode, strStockName);
					svoList.add(svo);
//					logger.debug(strStockCode + "\t" + strStockName);
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

	public static List<StockVO> getAllStockList(String fileName)
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
		logger.debug("\n\n getAllStockList \n");
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
			logger.debug("\n\n getAllStockList \n");
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


	public static List<StockVO> getStockCodeNameListFromKindKrxCoKr(List<StockVO> stockList, String marketType) {
		//searchType 13 = 상장법인
		return getStockCodeNameListFromKindKrxCoKr(stockList, marketType, "13");
	}

	public static List<StockVO> getStockCodeNameListFromKindKrxCoKr(List<StockVO> stockList, String marketType, String searchType) {
		List<StockVO> svoList = new ArrayList<>();
		try {
			String param = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3"
					+"&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType="
					+"&marketType="	+ marketType + "&searchType="+searchType
					+"&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

			String strUri = SERVER_URI + "?" + param;
//			Document doc = Jsoup.parse(new URL(strUri).openStream(), "EUC-KR", strUri);

//			Connection conn = Jsoup.connect(strUri).cookie("cookiereference", "cookievalue").method(Method.POST);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Accept", MediaType.APPLICATION_JSON
					+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			headers.put("Accept-Encoding", "Accept-Encoding: gzip, deflate");
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
//			headers.put("User-Agent", "mozilla");
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
			System.out.println("mime :" + mime);
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

					if(tdElements.size() > 1) {
						String strStockName = tdElements.get(0).text();
						String strStockCode = tdElements.get(1).text();
						System.out.print((i)+"."+strStockName+"(");
						System.out.println(strStockCode + ")");
						svo.setStockName(strStockName);
						svo.setStockCode(strStockCode);
						svo.setStockNameLength(strStockName.length());
						if (strStockCode.length() != 6) {
							System.out.println(strStockCode + "\t" + strStockName + " 종목은 체크바랍니다.");
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
		logger.debug("svoList.size :"+svoList.size());
		stockList.addAll(svoList);
		logger.debug("stockList.size :"+stockList.size());
		return stockList;
	}

	public StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
			// logger.debug("doc:"+doc);

			// Element tradeVolumeText =
			// doc.select(".sp_txt9").get(0);
			String tradeVolumeText = doc.select(".spot .rate_info .sp_txt9").get(0).parent().child(1).select("span")
					.get(0).text();
			if (tradeVolumeText.equals("0")) {
				return stock;
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

			if (specialLetter.equals("↑")) {
				stock.setStockGubun("상한가↑");
				stock.setLineUp(11);

				topStockList.add(stock);
				return stock;
			}
			if (specialLetter.equals("↓")) {
				stock.setStockGubun("하한가↓");
				stock.setLineUp(21);
				bottomStockList.add(stock);
				return stock;
			}

			String highPrice = stock.getHighPrice();
			String lowPrice = stock.getLowPrice();
			String maxPrice = stock.getMaxPrice();
			logger.debug("highPrice:" + highPrice);
			logger.debug("lowPrice:" + lowPrice);
			logger.debug("maxPrice:" + maxPrice);
			// 고가가 0이 아니고 고가가 상한가인가?
			logger.debug("고가가 0이 아니고 고가가 상한가인가? :" + (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())));
			// 현재가가 상한가가 아닌가?
			logger.debug("현재가가 상한가가 아닌가? :" + !curPrice.equals(stock.getMaxPrice()));
			// 고가가 상한가인가?
			logger.debug("고가가 상한가인가?:" + highPrice.equals(stock.getMaxPrice()));
			// 고가가 0이 아니고 고가가 상한가인가?
			if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
					&& !curPrice.equals(stock.getMaxPrice())) {
				logger.debug("고가가 0이 아니고 고가가 상한가인가?:" + (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
						&& !curPrice.equals(stock.getMaxPrice())));
				stock.setStockGubun("상터치↑↘");
				stock.setLineUp(12);
				topTouchStockList.add(stock);
				return stock;
			}
			if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
					&& !curPrice.equals(stock.getMinPrice())) {
				logger.debug("고가가 0이 아니고 저가 하한가인가?:" + (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
						&& !curPrice.equals(stock.getMinPrice())));
				stock.setStockGubun("하터치↓↗");
				stock.setLineUp(22);
				bottomTouchStockList.add(stock);
				return stock;
			}

			// 현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
			float higher = 0;
			String flag = "";
			int icur = stock.getiCurPrice();
			int ihigh = stock.getiHighPrice();
			int ilow = stock.getiLowPrice();

			long lTradingVolume = stock.getlTradingVolume();
			if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
				higher = Math.abs(icur - ihigh);
				flag = "↗↘";
				logger.debug("higher:" + higher + "\t" + (higher / icur * 100));
				float upDownRatio = higher / icur * 100;
				// upDownRatio = ((int)(upDownRatio * 100))/100f;
				String strUpDownRatio = df.format(upDownRatio);
				if (higher / icur * 100 > 10 && lTradingVolume > 0) {
					stock.setStockGubun(strUpDownRatio + "%" + flag);
					stock.setLineUp(16);
					upDownStockList.add(stock);
					return stock;
				}
			} else {
				higher = Math.abs(icur - ilow);
				flag = "↘↗";
				logger.debug("higher:" + higher + "\t" + (higher / icur * 100));
				float upDownRatio = higher / icur * 100;
				// upDownRatio = ((int)(upDownRatio * 100))/100f;
				String strUpDownRatio = df.format(upDownRatio);
				if (upDownRatio > 10 && lTradingVolume > 0) {
					stock.setStockGubun(strUpDownRatio + "%" + flag);
					stock.setLineUp(16);
					downUpStockList.add(stock);
					return stock;
				}
			}

			float fRatio = 0f;
			if (varyRatio.indexOf("%") != -1) {
				fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
				if (fRatio >= 5) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun("+5%이상↗");
						stock.setLineUp(13);
						over5PerUpStockList.add(stock);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun("-5%이상↘");
						stock.setLineUp(23);
						over5PerDownStockList.add(stock);
					}
					return stock;
				}
			}
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
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "MS949");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            //파일 출력
//            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
//            OutputStreamWriter OutputStreamWriter = new OutputStreamWriter(fileOutputStream, "MS949");
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

	public static void readExcelFile(List<StockVO> stockList, String fileName) {
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
			logger.debug("\n\n readExcelFile \n");
			logger.debug("\n\nIterating over Rows and Columns using Iterator4\n");
			Iterator<Row> rowIterator = sheet.rowIterator();
			int cnt = 0;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// Now let's iterate over the columns of the current row
				Iterator<Cell> cellIterator = row.cellIterator();
				StockVO svo = new StockVO();
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
					stockList.add(svo);
//					logger.debug(strStockCode + "\t" + strStockName);
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
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();

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
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stock;
	}

	public StockVO getStockInfo(StockVO stock) {
		Document doc;

		String code = stock.getStockCode();

		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();

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
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");
			sb1.append("</tr>\r\n");
		}
		int cnt = 1;
		for (StockVO s : stockList) {
			if (s != null) {
				String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
				logger.debug("specialLetter+++>" + specialLetter);
				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
				sb1.append("<td>" + cnt++ + "</td>\r\n");
				sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");

				String varyPrice = s.getVaryPrice();

				logger.debug("varyPrice+++>" + varyPrice);

				if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲") || specialLetter.startsWith("+")) {
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
					sb1.append("<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
				}
				sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingVolume(), "")
						+ "</td>\r\n");
				sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingAmount(), "")
						+ "</td>\r\n");

				sb1.append("</tr>\r\n");
			}
		}
		logger.debug(sb1.toString());
		return sb1;
	}

	public static Document getUrlDocument(String url) {
		Document doc = null;
		try {
			String userAgent = "Mozilla";
			// This will get you the response.
			Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false)
					.userAgent(userAgent).execute();
			// This will get you cookies
			Map<String, String> loginCookies = res.cookies();
			// And this is the easiest way I've found to remain in session
			doc = Jsoup.connect(url).cookies(loginCookies).userAgent(userAgent).get();
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(StockUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return doc;
	}

	public static String getMyCommentBox() {
		StringBuffer sb1 = new StringBuffer();
		sb1.append("<div style='border:1px solid #afaefe;width:548px;'>\r\n");
		sb1.append("<span style='font:12px bold;border:1px solid #afaefe'> 나도 한마디</span>\r\n");
		sb1.append("<blockquote>\r\n");
		sb1.append("<h3>\r\n");
		sb1.append("<span style='background-color: rgb(51, 51, 51); color: rgb(255, 255, 0);'>~~~</span>\r\n");
		sb1.append("</h3>\r\n");
		sb1.append("</blockquote>\r\n");
		sb1.append("</div>\r\n");
		return sb1.toString();
	}

	public static void main(String args[]) throws Exception {
		String kospiFileName = GlobalVariables.kospiFileName;
		String kosdaqFileName = GlobalVariables.kosdaqFileName;
		List<StockVO> stockList = new ArrayList<>();
		readStockCodeNameListFromExcel(stockList, kospiFileName);
	}

}
