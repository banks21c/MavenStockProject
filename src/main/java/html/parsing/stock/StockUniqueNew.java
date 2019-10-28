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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.VaryRatioDescCompare;
import java.util.Iterator;
import java.util.logging.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class StockUniqueNew extends Thread {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger1 = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    DecimalFormat df = new DecimalFormat("###.##");

    static String strYMD = "";

    String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
    int iHms = Integer.parseInt(strHms);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
    String strDefaultDate = sdf.format(new Date());
    // String strYmdDash = new SimpleDateFormat("yyyy년 M월 d일 E",Locale.KOREAN).format(new Date());
    int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
    String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
    String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

    String strDate = "";
    String strStockCodeOrName = "롯데케미칼";

    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;

    String strStockCode = "011170";
    String strStockName = "롯데케미칼";

    static int topCount = 0;
    static int upCount = 0;
    static int bottomCount = 0;
    static int downCount = 0;
    static int steadyCount = 0;
    
	List<StockVO> kospiStockList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

    List<StockVO> allStockList = new ArrayList<StockVO>();
    List<StockVO> topStockList = new ArrayList<StockVO>();
    List<StockVO> bottomStockList = new ArrayList<StockVO>();
    List<StockVO> topTouchStockList = new ArrayList<StockVO>();
    List<StockVO> bottomTouchStockList = new ArrayList<StockVO>();
    List<StockVO> upDownStockList = new ArrayList<StockVO>();
    List<StockVO> downUpStockList = new ArrayList<StockVO>();
    List<StockVO> over5PerUpStockList = new ArrayList<StockVO>();
    List<StockVO> over5PerDownStockList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockUniqueNew(1).start();
    }

    StockUniqueNew() {
    }

    StockUniqueNew(int i) {
        Class thisClass = this.getClass();
        logger1 = LoggerFactory.getLogger(thisClass);

        readOne("214310", "세미콘라이트");
        listSortAndAdd();
        writeFile(allStockList, kosdaqFileName, "코스닥");

    }

    void clearList() {
        allStockList.clear();
        topStockList.clear();
        bottomStockList.clear();
        topTouchStockList.clear();
        bottomTouchStockList.clear();
        upDownStockList.clear();
        downUpStockList.clear();
        over5PerUpStockList.clear();
        over5PerDownStockList.clear();
    }

    void listSortAndAdd() {
        Collections.sort(topStockList, new VaryRatioDescCompare());
        Collections.sort(bottomStockList, new VaryRatioDescCompare());
        Collections.sort(topTouchStockList, new VaryRatioDescCompare());
        Collections.sort(bottomTouchStockList, new VaryRatioDescCompare());
        Collections.sort(upDownStockList, new VaryRatioDescCompare());
        Collections.sort(downUpStockList, new VaryRatioDescCompare());
        Collections.sort(over5PerUpStockList, new VaryRatioDescCompare());
        Collections.sort(over5PerDownStockList, new VaryRatioDescCompare());

        allStockList.addAll(topStockList);
        allStockList.addAll(bottomStockList);
        allStockList.addAll(topTouchStockList);
        allStockList.addAll(bottomTouchStockList);
        allStockList.addAll(upDownStockList);
        allStockList.addAll(downUpStockList);
        allStockList.addAll(over5PerUpStockList);
        allStockList.addAll(over5PerDownStockList);
    }

    public void run() {
        execute();
    }

    public void execute() {
        try {
            Class thisClass = this.getClass();
            logger1 = LoggerFactory.getLogger(thisClass);

            readExcelFile("코스피", kospiFileName);
            listSortAndAdd();
            writeFile(allStockList, kospiFileName, "코스피 특징종목");

//            clearList();

//            readExcelFile("코스닥", kosdaqFileName);
//            listSortAndAdd();
//            writeFile(allStockList, kosdaqFileName, "코스닥 특징종목");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(StockUniqueNew.class.getName()).log(Level.SEVERE, null, ex);
            
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			for(int i=0;i<kospiStockList.size();i++) {
				StockVO svo = kospiStockList.get(i);
				String strStockCode = svo.getStockCode(); 
				String strStockName = svo.getStockName(); 
				getStockInfo(i+1, strStockCode, strStockName);
			}
            listSortAndAdd();
            writeFile(allStockList, kospiFileName, "코스피 특징종목");

            clearList();

			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			for(int i=0;i<kosdaqStockList.size();i++) {
				StockVO svo = kosdaqStockList.get(i);
				String strStockCode = svo.getStockCode(); 
				String strStockName = svo.getStockName(); 
				getStockInfo(i+1, strStockCode, strStockName);
			}			
            listSortAndAdd();
            writeFile(allStockList, kosdaqFileName, "코스닥 특징종목");            
        }
    }

    public void readOne(String stockCode, String stockName) {
        int cnt = 1;
        strStockCode = stockCode;
        strStockName = stockName;
        getStockInfo(cnt, strStockCode, strStockName);
    }

    public void readFile(String kospidaq, String fileName) {

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"));

            String read = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                strStockCode = read.split("\t")[0];
                strStockName = read.split("\t")[1];
                System.out.println(strStockCode + "\t" + strStockName);

                if (strStockCode.length() != 6) {
                    continue;
                }
                getStockInfo(cnt, strStockCode, strStockName);
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

    public void readExcelFile(String kospidaq, String fileName) throws IOException, InvalidFormatException {
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        Workbook workbook = WorkbookFactory.create(new File(fileName));
        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        // 1. You can obtain a rowIterator and columnIterator and iterate over them
        System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
        Iterator<Row> rowIterator = sheet.rowIterator();
        int cnt = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getLastCellNum() > 1) {
                int i = 0;
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
                if (strStockCode.length() != 6) {
                    continue;
                }
                System.out.println(strStockCode + "\t" + strStockName);
            }
            getStockInfo(cnt, strStockCode, strStockName);
            cnt++;
        }
        // Closing the workbook
        workbook.close();
    }

    public StockVO getStockInfo(int cnt, String strStockCode, String strStockName) {
        Document doc;
        StockVO stock = new StockVO();
        stock.setStockCode(strStockCode);
        stock.setStockName(strStockName);
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + strStockCode).get();
            // System.out.println("doc:"+doc);

            Elements dates = doc.select(".date");
            if (dates != null) {
                if (dates.size() > 0) {
                    Element date = dates.get(0);
                    strYmdDash = date.ownText();
                    strYmdDash = date.childNode(0).toString().trim();

                    String strYmd4Int = strYmdDash.replaceAll("\\.", "");
                    if (strYmd4Int.length() > 8) {
                        strYmd4Int = strYmd4Int.substring(0, 8);
                    }
                    iYmd = Integer.parseInt(strYmd4Int);

                    strYmdDash = strYmdDash.replaceAll("\\.", "-");
                    strYmdDash = strYmdDash.replaceAll(":", "-");
                    strYmdDashBracket = "[" + strYmdDash + "]";
                }
            }
            logger1.debug("iYmd:[" + iYmd + "]");
            logger1.debug("strYmdDash:[" + strYmdDash + "]");

            // Element tradeVolumeText =
            // doc.select(".sp_txt9").get(0);
            String tradeVolumeText = doc.select(".spot .rate_info .sp_txt9").get(0).parent().child(1).select("span").get(0).text();
            if (tradeVolumeText.equals("0")) {
                return stock;
            }
            System.out.println("tradeVolumeText:" + tradeVolumeText);

            Element new_totalinfo = doc.select(".new_totalinfo").get(0);
            Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
            Element blind = new_totalinfo_doc.select(".blind").get(0);
            Elements edds = blind.select("dd");

            String specialLetter = "";
            String sign = "";
            String curPrice = "";
            String varyPrice = "";
            String varyRatio = "";

            int iCurPrice = 0;
            int iVaryPrice = 0;

            for (int i = 0; i < edds.size(); i++) {
                Element dd = edds.get(i);
                String text = dd.text();
                //System.out.println("text:" + text);
                if (text.startsWith("종목명")) {
                    String stockName = text.substring(4);
                    //System.out.println("stockName:" + stockName);
                    stock.setStockName(stockName);
                }

                if (text.startsWith("현재가")) {
                    //System.out.println("data1:" + dd.text());
                    text = text.replaceAll("플러스", "+");
                    text = text.replaceAll("마이너스", "-");
                    text = text.replaceAll("상승", "▲");
                    text = text.replaceAll("하락", "▼");
                    text = text.replaceAll("퍼센트", "%");

                    String txts[] = text.split(" ");
                    curPrice = txts[1];
                    stock.setCurPrice(curPrice);
                    stock.setiCurPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(stock.getCurPrice(), "0").replaceAll(",", "")));
                    iCurPrice = stock.getiCurPrice();

                    // 특수문자
                    specialLetter = txts[3].replaceAll("보합", "");
                    stock.setSpecialLetter(specialLetter);

                    varyPrice = txts[4];
                    stock.setVaryPrice(varyPrice);
                    stock.setiVaryPrice(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));
                    iVaryPrice = stock.getiVaryPrice();

                    // +- 부호
                    sign = txts[5];
                    stock.setSign(sign);
                    //System.out.println("txts.length:" + txts.length);
                    if (txts.length == 7) {
                        stock.setVaryRatio(txts[5] + txts[6]);
                    } else if (txts.length == 8) {
                        stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
                    }
                    varyRatio = stock.getVaryRatio();
                    stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
                    //System.out.println("상승률:" + stock.getVaryRatio());
                }

                if (text.startsWith("전일가")) {
                    stock.setBeforePrice(text.split(" ")[1]);
                    stock.setiBeforePrice(Integer.parseInt(stock.getBeforePrice().replaceAll(",", "")));
                }
                if (text.startsWith("시가")) {
                    stock.setStartPrice(text.split(" ")[1]);
                    stock.setiStartPrice(Integer.parseInt(stock.getStartPrice().replaceAll(",", "")));
                }
                if (text.startsWith("고가")) {
                    stock.setHighPrice(text.split(" ")[1]);
                    stock.setiHighPrice(Integer.parseInt(stock.getHighPrice().replaceAll(",", "")));
                }
                if (text.startsWith("상한가")) {
                    stock.setMaxPrice(text.split(" ")[1]);
                    stock.setiMaxPrice(Integer.parseInt(stock.getMaxPrice().replaceAll(",", "")));
                }
                if (text.startsWith("저가")) {
                    stock.setLowPrice(text.split(" ")[1]);
                    stock.setiLowPrice(Integer.parseInt(stock.getLowPrice().replaceAll(",", "")));
                }
                if (text.startsWith("하한가")) {
                    stock.setMinPrice(text.split(" ")[1]);
                    stock.setiMinPrice(Integer.parseInt(stock.getMinPrice().replaceAll(",", "")));
                }
                if (text.startsWith("거래량")) {
                    stock.setTradingVolume(text.split(" ")[1]);
                    stock.setlTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
                }
                if (text.startsWith("거래대금") || text.startsWith("거래금액")) {
                    stock.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
                    stock.setlTradingAmount(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getTradingAmount().replaceAll(",", ""), "0")));
                }
            }

            String upDown = doc.select(".no_exday").get(0).select("em span").get(0).text();
            if (upDown.equals("상한가")) {
                specialLetter = "↑";
            } else if (upDown.equals("하한가")) {
                specialLetter = "↓";
            }
            stock.setSpecialLetter(specialLetter);

            if (specialLetter.equals("↑")) {
                stock.setStockGubun("상한가↑");
                stock.setLineUp(11);

                topStockList.add(stock);
                return stock;
            }
            if (specialLetter.equals("↓")) {
                stock.setStockGubun("하한가↓");
                stock.setLineUp(21);
                bottomStockList.add(stock);
                return stock;
            }

            String highPrice = stock.getHighPrice();
            String lowPrice = stock.getLowPrice();
            String maxPrice = stock.getMaxPrice();
            System.out.println("highPrice:" + highPrice);
            System.out.println("lowPrice:" + lowPrice);
            System.out.println("maxPrice:" + maxPrice);
            // 고가가 0이 아니고 고가가 상한가인가?
            System.out.println(!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice()));
            // 현재가가 상한가가 아닌가?
            System.out.println(!curPrice.equals(stock.getMaxPrice()));
            // 고가가 상한가인가?
            System.out.println(highPrice.equals(stock.getMaxPrice()));
            // 고가가 0이 아니고 고가가 상한가인가?
            if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
                    && !curPrice.equals(stock.getMaxPrice())) {
                stock.setStockGubun("상터치↑↘");
                stock.setLineUp(12);
                topTouchStockList.add(stock);
                return stock;
            }
            if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
                    && !curPrice.equals(stock.getMinPrice())) {
                stock.setStockGubun("하터치↓↗");
                stock.setLineUp(22);
                bottomTouchStockList.add(stock);
                return stock;
            }

            // 현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
            float higher = 0;
            String flag = "";
            int icur = stock.getiCurPrice();
            int ihigh = stock.getiHighPrice();
            int ilow = stock.getiLowPrice();

            long lTradingVolume = stock.getlTradingVolume();
            if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
                higher = Math.abs(icur - ihigh);
                flag = "↗↘";
                System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
                float upDownRatio = higher / icur * 100;
                // upDownRatio = ((int)(upDownRatio * 100))/100f;
                String strUpDownRatio = df.format(upDownRatio);
                if (higher / icur * 100 > 10 && lTradingVolume > 0) {
                    stock.setStockGubun(strUpDownRatio + "%" + flag);
                    stock.setLineUp(16);
                    upDownStockList.add(stock);
                    return stock;
                }
            } else {
                higher = Math.abs(icur - ilow);
                flag = "↘↗";
                System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
                float upDownRatio = higher / icur * 100;
                // upDownRatio = ((int)(upDownRatio * 100))/100f;
                String strUpDownRatio = df.format(upDownRatio);
                if (upDownRatio > 10 && lTradingVolume > 0) {
                    stock.setStockGubun(strUpDownRatio + "%" + flag);
                    stock.setLineUp(16);
                    downUpStockList.add(stock);
                    return stock;
                }
            }

            float fRatio = 0f;
            if (varyRatio.indexOf("%") != -1) {
                fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
                if (fRatio >= 5) {
                    if (specialLetter.equals("+") || specialLetter.equals("▲")) {
                        stock.setStockGubun("+5%이상↗");
                        stock.setLineUp(13);
                        over5PerUpStockList.add(stock);
                    } else if (specialLetter.equals("-") || specialLetter.equals("▼")) {
                        stock.setStockGubun("-5%이상↘");
                        stock.setLineUp(23);
                        over5PerDownStockList.add(stock);
                    }
                    return stock;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
        try {
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<h2>" + strYmdDashBracket + " " + title + "</h2>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>구분</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락률</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");
            sb1.append("</tr>\r\n");

            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + s.getStockGubun() + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String varyPrice = s.getVaryPrice();

                    System.out.println("specialLetter+++>" + specialLetter);
                    System.out.println("varyPrice+++>" + varyPrice);

                    if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
                            || specialLetter.startsWith("+")) {
                        sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
                                + "</font></td>\r\n");
                    } else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
                            || specialLetter.startsWith("-")) {
                        sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
                                + "</font></td>\r\n");
                    } else {
                        sb1.append("<td style='text-align:right'>0</td>\r\n");
                    }

                    String varyRatio = StringUtils.defaultIfEmpty(s.getVaryRatio(), "");
                    if (varyRatio.startsWith("+")) {
                        sb1.append("<td style='text-align:right'><font color='red'>" + varyRatio + "</font></td>\r\n");
                    } else if (varyRatio.startsWith("-")) {
                        sb1.append("<td style='text-align:right'><font color='blue'>" + varyRatio + "</font></td>\r\n");
                    } else {
                        sb1.append(
                                "<td style='text-align:right'><font color='black'>" + varyRatio + "</font></td>\r\n");
                    }
                    sb1.append("<td style='text-align:right'>" + s.getTradingVolume() + "</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getTradingAmount() + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</table>\r\n");
            sb1.append("<br><br>\r\n");

            for (StockVO s : list) {
                if (s != null) {
                    Document classAnalysisDoc = Jsoup.connect(
                            "http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + s.getStockCode())
                            .get();
                    // System.out.println("classAnalysisDoc:"+classAnalysisDoc);
                    Elements comment = classAnalysisDoc.select(".cmp_comment");
                    sb1.append("<div>\n");
                    sb1.append("<h4><a href='http://finance.naver.com/item/main.nhn?code=" + s.getStockCode() + "'>"
                            + s.getStockName() + "(" + s.getStockCode() + ")" + "</a></h4>\n");
                    sb1.append("<p>\n");
                    sb1.append(comment + "\n");
                    sb1.append("</p>");
                    sb1.append("</div>\n");
                    sb1.append("<br>\n");
                }
            }

            // 뉴스 첨부
            sb1.append(getNews(list).toString());

            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            System.out.println(sb1.toString());
            System.out.println("fileName==>" + userHome + "\\documents\\" + strYmdDashBracket + " " + strHms + "_"
                    + title.replaceAll(" ", "_") + ".html");
            fileName = userHome + "\\documents\\" + strYmdDashBracket + " " + strHms + "_" + title.replaceAll(" ", "_") + ".html";
            FileUtil.fileWrite(fileName, sb1.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    public StringBuilder getNews(List<StockVO> allStockList) {

        StringBuilder sb1 = new StringBuilder();

        for (StockVO vo : allStockList) {
            strStockCode = vo.getStockCode();
            strStockName = vo.getStockName();

            // 종합정보
            System.out.println("strStockCode:" + strStockCode + " strStockName:" + strStockName);
            System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=");

            Document doc;
            try {
                // http://finance.naver.com/item/news_news.nhn?code=110570
                doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=").get();
                // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
                // System.out.println(doc.html());
                Elements types = doc.select(".type5");
                Element type = null;
                if (types.size() <= 0) {
                    return sb1;
                }
                type = doc.select(".type5").get(0);

                Elements trs = type.getElementsByTag("tr");
                if (trs != null) {
                    System.out.println("trs.size:" + trs.size());
                }

                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);

                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() < 3) {
                        continue;
                    }

                    Element a1 = tr.getElementsByTag("a").first();
                    Element source = tr.getElementsByClass("info").first();
                    Element dayTime = tr.getElementsByClass("date").first();

                    if (a1 == null) {
                        continue;
                    }
                    System.out.println("a:" + a1);
                    System.out.println("source:" + source);
                    System.out.println("dayTime:" + dayTime);
                    System.out.println("title:" + a1.html());
                    System.out.println("href:" + a1.attr("href"));
                    System.out.println("source:" + source.html());
                    System.out.println("dayTime:" + dayTime.html());

                    String strTitle = a1.html();
                    String strLink = a1.attr("href");
                    String strSource = source.html();
                    String strDayTime = dayTime.html();
                    String strYmd2 = strDayTime.substring(0, 10);
                    int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

                    // sb1.append("<h3>"+ strTitle +"</h3>\n");
                    // sb1.append("<div>"+ strSource+" | "+ strDayTime
                    // +"</div>\n");
                    if (iYmd <= iYmd2) {
                        sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "(" + strStockCode + ")</a></h3>");
                        // sb1.append("<h3>"+ strTitle +"</h3>\n");
                        // sb1.append("<div>"+ strSource+" | "+ strDayTime
                        // +"</div>\n");

                        doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                        Elements link_news_elements = doc.select(".link_news");
                        if (link_news_elements != null) {
                            link_news_elements.remove();
                        }
                        Elements naver_splugin = doc.select(".naver-splugin");
                        System.out.println("naver_splugin:" + naver_splugin);
                        if (naver_splugin != null) {
                            naver_splugin.remove();
                        }
                        doc.select("a").remove();
                        doc.select("li").remove();

                        Element view = doc.select(".view").get(0);

                        String strView = view.toString();
                        strView = strView.replaceAll(strStockName,
                                "<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "</a>");

                        sb1.append(strView);
                        sb1.append("\n");

                        System.out.println("view:" + view);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb1;
    }

    public void readNews(List<StockVO> allStockList) {

        int cnt = 1;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\NewsTest." + strDate + ".html");
            StringBuilder sb1 = new StringBuilder();

            for (StockVO vo : allStockList) {
                strStockCode = vo.getStockCode();
                strStockName = vo.getStockName();

                System.out.println(cnt + "." + strStockCode + "." + strStockName);

                // 종합정보
                // http://finance.naver.com/item/news.nhn?code=246690
                System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=");

                Document doc = Jsoup
                        .connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&amp;page=")
                        .get();
                // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
                doc.select("script").remove();
                Element e1 = doc.select(".type5").get(0);

                Elements trs = e1.getElementsByTag("tr");

                for (int i = 0; i < trs.size(); i++) {
                    Element tr = trs.get(i);

                    Elements tds = tr.getElementsByTag("td");
                    if (tds.size() < 3) {
                        continue;
                    }

                    Element a1 = tr.getElementsByTag("a").first();
                    Element source = tr.getElementsByTag("td").get(2);
                    Element dayTime = tr.getElementsByTag("span").first();

                    System.out.println("title:" + a1.html());
                    System.out.println("href:" + a1.attr("href"));
                    System.out.println("source:" + source.html());
                    System.out.println("dayTime:" + dayTime.html());

                    String strTitle = a1.html();
                    String strLink = a1.attr("href");
                    String strSource = source.html();
                    String strDayTime = dayTime.html();
                    String strYmd2 = strDayTime.substring(0, 10);
                    int iYmd2 = Integer.parseInt(strYmd2.replaceAll("\\.", ""));

                    // sb1.append("<h3>"+ strTitle +"</h3>\n");
                    // sb1.append("<div>"+ strSource+" | "+ strDayTime
                    // +"</div>\n");
                    if (iYmd <= iYmd2) {
                        // sb1.append("<h3>"+ strTitle +"</h3>\n");
                        // sb1.append("<div>"+ strSource+" | "+ strDayTime
                        // +"</div>\n");
                        sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "(" + strStockCode + ")" + "</a></h3>\n");

                        doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                        Elements link_news_elements = doc.select(".link_news");
                        if (link_news_elements != null) {
                            link_news_elements.remove();
                        }
                        Elements naver_splugin = doc.select(".naver-splugin");
                        System.out.println("naver_splugin:" + naver_splugin);
                        if (naver_splugin != null) {
                            naver_splugin.remove();
                        }
                        Element view = doc.select(".view").get(0);

                        String strView = view.toString();
                        strView = strView.replaceAll(strStockName,
                                "<a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "</a>");

                        sb1.append(strView);
                        sb1.append("<br><br>\n");

                        System.out.println("view:" + view);
                    }
                }
            }

            System.out.println(sb1.toString());

            fw.write(sb1.toString());
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
