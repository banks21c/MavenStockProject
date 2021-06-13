package html.parsing.stock.news;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import html.parsing.stock.util.ImageUtil;
import html.parsing.stock.util.StockUtil;

public class ViewAsiaeCoKr extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(ViewAsiaeCoKr.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ViewAsiaeCoKr(1);
	}

	public ViewAsiaeCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	ViewAsiaeCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.asiae.co.kr/news/view.htm?idxno=2018041911043434606";
		}
		if (url != null && !url.equals("")) {
			createHTMLFile(url);
		}
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
//	.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
//	.header("Accept-Language", "en").header("Accept-Encoding", "gzip,deflate,sdch").get();
////	logger.debug("doc:[" + doc + "]");
	@Override
	public StringBuilder createHTMLFileCommon(Document doc, String strUrl, String strMyComment) {
		getURL(strUrl);
		String protocol = getProtocol();
		String host = getHost();
		String path = getPath();
		String protocolHost = getProtocolHost();
		logger.debug("url:" + strUrl);
		logger.debug("createHTMLFile protocol:" + protocol);
		logger.debug("createHTMLFile host:" + host);
		logger.debug("createHTMLFile path:" + path);
		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName = "";
		String strFileNameDate = "";
		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select(".news_twobox").remove();
		doc.select(".art_btm").remove();
		doc.select(".article_ad").remove();
		doc.select("a").removeAttr("title");
		doc.select(".add_banner").remove();
		doc.select("#comment_box").remove();
		
		Elements title = doc.select(".cont_sub .area_title h3");
		logger.debug("title:" + strTitle);
		if (title != null && title.size() > 0) {
			strTitle = title.get(0).text();
		}
		logger.debug("title:" + strTitle);
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		String strAuthor = doc.select(".e_article").text();
		logger.debug("strAuthor:[" + strAuthor + "]");

		Element paragraph = doc.select(".area_title p").get(0);
		List<Node> childNodes = paragraph.childNodes();
		int childNodeSize = paragraph.childNodeSize();
		logger.debug("childNodeSize:" + childNodeSize);
		for (Node n : childNodes) {
			String nodehtml = n.toString();
			logger.debug("nodehtml:" + nodehtml);
		}
		String strDate = paragraph.childNode(2).toString().trim();
		logger.debug("strDate:[" + strDate + "]");

		strFileNameDate = StockUtil.getDateForFileName(strDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		Elements contentEls = doc.select("#txt_area");
		Elements imgEls = contentEls.select("img");
		try {
			for (Element imgEl : imgEls) {
				String imgUrl = imgEl.attr("src");
				logger.debug("imgUrl:" + imgUrl);
				if (!imgUrl.startsWith("http")) {
					if (imgUrl.startsWith("//")) {
						imgUrl = protocol + ":" + imgUrl;
					} else {
						imgUrl = protocolHost + imgUrl;
					}
				}
				imgEl = ImageUtil.getImageWithStyle(imgEl, imgUrl);
				logger.debug("imgEl:" + imgEl);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String strContent = contentEls.html();
		strContent = strContent.replace("src=\"//", "src=\"" + protocol + "://");
		logger.debug("strContent:" + strContent);
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
		Document contentDoc = Jsoup.parse(strContent);
		contentDoc.select("#myCommentDiv").remove();
		strContent = contentDoc.select("body").html();

		String copyright = "";

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		sb1.append(StockUtil.getMyCommentBox(strMyComment));

		sb1.append("<div style='width:741px'>\r\n");

		sb1.append("<h3> 기사주소:[<a href='" + strUrl + "' target='_sub'>" + strUrl + "</a>] </h3>\n");
		sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
//		sb1.append(strSubTitle + "<br>\r\n");
		sb1.append(strAuthor + "<br>\r\n");
		sb1.append(strContent + "<br>\r\n");
		sb1.append(copyright + "<br>\r\n");

		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");

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
