package html.parsing.stock;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

import org.apache.commons.io.FilenameUtils;

public class FileDownloader {

    public static void main(String[] arguments) throws IOException {
        System.out.println("new File(\"\").getAbsolutePath():" + new File("").getAbsolutePath());
        downloadFile2("https://upload.wikimedia.org/wikipedia/commons/7/73/Lion_waiting_in_Namibia.jpg");
        downloadFile2("http://www.edaily.co.kr/resources/css/component_ui.css");
    }

    public static void downloadFile(String sourceUrl, String targetDirectory) throws MalformedURLException, IOException, FileNotFoundException {
        URL sourceFileUrl = new URL(sourceUrl);
        String fileName = FilenameUtils.getName(sourceUrl);
        System.out.println("fileName:" + fileName);
        if (fileName == null || fileName.equals("")) {
            fileName = "index.html";
        }
        String targetDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + fileName;
        try (InputStream is = new BufferedInputStream(sourceFileUrl.openStream());
                OutputStream os = new BufferedOutputStream(new FileOutputStream(targetDir));) {
            int readByte;

            while ((readByte = is.read()) != -1) {
                os.write(readByte);
            }
        }
    }

    public static void downloadFile2(String sourceUrl) {
        java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, "sourceUrl:" + sourceUrl);
        java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, "sourceUrl.substring(sourceUrl.length()):" + sourceUrl.substring(sourceUrl.length() - 1));
        if (sourceUrl != null && sourceUrl.substring(sourceUrl.length() - 1).equals("/")) {
            sourceUrl = sourceUrl.substring(0, sourceUrl.length() - 1);
        }
        sourceUrl = sourceUrl.replaceAll(" ", "%20");

        URL sourceFileUrl = null;
        try {
            sourceFileUrl = new URL(sourceUrl);
            String protocol = sourceFileUrl.getProtocol();
            System.out.println("protocol:" + protocol);
            String host = sourceFileUrl.getHost();
            System.out.println("host:" + host);
            String path = sourceFileUrl.getPath();
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, "path1:" + path);
            if (path != null && !path.equals("") && path.indexOf("/") != -1) {
                path = path.substring(0, path.lastIndexOf("/"));
                System.out.println("path==>" + path);
                path = path.replaceAll("[/]", "\\" + File.separator);
            }
            System.out.println("path2:" + path);
            File f = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + host + path);
            if (!f.exists()) {
                f.mkdirs();
            }
            System.out.println("AbsolutePath:" + f.getAbsolutePath());
            System.out.println("CanonicalPath:" + f.getCanonicalPath());
            String fileName = FilenameUtils.getName(sourceUrl);
            System.out.println("fileName:" + fileName);
            if (!fileName.contains(".")) {
                fileName = fileName + ".html";
            }
            if (fileName.startsWith("?")) {
                String tempParam = fileName.substring(fileName.lastIndexOf("?") + 1);
                String paramKeyValues[] = tempParam.split("&");
                String firstParamKeyValue = paramKeyValues[0];
                String firstParamKeyValuePair[] = firstParamKeyValue.split("=");
                fileName = firstParamKeyValuePair[1];
            } else if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.indexOf("?"));
            }

            String targetDir = System.getProperty("user.home") + File.separator + "Documents" + File.separator + host + path + File.separator + fileName;

            System.out.println("sourceFileUrl:" + sourceFileUrl);
            System.out.println("targetDir:" + targetDir);
            fileWrite(sourceFileUrl, targetDir);
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void fileWrite(URL sourceFileUrl, String targetDir) {
        System.out.println("sourceFileUrl:" + sourceFileUrl);
        System.out.println("targetDir:" + targetDir);
        System.out.println("sourceFileUrl.toString():" + sourceFileUrl.toString());

        try {
            InputStream is = new URL("http://search.joins.com?keyword=모바일%20택시").openStream();
            System.out.println("is:" + is);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (InputStream is = new BufferedInputStream(sourceFileUrl.openStream())) {
            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(targetDir))) {
                int readByte;

                while ((readByte = is.read()) != -1) {
                    os.write(readByte);
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
