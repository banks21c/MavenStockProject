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

public class WwwHankookilboCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(WwwHankookilboCom.class);

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
		new WwwHankookilboCom(1);
	}

	WwwHankookilboCom() {

	}

	WwwHankookilboCom(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://www.hankookilbo.com/News/Read/202006181492351265?did=PA&dtype=3&dtypecode=5680";
		}
		if (url != null && !url.equals("")) {
			createHTMLFile(url);
		}
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		StringBuilder sb1 = new StringBuilder();
		getURL(url);

		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			// strTitle = doc.select(".titGroup h4").text();
			strTitle = doc.select(".article-header h3").text();
			strTitle = doc.select(".end .end-top .col-main .title").text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			// Element timeElement = doc.select(".writeOption p").get(0);
//			Element timeElement = doc.select(".article-header .info p").get(0);
			Element timeElement = doc.select(".end .end-top .col-main .info dd").get(0);
			
			System.out.println("time html:" + timeElement);

			// strDate =
			// timeElement.childNode(0).toString().substring(timeElement.childNode(0).toString().indexOf(":")
			// + 1).trim();
			strDate = timeElement.text();
			System.out.println("strDate:" + strDate);
			strDate = strDate.replaceAll("입력 ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String dateTime = timeElement.html();
			System.out.println("dateTime:" + dateTime);

//			String author = doc.select(".article-footer .reporter .name").text() + " 기자";
			String author = doc.select(".end .end-body .col-main .writer .name").text();
			System.out.println("author:" + author);

//			Elements article = doc.select(".article-story");
			Elements article = doc.select(".end .end-body .col-main");
			article.select(".end .end-body .col-main .btn-area").remove();
			article.select(".end .end-body .col-main .naver-banner").remove();
			article.select(".btn-area").remove();
			article.select(".naver-banner").remove();
			article.select("#fileServer").remove();
			
			article.attr("style", "width:741px");
			String articleHtml = article.outerHtml();
			System.out.println("articleHtml:" + articleHtml);

			String copyright = doc.select(".copy span").outerHtml();
			System.out.println("copyright:" + copyright);

			String strContent = articleHtml.replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p></p>", "<br><br><p>");
			strContent = strContent.replaceAll("</article>", "</article><br>");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			

			sb1.append("<!DOCTYPE html>\r\n");
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
			sb1.append("<span style='font-size:12px'>" + dateTime + "</span><br><br>\n");
			sb1.append("<span style='font-size:12px'>" + author + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
			sb1.append("저작권자 © 한국일보 무단전재 및 재배포 금지<br><br>\n");
			sb1.append("Copyright ⓒ The Hankook-Ilbo All rights reserved<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println("sb.toString:" + sb1.toString());

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
