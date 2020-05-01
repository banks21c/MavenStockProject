package html.parsing.stock.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;

public class URLDecodeTest{

	private static Logger logger = LoggerFactory.getLogger(URLDecodeTest.class);
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	static String strDefaultDate = sdf.format(new Date());

	static final String userHome = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = strDefaultDate;
	static String strTitle = null;

	public static void main(String[] args) {
		new URLDecodeTest(1);
	}

	public URLDecodeTest() {
	}

	public URLDecodeTest(int i) {
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=%EC%9D%B4%EB%9E%80+%26quot%3B%EC%9D%B4%EC%8A%A4%EB%9D%BC%EC%97%98+%EA%B0%80%EB%A3%A8%EB%A1%9C+%EB%A7%8C%EB%93%A4%EA%B2%A0%EB%8B%A4%26quot%3B+%EB%AF%B8%EA%B5%AD%EC%97%90+%EA%B2%BD%EA%B3%A0&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false";
		}
		createHTMLFile(url);
	}

	public static void createHTMLFile(String strUrl) {

		try {
			strUrl = URLDecoder.decode(strUrl, "UTF-8");
			System.out.println("url:" + strUrl);
//			JOptionPane.showInputDialog(this.getClass().getSimpleName()+" URL:",url);
//			JOptionPane.showMessageDialog(null, url);
			Connection con = Jsoup.connect(strUrl);
			Document doc = con.get();

			URL url = new URL(strUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String path = url.getPath();
			logger.debug("protocol:"+protocol);
			logger.debug("host:"+host);
			logger.debug("path:"+path);

			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			String fileName = userHome + "\\documents\\URLDecodeTest." + strDate + ".html";
			FileUtil.fileWrite(fileName, doc.html().replace("https://blog.naver.com", "https://nid.naver.com"));
			System.out.println("file write finished");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
//https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=%EC%9D%B4%EB%9E%80+%26quot%3B%EC%9D%B4%EC%8A%A4%EB%9D%BC%EC%97%98+%EA%B0%80%EB%A3%A8%EB%A1%9C+%EB%A7%8C%EB%93%A4%EA%B2%A0%EB%8B%A4%26quot%3B+%EB%AF%B8%EA%B5%AD%EC%97%90+%EA%B2%BD%EA%B3%A0&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false
//https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=이란 &quot;이스라엘 가루로 만들겠다&quot; 미국에 경고&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false
//https://blog.naver.com/openapi/share?serviceCode=share
//&url=https://www.hankyung.com/international/article/2020010646277
//&title=이란 &quot;이스라엘 가루로 만들겠다&quot; 미국에 경고
//&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4
//&timestamp=1578284088739
//&isMobile=false

}
