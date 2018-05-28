package com.college.timetable.entity;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TimeTableReportCompositeKey extends  TimeTableCompositeKey implements Serializable{

    @NotNull
    @Column(columnDefinition = "default 1")
    private Boolean isGlobal;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date overrideDate;

    public TimeTableReportCompositeKey(){
        super();
    }

    public TimeTableReportCompositeKey(@NotNull Integer classId, @NotNull Integer day, @NotNull Integer hour, @NotNull Boolean isGlobal, @NotNull Date overrideDate) {
        super(classId, day, hour);
        this.isGlobal = isGlobal;
        this.overrideDate = overrideDate;
    }

    public TimeTableReportCompositeKey(@NotNull TimeTableCompositeKey timeTableCompositeKey, @NotNull Boolean isGlobal, @NotNull Date overrideDate) {
        this(timeTableCompositeKey.getClassId(), timeTableCompositeKey.getDay(), timeTableCompositeKey.getHour(), isGlobal, overrideDate);
    }

    @Override
    public boolean equals(Object o) {
        if(o!=null && o instanceof TimeTableReportCompositeKey && super.equals(o)){
            TimeTableReportCompositeKey compositeKey = (TimeTableReportCompositeKey) o;
            if(compositeKey.getGlobal() == this.getGlobal() &&
                    isOverrideDateEquals(compositeKey.getOverrideDate())){
                return true;
            }
        }
        return false;
    }

    private boolean isOverrideDateEquals(Date compareDate){
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.setTime(compareDate);
        Calendar thisCalender = Calendar.getInstance();
        thisCalender.setTime(getOverrideDate());

        return compareCalendar.get(Calendar.YEAR) == thisCalender.get(Calendar.YEAR)
                && compareCalendar.get(Calendar.MONTH) == thisCalender.get(Calendar.MONTH)
                && compareCalendar.get(Calendar.DATE) == thisCalender.get(Calendar.DATE);
    }

    public Boolean getGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    public Date getOverrideDate() {
        return overrideDate;
    }

    public void setOverrideDate(Date overrideDate) {
        this.overrideDate = overrideDate;
    }
}
