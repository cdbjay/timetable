package com.college.timetable.entity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ClassCapacityCompositeKey implements Serializable {
    @NotNull
    private Integer classId;

    @NotNull
    private Integer subjectId;

    @NotNull
    private Boolean isPractical;

    public ClassCapacityCompositeKey(@NotNull Integer classId, @NotNull Integer subjectId, @NotNull Boolean isPractical) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.isPractical = isPractical;
    }

    public ClassCapacityCompositeKey() {
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Boolean getPractical() {
        return isPractical;
    }

    public void setPractical(Boolean practical) {
        isPractical = practical;
    }

    @Override
    public String toString() {
        return "ClassCapacityCompositeKey{" +
                "classId=" + classId +
                ", subjectId=" + subjectId +
                ", isPractical=" + isPractical +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o!=null && o instanceof ClassCapacityCompositeKey){
            ClassCapacityCompositeKey compositeKey = (ClassCapacityCompositeKey) o;
            if(compositeKey.getClassId() == this.getClassId() && compositeKey.getSubjectId() == this.getSubjectId()
                    && compositeKey.getPractical() == this.getPractical()){
                return true;
            }
        }
        return false;
    }
}
