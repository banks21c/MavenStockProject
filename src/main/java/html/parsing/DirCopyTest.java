package html.parsing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class DirCopyTest {

    public DirCopyTest() {
        File srcDir = new File("C:\\Temp\\HncDownload");
        File destDir = new File("C:\\Temp\\HncDownload2");
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DirCopyTest();
    }

}
