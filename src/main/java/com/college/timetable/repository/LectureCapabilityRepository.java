package com.college.timetable.repository;


import com.college.timetable.entity.EGradeLevel;
import com.college.timetable.entity.LectureCapabilities;
import com.college.timetable.entity.LectureCapabilityCompositeKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface  LectureCapabilityRepository extends CrudRepository<LectureCapabilities, LectureCapabilityCompositeKey> {

    @Query("from LectureCapabilities Where compositeKey.gradeLevel in :gradeLevelList")
    public List<LectureCapabilities> getLectureCapabilitiesByGrade(@Param("gradeLevelList") List<String> gradeLevelList);
}
