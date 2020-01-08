/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author parsing-25
 */
public class URLTest {

    public static void main(String args[]) throws MalformedURLException, IOException, FileNotFoundException {
        try {
            String sourceUrl = "http://images.joins.com/ui_joongang/news/pc/common/i_ad_news.png";
            System.out.println("sourceUrl:" + sourceUrl);

            String fileName = FilenameUtils.getName(sourceUrl);
            System.out.println("fileName:" + fileName);
            if (fileName == null || fileName.equals("")) {
                fileName = "index.html";
            }

            System.out.println("targetUrl:" + System.getProperty("user.home") + File.separator + "Pictures" + File.separator + fileName);
            URL imageUrl = new URL(sourceUrl);
            try (InputStream imageReader = new BufferedInputStream(imageUrl.openStream());
                    OutputStream imageWriter = new BufferedOutputStream(
                            new FileOutputStream(System.getProperty("user.home") + File.separator + "Pictures" + File.separator + FilenameUtils.getName(sourceUrl)));) {
                int readByte;

                while ((readByte = imageReader.read()) != -1) {
                    imageWriter.write(readByte);
                }
            }
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger(URLTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
