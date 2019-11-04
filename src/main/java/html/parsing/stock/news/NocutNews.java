package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import java.io.File;
import java.io.FileWriter;
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

public class NocutNews extends News {

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
        new NewsNocutNews(1);
    }

    NocutNews() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NocutNews(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("Money Today URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://land.hankyung.com/news/app/newsview.php?aid=201802207330H";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        System.out.println("url:" + url);
        new News().getURL(url);

        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            strTitle = doc.select(".news_sbj_h").text();
            if (strTitle != null && strTitle.equals("")) {
                strTitle = doc.select(".article_top .title").text();
            }
            strTitleForFileName = strTitle;
            strFileNameDate = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            FileWriter fw0 = new FileWriter(userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + "_?���?.html");
            fw0.write(doc.html());
            fw0.close();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            Elements writerElements = doc.select(".colm_ist");
            Element writerElement = null;
            String author = "";
            if (writerElements.size() > 0) {
                writerElement = writerElements.get(0);
                if (writerElement != null) {
                    author = writerElement.text();
                }
            }

            //String strDate = content.select(".adrs .pblsh").outerHtml();
            String strDate = doc.select(".tt em").text();
            strFileNameDate = StockUtil.getDateForFileName(strDate);

            // Elements article = doc.select("#newsView");
            Elements article = doc.select(".news_article");
            if (article.size() <= 0) {
                article = doc.select("#newsView");
            }
            // article.select(".image-area").append("<br><br>");
            article.select(".image-area").after("<br><br>");

            String style = article.select("#mArticle").attr("style");
            System.out.println("style:" + style);

            article.removeAttr("style");
            article.removeAttr("class");
            article.attr("style", "width:548px");

            // article.select("img").attr("style", "width:548px");
            article.select(".txt_caption.default_figure").attr("style", "width:548px");

            // System.out.println("imageArea:"+article.select(".image-area"));
            String content = article.html().replaceAll("640px", "548px");
            content = content.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            content = content.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
            content = content.replaceAll("figure", "div");
            content = content.replaceAll("figcaption", "div");
            content = StockUtil.makeStockLinkStringByExcel(content);

            Elements copyRightElements = doc.select(".news_copyright");
            Element copyRightElement = null;
            String copyright = "";
            if (copyRightElements.size() > 0) {
                copyRightElement = copyRightElements.first();
                copyright = copyRightElement.text();
            } else {
                copyRightElements = doc.select("#newsView .copy");
                copyRightElement = copyRightElements.first();
                copyright = copyRightElement.text();
            }

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:12px'>" + author + "</span><br>\n");
            sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
            sb1.append(content + "<br><br>\n");
            sb1.append(copyright + "<br><br>\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = userHome + File.separator + "documents" + File.separator + host + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
        }
        return sb1;
    }

}