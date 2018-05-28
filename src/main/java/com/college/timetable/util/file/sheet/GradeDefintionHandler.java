package com.college.timetable.util.file.sheet;

import com.college.timetable.entity.GradeInfo;
import com.college.timetable.repository.GradeInfoRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("GRADE_DEFINITION")
public class GradeDefintionHandler implements ISheetHandler {
    @Autowired
    private GradeInfoRepository gradeInfoRepository;

    @Override
    public void processSheet(XSSFSheet sheet) throws Exception {
        List<GradeInfo> gradeInfoList = new ArrayList<>();
        for(int i=sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
            Row row=sheet.getRow(i);
            gradeInfoList.add(new GradeInfo(row.getCell(row.getFirstCellNum()).getStringCellValue(), Double.valueOf(row.getCell(row.getFirstCellNum()+1).getNumericCellValue()).intValue()));
        }
        if(gradeInfoList!=null){
            gradeInfoRepository.saveAll(gradeInfoList);
        }
    }
}
