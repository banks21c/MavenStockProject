package html.parsing.stock.news;

/**
 *
 * @author David
 */
public enum NewsPublisher {
	// 상수("연결할 문자")
	NewsEinfomaxCoKr("news.einfomax.co.kr"),
	WwwTheBellCoKr("www.thebell.co.kr"),
	WwwHidocCoKr("www.hidoc.co.kr"),
	WwwDtCoKr("www.dt.co.kr"),
	WwwKoreaKr("www.korea.kr"),
	WwwMtCoKr("www.mt.co.kr"),
	NewsMtCoKr("news.mt.co.kr"),
	MoneysMtCoKr("moneys.mt.co.kr"),
	WwwSisainCoKr("www.sisain.com"),
	NewsKmibCoKr("news.kmib.co.kr"),
	NewsJoinsCom("news.joins.com"),
	NewsJtbcJoinsCom("news.jtbc.joins.com"),
	BizChosunCom("biz.chosun.com"),
	WwwChosunCom("www.chosun.com"),
	NewsChosunCom("news.chosun.com"),
	HealthChosunCom("health.chosun.com"),
	MonthlyChosunCom("monthly.chosun.com"),
	WwwHankookilboCom("www.hankookilbo.com"),
	StarHankookilboCom("star.hankookilbo.com"),
	WwwHuffingtonpostKr("www.huffingtonpost.kr"),
	NewsDaumNet("news.daum.net"),
	NewsNaverCom("news.naver.com"),
	FinanceNaverCom("finance.naver.com"),
	WwwYonhapnewsCoKr("www.yonhapnews.co.kr"),
	WwwNocutNewsCoKr("www.nocutnews.co.kr"),
	WwwHankyungCom("www.hankyung.com"),
	NewsHankyungCom("news.hankyung.com"),
	MarketinsightHankyungCom("marketinsight.hankyung.com"),
	WwwYtnCoKr("www.ytn.co.kr"),
	WwwYnaCoKr("www.yna.co.kr"),
	WwwWikitreeCoKr("www.wikitree.co.kr"),
	WwwEdailyCoKr("www.edaily.co.kr"),
	WwwSedailyCom("www.sedaily.com"),
	MSedailyCom("m.sedaily.com"),
	WwwSentvCoKr("www.sentv.co.kr"),
	SnaptimeEdailyCoKr("snaptime.edaily.co.kr"),
	WwwEtodayCoKr("www.etoday.co.kr"),
	WwwNewsisCom("www.newsis.com"),
	NewsisCom("newsis.com"),
	WwwPressianCom("www.pressian.com"),
	NewsSbsCoKr("news.sbs.co.kr"),
	WwwFnnewsCom("www.fnnews.com"),
	MFnnewsCom("m.fnnews.com"),
	ImnewsImbcCom("imnews.imbc.com"),
	WwwItoozaCom("www.itooza.com"),
	WwwMediatodayCoKr("www.mediatoday.co.kr"),
	WwwAsiatodayCoKr("www.asiatoday.co.kr"),
	WwwOhmynewsCoKr("www.ohmynews.com"),
	WwwMediausCoKr("www.mediaus.co.kr"),
	WwwSeoulCoKr("www.seoul.co.kr"),
	WwwMunhwaCom("www.munhwa.com"),
	WwwAsiaeCoKr("www.asiae.co.kr"),
	StockAsiaeCoKr("stock.asiae.co.kr"),
	ViewAsiaeCoKr("view.asiae.co.kr"),
	WwwMkCoKr("www.mk.co.kr"),
	WwwMkCoKrPremium("www.mk.co.kr/premium"),
	NewsMkCoKr("news.mk.co.kr"),
	VipMkCoKr("vip.mk.co.kr"),
	EstateMkCoKr("estate.mk.co.kr"),
	NewsKbsCoKr("news.kbs.co.kr"),
	BizHeraldcorpCom("biz.heraldcorp.com"),
	NewsHeraldcorpCom("news.heraldcorp.com"),
	WwwHaniCoKr("www.hani.co.kr"),
	WwwHanitvCom("www.hanitv.com"),
	NewsTomatoCom("www.newstomato.com"),
	WwwKhanCoKr("www.khan.co.kr"),
	BizKhanCoKr("biz.khan.co.kr"),
	NewsKhanCoKr("news.khan.co.kr"),
	Ttalgi21KhanKr("ttalgi21.khan.kr"),
	NewsAutodaily("www.autodaily.co.kr"),
	MNewsPimCom("m.newspim.com"),
	WwwNewspimCom("www.newspim.com"),
	PaxnetMonetaCoKr("paxnet.moneta.co.kr"),
	WwwPaxnetCoKr("www.paxnet.co.kr"),
	FinanceDaumNet("finance.daum.net"),
	News1Kr("news1.kr"),
	WwwDongaCom("www.donga.com"),
	NewsDongaCom("news.donga.com"),
	WwwSegyeCom("www.segye.com"),
	NewsenCom("www.newsen.com"),
	WwwKinewsNet("www.kinews.net"),
	NewsWowtvCoKr("news.wowtv.co.kr"),
	WwwWowtvCoKr("www.wowtv.co.kr"),
	HngYnaCoKr("hng.yna.co.kr"),
	WwwSiksinhotCom("www.siksinhot.com"),
	WwwDailianCoKr("www.dailian.co.kr"),
	BizNewdailyCoKr("biz.newdaily.co.kr"),
	NewsNewswayCoKr("news.newsway.co.kr"),
	KrInvestingCom("kr.investing.com"),
	WwwPharmnewsCom("www.pharmnews.com"),
	WwwBusanCom("www.busan.com"),
	HugsFnnewsCom("hugs.fnnews.com"),
	WwwMbnCoKr("www.mbn.co.kr"),
	WwwIlyoseoulCoKr("www.ilyoseoul.co.kr");

	final private String name;

	private NewsPublisher(String name) { //enum에서 생성자 같은 역할
		this.name = name;
	}

	public String getName() { // 문자를 받아오는 함수
		return name;
	}

	public static void main(String args[]) {
		for (NewsPublisher np : NewsPublisher.values()) {
			String newsPublisherDomain = np.getName();
			int idx = np.ordinal();
			System.out.println("idx:" + idx + " newsPublisherDomain:" + newsPublisherDomain);
			String newsCompany = np.toString();
			System.out.println("newsCompany:" + newsCompany);
			System.out.println("np.name:" + np.name());
		}
	}
}
