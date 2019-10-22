
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import html.parsing.stock.KindKrxCoKrExcelDownloadTest;

public class KindKrxCoKrExcelDownloadTest2 {

//    public static final String SERVER_URI = "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage";
	public static final String SERVER_URI = "http://kind.krx.co.kr/corpgeneral/corpList.do";
	private static final Logger logger = LoggerFactory.getLogger(KindKrxCoKrExcelDownloadTest.class);

	public static void main(String args[]) throws InterruptedException, IOException, Exception {
		new KindKrxCoKrExcelDownloadTest2();
	}

	KindKrxCoKrExcelDownloadTest2() throws IOException, Exception {
//        fetchFiles();
		// downloadTest1("searchCorpList", "download.html");
		// downloadTest1("download", "download.xls");
//		fetchFiles();
//		fetchFiles2();
//		excelDownloadUsingByteArrayHttpMessageConverter("");
//		excelDownloadUsingStringHttpMessageConverter("stockMkt");
//		excelDownloadUsingStringHttpMessageConverter("kosdaqMkt");
//		excelDownloadUsingStringHttpMessageConverter2("kosdaqMkt");
//		printStockInfoUsingStringHttpMessageConverter("kosdaqMkt");
//		printStockInfoUsingJsoup("stockMkt");
//		printStockInfoUsingJsoup("kosdaqMkt");
//		printStockInfoUsingJsoup1("");
//		printStockInfoUsingJsoup2("");
		printStockInfoUsingJsoup3("");
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
			headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
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

	@Test
	public void fetchFiles2() {
		try {
			HttpHeaders headers = new HttpHeaders();
//            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//Header key list
//public static final String ACCEPT = "Accept";
//public static final String ACCEPT_CHARSET = "Accept-Charset";
//public static final String ACCEPT_ENCODING = "Accept-Encoding";
//public static final String ACCEPT_LANGUAGE = "Accept-Language";
//public static final String ACCEPT_RANGES = "Accept-Ranges";
//public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
//public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
//public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
//public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
//public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
//public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
//public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
//public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
//public static final String AGE = "Age";
//public static final String ALLOW = "Allow";
//public static final String AUTHORIZATION = "Authorization";
//public static final String CACHE_CONTROL = "Cache-Control";
//public static final String CONNECTION = "Connection";
//public static final String CONTENT_ENCODING = "Content-Encoding";
//public static final String CONTENT_DISPOSITION = "Content-Disposition";
//public static final String CONTENT_LANGUAGE = "Content-Language";
//public static final String CONTENT_LENGTH = "Content-Length";
//public static final String CONTENT_LOCATION = "Content-Location";
//public static final String CONTENT_RANGE = "Content-Range";
//public static final String CONTENT_TYPE = "Content-Type";
//public static final String COOKIE = "Cookie";
//public static final String DATE = "Date";
//public static final String ETAG = "ETag";
//public static final String EXPECT = "Expect";
//public static final String EXPIRES = "Expires";
//public static final String FROM = "From";
//public static final String HOST = "Host";
//public static final String IF_MATCH = "If-Match";
//public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
//public static final String IF_NONE_MATCH = "If-None-Match";
//public static final String IF_RANGE = "If-Range";
//public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
//public static final String LAST_MODIFIED = "Last-Modified";
//public static final String LINK = "Link";
//public static final String LOCATION = "Location";
//public static final String MAX_FORWARDS = "Max-Forwards";
//public static final String ORIGIN = "Origin";
//public static final String PRAGMA = "Pragma";
//public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
//public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
//public static final String RANGE = "Range";
//public static final String REFERER = "Referer";
//public static final String RETRY_AFTER = "Retry-After";
//public static final String SERVER = "Server";
//public static final String SET_COOKIE = "Set-Cookie";
//public static final String SET_COOKIE2 = "Set-Cookie2";
//public static final String TE = "TE";
//public static final String TRAILER = "Trailer";
//public static final String TRANSFER_ENCODING = "Transfer-Encoding";
//public static final String UPGRADE = "Upgrade";
//public static final String USER_AGENT = "User-Agent";
//public static final String VARY = "Vary";
//public static final String VIA = "Via";
//public static final String WARNING = "Warning";
//public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

			headers.set("Accept", "text/html, */*; q=0.01");
			headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
			headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "215");
			headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.set("Cookie",
					"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
			// headers.set("Host", "kind.krx.co.kr");
			// headers.set("Origin", "http://kind.krx.co.kr");
			// headers.set("Referer",
			// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
			headers.set("Host", "203.235.1.50");
			headers.set("Origin", "http://203.235.1.50");
			headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

			headers.set("X-Requested-With", "XMLHttpRequest");

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});
//          SERVER_URI = "https://www.google.com/images/srpr/logo11w.png";
//          SERVER_URI = "http://kind.krx.co.kr/corpgeneral/corpList.do";
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI)
					.queryParam("method", "searchCorpList").queryParam("pageIndex", "1")
					.queryParam("currentPageSize", "15").queryParam("comAbbrv", "").queryParam("beginIndex", "")
					.queryParam("orderMode", "3").queryParam("orderStat", "D").queryParam("isurCd", "")
					.queryParam("repIsuSrtCd", "").queryParam("searchCodeType", "").queryParam("marketType", "")
					.queryParam("searchType", "13").queryParam("industry", "").queryParam("fiscalYearEnd", "all")
					.queryParam("comAbbrvTmp", "").queryParam("location", "all");

			HttpEntity<?> entity = new HttpEntity<>(headers);
//            HttpEntity<String> entity = new HttpEntity<String>(headers);

			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new ByteArrayHttpMessageConverter());

			RestTemplate restTemplate = new RestTemplate(messageConverters);

//            HttpEntity<String> response = restTemplate.exchange(
//                    builder.toUriString(),
//                    HttpMethod.GET,
//                    entity,
//                    String.class);
			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);

			System.out.println("StatusCode :" + response.getStatusCode());
			System.out.println("Body :" + response.getBody());
			System.out.println("Body :" + response.getBody());
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			if (response.getStatusCode() == HttpStatus.OK) {
				Files.write(Paths.get("stockListDownload.html"), response.getBody());
			}
			;
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excelDownloadUsingByteArrayHttpMessageConverter(String marketType) {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept", MediaType.APPLICATION_JSON
					+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
			headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "215");
			headers.set("Content-Type", "application/x-www-form-urlencoded");
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
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

			headers.set("X-Requested-With", "XMLHttpRequest");

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

//          SERVER_URI = "https://www.google.com/images/srpr/logo11w.png";
//          SERVER_URI = "http://kind.krx.co.kr/corpgeneral/corpList.do";
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=stockMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=kosdaqMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI).queryParam("method", "download")
					.queryParam("pageIndex", "1").queryParam("currentPageSize", "5000").queryParam("comAbbrv", "")
					.queryParam("beginIndex", "").queryParam("orderMode", "3").queryParam("orderStat", "D")
					.queryParam("isurCd", "").queryParam("repIsuSrtCd", "").queryParam("searchCodeType", "")
					.queryParam("marketType", marketType).queryParam("searchType", "13").queryParam("industry", "")
					.queryParam("fiscalYearEnd", "all").queryParam("comAbbrvTmp", "").queryParam("location", "all");

//            HttpEntity<String> entity = new HttpEntity<String>(headers);
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new ByteArrayHttpMessageConverter());

			RestTemplate restTemplate = new RestTemplate(messageConverters);
//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
//			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpEntity<String> entity = new HttpEntity<String>(headers);

			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					byte[].class);
//			ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
//		        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);

			System.out.println(
					"Response Headers[Content-Description] :" + response.getHeaders().get("Content-Description"));
			System.out.println(
					"Response Headers[Content-Disposition] :" + response.getHeaders().get("Content-Disposition"));
			System.out.println("Response Headers[Content-Type] :" + response.getHeaders().get("Content-Type"));
			System.out.println("Response Headers[Date] :" + response.getHeaders().get("Date"));
			System.out
					.println("Response Headers[Transfer-Encoding] :" + response.getHeaders().get("Transfer-Encoding"));

//			System.out.println("Body1 :" + response.getBody());
//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			System.out.println("Body1 :" + new String(response.getBody()));
//			System.out.println("Body1 :" + new String(response.getBody(), guessEncoding(response.getBody())));
//			System.out.println("Body2 :" + new String(response.getBody(), "KSC-5601"));
//			System.out.println("Body3 :" + new String(response.getBody(), "UTF-8"));
//			System.out.println("Body4 :" + new String(response.getBody(), "ISO-8859-1"));
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			if (response.getStatusCode() == HttpStatus.OK) {
				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody());
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody().toString().getBytes("EUC-KR"));
			}
			;
			System.out.println("finished");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void excelDownloadUsingStringHttpMessageConverter(String marketType) {
		try {
			HttpHeaders headers = new HttpHeaders();

			headers.set("Accept", MediaType.APPLICATION_JSON
					+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
			headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
			headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
			headers.set("Cache-Control", "max-age=0");
			headers.set("Connection", "keep-alive");
			headers.set("Content-Length", "215");
			headers.set("Content-Type", "application/x-www-form-urlencoded");
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
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
			headers.set("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

			headers.set("X-Requested-With", "XMLHttpRequest");

			headers.forEach((key, value) -> {
				System.out.println(String.format("Header '%s' = %s", key, value));
			});

//          SERVER_URI = "https://www.google.com/images/srpr/logo11w.png";
//          SERVER_URI = "http://kind.krx.co.kr/corpgeneral/corpList.do";
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=stockMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
//method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=kosdaqMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI).queryParam("method", "download")
					.queryParam("pageIndex", "1").queryParam("currentPageSize", "5000").queryParam("comAbbrv", "")
					.queryParam("beginIndex", "").queryParam("orderMode", "3").queryParam("orderStat", "D")
					.queryParam("isurCd", "").queryParam("repIsuSrtCd", "").queryParam("searchCodeType", "")
					.queryParam("marketType", marketType).queryParam("searchType", "13").queryParam("industry", "")
					.queryParam("fiscalYearEnd", "all").queryParam("comAbbrvTmp", "").queryParam("location", "all");

//            HttpEntity<String> entity = new HttpEntity<String>(headers);
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
//			messageConverters.add(new ByteArrayHttpMessageConverter());
			messageConverters.add(0, new StringHttpMessageConverter(Charset.forName("EUC-KR")));

			RestTemplate restTemplate = new RestTemplate(messageConverters);
//			RestTemplate template = new RestTemplate();
//			restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

//			HttpEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
//			ResponseEntity<String> entity = restTemplate.getForEntity("https://example.com", String.class);
//			HttpEntity<?> entity = new HttpEntity<>(headers);
			HttpEntity<String> entity = new HttpEntity<String>(headers);

//			ResponseEntity<byte[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, byte[].class);
//			ResponseEntity<Object> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, Object.class);
//			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
			ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, entity,
					String.class);

			System.out.println(
					"Response Headers[Content-Description] :" + response.getHeaders().get("Content-Description"));
			System.out.println(
					"Response Headers[Content-Disposition] :" + response.getHeaders().get("Content-Disposition"));
			System.out.println("Response Headers[Content-Type] :" + response.getHeaders().get("Content-Type"));
			System.out.println("Response Headers[Date] :" + response.getHeaders().get("Date"));
			System.out
					.println("Response Headers[Transfer-Encoding] :" + response.getHeaders().get("Transfer-Encoding"));

//			System.out.println("Body1 :" + response.getBody());
//			System.out.println("guessEncoding :" + guessEncoding(response.getBody()));
			System.out.println("Body1 :" + new String(response.getBody()));
//			System.out.println("Body1 :" + new String(response.getBody(), guessEncoding(response.getBody())));
//			System.out.println("Body2 :" + new String(response.getBody(), "KSC-5601"));
//			System.out.println("Body3 :" + new String(response.getBody(), "UTF-8"));
//			System.out.println("Body4 :" + new String(response.getBody(), "ISO-8859-1"));
			System.out.println("response.getStatusCode():" + response.getStatusCode());
			if (response.getStatusCode() == HttpStatus.OK) {
//				Files.write(Paths.get(marketType + "_excelDownload.xls"), response.getBody());
				Files.write(Paths.get(marketType + "_excelDownload.xls"),
						response.getBody().toString().getBytes("EUC-KR"));
			}
			;
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
		headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
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
		headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
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

	public void printStockInfoUsingJsoup1(String marketType) throws Exception {

		String param1 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param2 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=stockMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param3 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=kosdaqMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

		String strUri = SERVER_URI + "?" + param1;
		Document doc = Jsoup.parse(new URL(strUri).openStream(), "EUC-KR", strUri);

		String html = doc.html();
		System.out.println("html:[" + html + "]");

//		Files.write(Paths.get(marketType + "_excelDownload.xls"), html.getBytes("EUC-KR"));
		Files.write(Paths.get(marketType + "_1_excelDownload.xls"), html.getBytes());

		System.out.println("finished");
	}

	public void printStockInfoUsingJsoup2(String marketType) throws Exception {

		String param1 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param2 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=stockMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param3 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=kosdaqMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

		String strUri = SERVER_URI + "?" + param1;
		Document doc = Jsoup.connect(strUri).ignoreContentType(true).get();

		String html = doc.html();
		System.out.println("html:[" + html + "]");

//		Files.write(Paths.get(marketType + "_excelDownload.xls"), html.getBytes("EUC-KR"));
		Files.write(Paths.get(marketType + "_2_excelDownload.xls"), html.getBytes());

		System.out.println("finished");
	}

	public void printStockInfoUsingJsoup3(String marketType) throws Exception {

		String param1 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param2 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=stockMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";
		String param3 = "method=download&pageIndex=1&currentPageSize=5000&comAbbrv=&beginIndex=&orderMode=3&orderStat=D&isurCd=&repIsuSrtCd=&searchCodeType=&marketType=kosdaqMkt&searchType=13&industry=&fiscalYearEnd=all&comAbbrvTmp=&location=all";

		String strUri = SERVER_URI + "?" + param1;
//		Document doc = Jsoup.parse(new URL(strUri).openStream(), "EUC-KR", strUri);

//		Connection conn = Jsoup.connect(strUri).cookie("cookiereference", "cookievalue").method(Method.POST);
		Map headers = new HashMap();
		headers.put("Accept", MediaType.APPLICATION_JSON
				+ ",text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
		headers.put("Accept-Encoding", "Accept-Encoding: gzip, deflate");
//		headers.put("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
		headers.put("Accept-Language", "ko");
		headers.put("Cache-Control", "max-age=0");
		headers.put("Connection", "keep-alive");
		headers.put("Content-Length", "215");
//		headers.put("Content-Type", "application/x-www-form-urlencoded");
//		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=EUC-KR");
//		headers.put("Content-Type", "text/*; charset=EUC-KR");
//		headers.put("Content-Type", "application/xml; charset=EUC-KR");
//		headers.put("Content-Type", "application/xhtml+xml; charset=EUC-KR");
		headers.put("Content-Type", "application/vnd.ms-excel; charset=EUC-KR");
		headers.put("Cookie",
				"__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
		// headers.put("Host", "kind.krx.co.kr");
		// headers.put("Origin", "http://kind.krx.co.kr");
		// headers.put("Referer",
		// "http://kind.krx.co.kr/corpgeneral/corpList.do?method=loadInitPage");
		headers.put("Host", "203.235.1.50");
		headers.put("Origin", "http://203.235.1.50");
		headers.put("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
		headers.put("Upgrade-Insecure-Requests", "1");
//		headers.put("User-Agent", "mozilla");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
		headers.put("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36 NetHelper70");

		headers.put("X-Requested-With", "XMLHttpRequest");

//		Connection conn = Jsoup.connect(strUri).headers(headers).cookie("cookiereference", "cookievalue").method(Method.POST);
//		Document doc = Jsoup.parse(new String(conn.execute().bodyAsBytes(), "EUC-KR"));
// Initialize UnSupportedMimeTypeExeception class 
		UnsupportedMimeTypeException mimeType = new UnsupportedMimeTypeException("Hey this is Mime",
				"application/vnd.ms-excel", strUri);
		String mime = mimeType.getMimeType();
		System.out.println("mime :" + mime);
//		Jsoup.connect(url).requestBody(json).header("Content-Type", "application/json").post();
		Document doc = Jsoup.connect(strUri).requestBody("JSON").header("Content-Type", mime)
				// .cookies(response.cookies())
				 .ignoreContentType(true)
				.post();
//		System.out.println("doc:"+doc);
		convertHtml2Excel(doc);
		String html = doc.html();
		System.out.println("html:[" + html + "]");

//		Files.write(Paths.get(marketType + "_excelDownload.xls"), html.getBytes("EUC-KR"));
		Files.write(Paths.get(marketType + "_3_excelDownload.xls"), html.getBytes("EUC-KR"));

		System.out.println("finished");
	}

	public void convertHtml2Excel(Document doc) {

		try {
			// Create Work book
			XSSFWorkbook xwork = new XSSFWorkbook();

			// Create Spread Sheet
			XSSFSheet xsheet = xwork.createSheet("MyFristSheet");

			// Create Row (Row is inside spread sheet)
			XSSFRow xrow = null;

			int rowid = 0;

			Elements trElements = doc.select("tr");
			Cell cell;
			for (int i = 0; i < trElements.size(); i++) {
				xrow = xsheet.createRow(rowid);
				Elements thElements = trElements.get(rowid).select("th");
				for(int j=0;j<thElements.size();j++) {
					cell = xrow.createCell(j);
					cell.setCellValue(thElements.get(j).text());
				}
				Elements tdElements = trElements.get(rowid).select("td");
				for(int j=0;j<tdElements.size();j++) {
					cell = xrow.createCell(j);
					cell.setCellValue(tdElements.get(j).text());
				}
				rowid++;
			}
			
			XSSFRow row = xsheet.getRow(xsheet.getLastRowNum());
			for(int colNum = 0; colNum<row.getLastCellNum();colNum++)   
				xwork.getSheetAt(0).autoSizeColumn(colNum);
			
			// create date for adding this to our workbook name like workbookname_date
			Date d1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String todaysDate = sdf.format(d1);
			System.out.println(sdf.format(d1));
			// Create file system using specific name
			FileOutputStream fout = new FileOutputStream(
					new File("C:\\Temp\\redaingfromHTMLFile_" + todaysDate + ".xlsx"));

			xwork.write(fout);
			fout.close();
			System.out.println("redaingfromHTMLFile_" + todaysDate + ".xlsx written successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
//
