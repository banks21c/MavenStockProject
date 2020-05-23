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
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.DataSort.NameAscCompare;

public class Weeks52NewLowHighNextDay {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger1 = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    DecimalFormat df = new DecimalFormat("###.##");

    String strHms = new SimpleDateFormat("HHmmss", Locale.KOREAN).format(new Date());
    int iHms = Integer.parseInt(strHms);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
    String strDefaultDate = sdf.format(new Date());
    // String strYmd = new SimpleDateFormat("yyyy년 M월 d일
    // E",Locale.KOREAN).format(new Date());
    int iYmd = Integer.parseInt(strDefaultDate.replaceAll("\\.", ""));
    String strYmdDash = strDefaultDate.replaceAll("\\.", "-");
    String strYmdDashBracket = "[" + strDefaultDate.replaceAll("\\.", "-") + "]";

    String kospiFileName = GlobalVariables.kospiFileName;
    String kosdaqFileName = GlobalVariables.kosdaqFileName;
    String strStockCode = "011170";
    String strStockName = "롯데케미칼";

    List<StockVO> newLowPriceList = new ArrayList<StockVO>();
    List<StockVO> newHighPriceList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new Weeks52NewLowHighNextDay(1);
    }

    Weeks52NewLowHighNextDay() {
        
        logger1 = LoggerFactory.getLogger(this.getClass());

        readOne("036530", "S&T홀딩스");
        writeFile(newLowPriceList, kospiFileName, "코스피 신저가", "LOW");
        writeFile(newHighPriceList, kospiFileName, "코스피 신고가", "HIGH");

    }

    Weeks52NewLowHighNextDay(int i) {
        
        logger1 = LoggerFactory.getLogger(this.getClass());
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        Properties props = new Properties();
        try {
            // InputStream is = new FileInputStream("log4j.properties");
            // props.load(new FileInputStream("log4j.properties"));
            // InputStream is =
            // getClass().getResourceAsStream("/log4j.properties");
            props.load(getClass().getResourceAsStream("log4j.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);

        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // URL url = loader.getResource("log4j.properties");
        // PropertyConfigurator.configure(url);
        File log4jfile = new File("log4j.properties");
        String absolutePath = log4jfile.getAbsolutePath();
        PropertyConfigurator.configure(absolutePath);

        // 모든 주식 정보를 조회한다.
        // 코스피
        readFile("코스피", kospiFileName);
        // 코스닥 신저가
        Collections.sort(newLowPriceList, new NameAscCompare());
        // 코스닥 신고가
        Collections.sort(newHighPriceList, new NameAscCompare());

        writeFile(newLowPriceList, kospiFileName, "코스피 신저가", "LOW");
        writeFile(newHighPriceList, kosdaqFileName, "코스피 신고가", "HIGH");

        newLowPriceList.clear();
        newHighPriceList.clear();

        // 코스닥
        readFile("코스닥", kosdaqFileName);
        // 코스닥 신저가
        Collections.sort(newLowPriceList, new NameAscCompare());
        // 코스닥 신고가
        Collections.sort(newHighPriceList, new NameAscCompare());

        writeFile(newLowPriceList, kospiFileName, "코스닥 신저가", "LOW");
        writeFile(newHighPriceList, kosdaqFileName, "코스닥 신고가", "HIGH");

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

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
            String tradeVolumeText = doc.select(".sp_txt9").get(0).parent().child(1).child(0).text();
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
                // System.out.println("text:" + text);
                if (text.startsWith("종목명")) {
                    String stockName = text.substring(4);
                    // System.out.println("stockName:" + stockName);
                    stock.setStockName(stockName);
                }

                if (text.startsWith("현재가")) {
                    // System.out.println("data1:" + dd.text());
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
                    // System.out.println("txts.length:" + txts.length);
                    if (txts.length == 7) {
                        stock.setVaryRatio(txts[5] + txts[6]);
                    } else if (txts.length == 8) {
                        stock.setVaryRatio(txts[5] + txts[6] + txts[7]);
                    }
                    varyRatio = stock.getVaryRatio();
                    stock.setfVaryRatio(Float.parseFloat(varyRatio.replaceAll("%", "")));
                    // System.out.println("상승률:" + stock.getVaryRatio());
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

            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/company/c1010001.aspx?cmp_cd=" + strStockCode).get();

            Element edd = doc.getElementById("cTB11");
            Element td0 = edd.select("td").first();
            Element td1 = edd.select("td").get(1);

            String strTd0[] = td0.text().split("/");
            String strTd1[] = td1.text().split("/");

            // String curPrice = strTd0[0].substring(0, strTd0[0].indexOf("원"));
            String weeks52MaxPrice = strTd1[0].substring(0, strTd1[0].indexOf("원"));
            String weeks52MinPrice = strTd1[1].substring(0, strTd1[1].indexOf("원"));

            // stock.setCurPrice(curPrice);
            stock.setWeeks52MaxPrice(weeks52MaxPrice);
            stock.setWeeks52MinPrice(weeks52MinPrice);

            // curPrice = curPrice.replaceAll(",", "").trim();
            weeks52MaxPrice = weeks52MaxPrice.replaceAll(",", "").trim();
            weeks52MinPrice = weeks52MinPrice.replaceAll(",", "").trim();

            System.out.println("curPrice:" + curPrice);
            System.out.println("weeks52MaxPrice:" + weeks52MaxPrice);
            System.out.println("weeks52MinPrice:" + weeks52MinPrice);

            int iWeeks52MaxPrice = Integer.parseInt(weeks52MaxPrice);
            int iWeeks52MinPrice = Integer.parseInt(weeks52MinPrice);

            if (stock.getiHighPrice() >= iWeeks52MaxPrice) {
                newHighPriceList.add(stock);
            } else if (stock.getiLowPrice() <= iWeeks52MinPrice) {
                newLowPriceList.add(stock);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return stock;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
        try {
            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strYmdDashBracket + "_" + title + ".html");
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
            sb1.append("\t<h2>" + strYmdDashBracket + " " + title + "</h2>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>번호</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>전일대비</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>등락율</td>\r\n");
            if (gubun.equals("LOW")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>저가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최저가</td>\r\n");
            } else if (gubun.equals("HIGH")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>고가</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>52주 최고가</td>\r\n");
            }
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "' target='_sub'>" + s.getStockName() + "</a></td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String varyPrice = s.getVaryPrice();

                    System.out.println("specialLetter+++>" + specialLetter);
                    System.out.println("varyPrice+++>" + varyPrice);

                    if (specialLetter.startsWith("↑") || specialLetter.startsWith("▲")
                            || specialLetter.startsWith("+")) {
                        sb1.append("<td style='text-align:right;color:red'>"
                                + StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
                        sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
                                + "</font></td>\r\n");
                    } else if (specialLetter.startsWith("↓") || specialLetter.startsWith("▼")
                            || specialLetter.startsWith("-")) {
                        sb1.append("<td style='text-align:right;color:blue'>"
                                + StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
                        sb1.append("<td style='text-align:right'><font color='blue'>" + specialLetter + " " + varyPrice
                                + "</font></td>\r\n");
                    } else {
                        sb1.append("<td style='text-align:right;color:metal'>"
                                + StringUtils.defaultIfEmpty(s.getCurPrice(), "") + "</td>\r\n");
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

                    if (gubun.equals("LOW")) {
                        sb1.append("<td style='text-align:right'>" + s.getLowPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MinPrice() + "</td>\r\n");
                    } else if (gubun.equals("HIGH")) {
                        sb1.append("<td style='text-align:right'>" + s.getHighPrice() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getWeeks52MaxPrice() + "</td>\r\n");
                    }
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
            fw.write(sb1.toString());
            fw.close();
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
            System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

            Document doc;
            try {
                // http://finance.naver.com/item/news_news.nhn?code=110570
                doc = Jsoup.connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=")
                        .get();
                // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=
                // System.out.println(doc.html());
                Element e1 = doc.select(".type2").get(0);

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
                    String yyyyMMdd = strDayTime.substring(0, 10);

                    // sb1.append("<h3>"+ strTitle +"</h3>\n");
                    // sb1.append("<div>"+ strSource+" | "+ strDayTime
                    // +"</div>\n");
                    if (strDefaultDate.equals(yyyyMMdd)) {
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
                        strView = strView.replaceAll(strStockName, "<a href='http://finance.naver.com/item/main.nhn?code="
                                + strStockCode + "'>" + strStockName + "</a>");

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
                System.out.println("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=");

                Document doc = Jsoup
                        .connect("http://finance.naver.com/item/news_news.nhn?code=" + strStockCode + "&page=").get();
                // http://finance.naver.com/item/news_read.nhn?article_id=0002942514&office_id=011&code=246690&page=

                Element e1 = doc.select(".type2").get(0);

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
                    String yyyyMMdd = strDayTime.substring(0, 10);

                    // sb1.append("<h3>"+ strTitle +"</h3>\n");
                    // sb1.append("<div>"+ strSource+" | "+ strDayTime
                    // +"</div>\n");
                    if (strDate.equals(yyyyMMdd)) {
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
                        strView = strView.replaceAll(strStockName, "<a href='http://finance.naver.com/item/main.nhn?code="
                                + strStockCode + "'>" + strStockName + "</a>");

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
