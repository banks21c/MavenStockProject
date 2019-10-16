package html.parsing;

import java.io.IOException;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParserExample1 {

    /**
     * @param args
     */
    public static void main(String[] args) {

        Document doc;
        try {

            doc = Jsoup.connect("http://finance.naver.com/item/main.nhn?code=004720").get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            Elements dds = doc.select("dd");

            Hashtable ht = new Hashtable();
            for (Element dd : dds) {

                // get the value from href attribute
                String priceTxt = dd.text();
                if (priceTxt.indexOf("가") != -1 && priceTxt.matches("(.*)[0-9]+(.*)")) {
                    String priceSplit[] = priceTxt.replaceAll(",", "").split(" ");
                    ht.put(priceSplit[0], priceSplit[1]);
                    System.out.println(priceSplit[0] + ":" + priceSplit[1]);
                }
            }
            boolean sang = false;
            boolean ha = false;
            boolean sangTouch = false;
            boolean haTouch = false;

            if (ht.get("현재가").equals(ht.get("상한가"))) {
                sang = true;
            }
            if (ht.get("현재가").equals(ht.get("하한가"))) {
                ha = true;
            }
            if (ht.get("고가").equals(ht.get("상한가")) && !ht.get("현재가").equals(ht.get("상한가"))) {
                sangTouch = true;
            }
            if (ht.get("저가").equals(ht.get("하한가")) && !ht.get("현재가").equals(ht.get("하한가"))) {
                haTouch = true;
            }
            System.out.println("상한가인가?" + sang);
            System.out.println("하한가인가?" + ha);
            System.out.println("상한가 터치인가?" + sangTouch);
            System.out.println("하한가 터치인가?" + haTouch);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
