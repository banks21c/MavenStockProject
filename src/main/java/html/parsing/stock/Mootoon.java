package html.parsing.stock;

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

public class Mootoon {

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
        new Mootoon();
    }

    Mootoon() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, "무툰 URL");
        String url = JOptionPane.showInputDialog("무툰 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://www.mootoon.co.kr/comic/moo_view.mg?tcode=iixl&cuid=p9%2Br5MKX0G4%3D&isview=R&in_id=qbmrnsLNwmGdpqio";
        }

        writeFile(url);
    }

    public StringBuilder writeFile(String url) {

        StringBuilder sb1 = new StringBuilder();

        // 종합정보
        System.out.println("url:" + url);

        Document doc;
        try {

            doc = Jsoup.connect(url).get();
            System.out.println("doc:" + doc);
            String title = doc.select(".wktit").text();
            title = title.replaceAll(" ", "_");
            title = title.replaceAll("\"", "'");
            System.out.println("title:" + title);
            String headTitle = doc.select(".pl-header-title").text();
            String publisher = "";

            String list = doc.select("#pl-video-table").outerHtml();
            doc = Jsoup.parse(list);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append("<h1><a href=\"" + url + "\">" + headTitle + "</a></h1>\n");
            sb1.append("<h3>" + publisher + "</h3>\n");

            sb1.append("<table>\n");
            sb1.append("<colgroup>\n");
            sb1.append("<col width='5%'/>\n");
            sb1.append("<col width='*'/>\n");
            sb1.append("<col width='23%'/>\n");
            sb1.append("</colgroup>\n");
            Elements trs = doc.select("tr");
            int cnt = 0;
            for (Element tr : trs) {
                if (cnt == 0) {
                    System.out.println(":::::::::::::::::::::::::::::::::::::");
                    System.out.println("tr:" + tr);
                    System.out.println(":::::::::::::::::::::::::::::::::::::");
                }
                String dataTitle = tr.attr("data-title");
                if (dataTitle.equals("[비공개 동영상]")) {
                    continue;
                }
                cnt++;
                sb1.append("<tr>\n");
                System.out.println("dataTitle:" + dataTitle);
                Elements as = tr.select("a");
                System.out.println("as:" + as);
                String titleHref = tr.select("a").get(0).attr("href");
                if (titleHref.startsWith("/watch")) {
                    titleHref = "https://www.youtube.com" + titleHref;
                    titleHref = titleHref.replaceAll("&amp;", "&");
                }
                String index = "";
                String hrefArray[] = titleHref.split("&");
                for (int i = 0; i < hrefArray.length; i++) {
                    String paramArray[] = hrefArray[i].split("=");
                    if (paramArray != null && paramArray.length > 0) {
                        if (paramArray[0].equals("index")) {
                            index = paramArray[1];
                            System.out.println("index:[" + index + "]");
                            // sb1.append("<td style='text-align:right'>"+index+"</td>");
                        }
                    }
                }
                sb1.append("<td style='text-align:right'>" + cnt + "</td>");
                System.out.println("titleHref:" + titleHref);
                System.out.println("<a href='" + titleHref + "'>" + dataTitle + "</a>");
                sb1.append(
                        "<td style='text-overflow: ellipsis'><a href='" + titleHref + "'>" + dataTitle + "</a></td>\n");
                String timestamp_label = tr.select(".timestamp span").attr("aria-label");
                String timestamp = tr.select(".timestamp span").text();
                sb1.append("<td align='right'>" + timestamp_label + "(" + timestamp + ")</td>\n");
                sb1.append("</tr>\n");
            }
            sb1.append("</table>\n");
            // sb1.append(list + "<br><br>\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            System.out.println(sb1.toString());

            fw.write(sb1.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb1;
    }

}
