
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

public class URLDecodeTest extends News {

//	String strEncoded = "https://ko.wikipedia.org/wiki/다우_존스_산업평균지수";
//	String strEncodedUrl = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/27613130/367373530?group=most-sold&product%5Btype%5D=PRODUCT&product%5BitemId%5D=367373530&product%5BproductId%5D=27613130&product%5BvendorItemId%5D=3892220508&product%5Bimage%5D=https%3A%2F%2Fthumbnail11.coupangcdn.com%2Fthumbnails%2Fremote%2F212x212ex%2Fimage%2Fretail%2Fimages%2F1417479973284-584e58d0-2470-44c6-ae8f-40129af8d5c4.jpg&product%5Btitle%5D=%EC%BF%A0%ED%8C%A1%20%EB%B8%8C%EB%9E%9C%EB%93%9C%20-%20%ED%83%90%EC%82%AC%EC%88%98%2C%20330ml%2C%2020%EA%B0%9C&product%5BdiscountRate%5D=30&product%5BoriginPrice%5D=6700&product%5BsalesPrice%5D=4690&product%5BdeliveryBadgeImage%5D=%2F%2Fimage15.coupangcdn.com%2Fimage%2Fbadges%2Frocket%2Frocket_logo.png";
	String strEncodedUrl = "https%3A%2F%2Fm.blog.naver.com%2Fbohumman01%2F221436194062";
	String strDecodedUrl = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/27613130/367373530?group=most-sold&product[type]=PRODUCT&product[itemId]=367373530&product[productId]=27613130&product[vendorItemId]=3892220508&product[image]=https://thumbnail11.coupangcdn.com/thumbnails/remote/212x212ex/image/retail/images/1417479973284-584e58d0-2470-44c6-ae8f-40129af8d5c4.jpg&product[title]=쿠팡 브랜드 - 탐사수, 330ml, 20개&product[discountRate]=30&product[originPrice]=6700&product[salesPrice]=4690&product[deliveryBadgeImage]=//image15.coupangcdn.com/image/badges/rocket/rocket_logo.png";

	private static final Logger logger = LoggerFactory.getLogger(URLEncodeDecodeTest.class);
	static String strCurrentDate = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS", Locale.KOREAN).format(new Date());
	String fileName = "";

	public static void main(String args[]) {

		new URLDecodeTest();
	}

	URLDecodeTest() {
		String strDecoded;
		String strEncoded;
		try {
			strDecoded = URLDecoder.decode(strEncodedUrl, "UTF8");
			logger.debug("dowJonesDecoder :" + strDecoded);
			strEncoded = URLEncoder.encode(strEncodedUrl);
			logger.debug("dowJonesEncoder0 :" + strEncoded);
			strEncoded = URLEncoder.encode(strEncodedUrl, "KSC5601");
			logger.debug("strDecoded1 :" + strEncoded);
			strEncoded = URLEncoder.encode(strEncodedUrl, "UTF8");
			logger.debug("strDecoded2 :" + strEncoded);
			strEncoded = URLEncoder.encode(strEncodedUrl, "MS949");
			logger.debug("strDecoded3 :" + strEncoded);
			strEncoded = URLEncoder.encode(strEncodedUrl, "ISO8859_1");
			logger.debug("strDecoded4 :" + strEncoded);

		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(URLEncodeDecodeTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}