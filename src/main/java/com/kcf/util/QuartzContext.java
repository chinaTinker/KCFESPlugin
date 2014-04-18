package com.kcf.util;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-17
 *
 * quartz job running context,
 * fire a job or stop the scheduling
 */
public class QuartzContext {
    private static final ESLogger logger = ESLoggerFactory.getLogger("QuartzContext");

    private static Scheduler scheduler;
    private static long delay = 10000l;
    private static Client client;

    public static void init(Client client, long delay){
        try {
            setClient(client);
            setDelay(delay);

            SchedulerFactory factory = new StdSchedulerFactory();
            scheduler = factory.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error("init scheduler failed", e);
        }
    }

    public static void fire(Class<? extends Job> clazz) {
        try {
            String name = clazz.getName();

            JobDataMap data = new JobDataMap();
            data.put("client", client);

            JobDetail job = newJob(clazz)
                    .withIdentity("job - " + name, "kcf")
                    .setJobData(data)
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity("trigger - " + name, "kcf")
                    .startNow()
                    .withSchedule(
                            simpleSchedule()
                                    .withIntervalInMilliseconds(delay)
                                    .repeatForever()
                    )
                    .build();

            scheduler.scheduleJob(job, trigger);

            logger.info("job[{}] has been fired", name);
        } catch (SchedulerException e) {
            logger.error("fire job failed", e);
        }
    }

    public static void shutdown(){
        try {
            scheduler.shutdown();

            logger.info("river job shutdown ...");
        } catch (SchedulerException e) {
            logger.error("shutdown scheduler failed", e);
        }
    }

    public static void setDelay(long delay) {
        QuartzContext.delay = delay;
    }

    public static void setClient(Client client) {
        QuartzContext.client = client;
    }

}
