package html.parsing.stock;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MootoonHttpUtils {

    public static void main(String args[]) {
        new MootoonHttpUtils();
    }

    MootoonHttpUtils() {
        String url = "http://www.mootoon.co.kr/comic/moo_view.mg";
        String param = "tcode=ndb8&cuid=p7m9n8KXsm4%3D&isview=W&in_id=qbmv5cHN2Kud3Kio";
        httpPostCallURLConnection(url, param);
    }

    public static String httpPostCallURLConnection(String urlString, String paramString) {
        String returnVal = "Error";
        // System.currentTimeMillis();

        URL url = null;

        try {

            url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 읽기와 쓰기 모두 가능하게 설정
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // 캐시를 사용하지 않게 설정
            conn.setUseCaches(false);
            // post방식
            conn.setRequestMethod("POST");
            // Timeout 설정
            conn.setConnectTimeout(6000);

            // String data = "_=1388827695612" ;
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(paramString);
            osw.flush();

            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            returnVal = builder.toString();

        } catch (MalformedURLException e) {
            // e.printStackTrace();
            returnVal = "Error: MalformedURLException";
        } catch (IOException e) {
            // e.printStackTrace();
            returnVal = "Error: IOException";
        } catch (Exception e) {
            // e.printStackTrace();
            returnVal = "Error: " + e.toString();
        }

        System.out.println(returnVal);

        return returnVal;
    }

    public static String httpMultipartCallURLConnection(String urlString, String fullFileName, String fileName) {
        String returnVal = "Error";
        String boundary = "dkjsei40f9844djs8dviwdf";
        // System.currentTimeMillis();

        System.out.println("url: " + urlString);
        System.out.println("fullFileName: " + fullFileName);
        System.out.println("fileName: " + fileName);

        URL url = null;

        try {

            File file = new File(fullFileName);
            if (file.exists()) {

                FileInputStream fileInputStream = new FileInputStream(file);
                // url = new URL(GlobalStatic.BLACKBOX_URL + urlString);
                url = new URL("http://jlmediabox.com/common/uploadFile.do");
                System.out.println("url: " + url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                // 읽기와 쓰기 모두 가능하게 설정
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // 캐시를 사용하지 않게 설정
                conn.setUseCaches(false);
                // post방식
                conn.setRequestMethod("POST");
                // Timeout 설정
                // conn.setConnectTimeout(2000);

                // 헤더 설정
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                // Output스트림을 열어
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes("--" + boundary + "\r\n");
                // dos.writeBytes("Content-Disposition: form-data;
                // name=\"fw_file\";filename=\""+ fileName +"\"" + "\r\n");
                dos.writeBytes(
                        "Content-Disposition: form-data; name=\"uploadFile\";filename=\"" + fileName + "\"" + "\r\n");
                dos.writeBytes("\r\n");

                // 버퍼사이즈를 설정하여 buffer할당
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                // 스트림에 작성
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    // Upload file part(s)
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes("\r\n");
                dos.writeBytes("--" + boundary + "--" + "\r\n");
                fileInputStream.close();

                // 써진 버퍼를 stream에 출력.
                dos.flush();

                // 전송. 결과를 수신.
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;

                while ((str = reader.readLine()) != null) {
                    builder.append(str);
                }

                returnVal = builder.toString();
            } else {
                returnVal = "Error: 파일이 존재하지 않습니다.";
            }

        } catch (MalformedURLException e) {
            // e.printStackTrace();
            returnVal = "Error: MalformedURLException";
        } catch (IOException e) {
            e.printStackTrace();
            returnVal = "Error: IOException";
        } catch (Exception e) {
            // e.printStackTrace();
            returnVal = "Error: " + e.toString();
        }

        System.out.println(returnVal);

        return returnVal;
    }

    public static String httpMultipartCallURLConnection(String urlString, String fileName,
            InputStream fileInputStream) {
        // TODO Auto-generated method stub
        String returnVal = "Error";
        String boundary = "dkjsei40f9844djs8dviwdf";
        // System.currentTimeMillis();

        System.out.println("url: " + urlString);
        System.out.println("fileName: " + fileName);
        URL url = null;

        try {

            url = new URL("" + urlString);
            // url = new URL("http://jlmediabox.com/common/uploadFile.do");
            System.out.println("url: " + url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // 읽기와 쓰기 모두 가능하게 설정
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // 캐시를 사용하지 않게 설정
            conn.setUseCaches(false);
            // post방식
            conn.setRequestMethod("POST");
            // Timeout 설정
            // conn.setConnectTimeout(2000);

            // 헤더 설정
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // Output스트림을 열어
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes("--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"fw_file\";filename=\"" + fileName + "\"" + "\r\n");
            dos.writeBytes("\r\n");

            // 버퍼사이즈를 설정하여 buffer할당
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // 스트림에 작성
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                // Upload file part(s)
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes("\r\n");
            dos.writeBytes("--" + boundary + "--" + "\r\n");
            fileInputStream.close();

            // 써진 버퍼를 stream에 출력.
            dos.flush();

            // 전송. 결과를 수신.
            InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            returnVal = builder.toString();

        } catch (MalformedURLException e) {
            // e.printStackTrace();
            returnVal = "Error: MalformedURLException";
        } catch (IOException e) {
            // e.printStackTrace();
            returnVal = "Error: IOException";
        } catch (Exception e) {
            // e.printStackTrace();
            returnVal = "Error: " + e.toString();
        }

        System.out.println(returnVal);

        return returnVal;
    }

}
