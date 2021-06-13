import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.JsoupChangeAhrefElementsAttribute;
import html.parsing.stock.JsoupChangeImageElementsAttribute;
import html.parsing.stock.JsoupChangeLinkHrefElementsAttribute;
import html.parsing.stock.JsoupChangeScriptSrcElementsAttribute;
import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;

public class Nasdaq100FromMarketsBusinessinsiderCom extends News implements NewsInterface {

	
	private static Logger logger = LoggerFactory.getLogger(Nasdaq100FromMarketsBusinessinsiderCom.class);

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
		new Nasdaq100FromMarketsBusinessinsiderCom();
	}

	Nasdaq100FromMarketsBusinessinsiderCom() {

		readNews("110570", "넥솔론");
	}

	public void readNews(String stockCode, String stockName) {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			StringBuilder sb1 = new StringBuilder();

			Document doc = null;
			String url = "";
			url = "https://markets.businessinsider.com/index/components/nasdaq_100";
			doc = Jsoup.connect(url).get();
			getURL(url);
			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
//			Document doc = Jsoup.connect("https://markets.businessinsider.com/stocks/atvi-stock") .get();
//			System.out.println(doc.html());
			System.out.println(doc.select(".table").outerHtml());
			Element table = doc.select(".table").get(0);

			url = "https://markets.businessinsider.com/index/components/nasdaq_100?p=2";
			doc = Jsoup.connect(url).get();
			JsoupChangeAhrefElementsAttribute.changeAhrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeImageElementsAttribute.changeImageElementsAttribute(doc, protocol, host, path);
			JsoupChangeLinkHrefElementsAttribute.changeLinkHrefElementsAttribute(doc, protocol, host, path);
			JsoupChangeScriptSrcElementsAttribute.changeScriptSrcElementsAttribute(doc, protocol, host, path);
			System.out.println(doc.select(".table").outerHtml());
			Element tbody = doc.select(".table tbody").get(0);
			tbody.select("tr").get(0).remove();

			System.out.println("tbody.html:" + tbody.html());

			table.select("tbody").append(tbody.html());

			sb1.append(table.outerHtml());

			FileWriter fw = new FileWriter(
					USER_HOME + "\\documents\\" + this.getClass().getSimpleName() + "_" + strDate + ".html");
			fw.write(sb1.toString());
			fw.close();
			System.out.println("file write finished...");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
