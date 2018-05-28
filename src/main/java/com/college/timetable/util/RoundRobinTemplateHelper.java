package com.college.timetable.util;

import com.college.timetable.AppConstants;
import com.college.timetable.entity.ClassCapacity;
import com.college.timetable.entity.TimeTableTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class RoundRobinTemplateHelper {
    private Queue<String> roundRobinQueue = null;
    private Map<String, Integer> subjectAvailableCountMap = new HashMap<>();

    public RoundRobinTemplateHelper(List<ClassCapacity> classCapacityList) {
        this.subjectAvailableCountMap = classCapacityList.stream().collect(Collectors.toMap(x -> x.getCompositeKey().getSubjectId().toString().concat(AppConstants.CONCAT_CHAR).concat(x.getCompositeKey().getPractical().toString()), x-> x.getCapacity()));
        this.roundRobinQueue = new ConcurrentLinkedQueue<>(getSubjectAvailableCountMap().keySet());
    }

    public String pickSubject(){
        String pickedSubjectWithPracticalIndicator = null;
        boolean foundFlag = false;
        while(!foundFlag){
            pickedSubjectWithPracticalIndicator = roundRobinQueue.poll();
            if(subjectAvailableCountMap.get(pickedSubjectWithPracticalIndicator) > 0){
                subjectAvailableCountMap.computeIfPresent(pickedSubjectWithPracticalIndicator, (key, value) -> value=value-1);
                roundRobinQueue.add(pickedSubjectWithPracticalIndicator);
            }else{
                pickedSubjectWithPracticalIndicator=null;
                if(subjectAvailableCountMap.entrySet().stream().filter((x -> x.getValue()>0)).count()>0){
                    continue;
                }
            }
            foundFlag = true;
        }
        return pickedSubjectWithPracticalIndicator;
    }

    public Map<String, Integer> getSubjectAvailableCountMap() {
        return subjectAvailableCountMap;
    }

    public void setSubjectAvailableCountMap(Map<String, Integer> subjectAvailableCountMap) {
        this.subjectAvailableCountMap = subjectAvailableCountMap;
    }
}

