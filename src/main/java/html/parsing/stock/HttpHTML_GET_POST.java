package html.parsing.stock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHTML_GET_POST {
    // private static final String rootAddr =
    // "http://www.huffingtonpost.kr/2017/04/19/story_n_16090016.html";
    // private static final String rootAddr = "http://www.huffingtonpost.kr/";

    private static final String rootAddr = "http://www.huffingtonpost.kr/2017/05/18/story_n_16677386.html?utm_hp_ref=korea";
    private static String PageSource = null;

    public static void main(String[] args) {
        final String Queuesource = rootAddr;
        PageSource = getHttpHTML_GET(Queuesource);
        PageSource = getHttpHTML_POST(Queuesource);
        System.out.println(PageSource);
    }

    private static String getHttpHTML_GET(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader br;
        String line;
        String result = "";
        String parameter = null;
        try {
            URLEncoder.encode("inp_file_1323792610_20160225131004.txt", "UTF-8");
            // parameter =
            // String.format("input_file_name=%s",URLEncoder.encode("inp_file_1323792610_20160225131004.txt","UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        // String parameter =
        // String.format("param1=%s¶m2=%s",URLEncoder.encode("param1"),URLEncoder.encode("param2"));

        try {
            url = new URL(urlToRead);
            // url = new URL(urlToRead + "?" + parameter);
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
        String parameter = null;
        try {
            URLEncoder.encode("param1", "UTF-8");
            // parameter =
            // String.format("param1=%s¶m2=%s",URLEncoder.encode("param1","UTF-8"),URLEncoder.encode("param2","UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

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
            // bw.write(parameter);
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

}
