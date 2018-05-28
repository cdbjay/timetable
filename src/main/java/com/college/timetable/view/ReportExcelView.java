package com.college.timetable.view;

import com.college.timetable.model.ReportModel;
import com.college.timetable.model.User;
import com.college.timetable.service.ReportGenerationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.Observable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsView;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
@Component
public class ReportExcelView extends AbstractXlsxView {
    ReportGenerationService reportGenerationService;

    @Value("${class.hours.list}")
    private String classHoursList;

    @Value("${class.days.indicator}")
    private String daysIndicator;

    @Value("#{${class.days.display.indicatorMap}}")
    private Map<String, String> dayDisplayMap;

    private List<ReportModel> reportModelList;

    @Autowired
    public ReportExcelView(ReportGenerationService reportGenerationService) {
        this.reportGenerationService = reportGenerationService;
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"TimeTableReport.xlsx\"");

        List<ReportModel> reportModelList = reportGenerationService.pullReportFromRepository();

        Map<Integer, List<ReportModel>> reportMapByClassId = reportModelList.stream().collect(Collectors.groupingBy(ReportModel::getClassId, Collectors.toList()));
        reportMapByClassId.entrySet().forEach(entry ->{
            fillSheet(workbook, entry.getValue());
        });
        /*try (FileOutputStream fileOut = new FileOutputStream("TimeTableReport.xlsx")) {
            workbook.write(fileOut);
        }*/
    }


    private void fillSheet(Workbook workbook, List<ReportModel> reportModelList){
        if(reportModelList!=null && reportModelList.size()>0) {
            try {
                String sheetName = reportModelList.get(0).getCourseName() + "-" + reportModelList.get(0).getMajorSubject();
                Sheet sheet = workbook.createSheet(sheetName);
                AtomicInteger rowAtomicInteger = new AtomicInteger(0);
                AtomicInteger headerColumnAtomicInteger = new AtomicInteger(0);
                // Create a row and put some cells in it. Rows are 0 based.
                Row row = sheet.createRow(0);
                createCellWithStyle(workbook, row, 0, sheetName+"-"+reportModelList.get(0).getSection(), ECellType.TITLE);
                sheet.autoSizeColumn(0);
                Arrays.asList(classHoursList.split(",")).stream().forEach(hour -> {
                    createCellWithStyle(workbook, row, headerColumnAtomicInteger.incrementAndGet(), hour, ECellType.HEADER);
                });

                reportModelList.stream().collect(Collectors.groupingBy(ReportModel::getDay)).keySet().stream().sorted()
                        .forEach(day -> {
                            Row reportRow = sheet.createRow(rowAtomicInteger.incrementAndGet());
                            AtomicInteger dataColumnAtomicInteger = new AtomicInteger(-1);
                            createCellWithStyle(workbook, reportRow, dataColumnAtomicInteger.incrementAndGet(), dayDisplayMap.get(String.valueOf(day)), ECellType.HEADER);
                            reportModelList.stream().filter(reportModel -> reportModel.getDay()==day).sorted(hourComparator).forEach(reportModel -> {
                                String displaySubjectName = reportModel.getSubjectName()+(reportModel.getPractical()!=null && reportModel.getPractical()?" (P)":"");
                                createCellWithStyle(workbook, reportRow, dataColumnAtomicInteger.incrementAndGet(), displaySubjectName, ECellType.DATA);
                            });

                        });
                headerColumnAtomicInteger.set(0);
                IntStream.range(0,classHoursList.split(",").length*2).forEach(s -> {sheet.autoSizeColumn(headerColumnAtomicInteger.getAndIncrement());}
                );
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    Comparator<ReportModel> hourComparator = (x, y )->{
        return x.getHour().compareTo(y.getHour());
    };


    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ReportModel> reportModelList = objectMapper.readValue(new File("./input/report.json"), objectMapper.getTypeFactory().constructCollectionType(List.class, ReportModel.class));
            ReportExcelView reportExcelView = new ReportExcelView(null);

            reportExcelView.reportModelList = reportModelList;
            reportExcelView.classHoursList="9,10,11,12,14,15,16,17";
            reportExcelView.daysIndicator="1,2,3,4,5";
            ObjectMapper objectMapper1 = new ObjectMapper();
            reportExcelView.dayDisplayMap=objectMapper1.readValue("{\"1\":\"Monday\",\"2\":\"Tuesday\",\"3\":\"Wednesday\",\"4\":\"Thursday\",\"5\":\"Friday\"}", objectMapper1.getTypeFactory().constructMapType(Map.class, String.class, String.class));


            reportExcelView.buildExcelDocument(null, new XSSFWorkbook(), null, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Cell createCellWithStyle(Workbook wb, Row row, Integer columnIndex, String cellValue, ECellType eCellType){
        CellStyle style = wb.createCellStyle();
        applyStyleForHeader(wb, style, eCellType);
        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(new XSSFRichTextString(cellValue));
        cell.setCellStyle(style);
        return cell;
    }

    private void applyStyleForHeader(Workbook wb, CellStyle style, ECellType eCellType){
        if(eCellType == ECellType.DATA) {
            style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.FINE_DOTS);
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            //style.setFont(font);
        }else if(eCellType == ECellType.HEADER){
            style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
        }else {
            style.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = wb.createFont();
            font.setColor(IndexedColors.WHITE.getIndex());
            style.setFont(font);
        }
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

    }

    private enum ECellType{
        TITLE, HEADER, DATA;
    }
}