package html.parsing.stock.news;

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

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.StockUtil;

public class NewsChosun extends News {

	private static Logger logger = LoggerFactory.getLogger(NewsChosun.class);

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
		new NewsChosun(1);
	}

	NewsChosun() {
		logger = LoggerFactory.getLogger(NewsChosun.class);
	}

	NewsChosun(int i) {
		String url = JOptionPane.showInputDialog("조선일보 URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://news.chosun.com/site/data/html_dir/2017/02/24/2017022401428.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		logger.debug("url:" + url);
		new News().getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();

			strTitle = doc.select("#news_title_text_id").html();
			logger.debug("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			logger.debug("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);

			strDate = doc.select(".news_body .news_date").text();
			logger.debug("strDate:" + strDate);
			String[] strDates = strDate.split("\\|");
			strDate = strDates[0].trim();
			strDate = strDate.replaceAll("입력 ", "");
			strDate = strDate.replaceAll("  ", "");

			doc.select(".news_date").remove();

			logger.debug("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			logger.debug("strFileNameDate:" + strFileNameDate);

			String strAuthor = doc.select(".news_title_author a").text();
			logger.debug("author:" + strAuthor);

			Elements article = doc.select("#news_body_id");
			article.select(".news_like").remove();
			// logger.debug("article:" + article);

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			logger.debug("articleHtml:[" + articleHtml + "]articleHtml");

			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<figure>", "");
			strContent = strContent.replaceAll("</figure>", "<br>");
			strContent = strContent.replaceAll("<figcaption>", "");
			strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			String copyright = doc.select(".csource").outerHtml();
			logger.debug("copyright:" + copyright);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
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
