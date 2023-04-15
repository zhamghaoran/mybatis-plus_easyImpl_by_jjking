package org.zhr;

import org.zhr.Service.ConditionBuilderImpl;
import org.zhr.Service.SqlExecute;
import org.zhr.entity.STUDENT;
import org.zhr.entity.User;
import org.zhr.entity.jobGrades;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

/**
 * @author 20179
 */
public class Main {
    public static void main(String[] args) {
        try {
            test4();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test() throws InvocationTargetException, NoSuchMethodException, SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        ConditionBuilderImpl<STUDENT> studentConditionBuilder = new ConditionBuilderImpl<>();
        studentConditionBuilder
                .eq(STUDENT::getSSEX, "男")
                .bt(STUDENT::getSAGE, 19)
                .lt(STUDENT::getSAGE, 25)
                .orderBy(STUDENT::getSAGE);
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>(STUDENT.class);
        List<STUDENT> students = studentSqlExecute.selectList(studentConditionBuilder);
        students.forEach(System.out::println);
    }

    public static void test1() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        STUDENT student = new STUDENT("123", "456", "男", "10", "11", 12, "13", "123@123");
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>(STUDENT.class);
        Integer insert = studentSqlExecute.insert(student);
        System.out.println(insert);
    }

    public static void test2() throws SQLException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        ConditionBuilderImpl<jobGrades> jobGradesConditionBuilder = new ConditionBuilderImpl<>();
        SqlExecute<jobGrades> jobGradesSqlExecute = new SqlExecute<>(jobGrades.class);
        jobGradesConditionBuilder
                .bt(jobGrades::getLowestSal, 3000)
                .eq(jobGrades::getHighestSal, 14999);
        List<jobGrades> jobGrade = jobGradesSqlExecute.selectList(jobGradesConditionBuilder);
        Iterator<jobGrades> iterator = jobGrade.iterator();
        for (int i = 0; !jobGrade.isEmpty(); i++) {
            jobGrade.remove(jobGrade.get(i));
        }
        ConditionBuilderImpl<jobGrades> jobGradesConditionBuilder1 = new ConditionBuilderImpl<>();
        SqlExecute<jobGrades> jobGradesSqlExecute1 = new SqlExecute<>(jobGrades.class);
        jobGradesConditionBuilder1
                .bt(jobGrades::getLowestSal, 3000)
                .eq(jobGrades::getHighestSal, 14999);
        List<jobGrades> jobGrades1 = jobGradesSqlExecute1.selectList(jobGradesConditionBuilder1);
        jobGrades1.forEach(System.out::println);
    }

    public static void test3() throws SQLException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        jobGrades jobGrades = new jobGrades();
        jobGrades.setGradeLevel("A");
        jobGrades.setLowestSal(100000);
        SqlExecute<org.zhr.entity.jobGrades> jobGradesSqlExecute = new SqlExecute<>(org.zhr.entity.jobGrades.class);
        Integer delete = jobGradesSqlExecute.delete(jobGrades);
        System.out.println(delete);
    }

    public static void test4() throws SQLException, IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        SqlExecute<User> userSqlExecute = new SqlExecute<>(User.class);
        ConditionBuilderImpl<User> userConditionBuilder = new ConditionBuilderImpl<>();
        userConditionBuilder.like(User::getUsername, "1").like(User::getPhone, "1");
        List<User> users = userSqlExecute.selectList(userConditionBuilder);
        users.forEach(System.out::println);
    }
}