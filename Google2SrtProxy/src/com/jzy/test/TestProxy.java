package com.jzy.test;

import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestProxy {

	public static void main(String[] args) throws Exception {
		//设置代理   
		System.setProperty("http.proxySet", "true");   
		System.setProperty("http.proxyHost", "proxy-sg-singapore.gemalto.com");   
		System.setProperty("http.proxyPort", "8080");   
		  
		//直接访问目的地址   
		URL url = new URL("http://video.google.com/timedtext?lang=en&v=G7djoQfncRw&safe=active");   
		URLConnection con = url.openConnection();   
		InputStreamReader isr = new InputStreamReader(con.getInputStream());   
		char[] cs = new char[1024];   
		int i = 0;   
		while ((i = isr.read(cs)) > 0) {   
		    System.out.println(new String(cs, 0, i));   
		}   
		isr.close(); 
	}
}
