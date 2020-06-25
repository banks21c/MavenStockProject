package html.parsing.stock.focus;

/**
 *
 * @author banks
 */
public class StockMarketPriceRun {
    public static void main(String args[]){
//        new StockMarketPrice().extractAll();
        new StockUnique_ReadTxtFile_ThreadCall();
        new StockWeeks52NewLowHighPriceTodayOneFile().start();
        new StockPlusMinusDivide().start();
        new StockPlusMinusDivide100().start();
    }
}
