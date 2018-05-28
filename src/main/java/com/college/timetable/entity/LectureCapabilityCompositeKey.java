package com.college.timetable.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LectureCapabilityCompositeKey implements Serializable {

    @NotNull
    private Integer lectureId;

    @NotNull
    private String gradeLevel;

    @NotNull
    private Integer subjectId;

    public LectureCapabilityCompositeKey() {
    }

    @Override
    public String toString() {
        return "LectureCapabilityCompositeKey{" +
                "lectureId=" + lectureId +
                ", gradeLevel=" + gradeLevel +
                ", subjectId=" + subjectId +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o!=null && o instanceof LectureCapabilityCompositeKey){
            LectureCapabilityCompositeKey lectureCapabilityCompositeKey = (LectureCapabilityCompositeKey) o;
            return lectureCapabilityCompositeKey.getGradeLevel()!=null && lectureCapabilityCompositeKey.getGradeLevel().equalsIgnoreCase(this.getGradeLevel())
                    && lectureCapabilityCompositeKey.getLectureId() == this.getLectureId()
                    && lectureCapabilityCompositeKey.getSubjectId() == this.getSubjectId();
        }
        return false;
    }



    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public LectureCapabilityCompositeKey(@NotNull Integer lectureId, @NotNull Integer subjectId, @NotNull String gradeLevel) {

        this.lectureId = lectureId;
        this.subjectId = subjectId;
        this.gradeLevel = gradeLevel;
    }


}
