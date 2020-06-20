package html.parsing.stock.news;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.util.FileUtil;

public class SnaptimeEdailyCoKr extends News {

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
		new SnaptimeEdailyCoKr(1);
	}

	public SnaptimeEdailyCoKr() {

	}

	public SnaptimeEdailyCoKr(int i) {


		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://snaptime.edaily.co.kr/2019/05/김보영의-키워드-우리는-왜-펭귄문제를-풀고-스냅챗/";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		try {
			url = URLDecoder.decode(url, "UTF-8");
			System.out.println("url:" + url);
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(SnaptimeEdailyCoKr.class.getName()).log(Level.SEVERE, null, ex);
		}
		getURL(url);
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
			doc.select(".social-list").remove();
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
			doc.select("div").removeAttr("style");
//			doc.select("div").attr("style","width:548px");
			System.out.println("a link:" + doc.select("a"));
			//System.out.println("doc:[" + doc+"]");
			strTitle = doc.select(".post-template-1 .single-post-title").text();
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

			Elements timeElement = doc.select(".post-published updated b");
			System.out.println("timeElement:" + timeElement);
			timeElement.select("em").remove();
			strDate = timeElement.text();
                        strDate = strDate.replace("마지막 업데이트 ","");
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
			Elements article = doc.select(".entry-content");
                        article.select(".cosmosfarm-share-buttons-default.cosmosfarm-align-center").remove();
                        article.select(".single-post-content .npqmgry").remove();
                        article.select("div").removeAttr("style");
                        article.select("div").attr("style","width:548px");
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
			article.select("figure").attr("style", "width:548px");
			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);;
			//Elements copyRightElement = doc.select(".txt_copyright");
			//String copyRight = copyRightElement.text();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
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
