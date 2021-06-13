package html.parsing.stock.javafx;

import org.apache.commons.lang3.StringUtils;

public class CreateKoreanName {
	String familyNameKo[] = {"갈","강","건","고","공","곽","구","권","기","김","남","노","도","류","마","모","목","문","민","박","방","배","백","부","빙","서","석","선우","성","손","송","신","심","안","양","어","여","오","왕","우","원","위","유","윤","이","임","장","전","정","제","조","주","지","진","채","최","표","하","한","홍","황"};
	String familyNameEn[] = {"gahl","gahng","geon","go","gong","gwak","goo","gwon","gi","gim","nahm","noh","doh","ryu","mah","moh","mok","moon","min","park","bahng","bae","baek","boo","bing","seo","seok","seonwoo","seong","son","song","sin","sim","ahn","yahng","eo","yeo","oh","wahng","woo","won","wi","yoo","yoon","lee","lim","jahng","jeon","jeong","je","jo","ju","ji","jin","chae","choi","pyo","hah","hahn","hong","hwang"};
	String maleNamesKo[] = {"강민","건","건우","규민","도윤","도현","도훈","동현","민규","민석","민성","민우","민재","민준","민찬","민혁","민호","서우","서준","서진","선우","성민","성준","성현","수현","수호","승민","승우","승원","승준","승현","시온","시우","시원","시윤","시현","시후","연우","예성","예준","예찬","우빈","우주","우진","유준","유찬","윤성","윤우","윤재","윤호","율","은성","은우","은찬","은호","이안","이준","재민","재원","재윤","재현","정민","정우","주원","준","준서","준성","준수","준영","준우","준혁","준호","준희","지민","지성","지안","지완","지우","지원","지율","지한","지호","지환","지후","지훈","진우","태민","태양","태윤","태현","하람","하민","하율","하준","하진","한결","현서","현수","현우","현준"};
	String maleNamesEn[] = {"gahngmin","geon","geonwoo","gyumin","doyoon","dohyun","dohoon","donghyun","minkyu","minseok","minseong","minwoo","minjae","minjun","minchan","minhyuk","minho","seowoo","seojun","seojin","seonwoo","seongmin","seongjun","seonghyun","soohyun","sooho","seungmin","seungwoo","seungwon","seungjun","seunghyun","sion","siwoo","siwon","siyoon","sihyun","sihoo","yeonwoo","yeseong","yejun","yechan","woobin","woojoo","woojin","yoojoon","yoochan","yoonseong","yoonwoo","yoonjae","yoonho","yool","eunseong","eunwoo","eunchan","eunho","yian","yijun","jaemin","jaewon","jaeyoon","jaehyun","jeongmin","jeongwoo","juwon","joon","joonseo","joonseong","joonsoo","joonyoung","joonwoo","joonhyuk","joonho","joonhui","jimin","jiseoung","jian","jiwan","jiwoo","jiwon","jiyool","jihan","jiho","jihwan","jihoo","jihoon","jinwoo","taemin","taeyang","taeyoon","taehyun","haram","hamin","hayool","hajun","hajin","hankyul","hyunseo","hyunsoo","hyunwoo","hyunjoon"};
	String femaleNamesKo[] = {"가연","가영","가은","가현","가희","경민","고은","규리","나연","나영","나은","나현","다빈","다솜","다영","다은","다정","다현","다혜","다희","단비","도연","도희","미영","민경","민서","민아","민영","민정","민주","민지","민희","사랑","서연","서영","서윤","서현","선영","선희","설아","세빈","세연","세영","세은","소라","소민","소연","소영","소율","소은","소정","소희","수민","수빈","수아","수연","수정","수지","수진","수현","슬기","승민","시아","시연","시영","시은","시현","아름","아영","아윤","아현","연서","연아","연주","연희","영주","예린","예림","예원","예은","예인","예주","예지","예진","유리","유림","유미","유빈","유정","유진","윤서","윤아","윤지","은경","은비","은서","은정","은주","은지","은채","이솔","이슬","정연","정은","정희","주영","주현","주희","지민","지수","지아","지영","지원","지윤","지율","지은","지현","지호","진영","진주","채아","채연","채은","채현","태연","하늘","하린","하영","하윤","하율","하은","현서","현아","현정","현지","혜림","혜인","혜진","효주","희원"};
	String femaleNamesEn[] = {"gaayeon","gaayoung","gaaeun","gaahyun","gaahui","gyeongmin","goeun","gyuri","naayeon","naayoung","naaeun","naahyun","daabin","daasom","daayoung","daaeun","daajeong","daahyun","daahye","daahui","daanbi","doyeon","dohui","miyoung","minkyung","minseo","mina","minyoung","minjeong","minjoo","minji","minhui","sarang","seoyeon","seoyoung","seoyoon","seohyun","seonyoung","seonhui","seolah","sebin","seyeon","seyoung","seeun","sora","somin","soyeon","soyoung","soyool","soeun","sojung","sohui","soomin","soobin","sooah","sooyeon","soojeong","sooji","soojin","soohyun","seulgi","seongmin","siah","siyeon","siyoung","sieun","sihyun","ahreum","ahyoung","ahyoon","ahhyun","yeonseo","yeonah","yeonjoo","yeonhui","yeonjoo","yerin","yerim","yewon","yeeun","yein","yejoo","yeji","yejin","yoori","yoorim","yoomi","yoobin","yoojeong","yoojin","yoonseo","yoonah","yoonji","eungyung","eunbi","eunseo","eunjeong","eunjoo","eunji","eunchae","yisol","yiseul","jeongyeon","jeongeun","jeonghui","jooyoung","joohyun","joohui","jimin","jisoo","jiah","jiyoung","jiwon","jiyoon","jiyool","jieun","jihyun","jiho","jinyoung","jinjoo","chaeah","chaeyeon","chaeeun","chaehyun","taeyeon","hahneul","hahrin","haayoung","haayoon","haayool","haaeun","hyunseo","hyunah","hyunjeong","hyunji","hyerim","hyein","hyejin","hyojoo","huiwon"};

	public CreateKoreanName() {
//		for (int i = 0; i < familyNameKo.length; i++) {
//			for (int j = 0; j < maleNamesKo.length; j++) {
//				if (maleNamesKo[j].equals(familyNameKo[i])) {
//					// System.out.println("\t\t\t\t\t\t\t"+maleNamesKo[j]+" "+familyNameKo[i]);
//				} else {
//					System.out.println(maleNamesKo[j] + " " + familyNameKo[i]);
//				}
//			}
//		}
//		System.out.println("==========================================");
//		for (int i = 0; i < familyNameKo.length; i++) {
//			for (int j = 0; j < femaleNamesKo.length; j++) {
//				if (femaleNamesKo[j].equals(familyNameKo[i])) {
//					// System.out.println("\t\t\t\t\t\t\t"+femaleNamesKo[j]+" "+familyNameKo[i]);
//				} else {
//					System.out.println(femaleNamesKo[j] + " " + familyNameKo[i]);
//				}
//			}
//		}
//		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println("남자이름 Ko");
		System.out.println("==========================================");
		for (int j = 0; j < maleNamesKo.length; j++) {
			for (int i = 0; i < familyNameKo.length; i++) {
				if (maleNamesKo[j].equals(familyNameKo[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+maleNamesKo[j]+" "+familyNameKo[i]);
				} else {
//					System.out.println(maleNamesKo[j] + " " + familyNameKo[i]);
//					System.out.println(familyNameKo[i] + " " + maleNamesKo[j]);
				}
			}
		}
		System.out.println("==========================================");
		System.out.println("남자이름 En");
		System.out.println("==========================================");
		for (int j = 0; j < maleNamesEn.length; j++) {
			for (int i = 0; i < familyNameEn.length; i++) {
				if (maleNamesEn[j].equals(familyNameEn[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+maleNamesKo[j]+" "+familyNameKo[i]);
				} else {
//					System.out.println(maleNamesEn[j] + " " + familyNameEn[i]);
//					System.out.println(familyNameEn[i] + " " + maleNamesEn[j]);
					System.out.println(StringUtils.capitalize(maleNamesEn[j]) + " " + StringUtils.capitalize(familyNameEn[i]));
//					System.out.println(StringUtils.capitalize(familyNameEn[i]) + " " + StringUtils.capitalize(maleNamesEn[j]));
				}
			}
		}		
		System.out.println("==========================================");
		System.out.println("여자이름 Ko");
		System.out.println("==========================================");
		for (int j = 0; j < femaleNamesKo.length; j++) {
			for (int i = 0; i < familyNameKo.length; i++) {
				if (femaleNamesKo[j].equals(familyNameKo[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+femaleNamesKo[j]+" "+familyNameKo[i]);
				} else {
//					System.out.println(femaleNamesKo[j] + " " + familyNameKo[i]);
//					System.out.println(familyNameKo[i] + " " + femaleNamesKo[j]);
				}
			}
		}
		System.out.println("==========================================");
		System.out.println("여자이름 En");
		System.out.println("==========================================");
		for (int j = 0; j < femaleNamesEn.length; j++) {
			for (int i = 0; i < familyNameEn.length; i++) {
				if (femaleNamesEn[j].equals(familyNameEn[i])) {
					// System.out.println("\t\t\t\t\t\t\t"+femaleNamesKo[j]+" "+familyNameKo[i]);
				} else {
//					System.out.println(femaleNamesEn[j] + " " + familyNameEn[i]);
//					System.out.println(familyNameEn[i] + " " + femaleNamesEn[j]);
//					System.out.println(StringUtils.capitalize(femaleNamesEn[j]) + " " + StringUtils.capitalize(familyNameEn[i]));
//					System.out.println(StringUtils.capitalize(familyNameEn[i]) + " " + StringUtils.capitalize(femaleNamesEn[j]));
				}
			}
		}		
	}

	public static void main(String[] args) {
		new CreateKoreanName();
	}

}
