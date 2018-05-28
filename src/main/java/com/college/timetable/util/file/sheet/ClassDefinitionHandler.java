package com.college.timetable.util.file.sheet;

import com.college.timetable.entity.ClassCapacity;
import com.college.timetable.entity.ClassCapacityCompositeKey;
import com.college.timetable.entity.ClassInfo;
import com.college.timetable.repository.ClassCapacityRepository;
import com.college.timetable.repository.ClassInfoRepository;
import com.college.timetable.repository.GradeInfoRepository;
import com.college.timetable.repository.SubjectRepository;
import com.college.timetable.service.RepositoryService;
import com.college.timetable.util.NewEmployee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("CLASS_DEFINITION")
public class ClassDefinitionHandler implements ISheetHandler {
    private ClassInfoRepository classInfoRepository;
    private ClassCapacityRepository classCapacityRepository;
    private GradeInfoRepository gradeInfoRepository;
    private SubjectRepository subjectRepository;
    private RepositoryService repositoryService;

    @Autowired
    public ClassDefinitionHandler(ClassInfoRepository classInfoRepository, ClassCapacityRepository classCapacityRepository, GradeInfoRepository gradeInfoRepository, SubjectRepository subjectRepository, RepositoryService repositoryService) {
        this.classInfoRepository = classInfoRepository;
        this.classCapacityRepository = classCapacityRepository;
        this.gradeInfoRepository = gradeInfoRepository;
        this.subjectRepository = subjectRepository;
        this.repositoryService = repositoryService;
    }

    @Override
    public void processSheet(XSSFSheet sheet) throws Exception {
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        Map<Integer, String> indexHeaderMap = new HashMap<>();
        for(int index = 0; index < firstRow.getLastCellNum();index++){
            indexHeaderMap.put(index, firstRow.getCell(index).getStringCellValue());
        }
        for(int i=sheet.getFirstRowNum()+1;i<=sheet.getLastRowNum();i++){
            Row row=sheet.getRow(i);
            int firstCellNum = row.getFirstCellNum();
            ClassInfo classInfo = new ClassInfo();
            switch (firstCellNum){
                case 0: classInfo.setCourseName(row.getCell(0).getStringCellValue());
                case 1: classInfo.setMajorSubject(row.getCell(1).getStringCellValue());
                case 2: classInfo.setSection(row.getCell(2).getStringCellValue());
                case 3: classInfo.setYear((int) row.getCell(3).getNumericCellValue());
                case 4: classInfo.setGradeLevel(gradeInfoRepository.findById(row.getCell(4).getStringCellValue()).map(grade -> grade.getGradeLevel()).orElse(null));

            }
            classInfoRepository.save(classInfo);
            processClassCapacity(row, classInfo, indexHeaderMap);

        }
    }

    private void processClassCapacity(Row row, ClassInfo classInfo, Map<Integer, String> indexHeaderMap){
        List<ClassCapacity> classCapacityList = new ArrayList<>();
        for(int j=5; j<=row.getLastCellNum();j++){
            int cellValue = row.getCell(j)!=null ? (int) row.getCell(j).getNumericCellValue():0;
            Integer classId = classInfo.getClassId();
            if(cellValue>0){
                ClassCapacity classCapacity = new ClassCapacity();

                String[] headerValue = indexHeaderMap.get(j).split("_");
                boolean isPractical = headerValue.length>1 && headerValue[1].equalsIgnoreCase("PRACTICAL");
                classCapacity.setCompositeKey(new ClassCapacityCompositeKey(classId, repositoryService.saveAndGetIfNotFound(headerValue[0].toUpperCase().trim()).getSubjectId(), isPractical));
                classCapacity.setCapacity(cellValue);
                classCapacityList.add(classCapacity);
            }
        }
        if(classCapacityList.size()>0){
            classCapacityRepository.saveAll(classCapacityList);
        }
    }
}
