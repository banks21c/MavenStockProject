package html.parsing.stock.news;

import html.parsing.stock.util.FileUtil;
import html.parsing.stock.StockUtil;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsFinanceNaverCom extends News {

	private static Logger logger = LoggerFactory.getLogger(NewsFinanceNaverCom.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	static String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ", Locale.KOREAN).format(new Date());
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new NewsFinanceNaverCom(1);
	}

	NewsFinanceNaverCom() {
		logger = LoggerFactory.getLogger(NewsFinanceNaverCom.class);
	}

	NewsFinanceNaverCom(int i) {
		logger = LoggerFactory.getLogger(NewsFinanceNaverCom.class);
		String url = JOptionPane.showInputDialog("naver news URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (url == null || url.equals("")) {
			url = "http://finance.naver.com/item/news_read.nhn?article_id=0004037813&office_id=008&code=192080&page=&sm=title_entity_id.basic";
		}
		createHTMLFile(url);
	}

	public static StringBuilder createHTMLFile(String url) {
		getURL(url);

		StringBuilder sb1 = new StringBuilder();
		Document doc;
		String strTitleForFileName = "";
		String strFileNameDate = "";
		try {
			doc = Jsoup.connect(url).get();
			System.out.println("view:[" + doc.select(".view") + "]");
			doc.select("iframe").remove();
			doc.select("script").remove();
			doc.select("#spiLayer").remove();
			doc.select(".sns_share").remove();

			strTitle = doc.select(".article_info h3").html();
			if(strTitle.equals("")){
				strTitle = doc.select(".p15").text();
			}
			System.out.println("title:" + strTitle);
			strTitleForFileName = strTitle;
			strTitleForFileName = StockUtil.getTitleForFileName(strTitleForFileName);
			System.out.println("strTitleForFileName:" + strTitleForFileName);

//			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
//			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
//			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
//			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);

			Elements writerElements = doc.select(".colm_ist");
			Element writerElement = null;
			String writer = "";
			if (writerElements.size() > 0) {
				writerElement = writerElements.get(0);
				if (writerElement != null) {
					writer = writerElement.text();
				}
			}

			Elements p11 = doc.select(".tah.p11");
			if (p11.size() > 0) {
				strDate = p11.get(0).text();
			} else {
				strDate = doc.select(".article_date").text();
			}
			System.out.println("strDate:" + strDate);
			String[] strDates = strDate.split("\\|");
			strDate = strDates[0].trim();
			System.out.println("time html:" + strDate);
			strFileNameDate = strDate;
			strFileNameDate = strFileNameDate.replaceAll("\\.", "-");
			strFileNameDate = StockUtil.getDateForFileName(strDate);
			strFileNameDate = strFileNameDate.split(" ")[0];
			System.out.println("strFileNameDate:" + strFileNameDate);

			// String author = doc.select(".news_title_author a").text();
			// System.out.println("author:" + author);
			Elements article = doc.select(".articleCont");
			if(article.html().equals("")){
				article = doc.select("#news_read");
			}
			logger.debug("articlehtml:"+article.html());
			article.select("#spiLayer").remove();
			article.select(".link_news").remove();

			article.select(".img_desc").before("<br>");

			article.select("figure").removeAttr("class");
			article.select("figcaption").removeAttr("class");
			article.select(".footer_btnwrap").remove();
			article.select(".hashtag").remove();
			article.select(".ad").remove();
			article.select("#BpromotionBanner").remove();
			System.out.println(article.select("figure img"));
			article.select("figure img").removeAttr("style");
			article.select("figure img").attr("style", "cursor:pointer;width:548px;height:365px");
			article.select("figure").tagName("div");
			article.select("figcaption").tagName("div");
			// System.out.println("article:" + article);

			article.attr("style", "width:548px");
			String articleHtml = article.outerHtml();
			System.out.println("articleHtml:[" + articleHtml + "]articleHtml");

			int indexOfCopyright = 	articleHtml.indexOf("ⓒ");
			String copyright = "";
			if(indexOfCopyright > 0){
				copyright = articleHtml.substring(indexOfCopyright);
			}
			System.out.println("copyright:" + copyright);

			String strContent = articleHtml.replaceAll("640px", "548px");
			strContent = strContent.replaceAll("<br> <br>", "\n<br>\n<br>");
			// strContent = strContent.replaceAll("<figure class=\"article_image\">", "");
			// strContent = strContent.replaceAll("</figure>", "<br>");
			// strContent = strContent.replaceAll("<figcaption class=\"caption\">", "");
			// strContent = strContent.replaceAll("</figcaption>", "<br>");
			strContent = strContent.replaceAll("<em>이미지 크게보기</em>", "");
			strContent = StockUtil.makeStockLinkStringByExcel(strContent);

			Elements copyRightElements = doc.select(".news_copyright");
			Element copyRightElement = null;
			String copyRight = "";
			if (copyRightElements.size() > 0) {
				copyRightElement = copyRightElements.first();
				copyRight = copyRightElement.text();
			} else {
				copyRightElements = doc.select("#newsView .copy");
				copyRightElement = copyRightElements.first();
				if (copyRightElement != null) {
					copyRight = copyRightElement.text();
				}
			}

			sb1.append("<html lang='ko'>\r\n");
			sb1.append("<head>\r\n");
			//sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
			sb1.append("</head>\r\n");
			sb1.append("<body>\r\n");

			sb1.append(StockUtil.getMyCommentBox());

			sb1.append("<div style='width:548px'>\r\n");

			doc.select(".news_date").remove();

			sb1.append("<h3> 기사주소:[<a href='" + url + "' target='_sub'>" + url + "</a>] </h3>\n");
			sb1.append("<h2>[").append(strDate).append("] ").append(strTitle).append("</h2>\n");
			sb1.append("<span style='font-size:12px'>" + writer + "</span><br>\n");
			sb1.append("<span style='font-size:12px'>").append(strDate).append("</span><br><br>\n");
			sb1.append(strContent).append("<br><br>\n");
			sb1.append(copyRight + "<br><br>\n");
			System.out.println("sb.toString:" + sb1.toString());
			sb1.append("</div>\r\n");
			sb1.append("</body>\r\n");
			sb1.append("</html>\r\n");

			File dir = new File(userHome + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

			fileName = userHome + File.separator + "documents" + File.separator + strFileNameDate + "_" + strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, sb1.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

	public static BufferedImage getImage(String src) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return destImg;
	}

}
