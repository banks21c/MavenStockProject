package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
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
import org.jsoup.select.Elements;

public class NewsChosun extends News {

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
        new NewsChosun(1);
    }

    NewsChosun() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NewsChosun(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("조선일보 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url == null || url.equals("")) {
            url = "http://news.chosun.com/site/data/html_dir/2017/02/24/2017022401428.html";
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

            strTitle = doc.select("#news_title_text_id").html();
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);

            strDate = doc.select(".news_body .news_date").text();
            System.out.println("strDate:" + strDate);
            String[] strDates = strDate.split("\\|");
            strDate = strDates[0].trim();
            strDate = strDate.replaceAll("입력 ", "");
            strDate = strDate.replaceAll("  ", "");

            doc.select(".news_date").remove();

            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            String author = doc.select(".news_title_author a").text();
            System.out.println("author:" + author);

            Elements article = doc.select("#news_body_id");
            article.select(".news_like").remove();
            // System.out.println("article:" + article);

            article.attr("style", "width:548px");
            String articleHtml = article.outerHtml();
            System.out.println("articleHtml:[" + articleHtml + "]articleHtml");

            String copyright = doc.select(".csource").outerHtml();
            System.out.println("copyright:" + copyright);

            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<figure>", "");
            strContent = strContent.replaceAll("</figure>", "<br>");
            strContent = strContent.replaceAll("<figcaption>", "");
            strContent = strContent.replaceAll("</figcaption>", "<br>");
            strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
            sb1.append("<span style='font-size:12px'>").append(author).append("</span><br><br>\n");
            sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
            sb1.append(strContent).append("<br><br>\n");
            sb1.append(copyright).append("<br><br>\n");
            System.out.println("sb.toString:" + sb1.toString());
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
