package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.ImageUtil;

public class WwwYnaCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(WwwYnaCoKr.class);
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
		new WwwYnaCoKr(1);
	}

	WwwYnaCoKr() {
		logger = LoggerFactory.getLogger(WwwYnaCoKr.class);
	}

	WwwYnaCoKr(int i) {
		logger = LoggerFactory.getLogger(WwwYnaCoKr.class);
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "https://www.yna.co.kr/view/AKR20190302013700001?input=1195p";
		}
		super.getURL(url);
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		logger = LoggerFactory.getLogger(WwwYnaCoKr.class);
		// getURL(url);
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
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			strTitle = doc.select(".title-article01>.tit").text();
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			String strDate = "";
			Elements dateEls = doc.select(".title-article01 .update-time");
			if (dateEls.size() > 0) {
				dateEls.select("span").remove();
				strDate = dateEls.get(0).text();
			}
			strDate = strDate.replaceAll("Posted : ", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

//			//class명에 있는 -를 _로 변경한다.
//			Elements allEls = doc.getAllElements();
//			for(Element el:allEls) {
//				String className = el.className();
//				className = className.replace("-", "_");
//				el.attr("class",className);
//			}
			
			Elements article = doc.select(".article");
			System.out.println("article:[" + article+"]\n\n");
			
			
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");
			article.removeAttr("class");
			article.attr("style", "width:548px");

			Element authorElement = article.select("p").last();
			String author = "";
			if (authorElement != null) {
				authorElement.html();
			}
			System.out.println("author:[" + author + "]");

			// article.select("img").attr("style", "width:548px");
			article.select(".txt_caption.default_figure").attr("style", "width:548px");
			article.select("p").attr("style", "font-size:16px");
			article.select(".img-info").attr("style", "font-size:12px;font-weight:bold;");

			Elements imgEls = article.select(".comp-box figure img");
			imgEls.removeAttr("alt");
			for (Element imgEl : imgEls) {
				String imgSrc = imgEl.attr("src");
				if (imgSrc.startsWith("//")) {
					imgEl.attr("src", protocol + ":" + imgSrc);
					// 이미지 width,height 관련 스타일 문자열을 가져온다.
					String imageStyle = ImageUtil.getImageStyle(imgEl.attr("src"));
					imgEl.attr("style", imageStyle);
				}
				logger.debug("imgEl:" + imgEl);
			}

			article.select(".comp-box figure figcaption").tagName("div");
			article.select(".comp-box figure").tagName("div");

			String strContent = article.outerHtml();
			System.out.println("strContent:[" + strContent + "]");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			// System.out.println("strContent:[" + strContent + "]strContent");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			String copyright = article.select(".txt-copyright").html();
			System.out.println("copyright:" + copyright);

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");
			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:13px'>" + strDate + "</span><br><br>\n");
			sb1.append("<span style='font-size:13px'>" + author + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
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
