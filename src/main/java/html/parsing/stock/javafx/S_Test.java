package html.parsing.stock.javafx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class S_Test {

	public S_Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		final String[][] bestCategoriesArray = { { "1001", "여성패션" }, { "1002", "남성패션" }, { "1010", "뷰티" },
				{ "1011", "출산/유아동" }, { "1012", "식품" }, { "1013", "주방용품" }, { "1014", "생활용품" }, { "1015", "홈인테리어" },
				{ "1016", "가전디지털" }, { "1017", "스포츠/레저" }, { "1018", "자동차용품" }, { "1019", "도서/음반/DVD" },
				{ "1020", "완구/취미" }, { "1021", "문구/오피스" }, { "1024", "헬스/건강식품" }, { "1025", "국내여행" }, { "1026", "해외여행" },
				{ "1029", "반려동물용품" }, { "1030", "유아동패션" } };
		final String[] bestCategoriesArray2 = {  "1001:여성패션" ,  "1002:남성패션" ,  "1010:뷰티" ,
				 "1011:출산/유아동" ,  "1012:식품" ,  "1013:주방용품" ,  "1014:생활용품" ,  "1015:홈인테리어" ,
				 "1016:가전디지털" ,  "1017:스포츠/레저" ,  "1018:자동차용품" ,  "1019:도서/음반/DVD" ,
				 "1020:완구/취미" ,  "1021:문구/오피스" ,  "1024:헬스/건강식품" ,  "1025:국내여행" ,  "1026:해외여행" ,
				 "1029:반려동물용품" ,  "1030:유아동패션"  };		
		final String bestCategoriesArray3 = "  \"1001:여성패션\" ,  \"1002:남성패션\" ,  \"1010:뷰티\" ,"
				 +"\"1011:출산/유아동\" ,  \"1012:식품\" ,  \"1013:주방용품\" ,  \"1014:생활용품\" ,  \"1015:홈인테리어\" ,"
				 +"\"1016:가전디지털\" ,  \"1017:스포츠/레저\" ,  \"1018:자동차용품\" ,  \"1019:도서/음반/DVD\" ,"
				 +"\"1020:완구/취미\" ,  \"1021:문구/오피스\" ,  \"1024:헬스/건강식품\" ,  \"1025:국내여행\" ,  \"1026:해외여행\" ,"
				 +"\"1029:반려동물용품\" ,  \"1030:유아동패션\"  ";		
		 List<String[]> lst = Arrays.asList(bestCategoriesArray);
		System.out.println(lst);
		for(String[] s:lst) {
			System.out.println(s);
			System.out.println(s.length);
			for(int i=0;i<s.length;i++) {
				System.out.println(s[i]);
			}			
		}
		ArrayList<String[]> arrayList = new ArrayList<>(Arrays.asList(bestCategoriesArray));
		
		System.out.println(bestCategoriesArray2.toString());
		
		
		
	}
}
