package com.college.timetable.entity;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class TimeTableReport {

    @EmbeddedId
    private TimeTableReportCompositeKey timeTableReportCompositeKey;

    private Integer subjectId;

    private Boolean isPractical;

    private Integer lectureId;

    public TimeTableReport(TimeTableReportCompositeKey timeTableReportCompositeKey, Integer subjectId, Boolean isPractical, Integer lectureId) {
        this.timeTableReportCompositeKey = timeTableReportCompositeKey;
        this.subjectId = subjectId;
        this.isPractical = isPractical;
        this.lectureId = lectureId;
    }

    @Override
    public String toString() {
        return "TimeTableReport{" +
                "timeTableReportCompositeKey=" + timeTableReportCompositeKey +
                ", subjectId=" + subjectId +
                ", isPractical=" + isPractical +
                ", lectureId=" + lectureId +
                '}';
    }

    public TimeTableReportCompositeKey getTimeTableReportCompositeKey() {
        return timeTableReportCompositeKey;
    }

    public void setTimeTableReportCompositeKey(TimeTableReportCompositeKey timeTableReportCompositeKey) {
        this.timeTableReportCompositeKey = timeTableReportCompositeKey;
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

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public TimeTableReport() {

    }
}
