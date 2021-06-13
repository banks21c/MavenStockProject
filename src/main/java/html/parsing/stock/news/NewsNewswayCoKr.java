/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.news;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

/**
 *
 * @author parsing-25
 */
public class NewsNewswayCoKr extends News implements NewsInterface {

	static final String USER_HOME = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;
	static String strSubTitle = null;
	Logger logger = null;
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new NewsNewswayCoKr(1);
	}

	public StringBuilder createHTMLFile(String url) {
		return createHTMLFile(url, "");
	}

	public StringBuilder createHTMLFile(String url, String strMyComment) {
		getURL(url);
		System.out.println("protocol1:" + protocol);
		System.out.println("host1:" + host);
		System.out.println("path1:" + path);
		System.out.println("url:" + url);
		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			doc.select(".googlead").remove();

			System.out.println("a link:" + doc.select("a"));
			// System.out.println("doc:[" + doc+"]");
			strTitle = doc.select(".rl_pw h4").text();
			System.out.println("strTitle:[" + strTitle + "]");
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

			strSubTitle = doc.select(".subTitle").outerHtml();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElement = doc.select(".reporterBox dt a");
			String writer = writerElement.text();

			Elements timeElement = doc.select(".reporterBox span");
			String strDate = "";
			if (timeElement.size() > 0) {
				strDate = timeElement.get(0).text();
			}
			System.out.println("timeElement:" + timeElement);
			System.out.println("strDate:" + strDate);
			strDate = strDate.replaceAll("등록 :", "");
			System.out.println("strDate:" + strDate);
			strFileNameDate = strDate;

			strFileNameDate = StockUtil.getDateForFileName(strDate);
			System.out.println("strFileNameDate:" + strFileNameDate);
			/* 본문 */
			Elements article = doc.select("#articleText");
			// article.select(".image-area").append("<br><br>");
			article.select(".image-area").after("<br><br>");
			String style = article.select("#mArticle").attr("style");
			System.out.println("style:" + style);
			article.removeAttr("style");
			article.removeAttr("class");
			article.attr("style", "width:741px");
			// article.select("img").attr("style", "width:741px");
			article.select(".txt_caption.default_figure").attr("style", "width:741px");
			// System.out.println("imageArea:"+article.select(".image-area"));
			String strContent = article.html().replaceAll("640px", "741px");
			strContent = strContent.replaceAll("<p align=\"justify\"></p>", "<br><br>");
			strContent = strContent.replaceAll("<span style=\"font-size: 11pt;\"> </span>", "");
			strContent = strContent.replaceAll("figure", "div");
			strContent = strContent.replaceAll("figcaption", "div");
			strContent = StockUtil.makeStockLinkStringByTxtFile(StockUtil.getMyCommentBox(strMyComment) + strContent);
			
			// Elements copyrightElement = doc.select(".txt_copyright");
			// String copyright = copyrightElement.text();
			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox(strMyComment));

			sb1.append("<div style='width:741px'>\r\n");
			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<h3>" + strSubTitle + "</h3>\n");
			sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
			sb1.append("<span style='font-size:12px'>" + strDate + "</span><br><br>\n");
			sb1.append(strContent + "\n");
			// sb1.append(copyright);
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			FileWriter fw;

			String fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

	public NewsNewswayCoKr() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	public NewsNewswayCoKr(int i) {
		logger = LoggerFactory.getLogger(this.getClass());

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "http://www.edaily.co.kr/news/news_detail.asp?newsId=01918806619115112&mediaCodeNo=257";
		}
		createHTMLFile(url);
	}

}
