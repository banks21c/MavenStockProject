package html.parsing.stock.stockholders;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.model.StockVO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MajorStockHolders {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(MajorStockHolders.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new MajorStockHolders(1);
    }

    MajorStockHolders() {

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        List<StockVO> kospiStockList = readOne("071970");
        writeFile(kospiStockList, kospiFileName, "코스피");
    }

    MajorStockHolders(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
        // 코스피
        List<StockVO> kospiAllStockList = getAllStockInfo("코스피", kospiFileName);
        writeFile(kospiAllStockList, kospiFileName, "코스피 주요주주 보유주식 현황");

        // 코스닥
        List<StockVO> kosdaqAllStockList = getAllStockInfo("코스닥", kosdaqFileName);
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 주요주주 보유주식 현황");

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
                    stock.setCurPrice(curPrice);
                    stock.setiCurPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(stock.getCurPrice(), "0").replaceAll(",", "")));
                    iCurPrice = stock.getiCurPrice();
                }
            }

            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();
            String docHtml = StringEscapeUtils.escapeHtml3(doc.html());
            System.out.println("★★★★★★★★docHtml★★★★★★★★★★★★★★★★★");
            System.out.println("docHtml:" + docHtml);
            System.out.println("★★★★★★★★docHtml★★★★★★★★★★★★★★★★★");
            System.out.println("title:" + doc.title());
            if (cnt == 1) {
                // System.out.println(doc.html());
            }

            Element ctb13 = doc.getElementById("cTB13");
            Elements ctb13trs = ctb13.select("tr");
            Element tableElement = doc.createElement("table");
            tableElement.attr("width", "100%");

            for (Element tr : ctb13trs) {
                Elements tds = tr.select("td");
                for (Element td : tds) {
                    String title = td.attr("title");
                    if (!title.equals("")) {
                        td = td.text(title);
                        tableElement.append(td.parent().html());
                    }
                }
            }
            System.out.println("★★★★★★★★tableElement★★★★★★★★★★★★★★★★★");
            System.out.println(tableElement);
            System.out.println("★★★★★★★★★★tableElement★★★★★★★★★★★★★★★★");
            stock.setMajorStockHolders(tableElement.toString());
            return stock;
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
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append(
                    "<td colspan='3' style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>주요주주명/보유주식수/보유지분율(%)</td>\r\n");
            // sb1.append("<td
            // style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");
            sb1.append("</tr>\r\n");

            // 주요주주 보유주식수(보통) 보유지분(%)
            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    // sb1.append("<td
                    // style='text-align:center'>"+s.getStockCode()+"</td>\r\n");
                    sb1.append("<td colspan='3' style='text-align:right'>" + s.getMajorStockHolders() + "</td>\r\n");
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
