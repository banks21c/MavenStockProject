/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author banksfamily
 */
public class CalendarTest2 {
	public static void main(String args[]){
		new CalendarTest2().test();
	}

	public void test() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// get today and clear time of day
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		System.out.println("Start of this week:       " + cal.getTime());
		System.out.println("... in milliseconds:      " + cal.getTimeInMillis());
		String ymd = sdf.format(cal.getTime());
		System.out.println("이번주 첫날:"+ymd);
		System.out.println("cal.getFirstDayOfWeek() :"+cal.getFirstDayOfWeek());
		System.out.println("cal.getMinimalDaysInFirstWeek() :"+cal.getMinimalDaysInFirstWeek());
		System.out.println("cal.getWeekYear() :"+cal.getWeekYear());
		System.out.println("cal.getWeeksInWeekYear() :"+cal.getWeeksInWeekYear());
		System.out.println("Calendar.DAY_OF_WEEK :"+Calendar.DAY_OF_WEEK);
		System.out.println("Calendar.DAY_OF_MONTH :"+Calendar.DAY_OF_MONTH);
		System.out.println("cal.getMaximum(Calendar.DAY_OF_MONTH) :"+cal.getMaximum(Calendar.DAY_OF_MONTH));
		System.out.println("cal.getActualMaximum(Calendar.DAY_OF_MONTH) :"+cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		// start of the next week
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		System.out.println("Start of the next week:   " + cal.getTime());
		System.out.println("... in milliseconds:      " + cal.getTimeInMillis());
	}
}
