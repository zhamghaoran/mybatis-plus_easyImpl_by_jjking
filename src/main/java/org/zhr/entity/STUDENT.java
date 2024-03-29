package org.zhr.entity;

import com.mysql.cj.protocol.PacketReceivedTimeHolder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class STUDENT implements Serializable {
    private String SNO;
    private String SNAME;
    private String SSEX;
    private String SMAJOR;
    private String SDEPT;
    private Integer SAGE;
    private String TEL;
    private String EMAIL;
}
