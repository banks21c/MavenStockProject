package com.coupang.partners;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.util.FileUtil;

/**
 *
 * @author banks
 */
public class CoupangPartnersWsBestGoldBoxPageDownload {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CoupangPartnersWsBestGoldBoxPageDownload.class);
	final static String userHome = System.getProperty("user.home");
	private String strUrl = "https://partners.coupang.com/#affiliate/ws/best/goldbox";

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
	String strDate = sdf.format(new Date());

	public static void main(String args[]) {
		new CoupangPartnersWsBestGoldBoxPageDownload();
	}

	public CoupangPartnersWsBestGoldBoxPageDownload() {
		initList();
	}

	void initList() {
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
		} catch (IOException ex) {
			Logger.getLogger(CoupangPartnersWsBestGoldBoxPageDownload.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
