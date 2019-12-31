package html.parsing.stock;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class StockTimeout {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    DecimalFormat df = new DecimalFormat("###.##");

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockTimeout();
    }

    StockTimeout() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url1 = "http://finance.daum.net/quote/timeout.daum?sign=2&order=desc&stype=P";
        createHTMLFile(url1, "시간외단일가 코스피 상승");
        String url2 = "http://finance.daum.net/quote/timeout.daum?sign=5&order=asc&stype=P";
        createHTMLFile(url2, "시간외단일가 코스피 하락");
        String url3 = "http://finance.daum.net/quote/timeout.daum?sign=0&col=outvol&stype=P";
        createHTMLFile(url3, "시간외단일가 코스피 체결량상위");
        String url4 = "http://finance.daum.net/quote/timeout.daum?sign=2&order=desc&stype=Q";
        createHTMLFile(url4, "시간외단일가 코스닥 상승");
        String url5 = "http://finance.daum.net/quote/timeout.daum?sign=5&order=asc&stype=Q";
        createHTMLFile(url5, "시간외단일가 코스닥 하락");
        String url6 = "http://finance.daum.net/quote/timeout.daum?sign=0&col=outvol&stype=Q";
        createHTMLFile(url6, "시간외단일가 코스닥 체결량상위");
    }

    public void createHTMLFile(String url, String title) {

        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            doc.select("tr .td_line").remove();
            doc.select("tr .td_top").remove();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.KOREAN);
            String strYear = sdf.format(new Date());
            String strDate = doc.select(".targetDate b").text();

            strDate = strYear + "." + strDate;
            logger.log(Level.INFO, "date:" + strDate);

            try (FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html")) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append("<html lang='ko'>\r\n");
                sb1.append("<head>\r\n");
                //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
                sb1.append("<style>\r\n");
                sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
                sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
                sb1.append("    .stUp,.cUp {color:#d80200;}\r\n");
                sb1.append("    .stDn, .cDn {color:#005dde;}\r\n");
                sb1.append("</style>\r\n");
                sb1.append("</head>\r\n");
                sb1.append("<body>\r\n");
                sb1.append("<h4>[" + strDate + "]</h4>");
                sb1.append("<h2>" + title + "</h2>");
                sb1.append("<div style='width:548px'>\r\n");

                String strContent = doc.select(".dTable").outerHtml();
                strContent = strContent.replaceAll("\"/item", "\"http://finance.daum.net/item");

                sb1.append(strContent + "\r\n");

                sb1.append("</div>\r\n");
                sb1.append("</body>\r\n");
                sb1.append("</html>\r\n");
                fw.write(sb1.toString());
            }
        } catch (IOException e) {
        }
    }
}
