package com.college.timetable.controller;

import com.college.timetable.model.ReportModel;
import com.college.timetable.service.ReportGenerationService;
import com.college.timetable.service.TimeTableTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeTableController {

    private TimeTableTemplateService timeTableTemplateService;
    ReportGenerationService reportGenerationService;

    @Autowired
    public TimeTableController(TimeTableTemplateService timeTableTemplateService, ReportGenerationService reportGenerationService) {
        this.timeTableTemplateService = timeTableTemplateService;
        this.reportGenerationService = reportGenerationService;
    }

    @GetMapping("/generateTemplate/{action}")
    public String prepareTimeTableTemplate(@PathVariable("action") String action, Model model){
        String returnValue = "Success";
        try {
            timeTableTemplateService.prepareTimeTableTemplate("regenerate".equalsIgnoreCase(action));
            model.addAttribute("messsage", returnValue);
        } catch (Exception e) {
            returnValue=e.getMessage();
            model.addAttribute("error",e.getMessage());
            e.printStackTrace();
        }
        return returnValue;
    }

    @GetMapping("/downloadReport")
    public List<ReportModel> downloadReportAsJson(){
        return reportGenerationService.pullReportFromRepository();
    }
}
