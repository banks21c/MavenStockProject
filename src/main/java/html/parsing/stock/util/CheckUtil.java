/*
 *****************************************************************************************************************
 * PROGRAM ID   :	CheckUtil.java
 * Description  :	���� üũ ��ƿ - �� üũ �� 
 * Input Param  :
 * Output Param :
 * Include File	:
 * Using Table  :
 * Return Value :
 * Sub Function :
 * SE Name                               Description                                             Date
 * -----------------------------------   -----------------------------------------------------   -----------
 * bluehiker(bluehiker@laonsum.com)      Initial Create                                          2005.08.01
 *****************************************************************************************************************
 */

package html.parsing.stock.util;

public class CheckUtil {

	// ########################################################################
	// null check 
	// ########################################################################	
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// null check #1
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	public static String NullChk(String str) {		
		if(str == null) { 
			return "";
		} else {
			return str;
		}
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// null check #2
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	public static String NullChk2(String str, String change_str)	{		
		//if(str == null) {
		if(str == null || str.equals(" ") ||  str.equals("") ||  str.equals("null")) {
			return change_str;
		}		
		String str2= str.trim();
		//if(str2.equals("")) {
		if(str2.equals("") || str2 == null || str2.equals(" ") || str2.equals("null")) {
			return change_str;
		} else {
			return str;
		}
	}
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// null check #3
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	 public static String nullCheck(String value) {
		 String valueCheck = "";
		 if (value != null) {
			 valueCheck = value;
		 }
		 return valueCheck;
	 }

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// null check #4
	// null -> 0 ���� ���� 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		 
	 public static String nullCheckToZero(String value) {
		 String valueCheck = "0";
		 if (value != null) {
			 valueCheck = value;
		 }
		 return valueCheck;
	 }		
	 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ���� ��ư üũ (input type=radio~~) 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	 public static String checkRadio(int value1, int value2) {
		 String checked = "";
		 if (value1 == value2) {
			 checked = "checked";
		 }
		 return checked;
	 }

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// üũ�ڽ� ��ư üũ (input type=checkbox~~) 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	 public static String checkBox(int value) {
		 String checked = "";
		 if (value == 1) {
			 checked = "checked";
		 }
		 return checked;
	 }	 
	 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// ����Ʈ �ڽ� ���� üũ (select name=select~~) 
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++	
	 public static String checkSelect(int value) {
		 String selected = "";
		 if (value == 1) {
			 selected = "selected";
		 }
		 return selected;
	 }		 
}
