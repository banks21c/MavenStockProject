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
public class JsoupChangeScriptSrcElementsAttribute {

    public static void changeScriptSrcElementsAttribute(Document doc, String protocol, String host, String path) {
        String protocolHost = protocol + "://" + host;
        Elements scripts = doc.select("script");

        for (Element script : scripts) {
            System.out.println("script ===> " + script);
            String strScriptSrc = script.attr("src");
            System.out.println("strScriptSrc ===> " + strScriptSrc);
            if (!strScriptSrc.equals("")) {
                String downloadLink = "";
                if (strScriptSrc.startsWith("//")) {
                	script.attr("src", protocol+":"+ strScriptSrc);
                	downloadLink = protocol+":" + strScriptSrc;
                } else if (!strScriptSrc.startsWith("//") && strScriptSrc.startsWith("/")) {
                    script.attr("src", protocolHost + strScriptSrc);
                    downloadLink = protocolHost + strScriptSrc;
                } else if (strScriptSrc.startsWith("../../")) {
                    path = path.substring(0, path.lastIndexOf("/") - 1);
                    path = path.substring(0, path.lastIndexOf("/") - 1);
                    strScriptSrc = strScriptSrc.substring(strScriptSrc.indexOf("../../") + 6);
                    script.attr("src", protocolHost + path + strScriptSrc);
                    downloadLink = protocolHost + path + strScriptSrc;
                } else if (strScriptSrc.startsWith("../")) {
                    path = path.substring(0, path.lastIndexOf("/") - 1);
                    strScriptSrc = strScriptSrc.substring(strScriptSrc.indexOf("../") + 3);
                    script.attr("src", protocolHost + path + strScriptSrc);
                    downloadLink = protocolHost + path + strScriptSrc;
                } else if (strScriptSrc.startsWith("http")) {
                    script.attr("src", strScriptSrc);
                    downloadLink = strScriptSrc;
                } else {
                    script.attr("src", protocolHost + path + strScriptSrc);
                    downloadLink = protocolHost + path + strScriptSrc;
                }
                System.out.println("downloadLink:" + downloadLink);
                if (!downloadLink.equals("")) {
                	//downloadLink에 있는 파일을 다운로드합니다.
                    //FileDownloader.downloadFile2(downloadLink);
                }
            }
        }

    }
}
