package com.college.timetable.repository;

import com.college.timetable.entity.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Map;

@RepositoryRestResource
public interface  LectureRepository extends CrudRepository<LectureInfo, Integer> {
    public LectureInfo findByFirstNameAndLastName(String firstName, String lastName);

    @Query("Select l from LectureInfo l, LectureCapabilities lc Where l.lectureId = lc.compositeKey.lectureId and lc.compositeKey.subjectId = :subjectId and lc.compositeKey.gradeLevel = :gradeLevel")
    public List<LectureInfo> findLectureListByCapability(@Param("subjectId") Integer subjectId, @Param("gradeLevel") String gradeLevel);

}