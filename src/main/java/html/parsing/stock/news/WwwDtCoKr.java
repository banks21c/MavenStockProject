package html.parsing.stock.news;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.StockUtil;
import html.parsing.stock.util.FileUtil;

public class WwwDtCoKr extends News {

	private static Logger logger = LoggerFactory.getLogger(WwwDtCoKr.class);

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
		new WwwDtCoKr(1);
	}

	WwwDtCoKr() {
		logger = LoggerFactory.getLogger(WwwDtCoKr.class);
	}

	WwwDtCoKr(int i) {
		logger = LoggerFactory.getLogger(WwwDtCoKr.class);
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://www.dt.co.kr/contents.html?article_no=2019061902101132049001";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
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
			doc.select("noscript").remove();
			doc.select("body").removeAttr("onload");
			doc.select("div.pop_prt_btns").remove();
			doc.select(".hidden-obj").remove();
			doc.select("#CSCNT").remove();
			doc.select(".news_like").remove();
			doc.select("#spiLayer").remove();
			doc.select(".sns_share").remove();

			strTitle = doc.select(".news_title .news_title_text h1").text();
			if (strTitle == null || strTitle.equals("")) {
				strTitle = doc.select(".news_title_text h1").text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			strDate = doc.select(".news_title_author .lasttime").text();
			if (strDate == null || strDate.equals("")) {
				strDate = doc.select(".news_body .news_date").text();
			}
			System.out.println("strDate:" + strDate);
			String[] strDates = strDate.split("\\|");
			strDate = strDates[0].trim();
			strDate = strDate.replaceAll("입력 : ", "");
			strDate = strDate.replaceAll("입력: ", "");
			strDate = strDate.replaceAll("입력 ", "");

			doc.select(".news_date").remove();

			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);

			String writer = doc.select(".news_title_author .author").text();
			System.out.println("writer:" + writer);

			Elements article = doc.select("#article_body");
			if (article == null || article.isEmpty()) {
				article = doc.select("#news_body_id");
				System.out.println("article14:[" + article + "]");
			}
			//System.out.println("article:[" + article+"]article");

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			System.out.println("articleHtml:[" + articleHtml + "]");
			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<img src=\"//", "<img src=\"" + protocol + "://");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("<figure>", "<div>");
			strContent = strContent.replaceAll("</figure>", "</div>");
			strContent = strContent.replaceAll("<figcaption>", "<div>");
			strContent = strContent.replaceAll("</figcaption>", "</div>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			//System.out.println("strContent:[" + strContent + "]strContent");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			String copyRight = "";

			sb1.append("<!doctype html>\r\n");
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			doc.select(".news_date").remove();

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>").append(writer).append("</span><br><br>\n");
			//sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append(copyRight).append("<br><br>\n");
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

	public static BufferedImage getImage(String src) {
		URL url;
		BufferedImage srcImg = null;
		BufferedImage destImg = null;
		try {
			url = new URL(src);
			srcImg = ImageIO.read(url);
			int width = srcImg.getWidth();
			int height = srcImg.getHeight();
			System.out.println("width1:" + width);
			System.out.println("height1:" + height);
			if (width > height && width > 548) {
				height = (548 * height) / width;
				width = 548;

				destImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = destImg.createGraphics();
				g.drawImage(srcImg, 0, 0, width, height, null);
			}
			width = destImg.getWidth();
			height = destImg.getHeight();
			System.out.println("width2:" + width);
			System.out.println("height2:" + height);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destImg;
	}

}
