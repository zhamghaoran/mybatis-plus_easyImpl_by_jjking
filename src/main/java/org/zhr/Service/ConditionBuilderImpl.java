package org.zhr.Service;

import org.zhr.Service.Interface.ConditionBuilder;
import org.zhr.Service.Interface.SFunction;
import org.zhr.annotation.Filed;
import org.zhr.utils.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 20179
 */
public class ConditionBuilderImpl<T> implements ConditionBuilder<T> {
    private StringUtils stringUtils;
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

    private Map<String, String> attributeConversionMap(Map<String, String> conditionMap) throws NoSuchFieldException {
        for (Map.Entry<String, String> i : conditionMap.entrySet()) {
            Field field = this.aClass.getDeclaredField(i.getKey());
            String s;
            if (field.isAnnotationPresent(Filed.class)) {
                Filed annotation = field.getAnnotation(Filed.class);
                s = annotation.value();
            } else {
                s = stringUtils.smallHumpToUnderline(i.getKey());
            }
            String value = i.getValue();
            conditionMap.remove(i.getKey());
            conditionMap.put(s, value);
        }
        return conditionMap;
    }

    private String attributeConversionString(String condition) {
        return stringUtils.smallHumpToUnderline(condition);
    }

    public Map<String, Object> getConditionMap() throws NoSuchFieldException {
        conditionMap = new HashMap<>(5);

        if (equalConditions.size() > 0) {
            Map<String, String> stringStringMap = attributeConversionMap(equalConditions);
            conditionMap.put(EQUAL_CONDITION, stringStringMap);
        }
        if (btCondition.size() > 0) {
            Map<String, String> stringStringMap = attributeConversionMap(btCondition);
            conditionMap.put(BT_CONDITION, stringStringMap);
        }
        if (ltCondition.size() > 0) {
            Map<String, String> stringStringMap = attributeConversionMap(ltCondition);
            conditionMap.put(LT_CONDITION, stringStringMap);
        }
        if (orderByCondition != null) {
            String s = attributeConversionString(orderByCondition);
            conditionMap.put(ORDER_CONDITION, s);
        }
        return conditionMap;
    }

    public ConditionBuilderImpl() {
        equalConditions = new HashMap<>();
        btCondition = new HashMap<>();
        ltCondition = new HashMap<>();
        stringUtils = new StringUtils();
    }


    @Override
    public <t, R> ConditionBuilderImpl<T> eq(SFunction<t, R> sFunction, String condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, "'" + condition + "'", EQUAL_CONDITION);
    }

    public <t, R> ConditionBuilderImpl<T> eq(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, condition.toString(), EQUAL_CONDITION);
    }

    @Override
    public <t, R> ConditionBuilderImpl<T> bt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, condition.toString(), BT_CONDITION);
    }

    @Override
    public <t, R> ConditionBuilderImpl<T> lt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, condition.toString(), LT_CONDITION);
    }

    @Override
    public <t, R> ConditionBuilderImpl<T> orderBy(SFunction<t, R> sFunction) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCondition(sFunction, null, ORDER_CONDITION);
    }

    @Override
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
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
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
