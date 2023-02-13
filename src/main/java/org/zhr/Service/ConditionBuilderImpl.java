package org.zhr.Service;

import org.zhr.Service.Interface.ConditionBuilder;
import org.zhr.Service.Interface.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 20179
 */
public class ConditionBuilderImpl<T> implements ConditionBuilder<T> {
    private final Map<String, String> equalConditions;
    private final Map<String, String> btCondition;
    private final Map<String, String> ltCondition;

    private String orderByCondition;
    public Class<?> aClass;

    public static final String EQUAL_CONDITION = "where";
    public static final String BT_CONDITION = "bt";
    public static final String LT_CONDITION = "lt";
    public static final String ORDER_CONDITION = "orderby";

    public Map<String, Object> conditionMap;

    public Map<String, Object> getConditionMap() {
        conditionMap = new HashMap<>(5);
        if (equalConditions != null) {
            conditionMap.put(EQUAL_CONDITION, equalConditions);
        }
        if (btCondition != null) {
            conditionMap.put(BT_CONDITION, btCondition);
        }
        if (ltCondition != null) {
            conditionMap.put(LT_CONDITION, ltCondition);
        }
        if (orderByCondition != null) {
            conditionMap.put(ORDER_CONDITION, orderByCondition);
        }
        return conditionMap;
    }

    public ConditionBuilderImpl(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException {
        equalConditions = new HashMap<>();
        btCondition = new HashMap<>();
        ltCondition = new HashMap<>();
        try {
            T target = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> ConditionBuilderImpl<T> builder(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException {
        return new ConditionBuilderImpl<>(tClass);
    }

    public <t, R> ConditionBuilderImpl<T> eq(SFunction<t, R> sFunction, String condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, "'" + condition + "'", EQUAL_CONDITION);
    }

    public <t, R> ConditionBuilderImpl<T> bt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, condition.toString(), BT_CONDITION);
    }

    public <t, R> ConditionBuilderImpl<T> lt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, condition.toString(), LT_CONDITION);
    }

    public <t, R> ConditionBuilderImpl<T> orderBy(SFunction<t, R> sFunction) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, null, ORDER_CONDITION);
    }

    public <t, R> ConditionBuilderImpl<T> addCondition(SFunction<t, R> sFunction, String Condition, String funcionName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Method methods = sFunction.getClass().getDeclaredMethod("writeReplace");
        methods.setAccessible(true);
        //反射调用
        Object sl = methods.invoke(sFunction);
        SerializedLambda serializedLambda = (SerializedLambda) sl;
        // 获取类
        String implClass = serializedLambda.getImplClass();
        this.aClass = Class.forName(implClass.replace("/", "."));
        // 获取方法名
        String name = serializedLambda.getImplMethodName().substring(3);
        if (funcionName.equals(EQUAL_CONDITION)) {
            equalConditions.put(name, Condition);
        }
        if (funcionName.equals(BT_CONDITION)) {
            btCondition.put(name, Condition);
        }
        if (funcionName.equals(LT_CONDITION)) {
            ltCondition.put(name, Condition);
        }
        if (funcionName.equals(ORDER_CONDITION)) {
            orderByCondition = name;
        }
        return this;
    }

}
