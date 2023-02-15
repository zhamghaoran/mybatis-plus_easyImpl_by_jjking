package org.zhr;

import org.zhr.Service.ConditionBuilderImpl;
import org.zhr.Service.SqlExecute;
import org.zhr.entity.STUDENT;
import org.zhr.entity.jobGrades;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, NoSuchMethodException {

        ConditionBuilderImpl<jobGrades> jobGradesConditionBuilder = new ConditionBuilderImpl<>();
        SqlExecute<jobGrades> jobGradesSqlExecute = new SqlExecute<>(jobGrades.class);
        jobGradesConditionBuilder
                .bt(jobGrades::getLowestSal,3000)
                .eq(jobGrades::getHighestSal,14999);
        List<jobGrades> jobGrade = jobGradesSqlExecute.selectList(jobGradesConditionBuilder);


        ConditionBuilderImpl<jobGrades> jobGradesConditionBuilder1 = new ConditionBuilderImpl<>();
        SqlExecute<jobGrades> jobGradesSqlExecute1 = new SqlExecute<>(jobGrades.class);
        jobGradesConditionBuilder1
                .bt(jobGrades::getLowestSal,3000)
                .eq(jobGrades::getHighestSal,14999);
        List<jobGrades> jobGrades1 = jobGradesSqlExecute1.selectList(jobGradesConditionBuilder);

    }

    public static void test() throws InvocationTargetException, NoSuchMethodException, SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ConditionBuilderImpl<STUDENT> studentConditionBuilder = new ConditionBuilderImpl<>();
        ConditionBuilderImpl<STUDENT> lt = studentConditionBuilder
                .eq(STUDENT::getSSEX,"男")
                .bt(STUDENT::getSAGE, 19)
                .lt(STUDENT::getSAGE, 25)
                .orderBy(STUDENT::getSAGE);
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>(STUDENT.class);
        List<STUDENT> students = studentSqlExecute.selectList(lt);
        students.forEach(System.out::println);
    }

    public static void test1() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        STUDENT student = new STUDENT("123","456","男","10","11",12,"13","123@123");
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>(STUDENT.class);
        Integer insert = studentSqlExecute.insert(student);
        System.out.println(insert);
    }
}