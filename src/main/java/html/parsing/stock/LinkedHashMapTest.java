/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkedHashMapTest {

    public static void initData(Map<String, String> map) {
        map.put("key1", "1");
        map.put("key2", "2");
        map.put("key3", "3");
        map.put("key4", "4");
        map.put("key5", "5");
        map.put("key6", "6");
        map.put("key7", "7");
        map.put("key8", "8");
        map.put("key9", "9");
        map.put("key10", "10");
    }

    public static void printResult(Map<String, String> map) {
        Set<String> set = map.keySet();
        Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            String key = ((String) iter.next());
            String value = map.get(key);
            System.out.println("key : " + key + ", value : " + value);
        }
    }

    public static void main(String[] args) {
        // HashMap
        System.out.println("====== HashMap Test ======");
        Map<String, String> hashMap = new HashMap<String, String>();
        initData(hashMap);
        printResult(hashMap);

        // LinkedHashMap
        System.out.println("====== LinkedHashMap Test ======");
        Map<String, String> linkedHashMap = new LinkedHashMap<String, String>();
        initData(linkedHashMap);
        printResult(linkedHashMap);
    }
}
