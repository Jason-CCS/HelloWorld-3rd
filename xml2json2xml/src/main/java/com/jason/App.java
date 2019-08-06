package com.jason;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");
        File xmlFile=new File("conf/delta_productmc.xml");
        BufferedReader br=new BufferedReader(new FileReader(xmlFile));
        StringBuilder sb=new StringBuilder();
        String tmpStr;
        do {
            tmpStr = br.readLine();
            sb.append(tmpStr);
        }while (tmpStr!=null);

        try {
            if(sb.length()==0){
                return;
            }
            JSONObject xmlJSONObj= XML.toJSONObject(sb.toString());
            System.out.println(xmlJSONObj.toString(4));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
