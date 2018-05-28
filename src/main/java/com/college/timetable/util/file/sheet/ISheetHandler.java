package com.college.timetable.util.file.sheet;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ISheetHandler {
    void processSheet(XSSFSheet sheet) throws Exception;
}
