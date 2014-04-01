package com.kcf.tasker.updater;

import com.kcf.tasker.looker.Looker;
import com.kcf.util.RiverConfig;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 下午6:07
 */
public abstract class Updater<T> implements Observer {
    private final static ESLogger logger = ESLoggerFactory.getLogger("Update");

    final private static String INDEX = RiverConfig.KCF_INDEX;

    protected String table = this.getEntityClassName();
    private Client client;

    protected Updater(Client client) {
        this.client = client;
    }

    @Override
    public void update(Observable observable, Object o) {
        if(observable instanceof Looker){
            Looker looker = (Looker) observable;

            if(this.table.equals(looker.getTable())){
                this.dealData(o);
            }
        }
    }

    /** deal with the data */
    protected void dealData(Object obj){
        if(obj instanceof List){
            List<T> objs = (List<T>) obj;

            logger.info("{} There commes {} records", this.table, objs.size());

            if(!objs.isEmpty()){
                for(T t : objs){
                    String json = this.builderJson(t);
                    String currrentId = this.getCurrentId(t);

                    this.upload(json, currrentId);
                }
            }

        }
    }

    /** parse tht T to a json format string */
    protected abstract String builderJson(T t);

    protected abstract String getESType();
    protected abstract String getCurrentId(T t);

    /** fire the json to es search */
    protected void upload(String json, String id){
        String crrType = getESType();

        IndexResponse resp =  client.prepareIndex(INDEX, crrType, id)
              .setSource(json)
              .execute()
              .actionGet();

        if(resp != null){
            logger.info("set index successfully {}, {}, {}",
                    resp.getIndex(), resp.getId(), resp.getType());
        }else {
            logger.info("set index failed: {}, {}", crrType, id);
        }

    }

    private String getEntityClassName() {
        ParameterizedType pt = (ParameterizedType)this.getClass().getGenericSuperclass();
        Class<T> tClazz = (Class<T>) pt.getActualTypeArguments()[0];
        return tClazz.getSimpleName();
    }
}
