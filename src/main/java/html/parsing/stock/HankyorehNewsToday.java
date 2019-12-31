package html.parsing.stock;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HankyorehNewsToday {

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
        new HankyorehNewsToday();
    }

    HankyorehNewsToday() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("한겨레 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://www.hani.co.kr/arti/society/society_general/779348.html?_fr=mt2";
        }

        Document doc;
        String strDate = null;
        String title = null;
        try {
            doc = Jsoup.connect(url).get();
            title = doc.select(".article-head .title").text();
            title = title.replaceAll(" ", "_");
            title = title.replaceAll("\\?", "");

            Element timeElement = doc.select(".article-head .date-time span").get(0);
            timeElement.select("em").remove();
            strDate = timeElement.text();
            strDate = strDate.replaceAll(" ", "_");
            strDate = strDate.replaceAll(":", ".");

            System.out.println("time:" + timeElement.text());

        } catch (IOException e) {
            e.printStackTrace();
        }

        writeFile(url, strDate, title);
    }

    public void writeFile(String url, String strDate, String title) {
        System.out.println("url:" + url);
        System.out.println("strDate:" + strDate);
        System.out.println("title:" + title);
        System.out.println(userHome + "\\documents\\" + strDate + "_" + title + ".html");

        try {
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

            // 뉴스 첨부
            sb1.append(getNews(url).toString());

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

    public StringBuilder getNews(String url) {

        StringBuilder sb1 = new StringBuilder();

        // 종합정보
        System.out.println("url:" + url);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        String strDate = sdf.format(new Date());
        System.out.println("날짜:" + strDate);

        Document doc;
        try {

            doc = Jsoup.connect(url).get();

            String title = doc.select(".article-head .title").html();
            String dateTime = doc.select(".article-head .date-time").html();

            Elements article = doc.select(".article-text");
            // article.select(".image-area").append("<br><br>");
            article.select(".image-area").after("<br><br>");

            String style = article.select(".article-text-font-size").attr("style");
            System.out.println("style:" + style);

            article.select(".article-text-font-size").removeAttr("style");
            article.select(".article-text-font-size").get(0).attr("style", "width:548px");
            article.select(".article-text-font-size .imageC").attr("style", "width:548px");
            article.select(".article-text-font-size .imageC .desc").attr("style", "width:548px");

            // System.out.println("imageArea:"+article.select(".image-area"));
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");

            Element time = doc.select(".article-head .date-time span").get(0);
            time.select("em").remove();

            System.out.println("time:" + time.text());

            sb1.append("<h3>[" + time.text() + "] " + title + "</h3>\n");
            sb1.append("<span style='font-size:12px'>" + dateTime + "</span><br><br>\n");
            sb1.append(strContent + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb1;
    }

}
