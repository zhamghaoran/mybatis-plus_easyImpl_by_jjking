package org.zhr.Service.Interface;

import org.zhr.Service.ConditionBuilderImpl;
import org.zhr.entity.Result;

public interface Cache {
    Result getCache(String  sql);


    void addCache(String sql, Result result);
}
