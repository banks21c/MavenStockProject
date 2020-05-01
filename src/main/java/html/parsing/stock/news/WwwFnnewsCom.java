package html.parsing.stock.news;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

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

public class WwwFnnewsCom extends News {

    private static Logger logger = LoggerFactory.getLogger(WwwFnnewsCom.class);

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
        new WwwFnnewsCom(1);
    }

    WwwFnnewsCom() {

    }

    WwwFnnewsCom(int i) {


        String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url == null || url.equals("")) {
            url = "http://www.fnnews.com/news/201803241205515030?utm_source=dable";
        }
        createHTMLFile(url);
    }

    public static StringBuilder createHTMLFile(String url) {
//        getURL(url);
        getURL(url);

        StringBuilder sb1 = new StringBuilder();
        Document doc;
        String strTitleForFileName = "";
        String strFileNameDate = "";
        try {
        	TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers(){return new X509Certificate[0];}
                public void checkClientTrusted(X509Certificate[] certs, String authType){}
                public void checkServerTrusted(X509Certificate[] certs, String authType){}
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        	
            doc = Jsoup.connect(url).get();
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

            strTitle = doc.select(".article_head h1").text();
            System.out.println("title2:" + strTitle);
            strTitleForFileName = strTitle;
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            strDate = doc.select(".byline em").get(1).text();
            strDate = strDate.replaceAll("입력 : ", "");
            System.out.println("strDate:" + strDate);

            strFileNameDate = StockUtil.getDateForFileName(strDate);
            strFileNameDate = strFileNameDate.replace(".", "-");
            System.out.println("strFileNameDate:" + strFileNameDate);

            String author = doc.select("#customByline").text();
            System.out.println("author:" + author);
            author = author.replaceAll(" ", "_");
            author = author.replaceAll(":", ".");
            System.out.println("author:" + author);

            Element articleElement = doc.select("#article_content").first();

            articleElement.attr("style", "width:548px");
            articleElement.select("img").attr("style", "width:548px");
            articleElement.select("img").removeAttr("width");
            articleElement.select(".news_photo_table").attr("style", "width:548px");
            articleElement.select(".news_photo_table").removeAttr("width");
            String strContent = articleElement.outerHtml();
            strContent = StockUtil.makeStockLinkString(strContent);

            System.out.println("article:" + strContent);

            String copyright = doc.select(".art_copyright mg").outerHtml();
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
            sb1.append("<span style='font-size:14px'>" + author + "</span><br><br>\n");
            sb1.append("<span style='font-size:14px'>" + strDate + "</span><br><br>\n");
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
