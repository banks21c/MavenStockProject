package html.parsing.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllStockForeignOrganDailySimpleAll30_ThreadCall {

	private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganDailySimpleAll30_ThreadCall.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new AllStockForeignOrganDailySimpleAll30_ThreadCall();
	}

	AllStockForeignOrganDailySimpleAll30_ThreadCall() {
		// 주간 거래일을 알아낸다.
		// getFirstLastDayOfWeek();

		// test();

		AllStockForeignOrganDailySimpleAll30_Thread thread1 = new AllStockForeignOrganDailySimpleAll30_Thread("코스피");
		thread1.start();

		AllStockForeignOrganDailySimpleAll30_Thread thread2 = new AllStockForeignOrganDailySimpleAll30_Thread("코스닥");
		thread2.start();
		
	}

}
