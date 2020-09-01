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
public class ResourcePath {

	public ResourcePath() {
		URL resource1 = getClass().getResource(".");
		System.out.println("path1:" + resource1.getPath());
		URL resource2 = getClass().getResource("/");
		System.out.println("path2:" + resource2.getPath());

	}

	public static void main(String a[]) {
		new ResourcePath();
	}
}
