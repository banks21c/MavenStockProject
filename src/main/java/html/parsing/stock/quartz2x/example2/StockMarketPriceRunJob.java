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

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey jobKey = context.getJobDetail().getKey();
		logger.info("SimpleJob says: " + jobKey + " executing at " + new Date());

		this.strNidAut = "J7aNAb3e4xK+3zRKEmqCrhV62qqew8NH1IOQe1KC1veyB6zspAdqcdyNYYmTJQCQ";
		this.strNidSes = "AAABhj6FHNpQWb6I8nfio1e3o8LgjXWTGqAdQDw8VQ4WVcY8hRdJDmGE8yzFpnHYYAsDhOW3whQB5GQxXQQfzOkTh6fZuN3hA0jsnEQ8e3SbUZa0qyzB7Z9j2KwZG8ZYKGz72RKug5mD+ydwEFAlbDkY/qXeUxTEbcnC8bs/n4BC/4D8nEgTpy0OowAnhwkiJ+U25pf4U4mCRfH4giwoSn+aXK+dpYUkOTy74GJMxa5dEYZR63XsJcaYSDaHAFz7P979J+IF/KLm+taGiScKpHSLdHHLTKdMPakC9Uj8TdrvFEEIVOxD8Gpenb4IINklkUm7G3BEcWlPyze5NKf2O4SYZH8jKd9/VT+tH3p/7io1AHOhy+8YBxK9PjNqz2LTHOSKHHT7o8rzT4Dt4k67eJ5iB1GDpYvB/9Mrs03WPVvoouVQY9eNDoht2e7EsKGQCcGTeyXrAuy3X3kYa/TIBQwRKyzAAhURBUzq+UCd+jfHWjsE+X05aobR7QcTcCkyK8ne56gbVjYrIlhPQnFPpZc2vpA=";

		StockMarketPriceRunJFrame jobThread = new StockMarketPriceRunJFrame(strNidAut, strNidSes);
		jobThread.start();
	}

}
