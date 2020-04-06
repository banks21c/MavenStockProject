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
import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class NewsHeraldcorpCom extends News {

	private static Logger logger = LoggerFactory.getLogger(NewsHeraldcorpCom.class);

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
		new NewsHeraldcorpCom(1);
	}

	NewsHeraldcorpCom() {
		logger = LoggerFactory.getLogger(NewsHeraldcorpCom.class);
	}

	NewsHeraldcorpCom(int i) {
		logger = LoggerFactory.getLogger(NewsHeraldcorpCom.class);
		String url = JOptionPane.showInputDialog("헤럴드경제 URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://news.heraldcorp.com/view.php?ud=20190226000403";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		logger.debug("url:" + url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();

			doc = Jsoup.parse(doc.html().replaceAll("data-src", "dataSrc"));

			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".w_mug_emotion").remove();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			strTitle = doc.select(".view_top_t2 ul li h1").text();
			logger.debug("title2:" + strTitle);
			strTitleForFileName = strTitle;
//			strTitle = StockUtil.makeStockLinkString(strTitle);
//			strTitle = StockUtil.makeStockLinkStringByExcel(strTitle);
			logger.debug("strTitle :" + strTitle);
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			logger.debug("strTitleForFileName:" + strTitleForFileName);

			String strAuthor = "";
			Elements aElements = doc.select("a");
			for (int i = 0; i < aElements.size(); i++) {
				Element aElement = aElements.get(i);
				String aHref = aElement.attr("href");
				if (aHref.startsWith("mailto:")) {
					strAuthor = aHref.substring("mailto:".length());
				}
			}

			String strEmail = strAuthor;
			logger.debug("strEmail:[" + strEmail + "]");

			String strDates = "";
			Elements ellipsis = doc.select(".ellipsis");
			for (Element e : ellipsis) {
				if (e.text().startsWith("기사입력")) {
					strDates = e.text();
				}
			}
			strDate = strDates.split("\\|")[0];
			strDate = strDate.replaceAll("기사입력", "").trim();
			strDate = strDate.replaceAll("입력", "").trim();
			strDate = strDate.replaceAll("기사", "").trim();
			logger.debug("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			logger.debug("strFileNameDate:" + strFileNameDate);

			String img = "";
			logger.debug("img:" + img);
			String strContent = doc.select("#articleText").outerHtml();
			//strContent = StockUtil.makeStockLinkString(strContent);
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);
			logger.debug("strContent:" + strContent);

			String copyright = "";
			logger.debug("copyright:" + copyright);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:14px'>").append(strAuthor).append("</span><br><br>\n");
			sb1.append("<span style='font-size:14px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append(copyright).append("<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			logger.debug(sb1.toString());

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
			logger.debug("추출완료");
		}
		return sb1;
	}

}
