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
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App4_hightlighter_02 {

    private static final String PATH="E:\\library\\index";

    @Test
    public void test_hightlighter02() throws Exception {
        Analyzer analyzer = new IKAnalyzer();
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(new File(PATH)));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("bookname",analyzer);
        Query query = queryParser.parse("bookname:java");
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询结果总数："+topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        Arrays.asList(scoreDocs).forEach(scoreDoc -> {
            try {
                System.out.println("===================================");
                System.out.println("文档id："+scoreDoc.doc+"--分值："+scoreDoc.score);
                Document document = indexSearcher.doc(scoreDoc.doc);
                System.out.println("图书id："+document.get("id"));

//                高亮显示

                QueryScorer queryScorer = new QueryScorer(query);
//                Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
                SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>","</span>");
                Highlighter highlighter = new Highlighter(simpleHTMLFormatter,queryScorer);

                String bookname = document.get("bookname");
                if (bookname!=null){
                    TokenStream tokenStream = TokenSources.getTokenStream(document,"bookname",analyzer);
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
