package com.kcf.tasker.deleter;

import com.kcf.util.RiverConfig;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

/**
 * User: 老牛 -- TK
 * Date: 14-4-2
 * Time: 上午11:57
 *
 * Index delete tasker.
 *
 * Checks the indices, if find some index is not kept
 * in db, delete it, with id.
 *
 * Do the job in loop with a delay every time.
 */
public abstract class Deleter<T> implements Job{
    private static final ESLogger logger = ESLoggerFactory.getLogger("Deleter");

    protected String table;
    protected Client client;
    protected long total;

    protected Deleter(){
        this.table = this.getEntityClassName();
    }

    protected Deleter(Client client) {
        this();
        this.client = client;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Client client = (Client) jobExecutionContext.getJobDetail().getJobDataMap().get("client");
        if(client != null) this.client = client;

        this.fire();
    }

    /**
     * the main logical method
     */
    protected void fire(){
        Collection<Long> ids = this.getToDeleteIDs();

        if(!ids.isEmpty()) {
            logger.info("there are {} {} records to delete", ids.size(), this.table);

            for (Long id : ids) {
                this.deleteFromEs(id);
            }
        }
    }

    /** delete the index from ES by id */
    private void deleteFromEs(Long id){
        client.prepareDelete()
              .setIndex(RiverConfig.KCF_INDEX)
              .setType(this.getESType())
              .setId(id.toString())
              .execute();
    }

    /** get the ids that will be deleted */
    private Collection<Long> getToDeleteIDs() {
        Collection<Long> ids = Lists.newArrayList();
        Collection<Long> baseIds = this.getAllIdFromDB();

        try {
            for(int start = 0, size = 10, from = 0; from <= this.total; start++, from = start * size){
                Collection<Long> toCheckIds = this.getCurrentIDs(from, size);
                toCheckIds.removeAll(baseIds);
                if(!toCheckIds.isEmpty()){
                    ids.addAll(toCheckIds);
                }
            }

        } catch (IOException e) {
            logger.error("get the {} ids failed", e, this.table);
        }

        return ids;
    }

    protected abstract Collection<Long> getAllIdFromDB();

    /**
     * get all the ids in ES server.
     * do fetch operation will b seperated by several times,
     * if the total count > the start offset.
     *
     * @param start the origin start offset
     * @param size  the max records every fetching
     * @return      all the ids in the ES server
     * @throws IOException
     */
    private Collection<Long> getCurrentIDs(int start, int size) throws IOException {
        List<Long> ids = Lists.newArrayList();

        SearchResponse resp = client.prepareSearch(RiverConfig.KCF_INDEX)
            .setTypes(this.getESType())
            .setQuery(
                QueryBuilders.matchAllQuery()
            )
            .addField("id")
            .setFrom(start)
            .setSize(size)
            .execute()
            .actionGet();

        if(resp != null){
            logger.debug(resp.toString());

            SearchHits searchHits = resp.getHits();
            this.total = searchHits.getTotalHits();
            SearchHit[] hits = searchHits.getHits();

            if(hits != null && hits.length > 0){
                for(SearchHit hit : hits){
                    Long id = Long.valueOf(hit.getId());
                    ids.add(id);
                }
            }
        }

        return ids;
    }

    protected abstract String getESType();

    /** get the T`s class name as table name */
    private String getEntityClassName() {
        ParameterizedType pt =
                (ParameterizedType)this.getClass().getGenericSuperclass();

        Class<T> tClazz = (Class<T>) pt.getActualTypeArguments()[0];
        return tClazz.getSimpleName();
    }
}

