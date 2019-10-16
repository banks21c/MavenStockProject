/**
 *
 */
package html.parsing.stock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author parsing-25
 *
 */
public class SimpleDateFormatTest {

    /**
     *
     */
    public SimpleDateFormatTest() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
        int iHms = Integer.parseInt(strHms);
        System.out.println("iHms:" + iHms);
    }

}
