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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class NewsEinfomaxCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(NewsEinfomaxCoKr.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strCurrentDate = new SimpleDateFormat("yyyy.MM.dd_E_HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new NewsEinfomaxCoKr(1);
	}

	NewsEinfomaxCoKr() {

	}

	NewsEinfomaxCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://news.einfomax.co.kr/news/articleView.html?idxno=4089497";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
		System.out.println("url:" + url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			URL u = new URL(url);
			String protocol = u.getProtocol();
			System.out.println("protocol:" + protocol);
			String host = u.getHost();
			System.out.println("host1:" + host);
			String path = u.getPath();
			System.out.println("path:" + path);

			doc = Jsoup.connect(url).get();
			doc.select(".kisa-sponsor-area").remove();
			doc.select("iframe").remove();
			doc.select(".adsbygoogle").remove();
			doc.select("#ad_tag").remove();
			doc.select("script").remove();
			doc.select("img").removeAttr("alt");
			doc.select("img").removeAttr("title");

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			strTitle = doc.select(".article-view-header .article-header-wrap .article-head-title").text();
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			Elements ahrefs = doc.select("a");
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

			Element authorEl = doc.select(".article-view-header .info-text ul li").get(0);
			String strAuthor = authorEl.text();

			Element timeEl = doc.select(".article-view-header .info-text ul li").get(1);
			String strDate = timeEl.text();
			strDate = strDate.replace("승인", "").trim();

			System.out.println("strDate:" + strDate);
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements articles = doc.select(".article-veiw-body");
			Element article = null;
			System.out.println("article1:" + article);
			if (articles.size() > 0) {
				article = articles.get(0);
			}
			System.out.println("article2:" + article);

			String strContent = article.html();
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			Document contentDoc = Jsoup.parse(strContent);
			contentDoc.select("#myCommentDiv").remove();
			strContent = contentDoc.select("body").html();

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
			sb1.append("<span style='font-size:12px'>").append(strAuthor).append("</span><br><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println(sb1.toString());

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html");
			System.out.println("f:" + f);

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (IOException e) {
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
