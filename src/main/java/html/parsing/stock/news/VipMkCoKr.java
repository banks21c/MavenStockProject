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
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class VipMkCoKr extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(VipMkCoKr.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	String strDefaultUrl = "http://vip.mk.co.kr/news/view/21/20/1820410.html";
	String strUrl;

	public static void main(String[] args) {
		new VipMkCoKr(1);
	}

	public VipMkCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	VipMkCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		strUrl = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + strUrl + "]");
		if (StringUtils.defaultString(strUrl).equals("")) {
			strUrl = strDefaultUrl;
		}
		createHTMLFile(strUrl);
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

//	doc = Jsoup.connect(strUrl)
//			.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
//			.header("Accept-Language", "en").header("Accept-Encoding", "gzip,deflate,sdch").get();
////			logger.debug("doc:[" + doc + "]");
	@Override
	public StringBuilder createHTMLFileCommon(Document doc, String strUrl, String strMyComment) {
		getURL(strUrl);
		String protocol = getProtocol();
		String host = getHost();
		String path = getPath();
		String protocolHost = getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName;
		logger.debug("strUrl:" + strUrl);

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		doc.select("iframe").remove();
		doc.select("script").remove();

		Elements title;
		Element tableEl;
		String strAuthor = "";
		Elements subTitle;
		String strDate = "";
		Node lastPNode;
		Element contentEl;
		if (strUrl.contains("planning_news_view")) {
			title = doc.select(".headtitle");
			tableEl = doc.select(".headtitle").get(0).parent().parent().parent().parent();
			Node authorNode = tableEl.select("br").last().previousSibling();
			strAuthor = authorNode.toString();
			strAuthor = strAuthor.replace("[", "").replace("]", "");
			lastPNode = tableEl.select("p").last();
			lastPNode = lastPNode.childNode(0);
			logger.debug("lastPNode:" + lastPNode);
			strDate = lastPNode.outerHtml();
			strDate = strDate.replace("입력", "").trim();
			logger.debug("strDate:" + strDate);
			contentEl = tableEl;
		} else {
			title = doc.select("div#Titless");
			contentEl = doc.select("div#Conts").get(0);
			logger.debug("div#Conts => " + contentEl);
		}

		contentEl.select("nobr").attr("style", "white-space:nowrap");
		contentEl.select("nobr").tagName("div");

		logger.debug("title:" + strTitle);
		if (title != null && title.size() > 0) {
			strTitle = title.get(0).text();
		}
		logger.debug("title:" + strTitle);
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		subTitle = doc.select("h2.sub_title1");
		String strSubTitle = "";
		if (subTitle.size() > 0) {
			strSubTitle = subTitle.outerHtml();
		}

		if (strDate.equals("")) {
			strDate = title.get(0).parent().nextElementSibling().text();
		}
		if (strDate.equals("")) {
			strDate = title.get(0).parent().nextElementSibling().nextElementSibling().text();
		}
		String strDateArray[] = strDate.split("\\|");
		if (strDateArray.length == 3) {
			strDate = strDateArray[2];
			strDate = strDate.replace("_", " ").trim();
		}
		String strFileNameDate = strDate;
		strFileNameDate = StockUtil.getDateForFileName(strDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		logger.debug("contentEl1:[" + contentEl + "]:contentEl1");
		contentEl.select(".con_txt_new").remove();
		contentEl.select("spacer").remove();
		contentEl.select("style").remove();
		logger.debug("contentEl2:[" + contentEl + "]:contentEl2");

		if (strAuthor.equals("")) {
//				strAuthor = doc.select(".author").html();
			strAuthor = contentEl.select("br").last().previousSibling().toString();
			contentEl.select("br").last().previousSibling().remove();
		}
		logger.debug("strAuthor:[" + strAuthor + "]");

		String strContent = contentEl.outerHtml();
		logger.debug("strContent:" + strContent);
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);

		String copyright = "";

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		sb1.append(StockUtil.getMyCommentBox(strMyComment));

		sb1.append("<div style='width:741px'>\r\n");

		sb1.append("<h3> 기사주소:[<a href='" + strUrl + "' target='_sub'>" + strUrl + "</a>] </h3>\n");
		sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
		sb1.append(strSubTitle + "<br>\r\n");
		sb1.append(strAuthor + "<br>\r\n");
		sb1.append(strContent + "<br>\r\n");
		sb1.append(copyright + "<br>\r\n");

		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		// System.out.println(sb1.toString());

		logger.debug("fileDir:" + USER_HOME + File.separator + "documents" + File.separator + host);
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
