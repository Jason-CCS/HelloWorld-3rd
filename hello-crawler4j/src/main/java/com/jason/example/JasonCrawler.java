package com.jason.example;

import com.jason.MyCrawler;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by jasonchang on 2017/3/31.
 */
public class JasonCrawler extends WebCrawler {
    private static final Logger logger = LogManager.getLogger(MyCrawler.class);
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");


    HashMap<String, String> kvPair = new HashMap<>();

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
//        return !FILTERS.matcher(href).matches() && href.startsWith("http://www.companys.com.tw/");
        // 如果url有.css,.js,.gif...等等的副檔名，那麼就跳過這個url
        // 代表不對page上的一些檔案進行抓取
        return !FILTERS.matcher(href).matches();
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        /**
         * 取得q=這個參數後面所要查詢的字串，並進行url decode
         */
        String url = page.getWebURL().getURL();
        logger.debug("Current url: {}", url);
        String queryString = null;
        try {
            queryString = URLDecoder.decode(url.split("q=")[1], "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 取得共同存取的爬蟲統計資料
        // 取得擷取到的資料
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();

            // 對html進行dom化
            Document doc = Jsoup.parseBodyFragment(html);
            // 對dom擷取出所需的selector
            Elements eles = doc.select(".container tbody td a");
            for (Element ele : eles) {
                // 檢查select結果是否符合查詢字串
                if (ele.text().equals(queryString)) {
                    String uid = ele.attr("href").replace("/", "");
                    logger.info("統一編號為：{}", uid);
                    kvPair.put(queryString, uid);
                    return;
                }
            }
        }
    }

    /**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        return kvPair;
    }
}
