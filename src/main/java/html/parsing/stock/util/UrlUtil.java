/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtil {

	private static Logger logger = LoggerFactory.getLogger(UrlUtil.class);

	public static String protocol;
	public static String host;
	public static String path;
	public static String filePath;
	public static String protocolHost;

	public static String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public static URL getURL(String strUrl) {
		System.out.println("getUrl(String " + strUrl + ")");
		URL url = null;

		try {
			url = new URL(strUrl);
			String strUrlToString = url.toString();
			System.out.println(strUrlToString);
			System.out.println("authority :" + url.getAuthority());
//			System.out.println("getContent :" + url.getContent());

			// System.out.println("URI ASCIIString :" + url.toURI().toASCIIString());
			System.out.println("ExternalForm :" + url.toExternalForm());
			System.out.println("DefaultPort :" + url.getDefaultPort());
			System.out.println("Query :" + url.getQuery());
			System.out.println("Ref :" + url.getRef());
			System.out.println("UserInfo :" + url.getUserInfo());
			System.out.println("Port :" + url.getPort());
			System.out.println("DefaultPort :" + url.getDefaultPort());

			protocol = url.getProtocol();
			host = url.getHost();
			path = url.getPath();

			System.out.println("protocol :" + protocol);
			System.out.println("host :" + host);
			System.out.println("path :" + path);
			System.out.println("path :" + path + " strUrl.indexOf(path)  :" + strUrl.indexOf(path));
			file = url.getFile();
			System.out.println("file :" + file);
			filePath = file.substring(0, file.lastIndexOf("/") + 1);
			System.out.println("filePath :" + filePath);
			String ref = url.getRef();
			System.out.println("ref :" + ref);

			if (strUrl.indexOf(path) > 0) {
				protocolHost = strUrl.substring(0, strUrl.indexOf(path));
			} else {
				protocolHost = protocol + "://" + host;
			}
			System.out.println("protocolHost1 :" + protocolHost);

			URI uri = null;
			uri = url.toURI();
			String scheme = uri.getScheme();
			System.out.println("scheme :" + scheme);
			System.out.println("ASCIIString :" + uri.toASCIIString());
			System.out.println("Authority :" + uri.getAuthority());
			System.out.println("RawAuthority :" + uri.getRawAuthority());
			System.out.println("getPath :" + uri.getPath());
			System.out.println("getRawPath :" + uri.getRawPath());
			System.out.println("getRawSchemeSpecificPart :" + uri.getRawSchemeSpecificPart());
			System.out.println("getSchemeSpecificPart :" + uri.getSchemeSpecificPart());
			System.out.println("Fragment :" + uri.getFragment());
			System.out.println("getRawFragment :" + uri.getRawFragment());
			System.out.println("getRawQuery :" + uri.getRawQuery());
			System.out.println("getRawUserInfo :" + uri.getRawUserInfo());
			System.out.println("getUserInfo :" + uri.getUserInfo());

		} catch (MalformedURLException ex) {

			System.out.println(ex.getMessage());
		} catch (URISyntaxException ex) {

			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
		}
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHost() {
		return host;
	}

	public String getPath() {
		return path;
	}

	public String getFilePath() {
		return filePath;
	}

	public static String getProtocolHost() {
		return protocolHost;
	}

	public static void main(String args[]) {
//		getURL("http://www.yonhapnews.co.kr/economy/2013/04/07/0318000000AKR20130407057100002.HTML");
		getURL("https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/1067381729/2014395921?group=rocket-fresh&product%5Btype%5D=PRODUCT&product%5BitemId%5D=2014395921&product%5BproductId%5D=1067381729&product%5BvendorItemId%5D=70014167606&product%5Bimage%5D=https%3A%2F%2Fthumbnail6.coupangcdn.com%2Fthumbnails%2Fremote%2F212x212ex%2Fimage%2Fretail%2Fimages%2F2019%2F12%2F06%2F15%2F9%2F3473bdce-a14b-41ba-902c-3e001bd03d71.jpg&product%5Btitle%5D=%ED%94%84%EB%9D%BC%EC%9D%B4%EB%93%9C%20%ED%9D%B0%EB%8B%A4%EB%A6%AC%20%EC%83%88%EC%9A%B0%EC%82%B4%20%28%EB%83%89%EB%8F%99%29%2C%20900g%20%2862~80%20%EB%A7%88%EB%A6%AC%29%2C%201%EA%B0%9C&product%5BdiscountRate%5D=12&product%5BoriginPrice%5D=21650&product%5BsalesPrice%5D=18900&product%5BdeliveryBadgeImage%5D=%2F%2Fimage6.coupangcdn.com%2Fimage%2Fbadges%2Ffalcon%2Fv1%2Fweb%2Frocket-fresh%402x.png");
	}
}
