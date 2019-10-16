/**
 *
 */
package html.parsing.stock;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;

/**
 * @author parsing-25
 *
 */
public class Base64StringToImage {

    /**
     *
     */
    public Base64StringToImage() {
        // TODO Auto-generated constructor stub
    }

    public static String getName(String strUrl) {
        System.out.println("strUrl:" + strUrl);
        String f = strUrl;
        String name = f.substring(f.lastIndexOf('/') + 1);
        System.out.println("org name:" + name);
        if (name.indexOf("?") != -1) {
            name = name.substring(0, name.indexOf("?"));
        }
        System.out.println("org name:" + name);
        try {
            name = URLDecoder.decode(name, "UTF8");
            name = name.replaceAll("\\?", "_");
            name = name.replaceAll("\\&", "_");
            name = name.replaceAll("\\;", "_");
            if (name.indexOf(":") != -1) {
                name = name.substring(name.indexOf(":") + 1);
            }
            if (name.indexOf(".") == -1) {
                name = name + ".jpg";
            }
            System.out.println("mod name:" + name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getName(String strUrl, String fileExt) {
        System.out.println("strUrl:" + strUrl);
        String f = strUrl;
        String name = f.substring(f.lastIndexOf('/') + 1);
        System.out.println("org name:" + name);
        if (name.indexOf("?") != -1) {
            name = name.substring(0, name.indexOf("?"));
        }
        System.out.println("org name:" + name);
        try {
            name = URLDecoder.decode(name, "UTF8");
            name = name.replaceAll("\\?", "_");
            name = name.replaceAll("\\&", "_");
            name = name.replaceAll("\\;", "_");
            if (name.indexOf(":") != -1) {
                name = name.substring(name.indexOf(":") + 1);
            }
            if (name.indexOf(".") == -1) {
                name = name + "." + fileExt;
            }
            System.out.println("mod name:" + name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    public static String getName(URL url) {
        System.out.println("urlGetFile:" + url.getFile());
        return getName(url.getFile());
    }

    public static HttpURLConnection getHttpURLCon(String strUrl) {
        HttpURLConnection hurlc = null;
        try {
            URL url = new URL(strUrl);
            hurlc = (HttpURLConnection) url.openConnection();
            // hurlc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;
            // Windows NT 5.0)");
            hurlc.addRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
            hurlc.connect();

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        return hurlc;

    }

    public static void saveImageFile(String strUrl, String saveDir, int index) {
        System.out.println("이미지 URL:" + strUrl);
        System.out.println("이미지 저장 폴더:" + saveDir);
        try {
            int slushIdx = strUrl.lastIndexOf("/");
            String fileName = "";
            if (strUrl.length() > slushIdx) {
                fileName = getName(strUrl);
            } else {
                return;
            }
            HttpURLConnection hurlc = getHttpURLCon(strUrl);
            if (hurlc != null) {
                ReadableByteChannel rbc;
                FileOutputStream fos;
                rbc = Channels.newChannel(hurlc.getInputStream());
                String outFileName = saveDir + "/" + index + "." + getName(new URL(strUrl));

                fos = new FileOutputStream(outFileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();

                /* 저장된 이미지를 읽어들여 작은 적은 삭제 */
                File f = new File(outFileName);
                BufferedImage bi = ImageIO.read(f);
                int width = bi.getWidth();
                int height = bi.getHeight();
                if (width < 60 && height < 60) {
                    f.delete();
                } else {
                    System.out.println("saveImageFile 검색된 결과가 " + index + "." + fileName + " 파일에 저장되었습니다.");
                }
            } else {
                System.out.println("HttpURLConnection 실패");
            }
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다 :" + strUrl);
            System.out.println(e);
        } catch (Exception ex) {
            System.out.println("저장중 에러난 URL:" + strUrl);
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public static void decodeToImage(String imageString, String type, String dirName, String fileName) {
        try {
            Decoder decoder = Base64.getDecoder();
            byte[] bytearray = decoder.decode(imageString);
            BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
            ImageIO.write(imag, type, new File(dirName, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("base64 string image converted to image successfully");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String base64String = "R0lGODlhMAAKAJECAAbMbQrrf////wAAACH/C05FVFNDQVBFMi4wAwEAAAAh/wtYTVAgRGF0YVhNUDw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6OTM1RjFCNzk4OUQyMTFFNkIxMUNDQUJBRDA4RDA2QjQiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6OTM1RjFCN0E4OUQyMTFFNkIxMUNDQUJBRDA4RDA2QjQiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo5MzVGMUI3Nzg5RDIxMUU2QjExQ0NBQkFEMDhEMDZCNCIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo5MzVGMUI3ODg5RDIxMUU2QjExQ0NBQkFEMDhEMDZCNCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJ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";
        // decodeToImage(base64String, "gif", System.getProperty("user.home"),
        // "NoName.gif");

        // String url =
        // "http://video.phinf.naver.net/20180102_144/1514876640193MlPOl_JPEG/1aa67a92-ef8b-11e7-be85-000000008ca5_02.jpg?type=s100";
        // String url =
        // "http://video.phinf.naver.net/20180102_40/1514876640373EoLY3_JPEG/1aa67a92-ef8b-11e7-be85-000000008ca5_04.jpg?type=s100";
        // saveImageFile(url, System.getProperty("user.home"), 1);
        // fileWriteMethod1("http://tv.naver.com/v/2494051");
        // fileWriteMethod2("http://tv.naver.com/v/2494051");
        // fileWriteMethod2("https://www.atomy.kr/v2/Home/Account/Login?rpage=Home%2FAccount%2FMemberJoin_Step1");
        fileWriteMethod2("https://www.atomy.kr/v2/bundles/Home/Common?v=9ZDPeJrfvZXDTfQCs6QIDKhHyqixlKcgoxQcLe6QgsE1",
                "js");
        fileWriteMethod2("https://www.atomy.kr/v2/Content/Home/Common?v=7BoYBQ7RYO6AsmNOl6oaLkBJDPa6UbKXpsSaxJNudZI1",
                "css");
    }

    public static void fileWriteMethod1(String urlStr) {
        System.out.println("method1");
        String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS ", Locale.KOREAN).format(new Date());
        System.out.println("strYMD:" + strYMD);
        InputStream in = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(urlStr);

            in = new BufferedInputStream(url.openStream());
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int count = 0;
            while ((count = in.read(buf)) != -1) {
                baos.write(buf, 0, count);
            }
            baos.close();
            in.close();
            byte[] response = baos.toByteArray();

            FileOutputStream fos = new FileOutputStream(
                    System.getProperty("user.home") + "/" + strYMD + getName(urlStr, "html"));
            fos.write(response);
            fos.close();

        } catch (MalformedURLException murl) {
            murl.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void fileWriteMethod2(String urlStr, String fileExt) {
        System.out.println("method2");
        String strYMD = new SimpleDateFormat("yyyy년 M월 d일 E HH.mm.ss.SSS ", Locale.KOREAN).format(new Date());
        System.out.println("strYMD:" + strYMD);
        BufferedInputStream in = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(urlStr);

            in = new BufferedInputStream(url.openStream());
            fos = new FileOutputStream(System.getProperty("user.home") + "/" + strYMD + getName(urlStr, fileExt));

            final byte buf[] = new byte[1024];
            int count = 0;
            while ((count = in.read(buf, 0, 1024)) != -1) {
                fos.write(buf, 0, count);
            }
            fos.close();
            in.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void fileWriteMethod2(String urlStr) {
        fileWriteMethod2(urlStr, "html");
    }

}
