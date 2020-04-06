package html.parsing.stock.util;

import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.StringUtils;

public class IndexOfTest {

	public IndexOfTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String inputMajorStockHolders = StringUtils.defaultString(JOptionPane.showInputDialog("대주주명을 입력해주세요.")).trim();
		String majorStockHolderName = "베어링자산운용";
		System.out.println(majorStockHolderName.indexOf(inputMajorStockHolders));
		if (inputMajorStockHolders.equals("") || majorStockHolderName.indexOf(inputMajorStockHolders) != -1) {
			System.out.println("kkkkkkk");
		}

	}

}
