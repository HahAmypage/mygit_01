package com.itheima;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class App5_solrj_index_crud02 {
    //1. 添加索引(更新索引：如果id值存在执行更新；否则执行添加)
    @Test
    public void add_index() throws Exception {
        //1.1 通过HttpSolrServer,连接solr服务器
        HttpSolrServer solrServer = new HttpSolrServer("http://127.0.0.1:8886/solr");
        //1.2 创建文档，添加域
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id",103);
        document.addField("name","hehehehe");
        //1.3 添加索引
        solrServer.add(document);
        //1.4 提交
        solrServer.commit();
    }

    //2、删除索引
    @Test
    public void  test_delete_index() throws IOException, SolrServerException {
        //2.1通过HttpSolrServer，连接solr服务器
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://127.0.0.1:8886/solr");

        //删除索引(根据id)
//        httpSolrServer.deleteById("101");

        //删除全部
        httpSolrServer.deleteByQuery("*:*");
        httpSolrServer.commit();
    }

    //3.查询
    @Test
    public void query() throws SolrServerException {
        //3.1 通过HttpSolrServer,连接solr服务器
        HttpSolrServer httpSolrServer = new HttpSolrServer("http://127.0.0.1:8886/solr");
        //3.2 查询对象(这里不传查询条件就不进行查询,*:*是查询全部)
        SolrQuery solrQuery = new SolrQuery("*:*");
        //3.3 查询
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        //3.4 获取查询结果
        SolrDocumentList results = queryResponse.getResults();
        //3.5 遍历结果
        results.forEach(document->{
            System.out.println("=======================");
            System.out.println(document.get("id"));
            System.out.println(document.get("name"));
        });
    }
}
