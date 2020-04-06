package html.parsing.stock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockLinkMakeWithCode {

    final static String userHome = System.getProperty("user.home");
    private static Logger logger = LoggerFactory.getLogger(StockLinkMakeWithCode.class);

    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    // String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E ",
    // Locale.KOREAN).format(new Date());
    static String strYMD = "";
    static String inputString;

    HashMap<String, String> kospiHashMap = new HashMap<String, String>();
    HashMap<String, String> kosdaqHashMap = new HashMap<String, String>();
    HashMap<String, String> ThemeHashMap = new HashMap<String, String>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        inputString = JOptionPane.showInputDialog("성명을 입력해주세요.");
        new StockLinkMakeWithCode(1);
    }

    StockLinkMakeWithCode() {

    }

    StockLinkMakeWithCode(int i) {

        // MakeKospiKosdaqList.makeKospiKosdaqList();

        String kospiFileName = GlobalVariables.kospiFileName;
        String kosdaqFileName = GlobalVariables.kosdaqFileName;

        HashMap<String, String> kospiHashMap = readFile("코스피", kospiFileName);
        HashMap<String, String> kosdaqHashMap = readFile("코스피", kosdaqFileName);

        HashMap<String, String> selectedMap = readSourceFile(inputString + ".txt");
        System.out.println("selectedMap:" + selectedMap);

        writeFile(selectedMap, kospiFileName, "링크 목록(정렬X)");

        ValueComparator bvc = new ValueComparator(selectedMap);
        TreeMap<String, String> sorted_map = new TreeMap<String, String>(bvc);
        sorted_map.putAll(selectedMap);

        writeFile(sorted_map, kospiFileName, "링크 목록(정렬O)");

    }

    public HashMap<String, String> readSourceFile(String fileName) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            String read = null;
            int cnt = 1;
            while ((read = reader.readLine()) != null) {
                System.out.println(cnt + "." + read);
                if (kospiHashMap.containsKey(read)) {
                    hashMap.put(read, kospiHashMap.get(read));
                }
                if (kosdaqHashMap.containsKey(read)) {
                    hashMap.put(read, kosdaqHashMap.get(read));
                }
                System.out.println(hashMap);
                cnt++;
            }
            System.out.println(hashMap);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
        }
        return hashMap;
    }

    public HashMap<String, String> readFile(String kospidaq, String fileName) {
        HashMap<String, String> hashMap = new HashMap<String, String>();

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
                if (kospidaq.equals("코스피")) {
                    hashMap.put(stockName, stockCode);
                    kospiHashMap.put(stockName, stockCode);
                } else {
                    hashMap.put(stockName, stockCode);
                    kosdaqHashMap.put(stockName, stockCode);
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
        return hashMap;
    }

    public void writeFile(HashMap<String, String> list, String fileName, String title) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(
                    userHome + "\\documents\\" + strDate + "_" + title + "(" + inputString + ").html");
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
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("</tr>\r\n");

            Set<String> set = list.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                String code = list.get(key);

                sb1.append("<tr>\r\n");
                String url = "http://finance.naver.com/item/main.nhn?code=" + code;
                sb1.append("<td><a href='" + url + "'>" + key + " (" + code + ")" + "</a></td>\r\n");
                sb1.append("</tr>\r\n");
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

    public void writeFile(TreeMap<String, String> list, String fileName, String title) {
        File f = new File(userHome + "\\documents\\" + fileName);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH.mm.ss.SSS", Locale.KOREAN);
            String strDate = sdf.format(new Date());

            FileWriter fw = new FileWriter(
                    userHome + "\\documents\\" + strDate + "_" + title + "(" + inputString + ").html");
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
            sb1.append("<td style='background:#669900;color:#ffffff;text-align:center;'>종목명</td>\r\n");
            sb1.append("</tr>\r\n");

            Set<String> set = list.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                String code = list.get(key);

                sb1.append("<tr>\r\n");
                String url = "http://finance.naver.com/item/main.nhn?code=" + code;
                sb1.append("<td><a href='" + url + "'>" + key + " (" + code + ")" + "</a></td>\r\n");
                sb1.append("</tr>\r\n");
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
