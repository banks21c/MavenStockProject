/**
 *
 */
package html.parsing.stock;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author banks
 *
 */
public class MakeKospiKosdaqFinanceList {

    final static String userHome = System.getProperty("user.home");
    final static String[] fundNames = {"GIANT", "KTOP", "PIONEER", "마이다스", "마이티",
        "파워", "흥국 S&P", "ARIRANG", "KBSTAR",
        "KINDEX", "KODEX", "KOSEF", "QV", "SMART", "TIGER", "TREX", "TRUE",
        "able", "대우", "동양"};

    /**
     *
     */
    public MakeKospiKosdaqFinanceList(String[] keywordArray) {
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd", "KOSPI", keywordArray);
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd", "KOSDAQ", keywordArray);
    }

    public static void makeKospiKosdaqFinanceList(String[] keywordArray) {
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSPI&orderBy=dd", "KOSPI", keywordArray);
        readMkURL("http://vip.mk.co.kr/newSt/rate/item_all.php?koskok=KOSDAQ&orderBy=dd", "KOSDAQ", keywordArray);
    }

    public static String convertArrayToString(String[] arr) {
        StringBuilder builder = new StringBuilder();
        for (String s : arr) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static String joinArrayToString(String[] arr) {
        String joinedString = StringUtils.join(arr, ",");
        System.out.println("StringUtils.join:" + joinedString);
        return joinedString;
    }

    public static String joinArrayToString2(String[] arr) {
        String joinedString = String.join(",", arr);
        System.out.println("String.join:" + joinedString);
        return joinedString;
    }

    public static String arraysToString(String[] arr) {
        String joinedString = Arrays.toString(arr);
        System.out.println("Arrays.toString:" + joinedString);
        return joinedString;
    }

    public static Hashtable<String, String> readMkURL(String url, String div, String[] keywordArray) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            System.out.println(doc.html());

            FileWriter fw = new FileWriter(
                    userHome + "\\documents\\new_" + div + "_" + convertArrayToString(keywordArray) + ".html", false);

            Elements edds = doc.select(".st2");
            Iterator<Element> it = edds.iterator();
            Hashtable<String, String> codeNameHt = new Hashtable<String, String>();
            while (it.hasNext()) {
                Element e = it.next();
                String attrName = e.html();
                Document d = Jsoup.parse(attrName);

                Element atag = d.getElementsByTag("a").get(0);

                String itemName = atag.html();
                itemName = itemName.replaceAll("&amp;", "&");
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
                            for (String s : keywordArray) {
                                if (itemName.indexOf(s) != -1) {
                                    codeNameHt.put(itemName, itemCode);
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

    /**
     * @param args
     */
    public static void main(String[] args) {
        String keywordArray[] = {"증권", "금융", "투자", "생명", "은행", "지주"};
        // new MakeKospiKosdaqFinanceList(keywordArray);
        // new MakeKospiKosdaqFinanceList(new String[]{"증권","금융","투자","생명","은행","지주"});
        joinArrayToString(keywordArray);
        joinArrayToString2(keywordArray);
        arraysToString(keywordArray);

        String[] strArray = new String[]{"Java", "PHP", ".NET", "PERL", "C",
            "COBOL"};
        String newString = Arrays.toString(strArray);
        newString = newString.substring(1, newString.length() - 1);
        System.out.println("New New String: " + newString);

        String[] array = {"cat", "mouse"};
        String delimiter = ",";
        String result = String.join(delimiter, array);
        System.out.println("result:" + result);
    }

}
