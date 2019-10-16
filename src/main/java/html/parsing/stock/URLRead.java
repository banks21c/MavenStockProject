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

public class URLRead {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
    String strDate = sdf.format(new Date());

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    DecimalFormat df = new DecimalFormat("###.##");

    public static void main(String[] args) {
        new URLRead();
    }

    URLRead() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        String tag = readURL();
        writeFile(tag);
    }

    public String readURL() {
        Document doc1;
        Document doc2;
        Document doc3 = new Document("");
        try {
            // doc = Jsoup.connect("http://banks.blog.me/220923772370").get();
            // doc = Jsoup.connect("http://blog.naver.com/banks/220923772370?").get();
            doc1 = Jsoup.connect(
                    "http://blog.naver.com/PostView.nhn?blogId=banks&logNo=220923772370&redirect=Dlog&widgetTypeCall=true")
                    .get();
            // doc = Jsoup.connect("http://blog.naver.com/PostList.nhn?blogId=banks").get();
            // System.out.println("doc:"+doc);
            Elements es = doc1.select("#postViewArea a");
            // System.out.println("es:"+es);

            Element olEle = doc1.createElement("ol");
            for (Element e : es) {
                // System.out.println("e:"+e);
                String title = e.text();
                String href = e.attr("href");
                // System.out.println("href:"+href);
                doc2 = Jsoup.connect(href).get();
                // System.out.println("doc2:"+doc2);
                Elements metas = doc2.select("meta");
                String date = "";
                System.out.println("____________________________");
                for (Element meta : metas) {
                    System.out.println("meta:" + meta);
                    String itemprop = meta.attr("itemprop");
                    if (itemprop.equals("datePublished")) {
                        date = meta.attr("content");
                        // System.out.println("date:"+date);
                    }
                }
                System.out.println("____________________________");
                // title = title+"("+date+")";
                title = "[" + date + "] " + title;
                // System.out.println("title:"+title);
                Element ee = e.text(title);
                // doc3.appendChild(ee);

                String eeString = ee.toString();

                Element liEle = doc1.createElement("li");
                Element e1 = liEle.html(eeString);
                olEle.appendChild(e1);
            }
            doc3.appendChild(olEle);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc3.toString();
    }

    public void writeFile(String tag) {
        String title = "파일";
        // File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
            String strDate2 = sdf2.format(new Date());

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
            sb1.append("\t<h2>" + "[" + strDate2 + "] " + title + "</h2>");

            sb1.append(tag);

            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            // System.out.println(sb1.toString());
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
