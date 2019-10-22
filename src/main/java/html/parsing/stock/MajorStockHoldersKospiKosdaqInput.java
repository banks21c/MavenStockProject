package html.parsing.stock;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import html.parsing.stock.DataSort.RetainAmountDescCompare;
import html.parsing.stock.DataSort.RetainRatioDescCompare;
import html.parsing.stock.DataSort.VaryRatioDescCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MajorStockHoldersKospiKosdaqInput {

    private static final Logger logger = LoggerFactory.getLogger(MajorStockHoldersKospiKosdaqInput.class);

    final static String userHome = System.getProperty("user.home");

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";
    static String majorStockHolders;
    static List<StockVO> kospiKosdaqStockList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        majorStockHolders = JOptionPane.showInputDialog("대주주명을 입력해주세요.");
        logger.debug("majorStockHolders :" + majorStockHolders);
        if (majorStockHolders.equals("")) {
            majorStockHolders = "국민연금";
        }
//		new MajorStockHoldersKospiKosdaqInput();
        try {
			new MajorStockHoldersKospiKosdaqInput(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    MajorStockHoldersKospiKosdaqInput() {
        // 대웅제약 069620
        kospiKosdaqStockList = readOne("069620", "대웅제약");
        writeFile(kospiKosdaqStockList, majorStockHolders);
//		kospiKosdaqStockList = readOne("019540", "일지테크");
//		writeFile(kospiKosdaqStockList, majorStockHolders);
//		kospiKosdaqStockList = readOne("017800", "현대엘리베이");
//		writeFile(kospiKosdaqStockList, majorStockHolders);
    }

    MajorStockHoldersKospiKosdaqInput(int i) throws Exception {

        kospiKosdaqStockList = StockUtil.readAllStockCodeNameListFromExcel();

        // 모든 주식 정보를 조회한다.
        kospiKosdaqStockList = getAllStockInfo();

        Collections.sort(kospiKosdaqStockList, new RetainRatioDescCompare());
        writeFile(kospiKosdaqStockList, majorStockHolders + " 보유율순");

        Collections.sort(kospiKosdaqStockList, new RetainAmountDescCompare());
        writeFile(kospiKosdaqStockList, majorStockHolders + " 보유금액순");

        Collections.sort(kospiKosdaqStockList, new VaryRatioDescCompare());
        writeFile(kospiKosdaqStockList, majorStockHolders + " 등락율순");

    }

    public static List<StockVO> readOne(String stockCode, String stockName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockHompage(cnt, stockCode, stockName);
        if (stock != null) {
            stocks.add(stock);
        }
        return stocks;
    }

    static long totalAmount = 0;

    public static List<StockVO> getAllStockInfo() {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 0;
        for (StockVO svo : kospiKosdaqStockList) {
            String stockCode = svo.getStockCode();
            String stockName = svo.getStockName();

            StockVO stock = getStockHompage(cnt, stockCode, stockName);
            if (stock != null) {
                stocks.add(stock);
                totalAmount += stock.getlRetainAmount();
            }
            cnt++;
        }

        return stocks;
    }

    // 종목분석-기업현황
    // http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=064260&cn=
    // 종목분석-기업개요
    // http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=010600&cn=
    public static StockVO getStockHompage(int cnt, String code, String name) {
        logger.debug(cnt + " code :" + code + " name :" + name);
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
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

            int iCurPrice = 0;

            Elements today = doc.select(".today");
            logger.debug("today :[" + Jsoup.parse(today.html()) + "]");

            Elements no_today = doc.select(".no_today");
            logger.debug("no_today :[" + Jsoup.parse(no_today.html()) + "]");

            Elements blindElements = doc.select(".no_today .blind");
            logger.debug("blindElements :[" + Jsoup.parse(blindElements.html()) + "]");
            if (blindElements.size() <= 0) {
                return null;
            }
            String curPriceWithComma = blindElements.get(0).text();
            String curPriceWithoutComma = curPriceWithComma.replace(",", "");
            iCurPrice = Integer.parseInt(curPriceWithoutComma);
            stock.setCurPrice(curPriceWithComma);
            stock.setiCurPrice(iCurPrice);

            Elements no_exday = doc.select(".no_exday");
            logger.debug("no_exday :[" + Jsoup.parse(no_exday.html()) + "]");
            Element new_totalinfo = null;
            if (no_exday.size() > 0) {
                new_totalinfo = no_exday.get(0);
                logger.debug("new_totalinfo:" + new_totalinfo);
                Document new_totalinfo_doc = Jsoup.parse(new_totalinfo.html());
                logger.debug("new_totalinfo_doc:" + new_totalinfo_doc);

                Elements emElements = null;
                String emClass = new_totalinfo_doc.select("em").attr("class");
                logger.debug("=============================");
                logger.debug("emClass :" + emClass);
                logger.debug("=============================");
                emElements = new_totalinfo_doc.select("." + emClass);
                logger.debug("emElements :" + emElements);

                if (emElements.size() > 0) {
                    Element emElements_idx0 = emElements.get(0);
                    logger.debug("emElements_idx0:" + emElements_idx0);
                    Element emElements_idx0_span0 = emElements_idx0.select("span").get(0);
                    Element emElements_idx0_span1 = emElements_idx0.select("span").get(1);
                    logger.debug("emElements_idx0_span0 :" + emElements_idx0_span0);
                    logger.debug("emElements_idx0_span1 :" + emElements_idx0_span1);

                    Element emElements_idx1 = emElements.get(1);
                    logger.debug("emElements_idx1:" + emElements_idx1);
                    String specialLetter = emElements_idx1.select("span").get(0).text();
                    String varyRatio = emElements_idx1.select("span").get(1).text();
                    logger.debug("specialLetter :" + specialLetter);
                    logger.debug("varyRatio :" + varyRatio);
                    if (specialLetter.equals("+")) {
                        stock.setSign("▲");
                        stock.setfVaryRatio(+Float.parseFloat(varyRatio));
                    } else if (specialLetter.equals("-")) {
                        stock.setSign("▼");
                        stock.setfVaryRatio(-Float.parseFloat(varyRatio));
                    } else {
                        stock.setSign("");
                    }

                    stock.setSpecialLetter(specialLetter);
                    stock.setVaryRatio(varyRatio);

                    String varyPriceWithComma = emElements_idx0_span1.text();
                    String varyPriceWithoutComma = varyPriceWithComma.replace(",", "");
                    stock.setVaryPrice(varyPriceWithComma);
                    stock.setiVaryPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));
                    int iVaryPrice = stock.getiVaryPrice();
                    logger.debug("varyPriceWithComma :" + varyPriceWithComma);
                    logger.debug("varyPriceWithoutComma :" + varyPriceWithoutComma);
                    logger.debug("iVaryPrice :" + iVaryPrice);
                }

            }

            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1010001.aspx?cmp_cd=" + code).get();

            Elements aClass = doc.select(".gHead01");
            Element aElem = null;
            if (aClass.size() <= 0) {
                return null;
            }
            if (aClass.size() > 1) {
                aElem = aClass.get(1);
            }
            System.out.println("aElem:" + aElem);
            if (aElem != null) {
                Elements tdElem = aElem.select("td");
                Element trElem = null;

                String retainVolumeWithComma = "";
                String retainVolumeWithoutComma = "";
                long lRetainVolume = 0;

                String retainAmount = "";
                long lRetainAmount = 0;
                String retainAmountByMillion = "";
                long lRetainAmountByMillion = 0;

                String retainRatio = "";
                float fRetainRatio = 0;

                long lRetainAmountTotal = 0;
                long lRetainAmountByMillionTotal = 0;
                long lRetainVolumeTotal = 0;
                float fRetainRatioTotal = 0;

                stock.setMajorStockHolderList(new Vector<MajorStockHolderVO>());

                for (Element td : tdElem) {
                    String majorStockHolderName = td.attr("title");
                    // if (title.equals(majorStockHolders)) {
                    if (majorStockHolderName.indexOf(majorStockHolders) != -1) {
                        System.out.println("title:" + majorStockHolderName);
                        stock.setMajorStockHolders(majorStockHolderName);
                        trElem = td.parent();
                        System.out.println("trElem:" + trElem);

                        Elements trTd = trElem.select("td");
                        MajorStockHolderVO majorStockHolderVO = new MajorStockHolderVO();
                        for (int i = 0; i < trTd.size(); i++) {
                            if (i == 1) {
                                logger.debug("trTd.get(1) :" + trTd.get(1));
                                retainVolumeWithComma = StringUtils.defaultIfEmpty(trTd.get(1).text(), "0");
                                logger.debug("retainVolumeWithComma :[" + retainVolumeWithComma + "]");
                                retainVolumeWithoutComma = retainVolumeWithComma.replaceAll(",", "");
                                lRetainVolume = Long.parseLong(retainVolumeWithoutComma);
                                lRetainAmount = lRetainVolume * iCurPrice;
                                lRetainAmountByMillion = lRetainVolume * iCurPrice / 1000000;

                                lRetainAmountTotal += lRetainAmount;
                                lRetainAmountByMillionTotal += lRetainAmountByMillion;
                                lRetainVolumeTotal += lRetainVolume;

                                DecimalFormat df1 = new DecimalFormat("#,##0");
                                retainAmount = df1.format(lRetainAmount);

                                DecimalFormat df2 = new DecimalFormat("#,##0");
                                retainAmountByMillion = df2.format(lRetainAmountByMillion);
                            }
                            if (i == 2) {
                                retainRatio = StringUtils.defaultIfEmpty(trTd.get(2).text(), "0");
                                logger.debug("retainRatio1 :[" + retainRatio + "]");
                                fRetainRatio = Float.parseFloat(retainRatio);

                                fRetainRatioTotal += fRetainRatio;
                            }
                            majorStockHolderVO.setMajorStockHolderName(majorStockHolderName);
                            majorStockHolderVO.setRetainVolume(retainVolumeWithComma);
                            majorStockHolderVO.setlRetainAmount(lRetainAmount);
                            majorStockHolderVO.setlRetainAmountByMillion(lRetainAmountByMillion);
                            majorStockHolderVO.setRetainAmount(retainAmount);
                            majorStockHolderVO.setRetainAmountByMillion(retainAmountByMillion);
                            majorStockHolderVO.setRetainRatio(retainRatio);
                        }
                        stock.getMajorStockHolderList().add(majorStockHolderVO);
                    }
                }
                System.out.println(stock.getMajorStockHolderList().toString());

                stock.setlRetainVolume(lRetainVolumeTotal);
                stock.setlRetainAmount(lRetainAmountTotal);
                stock.setfRetainRatio(fRetainRatioTotal);

                if (stock.getMajorStockHolderList().size() > 0) {
                    return stock;
                } else {
                    return null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeFile(List<StockVO> list, String title) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
        String strDate = sdf.format(new Date());
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        int cnt = 1;
        long lRetainAmount = 0L;
        long lEarningAmount = 0L;
        for (StockVO svo : list) {
            Vector vt = svo.getMajorStockHolderList();
            int listSize = vt.size();
            if (svo != null) {
                sb2.append("<tr>\r\n");
                String url = "http://finance.naver.com/item/main.nhn?code=" + svo.getStockCode();
                sb2.append("<td rowspan=" + listSize + ">" + cnt++ + "</td>\r\n");
                sb2.append(
                        "<td rowspan=" + listSize + "><a href='" + url + "' target='_blank'>" + svo.getStockName() + "</a></td>\r\n");

                for (int i = 0; i < listSize; i++) {
                    if (i > 0) {
                        sb2.append("<tr>\r\n");
                    }

                    MajorStockHolderVO holderVO = (MajorStockHolderVO) vt.get(i);
                    lRetainAmount += holderVO.getlRetainAmount();
                    int varyPrice = svo.getiVaryPrice();
                    int curPrice = svo.getiCurPrice();
                    lEarningAmount += varyPrice * curPrice;
                    logger.debug("lEarningAmount :" + lEarningAmount);
                    if (majorStockHolders.indexOf("국민연금") == -1) {
                        sb2.append("<td>" + holderVO.getMajorStockHolderName() + "</td>\r\n");
                    }
                    sb2.append("<td style='text-align:right'>" + holderVO.getRetainVolume() + "</td>\r\n");
                    sb2.append("<td style='text-align:right'>" + holderVO.getRetainRatio() + "%</td>\r\n");
                    if (svo.getSpecialLetter().equals("+")) {
                        sb2.append("<td style='text-align:right;color:red;'>" + svo.getCurPrice() + "</td>\r\n");
                        sb2.append("<td style='text-align:right;color:red;'>" + svo.getSign() + svo.getVaryPrice() + "%</td>\r\n");
                        sb2.append("<td style='text-align:right;color:red;'>" + svo.getSpecialLetter() + svo.getVaryRatio() + "%</td>\r\n");
                    } else if (svo.getSpecialLetter().equals("-")) {
                        sb2.append("<td style='text-align:right;color:blue;'>" + svo.getCurPrice() + "</td>\r\n");
                        sb2.append("<td style='text-align:right;color:blue;'>" + svo.getSign() + svo.getVaryPrice() + "</td>\r\n");
                        sb2.append("<td style='text-align:right;color:blue;'>" + svo.getSpecialLetter() + svo.getVaryRatio() + "%</td>\r\n");
                    } else {
                        sb2.append("<td style='text-align:right;color:gray;'>" + svo.getCurPrice() + "</td>\r\n");
                        sb2.append("<td style='text-align:right;color:gray;'>" + svo.getSign() + svo.getVaryPrice() + "%</td>\r\n");
                        sb2.append("<td style='text-align:right;color:gray;'>" + svo.getSpecialLetter() + svo.getVaryRatio() + "%</td>\r\n");
                    }
                    sb2.append("<td style='text-align:right'>" + holderVO.getRetainAmountByMillion() + "</td>\r\n");
                    sb2.append("</tr>\r\n");
                }

            }
        }
        String sColor = "";
        if (lEarningAmount > 0) {
            sColor = "red";
        } else if (lEarningAmount < 0) {
            sColor = "blue";
        } else {
            sColor = "gray";
        }
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
        sb1.append("<div style='width:548px;text-align:right;font-weight:bold;'>국민연금 총 보유액?<span style='color:" + sColor + "'>" + new DecimalFormat("#,##0").format(lRetainAmount / 1000000) + "(백만)</span></div>\r\n");
        sb1.append("<div style='width:548px;text-align:right;font-weight:bold;'>국민연금이 오늘 번 돈은?<span style='color:" + sColor + "'>" + new DecimalFormat("#,##0").format(lEarningAmount / 1000000) + "(백만)</span></div>\r\n");

        sb1.append("<table>\r\n");
        sb1.append("<tr>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>번호</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>종목명</td>\r\n");
        if (majorStockHolders.indexOf("국민연금") == -1) {
            sb1.append(
                    "<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>주요주주</td>\r\n");
        }
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유주식수</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>보유율</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>현재가</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>변동가</td>\r\n");
        sb1.append("<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>등락율</td>\r\n");
        sb1.append(
                "<td style='background:#669900;color:#ffffff;color:#ffffff;text-align:center;'>총금액(백만)</td>\r\n");
        sb1.append("</tr>\r\n");

        sb1.append(sb2.toString());
        sb1.append("</table>");

        sb1.append("</body>\r\n");
        sb1.append("</html>\r\n");
        String fileName = userHome + "\\documents\\" + strDate + "_" + title + ".html";
        FileUtil.fileWrite(fileName, sb1.toString());
    }

}
