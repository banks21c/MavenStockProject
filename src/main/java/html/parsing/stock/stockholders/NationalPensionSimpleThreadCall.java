package html.parsing.stock.stockholders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NationalPensionSimpleThreadCall {
	private static final Logger logger = LoggerFactory.getLogger(NationalPensionSimpleThreadCall.class);

	public static void main(String args[]) {
		NationalPensionSimpleThreadCall threadCall = new NationalPensionSimpleThreadCall();
		try {
			threadCall.readAndWriteMajorStockHolders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readAndWriteMajorStockHolders() throws Exception {
		// 프로그램 실행 시작 시간
		long start = System.currentTimeMillis();
		
		String currentThreadName = Thread.currentThread().getName();
		System.out.println("[" + currentThreadName + "]" + " thread starts here...");

		NationalPensionSimpleThread thread1 = new NationalPensionSimpleThread("kospi");
		thread1.start();
		NationalPensionSimpleThread thread2 = new NationalPensionSimpleThread("kosdaq");
		thread2.start();

		thread1.join();
		System.out.println("[" + currentThreadName + "]" + " joined " + thread1.getName());

		thread2.join();
		System.out.println("[" + currentThreadName + "]" + " joined " + thread2.getName());

		System.out.println("[" + currentThreadName + "]" + " thread ends here...");

		// 프로그램 실행 종료 시간
		long end = System.currentTimeMillis();
		long timeElapsed = end - start;
		logger.debug("실행시간 : " + (end - start) / 1000 + "초");

		int second = (int) timeElapsed / 1000 % 60;
		int minute = (int) timeElapsed / (1000 * 60) % 60;
		int hour = (int) timeElapsed / (1000 * 60 * 60);

		logger.debug("실행시간 : " + hour + " 시간 " + minute + " 분 " + second + " 초");

	}

}
