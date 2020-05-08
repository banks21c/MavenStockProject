package html.parsing.stock.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	public DateUtil(){
		
	}
	/**
	 * <pre>
	* 일자들의 계산을 수행한다..
	* 제일 마지막의 Parameter pType에 따라서 Return값이 다르다.
	* 둘째 Parameter는 첫째 Parameter의 입력 형식을 지정하고 넷째 Parameter는
	* 셋째 Parameter의 입력형식을 지정한다.
	* Return값의 단위를 정해주는 pType에는 3가지가 올 수 있는데
	* ECOMJDateU.DAY, ECOMJDateU.HOUR, ECOMJDateU.MIN 이다.
	* 각각의 단위는 일단위, 시단위, 분단위 이다.
	* 첫째 Parameter로 입력받은 일자에서 셋째 Parameter로 입력받은 일자를 빼서
	* 나온 결과를 돌려준다.
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	* 간단한 사용예는 다음과 같다.
	*
	* System.out.println(getComputedDate("2002/01/04 00:01","yyyy/MM/dd hh:mm","2002/01/02 23:59","yyyy/MM/dd hh:mm",ECOMJDateU.DAY));
	*
	* 작업 결과로 '1'이 표시된다.
	* </pre>
	* @return long
	* @param pIndate1 java.lang.String
	* @param pInformat1 java.lang.String
	* @param pIndate2 java.lang.String
	* @param pInformat2 java.lang.String
	* @param pType char
	*/
	public static long getComputedDate( String pIndate1, String pInformat1, String pIndate2, String pInformat2, char pType ) {

		SimpleDateFormat pInformatter1 =  new SimpleDateFormat (pInformat1, java.util.Locale.KOREA);
		SimpleDateFormat pInformatter2 =  new SimpleDateFormat (pInformat2, java.util.Locale.KOREA);

		long vDategap = 0;

		try {
			Date vIndate1 = pInformatter1.parse(pIndate1);
			Date vIndate2 = pInformatter2.parse(pIndate2);

			//System.out.println("DateUtil NowDateTime : " + vIndate2);
			//System.out.println("DateUtil RegistrationDateTime : " + vIndate1);
			vDategap = vIndate1.getTime() - vIndate2.getTime();

			if ( pType == 'D' )
				vDategap = vDategap / ( 1000 * 60 * 60 * 24 );
			else if ( pType == 'H' )
				vDategap = vDategap / ( 1000 * 60 * 60 );
			else if ( pType == 'M' )
				vDategap = vDategap / ( 1000 * 60 );

		} catch ( Exception e ) {}

		return vDategap;
	}

	/**
	* 일자들의 계산을 수행한다..
	* getComputedDate()의 사용이 번거로워 편하게 사용하기 위하여 제공된다..
	* 입력 받을 Date의 형식은 반드시 "yyyyMMdd" 이어야 하고
	* 첫째 Parameter로 입력받은 일자에서 셋째 Parameter로 입력받은 일자를 빼서
	* 나온 결과를 돌려준다.
	* 간단한 사용예는 다음과 같다.
	*
	* System.out.println(getDayGap("20020103","20020102"));
	*
	* 작업 결과로 '1'이 표시된다.
	*
	* @return long
	* @param pIndate1 java.lang.String
	* @param pIndate2 java.lang.String
	*/
	public static int getDayGap( String pIndate1, String pIndate2  ) {

		return (int)getComputedDate(pIndate1, "yyyyMMdd", pIndate2, "yyyyMMdd", 'D');
	}

	/**
	* 입력받은 날짜에 일/시/분 단위의 값을 더하여 출력Format에 따라 값을 넘겨준다. <BR><BR>
	* Parameter는 입력일, 입력일 Format, 출력일 Format, 일단위 더하기, 시단위 더하기,
	* 분단위 더하기이다.
	*
		* 간단한 사용예는 다음과 같다.
	*
	* 사용예) System.out.println( getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-100,10,-11) );
	* 결과) 2001/09/23 15:54
	*
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	*
	* @return java.lang.String
	* @param pIndate String
	* @param pInformat String
	* @param pOutformat String
	* @param pDay int
	* @param pHour int
	* @param pMin int
	*/
	public static String getFormattedDateAdd(String pIndate, String pInformat, String pOutformat, int pDay, int pHour, int pMin ) {

		SimpleDateFormat pInformatter =  new SimpleDateFormat (pInformat, java.util.Locale.KOREA);
		SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, java.util.Locale.KOREA);

		String rDateString = "";
		Date vIndate = null;
		long vAddon = ( pDay * 24L*60L*60L*1000L ) + ( pHour * 60L*60L*1000L ) + ( pMin * 60L*1000L );

		try
		{
			vIndate = pInformatter.parse(pIndate);

			Date vAddday = new Date( vIndate.getTime() + vAddon );
			rDateString = pOutformatter.format(vAddday);

		} catch( Exception e ) {
			rDateString = pIndate;
		}

		return rDateString;
	}

	//getFormattedDateAdd("200201010605","yyyyMMddhhmm","yyyy/MM/dd HH:mm",-100,10,-11) 간편화
	public static String getDayAdd( String inputDate, int addDay  ) {
		return getFormattedDateAdd(inputDate, "yyyyMMdd", "yyyyMMdd", addDay,0,0);
	}

	
	
	/**
	* 오늘 일자를 지정된 Format의 날짜 표현형식으로 돌려준다. <BR><BR>
	*
	* 사용예) getToday("yyyy/MM/dd hh:mm a")<BR>
	* 결 과 ) 2001/12/07 10:10 오후<BR><BR>
	*
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	*
	* @return java.lang.String
	* @param pOutformat String
	*/
	public static String getToday( String pOutformat) {

		SimpleDateFormat pOutformatter =  new SimpleDateFormat (pOutformat, Locale.KOREA );

		String rDateString = null;
		Date vDate = new Date();

		try
		{
			rDateString = pOutformatter.format(vDate);

		} catch( Exception e ) {}

		return rDateString;
	}

	/**
	 * <pre>
	* 오늘 날짜에 년/월/일 단위의 값을 더하여 출력Format에 따라 값을 넘겨준다. <BR><BR>
	* Parameter는 출력일 Format, 년단위 더하기, 월단위 더하기,
	* 일단위 더하기이다.
	*
	* 간단한 사용예는 다음과 같다.
	*
	* 사용예)  System.out.println( SSDateUtil.getToday("yyyy/MM/dd") );
	*          System.out.println( SSDateUtil.getTodayAddDate("MMM dd. yyyy", 3, -4, 6) );
	* 결과)    2006/04/25
	*          Dec 31. 2008
	*
	* Format은 J2SE의 SimpleDateFormat의 Documentation을 참고한다.
	* </pre>
	* 
	* @return java.lang.String
	* @param szOutformat String
	* @param nYear int
	* @param nMonth int
	* @param nDay int
	*/
	public static String getTodayAddDate(String szOutformat, int nYear, int nMonth, int nDay ) {

	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR,  nYear);
	    cal.add(Calendar.MONTH, nMonth);
	    cal.add(Calendar.DATE,  nDay);
	    
	    DecimalFormat dfDate = new DecimalFormat("00");
	    String szTargetDay = cal.get(Calendar.YEAR) + dfDate.format(cal.get(Calendar.MONTH) + 1) + dfDate.format(cal.get(Calendar.DATE));
	    
	    SimpleDateFormat dfTmp = new SimpleDateFormat ("yyyyMMdd");
	    SimpleDateFormat dfOut = new SimpleDateFormat (szOutformat, Locale.UK);
	    
	    String szDate = null;
	    try {
	        szDate = dfOut.format(dfTmp.parse(szTargetDay.toString()));
	    } catch (ParseException e) {
	        szDate = szTargetDay;
	    }
	    
	    return szDate;
	}

	public static String getKorDateType(String yyyyMMdd){
		java.util.Calendar c= java.util.Calendar.getInstance();
		String yyyy = yyyyMMdd.substring(0,4);
		String mm = yyyyMMdd.substring(4,6);
		String dd = yyyyMMdd.substring(6,8); 
		c.clear();
	    c.set(TypeConvertUtil.string2int(yyyy), TypeConvertUtil.string2int(mm), TypeConvertUtil.string2int(dd));
	    String ch_week = "";
	    switch(c.get(c.DAY_OF_WEEK)) {
	       case java.util.Calendar.SUNDAY:
	          ch_week = "(일)";
	          break;
	       case java.util.Calendar.MONDAY:
	          ch_week = "(월)";
	          break;
	       case java.util.Calendar.TUESDAY:
	          ch_week = "(화)";
	          break;
	       case java.util.Calendar.WEDNESDAY:
	          ch_week = "(수)";
	          break;
	       case java.util.Calendar.THURSDAY:
	          ch_week = "(목)";
	          break;
	       case java.util.Calendar.FRIDAY:
	          ch_week = "(금)";
	          break;
	       case java.util.Calendar.SATURDAY:
	          ch_week = "(토)";
	          break;
	    }    
	    return mm+"월 "+dd+"일 "+ch_week;
	}

	public static String getCurrencyFormat(String strInput)
	{
		String strComm = "";  /* Comma가 추가된 값 */
		String strSign = "";  /* 부호             */
		String strCent = "";  /* 소숫점 이하의 값   */

		int iLoop = strInput.length() - 3;
		int j;
		for ( j = iLoop ; j >= 1 ; j = j - 3 ) {

			strComm = "," + strInput.substring(j, j + 3) + strComm;
		}
		strComm = strSign + strInput.substring(0 , j + 3) + strComm;
		return strComm;
	}
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// 연도 리턴
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String getYear() {
        Locale lc = new Locale("Locale.KOREAN","Locale.KOREA");
		TimeZone mySTZ = (TimeZone)TimeZone.getTimeZone ("JST");
		Calendar today = Calendar.getInstance(mySTZ, lc);
		int year = today.get(Calendar.YEAR);
		String str = "";
		str += year;
		return str;
	}	
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// HH:MM:SS 형식 리턴 - LogWriter에서 사용
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String getNowTime() {
        Locale lc = new Locale("Locale.KOREAN","Locale.KOREA");
		TimeZone mySTZ = (TimeZone)TimeZone.getTimeZone ("JST");
		Calendar today = Calendar.getInstance(mySTZ, lc);
		int hour = today.get(Calendar.HOUR_OF_DAY);
	 	int min = today.get(Calendar.MINUTE);
		int sec = today.get(Calendar.SECOND);
		String str = "";
		if(hour < 10) str += "0";
			str += hour;
		str += ":";
		if(min < 10) str += "0";
			str += min;
		str += ":";
		if(sec < 10) str += "0";
			str += sec;
		return str;
	}	
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// YYYYMMDDHHMM 형식 리턴
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String getYYYYMMDDHHMM() {
        Locale lc = new Locale("Locale.KOREAN","Locale.KOREA");
		TimeZone mySTZ = (TimeZone)TimeZone.getTimeZone ("JST");
		Calendar today = Calendar.getInstance(mySTZ, lc);
		int year = today.get(Calendar.YEAR);
		int mon = today.get(Calendar.MONTH)+1;
		int day = today.get(Calendar.DAY_OF_MONTH);
		int hour = today.get(Calendar.HOUR_OF_DAY);
	 	int min = today.get(Calendar.MINUTE);
		String str = "";
		str += year;
		if(mon < 10) str += "0";
			str += mon;
		if(day < 10) str += "0";
			str += day;
		if(hour < 10) str += "0";
			str += hour;
		if(min < 10) str += "0";
			str += min;
		return str;
	}	
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// YYYY.MM.DD 형식  리턴
	// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	public static String getYYYYMMDD(String sDele) {
        Locale lc = new Locale("Locale.KOREAN","Locale.KOREA");
		TimeZone mySTZ = (TimeZone)TimeZone.getTimeZone ("JST");
		Calendar today = Calendar.getInstance(mySTZ, lc);
		int year = today.get(Calendar.YEAR);
		int mon = today.get(Calendar.MONTH)+1;
		int day = today.get(Calendar.DAY_OF_MONTH);
		String str = "";
		str += (year+sDele);
		if(mon < 10) str += "0";
			str += (mon+sDele);
		if(day < 10) str += "0";
			str += day;
		return str;
	}	
	
	// string to date
	public static Date getDateFromStr(String formatStr, String dateStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	//Date to data add Date
	public static Date getDateAdd(Date date, String type, int data){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		if("h".equals(type)){
			cal.add(Calendar.HOUR, data);
		} else if("m".equals(type)){
			cal.add(Calendar.MINUTE, data);
		} else if("s".equals(type)){
			cal.add(Calendar.SECOND, data);
		}
		Date oneHourBack = cal.getTime();		
		
		return oneHourBack;
	}

	/**
	 * <pre>
	* return 
	* 1 : 암에것이 큼
	* 0 : 서로같음
	* 2 : 뒤에것이 큼 
	* </pre>
	* @return int
	* @param date1 Date
	* @param date2 Date
	*/	
	public static int compareDate(Date date1, Date date2){
		int returnVal = 0;
		
		if(date1.compareTo(date2) == 0){
			returnVal = 0;
		} else if(date1.compareTo(date2) < 0) {
			returnVal = 2;
		} else if(date1.compareTo(date2) > 0) {
			returnVal = 1;
		}
		
		return returnVal;
	}
    public static void main(String[] args) {
        int year = 2019;
        int month = 2; // January
        int date = 15;

        Calendar cal = Calendar.getInstance();
        cal.clear();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, date);

        java.util.Date utilDate = cal.getTime();
        System.out.println(utilDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss", Locale.KOREAN);
        String strDate = sdf.format(new Date());
        System.out.println(strDate);
    }
}