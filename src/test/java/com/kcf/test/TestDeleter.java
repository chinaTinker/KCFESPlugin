package com.kcf.test;

import org.elasticsearch.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;

/**
 * Author: 老牛 -- TK
 * Date:   14-4-2
 */
@RunWith(JUnit4.class)
public class TestDeleter {

    @Test
    public void doTest(){
        Collection<Long> ids = Lists.newArrayList();
        ids.add(1l);
        ids.add(2l);
        ids.add(3l);

        Collection<Long> checkIds = Lists.newArrayList();
        checkIds.add(1l);
        checkIds.add(2l);
        checkIds.add(4l);

        ids.removeAll(checkIds);

        assert !ids.contains(1l);
        assert ids.contains(3l);

        System.out.println(ids);
    }

}
