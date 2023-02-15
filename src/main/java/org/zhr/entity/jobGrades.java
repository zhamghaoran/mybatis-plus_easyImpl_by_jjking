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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof jobGrades jobGrades)) return false;

        if (getGradeLevel() != null ? !getGradeLevel().equals(jobGrades.getGradeLevel()) : jobGrades.getGradeLevel() != null)
            return false;
        if (getLowestSal() != null ? !getLowestSal().equals(jobGrades.getLowestSal()) : jobGrades.getLowestSal() != null)
            return false;
        return getHighestSal() != null ? getHighestSal().equals(jobGrades.getHighestSal()) : jobGrades.getHighestSal() == null;
    }

    @Override
    public int hashCode() {
        int result = getGradeLevel() != null ? getGradeLevel().hashCode() : 0;
        result = 31 * result + (getLowestSal() != null ? getLowestSal().hashCode() : 0);
        result = 31 * result + (getHighestSal() != null ? getHighestSal().hashCode() : 0);
        return result;
    }
}
