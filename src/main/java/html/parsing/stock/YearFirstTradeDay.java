package html.parsing.stock;

public class YearFirstTradeDay {

	public static void main(String[] args) {
		String yearFirstTradeDay = "2020.01.02";

		String stockCode = "018000";
		String stockName = "유니슨";

		String stockListedDay = StockUtil.getStockListedDay(stockCode);
		yearFirstTradeDay = StockUtil.getYearFirstTradeDay(yearFirstTradeDay, stockListedDay);
		String yearFirstTradeDayEndPrice = StockUtil.getYearFirstTradeDayEndPrice(stockCode, stockName,
				yearFirstTradeDay);
		System.out.println("stockListedDay:" + stockListedDay);
		System.out.println("yearFirstTradeDay:" + yearFirstTradeDay);

		stockCode = "005930";
		stockName = "삼성전자";

		stockListedDay = StockUtil.getStockListedDay(stockCode);
		yearFirstTradeDay = StockUtil.getYearFirstTradeDay(yearFirstTradeDay, stockListedDay);
		yearFirstTradeDayEndPrice = StockUtil.getYearFirstTradeDayEndPrice(stockCode, stockName, yearFirstTradeDay);
		System.out.println("stockListedDay:" + stockListedDay);
		System.out.println("yearFirstTradeDay:" + yearFirstTradeDay);

		stockCode = "069620";
		stockName = "대웅제약";

		stockListedDay = StockUtil.getStockListedDay(stockCode);
		yearFirstTradeDay = StockUtil.getYearFirstTradeDay(yearFirstTradeDay, stockListedDay);
		yearFirstTradeDayEndPrice = StockUtil.getYearFirstTradeDayEndPrice(stockCode, stockName, yearFirstTradeDay);
		System.out.println("stockListedDay:" + stockListedDay);
		System.out.println("yearFirstTradeDay:" + yearFirstTradeDay);

	}
}
