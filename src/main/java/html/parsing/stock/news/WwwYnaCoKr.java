package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.StockUtil;

public class WwwYnaCoKr extends News {

	Logger logger = null;
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	static final String userHome = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	public static void main(String[] args) {
		new WwwYnaCoKr(1);
	}

	WwwYnaCoKr() {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
	}

	WwwYnaCoKr(int i) {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
		logger.log(Level.INFO, "YNA뉴스");
		String url = JOptionPane.showInputDialog("YNA뉴스 URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "https://www.yna.co.kr/view/AKR20190302013700001?input=1195p";
		}
		super.getURL(url);
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
//        new News().getURL(url);
		News.getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			strTitle = doc.select(".tit-article").text();
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			String strDate = doc.select(".article-sns-md .share-info .tt em").text();
			strDate = strDate.replaceAll("Posted : ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements article = doc.select(".article");
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");

			String style = article.select(".article").attr("style");
			System.out.println("style:" + style);
			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:548px");

			article.select(".adrs").remove();

			Element authorElement = article.select("p").last();
			String author = "";
			if (authorElement != null) {
				authorElement.html();
			}
			System.out.println("author:[" + author + "]");

			// article.select("img").attr("style", "width:548px");
			article.select(".txt_caption.default_figure").attr("style", "width:548px");
			article.select("p").attr("style", "font-size:16px");
			article.select(".img-info").attr("style", "font-size:12px;font-weight:bold;");

			// System.out.println("imageArea:"+article.select(".image-area"));
			String articleHtml = article.outerHtml();
			System.out.println("articleHtml:[" + articleHtml + "]");
			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<img src=\"//", "<img src=\"" + protocol + "://");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("<figure>", "<div>");
			strContent = strContent.replaceAll("</figure>", "</div>");
			strContent = strContent.replaceAll("<figcaption>", "<div>");
			strContent = strContent.replaceAll("</figcaption>", "</div>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			//System.out.println("strContent:[" + strContent + "]strContent");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			String copyright = "Copyright ⓒ YTN";
			System.out.println("copyright:" + copyright);

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
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
