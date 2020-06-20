package html.parsing.stock.news;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class WwwMediatodayCoKr extends News {

    private static Logger logger = LoggerFactory.getLogger(WwwMediatodayCoKr.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.MM.SS.sss ", Locale.KOREAN).format(new Date());
    static String strDate = null;
    static String strTitle = null;

    DecimalFormat df = new DecimalFormat("###.##");

    /**
     * @param args
     */
    public static void main(String[] args) {
        new WwwMediatodayCoKr(1);
    }

    WwwMediatodayCoKr() {

    }

    WwwMediatodayCoKr(int i) {


        String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url != null && url.equals("")) {
            url = "http://www.mediatoday.co.kr/?mod=news&act=articleView&idxno=142213";
            url = "http://www.mediatoday.co.kr/?mod=news&act=print&idxno=142213";
            url = "http://www.mediatoday.co.kr/?mod=news&act=articleView&idxno=141983&utm_source=dable";
            url = "http://www.mediatoday.co.kr/?mod=news&act=articleView&idxno=142299";
            createHTMLFile(url);
        }
    }

    public static StringBuilder createHTMLFile(String url) {
//        if (url.contains("?")) {
//            url = url.substring(0, url.indexOf("?"));
//        }
//        getURL(url);

        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        String strAuthor = "";
        String strDescription = "";
        try {
            Connection con = Jsoup.connect(url);
            doc = con.get();
            //doc = Jsoup.connect(url).get();
            System.out.println("doc:[" + doc + "]");

            String fileName2 = userHome + File.separator + "documents" + File.separator + strYMD + ".html";
            System.out.println("fileName2:" + fileName2);
            Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName2, true), StandardCharsets.UTF_8));
            bw.write(doc.html());
            bw.close();

            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();
            doc.select("#header").remove();
            doc.select(".not_print").remove();

            Elements metas = doc.select("meta");
            for (Element meta : metas) {
                String name = meta.attr("property");
                if (name.equals("og:title")) {
                    strTitle = meta.attr("strContent");
                } else if (name.equals("og:description")) {
                    strDescription = meta.attr("strContent");
                } else if (name.equals("dable:author")) {
                    strAuthor = meta.attr("strContent");
                }
            }

            strDate = doc.select(".arl_view_date").text();
            System.out.println("strDate:" + strDate);
            System.out.println("arl_view_writer_box:" + doc.select(".arl_view_writer_box"));

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
            if (strTitle.equals("")) {
                strTitle = doc.select(".arl_view_box .arl_view_title").text();
            }
            strTitleForFileName = strTitle;

            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            strFileNameDate = strDate;
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Elements article = doc.select("#talklink_strContents");
            System.out.println("article:" + article);
            if (article.toString().equals("")) {
                article = doc.select("#arl_view_content");
            }
            article.attr("style", "width:548px");
            String articleHtml = article.outerHtml();

            System.out.println("articleHtml:" + articleHtml);
            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String strCopyright = doc.select(".arl_view_copywriter").outerHtml();
            System.out.println("strCopyright:" + strCopyright);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" strContent=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:14px'>" + strDate + "</span><br><br>\n");
            sb1.append("<span style='font-size:14px'>" + strAuthor + "</span><br><br>\n");
            sb1.append(strContent + "<br><br>\n");
            sb1.append(strCopyright + "<br><br>\n");
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
