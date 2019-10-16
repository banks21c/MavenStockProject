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

public class NewsYonhap extends News {

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
        new NewsYonhap(1);
    }

    NewsYonhap() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NewsYonhap(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, "다음뉴스");
        String url = JOptionPane.showInputDialog("연합뉴스 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://www.yonhapnews.co.kr/pyeongchang2018/2018/02/23/5501000000AKR20180223142500007.HTML?template=2085";
        }
        createHTMLFile(url);
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
            doc.select(".hidden-obj").remove();
            doc.select(".optgroup").remove();
            doc.select(".article-ad-gg").remove();
            doc.select(".article-aside").remove();
            doc.select(".view-ad-wrap").remove();
            doc.select(".comment-wrap").remove();
            doc.select(".article-ad-bt").remove();
            doc.select(".link-btn-md").remove();
            doc.select(".sns-share01").remove();
            doc.select(".article-ad-box").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strTitle = doc.select(".contents .title-article01 h1").text();
            if (strTitle.equals("")) {
                strTitle = doc.select("#newstitle").text();
                if (strTitle.equals("")) {
                    strTitle = doc.select(".tit-article").text();
                }
            }

            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            String strDate = doc.select(".contents .title-article01 .update-time").text();
            System.out.println("strDate:" + strDate);
            if (strDate.equals("")) {
                strDate = doc.select(".adrs .pblsh").text();
                strDate = strDate.replaceAll("송고", "").trim();
            }
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Elements article = doc.select(".article");
            String strArticle = article.toString();
            if (strArticle.equals("")) {
                article = doc.select(".article_contents");
            }
            // article.select(".image-area").append("<br><br>");
            article.select(".image-area").after("<br><br>");

            String style = article.select("#mArticle").attr("style");
            System.out.println("style:" + style);

            article.removeAttr("style");
            article.removeAttr("class");
            article.attr("style", "width:548px");

            Element copyrightElement = article.select(".adrs .cprgt").first();
            System.out.println("copyrightElement:" + copyrightElement);
            String copyright = copyrightElement.toString();
            System.out.println("copyright:" + copyright);

            article.select(".adrs").remove();

            Element authorElement = article.select("p").last();
            String author = authorElement.html();
            System.out.println("author:[" + author + "]");

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
