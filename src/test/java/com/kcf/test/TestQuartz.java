package com.kcf.test;

import com.kcf.tasker.deleter.impl.DiscussDeleter;
import com.kcf.util.QuartzContext;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-18
 */
public class TestQuartz implements Job{

    public static void main(String[] args){
        QuartzContext.init(null, 10000);
        QuartzContext.fire(DiscussDeleter.class);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("test quartz");
    }
}
