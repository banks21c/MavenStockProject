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

public class StockVO {

	private String date;

	private String market;
	private String stockGubun;
	private int lineUp;
	private String stockCode;
	private String stockName;
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
	private double yearFirstTradeDayEndPriceVsCurPriceUpDownRatio;
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
    private String dividendRate;

    private int iBps;
    private float fRoe;
    private float fBizTypePer;
    private float fPbr;
    private float fDividendRate;

    private float minCurRatio;
    private float maxCurRatio;
    private float minMaxRatio;
    private float startCurRatio;
    private String yearFirstTradeDay;
    private String yearFirstTradeDayEndPrice;
    private int iYearFirstTradeDayEndPrice;
    private String yearStartPrice;

    long lRetainVolumeTotal;
    long lRetainAmountTotal;
    float fRetainRatioTotal;

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

	public double getYearFirstTradeDayEndPriceVsCurPriceUpDownRatio() {
		return yearFirstTradeDayEndPriceVsCurPriceUpDownRatio;
	}

	public void setYearFirstTradeDayEndPriceVsCurPriceUpDownRatio(double yearFirstTradeDayEndPriceVsCurPriceUpDownRatio) {
		this.yearFirstTradeDayEndPriceVsCurPriceUpDownRatio = yearFirstTradeDayEndPriceVsCurPriceUpDownRatio;
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

	public String getYearFirstTradeDay() {
		return yearFirstTradeDay;
	}

	public void setYearFirstTradeDay(String yearFirstTradeDay) {
		this.yearFirstTradeDay = yearFirstTradeDay;
	}

	public String getYearFirstTradeDayEndPrice() {
		return yearFirstTradeDayEndPrice;
	}

	public void setYearFirstTradeDayEndPrice(String yearFirstTradeDayEndPrice) {
		this.yearFirstTradeDayEndPrice = yearFirstTradeDayEndPrice;
	}

	public int getiYearFirstTradeDayEndPrice() {
		return iYearFirstTradeDayEndPrice;
	}

	public void setiYearFirstTradeDayEndPrice(int iYearFirstTradeDayEndPrice) {
		this.iYearFirstTradeDayEndPrice = iYearFirstTradeDayEndPrice;
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
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
