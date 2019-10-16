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

import html.parsing.stock.DataSort.GapRatioDescCompare;
import html.parsing.stock.DataSort.ScoreDescCompare;

public class InvestmentOpinion {

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
        new InvestmentOpinion(1);
    }

    InvestmentOpinion() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        String kospiFileName = "new_kospi_우선주제외.html";
        String kosdaqFileName = "new_kosdaq_우선주제외.html";

        List<StockVO> kospiStockList = readOne("185490");
        writeFile(kospiStockList, kospiFileName, "코스피");
    }

    InvestmentOpinion(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kospiFileName = "new_kospi_우선주제외.html";
        String kosdaqFileName = "new_kosdaq_우선주제외.html";

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);

        Collections.sort(kospiAllStockList, new ScoreDescCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 투자의견 점수순");

        Collections.sort(kospiAllStockList, new GapRatioDescCompare());
        writeFile(kospiAllStockList, kospiFileName, "코스피 투자의견 괴리율순");

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);

        Collections.sort(kosdaqAllStockList, new ScoreDescCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 투자의견 점수순");

        Collections.sort(kosdaqAllStockList, new GapRatioDescCompare());
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 투자의견 괴리율순");

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
                // System.out.println(doc.title());
                // System.out.println(doc.html());
            }
            stock.setStockCode(code);
            stock.setStockName(name);

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

            String curPrice = "";
            int iCurPrice = 0;

            for (int i = 0; i < edds.size(); i++) {
                Element dd = edds.get(i);
                String text = dd.text();

                if (text.startsWith("현재가")) {
                    // System.out.println("data:" + text);
                    text = text.replaceAll("플러스", "+");
                    text = text.replaceAll("마이너스", "-");
                    text = text.replaceAll("상승", "▲");
                    text = text.replaceAll("하락", "▼");
                    text = text.replaceAll("퍼센트", "%");

                    String txts[] = text.split(" ");
                    curPrice = txts[1];
                    iCurPrice = Integer.parseInt(StringUtils.defaultIfEmpty(curPrice, "0").replaceAll(",", ""));
                    stock.setCurPrice(curPrice);
                    stock.setiCurPrice(iCurPrice);
                    iCurPrice = stock.getiCurPrice();
                }
            }

            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();
            System.out.println("doc:" + doc);

            Element ctb15 = doc.getElementById("cTB15");
            System.out.println("ctb15:" + ctb15);
            // 데이터
            Element ctb15tr2 = ctb15.select("tr").get(1);
            Elements td = ctb15tr2.select("td");
            if (td.size() == 5) {
                int idx = 0;
                for (Element data : td) {
                    String dataTxt = data.text();
                    if (!dataTxt.matches("[0-9]+.*")) {
                        dataTxt = "";
                    }
                    System.out.println("dataTxt:" + dataTxt);
                    if (idx == 0) {
                        if (dataTxt.equals("")) {
                            break;
                        }
                        stock.setStrScore(dataTxt);
                        stock.setfScore(Float.parseFloat(StringUtils.defaultIfEmpty(dataTxt, "0")));
                    } else if (idx == 1) {
                        stock.setStrTargetPrice(dataTxt);
                        stock.setiTargetPrice(
                                Integer.parseInt(StringUtils.defaultIfEmpty(dataTxt.replaceAll(",", ""), "0")));
                    } else if (idx == 2) {
                        stock.setStrEps(dataTxt);
                        stock.setiEps(Integer.parseInt(StringUtils.defaultIfEmpty(dataTxt.replaceAll(",", ""), "0")));
                    } else if (idx == 3) {
                        stock.setStrPer(dataTxt);
                        stock.setfPer(Float.parseFloat(StringUtils.defaultIfEmpty(dataTxt, "0")));
                    } else if (idx == 4) {
                        stock.setStrEstimateCount(dataTxt);
                        stock.setiEstimateCount(Integer.parseInt(StringUtils.defaultIfEmpty(dataTxt, "0")));
                    }
                    idx++;
                }
                if (stock.getStrScore() == null) {
                    return null;
                }
                if (stock.getiTargetPrice() != 0) {
                    int iGapPrice = stock.getiTargetPrice() - iCurPrice;
                    DecimalFormat df = new DecimalFormat("#,##0");
                    String strGapPrice = df.format(iGapPrice);
                    System.out.println("iGapPrice:" + iGapPrice);
                    System.out.println("strGapPrice:" + strGapPrice);
                    float fGapRatio = iGapPrice * 100 / iCurPrice;
                    stock.setStrGapPrice(strGapPrice);
                    stock.setiGapPrice(fGapRatio);
                    stock.setfGapRatio(fGapRatio);
                }
                return stock;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(List<StockVO> list, String fileName, String title) {
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
            sb1.append("\t<font size=5>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>투자의견</td>\r\n");
            sb1.append(
                    "<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>목표주가(원)</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>괴리율(%)</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>EPS(원)</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>PER(배)</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>추정기관수</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td align='right'>" + s.getCurPrice() + "</td>\r\n");
                    sb1.append("<td align='right'>" + s.getStrScore() + "</td>\r\n");
                    sb1.append("<td align='right'>" + s.getStrTargetPrice() + "</td>\r\n");
                    sb1.append("<td align='right'>" + s.getfGapRatio() + "</td>\r\n");
                    sb1.append("<td align='right'>" + s.getStrEps() + "</td>\r\n");
                    sb1.append("<td align='right'>" + s.getStrPer() + "</td>\r\n");
                    sb1.append("<td align='center'>" + s.getStrEstimateCount() + "</td>\r\n");
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
