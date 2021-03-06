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
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;
import html.parsing.stock.util.StockUtil;

public class StockUnique_ReadTxtFile_Thread extends Thread {

	private final static String TOTAL_INFO_URL = "http://finance.naver.com/item/main.nhn?code=";
	final static String USER_HOME = System.getProperty("user.home");
	private static Logger logger = null;

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	DecimalFormat df = new DecimalFormat("###.##");

	static String strYMD = "";

	String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
	int iHms = Integer.parseInt(strHms);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	String strDefaultDate = sdf.format(new Date());
	// String strYmdDash = new SimpleDateFormat("yyyy년 M월 d일
	// E",Locale.KOREAN).format(new Date());
	int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
	String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
	String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

	String strDate = "";
	String strStockCodeOrName = "롯데케미칼";

	String strStockCode = "011170";
	String strStockName = "롯데케미칼";

	List<StockVO> stockList = new ArrayList<>();
	List<StockVO> uniqueStockList = new ArrayList<>();

	List<StockVO> topStockList = new ArrayList<>();
	List<StockVO> bottomStockList = new ArrayList<>();
	List<StockVO> topTouchStockList = new ArrayList<>();
	List<StockVO> bottomTouchStockList = new ArrayList<>();
	List<StockVO> upDownStockList = new ArrayList<>();
	List<StockVO> downUpStockList = new ArrayList<>();
	List<StockVO> over5PerUpStockList = new ArrayList<>();
	List<StockVO> over5PerDownStockList = new ArrayList<>();
	/**
	 * 몇% 이상의 데이터를 추출할 것인가?
	 */
	int upDownStdNo = 10;

	private String strBlogId;
	private String strNidAut;
	private String strNidSes;
	private String marketType = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		new StockUnique_ReadTxtFile_Thread("kospi").test();
		new StockUnique_ReadTxtFile_Thread("kospi").start();
	}

	StockUnique_ReadTxtFile_Thread() {
		logger = LoggerFactory.getLogger(getClass());
	}

	StockUnique_ReadTxtFile_Thread(String marketType) {
		logger = LoggerFactory.getLogger(getClass());
		this.marketType = marketType;
	}

	StockUnique_ReadTxtFile_Thread(String strBlogId, String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	StockUnique_ReadTxtFile_Thread(String marketType,String strBlogId, String strNidAut, String strNidSes) {
		logger = LoggerFactory.getLogger(getClass());
		this.marketType = marketType;
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;

		System.out.println("marketType:" + marketType);
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	public void test() {
		Class thisClass = this.getClass();
		logger = LoggerFactory.getLogger(thisClass);

		readOne("011160", "두산건설");
//		listSort();
		addToAllStockList();
		Collections.sort(uniqueStockList, new VaryRatioDescCompare());
//		writeFile(uniqueStockList, "코스닥");
		int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		if (iHourMinute >= 1830 || iHourMinute < 800) {
			// 기관,외인,개인매매
			uniqueStockList = NaverStockTradingVolume.getStockTradingVolumeList(uniqueStockList);
			// 시간외단일가
			uniqueStockList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(uniqueStockList);
		}	
		StringBuilder html = createHtmlString(uniqueStockList, marketType + " 특징주");
		writeFile(html, marketType + " 특징주");

		// 네이버 블로그에 공유
		naverBlogLinkShare(html);
	}

	void listSort() {
		Collections.sort(topStockList, new VaryRatioDescCompare());
		Collections.sort(bottomStockList, new VaryRatioDescCompare());
		Collections.sort(topTouchStockList, new VaryRatioDescCompare());
		Collections.sort(bottomTouchStockList, new VaryRatioDescCompare());
		Collections.sort(upDownStockList, new VaryRatioDescCompare());
		Collections.sort(downUpStockList, new VaryRatioDescCompare());
		Collections.sort(over5PerUpStockList, new VaryRatioDescCompare());
		Collections.sort(over5PerDownStockList, new VaryRatioDescCompare());
	}

	void addToAllStockList() {
		logger.debug("topStockList.size():" + topStockList.size());
		logger.debug("bottomStockList.size():" + bottomStockList.size());
		logger.debug("topTouchStockList.size():" + topTouchStockList.size());
		logger.debug("bottomTouchStockList.size():" + bottomTouchStockList.size());
		logger.debug("upDownStockList.size():" + upDownStockList.size());
		logger.debug("downUpStockList.size():" + downUpStockList.size());
		logger.debug("over5PerUpStockList.size():" + over5PerUpStockList.size());
		logger.debug("over5PerDownStockList.size():" + over5PerDownStockList.size());

		uniqueStockList.addAll(topStockList);
		uniqueStockList.addAll(bottomStockList);
		uniqueStockList.addAll(topTouchStockList);
		uniqueStockList.addAll(bottomTouchStockList);
		uniqueStockList.addAll(upDownStockList);
		uniqueStockList.addAll(downUpStockList);
		uniqueStockList.addAll(over5PerUpStockList);
		uniqueStockList.addAll(over5PerDownStockList);
	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		logger.debug("start run...");
		// 날짜정보 조회
		strYmdDashBracket = StockUtil.getDateInfo("005930");
		execute();
//		execute1();
//		execute2();

		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug(marketType + " 실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");

		logger.debug("end run...");
	}

	public void execute() {
		stockList = StockUtil.readStockCodeNameList(marketType);
		logger.debug("stockList.size() :" + stockList.size());

		for (int i = 0; i < stockList.size(); i++) {
			StockVO svo = stockList.get(i);
			String strStockCode = svo.getStockCode();
			String strStockName = svo.getStockName();
			logger.debug(strStockCode + ":" + strStockName);
			getStockInfo(strStockCode, strStockName);
		}
		addToAllStockList();
		logger.debug("uniqueStockList.size:" + uniqueStockList.size());
		Collections.sort(uniqueStockList, new VaryRatioDescCompare());
		int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		if (iHourMinute >= 1830 || iHourMinute < 800) {
			// 기관,외인,개인매매
			uniqueStockList = NaverStockTradingVolume.getStockTradingVolumeList(uniqueStockList);
			// 시간외단일가
			uniqueStockList = JsoupMkStockAfterHours.getAfterHoursStockTradeInfoList(uniqueStockList);
		}		
		StringBuilder html = createHtmlString(uniqueStockList, marketType + " 특징주");
		writeFile(html, marketType + " 특징주");

		// 네이버 블로그에 공유
		naverBlogLinkShare(html);

	}

	// 네이버 블로그에 공유
	public void naverBlogLinkShare(StringBuilder html) {
		String strUrl = "";
		String strTitle = Jsoup.parse(html.toString()).select("h2#title").text();
		if (strTitle.equals("")) {
			JOptionPane.showMessageDialog(null, "제목을 입력해주세요.");
			strTitle = "특징주";
		}
		String strBlogCategoryNo = "153";// 특징주
		StringBuilder contentSb = html;
		logger.debug("strBlogId:" + strBlogId);
		logger.debug("strNidAut:" + strNidAut);
		logger.debug("strNidSes:" + strNidSes);
		if (!StringUtils.defaultIfEmpty(strBlogId, "").equals("")&&!StringUtils.defaultIfEmpty(strNidAut, "").equals("")
				&& !StringUtils.defaultIfEmpty(strNidSes, "").equals("")) {
			NaverUtil.naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strBlogCategoryNo, contentSb, null);
		}
	}

	public void readOne(String stockCode, String stockName) {
		strStockCode = stockCode;
		strStockName = stockName;
		getStockInfo(strStockCode, strStockName);
	}

	public void readFile(String kospidaq, String fileName) {

		File f = new File(USER_HOME + "\\documents\\" + fileName);
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
				getStockInfo(strStockCode, strStockName);
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

	public StockVO getStockInfo(String strStockCode, String strStockName) {
		Document doc;
		StockVO stock = new StockVO();
		stock.setStockCode(strStockCode);
		stock.setStockName(strStockName);
		try {
			// 종합정보
			// http://finance.naver.com/item/main.nhn?code=005930
			doc = Jsoup.connect(TOTAL_INFO_URL + strStockCode).get();
			logger.debug("_______________________________________________");
			logger.debug(doc.select(".spot .rate_info .sp_txt9").html());
			if (doc.select(".spot .rate_info .sp_txt9").html().equals("")) {
				return stock;
			}
			logger.debug("_______________________________________________");
			// logger.debug("doc:"+doc);

			// Element tradeVolumeText =
			// doc.select(".sp_txt9").get(0);
			String tradeVolumeText = doc.select(".spot .rate_info .sp_txt9").get(0).parent().child(1).select("span")
					.get(0).text();
			if (tradeVolumeText.equals("0")) {
				return stock;
			}
			logger.debug("tradeVolumeText:" + tradeVolumeText);

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
			logger.debug("고가가 0이 아니고 고가가 상한가인가?" + (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())));
			logger.debug("현재가가 상한가가 아닌가?" + !curPrice.equals(stock.getMaxPrice()));
			logger.debug("고가가 상한가인가?" + highPrice.equals(stock.getMaxPrice()));
			// 고가가 0이 아니고 고가가 상한가인가?
			if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
					&& !curPrice.equals(stock.getMaxPrice())) {
				stock.setStockGubun("상터치↑↘");
				stock.setLineUp(12);
				topTouchStockList.add(stock);
				return stock;
			}
			if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
					&& !curPrice.equals(stock.getMinPrice())) {
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
				if (fRatio >= upDownStdNo) {
					if (specialLetter.equals("+") || specialLetter.equals("▲")) {
						stock.setStockGubun(specialLetter + upDownStdNo + "%이상");
						stock.setLineUp(13);
						over5PerUpStockList.add(stock);
					} else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
						stock.setStockGubun(specialLetter + upDownStdNo + "%이상");
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

	public StringBuilder createHtmlString(List<StockVO> stockList, String title) {
		StringBuilder sb1 = new StringBuilder();
		try {
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
			sb1.append("<h2 id='title'>").append(strYmdDashBracket).append(" ").append(title).append("</h2>");
			sb1.append("<table style='border-collapse:collapse'>\r\n");
			sb1.append("<tr>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>구분</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
			sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
			sb1.append(
					"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");

			int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
			if (iHourMinute >= 1830 || iHourMinute < 800) {
				// 2020.12.13 추가
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외국인매매</td>\r\n");
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관매매</td>\r\n");
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>개인매매</td>\r\n");
				// 2020.12.17 추가
				sb1.append(
						"<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>시간외단일가</td>\r\n");
			}
			sb1.append("</tr>\r\n");

			if (stockList.size() == 0) {
				sb1.append("<tr><td colspan='11' style='text-align:center;'>데이터가 없습니다.</td></tr>\r\n");
			}

			for (StockVO svo : stockList) {
				if (svo != null) {
					// 2020.12.13 추가
					DecimalFormat formatter = new DecimalFormat("#,##0");
					int iForeignTradingVolume = svo.getiForeignTradingVolume();
					int iOrganTradingVolume = svo.getiOrganTradingVolume();
					int iIndividualTradingVolume = svo.getiIndividualTradingVolume();

					String strForeignTradingVolume = formatter.format(iForeignTradingVolume);
					String strOrganTradingVolume = formatter.format(iOrganTradingVolume);
					String strIndividualTradingVolume = formatter.format(iIndividualTradingVolume);
					if (iForeignTradingVolume > 0 && iOrganTradingVolume > 0) {
						sb1.append("<tr style='border:1px solid red;'>\r\n");
					} else {
						sb1.append("<tr>\r\n");
					}
					String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
					sb1.append("<td>").append(svo.getStockGubun()).append("</td>\r\n");
					sb1.append("<td><a href='").append(url).append("'>").append(svo.getStockName())
							.append("</a></td>\r\n");
					sb1.append("<td style='text-align:right'>").append(svo.getCurPrice()).append("</td>\r\n");

					String specialLetter = StringUtils.defaultIfEmpty(svo.getSpecialLetter(), "");
					String varyPrice = svo.getVaryPrice();

					logger.debug("specialLetter+++>" + specialLetter);
					logger.debug("varyPrice+++>" + varyPrice);

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

					String varyRatio = StringUtils.defaultIfEmpty(svo.getVaryRatio(), "");
					if (varyRatio.startsWith("+")) {
						sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
					} else if (varyRatio.startsWith("-")) {
						sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
					} else {
						sb1.append(
								"<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + svo.getTradingVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + svo.getTradingAmount() + "</td>\r\n");

					if (iHourMinute >= 1830 || iHourMinute < 800) {
						// 외국인 매매
						if (iForeignTradingVolume < 0) {
							sb1.append(
									"<td style='text-align:right;color:blue'>" + strForeignTradingVolume + "</td>\r\n");
						} else if (iForeignTradingVolume > 0) {
							if (iOrganTradingVolume > 0) {
								sb1.append("<td style='text-align:right;color:red;background-color:yellow;'>" + strForeignTradingVolume + "</td>\r\n");
							}else {
								sb1.append("<td style='text-align:right;color:red'>" + strForeignTradingVolume + "</td>\r\n");
							}							
						} else {
							sb1.append("<td style='text-align:right'>0</td>\r\n");
						}
						// 기관 매매
						if (iOrganTradingVolume < 0) {
							sb1.append(
									"<td style='text-align:right;color:blue'>" + strOrganTradingVolume + "</td>\r\n");
						} else if (iOrganTradingVolume > 0) {
							if (iForeignTradingVolume > 0) {
								sb1.append("<td style='text-align:right;color:red;background-color:yellow;'>" + strOrganTradingVolume + "</td>\r\n");
							}else {
								sb1.append("<td style='text-align:right;color:red'>" + strOrganTradingVolume + "</td>\r\n");
							}	
						} else {
							sb1.append("<td style='text-align:right'>0</td>\r\n");
						}
						// 개인매매
						if (iIndividualTradingVolume < 0) {
							sb1.append("<td style='text-align:right;color:blue'>" + strIndividualTradingVolume
									+ "</td>\r\n");
						} else if (iIndividualTradingVolume > 0) {
							sb1.append("<td style='text-align:right;color:red'>" + strIndividualTradingVolume
									+ "</td>\r\n");
						} else {
							sb1.append("<td style='text-align:right'>0</td>\r\n");
						}
						// 2020.12.17 추가
						String strAfterHoursPrice = svo.getStrAfterHoursPrice();
						String strAfterHoursSign = StringUtils.defaultString(svo.getStrAfterHoursSign());
						String strAfterHoursPriceColor = "";
						String strAfterHoursPriceBgColor = "";
						if (strAfterHoursSign.equals("+")) {
							strAfterHoursPriceColor = "red";
							strAfterHoursPriceBgColor = "yellow";
						} else if (strAfterHoursSign.equals("-")) {
							strAfterHoursPriceColor = "blue";
						}
						sb1.append("<td style='text-align:right;color:" + strAfterHoursPriceColor + ";background-color:"+strAfterHoursPriceBgColor+"'>"
								+ strAfterHoursPrice + "</td>\r\n");
					}
					sb1.append("</tr>\r\n");
				}
			}
			sb1.append("</table>\r\n");
			sb1.append("<br><br>\r\n");
			/* 기업개요 */
			// http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=005930
			/*
			 * String companyInfoUrlPrefix =
			 * "http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd="; for
			 * (StockVO s : list) { if (s != null) { Document classAnalysisDoc; try {
			 * classAnalysisDoc = Jsoup.connect(companyInfoUrlPrefix +
			 * s.getStockCode()).get(); //
			 * logger.debug("classAnalysisDoc:"+classAnalysisDoc); Elements comment =
			 * classAnalysisDoc.select(".cmp_comment"); sb1.append("<div>\n");
			 * sb1.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" +
			 * s.getStockCode() + "'>" + s.getStockName() + "(" + s.getStockCode() + ")" +
			 * "</a></h4>\n"); sb1.append("<p>\n"); sb1.append(comment + "\n");
			 * sb1.append("</p>"); sb1.append("</div>\n"); sb1.append("<br>\n"); } catch
			 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); } }
			 * }
			 */
			// 뉴스 첨부
			StringBuilder newsAddedStockList = StockUtil.getNews(stockList);
			logger.debug("newsAddedStockList==>" + newsAddedStockList);
			// 증권명에 증권링크 생성
			StringBuilder stockTableAdded = StockUtil.stockLinkString(newsAddedStockList, stockList);
			sb1.append(stockTableAdded.toString());

			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			logger.debug(sb1.toString());
		} finally {
		}
		return sb1;
	}

	public void writeFile(StringBuilder sb, String title) {
		try {
			String fileName = USER_HOME + "\\documents\\" + strYmdDashBracket + " " + strHms + "_"
					+ title.replaceAll(" ", "_") + ".html";
			FileUtil.fileWrite(fileName, sb.toString());
		} finally {
		}
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

				logger.debug(cnt + "." + strStockCode + "." + strStockName);

				// 종합정보
				// http://finance.naver.com/item/news.nhn?code=246690
				logger.debug("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

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
						sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
								+ strStockName + "(" + strStockCode + ")" + "</a></h3>\n");

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

			String fileName = USER_HOME + "\\documents\\NewsTest." + strDate + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
