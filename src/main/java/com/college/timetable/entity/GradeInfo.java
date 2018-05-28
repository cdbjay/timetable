package com.college.timetable.entity;

import javax.persistence.*;

@Entity
public class GradeInfo {
    @Id
    private String gradeLevel;
    private Integer gradeSeniority;

    public GradeInfo() {
    }

    public GradeInfo(String gradeLevel, Integer gradeSeniority) {
        this.gradeLevel = gradeLevel;
        this.gradeSeniority = gradeSeniority;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getGradeSeniority() {
        return gradeSeniority;
    }

    public void setGradeSeniority(Integer gradeSeniority) {
        this.gradeSeniority = gradeSeniority;
    }

    @Override
    public String toString() {
        return "GradeInfo{" +
                ", gradeLevel='" + gradeLevel + '\'' +
                ", gradeSeniority=" + gradeSeniority +
                '}';
    }
}
