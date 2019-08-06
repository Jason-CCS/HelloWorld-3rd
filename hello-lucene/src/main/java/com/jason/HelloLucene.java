package com.jason;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jc6t on 2015/3/23.
 * Read all documents in a specific folder and create index for them.
 * Then, search document by keyword.*/
public class HelloLucene {
    private static final String INDEX_DIR = "try-lucene/testIndex";
    private static final String DATA_DIR = "try-lucene/inputData";

    public static void main(String[] args) throws IOException, ParseException {

        if(!new File(INDEX_DIR).exists()){
            createIndex(DATA_DIR);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("please type the keyword you want to search, then type enter...");
        String input = br.readLine();

        if (input.split(" ").length > 1) {
            System.out.println("only one arg!");
        } else {
            List<String> hitFileList = searchIndex(input);
            System.out.println("The files include " + input + " list below:");
            for (String fileName : hitFileList) {
                System.out.println(fileName);
            }
        }

    }

    /**
     * create indexes from specified folder*/
    private static void createIndex(String path) {
        System.out.println("start to create index...");
        List<File> fileList = Arrays.asList(new File(path).listFiles());

        for (File file : fileList) {
            buildIndex(file);// create index
        }

    }

    /**
     * create single index from a single document
     *
     * @param file*/
    private static void buildIndex(File file) {
        String content = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            // 使用readLine方法每次读取一行记录
            String s = null;
            while ((s = reader.readLine()) != null) {
                content = content + "\n" + s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Analyzer analyzer;
        Directory directory = null;
        IndexWriter indexWriter = null;
        try {

            /**
             * set an analyzer*/
            analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            directory = FSDirectory.open(new File(INDEX_DIR));

            // 假定索引目录不存在则创建目录
            File indexFile = new File(INDEX_DIR);
            if (!indexFile.exists()) {
                indexFile.mkdirs();
            }

            /**
             * set an indexWriterConfig
             * and set config properties.*/
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
            // here set OpenMode.Create_OR_APPEND is very important because we don't want the indices files are replaced by new.
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            // We want index files are replaced by new if the same.
//            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

            /**
             * set indexWriter constructor*/
            indexWriter = new IndexWriter(directory, config);

            /**
             * set segment here, force 10 documents needed to be merged.
             **/


//            indexWriter.forceMerge(5);// this function is very expensive

            // 创建文档document对象
//            System.out.println("文件名称为：" + file.getName());
//            System.out.println("文件path地址为：" + file.getPath());
//            System.out.println("文件内容为：" + content + "\n");
            Document document = new Document();
            document.add(new TextField("filename", file.getName(), Field.Store.YES));
            document.add(new TextField("content", content, Field.Store.YES));
            document.add(new TextField("path", file.getPath(), Field.Store.YES));

            // 通过index writer对象的addDocument方法写入索引
            indexWriter.addDocument(document);
            // here is commit(), like DBS
            indexWriter.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * search indices by keyword and return a list of string of file path.
     *
     * @param keyword
     * @return*/
    private static List<String> searchIndex(String keyword) throws IOException, ParseException {
        System.out.println("start to search...");
        List<String> result = new ArrayList<String>();
        Analyzer a = new StandardAnalyzer(Version.LUCENE_CURRENT);
        Directory directory = FSDirectory.open(new File(INDEX_DIR));
        DirectoryReader dicReader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(dicReader);
        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "content", a);
        Query query = parser.parse(keyword);
        ScoreDoc[] hits = searcher.search(query, 1000).scoreDocs;
        for (ScoreDoc hit : hits) {
            Document document = searcher.doc(hit.doc);
            Explanation explain=searcher.explain(query, hit.doc);
            System.out.println("----------- explain score ------------");
            System.out.println(explain.getDescription());
            System.out.println(explain.getValue());
            System.out.println(hit.score);
            System.out.println("---------- finish explain ------------");
//            System.out.println("搜索到filename: " + document.get("filename"));
//            System.out.println("搜索到content: " + document.get("content"));
//            System.out.println("搜索到path: " + document.get("path"));
            result.add(document.get("filename"));
        }
        directory.close();
        if (dicReader != null) {
            dicReader.close();
        }
        System.out.println("Search finished!");
        return result;
    }
}
