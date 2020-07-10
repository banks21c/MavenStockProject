package html.parsing.stock.quartz2x.example2;

import html.parsing.stock.focus.StockUnique_ReadTxtFile_ThreadCall;
import html.parsing.stock.focus.StockWeeks52NewLowHighPriceTodayOneFile;

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
		new StockWeeks52NewLowHighPriceTodayOneFile(strNidAut, strNidSes).start();
//		new StockPlusMinusDivide_ThreadCall(strNidAut,strNidSes).start();
		new StockPlusMinusDivide100(strNidAut, strNidSes).start();
	}

}
