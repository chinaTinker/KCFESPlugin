package com.kcf.util;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * User: 老牛 -- TK
 * Date: 14-3-31
 * Time: 上午10:22
 *
 * Database helper, to do some db initialization
 * And give the connection fetch method
 */
public class DBHelper {
    private static final ESLogger logger = ESLoggerFactory.getLogger("DBHelper");

    private static final List<Connection> pool = Lists.newLinkedList();
    private static final int poolSize = RiverConfig.DEFAULT_DB_POOL_SIZE;

    private static String url;
    private static String user;
    private static String pwd;

    /**
     * Init the jdbc driver
     * Load the driver
     * Fill the connection pool fully
     *
     * @param url   the db`s url
     * @param user  login user
     * @param pwd   login password
     */
    public static void init(String url, String user, String pwd){
        DBHelper.url = url;
        DBHelper.user = user;
        DBHelper.pwd = pwd;

        loadDriver();
        fillPool();
    }

    /**
     * if pool has connection, get from pool,
     * or else create a connection
     *
     * @return available connection
     */
    public static Connection getConnection(){
        if(pool.isEmpty()){
            return newConnection();
        }else {
            return pool.remove(0);
        }
    }

    public static Connection newConnection(){
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, pwd);
        } catch (SQLException e) {
            logger.error("create a connection failed", e);
        }

        return conn;
    }

    public static void close(Connection conn) {
        if(conn != null){
            if(pool.size() < poolSize){
                pool.add(conn);
            }else{
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(RiverConfig.DEFAULT_DB_DRIVER).newInstance();
        } catch (InstantiationException e) {
            logger.error("Instantiation error", e);
        } catch (IllegalAccessException e) {
            logger.error("access error", e);
        } catch (ClassNotFoundException e) {
            logger.error("class not found", e);
        }
    }

    private static void fillPool(){
        for(int i = 0; i < poolSize; i++){
            pool.add(newConnection());
        }

        logger.info("{} connection created and keep in pool", poolSize);
    }

}
