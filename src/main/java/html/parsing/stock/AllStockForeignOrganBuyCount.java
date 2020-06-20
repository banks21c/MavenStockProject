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

import html.parsing.stock.DataSort.ForeignStraitBuyCountDescCompare;
import html.parsing.stock.DataSort.OrganStraitBuyCountDescCompare;

public class AllStockForeignOrganBuyCount {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(AllStockForeignOrganBuyCount.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new AllStockForeignOrganBuyCount(1);
    }

    AllStockForeignOrganBuyCount() {

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        List<StockVO> kospiStockList = readOne("256840");
        writeFile(kospiStockList, kospiFileName, "코스피", true);
    }

    AllStockForeignOrganBuyCount(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
        System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

        // 1.외국인 연속매수순 정렬
        Collections.sort(kospiAllStockList, new ForeignStraitBuyCountDescCompare());
        Collections.sort(kosdaqAllStockList, new ForeignStraitBuyCountDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 외국인 연속매수일수 ", true);
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 외국인 연속매수일수 ", true);

        // 2.기관 연속매수순 정렬
        Collections.sort(kospiAllStockList, new OrganStraitBuyCountDescCompare());
        Collections.sort(kosdaqAllStockList, new OrganStraitBuyCountDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 기관 연속매수일수 ", false);
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 기관 연속매수일수 ", false);
    }

    public static List<StockVO> readOne(String stockCode) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode, "");
        if (stock != null) {
            stocks.add(stock);
        }
        return stocks;
    }

    public static List<StockVO> getAllStockInfo(String kospidaq, String fileName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            String stockCode = null;
            String stockName = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];
                System.out.println(stockCode + "\t" + stockName);

                if (stockCode.length() != 6) {
                    continue;
                }

                StockVO stock = getStockInfo(cnt, stockCode, stockName);
                if (stock != null) {
                    stock.setStockName(stockName);
                    stocks.add(stock);
                }
                cnt++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
        return stocks;
    }

    public static StockVO getStockInfo(int cnt, String code, String name) {
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            if (cnt == 1) {
                // System.out.println(doc.title());
                // System.out.println(doc.html());
            }
            stock.setStockCode(code);

            Elements dates = doc.select(".date");
            if (dates != null) {
                if (dates.size() > 0) {
                    Element date = dates.get(0);
                    strYMD = date.ownText();
                    strYMD = date.childNode(0).toString().trim();
                    strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
                }
            }
            Element new_totalinfo = doc.select(".new_totalinfo").get(0);
            Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
            Element blind = new_totalinfo_doc.select(".blind").get(0);
            Elements edds = blind.select("dd");

            String specialLetter = "";
            String sign = "";
            String curPrice = "";
            String varyRatio = "";

            int iCurPrice = 0;
            int iVaryPrice = 0;

            for (int i = 0; i < edds.size(); i++) {
                Element dd = edds.get(i);
                String text = dd.text();
                System.out.println("data:" + text);

                if (text.startsWith("현재가")) {
                    System.out.println("data1:" + dd.text());
                    text = text.replaceAll("플러스", "+");
                    text = text.replaceAll("마이너스", "-");
                    text = text.replaceAll("상승", "▲");
                    text = text.replaceAll("하락", "▼");
                    text = text.replaceAll("퍼센트", "%");

                    String txts[] = text.split(" ");
                    curPrice = txts[1];
                    stock.setCurPrice(txts[1]);
                    stock.setiCurPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(stock.getCurPrice(), "0").replaceAll(",", "")));
                    iCurPrice = stock.getiCurPrice();

                    // 특수문자
                    specialLetter = txts[3].replaceAll("보합", "");
                    stock.setSpecialLetter(specialLetter);

                    String varyPrice = txts[4];
                    stock.setVaryPrice(varyPrice);
                    stock.setiVaryPrice(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));
                    iVaryPrice = stock.getiVaryPrice();

                    // +- 부호
                    sign = txts[5];
                    stock.setSign(sign);
                    System.out.println("txts.length:" + txts.length);
                    if (txts.length == 7) {
                        stock.setVaryRatio(txts[5] + txts[6]);
                    } else if (txts.length == 8) {
                        stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
                    }
                    varyRatio = stock.getVaryRatio();
                    stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
                    System.out.println("상승률:" + stock.getVaryRatio());
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
                    stock.setiTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
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

            // ============================================================================
            // 투자자별 매매동향 - 외국인 보유주수, 보유율
            // http://finance.naver.com/item/frgn.nhn?code=102460&page=1
            // http://finance.naver.com/item/frgn.nhn?code=102460&page=2
            int organCount = 0;
            int foreignCount = 0;

            double dOrganVolumeSum = 0;
            double dOrganAmountSum = 0;

            double dForeignVolumeSum = 0;
            double dForeignAmountSum = 0;
            for (int i = 1; i <= 3; i++) {
                doc = Jsoup.connect("http://finance.naver.com/item/frgn.nhn?code=" + code + "&page=" + i).get();
                // System.out.println(doc.html());
                Elements type2Elements = doc.select(".type2");
                Element type2Element = type2Elements.get(1);
                System.out.println("type2:" + type2Element);
                // 외국인 기관 순매매 거래량에 관한표이며 날짜별로 정보를 제공합니다.
                // tr
                Elements trElements = type2Element.select("tr");
                for (Element trElement : trElements) {
                    // td
                    Elements tdElements = trElement.select("td");
                    if (tdElements.size() == 9) {
                        // 종가
                        String endPrice = tdElements.get(1).text();
                        // 전인대비
                        String varyPrice = tdElements.get(2).text();
                        try {
                            endPrice = StringUtils.replace(endPrice, ",", "");
                            varyPrice = StringUtils.replace(varyPrice, ",", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        char plusMinus = ' ';
                        String ratio = tdElements.get(3).text();
                        if (ratio.startsWith("+")) {
                            plusMinus = '+';
                        } else if (ratio.startsWith("-")) {
                            plusMinus = '-';
                        }
                        if (!endPrice.matches("[0-9]*")) {
                            System.out.println("endPrice is not matches number~~~");
                            continue;
                        }
                        System.out.println("endPrice:" + endPrice);
                        System.out.println("varyPrice:" + varyPrice);
                        double iEndPrice = Integer.parseInt(endPrice);
                        double iHalfVaryPrice = Integer.parseInt(varyPrice) / 2;
                        double dAveragePrice = 0;
                        if (plusMinus == '+') {
                            dAveragePrice = iEndPrice - iHalfVaryPrice;
                        } else if (plusMinus == '-') {
                            dAveragePrice = iEndPrice + iHalfVaryPrice;
                        } else {
                            dAveragePrice = iEndPrice;
                        }
                        System.out.println("dAveragePrice:" + dAveragePrice);
                        // 기관순매매량
                        String organVolume = tdElements.get(5).text();
                        double dOrganVolume = 0;
                        try {
                            organVolume = StringUtils.replace(organVolume, ",", "");
                            dOrganVolume = Double.parseDouble(organVolume);
                            System.out.println("organVolume:" + organVolume);
                            System.out.println("dOrganVolume:" + dOrganVolume);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        char organSign = ' ';
                        if (organVolume.startsWith("+") || organVolume.startsWith("-")) {
                            organSign = organVolume.charAt(0);
                        }
                        if (organSign == '+') {
                            dOrganAmountSum += dAveragePrice * dOrganVolume;
                            dOrganVolumeSum += dOrganVolume;
                            organCount++;
                        } else {
                            break;
                        }
                    }
                }
                for (Element trElement : trElements) {
                    // td
                    Elements tdElements = trElement.select("td");
                    if (tdElements.size() == 9) {
                        // 종가
                        String endPrice = tdElements.get(1).text();
                        // 전인대비
                        String varyPrice = tdElements.get(2).text();
                        try {
                            endPrice = StringUtils.replace(endPrice, ",", "");
                            varyPrice = StringUtils.replace(varyPrice, ",", "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        char plusMinus = ' ';
                        String ratio = tdElements.get(3).text();
                        if (ratio.startsWith("+")) {
                            plusMinus = '+';
                        } else if (ratio.startsWith("-")) {
                            plusMinus = '-';
                        }

                        if (!endPrice.matches("[0-9]*")) {
                            System.out.println("endPrice is not matches number~~~");
                            continue;
                        }

                        double iEndPrice = Integer.parseInt(endPrice);
                        double iHalfVaryPrice = Integer.parseInt(varyPrice) / 2;
                        double dAveragePrice = 0;
                        if (plusMinus == '+') {
                            dAveragePrice = iEndPrice - iHalfVaryPrice;
                        } else if (plusMinus == '-') {
                            dAveragePrice = iEndPrice + iHalfVaryPrice;
                        } else {
                            dAveragePrice = iEndPrice;
                        }
                        System.out.println("dAveragePrice:" + dAveragePrice);

                        // 외국인순매매량
                        String foreignVolume = tdElements.get(6).text();
                        double iForeignVolume = 0;
                        try {
                            foreignVolume = StringUtils.replace(foreignVolume, ",", "");
                            iForeignVolume = Integer.parseInt(foreignVolume);
                            System.out.println("foreignVolume:" + foreignVolume);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        char foreignSign = ' ';
                        if (foreignVolume.startsWith("+") || foreignVolume.startsWith("-")) {
                            foreignSign = foreignVolume.charAt(0);
                        }
                        if (foreignSign == '+') {
                            dForeignAmountSum += dAveragePrice * iForeignVolume;
                            dForeignVolumeSum += iForeignVolume;
                            foreignCount++;
                        } else {
                            break;
                        }
                    }
                }
                // System.out.println("organCount:" + organCount);
                // System.out.println("foreignCount:" + foreignCount);
                // System.out.println("dOrganVolumeSum:" + dOrganVolumeSum);
                // System.out.println("dOrganAmountSum:" + dOrganAmountSum);
                // System.out.println("dForeignAmountSum:" + dForeignAmountSum);
                // System.out.println("dForeignVolumeSum:" + dForeignVolumeSum);

                if (organCount == i * 20 || foreignCount == i * 20) {
                    continue;
                } else {
                    break;
                }
            }
            // System.out.println("============");
            // System.out.println("organCount:" + organCount);
            // System.out.println("foreignCount:" + foreignCount);

            DecimalFormat df = new DecimalFormat("#,##0");
            // String organTradingVolume =
            // df.format(String.valueOf((int)dOrganVolumeSum));
            // String organTradingAmount =
            // df.format(String.valueOf((int)(dOrganAmountSum/10000)));
            // String foreignTradingVolume =
            // df.format(String.valueOf((int)dForeignVolumeSum));
            // String foreignTradingAmount =
            // df.format(String.valueOf((int)(dForeignAmountSum/10000)));
            String organTradingVolume = df.format(((int) dOrganVolumeSum));
            String organTradingAmount = df.format(((int) (dOrganAmountSum / 10000)));
            String foreignTradingVolume = df.format(((int) dForeignVolumeSum));
            String foreignTradingAmount = df.format(((int) (dForeignAmountSum / 10000)));

            stock.setOrganStraitBuyCount(organCount);
            stock.setOrganTradingVolume(organTradingVolume);
            stock.setOrganTradingAmount(organTradingAmount);

            stock.setForeignStraitBuyCount(foreignCount);
            stock.setForeignTradingVolume(foreignTradingVolume);
            stock.setForeignTradingAmount(foreignTradingAmount);

            // System.out.println(stock.getOrganTradingVolume());
            // System.out.println(stock.getOrganTradingAmount());
            // System.out.println(stock.getForeignTradingVolume());
            // System.out.println(stock.getForeignTradingAmount());
            if (organCount == 0 && foreignCount == 0) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, boolean isForeign) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<html lang='ko'>\r\n");
            sb1.append("<head>\r\n");
            //sb1.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<font size=5>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
            sb1.append("<td colspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>연속매수일수</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관거래량</td>\r\n");
            sb1.append(
                    "<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관거래금액<br>(만원)</td>\r\n");
            sb1.append("<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인거래량</td>\r\n");
            sb1.append(
                    "<td rowspan='2' style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인거래금액<br>(만원)</td>\r\n");
            sb1.append("</tr>");

            sb1.append("<tr>");
            if (isForeign) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
            } else {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>기관</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>외인</td>\r\n");
            }
            sb1.append("</tr>");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String varyPrice = s.getVaryPrice();
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
                    if (isForeign) {
                        sb1.append("<td style='text-align:right'>" + s.getForeignStraitBuyCount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getOrganStraitBuyCount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignTradingVolume() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignTradingAmount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getOrganTradingVolume() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getOrganTradingAmount() + "</td>\r\n");
                    } else {
                        sb1.append("<td style='text-align:right'>" + s.getOrganStraitBuyCount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignStraitBuyCount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getOrganTradingVolume() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getOrganTradingAmount() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignTradingVolume() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignTradingAmount() + "</td>\r\n");
                    }
                    sb1.append("</tr>\r\n");
                }
            }
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

        }
    }

}
