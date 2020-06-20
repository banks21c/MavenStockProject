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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.StockNameLengthDescCompare;

public class StockNewsLink {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(StockNewsLink.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
    String strDate = sdf.format(new Date());
    String strStockCodeOrName = "롯데케미칼";
    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;
    String strStockName = "롯데케미칼";
    String strStockCode = "011170";

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    StockVO stock = new StockVO();
    List<StockVO> allStockList = new ArrayList<StockVO>();
    List<StockVO> kospiStockList = new ArrayList<>();
    List<StockVO> kosdaqStockList = new ArrayList<>();
    List<StockVO> searchStockList = new ArrayList<StockVO>();

    DecimalFormat df = new DecimalFormat("###.##");
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockNewsLink(1);
    }

    StockNewsLink() {


        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd","KOSPI");
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ");
        // List<Stock> kospiStockList = readOne("071970");
        // writeFile(kospiStockList,kospiFileName,"코스피");

        // readOne("246690", "티에스인베스트먼트");
        // readOne("049120", "파인디앤씨");
        // readOne("110570", "넥솔론");
        // readOne("195440", "테스트");
        writeFile(searchStockList);

    }

    StockNewsLink(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();
        strDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDate);
        String year = strDate.substring(0, 4);
        String month = strDate.substring(5, 7);
        String day = strDate.substring(8, 10);
        int iYear = Integer.parseInt(year);
        int iMonth = Integer.parseInt(month) - 1;
        int iDay = Integer.parseInt(day);
        System.out.println(year + month + day);

        cal1.set(iYear, iMonth, iDay);

        strStockCodeOrName = JOptionPane.showInputDialog("종목명이나 종목코드를 입력해 주세요", strStockCodeOrName);
        System.out.println("strStockCodeOrName:" + strStockCodeOrName);

        // 코스피
        simpleReadFile(kospiStockList, "코스피", kospiFileName);
        // 코스닥
        simpleReadFile(kosdaqStockList, "코스닥", kosdaqFileName);


        Collections.sort(searchStockList, new StockNameLengthDescCompare());
        Collections.sort(allStockList, new StockNameLengthDescCompare());

        writeFile(searchStockList);
    }

    public void simpleReadFile(List<StockVO> stockList, String kospidaq, String fileName) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int stockNameLength = 0;
            while ((read = reader.readLine()) != null) {
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

                if (strStockCodeOrName.equals(stockCode) || stockName.indexOf(strStockCodeOrName) != -1) {
                    System.out.println(kospidaq + " " + stockCode + " " + stockName);
                    searchStockList.add(stock1);
                }
                stockList.add(stock1);
                allStockList.add(stock1);
            }
            reader.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        } finally {
        }
    }

    public void writeFile(List<StockVO> searchList) {
        // File f = new File(userHome + "\\documents\\" + fileName);
        try {
            FileWriter fw = new FileWriter(
                    userHome + "\\documents\\" + strDate + "_오늘의_" + strStockCodeOrName + "_관련_뉴스.html");
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
            sb1.append("\t<h2>" + "[" + strDate + "] 오늘의 " + strStockCodeOrName + " 관련 뉴스</h2>");

            // 뉴스 첨부
            sb1.append(getAllNews(searchList).toString());

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

    public void writeFile(StockVO stock, String fileName, String title) {
        // File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
            String strDate2 = strDate;
            if (strDate.equals("")) {
                strDate = sdf.format(new Date());
                strDate2 = sdf2.format(new Date());
            }

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
            sb1.append("\t<h2>" + "[" + strDate2 + "] " + title + "</h2>");

            // 뉴스 첨부
            sb1.append(getNews(stock).toString());

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

    public StringBuilder getAllNews(List<StockVO> stockList) {
        System.out.println("stockList.size:" + stockList.size());

        StringBuilder sb = new StringBuilder();

        for (StockVO stock : stockList) {
            // 중복 뉴스 체크 로직 구현해야....
            StringBuilder sb1 = getNews(stock);
            sb.append(sb1);
        }
        return sb;
    }

    public StringBuilder getNews(StockVO stock) {

        StringBuilder sb1 = new StringBuilder();

        strStockCode = stock.getStockCode();
        strStockName = stock.getStockName();

        // 종합정보
        System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=1");

        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        // String strDate = sdf.format(new Date());
        System.out.println("날짜:" + strDate);

        Document doc;
        try {
            // http://finance.naver.com/item/news_news.nhn?code=110570
            doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=1").get();
            // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
            // System.out.println(doc.html());
            Element e1 = doc.select(".type2").get(0);
            // System.out.println(e1);

            Elements trs = e1.getElementsByTag("tr");

            for (int i = 0; i < trs.size(); i++) {
                Element tr = trs.get(i);

                Elements tds = tr.getElementsByTag("td");
                if (tds.size() < 3) {
                    continue;
                }

                Element dayTime = tr.getElementsByTag("span").first();
                Element a1 = tr.getElementsByTag("a").first();
                Element source = tr.getElementsByTag("td").get(2);

                if (a1 == null) {
                    continue;
                }

                System.out.println("dayTime:" + dayTime.html());
                System.out.println("dayTime:" + dayTime);
                System.out.println("dayTime:" + dayTime.text());
                System.out.println("a:" + a1);
                System.out.println("source:" + source);
                System.out.println("title:" + a1.html());
                System.out.println("href:" + a1.attr("href"));
                System.out.println("source:" + source.html());

                String strTitle = a1.html();
                String strLink = a1.attr("href");
                String strSource = source.html();
                String strDayTime = dayTime.html();
                String strDayTimeText = dayTime.text();
                System.out.println("strTitle:" + strTitle);
                System.out.println("strLink:" + strLink);
                System.out.println("strSource:" + strSource);
                System.out.println("strDayTime:" + strDayTime);
                System.out.println("strDayTimeText:" + strDayTimeText);
                String yyyyMMdd = strDayTime.substring(0, 10);
                System.out.println("yyyyMMdd:" + yyyyMMdd);
                System.out.println("strDate:" + strDate);

                String year = yyyyMMdd.substring(0, 4);
                String month = yyyyMMdd.substring(5, 7);
                String day = yyyyMMdd.substring(8, 10);
                int iYear = Integer.parseInt(year);
                int iMonth = Integer.parseInt(month) - 1;
                int iDay = Integer.parseInt(day);
                System.out.println(year + month + day);

                cal2.set(iYear, iMonth, iDay);

                long diffSec = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000; // 초
                long diffDay = diffSec / (60 * 60 * 24); // 날
                System.out.println("두 날자의 일 차이수 = " + diffDay);

                // sb1.append("<h3>"+ strTitle +"</h3>\n");
                // sb1.append("<div>"+ strSource+" | "+ strDayTime
                // +"</div>\n");
                if (diffDay >= 0) {
                    sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                            + strStockName + "(" + strStockCode + ")</a></h3>");
                    // sb1.append("<h3>"+ strTitle +"</h3>\n");
                    // sb1.append("<div>"+ strSource+" | "+ strDayTime
                    // +"</div>\n");

                    doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                    doc.select("meta").remove();
                    doc.select("link").remove();
                    doc.select("script").remove();
                    Elements link_news_elements = doc.select(".link_news");
                    if (link_news_elements != null) {
                        link_news_elements.remove();
                    }

                    Elements naver_splugin = doc.select(".naver-splugin");
                    System.out.println("naver_splugin:" + naver_splugin);
                    if (naver_splugin != null) {
                        naver_splugin.remove();
                    }

                    doc.select("li").remove();
                    Elements aElements = doc.select("a");
                    for (int ii = 0; ii < aElements.size(); ii++) {
                        Element aElement = aElements.get(ii);
                        String aElementText = aElement.text();

                        Element p = doc.createElement("span");
                        p.appendText(aElementText);

                        aElement.replaceWith(p);
                        System.out.println(aElement);
                    }
                    Element view = doc.select(".view").get(0);
                    System.out.println("view.className:" + view.className());
                    System.out.println("attr(class):" + view.attr("class"));
                    view.attr("style", "width:548px");
                    System.out.println("attr(style):" + view.attr("style"));

                    String strView = view.toString();
                    System.out.println("strStockName:" + strStockName);
                    // strView = strView.replaceAll(strStockName, "<a
                    // href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>" +
                    // strStockName + "</a>");
                    strView = strView.replaceAll("\\[\\]", "");
                    strView = strView.replaceAll("<ul></ul>", "");
                    strView = strView.replaceAll("<br>\r\n<br>\r\n<br>", "<br><br>");
                    strView = strView.replaceAll("&amp;", "&");

                    strView = strView.replaceAll("<script type=\"text/javascript\" src=\"/js/news_read.js\"></script>",
                            "");
                    sb1.append(strView);
                    sb1.append("<br><br>\n");

                } else {
                    break;
                }
            }
            System.out.println("=========================================");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 증권명에 증권링크 생성
        sb1 = StockUtil.stockLinkString(sb1, allStockList);
        return sb1;
    }

}
