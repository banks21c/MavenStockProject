package html.parsing.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

import html.parsing.stock.DataSort.TradingAmountDescCompare;
import html.parsing.stock.DataSort.TradingVolumeDescCompare;
import html.parsing.stock.DataSort.VaryRatioDescCompare;

public class StockSpac {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockSpac(1);
    }

    StockSpac() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        List<StockVO> kospiStockList = readOne("071970");
        writeFile(kospiStockList, kosdaqFileName, "코스닥");
    }

    StockSpac(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

        // 1.등락율순 정렬
        Collections.sort(kosdaqAllStockList, new VaryRatioDescCompare());

        writeFile(kosdaqAllStockList, kosdaqFileName, "스팩 등락율 ");

        // 2.거래량 정렬
        Collections.sort(kosdaqAllStockList, new TradingVolumeDescCompare());

        writeFile(kosdaqAllStockList, kosdaqFileName, "스팩 거래량");

        // 3.거래대금순 정렬
        Collections.sort(kosdaqAllStockList, new TradingAmountDescCompare());

        writeFile(kosdaqAllStockList, kosdaqFileName, "스팩 거래대금");

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
                cnt++;
                System.out.println(cnt + "." + read);
                stockCode = read.split("\t")[0];
                stockName = read.split("\t")[1];

                if (stockCode.length() != 6) {
                    continue;
                }
                if (stockName.indexOf("스팩") == -1) {
                    continue;
                }
                StockVO stock = getStockInfo(cnt, stockCode, stockName);
                if (stock != null) {
                    stocks.add(stock);
                }
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

            String stockName = "";
            String varyRatio = "";
            String curPrice = "";
            String varyPrice = "";
            String sign = "";
            String specialLetter = "";

            int iCurPrice = 0;
            int iVaryPrice = 0;

            Element meta = doc.select("meta").get(5);
            String metaProperty = meta.attr("property");
            if (metaProperty.equals("og:description")) {
                String metaContent = meta.attr("content");
                metaContent = metaContent.substring(0, metaContent.indexOf("%") + 1);
                System.out.println("metaContent:" + metaContent);
                String[] metaContentArray = metaContent.split(" ");
                int contentCnt = 0;
                for (String s : metaContentArray) {
                    System.out.println("content[" + (contentCnt++) + "]:" + s);
                }
                if (metaContentArray.length > 4) {
                    stockName = metaContentArray[0] + " " + metaContentArray[1];
                    if (metaContentArray[0] == null) {
                        stockName = name;
                    }
                } else {
                    stockName = metaContentArray[0];
                }
                varyRatio = metaContentArray[metaContentArray.length - 1];
                varyPrice = metaContentArray[metaContentArray.length - 2];
                if (varyPrice.indexOf(".") != -1) {
                    varyPrice = varyPrice.substring(0, varyPrice.indexOf("."));
                }
                if (!varyPrice.substring(0, 1).matches("[0-9]")) {
                    specialLetter = varyPrice.substring(0, 1);
                    varyPrice = varyPrice.substring(1);
                    if (varyPrice.equals("0")) {
                        specialLetter = "";
                    }
                }
                if (!varyRatio.substring(0, 1).matches("[0-9]")) {
                    sign = varyRatio.substring(0, 1);
                    stock.setVaryRatio(varyRatio);
                }
                curPrice = metaContentArray[metaContentArray.length - 3];
                System.out.println("sign :" + sign);
                System.out.println("specialLetter :" + specialLetter);
                System.out.println("varyRatio :" + varyRatio);
                System.out.println("curPrice :" + curPrice);
                System.out.println("varyPrice :" + varyPrice);

                stock.setStockName(stockName);
                stock.setSpecialLetter(specialLetter);
                stock.setSign(sign);
                stock.setCurPrice(curPrice);
                stock.setiCurPrice(Integer.parseInt(curPrice.replaceAll(",", "")));
                stock.setVaryPrice(varyPrice);
                stock.setiVaryPrice(Integer.parseInt(varyPrice.replaceAll(",", "")));
                stock.setVaryRatio(varyRatio);

                iCurPrice = Integer.parseInt(StringUtils.defaultIfEmpty(curPrice.replaceAll(",", ""), "0"));
                iVaryPrice = Integer.parseInt(StringUtils.defaultIfEmpty(varyPrice.replaceAll(",", ""), "0"));

                stock.setiCurPrice(iCurPrice);
                stock.setiVaryPrice(iVaryPrice);
                stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
            }

            Elements edds = doc.getElementsByTag("dd");
            System.out.println("dds.size:" + edds.size());
            for (int i = 0; i < edds.size(); i++) {
                Element dd = edds.get(i);
                System.out.println("data:" + dd.text());
                String text = dd.text();
                if (text.startsWith("거래량")) {
                    System.out.println("거래량 :" + text.split(" ")[1]);
                    stock.setTradingVolume(text.split(" ")[1]);
                    stock.setiTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
                }
                if (text.startsWith("거래대금")) {
                    stock.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
                    stock.setlTradingAmount(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getTradingAmount().replaceAll(",", ""), "0")));
                }
                System.out.println("data:" + dd.text());

                if (text.startsWith("고가")) {
                    stock.setHighPrice(text.split(" ")[1]);
                }
                if (text.startsWith("저가")) {
                    stock.setLowPrice(text.split(" ")[1]);
                }
            }

            Elements dds = doc.select("dd");
            for (Element dd : dds) {
                String priceTxt = dd.text();
                System.out.println("priceTxt:" + priceTxt);
                if ((priceTxt.indexOf("가") != -1 || priceTxt.indexOf("량") != -1)
                        && priceTxt.matches("(.*)[0-9]+(.*)")) {
                    // String priceSplit[] = priceTxt.replaceAll(",", "").split(" ");
                    String priceSplit[] = priceTxt.split(" ");
                    System.out.println(priceSplit[0] + "-------------->" + priceSplit[1]);
                    // ht.put(priceSplit[0], priceSplit[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
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
            sb1.append("\t<font size=5>" + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락률</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래량</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>거래대금(백만)</td>\r\n");
            sb1.append("</tr>\r\n");

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
