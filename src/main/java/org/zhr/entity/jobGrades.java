package org.zhr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class jobGrades {
    private String gradeLevel;
    private Integer lowestSal;
    private Integer highestSal;
}
