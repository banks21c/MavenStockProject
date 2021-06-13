package html.parsing.stock.news;

import java.io.File;
import java.io.IOException;
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
import html.parsing.stock.util.ImageUtil;
import html.parsing.stock.util.StockUtil;

public class WwwMkCoKr extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwMkCoKr.class);

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
		new WwwMkCoKr(1);
	}

	public WwwMkCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public WwwMkCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		logger.debug(this.getClass().getSimpleName());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://www.mk.co.kr/news/society/view/2019/12/1091653/";
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
		System.out.println("url:" + strUrl);
		System.out.println("createHTMLFile protocol:" + protocol);
		System.out.println("createHTMLFile host:" + host);
		System.out.println("createHTMLFile path:" + path);
		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName = "";
		String strFileNameDate = "";
		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select("all").remove();
		doc.select("div").attr("style", "width:741px");

		Elements title = doc.select("h1.top_title");
		logger.debug("title1:" + strTitle);
		if (title != null && title.size() > 0) {
			strTitle = title.get(0).text();
		}
		if (strTitle == null || strTitle.trim().equals("")) {
			title = doc.select("div.view_title h3");
			if (title != null && title.size() > 0) {
				strTitle = title.get(0).text();
			}
		}
		if (strTitle == null || strTitle.trim().equals("")) {
			title = doc.select("#view_tit .head_tit");
			if (title != null && title.size() > 0) {
				strTitle = title.get(0).text();
			}
		}

		logger.debug("title2:" + strTitle);
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		Elements subTitle = doc.select("h2.sub_title1");
		String strSubTitle = "";
		if (subTitle != null && subTitle.size() > 0) {
			strSubTitle = subTitle.outerHtml();
		}

		String strAuthor = doc.select(".author").html();
		logger.debug("strAuthor:[" + strAuthor + "]");
		strAuthor = strAuthor.replace("&nbsp;", " ");

		Elements dateElements = doc.select(".news_title_author .lasttime");
		logger.debug("dateElements:[" + dateElements + "]");
		Element dateElement = null;
		if (dateElements != null && !dateElements.isEmpty() && dateElements.size() > 0) {
			dateElement = dateElements.get(0);
		} else {
			dateElements = doc.select(".news_title_author .lasttime1");
			if (dateElements != null && dateElements.size() > 0) {
				dateElement = doc.select(".news_title_author .lasttime1").get(0);
			}
		}
		logger.debug("dateElements :" + dateElements);
		if (dateElements == null || dateElements.isEmpty() || dateElements.size() <= 0) {
			dateElements = doc.select("#view_tit .sm_num");
			if (dateElements != null && !dateElements.isEmpty() && dateElements.size() > 0) {
				dateElement = dateElements.get(0);
			}
		}

		if (dateElement != null) {
			strDate = dateElement.html();
			logger.debug("strDate :" + strDate);
			strDate = strDate.replace("입력 :", "").trim();
			if (strDate.contains("수정")) {
				strDate = strDate.substring(0, strDate.indexOf("수정"));
			}
			strDate = strDate.replace("&nbsp;", "").trim();
			logger.debug("strDate :[" + strDate + "]");
			strDate = strDate.replace(".", "-");
			logger.debug("strDate :[" + strDate + "]");

			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			logger.debug("strFileNameDate:" + strFileNameDate);
		}
		logger.debug("strDate:[" + strDate + "]");

		Elements contentEls = doc.select("#article_body");
		if (contentEls.size() <= 0) {
			contentEls = doc.select(".view_txt");
		}
		logger.debug("contentEls:" + contentEls);
		if (contentEls.size() <= 0) {
			contentEls = doc.select(".read_txt");
		}

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
		sb1.append(strSubTitle + "<br>\r\n");
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
