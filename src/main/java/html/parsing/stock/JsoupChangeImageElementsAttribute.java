/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.awt.image.BufferedImage;
import java.util.logging.Level;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author parsing-25
 */
public class JsoupChangeImageElementsAttribute {

	public static void changeImageElementsAttribute(Document doc, String protocol, String host, String path) {
		boolean resizeYn = true;
		String protocolHost = protocol + "://" + host;
		System.out.println("protocolHost:[" + protocolHost + "]");
		System.out.println("path:[" + path + "]");

		String filePath = "";
		if (path != null) {
			filePath = path.substring(0, path.lastIndexOf("/") + 1);
			System.out.println("filePath:[" + filePath + "]");
		}

		Elements imgs = doc.select("img");
		System.out.println("imgs:[" + imgs + "]");
		if (imgs != null) {
			System.out.println("width:[" + imgs.attr("width") + "]");
			System.out.println("height:[" + imgs.attr("height") + "]");
			String tempPath = "";
			String imgWidth = "";
			String imgHeight = "";
			int iImgWidth = 0;
			int iImgHeight = 0;

			for (Element img : imgs) {

				setImgAttr("src", protocol, protocolHost, filePath, tempPath, img, resizeYn);
				System.out.println("img--------->" + img);
				System.out.println("----------------------img.attr(src)3:" + img.attr("src"));
				
				img.attr("alt", "");
				img.attr("title", "");
				tempPath = path;
				System.out.println("tempPath:" + tempPath);
//                String strImg = img.toString();
//                strImg = strImg.replaceAll("data-src","datasrc");
//                Document docImg = Jsoup.parse(strImg);
//                System.out.println("docImg:"+docImg);
//                img = docImg.select("img").first();
//                System.out.println("Jsoup parse img 1:"+img);

				imgWidth = img.attr("width").replace("px", "");
				imgHeight = img.attr("height").replace("px", "");
				System.out.println("imgWidth:" + imgWidth);
				System.out.println("imgHeight:" + imgHeight);
				if (imgWidth.equals("")) {
					continue;
				}
				if (imgHeight.equals("")) {
					continue;
				}
				iImgWidth = Integer.parseInt(imgWidth);
				iImgHeight = Integer.parseInt(imgHeight);
				if (iImgWidth > 741) {
					iImgWidth = 741;
					iImgHeight = (741 * iImgHeight) / iImgWidth;
				}
				imgWidth = iImgWidth + "";
				imgHeight = iImgHeight + "";

				img.attr("width", imgWidth);
				img.attr("height", imgHeight);

			}
		}
		System.out.println("<===============imgs=================");
		System.out.println(imgs);
		System.out.println("==============imgs==================>");
	}

	public static void setImgAttr(String attrType, String protocol, String protocolHost, String filePath,
			String tempPath, Element img, boolean resizeYn) {
		java.util.logging.Logger.getLogger(JsoupChangeImageElementsAttribute.class.getName()).log(Level.SEVERE, "attrType:" + attrType);
		String src = img.attr(attrType);
		System.out.println("src====>" + src);
		String dataSrc = img.attr("datasrc");
		System.out.println("datasrc====>" + dataSrc);
		if (dataSrc != null && !dataSrc.equals("")) {
			src = dataSrc;
		}
		System.out.println(attrType + ":" + src);
		if (src.equals("")) {
			return;
		}
		// if(src.startsWith(".") || src.startsWith("..")){
		// src = protocol+"://"+host+path.substring(0,path.lastIndexOf("/")-1)+"/"+src;
		// }
		if (src.startsWith("/")) {
			if (src.startsWith("//")) {
				src = protocol + ":" + src;
				img.attr(attrType, src);
				System.out.println("//img====>" + img);
			} else {
				src = protocolHost + src;
				img.attr(attrType, src);
				System.out.println("/img====>" + img);
			}
		} else if (src.startsWith("../../")) {
			tempPath = tempPath.substring(0, tempPath.lastIndexOf("/") - 1);
			tempPath = tempPath.substring(0, tempPath.lastIndexOf("/") - 1);
			tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
			src = src.substring(src.indexOf("../../") + 6);
			src = protocolHost + tempPath + src;
			img.attr(attrType, src);
		} else if (src.startsWith("../")) {
			System.out.println("tempPath:" + tempPath);
			tempPath = tempPath.substring(0, tempPath.lastIndexOf("/") - 1);
			System.out.println("path1:" + tempPath);
			tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
			System.out.println("path2:" + tempPath);
			src = src.substring(src.indexOf("../") + 2);
			System.out.println("src1:" + src);
			src = protocolHost + tempPath + src;
			System.out.println("src2:" + src);
			img.attr(attrType, src);
		} else if (!src.startsWith("http") && String.valueOf(src.charAt(0)).matches("[a-zA-Z0-9]*")) {
			src = protocolHost + filePath + src;
			System.out.println("src3:" + src);
			img.attr(attrType, src);
		} else {
			System.out.println("src4:" + src);
			img.attr(attrType, src);
		}
		System.out.println("----------------------img.attr(src)1:" + img.attr("src"));
		if (!src.equals("") && !src.equals("#")) {
			if (src.indexOf(".jpg/") != -1) {
				src = src.substring(0, src.indexOf(".jpg/") + 4);
				System.out.println("src3:" + src);
			}
			// FileDownloader.downloadFile2(src);
			if (resizeYn) {
				BufferedImage resizedImg = GetUrlBufferedImage.getImage(src);
				System.out.println("resizedImg:" + resizedImg);
				if (resizedImg != null) {
					int resizedImgWidth = resizedImg.getWidth();
					int resizedImgHeight = resizedImg.getHeight();
					img.removeAttr("width");
					img.removeAttr("height");
					img.removeAttr("class");
					img.removeAttr("style");
					System.out.println("resizedImgWidth:" + resizedImgWidth + " resizedImgHeight:" + resizedImgHeight);
					img.attr("width", resizedImgWidth + "");
					img.attr("height", resizedImgHeight + "");
				}
			}
		}
		System.out.println("----------------------img.attr(src)2:" + img.attr("src"));
	}
}
