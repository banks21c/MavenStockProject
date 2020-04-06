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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class NewsHuffingtonpost2 extends News {

    private static Logger logger = LoggerFactory.getLogger(NewsHuffingtonpost2.class);

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
        new NewsHuffingtonpost2(1);
    }

    NewsHuffingtonpost2() {

    }

    NewsHuffingtonpost2(int i) {


        String url = JOptionPane.showInputDialog("허핑턴포스트 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (StringUtils.defaultString(url).equals("")) {
            url = "http://www.huffingtonpost.kr/2017/04/19/story_n_16090016.html";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        StringBuilder sb1 = new StringBuilder();
        getURL(url);

        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            System.out.println("doc:" + doc);
            doc.select("iframe").remove();
            doc.select("script").remove();
            strTitle = doc.select("strTitle").text();
            System.out.println("title1:" + strTitle);
            strTitle = doc.select(".strTitle").text();
            System.out.println("title2:" + strTitle);
            strTitle = doc.select(".entry .strTitle").text();
            System.out.println("title3:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strDate = doc.select(".posted time").text();

            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            Element timeElement = doc.select(".posted time").get(0);
            System.out.println("time html:" + timeElement);

            String dateTime = timeElement.html();
            System.out.println("dateTime:" + dateTime);

            String author = doc.select(".author span a").text();
            author = "작성자 " + author;
            System.out.println("author:" + author);

            String main_visual_html = doc.select(".main-visual").outerHtml();
            System.out.println("main_visual_html :" + main_visual_html);

            String article = doc.select("#mainentrycontent").outerHtml();
            Document articleDoc = Jsoup.parse(article);
            articleDoc.select("script").remove();
            articleDoc.select(".hp-slideshow-wrapper").remove();
            articleDoc.select(".float_left").remove();

            String strContent = articleDoc.select("#mainentrycontent").outerHtml();
            System.out.println("content:" + strContent);

            String copyright = doc.select(".copyright").outerHtml();
            System.out.println("copyright:" + copyright);

            strDate = timeElement.childNode(0).toString()
                    .substring(timeElement.childNode(0).toString().indexOf(":") + 1).trim();
            System.out.println("strDate:" + strDate);

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:12px'>" + dateTime + "</span><br><br>\n");
            sb1.append("<span style='font-size:12px'>" + author + "</span><br><br>\n");
            sb1.append(main_visual_html + "\n");
            sb1.append(strContent + "<br><br>\n");
            sb1.append(copyright + "\n");

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
