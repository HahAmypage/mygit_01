package com.itheima.dao;

import com.itheima.entity.Book;
import org.apache.lucene.index.IndexReader;

import java.util.List;

public interface IBookDao {
    /**
     * 查询全部图书
     */
    List<Book> findAll();

    List<Book> findById(Integer id);
}
