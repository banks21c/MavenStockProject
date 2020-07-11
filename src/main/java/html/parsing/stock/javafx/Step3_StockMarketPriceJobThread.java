package html.parsing.stock.javafx;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Step3_StockMarketPriceJobThread extends Thread implements Job {

	private static Logger logger = LoggerFactory.getLogger(Step3_StockMarketPriceJobThread.class);

	public Step3_StockMarketPriceJobThread() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("SimpleJob says: " + jobKey + " executing at " + new Date());
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		String strNidAut;
		String strNidSes;
		strNidAut = jobDataMap.getString("NID_AUT");
		strNidSes = jobDataMap.getString("NID_SES");
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		Step4_StockMarketPriceRun jobThread = new Step4_StockMarketPriceRun(strNidAut, strNidSes);
		jobThread.start();
	}

}
