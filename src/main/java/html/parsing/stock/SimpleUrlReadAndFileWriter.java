package html.parsing.stock;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.FileUtil;

public class SimpleUrlReadAndFileWriter {

	private static final Logger logger = LoggerFactory.getLogger(SimpleUrlReadAndFileWriter.class);

	final static String userHome = System.getProperty("user.home");

	@Test
	public void urlRead() {
//		String url = "http://banks.blog.me/221871208427";
//		String url = "https://blog.naver.com/banks/221871208427?proxyReferer=";
//		String url = "https://blog.naver.com/banks/221871208427";
//		String url = "https://blog.naver.com/NBlogHidden.nhn?blogId=banks&musicYN=false";
//		String url = "https://blog.naver.com/PostView.nhn?blogId=banks&logNo=221871208427&redirect=Dlog&widgetTypeCall=true&directAccess=false";
		String url = "https://blog.naver.com/PostView.nhn?blogId=banks&logNo=221871208427&redirect=Dlog&widgetTypeCall=false&directAccess=true";
//		String url = "https://blog.naver.com/NVisitorgp4Ajax.nhn?blogId=banks";
//		String url = "https://blog.naver.com/mylog/WidgetListAsync.nhn?blogId=banks&listNumVisitor=5&isVisitorOpen=false&isBuddyOpen=true&selectCategoryNo=171&skinId=0&skinType=C&isCategoryOpen=true&isEnglish=true&listNumComment=5&areaCode=11B10101&weatherType=0&currencySign=ALL&enableWidgetKeys=title%2Cmenu%2Cprofile%2Ccalendar%2Csearch%2Ccategory%2Ctag%2Ccounter%2Cvisitorgp%2Cbuddy%2Cstat%2Cbuddyconnect%2Crss%2Ccontent%2Cgnb%2Cexternalwidget%2Cmusic&writingMaterialListType=1&calType=";
//		String url = "https://blog.naver.com/PostViewBottomTitleListAsync.nhn?blogId=banks&logNo=221871208427&viewDate=&categoryNo=171&parentCategoryNo=&showNextPage=false&showPreviousPage=false&sortDateInMilli=1585058433009&isThumbnailViewType=false&countPerPage=";
//		String url = "https://blog.naver.com/BlogTagListInfo.nhn?blogId=banks&logNoList=221871208427&logType=mylog";

		try {
			Document doc = Jsoup.connect(url).get();
			String title = doc.select("#post-area .post-top .htitle .pcol1").text();
			logger.debug("title:"+title);
			String html = doc.html();
			logger.debug("html:"+html);

			SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd] HH.mm.ss.SSS", Locale.KOREAN);
			String strDate = sdf.format(new Date());
			String fileName = userHome + "\\documents\\[" + strDate + "]_" + title + ".html";
			logger.debug("fileName:"+fileName);
			FileUtil.fileWrite(fileName, html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
