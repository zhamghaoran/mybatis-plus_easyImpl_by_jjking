package org.zhr;

import org.zhr.Service.ConditionBuilder;
import org.zhr.Service.SqlExecute;
import org.zhr.entity.STUDENT;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {
        ConditionBuilder<STUDENT> studentConditionBuilder = new ConditionBuilder<>(STUDENT.class);
        ConditionBuilder<STUDENT> lt = studentConditionBuilder.bt(STUDENT::getSAGE, 19).lt(STUDENT::getSAGE, 25);
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>();
        List<STUDENT> students = studentSqlExecute.selectList(lt);
        students.forEach(System.out::println);
    }
}