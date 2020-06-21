package html.parsing.stock.quartz2x.example1;

import java.util.Date;
import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class HelloSchedule {

	public HelloSchedule() throws Exception {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		// sched.start();

		// define the job and tie it to our HelloJob class
		JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();

		// compute a time that is on the next round minute
		Date runTime = evenMinuteDate(new Date());

		// Trigger the job to run on the next round minute
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		// Tell quartz to schedule the job using our trigger
		sched.scheduleJob(job, trigger);
		sched.start();
		// Thread.sleep(10L * 1000L);
		// sched.shutdown(true);

	}

	public static void main(String args[]) {
		try {
			new HelloSchedule();
		} catch (Exception e) {
		}
	}
}
