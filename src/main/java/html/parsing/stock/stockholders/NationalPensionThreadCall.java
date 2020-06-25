package html.parsing.stock.stockholders;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

public class NationalPensionThreadCall {

	static String chosenDay = "";
	static String thisYearFirstTradeDay = "2020.01.02";

	static String majorStockHolders = "";

	public static void main(String args[]) {
		NationalPensionThreadCall threadCall = new NationalPensionThreadCall();
		try {
			threadCall.readAndWriteMajorStockHolders();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readAndWriteMajorStockHolders() throws Exception {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		if (majorStockHolders.equals(""))
			majorStockHolders = "국민연금공단";
		chosenDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (chosenDay.equals(""))
			chosenDay = thisYearFirstTradeDay;

		NationalPensionThread thread1 = new NationalPensionThread("kospi",majorStockHolders,chosenDay);
		thread1.start();
		NationalPensionThread thread2 = new NationalPensionThread("kosdaq",majorStockHolders,chosenDay);
		thread2.start();

	}


}
