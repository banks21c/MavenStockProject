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

public class WwwSisainCoKr extends News {

    private static Logger logger = LoggerFactory.getLogger(WwwSisainCoKr.class);

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
        new WwwSisainCoKr(1);
    }

    WwwSisainCoKr() {

    }

    WwwSisainCoKr(int i) {


        String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (StringUtils.defaultString(url).equals("")) {
            url = "http://www.sisain.co.kr/?mod=news&act=articleView&idxno=28743";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        getURL(url);

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

            Elements metaElements = doc.select("meta");
            for (Element e : metaElements) {
                if (e.attr("property").equals("og:title")) {
                    strTitle = e.attr("content");
                }
            }
            System.out.println("title2:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements arl_view_writer = doc.select(".arl_view_writer");
            System.out.println("arl_view_writer1:" + arl_view_writer);
            Element aElements = arl_view_writer.select("a").first();
            String email = aElements.text();
            System.out.println("email:" + email);
            arl_view_writer.select("a").remove();
            System.out.println("arl_view_writer2:" + arl_view_writer);

            strDate = arl_view_writer.select("span span").first().text();
            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);
            arl_view_writer.select("span span").remove();
            System.out.println("arl_view_writer3:" + arl_view_writer);
            String author = arl_view_writer.text();
            System.out.println("author:" + author);
            author = author.replaceAll(" ", "_");
            author = author.replaceAll(":", ".");
            System.out.println("author:" + author);

            Elements divs = doc.select("div");

            String strContent = "";
            for (Element e : divs) {
                String itemprop = e.attr("itemprop");
                if (itemprop.equals("articleBody")) {
                    e.attr("style", "width:548px");
                    e.select("img").attr("style", "width:548px");
                    e.select("img").removeAttr("width");
                    e.select(".news_photo_table").attr("style", "width:548px");
                    e.select(".news_photo_table").removeAttr("width");
                    strContent = e.outerHtml();
                }
            }
            System.out.println("strContent:" + strContent);
            strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);

            String copyright = doc.select(".arl_view_copywriter").outerHtml();
            System.out.println("copyright:" + copyright);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:14px'>" + author + "</span><br><br>\n");
            sb1.append("<span style='font-size:14px'>" + strDate + "</span><br><br>\n");
            sb1.append(strContent + "<br><br>\n");
            sb1.append(copyright + "<br><br>\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println(sb1.toString());

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
