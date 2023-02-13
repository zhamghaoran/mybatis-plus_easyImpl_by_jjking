package org.zhr.Service.Interface;

import org.zhr.Service.ConditionBuilderImpl;

import java.lang.reflect.InvocationTargetException;

public interface ConditionBuilder<T> {
    <t, R> ConditionBuilderImpl<T> eq(SFunction<t, R> sFunction, String condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    <t, R> ConditionBuilderImpl<T> bt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    <t, R> ConditionBuilderImpl<T> lt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    <t, R> ConditionBuilderImpl<T> orderBy(SFunction<t, R> sFunction) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    <t, R> ConditionBuilderImpl<T> addCondition(SFunction<t, R> sFunction, String Condition, String funcionName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException;

}
