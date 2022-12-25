package org.zhr.Service;

import org.zhr.Service.Interface.SFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ConditionBuilder<T> {
    private Map<String, String> Equalconditions;
    private Map<String, String> btCondition;
    private Map<String,String> ltCondition;
    public Class<?> aClass;
    public static final String EQUAL_CONDITION = "where";
    public static final String BT_CONDITION = "bt";
    public static final String LT_CONDITION = "lt";

    public Map<String, String> getEqualCondition() {
        return this.Equalconditions;
    }
    public Map<String,String> getBtCondition() {return this.btCondition;}
    public Map<String,String> getLtCondition() {return  this.ltCondition;}

    public ConditionBuilder(Class<T> tClass) throws NoSuchMethodException, InvocationTargetException {
        Equalconditions = new HashMap<>();
        btCondition = new HashMap<>();
        ltCondition = new HashMap<>();
        try {
            T target = tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> ConditionBuilder<T> builder(Class<T> tClass) throws InvocationTargetException, NoSuchMethodException {
        return new ConditionBuilder(tClass);
    }

    public <t, R> ConditionBuilder<T> eq(SFunction<t, R> sFunction, String condition) throws InvocationTargetException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException {
        return addCpndition(sFunction,"'" + condition + "'",EQUAL_CONDITION);
    }

    public <t, R> ConditionBuilder<T> bt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCpndition(sFunction,condition.toString(),BT_CONDITION);
    }
    public <t, R> ConditionBuilder<T> lt(SFunction<t, R> sFunction, Integer condition) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return addCpndition(sFunction,condition.toString(),LT_CONDITION);
    }

    public <t, R> ConditionBuilder<T> addCpndition(SFunction<t, R> sFunction, String Condition, String funcionName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
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
            Equalconditions.put(name,Condition);
        }
        if (funcionName.equals(BT_CONDITION)) {
            btCondition.put(name,Condition);
        }
        if (funcionName.equals(LT_CONDITION)) {
            ltCondition.put(name,Condition);
        }
        return this;
    }

}
