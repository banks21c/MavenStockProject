package html.parsing.stock.news;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.StockUtil;

public class FortuneCom500 extends News {

	private static Logger logger = LoggerFactory.getLogger(FortuneCom500.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ", Locale.KOREAN).format(new Date());
//	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FortuneCom500(1);
	}

	FortuneCom500() {

	}

	FortuneCom500(int i) {

		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://fortune.com/fortune500/2019/search/";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String strUrl) {
		System.out.println("url:" + strUrl);
		getURL(strUrl);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = strYMD;
		WebClient webClient = new WebClient();
		try {
//        	for(int i=1996;i<=2019;i++) {
//        		doc = Jsoup.connect("https://fortune.com/fortune500/"+i).get();
//        		logger.debug("doc:["+doc+"]");
//        		JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//        		JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//        		JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//        		JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
//        		
//        		String strContent = StockUtil.makeStockLinkStringByExcel(doc.html());
//        	}

			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getCookieManager().setCookiesEnabled(true);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			// Wait time
			webClient.waitForBackgroundJavaScript(15000);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getCookieManager().setCookiesEnabled(true);

			HtmlPage page = webClient.getPage(strUrl);

//			URL url = new URL("https://fortune.com/fortune500/2019/search/");
//			WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
//			HtmlPage page = webClient.getPage(requestSettings);

			// Wait
//			synchronized (page) {
//				try {
//					page.wait(15000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}

//			URL url = new URL("https://fortune.com/fortune500/2019/search/");
//			WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
//			HtmlPage page = webClient.getPage(requestSettings);

			// Parse logged in page as needed
			doc = Jsoup.parse(page.asXml());

			logger.debug("doc:[" + doc + "]");

//			Elements es = doc.select("ul");
			String strContent = StockUtil.makeStockLinkStringByExcel(doc.html());
			logger.debug("strContent:[" + strContent + "]");

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			// sb1.append("<meta http-equiv=\"Content-Type\"
			// content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + strUrl + "' target='_sub'>" + strUrl + "</a>] </h3>\n");
			sb1.append("<h2 id='title'>[" + strDate + "] " + strTitle + "</h2>\n");
			sb1.append("<span style='font-size:14px'>" + strDate + "</span><br><br>\n");
			sb1.append(strContent + "<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			System.out.println(sb1.toString());

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File f = new File(userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html");
			System.out.println("f:" + f);

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
			System.out.println("추출완료");
		}
		return sb1;
	}

}
