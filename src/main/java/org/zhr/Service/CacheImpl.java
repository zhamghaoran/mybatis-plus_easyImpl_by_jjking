package org.zhr.Service;

import lombok.extern.slf4j.Slf4j;
import org.zhr.Service.Interface.Cache;
import org.zhr.Service.Interface.ConditionBuilder;
import org.zhr.entity.Result;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class CacheImpl implements Cache {
    ConcurrentMap<ConditionBuilder,Result> concurrentMap;
    static CacheImpl cache;
    public CacheImpl () {
        concurrentMap = new ConcurrentHashMap<>();
    }
    public static CacheImpl getInstance() {
        if (cache == null ) {
            cache = new CacheImpl();
        }
        return cache;
    }
    @Override
    public Result getCache(ConditionBuilder conditionBuilder) {
        Result result = concurrentMap.get(conditionBuilder);
        if (result != null) {
            log.info("缓存命中 sql :" + result.getSql());
            return result;
        } else return null;

    }

    @Override
    public void addCache(ConditionBuilder conditionBuilder, Result result) {
        concurrentMap.put(conditionBuilder,result);
    }

}
