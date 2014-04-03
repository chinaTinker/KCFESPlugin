package com.kcf.util;

import org.elasticsearch.common.collect.Maps;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.EsExecutors;

import java.util.Map;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-2
 */
public class ThreadContext {
    private static final Map<String, Thread> runningThreads = Maps.newHashMap();


    public static void run(Runnable task, String name, Settings settings){
        Thread crrThread = EsExecutors.daemonThreadFactory(
                settings,
                name
        ).newThread(task);

        cacheThread(name, crrThread);

        crrThread.start();
    }

    public static void close(){
        for(String threadName : runningThreads.keySet()){
            try {
                runningThreads.remove(threadName).interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void cacheThread(String name, Thread thread){
        if(runningThreads.containsKey(name)){
            runningThreads.get(name).interrupt();
        }

        runningThreads.put(name, thread);
    }

}
