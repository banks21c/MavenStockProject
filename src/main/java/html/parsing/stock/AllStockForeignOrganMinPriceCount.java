package html.parsing.stock;

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

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import html.parsing.stock.DataSort.MaxPriceCountDescCompare;

public class AllStockForeignOrganMinPriceCount extends Thread {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;
    int flag = 0;
    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;
    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        Thread t = new AllStockForeignOrganMinPriceCount(1);
        t.start();
    }

    public void run() {
        if (flag == 1) {
            // MakeKospiKosdaqList.makeKospiKosdaqList();

            // 모든 주식 정보를 조회한다.
            // 코스피
            List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
            System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

            // 코스닥
            List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
            System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

            // 1.상한일수순 정렬
            Collections.sort(kospiAllStockList, new MaxPriceCountDescCompare());
            Collections.sort(kosdaqAllStockList, new MaxPriceCountDescCompare());

            writeFile(kospiAllStockList, kospiFileName, "코스피 하한일수 ");
            writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 하한일수 ");
        } else {
            List<StockVO> kospiStockList = readOne("101530");
            writeFile(kospiStockList, kospiFileName, "코스피");
        }
    }

    AllStockForeignOrganMinPriceCount() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    AllStockForeignOrganMinPriceCount(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        flag = i;
    }

    public static List<StockVO> readOne(String stockCode) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode, "");
        if (stock != null) {
            stocks.add(stock);
        }
        return stocks;
    }

    public static List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];
                System.out.println(stockCode + "\t" + stockName);

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
                System.out.println("data:" + text);

                if (text.startsWith("현재가")) {
                    System.out.println("data1:" + dd.text());
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
                    System.out.println("txts.length:" + txts.length);
                    if (txts.length == 7) {
                        stock.setVaryRatio(txts[5] + txts[6]);
                    } else if (txts.length == 8) {
                        stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
                    }
                    varyRatio = stock.getVaryRatio();
                    stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
                    System.out.println("상승률:" + stock.getVaryRatio());
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
            // ============================================================================
            String strDate2 = "";
            String strFirstDate = "";
            int minPriceCount = 0;
            for (int i = 1; i <= 7; i++) {
                doc = Jsoup.connect("http://finance.naver.com/item/frgn.nhn?code=" + code + "&page=" + i).get();
                System.out.println("i:" + i + " minPriceCount:" + minPriceCount);
                // System.out.println(i+" "+doc.html());
                Elements type2Elements = doc.select(".type2");

                Element element2 = type2Elements.get(1);
                // 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
                // tr
                Elements trElements = element2.select("tr");
                int trCnt = 1;
                for (Element trElement : trElements) {
                    // td
                    Elements tdElements = trElement.select("td");
                    if (tdElements.size() == 9) {
                        strDate2 = tdElements.get(0).text();
                        // System.out.println(strDate+"---"+strFirstDate);
                        if (strDate2.equals(strFirstDate)) {
                            break;
                        } else {
                            if (trCnt == 1) {
                                strFirstDate = strDate2;
                            }
                        }
                        if (strDate2.startsWith("2015")) {
                            break;
                        }
                        // System.out.println("tdElements.get(2) :"+tdElements.get(2));
                        Elements imgEls = tdElements.get(2).select("img");
                        // System.out.println("imgEls :"+imgEls);
                        if (imgEls.size() > 0) {
                            String src = imgEls.get(0).attr("src");
                            // System.out.println("src:"+src);
                            if (src.indexOf("ico_down02.gif") != -1) {
                                minPriceCount++;
                                System.out.println("strDate:" + strDate2 + " " + minPriceCount);
                            }
                        }
                        trCnt++;
                    }
                }
                if (strDate2.equals(strFirstDate)) {
                    break;
                }
                if (strDate2.startsWith("2015")) {
                    break;
                }
            }
            System.out.println("minPriceCount:" + minPriceCount);

            stock.setMinPriceCount(minPriceCount);
            if (minPriceCount > 0) {
                return stock;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<font size=5>" + strYear + "년 " + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>하한일수</td>\r\n");
            sb1.append("</tr>");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getMinPriceCount() + "</td>\r\n");
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
