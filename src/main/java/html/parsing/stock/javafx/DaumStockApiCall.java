package html.parsing.stock.javafx;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.sql.rowset.serial.SerialBlob;

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
import html.parsing.stock.util.NaverUtil;

/**
 * 유튜브에서 네이버로 공유할때
 */
public class DaumStockApiCall {

	private static final Logger logger = LoggerFactory.getLogger(DaumStockApiCall.class);

	String strYmdhms = new SimpleDateFormat("yyyyMMdd_hhmmss", Locale.KOREAN).format(new Date());
	public final static String USER_HOME = System.getProperty("user.home");

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new DaumStockApiCall();
	}

	DaumStockApiCall() throws IOException, Exception {
		apiCall();
	}

	public void apiCall() {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("authority", "finance.daum.net");
			headers.set("method", "GET");
			headers.set("path",
					"/api/trend/after_hours_spac?page=1&perPage=30&fieldName=changeRate&order=desc&market=KOSDAQ&type=CHANGE_RISE&pagination=true");
			headers.set("scheme", "https");
			headers.set("accept", "application/json, text/javascript, */*; q=0.01");
			headers.set("accept-encoding", "gzip, deflate, br");
			headers.set("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
//			headers.set("content-length", "3271");
//			headers.set("content-type", "multipart/form-data; boundary=----WebKitFormBoundary9t8zjt2Fnq9yXOyi");
			headers.set("content-type", "application/json; charset=utf-8");

			headers.set("cookie",
					"_ga=GA1.2.1153291821.1595643013; _gid=GA1.2.307841874.1595643013; _TI_NID=iok9mjEFfnWoNqMjDA5hhfcB5KxUHFigwdk85By7iuHlSXYnpeiVIg2iexl71nbgoRtWAbY+BtZ8ePYjUFEVtw==; _gat_gtag_UA_128578811_1=1; TIARA=nDb6ZSqDAfrKeREwgM.ES1OWWS4mKE2NLOnH9cIJUFenb6yQRmxBXgNwgLkqqaR-d_F9pHqEFTj2A7ZY9Qib1zk3F5AJnm5T; _dfs=TlV1YnFWS1FCVEFnWkM2c0xpYndJaFJtZTFEMEgrRWhtdzVNRFFiUVdJNk81YWZKSkNrZVZWSnN5aGMyYUU3c0xqN1UxNDVFWjNSV3crVWpVMDJYUFE9PS0tOHpiNmZXQ0d3VTJTSlh3aHlNZm51QT09--bfc04d869541881e29b2c832637356a35f0f3b26");
//			headers.set("origin", "https://www.facebook.com");
			headers.set("if-none-match", "W/\"b69797c0734e7b17b8bc899752638ca1\"");
			headers.set("referer",
					"https://finance.daum.net/domestic/after_hours?market=KOSDAQ");
			headers.set("sec-fetch-dest", "empty");
			headers.set("sec-fetch-mode", "cors");
			headers.set("sec-fetch-site", "same-origin");
			headers.set("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
			headers.set("x-requested-with","XMLHttpRequest");

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

//            HttpEntity<String> entity = new HttpEntity<String>(headers);
//			messageConverters.add(new org.springframework.http.converter.ByteArrayHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.ResourceHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
			// messageConverters.add(new
			// org.springframework.http.converter.xml.SourceHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not extract
			// response: no suitable HttpMessageConverter found for response type [class
			// java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not extract
			// response: no suitable HttpMessageConverter found for response type [class
			// java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.ResourceRegionHttpMessageConverter());
			// org.springframework.web.client.RestClientException: Could not write request:
			// no suitable HttpMessageConverter found for request type
			// [org.springframework.util.LinkedMultiValueMap] and content type
			// [application/x-www-form-urlencoded]
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
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("finance.daum.net");
//			builder = builder.path("/login.php?skip_api_login=1&api_key=966242223397117&signed_next=1&next=https%3A%2F%2Fwww.facebook.com%2Fsharer%2Fsharer.php%3Fu%3Dhttps%253A%252F%252Fwww.hankyung.com%252Feconomy%252Farticle%252F202006306250Y&cancel_url=https%3A%2F%2Fwww.facebook.com%2Fdialog%2Fclose_window%2F%3Fapp_id%3D966242223397117%26connect%3D0%23_%3D_&display=popup&locale=ko_KR");
			builder = builder.path("/api/trend/after_hours_spac");
			builder = builder.queryParam("page", "1");
			builder = builder.queryParam("perPage", "30");
			builder = builder.queryParam("fieldName", "changeRate");
			builder = builder.queryParam("order", "desc");
			builder = builder.queryParam("market", "KOSDAQ");
			builder = builder.queryParam("type", "CHANGE_RISE");
			builder = builder.queryParam("pagination", "true");
			
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
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
			System.out.println("response.getStatusCodeValue():" + response.getStatusCodeValue());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				System.out.println(String.format("Response Header [%s] = %s", key, value));
			});

//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));

			System.out.println("body :" + response.getBody());
			String strResponseBody = "";
			// Response Headers
			System.out.println("strResponseBody:" + strResponseBody);
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
				String fileName = USER_HOME + File.separator + "documents" + File.separator + strYmdhms + "_"
						+ "facebook.login.html";
				FileUtil.fileWrite(fileName, strResponseBody);
			}
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
