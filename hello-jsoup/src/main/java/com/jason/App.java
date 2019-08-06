package com.jason;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        Document doc = Jsoup.connect("http://www.companys.com.tw/litem.aspx?q=%E5%85%81%E5%B0%87").get();
        System.out.println(doc.html());
    }
}
