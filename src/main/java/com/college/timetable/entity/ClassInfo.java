package com.college.timetable.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class ClassInfo {
    @Id
    @GeneratedValue
    private Integer classId;
    private String courseName;
    private String majorSubject;
    private String section;
    private Integer year;

    @NotNull
    private String gradeLevel;

    public ClassInfo() {
    }

    public ClassInfo(String courseName, String majorSubject, String section, Integer year, @NotNull String gradeLevel) {
        this.courseName = courseName;
        this.majorSubject = majorSubject;
        this.section = section;
        this.year = year;
        this.gradeLevel = gradeLevel;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getMajorSubject() {
        return majorSubject;
    }

    public void setMajorSubject(String majorSubject) {
        this.majorSubject = majorSubject;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "classId=" + classId +
                ", courseName='" + courseName + '\'' +
                ", majorSubject='" + majorSubject + '\'' +
                ", section='" + section + '\'' +
                ", year=" + year +
                ", gradeLevel=" + gradeLevel +
                '}';
    }
}
