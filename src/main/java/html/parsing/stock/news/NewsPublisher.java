package html.parsing.stock.news;

/**
 *
 * @author David
 */
enum NewsPublisher {
	// 상수("연결할 문자")
	wwwthebellcokr("www.thebell.co.kr"),
	wwwhidoccokr("www.hidoc.co.kr"),
	wwwdtcokr("www.dt.co.kr"),
	wwwkoreakr("www.korea.kr"),
	wwwmtcokr("www.mt.co.kr"),
	newsmtcokr("news.mt.co.kr"),
	moneysmtcokr("moneys.mt.co.kr"),
	wwwsisaincom("www.sisain.com"),
	wwwkmibcokr("www.kmib.co.kr"),
	newskmibcokr("news.kmib.co.kr"),
	wwwkhancokr("www.khan.co.kr"),
	wwwhanicokr("www.hani.co.kr"),
	joongangjoinscom("joongang.joins.com"),
	newsjoinscom("news.joins.com"),
	newsjtbcjoinscom("news.jtbc.joins.com"),
	bizchosuncom("biz.chosun.com"),
	wwwchosuncom("www.chosun.com"),
	newschosuncom("news.chosun.com"),
	wwwhankookilbocom("www.hankookilbo.com"),
	hankookilbocom("hankookilbo.com"),
	starhankookilbocom("star.hankookilbo.com"),
	wwwhuffingtonpostkr("www.huffingtonpost.kr"),
	mediadaumnet("media.daum.net"),
	vmediadaumnet("v.media.daum.net"),
	ventertainmediadaumnet("v.entertain.media.daum.net"),
	newsnavercom("news.naver.com"),
	wwwnavercom("www.naver.com"),
	financenavercom("finance.naver.com"),
	yonhapnews("www.yonhapnews.co.kr"),
	nocutnews("www.nocutnews.co.kr"),
	wwwhankyungcom("www.hankyung.com"),
	newshankyungcom("news.hankyung.com"),
	wwwytncokr("www.ytn.co.kr"),
	ytncokr("ytn.co.kr"),
	wwwynacokr("www.yna.co.kr"),
	wwwedailycokr("www.edaily.co.kr"),
	wwwwikitreecokr("www.wikitree.co.kr"),
	jtbcjoinscom("jtbc.joins.com"),
	wwwsedailycom("www.sedaily.com"),
	SnaptimeEdailyCoKr("snaptime.edaily.co.kr"),
	wwwetodaycokr("www.etoday.co.kr"),
	wwwnewsiscom("www.newsis.com"),
	wwwpressiancom("www.pressian.com"),
	newssbscokr("news.sbs.co.kr"),
	wwwfnnewscom("www.fnnews.com"),
	mfnnewscom("m.fnnews.com"),
	newsMbcCom("imnews.imbc.com"),
	wwwitoozacom("www.itooza.com"),
	mediatoday("www.mediatoday.co.kr"),
	asiatoday("www.asiatoday.co.kr"),
	wwwohmynewscom("www.ohmynews.com"),
	wwwmediauscokr("www.mediaus.co.kr"),
	wwwseoulcokr("www.seoul.co.kr"),
	wwwmunhwacom("www.munhwa.com"),
	wwwasiaecokr("www.asiae.co.kr"),
	newsStockAsiaeCoKr("stock.asiae.co.kr"),
	newsViewAsiaeCoKr("view.asiae.co.kr"),
	wwwmkcokr("www.mk.co.kr"),
	newsmkcokr("news.mk.co.kr"),
	vipmkcokr("vip.mk.co.kr"),
	estatemkcokr("estate.mk.co.kr"),
	newskbscokr("news.kbs.co.kr"),
	bizheraldcorpcom("biz.heraldcorp.com"),
	newsheraldcorpcom("news.heraldcorp.com"),
	wwwhanitvcom("www.hanitv.com"),
	newstomatocom("www.newstomato.com"),
	bizkhancokr("biz.khan.co.kr"),
	newskhancokr("news.khan.co.kr"),
	autodailycokr("www.autodaily.co.kr"),
	mnewspimcom("m.newspim.com"),
	newspimcom("newspim.com"),
	paxnetmonetacokr("paxnet.moneta.co.kr"),
	wwwpaxnetcokr("www.paxnet.co.kr"),
	financedaumnet("finance.daum.net"),
	news1kr("news1.kr"),
	wwwdongacom("www.donga.com"),
	newsdongacom("news.donga.com"),
	wwwsegyecom("www.segye.com"),
	wwwnewsencom("www.newsen.com"),
	wwwkinewsnet("www.kinews.net"),
	newswowtvcokr("news.wowtv.co.kr"),
	wwwdailiancokr("www.dailian.co.kr/")
	;

	final private String name;
	final private String[] publisherKeys = {
		"WwwTheBellCoKr",
		"WwwHidocCoKr",
		"WwwDtCoKr",
		"WwwKoreaKr",
		"NewsMoneyToday",
		"NewsMtCoKr",
		"MoneysMtCoKr",
		"NewsSisain",
		"NewsKmib",
		"NewsKmib",
		"KHSM",
		"NewsHankyoreh",
		"NewsJoinsCom",
		"NewsJoinsCom",
		"NewsJtbcJoinsCom",
		"NewsBizChosunCom",
		"NewsChosunCom",
		"NewsChosunCom",
		"NewsHankookilbo",
		"NewsHankookilbo",
		"NewsStarHankook",
		"NewsHuffingtonpost1",
		"NewsDaumNet",
		"NewsDaumNet",
		"NewsDaumNet",
		"NewsNaverCom",
		"NewsNaverCom",
		"NewsFinanceNaverCom",
		"NewsYonhap",
		"NewsNocutNews",
		"NewsHankyung",
		"NewsHankyung",
		"NewsYTN",
		"NewsYTN",
		"WwwYnaCoKr",
		"NewsEdaily",
		"NewsWikitree",
		"NewsJTBC",
		"NewsSedailyCom",
		"SnaptimeEdailyCoKr",
		"NewsEtodayCoKr",
		"NewsNewsisCom",
		"NewsPressian",
		"NewsSbsCoKr",
		"NewsFnnews",
		"NewsFnnews",
		"NewsMbcCom",
		"NewsItooza",
		"NewsMediatoday",
		"NewsAsiaToday",
		"NewsOhmynews",
		"NewsMediaus",
		"NewsSeoul",
		"NewsMunhwa",
		"WwwAsiaeCoKr",
		"NewsStockAsiaeCoKr",
		"NewViewAsiaeCoKr",
		"NewsMkCoKr",
		"NewsMkCoKr",
		"NewsVipMkCoKr",
		"EstateMkCoKr",
		"NewsKbsCoKr",
		"NewsBizHeraldcorpCom",
		"NewsHeraldcorpCom",
		"WwwHanitvCom",
		"NewsTomatoCom",
		"NewsBizKhanCoKr",
		"NewsKhanCoKr",
		"NewsAutodaily",
		"MNewsPimCom",
		"NewsPimCom",
		"PaxnetMonetaCoKr",
		"WwwPaxnetCoKr",
		"FinanceDaumNet",
		"News1Kr",
		"WwwDongaCom",
		"NewsDongaCom",
		"WwwSegyeCom",
		"NewsenCom",
		"WwwKinewsNet",
		"NewsWowtvCoKr",
		"WwwDailianCoKr"
	};

	private NewsPublisher(String name) { //enum에서 생성자 같은 역할
		this.name = name;
	}

	public String getName() { // 문자를 받아오는 함수
		return name;
	}

	public String getPublisherKey(int idx) {
		return publisherKeys[idx];
	}

	public static void main(String args[]) {
		int idx = 0;
		for (NewsPublisher np : NewsPublisher.values()) {
			String newsPublisherDomain = np.getName();
			idx = np.ordinal();
			System.out.println("idx:" + idx + " newsPublisherDomain:" + newsPublisherDomain);
			String newsCompany = np.getPublisherKey(idx);
			System.out.println("newsCompany:" + newsCompany);
			System.out.println("np.name:" + np.name());
		}
	}
}
