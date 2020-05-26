
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

public class URLEncodeDecodeTest extends News {

//	String dowJones = "https://ko.wikipedia.org/wiki/다우_존스_산업평균지수";
	String dowJones = "https://ko.wikipedia.org/wiki/%EB%8B%A4%EC%9A%B0_%EC%A1%B4%EC%8A%A4_%EC%82%B0%EC%97%85%ED%8F%89%EA%B7%A0%EC%A7%80%EC%88%98";

	private static final Logger logger = LoggerFactory.getLogger(URLEncodeDecodeTest.class);
	final static String userHome = System.getProperty("user.home");
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {

		new URLEncodeDecodeTest();
	}

	URLEncodeDecodeTest() {
		String dowJonesDecoder;
		String dowJonesEncoder;
		try {
			dowJonesDecoder = URLDecoder.decode(dowJones, "UTF8");
			logger.debug("dowJonesDecoder :" + dowJonesDecoder);
			dowJonesEncoder = URLEncoder.encode(dowJones);
			logger.debug("dowJonesEncoder0 :" + dowJonesEncoder);
			dowJonesEncoder = URLEncoder.encode(dowJones, "KSC5601");
			logger.debug("dowJonesEncoder1 :" + dowJonesEncoder);
			dowJonesEncoder = URLEncoder.encode(dowJones, "UTF8");
			logger.debug("dowJonesEncoder2 :" + dowJonesEncoder);
			dowJonesEncoder = URLEncoder.encode(dowJones, "MS949");
			logger.debug("dowJonesEncoder3 :" + dowJonesEncoder);
			dowJonesEncoder = URLEncoder.encode(dowJones, "ISO8859_1");
			logger.debug("dowJonesEncoder4 :" + dowJonesEncoder);

		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(URLEncodeDecodeTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}