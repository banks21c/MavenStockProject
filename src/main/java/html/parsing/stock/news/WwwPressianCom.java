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

public class WwwPressianCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwPressianCom.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WwwPressianCom(1);
	}

	WwwPressianCom() {

	}

	WwwPressianCom(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.pressian.com/news/article.html?no=188351";
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
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select(".kakao_ad_area").remove();

			strTitle = doc.select(".article_view .view_header .title").text();
			System.out.println("strTitle:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			strSubTitle = doc.select(".article_view .view_header .sub_title").text();
			System.out.println("strSubTitle:" + strSubTitle);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerEls = doc.select(".article_view .view_header .box .byline span.name");
			Element writerEl = null;
			if (writerEls.size() > 0) {
				writerEl = writerEls.first();
			}
			String writer = "";
			if (writerEl != null) {
				writer = writerEl.text();
			}

			Elements timeEls = doc.select(".article_view .view_header .box .byline span.date");
			Element timeEl = null;
			if (timeEls.size() > 0) {
				timeEl = timeEls.first();
			}
			timeEl.select("em").remove();
			strDate = timeEl.text();
			strDate = strDate.replace("기사입력", "").trim();
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			// Elements article = doc.select("#news_body_area");
//			Elements article = doc.select("#articleBody");
			Elements article = doc.select(".article_body");
			// article.select(".image-area").append("<br><br>");
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
			System.out.println("strContent1:" + strContent);
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("<figure>", "<div>");
			strContent = strContent.replaceAll("</figure>", "</div>");
			strContent = strContent.replaceAll("<figcaption>", "<div>");
			strContent = strContent.replaceAll("</figcaption>", "</div>");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			
			System.out.println("strContent2:" + strContent);

			String moreInfo = doc.select(".more_info").outerHtml();

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
			sb1.append("<h4>").append(strSubTitle).append("</h4>\n");
			sb1.append("<span style='font-size:12px'>").append(writer).append("</span><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("\n\n");
			sb1.append(moreInfo).append("\n");
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
