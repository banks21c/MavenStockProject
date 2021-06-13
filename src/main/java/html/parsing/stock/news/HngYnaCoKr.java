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

public class HngYnaCoKr extends News implements NewsInterface {

	private static Logger logger = null;

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
		new HngYnaCoKr(1);
	}

	HngYnaCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());

	}

	HngYnaCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "https://hng.yna.co.kr/5747";
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
			System.out.println("view:[" + doc.select(".view") + "]");
//            doc.select("iframe").remove();
//            doc.select("script").remove();
//            doc.select("noscript").remove();
//            doc.select("body").removeAttr("onload");
//            doc.select("div.pop_prt_btns").remove();
//            doc.select(".hidden-obj").remove();
//            doc.select("#CSCNT").remove();
//            doc.select(".news_like").remove();
//            doc.select("#spiLayer").remove();
//            doc.select(".sns_share").remove();
			doc.select(".tags_box").remove();
			doc.select(".sns_box").remove();

			strTitle = doc.select(".title_box .box .tit").text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			String writer = doc.select(".title_box .box .name").text();
			System.out.println("writer:" + writer);

			String strDate = doc.select(".title_box .box .date").text();
			System.out.println("strDate:" + strDate);
			strDate = strDate.replace("등록일", "").trim();
			if (strDate.contains("조회수")) {
				strDate = strDate.substring(0, strDate.indexOf("조회수")).trim();
			}
			System.out.println("strDate:" + strDate);
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			// Elements article = doc.select("#newsView");
			Elements article = doc.select(".editor_cnts");

			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);

			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByKrx(strContent);

			Elements copyrightElements = doc.select(".news_copyright");
			Element copyrightElement = null;
			String copyright = "";
			if (copyrightElements.size() <= 0) {
				copyrightElements = doc.select("#newsView .copy");
			}
			copyrightElement = copyrightElements.first();
			if (copyrightElement != null) {
				copyright = copyrightElement.text();
			}

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");

			doc.select(".news_date").remove();

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(writer).append("</span><br><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append(copyright).append("<br><br>\n");
			sb1.append("</div>\r\n");
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
