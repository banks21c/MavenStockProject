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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import html.parsing.stock.DataSort.RetainAmountDescCompare;
import html.parsing.stock.DataSort.RetainRatioDescCompare;

public class MajorStockHolder {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ", Locale.KOREAN).format(new Date());

    /**
     * @param args
     */
    public static void main(String[] args) {
        new MajorStockHolder(1);
    }

    MajorStockHolder() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // S-Oil우
        List<StockVO> kospiStockList = readOne("010955");
        writeFile(kospiStockList, kospiFileName, "코스피");
        // cj e&m
        List<StockVO> kosdaqStockList = readOne("130960");
        writeFile(kosdaqStockList, kospiFileName, "코스닥");
    }

    MajorStockHolder(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        MakeKospiKosdaqListThread.makeKospiKosdaqList();

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
        System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

        Collections.sort(kospiAllStockList, new RetainRatioDescCompare());
        Collections.sort(kosdaqAllStockList, new RetainRatioDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 국민연금 보유율순");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 국민연금 보유율순");

        Collections.sort(kospiAllStockList, new RetainAmountDescCompare());
        Collections.sort(kosdaqAllStockList, new RetainAmountDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 국민연금 보유금액순");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 국민연금 보유금액순");

    }

    public static List<StockVO> readOne(String stockCode) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockHompage(cnt, stockCode, "");
        if (stock != null) {
            stocks.add(stock);
        }
        return stocks;
    }

    static long totalAmount = 0;

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

                StockVO stock = getStockHompage(cnt, stockCode, stockName);
                if (stock != null) {
                    stocks.add(stock);
                    totalAmount += stock.getlRetainAmount();
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

    // 종목분석-기업현황
    // http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
    // 종목분석-기업개요
    // http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
    public static StockVO getStockHompage(int cnt, String code, String name) {
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            if (cnt == 1) {
                System.out.println(doc.title());
                System.out.println(doc.html());
            }
            stock.setStockCode(code);

            String stockName = "";
            String curPrice = "";
            int iCurPrice = 0;

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
                System.out.println("stockName:" + stockName);

                curPrice = metaContentArray[metaContentArray.length - 3];
                iCurPrice = Integer.parseInt(curPrice.replaceAll(",", ""));

                stock.setStockName(stockName);
                stock.setCurPrice(curPrice);
                stock.setiCurPrice(iCurPrice);

            }

            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();
            System.out.println("title:" + doc.title());
            if (cnt == 1) {
                System.out.println(doc.html());
            }

            stock.setStockCode(code);

            System.out.println("stockName:" + stockName);
            Elements aClass = doc.select(".gHead01");
            Element aElem = null;
            if (aClass.size() <= 0) {
                return null;
            }
            if (aClass.size() > 1) {
                aElem = aClass.get(1);
            }
            if (aElem != null) {
                Elements tdElem = aElem.select("td");
                Element trElem = null;
                String retainVolume = "";
                String retainRatio = "";
                long lRetainVolume = 0;
                float fRetainRatio = 0;

                String retainAmount = "";
                long lRetainAmount = 0;
                for (Element td : tdElem) {
                    String title = td.attr("title");
                    if (title.equals("국민연금")) {
                        trElem = td.parent();
                        System.out.println("trElem:" + trElem);

                        Elements kookmin = trElem.select("td");
                        for (int i = 0; i < kookmin.size(); i++) {
                            if (i == 1) {
                                retainVolume = kookmin.get(1).text();
                                retainVolume = retainVolume.substring(0, retainVolume.length() - 1);
                                lRetainVolume = Long.parseLong(retainVolume.replaceAll(",", ""));
                                lRetainAmount = lRetainVolume * iCurPrice / 1000000;

                                DecimalFormat df = new DecimalFormat("#,##0");
                                retainAmount = df.format(lRetainAmount);
                            }
                            if (i == 2) {
                                retainRatio = kookmin.get(2).text();
                                retainRatio = retainRatio.substring(0, retainRatio.length() - 1);
                                fRetainRatio = Float.parseFloat(retainRatio);
                            }
                        }
                        System.out.println("curPrice:" + curPrice);
                        System.out.println("retainVolume:" + retainVolume);
                        System.out.println("retainRatio:" + retainRatio);
                        System.out.println("fRetainRatio:" + fRetainRatio);
                        System.out.println("retainAmount:" + retainAmount);
                        System.out.println("lRetainAmount:" + lRetainAmount);
                        stock.setRetainVolume(retainVolume);
                        stock.setRetainRatio(retainRatio);
                        stock.setfRetainRatio(fRetainRatio);
                        stock.setRetainAmount(retainAmount);
                        stock.setlRetainAmount(lRetainAmount);
                        return stock;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + title + ".html");
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<html>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<font size=5>" + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목코드</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유율</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append(
                    "<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {

                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    // sb1.append("<td
                    // style='text-align:center'>"+s.getStockCode()+"</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getRetainVolume() + "</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getRetainRatio() + "</td>\r\n");
                    // sb1.append("<td
                    // style='text-align:right'>"+s.getCurPrice()+"</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getRetainAmount() + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            fw.write(sb1.toString());
            System.out.println(sb1.toString());
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }
    }

}
