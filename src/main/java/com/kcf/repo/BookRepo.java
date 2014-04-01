package com.kcf.repo;

import com.kcf.entity.Book;
import com.kcf.util.DBHelper;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午3:30
 */
public class BookRepo {
    private static final ESLogger logger = ESLoggerFactory.getLogger("BookRepo");

    public Book getBookById(Long id){
        Book book = null;
        if(id != null && id > 0){
            String sql =
                " select id, outline, question, story, slug, created, updated" +
                " from User where id = ?";

            Connection conn = DBHelper.getConnection();
            try {
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setLong(1, id);

                ResultSet rs = pst.executeQuery();

                book = this.parseToBook(rs);

                pst.close();
                rs.close();
            } catch (SQLException e) {
                logger.error("query book failed", e);
            } finally {
                DBHelper.close(conn);
            }

        }

        return book;
    }


    private Book parseToBook(ResultSet rs){
        Book book = null;

        try {
            while(rs.next() && book == null){
                book = new Book();
                book.setId(rs.getLong("id"));
                book.setOutline(rs.getString("outline"));
                book.setQuestion(rs.getString("question"));
                book.setSlug(rs.getString("slug"));
                book.setStory(rs.getString("story"));
                book.setCreated(rs.getTimestamp("created"));
                book.setUpdated(rs.getTimestamp("updated"));
            }
        } catch (SQLException e) {
            logger.error("parse result to book failed", e);
        }

        return book;
    }
}
