package com.jason.example;

import java.util.HashMap;

/**
 * Created by jasonchang on 2017/4/6.
 */
public class CrawlStat {
    private int totalProcessedPages;
    private long totalLinks;
    private long totalTextSize;
    private HashMap<String, String> kvPair;

    public int getTotalProcessedPages() {
        return totalProcessedPages;
    }

    public void setTotalProcessedPages(int totalProcessedPages) {
        this.totalProcessedPages = totalProcessedPages;
    }

    public void incProcessedPages() {
        this.totalProcessedPages++;
    }

    public long getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(long totalLinks) {
        this.totalLinks = totalLinks;
    }

    public long getTotalTextSize() {
        return totalTextSize;
    }

    public void setTotalTextSize(long totalTextSize) {
        this.totalTextSize = totalTextSize;
    }

    public void incTotalLinks(int count) {
        this.totalLinks += count;
    }

    public void incTotalTextSize(int count) {
        this.totalTextSize += count;
    }

    public HashMap<String, String> getKvPair() {
        return kvPair;
    }

    public void setKvPair(HashMap<String, String> kvPair) {
        this.kvPair = kvPair;
    }
}
