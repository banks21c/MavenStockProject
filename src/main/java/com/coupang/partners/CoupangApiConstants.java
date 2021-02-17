/**
 * 
 */
package com.coupang.partners;

public abstract class CoupangApiConstants {
    final static String REQUEST_METHOD = "POST";
    final static String DOMAIN = "https://api-gateway.coupang.com";
    final static String URL = "/v2/providers/affiliate_open_api/apis/openapi/deeplink";
    // Replace with your own ACCESS_KEY and SECRET_KEY
    final static String ACCESS_KEY = "1895fbee-cac6-456a-9d9e-7b198e8735b8";
    final static String SECRET_KEY = "a59ac9889dbeb7b32cd7304bf361e13c05e0387a";

    final static String REQUEST_JSON = "{\"coupangUrls\": [\"https://www.coupang.com/np/search?component=&q=good&channel=user\",\"https://www.coupang.com/np/coupangglobal\"]}";
}