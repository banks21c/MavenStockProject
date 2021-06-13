/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.quartz2x.example2;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleJob implements Job {

	private static Logger logger = LoggerFactory.getLogger(SimpleJob.class);

//	public void execute(JobExecutionContext arg0) throws JobExecutionException {
//		System.out.println("Hello World Quartz Scheduler: " + new Date());
//	}
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("SimpleJob says: " + jobKey + " executing at " + new Date());
	}
}
