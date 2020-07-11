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

public class StarHankookilboCom extends News {

	private static Logger logger = LoggerFactory.getLogger(StarHankookilboCom.class);

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
		new StarHankookilboCom(1);
	}

	StarHankookilboCom() {

	}

	StarHankookilboCom(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://www.hankookilbo.com/News/Read/201701210457609652";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			System.out.println("doc:" + doc);

			doc.select("iframe").remove();
			doc.select("script").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			strTitle = doc.select(".news-title").text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			System.out.println("time html:" + doc.select(".news-date .ng-binding"));
			Element timeElement = doc.select(".news-date").get(0);
			System.out.println("time html:" + timeElement);
			// strDate =
			// timeElement.childNode(0).toString().substring(timeElement.childNode(0).toString().indexOf(":")
			// + 1).trim();
			strDate = timeElement.text().replaceAll("등록 : ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String dateTime = doc.select(".news-date-container").html();
			System.out.println("dateTime:" + dateTime);

			String author = doc.select(".news-writer").get(0).html();
			System.out.println("author:" + author);

			Elements article = doc.select(".news-detail-content");
			article.attr("style", "width:548px");
			String strContent = article.outerHtml();
			System.out.println("strContent:" + strContent);
			strContent = strContent.replaceAll("640px", "548px");
			strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);

			String copyright = doc.select(".news-writer").get(1).html();
			System.out.println("copyright:" + copyright);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:12px'>" + dateTime + "</span><br><br>\n");
			sb1.append("<span style='font-size:12px'>" + author + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
			sb1.append("저작권자 © 한국일보 무단전재 및 재배포 금지<br><br>\n");
			sb1.append("Copyright ⓒ The Hankook-Ilbo All rights reserved<br><br>\n");
			System.out.println("sb.toString:" + sb1.toString());
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
