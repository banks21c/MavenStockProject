package html.parsing.stock.news;

import html.parsing.stock.FileUtil;
import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsEdaily extends News {

	Logger logger = null;
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	static final String userHome = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	public static void main(String[] args) {
		new NewsEdaily(1);
	}

	public NewsEdaily() {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
	}

	public NewsEdaily(int i) {
		logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
		logger.log(Level.INFO, "이데일리뉴스");
		String url = JOptionPane.showInputDialog("이데일리뉴스 URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url.equals("")) {
			url = "http://www.edaily.co.kr/news/news_detail.asp?newsId=01918806619115112&mediaCodeNo=257";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
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
			doc.select(".news_domino").remove();
			doc.select(".hashtags").remove();
			doc.select(".reporter_sns").remove();
			doc.select(".tex_l_box").remove();
			doc.select(".tex_s_box").remove();
			doc.select(".issue_more").remove();
			doc.select("table").attr("width","548");
			System.out.println("a link:" + doc.select("a"));
			//System.out.println("doc:[" + doc+"]");
			strTitle = doc.select("h2.tit").text();
			if (strTitle.equals("")) {
				strTitle = doc.select(".news_titles h2").text();
			}
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElement = doc.select(".author_info .name");
			String writer = writerElement.text();
			if (writer.equals("")) {
				writer = doc.select(".stiky_l .reporter_info .reporter_name").text();
			}

			Elements timeElement = doc.select(".author_info .time");
			System.out.println("timeElement:" + timeElement);
			timeElement.select("em").remove();
			strDate = timeElement.text();
			if (strDate == null || strDate.equals("")) {
				String meta_published_time = "";
				Elements metas = doc.select("meta");
				for (Element meta : metas) {
					String metaProperty = meta.attr("property");
					if (metaProperty.equals("article:published_time")) {
						meta_published_time = meta.attr("content");
						if (meta_published_time != null) {
							meta_published_time = meta_published_time.replaceAll("T", " ");
							meta_published_time = meta_published_time.replaceAll("\\+", " ");
							strDate = meta_published_time;
						}
					}
				}
			}
			System.out.println("strDate:" + strDate);
			if (strDate == null || strDate.equals("")) {
				Elements scriptElements = doc.select("script");

				for (Element e : scriptElements) {
					String s = e.html();
					if (s.contains("fn_timer")) {
						strDate = s.substring(s.indexOf("fn_timer") + "fn_timer('".length());
						strDate = strDate.replace("'));", "");
						strDate = strDate.replace("T", " ");
					}
				}
			}
			strDate = strDate.replaceAll("입력 ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);
			Elements article = doc.select(".article_body");
			if (article.isEmpty()) {
				System.out.println("article is empty...");
				article = doc.select(".article_news");
			}
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");
			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);
			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:548px");
			// article.select("img").attr("style", "width:548px");
			article.select(".txt_caption.default_figure").attr("style", "width:548px");
			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);;
			//Elements copyRightElement = doc.select(".txt_copyright");
			//String copyRight = copyRightElement.text();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");
			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
			sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
			sb1.append(strContent + "\n");
			//sb1.append(copyRight);
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

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