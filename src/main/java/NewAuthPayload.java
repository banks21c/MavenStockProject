
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author banksfamily
 */
public class NewAuthPayload {

	@JsonProperty
	private String instrumentType = "EQUITY";
	@JsonProperty
	private int pageNumber = 1;
	@JsonProperty
	private String sortColumn = "NORMALIZED_TICKER";
	@JsonProperty
	private String sortOrder = "ASC";
	@JsonProperty
	private int maxResultsPerPage = 10;
	@JsonProperty
	private String filterToken = "";

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getMaxResultsPerPage() {
		return maxResultsPerPage;
	}

	public void setMaxResultsPerPage(int maxResultsPerPage) {
		this.maxResultsPerPage = maxResultsPerPage;
	}

	public String getFilterToken() {
		return filterToken;
	}

	public void setFilterToken(String filterToken) {
		this.filterToken = filterToken;
	}

	public NewAuthPayload(String instrumentType, int pageNumber, String sortColumn, String sortOrder, int maxResultsPerPage, String filterToken) {
		this.instrumentType = instrumentType;
		this.pageNumber = pageNumber;
		this.sortColumn = sortColumn;
		this.sortOrder = sortOrder;
		this.maxResultsPerPage = maxResultsPerPage;
		this.filterToken = filterToken;
	}
}
