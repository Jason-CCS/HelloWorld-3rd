package com.jason;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * <pre>
 * 此处为Lucene的demo例子程序，HelloIndex.java包括以下两部分内容：
 * 1.创建index索引过程
 * 2.通过关键字查询索引过程*/


public class HelloIndex {

    private static final String RELATIVE_FILE_PATH = "testIndex";

    private static final String FIELD_HELLO_KEY = "hello";

    public static void main(String[] args) throws Exception {
        HelloIndex demo=new HelloIndex();
        demo.createIndex();
        String keyword="hELLO"; // search keyword
        String result=demo.search(keyword);
        System.out.println("result= "+ result);
    }

/**
     * 此处演示Lucene的index索引创建过程*/


    private void createIndex() throws Exception {
        // 创建并实例化标准的分析器对象
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        // 索引index信息可以放在RAM内存或File文件系统中
        // 此处将index放在RAM内存中处理
        // Directory directory = new RAMDirectory();
        // 此处将index放在File文件系统中处理
        File file = new File(RELATIVE_FILE_PATH);// 此处为相对路径，当前目录的testindex/
        Directory directory = FSDirectory.open(file);
        // 创建并实例化index writer的configuration配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
        // 设定为每次重建模式
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 创建并实例化writer对象
        IndexWriter iwriter = new IndexWriter(directory, config);

        // 加入到index索引方式通过增加document方式处理
        Document document = new Document();
        String str = "Hello world\nLucene is an open souces writed in JAVA program language.";
        document.add(new Field(FIELD_HELLO_KEY, str, TextField.TYPE_STORED));
        // 通过add document方式处理
        iwriter.addDocument(document);
        iwriter.close();// 关闭资源writer对象
        return;
    }

/**
     * 此处演示Lucene的search搜索过程*/


    private String search(String keyword) throws Exception {
        // 前边两步是同理创建index索引过程
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
        File file = new File(RELATIVE_FILE_PATH);// 此处为相对路径，当前目录的testindex/
        Directory directory = FSDirectory.open(file);
        // 创建并实例化reader对象
        DirectoryReader ireader = DirectoryReader.open(directory);
        // 创建并实例化searcher对象
        IndexSearcher isearcher = new IndexSearcher(ireader);
        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, FIELD_HELLO_KEY, analyzer);
        // 通过parser，传入keyword关键字获取query对象
        Query query = parser.parse(keyword);
        // 通过searcher对象的search查询获取对应的score docs数组，此处设定1000为最大查询记录数
        ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
        // 因为此处create index创建的时候只加入了一条记录
        // 因此理论上只会返回一条记录结果集
        Document hitDoc = isearcher.doc(hits[0].doc);
        // 通过get方式获取对应的value值
        String result = hitDoc.get(FIELD_HELLO_KEY);
        // 关闭掉资源
        ireader.close();
        directory.close();
        return result;
    }
}
