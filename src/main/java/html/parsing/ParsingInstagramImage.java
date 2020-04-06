/**
 *
 */
package html.parsing;

import java.io.IOException;
import java.util.Hashtable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * @author banks
 *
 */
public class ParsingInstagramImage {

    /**
     *
     */
    public ParsingInstagramImage() {
        String s = ".9.1.0.0.$https=2//scontent=1cdninstagram=1com/t51=12885-15/e35/12907411_212897512418743_1208442386_n=1jpg?ig_cache_key=0MTIyMzkzODY2MDk0ODMxMjI3OQ%3D%3D=12.0.2";
        s = s.replace("=2", ":");
        s = s.replace("=1", ".");
        s = s.substring(s.indexOf("$") + 1, s.indexOf("?"));
        System.out.println("s:" + s);
        grabIt();

    }

    public Hashtable<String, String> grabIt() {
        Document doc;
        try {

            doc = Jsoup.connect("https://www.instagram.com/yeonwoojhi").get();
            System.out.println(doc.html());

            Hashtable<String, String> ht = new Hashtable<String, String>();

//			Elements metas = doc.select("meta");
//			String ratio = "";
//			String curPrice = "";
//			String chgPrice = "";
//			for (Element meta : metas) {
//				String metaProperty = meta.attr("property");
//				if(metaProperty.equals("og:description")){
//					String metaContent = meta.attr("content");
//					String [] metaContentArray = metaContent.split(" ");
//					int contentCnt = 0;
//					for(String s:metaContentArray){
//						System.out.println("content["+(contentCnt++)+"]:"+s);
//					}
//					ratio = metaContentArray[metaContentArray.length-3];
//					chgPrice = metaContentArray[metaContentArray.length-4];
//					curPrice = metaContentArray[metaContentArray.length-5];
//					System.out.println("ratio:"+ratio);
//					System.out.println("curPrice:"+curPrice);
//					System.out.println("chgPrice:"+chgPrice);
//				}
//			}
//			
//			// get page title
//			String title = doc.title();
//			String jongMok = title.split(":")[0].trim();
//			System.out.println(cnt +"."+code);
//			//System.out.println(cnt +"."+code+" "+jongMok);
//			if(jongMok.equals("네이버")) return null;
//
//			Elements dds = doc.select("dd");
//			ht.put("종목명", jongMok);
//			ht.put("ratio", ratio);
//			ht.put("curPrice", curPrice);
//			ht.put("chgPrice", chgPrice);
//			for (Element dd : dds) {
//				String priceTxt = dd.text();
//				if(priceTxt.indexOf("가") != -1 && priceTxt.matches("(.*)[0-9]+(.*)")){
//					String priceSplit[] = priceTxt.replaceAll(",", "").split(" ");
//					//System.out.println(priceSplit[0] +":"+ priceSplit[1]);
//					ht.put(priceSplit[0], priceSplit[1]);
//				}
//			}
//			boolean sang = false;
//			boolean ha = false;
//			boolean sangTouch = false;
//			boolean haTouch = false;
//
//			if(ht.get("현재가").equals(ht.get("상한가"))){
//				System.out.println("상한가:"+jongMok);
//				sang = true;
//				ht.put("구분","상한가");
//				return ht;
//			}
//			if(ht.get("현재가").equals(ht.get("하한가"))){
//				System.out.println("하한가:"+jongMok);
//				ha = true;
//				ht.put("구분","하한가");
//				return ht;
//			}
//			if(!ht.get("고가").equals("0") && ht.get("고가").equals(ht.get("상한가")) && !ht.get("현재가").equals(ht.get("상한가"))){
//				System.out.println("상터치:"+jongMok);
//				sangTouch = true;
//				ht.put("구분","상터치");
//				return ht;
//			}
//			if(!ht.get("저가").equals("0") && ht.get("저가").equals(ht.get("하한가")) && !ht.get("현재가").equals(ht.get("하한가"))){
//				System.out.println("하터치:"+jongMok);
//				haTouch = true;
//				ht.put("구분","하터치");
//				return ht;
//			}
//			String sign = ratio.substring(0,1);
//			float fRatio = Float.parseFloat(ratio.substring(1,ratio.indexOf("%")));
//			if(fRatio >= 15){
//				if(sign.equals("+")){
//					System.out.println("상승15%이상:" + jongMok);
//					ht.put("구분", "상승15%이상");
//				}else if(sign.equals("-")){
//					System.out.println("하락15%이상:" + jongMok);
//					ht.put("구분","하락15%이상");
//				}
//				return ht;
//			}
//			if(fRatio >= 10 && fRatio < 15){
//				if(sign.equals("+")){
//					System.out.println("상승10%이상:" + jongMok);
//					ht.put("구분", "상승10%이상");
//				}else if(sign.equals("-")){
//					System.out.println("하락10%이상:" + jongMok);
//					ht.put("구분","하락10%이상");
//				}
//				return ht;
//			}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new ParsingInstagramImage();
    }

}
