/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

/**
 *
 * @author banks
 */
public class StockMarketPriceRun {
    public static void main(String args[]){
//        new StockMarketPrice().extractAll();
        new StockUnique_ReadTxtFile_ThreadCall();
        new Weeks52NewLowHighPriceTodayOneFile().start();
        new AllStockPlusMinusDivide().start();
        new AllStockPlusMinusDivide100().start();
    }
}
