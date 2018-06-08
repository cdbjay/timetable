package com.college.timetable.view;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ColorView {
    public static void main(String[] args) throws IOException {
        try (Workbook wb = new XSSFWorkbook()) { //or new HSSFWorkbook();
            Sheet sheet = wb.createSheet("new sheet");

            // Create a row and put some cells in it. Rows are 0 based.
            Row row = sheet.createRow(1);

            // Aqua background
            CellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            Cell cell = row.createCell(1);
            cell.setCellValue(new XSSFRichTextString("X"));
            cell.setCellStyle(style);

            // Orange "foreground", foreground being the fill foreground not the font color.
            style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.FINE_DOTS);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell = row.createCell(2);
            cell.setCellValue(new XSSFRichTextString("X"));
            cell.setCellStyle(style);

            style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
            style.setFillPattern(FillPatternType.THICK_FORWARD_DIAG);
            font = wb.createFont();
            font.setColor(IndexedColors.BLACK1.getIndex());
            style.setFont(font);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell = row.createCell(3);
            cell.setCellValue(new XSSFRichTextString("X"));
            cell.setCellStyle(style);

            style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.LEAST_DOTS);
            font = wb.createFont();
            font.setColor(IndexedColors.BLACK.getIndex());
            style.setFont(font);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell = row.createCell(4);
            cell.setCellValue(new XSSFRichTextString("4"));
            cell.setCellStyle(style);

            style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            style.setFillPattern(FillPatternType.LEAST_DOTS);
            font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            cell = row.createCell(5);
            cell.setCellValue(new XSSFRichTextString("X"));
            cell.setCellStyle(style);

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("fill_colors.xlsx")) {
                wb.write(fileOut);
            }
        }
    }

}
