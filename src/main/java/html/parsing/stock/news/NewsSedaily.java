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

public class NewsSedaily extends News {

    java.util.logging.Logger logger = null;
    final static String userHome = System.getProperty("user.home");

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
        new NewsSedaily(1);
    }

    NewsSedaily() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NewsSedaily(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, "다음뉴스");
        String url = JOptionPane.showInputDialog("다음뉴스 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://v.media.daum.net/v/20170720073337746";
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
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("ins").remove();
            doc.select(".btn_info").remove();

            strTitle = doc.select(".tit_view").text();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            Element writerElement = doc.select(".info_view .txt_info").get(0);
            String writer = writerElement.text();

            Element timeElement = doc.select(".info_view .txt_info").get(1);
            timeElement.select("em").remove();
            strDate = timeElement.text();
            if (strDate.startsWith("입력 ")) {
                strDate = strDate.substring("입력 ".length());
            }
            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Elements article = doc.select(".news_view");
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
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
            strContent = strContent.replaceAll("<figure>", "<div>");
            strContent = strContent.replaceAll("</figure>", "</div>");
            strContent = strContent.replaceAll("<figcaption>", "<div>");
            strContent = strContent.replaceAll("</figcaption>", "</div>");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            Element copyRightElement = doc.select(".txt_copyright").first();
            String copyright = copyRightElement.text();

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\r\n");
            sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\r\n");
            sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\r\n");
            sb1.append(strContent + "\r\n");
            sb1.append(copyright + "<br>\r\n");

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