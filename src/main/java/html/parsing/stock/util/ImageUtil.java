/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jsoup.nodes.Element;

/**
 *
 * @author parsing-25
 */
public class ImageUtil {
	public static void download(String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return;
		}

		URL url = null;
		InputStream in = null;
		OutputStream out = null;

		try {

			url = new URL(imgUrl);
			in = url.openStream();
			out = new FileOutputStream("C:/Temp/test.jpg"); // 저장경로

			int data = 0;
			while ((data = in.read()) != -1) {
				// 이미지를 쓴다.
				out.write(data);
			}
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
		System.out.println("image download finished");
	}

	public static BufferedImage getImageInfoFromUrl(String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return null;
		}

		URL url = new URL(imgUrl);
		BufferedImage image = ImageIO.read(url);
		int height = image.getHeight();
		int width = image.getWidth();
		System.out.println("Height : " + height);
		System.out.println("Width : " + width);
		return image;
	}

	public static BufferedImage getImageInfoFromUrlByReadBytes(String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return null;
		}

		URL url = new URL(imgUrl);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = url.openStream();
		byte[] b = new byte[2 ^ 16];
		int read = is.read(b);
		while (read > -1) {
			baos.write(b, 0, read);
			read = is.read(b);
		}
		int countInBytes = baos.toByteArray().length;
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//		Image image = ImageIO.read(bais);
		BufferedImage image = ImageIO.read(bais);
		int height = image.getHeight();
		int width = image.getWidth();
		System.out.println("Height : " + height);
		System.out.println("Width : " + width);
		return image;
	}

	public static String getImageStyle(String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return null;
		}
		URL url = new URL(imgUrl);
		BufferedImage image = ImageIO.read(url);
		int iHeight = image.getHeight();
		int iWidth = image.getWidth();
		int changeHeight = iHeight;
		int changeWidth = iWidth;
		System.out.println("iHeight : " + iHeight);
		System.out.println("iWidth : " + iWidth);

		if (iWidth > 548) {
			changeWidth = 548;
			changeHeight = 548 * iHeight / iWidth;
		}
		System.out.println("style : " + "width:" + changeWidth + "px;height:" + changeHeight + "px;");

		return "width:" + changeWidth + "px;height:" + changeHeight + "px;";
	}

	public static Element getImageWithStyle(Element img, String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return null;
		}
		URL url = new URL(imgUrl);
		BufferedImage image = ImageIO.read(url);
		int iHeight = image.getHeight();
		int iWidth = image.getWidth();
		int changeHeight = iHeight;
		int changeWidth = iWidth;
		System.out.println("iHeight : " + iHeight);
		System.out.println("iWidth : " + iWidth);

		if (iWidth > 548) {
			changeWidth = 548;
			changeHeight = 548 * iHeight / iWidth;
		}
		System.out.println("style : " + "width:" + changeWidth + "px;height:" + changeHeight + "px;");

		img.attr("style", "width:" + changeWidth + "px;height:" + changeHeight + "px;");
		return img;
	}

}
