package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import html.parsing.stock.util.DataSort.TradingAmountDescCompare;
import html.parsing.stock.util.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.util.DataSort.VaryRatioAscCompare;
import html.parsing.stock.util.DataSort.VaryRatioDescCompare;
import html.parsing.stock.util.FileUtil;

public class AllStockPriceByDate extends Thread {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger1 = LoggerFactory.getLogger(AllStockPriceByDate.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.SSS", Locale.KOREAN).format(new Date());

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
        new AllStockPriceByDate(1);
        long startTime = System.currentTimeMillis();

        AllStockPriceByDate list1 = new AllStockPriceByDate(1, startTime);
        list1.start();
        Thread.sleep(2000);
        AllStockPriceByDate list2 = new AllStockPriceByDate(2, startTime);
        list2.start();
        Thread.sleep(2000);

        long endTime = System.currentTimeMillis();
        String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
        System.out.println("call time :" + elapsedTimeSecond);
        System.out.println("main method call finished.");
    }

    AllStockPriceByDate() {
        // List<Stock> kospiStockList = readOne("123890", "한국자산신탁");
        List<StockVO> kospiStockList = readOne("032980");
        writeFile(kospiStockList, kospiFileName, "코스피", "상승율");

        List<StockVO> kospiStockList1 = readOne("123890");
        writeFile(kospiStockList1, kospiFileName, "코스피", "상승율");
        // List<Stock> kosdaqStockList = readOne("003520");
        // writeFile(kosdaqStockList,kosdaqFileName,"코스닥");
    }

    AllStockPriceByDate(int i) {
        
        strDefaultDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
        String year = strDefaultDate.substring(0, 4);
        String month = strDefaultDate.substring(5, 7);
        String day = strDefaultDate.substring(8, 10);
        int iYear = Integer.parseInt(year);
        int iMonth = Integer.parseInt(month) - 1;
        int iDay = Integer.parseInt(day);

        strYMD = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";

        // System.out.println(year + month + day);
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

    @Override
	public void run() {

    }

    public AllStockPriceByDate(int iType, long firstStartTime) {
        this.iType = iType;
        this.firstStartTime = firstStartTime;
        System.out.println(iType + ": firstStartTime:" + this.firstStartTime);
    }

    public List<StockVO> readOne(String stockCode) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode);
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

    public StockVO getStockInfo(int cnt, String code) {
        Document doc;
        StockVO stock = new StockVO();
        stock.setStockCode(code);
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/sise_day.nhn?code=" + code + "&page=1").get();

            Elements type2s = doc.select(".type2");
            System.out.println("type2s.size:" + type2s.size());
            Element type2 = doc.select(".type2").get(0);

            Elements trElements = type2.select("tr");
            String date = "";
            String curPrice = "0";
            String varyPrice = "0";
            String beforePrice = "0";
            String startPrice = "0";
            String highPrice = "0";
            String lowPrice = "0";
            String tradingVolume = "0";
            String tradingAmount = "0";
            String maxPrice = "0";
            String minPrice = "0";

            int iCurPrice = 0;
            int iVaryPrice = 0;
            int iBeforePrice = 0;
            int iHighPrice = 0;
            int iLowPrice = 0;
            int iGapPrice = 0;
            int iMidPrice = 0;
            long lTradingVolume = 0;
            long lTradingAmount = 0;

            String specialLetter = "";
            String sign = "";
            String varyRatio = "";

            // Element trthElement = trElements.get(0);
            // Elements thElements = trthElement.select("th");
            // for (Element thElement : thElements) {
            // String th = thElement.text();
            // System.out.println("th:" + th);
            // }
            for (Element trElement : trElements) {
                // td
                Elements tdElements = trElement.select("td");
                System.out.println("tdElements.size:" + tdElements.size());
                if (tdElements.size() == 0) {
                    continue;
                }
                if (tdElements.get(0).text().equals(strDefaultDate)) {
                    date = tdElements.get(0).text();
                    curPrice = tdElements.get(1).text();
                    varyPrice = tdElements.get(2).text();
                    startPrice = tdElements.get(3).text();
                    highPrice = tdElements.get(4).text();
                    lowPrice = tdElements.get(5).text();
                    tradingVolume = tdElements.get(6).text();

                    iCurPrice = Integer.parseInt(curPrice.replaceAll(",", ""));
                    iVaryPrice = Integer.parseInt(varyPrice.replaceAll(",", ""));
                    iHighPrice = Integer.parseInt(highPrice.replaceAll(",", ""));
                    iLowPrice = Integer.parseInt(lowPrice.replaceAll(",", ""));
                    iGapPrice = iHighPrice - iLowPrice;
                    iMidPrice = iLowPrice + iGapPrice / 2;

                    lTradingVolume = Long.parseLong(tradingVolume.replaceAll(",", ""));
                    lTradingAmount = iMidPrice * lTradingVolume / 1000000;
                    tradingAmount = NumberFormat.getNumberInstance(Locale.US).format(lTradingAmount);

                    stock.setCurPrice(curPrice);
                    stock.setiCurPrice(Integer.parseInt(StringUtils.defaultIfEmpty(curPrice, "0").replaceAll(",", "")));

                    stock.setVaryPrice(varyPrice);
                    stock.setiVaryPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(varyPrice, "0").replaceAll(",", "")));

                    Elements imgElements = tdElements.get(2).select("img");
                    String imgName = "";
                    if (imgElements != null && imgElements.size() == 1) {
                        Element imgElement = imgElements.get(0);
                        if (imgElement != null) {
                            imgName = imgElement.attr("src");
                            imgName = imgName.substring(imgName.lastIndexOf("/") + 1);
                            System.out.println("imgName:" + imgName);
                            if (imgName.equals("ico_up.gif")) {
                                specialLetter = "▲";
                                sign = "+";
                            } else if (imgName.equals("ico_down.gif")) {
                                specialLetter = "▼";
                                sign = "-";
                            } else if (imgName.equals("ico_up02.gif")) {
                                specialLetter = "↑";
                                sign = "+";
                            } else if (imgName.equals("ico_down02.gif")) {
                                specialLetter = "↓";
                                sign = "-";
                            } else if (imgName.equals("ico_up.gif")) {
                                specialLetter = "";
                            }
                        }
                    }
                    stock.setSpecialLetter(specialLetter);
                    stock.setSign(sign);

                    if (sign.equals("+")) {
                        iBeforePrice = iCurPrice - iVaryPrice;
                        double dVaryRatio = +((double) iVaryPrice / iBeforePrice) * 100;
                        String tempVaryRatio = String.format("%,.2f", dVaryRatio);
                        varyRatio = tempVaryRatio + " %";
                    } else {
                        iBeforePrice = iCurPrice + iVaryPrice;
                        double dVaryRatio = -((double) iVaryPrice / iBeforePrice) * 100;
                        String tempVaryRatio = String.format("%,.2f", dVaryRatio);
                        varyRatio = tempVaryRatio + " %";
                    }
                    stock.setVaryRatio(varyRatio);
                    stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));

                    beforePrice = NumberFormat.getNumberInstance(Locale.US).format(iBeforePrice);

                    stock.setBeforePrice(beforePrice);
                    stock.setiBeforePrice(iBeforePrice);

                    stock.setStartPrice(startPrice);
                    stock.setiStartPrice(Integer.parseInt(startPrice.replaceAll(",", "")));

                    stock.setHighPrice(highPrice);
                    stock.setiHighPrice(Integer.parseInt(highPrice.replaceAll(",", "")));

                    stock.setLowPrice(lowPrice);
                    stock.setiLowPrice(Integer.parseInt(lowPrice.replaceAll(",", "")));

                    stock.setTradingVolume(tradingVolume);
                    stock.setiTradingVolume(Integer.parseInt(tradingVolume.replaceAll(",", "")));

                    stock.setTradingAmount(tradingAmount);
                    stock.setlTradingAmount(
                            Long.parseLong(StringUtils.defaultIfEmpty(tradingAmount.replaceAll(",", ""), "0")));

                    stock.setMaxPrice(maxPrice);
                    stock.setiMaxPrice(Integer.parseInt(stock.getMaxPrice().replaceAll(",", "")));

                    stock.setMinPrice(minPrice);
                    stock.setiMinPrice(Integer.parseInt(stock.getMinPrice().replaceAll(",", "")));

                    if (specialLetter.equals("↑")) {
                        topCount++;
                    } else if (specialLetter.equals("▲")) {
                        upCount++;
                    } else if (specialLetter.equals("↓")) {
                        bottomCount++;
                    } else if (specialLetter.equals("▼")) {
                        downCount++;
                    } else {
                            steadyCount++;
                    }
                    break;
                }
            }

            System.out.println("date:" + date);
            System.out.println("specialLetter:" + specialLetter);
            System.out.println("sign:" + sign);
            System.out.println("curPrice:" + curPrice);
            System.out.println("varyPrice:" + varyPrice);
            System.out.println("beforePrice:" + beforePrice);
            System.out.println("varyRatio:" + varyRatio);
            System.out.println("startPrice:" + startPrice);
            System.out.println("highPrice:" + highPrice);
            System.out.println("lowPrice:" + lowPrice);
            System.out.println("TradingVolume:" + tradingVolume);
            System.out.println("tradingAmount:" + tradingAmount);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
        StringBuilder sb1 = new StringBuilder();
        sb1.append("<!doctype html>\r\n");
        sb1.append("<html lang='ko'>\r\n");
        sb1.append("<head>\r\n");
        //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
        sb1.append("<style>\r\n");
        sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
        sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
        sb1.append("</style>\r\n");
        sb1.append("</head>\r\n");
        sb1.append("<body>\r\n");
        sb1.append("\t<h2>" + strYMD + " " + title + "</h2>");
        sb1.append("<h4><font color='red'>상한가:" + topCount + "</font><font color='red'> 상승:" + upCount
                + "</font><font color='blue'> 하한가:" + bottomCount + "</font><font color='blue'> 하락:" + downCount
                + "</font><font color='gray'> 보합:" + steadyCount + "</font></h4>");
        sb1.append("<table>\r\n");
        sb1.append("<tr>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
        // if(gubun.equals("상승율")){
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>상승율</td>\r\n");
        // }else if(gubun.equals("하락율")){
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>하락율</td>\r\n");
        // }else if(gubun.equals("거래량")){
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
        // }else if(gubun.equals("거래대금")){
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
        // sb1.append("<td
        // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
        // }
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락율</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
        sb1.append("</tr>\r\n");
        int cnt = 1;
        for (StockVO s : list) {
            if (s != null) {
                String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                System.out.println("specialLetter+++>" + specialLetter);
                sb1.append("<tr>\r\n");
                String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                sb1.append("<td>" + cnt++ + "</td>\r\n");
                sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");

                String varyPrice = s.getVaryPrice();

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
        fileName = userHome + "\\documents\\" + strYMD + "_" + title + ".html";
        FileUtil.fileWrite(fileName, sb1.toString());
    }

}
