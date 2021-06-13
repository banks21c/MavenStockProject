
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;

public class URLDecodeTest extends News implements NewsInterface {

//	String strEncoded = "https://ko.wikipedia.org/wiki/다우_존스_산업평균지수";
//	String strEncodedUrl = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/27613130/367373530?group=most-sold&product%5Btype%5D=PRODUCT&product%5BitemId%5D=367373530&product%5BproductId%5D=27613130&product%5BvendorItemId%5D=3892220508&product%5Bimage%5D=https%3A%2F%2Fthumbnail11.coupangcdn.com%2Fthumbnails%2Fremote%2F212x212ex%2Fimage%2Fretail%2Fimages%2F1417479973284-584e58d0-2470-44c6-ae8f-40129af8d5c4.jpg&product%5Btitle%5D=%EC%BF%A0%ED%8C%A1%20%EB%B8%8C%EB%9E%9C%EB%93%9C%20-%20%ED%83%90%EC%82%AC%EC%88%98%2C%20330ml%2C%2020%EA%B0%9C&product%5BdiscountRate%5D=30&product%5BoriginPrice%5D=6700&product%5BsalesPrice%5D=4690&product%5BdeliveryBadgeImage%5D=%2F%2Fimage15.coupangcdn.com%2Fimage%2Fbadges%2Frocket%2Frocket_logo.png";
//	String strEncodedUrl = "https%3A%2F%2Fm.blog.naver.com%2Fbohumman01%2F221436194062";

	String strEncodedUrl0 = "http%3A%2F%2Fblog.naver.com%2FLinkShare.nhn%3Furl%3Dhttps%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253DJ6zD3h_I3Lc%2526feature%253Dshare";
//	String strEncodedUrl = "http%3A%2F%2Fblog.naver.com%2FLinkShare.nhn%3Furl%3Dhttps%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253DJ6zD3h_I3Lc%2526feature%253Dshare";
	String strEncodedUrl = "%3Cspan%20id%3D%22se_object_1592901264185%22%20class%3D%22__se_object%22%20s_type%3D%22leverage%22%20s_subtype%3D%22oglink%22%20jsonvalue%3D%22%257B%2522url%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253DJ6zD3h_I3Lc%2526feature%253Dshare%2522%252C%2522domain%2522%253A%2522www.youtube.com%2522%252C%2522title%2522%253A%2522%25EC%25A3%25BD%25EC%259D%2584%2520%25EB%25BB%2594%2520%25ED%2595%259C%2520%25EC%2595%2584%25EA%25B8%25B0%2520%25EC%2588%2598%25EB%258B%25AC%25EC%259D%2584%2520%25EC%2582%25B4%25EB%25A0%25A4%25EC%25A4%25AC%25EB%258D%2594%25EB%258B%2588%2520%25EC%2583%259D%25EA%25B8%25B4%2520%25EC%259D%25BC%2520%25E3%2585%25A3%2520What%2520Happened%2520After%2520Rescuing%2520A%2520Nearly%2520Dying%2520Baby%2520Otter%2520Is..%2522%252C%2522description%2522%253A%2522%25ED%2595%2598%25EB%258A%2594%2520%25EC%25A7%2593%25EB%25A7%2588%25EB%258B%25A4%2520%25EB%25A9%258D%25EB%25AD%2589%25EB%25AF%25B8%2520%25EB%2584%2598%25EC%25B9%2598%25EB%258A%2594%2520%25EC%2588%2598%25EB%258B%25AC%2520%2526%252339%253B%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%2526%252339%253B%2520%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%25EA%25B0%2580%2520%25EC%2582%25AC%25EC%259C%25A1%25EC%2582%25AC%25EB%25A5%25BC%2520%25EB%2594%25B0%25EB%25A5%25B4%25EA%25B8%25B0%2520%25EC%258B%259C%25EC%259E%2591%25ED%2595%259C%2520%25EC%259D%25B4%25EC%259C%25A0%25EB%258A%2594..%2520%2523%25EB%258F%2599%25EB%25AC%25BC%25EB%2586%258D%25EC%259E%25A5%2520%2523%25EC%2595%25A0%25EB%258B%2588%25EB%25A9%2580%25EB%25B4%2590%25EC%2588%2598%25EB%258B%25AC%2520%2523%25EA%25B0%259C%25EC%2588%2598%25EB%258B%25AC%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%2520-------------------------------------------------%2520%25EC%2595%25A0%25EB%258B%2588%25EB%25A9%2580%25EB%25B4%2590%25EC%2599%2580%2520%25ED%2595%259C%25EB%25B0%25B0%25ED%2583%2580%25EA%25B3%25A0%25E2%259B%25B5%2520%25E2%2598%259E%2520https%253A%252F%252Fgoo.gl%252FWL9mGy%2520%25ED%2596%2587...%2522%252C%2522type%2522%253A%2522video%2522%252C%2522image%2522%253A%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252FJ6zD3h_I3Lc%252Fhqdefault.jpg%2522%252C%2522width%2522%253A480%252C%2522height%2522%253A360%257D%252C%2522allImages%2522%253A%255B%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252FJ6zD3h_I3Lc%252Fhqdefault.jpg%2522%252C%2522width%2522%253A480%252C%2522height%2522%253A360%257D%255D%252C%2522video%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fembed%252FJ6zD3h_I3Lc%2522%252C%2522site%2522%253A%2522YouTube%2522%252C%2522layoutType%2522%253A1%257D%22%3E%3C%2Fspan%3E%3Cbr%3E%EA%B7%80%EC%97%AC%EC%9B%8C%EC%9A%94...";

	// https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share

//	String strDecodedUrl = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/27613130/367373530?group=most-sold&product[type]=PRODUCT&product[itemId]=367373530&product[productId]=27613130&product[vendorItemId]=3892220508&product[image]=https://thumbnail11.coupangcdn.com/thumbnails/remote/212x212ex/image/retail/images/1417479973284-584e58d0-2470-44c6-ae8f-40129af8d5c4.jpg&product[title]=쿠팡 브랜드 - 탐사수, 330ml, 20개&product[discountRate]=30&product[originPrice]=6700&product[salesPrice]=4690&product[deliveryBadgeImage]=//image15.coupangcdn.com/image/badges/rocket/rocket_logo.png";
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
			strDecoded = URLDecoder.decode(strEncodedUrl0, "UTF8");
			logger.debug("strDecoded :" + strDecoded);
			// 두번디코드...
			strDecoded = URLDecoder.decode(strEncodedUrl, "UTF8");
			logger.debug("strDecoded1 :" + strDecoded);

			//한번 디코드 한다음에 select해야 된다.
			Document doc = Jsoup.parse(strDecoded);
			String jsonvalue = doc.select("span").attr("jsonvalue");
			logger.debug("jsonvalue:" + jsonvalue);
			String decodedJsonvalue = URLDecoder.decode(jsonvalue, "UTF8");
			logger.debug("decodedJsonvalue:" + decodedJsonvalue);
			
			JSONObject jobj = new JSONObject(decodedJsonvalue);
			Iterator it = jobj.keys();
			logger.debug("================");
			while(it.hasNext()) {
				String key = (String)it.next();
				Object valueObj = jobj.get(key);
				String value = "";
				if(valueObj instanceof String) {
					value = (String)valueObj;
				}else if(valueObj instanceof JSONObject) {
					value = valueObj.toString();
				}
				
				logger.debug(key+":"+value);
			}
			logger.debug("================");

			
			strDecoded = URLDecoder.decode(strDecoded, "UTF8");
			logger.debug("strDecoded2 :" + strDecoded);


			strEncoded = URLEncoder.encode(strEncodedUrl);
			logger.debug("strDecoded :" + strEncoded);
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