/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import html.parsing.stock.model.StockVO;

/**
 *
 * @author parsing-25
 */
public class StockExtractExceptWord {

	public static boolean dupCheck_bak(String stockName, String strNews) {
		if (stockName.equals("3S") && strNews.contains("3STEP") || stockName.equals("CS") && strNews.contains("CSA")
				|| stockName.equals("CS") && strNews.contains("크레디트스위스(CS)")
				|| stockName.equals("CS") && strNews.contains("CSSC")
				|| stockName.equals("CS") && strNews.contains("CSIS")
				|| stockName.equals("CS") && strNews.contains("CSR")
				|| stockName.equals("CS") && strNews.contains("CSV")
				|| stockName.equals("CS") && strNews.contains("GCSI")
				|| stockName.equals("DB") && strNews.contains("KDB")
				|| stockName.equals("DB") && strNews.contains("ADB")
				|| stockName.equals("DB") && strNews.contains("이데일리DB")
				|| stockName.equals("DB") && strNews.contains("이데일리 DB")
				|| stockName.equals("DB") && strNews.contains("머니투데이DB")
				|| stockName.equals("DB") && strNews.contains("머니투데이 DB")
				|| stockName.equals("DB") && strNews.contains("한경DB")
				|| stockName.equals("DB") && strNews.contains("한경 DB")
				|| stockName.equals("DB") && strNews.contains("경제DB")
				|| stockName.equals("DB") && strNews.contains("경제 DB")
				|| stockName.equals("DB") && strNews.contains("뉴시스DB")
				|| stockName.equals("DB") && strNews.contains("뉴시스 DB")
				|| stockName.equals("DB") && strNews.contains("머니투데이DB")
				|| stockName.equals("DB") && strNews.contains("머니투데이 DB")
				|| stockName.equals("EG") && strNews.contains("EGFR")
				|| stockName.equals("EG") && strNews.contains("EGR")
				|| stockName.equals("E1") && strNews.contains("FTSE100")
				|| stockName.equals("GS") && strNews.contains("KCGS")
				|| stockName.equals("GS") && strNews.contains("GSIA")
				|| stockName.equals("GS") && strNews.contains("ADOPIMGS")
				|| stockName.equals("GS") && strNews.contains("GS네오텍")
				|| stockName.equals("GS") && strNews.contains("GSOMIA")
				|| stockName.equals("GV") && strNews.contains("CGV")
				|| stockName.equals("KD") && strNews.contains("KDX")
				|| stockName.equals("KT") && strNews.contains("KTX")
				|| stockName.equals("KT") && strNews.contains("KTown")
				|| stockName.equals("LF") && strNews.contains("DLF")
				|| stockName.equals("LS") && strNews.contains("CLSA")
				|| stockName.equals("LS") && strNews.contains("ELS")
				|| stockName.equals("LS") && strNews.contains("GLS")
				|| stockName.equals("LS") && strNews.contains("DLS")
				|| stockName.equals("NEW") && strNews.contains("NEWSIS")
				|| stockName.equals("NEW") && strNews.contains("NEWYORK")
				|| stockName.equals("NEW") && strNews.contains("NEW YORK")
				|| stockName.equals("SG") && strNews.contains("MSG")
				|| stockName.equals("SG") && strNews.contains("SGI")
				|| stockName.equals("SG") && strNews.contains("SSG")
				|| stockName.equals("SG") && strNews.contains("ESG")
				|| stockName.equals("SK") && strNews.contains("ASK")
				|| stockName.equals("SK") && strNews.contains("SKY")
				|| stockName.equals("SK") && strNews.contains("MMSK")
				|| stockName.equals("SK") && strNews.contains("GSK")
				|| stockName.equals("고영") && strNews.contains("최고영도자")
				|| stockName.equals("고영") && strNews.contains("광고영역")
				|| stockName.equals("국보") && strNews.contains("중국보")
				|| stockName.equals("국보") && strNews.contains("한국보")
				|| stockName.equals("국보") && strNews.contains("미국보")
				|| stockName.equals("국보") && strNews.contains("영국보")
				|| stockName.equals("국동") && strNews.contains("전국동")
				|| stockName.equals("광림") && strNews.contains("김광림")
				|| stockName.equals("나무가") && strNews.contains("느릅나무가")
				|| stockName.equals("나무가") && strNews.contains("소나무가")
				|| stockName.equals("남성") && strNews.contains("남성이")
				|| stockName.equals("남성") && strNews.contains("남성들")
				|| stockName.equals("남성") && strNews.contains("남성과")
				|| stockName.equals("남성") && strNews.contains("남성을")
				|| stockName.equals("남성") && strNews.contains("남성 평균")
				|| stockName.equals("남성") && strNews.contains("중년 남성")
				|| stockName.equals("남성") && strNews.contains("한국 남성")
				|| stockName.equals("덕성") && strNews.contains("도덕성")
				|| stockName.equals("동원") && strNews.contains("운동원")
				|| stockName.equals("동원") && strNews.contains("동원됐다")
				|| stockName.equals("동원") && strNews.contains("동원해")
				|| stockName.equals("동원") && strNews.contains("동원했")
				|| stockName.equals("동원") && strNews.contains("동원될")
				|| stockName.equals("동원") && strNews.contains("총동원")
				|| stockName.equals("동양") && strNews.contains("김동양")
				|| stockName.equals("동양") && strNews.contains("행동양식")
				|| stockName.equals("동양") && strNews.contains("동양철학")
				|| stockName.equals("동양") && strNews.contains("동양 쪽")
				|| stockName.equals("동방") && strNews.contains("신동방")
				|| stockName.equals("동방") && strNews.contains("공동방")
				|| stockName.equals("동서") && strNews.contains("동서냉전")
				|| stockName.equals("동서") && strNews.contains("동서울")
				|| stockName.equals("두산") && strNews.contains("백두산")
				|| stockName.equals("대상") && strNews.contains("대상에")
				|| stockName.equals("대상") && strNews.contains("조사대상")
				|| stockName.equals("대상") && strNews.contains("지원 대상")
				|| stockName.equals("대상") && strNews.contains("금지 대상")
				|| stockName.equals("대상") && strNews.contains("대상으로")
				|| stockName.equals("대상") && strNews.contains("대상 사건")
				|| stockName.equals("대상") && strNews.contains("추징 대상")
				|| stockName.equals("대상") && strNews.contains("투자대상")
				|| stockName.equals("대상") && strNews.contains("매각 대상")
				|| stockName.equals("대상") && strNews.contains("환원 대상")
				|| stockName.equals("대상") && strNews.contains("비교 대상")
				|| stockName.equals("대상") && strNews.contains("상속대상")
				|| stockName.equals("대상") && strNews.contains("상속 대상")
				|| stockName.equals("대상") && strNews.contains("출고대상")
				|| stockName.equals("대상") && strNews.contains("출고 대상")
				|| stockName.equals("대상") && strNews.contains("시대상")
				|| stockName.equals("대상") && strNews.contains("대상자")
				|| stockName.equals("대상") && strNews.contains("처벌 대상")
				|| stockName.equals("대상") && strNews.contains("대상인데")
				|| stockName.equals("대상") && strNews.contains("연결대상")
				|| stockName.equals("대상") && strNews.contains("어떤 대상")
				|| stockName.equals("대상") && strNews.contains("배제 대상")
				|| stockName.equals("대상") && strNews.contains("대상차량")
				|| stockName.equals("대상") && strNews.contains("대상 차량")
				|| stockName.equals("대상") && strNews.contains("리콜대상")
				|| stockName.equals("대상") && strNews.contains("대상 가맹점")
				|| stockName.equals("대상") && strNews.contains("대상은")
				|| stockName.equals("대상") && strNews.contains("검토 대상")
				|| stockName.equals("대상") && strNews.contains("대상지역")
				|| stockName.equals("대상") && strNews.contains("교체대상")
				|| stockName.equals("대상") && strNews.contains("과세대상")
				|| stockName.equals("대상") && strNews.contains("대상화")
				|| stockName.equals("대유") && strNews.contains("김대유")
				|| stockName.equals("대유") && strNews.contains("대유행")
				|| stockName.equals("대원") && strNews.contains("부대원")
				|| stockName.equals("대원") && strNews.contains("구급대원")
				|| stockName.equals("대교") && strNews.contains("세대교체")
				|| stockName.equals("디아이") && strNews.contains("삼성에스디아이")
				|| stockName.equals("디오") && strNews.contains("디오르")
				|| stockName.equals("디오") && strNews.contains("스튜디오")
				|| stockName.equals("디오") && strNews.contains("비디오")
				|| stockName.equals("디오") && strNews.contains("라디오")
				|| stockName.equals("디오") && strNews.contains("오디오")
				|| stockName.equals("디오") && strNews.contains("디오스")
				|| stockName.equals("딜리") && strNews.contains("딜리버리")
				|| stockName.equals("레이") && strNews.contains("디스플레이")
				|| stockName.equals("레이") && strNews.contains("디플레이션")
				|| stockName.equals("레이") && strNews.contains("레이더")
				|| stockName.equals("레이") && strNews.contains("레이어")
				|| stockName.equals("레이") && strNews.contains("레이저")
				|| stockName.equals("레이") && strNews.contains("레이스")
				|| stockName.equals("레이") && strNews.contains("레이크")
				|| stockName.equals("레이") && strNews.contains("마이그레이션")
				|| stockName.equals("레이") && strNews.contains("말레이시아")
				|| stockName.equals("레이") && strNews.contains("바클레이")
				|| stockName.equals("레이") && strNews.contains("브레이크")
				|| stockName.equals("레이") && strNews.contains("블레이드")
				|| stockName.equals("레이") && strNews.contains("서킷브레이커")
				|| stockName.equals("레이") && strNews.contains("스프레이")
				|| stockName.equals("레이") && strNews.contains("솔레이마니")
				|| stockName.equals("레이") && strNews.contains("슬레이터")
				|| stockName.equals("레이") && strNews.contains("시뮬레이션")
				|| stockName.equals("레이") && strNews.contains("아랍에미레이트")
				|| stockName.equals("레이") && strNews.contains("엑스레이")
				|| stockName.equals("레이") && strNews.contains("인플레이션")
				|| stockName.equals("레이") && strNews.contains("업그레이드")
				|| stockName.equals("레이") && strNews.contains("일러스트레이터")
				|| stockName.equals("레이") && strNews.contains("캐치프레이즈")
				|| stockName.equals("레이") && strNews.contains("코퍼레이션")
				|| stockName.equals("레이") && strNews.contains("콜라보레이션")
				|| stockName.equals("레이") && strNews.contains("컬래버레이션")
				|| stockName.equals("레이") && strNews.contains("클레이")
				|| stockName.equals("레이") && strNews.contains("트레이더스")
				|| stockName.equals("레이") && strNews.contains("트레이딩")
				|| stockName.equals("레이") && strNews.contains("트레이드")
				|| stockName.equals("레이") && strNews.contains("플레이")
				|| stockName.equals("레이") && strNews.contains("플레이트")
				|| stockName.equals("레이") && strNews.contains("플레이스")
				|| stockName.equals("레이") && strNews.contains("프라임레이트")
				|| stockName.equals("레이") && strNews.contains("프레이")
				|| stockName.equals("리드") && strNews.contains("그리드")
				|| stockName.equals("리드") && strNews.contains("솔리드")
				|| stockName.equals("리드") && strNews.contains("서킷브레이커")
				|| stockName.equals("리드") && strNews.contains("하이브리드")
				|| stockName.equals("리드") && strNews.contains("솔리드")
				|| stockName.equals("리드") && strNews.contains("칼리드")
				|| stockName.equals("만도") && strNews.contains("왕만도")
				|| stockName.equals("만도") && strNews.contains("데만도")
				|| stockName.equals("만도") && strNews.contains("비만도")
				|| stockName.equals("만도") && strNews.contains("불만도")
				|| stockName.equals("만도") && strNews.contains("잔량만도")
				|| stockName.equals("머큐리") && strNews.contains("머큐리오")
				|| stockName.equals("모다") && strNews.contains("규모다")
				|| stockName.equals("무학") && strNews.contains("무학으로")
				|| stockName.equals("무학") && strNews.contains("재무학")
				|| stockName.equals("백산") && strNews.contains("혼비 백산")
				|| stockName.equals("백산") && strNews.contains("혼비백산")
				|| stockName.equals("배럴") && strNews.contains("억배럴")
				|| stockName.equals("배럴") && strNews.contains("배럴당")
				|| stockName.equals("상보") && strNews.contains("예상보다")
				|| stockName.equals("상보") && strNews.contains("정상보")
				|| stockName.equals("상보") && strNews.contains("영상보기")
				|| stockName.equals("서한") && strNews.contains("연례 서한")
				|| stockName.equals("서연") && strNews.contains("바이오센서연구소")
				|| stockName.equals("서한") && strNews.contains("공개 서한")
				|| stockName.equals("서한") && strNews.contains("결정 서한")
				|| stockName.equals("서한") && strNews.contains("서한기")
				|| stockName.equals("서한") && strNews.contains("서한만")
				|| stockName.equals("서한") && strNews.contains("공개서한")
				|| stockName.equals("서한") && strNews.contains("결정서한")
				|| stockName.equals("서원") && strNews.contains("최서원")
				|| stockName.equals("선진") && strNews.contains("선진화")
				|| stockName.equals("선진") && strNews.contains("선진국")
				|| stockName.equals("성안") && strNews.contains("성안 곳곳")
				|| stockName.equals("세방") && strNews.contains("월세방")
				|| stockName.equals("세하") && strNews.contains("자세하")
				|| stockName.equals("세하") && strNews.contains("우세하")
				|| stockName.equals("세하") && strNews.contains("상세하")
				|| stockName.equals("세하") && strNews.contains("맹세하")
				|| stockName.equals("세하") && strNews.contains("과세하")
				|| stockName.equals("세하") && strNews.contains("가세하")
				|| stockName.equals("세하") && strNews.contains("별세하")
				|| stockName.equals("세하") && strNews.contains("섬세하")
				|| stockName.equals("신흥") && strNews.contains("신흥국")
				|| stockName.equals("신흥") && strNews.contains("신흥부자")
				|| stockName.equals("신흥") && strNews.contains("신흥 부자")
				|| stockName.equals("수성") && strNews.contains("특수성")
				|| stockName.equals("수성") && strNews.contains("자수성가")
				|| stockName.equals("수성") && strNews.contains("수성갑")
				|| stockName.equals("수성") && strNews.contains("보수성향")
				|| stockName.equals("수성") && strNews.contains("감수성")
				|| stockName.equals("신원") && strNews.contains("신원철")
				|| stockName.equals("신원") && strNews.contains("신원이")
				|| stockName.equals("신원") && strNews.contains("신원 등을")
				|| stockName.equals("신원") && strNews.contains("신원증명")
				|| stockName.equals("신원") && strNews.contains("신원 증명")
				|| stockName.equals("신원") && strNews.contains("신원정보")
				|| stockName.equals("신원") && strNews.contains("신원 정보")
				|| stockName.equals("신한") && strNews.contains("대신한")
				|| stockName.equals("신한") && strNews.contains("변신한")
				|| stockName.equals("신한") && strNews.contains("확신한")
				|| stockName.equals("신한") && strNews.contains("경신한")
				|| stockName.equals("신한") && strNews.contains("참신한")
				|| stockName.equals("신한") && strNews.contains("출신한테")
				|| stockName.equals("신한") && strNews.contains("임신한")
				|| stockName.equals("신한") && strNews.contains("신한카드")
				|| stockName.equals("신한") && strNews.contains("혁신한")
				|| stockName.equals("아스트") && strNews.contains("아스트라제네카")
				|| stockName.equals("아이엠") && strNews.contains("아이엠에프")
				|| stockName.equals("야스") && strNews.contains("야스쿠니")
				|| stockName.equals("에이치케이") && strNews.contains("엔에이치케이")
				|| stockName.equals("엔에스") && strNews.contains("엘지씨엔에스")
				|| stockName.equals("오공") && strNews.contains("오공장")
				|| stockName.equals("오텍") && strNews.contains("GS네오텍")
				|| stockName.equals("우진") && strNews.contains("피우진")
				|| stockName.equals("에스엠") && strNews.contains("한국에스엠티")
				|| stockName.equals("아티스") && strNews.contains("아티스트")
				|| stockName.equals("아스타") && strNews.contains("아스타잔틴")
				|| stockName.equals("아스타") && strNews.contains("코리아스타트")
				|| stockName.equals("이디") && strNews.contains("아이디어")
				|| stockName.equals("이디") && strNews.contains("아이디")
				|| stockName.equals("이디") && strNews.contains("레이디")
				|| stockName.equals("이디") && strNews.contains("이디야")
				|| stockName.equals("이월드") && strNews.contains("싸이월드")
				|| stockName.equals("이마트") && strNews.contains("하이마트")
				|| stockName.equals("우진") && strNews.contains("정우진")
				|| stockName.equals("전방") && strNews.contains("전방위")
				|| stockName.equals("전방") && strNews.contains("전방추돌")
				|| stockName.equals("전방") && strNews.contains("전방 추돌")
				|| stockName.equals("진도") && strNews.contains("청사진도")
				|| stockName.equals("진도") && strNews.contains("취재진도")
				|| stockName.equals("진도") && strNews.contains("티슈진도")
				|| stockName.equals("진도") && strNews.contains("이사진도")
				|| stockName.equals("진도") && strNews.contains("경영진도")
				|| stockName.equals("진도") && strNews.contains("검진도")
				|| stockName.equals("코센") && strNews.contains("포스코센터")
				|| stockName.equals("카스") && strNews.contains("카스피")
				|| stockName.equals("카스") && strNews.contains("카스먼")
				|| stockName.equals("카스") && strNews.contains("박카스")
				|| stockName.equals("코오롱") && strNews.contains("코오롱 성산")
				|| stockName.equals("테스") && strNews.contains("테스트")
				|| stockName.equals("테스") && strNews.contains("테스크")
				|| stockName.equals("테스") && strNews.contains("필라테스")
				|| stockName.equals("태양") && strNews.contains("태양석재")
				|| stockName.equals("태양") && strNews.contains("태양광")
				|| stockName.equals("태양") && strNews.contains("태양절")
				|| stockName.equals("한창") && strNews.contains("한창인")
				|| stockName.equals("한창") && strNews.contains("한창일")
				|| stockName.equals("한창") && strNews.contains("한창이다")
				|| stockName.equals("한창") && strNews.contains("한창수")
				|| stockName.equals("한진") && strNews.contains("한진공인")
				|| stockName.equals("한진") && strNews.contains("대한진단")
				|| stockName.equals("혜인") && strNews.contains("특혜인")
				|| stockName.equals("효성") && strNews.contains("실효성")
				|| stockName.equals("효성") && strNews.contains("유효성")
				|| stockName.equals("효성") && strNews.contains("이효성")
				|| stockName.equals("화신") && strNews.contains("전화신청")
				|| stockName.equals("화신") && strNews.contains("배반의 화신")
				|| stockName.equals("흥국") && strNews.contains("신흥국")
				|| stockName.equals("힘스") && strNews.contains("현대힘스")) {
			return true;
		}
		return false;
	}

	public static boolean dupCheck(List<String> exceptWordList, String stockName, String strNews) {
		boolean isExceptWord = false;
		String strLineArray[];
		for (String strLine : exceptWordList) {
			strLineArray = strLine.split(":");
			String compareSName = strLineArray[0];
			String exceptWord = strLineArray[1];
//			System.out.println("compareSName:"+compareSName+" exceptWord:"+exceptWord);
			if (stockName.equals(compareSName) && strNews.contains(exceptWord)) {
				isExceptWord = true;
				System.out.println("기사에 예외어가 있는가?" + (stockName.equals(compareSName) && strNews.contains(exceptWord)));
				System.out.println("기사에 예외어가 있는가?" + "(" + stockName + ".equals(" + compareSName
						+ ") && strNews.contains(" + exceptWord + "))");
				break;
			}
		}
		return isExceptWord;		
	}
	
	public static List<String> exceptWordList() {
		List<String> exceptWordList = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// jar를 실행하였을 경우는 jar와 동일 경로
			// ide에서 실행하였을 경우에는 프로젝트 경로
			// 프로젝트 경로에 있는 파일들은 jar파일에 묶이지 않는다.
			File f = new File("./StockExtractExceptWord.txt");
//			System.out.println("f.exists1:"+f.exists());
			if (f.exists()) {
				fr = new FileReader(f);
				br = new BufferedReader(fr);
			} else {
				// classes root 경로
				f = new File("/StockExtractExceptWord.txt");
//				System.out.println("f.exists2:"+f.exists());
				fr = new FileReader(f);
				br = new BufferedReader(fr);
			}

			String strLine = "";
			while ((strLine = br.readLine()) != null) {
				exceptWordList.add(strLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return exceptWordList;
	}

	public static void main(String args[]) throws IOException {
		String url = "https://www.etoday.co.kr/news/view/1921896";
		Document doc = Jsoup.connect(url).get();
		String html = doc.select(".view_contents").html();
		System.out.println("html:" + html);
		List<String> exceptWordList = exceptWordList();

		List<StockVO> stockList = StockUtil.readStockCodeNameList("코스닥");
		for (StockVO svo : stockList) {
			dupCheck(exceptWordList, svo.getStockName(), html);
		}
	}

}
