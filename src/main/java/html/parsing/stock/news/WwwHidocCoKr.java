package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.StockUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class WwwHidocCoKr extends News {

	java.util.logging.Logger logger = null;

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
		new WwwHidocCoKr(1);
	}

	WwwHidocCoKr() {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
	}

	WwwHidocCoKr(int i) {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
		logger.log(Level.INFO, this.getClass().getSimpleName());
		String url = JOptionPane.showInputDialog("HiDoc URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "https://www.hidoc.co.kr/healthstory/news/C0000467639";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		System.out.println("url:" + url);
		new News().getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			System.out.println("doc:[" + doc + "]");
			doc.select("iframe").remove();
			doc.select("script").remove();

			strTitle = doc.select(".news_view .article_tit").text();
			if (strTitle == null || strTitle.equals("")) {
				strTitle = doc.select(".section_top_box h3").text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			String date = doc.select(".news_view .etc_info .date").get(0).text();
			if (date == null || date.equals("")) {
				date = doc.select(".section_top_box dl dd .wr_day").text();
				date = date.replace("등록", "").trim();
			}
			date = date.replace("등록", "").trim();
			date = date.replace("|", ";");
			System.out.println("date:" + date);
			String authorAndDate[] = date.split(";");
			System.out.println("authorAndDate:" + authorAndDate);
			if (authorAndDate != null && authorAndDate.length > 1) {
				strDate = authorAndDate[1].trim();
			} else {
				strDate = authorAndDate[0].trim();
			}
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String author = authorAndDate[0].trim();
			System.out.println("author:" + author);

			Elements article = doc.select(".article_body");
			//System.out.println("article:[" + article+"]article");

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			//System.out.println("articleHtml:[" + articleHtml+"]articleHtml");

			String copyright = "";
//			copyright = doc.select(".news_view .article_body .copyright_info").outerHtml();
			System.out.println("copyright:[" + copyright + "]copyright");

			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<figure>", "");
			strContent = strContent.replaceAll("</figure>", "<br>");
			strContent = strContent.replaceAll("<figcaption>", "");
			strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			//System.out.println("content:[" + content + "]content");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(author).append("</span><br><br>\n");
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

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
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
