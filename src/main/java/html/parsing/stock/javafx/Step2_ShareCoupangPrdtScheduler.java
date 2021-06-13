package html.parsing.stock.javafx;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Step2_ShareCoupangPrdtScheduler {
	/*
	 * <!-- 5분 마다 실행 ex) 00:05, 00:10. 00:15.... --> cron = "0 0/5 * * * *" <!-- 1시간
	 * 마다 실행 ex) 01:00, 02:00, 03:00.... --> cron = "0 0 0/1 * * *" <!-- 매일 오후 18시마다
	 * 실행 ex) 18:00 --> cron = "0 0 18 * * *" <!-- 2018년도만 매일 오후 18시마다 실행 ex) 18:00
	 * --> cron = "0 0 18 * * * 2018" <!-- 매일 오후 18시00분-18시55분 사이에 5분 간격으로 실행 ex)
	 * 18:00, 18:05.....18:55 --> cron = "0 0/5 18 * * *" <!-- 매일 오후 9시00분-9시55분,
	 * 18시00분-18시55분 사이에 5분 간격으로 실행 --> cron = "0 0/5 9,18 * * *" <!-- 매일 오후
	 * 9시00분-18시55분 사이에 5분 간격으로 실행 --> cron = "0 0/5 9-18 * * *" <!-- 매달 1일 00시에 실행
	 * --> cron = "0 0 0 1 * *" <!-- 매년 3월내 월-금요일 10시 30분에만 실행 --> cron =
	 * "0 30 10 ? 3 MON-FRI" <!-- 매월 마지막날 저녁 10시에 실행 --> cron = "0 0 10 L * ?"
	 */
	private static Logger logger = LoggerFactory.getLogger(Step2_ShareCoupangPrdtScheduler.class);

	String strBlogId = "";
	String strNidAut = "";
	String strNidSes = "";
	boolean isInstant = false;

	public Step2_ShareCoupangPrdtScheduler() {
	}

	public Step2_ShareCoupangPrdtScheduler(String strBlogId, String strNidAut, String strNidSes) {
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
	}

	Step2_ShareCoupangPrdtScheduler(String strBlogId, String strNidAut, String strNidSes, boolean isInstant) {
		this.strBlogId = strBlogId;
		this.strNidAut = strNidAut;
		this.strNidSes = strNidSes;
		this.isInstant = isInstant;
		System.out.println("strBlogId:" + strBlogId);
		System.out.println("strNidAut:" + strNidAut);
		System.out.println("strNidSes:" + strNidSes);
		System.out.println("isInstant:" + isInstant);
	}

	public void schedulerStart() {
		// The program starts by getting an instance of the Scheduler. This is done by
		// creating a StdSchedulerFactory and then using it to create a scheduler. This
		// will create a simple, RAM-based scheduler.
//		Step2_StockMarketPriceJobThread runJob = new Step2_StockMarketPriceJobThread(strNidAut,strNidSes);

		// "0 15 10 * * ? 2005" 2005년 동안 매일 오전 10시 15분에 실행

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched;
		JobDetail job;
		CronTrigger trigger;
		try {
			sched = sf.getScheduler();

			// * * * * * * *
			// 초 분 시 일 월 요일 년도(생략가능)

			// 즉시실행 버튼을 누르면 10초 뒤에 실행
			// 1회용....
			if (isInstant) {
				SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
				System.out.println("strDate1:" + sdf.format(new Date()));
				Calendar cal = Calendar.getInstance();
				long timeInMillis = cal.getTimeInMillis();
				long timeInMillisPlus = timeInMillis + 10000;
				cal.setTimeInMillis(timeInMillisPlus);
				Date d = cal.getTime();
				String strDate = sdf.format(d);
				System.out.println("strDate2:" + strDate);

				// Job #0 is scheduled to run every 10 minutes
//				job = newJob(StockPlusMinusDivide100Job.class).withIdentity("job0", "group0").build();
				job = newJob(Step3_ShareCoupangPrdtJobThread.class)
						.withIdentity("shareCoupangPrdtJob0", "shareCoupangPrdtGroup0").build();
				JobDataMap map = job.getJobDataMap();
				map.put("BLOG_ID", strBlogId);
				map.put("NID_AUT", strNidAut);
				map.put("NID_SES", strNidSes);

				trigger = newTrigger().withIdentity("shareCoupangPrdtTrigger0", "shareCoupangPrdtGroup0")
						.withSchedule(cronSchedule(strDate)).build();
				sched.scheduleJob(job, trigger);
				sched.start();
			} else {

				// Job #0 is scheduled to run every day at 매시 0분 0초에 실행
//				trigger = newTrigger().withIdentity("trigger0", "group0").withSchedule(cronSchedule("0 0 * ? * *")).build();
//				sched.scheduleJob(job, trigger);

				job = newJob(Step3_ShareCoupangPrdtJobThread.class).withIdentity("shareCoupangPrdtJob1", "group1")
						.build();
				JobDataMap map = job.getJobDataMap();
				map.put("BLOG_ID", strBlogId);
				map.put("NID_AUT", strNidAut);
				map.put("NID_SES", strNidSes);

				// Job #1 is scheduled to run every 10minutes
				// 10분마다실행
//				trigger = newTrigger().withIdentity("shareCoupangPrdtTrigger1", "group1").withSchedule(cronSchedule("0 0/10 * * * ?")) .build();
				// 30분마다실행
				trigger = newTrigger().withIdentity("shareCoupangPrdtTrigger1", "group1")
						.withSchedule(cronSchedule("0 0/30 * * * ?")).build();
				// 1시간마다실행
//				trigger = newTrigger().withIdentity("shareCoupangPrdtTrigger1", "group1").withSchedule(cronSchedule("0 0 0/1 * * ?")).build();
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
