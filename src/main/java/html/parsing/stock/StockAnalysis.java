/**
 *
 */
package html.parsing.stock;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author banks
 *
 */
public class StockAnalysis {

    private String stockCode;
    private String stockName;
    private String eps;
    private String bps;
    private String roe;
    private String per;
    private String bizTypePer;
    private String pbr;
    private String dividendRate;

    private int iEps;
    private int iBps;
    private float fRoe;
    private float fPer;
    private float fBizTypePer;
    private float fPbr;
    private float fDividendRate;

    private String weeks52MinPrice;
    private String weeks52MaxPrice;
    private String curPrice;

    private float minCurRatio;
    private float maxCurRatio;
    private float startCurRatio;
    private String yearStartPrice;
    private String stockTotalAmount;
    private long lStockTotalAmount;

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

    public String getEps() {
        return eps;
    }

    public void setEps(String eps) {
        this.eps = eps;
    }

    public String getRoe() {
        return roe;
    }

    public void setRoe(String roe) {
        this.roe = roe;
    }

    public String getBps() {
        return bps;
    }

    public void setBps(String bps) {
        this.bps = bps;
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

    public int getiEps() {
        return iEps;
    }

    public void setiEps(int iEps) {
        this.iEps = iEps;
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

    public float getfPer() {
        return fPer;
    }

    public void setfPer(float fPer) {
        this.fPer = fPer;
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

    public String getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(String curPrice) {
        this.curPrice = curPrice;
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

    // @Override
    // public String toString() {
    // return "StockAnalysis [stockCode=" + stockCode + ", stockName=" +
    // stockName + ", eps=" + eps + ", bps=" + bps
    // + ", per=" + per + ", bizTypePer=" + bizTypePer + ", pbr=" + pbr + ",
    // dividendRate=" + dividendRate
    // + ", iEps=" + iEps + ", iBps=" + iBps + ", fPer=" + fPer + ",
    // fBizTypePer=" + fBizTypePer + ", fPbr="
    // + fPbr + ", fDividendRate=" + fDividendRate + "]";
    // }
    public String toString() {
        // return ReflectionToStringBuilder.toString(this,
        // ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

    public static void main(String args[]) {
        System.out.println(new StockAnalysis().toString());
    }
}
