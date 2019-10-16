/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.SysexMessage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author banks
 */
public class RestTempleteTest01 {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RestTempleteTest01() {
        String url = "https://blog.naver.com/PostScrap.nhn";

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.set("Accept", "text/html, */*; q=0.01");
//        headers.set("Accept-Encoding", "Accept-Encoding: gzip, deflate");
//        headers.set("Accept-Language", "en-US,en;q=0.9,ko;q=0.8");
        headers.set("Connection", "keep-alive");
        headers.set("Host", "203.235.1.50");
//        headers.set("content-length", "2543");
//        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        headers.set("Content-Type", "text/plain");        
//        headers.set("Cookie", "__smVisitorID=QxeY65c5t3z; JSESSIONID=NyCFzfuTJuLCu1YTU5tAy2RDQUIha813iVKfZ9cnDZKOG81CUOKWwLcMsKQsK6JP.amV1c19kb21haW4vMTBfRFNUMg==; viewMode=1; krxMenu=ULDDST00000%2C%uC624%uB298%uC758%uACF5%uC2DC/ULDDST00100%2C%uD68C%uC0AC%uBCC4%uAC80%uC0C9/ULDDST00300%2C%uC0C1%uC138%uAC80%uC0C9/ULDDST00200%2C%uD1B5%uD569%uAC80%uC0C9/ULDDST71000%2C%uC608%uBE44%uC2EC%uC0AC%uAE30%uC5C5/");
//        headers.set("Origin", "http://203.235.1.50");
//        headers.set("Referer", "http://203.235.1.50/corpgeneral/corpList.do?method=loadInitPage");
//        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        headers.set("X-Requested-With", "XMLHttpRequest");

        headers.set("Connection", "close");
        headers.set("Cache-Control", "no-cache");
        headers.set("Access-Control-Allow-Credentials", "true");
        headers.set("Access-Control-Allow-Headers", "accept, content-type");
        headers.set("Access-Control-Allow-Methods", "GET, POST");
        headers.set("Server", "nxfps");
        headers.set("Referrer-policy", "unsafe-url");
        headers.set("Expires", "Thu, 01 Jan 1970 00:00:00 GMT");

        headers.set("%3Aauthority", "blog.naver.com");
        headers.set("%3Amethod", "POST");
        headers.set("%3Apath", "/PostScrap.nhn");
        headers.set("%3Ascheme", "https");
        headers.set("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        headers.set("accept-encoding", "gzip, deflate, br");
        headers.set("accept-language", "en-US,en;q=0.9,ko;q=0.8");
        headers.set("cache-control", "max-age=0");
        headers.set("content-length", "2543");
        headers.set("content-type", "application/x-www-form-urlencoded");
        headers.set("cookie", "NNB=JNN2EUOF47YVY; nx_ssl=2; stat_yn=1; page_uid=Un6ujsppmr8ssewjLnCsssssssh-366915; BMR=s=1559398701278&r=https%3A%2F%2Fm.blog.naver.com%2FPostView.nhn%3FblogId%3Dtaylorjun%26logNo%3D220679566304%26proxyReferer%3Dhttps%253A%252F%252Fwww.google.com%252F&r2=https%3A%2F%2Fwww.google.com%2F; nid_inf=1836870817; NID_AUT=NSK+xsBdTNAQKhj+8t1FUIBlOs1vX9UlCNml5Rd1UDyhH1G+4DVKs9T2OMcdm0z/; NID_JKL=Qnl+5Z33y8f8HPM6SYSg9FiXYfmLNJRIyOdECAMOqnw=; JSESSIONID=742E67A48A8F6C944A82ED685D52C7F4.jvm1; NID_SES=AAABfzBtHYI+sVc0yiVQgqOcyOUSrdVBUU8nlR0nxDvYKRusX9gNG32MuhLPz8JiiV9V/G27ax6Ig5/uqdLph0zWgXLGF74n4wPe11kpRaa5oCm8g6pqKND24bq/EvIrN6Ip/ZgMQf69kvIKSrg6VIiNEWWdJzniLndhrYXVLLPkUX/mqCAJvOrmnMnskxu189/KP3dnO9W1bExtrMTC8v9Vdi9Wq49EIdgDtetALvCrIJUhbXsgxLFR0TVpg7m7LQrzSsMOenoxWDisrYtJAB6ASNy59C9kst6WilPOaUZDggW/R8AMkFCIK/ZnHXJzjk3fsM0odpS+rfwucw0T5KQM67BKnwAYQMzoW5mbx1QeC7gME/kgj3ZQePJjY6Es5t5QpuikFz+Otb7wtFT1YdCWlbkNH0xG2pg0+wxwS3V/nZ9GI8kF5Dum+2DREqpGdXqOAKfYRo4O+KejJzUSWaXABwu0ILnpvkjpYn4iNEDune106OhC+VYfZnePXPqVG7pV/A==");
        headers.set("origin", "https://blog.naver.com");
        headers.set("referer", "https://blog.naver.com/ScrapFormUtf8.nhn");
        headers.set("upgrade-insecure-requests", "1");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//        Map<String, String> param = new HashMap<String, String>();
//        map.add("prevLogNo", "221552097233");
//        map.add("scrapCommentView", "TEST");
//        map.add("inadd_tag", "#TEST");
//        map.add("logType", "1");
//        map.add("category", "31#nhn#정치#nhn#true");
//        map.add("isSaveSetting", "1");
//        map.add("openyn", "2");
//        map.add("commentYn", "2");
////        map.add("blogId", "naver");
//        map.add("blogId", "banks");
//        map.add("source_type", "111");
//        map.add("source_type_real", "");
//        map.add("source_no", "");
//        map.add("source_sumyn", "");
//        map.add("source_sumtext", "");
//        map.add("source_paperno", "");
//        map.add("source_paperid", "");
//        map.add("source_nickname", "");
//        map.add("source_url", "http://naver.me/GzXzx4Im");
//        map.add("source_form", "2");
//        map.add("cclType", "");
//        map.add("sourceSmartEditorVersion", "");
//        map.add("greenReviewBannerYn", "NO_USE");
//        map.add("source_title", "머니투데이 | 네이버 금융");
//        map.add("title", "[공유​​] [뉴욕마감] ˙멕시코 관세쇼크˙에 와르르…다우 1.4%↓");
//        map.add("contents", "CONTENTS입니다.");
//        map.add("content", "CONTENT입니다.");
//        map.add("scrapComment", "scrapComment입니다.");
//        map.add("attach", "");
//        map.add("logNo", "221552097233");
//        map.add("source_categoryNo", "");
//        map.add("flv_include_yn", "N");
//        map.add("themeCode", "");
//        map.add("imageUrl", "");
//        map.add("eventCode", "");
//        map.add("callbackUrl", "https://api.share.naver.com/spi/v1/api/shareLog?evkey=finance&amp;snsId=blog&amp;platformCode=pc&amp;likeServiceId=&amp;likeContentsId=&amp;shareTime=1559383611478&amp;srcUrl=http%3A%2F%2Ffinance.naver.com%2Fnews%2Fnews_read.nhn%3Farticle_id%3D0004227075%26office_id%3D008%26mode%3Dmainnews%26type%3D%26section_id%3D%26section_id2%3D%26section_id3%3D%26date%3D20190601%26page%3D&amp;shareCode=finish&amp;useShortUrl=true");
//        map.add("callbackEncoding", "false");
//        map.add("callbackType", "image");
//        map.add("isMemolog", "false");
//        map.add("fromMylog", "false");
//        map.add("no", "0");
//        map.add("scrap_hintyn", "Y");
//        map.add("sourcePreviewJson", "{&quot;url&quot;:&quot;http://naver.me/GzXzx4Im&quot;,&quot;domain&quot;:&quot;naver.me&quot;,&quot;title&quot;:&quot;네이버 금융&quot;,&quot;description&quot;:&quot;관심종목의 실시간 주가를 가장 빠르게 확인하는 곳&quot;,&quot;image&quot;:{&quot;url&quot;:&quot;https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg&quot;,&quot;width&quot;:560,&quot;height&quot;:373},&quot;allImages&quot;:[{&quot;url&quot;:&quot;https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg&quot;,&quot;width&quot;:560,&quot;height&quot;:373},{&quot;url&quot;:&quot;https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg?type=w540&quot;,&quot;width&quot;:540,&quot;height&quot;:360}],&quot;layoutType&quot;:1}");

        map.add("scrapCommentView", "덧붙임 글을 입력해주세요 (500글자 이내)");
        map.add("tag", "");
        map.add("logType", "1");
        map.add("category", "31#nhn#정치#nhn#true");
        map.add("openyn", "0");
        map.add("commentYn", "false");
        map.add("blogId", "naver");
        map.add("source_type", "111");
        map.add("source_type_real", "");
        map.add("source_no", "");
        map.add("source_sumyn", "");
        map.add("source_sumtext", "");
        map.add("source_paperno", "");
        map.add("source_paperid", "");
        map.add("source_nickname", "");
        map.add("source_url", "http://naver.me/GzXzx4Im");
        map.add("source_form", "2");
        map.add("cclType", "");
        map.add("sourceSmartEditorVersion", "");
        map.add("greenReviewBannerYn", "NO_USE");
        map.add("source_title", "머니투데이 | 네이버 금융");
        map.add("title", "[공유​​] [뉴욕마감] ˙멕시코 관세쇼크˙에 와르르…다우 1.4%↓");
        map.add("contents", "");
        map.add("scrapComment", "");
        map.add("attach", "");
        map.add("logNo", "");
        map.add("source_categoryNo", "");
        map.add("flv_include_yn", "N");
        map.add("themeCode", "");
        map.add("imageUrl", "");
        map.add("eventCode", "");
        try {
            URLDecoder decoder = new URLDecoder();
            //https://api.share.naver.com/spi/v1/api/shareLog?
//            evkey=finance
//            &snsId=blog
//            &platformCode=pc
//            &likeServiceId=
//            &likeContentsId=
//            &shareTime=1559435071088
//            &srcUrl=http://finance.naver.com/news/news_read.nhn?article_id=0004227075&office_id=008&mode=mainnews&type=&section_id=&section_id2=&section_id3=&date=20190601&page=&shareCode=finish&useShortUrl=true
            String s = decoder.decode("https://api.share.naver.com/spi/v1/api/shareLog?evkey=finance&snsId=blog&platformCode=pc&likeServiceId=&likeContentsId=&shareTime=1559435071088&srcUrl=http%3A%2F%2Ffinance.naver.com%2Fnews%2Fnews_read.nhn%3Farticle_id%3D0004227075%26office_id%3D008%26mode%3Dmainnews%26type%3D%26section_id%3D%26section_id2%3D%26section_id3%3D%26date%3D20190601%26page%3D&shareCode=finish&useShortUrl=true", "UTF-8");
            System.out.println("s :" + s);
            map.add("callbackUrl", s);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RestTempleteTest01.class.getName()).log(Level.SEVERE, null, ex);
        }
        map.add("callbackEncoding", "false");
        map.add("callbackType", "image");
        map.add("isMemolog", "false");
        map.add("no", "0");
        map.add("scrap_hintyn", "Y");
        map.add("sourcePreviewJson", "{\"url\":\"http://naver.me/GzXzx4Im\",\"domain\":\"naver.me\",\"title\":\"네이버 금융\",\"description\":\"관심종목의 실시간 주가를 가장 빠르게 확인하는 곳\",\"image\":{\"url\":\"https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg\",\"width\":560,\"height\":373},\"allImages\":[{\"url\":\"https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg\",\"width\":560,\"height\":373},{\"url\":\"https://imgnews.pstatic.net/image/008/2019/06/01/0004227075_001_20190601064802046.jpg?type=w540\",\"width\":540,\"height\":360}],\"layoutType\":1}");

        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
//        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
//        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);
        System.out.println("responseEntity:" + responseEntity);
        System.out.println("responseEntity.getStatusCode():" + responseEntity.getStatusCode());
        System.out.println("responseEntity.getStatusCodeValue():" + responseEntity.getStatusCodeValue());
        System.out.println("responseEntity.getBody():" + Arrays.toString(responseEntity.getBody()));
        System.out.println("responseEntity.getHeaders():" + responseEntity.getHeaders());

        HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<byte[]> responseEntityStr = restTemplate.postForEntity(url, request, byte[].class);
        try {
            System.out.println("responseEntityStr.getBody():" + responseEntityStr.getBody());
            JsonNode root = objectMapper.readTree(Arrays.toString(responseEntityStr.getBody()));
            System.out.println("root:" + root);
        } catch (IOException ex) {
            Logger.getLogger(RestTempleteTest01.class.getName()).log(Level.SEVERE, null, ex);
        }

//        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class, map);
//        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(url, entity, byte[].class, map);
//        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
//        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class, map);
//            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(SERVER_URI,HttpMethod.GET, entity, byte[].class);
//            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(SERVER_URI, HttpMethod.POST, entity, byte[].class);
//            ResponseEntity<KindKrxCoKrVO[]> responseEntity = restTemplate.exchange(SERVER_URI, HttpMethod.PUT, entity, KindKrxCoKrVO[].class, param);
//            ResponseEntity<byte[]> responseEntity = restTemplate.postForObject(SERVER_URI, param, byte[].class);
        System.out.println("HttpStatus.ACCEPTED :" + HttpStatus.ACCEPTED);

//        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        //DEBUG [main] (HttpAccessor.java:87)- Created POST request for "https://blog.naver.com/PostScrap.nhn"
        //DEBUG [main] (RestTemplate.java:779)- Setting request Accept header to [text/plain, application/json, application/*+json, */*]
        //DEBUG [main] (RestTemplate.java:874)- Writing [{email=[first.last@example.com]}] as "application/x-www-form-urlencoded"using [org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter@17c386de]
        //DEBUG [main] (RestTemplate.java:691)- POST request for "https://blog.naver.com/PostScrap.nhn"resulted in 204 (No Content)
        //response:<204 No Content,{Date=[Sat, 01 Jun 2019 11:04:14 GMT], Content-Type=[text/plain], Content-Length=[0], Connection=[close], Cache-Control=[no-cache], Expires=[Thu, 01 Jan 1970 00:00:00 GMT], P3P=[CP="ALL CURa ADMa DEVa TAIa OUR BUS IND PHY ONL UNI PUR FIN COM NAV INT DEM CNT STA POL HEA PRE LOC OTC"], Access-Control-Allow-Credentials=[true], Access-Control-Allow-Headers=[accept, content-type], Access-Control-Allow-Methods=[GET, POST], Server=[nxfps], Referrer-policy=[unsafe-url]}>
    }

    private String doPOST(File file, String[] array, String name) {
        RestTemplate restTemplate = new RestTemplate();

        //add file
        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", new FileSystemResource(file));

        //add array
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://my_url");
        for (String item : array) {
            builder.queryParam("array", item);
        }

        //add some String
        builder.queryParam("name", name);

        //another staff
        String result = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.POST,
                requestEntity,
                String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        if (statusCode == HttpStatus.ACCEPTED) {
            result = responseEntity.getBody();
        }
        return result;
    }

    public static void main(String args[]) {
        new RestTempleteTest01();
    }
}
