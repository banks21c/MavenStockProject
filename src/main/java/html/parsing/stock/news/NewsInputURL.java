package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.StockUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsInputURL extends News {

	private static Logger logger = LoggerFactory.getLogger(NewsInputURL.class);

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
		new NewsInputURL(1);
	}

	NewsInputURL() {
		logger = LoggerFactory.getLogger(NewsInputURL.class);
	}

	NewsInputURL(int i) {
		logger = LoggerFactory.getLogger(NewsInputURL.class);
		String url = JOptionPane.showInputDialog("URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url != null && !url.equals("")) {
			createHTMLFile(url);
		}
	}

	public StringBuilder createHTMLFile(String url) {
		new News().getURL(url);
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
			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();
			doc.select("#CSCNT").remove();
			doc.select(".news_like").remove();

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);            
			strTitle = doc.select("#title_text").text();
			if (strTitle == null || strTitle.equals("")) {
				strTitle = doc.select(".news_title_text h1").text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);

			strDate = doc.select("#date_text").text();
			if (strDate == null || strDate.equals("")) {
				strDate = doc.select(".news_body .news_date").text();
			}
			System.out.println("strDate:" + strDate);
			String[] strDates = strDate.split("\\|");
			strDate = strDates[0].trim();
			strDate = strDate.replaceAll("입력 : ", "");
			strDate = strDate.replaceAll("입력 ", "");

			doc.select(".news_date").remove();

			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String strContent = doc.html();

			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			String copyright = "";

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			//sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append(copyright).append("<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println("sb.toString:[" + sb1.toString() + "]");

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = "";

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
