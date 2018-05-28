package com.college.timetable.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class SubjectInfo {

    @Id
    @GeneratedValue
    private Integer subjectId;

    @NotNull
    private String subjectName;

    public SubjectInfo(String subjectName) {
        this.subjectName = subjectName;
    }
    public SubjectInfo() {
    }

    @Override
    public String toString() {
        return "SubjectInfo{" +
                "subjectId=" + subjectId +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
