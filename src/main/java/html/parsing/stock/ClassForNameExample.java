/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package html.parsing.stock;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 *
 * @author parsing-25
 */
public class ClassForNameExample {

	ClassForNameExample() {
		test2();
		//test2();
		//test3();
	}

	public void test1() {
		String url = "http://www.newstomato.com/ReadNews.aspx?no=819904";
		Class<?> c;
		try {
			c = Class.forName("html.parsing.stock.NewsTomatoCom");
			//Constructor<?> ctor = c.getConstructors()[0];
			Constructor<?> ctor = c.getConstructor(String.class);
			Object object = ctor.newInstance(new Object[]{url});
			//c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs  );
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void test2() {
		String url = "http://www.newstomato.com/ReadNews.aspx?no=819904";
		Class<?> c;
		try {
			c = Class.forName("html.parsing.stock.NewsTomatoCom");
			//c.getDeclaredMethods()[0].invoke(object, Object... MethodArgs  );
			Method method = c.getDeclaredMethod("createHTMLFile", String.class);
			StringBuilder sb = (StringBuilder) method.invoke(String.class, new Object[]{url});
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.INFO, sb.toString());
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void test3() {
		String url = "http://www.newstomato.com/ReadNews.aspx?no=819904";
		Class<?> c;
		try {
			c = Class.forName("html.parsing.stock.NewsTomatoCom");
			// Get the private constructor.
			Constructor<?> ctor = c.getDeclaredConstructor();
			// Since it is private, make it accessible.
			ctor.setAccessible(true);
			// Create new object. 
			Object obj = ctor.newInstance();
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			java.util.logging.Logger.getLogger(ClassForNameExample.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void main(String args[]) {
		new ClassForNameExample();
	}
}
