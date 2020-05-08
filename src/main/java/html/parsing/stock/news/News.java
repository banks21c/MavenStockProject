/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.news;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.GlobalVariables;
import html.parsing.stock.StockVO;

public class News {
	private static Logger logger = LoggerFactory.getLogger(News.class);

	private static String kospiFileName = GlobalVariables.kospiFileName;
	private static String kosdaqFileName = GlobalVariables.kosdaqFileName;
	private static List<StockVO> allStockList = new ArrayList<>();
	private static List<StockVO> kospiStockList = new ArrayList<>();
	private static List<StockVO> kosdaqStockList = new ArrayList<>();
	private static List<StockVO> searchStockList = new ArrayList<StockVO>();

	public static String protocol;
	public static String host;
	public static String path;
	public static String filePath;
	public static String protocolHost;

	public static String file;
	public final static String userHome = System.getProperty("user.home");

	public String getKospiFileName() {
		return kospiFileName;
	}

	public void setKospiFileName(String kospiFileName) {
		this.kospiFileName = kospiFileName;
	}

	public String getKosdaqFileName() {
		return kosdaqFileName;
	}

	public void setKosdaqFileName(String kosdaqFileName) {
		this.kosdaqFileName = kosdaqFileName;
	}

	public List<StockVO> getAllStockList() {
		return allStockList;
	}

	public void setAllStockList(List<StockVO> allStockList) {
		this.allStockList = allStockList;
	}

	public List<StockVO> getKospiStockList() {
		return kospiStockList;
	}

	public void setKospiStockList(List<StockVO> kospiStockList) {
		this.kospiStockList = kospiStockList;
	}

	public List<StockVO> getKosdaqStockList() {
		return kosdaqStockList;
	}

	public void setKosdaqStockList(List<StockVO> kosdaqStockList) {
		this.kosdaqStockList = kosdaqStockList;
	}

	public List<StockVO> getSearchStockList() {
		return searchStockList;
	}

	public void setSearchStockList(List<StockVO> searchStockList) {
		this.searchStockList = searchStockList;
	}

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
			System.out.println("host1 :" + host);
			System.out.println("path :" + path);
			System.out.println("path1 :" + path + " strUrl.indexOf(path)  :" + strUrl.indexOf(path));
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
		getURL("http://www.yonhapnews.co.kr/economy/2013/04/07/0318000000AKR20130407057100002.HTML");
	}
}
