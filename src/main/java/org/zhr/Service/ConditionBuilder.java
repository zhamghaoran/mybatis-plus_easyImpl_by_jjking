package org.zhr.Service;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConditionBuilder<T> {
    private Map<String, String> conditions;
    public  Class<?> aClass;


    public Map<String, String> getConditions() {
        return this.conditions;
    }

    public ConditionBuilder(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException {
        conditions = new HashMap<>();
        try {
            T target = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> ConditionBuilder<T> builder(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException {
        return new ConditionBuilder(tClass);
    }

    public <t, R> ConditionBuilder<T> where(SFunction<t, R> sFunction, String condition) throws InvocationTargetException {
        try {
            // 直接调用writeReplace
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
            conditions.put(name, condition);
            return this;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException |
                 ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 获取值
        return null;
    }

}
