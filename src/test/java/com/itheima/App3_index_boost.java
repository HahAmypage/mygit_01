package com.itheima;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class App3_index_boost {
    private static final String PATH="E:\\library\\index";

    @Test
    public void test_setBoost() throws IOException {
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(PATH)),new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer()));

        Term term= new Term("id","2");

        Document doc = new Document();
        doc.add(new StringField("id","2", Field.Store.YES));
        TextField textField = new TextField("bookname","java web开发", Field.Store.YES);
        textField.setBoost(200);
        doc.add(textField);
        doc.add(new FloatField("price",189.8f, Field.Store.YES));
        doc.add(new StoredField("pic","2.png"));
        doc.add(new TextField("bookdesc","无", Field.Store.YES));

        indexWriter.updateDocument(term,doc);

        indexWriter.commit();
        indexWriter.close();
    }
}
