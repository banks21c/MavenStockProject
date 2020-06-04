/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coupang.partners;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class OpenApiTestApplication {
    private final static String REQUEST_METHOD = "POST";
    private final static String DOMAIN = "https://api-gateway.coupang.com";
    private final static String URL = "/v2/providers/affiliate_open_api/apis/openapi/deeplink";
    // Replace with your own ACCESS_KEY and SECRET_KEY
    private final static String ACCESS_KEY = "1895fbee-cac6-456a-9d9e-7b198e8735b8";
    private final static String SECRET_KEY = "a59ac9889dbeb7b32cd7304bf361e13c05e0387a";
    private final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/search?component=&q=good&channel=user\",\"https://www.coupang.com/np/coupangglobal\"]}";

    public static void main(String[] args) throws IOException {
        // Generate HMAC string
        String authorization = HmacGenerator.generate(REQUEST_METHOD, URL, SECRET_KEY, ACCESS_KEY);

        // Send request
        StringEntity entity = new StringEntity(REQUEST_JSON, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        org.apache.http.HttpHost host = org.apache.http.HttpHost.create(DOMAIN);
        org.apache.http.HttpRequest request = org.apache.http.client.methods.RequestBuilder
                .post(URL).setEntity(entity)
                .addHeader("Authorization", authorization)
                .build();

        org.apache.http.HttpResponse httpResponse = org.apache.http.impl.client.HttpClientBuilder.create().build().execute(host, request);

        // verify
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
//{
//   "rCode":"0",
//   "rMessage":"",
//   "data":[
//      {
//         "originalUrl":"https://www.coupang.com/np/search?component=&q=good&channel=user",
//         "shortenUrl":"https://coupa.ng/bDx9YY",
//         "landingUrl":"https://link.coupang.com/re/AFFSRP?lptag=AF5310383&pageKey=good&traceid=V0-183-c890f6fb18804fe8"
//      },
//      {
//         "originalUrl":"https://www.coupang.com/np/coupangglobal",
//         "shortenUrl":"https://coupa.ng/bDx9Y0",
//         "landingUrl":"https://link.coupang.com/re/AFFJIKGU?lptag=AF5310383&traceid=V0-183-50c6c2b97fba9aee"
//      }
//   ]
//}
	
    }
}