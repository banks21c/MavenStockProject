package html.parsing.stock;

public class CalcRatioTest {

	public CalcRatioTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String sTotalSpecificDayRetainAmount  = "3,022,074,824,400";
		double lTotalSpecificDayRetainAmount = Double.parseDouble(sTotalSpecificDayRetainAmount.replaceAll(",", ""));
		String sRetainAmount  = "3,947,082,517,800";
		double lRetainAmount = Double.parseDouble(sRetainAmount.replaceAll(",", ""));
		double t = (lTotalSpecificDayRetainAmount - lRetainAmount);

		double temp1 = t / lTotalSpecificDayRetainAmount;
		double temp2 = temp1 * 100.0;
		double gapRatio = Math.round(temp2 * 100)/100.0;
		System.out.println("gapRatio :"+gapRatio+"%");

		double gapRatio2 = Math.round((lTotalSpecificDayRetainAmount - lRetainAmount)/ lTotalSpecificDayRetainAmount * 100* 100)/100.0;
		if(lTotalSpecificDayRetainAmount < lRetainAmount) {
			gapRatio2 = -gapRatio2;
		}else {
			gapRatio2 = gapRatio2;
		}

		System.out.println("gapRatio2 :"+gapRatio2+"%");
	}

}
