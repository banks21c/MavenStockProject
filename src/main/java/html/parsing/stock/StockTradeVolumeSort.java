package html.parsing.stock;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockTradeVolumeSort {

    
	public final static String USER_HOME = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(StockTradeVolumeSort.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockTradeVolumeSort(1);
    }

    StockTradeVolumeSort() {

    }

    StockTradeVolumeSort(int i) {


        String tradeVolumeURL0 = "http://finance.naver.com/sise/sise_quant.nhn?sosok=0";
        String tradeVolumeURL1 = "http://finance.naver.com/sise/sise_quant.nhn?sosok=1";
        readTradeVolumeURL(tradeVolumeURL0, 0);
        readTradeVolumeURL(tradeVolumeURL1, 1);

    }

    public Hashtable<String, String> readTradeVolumeURL(String url, int div) {

        Document doc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            doc = Jsoup.connect(url).get();

            // System.out.println(doc.html());
            FileWriter fw = new FileWriter(USER_HOME + "\\documents\\new_" + div + ".html", false);

            String html = doc.select(".type_2").outerHtml();
            html = html.replaceAll(
                    "<img src=\"http://imgstock.naver.com/images/images4/ico_up.gif\" width=\"7\" height=\"6\" style=\"margin-right:4px;\" alt=\"상승\">",
                    "<span class=\"sign\"><font color=\"red\">▲</font></span>");

            html = html.replaceAll(
                    "<img src=\"http://imgstock.naver.com/images/images4/ico_up02.gif\" width=\"7\" height=\"11\" style=\"margin-right:4px;\">",
                    "<span class=\"sign\"><font color=\"red\">↑</font></span>");

            html = html.replaceAll(
                    "<img src=\"http://imgstock.naver.com/images/images4/ico_down.gif\" width=\"7\" height=\"6\" style=\"margin-right:4px;\" alt=\"하락\">",
                    "<span class=\"sign\"><font color=\"blue\">▼</font></span>");

            html = html.replaceAll(
                    "<img src=\"http://imgstock.naver.com/images/images4/ico_down02.gif\" width=\"7\" height=\"11\" style=\"margin-right:4px;\">",
                    "<span class=\"sign\"><font color=\"red\">↓</font></span>");

            // System.out.println(html);
            Document doc2 = Jsoup.parse(html);

            Elements edds = doc2.getElementsByTag("tr");
            System.out.println("size:" + edds.size());

            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            StringBuilder sb = new StringBuilder();
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<style>\n");
            sb.append(".number{text-align:right;} \n");
            sb.append(".red{color:red;} \n");
            sb.append(".blue{color:blue;} \n");
            sb.append("</style>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            sb.append("<table border=1 cellpadding=0 cellspacing=0>\n");
            sb.append("<tr>\n");
            while (it.hasNext()) {
                Element e = it.next();
                System.out.println("1:" + e.childNodes().size());
                System.out.println("2:" + e.children().size());
                System.out.println("3:" + e.getAllElements().size());
                System.out.println("4:" + e.getElementsByTag("td").size());

                String sign = "";
                Elements es = e.getElementsByClass("sign");
                if (es != null && es.size() > 0) {
                    Element e1 = es.get(0);
                    if (e1 != null) {
                        sign = e1.text();
                        System.out.println("sign:" + sign);
                    }
                }
                // 테이블 첫 헤더라인을 추출한다.
                // 내용만 추출하고 빈 라인은 건너뛴다.
                if (e.getElementsByTag("td").size() == 0 || e.getElementsByTag("td").size() > 1) {
                    // sb.append(attrName+"\n");
                    Elements tdElements = e.children();
                    System.out.println("========outerhtml start==========");
                    int tdIndex = 0;
                    sb.append("<tr>");
                    for (Element tdElement : tdElements) {
                        // 전일비와 등락률에 +-에 따라 RED,BLUE 색상을 준다.
                        if (tdIndex == 3 || tdIndex == 4) {
                            if (sign.equals("▲") || sign.equals("↑")) {
                                tdElement.addClass("red");
                            }
                            if (sign.equals("▼") || sign.equals("↓")) {
                                tdElement.addClass("blue");
                            }
                        }

                        if (tdIndex == 0 || tdIndex == 1 || tdIndex == 2 || tdIndex == 3 || tdIndex == 4 || tdIndex == 5
                                || tdIndex == 6) {
                            System.out.println(tdElement.outerHtml());
                            String outerHtml = tdElement.outerHtml().replaceFirst("/item",
                                    "http://finance.naver.com/item");
                            sb.append(outerHtml + "\n");
                        }
                        tdIndex++;
                    }
                    sb.append("</tr>");
                    System.out.println("========outerhtml end==========");
                }

            }
            sb.append("</tr>\n");
            sb.append("</table>\n");
            sb.append("</body>\n");
            sb.append("</html>\n");

            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
