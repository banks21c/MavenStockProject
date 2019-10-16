/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import html.parsing.stock.news.News;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author parsing-25
 */
public class URLFileDownloadTest {

	public static void main(String args[]) throws MalformedURLException, IOException, FileNotFoundException {
		new URLFileDownloadTest();
	}

	URLFileDownloadTest() throws MalformedURLException, IOException, FileNotFoundException {
//        BufferedInputStream  bis = (BufferedInputStream) this.getClass().getClassLoader().getResourceAsStream(".");
		URL curUrl = this.getClass().getClassLoader().getResource(".");
//        InputStream strm = url.openStream();
		System.out.println("path:" + curUrl.getPath());

		String userDir = System.getProperty("user.dir");
		System.out.println("userDir:" + userDir);

		File currentDirFile = new File(".");
		String helper = currentDirFile.getAbsolutePath();
		String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());//this line may need a try-catch block        
		System.out.println("currentDir:" + currentDir);

		try {
			//String strSourceUrl = "http://www.tutorialspoint.com/convert_webm_to_mp4.htm";
			//String strSourceUrl = "http://www.instagram.com";
			String strSourceUrl = "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage";
			Connection con = Jsoup.connect(strSourceUrl);
			Document doc = con.get();

			News gurl = new News();
			gurl.getURL(strSourceUrl);
			String protocol = gurl.getProtocol();
			String host = gurl.getHost();
			String path = gurl.getPath();
			String protocolHost = gurl.getProtocolHost();

			System.out.println("protocolHost ===> " + protocolHost);
			System.out.println("path ===> " + path);

//            JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocolHost, path);
//            JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocolHost, path);
//            JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocolHost, path);
//            JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocolHost, path);
			System.out.println("sourceUrl:" + strSourceUrl);
			String targetFileName = FilenameUtils.getName(strSourceUrl);
			if (targetFileName.indexOf("?") != -1) {
				targetFileName = targetFileName.substring(0, targetFileName.indexOf("?"));
			}
			if (!targetFileName.contains(".") || !targetFileName.contains("htm")) {
				targetFileName = targetFileName + ".html";
			}

			System.out.println("targetUrl:" + userDir + File.separator + targetFileName);
			URL url = new URL(strSourceUrl);
			try (InputStream is = new BufferedInputStream(url.openStream());
				OutputStream os = new BufferedOutputStream(
					new FileOutputStream(userDir + File.separator + targetFileName));) {
				int readByte;

				while ((readByte = is.read()) != -1) {
					os.write(readByte);
				}
				os.flush();
				os.close();
			}
		} catch (MalformedURLException ex) {
			Logger.getLogger(URLFileDownloadTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
