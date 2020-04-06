package html.parsing.stock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static void main(String[] args) {
        int year = 2019;
        int month = 2; // January
        int date = 15;

        Calendar cal = Calendar.getInstance();
        cal.clear();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        java.util.Date utilDate = cal.getTime();
        System.out.println(utilDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.KOREAN);
        String strDate = sdf.format(new Date());
        System.out.println(strDate);
    }
}
