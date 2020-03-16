package html.parsing.stock.news;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;

import javax.swing.JOptionPane;

public class FinanceNaverComWorld {

    FinanceNaverComWorld() {
        // urlOpenConnection();
        //printUrlOpenStream();
        urlOpenStream();
        writeFromUrlOpenStream();
    }

    public void printUrlOpenStream() {
        URL url;
        String strUrl = "https://finance.naver.com/world/";
        try {
        	url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"KSC5601"));
            
            String inputLine;
            int lineNumber = 1;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(lineNumber+" : "+inputLine);
                lineNumber++;
            }
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void urlOpenStream() {
        URL url;
        String strUrl = "https://finance.naver.com/world/";
        try {
            url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"KSC5601"));
            String inputLine;
            int start = 0;
            int lineNumber = 1;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();
                System.out.println(lineNumber+" : "+inputLine);
                lineNumber++;

                if (inputLine.indexOf("<title>") != -1) {
                    inputLine = inputLine.substring("<title>".length(), inputLine.indexOf(":"));
                    System.out.println("<h1>" + inputLine.trim() + "</h1>");
                }
            }
            System.out.println("</ul>");
            System.out.println("<BR><BR>");
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFromUrlOpenStream() {
        URL url;
        String strUrl = "https://finance.naver.com/world/";
        try {
            url = new URL(strUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"KSC5601"));
            BufferedWriter out = null;
            String inputLine;
            int start = 0;
            while ((inputLine = in.readLine()) != null) {
                inputLine = inputLine.trim();
                if (inputLine.indexOf("<title>") != -1) {
                    inputLine = inputLine.substring("<title>".length(), inputLine.indexOf(":"));
                    out = new BufferedWriter(new FileWriter(inputLine.trim() + "_" + "_기업개요.html"));
                    out.write("<h1>" + inputLine.trim() + " " + "</h1>\n");
                    System.out.println("<h1>" + inputLine.trim() + "</h1>");
                }
                if (inputLine.indexOf("<h4>기업개요</h4>") != -1) {
                    start = 1;
                    out.write(inputLine + "\n");
                    out.write("<ul>\n");
                    System.out.println(inputLine + "\n");
                    System.out.println("<ul>\n");
                    continue;
                }
                if (start == 1) {
                    if (inputLine.indexOf("<div class=\"txt_notice\">") != -1) {
                        start = 0;
                    } else {
                        inputLine = inputLine.replaceAll("<p>", "<li>");
                        inputLine = inputLine.replaceAll("</p>", "</li>");
                        out.write(inputLine + "\n");
                        System.out.println(inputLine + "\n");
                    }
                }
            }
            out.write("</ul>\n");
            out.write("<BR/><BR/>");
            System.out.println("</ul>");
            System.out.println("<BR/><BR/>");
            in.close();
            out.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void urlOpenConnection() {
        URL url;
        URLConnection uc;
        HttpURLConnection hurlc = null;
        String fcode = "102280";

        try {
            url = new URL("http://finance.naver.com/item/coinfo.nhn?code=102280");
            try {
                uc = url.openConnection();
                System.out.println(uc.getContent());
                System.out.println(uc.getContentEncoding());
                System.out.println(uc.getContentLength());
                System.out.println(uc.getContentLengthLong());
                System.out.println(uc.getContentType());

                hurlc = (HttpURLConnection) url.openConnection();
                hurlc.setRequestMethod("GET");
                hurlc.setDoOutput(true);
                hurlc.setReadTimeout(1000);
                hurlc.setConnectTimeout(1000);

                hurlc.setRequestProperty("Connection", "Keep-Alive");
                hurlc.setRequestProperty("Cookie", "name1=value1; name2=value2; name3=value3");
                hurlc.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36");
                hurlc.connect();

                FileOutputStream fos;
                ReadableByteChannel rbc;

                InputStream is = hurlc.getInputStream();

                // rbc = Channels.newChannel(is);
                // String fileName = fcode + ".html";
                // fos = new FileOutputStream(fileName);
                // fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                // fos.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        } catch (MalformedURLException e) {
            System.err.println(e);
        }
    }

    public static void main(String args[]) {
        new FinanceNaverComWorld();
    }

}
