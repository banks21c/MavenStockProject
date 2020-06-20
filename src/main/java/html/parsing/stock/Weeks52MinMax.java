package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.MaxCurAscCompare;
import html.parsing.stock.util.DataSort.MinCurDescCompare;
import html.parsing.stock.util.DataSort.MinMaxDescCompare;
import html.parsing.stock.util.DataSort.YearStartCurDescCompare;

public class Weeks52MinMax {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(Weeks52MinMax.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	List<StockVO> kospiStockList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Weeks52MinMax();
    }

    Weeks52MinMax() {

        List<StockVO> kospiStockList = readOne("040910", "아이씨디");
        writeFile(kospiStockList, kospiFileName, "코스피", "ROE");

    }

    Weeks52MinMax(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
        System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

		try {
			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(Weeks52NewLowHighPriceVsCurPrice.class.getName()).log(Level.SEVERE, null,
					ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		}

        // 저가 대비 현재가 상승률
        Collections.sort(kospiAllStockList, new MinCurDescCompare());
        Collections.sort(kosdaqAllStockList, new MinCurDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 52주 저가 대비 현재가 상승률", "MIN_CUR");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 52주 저가 대비 현재가 상승률", "MIN_CUR");

        // 고가 대비 현재가 하락률
        Collections.sort(kospiAllStockList, new MaxCurAscCompare());
        Collections.sort(kosdaqAllStockList, new MaxCurAscCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 52주 고가 대비 현재가 하락률 ", "MAX_CUR");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 52주 고가 대비 현재가 하락률 ", "MAX_CUR");

        // 저가 대비 고가 등락률
        Collections.sort(kospiAllStockList, new MinMaxDescCompare());
        Collections.sort(kosdaqAllStockList, new MinMaxDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 52주 저가 대비 고가 등락률 ", "MIN_MAX");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 52주 저가 대비 고가 등락률 ", "MIN_MAX");

        // 년초 대비 현재가 등락률
        Collections.sort(kospiAllStockList, new YearStartCurDescCompare());
        Collections.sort(kosdaqAllStockList, new YearStartCurDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 년초 대비 현재가 등락률 ", "START_CUR");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 년초 대비 현재가 등락률 ", "START_CUR");

        writeFile(kospiAllStockList, kospiFileName, "코스피 52주 저가,고가,년초 대비 현재가 등락률 ", "ALL");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 52주 저가,고가,년초 대비 현재가 등락률 ", "ALL");

    }

    public List<StockVO> readOne(String stockCode, String stockName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode, stockName);
        if (stock != null) {
            stocks.add(stock);
        }
        return stocks;
    }

    public List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                cnt++;
                System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];

                if (stockCode.length() != 6) {
                    continue;
                }
                StockVO stock = getStockInfo(cnt, stockCode, stockName);
                if (stock != null) {
                    stocks.add(stock);
                }
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

    public StockVO getStockInfo(int cnt, String code, String name) {
        System.out.println(code + ":" + name);
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/company/c1010001.aspx?cmp_cd=" + code).get();
            if (cnt == 1) {
                // System.out.println(doc.title());
                // System.out.println(doc.html());
            }
            stock.setStockCode(code);
            stock.setStockName(name);

            Element edd = doc.getElementById("cTB11");
            Element td0 = edd.select("td").first();
            Element td1 = edd.select("td").get(1);

            String strTd0[] = td0.text().split("/");
            String strTd1[] = td1.text().split("/");

            String curPrice = strTd0[0].substring(0, strTd0[0].indexOf("원"));
            String weeks52MaxPrice = strTd1[0].substring(0, strTd1[0].indexOf("원"));
            String weeks52MinPrice = strTd1[1].substring(0, strTd1[1].indexOf("원"));
            String yearStartPrice = yearStartPrice(stock).getYearStartPrice();

            stock.setWeeks52MaxPrice(weeks52MaxPrice);
            stock.setWeeks52MinPrice(weeks52MinPrice);
            stock.setCurPrice(curPrice);

            curPrice = curPrice.replaceAll(",", "").trim();
            weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
            weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();
            yearStartPrice = yearStartPrice.replaceAll(",", "").trim();

            System.out.println("curPrice:" + curPrice);
            System.out.println("weeks52MaxPrice:" + weeks52MaxPrice);
            System.out.println("weeks52MinPrice:" + weeks52MinPrice);
            System.out.println("yearStartPrice:" + yearStartPrice);

            int iCurPrice = Integer.parseInt(curPrice);
            int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
            int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);
            int iYearStartPrice = Integer.parseInt(yearStartPrice);

            float minMaxRatio = (float) (iWeeks52MaxPrice - iWeeks52MinPrice) / iWeeks52MinPrice * 100;
            float minCurRatio = (float) (iCurPrice - iWeeks52MinPrice) / iWeeks52MinPrice * 100;
            float maxCurRatio = -(float) (iWeeks52MaxPrice - iCurPrice) / iWeeks52MaxPrice * 100;
            float startCurRatio = 0f;
            if (iYearStartPrice > iCurPrice) {
                startCurRatio = -(float) (iYearStartPrice - iCurPrice) / iYearStartPrice * 100;
            } else {
                startCurRatio = (float) (iCurPrice - iYearStartPrice) / iYearStartPrice * 100;
            }

            minMaxRatio = (int) (minMaxRatio * 100) / 100f;
            minCurRatio = (int) (minCurRatio * 100) / 100f;
            maxCurRatio = (int) (maxCurRatio * 100) / 100f;
            startCurRatio = (int) (startCurRatio * 100) / 100f;

            System.out.println("minMaxRatio:" + minMaxRatio);
            System.out.println("minCurRatio:" + minCurRatio);
            System.out.println("maxCurRatio:" + maxCurRatio);
            System.out.println("startCurRatio:" + startCurRatio);

            stock.setMinMaxRatio(minMaxRatio);
            stock.setMinCurRatio(minCurRatio);
            stock.setMaxCurRatio(maxCurRatio);
            stock.setStartCurRatio(startCurRatio);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return stock;
    }

    public StockVO yearStartPrice(StockVO stock) {
        String strDate2 = "";
        String strFirstDate = "";
        String strCurPrice = "";
        try {
            for (int i = 1; i <= 13; i++) {
                Document doc;
                doc = Jsoup
                        .connect("http://finance.naver.com/item/frgn.nhn?code=" + stock.getStockCode() + "&page=" + i)
                        .get();

                // System.out.println(i+" "+doc.html());
                Elements type2Elements = doc.select(".type2");

                Element element2 = type2Elements.get(1);
                // 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
                // tr
                Elements trElements = element2.select("tr");
                int trCnt = 1;
                String strTempDate = "";
                for (Element trElement : trElements) {
                    // td
                    Elements tdElements = trElement.select("td");
                    if (tdElements.size() == 9) {
                        // 날짜를 구한다.
                        strTempDate = tdElements.get(0).text();
                        // System.out.println(strDate2+"---"+strTempDate+"---"+strFirstDate);
                        if (!strTempDate.startsWith(strYear)) {
                            break;
                        }
                        if (strTempDate.startsWith("" + (iYear - 1))) {
                            break;
                        }
                        if (strTempDate.equals(strFirstDate)) {
                            break;
                        } else {
                            if (trCnt == 1) {
                                strFirstDate = strTempDate;
                            }
                        }
                        // 해가 바뀌지 않은 마지막 날짜를 strDate2 변수에 담는다.
                        strDate2 = strTempDate;
                        strCurPrice = tdElements.get(1).text();
                        // System.out.println("tdElements.get(1):"+tdElements.get(1).text());
                        trCnt++;
                    }
                }
                if (!strTempDate.startsWith(strYear)) {
                    break;
                }
                if (strTempDate.equals(strFirstDate)) {
                    break;
                }
                if (strTempDate.startsWith("" + (iYear - 1))) {
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("yearStartPrice :" + strCurPrice);
        stock.setYearStartPrice(strCurPrice);
        // System.out.println(strDate2+"---0---"+strFirstDate);
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
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
            sb1.append("\t<font size=4>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");

            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
            if (gubun.equals("ALL")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 고가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");

                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저vs현td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 고vs현</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저vs고</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>년초 대비</td>\r\n");
            } else if (gubun.equals("MIN_CUR")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저vs현</td>\r\n");
            } else if (gubun.equals("MAX_CUR")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 고가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 고vs현</td>\r\n");
            } else if (gubun.equals("MIN_MAX")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 고가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>52주 저vs고</td>\r\n");
            } else if (gubun.equals("START_CUR")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기준일가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>년초vs현</td>\r\n");
            }
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>비고</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");

                    sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");
                    if (gubun.equals("ALL")) {
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MinPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MaxPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getYearStartPrice() + "</td>\r\n");

                        sb1.append("<td style='text-align:right'>" + s.getMinCurRatio() + "%</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getMaxCurRatio() + "%</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getMinMaxRatio() + "%</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getStartCurRatio() + "%</td>\r\n");
                    } else if (gubun.equals("MIN_CUR")) {
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MinPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getMinCurRatio() + "%</td>\r\n");
                    } else if (gubun.equals("MAX_CUR")) {
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MaxPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getMaxCurRatio() + "%</td>\r\n");
                    } else if (gubun.equals("MIN_MAX")) {
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MinPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MaxPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getMinMaxRatio() + "%</td>\r\n");
                    } else if (gubun.equals("START_CUR")) {
                        sb1.append("<td style='text-align:right'>" + s.getYearStartPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getStartCurRatio() + "%</td>\r\n");
                    }
                    sb1.append("<td style='text-align:left'>&nbsp;</td>\r\n");

                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println(sb1.toString());
            fw.write(sb1.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }
    }

}
