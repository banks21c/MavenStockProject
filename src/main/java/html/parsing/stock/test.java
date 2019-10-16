/**
 *
 */
package html.parsing.stock;

/**
 * @author banks
 *
 */
public class test {

    /**
     *
     */
    public test() {
        print1(true);
        print1(false);
    }

    public void print1(boolean isVolume) {
        String title = "";
        StringBuilder sb1 = new StringBuilder();
        sb1.append("<html>\r\n");
        sb1.append("<head>\r\n");
        sb1.append("<style>\r\n");
        sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
        sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
        sb1.append("</style>\r\n");
        sb1.append("</head>\r\n");
        sb1.append("<body>\r\n");
        sb1.append("\t<font size=5>" + title + "</font>");
        sb1.append("<table>\r\n");
        sb1.append("<tr>\r\n");
        sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
        sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
        sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
        sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
        sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>등락률</td>\r\n");
        if (isVolume) {
            sb1.append("<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>합계</td>\r\n");
        } else {
            sb1.append("<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;'>합계(백만)</td>\r\n");
        }
        sb1.append("</tr>\r\n");

        sb1.append("<tr>\r\n");
        if (isVolume) {
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>외인</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>기관</td>\r\n");
        } else {
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>외인</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>기관</td>\r\n");
        }
        sb1.append("</tr>\r\n");

        System.out.println(sb1.toString());
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new test();
        long i = 214912400000000L;
        System.out.println(i);
    }

}
