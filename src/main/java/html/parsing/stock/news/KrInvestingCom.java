package html.parsing.stock.news;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.StockUtil;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.ImageUtil;

public class KrInvestingCom extends News {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(KrInvestingCom.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new KrInvestingCom(1);
	}

	KrInvestingCom() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	KrInvestingCom(int i) {
		logger = LoggerFactory.getLogger(this.getClass());
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		logger.debug("url:[" + url + "]");
		if (url.equals("")) {
			url = "https://kr.investing.com/analysis/article-200432050";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		logger.debug("url:" + url);
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

//			SSLContext sc = SSLContext.getInstance("TLS");
//			SSLContext sc = SSLContext.getInstance("TLSv1");
			SSLContext sc = SSLContext.getInstance("TLSv1.1");
//			SSLContext sc = SSLContext.getInstance("TLSv1.2");

			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			System.setProperty("https.protocols", "TLSv1.1");
			System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

			doc = Jsoup.connect(url).get();
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select(".news_rel").remove();
			doc.select(".news_like").remove();
			doc.select("#news_rel_id").remove();
			doc.select(".news_copyright_links").remove();
			doc.select(".copy_bottom").remove();
//제목
			strTitle = doc.select("H1.articleHeader").html();
			logger.debug("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = strTitleForFileName.replaceAll(":", "-");
			strTitleForFileName = strTitleForFileName.replaceAll(" ", "_");
			strTitleForFileName = strTitleForFileName.replaceAll("\"", "'");
			strTitleForFileName = strTitleForFileName.replaceAll("\\?", "§");
			logger.debug("strFileNameTitle:" + strTitleForFileName);
//날짜
			strDate = doc.select(".contentSectionDetails>SPAN, .contentSectionDetails>A").text();
			strDate = strDate.replace("주식 시장", "").trim();
			logger.debug("strDate:" + strDate);
			if (strDate.contains("|")) {
				String strDateArray[] = strDate.split("\\|");
				strDate = strDateArray[0].trim();
			}
			logger.debug("strDate:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = strFileNameDate.replaceAll(" ", "_");
			strFileNameDate = strFileNameDate.replaceAll(":", ".");
			strFileNameDate = "[" + strFileNameDate + "]";
			logger.debug("strFileNameDate:" + strFileNameDate);
//작자
			Elements title_author_2011 = doc.select(".contentSectionDetails i a");
			logger.debug("title_author_2011:" + title_author_2011);
			String strAuthor = title_author_2011.text();
			logger.debug("author:" + strAuthor);

			Elements article = doc.select(".WYSIWYG.articlePage");
			logger.debug("article:" + article);
			article.select(".news_body .news_date").remove();

			Elements imgEls = article.select("img");
			for(Element imgEl:imgEls){
				String imgUrl = imgEl.attr("src");
				logger.debug("imgUrl:"+imgUrl);
				if(!imgUrl.startsWith("http")) {
					if(imgUrl.startsWith("//")) {
						imgUrl = protocol+":"+imgUrl;
					}else {					
						imgUrl = getProtocolHost()+imgUrl;
					}
				}				
				imgEl = ImageUtil.getImageWithStyle(imgEl,imgUrl);
				logger.debug("imgEl:"+imgEl);
			}

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			logger.debug("articleHtml:[" + articleHtml + "]articleHtml");

			String copyright = doc.select(".news_copyright").outerHtml();
			logger.debug("copyright:" + copyright);

			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<figure>", "");
			strContent = strContent.replaceAll("\"/", "\""+getProtocolHost()+"/");
			strContent = strContent.replaceAll("</figure>", "<br>");
			strContent = strContent.replaceAll("<figcaption>", "");
			strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			strContent = StockUtil.makeStockLinkStringByTxtFile(strContent);

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("<style>\r\n");
			sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
			sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
			sb1.append("</style>\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:14px'>").append(strAuthor).append("</span><br><br>\n");
			sb1.append("<span style='font-size:14px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");
			logger.debug("[sb.toString:" + sb1.toString() + "]");

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = "";

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			logger.debug("fileName:" + fileName);
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("추출완료");
		}
		return sb1;
	}

}
