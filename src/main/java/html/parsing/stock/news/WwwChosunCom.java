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
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class WwwChosunCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwChosunCom.class);

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
		new WwwChosunCom(1);
	}

	WwwChosunCom() {
		logger = LoggerFactory.getLogger(WwwChosunCom.class);
	}

	WwwChosunCom(int i) {
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://news.chosun.com/site/data/html_dir/2017/02/24/2017022401428.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String strUrl, String strMyComment) {
		logger.debug("url:" + strUrl);
		getURL(strUrl);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		try {
			doc = Jsoup.connect(strUrl).get();
			sb1 = createHTMLFile(doc, strUrl, strMyComment);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
		}
		return sb1;
	}

	public static StringBuilder createHTMLFileFromWebView(String strUrl, String strHtml, String strMyComment) {

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		try {
			doc = Jsoup.parse(strHtml);
			sb1 = createHTMLFile(doc, strUrl, strMyComment);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
		}
		return sb1;
	}

	public static StringBuilder createHTMLFile(Document doc, String strUrl, String strMyComment) {
		StringBuilder sb1 = new StringBuilder();
		String strTitleForFileName = "";
		String strFileNameDate = "";

		doc.select("iframe").remove();
		doc.select("script").remove();
		doc.select("svg").remove();

		strTitle = doc.select("#news_title_text_id").html();
		logger.debug("title:" + strTitle);
		if (strTitle.equals("")) {
			strTitle = doc.select("title").text();
		}
		strTitleForFileName = strTitle;
		strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
		logger.debug("strTitleForFileName:" + strTitleForFileName);

		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);

		strDate = doc.select(".news_body .news_date").text();
		logger.debug("strDate:" + strDate);
		if (strDate.equals("")) {
			strDate = doc.select(".font--size-md-14").text();
		}
		String[] strDates = strDate.split("\\|");
		strDate = strDates[0].trim();
		strDate = strDate.replaceAll("입력 ", "");
		strDate = strDate.replaceAll("  ", "");

		doc.select(".news_date").remove();

		logger.debug("strDate:" + strDate);
		strFileNameDate = strDate;

		strFileNameDate = StockUtil.getDateForFileName(strDate);
		logger.debug("strFileNameDate:" + strFileNameDate);

		String strAuthor = doc.select(".news_title_author a").text();
		if(strAuthor.equals("")) {
			strAuthor = doc.select(".article-byline__author").text();
		}
		logger.debug("author:" + strAuthor);

		Elements article = doc.select("#news_body_id");
		if(article.size()<=0) {
			article = doc.select(".article-body");
		}
		article.select(".news_like").remove();
		// logger.debug("article:" + article);

		article.attr("style", "width:741px");
		String articleHtml = article.outerHtml();
		logger.debug("articleHtml:[" + articleHtml + "]articleHtml");

		String strContent = articleHtml.replaceAll("640px", "741px");
		strContent = strContent.replaceAll("<figure>", "");
		strContent = strContent.replaceAll("</figure>", "<br>");
		strContent = strContent.replaceAll("<figcaption>", "");
		strContent = strContent.replaceAll("</figcaption>", "<br>");
		strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
		strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
		Document contentDoc = Jsoup.parse(strContent);
		contentDoc.select("#myCommentDiv").remove();
		strContent = contentDoc.select("body").html();

		String copyright = doc.select(".csource").outerHtml();
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
		sb1.append("<span style='font-size:14px'>").append(strAuthor).append("</span><br><br>\n");
		sb1.append("<span style='font-size:14px'>").append(strDate).append("</span><br><br>\n");
		sb1.append(strContent).append("<br><br>\n");
		sb1.append(copyright).append("<br><br>\n");
		sb1.append("</div>\r\n");
		sb1.append("</body>\r\n");
		sb1.append("</html>\r\n");
		logger.debug(sb1.toString());

		File dir = new File(userHome + File.separator + "documents" + File.separator + host);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());

		fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
				+ strTitleForFileName + ".html";
		FileUtil.fileWrite(fileName, sb1.toString());
		return sb1;

	}
}
