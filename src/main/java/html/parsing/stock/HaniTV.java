package html.parsing.stock;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HaniTV {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    DecimalFormat df = new DecimalFormat("###.##");

    StringBuilder sb = new StringBuilder();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new HaniTV(1);
    }

    HaniTV() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    HaniTV(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());

        sb.append("<table>");
        for (int j = 1; j < 12; j++) {
            getList(j);
        }
        sb.append("</table>");
        System.out.println("sb:" + sb);
        writeFile();
    }

    public void getList(int cnt) {
        Document doc;
        try {
            // http://www.hanitv.com/index.php?category=52596&document_srl=127453&page=
            System.out.println("url : http://www.hanitv.com/index.php?category=52596&document_srl=127453&page=" + cnt);
            doc = Jsoup.connect("http://www.hanitv.com/index.php?category=52596&document_srl=127453&page=" + cnt).get();
            System.out.println("doc:" + doc);

            Element list = doc.getElementById("prev_next");
            // System.out.println("list:"+list);
            Elements elems = list.getElementsByTag("a");
            // System.out.println("elems:"+elems);
            for (int i = 0; i < elems.size(); i++) {
                sb.append("<tr>");
                Element elem = elems.get(i);
                System.out.println("elem:" + elem);
                String href = elem.attr("href");
                System.out.println("href:" + href);
                String title = elem.attr("title");
                // System.out.println("title:"+title);

                Document doc2 = Jsoup.connect(href).get();
                // System.out.println("doc2:"+doc2);
                Element iframe = doc2.getElementsByTag("iframe").first();
                // System.out.println("iframe:"+iframe);
                String youtube = iframe.attr("src");
                // System.out.println("youtube:"+youtube);

                String articleTitle = doc2.getElementsByClass("article-title").select("h3").get(0).text();
                System.out.println("articleTitle-" + articleTitle + "-");

                String uploadDate = doc2.getElementsByClass("program-info").select("span").get(1).text();
                System.out.println("uploadDate-" + uploadDate + "-");

                sb.append("<td><a href='" + youtube + "' title='" + articleTitle + "'>" + articleTitle + "</a></td><td>"
                        + uploadDate + "</td>\n");
                sb.append("</tr>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        // File f = new File(userHome + "\\documents\\김어준의파파이스.html");
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\김어준의파파이스.html");
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
            sb1.append("\t<h2>김어준의 파파이스</h2>");
            sb1.append(sb.toString());
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
