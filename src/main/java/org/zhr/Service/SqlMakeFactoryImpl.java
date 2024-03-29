package org.zhr.Service;

import org.zhr.Service.Interface.SqlMakeFactory;
import org.zhr.annotation.Filed;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author 20179
 */
public class SqlMakeFactoryImpl<T> implements SqlMakeFactory {
    private final List<String> forList;
    private Boolean whereMark = false;
    private final static Map<String, String> FIELD_NAME = new HashMap<>();

    public SqlMakeFactoryImpl() {
        forList = new ArrayList<>();
        forList.add(ConditionBuilderImpl.EQUAL_CONDITION);
        forList.add(ConditionBuilderImpl.BT_CONDITION);
        forList.add(ConditionBuilderImpl.LT_CONDITION);
        forList.add(ConditionBuilderImpl.ORDER_CONDITION);
        forList.add(ConditionBuilderImpl.LIKE_CONDITION);
    }

    public String sqlMake(String name, ConditionBuilderImpl<T> conditionBuilder) throws NoSuchFieldException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select * from ").append(name).append(" ");
        if (conditionBuilder == null) {
            return stringBuilder.toString();
        }
        annotationScan(conditionBuilder.aClass);
        Map<String, Object> conditionMap = conditionBuilder.getConditionMap();
        Method[] methods = this.getClass().getMethods();
        forList.forEach(i -> {
            if (conditionMap.get(i) != null) {
                Arrays.stream(methods).forEach(j -> {
                    if (j.getName().equals(i)) {
                        try {
                            String ans = (String) j.invoke(this, conditionMap.get(i));
                            stringBuilder.append(ans);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        return stringBuilder.toString();
    }

    private void annotationScan(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        Arrays.stream(fields).toList().forEach(i -> {
            if (i.isAnnotationPresent(Filed.class)) {
                String value = i.getAnnotation(Filed.class).value();
                FIELD_NAME.put(i.getName(), value);
            } else {
                FIELD_NAME.put(i.getName(), i.getName());
            }
        });
    }

    @Override
    public String where(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" where ");
        whereMark = true;
        for (Map.Entry<String, String> i : map.entrySet()) {
            stringBuilder.append(FIELD_NAME.get(i.getKey())).append(" = ").append(i.getValue()).append(" and ");
        }
        stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        return stringBuilder.toString();
    }

    @Override
    public String lt(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (whereMark) {
            stringBuilder.append(" and ");
        } else {
            stringBuilder.append(" where ");
            whereMark = true;
        }
        for (Map.Entry<String, String> i : map.entrySet()) {
            stringBuilder.append(FIELD_NAME.get(i.getKey())).append(" < ").append(i.getValue()).append(" and ");
        }
        stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        return stringBuilder.toString();
    }

    @Override
    public String bt(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (whereMark) {
            stringBuilder.append(" and ");
        } else {
            stringBuilder.append(" where ");
            whereMark = true;
        }
        for (Map.Entry<String, String> i : map.entrySet()) {
            stringBuilder.append(FIELD_NAME.get(i.getKey())).append(" > ").append(i.getValue()).append(" and ");
        }
        stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        return stringBuilder.toString();
    }

    @Override
    public String like(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (whereMark) {
            stringBuilder.append(" and ");
        } else {
            stringBuilder.append(" where ");
            whereMark = true;
        }
        map.forEach((i, j) -> stringBuilder
                .append(FIELD_NAME.get(i))
                .append(" like ")
                .append("'%")
                .append(j)
                .append("%'")
                .append(" and "));
        stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        return stringBuilder.toString();
    }

    @Override
    public String orderby(String s) {
        return " order by " + s;
    }
}
