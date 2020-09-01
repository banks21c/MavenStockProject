package html.parsing.stock.stockholders;

public class NationalPensionSimpleThreadCall {

	public static void main(String args[]) {
		NationalPensionSimpleThreadCall threadCall = new NationalPensionSimpleThreadCall();
		try {
			threadCall.readAndWriteMajorStockHolders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readAndWriteMajorStockHolders() throws Exception {
		NationalPensionSimpleThread thread1 = new NationalPensionSimpleThread("kospi");
		thread1.start();
		NationalPensionSimpleThread thread2 = new NationalPensionSimpleThread("kosdaq");
		thread2.start();

	}


}
