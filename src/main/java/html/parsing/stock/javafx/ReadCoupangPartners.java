/**
 * 
 */
package html.parsing.stock.javafx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * @author banksfamily
 *
 */
public class ReadCoupangPartners {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ReadCoupangPartners();
	}

	/**
	 * 
	 */
	public ReadCoupangPartners() {
		List<Map<String, String>> userMapList = readCoupangPartners();
		System.out.println("userMapList:"+userMapList);
		List<String> userList = getUserList(userMapList);
		System.out.println("userList:"+userList);
	}
	
	public static List<Map<String, String>> readCoupangPartners() {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		InputStream is = null;
		try {
			System.out.println("ReadCoupangPartners.class.getProtectionDomain().getCodeSource().getLocation():"
					+ ReadCoupangPartners.class.getProtectionDomain().getCodeSource().getLocation());
			System.out.println("ReadCoupangPartners.class.getProtectionDomain().getCodeSource().getLocation().getPath():"
					+ ReadCoupangPartners.class.getProtectionDomain().getCodeSource().getLocation().getPath());
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource(\"coupangPartners.json\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource("coupangPartners.json"));
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource(\"./coupangPartners.json\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource("./coupangPartners.json"));
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource(\"/coupangPartners.json\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource("/coupangPartners.json"));
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource(\"/\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource("/"));
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource(\".\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResource("."));
			System.out
					.println("ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResourceAsStream(\"./coupangPartners.json\"):"
							+ ReadCoupangPartners.class.getProtectionDomain().getClassLoader().getResourceAsStream("./coupangPartners.json"));

			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			System.out.println(". AbsolutePath:" + new File(".").getAbsolutePath());
			File f = new File("./coupangPartners.json");
			System.out.println("f.exists():" + f.exists());
			if (f.exists()) {
				is = new FileInputStream(f);
				userList = readJson(is);
			} else {
				// classes root 경로
				is = ReadCoupangPartners.class.getResourceAsStream("/coupangPartners.json");
				System.out.println("class 경로 read /coupangPartners.json Resource");
				userList = readJson(is);
			}
			System.out.println("is :" + is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public static List<Map<String, String>> readJson(InputStream is) throws IOException {
		List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
		Reader reader = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(reader);
		StringBuilder sBuilder = new StringBuilder();
		String line = "";
		while ((line = br.readLine()) != null) {
			sBuilder.append(line + "\n");
		}
		System.out.println(sBuilder.toString());
		JSONObject jo1 = new JSONObject(sBuilder.toString());
		Iterator it = jo1.keys();
		while (it.hasNext()) {
			Map<String, String> userMap = new HashMap<String, String>();
			String coupangId = (String) it.next();
			userMap.put("coupangId", coupangId);
			System.out.println("coupangId:" + coupangId);
//			JSONObject jo2 = jo1.getJSONObject(coupangId);
			JSONObject jo2 = (JSONObject) jo1.get(coupangId);
			Iterator it2 = jo2.keys();
			while (it2.hasNext()) {
				String keyId = (String) it2.next();
				String keyValue = jo2.getString(keyId);
				System.out.println(keyId + ":" + keyValue);
				userMap.put(keyId, keyValue);
			}
			System.out.println("userMap:" + userMap);
			userList.add(userMap);
		}
		br.close();
		reader.close();
		is.close();
		System.out.println("userList:" + userList);
		return userList;
	}
	
	public static List<String> getUserList(List<Map<String, String>> userMapList) {
		List<String> userList = new ArrayList<String>();
		for(Map<String,String> m:userMapList) {
			String coupangId = (String)m.get("coupangId");
			userList.add(coupangId);
		}
		return userList;
	}

}
