/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Document;

/**
 *
 * @author parsing-25
 */
public class GoogleSearchAndWrite2 {

	public static void main(String args[]) throws MalformedURLException, IOException, FileNotFoundException {
		new GoogleSearchAndWrite2();
	}

	GoogleSearchAndWrite2() throws MalformedURLException, IOException, FileNotFoundException {
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

			String targetFileName = FilenameUtils.getName(strSourceUrl);
			if (targetFileName.indexOf("?") != -1) {
				targetFileName = targetFileName.substring(0, targetFileName.indexOf("?"));
			}
			if (!targetFileName.contains(".") || !targetFileName.contains("htm")) {
				targetFileName = targetFileName + "(" + searchWord + ").html";
			}

			String targetUrl = userHome + File.separator + "Documents" + File.separator + targetFileName;
			System.out.println("targetUrl:" + targetUrl);
			URL url = new URL(strSourceUrl);

			//InputStream in = new BufferedInputStream(url.openStream());
			//Exception in thread "main" java.io.IOException: Server returned HTTP response code: 403 for URL: https://www.google.com/search?q=브랜디
			//User-Agent를 세팅하지 않으면 403에러가 발생한다.

			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

			try (InputStream is = new BufferedInputStream(httpcon.getInputStream());
					OutputStream os = new BufferedOutputStream(new FileOutputStream(targetUrl));) {
				int readByte;

				while ((readByte = is.read()) != -1) {
					os.write(readByte);
				}
				os.flush();
				os.close();
			}
			System.out.println("file write finished");
		} catch (MalformedURLException ex) {
			java.util.logging.Logger.getLogger(GoogleSearchAndWrite2.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
