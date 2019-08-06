package com.jason;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jc6t on 2015/10/12.
 */
public class HelloSolrJ {
    public static void main(String[] args) throws IOException, SolrServerException {
        write();
        read();
    }

    static void write() throws IOException, SolrServerException {
        SolrServer server = new HttpSolrServer("http://10.16.133.61:8983/solr/");
        server.deleteByQuery("*:*");// CAUTION: deletes everything!

        SolrInputDocument doc1 = new SolrInputDocument();
        doc1.addField( "id", "id1", 1.0f );
        doc1.addField("name", "doc1", 1.0f);
        doc1.addField("price", 10);

        SolrInputDocument doc2 = new SolrInputDocument();
        doc2.addField( "id", "id2", 1.0f );
        doc2.addField("name", "doc2", 1.0f);
        doc2.addField("price", 20);

        Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
        docs.add(doc1);
        docs.add(doc2);

        UpdateResponse ur=server.add(docs);
        System.out.println(ur.toString());

        UpdateRequest req = new UpdateRequest();
        req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
        req.add(docs);
        UpdateResponse rsp = req.process( server );
        System.out.println(ur.toString());
    }

    static void read() throws SolrServerException {
        SolrServer server = new HttpSolrServer("http://10.16.133.61:8983/solr/");
        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addSortField( "price", SolrQuery.ORDER.asc );
        QueryResponse rsp = server.query( query );
        SolrDocumentList docs = rsp.getResults();
        for (Object doc : docs) {
            System.out.println(doc);
        }
    }
}
