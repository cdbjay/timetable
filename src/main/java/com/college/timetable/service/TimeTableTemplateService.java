package com.college.timetable.service;

import com.college.timetable.AppConstants;
import com.college.timetable.entity.ClassCapacity;
import com.college.timetable.entity.TimeTableCompositeKey;
import com.college.timetable.entity.TimeTableTemplate;
import com.college.timetable.repository.ClassCapacityRepository;
import com.college.timetable.repository.GradeInfoRepository;
import com.college.timetable.repository.TimeTableReportRepository;
import com.college.timetable.repository.TimeTableTemplateRepository;
import com.college.timetable.util.RoundRobinHelper;
import com.college.timetable.util.RoundRobinTemplateHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class TimeTableTemplateService {

    @Value("${class.hours.list}")
    private String classHours;

    @Value("${class.days.indicator}")
    private String daysIndicator;

    ClassCapacityRepository classCapacityRepository;
    GradeInfoRepository gradeInfoRepository;
    TimeTableTemplateRepository timeTableTemplateRepository;
    TimeTableReportRepository timeTableReportRepository;

    @Autowired
    public TimeTableTemplateService(ClassCapacityRepository classCapacityRepository, GradeInfoRepository gradeInfoRepository, TimeTableTemplateRepository timeTableTemplateRepository, TimeTableReportRepository timeTableReportRepository) {
        this.classCapacityRepository = classCapacityRepository;
        this.gradeInfoRepository = gradeInfoRepository;
        this.timeTableTemplateRepository = timeTableTemplateRepository;
        this.timeTableReportRepository = timeTableReportRepository;
    }

    public void prepareTimeTableTemplate(Boolean regenerateFlag) throws Exception {
        try {
            boolean templateRecordExists = hasTemplateRecords();
            if(!templateRecordExists || regenerateFlag){
                generateTemplate();
            }else{
                throw new Exception("Template exists. Use REGENERATE if you would like to regenerate the complete template again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Template Generation Failed - "+e.getMessage());
        }
    }

    public void generateTemplate() throws Exception {
        deleteTemplateRecords();
        List<TimeTableTemplate> timeTableTemplateList = new ArrayList<>();
        gradeInfoRepository.findAllByOrderByGradeSeniorityDesc().stream().forEach(gradeInfo -> {
                    classCapacityRepository.findClassCapacityByGradeLevel(gradeInfo.getGradeLevel())
                            .stream()
                            .collect(Collectors.groupingBy(c1 -> c1.getCompositeKey().getClassId(), Collectors.mapping(c1-> c1, Collectors.toList())))
                            .forEach((classId,classCapacityList) -> {
                                timeTableTemplateList.addAll(initializeTimeTableTemplate(classId));
                                prepareTimeTableForClassId(classId, classCapacityList, timeTableTemplateList);
                            });
                }
        );
        timeTableTemplateRepository.saveAll(timeTableTemplateList);
        //timeTableTemplateList.forEach(System.out::println);
    }

    private void prepareTimeTableForClassId(Integer classId, List<ClassCapacity> classCapacityList, List<TimeTableTemplate> timeTableTemplateList){

        RoundRobinTemplateHelper roundRobinTemplateHelper = new RoundRobinTemplateHelper(classCapacityList);
        String pickedSubjectWithPracticalIndicator = roundRobinTemplateHelper.pickSubject();

        while (pickedSubjectWithPracticalIndicator!=null){
            Integer pickedSubjectId = Integer.parseInt(pickedSubjectWithPracticalIndicator.split(AppConstants.CONCAT_CHAR)[0]);
            Boolean isPractical = Boolean.valueOf(pickedSubjectWithPracticalIndicator.split(AppConstants.CONCAT_CHAR)[1]);

            Integer pickedDay = pickDay(pickedSubjectId, classId, timeTableTemplateList);

            Integer pickedHour = pickHourV1(pickedSubjectId, classId, pickedDay, timeTableTemplateList);
            timeTableTemplateList.stream().filter(x -> compareInteger(x.getTimeTableCompositeKey().getClassId(), classId) && compareInteger(x.getTimeTableCompositeKey().getDay(),pickedDay)
                    && compareInteger(x.getTimeTableCompositeKey().getHour(),pickedHour)).findFirst().get().setSubjectIdAndIsPractical(pickedSubjectId, isPractical);

            pickedSubjectWithPracticalIndicator = roundRobinTemplateHelper.pickSubject();
        }
    }

    private List<TimeTableTemplate> initializeTimeTableTemplate(Integer classId){
        List<TimeTableTemplate> timeTableTemplateList = new ArrayList<>();
        convertStringToIntegerList(daysIndicator).stream().forEach(
                day -> {
                    convertStringToIntegerList(classHours).forEach(
                            hour ->{
                                timeTableTemplateList.add(new TimeTableTemplate(new TimeTableCompositeKey(classId, day, hour),  null, null));
                            }
                    );
                }
        );
        return timeTableTemplateList;
    }

    private Integer pickDay(Integer subjectId, Integer classId, List<TimeTableTemplate> timeTableTemplateList){
        Integer pickedDay = null;

        List<Integer> dayListWithSubjectNotfound = timeTableTemplateList.stream().filter(x -> compareInteger(x.getTimeTableCompositeKey().getClassId(),classId))
                .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.toList()))
                .entrySet().stream()
                .filter(x -> timeTableTemplateList.stream().filter(y -> compareInteger(y.getTimeTableCompositeKey().getClassId(),classId) &&
                        compareInteger(y.getTimeTableCompositeKey().getDay(),x.getKey()) && compareInteger(y.getSubjectId(),subjectId)).count() == 0)
                .map(x -> x.getKey()).collect(Collectors.toList());


        if(dayListWithSubjectNotfound!=null && dayListWithSubjectNotfound.size()>0){

           /* timeTableTemplateList.stream()
                    .filter(x -> x.getSubjectId() == null && x.getTimeTableCompositeKey().getClassId() == classId && dayListWithSubjectNotfound.contains(x.getTimeTableCompositeKey().getDay()))
                    .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting())).entrySet().stream().forEach(System.out::println);

            timeTableTemplateList.stream()
                    .filter(x -> x.getSubjectId() == null && x.getTimeTableCompositeKey().getClassId() == classId && dayListWithSubjectNotfound.contains(x.getTimeTableCompositeKey().getDay()))
                    .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(x -> x.getValue(), Comparator.reverseOrder())).forEach(System.out::println);

            timeTableTemplateList.stream()
                    .filter(x -> x.getSubjectId() == null && x.getTimeTableCompositeKey().getClassId() == classId && dayListWithSubjectNotfound.contains(x.getTimeTableCompositeKey().getDay()))
                    .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(x -> x.getValue(), Comparator.reverseOrder()))
                    .map(x-> x.getKey()).forEach(System.out::println);*/

            pickedDay = timeTableTemplateList.stream()
                    .filter(x -> x.getSubjectId() == null && compareInteger(x.getTimeTableCompositeKey().getClassId(),classId) && dayListWithSubjectNotfound.contains(x.getTimeTableCompositeKey().getDay().intValue()))
                    .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(x -> x.getValue(), Comparator.reverseOrder()))
                    .map(x-> x.getKey())
                    .findFirst().orElse(null);
        }

        if(pickedDay==null){
            List<Integer> dayWithMinOccurrenceOfGivenSubject = timeTableTemplateList.stream()
                    .filter(x -> compareInteger(x.getTimeTableCompositeKey().getClassId(),classId) && compareInteger(x.getSubjectId(), subjectId))
                    .filter(x -> timeTableTemplateList.stream()
                            .filter(y -> compareInteger(y.getTimeTableCompositeKey().getClassId(),classId)
                                    && compareInteger(y.getTimeTableCompositeKey().getDay(), x.getTimeTableCompositeKey().getDay())
                                    && y.getSubjectId() == null).count() > 0)
                    .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting()))
                    .entrySet().stream()
                    .collect(Collectors.groupingBy(x -> x.getValue(), Collectors.mapping(x -> x.getKey(), Collectors.toList())))
                    .entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(x -> x.getValue())
                    .findFirst().orElse(null);

            if(dayWithMinOccurrenceOfGivenSubject!=null){
                pickedDay = timeTableTemplateList.stream()
                        .filter(x -> dayWithMinOccurrenceOfGivenSubject.contains(x.getTimeTableCompositeKey().getDay().intValue()) && compareInteger(x.getTimeTableCompositeKey().getClassId(),classId) && x.getSubjectId() == null)
                        .collect(Collectors.groupingBy(x -> x.getTimeTableCompositeKey().getDay(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(x -> x.getValue(), Comparator.reverseOrder()))
                        .map(y -> y.getKey())
                        .findFirst().get();
            }
        }
        return pickedDay;
    }

    private Integer pickHour(Integer subjectId, Integer classId, Integer pickedDay, List<TimeTableTemplate> timeTableTemplateList){
        Integer pickedHour = null;
        List<TimeTableTemplate> currentClassHourList = timeTableTemplateList.stream()
                .filter(x->compareInteger(x.getTimeTableCompositeKey().getDay(),pickedDay) && x.getSubjectId()==null).collect(Collectors.toList());

        List<TimeTableTemplate> otherClassHourList = timeTableTemplateList.stream()
                .filter(x-> compareInteger(x.getTimeTableCompositeKey().getDay(),pickedDay) && x.getTimeTableCompositeKey().getClassId()!=classId)
                .filter(x-> currentClassHourList.stream().filter(y-> compareInteger(y.getTimeTableCompositeKey().getHour(),x.getTimeTableCompositeKey().getHour())).count()>0)
                .collect(Collectors.toList());

        if (otherClassHourList.size()>0) {
            Map<Boolean, List<TimeTableTemplate>> otherClassHourBySubjectMatch = otherClassHourList.stream().collect(Collectors.partitioningBy(x -> x.getSubjectId() != subjectId));

            List<TimeTableTemplate> hourList = otherClassHourBySubjectMatch.get(Boolean.TRUE)!=null && otherClassHourBySubjectMatch.get(Boolean.TRUE).size()>0 ?
                                                    otherClassHourBySubjectMatch.get(Boolean.TRUE) :
                                                    otherClassHourBySubjectMatch.get(Boolean.FALSE);
            if(hourList !=null && hourList.size()>0){
                pickedHour = hourList.get(0).getTimeTableCompositeKey().getHour();
            }
        } else if(currentClassHourList.size()>0) {
            pickedHour = currentClassHourList.stream().findAny().get().getTimeTableCompositeKey().getHour();
        }
        return pickedHour;
    }

    private Integer pickHourV1(Integer subjectId, Integer classId, Integer pickedDay, List<TimeTableTemplate> timeTableTemplateList){
        Integer pickedHour = timeTableTemplateList.stream()
                .filter(t -> compareInteger(t.getTimeTableCompositeKey().getDay(),pickedDay))
                .collect(Collectors.groupingBy(t -> t.getTimeTableCompositeKey().getHour(), Collectors.mapping(t -> t, Collectors.toList())))
                .entrySet().stream()
                .filter(t -> timeTableTemplateList
                        .stream()
                        .filter(y -> y.getSubjectId() == null && compareInteger(y.getTimeTableCompositeKey().getClassId(),classId) && compareInteger(y.getTimeTableCompositeKey().getHour(),t.getKey())).count() > 0)
                .sorted((e1, e2) -> {
                    Long e1Load = e1.getValue().stream().filter(t -> t.getSubjectId() != null).collect(Collectors.counting());
                    Long e2Load = e2.getValue().stream().filter(t -> t.getSubjectId() != null).collect(Collectors.counting());

                    Long e1SameSubjectLoad = e1.getValue().stream().filter(t -> t.getSubjectId() != null && compareInteger(t.getSubjectId(),subjectId)).collect(Collectors.counting());
                    Long e2SameSubjectLoad = e2.getValue().stream().filter(t -> t.getSubjectId() != null && compareInteger(t.getSubjectId(),subjectId)).collect(Collectors.counting());

                    return compareLong(e1Load,e2Load) ? (compareLong(e1SameSubjectLoad,e2SameSubjectLoad) ? e1.getKey().compareTo(e2.getKey()) : e1SameSubjectLoad.compareTo(e2SameSubjectLoad) ) : e1Load.compareTo(e2Load);
                })
                .map(x -> x.getKey()).findFirst().orElse(null);
        return pickedHour;
    }

    private Comparator<Map.Entry<Integer, List<TimeTableTemplate>>> loadComparator = (e1, e2)->{
        Long e1Load = e1.getValue().stream().filter(t -> t.getSubjectId()!=null).collect(Collectors.counting());
        Long e2Load = e2.getValue().stream().filter(t -> t.getSubjectId()!=null).collect(Collectors.counting());

        return e1Load.compareTo(e2Load);
    };

    private List<Integer> convertStringToIntegerList(String property){
        return Arrays.asList(property.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    private boolean compareInteger(Integer first, Integer second){
        if(first!=null && second!=null){
            return first.intValue() == second.intValue();
        }
        return false;
    }

    private boolean compareLong(Long first, Long second){
        if(first!=null && second!=null){
            return first.longValue() == second.longValue();
        }
        return false;
    }

    private void deleteTemplateRecords(){
        timeTableTemplateRepository.deleteAll();
        timeTableTemplateRepository.deleteAll();
    }

    public boolean hasTemplateRecords(){
        return timeTableTemplateRepository.findAll().iterator().hasNext();
    }
}
