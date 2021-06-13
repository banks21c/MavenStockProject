package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HourTest {
	
	private static String FAVORITE_KEYWORDS[] = { "물티슈", "커피믹스", "커피", "건전지", "화장지", "기저귀", "생리대", "피죤", "샴퓨", "세제",
			"노트북", "데스크탑", "스피커", "카메라", "모니터", "이어폰", "헤드폰", "로션", "스킨", "핸드폰 케이스", "스탠드", "애견 간식", "정수기", "가습기",
			"벽거울", "책상", "의자" };
	
	public HourTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int iHourMinute = Integer.parseInt(new SimpleDateFormat("HHmm").format(new Date()));
		System.out.println("iHourMinute :"+iHourMinute);

		System.out.println(Math.random());
		
		int randomNumber = (int) (Math.random() * FAVORITE_KEYWORDS.length);
		System.out.println(randomNumber);
		int index = randomNumber > 0 ? randomNumber - 1 : 0;
		System.out.println(index);
		String selectedKeyword = FAVORITE_KEYWORDS[index];
		System.out.println("selectedKeyword :"+selectedKeyword);
	}

}
