/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.net.URL;

/**
 *
 * @author parsing-25
 */
public class NewClass {

    public NewClass() {
        URL resource = getClass().getResource(".");
        System.out.println("path1:" + resource.getPath());
        System.out.println("path2:" + getClass().getResource("/").getPath());

    }

    public static void main(String a[]) {
        new NewClass();
    }
}
