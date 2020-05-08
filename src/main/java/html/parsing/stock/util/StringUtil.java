package html.parsing.stock.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Calendar;
import java.util.Date;

public class StringUtil {

	public static String dirTrim(String dir) {

		dir = dir.trim();
		if (dir.startsWith("/"))
			dir = dirTrim(dir.replaceFirst("/", ""));

		if (dir.endsWith("/"))
			dir = dirTrim(dir.substring(0, dir.lastIndexOf("/")));

		return dir;
	}

	public static String dirFirst(String dir) {
		if (dir.indexOf("/") > 0)
			dir = dir.substring(0, dir.indexOf("/"));

		return dir;
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// date format
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String ToDateFormat(String str, int type) {
		switch (type) {
		case 1:
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			str = sdf1.format(str);
			break;
		case 2:
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
			str = sdf2.format(str);
			break;
		case 3:
			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy.MM.dd");
			str = sdf3.format(str);
			break;
		case 4:
			SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			str = sdf4.format(str);
			break;
		case 5:
			SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			str = sdf5.format(str);
			break;
		case 6:
			SimpleDateFormat sdf6 = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
			str = sdf6.format(str);
			break;
		case 7:
			SimpleDateFormat sdf7 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
			str = sdf7.format(str);
			break;
		default:
			SimpleDateFormat sdf9 = new SimpleDateFormat("yyyy-MM-dd");
			str = sdf9.format(str);
			// break;
		}
		return str;
	}

	public static String number8ToDate(String str, int type) {

		// System.out.println(str.length());

		if (str != null && str.length() == 8) {
			String gubun = "";
			switch (type) {
			case 1:
				gubun = "-";
				break;
			default:
				gubun = "/";
				break;
			}

			str = str.substring(0, 4) + gubun + str.substring(4, 6) + gubun
					+ str.substring(6, 8);
			// System.out.println(str.length());
		} else if (str != null && str.length() == 6) {
			String gubun = "";
			switch (type) {
			case 1:
				gubun = "-";
				break;
			default:
				gubun = "/";
				break;
			}

			str = str.substring(0, 4) + gubun + str.substring(4, 6);
			// System.out.println(str.length());
		}

		return str;
	}

	public static String nl2br(String s) {
		return s.replaceAll("\n", "<br/>");
	}

	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// 문자열 자르기
	// @param String str : 문자열
	// int val : 문자열 길이
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String getByteCut(String str, int val)
			throws UnsupportedEncodingException {
		// null check
		str = CheckUtil.NullChk(str);
		System.out.println(str + ":" + str.getBytes().length);
		if (str.equals("") || str.getBytes().length <= val) {
			return str;
		}
		String a = str;
		int i = 0;
		String temp = "";
		String result = "";
		temp = a.substring(0, 1);
		while (i < val) {
			byte[] ar = temp.getBytes();
			i += ar.length;
			result += temp;
			a = a.substring(1);
			if (a.length() == 1) {
				temp = a;
			} else if (a.length() > 1) {
				temp = a.substring(0, 1);
			}
		}
		return result + "...";
	}

	// 파일확장자 가져오기
	public static String getExtension(String fileStr) {
		return fileStr
				.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
	}

	// 파일확장자 가져오기 (.포함)
	public static String getExtension2(String fileStr) {

		if (fileStr.lastIndexOf(".") == -1)
			return "";

		return fileStr.substring(fileStr.lastIndexOf("."), fileStr.length());
	}

		
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// 문자열 자르기 II
	// @param str 원본 String
	// @param int 자를 바이트 개수
	// @return String 절단된 String
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String spaces(int count) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public static String cutInStringByBytes(String str, int length) {
		byte[] bytes = str.getBytes();
		int len = bytes.length;
		int counter = 0;
		if (length >= len) {
			return str + spaces(length - len);
		}
		for (int i = length - 1; i >= 0; i--) {
			if (((int) bytes[i] & 0x80) != 0)
				counter++;
		}
		return new String(bytes, 0, length - (counter % 2))+"..."; 
	}
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// 문자열 자르기 III
	// @param str 원본 String
	// @param int 자를 바이트 개수
	// @return  int - 실제 subString()에서 사용할 절단용 길이
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	  	  
    public static int getCutLength( String str, int byteLength ) {
        
        int length = str.length();
        int retLength = 0;
        int tempSize = 0;
        int asc;
        
        for ( int i = 1; i<=length; i++)
        {
            asc = (int)str.charAt(i-1);
            if ( asc > 127)
            {
                if ( byteLength > tempSize )
                {
                    tempSize += 2;
                    retLength++;
                }
            }
            else
            {
                if ( byteLength > tempSize )
                {
                    tempSize++;
                    retLength++;
                }
            }
        }
        
        return retLength;
    }
	    
	public static String getSplitFirst(String str, String delim) {
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}
		
		if( delim==null || delim.equals(""))
			return str;

		String[] returnArray = str.split(delim);
		return returnArray[0];
	}    
	
	public static String getSplitData(String str, String delim, int index) {
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}
		
		if( delim==null || delim.equals(""))
			return str;

		String[] returnArray = str.split(delim);
		
		if(returnArray.length <= index)
			return str;
		
		return returnArray[index];
	}   
	
	public static String getNullToZero(String str) {
		// null 체크
		if(str==null || str.equals("")) {
			return "0";
		}
		
		return str;
	} 	
	
	//그달의 마지막날
	/*
	 * param yyyy/mm/dd
	 * 에러시 0 반환
	 */
	public static int getLastDayOfMonth(String str) {
		// null 체크
		if(str==null || str.equals("")) {
			return 0;
		}		
		if(str.length() != 10) {
			return 0;
		}
		
		String[] strArr = str.split("/");		
		Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		return oCalendar.getActualMaximum(Calendar.DATE); 
	} 	
	
	//입력된 날들 일수  차이
	
	public static int getDifferenceOfDate(String sDate, String eDate ) {
		// null 체크
		if(sDate==null || eDate==null  ) {
			return 0;
		}	
		if(sDate== "" || eDate=="") {
			return 0;
		}	
		if(sDate.length() != 10 || eDate.length() != 10) {
			return 0;
		}
		
		String[] sDateArr = sDate.split("/");	
		String[] eDateArr = eDate.split("/");
		//Calendar cal = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//cal.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));		
		
		int nYear1 = Integer.parseInt(eDateArr[0]);
		int nMonth1 = Integer.parseInt(eDateArr[1]);
		int nDate1 = Integer.parseInt(eDateArr[2]);
		int nYear2 = Integer.parseInt(sDateArr[0]);
		int nMonth2 = Integer.parseInt(sDateArr[1]);
		int nDate2 = Integer.parseInt(sDateArr[2]);
		
		Calendar cal = Calendar.getInstance ( );
		int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;
		
		if ( nYear1 > nYear2 )
		{
			for ( int i = nYear2; i < nYear1; i++ ) 
			{
				cal.set ( i, 12, 0 );
				nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
			}
			nTotalDate1 += nDiffOfYear;
		}
		else if ( nYear1 < nYear2 )
		{
			for ( int i = nYear1; i < nYear2; i++ )
			{
				cal.set ( i, 12, 0 );
				nDiffOfYear += cal.get ( Calendar.DAY_OF_YEAR );
			}
			nTotalDate2 += nDiffOfYear;
		}
		
		cal.set ( nYear1, nMonth1-1, nDate1 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate1 += nDiffOfDay;
		cal.set ( nYear2, nMonth2-1, nDate2 );
		nDiffOfDay = cal.get ( Calendar.DAY_OF_YEAR );
		nTotalDate2 += nDiffOfDay;
		return nTotalDate1-nTotalDate2;		
		
	} 		
	/*
	 * getCurrentDay("2010/01/22")
	 * param 2010/01/22
	 * 
	 * return 22
	 */
	public static int getCurrentDay(String str) {
		// null 체크
		if(str==null || str.equals("")) {
			return 0;
		}		
				
		String[] strArr = str.split("/");		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		
		return Integer.parseInt(strArr[2]); 
	
	}
	
	/*
	 * getCurrentDay("2010/01/22")
	 * param 2010/01/22
	 * 
	 * return 201001
	 */
	public static String getCurrentMonth(String str) {
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
				
		String[] strArr = str.split("/");		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		
		return strArr[0] + "/" + strArr[1]; 	
	}	
	
	/*
	 * getCurrentDay("2010/01")
	 * param 2010/01
	 * 
	 * return 201002
	 */
	public static String getNextMonth(String str) {
		
		String retVal;
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
				
		String[] strArr = str.split("/");	
		int year = Integer.parseInt(strArr[0]);
		int month = Integer.parseInt(strArr[1]);	
		
		if(month >= 12)
		{
			month = 1;
			year += 1;
		}
		else
			month += 1;
		
		
		retVal = year + "/" + (month < 10 ? "0" + month : month) + "/01";
		
		
		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		
		return retVal; 	
	}	
	
	/*
	 * getCurrentDay("2010/01")
	 * param 2010/01
	 * 
	 * return 201002
	 */
	public static String getNextMonth2(String str) {
		
		String retVal;
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
				
		String[] strArr = str.split("/");	
		int year = Integer.parseInt(strArr[0]);
		int month = Integer.parseInt(strArr[1]);	
		
		if(month >= 12)
		{
			month = 1;
			year += 1;
		}
		else
			month += 1;
		
		
		retVal = year + "/" + (month < 10 ? "0" + month : month);
		
		
		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		
		return retVal; 	
	}		
	
	public static int getDifferenceOfMonth(String sDate, String eDate ) {
		
		int retVal = 0;
		
		// null 체크
		if(sDate==null || eDate==null  ) {
			return 0;
		}	
		if(sDate== "" || eDate=="") {
			return 0;
		}	
		if(sDate.length() > 10 || eDate.length() > 10) {
			return 0;
		}
		
		String[] sDateArr = sDate.split("/");	
		String[] eDateArr = eDate.split("/");
		
		int sYear = Integer.parseInt(sDateArr[0]);
		int sMonth = Integer.parseInt(sDateArr[1]);
		int eYear = Integer.parseInt(eDateArr[0]);
		int eMonth = Integer.parseInt(eDateArr[1]);
		
		retVal= ((eYear - sYear) *12 + eMonth ) - sMonth;
		
		return retVal;
	}
	
	public static String getNextDate(String str ) {
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
		if(str.length() != 10) {
			return "";
		}
		
		String[] strArr = str.split("/");		

		Calendar today = Calendar.getInstance ( );
		today.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		today.add ( Calendar.DATE, 1 );
		Date tomorrow = today.getTime ( );
		
		SimpleDateFormat dFormat = new SimpleDateFormat ( "yyyy/MM/dd" );
		return dFormat.format(tomorrow);
	}
	
	
	  //****************//	
	  //  천단위 콤마 처리   //
	  //****************//
	   public static String replaceComma(String str)
	   {
	    int convert = Integer.parseInt(str);
	    DecimalFormat df = new DecimalFormat("#,###");
	    
	    String formatNum=(String)df.format(convert);
	    return formatNum;
	   }
	   
	/*
	 * getSpaceSpilt("기술전략실 기술전략담당 아무개")
	 * param 
	 * 
	 * return : 기술전략실 기술전략담당
	 */
	public static String getSpaceSpilt(String str, int count) {
		
		String retVal = "";
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
				
		String[] strArr = str.split(" ");		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		
		if(strArr.length >= count)
		{
			for(int i=0;i< count; i++)
			{
				if(i==0)
					retVal = strArr[i];
				else
					retVal += " " + strArr[i];
			}
		}
			
		return retVal; 
	}	  
	
	/*
	 * getDelimToBr("기술전략실/기술전략담당 아무개" , "/")
	 * param 
	 * 
	 * return : 기술전략실<br>기술전략담당
	 */
	public static String getDelimToBr(String str, String delim) {
		
		String retVal = "";
		// null 체크
		if(str==null || str.equals("")) {
			return "";
		}		
				
		String[] strArr = str.split(delim);		
		//Calendar oCalendar = Calendar.getInstance( );  // 현재 날짜/시간 등의 각종 정보 얻기		
		//oCalendar.set(Integer.parseInt(strArr[0]), Integer.parseInt(strArr[1]) - 1, Integer.parseInt(strArr[2]));
		

		for(int i=0;i< strArr.length; i++)
		{
			if(i==0)
				retVal = strArr[i];
			else
				retVal += "<br/>" + strArr[i];
		}
			
		System.out.println(retVal);
		return retVal; 
	}	

	/*
	 * getDefaultPasswd()
	 * param 
	 * 
	 * return : 8자리 임시패스워드 생성
	 */	
	public static String getDefaultPasswd() {
		String password = "";
		for (int i = 0; i < 8; i++) {
			// char upperStr = (char)(Math.random() * 26 + 65);
			char lowerStr = (char) (Math.random() * 26 + 97);
			if (i % 2 == 0) {
				password += (int) (Math.random() * 10);
			} else {
				password += lowerStr;
			}
		}
		System.out.println(password);
		return password;
	}
	
	/*
	 * getDefaultPasswd()
	 * param 
	 * 
	 * return : 8자리 임시패스워드 생성
	 */	
	public static String getDefaultPasswdWithTime(){
		String password = getDefaultPasswd();
		Calendar time = Calendar.getInstance();
		int ms = (time.get(Calendar.MINUTE)) * 60 + (time.get(Calendar.SECOND)) ;
		String suf = Integer.toHexString(ms);
		
		System.out.println(time.get((Calendar.MINUTE)) +"분"+ (time.get(Calendar.SECOND) +"초"+ms));
		return password +""+ suf;
	}
	public static String getAddrSidoConvert(String sido){
		String returnVal = null;
		
		if(sido != null){
		
			returnVal = sido.replaceAll("특별시$", "")
				.replaceAll("광역시$", "")
				.replaceAll("시$", "")
				.replaceAll("도$", "")
				.replaceAll("충청남도$", "충남")
				.replaceAll("충청북도$", "충북")
				.replaceAll("경상남도$", "경남")
				.replaceAll("경상북도$", "경북")
				.replaceAll("경상남도$", "경남")
				.replaceAll("전라남도$", "전남")
				.replaceAll("전라북도$", "전북");				
			
		}
		
		return returnVal;
	}

	/*
	 * getNumberOnly("010-2475-2052")
	 * param 
	 * 
	 * return : 01024752052
	 */
	public static String getNumberOnly(String original){
		StringBuffer sb = new StringBuffer();
		
		for(int i =0; i<original.length();i++)
		{		
			if(Character.isDigit(original.charAt(i)))
			{
				sb.append(sb.charAt(i));
			}
		}		
		return sb.toString();
	}
	
	// String to Unicode
	private static String escapeNonAscii(String str) {

		StringBuilder retStr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			int cp = Character.codePointAt(str, i);
			int charCount = Character.charCount(cp);
			if (charCount > 1) {
				i += charCount - 1; // 2.
				if (i >= str.length()) {
					throw new IllegalArgumentException("truncated unexpectedly");
				}
			}

			if (cp < 128) {
				retStr.appendCodePoint(cp);
			} else {
				retStr.append(String.format("\\u%x", cp));
			}
		}
		return retStr.toString();
	}
	
	// Unicode to String
	public static String unicode2String(String unicode) {

		String working = unicode;
		int index;
		index = working.indexOf("\\u");
		while (index > -1) {
			int length = working.length();
			if (index > (length - 6))
				break;
			int numStart = index + 2;
			int numFinish = numStart + 4;
			String substring = working.substring(numStart, numFinish);
			int number = Integer.parseInt(substring, 16);
			String stringStart = working.substring(0, index);
			String stringEnd = working.substring(numFinish);
			working = stringStart + ((char) number) + stringEnd;
			index = working.indexOf("\\u");
		}
		return working;

		// String text = Uni2Str(unicode);
		// String strArray[] = unicode.split(" ");
		// String text = "";
		// for(String str:strArray){
		// logger.debug("str:"+str);
		// str = str.replace("\\","");
		// String[] arr = str.split("u");
		// for(int i = 1; i < arr.length; i++){
		// int hexVal = Integer.parseInt(arr[i], 16);
		// text += (char)hexVal;
		// }
		// logger.debug("text:"+text);
		// }
		// return text;
	}

}
