
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;
import html.parsing.stock.util.FileUtil;

public class JsoupMStockNaverCom extends News implements NewsInterface {

	private static Logger logger = LoggerFactory.getLogger(JsoupMStockNaverCom.class);
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E mm.ss.SSS ", Locale.KOREAN).format(new Date());
	static String strDate = null;
	static String strTitle = null;

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JsoupMStockNaverCom(1);
	}

	JsoupMStockNaverCom() {

	}

	JsoupMStockNaverCom(int i) {

		createHTMLFile();
	}

	public StringBuilder createHTMLFile() {
		StringBuilder sb1 = new StringBuilder();
		Document doc = null;
		String strTitleForFileName = "";
		String strFileNameDate = strYMD;
		try {

			String url = "https://m.stock.naver.com/item/main.nhn#/stocks/251270/total";
			strTitleForFileName = url.substring(url.indexOf("stocks") + "stocks".length() + 1, url.indexOf("/total"));

			url = "https://m.stock.naver.com/api/item/getTrendList.nhn?code=251270&size=5";
//			org.jsoup.UnsupportedMimeTypeException: 
//Unhandled content type. Must be text/*, application/xml, or application/xhtml+xml. 
//Mimetype=application/json;charset=UTF-8
//, URL=https://m.stock.naver.com/api/item/getTrendList.nhn?code=251270&size=5추출완료

			doc = Jsoup.connect(url).get();
			logger.debug("doc:" + doc.html());
			doc.select("script").remove();
			doc.select("iframe").remove();
			doc.select("gb_5e").remove();
			doc.select(".u47KMb").remove();

			String bodyHtml = doc.select("#knowledge-finance-wholepage__entity-summary").html();
			doc.select("body").html(bodyHtml);
			sb1.append(doc.select("body").html());
			doc.select("body").html(sb1.toString());
			logger.debug("doc:" + doc.html());

			File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = "";

			fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
					+ strTitleForFileName + ".html";
			FileUtil.fileWrite(fileName, doc.html());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("추출완료");
		}
		return sb1;
	}

}
