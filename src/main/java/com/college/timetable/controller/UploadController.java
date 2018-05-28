package com.college.timetable.controller;

import com.college.timetable.service.ReportGenerationService;
import com.college.timetable.service.RepositoryService;
import com.college.timetable.service.TimeTableTemplateService;
import com.college.timetable.util.file.sheet.ISheetHandler;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class UploadController {

    @Autowired
    BeanFactory beanFactory;
    @Autowired
    TimeTableTemplateService timeTableTemplateService;
    @Autowired
    ReportGenerationService reportGenerationService;
    @Autowired
    RepositoryService repositoryService;

    @Value("${upload.file.sheet.names}")
    String sheetNames;

    @PostMapping(value = "/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            if (file != null && !file.isEmpty()){
                repositoryService.cleanUp();
                processFile(file.getInputStream());
                timeTableTemplateService.prepareTimeTableTemplate(Boolean.TRUE);
                reportGenerationService.buildTimeTableReport();
                model.addAttribute("reportExists", repositoryService.reportExists());
                model.addAttribute("message", "Congrats! File has been successfully uploaded. Report shall be download via Download Report button.");
            }else{
                model.addAttribute("error", "Upload file failed. Either content is empty or the file is corrupted");
            }
        } catch (Exception e) {
            repositoryService.cleanUp();
            e.printStackTrace();
            model.addAttribute("error", "Server Processing Error - "+e);
        }
        return "th_index";
    }

    private void processFile(InputStream inputStream) throws  Exception{
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            for(String sheetName: sheetNames.split(",")){
                XSSFSheet sheet = workbook.getSheet(sheetName);
                if(sheet!=null){
                    try {
                        beanFactory.getBean(sheetName, ISheetHandler.class).processSheet(sheet);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Exception while invoking the respective handler "+e+" for "+sheetName);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            inputStream.close();
        }
    }
}

