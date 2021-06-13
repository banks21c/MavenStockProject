package html.parsing.stock.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import org.brotli.dec.BrotliInputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class NaverUtil {

	private static final Logger logger = LoggerFactory.getLogger(NaverUtil.class);

	public static void main(String[] args) {
//		getNaverBlogReplyOpen("", "");

		JRootPane rootPane = null;
		String strBlogId = "soonks21";
		String strNidAut = "80pQ9sjL8eX2TSPE9NIbuj1SKyXkk6MfuwuASNjBanLlFZFp+swqQS9t0SR8xf6m";
		String strNidSes = "AAABdS2WwTbmu4RR/uf7eoGmkO5HhLVVLkoMJ46DLnxolZggFp2OfgLQ0c1DtEZSHvjN3xCMuo0Y4umG/Ua6fQ9IA/nCgZuhXhO7LpxOo3vcQcImlMj3ws36g/t1vw7V0Q3re/xgBqLzYqsqhewouIv8ON7atNZgdr5sN1+64m2Choq+zNRlO01YYRnC4UcsSBdRkNZaPTWSaweOWmf7jIXTyvbbWjLqegyQxHhyi9gnY3MDzZ7gu2bCEcjtYQYNmGa8Aw9IzEFyt04wiZByEEpYAj0WGnxStulQt5qMWj+hby4mujbFwd8ryZl9W2uk2n/6WcUb8lKosu6+/8TZ1Pr6Am/XkM/SksFyxcvpT/4DFsuyIAvKWcWlvZkXvOFaRIlsO51Wes1UwZl34M9d3k9Dod279ZJOL22b7H6uloeZP+5dbV99sBPc8gUh8xgFf+X5+4exhzC/zz0gkhekV4eSHseCEqbPn9OPxuY+aosmXXcUhq1i7u7wKHlw7/XZ+GVdEw==";
		String strUrl = "https://coupa.ng/bRA62y";
		String strTitle = "쿠팡을 추천 합니다! 쿠팡! | 하림 에어프라이어 순살치킨 (냉동)";
		String strCategoryNo = "2";
		StringBuilder contentSb = new StringBuilder();

//		naverBlogLinkShare(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strCategoryNo, contentSb, rootPane);
//		naverBlogLinkShareView(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strCategoryNo, contentSb, rootPane);
//		naverBlogOpenApiShare(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strCategoryNo, contentSb, rootPane);
		linkSharePostWriteAsync(strBlogId, strNidAut, strNidSes, strUrl, strTitle, strCategoryNo, contentSb, rootPane);
	}

	public static boolean naverBlogLinkShare(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("naverBlogLinkShare strBlogId :[" + strBlogId + "]");
		logger.debug("naverBlogLinkShare strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("naverBlogLinkShare contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}

		try {
			String strRefererUrl = "https://blog.naver.com/LinkShare.nhn";
			String strLinkShareUrl = "https://blog.naver.com/LinkSharePostWriteAsync.nhn";
			URL url = new URL(strLinkShareUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("athority", "");
			headers.set("method", "");
			headers.set("path", "");
			headers.set("scheme", "");

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

//			headers.set("Cache-Control", "max-age=0");
//			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", host);
			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", strRefererUrl + "?url=" + strUrl + "&title=" + strEncodedTitle);

			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("domain", host);
			map.add("token", "1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f");
			map.add("timestamp", "1613308403301");
			String strEncodedUrl = URLEncoder.encode(strUrl, "UTF8");
			map.add("url", strEncodedUrl);
			map.add("blogId", strBlogId);
			map.add("title", strEncodedTitle);
			// 내용
//			map.add("content",URLEncoder.encode(contentSb.toString(), "UTF8"));

			String temp = "";
			// 아래의 span 태그가 없으면 공유실패함..
			// 쌍따옴표를 홑따옴표로 바꿔도 공유실패.
			temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"테스트(test)\"> </span>";
			temp += contentSb.toString();
			// do url encoding
			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content", temp);
			// postOpenType 공개 여부(비공개),0:비공개, 1:이웃공개, 2: 전체공개, 3:서로이웃공개
			map.add("postOpenType", "2");

			map.add("categoryNo", strCategoryNo);

			String referrer = "https://share.naver.com/web/shareView.nhn?url=" + strEncodedUrl + "&title="
					+ strEncodedTitle;
			map.add("referrer", referrer);

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme(protocol).host(host);
			builder = builder.path(path);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
			logger.debug("cookieSb.toString():" + cookieSb.toString());
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
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

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
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/LinkShare.nhn");
//			builder = builder.queryParam("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
			builder = builder.queryParam("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

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
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}
			sb.append(unzipString);

			logger.debug("body:" + unzipString);
			logger.debug("finished");

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
		logger.debug("encoding:" + encoding);
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

	/**
	 * 쿠키에 있는 NID_AUT,NID_SES 정보를 이용하여 네이버 카테고리 정보를 가져온다.
	 */
	public static List<String> getNaverBlogCategoryList(String strNidAut, String strNidSes) {
		List<String> categoryList = new ArrayList<>();
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut).append(";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes).append(";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/openapi/share");
			// 죽을 뻔 한 아기 수달을 살려줬더니 생긴 일 ㅣ What Happened After Rescuing A Nearly Dying Baby
			// Otter Is..
			String strUrl = "https://www.youtube.com/watch?v=J6zD3h_I3Lc";
			strUrl = URLEncoder.encode(strUrl, "UTF-8");
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			// [B@2460600f
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			logger.debug("unzipString:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
				if (!unzipString.equals("")) {
					Document doc = Jsoup.parse(unzipString);
					Elements categoryEls = doc.select("#_categoryList option");
					for (Element categoryEl : categoryEls) {
						String categoryNo = categoryEl.attr("value");
						String categoryName = categoryEl.text();
						String categoryNoAndName = categoryNo + ":" + categoryName;
						categoryList.add(categoryNoAndName);
					}
				}
			}
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryList;
	}

	public static Map<String, String> getNaverBlogCategoryMap(String strNidAut, String strNidSes) {
		Map<String, String> categoryMap = new HashMap<>();
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut).append(";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes).append(";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/openapi/share");
			// 죽을 뻔 한 아기 수달을 살려줬더니 생긴 일 ㅣ What Happened After Rescuing A Nearly Dying Baby
			// Otter Is..
			String strUrl = "https://www.youtube.com/watch?v=J6zD3h_I3Lc";
			strUrl = URLEncoder.encode(strUrl, "UTF-8");
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			// [B@2460600f
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			logger.debug("unzipString:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
				if (!unzipString.equals("")) {
					Document doc = Jsoup.parse(unzipString);
					Elements categoryEls = doc.select("#_categoryList option");
					for (Element categoryEl : categoryEls) {
						String categoryNo = categoryEl.attr("value");
						String categoryName = categoryEl.text();
						categoryMap.put(categoryNo, categoryName);
					}
				}
			}
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryMap;
	}

	public static List<String> getNaverBlogCategoryList2(String strNidAut, String strNidSes) {
		List<String> categoryList = new ArrayList<>();
		Elements categoryEls = getNaverBlogCategoryElements(strNidAut, strNidSes);
		for (Element categoryEl : categoryEls) {
			String categoryNo = categoryEl.attr("value");
			String categoryName = categoryEl.text();
			String categoryNoAndName = categoryNo + ":" + categoryName;
			categoryList.add(categoryNoAndName);
		}
		return categoryList;
	}

	public static Map<String, String> getNaverBlogCategoryMap2(String strNidAut, String strNidSes) {
		Map<String, String> categoryMap = new HashMap<>();
		Elements categoryEls = getNaverBlogCategoryElements(strNidAut, strNidSes);
		for (Element categoryEl : categoryEls) {
			String categoryNo = categoryEl.attr("value");
			String categoryName = categoryEl.text();
			categoryMap.put(categoryNo, categoryName);
		}
		return categoryMap;
	}

	public static String getNaverBlogCategoryNo(String strNidAut, String strNidSes, String strCategoryName) {
		String strCategoryNo = null;
		Elements categoryEls = getNaverBlogCategoryElements(strNidAut, strNidSes);
		for (Element categoryEl : categoryEls) {
			String categoryNo = categoryEl.attr("value");
			String categoryName = categoryEl.text();
			if (strCategoryName.equals(categoryName)) {
				strCategoryNo = categoryNo;
			}
		}
		return strCategoryNo;
	}

	public static Elements getNaverBlogCategoryElements(String strNidAut, String strNidSes) {
		Elements categoryElements = null;
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut).append(";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes).append(";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "blog.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
//			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3DaL55d6sDiGE%26feature%3Dshare");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/openapi/share");
			// 죽을 뻔 한 아기 수달을 살려줬더니 생긴 일 ㅣ What Happened After Rescuing A Nearly Dying Baby
			// Otter Is..
			String strUrl = "https://www.youtube.com/watch?v=J6zD3h_I3Lc";
			strUrl = URLEncoder.encode(strUrl, "UTF-8");
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			// [B@2460600f
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
			}

			logger.debug("unzipString:" + unzipString);
			if (response.getStatusCode() == HttpStatus.OK) {
				if (!unzipString.equals("")) {
					Document doc = Jsoup.parse(unzipString);
					categoryElements = doc.select("#_categoryList option");
				}
			}
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryElements;
	}

	/**
	 * gif image를 가져온다.
	 * 
	 * @param strNidAut
	 * @param strNidSes
	 * @return
	 */
	public static Elements getNaverBlogReplyOpen(String strNidAut, String strNidSes) {
		Elements categoryElements = null;
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
//					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
					"image/avif,image/webp,image/apng,image/svg+xml,image/*,*/*;q=0.8");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");

			StringBuilder cookieSb = new StringBuilder();
//			cookieSb.append("NID_AUT=");
//			cookieSb.append(strNidAut).append(";");
//			cookieSb.append("NID_SES=");
//			cookieSb.append(strNidSes).append(";");
//			cookieSb.append("NNB=J7WTOJDPSYRGA; nid_inf=658455308; NID_JKL=T4HuwC8nl4Ho1VSoNwrcOMJQdjsEI87kTKMXu5V4NEg=");
			cookieSb.append(
					"NNB=J7WTOJDPSYRGA; nid_inf=658188320; NID_AUT=8ocW/bWyQtAwbFe1jaknwXni/zAnrRdE+gpggAtm9DN4ZFyU9q6Z9VhQnTbS8Kbg; NID_SES=AAABmTwqXjkm14vAuPmr0XKyFHeatwErMUd+ww5rxi0Fa5WrI02Bj9QfxDBJPGsxNJzqyQGaXaG4PCka3KHB47Oj+GtDyyUypmTXhJwxFlqkbA8GLDjA5MBibZXhFlCDR7VeqG1omxubbcoy7cuMCHkPD1cAI9LPYV9uDyv0LiIrMFK+r59278IahUI0u4DHq2zUHUgP4mWwef4HSMlPJOE7hxHMcheeAiEG2egtTfgY8CMCCtW7XwsVwLBDxLeqHYIh9MvYiQ3xTQP5KHAi9GpSLIq2J3SrUx/K/FKq5CDEwoaI0udMNSgxLedBxBIksldzMcdoADLICla36y7OuTS5ZJnP3XRkd/L8HNImdOLvtuDkuMbyFrL8e/DE3uB61Y4eb1BatYqnQxsIEwoxH4WyevscFyol3xOVyDcFfn+spRXiRo5kHnerhagQD5Q4x7GPGnqYqKPr2mx4hXYy3qghEJe4u3YJfWPP85aOtI+6sxUvE2a24hhb2UZtJIYOTBv+RsXEU5vC3Rl5ndXiesCyyj3HK0AF3TDJ2d5FZaZ8nWsl; NID_JKL=APHY/rOeR6jC6ee5gqPjbxBx/jYVo50XE8GwGCCTzPM=");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "cc.naver.com");
//			headers.set("Origin", "http://blog.naver.com");
			headers.set("Referer",
					"https://blog.naver.com/PostView.nhn?blogId=banks&logNo=222239938204&categoryNo=0&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=&from=postList&userTopListOpen=true&userTopListCount=5&userTopListManageOpen=false&userTopListCurrentPage=1");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
//					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("Sec-Fetch-Dest", "image");
			headers.set("Sec-Fetch-Mode", "no-cors");
			headers.set("Sec-Fetch-Site", "same-site");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");

			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			logger.debug("___________2____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________2_____________");

//			RestTemplate restTemplate2 = new RestTemplate(messageConverters);
//			restTemplate.setMessageConverters(messageConverters);
			messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________3_____________");

			// Form Data
//			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			map.add("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(LINK_SHARE_URI_PREFIX);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("cc.naver.com");
			builder = builder.path("/cc");
			// 죽을 뻔 한 아기 수달을 살려줬더니 생긴 일 ㅣ What Happened After Rescuing A Nearly Dying Baby
			// Otter Is..

			builder = builder.queryParam("a", "RPC.replyopen");
			builder = builder.queryParam("r", "");
			builder = builder.queryParam("i", "");
			builder = builder.queryParam("bw", "756");
			builder = builder.queryParam("px", "261");
			builder = builder.queryParam("py", "734");
			builder = builder.queryParam("sx", "261");
			builder = builder.queryParam("sy", "-4612");
			builder = builder.queryParam("m", "0");
			builder = builder.queryParam("nsc", "blog.post");

			String strUrl = "https://blog.naver.com/PostView.nhn?blogId=banks&logNo=222239938204&categoryNo=0&parentCategoryNo=0&viewDate=&currentPage=1&postListTopCurrentPage=&from=postList&userTopListOpen=true&userTopListCount=5&userTopListManageOpen=false&userTopListCurrentPage=1#";
			strUrl = URLEncoder.encode(strUrl, "UTF-8");

			builder = builder.queryParam("u", strUrl);
			builder = builder.queryParam("time", "1613034429614");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
//			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			HttpEntity<?> entity = new HttpEntity<>(headers);
//			HttpEntity<String> entity = new HttpEntity<String>(headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
					byte[].class);
			logger.debug("response :" + response);

//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			byte[] responseBody = response.getBody();
			logger.debug("body :" + responseBody);
			String strGif = NaverUtil.stringFromBytes(responseBody, "UTF8");
			logger.debug("strGif :" + strGif);

			if (response.getStatusCode() == HttpStatus.OK) {
				if (responseBody != null) {
					// convert byte[] back to a BufferedImage
					InputStream is = new ByteArrayInputStream(responseBody);
					BufferedImage newBi = ImageIO.read(is);

					// add a text on top on the image, optional, just for fun
					Graphics2D g = newBi.createGraphics();
					g.setFont(new Font("TimesRoman", Font.BOLD, 30));
					g.setColor(Color.BLACK);
					g.drawString("Hello World", 100, 100);

					// save it
					Path target = Paths.get("./replyOpen.gif");
					ImageIO.write(newBi, "gif", target.toFile());
				}
			}
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return categoryElements;
	}

	// https://share.naver.com/web/shareView.nhn?url=https%3A%2F%2Fcoupa.ng%2FbRA62y&title=%EC%BF%A0%ED%8C%A1%EC%9D%84%20%EC%B6%94%EC%B2%9C%20%ED%95%A9%EB%8B%88%EB%8B%A4!%0A%EC%BF%A0%ED%8C%A1!%20%7C%20%ED%95%98%EB%A6%BC%20%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4%20%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8%20(%EB%83%89%EB%8F%99)
	// 카페
	// https://nid.naver.com/nidlogin.login?svctype=64&url=https%3A%2F%2Fm.cafe.naver.com%2FExternalScrapView.nhn%3FserviceCode%3Dshare%26url%3Dhttps%3A%2F%2Fcoupa.ng%2FbRA62y%26title%3D%25EC%25BF%25A0%25ED%258C%25A1%25EC%259D%2584%2B%25EC%25B6%2594%25EC%25B2%259C%2B%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4%2521%250A%25EC%25BF%25A0%25ED%258C%25A1%2521%2B%257C%2B%25ED%2595%2598%25EB%25A6%25BC%2B%25EC%2597%2590%25EC%2596%25B4%25ED%2594%2584%25EB%259D%25BC%25EC%259D%25B4%25EC%2596%25B4%2B%25EC%2588%259C%25EC%2582%25B4%25EC%25B9%2598%25ED%2582%25A8%2B%2528%25EB%2583%2589%25EB%258F%2599%2529%26token%3D855facd89ade606039319c9f0f32208cb0e033443ec76ed8ce97a8bce0758fc0%26timestamp%3D1613308042752%26isMobile%3Dfalse
	// 블로그
	// https://nid.naver.com/nidlogin.login?svctype=64&url=https%3A%2F%2Fblog.naver.com%2Fopenapi%2Fshare%3FserviceCode%3Dshare%26url%3Dhttps%3A%2F%2Fcoupa.ng%2FbRA62y%26title%3D%25EC%25BF%25A0%25ED%258C%25A1%25EC%259D%2584%2B%25EC%25B6%2594%25EC%25B2%259C%2B%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4%2521%250A%25EC%25BF%25A0%25ED%258C%25A1%2521%2B%257C%2B%25ED%2595%2598%25EB%25A6%25BC%2B%25EC%2597%2590%25EC%2596%25B4%25ED%2594%2584%25EB%259D%25BC%25EC%259D%25B4%25EC%2596%25B4%2B%25EC%2588%259C%25EC%2582%25B4%25EC%25B9%2598%25ED%2582%25A8%2B%2528%25EB%2583%2589%25EB%258F%2599%2529%26token%3D5db34acb53a343e4a00a381c9fa5cda8a6814af75a08afac208fd238220a012f%26timestamp%3D1613300985796%26isMobile%3Dfalse
	public static boolean naverBlogLinkShareView(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("naverBlogLinkShareView strBlogId :[" + strBlogId + "]");
		logger.debug("naverBlogLinkShareView strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("naverBlogLinkShareView contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://share.naver.com/web/shareView.nhn";
			// url=https%3A%2F%2Fcoupa.ng%2FbRA62y
			// &title=%EC%BF%A0%ED%8C%A1%EC%9D%84%20%EC%B6%94%EC%B2%9C%20%ED%95%A9%EB%8B%88%EB%8B%A4!%0A%EC%BF%A0%ED%8C%A1!%20%7C%20%ED%95%98%EB%A6%BC%20%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4%20%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8%20(%EB%83%89%EB%8F%99)
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", host);
			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", strShareViewUrl + "?url=" + strUrl + "&title=" + strEncodedTitle);

			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("domain", host);
			map.add("token", "");
			map.add("timestamp", "");
//			map.add("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
			String strEncodedUrl = URLEncoder.encode(strUrl, "UTF8");
			map.add("url", strEncodedUrl);
			map.add("blogId", strBlogId);
			map.add("title", strEncodedTitle);
			// 내용
//			map.add("content",URLEncoder.encode(contentSb.toString(), "UTF8"));

			String temp = "";
			// 아래의 span 태그가 없으면 공유실패함..
			// 쌍따옴표를 홑따옴표로 바꿔도 공유실패.
			temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"테스트(test)\"> </span>";
			temp += contentSb.toString();
			// do url encoding
			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content", temp);
			// 공개 여부(비공개),0:비공개, 1:이웃공개, 2: 전체공개, 3:서로이웃공개
			map.add("postOpenType", "2");

			map.add("categoryNo", strCategoryNo);

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host(host);
			builder = builder.path(path);
			builder = builder.queryParam("url", strUrl);
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);

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
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// https://blog.naver.com/openapi/share?serviceCode=share&url=https://coupa.ng/bRA62y&title=%EC%BF%A0%ED%8C%A1%EC%9D%84+%EC%B6%94%EC%B2%9C+%ED%95%A9%EB%8B%88%EB%8B%A4%21%0A%EC%BF%A0%ED%8C%A1%21+%7C+%ED%95%98%EB%A6%BC+%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4+%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8+%28%EB%83%89%EB%8F%99%29&token=1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f&timestamp=1613308403301&isMobile=false
	public static boolean naverBlogOpenApiShare(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("naverBlogOpenApiShare strBlogId :[" + strBlogId + "]");
		logger.debug("naverBlogOpenApiShare strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("naverBlogOpenApiShare contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://blog.naver.com/openapi/share";
			// url=https%3A%2F%2Fcoupa.ng%2FbRA62y
			// &title=%EC%BF%A0%ED%8C%A1%EC%9D%84%20%EC%B6%94%EC%B2%9C%20%ED%95%A9%EB%8B%88%EB%8B%A4!%0A%EC%BF%A0%ED%8C%A1!%20%7C%20%ED%95%98%EB%A6%BC%20%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4%20%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8%20(%EB%83%89%EB%8F%99)
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("athority", "");
			headers.set("method", "");
			headers.set("path", "");
			headers.set("scheme", "");

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

//			headers.set("Cache-Control", "max-age=0");
//			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

//			headers.set("Host", host);
//			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", strShareViewUrl + "?url=" + strUrl + "&title=" + strEncodedTitle);
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("sec-fetch-dest", "document");
			headers.set("sec-fetch-mode", "navigate");
			headers.set("sec-fetch-site", "same-site");
			headers.set("sec-fetch-user", "?1");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("domain", host);
			map.add("token", "");
			map.add("timestamp", "");
//			map.add("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
			String strEncodedUrl = URLEncoder.encode(strUrl, "UTF8");
			map.add("url", strEncodedUrl);
			map.add("blogId", strBlogId);
			map.add("title", strEncodedTitle);
			// 내용
//			map.add("content",URLEncoder.encode(contentSb.toString(), "UTF8"));

			String temp = "";
			// 아래의 span 태그가 없으면 공유실패함..
			// 쌍따옴표를 홑따옴표로 바꿔도 공유실패.
			temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"테스트(test)\"> </span>";
			temp += contentSb.toString();
			// do url encoding
			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content", temp);
			// 공개 여부(비공개),0:비공개, 1:이웃공개, 2: 전체공개, 3:서로이웃공개
			map.add("postOpenType", "2");

			map.add("categoryNo", strCategoryNo);

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host(host);
			builder = builder.path(path);
			builder = builder.queryParam("serviceCode", "share");
			builder = builder.queryParam("url", strUrl);// https://coupa.ng/bRA62y
			builder = builder.queryParam("title", strEncodedTitle);// 쿠팡을 추천 합니다!쿠팡! | 하림 에어프라이어 순살치킨 (냉동)
			builder = builder.queryParam("token", "1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f");
			builder = builder.queryParam("timestamp", "1613308403301");
			builder = builder.queryParam("isMobile", "false");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);

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
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// https://blog.naver.com/openapi/share?serviceCode=share&url=https://coupa.ng/bRA62y&title=%EC%BF%A0%ED%8C%A1%EC%9D%84+%EC%B6%94%EC%B2%9C+%ED%95%A9%EB%8B%88%EB%8B%A4%21%0A%EC%BF%A0%ED%8C%A1%21+%7C+%ED%95%98%EB%A6%BC+%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4+%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8+%28%EB%83%89%EB%8F%99%29&token=1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f&timestamp=1613308403301&isMobile=false
	public static boolean linkSharePostWriteAsync(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("linkSharePostWriteAsync strBlogId :[" + strBlogId + "]");
		logger.debug("linkSharePostWriteAsync strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("linkSharePostWriteAsync contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://blog.naver.com/LinkSharePostWriteAsync.nhn";
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("athority", "");
			headers.set("method", "");
			headers.set("path", "");
			headers.set("scheme", "");

			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");

//			headers.set("Cache-Control", "max-age=0");
//			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

//			headers.set("Host", host);
//			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", strShareViewUrl + "?url=" + strUrl + "&title=" + strEncodedTitle);
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("sec-fetch-dest", "document");
			headers.set("sec-fetch-mode", "navigate");
			headers.set("sec-fetch-site", "same-site");
			headers.set("sec-fetch-user", "?1");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("domain", host);
			map.add("token", "1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f");
			map.add("timestamp", "1613308403301");
			String strEncodedUrl = URLEncoder.encode(strUrl, "UTF8");
			map.add("url", strEncodedUrl);
			map.add("blogId", strBlogId);
			map.add("title", strEncodedTitle);
			// 내용
//			map.add("content",URLEncoder.encode(contentSb.toString(), "UTF8"));

			String temp = "";
			// 아래의 span 태그가 없으면 공유실패함..
			// 쌍따옴표를 홑따옴표로 바꿔도 공유실패.
			temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"테스트(test)\"> </span>";
			temp += contentSb.toString();
			// do url encoding
			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content", temp);
			// postOpenType 공개 여부(비공개),0:비공개, 1:이웃공개, 2: 전체공개, 3:서로이웃공개
			map.add("postOpenType", "2");

			map.add("categoryNo", strCategoryNo);

			String referrer = "https://share.naver.com/web/shareView.nhn?url=" + strEncodedUrl + "&title="
					+ strEncodedTitle;
			map.add("referrer", referrer);

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host(host);
			builder = builder.path(path);

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// https://blog.naver.com/isLogin.nhn
	public static boolean isLogin(String strBlogId, String strNidAut, String strNidSes, String strUrl, String strTitle,
			String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("isLogin strBlogId :[" + strBlogId + "]");
		logger.debug("isLogin strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("isLogin contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://blog.naver.com/isLogin.nhn";
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("athority", "blog.naver.com");
			headers.set("method", "GET");
			headers.set("path", "/isLogin.nhn");
			headers.set("scheme", "https");

			headers.set("Accept", "*/*");
//			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("charset", "utf-8");
			headers.set("content-type", "application/x-www-form-urlencoded; charset=utf-8");
//			headers.set("cookie","NNB=J7WTOJDPSYRGA; NRTK=ag#all_gr#1_ma#-2_si#0_en#0_sp#0; bHideResizeNotice=1; _fbp=fb.1.1614255074399.1149044494; _ga=GA1.2.206197184.1614255075; nx_ssl=2; MM_NEW=1; NFS=2; MM_NOW_COACH=1; bNoAutoAttachOG=false; ASID=af7cbab4000001787415e5270000005e; nid_inf=591293557; NID_AUT=ab78fa4tlkKz/0Ltn6yrB7M3nOzfUj1WPDS/rhoVzTCP8CZo99RE11WmMninjrfJ; NID_JKL=lvUlefBquwQGpnisVk792NHlLeODx2Y7tU9kz39Huxw=; stat_yn=1; NID_SES=AAABjCn98L1L313FsolQLeNeWOCqzB6pBV9NhFLzq5gPgVWWMvmX750Rf07R1FnokiCB5i99HkIpYwN78UnJiAXASIWtizU50QGaU9YuzKozRFfhEVWbh6U/lnD29Ru3s0kJk/HRcponJ/Hi+EtafYLwPVyxsgicS22z9UUNISJqinjUSIsslhLK2F5+7RfKhB267zbE8NZnln1d0JiOdwACMekbBmrEqkPH6/tpMCjlPQXzN/YY5XAGtSzusWKUjXU+kek4J1naLftZDpqfhl7cvxpOw9IdqHFKPpN6sm2g3f8gmI/f9Io2a2NJsbP6RlaPfzzK0akgBtEUL7/KnFPFHCc/OYCIHgDNmivZ8TtkhD0jKyNsdvWFiHWdKn/shNJ0gtpG6U34ohyDknwbDcgDQpOn4gTQijCyAMeBgjR5N0cG/kIuQgDrfgo69MWnwbEziej4KXxP3TLpz2oYYseCBsXvG/eMiKkm3XcyOUIN5OmQWDuLOLNSXxBi7b8tvi312Gy+W/dmKFbo1+PlaI20g3U=; JSESSIONID=668C1102E76375E2390EF1E6E89E476F.jvm1");
			headers.set("referer", "https://blog.naver.com/" + strBlogId + "/postwrite?categoryNo=" + strCategoryNo);
			headers.set("sec-ch-ua", "\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("sec-fetch-dest", "empty");
			headers.set("sec-fetch-mode", "cors");
			headers.set("sec-fetch-site", "same-origin");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
			headers.set("x-requested-with", "XMLHttpRequest");

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

//			headers.set("Host", host);
//			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host(host);
			builder = builder.path(path);

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);
			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// https://static.nid.naver.com/getLoginStatus.nhn?callback=showGNB&charset=utf-8&svc=admin.blog&template=gnb_utf8&one_naver=0
	public static boolean getLoginStatus(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("getLoginStatus strBlogId :[" + strBlogId + "]");
		logger.debug("getLoginStatus strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("getLoginStatus contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://static.nid.naver.com/getLoginStatus.nhn";
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();

			headers.set("athority", "");
			headers.set("method", "");
			headers.set("path", "");
			headers.set("scheme", "");

			headers.set("Accept", "*/*");
//			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Connection", "keep-alive");

			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

			headers.set("Host", "static.nid.naver.com");
//			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", "https://blog.naver.com/" + strBlogId + "/postwrite?categoryNo=" + strCategoryNo);
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
			headers.set("sec-fetch-dest", "script");
			headers.set("sec-fetch-mode", "no-cors");
			headers.set("sec-fetch-site", "same-site");
			headers.set("sec-fetch-user", "?1");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host(host);
			builder = builder.path(path);

			builder = builder.queryParam("allback", "showGNB");
			builder = builder.queryParam("charset", "utf-8");
			builder = builder.queryParam("svc", "admin.blog");
			builder = builder.queryParam("template", "gnb_utf8");
			builder = builder.queryParam("one_naver", "0");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);

			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// https://static.nid.naver.com/getLoginStatus.nhn?callback=showGNB&charset=utf-8&svc=admin.blog&template=gnb_utf8&one_naver=0
	public static boolean postUpdate(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("getLoginStatus strBlogId :[" + strBlogId + "]");
		logger.debug("getLoginStatus strCategoryNo :[" + strCategoryNo + "]");
		logger.debug("getLoginStatus contentSb :[" + contentSb + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
		}

		try {
			String strShareViewUrl = "https://blog.naver.com/PostUpdate.nhn";
			URL url = new URL(strShareViewUrl);
			String protocol = url.getProtocol();
			String host = url.getHost();
			String protocolHost = protocol + "://" + host;
			String path = url.getPath();
			String query = url.getQuery();
			int port = url.getPort();

			HttpHeaders headers = new HttpHeaders();
			
			headers.set("athority", "blog.naver.com");
			headers.set("method", "POST");
			headers.set("path", "/PostUpdate.nhn");
			headers.set("scheme", "https");

			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			
			headers.set("Connection", "keep-alive");
			headers.set("cache-control","max-age=0");
//			headers.set("content-length","");
			headers.set("content-type","application/x-www-form-urlencoded");
			
			StringBuilder cookieSb = new StringBuilder();
			cookieSb.append("NID_AUT=");
			cookieSb.append(strNidAut + ";");
			cookieSb.append("NID_SES=");
			cookieSb.append(strNidSes + ";");

			headers.set("Cookie", cookieSb.toString());

//			headers.set("Host", "static.nid.naver.com");
			headers.set("Origin", protocolHost);

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer",protocolHost+"/PostUpdateForm.nhn?blogId="+strBlogId+"&Redirect=Update&cpage=1&sourceCode=0&logNo=222293009975&redirect=Update&widgetTypeCall=true&topReferer=https%3A%2F%2Fblog.naver.com%2FPostList.nhn%3FblogId%3D"+strBlogId+"%26from%3DpostList%26categoryNo%3D"+strCategoryNo+"&directAccess=false");
				
			headers.set("sec-ch-ua", "\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile", "?0");
//			headers.set("sec-fetch-dest", "script");
			headers.set("sec-fetch-dest", "iframe");
//			headers.set("sec-fetch-mode", "no-cors");
			headers.set("sec-fetch-mode", "navigate");
//			headers.set("sec-fetch-site", "same-site");
			headers.set("sec-fetch-site", "same-origin");
			headers.set("sec-fetch-user", "?1");
			headers.set("Upgrade-Insecure-Requests", "1");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.forEach((key, value) -> {
				logger.debug(String.format("Header '%s' = %s", key, value));
			});

			RestTemplate restTemplate = new RestTemplate();

//			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
			logger.debug("__________1_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				logger.debug(httpMessageConverter.toString());
			}
			logger.debug("__________1_____________");
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host(host);
			builder = builder.path(path);
			
			builder = builder.queryParam("captchaKey", "");
			builder = builder.queryParam("captchaValue", "");
			builder = builder.queryParam("appId", "");
			builder = builder.queryParam("tempLogNo", "");
			builder = builder.queryParam("blogId", strBlogId);
			builder = builder.queryParam("post.logNo", "222293009975");
			builder = builder.queryParam("post.sourceCode", "0");
			builder = builder.queryParam("post.contents.contentsValue", contentSb.toString());
			builder = builder.queryParam("post.prePostRegistDirectly", "false");
			builder = builder.queryParam("post.lastRelayTime", "");
			builder = builder.queryParam("smartEditorVersion", "2");
			builder = builder.queryParam("post.book.ratingScore", "0");
			builder = builder.queryParam("post.music.ratingScore", "0");
			builder = builder.queryParam("post.movie.ratingScore", "0");
			builder = builder.queryParam("post.scrapedYn", "false");
			builder = builder.queryParam("post.clientType", "");
			builder = builder.queryParam("post.contents.summaryYn", "false");
			builder = builder.queryParam("post.contents.summaryToggleText", "");
			builder = builder.queryParam("post.contents.summaryTogglePosition", "");
			builder = builder.queryParam("post.templatePhoto.width", "0");
			builder = builder.queryParam("post.templatePhoto.height", "0");
			builder = builder.queryParam("post.addedInfoSet.addedInfoStruct", "");
			builder = builder.queryParam("post.mapAttachmentSet.mapAttachStruct", "");
			builder = builder.queryParam("post.calendarAttachmentSet.calendarAttachmentStruct", "");
			builder = builder.queryParam("post.musicPlayerAttachmentSet.musicPlayerAttachmentStruct", "");
			builder = builder.queryParam("post.postOptions.openType", "2");
			builder = builder.queryParam("post.postOptions.commentYn", "true");
			builder = builder.queryParam("post.postOptions.isRelayOpen", "");
			builder = builder.queryParam("post.postOptions.sympathyYn", "true");
			builder = builder.queryParam("post.postOptions.outSideAllowYn", "true");
			builder = builder.queryParam("post.me2dayPostingYn", "");
			builder = builder.queryParam("post.facebookPostingYn", "false");
			builder = builder.queryParam("post.twitterPostingYn", "false");
			builder = builder.queryParam("post.postOptions.searchYn", "true");
			builder = builder.queryParam("post.postOptions.rssOpenYn", "true");
			builder = builder.queryParam("post.postOptions.scrapType", "2");
			builder = builder.queryParam("post.postOptions.ccl.commercialUsesYn", "false");
			builder = builder.queryParam("post.postOptions.ccl.contentsModification", "0");
			builder = builder.queryParam("post.postOptions.noticePostYn", "false");
			builder = builder.queryParam("directorySeq", "0");
			builder = builder.queryParam("directoryDetail", "");
			builder = builder.queryParam("post.bookTheme.infoPk", "");
			builder = builder.queryParam("post.movieTheme.infoPk", "");
			builder = builder.queryParam("post.musicTheme.infoPk", "");
			builder = builder.queryParam("post.kitchenTheme.recipeName", "");
			builder = builder.queryParam("post.postOptions.directoryOptions.directoryChangeYn", "false");
			builder = builder.queryParam("post.postOptions.directoryOptions.tagAutoChangedYn", "false");
			builder = builder.queryParam("post.postOptions.isAutoTaggingEnabled", "false");
			builder = builder.queryParam("post.postOptions.greenReviewBannerYn", "false");
			builder = builder.queryParam("previewGreenReviewBannerAsInteger", "0");
			builder = builder.queryParam("post.leverageOptions.themeSourceCode", "");
			builder = builder.queryParam("post.music.subType", "");
			builder = builder.queryParam("post.postOptions.isContinueSaved", "false");
			builder = builder.queryParam("post.mrBlogTalk.talkCode", "");
			builder = builder.queryParam("happyBeanGiveDayReqparam", "");
			builder = builder.queryParam("post.postOptions.isExifEnabled", "false");
			builder = builder.queryParam("editorSource", "0F7mfBFoFPpXb0MOz2JxMQ==");
			builder = builder.queryParam("post.category.categoryNo", "271");
			builder = builder.queryParam("post.title", "WOW 와우회원 전용 매일 오전 7시 골드박스 1일특가");
			builder = builder.queryParam("ir1", contentSb.toString());
			builder = builder.queryParam("query", "지역명을 입력해 주세요");
			builder = builder.queryParam("char_preview", "®º⊆●○");
			builder = builder.queryParam("se2_tbp", "on");
			builder = builder.queryParam("se2_tbp3", "on");
			builder = builder.queryParam("post.directorySeq", "0");
			builder = builder.queryParam("post.tag.names", "#에그팬");
			builder = builder.queryParam("openType", "2");
			builder = builder.queryParam("post.postWriteTimeType", "now");
			builder = builder.queryParam("prePostDay", "2021년 3월 31일");
			builder = builder.queryParam("prePostDateType.hour", "00");
			builder = builder.queryParam("prePostDateType.minute", "00");
			builder = builder.queryParam("prePostDateType.year", "2021");
			builder = builder.queryParam("prePostDateType.month", "3");
			builder = builder.queryParam("prePostDateType.date", "31");
			builder = builder.queryParam("commercialUses", "false");
			builder = builder.queryParam("contentsModification", "0");
			builder = builder.queryParam("writingMaterialInfos", "[]");

			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			logger.debug("uri:" + uri);
			logger.debug("uri path:" + uri.getPath());

			logger.debug("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
					headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
			logger.debug("response :" + response);

			logger.debug("response.getStatusCode():" + response.getStatusCode());
			HttpHeaders responseHeaders = response.getHeaders();
			responseHeaders.forEach((key, value) -> {
				logger.debug(String.format("Response Header [%s] = %s", key, value));
			});

//			logger.debug("guessEncoding :" + guessEncoding(response.getBody()));
			logger.debug("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			logger.debug("unzipString :" + unzipString);
			logger.debug("finished");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
