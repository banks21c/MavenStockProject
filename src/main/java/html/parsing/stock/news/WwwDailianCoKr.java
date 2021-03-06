package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class WwwDailianCoKr extends News implements NewsInterface {

	Logger logger = null;
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	static final String USER_HOME = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = "";
	static String strTitle = "";
	static String strSubTitle = "";

	public static void main(String[] args) {
		new WwwDailianCoKr(1);
	}

	WwwDailianCoKr() {

	}

	WwwDailianCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://www.dailian.co.kr/news/view/850199";
		}
		super.getURL(url);
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
//        getURL(url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
			doc.select("iframe").parents().select("div").removeAttr("style");
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
			strTitle = doc.select(".body .contents .news-contents h2").text();
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);
			strSubTitle = "";
			System.out.println("strSubTitle:" + strSubTitle);

			String strDate = doc.select(".body .contents .news-contents .information ul:first-child li:first-child")
					.text();
			strDate = strDate.replace("[데일리안] 입력 ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			strFileNameDate = strFileNameDate.replace(".", "-");

			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements article = doc.select(".article");
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");

			String style = article.select(".article").attr("style");
			System.out.println("style:" + style);
			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");

			article.select(".adrs").remove();

			Elements authorElements = article.select(".view_reporter_2017");
			authorElements.select("label").remove();
			String author = "";
			if (authorElements != null && authorElements.size() > 0) {
				author = authorElements.text();
			}
			System.out.println("author:[" + author + "]");

			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");
			article.select("p").attr("style", "font-size:16px");
			article.select(".img-info").attr("style", "font-size:12px;font-weight:bold;");

			// System.out.println("imageArea:"+article.select(".image-area"));
			String articleHtml = article.outerHtml();
			System.out.println("articleHtml:[" + articleHtml + "]");
			String strContent = articleHtml.replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<img src=\"//", "<img src=\"" + protocol + "://");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("<figure>", "<div>");
			strContent = strContent.replaceAll("</figure>", "</div>");
			strContent = strContent.replaceAll("<figcaption>", "<div>");
			strContent = strContent.replaceAll("</figcaption>", "</div>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			// System.out.println("strContent:[" + strContent + "]strContent");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			String copyright = "";
			System.out.println("copyright:" + copyright);

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");
			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:13px'>" + strDate + "</span><br><br>\n");
			sb1.append("<span style='font-size:13px'>" + author + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
			sb1.append(copyright + "<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
