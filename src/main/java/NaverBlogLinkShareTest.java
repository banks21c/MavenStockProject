
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.common.collect.Lists;
import java.net.URLEncoder;
import java.nio.file.Files;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

public class NaverBlogLinkShareTest {

	public static final String SERVER_URI = "http://blog.naver.com/LinkSharePostWriteAsync.nhn";
	private static final Logger logger = LoggerFactory.getLogger(NaverBlogLinkShareTest.class);

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new NaverBlogLinkShareTest();
	}

	NaverBlogLinkShareTest() throws IOException, Exception {
//        fetchFiles();
		// downloadTest1("searchCorpList", "download.html");
		// downloadTest1("download", "download.xls");
//		fetchFiles();
//		fetchFiles2();
		excelDownloadUsingByteArrayHttpMessageConverter("");
//		excelDownloadUsingStringHttpMessageConverter("stockMkt");
//		excelDownloadUsingStringHttpMessageConverter("kosdaqMkt");
//		excelDownloadUsingStringHttpMessageConverter2("kosdaqMkt");
//		printStockInfoUsingStringHttpMessageConverter("kosdaqMkt");
//		printStockInfoUsingJsoup("stockMkt");
//		printStockInfoUsingJsoup("kosdaqMkt");
//		printStockInfoUsingJsoup1("");
//		printStockInfoUsingJsoup2("");
	}

	private static void downloadTest1(String gubun, String fileName) {
		System.out.println("gubun :" + gubun);
		KindKrxCoKrVO vo = new KindKrxCoKrVO();
		vo.setMethod(gubun);
		vo.setPageIndex("1");
		vo.setCurrentPageSize("15");
		vo.setComAbbrv("");
		vo.setBeginIndex("");
		vo.setOrderMode("3");
		vo.setOrderStat("D");
		vo.setIsurCd("");
		vo.setRepIsuSrtCd("");
		vo.setSearchCodeType("");
		vo.setMarketType("");
		vo.setSearchType("13");
		vo.setIndustry("");
		vo.setFiscalYearEnd("all");
		vo.setComAbbrvTmp("");
		vo.setLocation("all");

		try {
//            Object response1 = restTemplate.getForObject(SERVER_URI, KindKrxCoKrVO.class);
//            Object response2 = restTemplate.postForObject(SERVER_URI, vo, KindKrxCoKrVO.class);
//            List<LinkedHashMap> response3 = restTemplate.postForObject(SERVER_URI, vo, List.class);
//            Object response4 = restTemplate.postForObject(SERVER_URI, vo, Object.class);

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

			messageConverters.add(new ByteArrayHttpMessageConverter());

//            RestTemplate restTemplate = new RestTemplate();
			RestTemplate restTemplate = new RestTemplate(messageConverters);
//            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

			Map<String, String> param = new HashMap<String, String>();
			param.put("method", "searchCorpList");
			param.put("pageIndex", "1");
			param.put("currentPageSize", "15");
			param.put("comAbbrv", "");
			param.put("beginIndex", "");
			param.put("orderMode", "3");
			param.put("orderStat", "D");
			param.put("isurCd", "");
			param.put("repIsuSrtCd", "");
			param.put("searchCodeType", "");
			param.put("marketType", "");
			param.put("searchType", "13");
			param.put("industry", "");
			param.put("fiscalYearEnd", "all");
			param.put("comAbbrvTmp", "");
			param.put("location", "all");

			HttpHeaders headers = new HttpHeaders();
			headers.set("Accept", "text/html, */*; q=0.01");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "215");
			headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.set("Cookie",
				"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
			headers.set("Host", "203.235.1.50");
			headers.set("Origin", "http://203.235.1.50");
			headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
			headers.set("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.set("X-Requested-With", "XMLHttpRequest");

			HttpEntity<String> entity = new HttpEntity<String>(headers);
//            HttpEntity<KindKrxCoKrVO> entity = new HttpEntity<KindKrxCoKrVO>(vo, headers);

//            ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI,HttpMethod.GET, entity, byte[].class);
//            ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, byte[].class);
//            ResponseEntity<KindKrxCoKrVO[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, entity, KindKrxCoKrVO[].class, param);
			ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, byte[].class,
				param);
//            ResponseEntity<byte[]> response = restTemplate.postForObject(SERVER_URI, param, byte[].class);

			if (response.getStatusCode() == HttpStatus.OK) {
				Files.write(Paths.get(fileName), response.getBody());
			}
			System.out.println("Body :" + response.getBody());
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void fetchFiles() {
		try {
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new ByteArrayHttpMessageConverter());
			RestTemplate restTemplate = new RestTemplate(messageConverters);

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<byte[]> response = restTemplate.exchange("https://www.google.com/images/srpr/logo11w.png",
				HttpMethod.GET, entity, byte[].class);

			if (response.getStatusCode() == HttpStatus.OK) {
				Files.write(Paths.get("google.png"), response.getBody());
			}
			;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excelDownloadUsingByteArrayHttpMessageConverter(String marketType) {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
			headers.set("Accept-Encoding", "gzip, deflate");
			headers.set("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			//headers.set("Content-Length", "5437");
//			headers.set("Content-Type", "application/x-www-form-urlencoded");
			//headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//			headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//			headers.setContentType(MediaType.TEXT_PLAIN);

			headers.set("Cookie",
				"stat_yn=1; BMR=s=1592365139732&r=https%3A%2F%2Fm.post.naver.com%2Fviewer%2FpostView.nhn%3FvolumeNo%3D18007858%26memberNo%3D43622425&r2=https%3A%2F%2Ffinance.daum.net%2Fquotes%2FA047810; _ga=GA1.2.1844994421.1592368169; pcview=pcview; happybean_pc_blog_2020_2=close; nid_inf=-1501804283; NID_AUT=OuxE9hJSyk7POzYwAVrE5R6+P3XsHL1YY3h/WbBvwr4L2eqtxZYB9/lacXNWa4Iu; bHideResizeNotice=1; NRTK=ag#40s_gr#1_ma#-2_si#0_en#0_sp#0; NID_SES=AAABh+y2FUjJI4qBy0AXNu/yNddAWf47Gdy0kv4UlFblpbw2CXzADDKN0TnkNrZQ0Y9ZJ1GOLr29UPTZjToN5EJqPKOYzGmjwPwXcxaPQTiT7Ki352Qf7PPJSUOAssvOGnOAhLzNzhzVHRrKklcK5ienVetEO/jYREu3OP1BpcL7CdtYnbrPBXqbnqf3dkqV1SxfBHDJf3qklF6kn7+u67Z2ypZWm2JRvsEEyBJwT9g2l5kIUjRu2PWIf2QOC9P0B3+iv2QMfE50JcOxaedUzpgUc/eeASazXdOpmKOiFkiQumwcA8V5woneyUNu2j1sVv+CIcUo8CIr0x1iQIumZ46xuOMnOFElX+3KIkQen2pUBNAmJaSgH5nMFcCn1j8x5b3OXKIBAjtCp8flMKZPaBUY0aL6sqB841hjhFiucGJLAO7Tcab9IjYQlCTNVfYuJI5gxs0WPyk6SnhCOxXJLBFAaRW+uQYMsqo1an02mMMNU3pJBcQB8Ggbh4AhKN2Ih/6frwDnyB0W+cbfDXqzAuAxlnI=; JSESSIONID=B60D604F87DC204E9A8C775AF52F0D0E.jvm1");
			headers.set("Host", "blog.naver.com");
			headers.set("Origin", "http://blog.naver.com");
			headers.set("Referer", "http://blog.naver.com/LinkShare.nhn?url=https%3A//www.youtube.com/watch%3Fv%3Dm2gv_a29Onw%26feature%3Dshare");
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
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
//			messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
//			messageConverters.add(new org.springframework.http.converter.ResourceHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]			
			//			messageConverters.add(new org.springframework.http.converter.xml.SourceHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
//			messageConverters.add(new org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
			messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not extract response: no suitable HttpMessageConverter found for response type [class java.lang.String] and content type [text/html;charset=UTF-8]
//			messageConverters.add(new org.springframework.http.converter.ResourceRegionHttpMessageConverter());
			//org.springframework.web.client.RestClientException: Could not write request: no suitable HttpMessageConverter found for request type [org.springframework.util.LinkedMultiValueMap] and content type [application/x-www-form-urlencoded]
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

			MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
//			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

			map.add("domain", "www.youtube.com");
			map.add("token", "");
			map.add("timestamp", "");
			map.add("url", "https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3Dm2gv_a29Onw%26feature%3Dshare");
			map.add("blogId", "banks");
			map.add("title", "타이틀테스트");
			map.add("content", "테스트입니다.");
//			String temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"%7B%22url%22%3A%22https%3A%2F%2Fwww.youtube.com%2Fwatch%3Fv%3Dm2gv_a29Onw%26feature%3Dshare%22%2C%22domain%22%3A%22www.youtube.com%22%2C%22title%22%3A%22*%ED%95%A8%EC%97%B0%EC%A7%80%EC%99%80%20%EB%82%A8%ED%8E%B8%EC%9D%98%20%EC%BB%A4%ED%94%8C%20%EB%93%80%EC%97%A3%F0%9F%91%A9%F0%9F%8E%A4%F0%9F%A7%91*%20%EA%B6%81%EA%B8%88%ED%95%B4%ED%95%98%EC%85%A8%EB%8D%98%20%ED%96%84%ED%8E%B8%EC%9D%98%20%EB%85%B8%EB%9E%98%20%EC%8B%A4%EB%A0%A5%EC%9D%84%20%EA%B3%B5%EA%B0%9C%ED%95%A9%EB%8B%88%EB%8B%A4%F0%9F%A4%A9%E3%85%A3%EB%AE%A4%EC%A7%80%EC%BB%AC%20%EC%B0%A8%EB%AF%B8%20-%20%EC%8A%A4%ED%81%AC%EB%9E%98%EC%B9%98%20(cover%20by%20%ED%96%84%EB%B6%80%EB%B6%80)%E3%85%A3%ED%96%84%EC%97%B0%EC%A7%80%20YONJIHAM%22%2C%22description%22%3A%22%23%ED%96%84%EC%97%B0%EC%A7%80%20%23%ED%96%84%ED%8E%B8%20%23%EB%93%80%EC%97%A3%20%EC%97%AC%EB%9F%AC%EB%B6%84%EA%BB%98%20%EB%93%9C%EB%A6%AC%EA%B3%A0%20%EC%8B%B6%EC%9D%80%20%EB%A7%90%EC%9D%B4%20%EC%9E%88%EC%96%B4%EC%9A%94!%20%EC%A0%9C%20%EC%B1%84%EB%84%90%EC%9D%84%20%EC%A7%80%EC%BC%9C%EB%B4%90%EC%A3%BC%EC%8B%9C%EA%B3%A0%20%EC%9D%91%EC%9B%90%ED%95%B4%EC%A3%BC%EC%85%94%EC%84%9C%20%EA%B0%90%EC%82%AC%ED%95%A9%EB%8B%88%EB%8B%A4.%20%EC%88%AB%EC%9E%90%EB%A5%BC%20%EC%83%9D%EA%B0%81%ED%95%98%EA%B3%A0%20%EC%8B%9C%EC%9E%91%ED%95%9C%20%EA%B2%83%EC%9D%80%20%EC%95%84%EB%8B%88%EC%97%88%EC%A7%80%EB%A7%8C%2010%EB%A7%8C%20%EB%AA%85%EC%9D%B4%EB%9D%BC%EB%8A%94%20%EB%A7%8E%EC%9D%80%20%EB%B6%84%EA%BB%98%EC%84%9C%20%EC%A0%9C%20%EC%B1%84%EB%84%90%EC%9D%84%20%EA%B5%AC%EB%8F%85%ED%95%B4%EC%A3%BC%EC%85%94%EC%84%9C%20%EA%B9%9C%EC%A7%9D%20%EB%86%80%EB%9E%90%EC%96%B4%EC%9A%94.%20(%EA%B0%80%EC%A1%B1%EB%93%A4%20%EB%AA%A8%EB%91%90%20%EA%B9%9C%EC%A7%9D%20%EB%86%80%EB%9E%90%EC%96%B4%EC%9A%94!)%20%EC%A0%9C%20%EC%B1%84%EB%84%90%EC%9D%84%20%EA%B5%AC%EB%8F%85%ED%95%B4%EC%A3%BC%EC%8B%9C%EA%B3%A0%20%EC%9D%91%EC%9B%90%ED%95%B4%EC%A3%BC%EC%8B%9C%EB%8A%94...%22%2C%22type%22%3A%22video%22%2C%22image%22%3A%7B%22url%22%3A%22https%3A%2F%2Fi.ytimg.com%2Fvi%2Fm2gv_a29Onw%2Fmaxresdefault.jpg%22%2C%22width%22%3A1280%2C%22height%22%3A720%7D%2C%22allImages%22%3A%5B%7B%22url%22%3A%22https%3A%2F%2Fi.ytimg.com%2Fvi%2Fm2gv_a29Onw%2Fmaxresdefault.jpg%22%2C%22width%22%3A1280%2C%22height%22%3A720%7D%5D%2C%22video%22%3A%22https%3A%2F%2Fwww.youtube.com%2Fembed%2Fm2gv_a29Onw%22%2C%22site%22%3A%22YouTube%22%2C%22layoutType%22%3A2%7D\"></span>";

			String temp = "<span id=\"se_object_1592490330981\" class=\"__se_object\" s_type=\"leverage\" s_subtype=\"oglink\" jsonvalue=\"testestestest\"></span>";
			String encoded = URLEncoder.encode(temp, "UTF8");
			map.add("content", encoded);
			map.add("content", "테스트야!!!");

			//map.add("content", "%3Cspan%20id%3D%22se_object_1592490330981%22%20class%3D%22__se_object%22%20s_type%3D%22leverage%22%20s_subtype%3D%22oglink%22%20jsonvalue%3D%22%257B%2522url%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fwatch%253Fv%253Dm2gv_a29Onw%2526feature%253Dshare%2522%252C%2522domain%2522%253A%2522www.youtube.com%2522%252C%2522title%2522%253A%2522*%25ED%2595%25A8%25EC%2597%25B0%25EC%25A7%2580%25EC%2599%2580%2520%25EB%2582%25A8%25ED%258E%25B8%25EC%259D%2598%2520%25EC%25BB%25A4%25ED%2594%258C%2520%25EB%2593%2580%25EC%2597%25A3%25F0%259F%2591%25A9%25F0%259F%258E%25A4%25F0%259F%25A7%2591*%2520%25EA%25B6%2581%25EA%25B8%2588%25ED%2595%25B4%25ED%2595%2598%25EC%2585%25A8%25EB%258D%2598%2520%25ED%2596%2584%25ED%258E%25B8%25EC%259D%2598%2520%25EB%2585%25B8%25EB%259E%2598%2520%25EC%258B%25A4%25EB%25A0%25A5%25EC%259D%2584%2520%25EA%25B3%25B5%25EA%25B0%259C%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4%25F0%259F%25A4%25A9%25E3%2585%25A3%25EB%25AE%25A4%25EC%25A7%2580%25EC%25BB%25AC%2520%25EC%25B0%25A8%25EB%25AF%25B8%2520-%2520%25EC%258A%25A4%25ED%2581%25AC%25EB%259E%2598%25EC%25B9%2598%2520(cover%2520by%2520%25ED%2596%2584%25EB%25B6%2580%25EB%25B6%2580)%25E3%2585%25A3%25ED%2596%2584%25EC%2597%25B0%25EC%25A7%2580%2520YONJIHAM%2522%252C%2522description%2522%253A%2522%2523%25ED%2596%2584%25EC%2597%25B0%25EC%25A7%2580%2520%2523%25ED%2596%2584%25ED%258E%25B8%2520%2523%25EB%2593%2580%25EC%2597%25A3%2520%25EC%2597%25AC%25EB%259F%25AC%25EB%25B6%2584%25EA%25BB%2598%2520%25EB%2593%259C%25EB%25A6%25AC%25EA%25B3%25A0%2520%25EC%258B%25B6%25EC%259D%2580%2520%25EB%25A7%2590%25EC%259D%25B4%2520%25EC%259E%2588%25EC%2596%25B4%25EC%259A%2594!%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EC%25A7%2580%25EC%25BC%259C%25EB%25B4%2590%25EC%25A3%25BC%25EC%258B%259C%25EA%25B3%25A0%2520%25EC%259D%2591%25EC%259B%2590%25ED%2595%25B4%25EC%25A3%25BC%25EC%2585%2594%25EC%2584%259C%2520%25EA%25B0%2590%25EC%2582%25AC%25ED%2595%25A9%25EB%258B%2588%25EB%258B%25A4.%2520%25EC%2588%25AB%25EC%259E%2590%25EB%25A5%25BC%2520%25EC%2583%259D%25EA%25B0%2581%25ED%2595%2598%25EA%25B3%25A0%2520%25EC%258B%259C%25EC%259E%2591%25ED%2595%259C%2520%25EA%25B2%2583%25EC%259D%2580%2520%25EC%2595%2584%25EB%258B%2588%25EC%2597%2588%25EC%25A7%2580%25EB%25A7%258C%252010%25EB%25A7%258C%2520%25EB%25AA%2585%25EC%259D%25B4%25EB%259D%25BC%25EB%258A%2594%2520%25EB%25A7%258E%25EC%259D%2580%2520%25EB%25B6%2584%25EA%25BB%2598%25EC%2584%259C%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EA%25B5%25AC%25EB%258F%2585%25ED%2595%25B4%25EC%25A3%25BC%25EC%2585%2594%25EC%2584%259C%2520%25EA%25B9%259C%25EC%25A7%259D%2520%25EB%2586%2580%25EB%259E%2590%25EC%2596%25B4%25EC%259A%2594.%2520(%25EA%25B0%2580%25EC%25A1%25B1%25EB%2593%25A4%2520%25EB%25AA%25A8%25EB%2591%2590%2520%25EA%25B9%259C%25EC%25A7%259D%2520%25EB%2586%2580%25EB%259E%2590%25EC%2596%25B4%25EC%259A%2594!)%2520%25EC%25A0%259C%2520%25EC%25B1%2584%25EB%2584%2590%25EC%259D%2584%2520%25EA%25B5%25AC%25EB%258F%2585%25ED%2595%25B4%25EC%25A3%25BC%25EC%258B%259C%25EA%25B3%25A0%2520%25EC%259D%2591%25EC%259B%2590%25ED%2595%25B4%25EC%25A3%25BC%25EC%258B%259C%25EB%258A%2594...%2522%252C%2522type%2522%253A%2522video%2522%252C%2522image%2522%253A%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252Fm2gv_a29Onw%252Fmaxresdefault.jpg%2522%252C%2522width%2522%253A1280%252C%2522height%2522%253A720%257D%252C%2522allImages%2522%253A%255B%257B%2522url%2522%253A%2522https%253A%252F%252Fi.ytimg.com%252Fvi%252Fm2gv_a29Onw%252Fmaxresdefault.jpg%2522%252C%2522width%2522%253A1280%252C%2522height%2522%253A720%257D%255D%252C%2522video%2522%253A%2522https%253A%252F%252Fwww.youtube.com%252Fembed%252Fm2gv_a29Onw%2522%252C%2522site%2522%253A%2522YouTube%2522%252C%2522layoutType%2522%253A2%257D%22%3E%3C%2Fspan%3E%3Cbr%3Ebbbbbbbbbbbbbb");
			map.add("postOpenType", "0");
			map.add("categoryNo", "33");

//			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI);
			UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme("http").host("blog.naver.com");

			UriComponents uriComponents = builder.path("/LinkSharePostWriteAsync.nhn").build();
			URI uri = uriComponents.toUri();

			System.out.println("uriComponents :" + uriComponents);
			HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
//			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);

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
			System.out.println("Response Headers[Date] :" + response.getHeaders().get("Date"));
			System.out.println("Response Headers[Content-Type] :" + response.getHeaders().get("Content-Type"));
			System.out.println("Response Headers[Transfer-Encoding] :" + response.getHeaders().get("Transfer-Encoding"));
			System.out.println("Response Headers[Connection] :" + response.getHeaders().get("Connection"));
			System.out.println("Response Headers[P3P] :" + response.getHeaders().get("P3P"));
			System.out.println("Response Headers[Set-Cookie] :" + response.getHeaders().get("Set-Cookie"));

			System.out.println("Response Headers[Cache-Control] :" + response.getHeaders().get("Cache-Control"));
			System.out.println("Response Headers[Expires] :" + response.getHeaders().get("Expires"));
			System.out.println("Response Headers[Access-Control-Allow-Credentials] :" + response.getHeaders().get("Access-Control-Allow-Credentials"));
			System.out.println("Response Headers[Expires] :" + response.getHeaders().get("Expires"));
			System.out.println("Response Headers[Access-Control-Allow-Headers] :" + response.getHeaders().get("Access-Control-Allow-Headers"));
			System.out.println("Response Headers[Access-Control-Allow-Methods] :" + response.getHeaders().get("Access-Control-Allow-Methods"));
			System.out.println("Response Headers[Vary] :" + response.getHeaders().get("Vary"));
			System.out.println("Response Headers[Content-Encoding] :" + response.getHeaders().get("Content-Encoding"));
			System.out.println("Response Headers[Server] :" + response.getHeaders().get("Server"));
			System.out.println("Response Headers[Referrer-policy] :" + response.getHeaders().get("Referrer-policy"));

//			System.out.println("Body1 :" + response.getBody());
//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			System.out.println("headers :" + response.getHeaders());
			System.out.println("body :" + response.getBody());
//			System.out.println("Body1 :" + new String(response.getBody()));
//			System.out.println("Body1 :" + new String(response.getBody().toString()));
//			System.out.println("Body1 :" + new String(response.getBody(), guessEncoding(response.getBody())));
//			System.out.println("Body2 :" + new String(response.getBody(), "EUC-KR"));
//			System.out.println("Body2 :" + new String(response.getBody(), "EUCKR"));
//			System.out.println("Body2 :" + new String(response.getBody(), "KSC5601"));
//			System.out.println("Body3 :" + new String(response.getBody().getBytes(), "UTF8"));
//			System.out.println("Body4 :" + new String(response.getBody(), "ISO-8859-1"));
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
			};
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@Test
	public void excelDownloadUsingStringHttpMessageConverter2(String marketType) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("EUC-KR"));
		List<HttpMessageConverter<?>> httpMessageConverter = Lists.newArrayList();
		httpMessageConverter.add(stringHttpMessageConverter);
		restTemplate.setMessageConverters(httpMessageConverter);

		URI targetUri = UriComponentsBuilder.fromHttpUrl(SERVER_URI).queryParam("method", "download")
			.queryParam("pageIndex", "1").queryParam("currentPageSize", "5000").queryParam("comAbbrv", "")
			.queryParam("beginIndex", "").queryParam("orderMode", "3").queryParam("orderStat", "D")
			.queryParam("isurCd", "").queryParam("repIsuSrtCd", "").queryParam("searchCodeType", "")
			.queryParam("marketType", marketType).queryParam("searchType", "13").queryParam("industry", "")
			.queryParam("fiscalYearEnd", "all").queryParam("comAbbrvTmp", "").queryParam("location", "all").build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		Charset charset = Charset.forName("EUC-KR");
		MediaType mediaType = new MediaType("text", "html", charset);
		headers.setContentType(mediaType);

		headers.set("Accept", MediaType.APPLICATION_JSON
			+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		headers.set("Accept-Encoding", "gzip, deflate");
//		headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
		headers.set("Accept-Language", "ko");
		headers.set("Cache-Control", "max-age=0");
		headers.set("Connection", "keep-alive");
		headers.set("Content-Length", "215");
//		headers.set("Content-Type", "application/x-www-form-urlencoded");
		headers.set("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
		headers.set("Cookie",
			"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
		// headers.set("Host", "kind.krx.co.kr");
		// headers.set("Origin", "http://kind.krx.co.kr");
		// headers.set("Referer",
		// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
		headers.set("Host", "203.235.1.50");
		headers.set("Origin", "http://203.235.1.50");
		headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
		headers.set("Upgrade-Insecure-Requests", "1");
//		headers.set("User-Agent", "mozilla");
		headers.set("User-Agent",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		headers.set("User-Agent",
			"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

		headers.set("X-Requested-With", "XMLHttpRequest");

		// gzip 사용하면 byte[] 로 받아서, 압축을 풀고 decoding 해야 한다.
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> response = restTemplate.exchange(targetUri.toURL().toString(), HttpMethod.GET, entity,
			String.class);
		String result = response.getBody();

		logger.info(result);
		System.setProperty("console.encoding", "EUC-KR");
//		System.setProperty("console.encoding", "UTF-8");
		System.out.println("result:[" + result + "]");
		System.out.println("response.getStatusCode():" + response.getStatusCode());
		if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody());
			Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
//			Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("UTF-8"));
		}
		;
		System.out.println("finished");
	}

	public void printStockInfoUsingStringHttpMessageConverter(String marketType) throws Exception {
		RestTemplate restTemplate = new RestTemplate();

		HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("EUC-KR"));
		List<HttpMessageConverter<?>> httpMessageConverter = Lists.newArrayList();
		httpMessageConverter.add(stringHttpMessageConverter);
		restTemplate.setMessageConverters(httpMessageConverter);

		URI targetUri = UriComponentsBuilder.fromHttpUrl(SERVER_URI).queryParam("method", "download")
			.queryParam("pageIndex", "1").queryParam("currentPageSize", "5000").queryParam("comAbbrv", "")
			.queryParam("beginIndex", "").queryParam("orderMode", "3").queryParam("orderStat", "D")
			.queryParam("isurCd", "").queryParam("repIsuSrtCd", "").queryParam("searchCodeType", "")
			.queryParam("marketType", marketType).queryParam("searchType", "13").queryParam("industry", "")
			.queryParam("fiscalYearEnd", "all").queryParam("comAbbrvTmp", "").queryParam("location", "all").build()
			.toUri();

		HttpHeaders headers = new HttpHeaders();
		Charset charset = Charset.forName("EUC-KR");
		MediaType mediaType = new MediaType("text", "html", charset);
		headers.setContentType(mediaType);

		headers.set("Accept", MediaType.APPLICATION_JSON
			+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		headers.set("Accept-Encoding", "gzip, deflate");
//		headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
		headers.set("Accept-Language", "ko");
		headers.set("Cache-Control", "max-age=0");
		headers.set("Connection", "keep-alive");
		headers.set("Content-Length", "215");
//		headers.set("Content-Type", "application/x-www-form-urlencoded");
		headers.set("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
		headers.set("Cookie",
			"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
		// headers.set("Host", "kind.krx.co.kr");
		// headers.set("Origin", "http://kind.krx.co.kr");
		// headers.set("Referer",
		// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
		headers.set("Host", "203.235.1.50");
		headers.set("Origin", "http://203.235.1.50");
		headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
		headers.set("Upgrade-Insecure-Requests", "1");
//		headers.set("User-Agent", "mozilla");
		headers.set("User-Agent",
			"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		headers.set("User-Agent",
			"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

		headers.set("X-Requested-With", "XMLHttpRequest");

		// gzip 사용하면 byte[] 로 받아서, 압축을 풀고 decoding 해야 한다.
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> response = restTemplate.exchange(targetUri.toURL().toString(), HttpMethod.GET, entity,
			String.class);
		String result = "";

		System.out.println("response.getStatusCode():" + response.getStatusCode());
		if (response.getStatusCode() == HttpStatus.OK) {
			result = response.getBody();
//			logger.info(result);
//			System.out.println("result:[" + result + "]");
			Document doc = Jsoup.parse(result);
			String html = doc.html();
			System.out.println("html:[" + html + "]");
		}
		;
		System.out.println("finished");
	}

}
//
