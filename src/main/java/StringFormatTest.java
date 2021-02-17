
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringFormatTest {

	private static final Logger logger = LoggerFactory.getLogger(StringFormatTest.class);

	public static void main(String[] args) {
		int i = 23;

		System.out.println(String.format("%d_", i));
		System.out.println(String.format("%5d_", i));
		System.out.println(String.format("%-5d_", i));
		System.out.println(String.format("%05d_", i));
		
		i = 123456789;

		System.out.println(String.format("%,d_", i));
		System.out.println(String.format("%,15d_", i));
		System.out.println(String.format("%,-15d_", i));
		System.out.println(String.format("%,015d_", i));
		
		String str = "tete";

		System.out.println(String.format("%s_", str));
		System.out.println(String.format("%12s_", str));
		System.out.println(String.format("%-12s_", str));
		System.out.println(String.format("%.2s_", str));
		System.out.println(String.format("%-12.2s_", str));
		System.out.println(String.format("%12.2s_", str));

		
		
		double db = 123.45678;

		System.out.println(3.4);
		System.out.println(db);
		System.out.println();

		System.out.println(String.format("%f_", 3.4));
		System.out.println(String.format("%f_", db));
		System.out.println(String.format("%.6f_", db));
		System.out.println(String.format("%15f_", db));
		System.out.println(String.format("%-15f_", db));
		System.out.println(String.format("%.3f_", db));
		System.out.println(String.format("%.2f_", db));
		System.out.println(String.format("%15.2f_", db));
		System.out.println(String.format("%-15.2f_", db));
		System.out.println(String.format("%015f_", db));
		System.out.println(String.format("%015.2f_", db));
		
		
		
		int money = 35000;
		Date today = new Date();

		System.out.println(String.format("￦ %,d", money));
		System.out.println(String.format(Locale.GERMANY, "%,d €", money));
		System.out.println(String.format("%tp", today));
		System.out.println(String.format(Locale.ENGLISH, "%tp", today));

		
		
		Date dt = new Date();
		System.out.println(dt +"\n");
		System.out.println(String.format("%%tF(yyyy-MM-dd): %tF", dt));
		System.out.println(String.format("%%tT(02H:02m:02s): %tT, %%tR(02H:02m): %tR", dt, dt));
		System.out.println(String.format("%%ty(2y): %ty, %%tY(4y): %tY", dt, dt));		
		System.out.println(String.format("%%tm(02M): %tm", dt));		
		System.out.println(String.format("%%td(02d): %td, %%te(d): %te", dt, dt));

		System.out.println(String.format("%%tH(02H): %tH", dt));
		System.out.println(String.format("%%tM(02m): %tM", dt));
		System.out.println(String.format("%%tS(02s): %tS", dt));

		System.out.println(String.format("%%tZ(time zone): %tZ, %%tz(time zone offset): %tz", dt, dt));

		
		
		
		System.out.println(String.format("%%tA(day of Week, Full name): %tA, %%ta: %ta", dt, dt));
		System.out.println(String.format("%%tB(month, Full name): %tB, %%tb: %tb", dt, dt));
		System.out.println(String.format(Locale.ENGLISH, "%%tB(month, Full name): %tB, %%tb: %tb", dt, dt));
		System.out.println(String.format("%%tc(= %%ta %%tb %%td %%tT %%tZ %%tY): %tc", dt));
		System.out.println(String.format("%%tD(MM/dd/yy): %tD", dt));
		System.out.println(String.format("%%td(02d): %td, %%te(d): %te", dt, dt));
		System.out.println(String.format("%%tF(yyyy-02M-02d): %tF", dt));
		System.out.println(String.format("%%tH(02H, 00-23): %tH, %%tk(H, 0-23): %tk", dt, dt));
		System.out.println(String.format("%%tI(02h, 01-12): %tI, %%tl(h, 1-12): %tl", dt, dt));
		System.out.println(String.format("%%tj(day of Year, 001-366): %tj", dt));
		System.out.println(String.format("%%tp(오전 또는 오후): %tp", dt));


		
		System.out.println("Unicode 코드 → 문자");
		System.out.println(String.format("48 → %c, 57 → %c", 48, 57));
		System.out.println(String.format("65 → %c, 90 → %c", 65, 90));
		System.out.println(String.format("97 → %c, 122 → %c", 97, 122));
		System.out.println(String.format("44032 → %c, 55203 → %c", 44032, 55203)); //  U+AC00, U+D7A3

		int n = 100;
		System.out.println(String.format("10진수(%d) : 2진수(%s), 8진수(%o), 16진수(%x)", n, Integer.toBinaryString(n), n, n));

	}

}
