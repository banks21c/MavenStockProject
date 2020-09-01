package test.calendar;

import html.parsing.stock.*;
import java.util.Calendar;

public class CalendarEx2 {

    public static void main(String[] arg) {

        Calendar yDay = Calendar.getInstance();
        Calendar toDay = Calendar.getInstance();

        // month의 경우 0부터 시작 => 9월이면 8
        yDay.set(2009, 8, 6);

        System.out.println("yDay = " + toString(yDay));
        System.out.println("toDay = " + toString(toDay));

        // 두 날짜간의 차이를 얻으려면, getTimeInMillis()를 이용해서 천분의 일초 단위로 변환해야한다.
        // 1일 = 24 * 60 * 60
        long diffSec = (toDay.getTimeInMillis() - yDay.getTimeInMillis()) / 1000; // 초
        long diffDay = diffSec / (60 * 60 * 24); // 날
        System.out.println("두 날자의 일 차이수 = " + diffDay);
    }

    public static String toString(Calendar cal) {
        // 요일은 1부터 시작
        final String[] arrWeek = {"", "일", "월", "화", "수", "목", "금", "토"};
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DATE);
        String weekDay = arrWeek[cal.get(Calendar.DAY_OF_WEEK)];

        return (year + " 년 " + mon + "월" + date + "일 (" + weekDay + "요일)");
    }
}
