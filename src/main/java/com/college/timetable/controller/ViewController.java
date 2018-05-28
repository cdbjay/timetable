package com.college.timetable.controller;

import com.college.timetable.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/views")
public class ViewController {
    @Value("${app.welcomeMessage}")
    private String welcomeMessage;

    @Autowired
    RepositoryService repositoryService;

    @GetMapping
    public String homePage(Map<String, Object> model){
        model.put("message",this.welcomeMessage);
        model.put("reportExists", repositoryService.reportExists());
        return "th_index";
    }
}
