/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coupang.partners;

import html.parsing.stock.util.FileUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
 *java.net.SocketTimeoutException: Read timed out
 * @author banks
 */
public class CoupangPartnersNpGoldBoxUrlConn_Failed {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(CoupangPartnersNpGoldBoxUrlConn_Failed.class);
	final static String userHome = System.getProperty("user.home");
	private URI uri = null;
	private String strUrl = "https://www.coupang.com/np/goldbox";

	public CoupangPartnersNpGoldBoxUrlConn_Failed() {
		initList();
	}

	void initList() {
		long start = System.currentTimeMillis();
		try {
//			Document doc = Jsoup.connect(strUrl).get();

			URL url;
			HttpURLConnection conn;
			BufferedReader br;
			String line;
			String result = "";
			Document doc;

			url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(60000);
			String charset = "UTF-8";
//			String charset = "EUC-KR";
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((line = br.readLine()) != null) {
				result += line + "\n";
			}
			br.close();
			logger.debug("result:" + result);
			doc = Jsoup.parse(result);

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
			Logger.getLogger(CoupangPartnersNpGoldBoxUrlConn_Failed.class.getName()).log(Level.SEVERE, null, ex);
		}
		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");		
	}

	public static void main(String args[]) {
		CoupangPartnersNpGoldBoxUrlConn_Failed coupangPartnersGoldBoxPageDownload = new CoupangPartnersNpGoldBoxUrlConn_Failed();
	}

}
