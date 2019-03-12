package com.itheima;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App2_index_search {
    private static final String PATH="E:\\library\\index";


    /**
     * 按query条件搜索的工具方法
     * @param query
     * @throws IOException
     */
    public void seacher(Query query) throws IOException {
        System.out.println("查询语法："+query);
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File(PATH)));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询总结果："+topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        Arrays.asList(scoreDocs).forEach(scoreDoc -> {
            try {
                System.out.println("--------分割线---------");
                System.out.println("文档id："+scoreDoc.doc);
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("图书ID:"+document.get("id"));
                System.out.println("图书名称："+document.get("bookname"));
                System.out.println("图书价格："+document.get("price"));
                System.out.println("图书图片："+document.get("pidc"));
                System.out.println("图示描述："+document.get("bookdesc"));
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        indexReader.close();
    }

    /**
     * 使用TermQuery搜索
     * @throws IOException
     */
    @Test
    public void searchByTermQuery() throws IOException {
        //查询语法：bookname:lucene
        TermQuery query = new TermQuery(new Term("bookname","lucene"));
        seacher(query);
    }

    @Test
    public void searchByNumericRangeQuery() throws IOException {
        NumericRangeQuery<Float> numericRangeQuery = NumericRangeQuery.newFloatRange("price",50f,80f,true,true);
        seacher(numericRangeQuery);
    }

    @Test
    public void searchByBooleanQuery() throws IOException {
        TermQuery query1 = new TermQuery(new Term("bookname","java"));
        TermQuery query2 = new TermQuery(new Term("bookname","lucene"));

        BooleanQuery booleanQuery = new BooleanQuery();

        //并集
        //查询语法：bookname:java bookname:lucene
        booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);

        //交集
        //查询语法：+bookname:java +bookname:lucene
//        booleanQuery.add(query1, BooleanClause.Occur.MUST);
//        booleanQuery.add(query2, BooleanClause.Occur.MUST);

        //MUST + MUST_NOT 包含前者，不包含后者
        //查询语法：+bookname:java -bookname:lucene
//        booleanQuery.add(query1, BooleanClause.Occur.MUST);
//        booleanQuery.add(query2, BooleanClause.Occur.MUST_NOT);

//        MUST_NOT + MUST_NOT 无意义。
//        booleanQuery.add(query1, BooleanClause.Occur.MUST_NOT);
//        booleanQuery.add(query2, BooleanClause.Occur.MUST_NOT);

        seacher(booleanQuery);
    }

    //查询语法：bookname:java bookname:lucene
    @Test
    public void searchByQueryParser() throws Exception {
        Analyzer a = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("bookname",a);
        //如果用AND 结果是1条，如果and结果是5条
        Query query = queryParser.parse("bookname:java and bookname:lucene");

        //执行Query
        seacher(query);
    }
}
