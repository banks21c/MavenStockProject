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
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import html.parsing.stock.DataSort.LineUpAscCompare;

public class StockSort {

    final static String userHome = System.getProperty("user.home");
    java.util.logging.Logger logger = null;

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy�� M�� d�� E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";

    /**
     * @param args
     */
    public static void main(String[] args) {
        new StockSort(1);
    }

    StockSort() {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        logger.log(Level.INFO, "StockSort");
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd","KOSPI");
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ");
        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        // List<Stock> kospiStockList = readOne("071970");
        // writeFile(kospiStockList,kospiFileName,"肄��ㅽ��");
        List<StockVO> kosdaqStockList = readOne("214420", "��������");
        writeFile(kosdaqStockList, kosdaqFileName, "肄��ㅻ��");
    }

    StockSort(int i) {
        logger = java.util.logging.Logger.getLogger(this.getClass().getSimpleName());
        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;
        // 肄��ㅽ��
        List<StockVO> kospiStockList = readFile("肄��ㅽ��", kospiFileName);
        System.out.println("kospiStockList.size :" + kospiStockList.size());

        Collections.sort(kospiStockList, new LineUpAscCompare());

        // 肄��ㅻ��
        List<StockVO> kosdaqStockList = readFile("肄��ㅻ��", kosdaqFileName);
        System.out.println("kosdaqStockList.size :" + kosdaqStockList.size());

        Collections.sort(kosdaqStockList, new LineUpAscCompare());

        writeFile(kospiStockList, kospiFileName, "肄��ㅽ�� ������媛�");
        writeFile(kosdaqStockList, kosdaqFileName, "肄��ㅻ�� ������媛�");
    }

    public List<StockVO> readOne(String stockCode, String stockName) {
        List<StockVO> stocks = new ArrayList<StockVO>();

        int cnt = 1;
        StockVO stock = getStockInfo(cnt, stockCode);
        if (stock != null) {
            stock.setStockName(stockName);
            stocks.add(stock);
        }
        return stocks;
    }

    public List<StockVO> readFile(String kospidaq, String fileName) {
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

    public StockVO getStockInfo(int cnt, String code) {
        Document doc;
        StockVO stock = new StockVO();
        try {
            // 醫��⑹��蹂�
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            if (cnt == 1) {
                System.out.println(doc.title());
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

                if (text.startsWith("���ш�")) {
                    System.out.println("data1:" + dd.text());
                    text = text.replaceAll("���ъ��", "+");
                    text = text.replaceAll("留��대����", "-");
                    text = text.replaceAll("����", "��");
                    text = text.replaceAll("����", "��");
                    text = text.replaceAll("�쇱�쇳��", "%");

                    String txts[] = text.split(" ");
                    curPrice = txts[1];
                    stock.setCurPrice(txts[1]);
                    stock.setiCurPrice(
                            Integer.parseInt(StringUtils.defaultIfEmpty(stock.getCurPrice(), "0").replaceAll(",", "")));
                    iCurPrice = stock.getiCurPrice();

                    // �뱀��臾몄��
                    specialLetter = txts[3].replaceAll("蹂댄��", "");
                    stock.setSpecialLetter(specialLetter);

                    String varyPrice = txts[4];
                    stock.setVaryPrice(varyPrice);
                    stock.setiVaryPrice(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getVaryPrice(), "0").replaceAll(",", "")));
                    iVaryPrice = stock.getiVaryPrice();

                    // +- 遺���
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
                    System.out.println("���밸�:" + stock.getVaryRatio());
                }

                if (text.startsWith("���쇨�")) {
                    stock.setBeforePrice(text.split(" ")[1]);
                    stock.setiBeforePrice(Integer.parseInt(stock.getBeforePrice().replaceAll(",", "")));
                }
                if (text.startsWith("��媛�")) {
                    stock.setStartPrice(text.split(" ")[1]);
                    stock.setiStartPrice(Integer.parseInt(stock.getStartPrice().replaceAll(",", "")));
                }
                if (text.startsWith("怨�媛�")) {
                    stock.setHighPrice(text.split(" ")[1]);
                    stock.setiHighPrice(Integer.parseInt(stock.getHighPrice().replaceAll(",", "")));
                }
                if (text.startsWith("����媛�")) {
                    stock.setMaxPrice(text.split(" ")[1]);
                    stock.setiMaxPrice(Integer.parseInt(stock.getMaxPrice().replaceAll(",", "")));
                }
                if (text.startsWith("��媛�")) {
                    stock.setLowPrice(text.split(" ")[1]);
                    stock.setiLowPrice(Integer.parseInt(stock.getLowPrice().replaceAll(",", "")));
                }
                if (text.startsWith("����媛�")) {
                    stock.setMinPrice(text.split(" ")[1]);
                    stock.setiMinPrice(Integer.parseInt(stock.getMinPrice().replaceAll(",", "")));
                }
                if (text.startsWith("嫄곕����")) {
                    stock.setTradingVolume(text.split(" ")[1]);
                    stock.setiTradingVolume(Integer.parseInt(stock.getTradingVolume().replaceAll(",", "")));
                }
                if (text.startsWith("嫄곕����湲�") || text.startsWith("嫄곕��湲���")) {
                    stock.setTradingAmount(text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("諛깅�")));
                    stock.setlTradingAmount(Integer
                            .parseInt(StringUtils.defaultIfEmpty(stock.getTradingAmount().replaceAll(",", ""), "0")));
                }
            }

            String upDown = doc.select(".no_exday").get(0).select("em span").get(0).text();
            if (upDown.equals("����媛�")) {
                specialLetter = "��";
            } else if (upDown.equals("����媛�")) {
                specialLetter = "��";
            }
            stock.setSpecialLetter(specialLetter);

            if (specialLetter.equals("��")) {
                stock.setStockGubun("��");
                stock.setLineUp(11);
                return stock;
            }
            if (specialLetter.equals("��")) {
                stock.setStockGubun("����媛�");
                stock.setLineUp(21);
                return stock;
            }

            String highPrice = stock.getHighPrice();
            String lowPrice = stock.getLowPrice();
            String maxPrice = stock.getMaxPrice();
            System.out.println(!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice()));
            System.out.println(!curPrice.equals(stock.getMaxPrice()));
            System.out.println(highPrice.equals(stock.getMaxPrice()));
            if (!highPrice.equals("0") && highPrice.equals(stock.getMaxPrice())
                    && !curPrice.equals(stock.getMaxPrice())) {
                stock.setStockGubun("���곗�");
                stock.setLineUp(12);
                return stock;
            }
            if (!lowPrice.equals("0") && lowPrice.equals(stock.getMinPrice())
                    && !curPrice.equals(stock.getMinPrice())) {
                stock.setStockGubun("���곗�");
                stock.setLineUp(22);
                return stock;
            }

            float fRatio = 0f;
            if (varyRatio.indexOf("%") != -1) {
                fRatio = Float.parseFloat(varyRatio.substring(1, varyRatio.indexOf("%")));
                if (fRatio >= 15) {
                    if (specialLetter.equals("+") || specialLetter.equals("��")) {
                        stock.setStockGubun("15%�댁����");
                        stock.setLineUp(13);
                    } else if (specialLetter.equals("-") || specialLetter.equals("��")) {
                        stock.setStockGubun("15%�댁����");
                        stock.setLineUp(23);
                    }
                    return stock;
                }
                if (fRatio >= 10 && fRatio < 15) {
                    if (specialLetter.equals("+") || specialLetter.equals("��")) {
                        stock.setStockGubun("10%�댁����");
                        stock.setLineUp(14);
                    } else if (specialLetter.equals("-") || specialLetter.equals("��")) {
                        stock.setStockGubun("10%�댁����");
                        stock.setLineUp(24);
                    }
                    return stock;
                }
                if (fRatio >= 5 && fRatio < 10) {
                    if (specialLetter.equals("+") || specialLetter.equals("��")) {
                        stock.setStockGubun("5%�댁����");
                        stock.setLineUp(15);
                    } else if (specialLetter.equals("-") || specialLetter.equals("��")) {
                        stock.setStockGubun("5%�댁����");
                        stock.setLineUp(25);
                    }
                    return stock;
                }
            }

            // ���ш��� 鍮��� �����대�� ������ 而몃�� 醫�紐⑹�� 李얜����.
            float higher = 0;
            String flag = "";
            int icur = stock.getiCurPrice();
            int ihigh = stock.getiHighPrice();
            int ilow = stock.getiLowPrice();

            if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
                higher = Math.abs(icur - ihigh);
                flag = "����";
            } else {
                higher = Math.abs(icur - ilow);
                flag = "����";
            }
            System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
            int iTradingVolume = stock.getiTradingVolume();
            if (higher / icur * 100 > 10 && iTradingVolume > 0) {
                stock.setStockGubun("10%�댁��" + flag);
                stock.setLineUp(16);
                return stock;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>援щ�</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>醫�紐⑸�</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>���ш�</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>���쇰��鍮�</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>�깅�쎈�</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>嫄곕����</td>\r\n");
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>嫄곕����湲�(諛깅�)</td>\r\n");
            sb1.append("</tr>\r\n");

            for (StockVO s : list) {
                if (s != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + s.getStockCode();
                    sb1.append("<td>" + s.getStockGubun() + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + s.getStockName() + "</a></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + s.getCurPrice() + "</td>\r\n");

                    String specialLetter = StringUtils.defaultIfEmpty(s.getSpecialLetter(), "");
                    String varyPrice = s.getVaryPrice();
                    if (specialLetter.startsWith("��") || specialLetter.startsWith("��")
                            || specialLetter.startsWith("+")) {
                        sb1.append("<td style='text-align:right'><font color='red'>" + specialLetter + " " + varyPrice
                                + "</font></td>\r\n");
                    } else if (specialLetter.startsWith("��") || specialLetter.startsWith("��")
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
