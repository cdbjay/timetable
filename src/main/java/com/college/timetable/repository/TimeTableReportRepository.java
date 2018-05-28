package com.college.timetable.repository;

import com.college.timetable.entity.TimeTableReport;
import com.college.timetable.entity.TimeTableTemplate;
import com.college.timetable.model.ReportModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TimeTableReportRepository extends CrudRepository<TimeTableReport, Integer> {

    @Query("select new com.college.timetable.model.ReportModel(ttr.timeTableReportCompositeKey.classId, ci.gradeLevel, ci.courseName, ci.majorSubject, ci.section, ci.year, si.subjectName, ttr.timeTableReportCompositeKey.day, ttr.timeTableReportCompositeKey.hour, ttr.isPractical, ttr.lectureId, li.firstName, li.lastName)  " +
            "From TimeTableReport ttr JOIN ClassInfo ci ON ci.classId=ttr.timeTableReportCompositeKey.classId " +
            "JOIN SubjectInfo si ON si.subjectId=ttr.subjectId " +
            "JOIN ClassInfo ci ON ci.classId=ttr.timeTableReportCompositeKey.classId " +
            "LEFT OUTER JOIN LectureInfo li ON ttr.lectureId = li.lectureId " +
            "order by ci.classId, day, hour")
    public List<ReportModel> pullReport();
}
