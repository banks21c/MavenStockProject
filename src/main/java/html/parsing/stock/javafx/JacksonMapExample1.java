package html.parsing.stock.javafx;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class JacksonMapExample1 {

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper();
//        String json = "{\"name\":\"mkyong\", \"age\":\"37\"}";
        String json = "{'name':'mkyong', 'age':'37'}";
       json = json.replace("'", "\"");
		final String bestCategoriesArray3 = "{  \"1001\":\"여성패션\" ,  \"1002\":\"남성패션\" ,  \"1010\":\"뷰티\" ,"
				 +"\"1011\":\"출산/유아동\" ,  \"1012\":\"식품\" ,  \"1013\":\"주방용품\" ,  \"1014\":\"생활용품\" ,  \"1015\":\"홈인테리어\" ,"
				 +"\"1016\":\"가전디지털\" ,  \"1017\":\"스포츠/레저\" ,  \"1018\":\"자동차용품\" ,  \"1019\":\"도서/음반/DVD\" ,"
				 +"\"1020\":\"완구/취미\" ,  \"1021\":\"문구/오피스\" ,  \"1024\":\"헬스/건강식품\" ,  \"1025\":\"국내여행\" ,  \"1026\":\"해외여행\" ,"
				 +"\"1029\":\"반려동물용품\" ,  \"1030\":\"유아동패션\"  }";		

        try {

            // convert JSON string to Map
            Map<String, String> map = mapper.readValue(bestCategoriesArray3, Map.class);

			// it works
            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});

            System.out.println(map);
            String strJson = new ObjectMapper().writeValueAsString(map);
            System.out.println(strJson);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
 