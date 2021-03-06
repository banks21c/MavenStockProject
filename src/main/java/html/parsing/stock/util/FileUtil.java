/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 *
 * @author parsing-25
 */
public class FileUtil {

	public static void simpleFileWrite1(String fileName, String strContent) {
//		FileWriter fw = null;
		Writer fw = null;
		try {
			fw = new FileWriter(fileName);
			fw.write(strContent);
			fw.flush();
		} catch (FileNotFoundException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fw.close();
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void simpleFileWrite2(String fileName, String strContent) {
		Writer writer = null;
		BufferedWriter bw = null;
		try {
			writer = new FileWriter(fileName);
			bw = new BufferedWriter(writer);
			bw.write(strContent);
			bw.flush();
		} catch (FileNotFoundException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public static void fileWrite(String fileName, String strContent) {
		Writer writer = null;
		BufferedWriter bw = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);
			bw = new BufferedWriter(writer);
			bw.write(strContent);
			bw.flush();
		} catch (FileNotFoundException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		java.util.logging.Logger.getLogger(FileUtil.class.getName()).log(Level.INFO, "파일 쓰기 완료", "NO PARAM");
		System.out.println(fileName+" 파일을 저장하였습니다.");
	}
}
