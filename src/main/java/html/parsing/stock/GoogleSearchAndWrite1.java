/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import html.parsing.stock.util.FileUtil;

/**
 *
 * @author parsing-25
 */
public class GoogleSearchAndWrite1 {

	public static void main(String args[]) throws MalformedURLException, IOException, FileNotFoundException {
		new GoogleSearchAndWrite1();
	}

	GoogleSearchAndWrite1() throws MalformedURLException, IOException, FileNotFoundException {
//        BufferedInputStream  bis = (BufferedInputStream) this.getClass().getClassLoader().getResourceAsStream(".");
		URL curUrl = this.getClass().getClassLoader().getResource(".");
//        InputStream strm = url.openStream();
		System.out.println("path:" + curUrl.getPath());

		String userHome = System.getProperty("user.home");
		System.out.println("userDir:" + userHome);

		File currentDirFile = new File(".");
		String absolutePath = currentDirFile.getAbsolutePath();
		String currentDir = absolutePath.substring(0,
				absolutePath.length() - currentDirFile.getCanonicalPath().length());
		// this line may need a try-catch block
		System.out.println("currentDir:" + currentDir);

		Document doc = null;
		try {
			String searchWord = JOptionPane.showInputDialog("조회할 검색어를 입력하여 주세요");
			String strSourceUrl = "https://www.google.com/search?q=" + searchWord;
			Connection con = Jsoup.connect(strSourceUrl);
			doc = con.get();
			System.out.println("doc:[" + doc + "]");

			/*
			 * String userAgent = "Mozilla"; // get response. Connection.Response res =
			 * Jsoup.connect(strSourceUrl).method(Connection.Method.GET).followRedirects(
			 * false).userAgent(userAgent).execute(); // get cookies Map<String, String>
			 * loginCookies = res.cookies(); // get document doc =
			 * Jsoup.connect(strSourceUrl).cookies(loginCookies).userAgent(userAgent).get();
			 * System.out.println("doc:["+doc+"]");
			 */
			String targetFileName = FilenameUtils.getName(strSourceUrl);
			if (targetFileName.indexOf("?") != -1) {
				targetFileName = targetFileName.substring(0, targetFileName.indexOf("?"));
			}
			if (!targetFileName.contains(".") || !targetFileName.contains("htm")) {
				targetFileName = targetFileName + "(" + searchWord + ").html";
			}

			String targetUrl = userHome + File.separator + "Documents" + File.separator + targetFileName;
			System.out.println("targetUrl:" + targetUrl);

			String writeContent = doc.select("#search div div div div").get(0).html();
			FileUtil.fileWrite(targetUrl, writeContent);

		} catch (MalformedURLException ex) {
			java.util.logging.Logger.getLogger(GoogleSearchAndWrite1.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
