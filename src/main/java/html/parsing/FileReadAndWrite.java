package html.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReadAndWrite {

    public FileReadAndWrite() {
        String kospiFileName = "kospi.html";
        String kosdaqFileName = "kosdaq.html";
        readFile(kospiFileName);
        readFile(kosdaqFileName);
    }

    public void readFile(String fileName) {
        String USER_HOME = System.getProperty("user.home");
        File f = new File(USER_HOME + "\\documents\\" + fileName);
        try {
            //FileReader reader = new FileReader(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF8"));

            FileWriter fw = new FileWriter(USER_HOME + "\\documents\\new_" + f.getName());
            String read = null;
            StringBuffer sb1 = new StringBuffer();
            while ((read = reader.readLine()) != null) {
                if (read.indexOf("Jong_Winopen") != -1) {
                    System.out.println(read);
                    int Jong_Winopen_Index = read.indexOf("Jong_Winopen");
                    int Jong_Winopen_length = "Jong_Winopen".length();
                    String code = read.substring(Jong_Winopen_Index + Jong_Winopen_length + 2, Jong_Winopen_Index + Jong_Winopen_length + 2 + 6);
                    System.out.println(code);
                    sb1.append(code + "\r\n");
                }
            }
            fw.write(sb1.toString());
            fw.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {

        }

    }

    public static void main(String args[]) {
        new FileReadAndWrite();
    }
}
