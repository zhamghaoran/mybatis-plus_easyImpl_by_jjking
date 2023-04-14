package org.zhr.annotation;

import jdk.jfr.Percentage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 20179
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OpLog {
    /**
     *  业务类型
     *
     */
    public String opType();

    /**
     *  业务对象名称，订单，库存，价格
     *
     */
    public String opItem();

    /**
     * 业务对象编号表达式，描述了如何获取订单号
     *
     */
    public String opItemIdExpression() ;
}
