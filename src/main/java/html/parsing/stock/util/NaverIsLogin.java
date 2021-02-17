package html.parsing.stock.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import org.brotli.dec.BrotliInputStream;

public class NaverIsLogin {

	private static final Logger logger = LoggerFactory.getLogger(NaverIsLogin.class);

	public static void main(String[] args) {
		new NaverIsLogin();
	}

	NaverIsLogin() {
		String blogId = "soonks21";
		String categoryNo = "2";
		String nidAut = "aW2ZMtajAi8PPkvVFNBXQsC1MfI0rxJzPH5c41owj0CjyHVPQguNnTn5H+5sX7Cs";
		String nidSes = "AAABervrvMq28f3zKOMjmStyUdJJi9BjnoM4K+fhsm7HiFpNKYqghk0FX+s/Bv2pNmEQRuus+JT8lTUc9ED5eA19TipRO1NGyUSzp58l0+JT2YjClhl8hT3/Bvw4xANvTgg8uDTIpjLBlqwtSbR3aGwd2+Ztf58wZgRoIUJ3xz6JfhR5Wcko191qj0XiBzX//XaB0jv51Uz4MNM3LrpNTZ49RpOCXu1oKVNE1m95qyyM4VncC1LofZ/Aw85svYeIuuz/k52uLcxshkGpoC0gi1K8J0wPb6hVKbjBQXH563dCgIBoGasSHfRoyMIOnRvt1Zjs/mR7VpImRfZDrSWsLjqUDQTD7U/Q9qcJEbkLJFjuvC5PbWGcFgZzpn7cguQ4nBlBueNLtNyXWAP2WgOEdrkyderq1btuMMILC3WmzUBdzJZBBEARytg0iI3eUdUEdPUupnXnQtmS38Et+5oxkH1YVq6w+tsIXylsHxYYEAe6pkh8FmLRuRuaC27BiFS1ejM2hQ==";
		isLogin(blogId, categoryNo, nidAut, nidSes);
	}

	public static boolean isLogin(String blogId, String categoryNo, String nidAut, String nidSes) {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("authority", "blog.naver.com");
			headers.set("method", "GET");
			headers.set("path", "/isLogin.nhn");
			headers.set("scheme", "https");

//			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept", "*/*");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

//			headers.set("charset", "utf-8");
//			headers.set("Content-Length", "4148");
//			headers.set("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.set("cookie", "NID_AUT=" + nidAut + "; NID_SES=" + nidSes + ";");

			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

//			headers.set("Host", "blog.naver.com");

			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("ec-fetch-dest", "empty");
			headers.set("sec-fetch-mode", "cors");
			headers.set("sec-fetch-site", "same-origin");
//			headers.set("Upgrade-Insecure-Requests", "1");
//			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36");

			headers.set("x-requested-with", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			// header에 있으면 Form Data에 없어도 된다.
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("blog.naver.com");
			builder = builder.path("/isLogin.nhn");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);

			RestTemplate restTemplate = new RestTemplate();
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					String.class);
//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
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
			System.out.println("body :" + response.getBody());

			//LOGIN,LOGOUT
//			String strBody = stringFromBytes(response.getBody(), "UTF8");
			String strBody = response.getBody();

			System.out.println("strBody :" + strBody);
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
