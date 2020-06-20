package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.VaryRatioDescCompare;

public class StockUniqueByWeekly {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger1 = null;
    private static java.util.logging.Logger logger2 = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    DecimalFormat df = new DecimalFormat("###.##");

    String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
    int iHms = Integer.parseInt(strHms);

    Date todayDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);

    String strDefaultDate = sdf.format(new Date());

    SimpleDateFormat sdfday = new SimpleDateFormat("yyyy.MM.dd E", Locale.KOREAN);

    int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
    String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
    String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

    String strDate = "";
    String strStockCodeOrName = "롯데케미칼";
    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;
    String strStockCode = "011170";
    String strStockName = "롯데케미칼";

    static int topCount = 0;
    static int upCount = 0;
    static int bottomCount = 0;
    static int downCount = 0;
    static int steadyCount = 0;

    List<StockVO> allStockList = new ArrayList<StockVO>();
    List<StockVO> topStockList = new ArrayList<StockVO>();
    List<StockVO> bottomStockList = new ArrayList<StockVO>();
    // List<StockVO> topTouchStockList = new ArrayList<StockVO>();
    // List<StockVO> bottomTouchStockList = new ArrayList<StockVO>();
    List<StockVO> upDownStockList = new ArrayList<StockVO>();
    List<StockVO> downUpStockList = new ArrayList<StockVO>();
    List<StockVO> over5PerUpStockList = new ArrayList<StockVO>();
    List<StockVO> over5PerDownStockList = new ArrayList<StockVO>();

    int inputDateMonday;
    int inputDateFriday;

    Calendar inputDateCal = Calendar.getInstance();
    Calendar inputDateMondayCal = Calendar.getInstance();
    Calendar inputDateFridayCal = Calendar.getInstance();
    Calendar tradeDateCal = Calendar.getInstance();
    Calendar newsDateCal = Calendar.getInstance();

    Date inputDate = inputDateCal.getTime();
    Date inputDateMondayDate = inputDateMondayCal.getTime();
    Date inputDateFridayDate = inputDateFridayCal.getTime();
    Date tradeDate = tradeDateCal.getTime();
    Date newsDate = newsDateCal.getTime();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockUniqueByWeekly(1);
    }

    StockUniqueByWeekly() {
        // MakeKospiKosdaqList.makeKospiKosdaqList();
        
        logger1 = LoggerFactory.getLogger(this.getClass());

        String strInputDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
        System.out.println("strInputDate:" + strInputDate);
        if (strInputDate != null) {
            strDate = strInputDate;
            String year = strDate.substring(0, 4);
            String month = strDate.substring(5, 7);
            String day = strDate.substring(8, 10);
            int iYear = Integer.parseInt(year);
            int iMonth = Integer.parseInt(month) - 1;
            int iDay = Integer.parseInt(day);
            // System.out.println(year + month + day);

            inputDateCal.set(iYear, iMonth, iDay);
            inputDateMondayCal.set(iYear, iMonth, iDay);
            inputDateFridayCal.set(iYear, iMonth, iDay);

            inputDate = inputDateCal.getTime();
            //the first day of the week is; e.g., SUNDAY in the U.S., MONDAY in France.
            int firstDayOfWeek = inputDateCal.getFirstDayOfWeek();
            int inputDayOfWeek = inputDateCal.get(Calendar.DAY_OF_WEEK);
            System.out.println("firstDayOfWeek:" + firstDayOfWeek);
            System.out.println("inputDayOfWeek:" + inputDayOfWeek);
            if (firstDayOfWeek == 1) {
                if (inputDate.after(todayDate)) {
                    if (inputDayOfWeek >= 1) {
                        JOptionPane.showMessageDialog(null, "입력일 기준 주 데이터가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            } else {
                if (inputDate.after(todayDate)) {
                    if (inputDayOfWeek >= 2) {
                        JOptionPane.showMessageDialog(null, "입력일 기준 주 데이터가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
            int mondayGap = inputDayOfWeek - (firstDayOfWeek + 1);
            int fridayGap = inputDayOfWeek - (firstDayOfWeek + 5);
            System.out.println("mondayGap:" + mondayGap);
            System.out.println("fridayGap:" + fridayGap);

            System.out.println("월요일:" + inputDateMondayCal.getTime());
            System.out.println("금요일:" + inputDateFridayCal.getTime());
            inputDateMondayCal.add(Calendar.DATE, -mondayGap);
            inputDateFridayCal.add(Calendar.DATE, -fridayGap);

            inputDateMondayDate = inputDateMondayCal.getTime();
            inputDateFridayDate = inputDateFridayCal.getTime();

            System.out.println("오늘:" + inputDateCal.getTime());
            System.out.println("월요일:" + inputDateMondayCal.getTime());
            System.out.println("금요일:" + inputDateFridayCal.getTime());

            System.out.println("getFirstDayOfWeek:" + inputDateCal.getFirstDayOfWeek());
            System.out.println("getWeekYear:" + inputDateCal.getWeekYear());
            System.out.println("getWeeksInWeekYear:" + inputDateCal.getWeeksInWeekYear());
            System.out.println("Calendar.DAY_OF_WEEK:" + inputDateCal.get(Calendar.DAY_OF_WEEK));
            System.out.println("Calendar.DAY_OF_WEEK_IN_MONTH:" + inputDateCal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("Calendar.DAY_OF_MONTH:" + inputDateCal.get(Calendar.DAY_OF_MONTH));
            System.out.println("Calendar.DAY_OF_YEAR:" + inputDateCal.get(Calendar.DAY_OF_YEAR));
            strDate = strDate.replaceAll(":", "-");
            strYmdDashBracket = "[" + strDate.replaceAll("\\.", "-") + "] ";
        }
        ((java.util.logging.Logger) logger2).log(java.util.logging.Level.INFO, "strYMD2:[" + strYmdDash + "]");

        Properties props = new Properties();
        try {
            // InputStream is = new FileInputStream("log4j.properties");
            // props.load(new FileInputStream("log4j.properties"));
            // InputStream is =
            // getClass().getResourceAsStream("/log4j.properties");
            props.load(getClass().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);

        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("log4j.properties");
        // PropertyConfigurator.configure(url);
        File log4jfile = new File("log4j.properties");
        String absolutePath = log4jfile.getAbsolutePath();
        PropertyConfigurator.configure(absolutePath);

        readOne("008800", "행남자기");
        listSortAndAdd();
        writeFile(allStockList, kosdaqFileName, "코스닥");

    }

    void clearList() {
        allStockList.clear();
        topStockList.clear();
        bottomStockList.clear();
        // topTouchStockList.clear();
        // bottomTouchStockList.clear();
        upDownStockList.clear();
        downUpStockList.clear();
        over5PerUpStockList.clear();
        over5PerDownStockList.clear();
    }

    void listSortAndAdd() {
        Collections.sort(topStockList, new VaryRatioDescCompare());
        Collections.sort(bottomStockList, new VaryRatioDescCompare());
        // Collections.sort(topTouchStockList, new VaryRatioDescCompare());
        // Collections.sort(bottomTouchStockList, new VaryRatioDescCompare());
        Collections.sort(upDownStockList, new VaryRatioDescCompare());
        Collections.sort(downUpStockList, new VaryRatioDescCompare());
        Collections.sort(over5PerUpStockList, new VaryRatioDescCompare());
        Collections.sort(over5PerDownStockList, new VaryRatioDescCompare());

        allStockList.addAll(topStockList);
        allStockList.addAll(bottomStockList);
        // allStockList.addAll(topTouchStockList);
        // allStockList.addAll(bottomTouchStockList);
        allStockList.addAll(upDownStockList);
        allStockList.addAll(downUpStockList);
        allStockList.addAll(over5PerUpStockList);
        allStockList.addAll(over5PerDownStockList);
    }

    StockUniqueByWeekly(String s) {
        
        logger1 = LoggerFactory.getLogger(this.getClass());

        getStockInfo(1,"008800", "행남자기");

    }

    StockUniqueByWeekly(int i) {
        // MakeKospiKosdaqList.makeKospiKosdaqList();
        
        logger1 = LoggerFactory.getLogger(this.getClass());

        String strInputDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
        System.out.println("strInputDate:" + strInputDate);
        if (strInputDate != null) {
            strDate = strInputDate;
            String year = strDate.substring(0, 4);
            String month = strDate.substring(5, 7);
            String day = strDate.substring(8, 10);
            int iYear = Integer.parseInt(year);
            int iMonth = Integer.parseInt(month) - 1;
            int iDay = Integer.parseInt(day);
            // System.out.println(year + month + day);

            inputDateCal.set(iYear, iMonth, iDay);
            inputDateMondayCal.set(iYear, iMonth, iDay);
            inputDateFridayCal.set(iYear, iMonth, iDay);

            inputDate = inputDateCal.getTime();
            //the first day of the week is; e.g., SUNDAY in the U.S., MONDAY in France.
            int firstDayOfWeek = inputDateCal.getFirstDayOfWeek();
            int inputDayOfWeek = inputDateCal.get(Calendar.DAY_OF_WEEK);
            System.out.println("firstDayOfWeek:" + firstDayOfWeek);
            System.out.println("inputDayOfWeek:" + inputDayOfWeek);
            if (firstDayOfWeek == 1) {
                if (inputDate.after(todayDate)) {
                    if (inputDayOfWeek >= 1) {
                        JOptionPane.showMessageDialog(null, "입력일 기준 주 데이터가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            } else {
                if (inputDate.after(todayDate)) {
                    if (inputDayOfWeek >= 2) {
                        JOptionPane.showMessageDialog(null, "입력일 기준 주 데이터가 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
            int mondayGap = inputDayOfWeek - (firstDayOfWeek + 1);
            int fridayGap = inputDayOfWeek - (firstDayOfWeek + 5);
            System.out.println("mondayGap:" + mondayGap);
            System.out.println("fridayGap:" + fridayGap);

            System.out.println("월요일:" + inputDateMondayCal.getTime());
            System.out.println("금요일:" + inputDateFridayCal.getTime());
            inputDateMondayCal.add(Calendar.DATE, -mondayGap);
            inputDateFridayCal.add(Calendar.DATE, -fridayGap);

            inputDateMondayDate = inputDateMondayCal.getTime();
            inputDateFridayDate = inputDateFridayCal.getTime();

            System.out.println("오늘:" + inputDateCal.getTime());
            System.out.println("월요일:" + inputDateMondayCal.getTime());
            System.out.println("금요일:" + inputDateFridayCal.getTime());

            System.out.println("getFirstDayOfWeek:" + inputDateCal.getFirstDayOfWeek());
            System.out.println("getWeekYear:" + inputDateCal.getWeekYear());
            System.out.println("getWeeksInWeekYear:" + inputDateCal.getWeeksInWeekYear());
            System.out.println("Calendar.DAY_OF_WEEK:" + inputDateCal.get(Calendar.DAY_OF_WEEK));
            System.out.println("Calendar.DAY_OF_WEEK_IN_MONTH:" + inputDateCal.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("Calendar.DAY_OF_MONTH:" + inputDateCal.get(Calendar.DAY_OF_MONTH));
            System.out.println("Calendar.DAY_OF_YEAR:" + inputDateCal.get(Calendar.DAY_OF_YEAR));
            strDate = strDate.replaceAll(":", "-");
            strYmdDashBracket = "[" + strDate.replaceAll("\\.", "-") + "] ";
        }
        ((java.util.logging.Logger) logger2).log(java.util.logging.Level.INFO, "strYMD2:[" + strYmdDash + "]");

        Properties props = new Properties();
        try {
            // InputStream is = new FileInputStream("log4j.properties");
            // props.load(new FileInputStream("log4j.properties"));
            // InputStream is =
            // getClass().getResourceAsStream("/log4j.properties");
            props.load(getClass().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);

        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("log4j.properties");
        // PropertyConfigurator.configure(url);
        File log4jfile = new File("log4j.properties");
        String absolutePath = log4jfile.getAbsolutePath();
        PropertyConfigurator.configure(absolutePath);

        // 모든 주식 정보를 조회한다.
        // 코스피
        readFile("코스피", kospiFileName);
        listSortAndAdd();
        writeFile(allStockList, kospiFileName, "코스피 특징주");

        clearList();

        // 코스닥
        readFile("코스닥", kosdaqFileName);
        listSortAndAdd();
        writeFile(allStockList, kosdaqFileName, "코스닥 특징주");
    }

    public void readOne(String stockCode, String stockName) {
        int cnt = 1;
        strStockCode = stockCode;
        strStockName = stockName;
        getStockInfo(cnt, strStockCode, strStockName);
    }

    public void readFile(String kospidaq, String fileName) {

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                strStockCode = read.split("\t")[0];
                strStockName = read.split("\t")[1];
                System.out.println(strStockCode + "\t" + strStockName);

                if (strStockCode.length() != 6) {
                    continue;
                }
                getStockInfo(cnt, strStockCode, strStockName);
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

    public StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
        System.out.println(strStockCode + "\t" + strStockName);
        Document doc;
        StockVO stock = null;

        try {
            doc = Jsoup.connect("http://finance.naver.com/item/sise_day.nhn?code=" + strStockCode + "&page=1").get();
            System.out.println("doc.select(\".Nnavi td a\"):" + doc.select(".Nnavi td a"));
            String lastPageHref = doc.select(".Nnavi td a").last().attr("href");
            int lastPageNo = 1;
            if (lastPageHref != null && lastPageHref.contains("&page")) {
                String strLastPageNo = lastPageHref.substring(lastPageHref.indexOf("&page=") + 6);
                System.out.println("strLastPageNo:" + strLastPageNo);
                if (strLastPageNo != null) {
                    lastPageNo = Integer.parseInt(strLastPageNo);
                    System.out.println("lastPageNo:" + lastPageNo);
                }
            }
            if (lastPageNo > 200) {
                lastPageNo = 200;
            }

            for (int page = 1; page <= lastPageNo; page++) {
                System.out.println("page===============================>" + page);
                // 일별시세
                doc = Jsoup.connect("http://finance.naver.com/item/sise_day.nhn?code=" + strStockCode + "&page=" + page).get();
                // System.out.println("doc:"+doc);

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
                    stock = new StockVO();
                    stock.setStockCode(strStockCode);
                    stock.setStockName(strStockName);

                    Elements tdElements = trElement.select("td");
                    System.out.println("tdElements.size:" + tdElements.size());
                    if (tdElements.size() <= 1) {
                        continue;
                    }
                    String strTradeDateText = tdElements.get(0).text();
                    System.out.println("strTradeDateText:" + strTradeDateText);
                    if (strTradeDateText.trim().equals("") || strTradeDateText.length() != 10) {
                        continue;
                    }

                    String year = strTradeDateText.substring(0, 4);
                    String month = strTradeDateText.substring(5, 7);
                    String day = strTradeDateText.substring(8, 10);
                    int iYear = Integer.parseInt(year);
                    int iMonth = Integer.parseInt(month) - 1;
                    int iDay = Integer.parseInt(day);

                    tradeDateCal.set(iYear, iMonth, iDay);

                    tradeDate = tradeDateCal.getTime();
                    System.out.println("tradeDate:" + tradeDate);
                    System.out.println("inputDateMondayDate:" + inputDateMondayDate);
                    System.out.println("inputDateFridayDate:" + inputDateFridayDate);

                    if (tradeDate.before(inputDateMondayDate)) {
                        return stock;
                    } else if (tradeDate.after(inputDateFridayDate)) {
                        continue;
                    } else {
                        System.out.println("tradeDate:" + tradeDate);
                        System.out.println("inputDateMondayDate:" + inputDateMondayDate);
                        System.out.println("inputDateFridayDate:" + inputDateFridayDate);
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

                        stock.setDate(date);
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
                            stock.setStockGubun("상한가↑");
                            stock.setLineUp(11);
                            topStockList.add(stock);
                            continue;
                        } else if (specialLetter.equals("▲")) {
                            upCount++;
                        } else if (specialLetter.equals("↓")) {
                            bottomCount++;
                            stock.setStockGubun("하한가↓");
                            stock.setLineUp(21);
                            bottomStockList.add(stock);
                            continue;
                        } else if (specialLetter.equals("▼")) {
                            downCount++;
                        } else {
                            if (stock.getiTradingVolume() > 0) {
                                steadyCount++;
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

                        // 현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
                        float higher = 0;
                        String flag = "";
                        int icur = stock.getiCurPrice();
                        int ihigh = stock.getiHighPrice();
                        int ilow = stock.getiLowPrice();

                        if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
                            higher = Math.abs(icur - ihigh);
                            flag = "↗↘";
                            System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
                            float upDownRatio = higher / icur * 100;
                            // upDownRatio = ((int)(upDownRatio * 100))/100f;
                            String strUpDownRatio = df.format(upDownRatio);
                            if (higher / icur * 100 > 10 && lTradingVolume > 0) {
                                stock.setStockGubun(strUpDownRatio + "%" + flag);
                                stock.setLineUp(16);
                                upDownStockList.add(stock);
                                continue;
                            }
                        } else {
                            higher = Math.abs(icur - ilow);
                            flag = "↘↗";
                            System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
                            float upDownRatio = higher / icur * 100;
                            // upDownRatio = ((int)(upDownRatio * 100))/100f;
                            String strUpDownRatio = df.format(upDownRatio);
                            if (upDownRatio > 10 && lTradingVolume > 0) {
                                stock.setStockGubun(strUpDownRatio + "%" + flag);
                                stock.setLineUp(16);
                                downUpStockList.add(stock);
                                continue;
                            }
                        }

                        float fRatio = 0f;
                        if (varyRatio.indexOf("%") != -1) {
                            fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
                            if (fRatio >= 10) {
                                if (specialLetter.equals("+") || specialLetter.equals("▲")) {
                                    stock.setStockGubun("+10%이상↗");
                                    stock.setLineUp(13);
                                    over5PerUpStockList.add(stock);
                                } else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
                                    stock.setStockGubun("-10%이상↘");
                                    stock.setLineUp(23);
                                    over5PerDownStockList.add(stock);
                                }
                            }
                        }
                    }
                }

            }
        } catch (IOException | NumberFormatException e) {
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
        String monday = sdfday.format(inputDateMondayDate);
        String friday = sdfday.format(inputDateFridayDate);
        try {
            FileWriter fw = new FileWriter(userHome + "\\documents\\" + monday + " ~ " + friday + " " + strHms + "_"
                    + title.replaceAll(" ", "_") + ".html");
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
            sb1.append("\t<h2>" + monday + " ~ " + friday + " " + title + "</h2>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>날짜</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>구분</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래량</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>거래대금(백만)</td>\r\n");
            sb1.append("</tr>\r\n");

            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + s.getDate() + "</td>\r\n");
                    sb1.append("<td>" + s.getStockGubun() + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String fontColor = "";
                    if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
                            || specialLetter.startsWith("+")) {
                        fontColor = "red";
                    } else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
                            || specialLetter.startsWith("-")) {
                        fontColor = "blue";
                    } else {
                        fontColor = "black";
                    }
                    sb1.append("<td style='text-align:right'><font color='" + fontColor + "'>" + s.getCurPrice()
                            + "</font></td>\r\n");
                    String varyPrice = s.getVaryPrice();
                    sb1.append("<td style='text-align:right'><font color='" + fontColor + "'>" + specialLetter + " "
                            + varyPrice + "</font></td>\r\n");

                    String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
                    sb1.append("<td style='text-align:right'><font color='" + fontColor + "'>" + varyRatio
                            + "</font></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getTradingVolume() + "</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getTradingAmount() + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</table>\r\n");
            sb1.append("<br><br>\r\n");

            for (StockVO s : list) {
                if (s != null) {
                    Document classAnalysisDoc = Jsoup.connect(
                            "http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + s.getStockCode())
                            .get();
                    // System.out.println("classAnalysisDoc:"+classAnalysisDoc);
                    Elements comment = classAnalysisDoc.select(".cmp_comment");
                    sb1.append("<div>\n");
                    sb1.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" + s.getStockCode() + "'>"
                            + s.getStockName() + "(" + s.getStockCode() + ")" + "</a></h4>\n");
                    sb1.append("<p>\n");
                    sb1.append(comment + "\n");
                    sb1.append("</p>");
                    sb1.append("</div>\n");
                    sb1.append("<br>\n");
                }
            }

            // 뉴스 첨부
            sb1.append(getNews(list).toString());

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

    public StringBuilder getNews(List<StockVO> allStockList) {

        StringBuilder sb1 = new StringBuilder();

        for (StockVO vo : allStockList) {
            strStockCode = vo.getStockCode();
            strStockName = vo.getStockName();

            // 종합정보
            System.out.println("strStockCode:" + strStockCode + " strStockName:" + strStockName);
            System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

            Document doc;
            try {
                // http://finance.naver.com/item/news_news.nhn?code=110570
                doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
                        .get();
                // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
                // System.out.println(doc.html());
                Elements types = doc.select(".type5");
                Element type = null;
                if (types.size() <= 0) {
                    return sb1;
                }
                type = doc.select(".type5").get(0);

                Elements trs = type.getElementsByTag("tr");
                if (trs != null) {
                    System.out.println("trs.size:" + trs.size());
                }

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
                    System.out.println("a:" + a1);
                    System.out.println("source:" + source);
                    System.out.println("dayTime:" + dayTime);
                    System.out.println("title:" + a1.html());
                    System.out.println("href:" + a1.attr("href"));
                    System.out.println("source:" + source.html());
                    System.out.println("dayTime:" + dayTime.html());

                    String strTitle = a1.html();
                    String strLink = a1.attr("href");
                    String strSource = source.html();
                    String strDayTime = dayTime.html();
                    String strNewsYmd = strDayTime.substring(0, 10);
                    //int iYmd2 = Integer.parseInt(strNewsYmd.replaceAll("\\.", ""));
                    String year = strNewsYmd.substring(0, 4);
                    String month = strNewsYmd.substring(5, 7);
                    String day = strNewsYmd.substring(8, 10);
                    int iYear = Integer.parseInt(year);
                    int iMonth = Integer.parseInt(month) - 1;
                    int iDay = Integer.parseInt(day);
                    newsDateCal.set(iYear, iMonth, iDay);
                    newsDate = newsDateCal.getTime();

                    if (newsDate.before(inputDateMondayDate)) {
                        continue;
                    } else if (newsDate.after(inputDateFridayDate)) {
                        continue;
                    } else {
                        sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "(" + strStockCode + ")</a></h3>");
                        // sb1.append("<h3>"+ strTitle +"</h3>\n");
                        // sb1.append("<div>"+ strSource+" | "+ strDayTime
                        // +"</div>\n");

                        doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                        Elements link_news_elements = doc.select(".link_news");
                        if (link_news_elements != null) {
                            link_news_elements.remove();
                        }
                        Elements naver_splugin = doc.select(".naver-splugin");
                        System.out.println("naver_splugin:" + naver_splugin);
                        if (naver_splugin != null) {
                            naver_splugin.remove();
                        }
                        doc.select("a").remove();
                        doc.select("li").remove();

                        Element view = doc.select(".view").get(0);

                        String strView = view.toString();
                        strView = strView.replaceAll(strStockName,
                                "<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "</a>");

                        sb1.append(strView);
                        sb1.append("\n");

                        System.out.println("view:" + view);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1;
    }

    public void readNews(List<StockVO> allStockList) {

        int cnt = 1;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\NewsTest." + strDate + ".html");
            StringBuilder sb1 = new StringBuilder();

            for (StockVO vo : allStockList) {
                strStockCode = vo.getStockCode();
                strStockName = vo.getStockName();

                System.out.println(cnt + "." + strStockCode + "." + strStockName);

                // 종합정보
                // http://finance.naver.com/item/news.nhn?code=246690
                System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

                Document doc = Jsoup
                        .connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
                        .get();
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

                    System.out.println("title:" + a1.html());
                    System.out.println("href:" + a1.attr("href"));
                    System.out.println("source:" + source.html());
                    System.out.println("dayTime:" + dayTime.html());

                    String strTitle = a1.html();
                    String strLink = a1.attr("href");
                    String strSource = source.html();
                    String strDayTime = dayTime.html();
                    String strNewsYmd = strDayTime.substring(0, 10);
                    String year = strNewsYmd.substring(0, 4);
                    String month = strNewsYmd.substring(5, 7);
                    String day = strNewsYmd.substring(8, 10);
                    int iYear = Integer.parseInt(year);
                    int iMonth = Integer.parseInt(month) - 1;
                    int iDay = Integer.parseInt(day);
                    newsDateCal.set(iYear, iMonth, iDay);
                    newsDate = newsDateCal.getTime();

                    if (newsDate.before(inputDateMondayDate)) {
                    } else if (newsDate.after(inputDateFridayDate)) {
                    } else {
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
                        System.out.println("naver_splugin:" + naver_splugin);
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

                        System.out.println("view:" + view);
                    }
                }
            }

            System.out.println(sb1.toString());

            fw.write(sb1.toString());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
