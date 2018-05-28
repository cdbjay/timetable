package com.college.timetable.repository;


import com.college.timetable.entity.SubjectInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SubjectRepository extends CrudRepository<SubjectInfo, Integer> {
    public SubjectInfo findBySubjectName(String subjectName);
}