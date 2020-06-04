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

public class CoupangPartnersGoldBox {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(CoupangPartnersGoldBox.class);

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
		new CoupangPartnersGoldBox();
	}

	CoupangPartnersGoldBox() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();
			sb1.append("<h1>골드박스</h1>");

			String url = "https://partners.coupang.com/#affiliate/ws/best/goldbox";
			System.out.println("url:" + url);

			Document doc = Jsoup.connect(url).get();
			System.out.println("doc:" + doc);
			Element e1 = doc.select(".page-spin-container").get(0);
//			Element e1 = doc.select(".page-spin-container .ant-spin-container").get(0);
//			Element e1 = doc.select(".page-spin-container .ant-spin-container .product-list").get(0);

			Elements rowEls = e1.getElementsByTag(".product-row");

			for (int i = 0; i < rowEls.size(); i++) {
				Element rowEl = rowEls.get(i);

				Elements itemEls = rowEl.getElementsByTag(".product-item");
				for (int j = 0; j < itemEls.size(); j++) {
					Element itemEl = itemEls.get(j);
					System.out.println("itemEl:" + itemEl);
					sb1.append(itemEl.outerHtml());
				}
			}

			System.out.println(sb1.toString());

			FileWriter fw = new FileWriter(
					userHome + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html");
			fw.write(sb1.toString());
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
