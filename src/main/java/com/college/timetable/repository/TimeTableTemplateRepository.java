package com.college.timetable.repository;

import com.college.timetable.entity.TimeTableCompositeKey;
import com.college.timetable.entity.TimeTableTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TimeTableTemplateRepository extends CrudRepository<TimeTableTemplate, Integer> {
    @Query("Select tt from TimeTableTemplate tt Where tt.timeTableCompositeKey.classId in :classIdList order by tt.timeTableCompositeKey.day, tt.timeTableCompositeKey.hour")
    public List<TimeTableTemplate> findByClassId(@Param("classIdList") List<Integer> classIdList);
}
