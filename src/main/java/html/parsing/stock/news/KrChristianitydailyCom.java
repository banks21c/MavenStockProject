package html.parsing.stock.news;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

public class KrChristianitydailyCom extends News {

	private static Logger logger = LoggerFactory.getLogger(KrChristianitydailyCom.class);

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
		new KrChristianitydailyCom(1);
	}

	KrChristianitydailyCom() {

	}

	KrChristianitydailyCom(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://kr.christianitydaily.com/articles/100431/20190611/%EA%B8%B0%EB%8F%84%EB%8A%94-%EB%8B%B9%EC%8B%A0%EC%9D%84-%EB%8C%80%ED%86%B5%EB%A0%B9%EC%9C%BC%EB%A1%9C-%EB%A7%8C%EB%93%A4-%EC%88%98%EB%8F%84-%EC%9E%88%EB%8B%A4.htm";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
		System.out.println("url:" + url);
		try {
			url = URLDecoder.decode(url, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select(".img_box_C").removeAttr("style");

//            strTitle = doc.select(".subject h2").text();
			strTitle = doc.select("h1.article-ttl").text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

//            String writer = doc.select(".cont_left_article .reporter_copy_w_1 strong").html();
			Element art_writer = doc.select(".art-writer").get(0);
			art_writer.select("em").remove();
			String writer = art_writer.text();

			writer = writer.replace("자세히보기", "");
			System.out.println("writer:" + writer);

			Element art_time = doc.select(".art-time").get(0);
			art_time.select("strong").remove();
			strDate = art_time.text();
			strDate = strDate.replace("입력", "").trim();
			if (strDate.indexOf("수정") != -1) {
				strDate = strDate.substring(0, strDate.indexOf("수정")).trim();
			}
			strDate = strDate.replace("입력", "").trim();
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String article_photo = doc.select(".article-photo").html();
			Elements article = doc.select(".article-txt");
			System.out.println("article:[" + article + "]");

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
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
			sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
			sb1.append(article_photo + "\n");
			sb1.append(strContent + "\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
