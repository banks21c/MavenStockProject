package html.parsing.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.model.StockNewsVO;
import html.parsing.stock.model.StockVO;
import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;
import html.parsing.stock.util.DataSort.StockNameLengthDescCompare;
import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;

public class StockUpjongInput extends News implements NewsInterface {

    
    private static Logger logger = LoggerFactory.getLogger(StockUpjongInput.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    DecimalFormat df = new DecimalFormat("###.##");

    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
    String strYmd = sdf0.format(new Date());
    int iYmd = Integer.parseInt(strYmd);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
    String strDefaultDate = sdf.format(new Date());
    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E
    // ",Locale.KOREAN).format(new Date());
    String strYMD = "[" + strDefaultDate.replaceAll("\\.", "-") + "] ";
    String strDate = "";
    String strThemeName = "2차전지 관련주";
    String strThemeCode = "";
    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;
    String strStockCode = "011170";
    String strStockName = "롯데케미칼";

    List<StockVO> allStockList = new ArrayList<StockVO>();
    List<StockVO> searchList = new ArrayList<StockVO>();

    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();

    List<StockNewsVO> stockNewsList1 = new ArrayList<StockNewsVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockUpjongInput(1);
    }

    StockUpjongInput() {


        strThemeName = JOptionPane.showInputDialog("업종명을 입력해 주세요", strThemeName);
        System.out.println("strThemeName:" + strThemeName);
        String themeMarketPrice = getThemeMarketPrice(strThemeName);
        writeFile(strThemeName, themeMarketPrice);
    }

    StockUpjongInput(int i) {

        
        strDate = JOptionPane.showInputDialog("날짜를 입력해 주세요(YYYY.MM.DD)", strDefaultDate);
        strDate = StringUtils.defaultString(strDate);
        if (strDate.equals("")) {
            strDate = strDefaultDate;
        }
        String year = strDate.substring(0, 4);
        String month = strDate.substring(5, 7);
        String day = strDate.substring(8, 10);
        int iYear = Integer.parseInt(year);
        int iMonth = Integer.parseInt(month) - 1;
        int iDay = Integer.parseInt(day);
        // System.out.println(year + month + day);

        cal1.set(iYear, iMonth, iDay);

        strThemeName = JOptionPane.showInputDialog("업종명을 입력해 주세요", strThemeName);
        // System.out.println("strThemeName:" + strThemeName);
        String themeMarketPrice = getThemeMarketPrice(strThemeName);
        // System.out.println("themeMarketPrice:" + themeMarketPrice);

        // 코스피
        readFile("코스피", kospiFileName);
        // 코스닥
        readFile("코스닥", kosdaqFileName);


        Collections.sort(allStockList, new StockNameLengthDescCompare());


        writeFile(strThemeName, themeMarketPrice);
    }

    public void readOne(String stockCode) {
        int cnt = 1;
        StockVO stock = new StockVO();
        stock.setStockCode(stockCode);
    }

    public void readFile(String kospidaq, String fileName) {
        File f = new File(USER_HOME + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int stockNameLength = 0;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                // System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];
                stockNameLength = stockName.length();

                StockVO stock1 = new StockVO();
                stock1.setStockCode(stockCode);
                stock1.setStockName(stockName);
                stock1.setStockNameLength(stockNameLength);

                if (stockCode.length() != 6) {
                    continue;
                }
                allStockList.add(stock1);
                cnt++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    public String getThemeMarketPrice(String strThemeName) {
        Document doc;
        String themeMarketPrice = null;
        try {
            // 업종목록
            doc = Jsoup.connect("https://finance.naver.com/sise/sise_group.nhn?type=upjong").get();
            // System.out.println("doc:"+doc);

            Elements themeTable = doc.select("table.type_1.theme");
            // System.out.println("themeTable:"+themeTable);
            Elements themeTr = themeTable.select("tr");
            System.out.println("themeTr.size:" + themeTr.size());
            for (int i = 3; i < themeTr.size(); i++) {
                Element tr = themeTr.get(i);
                // System.out.println("tr:" + tr);
                Elements as = tr.select("a");
                // System.out.println("as:" + as+" as.size:"+as.size());
                if (as != null && as.size() > 0) {
                    String trThemeName = as.first().text();
                    trThemeName = trThemeName.replaceAll("/", ",");
                    System.out.println("업종명:" + trThemeName);
                    if (trThemeName.indexOf(strThemeName) != -1) {
                        String themeLink = tr.select("a").first().attr("href");
                        if (themeLink != null && themeLink.indexOf("&no=") != -1) {
                            strThemeCode = themeLink.substring(themeLink.indexOf("&no=") + 4);
                            System.out.println("strThemeCode1:" + strThemeCode);
                        }
                    }
                }
            }
            themeMarketPrice = getThemeMarketPrice2(strThemeCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return themeMarketPrice;
    }

    public String getThemeMarketPrice2(String strThemeCode) {
        System.out.println("strThemeCode2:" + strThemeCode);
        Document doc = null;
        String themeMarketPrice = null;
        if (strThemeCode != null) {
            String url = "https://finance.naver.com/sise/sise_group.nhn?type=upjong&no=" + strThemeCode;

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(StockUpjongInput.class.getName()).log(Level.SEVERE, null, ex);
            }

            Elements themeStockTables = doc.select(".type_5");
            Element themeStockTable = null;
            if (!themeStockTables.isEmpty()) {
                themeStockTable = themeStockTables.get(0);
            } else {
                return null;
            }
            themeStockTable.attr("style", "margin:0 0 10px 0; width:741px;");
            // System.out.println("themeStockTable:"+themeStockTable);
            Element themeStockTableColgroup = themeStockTable.select("colgroup").get(0);
            themeStockTableColgroup.remove();
            // Elements themeStockTableCol = themeStockTableColgroup.select("col");
            // for(int i=0;i<themeStockTableCol.size();i++){
            // Element col = themeStockTableCol.get(i);
            // if (i == 4 || i == 5 || i == 8) {
            // col.remove();
            // }
            // }
            themeStockTable.select("caption").remove();
            Elements themeStockTableTr = themeStockTable.select("tr");
            for (int i = 0; i < themeStockTableTr.size(); i++) {
                Element tr = themeStockTableTr.get(i);
                Elements ths = tr.select("th");
                for (int j = 0; j < ths.size(); j++) {
                    Element th = ths.get(j);
                    th.attr("style", "white-space:nowrap");

                    if (j == 4 || j == 5 || j == 8) {
                        th.remove();
                    }
                }

                Elements tds = tr.select("td");
                String color = "";
                if (!tds.isEmpty() && tds.size() > 2) {
                    System.out.println("tds.size:" + tds.size());
                    Elements spanElements = tds.get(2).select("span");
                    if (!spanElements.isEmpty()) {
                        Element spanElement = spanElements.get(0);
                        if (spanElement != null && !spanElement.text().equals("")) {
                            String spanClass = spanElement.attr("class");
                            System.out.println("spanClass:" + spanClass);
                            if (spanClass.contains("nv01")) {
                                color = "blue";
                            } else if (spanClass.contains("red01")) {
                                color = "red";
                            }
                        }
                    }
                }

                for (int j = 0; j < tds.size(); j++) {
                    Element td = tds.get(j);

                    if (j == 4 || j == 5 || j == 8) {
                        td.remove();
                    }
                    td.removeAttr("style");
                    if (j == 0) {
                        td.attr("style", "text-align:left;padding:1px;color:" + color + ";");
                    } else if (j > 0 && j < 4) {
                        td.attr("style", "text-align:right;padding:0 5px;color:" + color + ";");
                    } else {
                        td.attr("style", "text-align:right;padding:0 5px;");
                    }
                    td.removeClass("number");
                    td.removeAttr("class");
                    System.out.println("span:" + td.select("span"));
                }
                if (tds.size() == 10) {
                    Element a = tds.select("a").get(0);
                    String href = a.attr("href");
                    String stockCode = "";
                    if (href != null && href.indexOf("code=") != -1) {
                        stockCode = href.substring(href.indexOf("code=") + 5);
                    }
                    String stockName = a.text();
                    System.out.println(stockCode + ":" + stockName);

                    StockVO stock = new StockVO();
                    stock.setStockCode(stockCode);
                    stock.setStockName(stockName);

                    searchList.add(stock);
                }
            }
            themeMarketPrice = themeStockTable.outerHtml();
            themeMarketPrice = themeMarketPrice.replaceAll("<a href=\"/", "<a href=\"http://finance.naver.com/");
        }
        return themeMarketPrice;
    }

    public void writeFile(String strThemeName, String themeMarketPrice) {
        try {
            FileWriter fw = new FileWriter(
                    USER_HOME + "\\documents\\" + strDate + "_업종[" + strThemeName + "]_관련_뉴스.html");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    th {border:1px solid #aaaaaa;background:#00c73c;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td.tar {text-align:right}\r\n");
            sb1.append("    td.red {color:red;}\r\n");
            sb1.append("    td.blue {color:blue;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<h2>" + strYMD + " 업종[" + strThemeName + "] 관련 뉴스</h2>");

            sb1.append(themeMarketPrice + "\r\n");

            StringBuilder commentSb = new StringBuilder();
            for (StockVO s : searchList) {
                if (s != null) {
                    Document classAnalysisDoc = Jsoup.connect(
                            "http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + s.getStockCode())
                            .get();
                    // System.out.println("classAnalysisDoc:"+classAnalysisDoc);
                    Elements comment = classAnalysisDoc.select(".cmp_comment");
                    String strCommentCheck = classAnalysisDoc.select(".cmp_comment .dot_cmp li").text();
                    if (strCommentCheck.equals("해당 자료가 없습니다.")) {
                        continue;
                    }
                    commentSb.append("<div>\n");
                    commentSb.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" + s.getStockCode()
                            + "'>" + s.getStockName() + "(" + s.getStockCode() + ")" + "</a></h4>\n");
                    commentSb.append("<p>\n");
                    commentSb.append(comment + "\n");
                    commentSb.append("</p>");
                    commentSb.append("</div>\n");
                }
            }
            if (!commentSb.toString().equals("")) {
                sb1.append("<table style='width:741px'>\r\n");
                sb1.append("<tr>\r\n");
                sb1.append("<td>\r\n");
                sb1.append(commentSb.toString());
                sb1.append("</td>\r\n");
                sb1.append("</tr>\r\n");
                sb1.append("</table>\r\n");
            }
            // 뉴스 첨부
            sb1.append(getAllNews(searchList).toString());

            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");

            System.out.println(sb1.toString());

            fw.write(sb1.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            System.out.println("file write finished...");
        }
    }

    public StringBuilder getAllNews(List<StockVO> stockList) {
        System.out.println("stockList.size:" + stockList.size());

        StringBuilder sb = new StringBuilder();

        for (StockVO stock : stockList) {
            // 중복 뉴스 체크 로직 구현해야....
            StringBuilder sb1 = StockUtil.getNews(stock);
            sb.append(sb1);
        }
        return sb;
    }


}
