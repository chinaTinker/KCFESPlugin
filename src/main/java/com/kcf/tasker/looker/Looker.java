package com.kcf.tasker.looker;

import com.kcf.util.DBHelper;
import com.kcf.util.RiverConfig;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.joda.time.format.DateTimeFormat;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午4:16
 *
 * DB data looker
 *
 * It`s duty looking the newest updated data,
 * include the new added data.
 *
 * If it find some newest record, get them, and
 * send to Updater for some logical operation
 */
public abstract class Looker<T> extends Observable implements Runnable {
    private static final ESLogger logger = ESLoggerFactory.getLogger("Looker");

    /** ES client instance */
    private Client client;

    /** to looked table name */
    private String table;

    /** every loop delay time in millisecond */
    private long delay = 10 * 60 * 1000;

    /** keep the last updated timestamp */
    protected DateTime lastUpdateTimeStamp = RiverConfig.DEFAULT_START_TIME;


    public Looker(long delay, Client client){
        this.delay = delay;
        this.client = client;
        this.table = this.getEntityClassName();
    }

    @Override
    public void run() {
        logger.info("DB Looker[{}] fired ...", this.table);

        this.recoverFromSavedUpdateInfo();

        boolean isRunning = true;

        while (isRunning) {
            try {
                List<T> data = this.lookData();
                if(data != null && !data.isEmpty()){
                    logger.debug("DB Looker[{}] got {} record", this.table, data.size());

                    this.lastUpdateTimeStamp = this.getLastUpdateTimeStamp(data);
                    this.saveUpdateInfo();
                    this.notify(data);
                }

                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
                logger.warn("Looker[{}], interrupted --> it will close", this.table, e);
                isRunning = false;
            }
        }
    }

    /** get data from db */
    protected  List<T> lookData(){
        List<T> data = Lists.newArrayList();
        String sql = this.getSql();

        Connection conn = null;
        try {
            logger.info("check {} start from {}", this.table, this.lastUpdateTimeStamp);

            Timestamp timestamp = new Timestamp(this.lastUpdateTimeStamp.getMillis());

            conn = DBHelper.getConnection();

            if(conn != null) {
                PreparedStatement st = conn.prepareStatement(sql);
                st.setTimestamp(1, timestamp);

                ResultSet rs = st.executeQuery();
                data.addAll(this.parseRow(rs));

                st.close();
                rs.close();
            }

        } catch (SQLException e) {
            logger.error("check new {} data failed", e, this.table);
        } finally {
            DBHelper.close(conn);
        }

        return data;
    }

    /** get the max update time in fetched data */
    protected abstract DateTime getLastUpdateTimeStamp(List<T> data);

    /** parse the query result to T into List */
    protected abstract List<T> parseRow(ResultSet rs);

    /** get the query sql prepare statement */
    protected abstract String getSql();

    /**
     * Keep the last update info into ES
     * It occurs when lastUpdateTimeStamp is changed
     */
    private void saveUpdateInfo(){
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("table", this.table);
            builder.field("delay", this.delay);
            builder.field("lastUpdateTimestamp",
                    this.lastUpdateTimeStamp.toString("yyyy-MM-dd HH:mm:ss"));
            builder.endObject();

            client.prepareIndex()
                .setIndex(RiverConfig.UPDATE_INDEX)
                .setType(RiverConfig.UPDATE_TYPE)
                .setId(this.table)
                .setSource(builder)
                .execute()
                .actionGet();
        } catch (IOException e) {
            logger.error("save updateInfo[{}] failed", e, this.table);
        }


    }

    /**
     * Get the last update info from ES.
     * It occurs when river starting,
     * or recover from a exception
     */
    private void recoverFromSavedUpdateInfo(){
        GetResponse resp = client.prepareGet()
              .setIndex(RiverConfig.UPDATE_INDEX)
              .setType(RiverConfig.UPDATE_TYPE)
              .setId(this.table)
              .execute()
              .actionGet();

        String key = "lastUpdateTimestamp";
        Map<String, Object> source = resp.getSource();

        if(source != null && !source.isEmpty() && source.containsKey(key)){
            Object lastUpdateTime = source.get(key);

            if(lastUpdateTime instanceof String){

                this.lastUpdateTimeStamp = DateTime.parse(
                    (String) lastUpdateTime,
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                );

                logger.info("recovered last update time: {}", lastUpdateTime);
            }else {
                logger.warn("{} is not a String clazz, but {}",
                        key, lastUpdateTime.getClass());
            }
        }

    }

    /** notify the server that I got the data */
    private void notify(Object data){
        super.setChanged();
        super.notifyObservers(data);
    }

    /** get the current table that been checking */
    public String getTable() {
        return table;
    }

    /** get the T`s class name as table name */
    private String getEntityClassName() {
        ParameterizedType pt =
                (ParameterizedType)this.getClass().getGenericSuperclass();

        Class<T> tClazz = (Class<T>) pt.getActualTypeArguments()[0];
        return tClazz.getSimpleName();
    }
}
