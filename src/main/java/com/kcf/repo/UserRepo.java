package com.kcf.repo;

import com.kcf.entity.User;
import com.kcf.util.DBHelper;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * User: 老牛 -- TK
 * Date: 14-4-1
 * Time: 下午3:32
 */
public class UserRepo {
    private static final ESLogger logger = ESLoggerFactory.getLogger("UserRepo");

    public User getUserById(Long id){
        User user = null;
        if(id != null && id > 0){
            String sql = "select id, name, birth, gender from UserBase" +
                    " where id = ?";

            Connection conn = DBHelper.getConnection();
            try {
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setLong(1, id);

                ResultSet rs = pst.executeQuery();

                user = this.parstToUser(rs);

                pst.close();
                rs.close();
            } catch (SQLException e) {
                logger.error("query user failed", e);
            } finally {
                DBHelper.close(conn);
            }
        }

        return user;
    }

    private User parstToUser(ResultSet rs){
        User user = null;

        try {
            while(rs.next() && user == null){
                user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));

                Date birth = rs.getDate("birth");
                if(birth != null){
                    DateTime theBirth = new DateTime(birth.getTime());
                    int age = DateTime.now().year().get() - theBirth.year().get();

                    user.setAge(age);
                }

                Integer gender = rs.getInt("gender");
                if(gender != null && gender > -1){
                    String theGender = gender == 1? "男" : "女";

                    user.setGender(theGender);
                }

            }
        } catch (SQLException e) {
            logger.error("parse to user failed", e);
        }

        return user;
    }
}
