/**
 * 
 */
package test;

import java.io.File;

/**
 * @author banksfamily
 *
 */
public class FileNameRenameTest {

	/**
	 * 
	 */
	public FileNameRenameTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File dir = new File("C:\\Users\\banksfamily\\Pictures\\1차 이미지");
		System.out.println("AbsolutePath:" + dir.getAbsolutePath());
		if (dir.isDirectory()) {
			File files[] = dir.listFiles();
			for (File f : files) {
				String fileName = f.getName();
				System.out.println("fileName:" + fileName);
				String fileName2 = "";
				if (fileName.contains("건강 및 당뇨 이미지")) {
					fileName2 = "건강및당뇨이미지" + fileName.substring("건강 및 당뇨 이미지".length());
					System.out.println("fileName2:" + fileName2);
					System.out.println(dir.getAbsolutePath() + "\\another\\" + fileName2);
					boolean b = f.renameTo(new File(dir.getAbsolutePath() + "\\" + fileName2));
					System.out.println("b:"+b);
					
				}
			}
		}
	}

}
