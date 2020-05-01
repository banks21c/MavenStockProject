package html.parsing.stock.news;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class NewsBizKhanCoKr extends News {

    private static Logger logger = LoggerFactory.getLogger(NewsBizKhanCoKr.class);

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
        new NewsBizKhanCoKr(1);
    }

    NewsBizKhanCoKr() {

    }

    NewsBizKhanCoKr(int i) {


        String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (StringUtils.defaultString(url).equals("")) {
            url = "http://www.hani.co.kr/arti/society/society_general/779348.html?_fr=mt2";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        getURL(url);
        Document doc;
        StringBuilder sb1 = new StringBuilder();

        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            URL u = new URL(url);
            String protocol = u.getProtocol();
            System.out.println("protocol:" + protocol);
            String host = u.getHost();
            System.out.println("host1:" + host);
            String path = u.getPath();
            System.out.println("path:" + path);

            doc = StockUtil.getUrlDocument(url);
            //광고제거
            doc.select(".article_bottom_ad").remove();

            Elements pElements = doc.select("p");
            for (Element pElement : pElements) {
                String styleAttr = pElement.attr("class");
                if (styleAttr != null && styleAttr.contains("content_text")) {
                    pElement.attr("style", "margin:10px 0px;");
                }
            }

            Elements divElements = doc.select("div");
            for (Element divElement : divElements) {
                String styleAttr = divElement.attr("style");
                String beforeString = "";
                String afterString = "";
                if (styleAttr != null && styleAttr.contains("width")) {
                    beforeString = styleAttr.substring(0, styleAttr.indexOf("width"));
                    String width1 = styleAttr.substring(styleAttr.indexOf("width"));
                    String width2 = width1.substring(0, width1.indexOf(";"));
                    afterString = width1.substring(width1.indexOf(";") + 1);
                    String width = width2.split(":")[1];
                    width = width.replaceAll("px", "").trim();

                    int iWidth = 0;
                    int changeWidth = 0;
                    System.out.println("width:" + width);
                    if (width != null && !width.equals("")) {
                        iWidth = Integer.parseInt(width);
                        if (iWidth > 548) {
                            changeWidth = 548;
                        } else {
                            changeWidth = iWidth;
                        }
                    }
                    String strChangeWidth = "width:" + changeWidth + "px";
                    divElement.attr("style", beforeString + changeWidth + ";" + afterString);
                }
            }

            System.out.println("path===>" + path);
            System.out.println("title===>" + doc.select("h1#articleTtitle"));
            //strTitle = doc.select("h1#article_title").get(0).text();
            strTitle = doc.select("h1#articleTtitle").get(0).text();
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
            //strDate = doc.select("div#byline em").text();
            strDate = doc.select("div#bylineArea em").text().trim();
            strDate = strDate.replaceAll("등록", "").trim();
            strDate = strDate.replaceAll("입력 : ", "").trim();
            strDate = strDate.replaceAll("입력", "").trim();
            if (strDate.contains("수정")) {
                strDate = strDate.substring(0, strDate.indexOf("수정"));
            }
            System.out.println("strDate:" + strDate);
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            //String strAuthor = doc.select(".art_header .subject .name a ").text();
            String strAuthor = doc.select(".view_header .subject .name a").text();

            Elements articles = doc.select("div.art_body");
            Element article = null;
            System.out.println("article1:" + article);
            if (articles.size() <= 0) {
                article = doc.select("div.article").get(0);
            } else {
                article = articles.get(0);
            }
            System.out.println("article2:" + article);
            // article.select(".image-area").append("<br><br>");
            article.select(".image-area").after("<br><br>");

            // System.out.println("imageArea:"+article.select(".image-area"));
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String copyright = doc.select(".art_copyright").text();

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
            sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
            sb1.append("<span style='font-size:12px'>").append(strAuthor).append("</span><br><br>\n");
            sb1.append(strContent).append("\n");
            sb1.append("<br>\n");
            sb1.append(copyright + "\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println(sb1.toString());

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            System.out.println("f:" + f);

            String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());

        } catch (IOException e) {
        } finally {
            System.out.println("추출완료");
        }

        return sb1;
    }

}
