package html.parsing.stock.javafx;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Step3_ShareCoupangPrdtJobThread extends Thread implements Job {

	private static Logger logger = LoggerFactory.getLogger(Step3_ShareCoupangPrdtJobThread.class);

	public Step3_ShareCoupangPrdtJobThread() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("SimpleJob says: " + jobKey + " executing at " + new Date());
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		String strBlogId;
		String strNidAut;
		String strNidSes;
		strBlogId = jobDataMap.getString("BLOG_ID");
		strNidAut = jobDataMap.getString("NID_AUT");
		strNidSes = jobDataMap.getString("NID_SES");
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		Step4_ShareCoupangPrdtRun jobThread = new Step4_ShareCoupangPrdtRun(strBlogId, strNidAut, strNidSes);
		jobThread.start();
	}

}
