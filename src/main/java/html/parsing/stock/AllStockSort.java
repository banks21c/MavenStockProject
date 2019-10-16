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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.TradingAmountDescCompare;
import html.parsing.stock.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.DataSort.VaryRatioAscCompare;
import html.parsing.stock.DataSort.VaryRatioDescCompare;

public class AllStockSort extends Thread {

    final static String userHome = System.getProperty("user.home");
    static java.util.logging.Logger logger1 = java.util.logging.Logger.getLogger("AllStockForeignOrganBothStraight");
    private static final Logger logger2 = LoggerFactory.getLogger(Weeks52NewLowHighPriceToday.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;

    static int topCount = 0;
    static int upCount = 0;
    static int bottomCount = 0;
    static int downCount = 0;
    static int steadyCount = 0;

    private int iType = 0;
    private long firstStartTime = 0L;

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new AllStockSort(1);
        long startTime = System.currentTimeMillis();

        AllStockSort list1 = new AllStockSort(1, startTime);
        list1.start();
        Thread.sleep(2000);
        AllStockSort list2 = new AllStockSort(2, startTime);
        list2.start();
        Thread.sleep(2000);

        long endTime = System.currentTimeMillis();
        String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
        System.out.println("call time :" + elapsedTimeSecond);
        System.out.println("main method call finished.");
    }

    AllStockSort() {
        // List<Stock> kospiStockList = readOne("123890", "한국자산신탁");
        List<StockVO> kospiStockList = readOne("032980");
        writeFile(kospiStockList, kospiFileName, "코스피", "상승율");

        List<StockVO> kospiStockList1 = readOne("123890");
        writeFile(kospiStockList1, kospiFileName, "코스피", "상승율");
        // List<Stock> kosdaqStockList = readOne("003520");
        // writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
    }

    AllStockSort(int i) {
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
        System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

        // 1.상승율순 정렬
        Collections.sort(kospiAllStockList, new VaryRatioDescCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 상승율 ", "상승율");

        // 2.하락율순 정렬
        Collections.sort(kospiAllStockList, new VaryRatioAscCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 하락율", "하락율");

        // 3.거래량 정렬
        Collections.sort(kospiAllStockList, new TradingVolumeDescCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 거래량", "거래량");

        // 4.거래대금순 정렬
        Collections.sort(kospiAllStockList, new TradingAmountDescCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 거래대금", "거래대금");

        topCount = 0;
        upCount = 0;
        bottomCount = 0;
        downCount = 0;
        steadyCount = 0;

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

        // 1.상승율순 정렬
        Collections.sort(kosdaqAllStockList, new VaryRatioDescCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 상승율 ", "상승율");

        // 2.하락율순 정렬
        Collections.sort(kosdaqAllStockList, new VaryRatioAscCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 하락율", "하락율");

        // 3.거래량 정렬
        Collections.sort(kosdaqAllStockList, new TradingVolumeDescCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 거래량", "거래량");

        // 4.거래대금순 정렬
        Collections.sort(kosdaqAllStockList, new TradingAmountDescCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 거래대금", "거래대금");

    }

    public void run() {

    }

    public AllStockSort(int iType, long firstStartTime) {
        this.iType = iType;
        this.firstStartTime = firstStartTime;
        System.out.println(iType + ": firstStartTime:" + this.firstStartTime);
    }

    public static List<StockVO> readOne(String stockCode) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode);
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

                StockVO stock = getStockInfo(cnt, stockCode);
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

    public static StockVO getStockInfo(int cnt, String code) {
        Document doc;
        StockVO stock = new StockVO();
        stock.setStockCode(code);
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
            logger1.log(java.util.logging.Level.INFO, "strYMD1:[" + strYMD + "]");
            logger2.debug("strYMD2:[" + strYMD + "]");

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
                System.out.println("text:" + text);
                if (text.startsWith("종목명")) {
                    String stockName = text.substring(4);
                    System.out.println("stockName:" + stockName);
                    stock.setStockName(stockName);
                }

                if (text.startsWith("현재가")) {
                    System.out.println("data1:" + dd.text());
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

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
        try {
            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strYMD + "_" + title + ".html");
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
            sb1.append("\t<h2>\r\n");
            sb1.append(strYMD + " " + title + "\r\n");
            sb1.append("</h2>\r\n");
            sb1.append("<h4><font color='red'>상한가:" + topCount + "</font><font color='red'> 상승:" + upCount
                    + "</font><font color='blue'> 하한가:" + bottomCount + "</font><font color='blue'> 하락:" + downCount
                    + "</font><font color='gray'> 보합:" + steadyCount + "</font></h4>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
            // if(gubun.equals("상승율")){
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>상승율</td>\r\n");
            // }else if(gubun.equals("하락율")){
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>하락율</td>\r\n");
            // }else if(gubun.equals("거래량")){
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
            // }else if(gubun.equals("거래대금")){
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");
            // }

            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");

            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String varyPrice = s.getVaryPrice();

                    System.out.println("specialLetter+++>" + specialLetter);
                    System.out.println("varyPrice+++>" + varyPrice);

                    if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
                            || specialLetter.startsWith("+")) {
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
                        sb1.append(
                                "<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
                    }
                    sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingVolume(), "")
                            + "</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + StringUtils.defaultIfEmpty(s.getTradingAmount(), "")
                            + "</td>\r\n");

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
