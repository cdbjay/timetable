package com.college.timetable.model;

import org.springframework.stereotype.Component;

public class ReportModel {
    private Integer classId;
    private String gradeLevel;
    private String courseName;
    private String majorSubject;
    private String section;
    private Integer year;
    private String subjectName;
    private Integer day;
    private Integer hour;
    private Boolean isPractical;
    private Integer lectureId;
    private String firstName;
    private String lastName;

    public ReportModel() {
    }

    public ReportModel(Integer classId, String gradeLevel, String courseName, String majorSubject, String section, Integer year, String subjectName, Integer day, Integer hour, Boolean isPractical, Integer lectureId, String firstName, String lastName) {
        this.classId = classId;
        this.gradeLevel = gradeLevel;
        this.courseName = courseName;
        this.majorSubject = majorSubject;
        this.section = section;
        this.year = year;
        this.subjectName = subjectName;
        this.day = day;
        this.hour = hour;
        this.isPractical = isPractical;
        this.lectureId = lectureId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
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

    public String getSection() {return section;}

    public void setSection(String section) {this.section = section;}

    public Integer getYear() {return year;}

    public void setYear(Integer year) {this.year = year;}

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
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
}
