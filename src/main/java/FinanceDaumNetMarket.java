
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.brotli.dec.BrotliInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class FinanceDaumNetMarket {

	private static final Logger logger = LoggerFactory.getLogger(FinanceDaumNetMarket.class);

	public static void main(String[] args) {
		new FinanceDaumNetMarket();
	}

	FinanceDaumNetMarket() {
		getMarket("KOSPI");
	}

	public boolean getMarket(String marketGubun) {

		try {
			// 헤더설정
			HttpHeaders headers = new HttpHeaders();

			headers.set("authority", "finance.daum.net");
			headers.set("method", "GET");
			headers.set("path", "/api/quotes/sectors?market=KOSPI");
			headers.set("scheme", "https");

			headers.set("Accept", "application/json, text/javascript, */*; q=0.01");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("cookie",
					"TIARA=sPi8vQIdX7xZL2.JDBYv_wNR.X8nBo3.pwhQmRX.nLBOW2fWpgRxDZG2GFBmwO3sBdgNsgIOgNum1agel7q2q7NISRFdUhtn1FvMJLMvAqA0; webid=b0781e04542946c78d1f78b55a208f00; webid_ts=1608381332777; _ga=GA1.2.836600285.1612521594; _gid=GA1.2.1126637121.1612521594; _gat_gtag_UA_128578811_1=1; _T_ANO=XHG3dEXX09bGY81UISKmzOPOjNQcbUjCVSAsYD9kIHtjqQBAJtfTxH1DslzYrpnWDaIKf+gNLo1cKEi78YkeiLiFJmmCbeaLKdNvs4vDipmuGDQ1P5yFevP72X8KSNQf8nrJdWgIp4HT2g7szCNFpQ49M0Vz1whnuAScpVpbRseBgZFZx2bcG3jc7YPKgHfUMWsLCiTyve+lfVHu4EfF67f6gUuDI8BHxQL+xsSRM7Ix6qLwOJjCtvSwhwitHDEPFvGXaZM7SCf6EZEL/U29Oye2WTnlTmqccAYk+xAHE94eyLei737cm95nY3xnaX3zll6EeQYy3QAvrsfEvV3iEA==; webid_sync=1612521593176");
			headers.set("if-none-match", "W/\"51a3b7f2daf97a5f501d98e3c021fcf3\"");
			headers.set("referer", "https://finance.daum.net/domestic/all_quotes");
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("sec-fetch-dest", "empty");
			headers.set("sec-fetch-mode", "cors");
			headers.set("sec-fetch-site", "same-origin");
			headers.set("user-agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36");
			headers.set("x-requested-with", "XMLHttpRequest");

			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			final ClientHttpRequestFactory requestFactory = getRequestFactory();
			final RestTemplate restTemplate = new RestTemplate(requestFactory);
			// 빈 RestTemplate 생성 후 messageConvert 추가
//			RestTemplate restTemplate = new RestTemplate();

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

			// messageConvert 목록을 만든 후 RestTemplate을 생성
//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");
			// 빈 RestTemplate에 MessageConverter 목록을 세팅
//			restTemplate.setMessageConverters(messageConverters);

			// Form Data
			// header에 있으면 Form Data에 없어도 된다.
//			MultiValueMap<String, Object> map = null;
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("scheme", "https");
			map.add("domain", "finance.daum.net");
			map.add("market", "KOSPI");
			map.add("method", "GET");
			map.add("path", "/api/quotes/sectors");
			map.add("referer", "https://finance.daum.net/domestic/all_quotes");

//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("finance.daum.net");
//			builder = builder.path("/api/quotes/sectors?market=KOSPI");
			builder = builder.path("/api/quotes/sectors");
			builder = builder.queryParam("market", "KOSPI");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();

			System.out.println("uriComponents :" + uriComponents);
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, byte[].class);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Object.class);
//			ResponseEntity<String> response = restTemplate.postForEntity(builder.toUriString(), entity,String.class);
//			ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class, entity);//org.springframework.web.client.HttpClientErrorException: 403 Forbidden
//			ResponseEntity response = restTemplate.getForObject(builder.toUriString(), ResponseEntity.class, entity);//org.springframework.web.client.HttpClientErrorException: 403 Forbidden
			System.out.println("response1 :" + response);
			
			Resource resource = new ClassPathResource("");
			ResponseEntity.ok().cacheControl(CacheControl.noCache()).lastModified(resource.lastModified());

			URI location = restTemplate.postForLocation(builder.toUriString(), entity);
			System.out.println("location :" + location);

//			response = restTemplate.exchange(location.toString(), HttpMethod.GET, entity, String.class);
			response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
			System.out.println("response2 :" + response);

//			restTemplate.patchForObject(builder.toUriString(), entity, String.class);

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
			System.out.println("body :" + response.getBody());

			String unzipString = "";
//			unzipString = unzipStringFromBytes(response.getBody(), "UTF8");

			System.out.println("unzipString :" + unzipString);
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	ClientHttpRequestFactory getRequestFactory() {
		final int timeoutInSecs = 5;

		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeoutInSecs * 5000);
		clientHttpRequestFactory.setReadTimeout(timeoutInSecs * 5000);
		return clientHttpRequestFactory;
	}

	public static Document getNaverBlogLinkSharePage(String strNidAut, String strNidSes) {
		StringBuilder sb = new StringBuilder();
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			// headers.set("Content-Length", "5437");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			// text/html;charset=UTF-8
			headers.setContentType(MediaType.TEXT_HTML);
			List<Charset> acceptableCharsets = new ArrayList<>();
			acceptableCharsets.add(Charset.forName("UTF-8"));
			headers.setAcceptCharset(acceptableCharsets);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);
			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");
			System.out.println("cookieSb.toString():" + cookieSb.toString());
			headers.set("Cookie", cookieSb.toString());

//			Cookie c = new Cookie();
			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
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

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("finance.daum.net");
//			builder = builder.path("/api/quotes/sectors?market=KOSPI");
			builder = builder.path("/api/quotes/sectors");
			builder = builder.queryParam("market", "KOSPI");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();

			System.out.println("uriComponents :" + uriComponents);
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
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
			System.out.println("response.getBody() :" + response.getBody());
			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			String unzipString = "";
			if (responseBody != null) {
				unzipString = unzipStringFromBytes(response.getBody(), "UTF8");
			}
			sb.append(unzipString);

			System.out.println("body:" + unzipString);
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Jsoup.parse(sb.toString());
	}

	public static String guessEncoding(byte[] bytes) {
		String DEFAULT_ENCODING = "UTF-8";
		org.mozilla.universalchardet.UniversalDetector detector = new org.mozilla.universalchardet.UniversalDetector(
				null);
		detector.handleData(bytes, 0, bytes.length);
		detector.dataEnd();
		String encoding = detector.getDetectedCharset();
		detector.reset();
		if (encoding == null) {
			encoding = DEFAULT_ENCODING;
		}
		System.out.println("encoding:" + encoding);
		return encoding;
	}

	// BrotliInputStream을 이용하여 byte배열 압축해제하기
	public static String unzipStringFromBrotliCompressBytes(byte[] bytes, String charset) throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		BrotliInputStream brotliInputStream = new BrotliInputStream(byteArrayInputStream);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(brotliInputStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[100];

		int length;
		while ((length = bufferedInputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}

		bufferedInputStream.close();
		brotliInputStream.close();
		byteArrayInputStream.close();
		byteArrayOutputStream.close();

		return byteArrayOutputStream.toString(charset);
	}

	// GZIPInputStream을 이용하여 byte배열 압축해제하기
	public static String unzipStringFromBytes(byte[] bytes, String charset) throws IOException {
		if (bytes == null)
			return null;
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipInputStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[100];

		int length;
		while ((length = bufferedInputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}

		bufferedInputStream.close();
		gzipInputStream.close();
		byteArrayInputStream.close();
		byteArrayOutputStream.close();

		return byteArrayOutputStream.toString(charset);
	}

	// byte배열 압축해제하기
	public static String stringFromBytes(byte[] bytes, String charset) throws IOException {

		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[100];

		int length;
		while ((length = bufferedInputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}

		bufferedInputStream.close();
		byteArrayInputStream.close();
		byteArrayOutputStream.close();

		return byteArrayOutputStream.toString(charset);
	}

}
