/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.Base64Util.Base64UtilDecoder;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;
import java.net.MalformedURLException;

/**
 *
 * @author parsing-25
 */
public class ImageUtil {

	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	public static String imageConvertCropAndResize(String fileAbsolutePath, String fileSaveAsNameSuffix, int reWidth,
		int reHeight) {

		String saveAsFilePath = null;

		try {
			Opener opener = new Opener();
			ImagePlus imp = opener.openImage(fileAbsolutePath);
			ImageProcessor ip = imp.getProcessor();
			StackProcessor sp = new StackProcessor(imp.getStack(), ip);

			int width = imp.getWidth();
			int height = imp.getHeight();

//            int cropWidth = 0;
//            int cropHeight = 0;
//            
//            if(width > height) {
//                cropWidth = height;
//                cropHeight = height;
//            } else {
//                cropWidth = width;
//                cropHeight = width;
//            }
//            
//            int x = -1;
//            int y = -1;
//            
//            if(width == height) {
//                x = 0;
//                y = 0;
//            } else if(width > height) {
//                x = (width - height) / 2;
//                y=0;
//            } else if (width < height) {
//                x = 0;
//                y = (height - width) / 2;
//            }
//
//            logger.debug("imp.getWidth() : ", imp.getWidth());
//            logger.debug("imp.getHeight() : " + imp.getHeight());
//            logger.debug("cropWidth : " + cropWidth);
//            logger.debug("cropHeight : " + cropHeight);
//            ImageStack croppedStack = sp.crop(x, y, cropWidth, cropHeight);            
//            imp.setStack(null, croppedStack);
			logger.debug("imp.getWidth(): " + imp.getWidth());
			logger.debug("imp.getHeight(): " + imp.getHeight());

			// sp = new StackProcessor(imp.getStack(), imp.getProcessor());
			ImageStack resizedStack = sp.resize(reWidth, reHeight, true);
			imp.setStack(null, resizedStack);
			StringBuffer filePath = new StringBuffer(fileAbsolutePath);
			filePath.replace(filePath.lastIndexOf("."), filePath.lastIndexOf("."), fileSaveAsNameSuffix);
			saveAsFilePath = filePath.toString();
			IJ.save(imp, saveAsFilePath);
			// return saveAsFilePath;
		} catch (Exception e) {
			logger.error("Error while resizing Image.");
			e.printStackTrace();
			return null;
		}

		return saveAsFilePath;
	}

	public static ByteArrayInputStream decodeBase64StringToByteArrayInputStream(String base64String)
		throws IOException {
		if (base64String.startsWith("\"data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1, base64String.length() - 1);
		} else if (base64String.startsWith("data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1);
		}
		byte[] bytearray = AndroidBase64.decode(base64String, AndroidBase64.DEFAULT);
		// byte[] bytearray = Base64.decodeBase64(base64String);
		return new ByteArrayInputStream(bytearray);
	}

	public static ByteArrayInputStream convertBase64StringToByteArrayInputStream(String base64String)
		throws IOException {
		Base64UtilDecoder decoder = Base64Util.getDecoder();
		if (base64String.startsWith("\"data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1, base64String.length() - 1);
		} else if (base64String.startsWith("data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1);
		}
		// byte[] bytearray = decoder.decode(base64String);
		byte[] bytearray = decoder.decode(base64String);
		return new ByteArrayInputStream(bytearray);
	}

	public static BufferedImage convertBase64StringToImage(String base64String) throws IOException {
		Base64UtilDecoder decoder = Base64Util.getDecoder();
		if (base64String.startsWith("\"data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1, base64String.length() - 1);
		} else if (base64String.startsWith("data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1);
		}
		System.out.println("base64String:" + base64String);
		byte[] bytearray = decoder.decode(base64String);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytearray));
		return image;
	}

	public static boolean convertBase64StringToImageFile(String base64String, String dirName, String fileName)
		throws IOException {
		Base64UtilDecoder decoder = Base64Util.getDecoder();
		if (base64String.startsWith("\"data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1, base64String.length() - 1);
		} else if (base64String.startsWith("data:image")) {
			base64String = base64String.substring(base64String.indexOf(',') + 1);
		}
		byte[] bytearray = decoder.decode(base64String);
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytearray));
		return ImageIO.write(image, "png", new File(dirName, fileName));
	}

	public static boolean isLandscape(BufferedImage image) {
		return image.getWidth() > image.getHeight();
	}

	public static void resize(File src, File dest, int width, int height) throws IOException {
		BufferedImage srcImg = ImageIO.read(src);

		int srcWidth = srcImg.getWidth();
		int srcHeight = srcImg.getHeight();
		int destWidth = 100;
		int destHeight = 100;
		BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = destImg.createGraphics();
		g.drawImage(srcImg, 0, 0, destWidth, destHeight, null);

		ImageIO.write(destImg, "jpg", dest);
	}

	public static void resizeImage(File src, File dest, int width, int height) throws IOException {
		Image srcImg = null;
		String suffix = src.getName().substring(src.getName().lastIndexOf('.') + 1).toLowerCase();
		if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
			srcImg = ImageIO.read(src);
		} else {
			// BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
			// 이렇게 하는 이유는 getScaledInstance를 통해 구한 이미지를
			// PixelGrabber.grabPixels로 리사이즈 할때
			// 빠르게 처리하기 위함이다.
			srcImg = new ImageIcon(src.toURI().toURL()).getImage();
		}

		Image imgTarget = srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		int pixels[] = new int[width * height];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, width, height, pixels, 0, width);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
		BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, width, height, pixels, 0, width);

		ImageIO.write(destImg, "jpg", dest);
	}

	public static BufferedImage resizeImage(BufferedImage bi, int width, int height) throws IOException {
		Image srcImg = bi;

		Image imgTarget = srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		int pixels[] = new int[width * height];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, width, height, pixels, 0, width);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
		BufferedImage destImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, width, height, pixels, 0, width);

		return destImg;
	}

	public static String imgToBase64String(final RenderedImage img, final String formatName) throws Exception {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			ImageIO.write(img, formatName, os);
			return Base64Util.getEncoder().encodeToString(os.toByteArray());
		} catch (final IOException ioe) {
			throw new Exception(ioe);
		}
	}

	public static void resize(File src, File dest, int width, int height, int RATIO) throws IOException {
		int SAME = -1;
		Image srcImg = null;
		String suffix = src.getName().substring(src.getName().lastIndexOf('.') + 1).toLowerCase();
		if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
			srcImg = ImageIO.read(src);
		} else {
			// BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
			// 이렇게 하는 이유는 getScaledInstance를 통해 구한 이미지를
			// PixelGrabber.grabPixels로 리사이즈 할때
			// 빠르게 처리하기 위함이다.
			srcImg = new ImageIcon(src.toURI().toURL()).getImage();
		}

		int srcWidth = srcImg.getWidth(null);
		int srcHeight = srcImg.getHeight(null);

		int destWidth = -1, destHeight = -1;

		if (width == SAME) {
			destWidth = srcWidth;
		} else if (width > 0) {
			destWidth = width;
		}

		if (height == SAME) {
			destHeight = srcHeight;
		} else if (height > 0) {
			destHeight = height;
		}

		if (width == RATIO && height == RATIO) {
			destWidth = srcWidth;
			destHeight = srcHeight;
		} else if (width == RATIO) {
			double ratio = ((double) destHeight) / ((double) srcHeight);
			destWidth = (int) ((double) srcWidth * ratio);
		} else if (height == RATIO) {
			double ratio = ((double) destWidth) / ((double) srcWidth);
			destHeight = (int) ((double) srcHeight * ratio);
		}

		Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
		int pixels[] = new int[destWidth * destHeight];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(e.getMessage());
		}
		BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);

		ImageIO.write(destImg, "jpg", dest);
	}

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

	public static BufferedImage getImage(String src) {
		if (src == null || src.equals("")) {
			return null;
		}
		if (src.indexOf("?") != -1) {
			src = src.substring(0, src.indexOf("?"));
		}
		URL url;
		BufferedImage srcImg = null;
		BufferedImage destImg = null;
		try {
			url = new URL(src);
			srcImg = ImageIO.read(url);
			int width = srcImg.getWidth();
			int height = srcImg.getHeight();
			System.out.println("width1:" + width);
			System.out.println("height1:" + height);
			if (width > height && width > 548) {
				height = (548 * height) / width;
				width = 548;

				destImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
				Graphics2D g = destImg.createGraphics();
				g.drawImage(srcImg, 0, 0, width, height, null);
			}
			width = destImg.getWidth();
			height = destImg.getHeight();
			System.out.println("width2:" + width);
			System.out.println("height2:" + height);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destImg;
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

	/**
	 * url에서 이미지 정보를 읽어와서 width와 height에 대한 style을 만들어 return한다.
	 *
	 * @param imgUrl
	 * @return
	 * @throws IOException
	 */
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

	/**
	 * url에서 이미지 정보를 읽어와서 width와 height를 org.jsoup.nodes.Element에 style 속성값에
	 * 세팅한다.
	 *
	 * @param img org.jsoup.nodes.Element
	 * @param imgUrl
	 * @return
	 * @throws IOException
	 */
	public static Element getImageWithStyle(Element img, String imgUrl) throws IOException {
		if (imgUrl.equals("")) {
			return null;
		}
		URL url = new URL(imgUrl);
		BufferedImage image = ImageIO.read(url);
		if (image != null) {
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
		}
		return img;
	}

}
