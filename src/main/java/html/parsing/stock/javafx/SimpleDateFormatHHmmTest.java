package html.parsing.stock.javafx;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDateFormatHHmmTest {

	public SimpleDateFormatHHmmTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {

		Calendar c = Calendar.getInstance();
//		c.set(Calendar.HOUR,13);
		c.set(Calendar.HOUR_OF_DAY,13);
		c.set(Calendar.MINUTE,9);
		long timeInMillis = c.getTimeInMillis();
		Date d = new Date(timeInMillis);
		int iHourMinute = 0;
		iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		System.out.println(iHourMinute);
		iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(d));
		System.out.println(iHourMinute);
//		iHourMinute = 1309;
		if (iHourMinute >= 1830 || iHourMinute < 800) {
			System.out.println(iHourMinute);
		}
	}
}
