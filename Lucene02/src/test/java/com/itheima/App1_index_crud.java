package com.itheima;

import com.itheima.dao.impl.BookDaoImpl;
import com.itheima.entity.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App1_index_crud {

    private static final String PATH="E:\\library\\index";

    @Test
    public void test_addIndex() throws Exception {
        List<Book> bookList = new BookDaoImpl().findAll();
        List<Document> documentList = new ArrayList<>();
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        conf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory d = FSDirectory.open(new File(PATH));
        IndexWriter indexWriter = new IndexWriter(d,conf);
        for (Book book : bookList){
            Document document = new Document();
            document.add(new StringField("id",book.getId()+"", Field.Store.YES));
            document.add(new TextField("bookname",book.getBookname()+"", Field.Store.YES));
            document.add(new FloatField("price",book.getPrice(), Field.Store.YES));
            document.add(new StoredField("pic",book.getPic()+""));
            document.add(new TextField("bookdesc",book.getBookdesc()+"", Field.Store.NO));

            documentList.add(document);
        }

        documentList.forEach(doc ->{
            try {
                indexWriter.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void deleteByTerm() throws IOException {
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(PATH)),new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer()));
        indexWriter.deleteDocuments(new Term("bookname","java"));

        indexWriter.commit();
        indexWriter.close();
    }

    @Test
    public void updateIndex() throws IOException {
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File(PATH)),new IndexWriterConfig(Version.LUCENE_4_10_3,new IKAnalyzer()));

        Document doc = new Document();
        doc.add(new StringField("id","3", Field.Store.YES));
        doc.add(new TextField("bookname","Headfirst Lucene", Field.Store.YES));
        doc.add(new FloatField("pirce",168.8f, Field.Store.YES));
        doc.add(new StoredField("pic","haha.png"));
        doc.add(new TextField("bookdesc","无", Field.Store.YES));

        indexWriter.updateDocument(new Term("id","3"),doc);

        indexWriter.commit();
        indexWriter.close();
    }
}
