/**
 * 
 */
package html.parsing.stock.news;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import html.parsing.stock.util.FileUtil;

/**
 * @author banksfamily
 *
 */
public class CreateNewsReadClass {

	/**
	 * 
	 */
	public CreateNewsReadClass() {
		String newsFileName = "WwwFortunekoreaCoKr";
		String newsTitleClass = ".article-view-header .article-header-wrap .article-head-title";
		String newsWriterClass = ".article-view-header .info-text li";
		String newsWriterClassIdx = "0";
		String newsDateClass = ".article-view-header .info-text li";
		String newsDateClassIdx = "1";
		String newsArticleClass = "#article-view-content-div";
		String copyrightClass = ".article-veiw-body .view-copyright";
		String sampleUrl = "http://www.fortunekorea.co.kr/news/articleView.html?idxno=11105";
		
		try {
//			String file = "D:\\workspace-spring-tool-suite-4-4.3.1.RELEASE\\MavenStockProject\\src\\main\\java\\html\\parsing\\stock\\news\\NewsTemplate.txt";
//			String file = "D:\\workspace-spring-tool-suite-4-4.3.1.RELEASE\\MavenStockProject\\target\\classes\\html\\parsing\\stock\\news\\NewsTemplate.txt";
			String file = "/D:/workspace-spring-tool-suite-4-4.3.1.RELEASE/MavenStockProject/target/classes/html/parsing/stock/news/NewsTemplate.txt";
			File f = new File(".");
			System.out.println(f.getAbsolutePath());
			
	        ClassLoader loader = CreateNewsReadClass.class.getClassLoader();
//	        loader.getResources(".");
	        System.out.println(this.getClass().getName());
	        String className = this.getClass().getName();
	        String pkgName = this.getClass().getPackage().getName();
	        String classPath = className.replace(".", "/");
	        String pkgPath = pkgName.replace(".", "/");
	        System.out.println(classPath);
	        System.out.println(pkgName);
	        System.out.println(loader.getResource(classPath+".class"));
	        System.out.println(loader.getResource(pkgPath+"/"+"NewsTemplate.txt"));

	        String templatePath = loader.getResource(pkgPath+"/"+"NewsTemplate.txt").getPath();
	        System.out.println("templatePath:"+templatePath);
	        String templateFilePath = loader.getResource(pkgPath).getPath();
	        System.out.println("templateFilePath:"+templateFilePath);
	        
	        String fileOutPath = "";
	        fileOutPath = templateFilePath.replace("target/classes", "src/main/java");
	        fileOutPath = fileOutPath+"/"+newsFileName+".java";

			BufferedReader br = new BufferedReader(new FileReader(new File(templatePath)));

			String lines = "";
			String line = "";
			while((line = br.readLine()) != null) {
//				System.out.println("line:"+line);
				lines += line+"\n";
			}
			if(lines.contains("${newsFileName}")) {
				System.out.println("lines contains ${newsFileName} ");
			}
			lines = lines.replace("${newsFileName}", newsFileName);
			lines = lines.replace("${newsTitleClass}", newsTitleClass);
			lines = lines.replace("${newsWriterClass}", newsWriterClass);
			lines = lines.replace("${newsWriterClassIdx}", newsWriterClassIdx);
			lines = lines.replace("${newsDateClass}", newsDateClass);
			lines = lines.replace("${newsDateClassIdx}", newsDateClassIdx);
			lines = lines.replace("${newsArticleClass}", newsArticleClass);
			lines = lines.replace("${copyrightClass}", copyrightClass);
			lines = lines.replace("${sampleUrl}", sampleUrl);
			
			FileUtil.fileWrite(fileOutPath,lines);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CreateNewsReadClass();
	}

}
