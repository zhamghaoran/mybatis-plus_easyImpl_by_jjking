package org.zhr.Service;

import lombok.extern.java.Log;
import org.zhr.annotation.Filed;
import org.zhr.entity.Result;
import org.zhr.utils.ConnectionFactory;
import org.zhr.utils.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 20179
 */
@Log
public class SqlExecute<T> {
    private final Connection connection;
    private final SqlMakeFactoryImpl<T> sqlMakeFactory;
    private final NameCheckImpl nameCheck;
    private final Class<T> aClass;
    private final StringUtils stringUtils;
    private final CacheImpl cache;

    public SqlExecute(Class<T> aclass) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        this.connection = ConnectionFactory.getSqlConnection();
        this.sqlMakeFactory = new SqlMakeFactoryImpl<>();
        this.nameCheck = new NameCheckImpl();
        this.stringUtils = new StringUtils();
        this.aClass = aclass;
        this.cache = CacheImpl.getInstance();
    }

    private String insertSqlFactory(List<String> key, List<String> val, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" ");
        sql.append("( ");
        key.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");
        sql.append("values ( ");
        val.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ").append(";");
        log.info(sql.toString());
        return sql.toString();
    }

    private String deleteSqlFactory(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        Field[] declaredFields = aClass.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        String simpleName = stringUtils.smallHumpToUnderline(aClass.getSimpleName());
        stringBuilder.append("delete from ").append(simpleName).append(" where ");
        Arrays.stream(declaredFields).forEach(i -> {
            try {
                i.setAccessible(true);
                Object o = i.get(t);
                if (o != null) {
                    if (i.getType().equals(String.class)) {
                        o = "'" + o + "'";
                    }
                    stringBuilder.append(stringUtils.smallHumpToUnderline(i.getName())).append(" = ").append(o).append(" and ");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        stringBuilder.delete(stringBuilder.length() - 4, stringBuilder.length());
        return stringBuilder.toString();
    }
    public Result select(ConditionBuilderImpl<T> conditions) throws SQLException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        // 对表名进行校验
        String name = nameCheck.CheckTableName(aClass);
        // 执行查询操作
        return execute(conditions, name);

    }
    public Result execute(ConditionBuilderImpl<T> conditions, String name) throws NoSuchFieldException, SQLException {
        // 生成sql
        String sql = sqlMakeFactory.sqlMake(name,conditions);
        log.info("sql :   " + sql);
        // 缓存判断
        Result cache1 = cache.getCache(sql);
        if (cache1 != null) {
            return cache1;
        }
        // 执行sql
        return executeSql(sql);
    }

    private Result executeSql(String sql) throws SQLException {
        Statement statement = this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int column = metaData.getColumnCount();
        Result result = new Result(column, resultSet, metaData, sql);
        cache.addCache(sql, result);
        return result;
    }

    public T selectOne(ConditionBuilderImpl<T> conditions) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException, IOException {
        List<T> ts = selectList(conditions);
        if (ts.size() == 1) {
            return ts.get(0);
        } else {
            throw new SQLException("查询结果大于1");
        }
    }

    public List<T> selectList(ConditionBuilderImpl<T> conditions) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, NoSuchFieldException {
        Result select = select(conditions);
        ResultSet resultSet = select.getResultSet();
        return getFinalResult(resultSet);

    }

    public List<T> getFinalResult(ResultSet resultSet) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<T> result = new ArrayList<>();
        resultSet.beforeFirst();
        while (resultSet.next()) {
            T o = this.aClass.getDeclaredConstructor().newInstance();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field i : declaredFields) {
                i.setAccessible(true);
                Object object = resultSet.getObject(getRealFieldName(i));
                i.set(o, object);
            }
            result.add(o);
        }
        return result;
    }

    // 获取属性与列的对应关系
    private String getRealFieldName(Field i) {
        if (i.isAnnotationPresent(Filed.class)) {
            Filed annotation = i.getAnnotation(Filed.class);
            return annotation.value();
        } else {
            return StringUtils.smallHumpToUnderline(i.getName());
        }
    }

    public List<String> getFiledVal(Field[] fields, T t) {
        List<String> val = new ArrayList<>();
        Arrays.stream(fields).forEach(i -> {
            try {
                i.setAccessible(true);
                if (i.get(t) == null) {
                    val.add("null");
                } else {
                    if (!"java.lang.String".equals(i.getType().getTypeName())) {
                        val.add(i.get(t).toString());
                    } else {
                        val.add("'" + i.get(t).toString() + "'");
                    }
                }
            } catch (IllegalAccessException ignored) {
                val.add("null");
            }
        });
        return val;
    }

    public int insert(T t) throws SQLException {
        Field[] declaredFields = this.aClass.getDeclaredFields();
        List<String> name = new ArrayList<>();
        Arrays.stream(declaredFields).forEach((i) -> name.add(i.getName()));
        List<String> val = getFiledVal(aClass.getFields(), t);
        String s = insertSqlFactory(name, val, aClass.getSimpleName());
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        return preparedStatement.executeUpdate();
    }

    public Integer delete(T t) throws SQLException {
        String s = deleteSqlFactory(t);
        Statement statement = connection.createStatement();
        log.info("sql : " + s);
        return statement.executeUpdate(s);
    }
}
