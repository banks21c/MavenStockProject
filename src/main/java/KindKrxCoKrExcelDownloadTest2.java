import html.parsing.stock.KindKrxCoKrExcelDownloadTest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class KindKrxCoKrExcelDownloadTest2 {

    public static final String SERVER_URI = "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage";
    private static final Logger logger = LoggerFactory.getLogger(KindKrxCoKrExcelDownloadTest.class);

    public static void main(String args[]) throws InterruptedException, IOException {
        new KindKrxCoKrExcelDownloadTest2();
    }

    KindKrxCoKrExcelDownloadTest2() throws IOException {
//        fetchFiles();
        downloadTest1();
        fetchFiles2();
    }

    private static void downloadTest1() {
        KindKrxCoKrVO vo = new KindKrxCoKrVO();
        vo.setMethod("searchCorpList");
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
            headers.set("Cookie", "__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
            headers.set("Host", "203.235.1.50");
            headers.set("Origin", "http://203.235.1.50");
            headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            headers.set("X-Requested-With", "XMLHttpRequest");
            
            HttpEntity<String> entity = new HttpEntity<String>(headers);
//            HttpEntity<KindKrxCoKrVO> entity = new HttpEntity<KindKrxCoKrVO>(vo, headers);

//            ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI,HttpMethod.GET, entity, byte[].class);
//            ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, byte[].class);
//            ResponseEntity<KindKrxCoKrVO[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, entity, KindKrxCoKrVO[].class, param);
            ResponseEntity<byte[]> response = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, byte[].class, param);
//            ResponseEntity<byte[]> response = restTemplate.postForObject(SERVER_URI, param, byte[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Files.write(Paths.get("downloadtest1.html"), response.getBody());
            }
            System.out.println("HttpStatus.OK:" + HttpStatus.OK);
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

            ResponseEntity<byte[]> response = restTemplate.exchange("https://www.google.com/images/srpr/logo11w.png", HttpMethod.GET, entity, byte[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Files.write(Paths.get("google.png"), response.getBody());
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fetchFiles2() {
        try {
            HttpHeaders headers = new HttpHeaders();
//            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Accept", "text/html, */*; q=0.01");
            headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
            headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
            headers.set("Connection", "keep-alive");
            headers.set("Content-Length", "215");
            headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.set("Cookie", "__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
            headers.set("Host", "203.235.1.50");
            headers.set("Origin", "http://203.235.1.50");
            headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
            headers.set("X-Requested-With", "XMLHttpRequest");

//          SERVER_URI = "https://www.google.com/images/srpr/logo11w.png";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(SERVER_URI)
                    .queryParam("method", "searchCorpList")
                    .queryParam("pageIndex", "1")
                    .queryParam("currentPageSize", "15")
                    .queryParam("comAbbrv", "")
                    .queryParam("beginIndex", "")
                    .queryParam("orderMode", "3")
                    .queryParam("orderStat", "D")
                    .queryParam("isurCd", "")
                    .queryParam("repIsuSrtCd", "")
                    .queryParam("searchCodeType", "")
                    .queryParam("marketType", "")
                    .queryParam("searchType", "13")
                    .queryParam("industry", "")
                    .queryParam("fiscalYearEnd", "all")
                    .queryParam("comAbbrvTmp", "")
                    .queryParam("location", "all");

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
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    entity,
                    byte[].class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Files.write(Paths.get("stock2.html"), response.getBody());
            };

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
