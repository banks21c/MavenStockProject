package html.parsing.stock.focus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateFormatExample {
	public static void main(String[] args) {
		Date today = new Date();
		SimpleDateFormat format1, format2, format3, format4, format5, format6, format7;
		format1 = new SimpleDateFormat("yyyy-MM-dd");
		format3 = new SimpleDateFormat("yy/MM/dd");
		format2 = new SimpleDateFormat("yyyy년 MM월 dd일 E요일");
		format4 = new SimpleDateFormat("HH:mm:ss");
		format5 = new SimpleDateFormat("hh:mm:ss a");
		format6 = new SimpleDateFormat("오늘은 yyyy년의 w주차이며 D번째 날입니다.");
		format7 = new SimpleDateFormat("오늘은 M월의 w번째 주, d번째 날이며, F번째 E요일입니다.");
		System.out.println(format1.format(today));
		System.out.println(format2.format(today));
		System.out.println(format3.format(today));
		System.out.println(format4.format(today));
		System.out.println(format5.format(today));
		System.out.println(format6.format(today));
		System.out.println(format7.format(today));
		
		
		int iHour = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		System.out.println("iHour :"+iHour);

	}
}
