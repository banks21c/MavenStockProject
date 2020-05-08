package html.parsing.stock.util;

import java.util.Map;

import org.json.JSONObject;

public class StringConvert {

	public static String ObjectToString(Object obj)
	{
		try 
		{
			return obj.toString();
		}
		catch(Exception e)
		{
			return "";
		}
	}

	public static String ObjectToString(Map<String, String> map, String key)
	{
		try 
		{
			String result = map.get(key);
			return result == null ? "" : result;
		}
		catch(Exception e)
		{
			return "";
		}
	}

	public static String ObjectToString(JSONObject object, String key)
	{
		try 
		{
			String result = String.valueOf(object.get(key));
			return result == null ? "" : result;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return "";
		}
	}
}
