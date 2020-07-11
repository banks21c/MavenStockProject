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

public class ImnewsImbcCom extends News {

	private static Logger logger = LoggerFactory.getLogger(ImnewsImbcCom.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");
	String classSimpleName = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ImnewsImbcCom(1);
	}

	ImnewsImbcCom() {
		classSimpleName = this.getClass().getSimpleName();
	}

	ImnewsImbcCom(int i) {
		classSimpleName = this.getClass().getSimpleName();

		String url = StringUtils.defaultString(JOptionPane.showInputDialog(classSimpleName + " URL을 입력하여 주세요."));
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://imnews.imbc.com/replay/2020/nwtoday/article/5700698_32531.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".w_mug_emotion").remove();

			doc.select(".wrap_next_news").remove();
			doc.select(".wrap_playall").remove();
			doc.select(".layer_popup").remove();

			doc.select(".section_mid").remove();
			doc.select(".imoji").remove();
			doc.select(".wrap_comment").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			strTitle = doc.select(".wrap_article .art_title").text();
			System.out.println("title2:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			String strAuthor = doc.select(".wrap_article .info_art .writer a").text();
			System.out.println("strAuthor:[" + strAuthor + "]");

			String strEmail = "";
			System.out.println("strEmail:[" + strEmail + "]");

			String strDates = doc.select(".wrap_article .ui_article .date span").get(0).text();
			strDate = strDates.split("\\|")[0];
			strDate = strDate.replaceAll("입력", "").trim();
			strDate = strDate.replaceAll("기사", "").trim();
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String img = "";
			System.out.println("img:" + img);
			Elements contentEls = doc.select(".news_cont");
			String strContent = "";
			if (contentEls.size() > 0) {
				Element contentEl = doc.select(".news_cont").get(0);
				if (contentEl != null) {
					strContent = contentEl.outerHtml();
				}
			}
			strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);
			System.out.println("content:" + strContent);

			Elements divs = doc.select(".news_cont .news_img");
			logger.debug("divs:" + divs);
			String style = "";
			String backgroundImage = "";
			for (Element e : divs) {
				style = e.attr("style");
				logger.debug("style:" + style);
				if (style.startsWith("background-image")) {
					backgroundImage = style.substring(style.indexOf("("), style.indexOf(")"));
					logger.debug("backgroundImage:" + backgroundImage);
				}
			}

			String copyright = "";
			System.out.println("copyright:" + copyright);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:14px'>" + strAuthor + "</span><br><br>\n");
			sb1.append("<span style='font-size:14px'>" + strDate + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
			sb1.append(copyright + "<br><br>\n");
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
