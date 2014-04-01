package com.kcf.repo;

import com.kcf.util.DBHelper;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午4:45
 */
public class DiscussRepo {
    private static final ESLogger logger = ESLoggerFactory.getLogger("DiscussRepo");
    /**
     * get the topic`s comments` content
     * each content seperated by "#"
     *
     * the format is :
     *   {content1}#{content2}#{content3}#...
     */
    public String getReplies(Long topicId){
        StringBuilder sb = new StringBuilder();

        if(topicId != null && topicId > 0){
            String sql = "select content from DiscussComment where topicId = ?";

            Connection conn = DBHelper.getConnection();
            try {
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setLong(1, topicId);

                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    sb.append(rs.getString("content")).append("#");
                }

                pst.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBHelper.close(conn);
            }
        }

        String result = sb.toString();
        if(result.length() > 0){
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
