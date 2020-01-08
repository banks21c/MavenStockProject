package html.parsing.stock;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;import org.slf4j.Logger;import org.slf4j.LoggerFactory;


import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadImages {

    // The url of the website. This is just an example
    // private static final String webSiteURL =
    // "http://news.naver.com/main/ranking/read.nhn?mid=etc&sid1=111&rankingType=popular_day&oid=001&aid=0009840594&date=20180127&type=1&rankingSeq=1&rankingSectionId=101";
    private static final String webSiteURL = "http://news.naver.com/main/read.nhn?mode=LSD&mid=shm&sid1=101&oid=008&aid=0003999234";

    // The path of the folder that you want to save the images to
    private static final String folderPath = "C:\\Users\\parsing-25\\Pictures";

    public static void main(String[] args) {

        try {

            // Connect to the website and get the html
            Document doc = Jsoup.connect(webSiteURL).get();

            // Get all elements with img tag ,
            Elements img = doc.getElementsByTag("img");

            for (Element el : img) {

                // for each element get the srs url
                String src = el.absUrl("src");

                System.out.println("Image Found!");
                System.out.println("src attribute is : " + src);
                if (!src.equals("")) {
                    getImages(src);
                    getImage(src);
                }

            }

        } catch (IOException ex) {
            System.err.println("There was an error");
            java.util.logging.Logger.getLogger(DownloadImages.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getImages(String src) throws IOException {
        // src =
        // "http://imgnews.naver.net/image/001/2018/01/27/PYH2018012702450007300_P2_20180127104932448.jpg?type=w647";

        String folder = null;

        // Exctract the name of the image from the src attribute
        int idx = src.lastIndexOf("/");

        if (idx == src.length()) {
            src = src.substring(1, idx);
        }

        idx = src.lastIndexOf("/");
        System.out.println("idx:" + idx);
        String name = "";

        if (idx != -1) {
            name = src.substring(idx, src.length());
        }

        if (name.indexOf("?") != -1) {
            name = name.substring(0, name.indexOf("?"));
        }
        if (name.indexOf("%") != -1) {
            System.out.println("name1...:" + name);
            name = URLDecoder.decode(name, "EUC-KR");
            System.out.println("name2...:" + name);
            name = URLDecoder.decode(name, "EUC-KR");
            System.out.println("name3...:" + name);
        }
        System.out.println(name);

        // Open a URL Stream
        URL url = new URL(src);
        InputStream in = url.openStream();

        OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));

        for (int b; (b = in.read()) != -1;) {
            out.write(b);
        }
        out.close();
        in.close();

    }

    public static Image getImage(String src) {
        URL url;
        BufferedImage img = null;
        try {
            url = new URL(src);
            img = ImageIO.read(url);
            int width = img.getWidth();
            int height = img.getHeight();
            System.out.println("width:" + width);
            System.out.println("height:" + height);
            if (width > height && width > 548) {
                height = (548 * height) / width;
                width = 548;
            }
            // 630x323
            System.out.println("width:" + width);
            System.out.println("height:" + height);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

}
