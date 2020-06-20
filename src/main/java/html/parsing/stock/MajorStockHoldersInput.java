package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.RetainAmountDescCompare;
import html.parsing.stock.util.DataSort.RetainRatioDescCompare;
import html.parsing.stock.model.MajorStockHolderVO;
import html.parsing.stock.util.FileUtil;

public class MajorStockHoldersInput {

	private static final Logger logger = LoggerFactory.getLogger(MajorStockHoldersInput.class);

	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String majorStockHolders;
	static List<StockVO> kospiStockList = new ArrayList<StockVO>();
	static List<StockVO> kosdaqStockList = new ArrayList<StockVO>();
	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	@Test
	public void readAndWriteMajorStockHoldersTest() {
		majorStockHolders = JOptionPane.showInputDialog("대주주명을 입력해주세요.");

		// 대웅제약 069620
		kospiStockList = readOne("069620","대웅제약");
		System.out.println("kospiStockList:"+kospiStockList);
		writeFile(kospiStockList, "코스피");
		// 삼성전자 005930
		kospiStockList = readOne("005930","삼성전자");
		System.out.println("kospiStockList:"+kospiStockList);
		writeFile(kospiStockList, "코스피");
	}

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
		majorStockHolders = JOptionPane.showInputDialog("대주주명을 입력해주세요.");
		try {
//			kospiStockList = StockUtil.readKospiStockCodeNameListFromExcel();
//			kosdaqStockList = StockUtil.readKosdaqStockCodeNameListFromExcel();
			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
			logger.debug("kosdaqStockList.size2 :" + kosdaqStockList.size());
		}

		kospiStockList = getAllStockInfo(kospiStockList);
		kosdaqStockList = getAllStockInfo(kosdaqStockList);

		Collections.sort(kospiStockList, new RetainRatioDescCompare());
		Collections.sort(kosdaqStockList, new RetainRatioDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + " 보유율순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + " 보유율순");

		Collections.sort(kospiStockList, new RetainAmountDescCompare());
		Collections.sort(kosdaqStockList, new RetainAmountDescCompare());

		writeFile(kospiStockList, "코스피 " + majorStockHolders + " 보유금액순");
		writeFile(kosdaqStockList, "코스닥 " + majorStockHolders + " 보유금액순");

	}

	public static List<StockVO> readOne(String stockCode,String stockName) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 1;
		StockVO stock = getStockHompage(cnt, stockCode, stockName);
		if (stock != null) {
			stocks.add(stock);
		}
		return stocks;
	}

	static long totalAmount = 0;

	public static List<StockVO> getAllStockInfo(List<StockVO> stockList) {
		List<StockVO> stocks = new ArrayList<StockVO>();

		int cnt = 0;
		for (StockVO svo : stockList) {
			String stockCode = svo.getStockCode();
			String stockName = svo.getStockName();

			StockVO stock = getStockHompage(cnt, stockCode, stockName);
			if (stock != null) {
				stocks.add(stock);
				totalAmount += stock.getlRetainAmount();
			}
			cnt++;
		}

		return stocks;
	}

	// 종목분석-기업현황
	// http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
	// 종목분석-기업개요
	// http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
	public static StockVO getStockHompage(int cnt, String code, String name) {
		logger.debug("code :" + code + " name :" + name);
		Document doc;
		StockVO stock = new StockVO();
		try {
			// 종합정보
			doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
			if (cnt == 1) {
				// System.out.println(doc.title());
				// System.out.println(doc.html());
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

			int iCurPrice = 0;

//			Elements blindElements = doc.select(".no_today .blind");
			Elements blindElements = doc.select(".spot .rate_info .today .no_today .no_up .blind");
			if (blindElements.size() <= 0) {
				return null;
			}
			String curPriceWithComma = blindElements.get(0).text();
			String curPriceWithoutComma = curPriceWithComma.replace(",", "");
			iCurPrice = Integer.parseInt(curPriceWithoutComma);
			stock.setCurPrice(curPriceWithComma);
			stock.setiCurPrice(iCurPrice);
			
			Elements no_exday = doc.select(".no_exday");
			Element new_totalinfo = null;
			if (no_exday.size() > 0) {
				new_totalinfo = no_exday.get(0);
				logger.debug("new_totalinfo:" + new_totalinfo);
				Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
				logger.debug("new_totalinfo_doc:" + new_totalinfo_doc);
				Elements no_up0 = new_totalinfo_doc.select(".no_up");
				logger.debug("no_up0:" + no_up0);
				if (no_up0.size() > 0) {
					Element no_up_idx0 = no_up0.get(0);
					logger.debug("no_up_idx0:" + no_up_idx0);
					Element span0 = no_up0.select("span").get(0);
					Element span1 = no_up0.select("span").get(1);
					logger.debug("span0 :" + span0);
					logger.debug("span1 :" + span1);

					Element no_up_idx1 = no_up0.get(1);
					logger.debug("no_up_idx1:" + no_up_idx1);

					String text = span0.text();

					// System.out.println("data:" + text);
					text = text.replaceAll("플러스", "+");
					text = text.replaceAll("마이너스", "-");
					text = text.replaceAll("상승", "▲");
					text = text.replaceAll("하락", "▼");
					text = text.replaceAll("퍼센트", "%");

//					stock.setVaryRatio(strYMD);
//					stock.setSpecialLetter(text);
				}

			}

			// 종목분석-기업현황
			doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();
			if (cnt == 1) {
				// System.out.println("title:" + doc.title());
				// System.out.println(doc.html());
			}

			Elements aClass = doc.select(".gHead01");
			Element aElem = null;
			if (aClass.size() <= 0) {
				return null;
			}
			if (aClass.size() > 1) {
				aElem = aClass.get(1);
			}
			System.out.println("aElem:" + aElem);
			if (aElem != null) {
				Elements tdElem = aElem.select("td");
				Element trElem = null;

				String retainVolumeWithComma = "";
				String retainVolumeWithoutComma = "";
				long lRetainVolume = 0;

				String retainAmount = "";
				long lRetainAmount = 0;

				String retainRatio = "";
				float fRetainRatio = 0;

				long lRetainAmountTotal = 0;
				long lRetainVolumeTotal = 0;
				float fRetainRatioTotal = 0;

				stock.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

				for (Element td : tdElem) {
					String majorStockHolderName = td.attr("title");
					// if (title.equals(majorStockHolders)) {
					if (majorStockHolderName.indexOf(majorStockHolders) != -1) {
						System.out.println("title:" + majorStockHolderName);
						stock.setMajorStockHolders(majorStockHolderName);
						trElem = td.parent();
						System.out.println("trElem:" + trElem);

						Elements trTd = trElem.select("td");
						MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();
						for (int i = 0; i < trTd.size(); i++) {
							if (i == 1) {
								logger.debug("trTd.get(1) :" + trTd.get(1));
								retainVolumeWithComma = StringUtils.defaultIfEmpty(trTd.get(1).text(), "0");
								logger.debug("retainVolumeWithComma :[" + retainVolumeWithComma + "]");
								retainVolumeWithoutComma = retainVolumeWithComma.replaceAll(",", "");
								lRetainVolume = Long.parseLong(retainVolumeWithoutComma);
								lRetainAmount = lRetainVolume * iCurPrice / 1000000;

								lRetainAmountTotal += lRetainAmount;
								lRetainVolumeTotal += lRetainVolume;

								DecimalFormat df = new DecimalFormat("#,##0");
								retainAmount = df.format(lRetainAmount);
							}
							if (i == 2) {
								retainRatio = StringUtils.defaultIfEmpty(trTd.get(2).text(), "0");
								logger.debug("retainRatio1 :[" + retainRatio + "]");
								fRetainRatio = Float.parseFloat(retainRatio);

								fRetainRatioTotal += fRetainRatio;
							}
							majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
							majorStockHolderVO.setRetainVolume(retainVolumeWithComma);
							majorStockHolderVO.setRetainAmount(retainAmount);
							majorStockHolderVO.setRetainRatio(retainRatio);
						}
						stock.getMajorStockHolderList().add(majorStockHolderVO);
					}
				}
				System.out.println(stock.getMajorStockHolderList().toString());

				stock.setlRetainVolume(lRetainVolumeTotal);
				stock.setlRetainAmount(lRetainAmountTotal);
				stock.setfRetainRatio(fRetainRatioTotal);

				if (stock.getMajorStockHolderList().size() > 0) {
					return stock;
				} else {
					return null;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void writeFile(List<StockVO> list, String title) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
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
		sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>No.</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
		if (majorStockHolders.indexOf("국민연금") == -1) {
			sb1.append(
				"<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
		}
		sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유율</td>\r\n");
		sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>현재가</td>\r\n");
		sb1.append(
			"<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");
		sb1.append("</tr>\r\n");
		int cnt = 1;
		for (StockVO svo : list) {
			Vector vt = svo.getMajorStockHolderList();
			int listSize = vt.size();
			if (svo != null) {
				sb1.append("<tr>\r\n");
				String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
				sb1.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
				sb1.append(
					"<td rowspan=" + listSize + "><a href='" + url + "'>" + svo.getStockName() + "</a></td>\r\n");

				for (int i = 0; i < listSize; i++) {
					if (i > 0) {
						sb1.append("<tr>\r\n");
					}

					MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
					if (majorStockHolders.indexOf("국민연금") == -1) {
						sb1.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
					}
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
					sb1.append("<td style='text-align:right'>" + svo.getCurPrice() + "</td>\r\n");
					sb1.append("<td style='text-align:right'>" + holderVO.getRetainAmount() + "</td>\r\n");
					sb1.append("</tr>\r\n");
				}
			}
		}
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		String fileName = userHome + "\\documents\\" + strDate + "_" + title + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());
	}

}
