
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpURLConnectionTest {
    private final static Logger logger = LoggerFactory.getLogger(HttpURLConnectionTest.class);

	public HttpURLConnectionTest() {
		String url = "http://10.118.242.16:10380/econ/search?collectionId=8&skip=0&limit=30&volume=general1&q=%EA%B0%80%EB%94%94%EA%B1%B4";
		try {
			postRequest(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testCodota() {
		InputStream in;
		in = System.in;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	}

	public void postRequest(String urlStr) throws IOException {
		URL url = new URL(urlStr);

		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		logger.debug("getResponseCode:" + httpURLConnection.getResponseCode());
		logger.debug("getResponseCode:" + httpURLConnection.getResponseCode());
		logger.debug("getContentEncoding:" + httpURLConnection.getContentEncoding());
		logger.debug("getContentLength:" + httpURLConnection.getContentLength());
		logger.debug("getContentType:" + httpURLConnection.getContentType());
		logger.debug("getContentLengthLong:" + httpURLConnection.getContentLengthLong());
		logger.debug("getContent:" + httpURLConnection.getContent());

		if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			InputStream is = httpURLConnection.getInputStream();
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
				String line;
				StringBuffer sb = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
					System.out.println(line);
				}
				String data = sb.toString();
				System.out.println("data :" + data);

				JSONArray arrayObj = null;
				JSONParser jsonParser = new JSONParser();
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
					System.out.println("object.toString() :" + jsonObject.toString());
					getJsonObject("", jsonObject);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Json object :: " + arrayObj);
			}
		} else {
			// ... do something with unsuccessful response
		}
	}

	int objDepth = 0;
	int arrayDepth = 0;
	public void getJsonObject(String strKey, JSONObject jsonObject) {
		objDepth++;
		Set keySet = jsonObject.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String keyObj = (String) it.next();
			Object valueObj = jsonObject.get(keyObj);
			for(int i=0;i<objDepth;i++) {
				System.out.print("\t");	
			}
			System.out.println(keyObj+" : " + valueObj.toString());
			if (valueObj instanceof JSONObject) {
				JSONObject value = (JSONObject) valueObj;
				getJsonObject(keyObj,value);
			} else if (valueObj instanceof JSONArray) {
				JSONArray value = (JSONArray) valueObj;
				getJsonArray(keyObj,value);
			}else {
			}
		}
		objDepth--;
	}

	public void getJsonArray(String strKey, JSONArray jsonArray) {
		arrayDepth++;
		Iterator it = jsonArray.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (obj instanceof JSONObject) {
				JSONObject jsonObject = (JSONObject) obj;
				for(int i=0;i<arrayDepth;i++) {
					System.out.print("\t");	
				}
				System.out.println("jsonObject :" + jsonObject);
				getJsonObject(strKey, jsonObject);
			} else {
				System.out.println("obj :" + obj.toString());
			}
		}
		arrayDepth++;
	}

	public void postRequest(String urlStr, String jsonBodyStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Content-Type", "application/json");
		try (OutputStream outputStream = httpURLConnection.getOutputStream()) {
			outputStream.write(jsonBodyStr.getBytes());
			outputStream.flush();
		}
		if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(httpURLConnection.getInputStream()))) {
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
				}
			}
		} else {
			// ... do something with unsuccessful response
		}
	}

	public static void main(String[] args) {
		new HttpURLConnectionTest();

	}

}
