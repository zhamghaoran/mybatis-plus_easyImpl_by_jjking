package org.zhr.Service.Interface;

import java.io.IOException;
import java.sql.SQLException;

public interface NameCheck {
    String CheckTableName(String name) throws SQLException, IOException;
}
