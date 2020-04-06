package html.parsing.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.StockNameLengthDescCompare;
import html.parsing.stock.news.News;
import html.parsing.stock.util.FileUtil;

public class StockThemeNoInput extends News {

    final static String userHome = System.getProperty("user.home");
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(StockThemeNoInput.class);

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
    // String strThemeName = "2차전지 관련주";
    // String strThemeCode = "";
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
     * @throws Exception
     */
    public static void main(String[] args){
        try {
			new StockThemeNoInput(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    StockThemeNoInput() throws Exception {
        // 코스피
        //readFile("코스피", kospiFileName);
        // 코스닥
        //readFile("코스닥", kosdaqFileName);

	StockUtil.readStockCodeNameListFromExcel(allStockList,kospiFileName);
	StockUtil.readStockCodeNameListFromExcel(allStockList,kosdaqFileName);

        Collections.sort(allStockList, new StockNameLengthDescCompare());

        writeThemeMarketPrice();
    }

    StockThemeNoInput(int i) throws Exception {
        // MakeKospiKosdaqList.makeKospiKosdaqList();
        strDate = strDefaultDate;
        String year = strDate.substring(0, 4);
        String month = strDate.substring(5, 7);
        String day = strDate.substring(8, 10);
        int iYear = Integer.parseInt(year);
        int iMonth = Integer.parseInt(month) - 1;
        int iDay = Integer.parseInt(day);
        // System.out.println(year + month + day);

        cal1.set(iYear, iMonth, iDay);

        // 코스피
        //readFile("코스피", kospiFileName);
        // 코스닥
        //readFile("코스닥", kosdaqFileName);

	StockUtil.readStockCodeNameListFromExcel(allStockList,kospiFileName);
	StockUtil.readStockCodeNameListFromExcel(allStockList,kosdaqFileName);

        Collections.sort(allStockList, new StockNameLengthDescCompare());

        writeThemeMarketPrice();
    }

    public void readOne(String stockCode) {
        int cnt = 1;
        StockVO stock = new StockVO();
        stock.setStockCode(stockCode);
    }

    public void readFile(String kospidaq, String fileName) {
        File f = new File(userHome + "\\documents\\" + fileName);
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

    public void writeThemeMarketPrice() {
        Document doc;

        try {
            // 테마목록
            doc = Jsoup.connect("http://finance.naver.com/sise/theme.nhn").get();
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
                    System.out.println("테마명:" + trThemeName);
                    String themeLink = tr.select("a").first().attr("href");
                    if (themeLink != null && themeLink.indexOf("&no=") != -1) {
                        String strThemeCode = themeLink.substring(themeLink.indexOf("&no=") + 4);
                        System.out.println("strThemeCode1:" + strThemeCode);
                        String themeMarketPrice = getThemeMarketPrice2(strThemeCode);
                        writeFile(trThemeName, themeMarketPrice);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getThemeMarketPrice2(String strThemeCode) {
        System.out.println("strThemeCode2:" + strThemeCode);
        Document doc = null;
        String themeMarketPrice = null;
        if (strThemeCode != null) {
            String url = "http://finance.naver.com/sise/sise_group_detail.nhn?type=theme&no=" + strThemeCode;

            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(StockThemeInput.class.getName()).log(Level.SEVERE, null, ex);
            }

            Elements themeStockTables = doc.select(".type_5");
            Element themeStockTable = null;
            if (!themeStockTables.isEmpty()) {
                themeStockTable = themeStockTables.get(0);
            } else {
                return null;
            }
            themeStockTable.attr("style", "margin:0 0 10px 0; width:548px;");
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
            searchList.clear();
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
            sb1.append("\t<h2>" + strYMD + " 테마[" + strThemeName + "] 관련 뉴스</h2>");

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
                sb1.append("<table style='width:548px'>\r\n");
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

            String fileName = userHome + "\\documents\\" + strDate + "_테마[" + strThemeName + "]_관련_뉴스.html";
            FileUtil.fileWrite(fileName, sb1.toString());
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
            StringBuilder sb1 = getNews(stock);
            sb.append(sb1);
        }
        return sb;
    }

    public StringBuilder getNews(StockVO stock) {

        StringBuilder sb1 = new StringBuilder();

        strStockCode = stock.getStockCode();
        strStockName = stock.getStockName();
        System.out.println("strStockCode:" + strStockCode + " strStockName:" + strStockName);

        // 종합정보
        // System.out.println("http://finance.naver.com/item/news_news.nhn?code="
        // + strStockCode + "&page=1");
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd",
        // Locale.KOREAN);
        // String strDate = sdf.format(new Date());
        // System.out.println("날짜:" + strDate);
        Document doc;
        try {
            // 028260 삼성물산
            // http://finance.naver.com/item/news.nhn?code=091580
            // http://finance.naver.com/item/news_news.nhn?code=028260&page=1
            doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=1").get();
            // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
            // System.out.println(doc.html());
            Element type5 = doc.select(".type5").get(0);
            System.out.println("type5:" + type5);

            Elements trs = type5.getElementsByTag("tr");

            for (int i = 0; i < trs.size(); i++) {
                Element tr = trs.get(i);
                System.out.println("tr:[" + tr + "]");

                Elements tds = tr.getElementsByTag("td");
                System.out.println("tds.size:[" + tds.size() + "]");
                if (tds.size() < 3) {
                    continue;
                }

                Element dayTime = tr.select(".date").first();
                Element a1 = tr.getElementsByTag("a").first();
                Element source = tr.select(".info").first();

                if (a1 == null) {
                    continue;
                }

                System.out.println("dayTime:" + dayTime);
                System.out.println("a1:" + a1);
                System.out.println("source" + source);
                // System.out.println("a:" + a1);
                // System.out.println("source:" + source);
                // System.out.println("title:" + a1.html());
                // System.out.println("href:" + a1.attr("href"));
                // System.out.println("source:" + source.html());

                String strTitle = a1.html();
                String strLink = a1.attr("href");
                String strSource = source.html();
                String strDayTime = dayTime.html();
                String yyyyMMdd = strDayTime.substring(0, 10);
                System.out.println("yyyyMMdd:" + yyyyMMdd);
                String strDayTimeText = dayTime.text();

                String strYmdTemp = yyyyMMdd.replaceAll("\\.", "");
                System.out.println("strYmdTemp:" + strYmdTemp);
                int iYmdTemp = Integer.parseInt(strYmdTemp);
                System.out.println("yyyyMMdd:" + yyyyMMdd);
                System.out.println("iYmdTemp:" + iYmdTemp);

                if (iYmdTemp < iYmd) {
                    continue;
                }

                // System.out.println("strTitle:" + strTitle);
                // System.out.println("strLink:" + strLink);
                // System.out.println("strSource:" + strSource);
                // System.out.println("strDayTime:" + strDayTime);
                // System.out.println("strDayTimeText:" + strDayTimeText);
                // System.out.println("yyyyMMdd:" + yyyyMMdd);
                // System.out.println("strDate:" + strDate);
                String year = yyyyMMdd.substring(0, 4);
                String month = yyyyMMdd.substring(5, 7);
                String day = yyyyMMdd.substring(8, 10);
                int iYear = Integer.parseInt(year);
                int iMonth = Integer.parseInt(month) - 1;
                int iDay = Integer.parseInt(day);
                // System.out.println(year + month + day);

                cal2.set(iYear, iMonth, iDay);

                long diffSec = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000; // 초
                long diffDay = diffSec / (60 * 60 * 24); // 날
                // System.out.println("두 날자의 일 차이수 = " + diffDay);

                if (diffDay >= 0) {

                    doc = Jsoup.connect("http://finance.naver.com" + strLink).get();
                    doc.select("meta").remove();
                    doc.select("link").remove();
                    doc.select("script").remove();
                    Elements link_news_elements = doc.select(".link_news");
                    if (link_news_elements != null) {
                        link_news_elements.remove();
                    }

                    Elements naver_splugin = doc.select(".naver-splugin");
                    System.out.println("naver_splugin:" + naver_splugin);
                    if (naver_splugin != null) {
                        naver_splugin.remove();
                    }

                    // doc.select("a").remove();
                    doc.select("li").remove();
                    Elements aElements = doc.select("a");
                    for (int ii = 0; ii < aElements.size(); ii++) {
                        Element aElement = aElements.get(ii);
                        String aElementText = aElement.text();

                        Element p = doc.createElement("span");
                        p.appendText(aElementText);

                        aElement.replaceWith(p);
                        // System.out.println(aElement);
                    }
                    Element view = doc.select(".view").get(0);
                    view.select(".end_btn").remove();
                    // System.out.println("view.className:" + view.className());
                    // System.out.println("attr(class):" + view.attr("class"));
                    view.attr("style", "width:548px");
                    // System.out.println("attr(style):" + view.attr("style"));

                    String strView = view.toString();
                    // System.out.println("strStockName:" + strStockName);
                    // strView = strView.replaceAll(strStockName, "<a
                    // href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>" +
                    // strStockName + "</a>");
                    strView = strView.replaceAll("\\[\\]", "");
                    strView = strView.replaceAll("<ul></ul>", "");
                    strView = strView.replaceAll("<br>\r\n<br>\r\n<br>", "<br><br>");
                    strView = strView.replaceAll("&amp;", "&");

                    strView = strView.replaceAll("<script type=\"text/javascript\" src=\"/js/news_read.js\"></script>",
                            "");
                    strView = strView.replaceAll("NEWS\" data-cid=\"ne_[0-9]*_[0-9]*\" style=\"visibility: hidden;\">",
                            "");

                    String title = view.select("tr th strong").get(0).text();
                    Element dateElement = view.select("tr th span span").get(0);
                    String date = dateElement.text();

                    Element companyElement = view.select("tr th span").get(0);
                    companyElement.select("img").remove();
                    companyElement.select("span").remove();
                    String company = companyElement.text();

                    System.out.println("title:" + title);
                    System.out.println("company:" + company);
                    System.out.println("date:" + date);
                    System.out.println("stockNewsList1:" + stockNewsList1);
                    System.out.println("stockNewsList1.size:" + stockNewsList1.size());
                    // 소스 수정필요
                    StockNewsVO newsVO = new StockNewsVO();
                    newsVO.setTitle(title);
                    newsVO.setCompany(company);
                    newsVO.setDate(date);

                    if (stockNewsList1.size() == 0) {
                        stockNewsList1.add(newsVO);
                        sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                + strStockName + "(" + strStockCode + ")</a></h3>");
                        sb1.append(strView);
                        sb1.append("<br><br>\n");
                    } else {
                        for (StockNewsVO sNewsVO : stockNewsList1) {
                            String tempTitle = sNewsVO.getTitle();
                            String tempCompany = sNewsVO.getCompany();
                            String tempDate = sNewsVO.getDate();

                            System.out.println("tempTitle:" + tempTitle);
                            System.out.println("tempCompany:" + tempCompany);
                            System.out.println("tempDate:" + tempDate);
                            System.out.println("title.equals(tempTitle):" + title.equals(tempTitle));
                            System.out.println("company.equals(tempCompany):" + company.equals(tempCompany));
                            System.out.println("date.equals(tempDate):" + date.equals(tempDate));

                            if ((title.equals(tempTitle) && company.equals(tempCompany) && date.equals(tempDate))) {
                                System.out.println("다 같으면...");
                                newsVO = null;
                            }
                        }
                        System.out.println("newsVO:" + newsVO);
                        if (newsVO != null) {
                            System.out.println("strStockCode:" + strStockCode);
                            System.out.println("strStockName:" + strStockName);
                            stockNewsList1.add(newsVO);
                            sb1.append("<h3><a href='http://finance.naver.com/item/main.nhn?code=" + strStockCode + "'>"
                                    + strStockName + "(" + strStockCode + ")</a></h3>");
                            sb1.append(strView);
                            sb1.append("<br><br>\n");
                        }
                    }
                } else {
                    break;
                }
            }
            System.out.println("=========================================");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 증권명에 증권링크 생성
        sb1 = StockUtil.stockLinkString(sb1, allStockList);
        return sb1;
    }

}
