package html.parsing.stock.news;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class BizChosunCom {

	final static String userHome = System.getProperty("user.home");
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
		new BizChosunCom(1);
	}

	BizChosunCom() {
		logger = java.util.logging.Logger.getLogger(this.getClass().getName());
	}

	BizChosunCom(int i) {
		logger = java.util.logging.Logger.getLogger(this.getClass().getName());
		logger.log(Level.INFO, this.getClass().getName());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url.equals("")) {
			url = "http://biz.chosun.com/site/data/html_dir/2018/01/06/2018010601314.html";
			// url =
			// "http://biz.chosun.com/site/data/html_dir/2018/01/09/2018010901170.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strFileNameTitle = "";
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
			System.out.println("title:" + strTitle);
			strFileNameTitle = strTitle;
			strFileNameTitle = strFileNameTitle.replaceAll(" ", "_");
			strFileNameTitle = strFileNameTitle.replaceAll("\"", "'");
			strFileNameTitle = strFileNameTitle.replaceAll("\\?", "§'");
			System.out.println("strFileNameTitle:" + strFileNameTitle);
//날짜
			strDate = doc.select(".news_body .news_date").text();
			strDate = strDate.replace("입력 ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = strFileNameDate.replaceAll(" ", "_");
			strFileNameDate = strFileNameDate.replaceAll(":", ".");
			strFileNameDate = "[" + strFileNameDate + "]";
			System.out.println("strFileNameDate:" + strFileNameDate);
//작자
			Elements title_author_2011 = doc.select(".news_title_author li");
			System.out.println("title_author_2011:" + title_author_2011);
			String author = title_author_2011.text();
			System.out.println("author:" + author);

			Elements article = doc.select("#news_body_id");
			System.out.println("article:" + article);
			article.select(".news_body .news_date").remove();

			article.attr("style", "width:548px");
			String contentElements = article.outerHtml();
			System.out.println("contentElements:[" + contentElements + "]contentElements");

			String copyright = doc.select(".news_copyright").outerHtml();
			System.out.println("copyright:" + copyright);

			String strContent = contentElements.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<figure>", "");
			strContent = strContent.replaceAll("</figure>", "<br>");
			strContent = strContent.replaceAll("<figcaption>", "");
			strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");
			sb1.append("<div style='width:548px'>\r\n");


			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(author).append("</span><br><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println("[sb.toString:" + sb1.toString() + "]");

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strFileNameTitle + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
