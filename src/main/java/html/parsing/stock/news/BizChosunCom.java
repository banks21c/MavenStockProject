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

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class BizChosunCom extends News {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(BizChosunCom.class);

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
		new BizChosunCom(1);
	}

	BizChosunCom() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	BizChosunCom(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (url.equals("")) {
			url = "http://biz.chosun.com/site/data/html_dir/2018/01/06/2018010601314.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public static StringBuilder createHTMLFile(String url, String strMyComment) {
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
			doc.select(".news_rel").remove();
			doc.select(".news_like").remove();
			doc.select("#news_rel_id").remove();
			doc.select(".news_copyright_links").remove();
			doc.select(".copy_bottom").remove();
//제목
			strTitle = doc.select(".news_title_text h1").html();
			logger.debug("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = strTitleForFileName.replaceAll(" ", "_");
			strTitleForFileName = strTitleForFileName.replaceAll("\"", "'");
			strTitleForFileName = strTitleForFileName.replaceAll("\\?", "§");
			logger.debug("strFileNameTitle:" + strTitleForFileName);
//날짜
			strDate = doc.select(".news_body .news_date").text();
			strDate = strDate.replace("입력 ", "");
			logger.debug("strDate:" + strDate);
			if (strDate.contains("|")) {
				String strDateArray[] = strDate.split("\\|");
				strDate = strDateArray[0].trim();
			}
			logger.debug("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = strFileNameDate.replaceAll(" ", "_");
			strFileNameDate = strFileNameDate.replaceAll(":", ".");
			strFileNameDate = "[" + strFileNameDate + "]";
			logger.debug("strFileNameDate:" + strFileNameDate);
//작자
			Elements title_author_2011 = doc.select(".news_title_author li");
			logger.debug("title_author_2011:" + title_author_2011);
			String strAuthor = title_author_2011.text();
			logger.debug("author:" + strAuthor);

			Elements article = doc.select("#news_body_id");
			logger.debug("article:" + article);
			article.select(".news_body .news_date").remove();

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			logger.debug("articleHtml:[" + articleHtml + "]articleHtml");

			String copyright = doc.select(".news_copyright").outerHtml();
			logger.debug("copyright:" + copyright);

			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<figure>", "");
			strContent = strContent.replaceAll("</figure>", "<br>");
			strContent = strContent.replaceAll("<figcaption>", "");
			strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			Document contentDoc = Jsoup.parse(strContent);
			contentDoc.select("#myCommentDiv").remove();
			strContent = contentDoc.select("body").html();

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:14px'>").append(strAuthor).append("</span><br><br>\n");
			sb1.append("<span style='font-size:14px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			logger.debug("[sb.toString:" + sb1.toString() + "]");

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
