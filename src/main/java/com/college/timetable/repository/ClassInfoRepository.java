package com.college.timetable.repository;

import com.college.timetable.entity.ClassInfo;
import com.college.timetable.entity.EGradeLevel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface  ClassInfoRepository extends CrudRepository<ClassInfo, Long> {
    public ClassInfo findByCourseNameAndSectionAndGradeLevel(String courseName, String section, String gradeLevel);

    @Query("Select ci.classId from ClassInfo ci Where ci.gradeLevel = :gradeLevel")
    public List<Integer> findByGradeLevel(@Param("gradeLevel") String gradeLevel);
}
