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
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.util.FileUtil;

public class BizNewdailyCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(BizNewdailyCoKr.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new BizNewdailyCoKr(1);
	}

	BizNewdailyCoKr() {

	}

	BizNewdailyCoKr(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://biz.newdaily.co.kr/site/data/html/2020/04/23/2020042300062.html";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		System.out.println("url:" + url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select(".h_news").remove();
			doc.select("ins").remove();
			doc.select("#lv-container").remove();
			doc.select(".nd-rel").remove();

			Elements spanEl = doc.select("span");
			for (Element el : spanEl) {
				if (el.text().equals("뉴데일리 댓글 운영정책")) {
					el.parent().parent().remove();
				}
			}

//            strTitle = doc.select(".subject h2").text();
			strTitle = doc.select("h1.nd-news-tit").text();
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			strSubTitle = doc.select(".nd-news-sub-tit").text();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Element writerEl = doc.select(".nd-news-writer").get(0);
			writerEl.select("a").remove();
			String writer = writerEl.text();
//			writer = writer.replace("기자의 다른 기사 보기", "");
			System.out.println("writer:" + writer);

//            Elements timeElements = doc.select(".byline em");
			strDate = doc.select(".nd-news-info-date").text();
			if (strDate.indexOf("|") != -1) {
				strDate = strDate.substring(0, strDate.indexOf("|"));
			}
			strDate = strDate.replace("입력", "").trim();
			if (strDate.indexOf("수정") != -1) {
				strDate = strDate.substring(0, strDate.indexOf("수정")).trim();
			}
			strDate = strDate.replace("입력", "").trim();
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			Elements article = doc.select(".nd-news-body");
			System.out.println("article:[" + article + "]");

			article.select(".nd-news-writer").remove();
			article.select(".nd-news-info-date").remove();

			String style = article.attr("style");
			System.out.println("style:" + style);

			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:548px");

			Element copyRightElement = article.select(".nd-by-line").first();
			String copyRight = "";
			if (copyRightElement != null) {
				copyRight = copyRightElement.outerHtml();
			}
			System.out.println("copyRight:" + copyRight);
			article.select(".nd-by-line").first().remove();
			
			article.add(copyRightElement);

			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");

			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px;padding:5px 0px;'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:13px;font-family:맑은 고딕'>" + writer + "</span><br>\n");
			sb1.append("<span style='font-size:13px;font-family:맑은 고딕'>" + strDate + "</span><br><br>\n");
			sb1.append(strContent + "\r\n");
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
