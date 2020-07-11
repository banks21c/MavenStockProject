package html.parsing.stock.quartz2x.example2;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockMarketPriceRunJob extends Thread implements Job {

	String strNidAut;
	String strNidSes;
	private static Logger logger = LoggerFactory.getLogger(StockMarketPriceRunJob.class);

	public StockMarketPriceRunJob() {
	}

	public StockMarketPriceRunJob(String strNidAut, String strNidSes) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("SimpleJob says: " + jobKey + " executing at " + new Date());

		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		StockMarketPriceRunJFrame jobThread = new StockMarketPriceRunJFrame(strNidAut, strNidSes);
		jobThread.start();
	}

}
