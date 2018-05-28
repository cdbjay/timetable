package com.college.timetable.service;

import com.college.timetable.entity.SubjectInfo;
import com.college.timetable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

    private TimeTableReportRepository timeTableReportRepository;
    private TimeTableTemplateRepository timeTableTemplateRepository;
    private LectureCapabilityRepository lectureCapabilityRepository;
    private LectureRepository lectureRepository;
    private ClassCapacityRepository classCapacityRepository;
    private ClassInfoRepository classInfoRepository;
    private SubjectRepository subjectRepository;
    private GradeInfoRepository gradeInfoRepository;

    @Autowired
    public RepositoryService(TimeTableReportRepository timeTableReportRepository, TimeTableTemplateRepository timeTableTemplateRepository, LectureCapabilityRepository lectureCapabilityRepository, LectureRepository lectureRepository, ClassCapacityRepository classCapacityRepository, ClassInfoRepository classInfoRepository, SubjectRepository subjectRepository, GradeInfoRepository gradeInfoRepository) {
        this.timeTableReportRepository = timeTableReportRepository;
        this.timeTableTemplateRepository = timeTableTemplateRepository;
        this.lectureCapabilityRepository = lectureCapabilityRepository;
        this.lectureRepository = lectureRepository;
        this.classCapacityRepository = classCapacityRepository;
        this.classInfoRepository = classInfoRepository;
        this.subjectRepository = subjectRepository;
        this.gradeInfoRepository = gradeInfoRepository;
    }

    public void cleanUp(){
        this.timeTableReportRepository.deleteAll();
        this.timeTableTemplateRepository.deleteAll();
        this.lectureCapabilityRepository.deleteAll();
        this.lectureRepository.deleteAll();
        this.classCapacityRepository.deleteAll();
        this.classInfoRepository.deleteAll();
        this.subjectRepository.deleteAll();
        this.gradeInfoRepository.deleteAll();
    }


    public boolean reportExists(){
        return timeTableReportRepository.findAll().iterator().hasNext();
    }

    public SubjectInfo saveAndGetIfNotFound(String subjectName){
        SubjectInfo subjectInfo = subjectRepository.findBySubjectName(subjectName);
        if(subjectInfo==null){
            subjectInfo = new SubjectInfo(subjectName.toUpperCase());
            subjectRepository.save(subjectInfo);
        }
        return subjectInfo;
    }
}
