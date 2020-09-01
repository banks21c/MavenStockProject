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

public class NaverUtil {

	private static final Logger logger = LoggerFactory.getLogger(NaverUtil.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int serial = 567;
		// String serial = "123";
		String suffix = String.format("%05s", serial);
		System.out.println(suffix);
	}

	public static boolean naverBlogLinkShare(String strNidAut, String strNidSes, String strUrl, String strTitle,
		String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		System.out.println("contentSb :["+contentSb+"]:contentSb");
		System.out.println("strCategoryNo :["+strCategoryNo+"]:strCategoryNo");
		if (strNidAut.equals("") || strNidSes.equals("")) {
			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning",
				JOptionPane.WARNING_MESSAGE);
			return false;
		}

		try {
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

			headers.set("Host", "blog.naver.com");
			headers.set("Origin", "http://blog.naver.com");

			String strEncodedTitle = URLEncoder.encode(strTitle, "UTF8");
			logger.debug("strEncodedTitle==>" + strEncodedTitle);

			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=" + strUrl + "&title=" + strEncodedTitle);

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
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

//			map.add("domain", "www.youtube.com");
			map.add("domain", "www.asiae.co.kr");
			map.add("token", "");
			map.add("timestamp", "");
//			map.add("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
			String strEncodedUrl = URLEncoder.encode(strUrl, "UTF8");
			map.add("url", strEncodedUrl);
			map.add("blogId", "banks");
			map.add("title", strEncodedTitle);
//			temp += contentSb.toString();
//			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content",URLEncoder.encode(contentSb.toString(), "UTF8"));
			String temp = "";

			temp = "%3Cspan%20id%3D%22se_object_1592901264185%22%20class%3D%22__se_object%22%20s_type%3D%22leverage%22%20s_subtype%3D%22oglink%22%20jsonvalue%3D%22%257B%2522url%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253DJ6zD3h_I3Lc%2526feature%253Dshare%2522%252C%2522domain%2522%253A%2522www.youtube.com%2522%252C%2522title%2522%253A%2522%25EC%25A3%25BD%25EC%259D%2584%2520%25EB%25BB%2594%2520%25ED%2595%259C%2520%25EC%2595%2584%25EA%25B8%25B0%2520%25EC%2588%2598%25EB%258B%25AC%25EC%259D%2584%2520%25EC%2582%25B4%25EB%25A0%25A4%25EC%25A4%25AC%25EB%258D%2594%25EB%258B%2588%2520%25EC%2583%259D%25EA%25B8%25B4%2520%25EC%259D%25BC%2520%25E3%2585%25A3%2520What%2520Happened%2520After%2520Rescuing%2520A%2520Nearly%2520Dying%2520Baby%2520Otter%2520Is..%2522%252C%2522description%2522%253A%2522%25ED%2595%2598%25EB%258A%2594%2520%25EC%25A7%2593%25EB%25A7%2588%25EB%258B%25A4%2520%25EB%25A9%258D%25EB%25AD%2589%25EB%25AF%25B8%2520%25EB%2584%2598%25EC%25B9%2598%25EB%258A%2594%2520%25EC%2588%2598%25EB%258B%25AC%2520%2526%252339%253B%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%2526%252339%253B%2520%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%25EA%25B0%2580%2520%25EC%2582%25AC%25EC%259C%25A1%25EC%2582%25AC%25EB%25A5%25BC%2520%25EB%2594%25B0%25EB%25A5%25B4%25EA%25B8%25B0%2520%25EC%258B%259C%25EC%259E%2591%25ED%2595%259C%2520%25EC%259D%25B4%25EC%259C%25A0%25EB%258A%2594..%2520%2523%25EB%258F%2599%25EB%25AC%25BC%25EB%2586%258D%25EC%259E%25A5%2520%2523%25EC%2595%25A0%25EB%258B%2588%25EB%25A9%2580%25EB%25B4%2590%25EC%2588%2598%25EB%258B%25AC%2520%2523%25EA%25B0%259C%25EC%2588%2598%25EB%258B%25AC%25ED%2596%2587%25EB%258B%2598%25EC%259D%25B4%2520-------------------------------------------------%2520%25EC%2595%25A0%25EB%258B%2588%25EB%25A9%2580%25EB%25B4%2590%25EC%2599%2580%2520%25ED%2595%259C%25EB%25B0%25B0%25ED%2583%2580%25EA%25B3%25A0%25E2%259B%25B5%2520%25E2%2598%259E%2520https%253A%252F%252Fgoo.gl%252FWL9mGy%2520%25ED%2596%2587...%2522%252C%2522type%2522%253A%2522video%2522%252C%2522image%2522%253A%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252FJ6zD3h_I3Lc%252Fhqdefault.jpg%2522%252C%2522width%2522%253A480%252C%2522height%2522%253A360%257D%252C%2522allImages%2522%253A%255B%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252FJ6zD3h_I3Lc%252Fhqdefault.jpg%2522%252C%2522width%2522%253A480%252C%2522height%2522%253A360%257D%255D%252C%2522video%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fembed%252FJ6zD3h_I3Lc%2522%252C%2522site%2522%253A%2522YouTube%2522%252C%2522layoutType%2522%253A1%257D%22%3E%3C%2Fspan%3E%3Cbr%3E%EA%B7%80%EC%97%AC%EC%9B%8C%EC%9A%94...";
			temp = URLDecoder.decode(temp, "UTF-8");
			Document doc = Jsoup.parse(temp);
			logger.debug("doc:" + doc);
			String jsonvalue = doc.select("span").attr("jsonvalue");
			logger.debug("jsonvalue:" + jsonvalue);
			String decodedJsonvalue = URLDecoder.decode(jsonvalue, "UTF8");
			logger.debug("decodedJsonvalue:" + decodedJsonvalue);
			if (!decodedJsonvalue.equals("")) {

				JSONObject jobj = new JSONObject(decodedJsonvalue);
				Iterator it = jobj.keys();
				logger.debug("================");
				while (it.hasNext()) {
					String key = (String) it.next();
					Object valueObj = jobj.get(key);
					String value = "";
					if (valueObj instanceof String) {
						value = (String) valueObj;
					} else if (valueObj instanceof JSONObject) {
						value = valueObj.toString();
					}else{
						value = String.valueOf(valueObj);
					}

					logger.debug(key + ":" + value);
				}
			}
			logger.debug("================");

			// 아래의 span 태그가 없으면 공유실패함..
			temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"테스트(test)\"></span>";
//			temp += contentSb.toString();
			
			temp = URLEncoder.encode(temp, "UTF8");
			map.add("content", temp);
			// 공개 여부(비공개),0:비공개, 1:이웃공개, 2: 전체공개, 3:서로이웃공개
			map.add("postOpenType", "2");

			map.add("categoryNo", strCategoryNo);

			// header에 있으면 Form Data에 없어도 된다.
			// map.add("referrer",
			// "https://blog.naver.com/openapi/share?url=https://www.asiae.co.kr/article/nationaldefense-diplomacy/2020062421382026021&title=%E5%8C%97,%20%EC%A0%95%EA%B2%BD%EB%91%90%20%EA%B5%AD%EB%B0%A9%EC%9E%A5%EA%B4%80%EC%97%90%20%22%EA%B2%81%20%EB%A8%B9%EC%9D%80%20%EA%B0%9C%EA%B0%80%20%EB%8D%94%20%EC%9A%94%EB%9E%80%22%20%EA%B2%BD%EA%B3%A0");
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/LinkSharePostWriteAsync.nhn");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map,
				headers);
//			ResponseEntity<byte[]> response = restTemplate.exchange(uri, HttpMethod.POST, entity, byte[].class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
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
			System.out.println("body :" + response.getBody());

			String unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");

			System.out.println("unzipString :" + unzipString);
			System.out.println("finished");

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
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");
			builder = builder.path("/LinkShare.nhn");
//			builder = builder.queryParam("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3DJ6zD3h_I3Lc%26feature%3Dshare");
			builder = builder.queryParam("url", "https://www.youtube.com/watch?v=J6zD3h_I3Lc&feature=share");
			UriComponents uriComponents = builder.build();
			URI uri = uriComponents.toUri();
			System.out.println("uri:" + uri);
			System.out.println("uri path:" + uri.getPath());

			System.out.println("uriComponents :" + uriComponents);
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
			byte[] responseBody = response.getBody();
			System.out.println("body :" + responseBody);
			String unzipString = "";
			if (responseBody != null) {
				unzipString = NaverUtil.unzipStringFromBytes(response.getBody(), "UTF8");
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
