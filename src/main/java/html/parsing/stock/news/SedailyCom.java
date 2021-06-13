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

public class SedailyCom extends News {

	private static Logger logger = LoggerFactory.getLogger(SedailyCom.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	public static void main(String[] args) {
		new SedailyCom(1);
	}

	public SedailyCom() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	SedailyCom(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String strUrl = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("strUrl:[" + strUrl + "]");
		if (StringUtils.defaultString(strUrl).equals("")) {
			strUrl = "http://www.sedaily.com/NewsView/1RVOCVY2MC";
		}
		createHTMLFile(strUrl);
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
		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select("ins").remove();
		doc.select(".btn_info").remove();

		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

		Elements title = doc.select("#v-left-scroll-in h2");
		if (title.size() <= 0) {
			title = doc.select(".article_head .art_tit");
		}
		strTitle = title.get(0).text();
		logger.debug("title:" + strTitle);
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		Elements authorEls = doc.select("#v-left-scroll-in .view_top ul");
		String strAuthor = "";
		String authorAndTime = "";
		String strDate = "";
		if (authorEls.size() > 0) {
			strAuthor = authorEls.select("li").get(0).text();
			authorAndTime = authorEls.outerHtml();
			strDate = authorEls.select("li").get(1).text();
			strDate = strDate.replace("입력", "");
		} else {
			authorEls = doc.select(".article_head .article_info");
			strAuthor = authorEls.select("span").get(2).text();
			authorAndTime = authorEls.outerHtml();
			strDate = authorEls.select("span").get(0).text();
			strDate = strDate.replace("입력", "");
		}
		logger.debug("authorEls:[" + authorEls + "]");
		logger.debug("strAuthor:[" + strAuthor + "]");
		logger.debug("authorAndTime:[" + authorAndTime + "]");
		logger.debug("strDate:[" + strDate + "]");

		strFileNameDate = StockUtil.getDateForFileName(strDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		Elements contentEls = doc.select(".view_con");
		logger.debug("contentEls:" + contentEls);
		logger.debug("contentEls.size:" + contentEls.size());
		Element contentEl;
		String strArticleSummary = "";
		String strArticle = "";
		if (contentEls.size() > 0) {
			contentEl = doc.select(".view_con").get(0);
		} else {
			Elements articleSummaryEls = doc.select(".article_summary");
			logger.debug("articleSummaryEls:" + articleSummaryEls);
			if (articleSummaryEls.size() > 0) {
				Element articleSummaryEl = articleSummaryEls.get(0);
				logger.debug("articleSummaryEl:" + articleSummaryEl);
				articleSummaryEl.removeAttr("style");
				articleSummaryEl.attr("style",
						"font-family: 'Noto Sans KR', sans-serif;margin-bottom: 6px;font-weight: bold;line-height: 1.2em;letter-spacing: 0 !important;");
				strArticleSummary = articleSummaryEl.outerHtml();
			}
			contentEl = doc.select(".article_view").get(0);
		}
		// 관련기사 삭제
		contentEl.select(".art_rel").remove();

		strArticle = contentEl.outerHtml();
		logger.debug("strArticle:" + strArticle);
		Document textBodyDoc = Jsoup.parse(strArticleSummary + strArticle);
		textBodyDoc.select(".lmbox1").attr("style", "font-size:10pt;color:gray;");
		String strContent = textBodyDoc.html();
		logger.debug("strContent:" + strContent);
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);

		Elements copyrightEls = doc.select(".copyright");
		String copyright = "";
		if (copyrightEls.size() > 0) {
			copyright = copyrightEls.get(0).outerHtml();
		} else {
			copyrightEls = doc.select(".article_copy");
			if (copyrightEls.size() > 0) {
				copyright = copyrightEls.get(0).outerHtml();
			}
		}

		sb1.append("<html lang='ko'>\r\n");
		sb1.append("<head>\r\n");
		sb1.append("</head>\r\n");
		sb1.append("<body>\r\n");

		sb1.append(StockUtil.getMyCommentBox(strMyComment));

		sb1.append("<div style='width:741px'>\r\n");

		sb1.append("<h3> 기사주소:[<a href='").append(strUrl).append("' target='_sub'>").append(strUrl)
				.append("</a>] </h3>\n");
		sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
		sb1.append(authorAndTime).append("<br>\r\n");
		sb1.append(strContent).append("<br>\r\n");

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
