package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

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

public class MagazineHankyungCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(MagazineHankyungCom.class);

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
		new MagazineHankyungCom(1);
	}

	MagazineHankyungCom() {

	}

	MagazineHankyungCom(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "https://magazine.hankyung.com/business/article/202102095585b";
		}
		createHTMLFile(url);
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		getURL(url);
		System.out.println("url:" + url);
		System.out.println("createHTMLFile protocol:" + protocol);
		System.out.println("createHTMLFile host:" + host);
		System.out.println("createHTMLFile path:" + path);
		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();
			doc.select("#CSCNT").remove();
			doc.select(".news_like").remove();
			doc.select("#spiLayer").remove();
			doc.select(".sns_share").remove();

			strTitle = doc.select(".article-head .news-tit").text();
			if (strTitle != null && strTitle.equals("")) {
				strTitle = doc.select(".article_top .title").text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElements = doc.select(".article-writer .writer-info .name");
			Element writerElement = null;
			String writer = "";
			if (writerElements.size() > 0) {
				writerElement = writerElements.get(0);
				if (writerElement != null) {
					writer = writerElement.text();
				}
			}

			Elements timeElements = doc.select(".utility-wrap .date-info .txt-num");
			if (timeElements.size() > 0) {
				Element timeElement = timeElements.get(0);
				timeElement.select("em").remove();
				strDate = timeElement.text();
				strDate = strDate.replace("입력", "").trim();

				System.out.println("strDate:" + strDate);
			} else {
				timeElements = doc.select(".date-published .num");
				if (timeElements.size() > 0) {
					Element timeElement = timeElements.get(0);
					timeElement.select("em").remove();
					strDate = timeElement.text();
					strDate = strDate.replace("입력", "").trim();
				}
				System.out.println("strDate:" + strDate);
			}
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			// Elements article = doc.select("#newsView");
			Elements article = doc.select("#magazineView");
			if (article.size() <= 0) {
				article = doc.select("#newsView");
			}
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");
			article.select("svg").remove();
			//Update the tag name of each matched element. For example, to change each <i> to a <em>, do doc.select("i").tagName("em");
			Elements divEls = article.select("blockquote").tagName("div");
			divEls.attr("style","pading:5px;border:1px solid gray;");

			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);

			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");

			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");
			article.select("ylink").remove();

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
//          //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			StringBuilder bodySb = new StringBuilder();
			bodySb.append(StockUtil.getMyCommentBox(strMyComment));

			bodySb.append("<div style='width:741px'>\r\n");

			bodySb.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			bodySb.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			bodySb.append("<span style='font-size:12px'>").append(writer).append("</span><br><br>\n");
			bodySb.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			bodySb.append(strContent);
			bodySb.append(doc.select(".article-copy").outerHtml());
			bodySb.append("</div>\r\n");
			
			sb1.append(StockUtil.makeStockLinkStringByTxtFile(bodySb.toString()));

			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println("sb.toString:[" + sb1.toString() + "]");

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
