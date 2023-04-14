package org.zhr.Service.Interface;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface NameCheck {
    String CheckTableName(Class<?> aclass) throws SQLException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
