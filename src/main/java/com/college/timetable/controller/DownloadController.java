package com.college.timetable.controller;

import com.college.timetable.model.User;
import com.college.timetable.view.ExcelView;
import com.college.timetable.view.ReportExcelView;
import com.college.timetable.view.ReportMergeExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Controller
public class DownloadController {

    @Autowired
    ReportMergeExcelView reportExcelView;

    @Value(value = "classpath:download/Upload_Template.xlsx")
    private Resource uploadTemplateFile;

    @Value(value = "classpath:download/Sample_Template.xlsx")
    private Resource sampleTemplateFile;

    @GetMapping("/download")
    public ModelAndView downloadReport(Model model){
        /*model.addAttribute("users",getUsers());
        return "xls";*/
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(reportExcelView);
        return modelAndView;
    }
    @GetMapping(value = "/downloadTemplate", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public HttpEntity<byte[]> downloadTemplate(){
        return getHttpEntity(uploadTemplateFile, "Template");
    }

    @GetMapping(value = "/downloadSampleTemplate", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public HttpEntity<byte[]> downloadSampleTemplate(){
        return getHttpEntity(sampleTemplateFile, "Sample_Template");
    }

    private HttpEntity<byte[]> getHttpEntity(Resource resource, String fileName){
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName+".xlsx");
            return new HttpEntity<byte[]>(buffer, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
