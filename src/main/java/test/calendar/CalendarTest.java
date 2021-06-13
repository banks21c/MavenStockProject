package test.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

public class CalendarTest {

    public CalendarTest() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        Calendar cal1 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
        String strDefaultDate = sdf.format(new Date());
        String strDate;
        String strInputDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
        System.out.println("strInputDate:" + strInputDate);
        if (strInputDate != null) {
            strDate = strInputDate;
            String year = strDate.substring(0, 4);
            String month = strDate.substring(5, 7);
            String day = strDate.substring(8, 10);
            int iYear = Integer.parseInt(year);
            int iMonth = Integer.parseInt(month) - 1;
            int iDay = Integer.parseInt(day);
            // System.out.println(year + month + day);

            cal1.set(iYear, iMonth, iDay);
            System.out.println("getFirstDayOfWeek:" + cal1.getFirstDayOfWeek());
            System.out.println("getWeekYear:" + cal1.getWeekYear());
            System.out.println("getWeeksInWeekYear:" + cal1.getWeeksInWeekYear());
            System.out.println("Calendar.DAY_OF_WEEK:" + cal1.get(Calendar.DAY_OF_WEEK));
            System.out.println("Calendar.DAY_OF_WEEK_IN_MONTH:" + cal1.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("Calendar.DAY_OF_MONTH:" + cal1.get(Calendar.DAY_OF_MONTH));
            System.out.println("Calendar.DAY_OF_YEAR:" + cal1.get(Calendar.DAY_OF_YEAR));
            cal1.set(iYear, iMonth, iDay);
            System.out.println("getFirstDayOfWeek:" + cal1.getFirstDayOfWeek());
            System.out.println("getWeekYear:" + cal1.getWeekYear());
            System.out.println("getWeeksInWeekYear:" + cal1.getWeeksInWeekYear());
            System.out.println("Calendar.DAY_OF_WEEK:" + cal1.get(Calendar.DAY_OF_WEEK));
            System.out.println("Calendar.DAY_OF_WEEK_IN_MONTH:" + cal1.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("Calendar.DAY_OF_MONTH:" + cal1.get(Calendar.DAY_OF_MONTH));
            System.out.println("Calendar.DAY_OF_YEAR:" + cal1.get(Calendar.DAY_OF_YEAR));

            System.out.println("SUNDAY:" + cal1.SUNDAY);
            System.out.println("MONDAY:" + cal1.MONDAY);
            System.out.println("TUESDAY:" + cal1.TUESDAY);
            System.out.println("WEDNESDAY:" + cal1.WEDNESDAY);
            System.out.println("THURSDAY:" + cal1.THURSDAY);
            System.out.println("FRIDAY:" + cal1.FRIDAY);
            System.out.println("SATURDAY:" + cal1.SATURDAY);

            int iDayOfWeek = cal1.get(Calendar.DAY_OF_WEEK);
            switch (iDayOfWeek) {
                case 1:
                    day = "일";
                    break;
                case 2:
                    day = "월";
                    break;
                case 3:
                    day = "화";
                    break;
                case 4:
                    day = "수";
                    break;
                case 5:
                    day = "목";
                    break;
                case 6:
                    day = "금";
                    break;
                case 7:
                    day = "토";
                    break;
            }
            System.out.println("요일:" + day);
        }

    }

}
