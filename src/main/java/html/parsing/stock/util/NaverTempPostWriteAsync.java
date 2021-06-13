package html.parsing.stock.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.brotli.dec.BrotliInputStream;
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

public class NaverTempPostWriteAsync {

	private static final Logger logger = LoggerFactory.getLogger(NaverTempPostWriteAsync.class);

	public static void main(String[] args) {
		new NaverTempPostWriteAsync();
	}
	
	NaverTempPostWriteAsync(){
		naverBlogLinkShare();
	}

	public static boolean naverBlogLinkShare() {
		try {
			HttpHeaders headers = new HttpHeaders();
			
			headers.set("authority", "blog.naver.com");
			headers.set("method", "POST");
			headers.set("path", "/TempPostWriteAsync.nhn");
			headers.set("scheme", "https");
			
//			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept", "*/*");
			headers.set("Accept-Encoding", "gzip, deflate, br");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			
			headers.set("ajax", "true");
			headers.set("charset", "utf-8");
			headers.set("content-length", "2826");
			headers.set("content-type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.set("cookie", "NNB=C7BPGOO3VPKF6; NRTK=ag#all_gr#1_ma#-2_si#0_en#0_sp#0; ASID=af7cbab400000176b851a69200000064; bNoAutoAttachOG=false; bHideResizeNotice=1; nx_ssl=2; stat_yn=1; JSESSIONID=5E2D6F9003838B8D347ED707DFB33053.jvm1");
			headers.set("origin", "https://blog.naver.com");
			headers.set("referer", "https://blog.naver.com/PostWriteForm.nhn?blogId=soonks21&Redirect=Write&categoryNo=2&redirect=Write&widgetTypeCall=true&topReferer=https%3A%2F%2Fblog.naver.com%2Fsoonks21%2F222236832467&directAccess=false");

			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
//			headers.set("Content-Length", "4148");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			// headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

//			headers.set("Host", "blog.naver.com");

			headers.set("sec-ch-ua","\"Chromium\";v=\"88\", \"Google Chrome\";v=\"88\", \";Not A Brand\";v=\"99\"");
			headers.set("sec-ch-ua-mobile","?0");
			headers.set("ec-fetch-dest","empty");
			headers.set("sec-fetch-mode","cors");
			headers.set("sec-fetch-site","same-origin");			
//			headers.set("Upgrade-Insecure-Requests", "1");
//			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36");
			headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36");

//			headers.set("X-Requested-With", "XMLHttpRequest");
			headers.set("x-requested-with", "XMLHttpRequest");
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

			messageConverters = restTemplate.getMessageConverters();
			System.out.println("__________3_____________");
			for (HttpMessageConverter httpMessageConverter : messageConverters) {
				System.out.println(httpMessageConverter);
			}
			System.out.println("__________3_____________");

			// Form Data
			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();

			map.add("captchaKey","");
			map.add("captchaValue","");
			map.add("appId","");
			map.add("tempLogNo","");
			map.add("blogId","soonks21");
			map.add("post.logNo","");
			map.add("post.sourceCode","0");
			map.add("post.contents.contentsValue","%3Cp%3E%E3%85%85%E3%84%B7%3C%2Fp%3E");
			map.add("post.prePostRegistDirectly","false");
			map.add("post.lastRelayTime","");
			map.add("smartEditorVersion","2");
			map.add("post.book.ratingScore","0");
			map.add("post.music.ratingScore","0");
			map.add("post.movie.ratingScore","0");
			map.add("post.scrapedYn","false");
			map.add("post.clientType","");
			map.add("post.contents.summaryYn","false");
			map.add("post.contents.summaryToggleText","");
			map.add("post.contents.summaryTogglePosition","");
			map.add("post.templatePhoto.width","0");
			map.add("post.templatePhoto.height","0");
			map.add("post.addedInfoSet.addedInfoStruct","");
			map.add("post.mapAttachmentSet.mapAttachStruct","");
			map.add("post.calendarAttachmentSet.calendarAttachmentStruct","");
			map.add("post.musicPlayerAttachmentSet.musicPlayerAttachmentStruct","");
			map.add("post.postOptions.openType","2");
			map.add("post.postOptions.commentYn","true");
			map.add("post.postOptions.isRelayOpen","");
			map.add("post.postOptions.sympathyYn","true");
			map.add("post.postOptions.outSideAllowYn","true");
			map.add("post.me2dayPostingYn","");
			map.add("post.facebookPostingYn","false");
			map.add("post.twitterPostingYn","false");
			map.add("post.postOptions.searchYn","true");
			map.add("post.postOptions.rssOpenYn","true");
			map.add("post.postOptions.scrapType","2");
			map.add("post.postOptions.ccl.commercialUsesYn","false");
			map.add("post.postOptions.ccl.contentsModification","");
			map.add("post.postOptions.noticePostYn","false");
			map.add("directorySeq","0");
			map.add("directoryDetail","");
			map.add("post.bookTheme.infoPk","");
			map.add("post.movieTheme.infoPk","");
			map.add("post.musicTheme.infoPk","");
			map.add("post.kitchenTheme.recipeName","");
			map.add("post.postOptions.directoryOptions.directoryChangeYn","false");
			map.add("post.postOptions.directoryOptions.tagAutoChangedYn","false");
			map.add("post.postOptions.isAutoTaggingEnabled","false");
			map.add("post.postOptions.greenReviewBannerYn","false");
			map.add("previewGreenReviewBannerAsInteger","0");
			map.add("post.leverageOptions.themeSourceCode","");
			map.add("post.music.subType","");
			map.add("post.postOptions.isContinueSaved","false");
			map.add("post.mrBlogTalk.talkCode","");
			map.add("happyBeanGiveDayReqparam","");
			map.add("post.postOptions.isExifEnabled","false");
			map.add("editorSource","OkrX6o7LmyoqZPeZtS1yqA==");
			map.add("post.category.categoryNo","2");
			map.add("post.title","2021%EB%85%84%202%EC%9B%94%208%EC%9D%BC%20%EC%98%A4%ED%9B%84%209%EC%8B%9C%2021%EB%B6%84%EC%97%90%20%EC%A0%80%EC%9E%A5%ED%95%9C%20%EA%B8%80%EC%9E%85%EB%8B%88%EB%8B%A4.");
			map.add("ir1","");
			map.add("query","지역명을 입력해 주세요");
			map.add("char_preview","®º⊆●○");
			map.add("se2_tbp","on");
			map.add("se2_tbp3","on");
			
			map.add("post.directorySeq","0");
			map.add("post.tag.names","태그와 태그는 쉼표로 구분하며, 30개까지 입력하실 수 있습니다.");
			map.add("openType","2");
			map.add("post.postWriteTimeType","now");
			map.add("prePostDay","2021년 02월 08일");
			map.add("prePostDateType.hour","20");
			map.add("prePostDateType.minute","50");
			map.add("prePostDateType.year","");
			map.add("prePostDateType.month","");
			map.add("prePostDateType.date","");
			map.add("commercialUses","false");
			map.add("contentsModification","0");
			map.add("writingMaterialInfos","[]");
			
			
			// header에 있으면 Form Data에 없어도 된다.
//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("https").host("blog.naver.com");
			builder = builder.path("/TempPostWriteAsync.nhn");
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

			String unzipString = unzipStringFromBytes(response.getBody(), "UTF8");

			System.out.println("unzipString :" + unzipString);
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
		if(bytes == null) return null;
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
