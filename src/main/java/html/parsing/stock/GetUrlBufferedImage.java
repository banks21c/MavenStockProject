package html.parsing.stock;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author parsing-25
 */
public class GetUrlBufferedImage {

	public static BufferedImage getImage(String src) {
		System.out.println("getImage src1:" + src);
		if (src == null || src.equals("")) {
			return null;
		}
		if (src.indexOf("?") != -1) {
			src = src.substring(0, src.indexOf("?"));
		}
		System.out.println("getImage src2:" + src);
		URL url;
		BufferedImage srcImg = null;
		BufferedImage destImg = null;
		try {
			url = new URL(src);
			System.out.println("url:" + url);
			srcImg = ImageIO.read(url);
			System.out.println("srcImg:" + srcImg);
			if (srcImg == null) {
				return null;
			}

			int width = srcImg.getWidth();
			int height = srcImg.getHeight();
			System.out.println("width1:" + width);
			System.out.println("height1:" + height);
			if (width > 741) {
				height = (741 * height) / width;
				width = 741;

				destImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = destImg.createGraphics();
				g.drawImage(srcImg, 0, 0, width, height, null);
			} else {
				destImg = srcImg;
			}
			width = destImg.getWidth();
			height = destImg.getHeight();
			System.out.println("width2:" + width);
			System.out.println("height2:" + height);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return destImg;
	}

}
