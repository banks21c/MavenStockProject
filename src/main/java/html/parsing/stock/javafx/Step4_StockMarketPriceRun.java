package html.parsing.stock.javafx;

import html.parsing.stock.focus.StockWeeks52NewLowHighPriceTodayOneFileV2;
import html.parsing.stock.focus.StockPlusMinusDivide100;
import html.parsing.stock.focus.StockUnique_ReadTxtFile_ThreadCall;

public class Step4_StockMarketPriceRun{

	String strBlogId = "";
	String strNidAut = "";
	String strNidSes = "";

	public Step4_StockMarketPriceRun() {
//		execute();
	}

	public Step4_StockMarketPriceRun(String strBlogId, String strNidAut, String strNidSes) {
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	public void execute() {
		start();
	}

	public void start() {
		new StockUnique_ReadTxtFile_ThreadCall(strBlogId, strNidAut, strNidSes).start();
		new StockWeeks52NewLowHighPriceTodayOneFileV2(strBlogId, strNidAut, strNidSes).start();
//		new StockPlusMinusDivide_ThreadCall(strBlogId, strNidAut,strNidSes).start();
		new StockPlusMinusDivide100(strBlogId, strNidAut, strNidSes).start();
	}

}
