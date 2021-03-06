/**
 *
 */
package html.parsing.stock.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import html.parsing.stock.model.StockVO;

/**
 * @author banks
 *
 */
public class DataSort {

    /**
     * 날짜 내림차순
     *
     * @author banks
     *
     */
    public static class ListedDayDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg1.getListedDay().compareTo(arg0.getListedDay());
        }

    }

    /**
     * 이름 오름차순
     *
     * @author banks
     *
     */
    public static class NameAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStockName().compareTo(arg1.getStockName());
        }

    }

    /**
     * 이름 내림차순
     *
     * @author banks
     *
     */
    public static class NameDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg1.getStockName().compareTo(arg0.getStockName());
        }

    }

    /**
     * 날짜 내림차순
     *
     * @author banks
     *
     */
    public static class DateDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg1.getDate().compareTo(arg0.getDate());
        }

    }

    /**
     * 정렬순서(숫자) 오름차순
     *
     * @author banks
     *
     */
    public static class LineUpAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getLineUp() < arg1.getLineUp() ? -1 : arg0.getLineUp() > arg1.getLineUp() ? 1 : 0;
        }

    }

    /**
     * 정렬순서(숫자) 내림차순
     *
     * @author banks
     *
     */
    public static class LineUpDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getLineUp() > arg1.getLineUp() ? -1 : arg0.getLineUp() < arg1.getLineUp() ? 1 : 0;
        }

    }

    /**
     * 등락율 오름차순
     *
     * @author banks
     *
     */
    public static class VaryRatioAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfVaryRatio() < arg1.getfVaryRatio() ? -1
                    : arg0.getfVaryRatio() > arg1.getfVaryRatio() ? 1 : 0;
        }

    }

    /**
     * 등락율 내림차순
     *
     * @author banks
     *
     */
    public static class VaryRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getfVaryRatio() > arg1.getfVaryRatio() ? -1
                        : arg0.getfVaryRatio() < arg1.getfVaryRatio() ? 1 : 0;
            }
            return ret;
        }

    }

    /**
     * 기준일 대비 등락율 오름차순
     *
     * @author banks
     *
     */
    public static class ChosenDayEndPriceVsCurPriceUpDownRatioRatioAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() < arg1.getChosenDayEndPriceVsCurPriceUpDownRatio() ? -1
                    : arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() > arg1.getChosenDayEndPriceVsCurPriceUpDownRatio() ? 1 : 0;
        }

    }

    /**
     * 기준일 대비 등락율 내림차순
     *
     * @author banks
     *
     */
    public static class ChosenDayEndPriceVsCurPriceUpDownRatioRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() > arg1.getChosenDayEndPriceVsCurPriceUpDownRatio() ? -1
                        : arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() < arg1.getChosenDayEndPriceVsCurPriceUpDownRatio() ? 1 : 0;
            }
            return ret;
        }

    }

    /**
     * 거래량 오름차순
     *
     * @author banks
     *
     */
    public static class TradingVolumeAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlTradingVolume() < arg1.getlTradingVolume() ? -1
                    : arg0.getlTradingVolume() > arg1.getlTradingVolume() ? 1 : 0;
        }

    }

    /**
     * 거래량 내림차순
     *
     * @author banks
     *
     */
    public static class TradingVolumeDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlTradingVolume() > arg1.getlTradingVolume() ? -1
                    : arg0.getlTradingVolume() < arg1.getlTradingVolume() ? 1 : 0;
        }

    }

    /**
     * 거래금액 오름차순
     *
     * @author banks
     *
     */
    public static class TradingAmountAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlTradingAmount() < arg1.getlTradingAmount() ? -1
                    : arg0.getlTradingAmount() > arg1.getlTradingAmount() ? 1 : 0;
        }

    }

    /**
     * 거래금액 내림차순
     *
     * @author banks
     *
     */
    public static class TradingAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlTradingAmount() > arg1.getlTradingAmount() ? -1
                    : arg0.getlTradingAmount() < arg1.getlTradingAmount() ? 1 : 0;
        }

    }

    /**
     * 외국인 거래금액 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignTradingAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getdForeignTradingAmount() > arg1.getdForeignTradingAmount() ? -1
                    : arg0.getdForeignTradingAmount() < arg1.getdForeignTradingAmount() ? 1 : 0;
        }

    }
    
    /**
     * 외국인 거래금액 오름차순
     *
     * @author banks
     *
     */
    public static class ForeignTradingAmountAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getdForeignTradingAmount() < arg1.getdForeignTradingAmount() ? -1
                    : arg0.getdForeignTradingAmount() > arg1.getdForeignTradingAmount() ? 1 : 0;
        }
    }
    
    /**
     * 외국인 거래량 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignTradingVolumeDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignTradingVolume() > arg1.getlForeignTradingVolume() ? -1
                    : arg0.getlForeignTradingVolume() < arg1.getlForeignTradingVolume() ? 1 : 0;
        }
    }

    /**
     * 외국인 거래량 오름차순
     *
     * @author banks
     *
     */
    public static class ForeignTradingVolumeAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignTradingVolume() < arg1.getlForeignTradingVolume() ? -1
                    : arg0.getlForeignTradingVolume() > arg1.getlForeignTradingVolume() ? 1 : 0;
        }
    }

    /**
     * 기관 거래금액 내림차순
     *
     * @author banks
     *
     */
    public static class OrganTradingAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getdOrganTradingAmount() > arg1.getdOrganTradingAmount() ? -1
                    : arg0.getdOrganTradingAmount() < arg1.getdOrganTradingAmount() ? 1 : 0;
        }
    }
    /**
     * 기관 거래금액 오름차순
     *
     * @author banks
     *
     */
    public static class OrganTradingAmountAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getdOrganTradingAmount() < arg1.getdOrganTradingAmount() ? -1
                    : arg0.getdOrganTradingAmount() > arg1.getdOrganTradingAmount() ? 1 : 0;
        }
    }
    /**
     * 기관 거래량 내림차순
     *
     * @author banks
     *
     */
    public static class OrganTradingVolumeDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlOrganTradingVolume() > arg1.getlOrganTradingVolume() ? -1
                    : arg0.getlOrganTradingVolume() < arg1.getlOrganTradingVolume() ? 1 : 0;
        }
    }

    /**
     * 기관 거래량 오름차순
     *
     * @author banks
     *
     */
    public static class OrganTradingVolumeAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlOrganTradingVolume() < arg1.getlOrganTradingVolume() ? -1
                    : arg0.getlOrganTradingVolume() > arg1.getlOrganTradingVolume() ? 1 : 0;
        }
    }

    /**
     * 외인,기관 거래량 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignOrganTradingVolumeDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignOrganTradingVolume() > arg1.getlForeignOrganTradingVolume() ? -1
                    : arg0.getlForeignOrganTradingVolume() < arg1.getlForeignOrganTradingVolume() ? 1 : 0;
        }

    }

    /**
     * 외인,기관 거래량 오름차순
     *
     * @author banks
     *
     */
    public static class ForeignOrganTradingVolumeAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignOrganTradingVolume() < arg1.getlForeignOrganTradingVolume() ? -1
                    : arg0.getlForeignOrganTradingVolume() > arg1.getlForeignOrganTradingVolume() ? 1 : 0;
        }
    }

    /**
     * 외인,기관 거래금액 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignOrganTradingAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignOrganTradingAmount() > arg1.getlForeignOrganTradingAmount() ? -1
                    : arg0.getlForeignOrganTradingAmount() < arg1.getlForeignOrganTradingAmount() ? 1 : 0;
        }
    }

    /**
     * 외인,기관 거래금액 오름차순
     *
     * @author banks
     *
     */
    public static class ForeignOrganTradingAmountAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignOrganTradingAmount() < arg1.getlForeignOrganTradingAmount() ? -1
                    : arg0.getlForeignOrganTradingAmount() > arg1.getlForeignOrganTradingAmount() ? 1 : 0;
        }
    }

    /**
     * 외국인 연속거래일수 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignStraitBuyCountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getForeignStraitBuyCount() > arg1.getForeignStraitBuyCount() ? -1
                    : arg0.getForeignStraitBuyCount() < arg1.getForeignStraitBuyCount() ? 1 : 0;
        }

    }

    /**
     * 기관 연속거래일수 내림차순
     *
     * @author banks
     *
     */
    public static class OrganStraitBuyCountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getOrganStraitBuyCount() > arg1.getOrganStraitBuyCount() ? -1
                    : arg0.getOrganStraitBuyCount() < arg1.getOrganStraitBuyCount() ? 1 : 0;
        }
    }

    /**
     * 외국인 연속거래일수 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignStraitSellCountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getForeignStraitSellCount() > arg1.getForeignStraitSellCount() ? -1
                    : arg0.getForeignStraitSellCount() < arg1.getForeignStraitSellCount() ? 1 : 0;
        }

    }

    /**
     * 기관 연속거래일수 내림차순
     *
     * @author banks
     *
     */
    public static class OrganStraitSellCountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getOrganStraitSellCount() > arg1.getOrganStraitSellCount() ? -1
                    : arg0.getOrganStraitSellCount() < arg1.getOrganStraitSellCount() ? 1 : 0;
        }
    }

    /**
     * 외국인 보유율 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignHaveRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfForeignHaveRatio() > arg1.getfForeignHaveRatio() ? -1
                    : arg0.getfForeignHaveRatio() < arg1.getfForeignHaveRatio() ? 1 : 0;
        }

    }

    /**
     * 외국인 보유금액순 내림차순
     *
     * @author banks
     *
     */
    public static class ForeignHaveAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlForeignHaveAmount() > arg1.getlForeignHaveAmount() ? -1
                    : arg0.getlForeignHaveAmount() < arg1.getlForeignHaveAmount() ? 1 : 0;
        }

    }

    /**
     * 보유율 내림차순
     *
     * @author banks
     *
     */
    public static class RetainRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfRetainRatio() > arg1.getfRetainRatio() ? -1
                    : arg0.getfRetainRatio() < arg1.getfRetainRatio() ? 1 : 0;
        }
    }

    /**
     * 보유금액 내림차순
     *
     * @author banks
     *
     */
    public static class RetainAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlRetainAmount() > arg1.getlRetainAmount() ? -1
                    : arg0.getlRetainAmount() < arg1.getlRetainAmount() ? 1 : 0;
        }
    }

    /**
     * 상한일수 내림차순
     *
     * @author banks
     *
     */
    public static class MaxPriceCountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getMaxPriceCount() > arg1.getMaxPriceCount() ? -1
                    : arg0.getMaxPriceCount() < arg1.getMaxPriceCount() ? 1 : 0;
        }

    }

    /**
     * 저가 대비 현재가 상승률
     *
     * @author banks
     *
     */
    public static class MinCurDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getMinCurRatio() > arg1.getMinCurRatio() ? -1
                    : arg0.getMinCurRatio() < arg1.getMinCurRatio() ? 1 : 0;
        }

    }

    /**
     * 고가 대비 현재가 하락률
     *
     * @author banks
     *
     */
    public static class MaxCurAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getMaxCurRatio() < arg1.getMaxCurRatio() ? -1
                    : arg0.getMaxCurRatio() > arg1.getMaxCurRatio() ? 1 : 0;
        }

    }

    /**
     * 저가 대비 고가 등락률 내림차순
     *
     * @author banks
     *
     */
    public static class MinMaxDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getMinMaxRatio() > arg1.getMinMaxRatio() ? -1
                    : arg0.getMinMaxRatio() < arg1.getMinMaxRatio() ? 1 : 0;
        }

    }

    /**
     * 년초 대비 현재가 등락률
     *
     * @author banks
     *
     */
    public static class YearStartCurDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStartCurRatio() > arg1.getStartCurRatio() ? -1
                    : arg0.getStartCurRatio() < arg1.getStartCurRatio() ? 1 : 0;
        }

    }

    /**
     * 시가총액
     *
     * @author banks
     *
     */
    public static class TotalAmountDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getlStockTotalAmount() > arg1.getlStockTotalAmount() ? -1
                    : arg0.getlStockTotalAmount() < arg1.getlStockTotalAmount() ? 1 : 0;
        }

    }

    /**
     * 발행주식수
     *
     * @author banks
     *
     */
    public static class TotalVolumeDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getiStockTotalVolume() > arg1.getiStockTotalVolume() ? -1
                    : arg0.getiStockTotalVolume() < arg1.getiStockTotalVolume() ? 1 : 0;
        }

    }

    /**
     * 투자지표 오름차순
     *
     * @author banks
     *
     */
    public static class ScoreAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfScore() < arg1.getfScore() ? -1 : arg0.getfScore() > arg1.getfScore() ? 1 : 0;
        }
    }

    /**
     * 투자지표 내림차순
     *
     * @author banks
     *
     */
    public static class ScoreDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfScore() > arg1.getfScore() ? -1 : arg0.getfScore() < arg1.getfScore() ? 1 : 0;
        }

    }

    /**
     * 괴리율 오름차순
     *
     * @author banks
     *
     */
    public static class GapRatioAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfGapRatio() < arg1.getfGapRatio() ? -1 : arg0.getfGapRatio() > arg1.getfGapRatio() ? 1 : 0;
        }
    }

    /**
     * 괴리율 내림차순
     *
     * @author banks
     *
     */
    public static class GapRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfGapRatio() > arg1.getfGapRatio() ? -1 : arg0.getfGapRatio() < arg1.getfGapRatio() ? 1 : 0;
        }

    }

    /**
     * 이름 오름차순
     *
     * @author banks
     *
     */
    public static class StockNameAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStockName().compareTo(arg1.getStockName());
        }

    }

    public static class StockNameAscCompare2 implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStockName().compareTo(arg1.getStockName());
        }

    }

    /**
     * 이름 내림차순
     *
     * @author banks
     *
     */
    public static class StockNameDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg1.getStockName().compareTo(arg0.getStockName());
        }

    }

    public static class StockNameDescCompare2 implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg1.getStockName().compareTo(arg0.getStockName());
        }

    }

    /**
     * 정렬순서(EPS) 오름차순
     *
     * @author banks
     *
     */
    public static class EpsAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getiEps() < arg1.getiEps() ? -1 : arg0.getiEps() > arg1.getiEps() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(EPS) 내림차순
     *
     * @author banks
     *
     */
    public static class EpsDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getiEps() > arg1.getiEps() ? -1 : arg0.getiEps() < arg1.getiEps() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(BPS) 오름차순
     *
     * @author banks
     *
     */
    public static class BpsAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getiBps() < arg1.getiBps() ? -1 : arg0.getiBps() > arg1.getiBps() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(BPS) 내림차순
     *
     * @author banks
     *
     */
    public static class BpsDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getiBps() > arg1.getiBps() ? -1 : arg0.getiBps() < arg1.getiBps() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(PER) 오름차순
     *
     * @author banks
     *
     */
    public static class PerAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfPer() < arg1.getfPer() ? -1 : arg0.getfPer() > arg1.getfPer() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(PER) 내림차순
     *
     * @author banks
     *
     */
    public static class PerDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfPer() > arg1.getfPer() ? -1 : arg0.getfPer() < arg1.getfPer() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(업종PER) 오름차순
     *
     * @author banks
     *
     */
    public static class BizTypePerAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfBizTypePer() < arg1.getfBizTypePer() ? -1
                    : arg0.getfBizTypePer() > arg1.getfBizTypePer() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(업종PER) 내림차순
     *
     * @author banks
     *
     */
    public static class BizTypePerDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfBizTypePer() > arg1.getfBizTypePer() ? -1
                    : arg0.getfBizTypePer() < arg1.getfBizTypePer() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(PBR) 오름차순
     *
     * @author banks
     *
     */
    public static class PbrAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfPbr() < arg1.getfPbr() ? -1 : arg0.getfPbr() > arg1.getfPbr() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(PBR) 내림차순
     *
     * @author banks
     *
     */
    public static class PbrDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfPbr() > arg1.getfPbr() ? -1 : arg0.getfPbr() < arg1.getfPbr() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(배당수익률) 오름차순
     *
     * @author banks
     *
     */
    public static class DividendRateAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfDividendRate() < arg1.getfDividendRate() ? -1
                    : arg0.getfDividendRate() > arg1.getfDividendRate() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(배당수익률) 내림차순
     *
     * @author banks
     *
     */
    public static class DividendRateDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfDividendRate() > arg1.getfDividendRate() ? -1
                    : arg0.getfDividendRate() < arg1.getfDividendRate() ? 1 : 0;
        }
    }

    /**
     * 정렬순서(ROE) 내림차순
     *
     * @author banks
     *
     */
    public static class RoeDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getfRoe() > arg1.getfRoe() ? -1 : arg0.getfRoe() < arg1.getfRoe() ? 1 : 0;
        }
    }

    /**
     * 증권명 길이 오름차순
     *
     * @author banks
     *
     */
    public static class StockNameLengthAscCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStockNameLength() < arg1.getStockNameLength() ? -1
                    : arg0.getStockNameLength() > arg1.getStockNameLength() ? 1 : 0;
        }
    }

    /**
     * 증권명 길이 내림차순
     *
     * @author banks
     *
     */
    public static class StockNameLengthDescCompare implements Comparator<StockVO> {

        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            return arg0.getStockNameLength() > arg1.getStockNameLength() ? -1
                    : arg0.getStockNameLength() < arg1.getStockNameLength() ? 1 : 0;
        }
    }

    /**
     * 52주 신고가 대비 하락율 오름차순
     *
     * @author banks
     *
     */
    public static class Weeks52NewHighPriceVsCurPriceDownRatioAscCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getWeeks52NewHighPriceVsCurPriceDownRatio() < arg1
                        .getWeeks52NewHighPriceVsCurPriceDownRatio() ? -1
                                : arg0.getWeeks52NewHighPriceVsCurPriceDownRatio() > arg1
                                .getWeeks52NewHighPriceVsCurPriceDownRatio() ? 1 : 0;
            }
            return ret;
        }

    }

    /**
     * 52주 신저가 대비 상승율 내림차순
     *
     * @author banks
     *
     */
    public static class Weeks52NewLowPriceVsCurPriceUpRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getWeeks52NewLowPriceVsCurPriceUpRatio() > arg1.getWeeks52NewLowPriceVsCurPriceUpRatio() ? -1
                        : arg0.getWeeks52NewLowPriceVsCurPriceUpRatio() < arg1.getWeeks52NewLowPriceVsCurPriceUpRatio()
                        ? 1
                        : 0;
            }
            return ret;
        }

    }

    /**
     * 년초 대비 하락율 오름차순
     *
     * @author banks
     *
     */
    public static class ChosenDayVsCurPriceUpDownRatioAscCompare implements Comparator<StockVO> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() < arg1
                        .getChosenDayEndPriceVsCurPriceUpDownRatio() ? -1
                                : arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() > arg1
                                .getChosenDayEndPriceVsCurPriceUpDownRatio() ? 1 : 0;
            }
            return ret;
        }

    }

    /**
     * 년초가 대비 상승율 내림차순
     *
     * @author banks
     *
     */
    public static class ChosenDayVsCurPriceUpDownRatioDescCompare implements Comparator<StockVO> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(StockVO arg0, StockVO arg1) {
            int ret = 0;
            if (arg0 != null) {
                ret = arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() > arg1.getChosenDayEndPriceVsCurPriceUpDownRatio() ? -1
                        : arg0.getChosenDayEndPriceVsCurPriceUpDownRatio() < arg1.getChosenDayEndPriceVsCurPriceUpDownRatio()
                        ? 1
                        : 0;
            }
            return ret;
        }

    }

    /**
     * 이름 오름차순
     *
     * @author banks
     *
     */
    public static class CompanyNameAscCompare implements Comparator<Map> {

        /**
         * 오름차순(ASC)
         */
//		@Override
//		public int compare(StockVO arg0, StockVO arg1) {
//			return arg0.getStockName().compareTo(arg1.getStockName());
//		}
        @Override
        public int compare(Map arg0, Map arg1) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                Set keySet1 = arg0.keySet();
                Iterator it1 = keySet1.iterator();
                String key1 = (String)it1.next();
System.out.println("key1:"+key1);
                Set keySet2 = arg1.keySet();
                Iterator it2 = keySet2.iterator();
                String key2 = (String)it2.next();
System.out.println("key2:"+key2);
                
            return key1.compareTo(key2);
        }

//        @Override
//        public Comparator<String> reversed() {
//            return Comparator.super.reversed(); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public Comparator<String> thenComparing(Comparator<? super String> other) {
//            return Comparator.super.thenComparing(other); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public <U> Comparator<String> thenComparing(Function<? super String, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
//            return Comparator.super.thenComparing(keyExtractor, keyComparator); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public <U extends Comparable<? super U>> Comparator<String> thenComparing(Function<? super String, ? extends U> keyExtractor) {
//            return Comparator.super.thenComparing(keyExtractor); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public Comparator<String> thenComparingInt(ToIntFunction<? super String> keyExtractor) {
//            return Comparator.super.thenComparingInt(keyExtractor); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public Comparator<String> thenComparingLong(ToLongFunction<? super String> keyExtractor) {
//            return Comparator.super.thenComparingLong(keyExtractor); //To change body of generated methods, choose Tools | Templates.
//        }
//
//        @Override
//        public Comparator<String> thenComparingDouble(ToDoubleFunction<? super String> keyExtractor) {
//            return Comparator.super.thenComparingDouble(keyExtractor); //To change body of generated methods, choose Tools | Templates.
//        }

    }
    
}
