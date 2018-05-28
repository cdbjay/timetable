package com.college.timetable.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class FileUtil {
    public static void main(String[] args) {
        try
        {
            FileInputStream file = new FileInputStream(new File("d://project/input.xlsx"));

            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            //Get first/desired sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

            ArrayList<NewEmployee> employeeList = new ArrayList<>();
            //I've Header and I'm ignoring header for that I've +1 in loop
            for(int i=sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
                NewEmployee e= new NewEmployee();
                Row ro=sheet.getRow(i);
                for(int j=ro.getFirstCellNum();j<=ro.getLastCellNum();j++){
                    Cell ce = ro.getCell(j);
                    if(j==0){
                        //If you have Header in text It'll throw exception because it won't get NumericValue
                        e.setId(ce.getNumericCellValue());
                    }
                    if(j==1){
                        e.setFirstName(ce.getStringCellValue());
                    }
                    if(j==2){
                        e.setLastName(ce.getStringCellValue());
                    }
                }
                employeeList.add(e);
            }
            file.close();
            employeeList.forEach(System.out::println);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
