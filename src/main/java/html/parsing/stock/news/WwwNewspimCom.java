package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class WwwNewspimCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwNewspimCom.class);

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
		new WwwNewspimCom(1);
	}

	public WwwNewspimCom() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public WwwNewspimCom(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://www.newspim.com/news/view/20200425000015";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String strUrl) {
		return createHTMLFile(strUrl, "");
	}

	public StringBuilder createHTMLFile(String strUrl, String strMyComment) {

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		try {
			doc = Jsoup.connect(strUrl).get();
			sb1 = createHTMLFileCommon(doc, strUrl, strMyComment);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
		}
		return sb1;
	}

	public StringBuilder createHTMLFileFromWebView(String strUrl, String strContent, String strMyComment) {

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		try {
			doc = Jsoup.parse(strContent);
			sb1 = createHTMLFileCommon(doc, strUrl, strMyComment);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
		}
		return sb1;
	}

	public StringBuilder createHTMLFileCommon(Document doc, String strUrl, String strMyComment) {
		getURL(strUrl);
		String protocol = getProtocol();
		String host = getHost();
		String path = getPath();
		String protocolHost = getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName = "";
		String strFileNameDate = "";
		doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select("noscript").remove();
		doc.select("body").removeAttr("onload");
		doc.select("div.pop_prt_btns").remove();
		doc.select(".hidden-obj").remove();

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
		strTitle = doc.select(".bodynews .bodynews_title h1").text();
		logger.debug("title:" + strTitle);

		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		String strDate = doc.select(".bodynews .bodynews_title li.writetime").text();
		strDate = strDate.replaceAll("기사입력 : ", "");
		strDate = strDate.replaceAll("년", "-");
		strDate = strDate.replaceAll("월", "-");
		strDate = strDate.replaceAll("일", "");
		logger.debug("strDate:" + strDate);
		strFileNameDate = strDate;
		strFileNameDate = StockUtil.getDateForFileName(strFileNameDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		Elements article = doc.select(".bodynews #news_contents");
		// article.select(".image-area").append("<br><br>");
		article.select(".image-area").after("<br><br>");

		String style = article.select(".article").attr("style");
		logger.debug("style:" + style);

		article.removeAttr("style");
		article.removeAttr("class");
		article.attr("style", "width:741px");

		article.select(".adrs").remove();

		Element authorElement = article.select("p").last();
		String author = "";
		if (authorElement != null) {
			authorElement.html();
		}
		logger.debug("author:[" + author + "]");

		// article.select("img").attr("style", "width:741px");
		article.select(".txt_caption.default_figure").attr("style", "width:741px");
		article.select("p").attr("style", "font-size:16px");
		article.select(".img-info").attr("style", "font-size:12px;font-weight:bold;");

		// logger.debug("imageArea:"+article.select(".image-area"));
		String articleHtml = article.outerHtml();
		logger.debug("articleHtml:[" + articleHtml + "]");
		String strContent = articleHtml.replaceAll("640px", "741px");
		strContent = strContent.replaceAll("<img src=\"//", "<img src=\"" + protocol + "://");
		strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
		strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
		strContent = strContent.replaceAll("<figure>", "<div>");
		strContent = strContent.replaceAll("</figure>", "</div>");
		strContent = strContent.replaceAll("<figcaption>", "<div>");
		strContent = strContent.replaceAll("</figcaption>", "</div>");
		strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
		// logger.debug("strContent:[" + strContent + "]strContent");
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);

		String copyright = "";
		logger.debug("copyright:" + copyright);

		sb1.append("<!doctype html>\r\n");
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
		sb1.append("<span style='font-size:13px'>").append(strDate).append("</span><br><br>\n");
		sb1.append("<span style='font-size:13px'>").append(author).append("</span><br><br>\n");
		sb1.append(strContent).append("<br><br>\n");
		sb1.append(copyright).append("<br><br>\n");
		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug("sb.toString:[" + sb1.toString() + "]");

		File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = "";

		fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());

		return sb1;
	}

}
