package html.parsing.stock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Stock {

    static String kospiFileName = GlobalVariables.kospiFileName;
    static String kosdaqFileName = GlobalVariables.kosdaqFileName;
//    static String kospiFileName = "new_kospi.txt";
//    static String kosdaqFileName = "new_kosdaq.txt";
    static List<StockVO> allStockList = new ArrayList<>();
    static List<StockVO> kospiStockList = new ArrayList<>();
    static List<StockVO> kosdaqStockList = new ArrayList<>();
    static List<StockVO> searchStockList = new ArrayList<StockVO>();

    private String stockGubun;
    private int lineUp;
    private String stockCode;
    private String stockName;
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
    private String forOrgTradingVolume;

    private String foreignTradingAmount;
    private String organTradingAmount;
    private String forOrgTradingAmount;

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
    private float iVaryRatio;

    private int iTradingVolume;
    private int iForeignTradingVolume;
    private int iOrganTradingVolume;
    private int iForOrgTradingVolume;

    private long lForeignTradingAmount;
    private long lTradingAmount;
    private long lOrganTradingAmount;
    private long lForOrgTradingAmount;

    private long lForeignHaveVolume;
    private long lForeignHaveAmount;
    private float fForeignHaveRatio;
    private String homePage;
    private String mainPhone;
    private String stockPhone;

    private String retainVolume;
    private String retainRatio;
    private float fRetainRatio;
    private String retainAmount;
    private long lRetainAmount;

    private int organStraitBuyCount;
    private int foreignStraitBuyCount;

    private String headquartersAddress;
    private String foundDay;
    private String listedDay;
    private String ceo;
    private String affiliation;
    private String numberOfEmployee;
    private String totalNumberOfStock;
    private String auditor;
    private String transferShareholdersName;
    private String mainBank;

    private int maxPriceCount;
    private int minPriceCount;
    private String majorStockHolders;

    private String stockTotalAmount;
    private long lStockTotalAmount;

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

    public String getForOrgTradingVolume() {
        return forOrgTradingVolume;
    }

    public void setForOrgTradingVolume(String forOrgTradingVolume) {
        this.forOrgTradingVolume = forOrgTradingVolume;
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

    public String getForOrgTradingAmount() {
        return forOrgTradingAmount;
    }

    public void setForOrgTradingAmount(String forOrgTradingAmount) {
        this.forOrgTradingAmount = forOrgTradingAmount;
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

    public float getiVaryRatio() {
        return iVaryRatio;
    }

    public void setiVaryRatio(float iVaryRatio) {
        this.iVaryRatio = iVaryRatio;
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

    public int getiForOrgTradingVolume() {
        return iForOrgTradingVolume;
    }

    public void setiForOrgTradingVolume(int iForOrgTradingVolume) {
        this.iForOrgTradingVolume = iForOrgTradingVolume;
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

    public long getlForOrgTradingAmount() {
        return lForOrgTradingAmount;
    }

    public void setlForOrgTradingAmount(long lForOrgTradingAmount) {
        this.lForOrgTradingAmount = lForOrgTradingAmount;
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);

        // determine fields declared in this class only (no fields of superclass)
        Field[] fields = this.getClass().getDeclaredFields();

        // print field names paired with their values
        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");
                // requires access to private field:
                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }

}
