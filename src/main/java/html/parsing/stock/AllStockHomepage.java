package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

import html.parsing.stock.util.DataSort.ListedDayDescCompare;

public class AllStockHomepage {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(AllStockHomepage.class);

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
        new AllStockHomepage(1);
    }

    AllStockHomepage() {

        List<StockVO> kospiStockList = readOne("012450", "한화테크윈");
        writeFile(kospiStockList, kospiFileName, "코스피");

        // List<Stock> kosdaqStockList = readOne("003520");
        // writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
    }

    AllStockHomepage(int i) {

        

        // 모든 주식 정보를 조회한다.
		try {
			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(AllStockHomepage.class.getName()).log(Level.SEVERE, null,
					ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr("kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		}

		// 코스피
        List<StockVO> kospiAllStockList = new ArrayList<StockVO>();
        for(int j=0;j<kospiStockList.size();j++) {
        	StockVO svo = kospiStockList.get(j);
        	if (svo.getStockCode().length() != 6) {
        		logger.debug("stock code length :"+svo.getStockCode());;
        	}
            svo = getStockHomepage(j, svo.getStockCode());
            kospiAllStockList.add(svo);
        }
        logger.debug("kospiAllStockList.size :" + kospiAllStockList.size());
        // 코스닥
        List<StockVO> kosdaqAllStockList = new ArrayList<StockVO>();
        for(int j=0;j<kosdaqStockList.size();j++) {
        	StockVO svo = kosdaqStockList.get(j);
        	if (svo.getStockCode().length() != 6) {
        		logger.debug("stock code length :"+svo.getStockCode());;
        	}
            svo = getStockHomepage(j, svo.getStockCode());
            kosdaqAllStockList.add(svo);
        }
        writeFile(kospiAllStockList, kospiFileName, "코스피 홈페이지 목록");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 홈페이지 목록");

        // 상장일 내림차순 정렬
        Collections.sort(kospiAllStockList, new ListedDayDescCompare());
        Collections.sort(kosdaqAllStockList, new ListedDayDescCompare());

        writeListedDay(kospiAllStockList, kospiFileName, "코스피 설립일,상장일 목록");
        writeListedDay(kosdaqAllStockList, kosdaqFileName, "코스닥 설립일,상장일 목록");
    }

    public static List<StockVO> readOne(String stockCode, String stockName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockHomepage(cnt, stockCode);
        if (stock != null) {
            stock.setStockName(stockName);
            stocks.add(stock);
        }
        return stocks;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
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
            sb1.append("\t<font size=5>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목코드</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>홈페이지</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>대표전화</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>주담전화</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {

                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "' target='_new'>" + s.getStockName() + "</a></td>\r\n");
                    // sb1.append("<td
                    // style='text-align:center'>"+s.getStockCode()+"</td>\r\n");
                    if (!s.getHomePage().equals("")) {
                        sb1.append("<td style='text-align:left'><a href='" + s.getHomePage() + "' target='new'>"
                                + s.getHomePage().replaceAll("http://", "") + "</a></td>\r\n");
                    } else {
                        sb1.append("<td style='text-align:left'>-</td>\r\n");
                    }
                    sb1.append("<td style='text-align:left'>" + s.getMainPhone() + "</td>\r\n");
                    sb1.append("<td style='text-align:left'>" + s.getStockPhone() + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            fw.write(sb1.toString());
            logger.debug(sb1.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }

    }

    private static StockVO getStockHomepage(int cnt, String stockCode) {
        return getStockHompage2(cnt, stockCode);
    }

    // http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=010600
    // 기업현황
    // http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
    // 기업개요
    // http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
    public static StockVO getStockHomepage1(int cnt, String code) {
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 종합분석-기업개요
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=" + code).get();
            logger.debug("title:" + doc.title());
            if (cnt == 1) {
                // logger.debug(doc.html());
            }
            stock.setStockCode(code);

            String stockCode = code;
            String stockHomePage = "";
            String mainPhone = "";
            String stockPhone = "";

            stock.setStockCode(stockCode);

            Elements aClass = doc.select(".cEm");
            if (aClass.size() <= 0) {
                return null;
            }
            logger.debug("■■■■■■■■■■■■■■■■■■■■■■");
            logger.debug("meta:" + aClass);
            logger.debug("■■■■■■■■■■■■■■■■■■■■■■");
            logger.debug(aClass.get(0).html());
            logger.debug(aClass.get(0).attr("title"));
            String strHomePage[] = aClass.get(0).attr("title").split(" ");
            if (strHomePage.length > 1) {
                stockHomePage = aClass.get(0).attr("title").split(" ")[1];

                if (!stockHomePage.startsWith("http")) {
                    stockHomePage = "http://" + stockHomePage;
                }
            }
            stock.setHomePage(stockHomePage);
            logger.debug(stockHomePage);

            String title2[] = aClass.get(1).attr("title").split("[\r\n]+");
            mainPhone = title2[0].split(" ")[1];

            String judam[] = title2[1].split(" ");
            if (judam.length > 1) {
                stockPhone = title2[1].split(" ")[1];
            }
            stock.setMainPhone(mainPhone);
            stock.setStockPhone(stockPhone);

            logger.debug(stock.getStockCode());
            logger.debug(stock.getStockName());
            logger.debug(stock.getMainPhone());
            logger.debug(stock.getStockPhone());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public static StockVO getStockHompage2(int cnt, String code) {
        Document doc;
        StockVO stock = new StockVO();
        stock.setStockCode(code);
        try {
            // 종합분석-기업개요
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=" + code).get();
            logger.debug("title:" + doc.title());
            if (cnt == 1) {
                // logger.debug(doc.html());
            }
            String strDoc = doc.html();
            strDoc = strDoc.replace("&nbsp;", " ");

            doc = Jsoup.parse(strDoc);

            //logger.debug("cT201:" + doc.getElementById("cTB201"));

            Element cTB201 = doc.getElementById("cTB201");

            logger.debug("tr start----------------");
            Elements trEls = cTB201.select("tbody tr");
            for (Element tr : trEls) {
                logger.debug("tr:" + tr);
                Elements thEls = tr.select("th");
                Elements tdEls = tr.select("td");
                int thCnt = 0;
                for (Element th : thEls) {

                    String key = th.text();
                    String value = tdEls.get(thCnt).text();

                    logger.debug("key:" + key + " value:" + value);
                    if (key.equals("본사주소")) {
                        stock.setHeadquartersAddress(value);
                    } else if (key.equals("홈페이지")) {
                        stock.setHomePage(value);
                    } else if (key.equals("대표전화")) {
                        String phoneArray[] = value.split("\\(");
                        if (phoneArray.length > 1) {
                            String mainPhone = value.split("\\(")[0];
                            stock.setMainPhone(mainPhone);

                            String stockPhoneArray[] = value.split("\\(")[1].split(" ");
                            String stockPhone = "";
                            if (stockPhoneArray.length > 1) {
                                stockPhone = stockPhoneArray[1].substring(0, stockPhoneArray[1].length() - 1);
                            }
                            stock.setStockPhone(stockPhone);
                        } else {
                            stock.setMainPhone(value);
                            stock.setStockPhone("-");
                        }
                    } else if (key.equals("설립일")) {
                        String foundDay = value.substring(0, value.indexOf(" "));
                        String listedDay = value.substring(value.indexOf(" ")).trim().replaceAll("\\(", "")
                                .replaceAll("\\)", "").split(" ")[1];
                        logger.debug(foundDay + "===" + listedDay);
                        //설립일
                        stock.setFoundDay(foundDay);
                        //상장일
                        stock.setListedDay(listedDay);
                    } else if (key.equals("대표이사")) {
                        stock.setCeo(value);
                    } else if (key.equals("계열")) {
                        stock.setAffiliation(value);
                    } else if (key.equals("종업원수")) {
                        stock.setNumberOfEmployee(value);
                    } else if (key.equals("발행주식수(보통/우선)")) {
                        stock.setTotalNumberOfStock(value);
                    } else if (key.equals("감사인")) {
                        stock.setAuditor(value);
                    } else if (key.equals("명의개서")) {
                        stock.setTransferShareholdersName(value);
                    } else if (key.equals("주거래은행")) {
                        stock.setMainBank(value);
                    }
                    thCnt++;
                }
            }
            logger.debug("tr end----------------");
            logger.debug("----------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void writeListedDay(List<StockVO> list, String fileName, String title) {
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
            sb1.append("\t<font size=5>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>설립일</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>상장일</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {

                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "' target='_new'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td style='text-align:left'>" + s.getFoundDay() + "</td>\r\n");
                    sb1.append("<td style='text-align:left'>" + s.getListedDay() + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            fw.write(sb1.toString());
            logger.debug(sb1.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

}
