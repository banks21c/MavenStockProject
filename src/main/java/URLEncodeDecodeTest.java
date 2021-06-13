
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;

public class URLEncodeDecodeTest extends News implements NewsInterface {

//	String strEncodedSample = "https://ko.wikipedia.org/wiki/다우_존스_산업평균지수";
//	String strEncodedSample = "https://ko.wikipedia.org/wiki/%EB%8B%A4%EC%9A%B0_%EC%A1%B4%EC%8A%A4_%EC%82%B0%EC%97%85%ED%8F%89%EA%B7%A0%EC%A7%80%EC%88%98";
	String strEncodedSample = "%E5%8C%97%2C%20%EC%A0%95%EA%B2%BD%EB%91%90%20%EA%B5%AD%EB%B0%A9%EC%9E%A5%EA%B4%80%EC%97%90%20%EA%B2%81%20%EB%A8%B9%EC%9D%80%20%EA%B0%9C%EA%B0%80%20%EB%8D%94%20%EC%9A%94%EB%9E%80%20%EA%B2%BD%EA%B3%A0";

	private static final Logger logger = LoggerFactory.getLogger(URLEncodeDecodeTest.class);
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {

		new URLEncodeDecodeTest();
	}

	URLEncodeDecodeTest() {
		String strDecoded;
		String strEncoded;
		try {
			strDecoded = URLDecoder.decode(strEncodedSample, "UTF8");
			logger.debug("strDecoded :" + strDecoded);
			
			strEncoded = URLEncoder.encode(strDecoded);
			logger.debug("strEncoded1 :" + strEncoded);
			strEncoded = URLEncoder.encode(" ");
			logger.debug("strEncoded2 :" + strEncoded);
			strEncoded = URLEncoder.encode(strDecoded, "KSC5601");
			logger.debug("strEncoded3 :" + strEncoded);
			strEncoded = URLEncoder.encode(strDecoded, "UTF8");
			logger.debug("strEncoded4 :" + strEncoded);
			strEncoded = URLEncoder.encode(strDecoded, "MS949");
			logger.debug("strEncoded5 :" + strEncoded);
			strEncoded = URLEncoder.encode(strDecoded, "ISO8859_1");
			logger.debug("strEncoded6 :" + strEncoded);

		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(URLEncodeDecodeTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}