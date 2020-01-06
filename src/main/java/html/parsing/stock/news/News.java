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
		logger.debug("getUrl(String " + strUrl + ")");
		URL url = null;

		try {
			url = new URL(strUrl);
			String strUrlToString = url.toString();
			logger.debug(strUrlToString);
			logger.debug("authority :" + url.getAuthority());
			logger.debug("getContent :" + url.getContent());

			// logger.debug("URI ASCIIString :" + url.toURI().toASCIIString());
			logger.debug("ExternalForm :" + url.toExternalForm());
			logger.debug("DefaultPort :" + url.getDefaultPort());
			logger.debug("Query :" + url.getQuery());
			logger.debug("Ref :" + url.getRef());
			logger.debug("UserInfo :" + url.getUserInfo());
			logger.debug("Port :" + url.getPort());
			logger.debug("DefaultPort :" + url.getDefaultPort());
			protocol = url.getProtocol();
			host = url.getHost();
			path = url.getPath();
			logger.debug("protocol :" + protocol);
			logger.debug("host1 :" + host);
			logger.debug("path :" + path);
			logger.debug("path1 :" + path + " strUrl.indexOf(path)  :" + strUrl.indexOf(path));
			file = url.getFile();
			logger.debug("file :" + file);
			filePath = file.substring(0, file.lastIndexOf("/") + 1);
			logger.debug("filePath :" + filePath);
			String ref = url.getRef();
			logger.debug("ref :" + ref);

			if (strUrl.indexOf(path) > 0) {
				protocolHost = strUrl.substring(0, strUrl.indexOf(path));
			} else {
				protocolHost = protocol + "://" + host;
			}
			logger.debug("protocolHost1 :" + protocolHost);

			URI uri = null;
			uri = url.toURI();
			String scheme = uri.getScheme();
			logger.debug("scheme :" + scheme);
			logger.debug("ASCIIString :" + uri.toASCIIString());
			logger.debug("Authority :" + uri.getAuthority());
			logger.debug("RawAuthority :" + uri.getRawAuthority());
			logger.debug("getPath :" + uri.getPath());
			logger.debug("getRawPath :" + uri.getRawPath());
			logger.debug("getRawSchemeSpecificPart :" + uri.getRawSchemeSpecificPart());
			logger.debug("getSchemeSpecificPart :" + uri.getSchemeSpecificPart());
			logger.debug("Fragment :" + uri.getFragment());
			logger.debug("getRawFragment :" + uri.getRawFragment());
			logger.debug("getRawQuery :" + uri.getRawQuery());
			logger.debug("getRawUserInfo :" + uri.getRawUserInfo());
			logger.debug("getUserInfo :" + uri.getUserInfo());

		} catch (MalformedURLException ex) {

			logger.debug(ex.getMessage());
		} catch (URISyntaxException ex) {

			logger.debug(ex.getMessage());
		} catch (IOException ex) {
			logger.debug(ex.getMessage());
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

	public String getProtocolHost() {
		return protocolHost;
	}

	public static void main(String args[]) {
		new News().getURL("http://www.yonhapnews.co.kr/economy/2013/04/07/0318000000AKR20130407057100002.HTML");
	}
}
