package com.college.timetable.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class LectureInfo {

    @Id
    @GeneratedValue
    private Integer lectureId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private Integer employeeId;
    private Integer capacity;
    private Boolean isActive;

    @Temporal(TemporalType.DATE)
    private Date beginEffectiveDate;

    @Temporal(TemporalType.DATE)
    private Date endEffectiveDate;

    public LectureInfo(@NotNull String firstName, @NotNull String lastName, Integer employeeId, Integer capacity, Boolean isActive, Date beginEffectiveDate, Date endEffectiveDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeId = employeeId;
        this.capacity = capacity;
        this.isActive = isActive;
        this.beginEffectiveDate = beginEffectiveDate;
        this.endEffectiveDate = endEffectiveDate;
    }

    public LectureInfo() {
    }

    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getBeginEffectiveDate() {
        return beginEffectiveDate;
    }

    public void setBeginEffectiveDate(Date beginEffectiveDate) {
        this.beginEffectiveDate = beginEffectiveDate;
    }

    public Date getEndEffectiveDate() {
        return endEffectiveDate;
    }

    public void setEndEffectiveDate(Date endEffectiveDate) {
        this.endEffectiveDate = endEffectiveDate;
    }

    @Override
    public String toString() {
        return "LectureInfo{" +
                "lectureId=" + lectureId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employeeId=" + employeeId +
                ", capacity=" + capacity +
                ", isActive=" + isActive +
                ", beginEffectiveDate=" + beginEffectiveDate +
                ", endEffectiveDate=" + endEffectiveDate +
                '}';
    }
}
