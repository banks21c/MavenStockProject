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

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class WwwInews24Com extends News implements NewsInterface {

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
	static String strWriter = "";
	static String strContent = "";

	public static void main(String[] args) {
		new WwwInews24Com(1);
	}

	public WwwInews24Com() {

	}

	public WwwInews24Com(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.inews24.com/view/1285088";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String strUrl) {
		return createHTMLFile(strUrl, "");
	}

	public StringBuilder createHTMLFile(String strUrl, String strMyComment) {

		getURL(strUrl);
		System.out.println("url:" + strUrl);
		System.out.println("createHTMLFile protocol:" + protocol);
		System.out.println("createHTMLFile host:" + host);
		System.out.println("createHTMLFile path:" + path);
		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		String strFileName;
		try {
			doc = Jsoup.connect(strUrl).get();
			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();
			doc.select(".news_domino").remove();
			doc.select(".hashtags").remove();
			doc.select(".reporter_sns").remove();
			doc.select(".tex_l_box").remove();
			doc.select(".tex_s_box").remove();
			doc.select(".issue_more").remove();
			doc.select("table").attr("width", "741");

			strTitle = doc.select("header.view > h1").text();
			strSubTitle = doc.select("header.view > h1").html();
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElement = doc.select("article.view address a");
			strWriter = writerElement.get(1).text();

			Elements timeElement = doc.select("header.view > time");
			System.out.println("timeElement:" + timeElement);
			timeElement.select("em").remove();
			strDate = timeElement.text();
			System.out.println("strDate:" + strDate);
			strDate = strDate.replaceAll("입력 ", "").trim();
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);
			Elements article = doc.select("article.view");
			article.select(".photo-list").remove();
			article.select(".related").remove();
			article.select("ad").remove();
			article.select("aside").remove();

			if (article.isEmpty()) {
				System.out.println("article is empty...");
			}
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");
			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);
			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");
			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");
			// System.out.println("imageArea:"+article.select(".image-area"));
			strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			
			// Elements copyrightElement = doc.select(".txt_copyright");
			// String copyright = copyrightElement.text();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");
			sb1.append("<h3> 기사주소:[<a href='").append(strUrl).append("' target='_sub'>").append(strUrl)
				.append("</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append(strSubTitle).append("\r\n");
			sb1.append("<span style='font-size:12px'>").append(strWriter).append("</span><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("\n");
			// sb1.append(copyright);
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			strFileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
			FileUtil.fileWrite(strFileName, sb1.toString());

			strFileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
			FileUtil.fileWrite(strFileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
