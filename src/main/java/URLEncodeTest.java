
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;

public class URLEncodeTest extends News implements NewsInterface {

//	String strUrl = "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share";
//	String strUrl = "https%3A%2F%2Fwww.asiae.co.kr%2Farticle%2Fnationaldefense-diplomacy%2F2020062421382026021";
	String strUrl = "https://www.asiae.co.kr/article/nationaldefense-diplomacy/2020062421382026021";
	
	private static final Logger logger = LoggerFactory.getLogger(URLEncodeDecodeTest.class);
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {

		new URLEncodeTest();
	}

	URLEncodeTest() {
		String strEncoded;
		try {
			//한번 encode
			strEncoded= URLEncoder.encode(strUrl, "UTF8");
			logger.debug("strEncoded :" + strEncoded);
			//두번 encode
			strEncoded= URLEncoder.encode(strEncoded, "UTF8");
			logger.debug("strEncoded :" + strEncoded);
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(URLEncodeDecodeTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}