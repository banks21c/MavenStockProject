package html.parsing.stock.focus;

public class StockUnique_ReadTxtFile_ThreadCall extends Thread {
	private String strBlogId;
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

	public StockUnique_ReadTxtFile_ThreadCall(String strBlogId, String strNidAut, String strNidSes) {
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	public void run() {
		System.out.println("thread run............");
		StockUnique_ReadTxtFile_Thread thread1 = new StockUnique_ReadTxtFile_Thread("kospi", strBlogId, strNidAut,
				strNidSes);
		thread1.start();

		StockUnique_ReadTxtFile_Thread thread2 = new StockUnique_ReadTxtFile_Thread("kosdaq", strBlogId, strNidAut,
				strNidSes);
		thread2.start();
	}
}