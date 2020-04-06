/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 * @author banks
 */
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxFileReadTest2 {
	public static final String xlsxFileName = "./kospi.xlsx";

	public static void main(String[] args) throws IOException, InvalidFormatException {

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		Workbook workbook = WorkbookFactory.create(new File(xlsxFileName));

		// Creating a Workbook from an Excel file (.xls or .xlsx)
		// Getting the Sheet at index zero
		Sheet sheet = workbook.getSheetAt(0);

		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();

		// 1. You can obtain a rowIterator and columnIterator and iterate over them
		Iterator<Row> rowIterator = sheet.rowIterator();
		int rowCnt = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			String strStockName = null;
			String strStockCode = null;
			System.out.println(rowCnt + " cell cnt:" + row.getLastCellNum());
			if (row.getLastCellNum() > 1) {
				int i = 0;
				// Now let's iterate over the columns of the current row
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					if (i == 2) {
						break;
					}
					Cell cell = cellIterator.next();
					String cellValue = dataFormatter.formatCellValue(cell);
					if (i == 0) {
						strStockName = cellValue;
					}
					if (i == 1) {
						strStockCode = cellValue;
					}
					i++;
				}
				System.out.println(rowCnt + "\t" + strStockCode + "\t" + strStockName);
				
				if (strStockCode.length() != 6) {
					continue;
				}
			}
			rowCnt++;
		}
		System.out.println("rowCnt :" + rowCnt);
		// Closing the workbook
		workbook.close();

	}
}
