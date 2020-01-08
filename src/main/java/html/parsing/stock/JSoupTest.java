package html.parsing.stock;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupTest {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(JSoupTest.class);

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
        new JSoupTest();
    }

    JSoupTest() {

        readNews("110570", "넥솔론");
    }

    public void readNews(String stockCode, String stockName) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\NewsTest." + strDate + ".html");
            StringBuilder sb1 = new StringBuilder();

            // 종합정보
            // http://finance.naver.com/item/news.nhn?code=246690
            System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + stockCode + "&amp;page=");

            Document doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + stockCode + "&amp;page=")
                    .get();
            // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
            // System.out.println(doc.html());
            // System.out.println(doc.html());
            Element e1 = doc.select(".type2").get(0);

            Elements trs = e1.getElementsByTag("tr");

            sb1.append("<h3>" + stockName + "(" + stockCode + ")</h3>");
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

                // sb1.append("<h3>"+ strTitle +"</h3>\n");
                // sb1.append("<div>"+ strSource+" | "+ strDayTime +"</div>\n");
                doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                Elements link_news_elements = doc.select(".link_news");
                if (link_news_elements != null) {
                    link_news_elements.remove();
                }
                Element view = doc.select(".view").get(0);

                String strView = view.toString();
                strView = strView.replaceAll(stockName, "<a href='http://finance.naver.com/item/main.nhn?code="
                        + stockCode + "'>" + stockName + "</a>");

                sb1.append(strView);
                sb1.append("\n");

            }

            System.out.println(sb1.toString());

            fw.write(sb1.toString());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
