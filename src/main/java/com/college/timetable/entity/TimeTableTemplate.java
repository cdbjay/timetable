package com.college.timetable.entity;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class TimeTableTemplate {

    @EmbeddedId
    private TimeTableCompositeKey timeTableCompositeKey;

    private Integer subjectId;

    private Boolean isPractical;

    public TimeTableTemplate() {
    }

    @Override
    public String toString() {
        return "TimeTableTemplate{" +
                "timeTableCompositeKey=" + timeTableCompositeKey +
                ", subjectId=" + subjectId +
                ", isPractical=" + isPractical +
                '}';
    }

    public TimeTableCompositeKey getTimeTableCompositeKey() {
        return timeTableCompositeKey;
    }

    public void setTimeTableCompositeKey(TimeTableCompositeKey timeTableCompositeKey) {
        this.timeTableCompositeKey = timeTableCompositeKey;
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

    public void setSubjectIdAndIsPractical(Integer subjectId, Boolean isPractical){
        setSubjectId(subjectId);
        setPractical(isPractical);
    }

    public TimeTableTemplate(TimeTableCompositeKey timeTableCompositeKey, Integer subjectId, Boolean isPractical) {
        this.timeTableCompositeKey = timeTableCompositeKey;
        this.subjectId = subjectId;
        this.isPractical = isPractical;
    }
}
