/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeekFirstLastDay {

	public static void main(String args[]) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//		String dateString = "";
//			dateString = new Date().toString();
//		System.out.println("dateString:"+dateString);
//		Date date = null;
//		try {
//			date = simpleDateFormat.parse(dateString);
//		} catch (ParseException e) {
//			System.out.println("잘못된 문자열이네요");
//		}
//		Date date = new Date();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyyMMdd").parse("20200901");
		} catch (ParseException ex) {
			Logger.getLogger(WeekFirstLastDay.class.getName()).log(Level.SEVERE, null, ex);
		}
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		cal.setTime(date);
		System.out.println("입력한 날짜 : " + simpleDateFormat.format(cal.getTime()));
		System.out.println("Calendar.DAY_OF_WEEK :" + Calendar.DAY_OF_WEEK);
		System.out.println("Calendar.DATE :" + Calendar.DATE);
		System.out.println("dayofweek :" + cal.get(Calendar.DAY_OF_WEEK));
		System.out.println("getFirstDayOfWeek :" + cal.getFirstDayOfWeek());
		cal.add(Calendar.DATE, 1 - cal.get(Calendar.DAY_OF_WEEK));
		System.out.println("첫번째 요일(일요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));
		cal.setTime(date);
		cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK));
		System.out.println("마지막 요일(토요일) 날짜 : " + simpleDateFormat.format(cal.getTime()));

		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		cal.set(Calendar.DAY_OF_WEEK, 3);
		cal.set(Calendar.DAY_OF_WEEK, 4);
		cal.set(Calendar.DAY_OF_WEEK, 5);
		cal.set(Calendar.DAY_OF_WEEK, 6);
		cal.set(Calendar.DAY_OF_WEEK, 7);
		System.out.println("Start of this week:       " + cal.getTime());
		System.out.println("... in milliseconds:      " + cal.getTimeInMillis());
		String ymd = simpleDateFormat.format(cal.getTime());
		System.out.println("이번주 첫날:" + ymd);
	}
}
