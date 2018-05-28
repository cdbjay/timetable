package com.college.timetable.view;

import com.college.timetable.model.User;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"TimeTableReport.xls\"");

        @SuppressWarnings("unchecked")
        List<User> users = model==null ? Arrays.asList(new User("jai","thange", 33, "Sr. Arch","CTS","MBP","bangalore","India","99999")): (List<User>) model.get("users");
        // create excel xls sheet
        Sheet sheet = workbook.createSheet("User Detail");
        sheet.autoSizeColumn(1);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
        //font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);


        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("FirstName");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("LastName");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Age");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Job Title");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Company");
        header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Address");
        header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("City");
        header.getCell(6).setCellStyle(style);
        header.createCell(7).setCellValue("Country");
        header.getCell(7).setCellStyle(style);
        header.createCell(8).setCellValue("Phone Number");
        header.getCell(8).setCellStyle(style);

        header.setRowStyle(style);



        int rowCount = 1;

        for(User user : users){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(user.getFirstName());
            userRow.createCell(1).setCellValue(user.getLastName());
            userRow.createCell(2).setCellValue(user.getAge());
            userRow.createCell(3).setCellValue(user.getJobTitle());
            userRow.createCell(4).setCellValue(user.getCompany());
            userRow.createCell(5).setCellValue(user.getAddress());
            userRow.createCell(6).setCellValue(user.getCity());
            userRow.createCell(7).setCellValue(user.getCountry());
            userRow.createCell(8).setCellValue(user.getPhoneNumber());

        }
    }

    public static void main1(String[] args) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) { //or new HSSFWorkbook();
            CreationHelper creationHelper = wb.getCreationHelper();
            Sheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow((short) 0);
            // Create a cell and put a value in it.
            Cell cell = row.createCell((short) 0);
            cell.setCellValue(1);

            //numeric value
            row.createCell(1).setCellValue(1.2);

            //plain string value
            row.createCell(2).setCellValue("This is a string cell");

            //rich text string
            RichTextString str = creationHelper.createRichTextString("Apache");
            Font font = wb.createFont();
            font.setItalic(true);
            font.setUnderline(Font.U_SINGLE);
            str.applyFont(font);
            row.createCell(3).setCellValue(str);

            //boolean value
            row.createCell(4).setCellValue(true);

            //formula
            row.createCell(5).setCellFormula("SUM(A1:B1)");

            //date
            CellStyle style = wb.createCellStyle();
            style.setDataFormat(creationHelper.createDataFormat().getFormat("m/d/yy h:mm"));
            cell = row.createCell(6);
            cell.setCellValue(new Date());
            cell.setCellStyle(style);

            //hyperlink
            row.createCell(7).setCellFormula("SUM(A1:B1)");
            cell.setCellFormula("HYPERLINK(\"http://google.com\",\"Google\")");


            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("ooxml-cell.xlsx")) {
                wb.write(fileOut);
            }
        }
    }

    public static void main2(String[] args) throws IOException {
        try (Workbook wb = new HSSFWorkbook()) { //or new HSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow(1);

            // Aqua background
            CellStyle style = wb.createCellStyle();
            style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            style.setFillPattern(FillPatternType.BIG_SPOTS);
            Cell cell = row.createCell(1);
            cell.setCellValue("X");
            cell.setCellStyle(style);

            // Orange "foreground", foreground being the fill foreground not the font color.
            style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell = row.createCell(2);
            cell.setCellValue("X");
            cell.setCellStyle(style);

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("fill_colors.xls")) {
                wb.write(fileOut);
            }
        }
    }

    public static void main3(String[] args) throws IOException {

        try (Workbook wb = new XSSFWorkbook()) {
            XSSFSheet sheet = (XSSFSheet) wb.createSheet();

            // Create
            XSSFTable table = sheet.createTable();
            table.setName("Test");
            table.setDisplayName("Test_Table");

            // For now, create the initial style in a low-level way
            table.getCTTable().addNewTableStyleInfo();
            table.getCTTable().getTableStyleInfo().setName("TableStyleMedium2");

            // Style the table
            XSSFTableStyleInfo style = (XSSFTableStyleInfo) table.getStyle();
            style.setName("TableStyleMedium2");
            style.setShowColumnStripes(false);
            style.setShowRowStripes(true);
            style.setFirstColumn(false);
            style.setLastColumn(false);
            style.setShowRowStripes(true);
            style.setShowColumnStripes(true);

            // Set the values for the table
            XSSFRow row;
            XSSFCell cell;
            for (int i = 0; i < 6; i++) {
                // Create row
                row = sheet.createRow(i);
                for (int j = 0; j < 3; j++) {
                    // Create cell
                    cell = row.createCell(j);
                    if (i == 0) {
                        cell.setCellValue("Column" + (j + 1));
                    } else {
                        cell.setCellValue((i + 1) * (j + 1));
                    }
                }
            }
            // Create the columns
            table.addColumn();table.addColumn();table.addColumn();

         /*   table.createColumn("Column 2");
            table.createColumn("Column 3");*/

            // Set which area the table should be placed in
            AreaReference reference = wb.getCreationHelper().createAreaReference(
                    new CellReference(0, 0), new CellReference(5, 2));
            table.setCellReferences(reference);

            // Save
            try (FileOutputStream fileOut = new FileOutputStream("ooxml-table.xlsx")) {
                wb.write(fileOut);
            }
        }
    }

    public static void main(String[] args) {
        Boolean input = null;
        System.out.println(Boolean.FALSE == input);
    }
}