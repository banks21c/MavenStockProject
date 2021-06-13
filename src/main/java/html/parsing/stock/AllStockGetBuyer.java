package html.parsing.stock;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

import html.parsing.stock.util.FileUtil;

/**
 * 유튜브에서 네이버로 공유할때
 */
public class AllStockGetBuyer {

	String url = "https://m.stock.naver.com/api/item/getTrendList.nhn?code=005930&size=10&bizdate=20200805";
//	public static final String SERVER_URI = "http://blog.naver.com/LinkShare.nhn?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare";
	private static final Logger logger = LoggerFactory.getLogger(AllStockGetBuyer.class);

	String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	public final static String USER_HOME = System.getProperty("user.home");

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new AllStockGetBuyer();
	}

	AllStockGetBuyer() throws IOException, Exception {
		naverBlogLinkShare();
	}

	public void naverBlogLinkShare() {
		try {
			HttpHeaders headers = new HttpHeaders();
//			headers.set("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("authority","m.stock.naver.com");
			headers.set("method","GET");
			headers.set("path","/api/item/getTrendList.nhn?code=005930&size=10&bizdate=20200805");
			headers.set("scheme","https");
			headers.set("accept", "*/*");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("content-type","application/x-www-form-urlencoded; charset=utf-8");
			headers.set("Connection", "keep-alive");

			// 쿠키가 없으면 로그인 정보가 없어서 페이지를 조회하지 못한다.
			// NID_AUT,NID_SES 필수
			headers.set("cookie",
				"NNB=AOXX4MFAR4ZF6; nid_inf=-1557522113; NID_AUT=r0JGQfBLGLvE/CQ/3b+6ciRRZD8iGz07G4DH/xWbNMg2+9c0vt0Uc+p/CryxwxQm; NID_SES=AAABlbzCje54baaCjB2t9cvKFIPHXdZE9Q/aiqWwwkTLO88xyAhrt6bLHUYTNq309z+tRFS1oURhFLZ71RhAiAMHXpQ7cz/OCJQazWXQyIlCaxIYgZBuvmov6hM9D5RRjGBa5+EehrXhAG+C23B4g0+6Q30a6Epx6qiT+33Gn8nELFfiSX5FC6cvWK8VJBHF6/gi0ieGinxR4/ZbPV9H7GY2Y6ZLyDXQdmRiJ2GeNAD5PKg5iYq2xba+CCvnc9B6C0YCgf+D9hkV65gpQmpkRzzUSnP1CFGPA+MYinA96pJ+Ptbbnz3TraZq1HicweMhF3+qPsR1S95qKEmowBvWvljvojIrSmc+7HEr6IGsGz1+PQjkVCuCRk089u0in0isweQ5+5sD6Ob9965yZcEo0g9INBSid5qr3lYUaGxF0et6/FIt9xI0elGw66kDK5NA7Yd1EiuHRyCdxz38xSxMF+DicAdnQ9nsn4ruM79raR5Vzyu9h2FcY2Ch2z/MUDJUqqRNvd3/E9ei5o6hKW5j0KFcVaufXgHLxNgcLX3Jwks8xAr3; NID_JKL=RN8fca1h2kTdvtblTCzMi3sKluoGgIqMN0V8mpuRPJc=; naver_mobile_stock_codeList=005930%7C; BMR=; JSESSIONID=3CD5E7B538AB7B83BFE5629F773838DA; XSRF-TOKEN=1f290e70-7182-4366-b310-8b5f5ee3eb68");

			headers.set("referer","https://m.stock.naver.com/item/main.nhn");
			headers.set("sec-fetch-dest","empty");
			headers.set("sec-fetch-mode","cors");
			headers.set("sec-fetch-site","same-origin");
			headers.set("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");
			headers.set("x-requested-with","XMLHttpRequest");
	
//			headers.set("host", "blog.naver.com");
//			headers.set("upgrade-insecure-requests", "1");
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
//			builder = builder.path("/LinkShare.nhn?url=https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
//			builder = builder.path("/api/item/getTrendList.nhn?code=005930&size=10&bizdate=20200805");
			builder = builder.path("/api/item/getTrendList.nhn");
			builder = builder.queryParam("code", "005930");
			builder = builder.queryParam("size", "10");
			builder = builder.queryParam("bizdate", "20200805");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
			System.out.println("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
//			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
//			ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
//		        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			System.out.println("body :" + new String(responseBody));
			String unzipString = "";
			if (responseBody != null) {
			}

			System.out.println("body:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
				String fileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_"
					+ "nidlogin.login.html";
				FileUtil.fileWrite(fileName, unzipString);
			}
			;
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
