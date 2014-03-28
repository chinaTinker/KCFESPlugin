package com.kcf.tasker.looker;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.List;
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

    /** to looked table name */
    private String table = null;

    /** every loop delay time in millisecond */
    private long delay = 10 * 60 * 1000;

    /** keep the last updated timestamp */
    private long lastUpdateTimeStamp = 0l;


    public Looker(long delay){
        this.delay = delay;
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
                logger.warn("Looker[{}], interupted --> it will close", this.table, e);
                isRunning = false;
            }
        }
    }

    /** get data from db */
    protected abstract List<T> lookData();

    /** get the max update time in fetched data */
    protected abstract long getLastUpdateTimeStamp(List<T> data);

    /**
     * Keep the last update info into ES
     * It occurs when lastUpdateTimeStamp is changed
     */
    private void saveUpdateInfo(){
        //TODO
    }

    /**
     * Get the last update info from ES.
     * It occurs when river starting,
     * or recover from a exception
     */
    private void recoverFromSavedUpdateInfo(){
        //TODO
    }

    /** notify the server that I got the data */
    private void notify(Object data){
        super.setChanged();
        super.notifyObservers(data);
    }

    public String getTable() {
        return table;
    }

    private String getEntityClassName() {
        ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
        Class<T> tClazz = (Class<T>) pt.getActualTypeArguments()[0];
        return tClazz.getSimpleName();
    }
}
