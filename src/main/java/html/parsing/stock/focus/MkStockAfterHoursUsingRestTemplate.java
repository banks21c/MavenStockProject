package html.parsing.stock.focus;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import html.parsing.stock.model.StockVO;
import html.parsing.stock.util.FileUtil;
import html.parsing.stock.util.NaverUtil;

/**
 * 유튜브에서 네이버로 공유할때
 */
public class MkStockAfterHoursUsingRestTemplate {

	private static final Logger logger = LoggerFactory.getLogger(MkStockAfterHoursUsingRestTemplate.class);

	static String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	public final static String USER_HOME = System.getProperty("user.home");

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new MkStockAfterHoursUsingRestTemplate();
	}

	MkStockAfterHoursUsingRestTemplate() throws IOException, Exception {
		getStockTradingVolume();
	}

	public void getStockTradingVolume() {
		try {
			HttpHeaders headers = new HttpHeaders();
//			https://vip.mk.co.kr/newSt/price/current9.php?stCode=013580
			headers.set("authority", "vip.mk.co.kr");
			headers.set("method", "GET");
			headers.set("path", "/newSt/price/current9.php?stCode=013580");
			headers.set("scheme", "https");
//			headers.set("accept", "*/*");
			headers.set("accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("accept-encoding", "gzip, deflate, br");
			headers.set("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("cache-control", "max-age=0");
			headers.set("content-length", "3271");
//			headers.set("content-type", "multipart/form-data; boundary=----WebKitFormBoundary9t8zjt2Fnq9yXOyi");
			headers.set("cookie",
					"XSRF-TOKEN=410d7f7c-c6de-41c8-81c9-846bf3655122; BMR=; NNB=C7BPGOO3VPKF6; naver_mobile_stock_codeList=251270%7C; JSESSIONID=8E549C360FA814069E29BC3967B54EC1");
//			headers.set("origin", "https://m.stock.naver.com");
//			headers.set("referer", "https://m.stock.naver.com/api/item/getTrendList.nhn?code=251270&size=5");
			headers.set("sec-ch-ua", "\"Google Chrome\";v=\"87\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"87");
			headers.set("sec-ch-ua-mobile", "?0");
//			headers.set("sec-fetch-dest", "empty");
//			headers.set("sec-fetch-mode", "cors");
//			headers.set("sec-fetch-site", "same-origin");
			headers.set("sec-fetch-dest", "document");
			headers.set("sec-fetch-mode", "navigate");
			headers.set("sec-fetch-site", "none");
			headers.set("sec-fetch-user", "?1");
			headers.set("upgrade-insecure-requests", "1");
//			headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
			headers.set("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

//			headers.setContentType(MediaType.TEXT_HTML);
//			List<Charset> acceptableCharsets = new ArrayList<>();
//			acceptableCharsets.add(Charset.forName("UTF-8"));
//			headers.setAcceptCharset(acceptableCharsets);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			System.out.println("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");

//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("m.stock.naver.com");
			builder = builder.path("/api/item/getTrendList.nhn");
			builder = builder.queryParam("code", "251270");
			builder = builder.queryParam("size", "5");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			System.out.println("response :" + response);
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));

			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			String strResponseBody = "";
			// Response Headers
			// content-encoding: br : Brotli 압축

			if (responseBody != null) {
				String charset = NaverUtil.guessEncoding(responseBody);
				System.out.println("charset :" + charset);
				// Brotli압축해제
//				strResponseBody = NaverUtil.unzipStringFromBrotliCompressBytes(responseBody, charset);
//				strResponseBody = NaverUtil.stringFromBytes(responseBody, "UTF-8");
				strResponseBody = new String(new String(responseBody, "UTF-8").getBytes("UTF-8"), "UTF-8");

				JSONObject jo = new JSONObject(strResponseBody);
				JSONArray result = (JSONArray) jo.get("result");
				for (int i = 0; i < result.length(); i++) {
					JSONObject jObj = (JSONObject) result.get(i);
					System.out.println(jObj);

					int change_val = jObj.getInt("change_val");
					int frgn_stock = jObj.getInt("frgn_stock");
					// 거래량
					int acc_quant = jObj.getInt("acc_quant");
					// 거래일
					String bizdate = jObj.getString("bizdate");
					String risefall = jObj.getString("risefall");
					String itemcode = jObj.getString("itemcode");
					// 외국인순매매
					int frgn_pure_buy_quant = jObj.getInt("frgn_pure_buy_quant");
					int close_val = jObj.getInt("close_val");
					String sosok = jObj.getString("sosok");
					// 외국인보유율
					float frgn_hold_ratio = jObj.getFloat("frgn_hold_ratio");
					// 기관순매매
					int organ_pure_buy_quant = jObj.getInt("organ_pure_buy_quant");
					// 개인순매매
					int indi_pure_buy_quant = jObj.getInt("indi_pure_buy_quant");
					System.out.println("change_val:" + change_val);
					System.out.println("frgn_stock:" + frgn_stock);
					System.out.println("acc_quant:" + acc_quant);
					System.out.println("bizdate:" + bizdate);
					System.out.println("risefall:" + risefall);
					System.out.println("itemcode:" + itemcode);
					System.out.println("frgn_pure_buy_quant:" + frgn_pure_buy_quant);
					System.out.println("close_val:" + close_val);
					System.out.println("sosok:" + sosok);
					System.out.println("frgn_hold_ratio:" + frgn_hold_ratio);
					System.out.println("organ_pure_buy_quant:" + organ_pure_buy_quant);
					System.out.println("indi_pure_buy_quant:" + indi_pure_buy_quant);
				}
			}
			System.out.println("strResponseBody:" + strResponseBody);
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
				String fileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_"
						+ "m.stock.naver.com.html";
				FileUtil.fileWrite(fileName, strResponseBody);
			}
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<StockVO> getStockTradingVolumeList(List<StockVO> uniqueStockList) {
		List<StockVO> svoList = new ArrayList<StockVO>();
		for (StockVO svo : uniqueStockList) {
			StockVO svo2 = getStockTradingVolumeVO(svo);
			svoList.add(svo2);
		}
		return svoList;
	}
	
	public static LinkedHashMap<String, List<StockVO>> getStockTradingVolumeList(
			LinkedHashMap<String, List<StockVO>> newLowHighPriceMap) {

		LinkedHashMap<String, List<StockVO>> newLowHighPriceMap2 = new LinkedHashMap<String, List<StockVO>>();

		Set keySet = newLowHighPriceMap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			List<StockVO> stockList = newLowHighPriceMap.get(key);
			List<StockVO> svoList = new ArrayList<StockVO>();
			for (StockVO svo : stockList) {
				StockVO svo2 = getStockTradingVolumeVO(svo);
				svoList.add(svo2);
			}
			newLowHighPriceMap2.put(key, svoList);
		}
		return newLowHighPriceMap2;
	}
	
	public static StockVO getStockTradingVolumeVO(StockVO svo) {
		String stockCode = svo.getStockCode();
		try {
			HttpHeaders headers = new HttpHeaders();
//			https://vip.mk.co.kr/newSt/price/current9.php?stCode=013580
			headers.set("authority", "vip.mk.co.kr");
			headers.set("method", "GET");
			headers.set("path", "/newSt/price/current9.php?stCode=" + stockCode);
			headers.set("scheme", "https");
//			headers.set("accept", "*/*");
			headers.set("accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("accept-encoding", "gzip, deflate, br");
			headers.set("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("cache-control", "max-age=0");
			headers.set("content-length", "3271");
//			headers.set("content-type", "multipart/form-data; boundary=----WebKitFormBoundary9t8zjt2Fnq9yXOyi");
			headers.set("cookie",
					"XSRF-TOKEN=410d7f7c-c6de-41c8-81c9-846bf3655122; BMR=; NNB=C7BPGOO3VPKF6; naver_mobile_stock_codeList=251270%7C; JSESSIONID=8E549C360FA814069E29BC3967B54EC1");
//			headers.set("origin", "https://m.stock.naver.com");
//			headers.set("referer", "https://m.stock.naver.com/api/item/getTrendList.nhn?code=251270&size=5");
			headers.set("sec-ch-ua", "\"Google Chrome\";v=\"87\", \" Not;A Brand\";v=\"99\", \"Chromium\";v=\"87");
			headers.set("sec-ch-ua-mobile", "?0");
//			headers.set("sec-fetch-dest", "empty");
//			headers.set("sec-fetch-mode", "cors");
//			headers.set("sec-fetch-site", "same-origin");
			headers.set("sec-fetch-dest", "document");
			headers.set("sec-fetch-mode", "navigate");
			headers.set("sec-fetch-site", "none");
			headers.set("sec-fetch-user", "?1");
			headers.set("upgrade-insecure-requests", "1");
//			headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36");
			headers.set("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");

//			headers.setContentType(MediaType.TEXT_HTML);
//			List<Charset> acceptableCharsets = new ArrayList<>();
//			acceptableCharsets.add(Charset.forName("UTF-8"));
//			headers.setAcceptCharset(acceptableCharsets);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			System.out.println("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("vip.mk.co.kr");
			builder = builder.path("/newSt/price/current9.php");
			builder = builder.queryParam("stCode", stockCode);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			System.out.println("response :" + response);
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));

			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			String strResponseBody = "";
			// Response Headers
			// content-encoding: br : Brotli 압축

			if (responseBody != null) {
				String charset = NaverUtil.guessEncoding(responseBody);
				System.out.println("charset :" + charset);
				// Brotli압축해제
//				strResponseBody = NaverUtil.unzipStringFromBrotliCompressBytes(responseBody, charset);
//				strResponseBody = NaverUtil.stringFromBytes(responseBody, "UTF-8");
				strResponseBody = new String(new String(responseBody, "UTF-8").getBytes("UTF-8"), "UTF-8");

				JSONObject jo = new JSONObject(strResponseBody);
				JSONArray result = (JSONArray) jo.get("result");
				if (result.length() > 0) {
					JSONObject jObj = (JSONObject) result.get(0);
					System.out.println(jObj);

					int change_val = jObj.getInt("change_val");
					int frgn_stock = jObj.getInt("frgn_stock");
					// 거래량
					int acc_quant = jObj.getInt("acc_quant");
					// 거래일
					String bizdate = jObj.getString("bizdate");
					String risefall = jObj.getString("risefall");
					String itemcode = jObj.getString("itemcode");
					// 외국인순매매
					int frgn_pure_buy_quant = jObj.getInt("frgn_pure_buy_quant");
					int close_val = jObj.getInt("close_val");
					String sosok = jObj.getString("sosok");
					// 외국인보유율
					float frgn_hold_ratio = jObj.getFloat("frgn_hold_ratio");
					// 기관순매매
					int organ_pure_buy_quant = jObj.getInt("organ_pure_buy_quant");
					// 개인순매매
					int indi_pure_buy_quant = jObj.getInt("indi_pure_buy_quant");

					svo.setiOrganTradingVolume(organ_pure_buy_quant);
					svo.setiForeignTradingVolume(frgn_pure_buy_quant);
					svo.setiIndividualTradingVolume(indi_pure_buy_quant);

					System.out.println("change_val:" + change_val);
					System.out.println("frgn_stock:" + frgn_stock);
					System.out.println("acc_quant:" + acc_quant);
					System.out.println("bizdate:" + bizdate);
					System.out.println("risefall:" + risefall);
					System.out.println("itemcode:" + itemcode);
					System.out.println("frgn_pure_buy_quant:" + frgn_pure_buy_quant);
					System.out.println("close_val:" + close_val);
					System.out.println("sosok:" + sosok);
					System.out.println("frgn_hold_ratio:" + frgn_hold_ratio);
					System.out.println("organ_pure_buy_quant:" + organ_pure_buy_quant);
					System.out.println("indi_pure_buy_quant:" + indi_pure_buy_quant);
				}
			}
			System.out.println("strResponseBody:" + strResponseBody);
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
				String fileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_"
						+ "m.stock.naver.com.html";
				FileUtil.fileWrite(fileName, strResponseBody);
			}
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return svo;
	}

}
