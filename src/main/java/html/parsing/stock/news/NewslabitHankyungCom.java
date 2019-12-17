package html.parsing.stock.news;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewslabitHankyungCom extends News {

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
        new NewslabitHankyungCom(1);
    }

    NewslabitHankyungCom() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
    }

    NewslabitHankyungCom(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, this.getClass().getSimpleName());
        String url = JOptionPane.showInputDialog("뉴스래빗 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url == null || url.equals("")) {
            url = "http://newslabit.hankyung.com/article/201907154922G";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
        new News().getURL(url);
        System.out.println("url:" + url);
        System.out.println("createHTMLFile protocol:" + protocol);
        System.out.println("createHTMLFile host:" + host);
        System.out.println("createHTMLFile path:" + path);
        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
            doc = Jsoup.connect(url).get();
            System.out.println("view:[" + doc.select(".view") + "]");
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("noscript").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();
            doc.select(".hidden-obj").remove();
            doc.select("#CSCNT").remove();
            doc.select(".news_like").remove();
            doc.select("#spiLayer").remove();
            doc.select(".sns_share").remove();

            strTitle = doc.select(".article_view_txt").text();
            if (strTitle != null && strTitle.equals("")) {
                strTitle = doc.select(".article_top .title").text();
            }
            System.out.println("title:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            Elements writerElements = doc.select(".colm_ist");
            Element writerElement = null;
            String writer = "";
            if (writerElements.size() > 0) {
                writerElement = writerElements.get(0);
                if (writerElement != null) {
                    writer = writerElement.text();
                }
            }

            Elements timeElements = doc.select(".list_input_mod li");
            if (timeElements.size() > 0) {
                Element timeElement = timeElements.get(0);
                timeElement.select("em").remove();
                strDate = timeElement.text();
                if (strDate.startsWith("입력 ")) {
                    strDate = strDate.substring("입력 ".length());
                }
                System.out.println("strDate:" + strDate);
                strFileNameDate = strDate;
            } else {
                timeElements = doc.select(".date-published .num");
                Element timeElement = timeElements.get(0);
                timeElement.select("em").remove();
                strDate = timeElement.text();
                if (strDate.startsWith("입력 ")) {
                    strDate = strDate.substring("입력 ".length());
                }
                System.out.println("strDate:" + strDate);
                strFileNameDate = strDate;
            }
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            // Elements article = doc.select("#newsView");
            Elements article = doc.select("#newsView");
            if (article.size() <= 0) {
                article = doc.select("#newsView");
            }
            // article.select(".image-area").append("<br><br>");
            article.select(".image-area").after("<br><br>");

            String style = article.select("#mArticle").attr("style");
            System.out.println("style:" + style);

            article.removeAttr("style");
            article.removeAttr("class");
            article.attr("style", "width:548px");

            // article.select("img").attr("style", "width:548px");
            article.select(".txt_caption.default_figure").attr("style", "width:548px");
            article.select("ylink").remove();

            // System.out.println("imageArea:"+article.select(".image-area"));
            String strContent = article.html().replaceAll("640px", "548px");
            strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
            strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
            strContent = strContent.replaceAll("figure", "div");
            strContent = strContent.replaceAll("figcaption", "div");
            strContent = StockUtil.makeStockLinkStringByExcel(strContent);

            Elements copyRightElements = doc.select(".news_copyright");
            Element copyRightElement = null;
            String copyRight = "";
            if (copyRightElements.size() > 0) {
                copyRightElement = copyRightElements.first();
                copyRight = copyRightElement.text();
            } else {
                copyRightElements = doc.select("#newsView .copy");
                copyRightElement = copyRightElements.first();
		if(copyRightElement != null){
			copyRight = copyRightElement.text();
		}
            }

			sb1.append("<!doctype html>\r\n");
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
            sb1.append("<span style='font-size:12px'>").append(writer).append("</span><br><br>\n");
            sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
            sb1.append(strContent).append("<br><br>\n");
            sb1.append(copyRight).append("<br><br>\n");
            sb1.append("</div>\r\n");
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println("sb.toString:[" + sb1.toString() + "]");

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

    public static BufferedImage getImage(String src) {
        URL url;
        BufferedImage srcImg = null;
        BufferedImage destImg = null;
        try {
            url = new URL(src);
            srcImg = ImageIO.read(url);
            int width = srcImg.getWidth();
            int height = srcImg.getHeight();
            System.out.println("width1:" + width);
            System.out.println("height1:" + height);
            if (width > height && width > 548) {
                height = (548 * height) / width;
                width = 548;

                destImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g = destImg.createGraphics();
                g.drawImage(srcImg, 0, 0, width, height, null);
            }
            width = destImg.getWidth();
            height = destImg.getHeight();
            System.out.println("width2:" + width);
            System.out.println("height2:" + height);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return destImg;
    }

}
