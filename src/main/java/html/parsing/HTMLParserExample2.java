package html.parsing;

import html.parsing.stock.GlobalVariables;
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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParserExample2 {

    final static String userHome = System.getProperty("user.home");
    final static String[] fundNames = {"ARIRANG", "KBSTAR", "KINDEX", "KODEX",
        "KOSEF", "QV", "SMART", "TIGER", "TREX", "TRUE", "able", "대우", "동양"};

    public HTMLParserExample2() {
        //readURL("http://vip.mk.co.kr/newSt/rate/item_all.php");
        //readHankyungURL("http://www.hankyung.com/stockplus/main.php?module=stock&mode=stockinfo_panel_sub&market=1");
        //readHankyungURL("http://www.hankyung.com/stockplus/main.php?module=stock&mode=stockinfo_panel_sub&market=2");
//		readDaumURL("http://finance.daum.net/quote/allpanel.daum?stype=P&type=U");
//		readDaumURL("http://finance.daum.net/quote/allpanel.daum?stype=Q&type=U");
//		readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=upjong","KOSPI");
//		readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=upjong","KOSDAQ");

        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd", "KOSPI");
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd", "KOSDAQ");

//		grabIt(0,"011150");
//		grabIt(0,"017550");
//		grabIt(0,"018700");
//		grabIt(0,"063440");
//		grabIt(0,"014200");
//		grabIt(0,"119830");
//		grabIt(0,"00341");
//		grabIt(0,"008020");
//		grabIt(0,"011155");
        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;
        readFile("코스피", kospiFileName);
        readFile("코스닥", kosdaqFileName);
    }

    public Hashtable<String, String> readHankyungURL(String url) {
        Document doc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            doc = Jsoup.connect(url).get();
            Elements liCurrent = doc.select(".current");
            Element el = liCurrent.get(0);
            Elements span = el.getElementsByTag("span");
            Element span1 = span.get(0);
            String tabName = span1.html();
            System.out.println("tabName:" + tabName);

            System.out.println(doc.html());

            FileWriter fw = new FileWriter(userHome + "\\documents\\new_" + tabName + ".html", false);

            Elements edds = doc.getElementsByTag("a");
            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            while (it.hasNext()) {
                Element e = it.next();
                String attrName = e.attr("name");
                String attrHref = e.attr("href");
                String itemName = "";
                String itemCode = "";
                System.out.println(e);
                if (attrName.startsWith("PanelTableBusiness")) {
                    itemName = attrName.substring("PanelTableBusiness".length());
                }
                if (attrHref.indexOf("itemcode=") != -1) {
                    itemCode = attrHref.substring(attrHref.indexOf("itemcode=") + "itemcode=".length());
                }
                System.out.println(e.html());
                if (itemCode != null && !itemCode.equals("")) {
                    codeNameHt.put(itemName, itemCode);
                }
            }

            StringBuffer sb1 = new StringBuffer();
            List<String> keyList = new ArrayList<String>(codeNameHt.keySet());
            Collections.sort(keyList);

//			Enumeration<String> enm = codeNameHt.keys();
//			while(enm.hasMoreElements()){
//				String code = (String)enm.nextElement();
//				String name = (String)codeNameHt.get(code);
//				sb1.append(code+"\t"+name+"\r\n");
//			}
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

    public Hashtable<String, String> readDaumURL(String url) {
        Document doc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            doc = Jsoup.connect(url).get();
            System.out.println("doc:" + doc);
            Elements liCurrent = doc.select(".current");
            Element el = liCurrent.get(0);
            Elements span = el.getElementsByTag("span");
            Element span1 = span.get(0);
            String tabName = span1.html();
            System.out.println("tabName:" + tabName);

            System.out.println(doc.html());

            FileWriter fw = new FileWriter(userHome + "\\documents\\new_" + tabName + ".html", false);

            Elements edds = doc.getElementsByTag("a");
            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            while (it.hasNext()) {
                Element e = it.next();
                String attrName = e.attr("name");
                String attrHref = e.attr("href");
                String itemName = "";
                String itemCode = "";
                System.out.println(e);
                if (attrName.startsWith("PanelTableBusiness")) {
                    itemName = attrName.substring("PanelTableBusiness".length());
                }
                if (attrHref.indexOf("itemcode=") != -1) {
                    itemCode = attrHref.substring(attrHref.indexOf("itemcode=") + "itemcode=".length());
                }
                System.out.println(e.html());
                if (itemCode != null && !itemCode.equals("")) {
                    codeNameHt.put(itemName, itemCode);
                }
            }

            StringBuffer sb1 = new StringBuffer();
            List<String> keyList = new ArrayList<String>(codeNameHt.keySet());
            Collections.sort(keyList);

//			Enumeration<String> enm = codeNameHt.keys();
//			while(enm.hasMoreElements()){
//				String code = (String)enm.nextElement();
//				String name = (String)codeNameHt.get(code);
//				sb1.append(code+"\t"+name+"\r\n");
//			}
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

    public Hashtable<String, String> readMkURL(String url, String div) {
        Document doc;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            doc = Jsoup.connect(url).get();
            System.out.println(doc.html());

            FileWriter fw = new FileWriter(userHome + "\\documents\\new_" + div + ".html", false);

            Elements edds = doc.select(".st2");
            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            while (it.hasNext()) {
                Element e = it.next();
                String attrName = e.html();
                Document d = Jsoup.parse(attrName);

                Element atag = d.getElementsByTag("a").get(0);

                String itemName = atag.html();
                String itemCode = atag.attr("title");

                if (!itemCode.substring(0, 1).matches("[A-Z]")) {
                    boolean isFund = false;
                    for (String s : fundNames) {
                        if (itemName.startsWith(s + " ")) {
                            isFund = true;
                        }
                    }
                    if (!isFund) {
                        System.out.println("itemCode :" + itemCode);
                        System.out.println("itemName :" + itemName);
                        if (itemCode != null && !itemCode.equals("")) {
                            codeNameHt.put(itemName, itemCode);
                        }
                    }
                }
            }

            StringBuffer sb1 = new StringBuffer();
            List<String> keyList = new ArrayList<String>(codeNameHt.keySet());
            Collections.sort(keyList);

//			Enumeration<String> enm = codeNameHt.keys();
//			while(enm.hasMoreElements()){
//				String code = (String)enm.nextElement();
//				String name = (String)codeNameHt.get(code);
//				sb1.append(code+"\t"+name+"\r\n");
//			}
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

    public void readFile(String kospidaq, String fileName) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            //FileReader reader = new FileReader(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(userHome + "\\documents\\" + strDate + "_" + f.getName());
            String read = null;
            StringBuffer sb1 = new StringBuffer();
            sb1.append("<html>\r\n");
            sb1.append("<head>\r\n");
            sb1.append("<style>\r\n");
            sb1.append("    table {border:1px solid #aaaaaa;}\r\n");
            sb1.append("    td {border:1px solid #aaaaaa;}\r\n");
            sb1.append("</style>\r\n");
            sb1.append("</head>\r\n");
            sb1.append("<body>\r\n");
            sb1.append("\t<font size=5>" + kospidaq + "</font>");
            sb1.append("<table>\r\n");
            sb1.append("<tr>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>정렬순서</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>구분</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>종목명</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>현재가</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>전일비</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>등락률</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>거래량</td>\r\n");
            sb1.append("<td style='background:#669900;text-align:center;'>거래대금(백만)</td>\r\n");
            sb1.append("</tr>\r\n");
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                read = read.split("\t")[0];

                if (read.length() != 6) {
                    continue;
                }

                Hashtable<String, String> htable = grabIt(cnt++, read);
                if (htable != null) {
                    sb1.append("<tr>\r\n");
                    String url = "http://finance.naver.com/item/main.nhn?code=" + read;
                    sb1.append("<td>" + htable.get("lineUp") + "</td>\r\n");
                    sb1.append("<td>" + htable.get("구분") + "</td>\r\n");
                    sb1.append("<td><a href='" + url + "'>" + htable.get("종목명") + "</a></td>\r\n");
                    sb1.append("<td style='text-align:right'>" + htable.get("curPrice") + "</td>\r\n");
                    String chgPrice = htable.get("chgPrice");
                    System.out.println("chgPrice1:::" + chgPrice);
                    if (chgPrice.indexOf('.') != -1) {
                        chgPrice = chgPrice.substring(0, chgPrice.indexOf('.'));
                    }
                    System.out.println("chgPrice2:::" + chgPrice);
                    if (chgPrice.startsWith("↑") || chgPrice.startsWith("▲") || chgPrice.startsWith("+")) {
                        sb1.append("<td style='text-align:right'><font color='red'>" + chgPrice + "</font></td>\r\n");
                    } else if (chgPrice.startsWith("↓") || chgPrice.startsWith("▼") || chgPrice.startsWith("-")) {
                        sb1.append("<td style='text-align:right'><font color='blue'>" + chgPrice + "</font></td>\r\n");
                    }
                    String ratio = htable.get("ratio");
                    if (ratio.startsWith("+")) {
                        sb1.append("<td style='text-align:right'><font color='red'>" + ratio + "</font></td>\r\n");

                    } else if (ratio.startsWith("-")) {
                        sb1.append("<td style='text-align:right'><font color='blue'>" + ratio + "</font></td>\r\n");
                    }
                    sb1.append("<td style='text-align:right'>" + htable.get("거래량") + "</td>\r\n");
                    sb1.append("<td style='text-align:right'>" + htable.get("거래대금") + "</td>\r\n");
                    sb1.append("</tr>\r\n");
                }
            }
            sb1.append("</body>\r\n");
            sb1.append("</html>\r\n");
            fw.write(sb1.toString());
            fw.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }

    }

    public Hashtable<String, String> grabIt(int cnt, String code) {
        Document doc;
        try {

            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            if (cnt == 0) {
                System.out.println(doc.html());
            }

            Hashtable<String, String> ht = new Hashtable<String, String>();

            Elements metas = doc.select("meta");
            String ratio = "";
            String curPrice = "";
            String chgPrice = "";
            String sign = "";
            for (Element meta : metas) {
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
                    ratio = metaContentArray[metaContentArray.length - 1];
                    chgPrice = metaContentArray[metaContentArray.length - 2];
                    curPrice = metaContentArray[metaContentArray.length - 3];
                    if (!chgPrice.substring(0, 1).matches("[0-9]")) {
                        sign = chgPrice.substring(0, 1);
                    }
                    System.out.println("sign :" + sign);
                    System.out.println("ratio :" + ratio);
                    System.out.println("curPrice :" + curPrice);
                    System.out.println("chgPrice :" + chgPrice);
                }
            }

            Elements edds = doc.getElementsByTag("dd");
            System.out.println("dds.size:" + edds.size());
            for (int i = 0; i < edds.size(); i++) {
                Element dd = edds.get(i);
                String text = dd.text();
                if (text.startsWith("거래량")) {
                    System.out.println("거래량 :" + text.split(" ")[1]);
                    ht.put(text.split(" ")[0], text.split(" ")[1]);
                }
                if (text.startsWith("거래대금")) {
                    ht.put(text.split(" ")[0], text.split(" ")[1].substring(0, text.split(" ")[1].indexOf("백만")));
                }
                System.out.println("data:" + dd.text());

                if (text.startsWith("고가") || text.startsWith("저가") || text.startsWith("현재가")) {
                    ht.put(text.split(" ")[0], text.split(" ")[1]);
                    System.out.println("0:" + text.split(" ")[0]);
                    System.out.println("1:" + text.split(" ")[1]);
                }
            }

            // get page title
            String title = doc.title();
            String jongMok = title.split(":")[0].trim();
            //System.out.println(cnt +"."+code);
            //System.out.println(cnt +"."+code+" "+jongMok);
            if (jongMok.equals("네이버")) {
                return null;
            }

            Elements dds = doc.select("dd");
            ht.put("종목명", jongMok);
            ht.put("ratio", ratio);
            ht.put("sign", sign);
            ht.put("curPrice", curPrice);
            ht.put("chgPrice", chgPrice);
            for (Element dd : dds) {
                String priceTxt = dd.text();
                System.out.println("priceTxt:" + priceTxt);
                if ((priceTxt.indexOf("가") != -1 || priceTxt.indexOf("량") != -1) && priceTxt.matches("(.*)[0-9]+(.*)")) {
                    String priceSplit[] = priceTxt.replaceAll(",", "").split(" ");
                    System.out.println(priceSplit[0] + "============" + priceSplit[1]);
                    ht.put(priceSplit[0], priceSplit[1]);
                }
            }

            String cur = StringUtils.defaultIfBlank(ht.get("현재가"), "");
            String high = StringUtils.defaultIfBlank(ht.get("고가"), "");
            String low = StringUtils.defaultIfBlank(ht.get("저가"), "");
            String tradeCnt = StringUtils.defaultIfBlank(ht.get("거래량"), "");

            int icur = Integer.parseInt(cur);
            int ihigh = Integer.parseInt(high);
            int ilow = Integer.parseInt(low);
            int itradeCnt = Integer.parseInt(tradeCnt);
            //거래량 콤마 삽입후 다시 hashtable에 put
            DecimalFormat df = new DecimalFormat("#,##0");
            String tradeCnt2 = df.format(itradeCnt);
            ht.put("거래량", tradeCnt2);

            System.out.println("sign ==> " + sign);
            System.out.println("cur ==> " + cur);
            System.out.println("high ==> " + high);
            System.out.println("low ==> " + low);
            System.out.println("itradeCnt ==> " + itradeCnt);
            System.out.println("상한가 ===> " + ht.get("상한가"));
            System.out.println("하한가 ===> " + ht.get("하한가"));
            System.out.println("저가 ===> " + ht.get("저가"));
            System.out.println("고가 ===> " + ht.get("고가"));

            if (sign.equals("↑")) {
                System.out.println("↑:" + jongMok);
                ht.put("구분", "↑");
                ht.put("lineUp", "1");
                return ht;
            }
            if (sign.equals("↓")) {
                System.out.println("↓:" + jongMok);
                ht.put("구분", "↓");
                ht.put("lineUp", "5");
                return ht;
            }
            if (!high.equals("0") && high.equals(ht.get("상한가")) && !cur.equals(ht.get("상한가"))) {
                System.out.println("상터치:" + jongMok);
                ht.put("구분", "상터치");
                ht.put("lineUp", "2");
                return ht;
            }
            if (!low.equals("0") && low.equals(ht.get("하한가")) && !cur.equals(ht.get("하한가"))) {
                System.out.println("하터치:" + jongMok);
                ht.put("구분", "하터치");
                ht.put("lineUp", "6");
                return ht;
            }

            float fRatio = 0f;
            if (ratio.indexOf("%") != -1) {
                fRatio = Float.parseFloat(ratio.substring(1, ratio.indexOf("%")));
                if (fRatio >= 15) {
                    if (sign.equals("+") || sign.equals("▲")) {
                        System.out.println("↗15%이상:" + jongMok);
                        ht.put("구분", "↗15%이상");
                        ht.put("lineUp", "3");
                    } else if (sign.equals("-") || sign.equals("▼")) {
                        System.out.println("↘15%이상:" + jongMok);
                        ht.put("구분", "↘15%이상");
                        ht.put("lineUp", "7");
                    }
                    return ht;
                }
                if (fRatio >= 10 && fRatio < 15) {
                    if (sign.equals("+") || sign.equals("▲")) {
                        System.out.println("↗10%이상:" + jongMok);
                        ht.put("구분", "↗10%이상");
                        ht.put("lineUp", "4");
                    } else if (sign.equals("-") || sign.equals("▼")) {
                        System.out.println("↘10%이상:" + jongMok);
                        ht.put("구분", "↘10%이상");
                        ht.put("lineUp", "8");
                    }
                    return ht;
                }
            }

            //현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
            float higher = 0;
            String flag = "";
            if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
                higher = Math.abs(icur - ihigh);
                flag = "↗↘";
            } else {
                higher = Math.abs(icur - ilow);
                flag = "↘↗";
            }
            System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
            if (higher / icur * 100 > 10 && itradeCnt > 0) {
                System.out.println("10%:" + jongMok);
                ht.put("구분", flag + "10%이상");
                ht.put("lineUp", "10");
                return ht;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Hashtable<String, String> grabIt_(int cnt, String code) {
        Document doc;
        try {

            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=" + code).get();
            if (cnt == 0) {
                System.out.println(doc.html());
            }

            Hashtable<String, String> ht = new Hashtable<String, String>();

            Elements dds1 = doc.select("dd");
            System.out.println("dds1.length:" + dds1.size());
            int ddCnt = 0;
            for (Element dd : dds1) {
                ddCnt++;
                String ddText = dd.text();
                System.out.println(ddCnt + ".ddText:" + ddText);
//				ddText:종목명 바른손
//				ddText:종목코드 018700 코스닥
//				ddText:현재가 3,825 전일대비 하락 245 마이너스 6.02 퍼센트
//				ddText:전일가 4,070
//				ddText:시가 4,100
//				ddText:고가 4,180
//				ddText:상한가 5,290
//				ddText:저가 3,580
//				ddText:하한가 2,850
//				ddText:거래량 947,122
//				ddText:거래대금 3,607백만				

                if (ddText.startsWith("종목명")
                        || ddText.startsWith("종목코드")
                        || ddText.startsWith("전일가")
                        || ddText.startsWith("시가")
                        || ddText.startsWith("고가")
                        || ddText.startsWith("상한가")
                        || ddText.startsWith("저가")
                        || ddText.startsWith("하한가")
                        || ddText.startsWith("거래량")
                        || ddText.startsWith("거래대금")) {
                    String[] ddTextArray = ddText.split(" ");
                    int contentCnt = 0;
//					for (String s : ddTextArray) {
//						System.out.print("content[" + (contentCnt++) + "]:" + s+"\t");
//					}
                    System.out.println("");
                    ht.put(ddTextArray[0], ddTextArray[1].replaceAll(",", ""));
                }
//				ddText:현재가 3,825 전일대비 하락 245 마이너스 6.02 퍼센트
                if (ddText.startsWith("현재가")) {
                    String str = ddText.substring(ddText.indexOf("현재가") + "현재가".length() + 1);
                    String tempCurPrice = str.substring(0, str.indexOf(" "));
                    ht.put("현재가", tempCurPrice.replaceAll(",", ""));
                }
                if (ddCnt == 14) {
                    if (ddText.indexOf("포인트 하락") != -1) {
                        ht.put("chgPrice", "-" + ddText.split(" ")[0].replaceAll(",", ""));
                    } else {
                        ht.put("chgPrice", "+" + ddText.split(" ")[0].replaceAll(",", ""));
                    }
                }
                if (ddCnt == 15) {
                    if (ddText.indexOf("마이너스") != -1) {
                        ht.put("ratio", "-" + ddText.split(" ")[0].replaceAll(",", ""));
                    } else {
                        ht.put("ratio", "+" + ddText.split(" ")[0].replaceAll(",", ""));
                    }
                }

            }

            // get page title
            String title = doc.title();
            System.out.println("title:" + title);
            String jongMok = title.split(":")[0].trim();
            if (jongMok.equals("네이버")) {
                return null;
            }

            String cur = StringUtils.defaultIfBlank(ht.get("현재가"), "");
            String high = StringUtils.defaultIfBlank(ht.get("고가"), "");
            String low = StringUtils.defaultIfBlank(ht.get("저가"), "");
            String tradeCnt = StringUtils.defaultIfBlank(ht.get("거래량"), "");

            int icur = Integer.parseInt(cur);
            int ihigh = Integer.parseInt(high);
            int ilow = Integer.parseInt(low);
            int itradeCnt = Integer.parseInt(tradeCnt);
            //거래량 콤마 삽입후 다시 hashtable에 put
            DecimalFormat df = new DecimalFormat("#,##0");
            String tradeCnt2 = df.format(itradeCnt);
            ht.put("거래량", tradeCnt2);

            System.out.println("cur ==> " + cur);
            System.out.println("high ==> " + high);
            System.out.println("low ==> " + low);
            System.out.println("itradeCnt ==> " + itradeCnt);

            if (cur.equals(ht.get("↑"))) {
                System.out.println("↑:" + jongMok);
                ht.put("구분", "↑");
                ht.put("lineUp", "1");
                return ht;
            }
            if (cur.equals(ht.get("↓"))) {
                System.out.println("↓:" + jongMok);
                ht.put("구분", "↓");
                ht.put("lineUp", "5");
                return ht;
            }
            if (!high.equals("0") && high.equals(ht.get("↑")) && !cur.equals(ht.get("↑"))) {
                System.out.println("상터치:" + jongMok);
                ht.put("구분", "상터치");
                ht.put("lineUp", "2");
                return ht;
            }
            if (!low.equals("0") && low.equals(ht.get("↓")) && !cur.equals(ht.get("↓"))) {
                System.out.println("하터치:" + jongMok);
                ht.put("구분", "하터치");
                ht.put("lineUp", "6");
                return ht;
            }

            String ratio = ht.get("ratio");
            String sign = ratio.substring(0, 1);
            float fRatio = 0f;
            if (ratio.indexOf("%") != -1) {
                fRatio = Float.parseFloat(ratio.substring(1, ratio.indexOf("%")));
                if (fRatio >= 15) {
                    if (sign.equals("+")) {
                        System.out.println("↗15%이상:" + jongMok);
                        ht.put("구분", "↗15%이상");
                        ht.put("lineUp", "3");
                    } else if (sign.equals("-")) {
                        System.out.println("↘15%이상:" + jongMok);
                        ht.put("구분", "↘15%이상");
                        ht.put("lineUp", "7");
                    }
                    return ht;
                }
                if (fRatio >= 10 && fRatio < 15) {
                    if (sign.equals("+")) {
                        System.out.println("↗10%이상:" + jongMok);
                        ht.put("구분", "↗10%이상");
                        ht.put("lineUp", "4");
                    } else if (sign.equals("-")) {
                        System.out.println("↘10%이상:" + jongMok);
                        ht.put("구분", "↘10%이상");
                        ht.put("lineUp", "8");
                    }
                    return ht;
                }
            }

            //현재가에 비한 ↗폭이나 ↘폭이 컸던 종목을 찾는다.
            float higher = 0;
            String flag = "";
            if (Math.abs(icur - ihigh) > Math.abs(icur - ilow)) {
                higher = Math.abs(icur - ihigh);
                flag = "↗↘";
            } else {
                higher = Math.abs(icur - ilow);
                flag = "↘↗";
            }
            System.out.println("higher:" + higher + "\t" + (higher / icur * 100));
            if (higher / icur * 100 > 10 && itradeCnt > 0) {
                System.out.println("10%:" + jongMok);
                ht.put("구분", "10%이상" + flag);
                ht.put("lineUp", "10");
                return ht;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) {
        new HTMLParserExample2();
    }
}
