package html.parsing.stock.stockholders;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class NationalPensionThreadCall {

	static String specificDay = "";
	static String thisYearFirstTradeDay = "2020.01.02";

	static String majorStockHolders = "";

	@Test
	public void readAndWriteMajorStockHolders() throws Exception {
		majorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		if (majorStockHolders.equals(""))
			majorStockHolders = "국민연금공단";
		specificDay = StringUtils.defaultString(JOptionPane.showInputDialog("기준일을 입력해주세요.")).trim();
		if (specificDay.equals(""))
			specificDay = thisYearFirstTradeDay;

		NationalPensionThread thread1 = new NationalPensionThread("kospi",majorStockHolders,specificDay);
		thread1.start();
//		NationalPensionThread thread2 = new NationalPensionThread("kosdaq",majorStockHolders,specificDay);
//		thread2.start();

	}


}
