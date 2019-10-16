package html.parsing.stock;

import java.text.DecimalFormat;
/**
 * Letter Date or Time Component Presentation Examples G Era designator Text AD
 * y Year Year 1996; 96 M Month in year Month July; Jul; 07 w Week in year
 * Number 27 W Week in month Number 2 D Day in year Number 189 d Day in month
 * Number 10 F Day of week in month Number 2 E Day in week Text Tuesday; Tue a
 * Am/pm marker Text PM H Hour in day (0-23) Number 0 k Hour in day (1-24)
 * Number 24 K Hour in am/pm (0-11) Number 0 h Hour in am/pm (1-12) Number 12 m
 * Minute in hour Number 30 s Second in minute Number 55 S Millisecond Number
 * 978 z Time zone General time zone Pacific Standard Time; PST; GMT-08:00 Z
 * Time zone RFC 822 time zone -0800
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatTest {

    public DateFormatTest() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        String format1 = "yyyyMMdd HH.mm.ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(format1, Locale.KOREAN);
        String strDate = sdf.format(new Date());
        System.out.println(strDate);

        String format2 = "yyyy년 M월 d일 E";
        SimpleDateFormat sdf2 = new SimpleDateFormat(format2, Locale.KOREAN);
        String strDate2 = sdf2.format(new Date());
        System.out.println(strDate2);

        float upDownRatio = 17.301039f;

        System.out.println(upDownRatio * 100);
        System.out.println(((int) (upDownRatio * 100)) / 100f);

        DecimalFormat df = new DecimalFormat("###.##");
        System.out.println(df.format(12.304645));
    }

}
