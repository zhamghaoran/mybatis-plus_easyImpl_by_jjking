package org.zhr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.ProcessingInstruction;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @author 20179
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int colum;
    private ResultSet resultSet;
    private ResultSetMetaData resultSetMetaData;
    private String sql;
}
