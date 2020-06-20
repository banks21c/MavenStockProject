package html.parsing.stock.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class URLDecodeTest {

	private static Logger logger = LoggerFactory.getLogger(URLDecodeTest.class);
	String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
	int iYear = Integer.parseInt(strYear);
	DecimalFormat df = new DecimalFormat("###.##");

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
	static String strDefaultDate = sdf.format(new Date());

	static final String userHome = System.getProperty("user.home");
	// String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
	// Locale.KOREAN).format(new Date());
	static String strYMD = "";
	static String strDate = strDefaultDate;
	static String strTitle = null;

	public static void main(String[] args) {
		new URLDecodeTest();
		new URLDecodeTest(1);
	}

	public URLDecodeTest() {
		try {
			String s = "“1,500만 원이나 싸네!”";
			String s1 = URLEncoder.encode("“1,500만 원이나 싸네!”", "UTF8");
			System.out.println("s1:[" + s1 + "]");
			s1 = s1.replace("%E2%80%9C", "");
			s1 = s1.replace("%E2%80%9D", "");
			System.out.println("s11:[" + s1 + "]");
			String s2 = URLDecoder.decode(s1, "UTF8");

			System.out.println("s2:[" + s2 + "]");
		} catch (UnsupportedEncodingException ex) {
			java.util.logging.Logger.getLogger(URLDecodeTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public URLDecodeTest(int i) {
		String url = JOptionPane.showInputDialog(this.getClass().getSimpleName() + " URL을 입력하여 주세요.");
		System.out.println("url:[" + url + "]");
		if (StringUtils.defaultString(url).equals("")) {
			url = "https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=%EC%9D%B4%EB%9E%80+%26quot%3B%EC%9D%B4%EC%8A%A4%EB%9D%BC%EC%97%98+%EA%B0%80%EB%A3%A8%EB%A1%9C+%EB%A7%8C%EB%93%A4%EA%B2%A0%EB%8B%A4%26quot%3B+%EB%AF%B8%EA%B5%AD%EC%97%90+%EA%B2%BD%EA%B3%A0&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false";
			url = "https://partners.coupang.com/#affiliate/ws/linkgeneration/PRODUCT/1634311237/2787541793?group=goldbox&product%5Btype%5D=PRODUCT&product%5BitemId%5D=2787541793&product%5BproductId%5D=1634311237&product%5BvendorItemId%5D=3001811779&product%5Bimage%5D=https%3A%2F%2Fthumbnail10.coupangcdn.com%2Fthumbnails%2Fremote%2F212x212ex%2Fimage%2Fproduct%2Fimage%2Fvendoritem%2F2018%2F03%2F28%2F3001811779%2F70c8255f-da6d-4b56-b81b-d87678afbdd2.jpg&product%5Btitle%5D=%EC%BD%94%EB%A6%AC%EC%95%84%EB%B3%B4%EB%93%9C%EA%B2%8C%EC%9E%84%EC%A6%88%20%EC%BF%BC%EB%A6%AC%EB%8F%84%20%EC%B6%94%EC%83%81%EC%A0%84%EB%9E%B5%EA%B2%8C%EC%9E%84%2C%20%ED%98%BC%ED%95%A9%EC%83%89%EC%83%81&product%5BdiscountRate%5D=43&product%5BoriginPrice%5D=30100&product%5BsalesPrice%5D=28100&product%5BdeliveryBadgeImage%5D=%2F%2Fimage10.coupangcdn.com%2Fimage%2Fbadges%2Frocket%2Frocket_logo.png";
//			url = "https://www.facebook.com/dialog/share?app_id=441801156302191&href=https%3A%2F%2Fbanks.blog.me%2F221983808173&display=popup&enc=utf-8";
			url = "%3Cspan%20id%3D%22se_object_1592490330981%22%20class%3D%22__se_object%22%20s_type%3D%22leverage%22%20s_subtype%3D%22oglink%22%20jsonvalue%3D%22%257B%2522url%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253Dm2gv_a29Onw%2526feature%253Dshare%2522%252C%2522domain%2522%253A%2522www.youtube.com%2522%252C%2522title%2522%253A%2522*%25ED%2595%25A8%25EC%2597%25B0%25EC%25A7%2580%25EC%2599%2580%2520%25EB%2582%25A8%25ED%258E%25B8%25EC%259D%2598%2520%25EC%25BB%25A4%25ED%2594%258C%2520%25EB%2593%2580%25EC%2597%25A3%25F0%259F%2591%25A9%25F0%259F%258E%25A4%25F0%259F%25A7%2591*%2520%25EA%25B6%2581%25EA%25B8%2588%25ED%2595%25B4%25ED%2595%2598%25EC%2585%25A8%25EB%258D%2598%2520%25ED%2596%2584%25ED%258E%25B8%25EC%259D%2598%2520%25EB%2585%25B8%25EB%259E%2598%2520%25EC%258B%25A4%25EB%25A0%25A5%25EC%259D%2584%2520%25EA%25B3%25B5%25EA%25B0%259C%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4%25F0%259F%25A4%25A9%25E3%2585%25A3%25EB%25AE%25A4%25EC%25A7%2580%25EC%25BB%25AC%2520%25EC%25B0%25A8%25EB%25AF%25B8%2520-%2520%25EC%258A%25A4%25ED%2581%25AC%25EB%259E%2598%25EC%25B9%2598%2520(cover%2520by%2520%25ED%2596%2584%25EB%25B6%2580%25EB%25B6%2580)%25E3%2585%25A3%25ED%2596%2584%25EC%2597%25B0%25EC%25A7%2580%2520YONJIHAM%2522%252C%2522description%2522%253A%2522%2523%25ED%2596%2584%25EC%2597%25B0%25EC%25A7%2580%2520%2523%25ED%2596%2584%25ED%258E%25B8%2520%2523%25EB%2593%2580%25EC%2597%25A3%2520%25EC%2597%25AC%25EB%259F%25AC%25EB%25B6%2584%25EA%25BB%2598%2520%25EB%2593%259C%25EB%25A6%25AC%25EA%25B3%25A0%2520%25EC%258B%25B6%25EC%259D%2580%2520%25EB%25A7%2590%25EC%259D%25B4%2520%25EC%259E%2588%25EC%2596%25B4%25EC%259A%2594!%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EC%25A7%2580%25EC%25BC%259C%25EB%25B4%2590%25EC%25A3%25BC%25EC%258B%259C%25EA%25B3%25A0%2520%25EC%259D%2591%25EC%259B%2590%25ED%2595%25B4%25EC%25A3%25BC%25EC%2585%2594%25EC%2584%259C%2520%25EA%25B0%2590%25EC%2582%25AC%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4.%2520%25EC%2588%25AB%25EC%259E%2590%25EB%25A5%25BC%2520%25EC%2583%259D%25EA%25B0%2581%25ED%2595%2598%25EA%25B3%25A0%2520%25EC%258B%259C%25EC%259E%2591%25ED%2595%259C%2520%25EA%25B2%2583%25EC%259D%2580%2520%25EC%2595%2584%25EB%258B%2588%25EC%2597%2588%25EC%25A7%2580%25EB%25A7%258C%252010%25EB%25A7%258C%2520%25EB%25AA%2585%25EC%259D%25B4%25EB%259D%25BC%25EB%258A%2594%2520%25EB%25A7%258E%25EC%259D%2580%2520%25EB%25B6%2584%25EA%25BB%2598%25EC%2584%259C%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EA%25B5%25AC%25EB%258F%2585%25ED%2595%25B4%25EC%25A3%25BC%25EC%2585%2594%25EC%2584%259C%2520%25EA%25B9%259C%25EC%25A7%259D%2520%25EB%2586%2580%25EB%259E%2590%25EC%2596%25B4%25EC%259A%2594.%2520(%25EA%25B0%2580%25EC%25A1%25B1%25EB%2593%25A4%2520%25EB%25AA%25A8%25EB%2591%2590%2520%25EA%25B9%259C%25EC%25A7%259D%2520%25EB%2586%2580%25EB%259E%2590%25EC%2596%25B4%25EC%259A%2594!)%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EA%25B5%25AC%25EB%258F%2585%25ED%2595%25B4%25EC%25A3%25BC%25EC%258B%259C%25EA%25B3%25A0%2520%25EC%259D%2591%25EC%259B%2590%25ED%2595%25B4%25EC%25A3%25BC%25EC%258B%259C%25EB%258A%2594...%2522%252C%2522type%2522%253A%2522video%2522%252C%2522image%2522%253A%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252Fm2gv_a29Onw%252Fmaxresdefault.jpg%2522%252C%2522width%2522%253A1280%252C%2522height%2522%253A720%257D%252C%2522allImages%2522%253A%255B%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252Fm2gv_a29Onw%252Fmaxresdefault.jpg%2522%252C%2522width%2522%253A1280%252C%2522height%2522%253A720%257D%255D%252C%2522video%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fembed%252Fm2gv_a29Onw%2522%252C%2522site%2522%253A%2522YouTube%2522%252C%2522layoutType%2522%253A2%257D%22%3E%3C%2Fspan%3E%3Cbr%3Ebbbbbbbbbbbbbb";
		}

		createHTMLFile(url);
	}

	public static void createHTMLFile(String strUrl) {

		try {
			strUrl = URLDecoder.decode(strUrl, "UTF-8");

			Document doc = Jsoup.parse(strUrl);
			Element els = doc.select("span").first();
			System.out.println("els:" + els);

			String jsonvalue = els.attr("jsonvalue");
			System.out.println("jsonvalue:" + jsonvalue);
			jsonvalue = URLDecoder.decode(jsonvalue, "UTF-8");
			System.out.println("jsonvalue:" + jsonvalue);
			System.out.println("======================");
			
			JSONObject jobj = new JSONObject(jsonvalue);
			Set keySet = jobj.keySet();
			Iterator it = keySet.iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				Object value = jobj.get(key);
				System.out.println(key+":"+value);
			}
			

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
//https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=%EC%9D%B4%EB%9E%80+%26quot%3B%EC%9D%B4%EC%8A%A4%EB%9D%BC%EC%97%98+%EA%B0%80%EB%A3%A8%EB%A1%9C+%EB%A7%8C%EB%93%A4%EA%B2%A0%EB%8B%A4%26quot%3B+%EB%AF%B8%EA%B5%AD%EC%97%90+%EA%B2%BD%EA%B3%A0&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false
//https://blog.naver.com/openapi/share?serviceCode=share&url=https://www.hankyung.com/international/article/2020010646277&title=이란 &quot;이스라엘 가루로 만들겠다&quot; 미국에 경고&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4&timestamp=1578284088739&isMobile=false
//https://blog.naver.com/openapi/share?serviceCode=share
//&url=https://www.hankyung.com/international/article/2020010646277
//&title=이란 &quot;이스라엘 가루로 만들겠다&quot; 미국에 경고
//&token=d824605cb955bab9bdecd1846e6f91351722dfa515d981881273cf0581d9b4a4
//&timestamp=1578284088739
//&isMobile=false

}
