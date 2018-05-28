package com.college.timetable.util.file.sheet;

import com.college.timetable.entity.SubjectInfo;
import com.college.timetable.repository.SubjectRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class SubjectDefinitionHandler implements ISheetHandler {

    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public void processSheet(XSSFSheet sheet) throws Exception {
        List<SubjectInfo> subjectInfoList = new ArrayList<>();
        for(int i=sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
            Row row=sheet.getRow(i);
            Cell cell = row.getCell(row.getFirstCellNum());
            subjectInfoList.add(new SubjectInfo(cell.getStringCellValue()));
        }
        if(subjectInfoList.size()>0){
            subjectRepository.saveAll(subjectInfoList);
        }
    }
}
