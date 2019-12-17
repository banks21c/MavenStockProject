package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;

public class EstateMkCoKr extends News {

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
        new EstateMkCoKr(1);
    }

    EstateMkCoKr() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    EstateMkCoKr(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("News URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://estate.mk.co.kr/news2011/view.php?year=2018&no=675016";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        News gurl = new News();
        gurl.getURL(url);
        String protocol = gurl.getProtocol();
        String host = gurl.getHost();
        String path = gurl.getPath();
        String protocolHost = gurl.getProtocolHost();
	
        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            String userAgent = "Mozilla";
            // This will get you the response.
            Connection.Response res = Jsoup.connect(url).method(Connection.Method.POST).followRedirects(false).userAgent(userAgent).execute();
            // This will get you cookies
            Map<String, String> loginCookies = res.cookies();
            // And this is the easiest way I've found to remain in session
            doc = Jsoup.connect(url).cookies(loginCookies).userAgent(userAgent).get();

            doc.select("iframe").remove();
            doc.select("script").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strTitle = doc.select("#view_tit .head_tit").html();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            strDate = doc.select("#view_tit .sm_num").get(0).text();
            System.out.println("strDate:" + strDate);
            String[] strDates = strDate.split("\\|");
            strDate = strDates[0].trim();
            System.out.println("time html:" + strDate);
            strFileNameDate = strDate;
            strFileNameDate = strFileNameDate.replaceAll("\\.", "-");
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            strFileNameDate = strFileNameDate.split(" ")[0];
            System.out.println("strFileNameDate:" + strFileNameDate);

            // String author = doc.select(".news_title_author a").text();
            // System.out.println("author:" + author);
            Elements article = doc.select("#artText");

            article.select(".img_desc").before("<br>");

            article.select("figure").removeAttr("class");
            article.select("figcaption").removeAttr("class");
            article.select(".footer_btnwrap").remove();
            article.select(".hashtag").remove();
            article.select(".ad").remove();
            article.select("#BpromotionBanner").remove();
            System.out.println(article.select("figure img"));
            article.select("figure img").removeAttr("style");
            article.select("figure img").attr("style", "cursor:pointer;width:548px;height:365px");
            article.select("figure").tagName("div");
            article.select("figcaption").tagName("div");
            // System.out.println("article:" + article);

            article.attr("style", "width:548px");
            String articleHtml = article.outerHtml();
            System.out.println("articleHtml:[" + articleHtml + "]articleHtml");

            String copyright = doc.select(".copy_2011 .csource").outerHtml();
            System.out.println("copyright:" + copyright);

            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<br> <br>", "\n<br>\n<br>");
            // strContent = strContent.replaceAll("<figure class=\"article_image\">", "");
            // strContent = strContent.replaceAll("</figure>", "<br>");
            // strContent = strContent.replaceAll("<figcaption class=\"caption\">", "");
            // strContent = strContent.replaceAll("</figcaption>", "<br>");
            strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            doc.select(".news_date").remove();

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
            sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
            sb1.append(strContent).append("<br><br>\n");
            System.out.println("sb.toString:" + sb1.toString());
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
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
        }
        return sb1;
    }


}
