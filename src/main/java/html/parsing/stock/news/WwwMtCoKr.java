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

public class WwwMtCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(WwwMtCoKr.class);

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
		new WwwMtCoKr(1);
	}

	public WwwMtCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public WwwMtCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://stylem.mt.co.kr/stylemView.php?no=2018020809234688426";
			url = "https://www.mt.co.kr/";
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

	@Override
	public StringBuilder createHTMLFileCommon(Document doc, String strUrl, String strMyComment) {
		getURL(strUrl);
		String protocol = getProtocol();
		String host = getHost();
		String path = getPath();
		String protocolHost = getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName = "";
		String strFileNameDate = "";

		doc.select(".util_box").remove();
		doc.select(".articleRelnewsFrame").remove();
		doc.select("iframe").remove();

		Element article = doc.select("#article").get(0);
		logger.debug("article :[" + article + "]");

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		logger.debug(".view .view_top h1.subject :" + doc.select(".view .view_top h1.subject"));
		logger.debug(".view .view_top h1.subject :" + doc.select(".view .view_top h1.subject").get(0));
		strTitle = doc.select(".view .view_top h1.subject").get(0).text();
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		Elements subtitles = article.select("h2.sub_subject");
		Element subtitle = null;
		String strSubTitle = "";
		if(subtitles.size() > 0) {
			subtitle = subtitles.get(0);
			logger.debug("subtitle:" + subtitle);
			if (subtitle != null) {
				strSubTitle = article.select("#article h2").get(0).outerHtml();
			}
		}
		String strAuthor = article.select("#article .infobox1 a").outerHtml();
		logger.debug("strAuthor:" + strAuthor);
		Elements dateEl = doc.select(".view .view_top .view_info .info2 li.date");
		if (dateEl.size() > 0) {
			strDate = dateEl.get(0).text();
		} else {
			strDate = doc.select(".view_content .vc_top .info .info2 li.date").get(0).text();

		}
		logger.debug("strDate:" + strDate);
		if (strDate.startsWith(":")) {
			strDate = strDate.substring(1).trim();
		}
		strFileNameDate = strDate;
		strFileNameDate = StockUtil.getDateForFileName(strDate);
		strFileNameDate = strFileNameDate.replace(".", "-");
		logger.debug("strFileNameDate:" + strFileNameDate);

		String textBody = article.select("#textBody").outerHtml();
		textBody = textBody.replaceAll("src=\"//", "src=\"http://");
		// logger.debug("textBody:"+textBody);
		Document textBodyDoc = Jsoup.parse(textBody);
		logger.debug(textBodyDoc.select("div").get(0).html());

		textBodyDoc.select("div").get(0).attr("style", "font-size:11pt");
		textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
		String strContent = textBodyDoc.html();
		logger.debug("strContent:" + strContent);

		String copyright = article.select(".copyright").outerHtml();
		strContent = strContent + copyright;

		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
		Document contentDoc = Jsoup.parse(strContent);
		contentDoc.select("#myCommentDiv").remove();
		strContent = contentDoc.select("body").html();

//		String copyright = "";

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		sb1.append(StockUtil.getMyCommentBox(strMyComment));

		sb1.append("<div style='width:741px'>\r\n");

		sb1.append("<h3> 기사주소:[<a href='").append(strUrl).append("' target='_sub'>").append(strUrl)
				.append("</a>] </h3>\n");
		sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
		sb1.append(strSubTitle + "<br>\r\n");
		sb1.append(strAuthor + "<br>\r\n");
		sb1.append(strDate + "<br>\r\n");
		sb1.append(strContent + "<br>\r\n");
//		sb1.append(copyright + "<br>\r\n");

		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		// logger.debug(sb1.toString());

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
