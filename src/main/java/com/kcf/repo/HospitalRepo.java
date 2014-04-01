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
 * Time: 下午3:20
 *
 * hospital repository
 */
public class HospitalRepo {
    private static final ESLogger logger = ESLoggerFactory.getLogger("HospitalRepo");

    /** get the current hospitals by book id */
    public String getHospitals(Long bookId){
        StringBuilder sb = new StringBuilder();

        if(bookId != null && bookId > 0){
            String sql =
                    " select distinct h.name from ItemHospital ih " +
                    " inner join `Hospital` h on ih.hospitalId=h.id " +
                    " where ih.userId=? and h.name != '' and h.name is not null " +
                    " order by ih.id desc";

            Connection conn = DBHelper.getConnection();
            try {
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setLong(1, bookId);

                ResultSet rs = pst.executeQuery();

                while(rs.next()){
                    sb.append(rs.getString("name")).append(",");
                }

                pst.close();
                rs.close();

            } catch (SQLException e) {
                logger.error("query hospitals failed", e);
            } finally {
                DBHelper.close(conn);
            }
        }

        String result = sb.toString();

        if(result.length() > 0){
            result = result.substring(0, sb.length() - 1);
        }

        return result;
    }
}
