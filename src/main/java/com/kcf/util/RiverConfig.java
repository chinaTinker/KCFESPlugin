package com.kcf.util;

import org.elasticsearch.common.collect.Lists;

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

    final public static String RIVER_NAME = "kcf-index-update-river";
    final public static String RIVER_DESC = "kcf indices update river";
    final public static String RIVER_TYPE = "kcf";

    final public static String DEFAULT_DB_URL  = "jdbc:mysql://localhost:3306/kcf";
    final public static String DEFAULT_DB_USER = "root";
    final public static String DEFAULT_DB_PWD  = "12345";

    /** the default delay time every loop, 10 minutes */
    final public static long DEFAULT_DELAY = 10 * 60 * 1000;

    /** the tables need to be looked */
    public static enum Tables{
        Discuss
    };
}
