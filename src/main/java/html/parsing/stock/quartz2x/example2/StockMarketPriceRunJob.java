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

//		this.strNidAut = "/wvuIiNIdjodSe0A7Zdn6czwTTQyVa9rdzGre9mdEwDqND11/4JHayG3b+3qotvN";
//		this.strNidSes = "AAABjalpBOZTh89KH4uMNvAD+2YrGdj8ZUVycycAj32FIAzXmIV1aqiljLGi9ifYgdY3nYYJxG/F7XDlKvAPQ69fiwgGvHOt22Q24KT2BIAzUQ8iZeaIHk09JUJDxOtcjGAEJXmEYFIJCdKze++QT68KnrTHMD+T7y43LFhYi/8/IfjstJ1mfGmD3K0rbwOMLhyCjc9jZiejpwdex/0M6MhN4uxK7qYcwMpS4ZQLBqZjOu78bdQV/rhYYAGwOfwVJ2U9hMQmoHnFpXk2Z3Ft8eEJt6BIQxEg+a4m2YBIQmWzS0RTTFJ6wBDZrKK1e7s83ex8eG2hLqHRXZDIpl6xnlVobcJNROzL5va6F8VHyNDJdQh46G1e/fsuHxYjzeKfTpAMaYr/gStiTG/+t9Jcy5/is04bFNJjSp5ZVUbV3CC5N6JK74aWRGc9VHTMM174+hgv3KDYJ9mbgdfvYkxb9KhDPrPdxpzZTE709DNYpQxDY8YxvlxldZeMIRBxDdsTIw2WuFlpcU+QlPzWGwQVoyoKl1s=";
System.out.println("strNidAut:"+strNidAut);
System.out.println("strNidSes:"+strNidSes);
		StockMarketPriceRunJFrame jobThread = new StockMarketPriceRunJFrame(strNidAut, strNidSes);
		jobThread.start();
	}

}
