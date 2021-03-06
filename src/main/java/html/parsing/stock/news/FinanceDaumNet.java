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

public class FinanceDaumNet extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(FinanceDaumNet.class);

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
		new FinanceDaumNet(1);
	}

	FinanceDaumNet() {

	}

	FinanceDaumNet(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://finance.daum.net/news/20181101040046650";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		return createHTMLFile(url, "", strMyComment);
	}

	public static StringBuilder parseHTMLFile(String url, String html, String strMyComment) {
		return createHTMLFile(url, html, strMyComment);
	}

	public static StringBuilder createHTMLFile(String url, String html, String strMyComment) {
		System.out.println("createHTMLFile url:" + url);
		News gurl = new News();
		gurl.getURL(url);
		String protocol = gurl.getProtocol();
		String host = gurl.getHost();
		String path = gurl.getPath();
		String protocolHost = gurl.getProtocolHost();

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
//			html = html.replace("dmcf-pid","dmcf_pid");
//			html = html.replace("dmcf-ptype","dmcf_ptype");
			if (html.equals("")) {
				doc = Jsoup.connect(url).get();
			} else {
				doc = Jsoup.parse(html);
			}
			System.out.println("doc:" + doc);
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("p").removeAttr("dmcf-pid");
			doc.select("p").removeAttr("dmcf-ptype");
			doc.select("section").removeAttr("dmcf-sid");
			doc.select("div").removeAttr("dmcf-pid");
			doc.select("div").removeAttr("dmcf-ptype");
			doc.select("img").removeAttr("dmcf-mid");
			doc.select("img").removeAttr("dmcf-mtype");

			Elements strongEls = doc.select("strong");
			for (Element strongEl : strongEls) {
				String strStrong = strongEl.text();
				if (strStrong.equals("[관련기사]")) {
					strongEl.parent().remove();
					break;
				}
			}

			System.out.println("title1:" + doc.select(".finance"));
			System.out.println("title2:" + doc.select(".finance .head"));
			System.out.println("title3:" + doc.select(".finance .head .titView"));
			strTitle = doc.select(".finance .head .titView h5").text();
			if (strTitle == null || strTitle.equals("")) {
				return sb1;
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElements = doc.select(".finance .head .infoView p");
			System.out.println("writerElements:" + writerElements);
			String writer = "";
			if (writerElements.size() > 0) {
				writer = writerElements.get(0).text();
			}

			Elements timeElements = doc.select(".finance .head .infoView p");
			Element timeElement = null;
			if (timeElements.size() > 0) {
				timeElement = doc.select(".finance .head .infoView p").get(1);
				timeElement.select("em").remove();
				strDate = timeElement.text();
				strDate = strDate.replace("입력", "").trim();

				System.out.println("strDate:" + strDate);

				strFileNameDate = StockUtil.getDateForFileName(strDate);
				System.out.println("strFileNameDate:" + strFileNameDate);
			}

			Elements article = doc.select("#dmcfContents");
			System.out.println("article:" + article);
			article.select(".image-area").after("<br><br>");

			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);

			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");

			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			String copyright = "";

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='").append(url).append("' target='_sub'>").append(url)
					.append("</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(writer).append("</span><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("\n");
			sb1.append(copyright);
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
