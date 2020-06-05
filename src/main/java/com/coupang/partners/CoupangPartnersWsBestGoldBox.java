package com.coupang.partners;
import java.io.FileWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

public class CoupangPartnersWsBestGoldBox {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(CoupangPartnersWsBestGoldBox.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CoupangPartnersWsBestGoldBox();
	}

	CoupangPartnersWsBestGoldBox() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();
			sb1.append("<h1>골드박스</h1>");

			String url = "https://partners.coupang.com/#affiliate/ws/best/goldbox";
			System.out.println("url:" + url);

			Document doc = Jsoup.connect(url).get();
			System.out.println("doc:" + doc);
			
            URL u = new URL(url);
            String protocol = u.getProtocol();
            System.out.println("protocol:" + protocol);
            String host = u.getHost();
            System.out.println("host1:" + host);
            String path = u.getPath();
            System.out.println("path:" + path);
            
            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
            
            String docHtml = doc.html();
            docHtml = docHtml.replace("\"//", "\""+protocol+"://");
            docHtml = docHtml.replace("\"/", "\""+protocol+":/"+host+"/");
            docHtml = docHtml.replace("'//", "'"+protocol+"://");
            docHtml = docHtml.replace("'/", "'"+protocol+"://"+host+"/");
            
			sb1.append(docHtml);
			System.out.println(sb1.toString());

			FileWriter fw = new FileWriter(
					userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html");
			fw.write(sb1.toString());
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
