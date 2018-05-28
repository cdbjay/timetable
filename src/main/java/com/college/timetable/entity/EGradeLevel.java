package com.college.timetable.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EGradeLevel {
    /*!!!!!!!!!!!!!!!!!!STOP !!!!!!!!!!!!
    * IF YOU ARE MODIFYING THE ELEMENTS, MAKE SURE THE ELEMENTS ARE ADDED IN THE CHRONOLOGICAL ORDER WHREE THE HIGHER RANKING SHOULD LEAD THE LOWER RANK TO AVOID CRASH
    * EG: PG SHOULD BE FOLLOWED BY UG AND NOT OTHER DIRECTION
    * PG, UG, PUC IS ALLOWED
    * PUC, UG, PG WILL MAKE APPLICATION TO MIS BEHAVE DIFFERENTLY*/
    PG, UG;

    public List<EGradeLevel> getCurrentAndAboveOrderinalEnum(){
        return Arrays.asList(EGradeLevel.values()).stream().filter(eGradeLevel -> this.compareTo(eGradeLevel) >= 0).collect(Collectors.toList());
    }

}
