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

public class WwwEtodayCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(WwwEtodayCoKr.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WwwEtodayCoKr(1);
	}

	public WwwEtodayCoKr() {
	}

	public WwwEtodayCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.etoday.co.kr/news/section/newsview.php?idxno=1600693";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String strUrl) {
		return createHTMLFile(strUrl, "");
	}

	public StringBuilder createHTMLFile(String strUrl, String strMyComment) {
		logger = LoggerFactory.getLogger(WwwMtCoKr.class.getName());

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

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select(".img_box_C").removeAttr("style");
		doc.select("#div-gpt-ad-1569217938641-0").remove();

//            strTitle = doc.select(".subject h2").text();
		strTitle = doc.select(".news_dtail_view_top_wrap h2.main_title").text();
		logger.debug("title:" + strTitle);
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

//            String writer = doc.select(".cont_left_article .reporter_copy_w_1 strong").html();
		String writer = doc.select(".vw_byline .vw_by_reporter li dl dd.reporter_name").text();
		writer = writer.replace("자세히보기", "");
		logger.debug("writer:" + writer);

//            Elements timeElements = doc.select(".byline em");
		strDate = doc.select(".view_top_container div.newsinfo span").text();
		strDate = strDate.replace("입력", "").trim();
		if (strDate.indexOf("수정") != -1) {
			strDate = strDate.substring(0, strDate.indexOf("수정")).trim();
		}
		strDate = strDate.replace("입력", "").trim();
		logger.debug("strDate:" + strDate);
		strFileNameDate = strDate;

		strFileNameDate = StockUtil.getDateForFileName(strDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		Elements article = doc.select(".articleView");
		logger.debug("article:[" + article + "]");
		if (article == null || article.isEmpty()) {
			article = doc.select(".view_body");
		}
		// article.select(".image-area").append("<br><br>");
		article.select(".image-area").after("<br><br>");

		String style = article.select("#mArticle").attr("style");
		logger.debug("style:" + style);

		article.removeAttr("style");
		article.removeAttr("class");
		article.attr("style", "width:741px");

		// article.select("img").attr("style", "width:741px");
		article.select(".txt_caption.default_figure").attr("style", "width:741px");

		// logger.debug("imageArea:"+article.select(".image-area"));
		String strContent = article.html().replaceAll("640px", "741px");
		strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
		strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
		strContent = strContent.replaceAll("figure", "div");
		strContent = strContent.replaceAll("figcaption", "div");
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);

		Element copyrightElement = doc.select(".cont_left_article .reporter_copy_w_2").first();
		String copyright = "";
		if (copyrightElement != null) {
			copyright = copyrightElement.text();
		}
		logger.debug("copyright:" + copyright);

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		// sb1.append("<meta http-equiv=\"Content-Type\"
		// content=\"text/html;charset=utf-8\">\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		sb1.append(StockUtil.getMyCommentBox(strMyComment));

		sb1.append("<div style='width:741px'>\r\n");

		sb1.append("<h3> 기사주소:[<a href='" + strUrl + "' target='_sub'>" + strUrl + "</a>] </h3>\n");
		sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
		sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
		sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
		sb1.append(strContent + "\n");
		sb1.append(copyright);
		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");

		File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = null;

		fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());

		return sb1;
	}

}
