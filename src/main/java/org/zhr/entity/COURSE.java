package org.zhr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class COURSE {
    private String CNO;
    private String CNAME;
    private Integer CCREDIT;
    private String XKLB;
}
