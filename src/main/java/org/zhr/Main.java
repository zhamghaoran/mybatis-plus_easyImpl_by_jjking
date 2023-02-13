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
        test();
    }
    public static void test() throws InvocationTargetException, NoSuchMethodException, SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ConditionBuilder<STUDENT> studentConditionBuilder = new ConditionBuilder<>(STUDENT.class);
        ConditionBuilder<STUDENT> lt = studentConditionBuilder
                .eq(STUDENT::getSSEX,"男")
                .bt(STUDENT::getSAGE, 19)
                .lt(STUDENT::getSAGE, 25)
                .orderBy(STUDENT::getSAGE);
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>();
        List<STUDENT> students = studentSqlExecute.selectList(lt);
        students.forEach(System.out::println);
    }
    public static void test1() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        STUDENT student = new STUDENT("123","456","男","10","11",12,"13","123@123");
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>();
        Integer insert = studentSqlExecute.insert(student);
        System.out.println(insert);
    }
}