package html.parsing.stock.news;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class WwwHaniCoKr extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwHaniCoKr.class);

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
		new WwwHaniCoKr(1);
	}

	WwwHaniCoKr() {

	}

	WwwHaniCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.hani.co.kr/arti/society/society_general/779348.html?_fr=mt2";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		System.out.println("url:" + url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select(".kisa-sponsor-area").remove();
			doc.select("iframe").remove();
			doc.select(".adsbygoogle").remove();
			doc.select("#ad_tag").remove();
			doc.select("script").remove();
			doc.select("img").removeAttr("alt");
			doc.select("img").removeAttr("title");
			if (host.startsWith("www.")) {
				strTitle = doc.select(".article-head .title").text();
			} else {
				strTitle = doc.select(".headline h1").get(0).text();
			}
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			Elements ahrefs = doc.select("a");
			URL u = new URL(url);
			String protocol = u.getProtocol();
			System.out.println("protocol:" + protocol);
			String host = u.getHost();
			System.out.println("host1:" + host);
			String path = u.getPath();
			System.out.println("path:" + path);
			for (Element ahref : ahrefs) {
				String strAhref = ahref.attr("href");
				if (!strAhref.startsWith("http")) {
					System.out.println("strAhref1:" + strAhref);
					if (strAhref.startsWith("/")) {
						ahref.attr("href", protocol + "://" + host + strAhref);
					} else {
						ahref.attr("href", protocol + "://" + host + path + strAhref);
					}
				}
			}

			Element timeElement = null;
			if (host.startsWith("www.")) {
				timeElement = doc.select(".article-head .date-time span").get(0);
				timeElement.select("em").remove();
				strDate = timeElement.text();
			} else {
				Node n = doc.select(".tools .date").get(0).childNodes().get(0);
				strDate = n.toString();
				strDate = strDate.replaceAll("등록", "").trim();
			}
			System.out.println("strDate:" + strDate);
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements articles = doc.select(".article-text");
			Element article = null;
			System.out.println("article1:" + article);
			if (articles.size() <= 0) {
				article = doc.select("div.article").get(0);
			} else {
				article = articles.get(0);
			}
			System.out.println("article2:" + article);
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");

			Elements articleTextFontSizes = article.select(".article-text-font-size");
			Element articleTextFontSize = null;
			String style = "";
			if (articleTextFontSizes.size() > 0) {
				articleTextFontSize = article.select(".article-text-font-size").get(0);
				style = articleTextFontSize.attr("style");

				System.out.println("style:" + style);

				articleTextFontSize.removeAttr("style");
				articleTextFontSize.attr("style", "width:741px");
				article.select(".article-text-font-size .imageC").attr("style", "width:741px");
				article.select(".article-text-font-size .imageC .desc").attr("style", "width:741px");

			}
			System.out.println("article:" + article);

			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println(sb1.toString());

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html");
			System.out.println("f:" + f);

			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (IOException e) {
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
