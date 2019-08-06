package com.jason;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jc6t on 2015/4/8.
 */
public class HelloJoda {

    public static void main(String[] args) {
        String start="2010/10/10 17:17:17";
        String end="2014/10/10 17:17:17";

        String[] dateAndTime=start.split("\\s");
        List<Integer> dateTimeList=new ArrayList<Integer>(7);
        for(int i=0;i<7;i++)
            dateTimeList.add(0);

        String[] date=dateAndTime[0].split("/");
        String[] time;
        if(dateAndTime.length==2) {
            time = dateAndTime[1].split(":");
            dateTimeList.set(3,Integer.parseInt(time[0]));
            dateTimeList.set(4,Integer.parseInt(time[1]));
            dateTimeList.set(5,Integer.parseInt(time[2]));
        }

        dateTimeList.set(0,Integer.parseInt(date[0]));
        dateTimeList.set(1,Integer.parseInt(date[1]));
        dateTimeList.set(2,Integer.parseInt(date[2]));

        int[] t=new int[7];
        for(int i=0;i<dateTimeList.size();i++)
            t[i]=dateTimeList.get(i);
        DateTime d1=new DateTime(t[0],t[1],t[2],t[3],t[4],t[5],t[6]);

        System.out.println(d1);

    }
}
