package html.parsing.stock;

public class CalcRatioTest {

	public CalcRatioTest() {
	}

	public static void main(String[] args) {
		String sTotalChosenDayRetainAmount  = "3,022,074,824,400";
		double lTotalChosenDayRetainAmount = Double.parseDouble(sTotalChosenDayRetainAmount.replaceAll(",", ""));
		String sRetainAmount  = "3,947,082,517,800";
		double lRetainAmount = Double.parseDouble(sRetainAmount.replaceAll(",", ""));
		double t = (lTotalChosenDayRetainAmount - lRetainAmount);

		double temp1 = t / lTotalChosenDayRetainAmount;
		double temp2 = temp1 * 100.0;
		double gapRatio = Math.round(temp2 * 100)/100.0;
		System.out.println("gapRatio :"+gapRatio+"%");

		double gapRatio2 = Math.round((lTotalChosenDayRetainAmount - lRetainAmount)/ lTotalChosenDayRetainAmount * 100* 100)/100.0;
		if(lTotalChosenDayRetainAmount < lRetainAmount) {
			gapRatio2 = -gapRatio2;
		}else {
			gapRatio2 = gapRatio2;
		}

		System.out.println("gapRatio2 :"+gapRatio2+"%");
	}

}
