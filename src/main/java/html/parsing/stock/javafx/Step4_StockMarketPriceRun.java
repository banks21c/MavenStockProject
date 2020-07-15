package html.parsing.stock.javafx;

import html.parsing.stock.focus.MStockWeeks52NewLowHighPriceOneFile;
import html.parsing.stock.focus.StockPlusMinusDivide100;
import html.parsing.stock.focus.StockUnique_ReadTxtFile_ThreadCall;

public class Step4_StockMarketPriceRun{

	String strNidAut = "";
	String strNidSes = "";

	public Step4_StockMarketPriceRun() {
//		execute();
	}

	public Step4_StockMarketPriceRun(String strNidAut, String strNidSes) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	public void execute() {
		start();
	}

	public void start() {
		new StockUnique_ReadTxtFile_ThreadCall(strNidAut, strNidSes).start();
		new MStockWeeks52NewLowHighPriceOneFile(strNidAut, strNidSes).start();
//		new StockPlusMinusDivide_ThreadCall(strNidAut,strNidSes).start();
		new StockPlusMinusDivide100(strNidAut, strNidSes).start();
	}

}
