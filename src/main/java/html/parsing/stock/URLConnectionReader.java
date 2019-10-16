package html.parsing.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class URLConnectionReader {

    private static String getHttpHTML_GET(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader br;
        String line;
        String result = "";
        String parameter = String.format("input_file_name=%s", URLEncoder.encode("input_file_name=inp_file1004.txt"));
        // String parameter =
        // String.format("param1=%s¶m2=%s",URLEncoder.encode("param1"),URLEncoder.encode("param2"));

        try {
            // url = new URL(urlToRead);
            url = new URL(urlToRead + "?" + parameter);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            // br = new BufferedReader(new
            // InputStreamReader(conn.getInputStream(),"euc-kr"));
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static String getHttpHTML_POST(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader br;
        BufferedWriter bw;
        String line;
        String result = "";
        String parameter = String.format("param1=%s¶m2=%s", URLEncoder.encode("param1"), URLEncoder.encode("param2"));

        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // 파라메터가 없으면 지워주면 된다.
            bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            bw.write(parameter);
            // 요기까지

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            // br = new BufferedReader(new
            // InputStreamReader(conn.getInputStream(),"euc-kr"));
            while ((line = br.readLine()) != null) {
                result += line + "\n";
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        getHttpHTML_POST("http://www.huffingtonpost.kr/2017/04/19/story_n_16090016.html");

        URL oracle = new URL("http://www.huffingtonpost.kr/2017/04/19/story_n_16090016.html");
        System.out.println("oracle:" + oracle);
        URLConnection yc = oracle.openConnection();
        System.out.println("yc:" + yc);
        InputStream is = yc.getInputStream();
        System.out.println("is:" + is);
        InputStreamReader isr = new InputStreamReader(is);
        System.out.println("isr:" + isr);
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        System.out.println("in:" + in);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }
}
