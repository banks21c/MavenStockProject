/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author parsing-25
 */
public class SystemPropertisPrint {

	public static void main(String args[]) throws MalformedURLException, IOException, FileNotFoundException {
		Properties props = System.getProperties();
		Enumeration keys = props.keys();
		while(keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = System.getProperty(key);
			System.out.println(key+" : "+value);
		}
	}
}
