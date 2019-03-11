package com.itheima.dao.impl;

import com.itheima.dao.IBookDao;
import com.itheima.entity.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements IBookDao {
    /**
     * 查询全部图书
     */
    @Override
    public List<Book> findAll() {
        //创建结果集合
        List<Book> bookList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/day64","root","chanyx123");
            String sql = "select * from book";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookname(rs.getString("bookname"));
                book.setPic(rs.getString("pic"));
                book.setPrice(rs.getFloat("price"));
                book.setBookdesc(rs.getString("bookdesc"));

                bookList.add(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                if (connection!=null) connection.close();
                if (ps!=null) ps.close();
                if (rs!=null) rs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bookList;
    }

    @Override
    public List<Book> findById(Integer id) {
        //创建结果集合
        List<Book> bookList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/day64","root","chanyx123");
            String sql = "select * from book WHERE id="+id;
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookname(rs.getString("bookname"));
                book.setPic(rs.getString("pic"));
                book.setPrice(rs.getFloat("price"));
                book.setBookdesc(rs.getString("bookdesc"));

                bookList.add(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放资源
            try {
                if (connection!=null) connection.close();
                if (ps!=null) ps.close();
                if (rs!=null) rs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bookList;
    }
}
