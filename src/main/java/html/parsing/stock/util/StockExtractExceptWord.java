/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import html.parsing.stock.model.StockVO;

/**
 *
 * @author parsing-25
 */
public class StockExtractExceptWord {

	public static boolean dupCheck(List<String> exceptWordList, String stockName, String strNews) {
		boolean isExceptWord = false;
		String strLineArray[];
		for (String strLine : exceptWordList) {
			strLineArray = strLine.split(":");
			String compareStockName = strLineArray[0];
			String exceptWord = strLineArray[1];
			System.out.println(
					"stockName:" + stockName + " compareSName:" + compareStockName + " exceptWord:" + exceptWord);
			if (stockName.equals(compareStockName) && strNews.contains(exceptWord)) {
				isExceptWord = true;
				System.out.println(
						"기사에 예외어가 있는가?" + (stockName.equals(compareStockName) && strNews.contains(exceptWord)));
				System.out.println("예외어는 무엇인가?" + exceptWord);
				break;
			}
		}
		return isExceptWord;
	}

	public static List<String> exceptWordList() {
		List<String> exceptWordList = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;

		try {
			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			File f = new File("./StockExtractExceptWord.txt");
//			System.out.println("f.exists1:"+f.exists());
			if (!f.exists()) {
				f = new File("/StockExtractExceptWord.txt");
//				System.out.println("f.exists2:"+f.exists());
			}
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

			String strLine = "";
			while ((strLine = br.readLine()) != null) {
				exceptWordList.add(strLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return exceptWordList;
	}

	public static void main(String args[]) throws IOException {
		String url = "https://www.etoday.co.kr/news/view/1921896";
		Document doc = Jsoup.connect(url).get();
		String html = doc.select(".view_contents").html();
		System.out.println("html:" + html);
		List<String> exceptWordList = exceptWordList();

		List<StockVO> stockList = StockUtil.readStockCodeNameList("코스닥");
		for (StockVO svo : stockList) {
			dupCheck(exceptWordList, svo.getStockName(), html);
		}
	}

}
