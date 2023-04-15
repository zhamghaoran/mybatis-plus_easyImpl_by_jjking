package org.zhr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zhr.annotation.Filed;
import org.zhr.annotation.Table;

import java.sql.Timestamp;

/**
 * @author 20179
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "user")
public class User {
    @Filed("id")
    private Long ID;
    private String username;
    private String phone;
    private String password;
    private Timestamp createTime;
    private Timestamp updateTime;

}
