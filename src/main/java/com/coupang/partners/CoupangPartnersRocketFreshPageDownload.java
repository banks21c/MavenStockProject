/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coupang.partners;

import html.parsing.stock.util.FileUtil;
import java.io.IOException;
import java.net.URI;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author banks
 */
public class CoupangPartnersRocketFreshPageDownload {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CoupangPartnersRocketFreshPageDownload.class);
	final static String userHome = System.getProperty("user.home");
	private URI uri = null;
	private String strUrl = "https://partners.coupang.com/#affiliate/ws/best/rocket-fresh";

	public CoupangPartnersRocketFreshPageDownload() {
		initList();
	}

	void initList() {
		try {
			Document doc = Jsoup.connect(strUrl).get();
			URL url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();

			Elements linkEls = doc.select("link");
			for (Element linkEl : linkEls) {
				String strHref = linkEl.attr("href");
				if (strHref.startsWith("//")) {
					linkEl.attr("href", protocol + ":" + strHref);
				} else if (strHref.startsWith("/")) {
					linkEl.attr("href", protocol + "://" + host + strHref);
				}
			}
			Elements scriptEls = doc.select("script");
			for (Element scriptEl : scriptEls) {
				String strSrc = scriptEl.attr("src");
				if (strSrc.startsWith("//")) {
					scriptEl.attr("src", protocol + ":" + strSrc);
				} else if (strSrc.startsWith("/")) {
					scriptEl.attr("src", protocol + "://" + host + strSrc);
				}
			}

			System.out.println(doc.html());
			String docHtml = doc.html();
			docHtml = docHtml.replace("\"//", "\"" + protocol + "://");
			docHtml = docHtml.replace("\"/", "\"" + protocol + "://" + host + "/");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());
			String fileName = userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html";
			FileUtil.fileWrite(fileName, docHtml);
		} catch (IOException ex) {
			Logger.getLogger(CoupangPartnersRocketFreshPageDownload.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String args[]) {
		CoupangPartnersRocketFreshPageDownload coupangPartnersRocketFreshPageDownload = new CoupangPartnersRocketFreshPageDownload();
	}

}
