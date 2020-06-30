package html.parsing.stock.focus;

public class StockUnique_ReadTxtFile_ThreadCall extends Thread {
	private String strNidAut;
	private String strNidSes;

	public static void main(String[] args) {
		new StockUnique_ReadTxtFile_ThreadCall();
	}

	StockUnique_ReadTxtFile_ThreadCall() {
		StockUnique_ReadTxtFile_Thread thread1 = new StockUnique_ReadTxtFile_Thread("kospi");
		thread1.start();

		StockUnique_ReadTxtFile_Thread thread2 = new StockUnique_ReadTxtFile_Thread("kosdaq");
		thread2.start();
	}

	public StockUnique_ReadTxtFile_ThreadCall(String strNidAut, String strNidSes) {
		System.out.println("strNidAut:"+strNidAut);
		System.out.println("strNidSes:"+strNidSes);
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	public void run() {
		System.out.println("thread run............");
		StockUnique_ReadTxtFile_Thread thread1 = new StockUnique_ReadTxtFile_Thread("kospi", strNidAut, strNidSes);
		thread1.start();

		StockUnique_ReadTxtFile_Thread thread2 = new StockUnique_ReadTxtFile_Thread("kosdaq", strNidAut, strNidSes);
		thread2.start();
	}
}