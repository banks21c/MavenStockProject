package html.parsing.stock;

import html.parsing.stock.util.GlobalVariables;
import html.parsing.stock.util.StockUtil;
import html.parsing.stock.model.StockVO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.util.DataSort.BizTypePerDescCompare;
import html.parsing.stock.util.DataSort.BpsDescCompare;
import html.parsing.stock.util.DataSort.DividendRateDescCompare;
import html.parsing.stock.util.DataSort.EpsDescCompare;
import html.parsing.stock.util.DataSort.PbrDescCompare;
import html.parsing.stock.util.DataSort.PerDescCompare;
import html.parsing.stock.util.DataSort.RoeDescCompare;
import html.parsing.stock.util.DataSort.StockNameAscCompare;

public class AllCompanyInfo {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(AllCompanyInfo.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

	String kospiFileName = GlobalVariables.kospiFileName;
	String kosdaqFileName = GlobalVariables.kosdaqFileName;

	List<StockVO> kospiStockList = new ArrayList<StockVO>();
	List<StockVO> kosdaqStockList = new ArrayList<StockVO>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        new AllCompanyInfo(1);
    }

    AllCompanyInfo() {

        List<StockVO> kospiStockList = readOne("028050");
        writeFile(kospiStockList, kospiFileName, "코스피 현금배당수익률 ", "DRATE");

        // List<StockAnalysis> kosdaqStockList = readOne("095570");
        // writeFile(kosdaqStockList,kosdaqFileName,"코스닥","ALL");
        writeDividendRate(kospiStockList, kospiFileName, "코스피 현금배당수익률 ", "DRATE");

    }

    AllCompanyInfo(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();

    	String kospiFileName = GlobalVariables.kospiFileName;
    	String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // 모든 주식 정보를 조회한다.
		try {
			kospiStockList = StockUtil.getAllStockListFromExcel(kospiFileName);
			kosdaqStockList = StockUtil.getAllStockListFromExcel(kosdaqFileName);
			logger.debug("kospiStockList.size1 :" + kospiStockList.size());
		} catch (Exception ex) {
			java.util.logging.Logger.getLogger(Weeks52NewLowHighPriceVsCurPrice.class.getName()).log(Level.SEVERE, null,
					ex);
			kospiStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kospiStockList, "stockMkt");
			kosdaqStockList = StockUtil.getStockCodeNameListFromKindKrxCoKr(kosdaqStockList, "kosdaqMkt");
			logger.debug("kospiStockList.size2 :" + kospiStockList.size());
		}

		// 코스피
        List<StockVO> kospiAllStockList = new ArrayList<StockVO>();
        for(int j=0;j<kospiStockList.size();j++) {
        	StockVO svo = kospiStockList.get(j);
            svo = getStockInfo(j, svo.getStockCode(), svo.getStockName());
            kospiAllStockList.add(svo);
        }
        System.out.println("kospiAllStockList.size :" + kospiAllStockList.size());
        // 코스닥
        List<StockVO> kosdaqAllStockList = new ArrayList<StockVO>();
        for(int j=0;j<kosdaqStockList.size();j++) {
        	StockVO svo = kosdaqStockList.get(j);
            svo = getStockInfo(j, svo.getStockCode(), svo.getStockName());
            kosdaqAllStockList.add(svo);
        }
        System.out.println("kosdaqAllStockList.size :" + kosdaqAllStockList.size());

        // 0.이름순 정렬
        Collections.sort(kospiAllStockList, new StockNameAscCompare());
        Collections.sort(kosdaqAllStockList, new StockNameAscCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 EPS,BPS,ROE,PER,업종PER,PBR,현금배당수익률", "ALL");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 EPS,BPS,ROE,PER,업종PER,PBR,현금배당수익률", "ALL");

        // 1.EPS 정렬
        Collections.sort(kospiAllStockList, new EpsDescCompare());
        Collections.sort(kosdaqAllStockList, new EpsDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 EPS ", "EPS");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 EPS ", "EPS");

        // 2.BPS 정렬
        Collections.sort(kospiAllStockList, new BpsDescCompare());
        Collections.sort(kosdaqAllStockList, new BpsDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 BPS ", "BPS");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 BPS ", "BPS");

        // 3.PER 정렬
        Collections.sort(kospiAllStockList, new PerDescCompare());
        Collections.sort(kosdaqAllStockList, new PerDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 PER ", "PER");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 PER ", "PER");

        // 4.업종PER 정렬
        Collections.sort(kospiAllStockList, new BizTypePerDescCompare());
        Collections.sort(kosdaqAllStockList, new BizTypePerDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 업종PER ", "BTPER");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 업종PER ", "BTPER");

        // 5.PBR 정렬
        Collections.sort(kospiAllStockList, new PbrDescCompare());
        Collections.sort(kosdaqAllStockList, new PbrDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 PBR ", "PBR");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 PBR ", "PBR");

        // 6.현금배당수익률 정렬
        Collections.sort(kospiAllStockList, new DividendRateDescCompare());
        Collections.sort(kosdaqAllStockList, new DividendRateDescCompare());

        writeDividendRate(kospiAllStockList, kospiFileName, "코스피 현금배당수익률 ", "DRATE");
        writeDividendRate(kosdaqAllStockList, kosdaqFileName, "코스닥 현금배당수익률 ", "DRATE");

        // 7.ROE 정렬
        Collections.sort(kospiAllStockList, new RoeDescCompare());
        Collections.sort(kosdaqAllStockList, new RoeDescCompare());

        writeFile(kospiAllStockList, kospiFileName, "코스피 ROE ", "ROE");
        writeFile(kosdaqAllStockList, kosdaqFileName, "코스닥 ROE ", "ROE");

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

    public static StockVO getStockInfo(int cnt, String code, String name) {
        System.out.println(code + ":" + name);
        Document doc;
        StockVO analysis = new StockVO();
        try {
            // 종합정보
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();

            Elements dates = doc.select(".date");
            if (dates != null) {
                if (dates.size() > 0) {
                    Element date = dates.get(0);
                    strYMD = date.ownText();
                    strYMD = date.childNode(0).toString().trim();
                    strYMD = "[" + strYMD.replaceAll("\\.", "-") + "] ";
                }
            }
            // 종목분석-기업현황
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/company/c1060001.aspx?cmp_cd=" + code).get();
            analysis.setStockCode(code);
            analysis.setStockName(name);

            Element cmpTable = doc.select(".cmp-table").get(0);

            Element td0301 = cmpTable.getElementsByClass("td0301").get(0);

            Elements dlDt = td0301.select("dl dt");
            for (Element e : dlDt) {
                System.out.println("txt:" + e.text());
                String items[] = e.text().split(" ");
                System.out.println("itemLength:" + items.length);
                String key = "";
                String value = "";
                if (items.length > 1) {
                    key = items[0];
                    value = items[1];
                } else {
                    key = items[0];
                }
                value = StringUtils.defaultIfEmpty(value, "").replaceAll("%", "");

                System.out.println("key:" + key + " value:" + value);

                if (key.equals("EPS")) {
                    analysis.setEps(value);
                    if (!analysis.getEps().equals("")) {
                        analysis.setiEps(Integer.parseInt(analysis.getEps().replaceAll(",", "")));
                    }
                } else if (key.equals("BPS")) {
                    analysis.setBps(value);
                    if (!analysis.getBps().equals("")) {
                        analysis.setiBps(Integer.parseInt(analysis.getBps().replaceAll(",", "")));
                    }
                } else if (key.equals("PER")) {
                    analysis.setPer(value);
                    if (!analysis.getPer().equals("")) {
                        analysis.setfPer(Float.parseFloat(analysis.getPer().replaceAll(",", "")));
                    }
                } else if (key.equals("업종PER")) {
                    analysis.setBizTypePer(value);
                    if (!analysis.getBizTypePer().equals("")) {
                        analysis.setfBizTypePer(Float.parseFloat(analysis.getBizTypePer().replaceAll(",", "")));
                    }
                } else if (key.equals("PBR")) {
                    analysis.setPbr(value);
                    if (!analysis.getPbr().equals("")) {
                        analysis.setfPbr(Float.parseFloat(analysis.getPbr().replaceAll(",", "")));
                    }
                } else if (key.equals("현금배당수익률")) {
                    analysis.setDividendRate(value);
                    if (!analysis.getDividendRate().equals("")) {
                        analysis.setfDividendRate(
                                Float.parseFloat(analysis.getDividendRate().replaceAll(",", "").replace("%", "")));
                    }
                }

            }
            int bps = analysis.getiBps();
            int eps = analysis.getiEps();
            System.out.println("bps:" + bps);
            System.out.println("eps:" + eps);
            float roe = 0;
            if (bps != 0 && (bps > 0 && eps > 0)) {
                System.out.println((float) eps / bps);
                roe = (float) eps / bps * 100;
                System.out.println("roe:" + roe);
            }
            roe = Math.round(roe * 100f) / 100f;
            analysis.setRoe(String.valueOf(roe));
            analysis.setfRoe(roe);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return analysis;
    }

    public void writeFile(List<StockVO> list, String fileName, String title, String gubun) {
        System.out.println("gubun:" + gubun);
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
            sb1.append("\t<font size=4>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            if (gubun.equals("ALL")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>EPS</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>BPS</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>ROE</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PER</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>업종PER</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PBR</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현금배당수익률</td>\r\n");
            } else if (gubun.equals("EPS")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>EPS</td>\r\n");
            } else if (gubun.equals("BPS")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>BPS</td>\r\n");
            } else if (gubun.equals("ROE")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>ROE</td>\r\n");
            } else if (gubun.equals("PER") || gubun.equals("BTPER")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PER</td>\r\n");
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>업종PER</td>\r\n");
            } else if (gubun.equals("PBR")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>PBR</td>\r\n");
            } else if (gubun.equals("DRATE")) {
                sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현금배당수익률</td>\r\n");
            }
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + cnt++ + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    if (gubun.equals("ALL")) {
                        sb1.append("<td style='text-align:right'>" + s.getEps() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getBps() + "</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getRoe() + " %</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getPer() + " %</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getBizTypePer() + " %</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getPbr() + " %</td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getDividendRate() + " %</td>\r\n");
                    } else if (gubun.equals("EPS")) {
                        sb1.append("<td style='text-align:right'>" + s.getEps() + "</td>\r\n");
                    } else if (gubun.equals("BPS")) {
                        sb1.append("<td style='text-align:right'>" + s.getBps() + "</td>\r\n");
                    } else if (gubun.equals("ROE")) {
                        sb1.append("<td style='text-align:right'>" + s.getRoe() + " %</td>\r\n");
                    } else if (gubun.equals("PER") || gubun.equals("BTPER")) {
                        if (s.getfPer() < s.getfBizTypePer()) {
                            sb1.append("<td style='text-align:right;color:blue;'>" + s.getPer() + " %</td>\r\n");
                            sb1.append("<td style='text-align:right'>" + s.getBizTypePer() + " %</td>\r\n");
                        } else {
                            sb1.append("<td style='text-align:right;color:red'>" + s.getPer() + " %</td>\r\n");
                            sb1.append("<td style='text-align:right'>" + s.getBizTypePer() + " %</td>\r\n");
                        }
                    } else if (gubun.equals("PBR")) {
                        sb1.append("<td style='text-align:right'>" + s.getPbr() + " %</td>\r\n");
                    } else if (gubun.equals("DRATE")) {
                        sb1.append("<td style='text-align:right'>" + s.getDividendRate() + " %</td>\r\n");
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

    public void writeDividendRate(List<StockVO> list, String fileName, String title, String gubun) {
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
            sb1.append("\t<font size=4>" + strYMD + title + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>No.</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;font-size:12px;'>현금배당수익률</td>\r\n");
            sb1.append("</tr>\r\n");

            int cnt = 1;
            for (StockVO s : list) {
                if (s != null) {
                    String dividendRate = StringUtils.defaultIfEmpty(s.getDividendRate(), "");
                    System.out.println("dividendRate:" + dividendRate);
                    if (!dividendRate.equals("0.00") && !dividendRate.equals("")) {
                        sb1.append("<tr>\r\n");
                        String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                        sb1.append("<td>" + cnt++ + "</td>\r\n");
                        sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                        sb1.append("<td style='text-align:right'>" + s.getDividendRate() + "</td>\r\n");
                        sb1.append("</tr>\r\n");
                    }
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
