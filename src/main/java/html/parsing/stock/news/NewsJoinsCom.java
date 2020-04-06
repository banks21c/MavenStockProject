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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class NewsJoinsCom extends News {

    private static Logger logger = LoggerFactory.getLogger(NewsJoinsCom.class);

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
        new NewsJoinsCom(1);
    }

    NewsJoinsCom() {

    }

    NewsJoinsCom(int i) {


        String url = JOptionPane.showInputDialog("중앙일보 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (StringUtils.defaultString(url).equals("")) {
            url = "http://news.joins.com/article/21126593";
        }
        if (url != null && !url.equals("")) {
            createHTMLFile(url);
        }
    }

    public static StringBuilder createHTMLFile(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }
        getURL(url);

        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            doc = Jsoup.parse(doc.html().replaceAll("data-src", "src"));
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
            strTitle = doc.select(".article_head .subject h1").html();
            System.out.println("title:" + strTitle);
            if (strTitle.equals("")) {
                strTitle = doc.select("#article_title").text();
            }
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            Elements strDates = doc.select(".article_head .byline em");
            System.out.println("strDate:" + strDates);
            if (strDates.size() > 0) {
                strDate = strDates.get(1).text();
            } else {
                strDate = doc.select(".artical_date .i_date").text();
            }
            strDate = strDate.replaceAll("입력 ", "");
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            String author = doc.select(".journalist_area a").text();
            System.out.println("author:" + author);

            Elements article = doc.select("#article_body");
	    article.select("img").removeAttr("alt");
	    article.select(".ab_related_article").remove();
            article.attr("style", "font-size:14px;");

            Elements divs = article.select("div");
            String width = "";
            for (int i = 0; i < divs.size(); i++) {
                if (divs.get(i).hasAttr("style")) {
                    String divStyle = divs.get(i).attr("style");
                    System.out.println("divStyle1 :" + divStyle);
                    String styles[] = null;
                    styles = divStyle.split(";");
                    if (styles != null) {
                        for (int j = 0; j < styles.length; j++) {
                            String style1 = styles[j];
                            String widthStyleArr[] = null;
                            if (style1.contains("width")) {
                                System.out.println("style1###:" + style1);
                                widthStyleArr = style1.split(":");
                                if (widthStyleArr.length > 1) {
                                    width = widthStyleArr[1];
                                System.out.println("width###:" + width);
                                    width = width.replaceAll("px", "");
                                    width = width.replaceAll(";", "");
                                    width = width.trim();
                                    int iWidth = Integer.parseInt(width);
                                    if (iWidth > 548) {
                                        styles[j] = "width:548px";
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("styles :" + styles);
                    if (styles != null) {
                        divStyle = String.join(",", styles);
                    }
                    System.out.println("divStyle2 :" + divStyle);
                    divs.get(i).attr("style", divStyle);
                }
            }

            System.out.println("article:[" + article + "]");
            article.attr("style", "width:548px");
            String articleHtml = article.outerHtml();
            System.out.println("articleHtml:[" + articleHtml + "]");
            String strContent = articleHtml.replaceAll("640px", "548px");
            strContent = strContent.replaceAll("class=\"caption\"", "style=\"font-size:12px;font-weight:bold;\"");
            strContent = strContent.replaceAll("data-src", "src");
            strContent = strContent.replaceAll("<figure>", "<div>");
            strContent = strContent.replaceAll("</figure>", "</div>");
            strContent = strContent.replaceAll("<figcaption>", "<div>");
            strContent = strContent.replaceAll("</figcaption>", "</div>");
            strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            String copyright = doc.select(".copy_2011 .csource").outerHtml();
            System.out.println("copyright:" + copyright);

            sb1.append("<!doctype html>\r\n");
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
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
            System.out.println(sb1.toString());

            File dir = new File(userHome + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = "";
//            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
//            FileWriter fw = new FileWriter(fileName);
//            fw.write(sb1.toString());
//            fw.close();
//
//            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
//            fw = new FileWriter(fileName);
//            fw.write(sb1.toString());
//            fw.close();

            fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
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
