package html.parsing.stock.util;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.swing.JRootPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class NaverUtil3 {

	private static final Logger logger = LoggerFactory.getLogger(NaverUtil3.class);

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

	// https://blog.naver.com/openapi/share?serviceCode=share&url=https://coupa.ng/bRA62y&title=%EC%BF%A0%ED%8C%A1%EC%9D%84+%EC%B6%94%EC%B2%9C+%ED%95%A9%EB%8B%88%EB%8B%A4%21%0A%EC%BF%A0%ED%8C%A1%21+%7C+%ED%95%98%EB%A6%BC+%EC%97%90%EC%96%B4%ED%94%84%EB%9D%BC%EC%9D%B4%EC%96%B4+%EC%88%9C%EC%82%B4%EC%B9%98%ED%82%A8+%28%EB%83%89%EB%8F%99%29&token=1ded42475374579f8d9197a195c6813e55fb5fd60e9b75264e99da234856586f&timestamp=1613308403301&isMobile=false
	public static boolean linkSharePostWriteAsync(String strBlogId, String strNidAut, String strNidSes, String strUrl,
			String strTitle, String strCategoryNo, StringBuilder contentSb, JRootPane rootPane) {
		logger.debug("strBlogId :[" + strBlogId + "]");
		logger.debug("contentSb :[" + contentSb + "]");
		logger.debug("strCategoryNo :[" + strCategoryNo + "]");
		if (strNidAut.equals("") || strNidSes.equals("")) {
//			JOptionPane.showMessageDialog(rootPane, "NID_AUT와 NID_SES를 입력하여 주세요.", "Warning", JOptionPane.WARNING_MESSAGE);
			logger.debug("NID_AUT와 NID_SES를 입력하여 주세요.");
//			return false;
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

			headers.set("Referer", strRefererUrl + "?url=" + strUrl + "&title=" + strEncodedTitle);
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

}
