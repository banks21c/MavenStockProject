/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

/**
 *
 * @author parsing-25
 */
public class JsoupChangeAhrefElementsAttribute {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JsoupChangeAhrefElementsAttribute.class);

	public static void changeAhrefElementsAttribute(Document doc, String protocol, String host, String path) {
		changeAhrefElementsAttribute(doc, protocol + "://" + host, path);
	}

	public static void changeAhrefElementsAttribute(Document doc, String protocolHost, String path) {

		Elements ahrefs = doc.select("a");
		System.out.println("protocolHost===>" + protocolHost);
		System.out.println("path===>" + path);

		String filePath = "";
		if (path != null) {
			if (path.length() == path.lastIndexOf("/") + 1) {
				path = path + "index.html";
			}
			System.out.println("path===>" + path);

			filePath = path.substring(0, path.lastIndexOf("/") + 1);
			System.out.println("filePath:[" + filePath + "]");
		}

		for (Element ahref : ahrefs) {
			String strAhref = ahref.attr("href");
			if (strAhref.contains("#")) {
				strAhref = strAhref.substring(0, strAhref.indexOf("#"));
			}
			if (strAhref.equals("")) {
				continue;
			}
			if (strAhref != null && !strAhref.startsWith("javascript")) {
				if (strAhref.startsWith("/")) {
					if (strAhref.startsWith("//")) {
						ahref.attr("href", "http:" + strAhref);
					} else {
						ahref.attr("href", protocolHost + strAhref);
					}
				} else if (strAhref.startsWith("../../")) {
					path = path.substring(0, path.lastIndexOf("/") - 1);
					path = path.substring(0, path.lastIndexOf("/") - 1);
					strAhref = strAhref.substring(strAhref.indexOf("../../") + 6);
					ahref.attr("href", protocolHost + path + strAhref);
				} else if (strAhref.startsWith("../")) {
					path = path.substring(0, path.lastIndexOf("/") - 1);
					strAhref = strAhref.substring(strAhref.indexOf("../") + 3);
					ahref.attr("href", protocolHost + path + strAhref);
				} else if (strAhref.startsWith("http") || strAhref.startsWith("mailto")) {
					ahref.attr("href", strAhref);
				} else if (!strAhref.startsWith("http") && String.valueOf(strAhref.charAt(0)).matches("[a-zA-Z0-9]*")) {
					ahref.attr("href", protocolHost + filePath + strAhref);
				} else {
					ahref.attr("href", protocolHost + path + strAhref);
				}
				logger.debug("ahref.attr href7===>" + ahref.attr("href"));
				if (!ahref.attr("href").equals("") && !ahref.attr("href").startsWith("mailto")) {
					// FileDownloader.downloadFile2(ahref.attr("href"));
				}
			}
		}
	}
}
