package html.parsing.stock;

public class StockUnique_ReadTxtFile_ThreadCall extends Thread {
	public static void main(String[] args) {
		new StockUnique_ReadTxtFile_ThreadCall();
	}

	StockUnique_ReadTxtFile_ThreadCall() {
		StockUnique_ReadTxtFile_Thread thread1 = new StockUnique_ReadTxtFile_Thread("kospi");
		thread1.start();

		StockUnique_ReadTxtFile_Thread thread2 = new StockUnique_ReadTxtFile_Thread("kosdaq");
		thread2.start();
	}
}