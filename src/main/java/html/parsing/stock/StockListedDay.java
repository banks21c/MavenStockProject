package html.parsing.stock;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockListedDay {

    private static Logger logger = LoggerFactory.getLogger(StockListedDay.class);

    @Test
    public void getStockListedDay() {
        String listedDay = getStockListedDay("012450");
        logger.debug("상장일 :"+listedDay);
    }

    public static String getStockListedDay(String code) {
    	String listedDay = "";
    	Document doc;
        try {
            // 종합분석-기업개요
            doc = Jsoup.connect("http://companyinfo.stock.naver.com/v1/company/c1020001.aspx?cmp_cd=" + code).get();
            logger.debug("title:" + doc.title());
            String strDoc = doc.html();
            strDoc = strDoc.replace("&nbsp;", " ");

            doc = Jsoup.parse(strDoc);

            Element cTB201 = doc.getElementById("cTB201");

            Elements trEls = cTB201.select("tbody tr");
            for (Element tr : trEls) {
                Elements thEls = tr.select("th");
                Elements tdEls = tr.select("td");
                int thCnt = 0;
                for (Element th : thEls) {

                    String key = th.text();
                    String value = tdEls.get(thCnt).text();

                    logger.debug("key:" + key + " value:" + value);
                    if (key.equals("설립일")) {
                    	//설립일
                        String foundDay = value.substring(0, value.indexOf(" "));
                      //상장일
                        listedDay = value.substring(value.indexOf(" ")).trim().replaceAll("\\(", "")
                                .replaceAll("\\)", "").split(" ")[1];
                        logger.debug(foundDay + "===" + listedDay);
                    }
                    thCnt++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listedDay.replaceAll("/", ".");
    }

}
