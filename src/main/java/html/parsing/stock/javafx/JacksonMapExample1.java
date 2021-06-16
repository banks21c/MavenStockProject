package html.parsing.stock.javafx;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

//import org.codehaus.jackson.node.ObjectNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public class JacksonMapExample1 {
	// not including SerializationFeatures for brevity
	static final ObjectMapper mapper = new ObjectMapper();

	final String bestCategoriesArray3 = "{  \"1001\":\"여성패션\" ,  \"1002\":\"남성패션\" ,  \"1010\":\"뷰티\" ,"
			+ "\"1011\":\"출산/유아동\" ,  \"1012\":\"식품\" ,  \"1013\":\"주방용품\" ,  \"1014\":\"생활용품\" ,  \"1015\":\"홈인테리어\" ,"
			+ "\"1016\":\"가전디지털\" ,  \"1017\":\"스포츠/레저\" ,  \"1018\":\"자동차용품\" ,  \"1019\":\"도서/음반/DVD\" ,"
			+ "\"1020\":\"완구/취미\" ,  \"1021\":\"문구/오피스\" ,  \"1024\":\"헬스/건강식품\" ,  \"1025\":\"국내여행\" ,  \"1026\":\"해외여행\" ,"
			+ "\"1029\":\"반려동물용품\" ,  \"1030\":\"유아동패션\"  }";

	public JacksonMapExample1() {
//		objectMapperTest();
//		convObjToONode(bestCategoriesArray3);
//		convObjToONode2(bestCategoriesArray3);
		convObjToONode3(bestCategoriesArray3);
	}

	// pass it your payload
	public static ObjectNode convObjToONode(Object o) {
		StringWriter stringify = new StringWriter();
		ObjectNode objToONode = null;

		try {
			mapper.writeValue(stringify, o);
			objToONode = (ObjectNode) mapper.readTree(stringify.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(objToONode);
		return objToONode;
	}

	// pass it your payload
	public static TextNode convObjToONode2(Object o) {
		StringWriter stringify = new StringWriter();
		TextNode objToONode = null;

		try {
			mapper.writeValue(stringify, o);
			objToONode = (TextNode) mapper.readTree(stringify.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(objToONode);
		return objToONode;
	}

	// pass it your payload
	public static JsonNode convObjToONode3(Object o) {
		StringWriter stringify = new StringWriter();
		JsonNode objToONode = null;

		try {
			mapper.writeValue(stringify, o);
			objToONode = (JsonNode) mapper.readTree(stringify.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(objToONode);
		return objToONode;
	}

	public void objectMapperTest() {

//	        String json = "{\"name\":\"mkyong\", \"age\":\"37\"}";
		String json = "{'name':'mkyong', 'age':'37'}";
		json = json.replace("'", "\"");

		try {

			// convert JSON string to Map
			Map<String, String> map = mapper.readValue(bestCategoriesArray3, Map.class);

			// it works
			// Map<String, String> map = mapper.readValue(json, new
			// TypeReference<Map<String, String>>() {});

			System.out.println(map);
			String strJson = new ObjectMapper().writeValueAsString(map);
			System.out.println(strJson);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		new JacksonMapExample1();

	}
}
