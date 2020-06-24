package html.parsing.stock.dividends;

public class KoreaStockDividendsThreadCall {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		KoreaStockDividendsThreadCall list1 = new KoreaStockDividendsThreadCall();
	}

	KoreaStockDividendsThreadCall() {
		KoreaStockDividendsThread thread1 = new KoreaStockDividendsThread("kospi");
		thread1.start();
		KoreaStockDividendsThread thread2 = new KoreaStockDividendsThread("kosdaq");
		thread2.start();
	}
}
