package html.parsing.stock.quartz2x.example2;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class StockCronTriggerExample {
	public static void main(String args[]) {
		new StockCronTriggerExample();
	}

	public StockCronTriggerExample() {
		// The program starts by getting an instance of the Scheduler. This is done by
		// creating a StdSchedulerFactory and then using it to create a scheduler. This
		// will create a simple, RAM-based scheduler.

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched;
		JobDetail job;
		CronTrigger trigger;
		try {
			sched = sf.getScheduler();

			// Job #0 is scheduled to run every 10 minutes
//		job = newJob(StockPlusMinusDivide100Job.class).withIdentity("job0", "group0").build();
			job = newJob(StockMarketPriceRunJob.class).withIdentity("job0", "group0").build();
			trigger = newTrigger().withIdentity("trigger0", "group0").withSchedule(cronSchedule("0 */30 * ? * *"))
					.build();
			sched.scheduleJob(job, trigger);

			// Job #1 is scheduled to run every day at 15:40pm
			job = newJob(StockMarketPriceRunJob.class).withIdentity("job1", "group1").build();
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
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}
