package org.zhr;

import org.zhr.Service.ConditionBuilder;
import org.zhr.Service.SqlExecute;
import org.zhr.entity.COURSE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        ConditionBuilder<COURSE> courseBuilder = new ConditionBuilder<>(COURSE.class);
        SqlExecute<COURSE> courseSql = new SqlExecute<>();
        List<COURSE> course = courseSql.selectList(courseBuilder.where(COURSE::getXKLB, "'必修'"));
        System.out.println(Arrays.toString(course.toArray()));
    }
}