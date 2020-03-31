package html.parsing.stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class AssetManagementTest {

	public AssetManagementTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String assetMgmt[] = { "KB자산운용", "포트코리아자산운용", "아이맵자산운용", "포트코리아자산운용", "코어자산운용", "아너스자산운용", "지마이티자산운용", "코어자산운용",
				"코어자산운용", "코어자산운용", "퀸즈가드자산운용", "미래에셋자산운용", "베어링자산운용", "KB자산운용", "베어링자산운용", "미래에셋자산운용", "브이아이피자산운용",
				"한화자산운용", "미래에셋자산운용", "KB자산운용", "KB자산운용", "한국투자밸류자산운용", "KB자산운용", "한국투자밸류자산운용", "신영자산운용", "한국투자밸류자산운용",
				"KB자산운용", "타이거자산운용투자자문", "신영자산운용", "삼성액티브자산운용", "브이아이피자산운용", "한국투자밸류자산운용", "KB자산운용", "쿼드자산운용",
				"타이거자산운용투자자문", "트러스톤자산운용", "삼성액티브자산운용", "미래에셋자산운용", "쿼드자산운용", "KB자산운용", "한국투자밸류자산운용", "미래에셋자산운용",
				"KB자산운용", "트러스톤자산운용", "미래에셋자산운용", "미래에셋자산운용", "신영자산운용", "트러스톤자산운용", "미래에셋자산운용", "KB자산운용", "신영자산운용",
				"디비자산운용", "디비자산운용", "미래에셋자산운용", "트러스톤자산운용", "신영자산운용", "베어링자산운용", "미래에셋자산운용", "베어링자산운용", "KB자산운용",
				"베어링자산운용", "미래에셋자산운용", "한화자산운용", "브이아이피자산운용", "미래에셋자산운용", "신영자산운용", "베어링자산운용", "신영자산운용", "한국투자밸류자산운용",
				"베어링자산운용", "한화자산운용", "KB자산운용", "신영자산운용", "KB자산운용", "KB자산운용", "KB자산운용", "KB자산운용", "KB자산운용", "미래에셋자산운용",
				"쿼드자산운용", "미래에셋자산운용", "베어링자산운용", "삼성액티브자산운용", "신영자산운용", "KB자산운용", "밸류파트너스자산운용", "베어링자산운용", "한국투자밸류자산운용",
				"타이거자산운용투자자문", "베어링자산운용", "신한비엔피파리바자산운용", "마이다스에셋자산운용", "한국투자밸류자산운용", "마이다스에셋자산운용", "수성자산운용", "신영자산운용",
				"라임자산운용", "타이거자산운용투자자문", "디비자산운용", "타임폴리오자산운용", "신영자산운용", "BNK자산운용", "한국투자밸류자산운용", "한화자산운용",
				"삼성액티브자산운용", "타이거자산운용투자자문", "플랫폼파트너스자산운용", "마이다스에셋자산운용", "한국투자밸류자산운용", "마이다스에셋자산운용", "신영자산운용", "수성자산운용",
				"신영자산운용", "한국투자밸류자산운용", "라임자산운용", "머스트자산운용", "코어자산운용", "헤이스팅스자산운용", "파인밸류자산운용", "대덕자산운용", "라임자산운용",
				"더블유자산운용", "더블유자산운용", "더블유자산운용", "유진자산운용" };

		List<String> assetMgmtList = new ArrayList<String>();
		for(int i=0;i<assetMgmt.length;i++) {
			assetMgmtList.add(assetMgmt[i]);
		}

		Vector<String> v = new Vector<String>();
		for(int i=0;i<assetMgmt.length;i++) {
			v.add(assetMgmt[i]);
		}

		Hashtable<String, String> ht = new Hashtable<String,String>();
		for(int i=0;i<assetMgmt.length;i++) {
			ht.put(assetMgmt[i],assetMgmt[i]);
		}

		Map<String, String> map = new HashMap<String,String>();
		for(int i=0;i<assetMgmt.length;i++) {
			map.put(assetMgmt[i],assetMgmt[i]);
		}

		System.out.println(assetMgmt.length);
		System.out.println(assetMgmt);

		System.out.println(assetMgmtList.size());
		System.out.println(assetMgmtList);

		System.out.println(v.size());
		System.out.println(v);

		System.out.println(ht.size());
		System.out.println(ht);

		System.out.println(map.size());
		System.out.println(map);

		Set<String> keys = map.keySet();
		Iterator it = keys.iterator();
		int count = 0;
		while(it.hasNext()) {
			String key = (String)it.next();
			String value = map.get(key);
//			System.out.println(key+"\t"+value);
			if(count != 0) {
				System.out.print(",");
			}
			System.out.println("\""+key+"\"");
			count++;
		}
	}

}
