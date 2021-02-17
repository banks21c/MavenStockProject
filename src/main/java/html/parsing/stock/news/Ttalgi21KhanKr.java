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
import org.jsoup.Jsoup;

public class Ttalgi21KhanKr extends News {

	private static Logger logger = LoggerFactory.getLogger(Ttalgi21KhanKr.class);

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
		new Ttalgi21KhanKr(1);
	}

	Ttalgi21KhanKr() {

	}

	Ttalgi21KhanKr(int i) {

		String url = StringUtils
				.defaultString(JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요."));
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://ttalgi21.khan.kr/6033";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
		getURL(url);
		Document doc;
		StringBuilder sb1 = new StringBuilder();

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

			doc = StockUtil.getUrlDocument(url);
			// 광고제거
			doc.select(".article_bottom_ad").remove();

			Elements pElements = doc.select("p");
			for (Element pElement : pElements) {
				String styleAttr = pElement.attr("class");
				if (styleAttr != null && styleAttr.contains("content_text")) {
					pElement.attr("style", "margin:10px 0px;");
				}
			}

			Elements imgElements = doc.select("#tt-body-page figure.imageblock img, figure.imageblock img");
			for (Element imgElement : imgElements) {
				String imgWidth = imgElement.attr("data-origin-width");
				String imgHeight = imgElement.attr("data-origin-height");
				logger.debug("imgWidth:" + imgWidth);
				logger.debug("imgHeight:" + imgHeight);

				int iWidth = 0;
				int changeWidth = 0;
				int iHeight = 0;
				int changeHeight = 0;

				if ((imgWidth != null && !imgWidth.equals("")) && (imgHeight != null && !imgHeight.equals(""))) {
					iWidth = Integer.parseInt(imgWidth);
					iHeight = Integer.parseInt(imgHeight);
					if (iWidth > 741) {
						changeWidth = 741;
						changeHeight = 741 * iHeight / iWidth;
					} else {
						changeWidth = iWidth;
						changeHeight = iHeight;
					}
				}
				String strChangeWidth = changeWidth + "";
				String strChangeHeight = changeHeight + "";
				imgElement.attr("data-origin-width", strChangeWidth);
				imgElement.attr("data-origin-height", strChangeHeight);
				imgElement.attr("width", strChangeWidth);
				imgElement.attr("height", strChangeHeight);
				logger.debug("image parent parent:" + imgElement.parent().parent());
				imgElement.parent().parent().replaceWith(imgElement);

			}

			System.out.println("path===>" + path);
			System.out.println("title===>" + doc.select("h1#article_title"));
			strTitle = doc.select(".titleWrap h2 a").get(0).text();
			// strTitle = doc.select("h1#articleTtitle").get(0).text();
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			strDate = doc.select(".titleWrap .date").text();
			// strDate = doc.select("div#bylineArea em").text();
			strDate = strDate.replaceAll("등록", "").trim();
			strDate = strDate.replaceAll("입력 : ", "").trim();
			strDate = strDate.replaceAll("입력", "").trim();
			if (strDate.contains("수정")) {
				strDate = strDate.substring(0, strDate.indexOf("수정"));
			}
			System.out.println("strDate:" + strDate);
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			// writer,author
			String strAuthor = doc.select(".titleWrap .category, .titleWrap .category a").text();
			// String strAuthor = doc.select(".view_header .subject .name a").text();

			Elements articles = doc.select(".article");
			articles.select(".tt_adsense_bottom").remove();
			articles.select(".container_postbtn").remove();
			articles.select(".container_postbtn .postbtn_like").remove();
			articles.select(".tt-share-entry-with-sns").remove();
			articles.select("iframe").remove();
			articles.select(".another_category_color_gray").remove();

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

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			Document contentDoc = Jsoup.parse(strContent);
			contentDoc.select("#myCommentDiv").remove();
			strContent = contentDoc.select("body").html();

			String copyright = doc.select(".art_copyright").text();

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
			sb1.append("<span style='font-size:12px'>").append(strAuthor).append("</span><br><br>\n");
			sb1.append(strContent).append("\n");
			sb1.append("<br>\n");
			sb1.append(copyright + "\n");
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
