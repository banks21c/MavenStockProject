package html.parsing.stock.focus;

public class StockPlusMinusDivide_ThreadCall extends Thread {
	public static void main(String[] args) {
		new StockPlusMinusDivide_ThreadCall();
	}

	StockPlusMinusDivide_ThreadCall() {
		StockPlusMinusDivide_Thread thread1 = new StockPlusMinusDivide_Thread("kospi");
		thread1.start();

		StockPlusMinusDivide_Thread thread2 = new StockPlusMinusDivide_Thread("kosdaq");
		thread2.start();
	}

	StockPlusMinusDivide_ThreadCall(String strNidAut, String strNidSes) {
		StockPlusMinusDivide_Thread thread1 = new StockPlusMinusDivide_Thread("kospi",strNidAut,strNidSes);
		thread1.start();

		StockPlusMinusDivide_Thread thread2 = new StockPlusMinusDivide_Thread("kosdaq",strNidAut,strNidSes);
		thread2.start();
	}
}