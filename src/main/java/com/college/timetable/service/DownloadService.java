package com.college.timetable.service;

import com.college.timetable.repository.TimeTableReportRepository;
import org.springframework.stereotype.Service;

@Service
public class DownloadService {

    TimeTableReportRepository timeTableReportRepository;

    private void generateExcel(){
        timeTableReportRepository
                .findAll()
                .forEach(timeTableReport -> {
                        System.out.println();
                    }
                );
    }
}
