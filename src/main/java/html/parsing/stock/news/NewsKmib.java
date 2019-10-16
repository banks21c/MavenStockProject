package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
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

public class NewsKmib extends News {

    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";
    static String strDate = null;
    static String strTitle = null;

    DecimalFormat df = new DecimalFormat("###.##");

    /**
     * @param args
     */
    public static void main(String[] args) {
        new NewsKmib(1);
    }

    NewsKmib() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
    }

    NewsKmib(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("국민일보 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://news.kmib.co.kr/article/print.asp?arcid=0011363082";
        }
        if (url != null && !url.equals("")) {
            createHTMLFile(url);
        }
    }

    public static StringBuilder createHTMLFile(String url) {
        new News().getURL(url);

        System.out.println("url:" + url);
        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strTitle = doc.select(".nwsti h2").text();
            System.out.println("title1:" + strTitle);
            if (strTitle.equals("")) {
                strTitle = doc.select(".nwsti h3").text();
            }
            System.out.println("title2:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);

            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements ahrefs = doc.select("a");
            URL u = new URL(url);
            String protocol = u.getProtocol();
            System.out.println("protocol:" + protocol);
            String host = u.getHost();
            System.out.println("host1:" + host);
            String path = u.getPath();
            System.out.println("path:" + path);
            for (Element ahref : ahrefs) {
                String strAhref = ahref.attr("href");
                if (!strAhref.startsWith("http")) {
                    if (strAhref.startsWith("/")) {
                        ahref.attr("href", protocol + "://" + host + strAhref);
                    } else {
                        ahref.attr("href", protocol + "://" + host + path + strAhref);
                    }
                }
            }

            strDate = doc.select(".date .t11").eq(0).text();
            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            String author = "";
            Elements bs = doc.select("b");
            for (Element b : bs) {
                if (b.text().indexOf("@kmib.co.kr") > 0) {
                    author = b.text();
                }
            }
            System.out.println("author:" + author);

            Elements article = doc.select("#article");
            article.attr("style", "width:548px");

            String articleHtml = article.outerHtml();
            System.out.println("articleHtml:" + articleHtml);
            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String copyright = doc.select(".pop_prt_foot").outerHtml();
            System.out.println("copyright:" + copyright);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:13px'>" + strDate + "</span><br><br>\n");
            sb1.append("<span style='font-size:13px'>" + author + "</span><br><br>\n");
            sb1.append(strContent + "<br><br>\n");
            sb1.append(copyright + "<br><br>\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
        }
        return sb1;
    }

}
