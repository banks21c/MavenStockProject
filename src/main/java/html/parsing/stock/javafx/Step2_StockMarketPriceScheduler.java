package html.parsing.stock.javafx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Step2_StockMarketPriceScheduler {

	private static Logger logger = LoggerFactory.getLogger(Step2_StockMarketPriceScheduler.class);

	String strNidAut = "";
	String strNidSes = "";
	boolean isInstant = false;

	public Step2_StockMarketPriceScheduler() {
	}

	public Step2_StockMarketPriceScheduler(String strNidAut, String strNidSes) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	Step2_StockMarketPriceScheduler(String strNidAut, String strNidSes, boolean isInstant) {
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		this.isInstant = isInstant;
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		System.out.println("isInstant:" + isInstant);
	}

	public void schedulerStart() {
		// The program starts by getting an instance of the Scheduler. This is done by
		// creating a StdSchedulerFactory and then using it to create a scheduler. This
		// will create a simple, RAM-based scheduler.
//		Step2_StockMarketPriceJobThread runJob = new Step2_StockMarketPriceJobThread(strNidAut,strNidSes);

		//"0 15 10 * * ? 2005"	2005년 동안 매일 오전 10시 15분에 실행
		SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
		System.out.println("strDate1:" + sdf.format(new Date()));
		Calendar cal = Calendar.getInstance();
		long timeInMillis = cal.getTimeInMillis();
		long timeInMillisPlus = timeInMillis+10000;
		cal.setTimeInMillis(timeInMillisPlus);
		Date d = cal.getTime();
		String strDate = sdf.format(d);
		System.out.println("strDate2:" + strDate);

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched;
		JobDetail job;
		CronTrigger trigger;
		try {
			sched = sf.getScheduler();

			// Job #0 is scheduled to run every 10 minutes
//		job = newJob(StockPlusMinusDivide100Job.class).withIdentity("job0", "group0").build();
			job = newJob(Step3_StockMarketPriceJobThread.class).withIdentity("job0", "group0").build();
			JobDataMap map = job.getJobDataMap();
			map.put("NID_AUT", strNidAut);
			map.put("NID_SES", strNidSes);

			if (isInstant) {
				trigger = newTrigger().withIdentity("trigger0", "group0").withSchedule(cronSchedule(strDate)).build();
				sched.scheduleJob(job, trigger);
				sched.start();
			} else {

				trigger = newTrigger().withIdentity("trigger0", "group0").withSchedule(cronSchedule("0 0 * ? * *")).build();
				sched.scheduleJob(job, trigger);

				// Job #1 is scheduled to run every day at 15:40pm
				job = newJob(Step3_StockMarketPriceJobThread.class).withIdentity("job1", "group1").build();
				trigger = newTrigger().withIdentity("trigger1", "group1").withSchedule(cronSchedule("0 40 15 * * ?"))
					.build();
				sched.scheduleJob(job, trigger);

				// The scheduler is then started (it also would have been fine to start it
				// before scheduling the jobs).
				sched.start();
				// To let the program have an opportunity to run the job, we then sleep for five
				// minutes (300 seconds). The scheduler is running in the background and should
				// fire off several jobs during that time.

				// Note: Because many of the jobs have hourly and daily restrictions on them,
				// not all of the jobs will run in this example. For example: Job #6 only runs
				// on Weekdays while Job #7 only runs on Weekends.
				// Thread.sleep(300L * 1000L);
				// Finally, we will gracefully shutdown the scheduler:
				// sched.shutdown(true);
				// Note: passing true into the shutdown message tells the Quartz Scheduler to
				// wait until all jobs have completed running before returning from the method
				// call.
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}
