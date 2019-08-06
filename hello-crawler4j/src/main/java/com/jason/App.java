package com.jason;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.message.BasicHeader;

import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/Users/jasonchang/crawl-data/";
        int numberOfCrawlers = 1;
        int maxDepthOfCrawling = 0;

        CrawlConfig config = new CrawlConfig();
        // 這個一定要設定，設定中間資料存放的位置
        config.setCrawlStorageFolder(crawlStorageFolder);
        // 設定要抓取的深度，這邊設定1代表只爬給定的url那個頁面，不再抓取url底下其他的連結
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        // 設定可以抓取https的網址
        config.setIncludeHttpsPages(true);
        // 要設定這個user agent有些網站才給予抓取，否則會擋
        config.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
        // 要設定headers才能抓取，目前這個解決辦法是因為www.companys.com.tw這個網站所設定的，而headers到底要設定怎樣才ok，不太確定
        // 目前是設定成跟chrome自動送出是一樣的，這樣是有辦法抓得到的
        List<BasicHeader> headers = Arrays.asList(
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
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.companys.com.tw/litem.aspx?q=%E5%85%81%E5%B0%87");
//        controller.addSeed("http://www.ics.uci.edu/~lopes/");
//        controller.addSeed("http://www.yahoo.com/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
    }
}
