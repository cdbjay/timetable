package com.college.timetable.repository;

import com.college.timetable.entity.ClassCapacity;
import com.college.timetable.entity.ClassCapacityCompositeKey;
import com.college.timetable.entity.ClassInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClassCapacityRepository extends CrudRepository<ClassCapacity, ClassCapacityCompositeKey> {

    @Query("Select cc from ClassCapacity cc, ClassInfo ci where ci.classId = cc.compositeKey.classId and ci.gradeLevel = :gradeLevel order by cc.compositeKey.classId, cc.compositeKey.isPractical")
    public List<ClassCapacity> findClassCapacityByGradeLevel(@Param("gradeLevel") String gradeLevel);
}
