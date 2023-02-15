package org.zhr.Service.Interface;

import org.zhr.entity.Result;

public interface Cache {
    Result getCache(ConditionBuilder conditionBuilder);

    void addCache(ConditionBuilder conditionBuilder,Result result);
}
