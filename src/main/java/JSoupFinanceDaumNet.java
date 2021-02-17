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

public class JSoupFinanceDaumNet {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(JSoupFinanceDaumNet.class);

	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);

	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";

	DecimalFormat df = new DecimalFormat("###.##");
	String className = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new JSoupFinanceDaumNet();
	}

	JSoupFinanceDaumNet() {
		className = this.getClass().getSimpleName();
		market();
	}

	public void market() {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());

			FileWriter fw = new FileWriter(userHome + "\\documents\\" + className + "." + strDate + ".html");
			StringBuilder sb1 = new StringBuilder();

			System.out.println("https://finance.daum.net/api/quotes/sectors?market=KOSPI");

			Document doc = Jsoup.connect("https://finance.daum.net/api/quotes/sectors?market=KOSPI").maxBodySize(0)
	                .timeout(0)
	                .get();
			System.out.println("doc :"+doc);

			System.out.println(sb1.toString());

			fw.write(sb1.toString());
			fw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
