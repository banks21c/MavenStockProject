package html.parsing.stock.news;

import html.parsing.stock.util.FileUtil;
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

public class NewsHerald extends News {

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
        new NewsHerald(1);
    }

    NewsHerald() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NewsHerald(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("헤럴드뉴스 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://biz.heraldcorp.com/view.php?ud=20180209000211";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        StringBuilder sb1 = new StringBuilder();
        new News().getURL(url);

        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            strTitle = doc.select(".view_top h1").get(0).text();
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            Element timeElement = doc.select(".view_top .ellipsis").get(0);
            timeElement.select("em").remove();
            strDate = timeElement.text();
            strDate = strDate.replaceAll("기사입력 ", "");
            strFileNameDate = strDate;

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            Elements article = doc.select("#content_ADTOM");
            Elements article0 = doc.select("#articleText");
            System.out.println("article0:" + article0);
            // article0.select(".image-area").append("<br><br>");
            article0.select(".image-area").after("<br><br>");

            article.select("iframe").remove();
            String style = article.select("#articleText").attr("style");
            System.out.println("style:" + style);

            article.select("#articleText").removeAttr("style");
            article.select("#articleText").get(0).attr("style", "width:548px");
            article.select("#articleText .imageC").attr("style", "width:548px");
            article.select("#articleText .imageC .desc").attr("style", "width:548px");

            String orgTableStyles = article.select("table").attr("style");
            System.out.println("orgTableStyles:" + orgTableStyles);
            StringBuilder chgTableStyles = new StringBuilder();
            String tableStylesArray[] = orgTableStyles.split(";");
            String width = "";
            String height = "";
            int iWidth = 0;
            int iHeight = 0;
            for (String tableStyle : tableStylesArray) {
                if (tableStyle != null) {
                    if (tableStyle.trim().startsWith("width")) {
                        String widthStyle = tableStyle;
                        String[] widthArray = widthStyle.split(":");
                        width = widthArray[1].trim().replaceAll("px", "");
                        if (width != null && !width.trim().equals("")) {
                            iWidth = Integer.parseInt(width);
                        }
                    } else if (tableStyle.trim().startsWith("height")) {
                        String heightStyle = tableStyle;
                        String[] heightArray = heightStyle.split(":");
                        height = heightArray[1].trim().replaceAll("px", "");
                        if (height != null && !height.trim().equals("")) {
                            iHeight = Integer.parseInt(height);
                        }
                    } else {
                        chgTableStyles.append(tableStyle + ";");
                    }
                }
            }

            int iWidth2 = 0;
            int iHeight2 = 0;
            System.out.println("iWidth:" + iWidth);
            System.out.println("iHeight:" + iHeight);
            if (iWidth > 0) {
                if (iWidth > 548) {
                    iWidth2 = 548;
                } else {
                    iWidth2 = iWidth;
                }

                if (iHeight > 0 && iWidth > iHeight) {
                    if (iWidth > 548) {
                        iHeight2 = iHeight * 548 / iWidth;
                    } else {
                        iHeight2 = iHeight;
                    }
                }
            }

            if (iWidth2 > 0) {
                chgTableStyles.append("width: " + iWidth2 + "px;");
            }
            if (iHeight2 > 0) {
                chgTableStyles.append("height: " + iHeight2 + "px;");
            }

            System.out.println("chgTableStyles:" + chgTableStyles.toString());
            article.select("table").attr("style", chgTableStyles.toString());

            String orgImgStyles = article.select("table img").attr("style");
            if (!orgImgStyles.equals("")) {
                System.out.println("orgImgStyles:" + orgImgStyles);
                StringBuilder chgImgStyles = new StringBuilder();
                String imgStylesArray[] = orgImgStyles.split(";");
                width = "";
                height = "";
                iWidth = 0;
                iHeight = 0;
                for (String imgStyle : imgStylesArray) {
                    if (imgStyle != null) {
                        if (imgStyle.trim().startsWith("width")) {
                            String widthStyle = imgStyle;
                            String[] widthArray = widthStyle.split(":");
                            width = widthArray[1].trim().replaceAll("px", "");
                            if (width != null && !width.trim().equals("")) {
                                iWidth = Integer.parseInt(width);
                            }
                        } else if (imgStyle.trim().startsWith("height")) {
                            String heightStyle = imgStyle;
                            String[] heightArray = heightStyle.split(":");
                            height = heightArray[1].trim().replaceAll("px", "");
                            if (height != null && !height.trim().equals("")) {
                                iHeight = Integer.parseInt(height);
                            }
                        } else {
                            chgImgStyles.append(imgStyle + ";");
                        }
                    }
                }

                iWidth2 = 0;
                iHeight2 = 0;
                System.out.println("iWidth:" + iWidth);
                System.out.println("iHeight:" + iHeight);
                if (iWidth > 0) {
                    if (iWidth > 548) {
                        iWidth2 = 548;
                    } else {
                        iWidth2 = iWidth;
                    }

                    if (iHeight > 0 && iWidth > iHeight) {
                        if (iWidth > 548) {
                            iHeight2 = iHeight * 548 / iWidth;
                        } else {
                            iHeight2 = iHeight;
                        }
                    }
                }

                if (iWidth2 > 0) {
                    chgImgStyles.append("width: " + iWidth2 + "px;");
                }
                if (iHeight2 > 0) {
                    chgImgStyles.append("height: " + iHeight2 + "px;");
                }

                System.out.println("chgImgStyles:" + chgImgStyles.toString());
                article.select("table img").attr("style", chgImgStyles.toString());
            } else {
                String orgImgWidth = article.select("table img").attr("width");
                String orgImgHeight = article.select("table img").attr("height");
                System.out.println("orgImgWidth:" + orgImgWidth);
                System.out.println("orgImgHeight:" + orgImgHeight);
                StringBuilder chgImgStyles = new StringBuilder();
                width = "";
                height = "";
                iWidth = 0;
                iHeight = 0;

                if (orgImgWidth != null) {
                    iWidth = Integer.parseInt(orgImgWidth);
                }

                if (orgImgHeight != null) {
                    iHeight = Integer.parseInt(orgImgHeight);
                }
                System.out.println("iWidth:" + iWidth);
                System.out.println("iHeight:" + iHeight);

                iWidth2 = 0;
                iHeight2 = 0;
                if (iWidth > 0) {
                    if (iWidth > 548) {
                        iWidth2 = 548;
                    } else {
                        iWidth2 = iWidth;
                    }

                    if (iHeight > 0 && iWidth > iHeight) {
                        if (iWidth > 548) {
                            iHeight2 = iHeight * 548 / iWidth;
                        } else {
                            iHeight2 = iHeight;
                        }
                    }
                }

                if (iWidth2 > 0) {
                    article.select("table img").attr("width", iWidth2 + "");
                }
                if (iHeight2 > 0) {
                    article.select("table img").attr("height", iHeight2 + "");
                }

                System.out.println("img width:" + article.select("table img").attr("width"));
                System.out.println("img height:" + article.select("table img").attr("height"));
            }

            System.out.println("article:" + article);

            article.select("table").removeAttr("style");
            article.select("table").attr("style", "width:548px");

            article.select("table img").attr("style", "width:548px");
            article.select("table img").attr("style", "width:548px");

            // System.out.println("imageArea:"+article0.select(".image-area"));
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            Element time = doc.select(".view_top .ellipsis").get(0);
            time.select("em").remove();
            String strTime = time.text();
            strTime = strTime.replaceAll("기사입력 ", "");

            System.out.println("strTime:" + strTime);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h2>[" + strTime + "] " + strTitle + "</h2>\n");
            sb1.append("<h2>" + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
            sb1.append(strContent + "\n");
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
