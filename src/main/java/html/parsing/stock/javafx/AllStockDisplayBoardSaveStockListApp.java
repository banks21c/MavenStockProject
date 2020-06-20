package html.parsing.stock.javafx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.FileUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AllStockDisplayBoardSaveStockListApp extends Application {

	final static String userHome = System.getProperty("user.home");
	private static Logger logger = LoggerFactory.getLogger(AllStockDisplayBoardSaveStockListApp.class);

	List<StockVO> kospiUniqueStockList = new ArrayList<>();
	List<StockVO> kosdaqUniqueStockList = new ArrayList<>();

	WebEngine webengine = null;

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("주식 전광판");

		WebView webView = new WebView();
		webengine = webView.getEngine();

		webengine.load("https://finance.daum.net/domestic/all_quotes");

		Button button1 = new Button("Save");
		button1.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED,
			new EventHandler<javafx.scene.input.MouseEvent>() { // Was missing the <MouseEvent>
			@Override
			public void handle(javafx.scene.input.MouseEvent event) {
				String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");

				System.out.println("html:" + html);
				saveHtml(html, "전광판");
			}
		;

		});

		VBox vBox = new VBox(webView, button1);
		Scene scene = new Scene(vBox, 1600, 900);
		primaryStage.setScene(scene);

		primaryStage.show();

	}

	public void saveHtml(String contentHtml, String title) {
		String strUrl = "https://finance.daum.net/domestic/all_quotes";
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd]", Locale.KOREAN);
		String strYmd = sdf.format(new Date());
		SimpleDateFormat sdf2 = new SimpleDateFormat("[yyyy-MM-dd_hhmmss]", Locale.KOREAN);
		String strYmdhms = sdf2.format(new Date());

		Document doc = Jsoup.parse(contentHtml);
		Elements marketEls = doc.select(".marketPrice .tabW ul li a");
		String market_ko = "";
		String market_en = "";
		for (Element marketEl : marketEls) {
			String marketClass = marketEl.attr("class");
			if (marketClass.equals("on")) {
				market_ko = marketEl.attr("title");
			}
		}
		System.out.println("market" + ":" + market_ko);
		if (market_ko.equals("코스피")) {
			market_en = "kospi";
		} else {
			market_en = "kosdaq";
		}
		Elements tableEls = doc.select("table");
		System.out.println("tableEls.size:" + tableEls.size());
		int stockCount = 0;
		int fundCount = 0;
		int preferredStockCount = 0;
		Map stockMap = new HashMap<>();
		for (Element tableEl : tableEls) {
			Elements trEls = tableEl.select("tr");
			for (Element trEl : trEls) {

				Elements tdEls = trEl.select("td");
				int tdCnt = 0;
				for (Element tdEl : tdEls) {
					logger.debug("tdEl:" + tdEl);
					if (tdCnt == 0) {
						Elements aEls = tdEl.select("a");
						if (aEls.size() >= 0) {
							Element firstAEl = aEls.first();
							System.out.println("firstAEl:" + firstAEl);
							if (firstAEl != null) {
								String strStockName = firstAEl.text();
								String onclick = firstAEl.attr("onclick");
								String strStockCode = onclick.substring(onclick.lastIndexOf("/") + 1);
								strStockCode = strStockCode.replace("'", "");
								if (strStockCode.startsWith("A")) {
									if (!strStockName.startsWith("ARIRANG")
										&& !strStockName.startsWith("KINDEX")
										&& !strStockName.startsWith("TIGER")
										&& !strStockName.startsWith("KBSTAR")
										&& !strStockName.startsWith("SMART")
										&& !strStockName.startsWith("KODEX")
										&& !strStockName.startsWith("TREX")
										&& !strStockName.startsWith("HANARO")
										&& !strStockName.startsWith("KOSEF")
										&& !strStockName.contains("코스피")
										&& !strStockName.contains("레버리지")
										&& !strStockName.contains("FOCUS")) {

										stockCount++;
										strStockCode = strStockCode.replace("A", "");
										System.out.println(strStockCode + "\t" + strStockName);
										stockMap.put(strStockCode, strStockName);
									}
								} else {
									fundCount++;
								}
								if (strStockCode.toLowerCase().contains("k")) {
//								System.out.println("우선주 "+strStockCode+"(" + strStockName+")");
									System.out.println("우선주 " + strStockName + "(" + strStockCode + ")");
									preferredStockCount++;
								}
							}

						}
					}

					if (tdCnt == 1) {
						String curPrice = tdEl.text();
						curPrice = curPrice.replace(",", "");
						int iCurPrice = Integer.parseInt(curPrice);
					}
					//specialLetter ↑↓ ▲ ▼ -
					if (tdCnt == 2) {
						String specialLetter = tdEl.select("i").text();
						tdEl.select("i").remove();
						String varyPrice = StringUtils.defaultIfEmpty(tdEl.text(), "0");
						logger.debug("varyPrice1:" + varyPrice);
						varyPrice = varyPrice.replace(",", "");
						logger.debug("varyPrice2:" + varyPrice);
						int iVaryPrice = Integer.parseInt(varyPrice);
					}
					if (tdCnt == 3) {
						String varyRatio = tdEl.text();
						logger.debug("varyRatio:" + varyRatio);
						varyRatio = varyRatio.replace("%", "");
						float fVaryRatio = Float.parseFloat(varyRatio);
					}
					tdCnt++;
				}
			}

		}
		System.out.println("stockCount:" + stockCount);
		System.out.println("fundCount:" + fundCount);
		System.out.println("preferredStockCount:" + preferredStockCount);
		System.out.println("stockCount- preferredStockCount:" + (stockCount - preferredStockCount));
		System.out.println("stockMap.size:" + stockMap.size());
		StringBuilder stockCodeNameSb = new StringBuilder();
		Set keySet = stockMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String strStockCode = (String) it.next();
			String strStockName = (String) stockMap.get(strStockCode);
			stockCodeNameSb.append(strStockCode + "\t" + strStockName + "\r\n");
		}
		//String jsonArray = JSONArray.toJSONString(stockVoList);
		//jsonArray=list
		List<Map> stockMapList = new ArrayList<>();
		stockMapList.add(stockMap);
		String jsonArray = JSONArray.toJSONString(stockMapList);
		//jsonObject=map
		String jsonObject = JSONObject.toJSONString(stockMap);
		String fileName = "";
//		fileName = userHome + "\\documents\\" + strYmdhms + "_" + market_en + "_list.json";
		fileName = market_en + "_list.json";
		FileUtil.fileWrite(fileName, jsonObject);
//		fileName = userHome + "\\documents\\" + strYmdhms + "_" + market_en + "_list.txt";
		fileName = market_en + "_list.txt";
		FileUtil.fileWrite(fileName, stockCodeNameSb.toString());
	}

}
