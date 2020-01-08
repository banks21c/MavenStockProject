package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JOptionPane;

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
import html.parsing.stock.StockUtil;
import html.parsing.stock.StockVO;
import html.parsing.stock.util.FileUtil;

public class NewsBizHeraldcorpCom extends News {

    private static Logger logger = LoggerFactory.getLogger(NewsBizHeraldcorpCom.class);

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
        new NewsBizHeraldcorpCom(1);
    }

    NewsBizHeraldcorpCom() {

    }

    NewsBizHeraldcorpCom(int i) {


        String url = JOptionPane.showInputDialog("헤럴드경제 URL을 입력하여 주세요.");
        System.out.println("url:[" + url + "]");
        if (url.equals("")) {
            url = "http://biz.heraldcorp.com/view.php?ud=20190226000063";
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
            doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
            doc.select("iframe").remove();
            doc.select("script").remove();
            doc.select("body").removeAttr("onload");
            doc.select("div.pop_prt_btns").remove();
            doc.select(".w_mug_emotion").remove();

            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

	    List<StockVO> stockList = StockUtil.readAllStockCodeNameListFromExcel();
            strTitle = doc.select(".view_top_t2 ul li h1").text();
            System.out.println("title2:" + strTitle);
            strTitleForFileName = strTitle;
            strTitle = StockUtil.stockTitleLinkString(strTitle,stockList);
            System.out.println("strTitle##:" + strTitle);
            strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
            System.out.println("strTitleForFileName:" + strTitleForFileName);

            String strAuthor = "";
	    Elements pElements = doc.select("p");
	    for(Element e : pElements){
		    if(e.text().contains("[헤럴드경제=")){
			    strAuthor = e.text().substring(0,e.text().indexOf("]")+1);
		    }
	    }
            System.out.println("strAuthor:[" + strAuthor + "]");

            String strEmail = "";
            System.out.println("strEmail:[" + strEmail + "]");

            String strDates = "";
            Elements ellipsisElements = doc.select(".ellipsis");
	    for(Element e : ellipsisElements){
		    if(e.text().contains("기사입력")){
			    strDates = e.text();
		    }
	    }
            strDate = strDates.split("\\|")[0];
            strDate = strDate.replaceAll("기사입력", "").trim();
            strDate = strDate.replaceAll("입력", "").trim();
            strDate = strDate.replaceAll("기사", "").trim();
            System.out.println("strDate:" + strDate);
            strFileNameDate = strDate;
            strFileNameDate = StockUtil.getDateForFileName(strDate);
            System.out.println("strFileNameDate:" + strFileNameDate);

            String img = "";
            System.out.println("img:" + img);
            String strContent = doc.select("#articleText").outerHtml();
            strContent = StockUtil.stockLinkString(strContent,stockList);
            System.out.println("article:" + strContent);

            String copyright = "";
            System.out.println("copyright:" + copyright);

            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");

            sb1.append(StockUtil.getMyCommentBox());

            sb1.append("<div style='width:548px'>\r\n");

            sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
            sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
            sb1.append("<span style='font-size:14px'>" + strAuthor + "</span><br><br>\n");
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
            File f = new File(userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html");
            System.out.println("f:" + f);

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
