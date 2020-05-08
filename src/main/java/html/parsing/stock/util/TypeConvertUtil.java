package html.parsing.stock.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class TypeConvertUtil {
	public TypeConvertUtil(){
		
	}
	 
	public static int string2int(String s)
	{
	    try {
	        return Integer.parseInt(s);
	    } catch (Exception e) {
	        //DEBUG.PRINT(e.toString() + ". Ignored! string2int() returns 0.");
	        return 0;
	    }
	}

	public static String int2string(int i)
	{
	    return String.valueOf(i);
	}

	public static String strGetStringToFormat(String strInput)
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
	
	public static String null2Blank(String arg){
		return arg == null || "null".equals(arg) ? "" : arg.trim(); 
	}
	
	public static String null2Zero(String arg){
		return arg == null || "null".equals(arg) ? "0" : arg.trim(); 
	}
	
	public static String null2inputparam(String arg, String inputParam){
		return arg == null || "null".equals(arg) ? inputParam : arg.trim(); 
	}
	
	public static String getParameterOrAttribute(HttpServletRequest request, String paramName){
		String value = request.getParameter(paramName);
		return value == null ? (String)request.getAttribute(paramName): value;
	}
	
	public static int nullToZero(Integer arg){
		return arg == null ? 0 : arg; 
	}
	
	public static int nullToZero(String arg){
		return arg == null ? 0 : Integer.parseInt(arg); 
	}
	
	public static int nullToNumber(String arg, int number){
		return arg == null ? number : Integer.parseInt(arg); 
	}	
	
	public static Map<String, String> getQueryMap(String query)  
	{  
	    String[] params = query.split("&");  
	    Map<String, String> map = new HashMap<String, String>();  
	    for (String param : params)  
	    {  
	    	String[] pairArr = param.split("=");
	    	String name = "";
	    	String value = "";
	    	if(pairArr != null && pairArr.length == 1){
	    		name = param.split("=")[0];  
	    	} else if(pairArr != null && pairArr.length == 2){
	    		name = param.split("=")[0];
	    		value = param.split("=")[1];
	    	}   
	        map.put(name, value);  
	    }  
	    return map;  
	}  	
}
