package com.college.timetable.service;

import com.college.timetable.AppConstants;
import com.college.timetable.entity.*;
import com.college.timetable.model.ReportModel;
import com.college.timetable.repository.*;
import com.college.timetable.util.RoundRobinHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReportGenerationService {
    private TimeTableTemplateRepository timeTableTemplateRepository;
    private TimeTableReportRepository timeTableReportRepository;
    private LectureCapabilityRepository lectureCapabilityRepository;
    private LectureRepository lectureRepository;
    private ClassInfoRepository classInfoRepository;
    private SubjectRepository subjectRepository;
    private GradeInfoRepository gradeInfoRepository;

    @Autowired
    public ReportGenerationService(TimeTableTemplateRepository timeTableTemplateRepository, TimeTableReportRepository timeTableReportRepository,
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

    public void buildTimeTableReport() throws Exception{

        Map<Integer, Integer> lectureCapacityMap = StreamSupport.stream(lectureRepository.findAll().spliterator(), false).collect(Collectors.toMap(x -> x.getLectureId(), x -> x.getCapacity()));
        Map<String, Set<Integer>> slotRegistryMap = new HashMap<>();
        RoundRobinHelper roundRobinHelper = new RoundRobinHelper();
        for (GradeInfo gradeInfo : gradeInfoRepository.findAllByOrderByGradeSeniorityWhereClassFoundDesc()) {
            List<Integer> classIdList = classInfoRepository.findByGradeLevel(gradeInfo.getGradeLevel());
            List<LectureCapabilities> lectureCapabilitiesByGradeLevel = lectureCapabilityRepository.getLectureCapabilitiesByGrade(gradeInfoRepository.findEqualAndAboveGradeSeniority(gradeInfo.getGradeSeniority()));
            List<TimeTableTemplate> timeTableTemplateList = timeTableTemplateRepository.findByClassId(classIdList);

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
                                                        .filter(capability -> compareInteger(capability.getCompositeKey().getSubjectId(), timeTableTemplate.getSubjectId())
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
        //timeTableTemplateList.forEach(System.out::println);
        //timeTableTemplateList.forEach(System.out::println);

        List<TimeTableReport> timeTableReportList = new ArrayList<>();
        timeTableTemplateList.stream()
                .forEach(timeTableTemplate -> {
                    String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+ AppConstants.CONCAT_CHAR+timeTableTemplate.getTimeTableCompositeKey().getHour();
                    List<Integer> qualifiedLectureIdList = lectureCapabilitiesByGradeLevel
                                                            .stream()
                                                            .filter(capability -> compareInteger(capability.getCompositeKey().getSubjectId(),timeTableTemplate.getSubjectId())
                                                                    && (slotRegistryMap.get(slotRegistryKey) == null || !slotRegistryMap.get(slotRegistryKey).contains(capability.getCompositeKey().getLectureId())))
                                                            .filter(capability -> (lectureCapacityMap.get(capability.getCompositeKey().getLectureId()) != null && lectureCapacityMap.get(capability.getCompositeKey().getLectureId()) > 0))
                                                            .map(capability -> capability.getCompositeKey().getLectureId())
                                                            .collect(Collectors.toList());
                    Integer assignLectureId = roundRobinHelper.pickLecture(qualifiedLectureIdList);

                    timeTableReportList.add(generateReportEntry(lectureCapacityMap, assignLectureId, timeTableTemplate, slotRegistryMap));

                    System.out.println(slotRegistryKey+"_"+timeTableTemplate.getSubjectId()+"_"+assignLectureId+"_"+qualifiedLectureIdList+"_"+lectureCapacityMap);
                });
        //timeTableReportList.forEach(System.out::println);
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
            String slotRegistryKey = timeTableTemplate.getTimeTableCompositeKey().getDay()+AppConstants.CONCAT_CHAR+timeTableTemplate.getTimeTableCompositeKey().getHour();
            slotRegistryMap.computeIfAbsent(slotRegistryKey, v-> new HashSet<>()).add(lectureId);
        }
        return copyTimeTableTemplate(timeTableTemplate, lectureId);
    }



    private TimeTableReport copyTimeTableTemplate(TimeTableTemplate timeTableTemplate, Integer lectureId){
        return new TimeTableReport(new TimeTableReportCompositeKey(timeTableTemplate.getTimeTableCompositeKey(),Boolean.TRUE, Calendar.getInstance().getTime()), timeTableTemplate.getSubjectId(), timeTableTemplate.getPractical(), lectureId);
    }
    public List<ReportModel> pullReportFromRepository(){
        return StreamSupport.stream(timeTableReportRepository.pullReport().spliterator(), false).collect(Collectors.toList());
    }

    private boolean compareInteger(Integer first, Integer second){
        if(first!=null && second!=null){
            return first.intValue() == second.intValue();
        }
        return false;
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
