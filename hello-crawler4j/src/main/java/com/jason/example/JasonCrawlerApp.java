package com.jason.example;

import com.google.common.base.Strings;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jasonchang on 2017/3/31.
 */
public class JasonCrawlerApp {
    public static void main(String[] args) throws Exception {
        /*
         * Crawler 的 configuration 設定
         */
        String crawlStorageFolder = "/Users/jasonchang/crawl-data/";
        int numberOfCrawlers = 1; // 設定一個thread
        int maxDepthOfCrawling = 0;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setIncludeHttpsPages(true);
        config.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
        List<BasicHeader> headers = Arrays.asList(
                new BasicHeader("Accept", "text/html,text/xml"),
                new BasicHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4")
        );
        config.setDefaultHeaders(headers);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * INPUT: 讀取csv file 公司名單
         */
        File file = new File("公司名單.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> queryList = new ArrayList<>();
        String tmpStr = null;
        do {
            tmpStr = br.readLine();
            // 讀取的字串不等於空，就加入到 query list中
            if (!Strings.isNullOrEmpty(tmpStr)) {
                queryList.add(tmpStr);
            }
        } while (tmpStr != null);
        br.close();


        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        for (String s : queryList) {
            controller.addSeed("http://www.companys.com.tw/litem.aspx?q=" + s);
        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(JasonCrawler.class, numberOfCrawlers);

        /*
         * OUTPUT: crawler爬文結束，取得每個crawler的資料，並將資料寫出到檔案中，取得方法 controller.getCrawlersLocalData()
         */
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        FileOutputStream fos = new FileOutputStream("統一編號.csv", false);

        // 在這裡 List<Object> 的 crawlersLocalData 只有一個，才能這樣寫，如果 thread 真的有多個， 這樣寫是不正確的。
        for (Object localData : crawlersLocalData) {
            HashMap<String, String> curKVPair = (HashMap)localData;
            for (String companyName: queryList){
                if(curKVPair.containsKey(companyName)){
                    String uid = curKVPair.get(companyName);
                    fos.write(uid.getBytes("utf8"));
                    fos.write("\n".getBytes("utf8"));
                }
            }
        }

        fos.close();
    }
}
