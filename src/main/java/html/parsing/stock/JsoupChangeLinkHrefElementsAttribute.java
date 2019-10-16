/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author parsing-25
 */
public class JsoupChangeLinkHrefElementsAttribute {

    public static void changeLinkHrefElementsAttribute(Document doc, String protocol, String host, String path) {
        String protocolHost = protocol + "://" + host;
        Elements links = doc.select("link");
        String tempPath = "";
        System.out.println("protocolHost ===> " + protocolHost);
        System.out.println("path ===> " + path);
        for (Element link : links) {
            tempPath = path;
            String strLinkHref = link.attr("href");
            String downloadLink = "";
            System.out.println("strLinkHref ===> " + strLinkHref);
            if(strLinkHref.equals("")) continue;
            if (strLinkHref.startsWith("/")) {
                link.attr("href", protocolHost + strLinkHref);
                downloadLink = protocolHost + strLinkHref;
            } else if (strLinkHref.startsWith("../../")) {
                tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                strLinkHref = strLinkHref.substring(strLinkHref.indexOf("../../") + 5);
                link.attr("href", protocolHost + tempPath + strLinkHref);
                downloadLink = protocolHost + tempPath + strLinkHref;
            } else if (strLinkHref.startsWith("../")) {
                System.out.println("tempPath1:" + tempPath);
                tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                tempPath = tempPath.substring(0, tempPath.lastIndexOf("/"));
                strLinkHref = strLinkHref.substring(strLinkHref.indexOf("../") + 2);
                System.out.println("tempPath2:" + tempPath);
                System.out.println("strLinkHref1:" + strLinkHref);
                link.attr("href", protocolHost + tempPath + strLinkHref);
                downloadLink = protocolHost + tempPath + strLinkHref;
            } else if (strLinkHref.startsWith("http")) {
                link.attr("href", strLinkHref);
                downloadLink = strLinkHref;
            } else if (!strLinkHref.startsWith("http") && String.valueOf(strLinkHref.charAt(0)).matches("[a-zA-Z0-9]*")) {
                strLinkHref = protocolHost + "/" + strLinkHref;
                link.attr("href", strLinkHref);
            } else {
                link.attr("href", protocolHost + tempPath + strLinkHref);
                downloadLink = protocolHost + tempPath + strLinkHref;
            }
            System.out.println("downloadLink:" + downloadLink);
            if (!downloadLink.equals("")) {
                //FileDownloader.downloadFile2(downloadLink);
            }
        }
    }
}
