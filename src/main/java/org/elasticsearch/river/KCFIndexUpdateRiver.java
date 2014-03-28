package org.elasticsearch.river;

import com.kcf.tasker.looker.Looker;
import com.kcf.tasker.looker.Lookers;
import com.kcf.util.RiverConfig;
import com.kcf.util.RiverConfig.Tables;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.common.xcontent.support.XContentMapValues;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 老牛 -- TK
 * Date: 14-3-27
 * Time: 上午11:13
 *
 * KCF indices update river
 * The logical engine fired here
 */
public class KCFIndexUpdateRiver extends AbstractRiverComponent implements River{
    private SettingInfo settingInfo = null;

    private Map<String, Thread> threads = Maps.newHashMap();

    @Inject
    public KCFIndexUpdateRiver(RiverName riverName, RiverSettings settings,
                               @RiverIndexName String riverIndexName, Client client) {
        super(riverName, settings);

        this.settingInfo = this.wrapSetting(this.getSourceSettings(settings));

        logger.info("get the custom config info: {}", this.settingInfo);

        //TODO some db initials operations
    }

    @Override
    public void start() {
        logger.info("start the kcf-index-update river");

        for(Tables table : Tables.values()){
            String name = table.name();
            Looker crrLooker = Lookers.getLooker(name, this.settingInfo.delay);

            logger.info("the {} looker fired", name);

            String threadPrefix = "kcf-index-update-river[" + name + "]";

            Thread crrThread = EsExecutors.daemonThreadFactory(
                 settings.globalSettings(),
                 threadPrefix
            ).newThread(crrLooker);

            this.cacheThread(threadPrefix, crrThread);

            crrThread.start();
        }
    }

    @Override
    public void close() {
        logger.info("close the kcf-index-update river");

        for(String threadName : this.threads.keySet()){
            logger.info("to stop the thread: {}", threadName);

            this.threads.remove(threadName).interrupt();
        }
    }

    /**
     * cache the thread
     * if there is a old thread instance in cache,
     * close it and add the new one
     *
     * @param key     thread name
     * @param thread  new thread instance
     */
    private void cacheThread(String key, Thread thread){
        Thread oldThread = this.threads.get(key);
        if(oldThread != null){
            oldThread.interrupt();
        }

        this.threads.put(key, thread);
    }

    private Map<String, Object> getSourceSettings(RiverSettings settings){
        Map<String, Object> sourceSettings = Maps.newHashMap();

        if(settings.settings().containsKey(RiverConfig.RIVER_TYPE)){
            sourceSettings = (Map<String, Object>)(
                    settings.settings().get(RiverConfig.RIVER_TYPE));
        }

        return sourceSettings;
    }

    private SettingInfo wrapSetting(Map<String, Object> settings) {
        String url  = XContentMapValues.nodeStringValue(
                settings.get("url"), RiverConfig.DEFAULT_DB_URL);
        String user = XContentMapValues.nodeStringValue(
                settings.get("user"), RiverConfig.DEFAULT_DB_USER);
        String pwd  = XContentMapValues.nodeStringValue(
                settings.get("password"), RiverConfig.DEFAULT_DB_PWD);
        long delay  = XContentMapValues.nodeLongValue(
                settings.get("delay"), RiverConfig.DEFAULT_DELAY);

        return new SettingInfo(url, user, pwd, delay);
    }

    /** custom settings */
    private class SettingInfo{
        private String url;
        private String user;
        private String pwd;
        private long   delay;

        private SettingInfo(String url, String user, String pwd, long delay) {
            this.url = url;
            this.user = user;
            this.pwd = pwd;
            this.delay = delay;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n=====================\n");
            sb.append("url:   ").append(this.url).append("\n");
            sb.append("user:  ").append(this.user).append("\n");
            sb.append("pwd:   ").append(this.pwd).append("\n");
            sb.append("delay: ").append(this.delay).append(" ms");
            sb.append("\n=====================\n");
            return sb.toString();
        }
    }
}
