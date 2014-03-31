package com.kcf.util;

import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.joda.time.DateTime;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午3:12
 *
 * KCF index update river static configs
 */
public class RiverConfig {

    /** basic river configs */
    final public static String RIVER_NAME = "kcf-index-update-river";
    final public static String RIVER_DESC = "kcf indices update river";
    final public static String RIVER_TYPE = "kcf";

    /** db configs */
    final public static String DEFAULT_DB_DRIVER = "com.mysql.jdbc.Driver";
    final public static String DEFAULT_DB_URL  = "jdbc:mysql://localhost:3306/kcf";
    final public static String DEFAULT_DB_USER = "root";
    final public static String DEFAULT_DB_PWD  = "12345";
    final public static int DEFAULT_DB_POOL_SIZE = 10;

    /** the default delay time every loop, 10 minutes */
    final public static long DEFAULT_DELAY = 10 * 60 * 1000;

    /** the start time to check the data */
    final public static DateTime DEFAULT_START_TIME = new DateTime(2014, 2, 2, 2, 2);

    /** the tables need to be looked */
    public static enum Tables{
        Discuss
    };

    /** update info configs */
    final public static String UPDATE_INDEX = "kcf_river";
    final public static String UPDATE_TYPE  = "river_table";

    /** the es index name */
    final public static String KCF_INDEX = "kcf-jdbc-index";

}
