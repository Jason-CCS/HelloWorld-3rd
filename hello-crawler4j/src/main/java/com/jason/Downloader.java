package com.jason;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is a demonstration of how crawler4j can be used to download a
 * single page and extract its title and text.
 */
public class Downloader {
    private static final Logger logger = LogManager.getLogger(Downloader.class);

    private final Parser parser;
    private final PageFetcher pageFetcher;

    public Downloader() throws InstantiationException, IllegalAccessException {
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder("/Users/jasonchang/crawl-data/");
        config.setMaxDepthOfCrawling(0);
        config.setIncludeHttpsPages(true);
        config.setUserAgentString("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
        parser = new Parser(config);
        pageFetcher = new PageFetcher(config);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Downloader downloader = new Downloader();
//        downloader.processUrl("https://en.wikipedia.org/wiki/Main_Page/");
        downloader.processUrl("http://www.companys.com.tw/");
    }

    public void processUrl(String url) {
        logger.debug("Processing: {}", url);
        Page page = download(url);
        if (page != null) {
            ParseData parseData = page.getParseData();
            if (parseData != null) {
                if (parseData instanceof HtmlParseData) {
                    HtmlParseData htmlParseData = (HtmlParseData) parseData;
                    logger.debug("Title: {}", htmlParseData.getTitle());
                    logger.debug("Text length: {}", htmlParseData.getText().length());
                    logger.debug("Html length: {}", htmlParseData.getHtml().length());
                }
            } else {
                logger.warn("Couldn't parse the content of the page.");
            }
        } else {
            logger.warn("Couldn't fetch the content of the page.");
        }
        logger.debug("==============");
    }

    private Page download(String url) {
        WebURL curURL = new WebURL();
        curURL.setURL(url);
        PageFetchResult fetchResult = null;
        try {
            fetchResult = pageFetcher.fetchPage(curURL);
            if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
                Page page = new Page(curURL);
                fetchResult.fetchContent(page);
                parser.parse(page, curURL.getURL());
                return page;
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching url: " + curURL.getURL(), e);
        } finally {
            if (fetchResult != null) {
                fetchResult.discardContentIfNotConsumed();
            }
        }
        return null;
    }
}
