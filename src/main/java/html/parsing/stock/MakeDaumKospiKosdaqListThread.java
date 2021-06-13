/**
 *
 */
package html.parsing.stock;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author banks
 *
 */
public class MakeDaumKospiKosdaqListThread extends Thread {

    
	public final static String USER_HOME = System.getProperty("user.home");
    final static String[] fundNames = {"GIANT", "KTOP", "PIONEER", "마이다스", "마이티",
        "파워", "흥국 S&P", "ARIRANG", "KBSTAR",
        "KINDEX", "KODEX", "KOSEF", "QV", "SMART", "TIGER", "TREX", "TRUE",
        "able", "대우", "동양"};
    final static String[] itsNotWoosun = {"미래에셋대우", "포스코대우", "동우", "연우"};
    static String className = "";
    private int iType = 0;
    private long firstStartTime = 0L;

    // dd = 가나다순
    // upjong = 업종순
    /**
     *
     */
    public MakeDaumKospiKosdaqListThread() {
        className = this.getClass().getName();
        makeKospiKosdaqList();
    }

    public MakeDaumKospiKosdaqListThread(int iType) {
        this.iType = iType;
    }

    public MakeDaumKospiKosdaqListThread(int iType, long firstStartTime) {
        this.iType = iType;
        this.firstStartTime = firstStartTime;
        System.out.println(iType + ": firstStartTime:" + this.firstStartTime);
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        MakeDaumKospiKosdaqListThread list1 = new MakeDaumKospiKosdaqListThread(1, startTime);
        list1.start();
        Thread.sleep(2000);
        MakeDaumKospiKosdaqListThread list2 = new MakeDaumKospiKosdaqListThread(2, startTime);
        list2.start();
        Thread.sleep(2000);
        MakeDaumKospiKosdaqListThread list3 = new MakeDaumKospiKosdaqListThread(3, startTime);
        list3.start();
        Thread.sleep(2000);
        MakeDaumKospiKosdaqListThread list4 = new MakeDaumKospiKosdaqListThread(4, startTime);
        list4.start();
        Thread.sleep(2000);
        MakeDaumKospiKosdaqListThread list5 = new MakeDaumKospiKosdaqListThread(5, startTime);
        list5.start();
        Thread.sleep(2000);
        MakeDaumKospiKosdaqListThread list6 = new MakeDaumKospiKosdaqListThread(6, startTime);
        list6.start();
        Thread.sleep(2000);

        long endTime = System.currentTimeMillis();
        String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초";
        System.out.println("call time :" + elapsedTimeSecond);
        System.out.println("main method call finished.");
    }

    public void run() {
        System.out.println("\n run iType :" + iType);
        makeKospiKosdaqList();
    }

    public void makeKospiKosdaqList() {
        System.out.println("makeKospiKosdaqList started(type:" + iType + ").");
        System.out.println("type1:코스피 모든주식,type2:코스닥 모든주식,type3:코스피 우선주제외, type4:코스닥 우선주제외, type5:코스피 우선주제외,선박펀드제외, type6:코스닥 우선주제외, 선박펀드제외");
        // readMkURL("http://finance.daum.net/quote/all.daum?nil_profile=stockprice&nil_menu=siseleftmenu23","KOSPIDAQ");
        // readMkURL("http://finance.daum.net/quote/allpanel.daum?stype=P&type=U","KOSPIDAQ");
        // readMkURL("http://finance.daum.net/quote/allpanel.daum?stype=P&type=S","KOSPIDAQ");
        switch (iType) {
            case 1:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=P", "KOSPI");
                break;
            case 2:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=Q", "KOSDAQ");
                break;
            case 3:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=P", "KOSPI", true);
                break;
            case 4:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=Q", "KOSDAQ", true);
                break;
            case 5:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=P", "KOSPI", true, true);
                break;
            case 6:
                readMkURL(iType, "http://finance.daum.net/quote/all.daum?type=U&stype=Q", "KOSDAQ", true, true);
                break;
        }
    }

    public void readMkURL(int type, String url, String div) {
        readMkURL(type, url, div, false, false);
    }

    public void readMkURL(int type, String url, String div, boolean excludeWoosun) {
        readMkURL(type, url, div, excludeWoosun, false);
    }

    public Hashtable<String, String> readMkURL(int type, String url, String div, boolean excludeWoosun,
            boolean excludeBoat) {
        System.out.println("readMkURL type :" + type);
        long startTime = System.currentTimeMillis();
        System.out.println(type + ": startTime :" + startTime);

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
            // System.out.println("url:"+url);
            doc = Jsoup.connect(url).get();
            // System.out.println(doc.html());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            // FileWriter fw = new
            // FileWriter(USER_HOME+"\\documents\\new_"+div+woosun+boat+"_"+strDate+".html",false);
            FileWriter fw = new FileWriter(USER_HOME + "\\documents\\new_" + div + woosun + boat + ".html", false);

            Elements edds = doc.select(".txt a");
            // System.out.println("edds.size:"+edds.size());
            // System.out.println("edds:"+edds);
            // System.out.println("edds.size:"+edds.size());
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            Iterator<Element> it = edds.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                String stockName = e.text();
                if (stockName.contains("코오롱티슈진")) {
                    System.out.println("빙고~~ this is " + stockName);
                }
                stockName = stockName.replaceAll("&amp;", "&");
                if (stockName.contains("(")) {
                    stockName = stockName.substring(0, stockName.indexOf("("));
                }
                if (stockName.equals("코오롱티슈진")) {
                    System.out.println("빙고~~ this is 코오롱티슈진");
                }
                String href = e.attr("href");
                String stockCode = href.substring(href.indexOf("=") + 1);
                // System.out.println(div+" code:"+stockCode+" name:"+stockName);

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
        } finally {
            System.out.println("makeKospiKosdaqList finished.");
        }

        long endTime = System.currentTimeMillis();
        System.out.println(
                type + ": firstStartTime:" + firstStartTime + "/startTime:" + startTime + "/endTime:" + endTime);
        System.out.println(type + ": endTime - firstStartTime:" + (endTime - firstStartTime));
        System.out.println(type + ": endTime - startTime:" + (endTime - startTime));

        String elapsedTimeSecond = (endTime - startTime) / 1000 % 60 + "초 ";
        String elapsedTimeMinute = (endTime - startTime) / 1000 / 60 % 60 + "분 ";
        String elapsedTimeHour = (endTime - startTime) / 1000 / 60 / 60 % 24 + "시간 ";
        String elapsedTimeDay = (endTime - startTime) / 1000 / 60 / 60 / 24 + "일 ";
        System.out.println(type + ": 부분 :" + elapsedTimeDay + elapsedTimeHour + elapsedTimeMinute + elapsedTimeSecond);

        String elapsedTimeSecond2 = (endTime - firstStartTime) / 1000 % 60 + "초 ";
        String elapsedTimeMinute2 = (endTime - firstStartTime) / 1000 / 60 % 60 + "분 ";
        String elapsedTimeHour2 = (endTime - firstStartTime) / 1000 / 60 / 60 % 24 + "시간 ";
        String elapsedTimeDay2 = (endTime - firstStartTime) / 1000 / 60 / 60 / 24 + "일 ";
        System.out.println(
                type + ": 전체 :" + elapsedTimeDay2 + elapsedTimeHour2 + elapsedTimeMinute2 + elapsedTimeSecond2);

        return null;
    }

    public static boolean isKonex(String code) {
        Document doc;
        try {
            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            String codeName = doc.select(".wrap_company h2 a").text();

            // System.out.println("isKonex "+className+" code:"+code+" name:"+codeName);
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
