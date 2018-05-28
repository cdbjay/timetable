package com.college.timetable.repository;

import com.college.timetable.entity.GradeInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GradeInfoRepository extends CrudRepository<GradeInfo, String>{

    @Query("Select gi from GradeInfo gi Where exists (select 1 from ClassInfo ci Where ci.gradeLevel = gi.gradeLevel) order by gi.gradeSeniority desc")
    public List<GradeInfo> findAllByOrderByGradeSeniorityWhereClassFoundDesc();


    public List<GradeInfo> findAllByOrderByGradeSeniorityDesc();

    @Query("Select gi.gradeLevel from GradeInfo gi Where gradeSeniority >= :givenSeniority")
    public List<String> findEqualAndAboveGradeSeniority(@Param("givenSeniority") Integer givenSeniority);
}
