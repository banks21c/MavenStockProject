package html.parsing.stock;

import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import html.parsing.stock.model.MajorStockHolderVO;

public class StockVO {

	private String date;

	private String market;
	private String companyNameHan;
	private String companyNameEn;
	private String wikiCompanyUrl;
	/**거래소 구분*/
	private String stockExchange;
	private String stockGubun;
	private int lineUp;
	private String stockCode;
	private String stockName;
	private String abbreviation;
	private String industry;
	private int stockNameLength;
	private String sign;
	private String specialLetter;
	private String curPrice;
	private String startPrice;
	private String beforePrice;
	private String lowPrice;
	private String highPrice;
	private String varyPrice;
	private String maxPrice;
	private String minPrice;
	private String varyRatio;
	private String tradingVolume;
	private String tradingAmount;

	private String foreignTradingVolume;
	private String organTradingVolume;
	private String foreignOrganTradingVolume;

	private String foreignTradingAmount;
	private String organTradingAmount;
	private String foreignOrganTradingAmount;

	private String foreignHaveVolume;
	private String foreignHaveAmount;
	private String foreignHaveRatio;

	private int iCurPrice;
	private int iStartPrice;
	private int iBeforePrice;
	private int iLowPrice;
	private int iHighPrice;
	private int iVaryPrice;
	private int iMaxPrice;
	private int iMinPrice;

	//미국 통화는 달러화로 주식이 소숫점으로 되어 있어서 float 추가
	private int fCurPrice;
	private int fStartPrice;
	private int fBeforePrice;
	private int fLowPrice;
	private int fHighPrice;
	private int fVaryPrice;
	private int fMaxPrice;
	private int fMinPrice;

	private float fVaryRatio;

	private int iTradingVolume;
	private int iForeignTradingVolume;
	private int iOrganTradingVolume;
	private int iForeignOrganTradingVolume;

	private long lTradingVolume;
	private long lForeignTradingAmount;
	private long lTradingAmount;
	private long lOrganTradingAmount;
	private long lForeignOrganTradingAmount;

	private long lForeignHaveVolume;
	private long lForeignHaveAmount;
	private float fForeignHaveRatio;
	private String homePage;
	private String mainPhone;
	private String stockPhone;

	private String retainVolume;
	private float lRetainVolume;
	private String retainRatio;
	private float fRetainRatio;
	private String retainAmount;
	private long lRetainAmount;

	private int organStraitBuyCount;
	private int foreignStraitBuyCount;

	private int organStraitSellCount;
	private int foreignStraitSellCount;

	private String headquartersAddress;
	private String foundDay;
	private String listedDay;
	private String ceo;
	private String affiliation;
	private String numberOfEmployee;
	private String totalNumberOfStock;
	private int iTotalNumberOfStock;
	private String auditor;
	private String transferShareholdersName;
	private String mainBank;

	private int maxPriceCount;
	private int minPriceCount;
	private String majorStockHolders;

	private String stockTotalAmount;
	private long lStockTotalAmount;
	private String stockTotalVolume;
	private int iStockTotalVolume;
	private String weeks52MinPrice;
	private String weeks52MaxPrice;
	private int iWeeks52MinPrice;
	private int iWeeks52MaxPrice;
	private boolean weeks52NewHighPrice = false;
	private boolean weeks52NewLowPrice = false;

	private double weeks52NewHighPriceVsCurPriceDownRatio;
	private double weeks52NewLowPriceVsCurPriceUpRatio;
	private double specificDayEndPriceVsCurPriceUpDownRatio;
	private double specificeDayPriceVsCurPriceUpDownRatio;

	private Vector<MajorStockHolderVO> majorStockHolderList;
	// 투자의견
	private String strScore;
	private float fScore;
	private String strTargetPrice;
	private int iTargetPrice;
	private String strEps;
	private int iEps;
	private String strPer;
	private float fPer;
	private String strEstimateCount;
	private int iEstimateCount;
	private String strGapPrice;
	private float iGapPrice;
	private float fGapRatio;

	private float upDownRatio;

	private String resultCode;
	private String resultMessage;
	private String resultDetailMessage;

	private String eps;
	private String bps;
	private String roe;

	private String per;
	private String bizTypePer;
	private String pbr;

	private String dividends;
	private String dividendRate;
	private String ev;

	private int iBps;
	private float fRoe;
	private float fBizTypePer;
	private float fPbr;
	private float fDividendRate;

	private float minCurRatio;
	private float maxCurRatio;
	private float minMaxRatio;
	private float startCurRatio;

	private String specificDay;
	private String specificDayEndPrice;
	private int iSpecificDayEndPrice;

	private String specificDay1;
	private String specificDayEndPrice1;
	private int iSpecificDayEndPrice1;

	private String specificDay2;
	private String specificDayEndPrice2;
	private int iSpecificDayEndPrice2;

	private String specificDay3;
	private String specificDayEndPrice3;
	private int iSpecificDayEndPrice3;

	private String specificDay4;
	private String specificDayEndPrice4;
	private int iSpecificDayEndPrice4;

	private String specificDay5;
	private String specificDayEndPrice5;
	private int iSpecificDayEndPrice5;

	private String yearStartPrice;

	long lRetainVolumeTotal;
	long lRetainAmountTotal;
	float fRetainRatioTotal;

	String url;
	String symbolExchangeTicker;
	String instrumentName;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getCompanyNameHan() {
		return companyNameHan;
	}

	public void setCompanyNameHan(String companyNameHan) {
		this.companyNameHan = companyNameHan;
	}

	public String getCompanyNameEn() {
		return companyNameEn;
	}

	public void setCompanyNameEn(String companyNameEn) {
		this.companyNameEn = companyNameEn;
	}

	public String getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(String stockExchange) {
		this.stockExchange = stockExchange;
	}

	public String getStockGubun() {
		return stockGubun;
	}

	public void setStockGubun(String stockGubun) {
		this.stockGubun = stockGubun;
	}

	public int getLineUp() {
		return lineUp;
	}

	public void setLineUp(int lineUp) {
		this.lineUp = lineUp;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public void setStockNameLength(int length) {
		stockNameLength = length;
	}

	public int getStockNameLength() {
		return stockNameLength;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSpecialLetter() {
		return specialLetter;
	}

	public void setSpecialLetter(String specialLetter) {
		this.specialLetter = specialLetter;
	}

	public String getCurPrice() {
		return curPrice;
	}

	public void setCurPrice(String curPrice) {
		this.curPrice = curPrice;
	}

	public String getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(String startPrice) {
		this.startPrice = startPrice;
	}

	public String getBeforePrice() {
		return beforePrice;
	}

	public void setBeforePrice(String beforePrice) {
		this.beforePrice = beforePrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getVaryPrice() {
		return varyPrice;
	}

	public void setVaryPrice(String varyPrice) {
		this.varyPrice = varyPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getVaryRatio() {
		return varyRatio;
	}

	public void setVaryRatio(String varyRatio) {
		this.varyRatio = varyRatio;
	}

	public String getTradingVolume() {
		return tradingVolume;
	}

	public void setTradingVolume(String tradingVolume) {
		this.tradingVolume = tradingVolume;
	}

	public String getTradingAmount() {
		return tradingAmount;
	}

	public void setTradingAmount(String tradingAmount) {
		this.tradingAmount = tradingAmount;
	}

	public String getForeignTradingVolume() {
		return foreignTradingVolume;
	}

	public void setForeignTradingVolume(String foreignTradingVolume) {
		this.foreignTradingVolume = foreignTradingVolume;
	}

	public String getOrganTradingVolume() {
		return organTradingVolume;
	}

	public void setOrganTradingVolume(String organTradingVolume) {
		this.organTradingVolume = organTradingVolume;
	}

	public String getForeignOrganTradingVolume() {
		return foreignOrganTradingVolume;
	}

	public void setForeignOrganTradingVolume(String foreignOrganTradingVolume) {
		this.foreignOrganTradingVolume = foreignOrganTradingVolume;
	}

	public String getForeignTradingAmount() {
		return foreignTradingAmount;
	}

	public void setForeignTradingAmount(String foreignTradingAmount) {
		this.foreignTradingAmount = foreignTradingAmount;
	}

	public String getOrganTradingAmount() {
		return organTradingAmount;
	}

	public void setOrganTradingAmount(String organTradingAmount) {
		this.organTradingAmount = organTradingAmount;
	}

	public String getForeignOrganTradingAmount() {
		return foreignOrganTradingAmount;
	}

	public void setForeignOrganTradingAmount(String foreignOrganTradingAmount) {
		this.foreignOrganTradingAmount = foreignOrganTradingAmount;
	}

	public String getForeignHaveVolume() {
		return foreignHaveVolume;
	}

	public void setForeignHaveVolume(String foreignHaveVolume) {
		this.foreignHaveVolume = foreignHaveVolume;
	}

	public String getForeignHaveAmount() {
		return foreignHaveAmount;
	}

	public void setForeignHaveAmount(String foreignHaveAmount) {
		this.foreignHaveAmount = foreignHaveAmount;
	}

	public String getForeignHaveRatio() {
		return foreignHaveRatio;
	}

	public void setForeignHaveRatio(String foreignHaveRatio) {
		this.foreignHaveRatio = foreignHaveRatio;
	}

	public int getiCurPrice() {
		return iCurPrice;
	}

	public void setiCurPrice(int iCurPrice) {
		this.iCurPrice = iCurPrice;
	}

	public int getiStartPrice() {
		return iStartPrice;
	}

	public void setiStartPrice(int iStartPrice) {
		this.iStartPrice = iStartPrice;
	}

	public int getiBeforePrice() {
		return iBeforePrice;
	}

	public void setiBeforePrice(int iBeforePrice) {
		this.iBeforePrice = iBeforePrice;
	}

	public int getiLowPrice() {
		return iLowPrice;
	}

	public void setiLowPrice(int iLowPrice) {
		this.iLowPrice = iLowPrice;
	}

	public int getiHighPrice() {
		return iHighPrice;
	}

	public void setiHighPrice(int iHighPrice) {
		this.iHighPrice = iHighPrice;
	}

	public int getiVaryPrice() {
		return iVaryPrice;
	}

	public void setiVaryPrice(int iVaryPrice) {
		this.iVaryPrice = iVaryPrice;
	}

	public int getiMaxPrice() {
		return iMaxPrice;
	}

	public void setiMaxPrice(int iMaxPrice) {
		this.iMaxPrice = iMaxPrice;
	}

	public int getiMinPrice() {
		return iMinPrice;
	}

	public void setiMinPrice(int iMinPrice) {
		this.iMinPrice = iMinPrice;
	}

	public float getfVaryRatio() {
		return fVaryRatio;
	}

	public void setfVaryRatio(float fVaryRatio) {
		this.fVaryRatio = fVaryRatio;
	}

	public int getiTradingVolume() {
		return iTradingVolume;
	}

	public void setiTradingVolume(int iTradingVolume) {
		this.iTradingVolume = iTradingVolume;
	}

	public int getiForeignTradingVolume() {
		return iForeignTradingVolume;
	}

	public void setiForeignTradingVolume(int iForeignTradingVolume) {
		this.iForeignTradingVolume = iForeignTradingVolume;
	}

	public int getiOrganTradingVolume() {
		return iOrganTradingVolume;
	}

	public void setiOrganTradingVolume(int iOrganTradingVolume) {
		this.iOrganTradingVolume = iOrganTradingVolume;
	}

	public int getiForeignOrganTradingVolume() {
		return iForeignOrganTradingVolume;
	}

	public void setiForeignOrganTradingVolume(int iForeignOrganTradingVolume) {
		this.iForeignOrganTradingVolume = iForeignOrganTradingVolume;
	}

	public long getlForeignTradingAmount() {
		return lForeignTradingAmount;
	}

	public void setlForeignTradingAmount(long lForeignTradingAmount) {
		this.lForeignTradingAmount = lForeignTradingAmount;
	}

	public long getlTradingAmount() {
		return lTradingAmount;
	}

	public void setlTradingAmount(long lTradingAmount) {
		this.lTradingAmount = lTradingAmount;
	}

	public long getlOrganTradingAmount() {
		return lOrganTradingAmount;
	}

	public void setlOrganTradingAmount(long lOrganTradingAmount) {
		this.lOrganTradingAmount = lOrganTradingAmount;
	}

	public long getlForeignOrganTradingAmount() {
		return lForeignOrganTradingAmount;
	}

	public void setlForeignOrganTradingAmount(long lForeignOrganTradingAmount) {
		this.lForeignOrganTradingAmount = lForeignOrganTradingAmount;
	}

	public long getlForeignHaveVolume() {
		return lForeignHaveVolume;
	}

	public void setlForeignHaveVolume(long lForeignHaveVolume) {
		this.lForeignHaveVolume = lForeignHaveVolume;
	}

	public long getlForeignHaveAmount() {
		return lForeignHaveAmount;
	}

	public void setlForeignHaveAmount(long lForeignHaveAmount) {
		this.lForeignHaveAmount = lForeignHaveAmount;
	}

	public float getfForeignHaveRatio() {
		return fForeignHaveRatio;
	}

	public void setfForeignHaveRatio(float fForeignHaveRatio) {
		this.fForeignHaveRatio = fForeignHaveRatio;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getMainPhone() {
		return mainPhone;
	}

	public void setMainPhone(String mainPhone) {
		this.mainPhone = mainPhone;
	}

	public String getStockPhone() {
		return stockPhone;
	}

	public void setStockPhone(String stockPhone) {
		this.stockPhone = stockPhone;
	}

	public String getRetainVolume() {
		return retainVolume;
	}

	public void setRetainVolume(String retainVolume) {
		this.retainVolume = retainVolume;
	}

	public String getRetainRatio() {
		return retainRatio;
	}

	public void setRetainRatio(String retainRatio) {
		this.retainRatio = retainRatio;
	}

	public float getfRetainRatio() {
		return fRetainRatio;
	}

	public void setfRetainRatio(float fRetainRatio) {
		this.fRetainRatio = fRetainRatio;
	}

	public String getRetainAmount() {
		return retainAmount;
	}

	public void setRetainAmount(String retainAmount) {
		this.retainAmount = retainAmount;
	}

	public long getlRetainAmount() {
		return lRetainAmount;
	}

	public void setlRetainAmount(long lRetainAmount) {
		this.lRetainAmount = lRetainAmount;
	}

	public int getOrganStraitBuyCount() {
		return organStraitBuyCount;
	}

	public void setOrganStraitBuyCount(int organStraitBuyCount) {
		this.organStraitBuyCount = organStraitBuyCount;
	}

	public int getForeignStraitBuyCount() {
		return foreignStraitBuyCount;
	}

	public void setForeignStraitBuyCount(int foreignStraitBuyCount) {
		this.foreignStraitBuyCount = foreignStraitBuyCount;
	}

	public int getOrganStraitSellCount() {
		return organStraitSellCount;
	}

	public void setOrganStraitSellCount(int organStraitSellCount) {
		this.organStraitSellCount = organStraitSellCount;
	}

	public int getForeignStraitSellCount() {
		return foreignStraitSellCount;
	}

	public void setForeignStraitSellCount(int foreignStraitSellCount) {
		this.foreignStraitSellCount = foreignStraitSellCount;
	}

	public String getHeadquartersAddress() {
		return headquartersAddress;
	}

	public void setHeadquartersAddress(String headquartersAddress) {
		this.headquartersAddress = headquartersAddress;
	}

	public String getFoundDay() {
		return foundDay;
	}

	public void setFoundDay(String foundDay) {
		this.foundDay = foundDay;
	}

	public String getListedDay() {
		return listedDay;
	}

	public void setListedDay(String listedDay) {
		this.listedDay = listedDay;
	}

	public String getCeo() {
		return ceo;
	}

	public void setCeo(String ceo) {
		this.ceo = ceo;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getNumberOfEmployee() {
		return numberOfEmployee;
	}

	public void setNumberOfEmployee(String numberOfEmployee) {
		this.numberOfEmployee = numberOfEmployee;
	}

	public String getTotalNumberOfStock() {
		return totalNumberOfStock;
	}

	public void setTotalNumberOfStock(String totalNumberOfStock) {
		this.totalNumberOfStock = totalNumberOfStock;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getTransferShareholdersName() {
		return transferShareholdersName;
	}

	public void setTransferShareholdersName(String transferShareholdersName) {
		this.transferShareholdersName = transferShareholdersName;
	}

	public String getMainBank() {
		return mainBank;
	}

	public void setMainBank(String mainBank) {
		this.mainBank = mainBank;
	}

	public int getMaxPriceCount() {
		return maxPriceCount;
	}

	public void setMaxPriceCount(int maxPriceCount) {
		this.maxPriceCount = maxPriceCount;
	}

	public int getMinPriceCount() {
		return minPriceCount;
	}

	public void setMinPriceCount(int minPriceCount) {
		this.minPriceCount = minPriceCount;
	}

	public String getMajorStockHolders() {
		return majorStockHolders;
	}

	public void setMajorStockHolders(String majorStockHolders) {
		this.majorStockHolders = majorStockHolders;
	}

	public String getStockTotalAmount() {
		return stockTotalAmount;
	}

	public void setStockTotalAmount(String stockTotalAmount) {
		this.stockTotalAmount = stockTotalAmount;
	}

	public long getlStockTotalAmount() {
		return lStockTotalAmount;
	}

	public void setlStockTotalAmount(long lStockTotalAmount) {
		this.lStockTotalAmount = lStockTotalAmount;
	}

	public int getiTotalNumberOfStock() {
		return iTotalNumberOfStock;
	}

	public void setiTotalNumberOfStock(int iTotalNumberOfStock) {
		this.iTotalNumberOfStock = iTotalNumberOfStock;
	}

	public String getStockTotalVolume() {
		return stockTotalVolume;
	}

	public void setStockTotalVolume(String stockTotalVolume) {
		this.stockTotalVolume = stockTotalVolume;
	}

	public int getiStockTotalVolume() {
		return iStockTotalVolume;
	}

	public void setiStockTotalVolume(int iStockTotalVolume) {
		this.iStockTotalVolume = iStockTotalVolume;
	}

	public String getWeeks52MinPrice() {
		return weeks52MinPrice;
	}

	public void setWeeks52MinPrice(String weeks52MinPrice) {
		this.weeks52MinPrice = weeks52MinPrice;
	}

	public String getWeeks52MaxPrice() {
		return weeks52MaxPrice;
	}

	public void setWeeks52MaxPrice(String weeks52MaxPrice) {
		this.weeks52MaxPrice = weeks52MaxPrice;
	}

	public int getiWeeks52MinPrice() {
		return iWeeks52MinPrice;
	}

	public void setiWeeks52MinPrice(int iWeeks52MinPrice) {
		this.iWeeks52MinPrice = iWeeks52MinPrice;
	}

	public int getiWeeks52MaxPrice() {
		return iWeeks52MaxPrice;
	}

	public void setiWeeks52MaxPrice(int iWeeks52MaxPrice) {
		this.iWeeks52MaxPrice = iWeeks52MaxPrice;
	}

	public Vector<MajorStockHolderVO> getMajorStockHolderList() {
		return majorStockHolderList;
	}

	public void setMajorStockHolderList(Vector<MajorStockHolderVO> majorStockHolderList) {
		this.majorStockHolderList = majorStockHolderList;
	}

	public float getlRetainVolume() {
		return lRetainVolume;
	}

	public void setlRetainVolume(float lRetainVolume) {
		this.lRetainVolume = lRetainVolume;
	}

	public String getStrScore() {
		return strScore;
	}

	public void setStrScore(String strScore) {
		this.strScore = strScore;
	}

	public float getfScore() {
		return fScore;
	}

	public void setfScore(float fScore) {
		this.fScore = fScore;
	}

	public String getStrTargetPrice() {
		return strTargetPrice;
	}

	public void setStrTargetPrice(String strTargetPrice) {
		this.strTargetPrice = strTargetPrice;
	}

	public int getiTargetPrice() {
		return iTargetPrice;
	}

	public void setiTargetPrice(int iTargetPrice) {
		this.iTargetPrice = iTargetPrice;
	}

	public String getStrEps() {
		return strEps;
	}

	public void setStrEps(String strEps) {
		this.strEps = strEps;
	}

	public int getiEps() {
		return iEps;
	}

	public void setiEps(int iEps) {
		this.iEps = iEps;
	}

	public String getStrPer() {
		return strPer;
	}

	public void setStrPer(String strPer) {
		this.strPer = strPer;
	}

	public float getfPer() {
		return fPer;
	}

	public void setfPer(float fPer) {
		this.fPer = fPer;
	}

	public String getStrEstimateCount() {
		return strEstimateCount;
	}

	public void setStrEstimateCount(String strEstimateCount) {
		this.strEstimateCount = strEstimateCount;
	}

	public int getiEstimateCount() {
		return iEstimateCount;
	}

	public void setiEstimateCount(int iEstimateCount) {
		this.iEstimateCount = iEstimateCount;
	}

	public float getfGapRatio() {
		return fGapRatio;
	}

	public void setfGapRatio(float fGapRatio) {
		this.fGapRatio = fGapRatio;
	}

	public float getiGapPrice() {
		return iGapPrice;
	}

	public void setiGapPrice(float iGapPrice) {
		this.iGapPrice = iGapPrice;
	}

	public String getStrGapPrice() {
		return strGapPrice;
	}

	public void setStrGapPrice(String strGapPrice) {
		this.strGapPrice = strGapPrice;
	}

	public float getUpDownRatio() {
		return upDownRatio;
	}

	public void setUpDownRatio(float upDownRatio) {
		this.upDownRatio = upDownRatio;
	}

	public long getlTradingVolume() {
		return lTradingVolume;
	}

	public void setlTradingVolume(long lTradingVolume) {
		this.lTradingVolume = lTradingVolume;
	}

//	@Override
//	public String toString() {
//		StringBuilder result = new StringBuilder();
//		String newLine = System.getProperty("line.separator");
//
//		result.append(this.getClass().getName());
//		result.append(" Object {");
//		result.append(newLine);
//
//		// determine fields declared in this class only (no fields of
//		// superclass)
//		Field[] fields = this.getClass().getDeclaredFields();
//
//		// print field names paired with their values
//		for (Field field : fields) {
//			result.append("  ");
//			try {
//				result.append(field.getName());
//				result.append(": ");
//				// requires access to private field:
//				result.append(field.get(this));
//			} catch (IllegalAccessException ex) {
//				System.out.println(ex);
//			}
//			result.append(newLine);
//		}
//		result.append("}");
//
//		return result.toString();
//	}
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getResultDetailMessage() {
		return resultDetailMessage;
	}

	public void setResultDetailMessage(String resultDetailMessage) {
		this.resultDetailMessage = resultDetailMessage;
	}

	public boolean isWeeks52NewHighPrice() {
		return weeks52NewHighPrice;
	}

	public void setWeeks52NewHighPrice(boolean weeks52NewHighPrice) {
		this.weeks52NewHighPrice = weeks52NewHighPrice;
	}

	public boolean isWeeks52NewLowPrice() {
		return weeks52NewLowPrice;
	}

	public void setWeeks52NewLowPrice(boolean weeks52NewLowPrice) {
		this.weeks52NewLowPrice = weeks52NewLowPrice;
	}

	public double getWeeks52NewHighPriceVsCurPriceDownRatio() {
		return weeks52NewHighPriceVsCurPriceDownRatio;
	}

	public void setWeeks52NewHighPriceVsCurPriceDownRatio(double weeks52NewHighPriceVsCurPriceDownRatio) {
		this.weeks52NewHighPriceVsCurPriceDownRatio = weeks52NewHighPriceVsCurPriceDownRatio;
	}

	public double getWeeks52NewLowPriceVsCurPriceUpRatio() {
		return weeks52NewLowPriceVsCurPriceUpRatio;
	}

	public void setWeeks52NewLowPriceVsCurPriceUpRatio(double weeks52NewLowPriceVsCurPriceUpRatio) {
		this.weeks52NewLowPriceVsCurPriceUpRatio = weeks52NewLowPriceVsCurPriceUpRatio;
	}

	public double getSpecificeDayPriceVsCurPriceUpDownRatio() {
		return specificeDayPriceVsCurPriceUpDownRatio;
	}

	public void setSpecificeDayPriceVsCurPriceUpDownRatio(double specificeDayPriceVsCurPriceUpDownRatio) {
		this.specificeDayPriceVsCurPriceUpDownRatio = specificeDayPriceVsCurPriceUpDownRatio;
	}

	public double getSpecificDayEndPriceVsCurPriceUpDownRatio() {
		return specificDayEndPriceVsCurPriceUpDownRatio;
	}

	public void setSpecificDayEndPriceVsCurPriceUpDownRatio(double specificDayEndPriceVsCurPriceUpDownRatio) {
		this.specificDayEndPriceVsCurPriceUpDownRatio = specificDayEndPriceVsCurPriceUpDownRatio;
	}

	public String getEps() {
		return eps;
	}

	public void setEps(String eps) {
		this.eps = eps;
	}

	public String getBps() {
		return bps;
	}

	public void setBps(String bps) {
		this.bps = bps;
	}

	public String getRoe() {
		return roe;
	}

	public void setRoe(String roe) {
		this.roe = roe;
	}

	public String getPer() {
		return per;
	}

	public void setPer(String per) {
		this.per = per;
	}

	public String getBizTypePer() {
		return bizTypePer;
	}

	public void setBizTypePer(String bizTypePer) {
		this.bizTypePer = bizTypePer;
	}

	public String getPbr() {
		return pbr;
	}

	public void setPbr(String pbr) {
		this.pbr = pbr;
	}

	public String getDividends() {
		return dividends;
	}

	public void setDividends(String dividends) {
		this.dividends = dividends;
	}

	public String getDividendRate() {
		return dividendRate;
	}

	public void setDividendRate(String dividendRate) {
		this.dividendRate = dividendRate;
	}

	public int getiBps() {
		return iBps;
	}

	public void setiBps(int iBps) {
		this.iBps = iBps;
	}

	public float getfRoe() {
		return fRoe;
	}

	public void setfRoe(float fRoe) {
		this.fRoe = fRoe;
	}

	public float getfBizTypePer() {
		return fBizTypePer;
	}

	public void setfBizTypePer(float fBizTypePer) {
		this.fBizTypePer = fBizTypePer;
	}

	public float getfPbr() {
		return fPbr;
	}

	public void setfPbr(float fPbr) {
		this.fPbr = fPbr;
	}

	public float getfDividendRate() {
		return fDividendRate;
	}

	public void setfDividendRate(float fDividendRate) {
		this.fDividendRate = fDividendRate;
	}

	public float getMinCurRatio() {
		return minCurRatio;
	}

	public void setMinCurRatio(float minCurRatio) {
		this.minCurRatio = minCurRatio;
	}

	public float getMaxCurRatio() {
		return maxCurRatio;
	}

	public void setMaxCurRatio(float maxCurRatio) {
		this.maxCurRatio = maxCurRatio;
	}

	public float getMinMaxRatio() {
		return minMaxRatio;
	}

	public void setMinMaxRatio(float minMaxRatio) {
		this.minMaxRatio = minMaxRatio;
	}

	public float getStartCurRatio() {
		return startCurRatio;
	}

	public void setStartCurRatio(float startCurRatio) {
		this.startCurRatio = startCurRatio;
	}

	public String getYearStartPrice() {
		return yearStartPrice;
	}

	public void setYearStartPrice(String yearStartPrice) {
		this.yearStartPrice = yearStartPrice;
	}

	public String getSpecificDay() {
		return specificDay;
	}

	public void setSpecificDay(String specificDay) {
		this.specificDay = specificDay;
	}

	public String getSpecificDayEndPrice() {
		return specificDayEndPrice;
	}

	public void setSpecificDayEndPrice(String specificDayEndPrice) {
		this.specificDayEndPrice = specificDayEndPrice;
	}

	public int getiSpecificDayEndPrice() {
		return iSpecificDayEndPrice;
	}

	public void setiSpecificDayEndPrice(int iSpecificDayEndPrice) {
		this.iSpecificDayEndPrice = iSpecificDayEndPrice;
	}

	public long getlRetainVolumeTotal() {
		return lRetainVolumeTotal;
	}

	public void setlRetainVolumeTotal(long lRetainVolumeTotal) {
		this.lRetainVolumeTotal = lRetainVolumeTotal;
	}

	public long getlRetainAmountTotal() {
		return lRetainAmountTotal;
	}

	public void setlRetainAmountTotal(long lRetainAmountTotal) {
		this.lRetainAmountTotal = lRetainAmountTotal;
	}

	public float getfRetainRatioTotal() {
		return fRetainRatioTotal;
	}

	public void setfRetainRatioTotal(float fRetainRatioTotal) {
		this.fRetainRatioTotal = fRetainRatioTotal;
	}

	public String getSpecificDay1() {
		return specificDay1;
	}

	public void setSpecificDay1(String specificDay1) {
		this.specificDay1 = specificDay1;
	}

	public String getSpecificDayEndPrice1() {
		return specificDayEndPrice1;
	}

	public void setSpecificDayEndPrice1(String specificDayEndPrice1) {
		this.specificDayEndPrice1 = specificDayEndPrice1;
	}

	public int getiSpecificDayEndPrice1() {
		return iSpecificDayEndPrice1;
	}

	public void setiSpecificDayEndPrice1(int iSpecificDayEndPrice1) {
		this.iSpecificDayEndPrice1 = iSpecificDayEndPrice1;
	}

	public String getSpecificDay2() {
		return specificDay2;
	}

	public void setSpecificDay2(String specificDay2) {
		this.specificDay2 = specificDay2;
	}

	public String getSpecificDayEndPrice2() {
		return specificDayEndPrice2;
	}

	public void setSpecificDayEndPrice2(String specificDayEndPrice2) {
		this.specificDayEndPrice2 = specificDayEndPrice2;
	}

	public int getiSpecificDayEndPrice2() {
		return iSpecificDayEndPrice2;
	}

	public void setiSpecificDayEndPrice2(int iSpecificDayEndPrice2) {
		this.iSpecificDayEndPrice2 = iSpecificDayEndPrice2;
	}

	public String getSpecificDay3() {
		return specificDay3;
	}

	public void setSpecificDay3(String specificDay3) {
		this.specificDay3 = specificDay3;
	}

	public String getSpecificDayEndPrice3() {
		return specificDayEndPrice3;
	}

	public void setSpecificDayEndPrice3(String specificDayEndPrice3) {
		this.specificDayEndPrice3 = specificDayEndPrice3;
	}

	public int getiSpecificDayEndPrice3() {
		return iSpecificDayEndPrice3;
	}

	public void setiSpecificDayEndPrice3(int iSpecificDayEndPrice3) {
		this.iSpecificDayEndPrice3 = iSpecificDayEndPrice3;
	}

	public String getSpecificDay4() {
		return specificDay4;
	}

	public void setSpecificDay4(String specificDay4) {
		this.specificDay4 = specificDay4;
	}

	public String getSpecificDayEndPrice4() {
		return specificDayEndPrice4;
	}

	public void setSpecificDayEndPrice4(String specificDayEndPrice4) {
		this.specificDayEndPrice4 = specificDayEndPrice4;
	}

	public int getiSpecificDayEndPrice4() {
		return iSpecificDayEndPrice4;
	}

	public void setiSpecificDayEndPrice4(int iSpecificDayEndPrice4) {
		this.iSpecificDayEndPrice4 = iSpecificDayEndPrice4;
	}

	public String getSpecificDay5() {
		return specificDay5;
	}

	public void setSpecificDay5(String specificDay5) {
		this.specificDay5 = specificDay5;
	}

	public String getSpecificDayEndPrice5() {
		return specificDayEndPrice5;
	}

	public void setSpecificDayEndPrice5(String specificDayEndPrice5) {
		this.specificDayEndPrice5 = specificDayEndPrice5;
	}

	public int getiSpecificDayEndPrice5() {
		return iSpecificDayEndPrice5;
	}

	public void setiSpecificDayEndPrice5(int iSpecificDayEndPrice5) {
		this.iSpecificDayEndPrice5 = iSpecificDayEndPrice5;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getEv() {
		return ev;
	}

	public void setEv(String ev) {
		this.ev = ev;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSymbolExchangeTicker() {
		return symbolExchangeTicker;
	}

	public void setSymbolExchangeTicker(String symbolExchangeTicker) {
		this.symbolExchangeTicker = symbolExchangeTicker;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public int getfCurPrice() {
		return fCurPrice;
	}

	public void setfCurPrice(int fCurPrice) {
		this.fCurPrice = fCurPrice;
	}

	public int getfStartPrice() {
		return fStartPrice;
	}

	public void setfStartPrice(int fStartPrice) {
		this.fStartPrice = fStartPrice;
	}

	public int getfBeforePrice() {
		return fBeforePrice;
	}

	public void setfBeforePrice(int fBeforePrice) {
		this.fBeforePrice = fBeforePrice;
	}

	public int getfLowPrice() {
		return fLowPrice;
	}

	public void setfLowPrice(int fLowPrice) {
		this.fLowPrice = fLowPrice;
	}

	public int getfHighPrice() {
		return fHighPrice;
	}

	public void setfHighPrice(int fHighPrice) {
		this.fHighPrice = fHighPrice;
	}

	public int getfVaryPrice() {
		return fVaryPrice;
	}

	public void setfVaryPrice(int fVaryPrice) {
		this.fVaryPrice = fVaryPrice;
	}

	public int getfMaxPrice() {
		return fMaxPrice;
	}

	public void setfMaxPrice(int fMaxPrice) {
		this.fMaxPrice = fMaxPrice;
	}

	public int getfMinPrice() {
		return fMinPrice;
	}

	public void setfMinPrice(int fMinPrice) {
		this.fMinPrice = fMinPrice;
	}

	public String getWikiCompanyUrl() {
		return wikiCompanyUrl;
	}

	public void setWikiCompanyUrl(String wikiCompanyUrl) {
		this.wikiCompanyUrl = wikiCompanyUrl;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject objJson = new JSONObject();

		// object -> Map
		ObjectMapper oMapper = new ObjectMapper();
		Map<String, Object> map = oMapper.convertValue(this, Map.class);
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		String strJson = gson.toJson(map);
		try {
			objJson = (JSONObject) new JSONParser().parse(strJson);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objJson;
	}
}
