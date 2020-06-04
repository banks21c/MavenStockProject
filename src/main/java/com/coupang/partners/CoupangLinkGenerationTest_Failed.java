package com.coupang.partners;


import html.parsing.stock.util.FileUtil;
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

public class CoupangLinkGenerationTest_Failed {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(CoupangLinkGenerationTest_Failed.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	DecimalFormat df = new DecimalFormat("###.##");
	String strUrl = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/1067381729/2014395921?group=rocket-fresh&product%5Btype%5D=PRODUCT&product%5BitemId%5D=2014395921&product%5BproductId%5D=1067381729&product%5BvendorItemId%5D=70014167606&product%5Bimage%5D=https%3A%2F%2Fthumbnail6.coupangcdn.com%2Fthumbnails%2Fremote%2F212x212ex%2Fimage%2Fretail%2Fimages%2F2019%2F12%2F06%2F15%2F9%2F3473bdce-a14b-41ba-902c-3e001bd03d71.jpg&product%5Btitle%5D=%ED%94%84%EB%9D%BC%EC%9D%B4%EB%93%9C%20%ED%9D%B0%EB%8B%A4%EB%A6%AC%20%EC%83%88%EC%9A%B0%EC%82%B4%20%28%EB%83%89%EB%8F%99%29%2C%20900g%20%2862~80%20%EB%A7%88%EB%A6%AC%29%2C%201%EA%B0%9C&product%5BdiscountRate%5D=12&product%5BoriginPrice%5D=21650&product%5BsalesPrice%5D=18900&product%5BdeliveryBadgeImage%5D=%2F%2Fimage6.coupangcdn.com%2Fimage%2Fbadges%2Ffalcon%2Fv1%2Fweb%2Frocket-fresh%402x.png";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CoupangLinkGenerationTest_Failed coupangLinkGenerationTest = new CoupangLinkGenerationTest_Failed();
	}

	CoupangLinkGenerationTest_Failed() {

		readPage();
	}

	public void readPage() {

		try {
			URL url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			Document doc = Jsoup.connect(strUrl).get();
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
			docHtml = docHtml.replace("\"//","\""+protocol+"://");
			docHtml = docHtml.replace("\"/","\""+protocol+"://"+host+"/");
			sb1.append(docHtml);

			String fileName = userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
