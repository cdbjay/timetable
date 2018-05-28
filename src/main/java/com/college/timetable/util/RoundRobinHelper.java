package com.college.timetable.util;

import com.college.timetable.entity.LectureCapabilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Predicate;

@Component
public class RoundRobinHelper {
    private List<Integer> roundRobinList = null;

    public RoundRobinHelper() {
        this.roundRobinList = new LinkedList<>();
    }

    public Integer pickLecture(List<Integer> lectureIdList){
        Integer lectureIdToReturn = null;
        if(lectureIdList!=null && lectureIdList.size()>0){
            lectureIdToReturn = lectureIdList.stream().filter(id -> !roundRobinList.contains(id)).findAny().orElse(null);
            if(lectureIdToReturn!=null){
                roundRobinList.add(lectureIdToReturn);
            }else{
                lectureIdToReturn = lectureIdList.stream().sorted(positionComparator).findFirst().get();
                roundRobinList.remove(roundRobinList.indexOf(lectureIdToReturn));
                roundRobinList.add(lectureIdToReturn);
            }
        }
        return lectureIdToReturn;
    }
    Comparator<Integer> positionComparator = (i1,i2) -> {
        return Integer.valueOf(roundRobinList.indexOf(i1)).compareTo(Integer.valueOf(roundRobinList.indexOf(i2)));
    };

    private Integer pickAny(List<Integer> inputList){
        Integer randomPick = inputList.get(new Random().nextInt(inputList.size()));
        roundRobinList.add(randomPick);
        return randomPick;
    }

}

