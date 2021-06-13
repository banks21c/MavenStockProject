

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import html.parsing.stock.news.News;
import html.parsing.stock.news.NewsInterface;
import html.parsing.stock.util.FileUtil;

public class GoogleStockCrawler extends News implements NewsInterface {

    private static Logger logger = LoggerFactory.getLogger(GoogleStockCrawler.class);
    private char[] alphabets = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
//    private char[] alphabets = {'A'};
    String strYear = new SimpleDateFormat("yyyy", Locale.KOREAN).format(new Date());
    int iYear = Integer.parseInt(strYear);

    String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E mm.ss.SSS ",
            Locale.KOREAN).format(new Date());
    static String strDate = null;
    static String strTitle = null;

    DecimalFormat df = new DecimalFormat("###.##");

    /**
     * @param args
     */
    public static void main(String[] args) {
        new GoogleStockCrawler(1);
    }

    GoogleStockCrawler() {

    }

    GoogleStockCrawler(int i) {

        createHTMLFile();
    }

    public StringBuilder createHTMLFile() {
        logger.debug("alphabets.length:" + alphabets.length);
        StringBuilder sb1 = new StringBuilder();
        Document doc = null;
        String strTitleForFileName = "NYSE 알파벳 A-Z 주식";
        String strFileNameDate = strYMD;
        try {

            for (int i = 0; i < alphabets.length; i++) {
                char alphabet = alphabets[i];
                System.out.println("alphabet:"+alphabet);
                String strUrl = "https://www.google.com/search?q=NYSE:+" + alphabet;
                System.out.println("strUrl:"+strUrl);
                doc = Jsoup.connect(strUrl).get();
                doc.select("script").remove();
                doc.select("iframe").remove();
                doc.select("gb_5e").remove();
                doc.select(".u47KMb").remove();

                String bodyHtml = doc.select("#knowledge-finance-wholepage__entity-summary").html();
                doc.select("body").html(bodyHtml);
                sb1.append(doc.select("body").html());
            }
            doc.select("body").html(sb1.toString());
            logger.debug("doc:" + doc.html());

            File dir = new File(USER_HOME + File.separator + "documents" + File.separator + host);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = "";

            fileName = USER_HOME + File.separator + "documents" + File.separator + strFileNameDate + "_"
                    + strTitleForFileName + ".html";
            FileUtil.fileWrite(fileName, doc.html());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("추출완료");
        }
        return sb1;
    }

}
