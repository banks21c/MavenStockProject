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

import html.parsing.stock.DataSort.ForeignHaveAmountDescCompare;
import html.parsing.stock.DataSort.ForeignHaveRatioDescCompare;

public class AllStockForeignRetainRatio {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(AllStockForeignRetainRatio.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new AllStockForeignRetainRatio(1);
    }

    AllStockForeignRetainRatio() {

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // KSS해운
        List<StockVO> kospiStockList = readOne("044450", "");
        writeFile(kospiStockList, kospiFileName, "코스피", "ratio");
        // cj e&m
        List<StockVO> kosdaqStockList = readOne("130960", "");
        writeFile(kosdaqStockList, kospiFileName, "코스닥", "ratio");
    }

    AllStockForeignRetainRatio(int i) {

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

        // 외국인 보유율순 정렬
        Collections.sort(kospiAllStockList, new ForeignHaveRatioDescCompare());
        Collections.sort(kosdaqAllStockList, new ForeignHaveRatioDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 외국인 보유율 ", "ratio");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 외국인 보유율 ", "ratio");

        // 외국인 보유금액순 정렬
        Collections.sort(kospiAllStockList, new ForeignHaveAmountDescCompare());
        Collections.sort(kosdaqAllStockList, new ForeignHaveAmountDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 외국인 보유금액순 ", "amount");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 외국인 보유금액순 ", "amount");

        // 외국인 보유율순 정렬, 보유금액 포함
        Collections.sort(kospiAllStockList, new ForeignHaveRatioDescCompare());
        Collections.sort(kosdaqAllStockList, new ForeignHaveRatioDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 외국인 보유율,보유금액순 ", "all");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 외국인 보유율,보유금액순 ", "all");
    }

    public static List<StockVO> readOne(String stockCode, String stockName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode);
        if (stock != null) {
            stock.setStockName(stockName);
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

                if (stockCode.length() != 6) {
                    continue;
                }
                StockVO stock = getStockInfo(cnt, stockCode);
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

    public static StockVO getStockInfo(int cnt, String code) {
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
            ;

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
            // 투자자별 매매동향 - 외국인 보유주수, 보유율
            doc = Jsoup.connect("http://finance.naver.com/item/frgn.nhn?code=" + code).get();
            // System.out.println(doc.html());

            String foreignHaveVolume = doc.select("table.type2").get(1).select("tr").get(3).select("td").get(7).text();
            String foreignHaveRatio = doc.select("table.type2").get(1).select("tr").get(3).select("td").get(8).text()
                    .replace("%", "");

            System.out.println("foreignHaveVolume:" + foreignHaveVolume);
            System.out.println("foreignHaveRatio:" + foreignHaveRatio);

            long lForeignHaveVolume = Long.parseLong(foreignHaveVolume.replaceAll(",", ""));
            float fForeignHaveRatio = Float.parseFloat(foreignHaveRatio);

            long lForeignHaveAmount = lForeignHaveVolume * iCurPrice / 1000000;

            DecimalFormat df = new DecimalFormat("#,##0");
            String foreignHaveAmount = df.format(lForeignHaveAmount);

            stock.setForeignHaveVolume(foreignHaveVolume);
            stock.setForeignHaveAmount(foreignHaveAmount);
            stock.setForeignHaveRatio(foreignHaveRatio);
            stock.setfForeignHaveRatio(lForeignHaveVolume);
            stock.setlForeignHaveAmount(lForeignHaveAmount);
            stock.setfForeignHaveRatio(fForeignHaveRatio);
            if (lForeignHaveVolume != 0) {
                return stock;
            }
        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException:" + e.getMessage());
        }
        return null;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
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
            sb1.append("\t<h2>" + strYMD + title + "</h2>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현재가</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>전일대비</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>등락률</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유주수</td>\r\n");
            if (gubun.equals("ratio")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
            } else if (gubun.equals("amount")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유금액(백만)</td>\r\n");
            } else if (gubun.equals("all")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유율</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>보유금액(백만)</td>\r\n");
            }
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");

                    sb1.append("<td style='text-align:right'>" + s.getForeignHaveVolume() + "</td>\r\n");
                    if (gubun.equals("ratio")) {
                        sb1.append("<td style='text-align:right'>" + s.getForeignHaveRatio() + "%</td>\r\n");
                    } else if (gubun.equals("amount")) {
                        sb1.append("<td style='text-align:right'>" + s.getForeignHaveAmount() + "</td>\r\n");
                    } else if (gubun.equals("all")) {
                        sb1.append("<td style='text-align:right'>" + s.getForeignHaveRatio() + "%</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getForeignHaveAmount() + "</td>\r\n");
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
