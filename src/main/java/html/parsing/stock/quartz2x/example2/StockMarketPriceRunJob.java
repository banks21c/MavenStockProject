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

		this.strNidAut = "Jsx65uOgdDzm+2gvGwINz/9qjTB1MmEHFaAy1P3I5weOnmeEiQE4oUzQ3c2klCE3";
		this.strNidSes = "AAABhzeif/40WSx5+JeLEw3MgLUcFLbVaQ//fNPz/vLpT/e0ZRPx+9j851jHrOTLUIdJNW9+Qf1fD3H8KFguQ4DqC/FvZV3l+I+DC8xW4gBhtZy4LPOXv+4I0ddJqZtRkwPZ9A21Eppej+ZUqkpgRo2Juuw4y1phsvQ5bnohyjZ8LZFNc3Qpmm0Dnc2r0IHl68BTX94yeR/8lg+OMp0tRgkfvNrcrlNHnkCBV1tcLtHYK51oGUxRpFsR3nphkJdbHsUmbC2gMK3BGthWvzSOdCJ+EmBS3mMGSidBnFhygRQ9kY7AsKG9ULfcakMJdRcnBb4/8SxX2UNW1KZ9vbWjNszObyuw6wRa1SgGShWIFDkzB9DWWtg1dTlhTtr+owmg0SIYjgDMuiWARBfyr9mBQDc4uGr3/TT+S7YqfT9pz7+64yJsP2HhWk8P5oNOKZErB0KtAARRISnOjtOFTwUNEAO5wdVJFU1g7MUx3gqVVmmv9+T7sRe4jKQeTCQFM+9OV5TOtuRMKrW7JCnnrsutP867w88=";

		StockMarketPriceRunJFrame jobThread = new StockMarketPriceRunJFrame(strNidAut, strNidSes);
		jobThread.start();
	}

}
