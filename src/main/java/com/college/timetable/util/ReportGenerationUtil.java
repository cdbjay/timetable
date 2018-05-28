package com.college.timetable.util;

import com.college.timetable.entity.*;
import com.college.timetable.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.spi.CalendarNameProvider;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class ReportGenerationUtil {
    private TimeTableTemplateRepository timeTableTemplateRepository;
    private TimeTableReportRepository timeTableReportRepository;
    private LectureCapabilityRepository lectureCapabilityRepository;
    private LectureRepository lectureRepository;
    private ClassInfoRepository classInfoRepository;
    private SubjectRepository subjectRepository;
    private GradeInfoRepository gradeInfoRepository;

    @Autowired
    public ReportGenerationUtil(TimeTableTemplateRepository timeTableTemplateRepository, TimeTableReportRepository timeTableReportRepository,
                                ClassInfoRepository classInfoRepository, SubjectRepository subjectRepository, LectureRepository lectureRepository,
                                LectureCapabilityRepository lectureCapabilityRepository, GradeInfoRepository gradeInfoRepository) {
        this.timeTableTemplateRepository = timeTableTemplateRepository;
        this.timeTableReportRepository = timeTableReportRepository;
        this.classInfoRepository = classInfoRepository;
        this.subjectRepository = subjectRepository;
        this.lectureRepository = lectureRepository;
        this.lectureCapabilityRepository = lectureCapabilityRepository;
        this.gradeInfoRepository = gradeInfoRepository;
    }

    public void getTimeTableByMinGrade(){

        Map<Integer, Integer> lectureCapacityMap = StreamSupport.stream(lectureRepository.findAll().spliterator(), false).collect(Collectors.toMap(x -> x.getLectureId(), x -> x.getCapacity()));
        Map<String, Set<Integer>> slotRegistryMap = new HashMap<>();
        RoundRobinHelper roundRobinHelper = new RoundRobinHelper();
        for (GradeInfo gradeInfo : gradeInfoRepository.findAllByOrderByGradeSeniorityDesc()) {
            List<Integer> classIdList = classInfoRepository.findByGradeLevel(gradeInfo.getGradeLevel());
            List<LectureCapabilities> lectureCapabilitiesByGradeLevel = lectureCapabilityRepository.getLectureCapabilitiesByGrade(gradeInfoRepository.findEqualAndAboveGradeSeniority(gradeInfo.getGradeSeniority()));
            List<TimeTableTemplate> timeTableTemplateList = timeTableTemplateRepository.findByClassId(classIdList);

            //List<TimeTableTemplate> timeTableTemplatesBySubjectId = timeTableTemplateList.stream().filter(template -> template.getSubjectId() == subjectInfo.getSubjectId()).collect(Collectors.toList());
            generateReportByTemplate(timeTableTemplateList, lectureCapabilitiesByGradeLevel, lectureCapacityMap, slotRegistryMap, roundRobinHelper);
        }
    }

    private void generateReportByTemplate_v1(List<TimeTableTemplate> timeTableTemplateListBySubjectId, List<LectureCapabilities> lectureCapabilitiesByGradeLevel,
                                          Map<Integer, Integer> lectureCapacityMap, Map<String, Set<Integer>> slotRegistryMap) {
        timeTableTemplateListBySubjectId.forEach(System.out::println);
        lectureCapabilitiesByGradeLevel.forEach(System.out::println);
        List<TimeTableReport> timeTableReportList = new ArrayList<>();
        timeTableTemplateListBySubjectId.stream()
                .forEach(timeTableTemplate -> {
                    String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+Boolean.TRUE.toString()+timeTableTemplate.getTimeTableCompositeKey().getHour();
                    TimeTableReport timeTableReport = lectureCapabilitiesByGradeLevel
                                                        .stream()
                                                        .filter(capability -> capability.getCompositeKey().getSubjectId() == timeTableTemplate.getSubjectId()
                                                                && (slotRegistryMap.get(slotRegistryKey)==null || !slotRegistryMap.get(slotRegistryKey).contains(capability.getCompositeKey().getLectureId())))
                                                        .filter(capability -> (lectureCapacityMap.get(capability.getCompositeKey().getLectureId())!=null && lectureCapacityMap.get(capability.getCompositeKey().getLectureId())>0))
                                                        .findAny()
                                                        .map(capability -> generateTimeTableEntry(lectureCapacityMap, capability.getCompositeKey().getLectureId(), timeTableTemplate, slotRegistryMap))
                                                        .orElse(copyTimeTableTemplate(timeTableTemplate, null));
                    timeTableReportList.add(timeTableReport);
                    //slotRegistryMap.forEach((k,v) -> System.out.println(k+"-"+v));
                });
        timeTableReportList.forEach(System.out::println);
        timeTableReportRepository.saveAll(timeTableReportList);
       /* timeTableTemplateListBySubjectId.stream()
                .map(template -> {
                    EGradeLevel eGradeLevel = classInfoRepository.findById(template.getTimeTableCompositeKey().getClassId()).get().getGradeLevel();
                    return lectureRepository.findByCapability(template.getSubjectId(), eGradeLevel);
                }).forEach(System.out::println);*/
    }

    private void generateReportByTemplate(List<TimeTableTemplate> timeTableTemplateList, List<LectureCapabilities> lectureCapabilitiesByGradeLevel,
                                          Map<Integer, Integer> lectureCapacityMap, Map<String, Set<Integer>> slotRegistryMap, RoundRobinHelper roundRobinHelper) {
        timeTableTemplateList.forEach(System.out::println);
        timeTableTemplateList.forEach(System.out::println);

        List<TimeTableReport> timeTableReportList = new ArrayList<>();
        timeTableTemplateList.stream()
                .forEach(timeTableTemplate -> {
                    String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+Boolean.TRUE.toString()+timeTableTemplate.getTimeTableCompositeKey().getHour();
                    List<Integer> qualifiedLectureIdList = lectureCapabilitiesByGradeLevel
                                                            .stream()
                                                            .filter(capability -> capability.getCompositeKey().getSubjectId() == timeTableTemplate.getSubjectId()
                                                                    && (slotRegistryMap.get(slotRegistryKey) == null || !slotRegistryMap.get(slotRegistryKey).contains(capability.getCompositeKey().getLectureId())))
                                                            .filter(capability -> (lectureCapacityMap.get(capability.getCompositeKey().getLectureId()) != null && lectureCapacityMap.get(capability.getCompositeKey().getLectureId()) > 0))
                                                            .map(capability -> capability.getCompositeKey().getLectureId())
                                                            .collect(Collectors.toList());

                    Integer assignLectureId = roundRobinHelper.pickLecture(qualifiedLectureIdList);
                    timeTableReportList.add(generateReportEntry(lectureCapacityMap, assignLectureId, timeTableTemplate, slotRegistryMap));
                });
        timeTableReportList.forEach(System.out::println);
        timeTableReportRepository.saveAll(timeTableReportList);
    }

    private TimeTableReport generateTimeTableEntry(Map<Integer, Integer> capacityMap, Integer lectureId, TimeTableTemplate timeTableTemplate, Map<String, Set<Integer>> slotRegistryMap){
        if(lectureId!=null && capacityMap!=null && capacityMap.get(lectureId)!=null){
            capacityMap.put(lectureId, capacityMap.get(lectureId)-1);
            String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+Boolean.TRUE.toString()+timeTableTemplate.getTimeTableCompositeKey().getHour();
            slotRegistryMap.computeIfAbsent(slotRegistryKey, v-> new HashSet<>()).add(lectureId);
            return copyTimeTableTemplate(timeTableTemplate, lectureId);
        }
        return null;
    }

    private TimeTableReport generateReportEntry(Map<Integer, Integer> capacityMap, Integer lectureId, TimeTableTemplate timeTableTemplate, Map<String, Set<Integer>> slotRegistryMap){
        if(lectureId!=null && capacityMap!=null && capacityMap.get(lectureId)!=null){
            capacityMap.put(lectureId, capacityMap.get(lectureId)-1);
            String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+Boolean.TRUE.toString()+timeTableTemplate.getTimeTableCompositeKey().getHour();
            slotRegistryMap.computeIfAbsent(slotRegistryKey, v-> new HashSet<>()).add(lectureId);
        }
        return copyTimeTableTemplate(timeTableTemplate, lectureId);
    }



    private TimeTableReport copyTimeTableTemplate(TimeTableTemplate timeTableTemplate, Integer lectureId){
        return new TimeTableReport(new TimeTableReportCompositeKey(timeTableTemplate.getTimeTableCompositeKey(),Boolean.TRUE, Calendar.getInstance().getTime()), timeTableTemplate.getSubjectId(), timeTableTemplate.getPractical(), lectureId);
    }

    public void testCompositeKey(){
        TimeTableCompositeKey timeTableCompositeKey = new TimeTableCompositeKey(3,17,2);
        TimeTableReportCompositeKey timeTableReportCompositeKey = new TimeTableReportCompositeKey(timeTableCompositeKey, Boolean.TRUE, Calendar.getInstance().getTime());
        TimeTableReport timeTableReport = new TimeTableReport(timeTableReportCompositeKey, 2,Boolean.FALSE, 1);
        timeTableReportRepository.save(timeTableReport);
    }

    public TimeTableTemplateRepository getTimeTableTemplateRepository() {
        return timeTableTemplateRepository;
    }

    public TimeTableReportRepository getTimeTableReportRepository() {
        return timeTableReportRepository;
    }

    public LectureCapabilityRepository getLectureCapabilityRepository() {
        return lectureCapabilityRepository;
    }

    public LectureRepository getLectureRepository() {
        return lectureRepository;
    }

    public ClassInfoRepository getClassInfoRepository() {
        return classInfoRepository;
    }

    public SubjectRepository getSubjectRepository() {
        return subjectRepository;
    }

    public GradeInfoRepository getGradeInfoRepository() {
        return gradeInfoRepository;
    }
}
