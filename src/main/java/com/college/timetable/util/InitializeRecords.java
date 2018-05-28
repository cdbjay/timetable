package com.college.timetable.util;

import com.college.timetable.AppConstants;
import com.college.timetable.entity.*;
import com.college.timetable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class InitializeRecords implements CommandLineRunner {
    private SubjectRepository subjectRepository;
    private LectureRepository lectureRepository;
    private ClassInfoRepository classInfoRepository;
    private TimeTableTemplateRepository timeTableTemplateRepository;
    private LectureCapabilityRepository lectureCapabilityRepository;

    @Autowired
    public InitializeRecords(SubjectRepository subjectRepository, LectureRepository lectureRepository, ClassInfoRepository classInfoRepository,
                             TimeTableTemplateRepository timeTableTemplateRepository, LectureCapabilityRepository lectureCapabilityRepository) {
        this.subjectRepository = subjectRepository;
        this.lectureRepository = lectureRepository;
        this.classInfoRepository = classInfoRepository;
        this.timeTableTemplateRepository = timeTableTemplateRepository;
        this.lectureCapabilityRepository = lectureCapabilityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<SubjectInfo> subjectInfoList = Arrays.asList(
                new SubjectInfo(AppConstants.ESubjectNames.PHYSICS.toString()),
                new SubjectInfo(AppConstants.ESubjectNames.CHEMISTRY.toString()),
                new SubjectInfo(AppConstants.ESubjectNames.MATHS.toString())
        );

        subjectRepository.saveAll(subjectInfoList);

        lectureRepository.saveAll(Arrays.asList(
                new LectureInfo("Manoj", "Kumar", null,  10, Boolean.TRUE, Calendar.getInstance().getTime(), null),
                new LectureInfo("Jay", "Thangeswaran", null,  5, Boolean.TRUE, Calendar.getInstance().getTime(), null)
                //,new Lecture("Ramesh", "Kannan", null,  10, Boolean.TRUE, Calendar.getInstance().getTime(), null)
        ));


        subjectRepository.findAll().forEach(System.out::println);

        lectureRepository.findAll().forEach(System.out::println);

        classInfoRepository.saveAll(Arrays.asList(
                new ClassInfo("B.SC","MATHS","A", 1,"UG" ),
                new ClassInfo("M.SC","MATHS","A", 1,"PG" )
        ));

        timeTableTemplateRepository.saveAll(
                Arrays.asList(new TimeTableTemplate(
                        new TimeTableCompositeKey(classInfoRepository.findByCourseNameAndSectionAndGradeLevel("B.SC","A","UG" ).getClassId(), 1,12),
                        subjectRepository.findBySubjectName(AppConstants.ESubjectNames.PHYSICS.toString()).getSubjectId(), Boolean.FALSE)
                ));

        lectureCapabilityRepository.saveAll(
                Arrays.asList(
                        new LectureCapabilities(new LectureCapabilityCompositeKey(lectureRepository.findByFirstNameAndLastName("Manoj","Kumar").getLectureId(),
                                subjectRepository.findBySubjectName(AppConstants.ESubjectNames.PHYSICS.toString()).getSubjectId(),
                                "PG")))
        );

        timeTableTemplateRepository.findAll().forEach(System.out::println);

        lectureCapabilityRepository.findAll().forEach(System.out::println);
    }
}
