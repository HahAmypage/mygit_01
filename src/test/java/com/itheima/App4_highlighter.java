package com.itheima;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App4_highlighter {
    //索引地址
    private static final String PATH="E:\\library\\index";

    @Test
    public void test_hightlighter() throws Exception {


        Analyzer anlayzer = new IKAnalyzer();
        QueryParser queryParser = new QueryParser("bookname",anlayzer);
        Query query = queryParser.parse("bookname:java");

        /**
         * 高亮实现步骤：
         * 1.建立分析器对象（Analyzer），用于分词
         * 2.建立分值对象（QueryScorer），计算分值
         * 3.建立输出片段对象（Fragmenter），用于把文档内容切片（分段）
         * 4.建立高亮组件对象（Highlighter）
         * 5.通过TokenSources类获取文档对象的流对象（TokenStream）
         * 6.使用高亮组件对象，获取高亮结果内容
         */
        QueryScorer queryScorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
        Highlighter highlighter = new Highlighter(queryScorer);
        highlighter.setTextFragmenter(fragmenter);

        Directory d = FSDirectory.open(new File(PATH));
        IndexReader indexReader = DirectoryReader.open(d);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //搜索
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询结果："+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        Arrays.asList(scoreDocs).forEach(scoreDoc -> {
            try {
                System.out.println("---------------------");
                System.out.println("文档id："+scoreDoc.doc+";分值："+scoreDoc.score);
                //获取文档对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("图书id："+document.get("id"));
                String bookname = document.get("bookname");
                if (bookname!=null){
                    TokenStream tokenStream = TokenSources.getTokenStream(document,"bookname",anlayzer);
                    bookname = highlighter.getBestFragment(tokenStream,bookname);
                }
                System.out.println("图书名称："+bookname);
                System.out.println("图书价格："+document.get("price"));
                System.out.println("图书图片："+document.get("pic"));
                System.out.println("图书描述："+document.get("bookdesc"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        indexReader.close();
    }
}
