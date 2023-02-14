## 简单介绍
实现了mybatis-plus的简单查询和插入
新增了属性名的自动对应。
把以小驼峰风格命名的java实体转化到以下划线命名的表
可以自动转化表名和实体名，列名和属性名
## 使用介绍
```java
public class test1{
    public void test() throws InvocationTargetException, NoSuchMethodException, SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ConditionBuilder<STUDENT> studentConditionBuilder = new ConditionBuilder<>(STUDENT.class);
        ConditionBuilder<STUDENT> lt = studentConditionBuilder
                .eq(STUDENT::getSSEX,"男")
                .bt(STUDENT::getSAGE, 19)
                .lt(STUDENT::getSAGE, 25);
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>();
        List<STUDENT> students = studentSqlExecute.selectList(lt);
        students.forEach(System.out::println);
    }
}
```
首先需要定义一个实体类来接受参数，然后根据eq，bt，lt等来设置条件即可。
插入
```java
class test {
    public static void test1() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        STUDENT student = new STUDENT("123","456","男","10","11",12,"13","123@123");
        SqlExecute<STUDENT> studentSqlExecute = new SqlExecute<>();
        Integer insert = studentSqlExecute.insert(student);
        System.out.println(insert);
    }
}
```
我们定义一个实体类对其进行赋值，然后插入到其中就可以了。