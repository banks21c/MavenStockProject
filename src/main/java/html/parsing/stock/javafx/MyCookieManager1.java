/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock.javafx;

import com.sun.webkit.network.CookieManager;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.CookieHandler;
import java.net.URI;
import java.util.*;

public class MyCookieManager1 extends CookieHandler {
    private final CookieManager cookieManager = new CookieManager();
    private final Set<URI> uris = new HashSet<>();

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        uris.add(uri);
        return cookieManager.get(uri, requestHeaders);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        uris.add(uri);
        cookieManager.put(uri, responseHeaders);
    }

    void save() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Saving cookies");
        System.out.println(uris);
        for (URI uri : uris) {
            System.out.println(uri);
            System.out.println(cookieManager.get(uri, new HashMap<>()));
        }
    }
}