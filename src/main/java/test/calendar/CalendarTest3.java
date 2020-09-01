package test.calendar;

import html.parsing.stock.util.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarTest3 {

	public CalendarTest3() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String args[]) {
		Calendar cal1 = Calendar.getInstance();//오늘 ymd
		Calendar cal2 = Calendar.getInstance();//기사입력일 ymd
		int iYear = 2020;
		int iMonth = 6-1;
		int iDay = 28;
		int hourOfDay=21;
		int minute=52;
        int second=59;

//		cal2.set(iYear, iMonth, iDay);
		cal2.set(iYear, iMonth, iDay,hourOfDay,minute,second);
		System.out.println(cal1.getTimeInMillis());
		System.out.println(cal2.getTimeInMillis());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hhmmss", Locale.KOREAN);

		Date d1 = cal1.getTime();
		Date d2 = cal2.getTime();

		String str1 = sdf.format(d1);
		String str2 = sdf.format(d2);
		
		System.out.println(str1);
		System.out.println(str2);
		
	}

}
