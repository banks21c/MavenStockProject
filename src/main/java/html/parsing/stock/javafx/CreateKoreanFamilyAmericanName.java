package html.parsing.stock.javafx;

public class CreateKoreanFamilyAmericanName {
	String familyName[] = { "갈", "강", "건", "고", "공", "곽", "구", "국", "권", "금", "기", "길", "김", "나", "남", "남궁", "노", "도",
			"라", "류", "마", "맹", "명", "모", "목", "문", "민", "박", "반", "방", "배", "백", "변", "부", "빙", "서", "석", "선우", "설",
			"성", "소", "손", "송", "신", "심", "안", "양", "어", "엄", "여", "연", "염", "오", "옥", "왕", "우", "원", "위", "유", "육",
			"윤", "이", "인", "임", "장", "전", "정", "제", "조", "주", "지", "진", "차", "채", "천", "최", "추", "탁", "표", "하", "한",
			"함", "허", "현", "홍", "황" };
	String maleNames[] = { "제임스", "존", "로버트", "마이클", "윌리암", "데이빗", "리차드", "요셉", "토마스", "찰스", "크리스토퍼", "다니엘", "매튜",
			"안토니", "도날드", "마크", "폴", "스티븐", "앤드류", "케네스", "조슈아", "케빈", "브라이언", "조지", "에드워드", "로날드", "티모시", "제이선", "제프리",
			"라이언", "야콥", "게리", "니콜라스", "에릭", "조나단", "스페판", "래리", "저스틴", "스코트", "브랜든", "벤자민", "사무엘", "프랭크", "그레고리",
			"레이몬드", "알렉산더", "패트릭", "잭", "데니스", "제리", "타일러", "아론", "조즈", "헨리", "아담", "더글라스", "나단", "피터", "재커리", "카일",
			"월터", "헤롤드", "제레미", "에단", "칼", "카이스", "로저", "제럴드", "크리스틴", "테리", "씬", "아더", "오스틴", "노아", "로렌스", "제쓰", "조",
			"브라이언", "빌리", "조단", "알버트", "딜런", "브루스", "윌리", "가브리엘", "알란", "주안", "로건", "웨인", "랠프", "로이", "유진", "랜디", "빈센트",
			"러셀", "루이스", "필립", "바비", "조니", "브래들리" };
	String femaleName[] = { "메리", "패트리샤", "제니퍼", "린다", "엘리자베스", "바바라", "수잔", "제시카", "사라", "카렌", "낸시", "리사", "마가렛", "베티",
			"산드라", "애쉴리", "도로시", "킴벌리", "에밀리", "도나", "미쉘", "캐롤", "아만다", "멜리사", "드보라", "스테파니", "레베카", "로라", "샤론", "신디아",
			"캐슬린", "에이미", "셜리", "안젤라", "헬렌", "안나", "브렌다", "파멜라", "니콜", "사만다", "캐서린", "엠마", "룻", "크리스틴", "캐서린", "드보라",
			"레이첼", "캐롤린", "자넷", "버지니아", "마리아", "헤더", "다이앤", "줄리", "조이스", "빅토리아", "켈리", "크리스티나", "로렌", "조안", "에블린",
			"올리비아", "주디스", "메간", "쉐릴", "마사", "안드리아", "프랜시스", "한나", "재클린", "앤", "글로리아", "진", "캐서린", "앨리스", "테레사", "사라",
			"재니스", "도리스", "메디슨", "줄리아", "그레이스", "주디", "아비가일", "메리", "데니즈", "비벌리", "앰버", "테레사", "메릴린", "다니엘", "다이아나",
			"브리트니", "나탈리", "소피아", "로즈", "이사벨라", "알렉시스", "카일라", "샬럿" };

	public CreateKoreanFamilyAmericanName() {
		for (int i = 0; i < familyName.length; i++) {
			for (int j = 0; j < maleNames.length; j++) {
				if (maleNames[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+maleNames[j]+" "+familyName[i]);
				} else {
				}
//				System.out.println(maleNames[j] + " " + familyName[i]);
			}
		}
//		System.out.println("==========================================");
		for (int i = 0; i < familyName.length; i++) {
			for (int j = 0; j < femaleName.length; j++) {
				if (femaleName[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+femaleName[j]+" "+familyName[i]);
				} else {
				}
//				System.out.println(femaleName[j] + " " + familyName[i]);
			}
		}
//		System.out.println("==========================================");
		for (int j = 0; j < maleNames.length; j++) {
			for (int i = 0; i < familyName.length; i++) {
				if (maleNames[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+maleNames[j]+" "+familyName[i]);
				} else {
				}
//				System.out.println(maleNames[j] + " " + familyName[i]);
			}
		}
		System.out.println("==========================================");
		for (int j = 0; j < femaleName.length; j++) {
			for (int i = 0; i < familyName.length; i++) {
				if (femaleName[j].equals(familyName[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+femaleName[j]+" "+familyName[i]);
				} else {
				}
				System.out.println(femaleName[j] + " " + familyName[i]);
			}
		}
	}

	public static void main(String[] args) {
		new CreateKoreanFamilyAmericanName();
	}

}
