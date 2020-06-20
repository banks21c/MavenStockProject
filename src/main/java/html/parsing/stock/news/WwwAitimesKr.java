package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.util.FileUtil;

public class WwwAitimesKr extends News {

    private static Logger logger = LoggerFactory.getLogger(WwwAitimesKr.class);

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
        new WwwAitimesKr(1);
    }

    WwwAitimesKr() {

    }

    WwwAitimesKr(int i) {

        String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (StringUtils.defaultString(url).equals("")) {
            url = "http://www.aitimes.kr/news/articleView.html?idxno=13853";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        System.out.println("url:" + url);
        getURL(url);

        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            doc.select("iframe").remove();
            doc.select("script").remove();

            strTitle = doc.select(".article-view-header .article-header-wrap .article-head-title").text();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            String writer = doc.select(".article-view-header .info-text li:first-child").text();
            writer = writer.replace("자세히보기", "");
            System.out.println("writer:" + writer);

//            Elements timeElements = doc.select(".byline em");
            strDate = doc.select(".article-view-header .info-text li").get(1).text();
            strDate = strDate.replace("승인", "").trim();
            System.out.println("strDate:" + strDate);
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Elements article = doc.select("#article-view-content-div");
            System.out.println("article:[" + article + "]");
            article.select(".image-area").after("<br><br>");

            String style = article.select("#mArticle").attr("style");
            System.out.println("style:" + style);

            article.removeAttr("style");
            article.removeAttr("class");
            article.attr("style", "width:548px");

            // System.out.println("imageArea:"+article.select(".image-area"));
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
            strContent = strContent.replaceAll("figure", "div");
            strContent = strContent.replaceAll("figcaption", "div");
            strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);

            Element copyRightElement = doc.select(".cont_left_article .reporter_copy_w_2").first();
            String copyRight = "";
            if (copyRightElement != null) {
                copyRight = copyRightElement.text();
            }
            System.out.println("copyRight:" + copyRight);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            // sb1.append("<meta http-equiv=\"Content-Type\"
            // content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
            sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
            sb1.append(strContent + "\n");
            sb1.append(copyRight);
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
                    + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
                    + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
        }
        return sb1;
    }

}
