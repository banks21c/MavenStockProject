/**
 *
 */
package html.parsing.stock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author banks
 *
 */
public class MakeKospiKosdaqList {

    final static String userHome = System.getProperty("user.home");
    final static String[] fundNames = {"GIANT", "KTOP", "PIONEER", "마이다스", "마이티",
        "파워", "흥국 S&P", "ARIRANG", "KBSTAR",
        "KINDEX", "KODEX", "KOSEF", "QV", "SMART", "TIGER", "TREX", "TRUE",
        "able", "대우", "동양"};
    final static String[] itsNotWoosun = {"미래에셋대우", "포스코대우", "동우", "연우"};
    static String className = "";

    // dd = 가나다순
    // upjong = 업종순
    /**
     *
     */
    public MakeKospiKosdaqList(int i) {
        isKonex("066570");
        isKonex("048870");
        isKonex("219420");
    }

    public static void main(String[] args) {
        // new MakeKospiKosdaqList();
        makeKospiKosdaqList();
    }

    public MakeKospiKosdaqList() {
        className = this.getClass().getName();
        makeKospiKosdaqList();
    }

    public static void makeKospiKosdaqList() {
        System.out.println("makeKospiKosdaqList started.");
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd", "KOSPI");
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ");
        //
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd","KOSPI",true);
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ",true);
        //
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd","KOSPI",true,true);
        // readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd","KOSDAQ",true,true);
        System.out.println("makeKospiKosdaqList finished.");
    }

    public static void readMkURL(String url, String div) {
        readMkURL(url, div, false, false);
    }

    public static void readMkURL(String url, String div, boolean excludeWoosun) {
        readMkURL(url, div, excludeWoosun, false);
    }

    public static Hashtable<String, String> readMkURL(String url, String div, boolean excludeWoosun,
            boolean excludeBoat) {
        String woosun = "";
        if (excludeWoosun) {
            woosun = "_우선주제외";
        } else {
            woosun = "";
        }
        String boat = "";
        if (excludeBoat) {
            boat = "_선박펀드제외";
        } else {
            boat = "";
        }
        Document doc;
        try {
            System.out.println("url:" + url);
            doc = Jsoup.connect(url).get();
            // System.out.println(doc.html());

            FileWriter fw = new FileWriter(userHome + "\\documents\\new_" + div + woosun + boat + ".html", false);

            Elements edds = doc.select(".st2");
            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            while (it.hasNext()) {
                Element e = it.next();
                String attrName = e.html();
                Document d = Jsoup.parse(attrName);

                Elements atags = d.getElementsByTag("a");
                // System.out.println("atags:"+atags);

                Element atag = null;
                if (atags != null && atags.size() > 0) {
                    atag = atags.get(0);
                } else {
                    continue;
                }

                String stockName = atag.html();
                stockName = stockName.replaceAll("&amp;", "&");
                String stockCode = atag.attr("title");
                System.out.println(div + " code:" + stockCode + " name:" + stockName);

                if (!stockCode.substring(0, 1).matches("[A-Z]")) {
                    boolean isFund = false;
                    for (String s : fundNames) {
                        if (stockName.startsWith(s + " ")) {
                            isFund = true;
                        }
                    }
                    boolean isWoosun = true;
                    for (String s : itsNotWoosun) {
                        if (stockName.equals(s)) {
                            isWoosun = false;
                        }
                    }

                    if (excludeBoat) {
                        // System.out.println(stockName+":"+stockName.matches(".*[0-9]+호"));
                        if (stockName.matches(".*[0-9]+호")) {
                            continue;
                        }
                    }

                    if (!isFund) {
                        // System.out.println(stockCode+" :"+stockName);
                        if (stockCode != null && !stockCode.equals("")) {
                            if (!excludeWoosun) {
                                // 코넥스(konex)가 아닐 경우에만 추가한다.
                                if (!isKonex(stockCode)) {
                                    codeNameHt.put(stockName, stockCode);
                                }
                            } else {
                                if (stockName.indexOf("우") != -1) {
                                    if (stockName.length() - 1 == stockName.lastIndexOf("우")) {
                                        if (isWoosun) {
                                            continue;
                                        }
                                    }
                                    if (stockName.length() - 1 == stockName.lastIndexOf("B")) {
                                        continue;
                                    }
                                }
                                // 코넥스(konex)가 아닐 경우에만 추가한다.
                                if (!isKonex(stockCode)) {
                                    codeNameHt.put(stockName, stockCode);
                                }
                            }
                        }
                    }
                }
            }

            StringBuilder sb1 = new StringBuilder();
            List<String> keyList = new ArrayList<String>(codeNameHt.keySet());
            Collections.sort(keyList);

            // Enumeration<String> enm = codeNameHt.keys();
            // while(enm.hasMoreElements()){
            // String code = (String)enm.nextElement();
            // String name = (String)codeNameHt.get(code);
            // sb1.append(code+"\t"+name+"\r\n");
            // }
            for (String key : keyList) {
                sb1.append((String) codeNameHt.get(key) + "\t" + key + "\r\n");
            }
            fw.write(sb1.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isKonex(String code) {
        Document doc;
        try {
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            String codeName = doc.select(".wrap_company h2 a").text();

            System.out.println(className + " code:" + code + " name:" + codeName);

            String market = doc.select(".wrap_company .description img").attr("alt");
            if (market.equals("코넥스")) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
