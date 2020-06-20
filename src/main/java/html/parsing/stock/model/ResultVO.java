package html.parsing.stock.model;

import java.util.List;


public class ResultVO {

	private String resultCode = "";
	private String resultMessage = "";
	private String resultDetailMessage = "";
	private List<StockVO> stockList;

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

	public List<StockVO> getStockList() {
		return stockList;
	}

	public void setStockList(List<StockVO> stockList) {
		this.stockList = stockList;
	}

}
