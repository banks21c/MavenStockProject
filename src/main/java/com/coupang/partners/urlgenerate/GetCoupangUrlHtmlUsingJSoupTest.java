package com.coupang.partners.urlgenerate;

import html.parsing.stock.util.FileUtil;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetCoupangUrlHtmlUsingJSoupTest {

//	String strUrl = "https://link.coupang.com/re/AFFSDP?lptag=AF5310383&pageKey=1388898744&itemId=2424448990&vendorItemId=70418508676&traceid=V0-113-b71d24f690135200";
	String strUrl = "https://www.coupang.com/vp/products/1388898744?itemId=2424448990&vendorItemId=70418508676&src=1139000&spec=10799999&addtag=400&ctag=1388898744&lptag=AF5310383&itime=20200610163618&pageType=PRODUCT&pageValue=1388898744&wPcid=15917558195989300468678&wRef=&wTime=20200610163618&redirect=landing&isAddedCart=";
	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(GetCoupangUrlHtmlUsingJSoupTest.class);

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
		new GetCoupangUrlHtmlUsingJSoupTest();
	}

	GetCoupangUrlHtmlUsingJSoupTest() {

		readUrl();
	}

	public void readUrl() {

		try {
			URL url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			Document doc = Jsoup.connect(strUrl).timeout(0).userAgent("Opera").get();

			System.out.println(doc.html());
			String docHtml = doc.html();
			docHtml = docHtml.replace("\"//", "\"" + protocol + "://");
			docHtml = docHtml.replace("\"/", "\"" + protocol + "://" + host + "/");

			docHtml = docHtml.replace("'//", "'" + protocol + "://");
			docHtml = docHtml.replace("'/", "'" + protocol + "://" + host + "/");
			
			sb1.append(docHtml);

			String fileName = userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
