package com.coupang.partners;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;

public class CoupangPartnersWsBestGoldBox {

	private static Logger logger = LoggerFactory.getLogger(CoupangPartnersWsBestGoldBox.class);
	final static String userHome = System.getProperty("user.home");

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	String strUrl = "https://partners.coupang.com/#affiliate/ws/best/goldbox";

	static String strYMD = "";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
	String strDate = sdf.format(new Date());

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CoupangPartnersWsBestGoldBox();
	}

	CoupangPartnersWsBestGoldBox() {

		initList();
	}

	public void initList() {

		try {

			Document doc = Jsoup.connect(strUrl).get();

			URL url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String path = url.getPath();

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			String docHtml = doc.html();
			docHtml = docHtml.replace("\"//", "\"" + protocol + "://");
			docHtml = docHtml.replace("\"/", "\"" + protocol + "://" + host + "/");
			docHtml = docHtml.replace("'//", "'" + protocol + "://");
			docHtml = docHtml.replace("'/", "'" + protocol + "://" + host + "/");

			System.out.println(docHtml);
			String fileName = userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html";
			FileUtil.fileWrite(fileName, docHtml);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
