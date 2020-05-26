import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.ImageUtil;

public class WwwRichousesComArticle {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(WwwRichousesComArticle.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	DecimalFormat df = new DecimalFormat("###.##");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WwwRichousesComArticle();
	}

	WwwRichousesComArticle() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			String strUrl = "https://www.richouses.com/worldwide/notpu-ta";
			Document doc = Jsoup.connect(strUrl).get();

			String header = doc.select(".entry-header h1").outerHtml();
			sb1.append(header);
			
			
			Element contentEl = doc.selectFirst(".entry-content");
			contentEl.select(".ad2").parents().remove();
			contentEl.select("div").removeAttr("style");
			
			Elements imgEls = contentEl.select("img");
			for(Element imgEl:imgEls) {
				imgEl.removeAttr("width");
				imgEl.removeAttr("height");
				String src = imgEl.attr("src");
				String style = ImageUtil.getImageStyle(src);
				imgEl.attr("style",style);
			}
			
			String content = contentEl.html();
			sb1.append(content);

			System.out.println(sb1.toString());

			FileWriter fw = new FileWriter(
					userHome + "\\documents\\" + this.getClass().getSimpleName() + " " + strDate + ".html");
			fw.write(Jsoup.parse(sb1.toString()).html());
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
