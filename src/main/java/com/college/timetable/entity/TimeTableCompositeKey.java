package com.college.timetable.entity;

import org.springframework.util.StringUtils;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@MappedSuperclass
public class TimeTableCompositeKey implements Serializable{
    @NotNull
    private Integer classId;

    @NotNull
    private Integer day;

    @NotNull
    private Integer hour;

    @Override
    public String toString() {
        return "TimeTableCompositeKey{" +
                "classId=" + classId +
                ", day=" + day +
                ", hour=" + hour +
                '}';
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public TimeTableCompositeKey() {

    }

    public TimeTableCompositeKey(@NotNull Integer classId, @NotNull Integer day, @NotNull Integer hour) {
        this.classId = classId;
        this.day = day;
        this.hour = hour;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if(o!=null && o instanceof  TimeTableCompositeKey){
            TimeTableCompositeKey compositeKey = (TimeTableCompositeKey) o;
            if(compositeKey.getClassId() == this.getClassId() &&
                    compositeKey.getDay() == this.getDay() &&
                    compositeKey.getHour() == this.getHour()){
                return true;
            }
        }
        return false;
    }


}
